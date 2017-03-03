package com.heng.weather.Network;

import okhttp3.OkHttpClient;
import okhttp3.Request;
/**
 * Created by heng on 2017/3/3.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
