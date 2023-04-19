package cn.seheum.mybatis;

import cn.seheum.mybatis.binding.MapperProxyFactory;
import cn.seheum.mybatis.dao.IUserDao;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author seheum
 * @date 2023/4/19
 */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_MapperProxyFactory() {
        MapperProxyFactory<IUserDao> mapperProxyFactory = new MapperProxyFactory<>(IUserDao.class);
        Map<String,String> sqlSession = new HashMap<>();

        sqlSession.put("cn.seheum.mybatis.dao.IUserDao.queryUserName", "模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户姓名");
        sqlSession.put("cn.seheum.mybatis.dao.IUserDao.queryUserAge", "模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户年龄");


        IUserDao userDao = mapperProxyFactory.newInstance(sqlSession);
        String result = userDao.queryUserName("23333");
        logger.info("打印出来的测试结果{}",result);
    }
}
