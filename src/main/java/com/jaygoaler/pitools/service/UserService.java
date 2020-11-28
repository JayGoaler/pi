package com.jaygoaler.pitools.service;

/**
 * @author jaygoaler
 */
public interface UserService {

    /**
     * 创建用户
     * @param userName
     * @param password
     * @return
     */
    boolean createUser(String userName,String password);
}
