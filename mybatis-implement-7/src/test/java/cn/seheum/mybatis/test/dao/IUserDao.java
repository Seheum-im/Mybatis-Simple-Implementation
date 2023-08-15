package cn.seheum.mybatis.test.dao;

import cn.seheum.mybatis.test.po.User;

public interface IUserDao {

    User queryUserInfoById(Long uId);

}
