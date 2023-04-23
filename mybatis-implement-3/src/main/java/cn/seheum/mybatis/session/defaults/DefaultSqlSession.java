package cn.seheum.mybatis.session.defaults;

import cn.seheum.mybatis.binding.MapperRegistry;
import cn.seheum.mybatis.mapping.MappedStatement;
import cn.seheum.mybatis.session.Configuration;
import cn.seheum.mybatis.session.SqlSession;

/**
 * @author seheum
 * @date 2023/4/20
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        return (T) ("你被代理了！" + "\n方法：" + statement + "\n入参：" + parameter + "\n待执行SQL：" + mappedStatement.getSql());
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
