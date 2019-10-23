package com.github.springwind.modules.service;

import com.github.springwind.common.utils.Page;
import com.github.springwind.modules.entity.UserDto;
import com.github.springwind.modules.entity.UserEsInfo;
import com.github.springwind.modules.entity.UserInfo;

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
     * @param userId
     * @return
     */
    UserEsInfo getUser(Long userId);

    /**
     * 新增用户
     *
     * @param userInfo
     * @return
     */
    String addUser(UserInfo userInfo);

    /**
     * 查询分页
     *
     * @param userDto
     * @return
     */
    Page<UserEsInfo> getPageInfo(UserDto userDto);
}
