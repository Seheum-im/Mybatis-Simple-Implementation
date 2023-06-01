package cn.seheum.mybatis.executor;

import cn.seheum.mybatis.executor.statement.StatementHandler;
import cn.seheum.mybatis.mapping.BoundSql;
import cn.seheum.mybatis.mapping.MappedStatement;
import cn.seheum.mybatis.session.Configuration;
import cn.seheum.mybatis.session.ResultHandler;
import cn.seheum.mybatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author seheum
 * @date 2023/5/31
 */
public class SimpleExecutor extends BaseExecutor {
    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {

        try {
            Configuration configuration = ms.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler(this,ms,parameter,resultHandler,boundSql);
            Connection connection = transaction.getConnection();
            Statement statement = handler.prepare(connection);
            handler.parameterize(statement);
            return handler.query(statement,resultHandler);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
