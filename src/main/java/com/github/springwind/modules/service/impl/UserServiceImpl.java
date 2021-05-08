package com.github.springwind.modules.service.impl;

import com.github.springwind.modules.dao.UserDao;
import com.github.springwind.modules.entity.User;
import com.github.springwind.modules.service.UserService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/8/21 14:50
 * @Desc
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public String getUser(String id) {

        return userDao.getUser(id);
    }

    @Override
    public String addUser(User user) {
        return null;
    }
}
