package cn.seheum.mybatis;

import cn.seheum.mybatis.Dao.ISchoolDao;
import cn.seheum.mybatis.Dao.IUserDao;
import cn.seheum.mybatis.binding.MapperRegistry;
import cn.seheum.mybatis.session.SqlSession;
import cn.seheum.mybatis.session.SqlSessionFactory;
import cn.seheum.mybatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author seheum
 * @date 2023/4/20
 */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_MapperProxyFactory() {

        //1、注册MapperRegistry
        MapperRegistry mapperRegistry = new MapperRegistry();
        //从包内读取
        mapperRegistry.addMappers("cn.seheum.mybatis.Dao");
        //2、SqlSession工厂中获取Session
        SqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(mapperRegistry);
        SqlSession sqlSession = defaultSqlSessionFactory.openSession();
        //3、获取mapper代理
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        ISchoolDao schoolDao = sqlSession.getMapper(ISchoolDao.class);
        logger.info("就读于国内最高学府{}",schoolDao.querySchoolName("北京大学"));

        //4、测试
        String s = userDao.queryUserName("233333");
        logger.info("得到的最终结果：{}",s);
    }
}
