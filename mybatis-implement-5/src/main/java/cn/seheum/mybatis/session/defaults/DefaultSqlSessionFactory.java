package cn.seheum.mybatis.session.defaults;

import cn.seheum.mybatis.session.Configuration;
import cn.seheum.mybatis.session.SqlSession;
import cn.seheum.mybatis.session.SqlSessionFactory;

/**
 * @author seheum
 * @date 2023/4/20
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
