package cn.seheum.mybatis.executor;

import cn.seheum.mybatis.mapping.BoundSql;
import cn.seheum.mybatis.mapping.MappedStatement;
import cn.seheum.mybatis.session.ResultHandler;
import cn.seheum.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * @author seheum
 * @date 2023/5/31
 */
public interface Executor {
    ResultHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql);

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);
}
