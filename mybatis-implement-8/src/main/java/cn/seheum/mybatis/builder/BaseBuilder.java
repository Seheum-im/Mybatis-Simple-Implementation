package cn.seheum.mybatis.builder;

import cn.seheum.mybatis.session.Configuration;
import cn.seheum.mybatis.type.TypeAliasRegistry;
import cn.seheum.mybatis.type.TypeHandlerRegistry;

/**
 * @author seheum
 * @date 2023/4/20
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;

    protected final TypeHandlerRegistry typeHandlerRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    protected Class<?> resolveAlias(String alias) {
        return typeAliasRegistry.resolveAlias(alias);
    }
}
