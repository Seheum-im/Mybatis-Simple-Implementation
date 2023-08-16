package cn.seheum.mybatis.datasource.pooled;

import cn.seheum.mybatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * @author seheum
 * @date 2023/5/23
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {

    public PooledDataSourceFactory() {
        this.dataSource = new PooledDataSource();
    }
}
