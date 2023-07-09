package cn.seheum.mybatis.session.defaults;

import cn.seheum.mybatis.executor.Executor;
import cn.seheum.mybatis.mapping.Environment;
import cn.seheum.mybatis.session.Configuration;
import cn.seheum.mybatis.session.SqlSession;
import cn.seheum.mybatis.session.SqlSessionFactory;
import cn.seheum.mybatis.session.TransactionIsolationLevel;
import cn.seheum.mybatis.transaction.Transaction;
import cn.seheum.mybatis.transaction.TransactionFactory;

import java.sql.SQLException;

/**
 * @author seheum
 * @date 2023/4/20
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        Transaction tx = null;
        try {
            final Environment environment = configuration.getEnvironment();
            TransactionFactory transactionFactory = environment.getTransactionFactory();
            tx = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolationLevel.READ_COMMITTED,false);

            //创建执行器
            final Executor executor = configuration.newExecutor(tx);

            //创建DefaultSqlSession
            return new DefaultSqlSession(configuration,executor);
        }catch (Exception e) {
            try {
                assert tx != null;
                tx.close();
            } catch (SQLException ignore) {
            }
            throw new RuntimeException("Error opening session.  Cause: " + e);
        }
    }
}
