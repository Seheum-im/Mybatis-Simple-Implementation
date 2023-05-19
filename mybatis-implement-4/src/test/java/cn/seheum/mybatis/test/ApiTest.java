package cn.seheum.mybatis.test;

import cn.seheum.mybatis.builder.xml.XMLConfigBuilder;
import cn.seheum.mybatis.io.Resources;
import cn.seheum.mybatis.session.Configuration;
import cn.seheum.mybatis.session.SqlSession;
import cn.seheum.mybatis.session.SqlSessionFactory;
import cn.seheum.mybatis.session.SqlSessionFactoryBuilder;
import cn.seheum.mybatis.session.defaults.DefaultSqlSession;
import cn.seheum.mybatis.test.dao.IUserDao;
import cn.seheum.mybatis.test.po.User;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;

/**
 * @author seheum
 * @date 2023/5/16
 */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_SqlSessionFactory() throws IOException {
        //1.从SqlSessionFactory获取SqlSession
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //2.获取mapper对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        //3.test
        User user = userDao.queryUserInfoById(1L);
        logger.info("最终的测试结果:{}",JSON.toJSONString(user));
    }

    //测试新增的内容
    @Test
    public void test_SelectOne() throws IOException {
        //解析XML
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        Configuration configuration = xmlConfigBuilder.parse();

        //获取DefaultSqlSession
        SqlSession defaultSqlSession = new DefaultSqlSession(configuration);

        //查询语句执行
        Object[] req = {2L};
        Object res = defaultSqlSession.selectOne("cn.seheum.mybatis.test.dao.IUserDao.queryUserInfoById", req);
        logger.info("执行查询语句的结果：{}",JSON.toJSONString(res));
    }
}
