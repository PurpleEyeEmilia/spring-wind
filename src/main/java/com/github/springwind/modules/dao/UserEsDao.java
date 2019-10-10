package com.github.springwind.modules.dao;

import com.github.springwind.modules.entity.UserEsInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/10/9 15:38
 * @Desc
 */
public interface UserEsDao extends ElasticsearchRepository<UserEsInfo, Long> {

}
