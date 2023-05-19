package cn.seheum.mybatis.transaction;

import cn.seheum.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author seheum
 * @date 2023/5/11
 */
public interface TransactionFactory {

    /**
     * 根据连接建立 Transaction
     * @param connection
     * @return
     */
    Transaction newTransaction(Connection connection);

    /**
     * 根据数据源和事务隔离级别建立  Transaction
     * @param dataSource
     * @param level
     * @param autoCommit
     * @return
     */
    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level,boolean autoCommit);
}
