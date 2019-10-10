package com.github.springwind.modules.controller;

import com.github.springwind.modules.entity.UserInfo;
import com.github.springwind.modules.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String getUser(String id) {
        stringRedisTemplate.opsForValue().set("id", id);
        return userService.getUser(id);
    }

    @PostMapping("/add")
    public String addUser(UserInfo userInfo) {
//        stringRedisTemplate.opsForValue().set("xx", "1");
        return userService.addUser(userInfo);
    }

}
