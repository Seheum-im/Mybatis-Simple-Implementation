package cn.seheum.mybatis.datasource.pooled;

import cn.seheum.mybatis.datasource.unpooled.UnpooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author seheum
 * @date 2023/5/23
 */
public class PooledDataSource implements DataSource {

    private Logger logger = LoggerFactory.getLogger(PooledDataSource.class);

    //池状态
    private final PoolState state = new PoolState(this);

    private final UnpooledDataSource dataSource;
    //活跃链接数
    protected int poolMaximumActiveConnections = 10;

    //空闲链接数
    protected int poolMaximumIdleConnections = 5;

    //在被强制返回之前，池中链接被检查的时间
    protected int poolMaximumCheckoutTime = 20000;

    //给连接池一个打印日志状态机会的低层次设置,还有重新尝试获得连接, 这些情况下往往需要很长时间 为了避免连接池没有配置时静默失败)。
    protected int poolTimeToWait = 20000;

    //发送到数据的侦测查询，用来验证链接是否正常工作，并且准备接受请求。默认是“NO PING QUERY SET”，这会引起很多数据库驱动连接由一个错误而导致失败
    protected String poolPingQuery = "NO PING QUERY SET";

    //开启或者禁用侦测查询
    protected boolean poolPingEnabled = false;

    // 用来配置 poolPingQuery 多久时间被用一次
    protected int poolPingConnectionsNotUsedFor = 0;

    private int expectedConnectionTypeCode;

    public PooledDataSource() {
        this.dataSource = new UnpooledDataSource();
    }

    //回收链接
    protected void pushConnection(PooledConnection connection) throws SQLException {
        synchronized (state) {
            state.activeConnections.remove(connection);
            //判断链接是否有效
            if(connection.isValid()) {
                //如果空闲链接小于设定数量时，也就是空闲链接太少时
                if(state.idleConnections.size() < poolMaximumIdleConnections && connection.getConnectionTypeCode() == expectedConnectionTypeCode) {
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    //他先检查数据库连接是否处于自动提交模式，如果不是，则调用rollback()方法执行回滚操作
                    //在Mybatis中，如果没有开启自动提交模式，则需要手动提交或者回滚事务；因此这段代码可能是在确保操作完成后，如果没有开启自动提交模式，则执行回滚操作
                    //总的来说，这段代码用于保证数据库的一致性，确保操作完成后，如果未开启自动提交模式，则执行回滚操作
                    if(!connection.getRealConnection().getAutoCommit()) {
                            connection.getRealConnection().rollback();
                    }

                    //实例化一个新的DB连接，加入到idle列表中
                    PooledConnection newConnection = new PooledConnection(connection.getRealConnection(),this);
                    state.idleConnections.add(newConnection);
                    newConnection.setCreatedTimestamp(connection.getCreatedTimestamp());
                    newConnection.setLastUsedTimestamp(connection.getLastUsedTimestamp());
                    connection.invalidate();

                    logger.info("Returned connection " + newConnection.getRealHashCode() + " to pool.");

                    // 通知其他线程可以来抢夺DB连接了
                    state.notifyAll();
                }else {//否则，其他空闲链接还很充足
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    if(!connection.getRealConnection().getAutoCommit()) {
                        connection.getRealConnection().rollback();
                    }
                    //将connection关闭
                    connection.getRealConnection().close();
                    logger.info("Closed connection " + connection.getRealHashCode() + ".");
                    connection.invalidate();
                }
            }else {
                logger.info("A bad connection (" + connection.getRealHashCode() + ") attempted to return to the pool, discarding connection.");
                state.badConnectionCount++;
            }
        }
    }


    //获取链接
    private PooledConnection popConnection(String username,String password) throws SQLException {
        boolean countedWait = false;
        PooledConnection conn = null;
        long t = System.currentTimeMillis();
        int localBadConnectionCount = 0;


        while(conn == null) {
            synchronized (state) {
                //如果有空链接，返回队列中的第一个
                if(!state.idleConnections.isEmpty()) {
                    conn = state.idleConnections.remove(0);
                    logger.info("Checked out connection " + conn.getRealHashCode() + " from pool.");
                }// 如果无空闲链接：创建新的链接
                else {
                    //活跃链接数没有达到池内最大设置的链接数
                    if(state.activeConnections.size() < poolMaximumActiveConnections) {
                        conn = new PooledConnection(dataSource.getConnection(),this);
                        logger.info("Created connection " + conn.getRealHashCode() + ".");
                    } else {//活跃链接已经达到/超过 池内的最大设置链接数
                        //获取活跃链接队列中的第一个链接，也就是存活时间最长的链接
                        PooledConnection oldestActiveConnection = state.activeConnections.get(0);
                        long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
                        //如果checkout时间足够长，则这个链接标记给过期
                        if(longestCheckoutTime > poolMaximumCheckoutTime) {
                            state.claimedOverdueConnectionCount++;
                            state.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
                            state.accumulatedCheckoutTime += longestCheckoutTime;
                            state.activeConnections.remove(oldestActiveConnection);

                            if(!oldestActiveConnection.getRealConnection().getAutoCommit()) {
                                oldestActiveConnection.getRealConnection().rollback();
                            }
                            //TODO 还有继续实现的部分
                        }
                    }
                }

            }
        }
    }

}
