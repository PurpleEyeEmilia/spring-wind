package com.github.springwind.modules.dao;

import com.github.springwind.modules.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/8/21 14:51
 * @Desc
 */
@Mapper
public interface UserDao {
    /**
     * 查询用户信息
     *
     * @param id
     * @return
     */
    String getUser(String id);

    /**
     * 添加用户
     *
     * @param userInfo
     * @return
     */
    String addUser(UserInfo userInfo);
}
