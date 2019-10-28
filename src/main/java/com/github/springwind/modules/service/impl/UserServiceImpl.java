package com.github.springwind.modules.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.springwind.common.constants.CommonConstants;
import com.github.springwind.common.utils.CanalEntityParser;
import com.github.springwind.common.utils.Page;
import com.github.springwind.core.canal.entity.CanalMsg;
import com.github.springwind.core.generate.SnowflakeGenerator;
import com.github.springwind.core.sync.SyncExecutor;
import com.github.springwind.core.sync.SyncHandler;
import com.github.springwind.modules.dao.UserDao;
import com.github.springwind.modules.dao.UserEsDao;
import com.github.springwind.modules.entity.UserAccount;
import com.github.springwind.modules.entity.UserDto;
import com.github.springwind.modules.entity.UserEsInfo;
import com.github.springwind.modules.entity.UserInfo;
import com.github.springwind.modules.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/8/21 14:50
 * @Desc
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService, SyncHandler {

    @Resource
    private UserDao userDao;

    @Resource
    private UserEsDao userEsDao;

    @Resource
    private SyncExecutor syncExecutor;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private SnowflakeGenerator snowflakeGenerator;

    @PostConstruct
    private void register() {
        syncExecutor.register(this);
    }

    @Override
    public Boolean watch(CanalMsg canalMsg) {
        if (canalMsg == null) {
            return false;
        }
        if (CommonConstants.User.USER_DB_NAME.equals(canalMsg.getDbName())) {
            return CommonConstants.User.USER_INFO_TABLE_NAME.equals(canalMsg.getTableName()) || CommonConstants.User.USER_ACCOUNT_TABLE_NAME.equals(canalMsg.getTableName());
        }
        return false;
    }

    @Override
    public void handle(CanalMsg canalMsg) {
        if (CommonConstants.User.USER_DB_NAME.equals(canalMsg.getDbName())) {
            if (CommonConstants.User.USER_INFO_TABLE_NAME.equals(canalMsg.getTableName())) {
                log.info("开始处理user_info数据");
                doUserInfo(canalMsg);
            }
        } else if (CommonConstants.User.USER_ACCOUNT_DB_NAME.equals(canalMsg.getDbName())) {
            if (CommonConstants.User.USER_ACCOUNT_TABLE_NAME.equals(canalMsg.getTableName())) {
                log.info("开始处理user_account数据");
                doUserAccount(canalMsg);
            }
        }
    }

    private void doUserAccount(CanalMsg canalMsg) {
        UserAccount userAccountBeforeData = CanalEntityParser.parse(canalMsg.getBeforeData(), UserAccount.class);
        UserAccount userAccountAfterData = CanalEntityParser.parse(canalMsg.getAfterData(), UserAccount.class);

        if (userAccountBeforeData == null && userAccountAfterData == null) {
            return;
        }

        if (CommonConstants.EventType.OPT_UPDATE.equalsIgnoreCase(canalMsg.getEventType())) {
            UserEsInfo userEsInfo = null;
            if (userAccountBeforeData != null) {
                userEsInfo = userEsDao.findOne(userAccountBeforeData.getUserId());
            }
            if (userEsInfo != null) {
                UserAccount userAccount = userEsInfo.getUserAccount();
                UserAccount afterData = CanalEntityParser.parse(userAccount, canalMsg.getAfterData(), UserAccount.class);
                userEsInfo.setUserAccount(afterData);
                log.info("更新user_info数据, 数据：{}", JSON.toJSONString(userEsInfo));
                userEsDao.save(userEsInfo);
            } else if (userAccountAfterData != null && userAccountAfterData.getUserId() != null) {
                userEsInfo = new UserEsInfo();
                userEsInfo.setUserAccount(userAccountAfterData);
                userEsInfo.setUserId(userAccountAfterData.getUserId());
                log.info("插入user_info数据，数据：{}", JSON.toJSONString(userEsInfo));
                userEsDao.save(userEsInfo);
            }

        } else if (CommonConstants.EventType.OPT_DELETE.equalsIgnoreCase(canalMsg.getEventType())) {
            if (userAccountAfterData != null) {
                log.info("插入user_info数据，数据：{}", JSON.toJSONString(userAccountBeforeData));
                userEsDao.delete(userAccountAfterData.getUserId());
            }

        } else if (CommonConstants.EventType.OPT_INSERT.equalsIgnoreCase(canalMsg.getEventType())) {
            if (userAccountAfterData != null) {
                UserEsInfo userEsInfo = userEsDao.findOne(userAccountAfterData.getUserId());
                if (userEsInfo == null) {
                    userEsInfo = new UserEsInfo();
                    userEsInfo.setUserAccount(userAccountAfterData);
                    userEsInfo.setUserId(userAccountAfterData.getUserId());
                    log.info("插入user_info数据，数据：{}", JSON.toJSONString(userEsInfo));
                    userEsDao.save(userEsInfo);
                } else {
                    userEsInfo.setUserAccount(userAccountAfterData);
                    userEsInfo.setUserId(userAccountAfterData.getUserId());
                    log.info("插入user_info数据，数据：{}", JSON.toJSONString(userEsInfo));
                    userEsDao.save(userEsInfo);
                }
            }
        }
    }

    private void doUserInfo(CanalMsg canalMsg) {
        UserInfo userInfoBeforeData = CanalEntityParser.parse(canalMsg.getBeforeData(), UserInfo.class);
        UserInfo userInfoAfterData = CanalEntityParser.parse(canalMsg.getAfterData(), UserInfo.class);

        if (userInfoBeforeData == null && userInfoAfterData == null) {
            return;
        }

        if (CommonConstants.EventType.OPT_UPDATE.equalsIgnoreCase(canalMsg.getEventType())) {
            UserEsInfo userEsInfo = null;
            if (userInfoBeforeData != null && userInfoBeforeData.getUserId() != null) {
                userEsInfo = userEsDao.findOne(userInfoBeforeData.getUserId());
            }
            if (userEsInfo != null) {
                UserInfo userInfo = userEsInfo.getUserInfo();
                UserInfo afterData = CanalEntityParser.parse(userInfo, canalMsg.getAfterData(), UserInfo.class);
                userEsInfo.setUserInfo(afterData);
                userEsDao.save(userEsInfo);
                log.info("更新user_info数据, 数据：{}", JSON.toJSONString(userEsInfo));
            } else if (userInfoAfterData != null && userInfoAfterData.getUserId() != null) {
                userEsInfo = new UserEsInfo();
                userEsInfo.setUserInfo(userInfoAfterData);
                userEsInfo.setUserId(userInfoAfterData.getUserId());
                userEsDao.save(userEsInfo);
                log.info("插入user_info数据，数据：{}", JSON.toJSONString(userEsInfo));
            }

        } else if (CommonConstants.EventType.OPT_DELETE.equalsIgnoreCase(canalMsg.getEventType())) {
            if (userInfoBeforeData != null) {
                userEsDao.delete(userInfoBeforeData.getUserId());
                log.info("插入user_info数据，数据：{}", JSON.toJSONString(userInfoBeforeData));
            }

        } else if (CommonConstants.EventType.OPT_INSERT.equalsIgnoreCase(canalMsg.getEventType())) {
            if (userInfoAfterData != null && userInfoAfterData.getUserId() != null) {
                UserEsInfo userEsInfo = userEsDao.findOne(userInfoAfterData.getUserId());
                if (userEsInfo == null) {
                    userEsInfo = new UserEsInfo();
                    userEsInfo.setUserInfo(userInfoAfterData);
                    userEsInfo.setUserId(userInfoAfterData.getUserId());
                    userEsDao.save(userEsInfo);
                    log.info("插入user_info数据，数据：{}", JSON.toJSONString(userEsInfo));
                } else {
                    userEsInfo.setUserInfo(userInfoAfterData);
                    userEsInfo.setUserId(userInfoAfterData.getUserId());
                    userEsDao.save(userEsInfo);
                    log.info("插入user_info数据，数据：{}", JSON.toJSONString(userEsInfo));
                }
            }
        }
    }

    @Override
    public UserEsInfo getUser(Long userId) {
        return userEsDao.findOne(userId);
    }

    @Override
    public String addUser(UserInfo userInfo) {
        Long userId = snowflakeGenerator.getNextId();
        userInfo.setUserId(userId);
        return userDao.addUser(userInfo);
    }

    @Override
    public Page<UserEsInfo> getPageInfo(UserDto userDto) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        if (StringUtils.isNotBlank(userDto.getName())) {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("userInfo.name", userDto.getName());
            nativeSearchQueryBuilder.withFilter(QueryBuilders.boolQuery().should(matchQueryBuilder));
        }

        if (userDto.getAge() != null) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("userInfo.age", userDto.getAge());
            nativeSearchQueryBuilder.withFilter(QueryBuilders.boolQuery().must(termQueryBuilder));
        }

        if (StringUtils.isNotBlank(userDto.getSign())) {
            WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("userInfo.sign", "*" + userDto.getSign() + "*");
            nativeSearchQueryBuilder.withFilter(QueryBuilders.boolQuery().should(wildcardQueryBuilder));
        }

        PageRequest pageRequest = new PageRequest((userDto.getPageNo() - 1) * userDto.getPageSize(), userDto.getPageSize(), Sort.Direction.DESC, "userId");

        NativeSearchQuery build = nativeSearchQueryBuilder.withPageable(pageRequest).build();

        List<UserEsInfo> userEsInfos = elasticsearchTemplate.queryForList(build, UserEsInfo.class);

        return new Page<>(userDto.getPageNo(), userDto.getPageSize(), userEsInfos);
    }
}
