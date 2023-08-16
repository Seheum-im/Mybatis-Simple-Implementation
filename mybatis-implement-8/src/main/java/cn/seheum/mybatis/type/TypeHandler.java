package cn.seheum.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author seheum
 * @date 2023/8/16
 **/
public interface TypeHandler<T> {

    /**
     * 设置参数
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;
}
