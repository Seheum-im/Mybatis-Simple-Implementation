package cn.seheum.mybatis.transaction.jdbc;

import cn.seheum.mybatis.session.TransactionIsolationLevel;
import cn.seheum.mybatis.transaction.Transaction;
import cn.seheum.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author seheum
 * @date 2023/5/11
 */
public class JdbcTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(Connection connection) {
        return new JdbcTransaction(connection);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }
}
