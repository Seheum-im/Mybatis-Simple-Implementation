package cn.seheum.mybatis.Dao;

/**
 * @author seheum
 * @date 2023/4/20
 */
public interface IUserDao {
    String queryUserName(String uId);

    Integer queryUserAge(String uId);
}
