package cn.seheum.mybatis.dao;

/**
 * @author seheum
 * @date 2023/4/19
 */
public interface IUserDao {
    String queryUserName(String uId);

    Integer queryUserAge(String uId);
}
