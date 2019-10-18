package com.github.springwind.modules.entity;

import com.github.springwind.common.utils.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

}
