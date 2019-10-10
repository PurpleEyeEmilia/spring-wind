package com.github.springwind.modules.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/10/9 15:06
 * @Desc
 */
@Data
@Document(indexName = "wind-user-info", type = "userInfo")
public class UserEsInfo implements Serializable {
    /**
     * 用户id
     */
    @Id
    private Long userId;

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    /**
     * 用户账户信息
     */
    private UserAccount userAccount;

}
