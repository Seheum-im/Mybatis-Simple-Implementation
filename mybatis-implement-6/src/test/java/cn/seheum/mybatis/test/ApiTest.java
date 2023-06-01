package cn.seheum.mybatis.test;

import cn.seheum.mybatis.datasource.pooled.PooledDataSource;
import cn.seheum.mybatis.io.Resources;
import cn.seheum.mybatis.session.SqlSession;
import cn.seheum.mybatis.session.SqlSessionFactory;
import cn.seheum.mybatis.session.SqlSessionFactoryBuilder;
import cn.seheum.mybatis.test.dao.IUserDao;
import cn.seheum.mybatis.test.po.User;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author seheum
 * @date 2023/5/29
 */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_SqlSessionFactory() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        User user = userDao.queryUserInfoById(2L);
        logger.info("测试结果：{}", JSON.toJSONString(user));
    }



}
