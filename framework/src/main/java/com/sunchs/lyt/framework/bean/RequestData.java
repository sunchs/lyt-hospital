package com.sunchs.lyt.framework.bean;

import com.google.gson.JsonObject;
import com.sunchs.lyt.framework.util.JsonUtil;

public class RequestData {

    public String version;

    public String platform;

    public Object params;

    public String token;

    private JsonObject jsonParams;

    public String getVersion() {
        return version;
    }

    public String getPlatform() {
        return platform;
    }

    public String getToken() {
        return token;
    }

    public JsonObject getParams() {
        if (jsonParams == null) {
            jsonParams = JsonUtil.toJsonObject(params);
        }
        return jsonParams;
    }

    public String getString(String key) {
        if (getParams().has(key)) {
            return getParams().get(key).getAsString();
        }
        return "";
    }

    public Integer getInt(String key) {
        if (getParams().has(key)) {
            return getParams().get(key).getAsInt();
        }
        return 0;
    }
}
