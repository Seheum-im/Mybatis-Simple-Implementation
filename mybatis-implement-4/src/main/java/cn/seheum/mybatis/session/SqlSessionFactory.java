package cn.seheum.mybatis.session;

/**
 * @author seheum
 * @date 2023/4/20
 */
public interface SqlSessionFactory {

    SqlSession openSession();
}
