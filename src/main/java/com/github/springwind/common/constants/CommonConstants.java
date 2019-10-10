package com.github.springwind.common.constants;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/10/9 11:09
 * @Desc
 */
public class CommonConstants {

    public static class User {
        public static final String USER_DB_NAME = "user";

        public static final String USER_INFO_TABLE_NAME = "user_info";

        public static final String USER_ACCOUNT_TABLE_NAME = "user_account";
    }

    public static class EventType {
        public final static String OPT_UPDATE = "update";
        public final static String OPT_DELETE = "delete";
        public final static String OPT_INSERT = "insert";
    }
}
