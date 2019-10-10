package com.github.springwind.modules.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/10/9 15:36
 * @Desc
 */
@Data
public class UserAccount {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    @Field(type = FieldType.String)
    private String username;

    /**
     * 昵称
     */
    @Field(type = FieldType.String)
    private String nickName;
}
