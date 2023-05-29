package cn.seheum.mybatis.datasource.pooled;

import cn.seheum.mybatis.datasource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;

/**
 * @author seheum
 * @date 2023/5/23
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {
    //TODO 继续进行代码实现


    @Override
    public DataSource getDataSource() {
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDriver(properties.getProperty("driver"));
        pooledDataSource.setUrl(properties.getProperty("url"));
        pooledDataSource.setUsername(properties.getProperty("username"));
        pooledDataSource.setPassword(properties.getProperty("password"));
        return pooledDataSource;
    }
}
