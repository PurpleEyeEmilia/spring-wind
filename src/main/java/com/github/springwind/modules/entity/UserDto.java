package com.github.springwind.modules.entity;

import com.github.springwind.common.utils.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/10/11 10:05
 * @Desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDto extends Page {

    private Long userId;

    private String name;

    private String sign;

    private Integer age;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

}
