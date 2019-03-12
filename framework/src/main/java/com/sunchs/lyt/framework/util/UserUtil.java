package com.sunchs.lyt.framework.util;

import com.sunchs.lyt.framework.bean.RequestData;
import com.sunchs.lyt.framework.bean.UserCacheData;

public class UserUtil {

    public static ThreadLocal<UserCacheData> userHandle = new ThreadLocal<>();

    public static void initUser(String user) {
        RequestData data = JsonUtil.toObject(user, RequestData.class);
        if (data != null && StringUtil.isNotEmpty(data.getToken())) {
            if (RedisUtil.exists(data.getToken())) {
                UserCacheData res = RedisUtil.getValue(data.getToken(), UserCacheData.class);
                userHandle.set(res);
            }
        }
    }

    public static Integer getUserId() {
        UserCacheData user = userHandle.get();
        if (user != null) {
            return user.getUserId();
        }
        return 0;
    }

    public static String getUserToken() {
        UserCacheData user = userHandle.get();
        if (user != null) {
            return user.getToken();
        }
        return "";
    }
}
