package cn.seheum.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author seheum
 * @date 2023/5/16
 */
public interface DataSourceFactory {

    void setProperties(Properties properties);

    DataSource getDataSource();
}
