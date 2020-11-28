package com.jaygoaler.pitools.service.impl;

import com.jaygoaler.pitools.entity.User;
import com.jaygoaler.pitools.mapper.UserMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.Md5Crypt;
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
        User user = new User(userName,Md5Crypt.md5Crypt(password.getBytes()));

        System.out.println(Md5Crypt.md5Crypt(password.getBytes()).length());
        int i = userMapper.insert(user);
        System.out.println(i);
    }
}