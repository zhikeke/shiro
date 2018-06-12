package com.shiro.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/3 19:23
 * 返回Json 数据
 */
@Getter
@Setter
public class JsonData {
    private boolean res;
    private String msg;
    private Object data;


    public JsonData(boolean res) {
        this.res = res;
    }

    public static JsonData success(boolean res, String msg) {
        JsonData jsonData = new JsonData(true);
        jsonData.res = res;
        jsonData.msg = msg;
        return jsonData;
    }

    public static JsonData success(boolean res, String msg, Object data) {
        JsonData jsonData = new JsonData(true);
        jsonData.res = res;
        jsonData.msg = msg;
        jsonData.data = data;
        return jsonData;
    }

    public static JsonData success(Object data) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = data;
        return jsonData;
    }

    public static JsonData success() {
        return new JsonData(true);
    }

    public static JsonData fail(String msg) {
        JsonData jsonData = new JsonData(false);
        jsonData.msg = msg;
        return jsonData;
    }

    public  Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("res", res);
        map.put("msg", msg);
        map.put("data", data);
        return map;
    }


}
