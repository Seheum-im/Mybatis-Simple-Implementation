package cn.seheum.mybatis.test.dao;

import cn.seheum.mybatis.test.po.User;

/**
 * @author seheum
 * @date 2023/5/29
 */
public interface IUserDao {

    User queryUserInfoById(Long uId);
}
