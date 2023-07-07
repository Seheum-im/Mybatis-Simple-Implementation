package cn.seheum.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author seheum
 * @date 2023/5/11
 */
public interface Transaction {
    Connection getConnection() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;
}
