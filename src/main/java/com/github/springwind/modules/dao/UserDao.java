package com.github.springwind.modules.dao;

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
     *
     *
     * @param id
     * @return
     */
    String getUser(String id);
}
