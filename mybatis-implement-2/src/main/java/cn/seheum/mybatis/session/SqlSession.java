package cn.seheum.mybatis.session;

/**
 * @author seheum
 * @date 2023/4/19
 */
public interface SqlSession {

    /**
     *
     * @param statement
     * @param <T>
     * @return
     */
    <T> T selectOne(String statement);


    /**
     *
     * @param statement
     * @param parameter
     * @param <T>
     * @return
     */
    <T> T selectOne(String statement,Object parameter);

    /**
     *
     * @param type
     * @param <T>
     * @return
     */
    <T> T getMapper(Class<T> type);
}
