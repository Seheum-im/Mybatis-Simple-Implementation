package cn.seheum.mybatis.datasource.unpooled;

import cn.seheum.mybatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author seheum
 * @date 2023/5/23
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {

    protected Properties properties;

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public DataSource getDataSource() {
        UnpooledDataSource unpooledDataSource = new UnpooledDataSource();
        unpooledDataSource.setDriver(properties.getProperty("driver"));
        unpooledDataSource.setUrl(properties.getProperty("url"));
        unpooledDataSource.setUsername(properties.getProperty("username"));
        unpooledDataSource.setPassword(properties.getProperty("password"));
        return unpooledDataSource;
    }
}
