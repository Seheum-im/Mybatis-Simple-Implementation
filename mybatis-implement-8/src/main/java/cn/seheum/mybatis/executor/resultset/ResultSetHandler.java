package cn.seheum.mybatis.executor.resultset;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author seheum
 * @date 2023/5/31
 */
public interface ResultSetHandler {

    <E> List<E> handleResultSets(Statement statement) throws SQLException;
}
