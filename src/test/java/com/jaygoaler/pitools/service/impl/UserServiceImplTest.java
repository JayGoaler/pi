package com.jaygoaler.pitools.service.impl;

import com.jaygoaler.pitools.entity.User;
import com.jaygoaler.pitools.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceImplTest {

    @Resource
    private UserMapper userMapper;

    @Test
    void createUser() {
        String userName = "jaygoaler";
        String password = "yangjie520";
        User user = new User(userName,password);

        int i = userMapper.insert(user);
        System.out.println(i);
    }
}