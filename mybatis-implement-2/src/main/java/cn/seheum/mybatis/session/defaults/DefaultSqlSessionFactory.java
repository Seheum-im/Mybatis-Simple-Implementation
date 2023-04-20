package cn.seheum.mybatis.session.defaults;

import cn.seheum.mybatis.binding.MapperRegistry;
import cn.seheum.mybatis.session.SqlSession;
import cn.seheum.mybatis.session.SqlSessionFactory;

/**
 * @author seheum
 * @date 2023/4/20
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private MapperRegistry mapperRegistry;

    public DefaultSqlSessionFactory(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(mapperRegistry);
    }
}
