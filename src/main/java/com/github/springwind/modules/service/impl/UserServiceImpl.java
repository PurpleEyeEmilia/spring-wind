package com.github.springwind.modules.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.springwind.common.constants.CommonConstants;
import com.github.springwind.common.utils.CanalEntityParser;
import com.github.springwind.core.canal.entity.CanalMsg;
import com.github.springwind.core.sync.SyncExecutor;
import com.github.springwind.core.sync.SyncHandler;
import com.github.springwind.modules.dao.UserDao;
import com.github.springwind.modules.dao.UserEsDao;
import com.github.springwind.modules.entity.UserAccount;
import com.github.springwind.modules.entity.UserEsInfo;
import com.github.springwind.modules.entity.UserInfo;
import com.github.springwind.modules.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
            if (CommonConstants.User.USER_ACCOUNT_TABLE_NAME.equals(canalMsg.getTableName())) {
                log.info("开始处理user_account数据");
                doUserAccount(canalMsg);
            }
        }
    }

    private void doUserAccount(CanalMsg canalMsg) {
        UserAccount userAccountBeforeData = CanalEntityParser.parse(canalMsg.getBeforeData(), UserAccount.class);
        UserAccount userAccountAfterData = CanalEntityParser.parse(canalMsg.getAfterData(), UserAccount.class);

        if (userAccountBeforeData == null || userAccountAfterData == null) {
            return;
        }

        if (CommonConstants.EventType.OPT_UPDATE.equals(canalMsg.getEventType())) {
            UserEsInfo userEsInfo = userEsDao.findOne(userAccountBeforeData.getUserId());
            if (userEsInfo != null) {
                UserAccount userAccount = userEsInfo.getUserAccount();
                UserAccount afterData = CanalEntityParser.parse(userAccount, canalMsg.getAfterData(), UserAccount.class);
                userEsInfo.setUserAccount(afterData);
                log.info("更新user_info数据, 数据：{}", JSON.toJSONString(userEsInfo));
                userEsDao.save(userEsInfo);
            } else {
                userEsInfo = new UserEsInfo();
                userEsInfo.setUserAccount(userAccountAfterData);
                userEsInfo.setUserId(userAccountAfterData.getUserId());
                log.info("插入user_info数据，数据：{}", JSON.toJSONString(userEsInfo));
                userEsDao.save(userEsInfo);
            }

        } else if (CommonConstants.EventType.OPT_DELETE.equals(canalMsg.getEventType())) {
            log.info("插入user_info数据，数据：{}", JSON.toJSONString(userAccountBeforeData));
            userEsDao.delete(userAccountAfterData.getUserId());

        } else if (CommonConstants.EventType.OPT_INSERT.equals(canalMsg.getEventType())) {
            UserEsInfo userEsInfo = userEsDao.findOne(userAccountBeforeData.getUserId());
            if (userEsInfo == null) {
                userEsInfo = new UserEsInfo();
                userEsInfo.setUserAccount(userAccountAfterData);
                userEsInfo.setUserId(userAccountAfterData.getUserId());
                log.info("插入user_info数据，数据：{}", JSON.toJSONString(userEsInfo));
                userEsDao.save(userEsInfo);
            }
        }
    }

    private void doUserInfo(CanalMsg canalMsg) {
        UserInfo userInfoBeforeData = CanalEntityParser.parse(canalMsg.getBeforeData(), UserInfo.class);
        UserInfo userInfoAfterData = CanalEntityParser.parse(canalMsg.getAfterData(), UserInfo.class);

        if (userInfoBeforeData == null || userInfoAfterData == null) {
            return;
        }

        if (CommonConstants.EventType.OPT_UPDATE.equals(canalMsg.getEventType())) {
            UserEsInfo userEsInfo = userEsDao.findOne(userInfoBeforeData.getUserId());
            if (userEsInfo != null) {
                UserInfo userInfo = userEsInfo.getUserInfo();
                UserInfo afterData = CanalEntityParser.parse(userInfo, canalMsg.getAfterData(), UserInfo.class);
                userEsInfo.setUserInfo(afterData);
                log.info("更新user_info数据, 数据：{}", JSON.toJSONString(userEsInfo));
                userEsDao.save(userEsInfo);
            } else {
                userEsInfo = new UserEsInfo();
                userEsInfo.setUserInfo(userInfoAfterData);
                userEsInfo.setUserId(userInfoAfterData.getUserId());
                log.info("插入user_info数据，数据：{}", JSON.toJSONString(userEsInfo));
                userEsDao.save(userEsInfo);
            }

        } else if (CommonConstants.EventType.OPT_DELETE.equals(canalMsg.getEventType())) {
            log.info("插入user_info数据，数据：{}", JSON.toJSONString(userInfoBeforeData));
            userEsDao.delete(userInfoBeforeData.getUserId());

        } else if (CommonConstants.EventType.OPT_INSERT.equals(canalMsg.getEventType())) {
            UserEsInfo userEsInfo = userEsDao.findOne(userInfoBeforeData.getUserId());
            if (userEsInfo == null) {
                userEsInfo = new UserEsInfo();
                userEsInfo.setUserInfo(userInfoAfterData);
                userEsInfo.setUserId(userInfoAfterData.getUserId());
                log.info("插入user_info数据，数据：{}", JSON.toJSONString(userEsInfo));
                userEsDao.save(userEsInfo);
            }
        }
    }

    @Override
    public String getUser(String id) {

        return userDao.getUser(id);
    }

    @Override
    public String addUser(UserInfo userInfo) {
        return null;
    }
}
