package cn.seheum.mybatis.builder;

import cn.seheum.mybatis.session.Configuration;

/**
 * @author seheum
 * @date 2023/4/20
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
