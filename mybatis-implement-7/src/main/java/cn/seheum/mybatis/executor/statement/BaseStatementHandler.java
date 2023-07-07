package cn.seheum.mybatis.executor.statement;

import cn.seheum.mybatis.executor.Executor;
import cn.seheum.mybatis.executor.resultset.ResultSetHandler;
import cn.seheum.mybatis.mapping.BoundSql;
import cn.seheum.mybatis.mapping.MappedStatement;
import cn.seheum.mybatis.session.Configuration;
import cn.seheum.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author seheum
 * @date 2023/5/31
 */
public abstract class BaseStatementHandler implements StatementHandler{
    protected final Configuration configuration;

    protected final Executor executor;

    protected final MappedStatement mappedStatement;

    protected final Object parameterObject;

    //返回结果集处理器
    protected final ResultSetHandler resultSetHandler;

    protected BoundSql boundSql;

    public BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;

        //参数对象
        this.parameterObject = parameterObject;
        //结果集
        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, boundSql);

        this.boundSql = boundSql;
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            // 实例化 Statement
            statement = instantiateStatement(connection);
            // 参数设置，可以被抽取，提供配置
            statement.setQueryTimeout(350);
            statement.setFetchSize(10000);
            return statement;
        } catch (Exception e) {
            throw new RuntimeException("Error preparing statement.  Cause: " + e, e);
        }

    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;
}
