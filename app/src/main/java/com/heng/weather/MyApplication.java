package com.heng.weather;

import android.app.Application;
import android.content.Context;

import com.heng.weather.DataStore.DataStoreHelper;

/**
 * Created by heng on 2017/3/3.
 */

public class MyApplication extends Application {
//    private static Context mContext;
    private static DataStoreHelper dataStoreHelper;

    @Override
    public void onCreate() {
        super.onCreate();
//        mContext = getApplicationContext();
        dataStoreHelper = new DataStoreHelper(getApplicationContext(),"Location",null,1);
    }

//    public static Context getmContext() {
//        return mContext;
//    }

    public static DataStoreHelper getDataStoreHelper() {
        return dataStoreHelper;
    }
}
