package app.core.utils;

import com.google.gson.Gson;

public class GsonUtil {

    public static String serialize(Object object) {
        Gson gsonUtil = new Gson();
        return gsonUtil.toJson(object);
    }
}
