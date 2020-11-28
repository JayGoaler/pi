package com.jaygoaler.pitools.service.impl;

import com.jaygoaler.pitools.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author jaygoaler
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Override
    public boolean createUser(String userName, String password) {
        return false;
    }
}
