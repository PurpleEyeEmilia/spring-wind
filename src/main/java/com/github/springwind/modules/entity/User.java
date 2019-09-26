package com.github.springwind.modules.entity;

import lombok.Data;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/8/21 15:14
 * @Desc
 */
@Data
public class User {
    /**
     * id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

}
