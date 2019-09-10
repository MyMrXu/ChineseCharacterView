package com.xzwzz.writecharacter.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @package_Name :com.example.common.commonutils
 * @AUTHOR :xzwzz@vip.qq.com
 * @DATE :2017/12/1  19:12
 */
public class JsonUtil {
    private static Gson mGson;

    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        if (mGson == null) {
            mGson = new Gson();
        }
        List<T> list = new ArrayList<T>();
        try {
            list = mGson.fromJson(json, new ListOfJson(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        if (mGson == null) {
            mGson = new Gson();
        }
        return new Gson().fromJson(json, clazz);
    }

    public static class ListOfJson<T> implements ParameterizedType {
        private Class<?> wrapped;

        public ListOfJson(Class<T> wrapper) {
            this.wrapped = wrapper;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{wrapped};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    public static int getStatus(String json) {
        try {
            JSONObject j = new JSONObject(json);
            return j.optInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getMsg(String json) {
        try {
            JSONObject j = new JSONObject(json);
            return j.optString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static <T> ArrayList<T> parseData(String result, Class<T> c) {
        if (mGson == null) {
            mGson = new Gson();
        }
        ArrayList<T> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            for (int i = 0; i < data.length(); i++) {
                T entity = mGson.fromJson(data.optJSONObject(i).toString(), c);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public final static boolean isJSONValid3(String jsonInString) {
        if (mGson == null) {
            mGson = new Gson();
        }
        try {
            mGson.fromJson(jsonInString, Object.class);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }
}
