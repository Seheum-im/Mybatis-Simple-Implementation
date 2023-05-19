package cn.seheum.mybatis.session;

import cn.seheum.mybatis.builder.xml.XMLConfigBuilder;
import cn.seheum.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

/**
 * @author seheum
 * @date 2023/4/20
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }


    public SqlSessionFactory build(Configuration configuration) {
        return new DefaultSqlSessionFactory(configuration);
    }
}
