package com.heng.weather.DataStore;

import com.heng.weather.MyApplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heng on 2017/3/3.
 */

public class DataStoreHelper extends SQLiteOpenHelper {
//    private Context mContext;

    // 建立一个用于存储省份内容的表
    public static final String CREATE_TABLE_PROVINCE = "create table province (id integer primary key autoincrement,code int,name text)";

    // 建立一个用于存储城市内容的表
    public static final String CREATE_TABLE_CITY = "create table city (id integer primary key autoincrement,code int,name text,provinceId int)";

    // 建立一个用于存储县内容的表
    public static final String CREATE_TABLE_COUNTY = "create table county (id integer primary key autoincrement,code int,name text,cityId int)";


    public DataStoreHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
//        mContext = context;
    }

    // 创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PROVINCE);
        db.execSQL(CREATE_TABLE_CITY);
        db.execSQL(CREATE_TABLE_COUNTY);
    }

    // 升级数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    // 插入省数据
    public static void saveProvince (Province province) {
        SQLiteDatabase db = MyApplication.getDataStoreHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("code",province.getProvinceCode());
        values.put("name",province.getProvinceName());

        db.insert("province",null,values);

    }

    //插入城市数据
    public static void saveCity(City city) {
        SQLiteDatabase db = MyApplication.getDataStoreHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("code",city.getCityCode());
        values.put("name",city.getCityName());
        values.put("provinceId",city.getProvinceId());
        db.insert("city",null,values);
    }

    //插入县数据
    public static void saveCounty(County county) {
        SQLiteDatabase db = MyApplication.getDataStoreHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("code",county.getCountyCode());
        values.put("name",county.getCountyName());
        values.put("cityId",county.getCityID());
        db.insert("county",null,values);
    }


    /** 从数据库中查询相应的地理信息 **/

    // 查询省份列表
    public List<Province> queryProvinces() {
        List<Province> provinceList = new ArrayList<Province>();
        SQLiteDatabase db = MyApplication.getDataStoreHelper().getReadableDatabase();
        Cursor cursor = db.query("province",null,null,null,null,null,"id asc");
        if (cursor.moveToFirst()) {
            do {
                Province province= new Province();
                province.setProvinceCode(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("name")));
                provinceList.add(province);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return provinceList;
    }

    // 查询省份所辖的城市列表
    public List<City> queryCitiesForProvince(int provinceId) {
        List<City> cityList = new ArrayList<City>();
        SQLiteDatabase db = MyApplication.getDataStoreHelper().getReadableDatabase();
        Cursor cursor = db.query("city",null,"provinceId = ?",new String[]{""+ provinceId},null,null,"id asc");
        if (cursor.moveToFirst()){
            do {
                City city = new City();
                city.setCityCode(cursor.getInt(cursor.getColumnIndex("code")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("name")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("provinceId")));
                cityList.add(city);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return cityList;
    }

    // 查询城市所辖的县列表
    public List<County> queryCountiesForCity(int cityId) {
        List<County> countiesList = new ArrayList<County>();
        SQLiteDatabase db = MyApplication.getDataStoreHelper().getReadableDatabase();
        Cursor cursor = db.query("county",null,"cityId = ?",new String[]{"" + cityId},null,null,"id asc");
        if (cursor.moveToFirst()){
            do {
                County county = new County();
                county.setCountyCode(cursor.getInt(cursor.getColumnIndex("code")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("name")));
                county.setCityID(cityId);
                countiesList.add(county);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return countiesList;
    }
}
