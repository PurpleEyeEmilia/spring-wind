package com.github.springwind.modules.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/8/21 15:14
 * @Desc
 */
@Data
public class UserInfo {
    /**
     * 雪花唯一id
     */
    private Long userId;

    /**
     * 姓名
     */
    @Field(type = FieldType.String)
    private String name;

    /**
     * 年龄
     */
    @Field(type = FieldType.Integer)
    private Integer age;

    /**
     * 个性签名
     */
    @Field(type = FieldType.String)
    private String sign;
}
