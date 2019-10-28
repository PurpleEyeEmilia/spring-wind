package com.github.springwind.modules.controller;

import com.github.springwind.common.utils.Page;
import com.github.springwind.modules.entity.UserDto;
import com.github.springwind.modules.entity.UserEsInfo;
import com.github.springwind.modules.entity.UserInfo;
import com.github.springwind.modules.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/8/21 14:47
 * @Desc
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/info")
    public UserEsInfo getUser(Long userId) {
//        stringRedisTemplate.opsForValue().set("id", String.valueOf(userId));
        return userService.getUser(userId);
    }

    @PostMapping("/add")
    public Integer addUser(UserInfo userInfo) {
//        stringRedisTemplate.opsForValue().set("xx", "1");
        return userService.addUser(userInfo);
    }

    @GetMapping("/page/info")
    @ResponseBody
    public Page<UserEsInfo> getPageInfo(UserDto userDto) {
        return userService.getPageInfo(userDto);
    }

}
