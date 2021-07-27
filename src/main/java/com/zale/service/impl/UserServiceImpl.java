package com.zale.service.impl;

import com.zale.dao.UserDao;
import com.zale.entity.User;
import com.zale.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;


    @Override
    public void register(User user) {
        User userDB = userDao.findByUserName(user.getUsername());
        if(ObjectUtils.isEmpty(userDB)) {
            user.setStatus("activated");
            user.setRegisterTime(new Date());
            userDao.save(user);
        }
        else {
            throw new RuntimeException("Username already exists");
        }
    }

    @Override
    public User login(User user) {
        User userDB = userDao.findByUserName(user.getUsername());
        if(!ObjectUtils.isEmpty(userDB)) {
            //compare password
            if(userDB.getPassword().equals(user.getPassword())) {
                return userDB;
            }
            else {
                throw new RuntimeException("Password incorrect!");
            }
        }
        else {
            throw new RuntimeException("Username incorrect");
        }

    }
}
