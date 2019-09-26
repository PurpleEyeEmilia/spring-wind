package com.github.springwind.modules.service;

import com.github.springwind.modules.entity.User;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/8/21 14:50
 * @Desc
 */
public interface UserService {
    /**
     * 查询用户
     *
     * @param id
     * @return
     */
    String getUser(String id);

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    String addUser(User user);
}
