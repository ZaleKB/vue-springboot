package com.zale.dao;

import com.zale.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    void save(User user);

    User findByUserName(String username);
}
