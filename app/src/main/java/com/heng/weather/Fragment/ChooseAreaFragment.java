package com.heng.weather.Fragment;

import com.heng.weather.DataStore.*;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.heng.weather.JsonAnalyse.JsonHandle;
import com.heng.weather.MyApplication;
import com.heng.weather.Network.HttpUtil;
import com.heng.weather.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by heng on 2017/3/3.
 */

public class ChooseAreaFragment extends Fragment {
    // 层级-省
    public static final int LEAVEL_PROVINCE = 0;
    // 层级-市
    public static final int LEAVEL_CITY = 1;
    // 层级-县
    public static final int LEAVEL_COUNTY = 2;

    // 加载指示符
    private ProgressDialog progressDialog;

    // 标题文本框
    private TextView titleView;
    // 返回按钮
    private Button backButton;
    // 内容列表
    private ListView listView;

    // 适配器
    private ArrayAdapter<String> adapter;

    // 数据源列表
    private List<String> dataList = new ArrayList<String>();

    // 省列表
    private List<Province> provinceList;
    // 市列表
    private List<City> cityList;
    // 县列表
    private List<County> countyList;

    // 选中的省
    private Province selectedProvince;
    // 选中的市
    private City selectedCity;
    // 选中的县
    private County selectedCounty;

    // 当前选中的层级
    private int currentLeavel;


    private static final String TAG = "ChooseAreaFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);

        titleView = (TextView)view.findViewById(R.id.title_text);
        backButton = (Button)view.findViewById(R.id.back_button);
        listView = (ListView)view.findViewById(R.id.list_view);

        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLeavel == LEAVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }
                else if (currentLeavel == LEAVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLeavel == LEAVEL_COUNTY) {
                    queryCities();
                }
                else if (currentLeavel == LEAVEL_CITY){
                    queryProvinces();
                }
            }
        });

        // 先显示城市内容
        queryProvinces();

    }

    // 查询省份列表
    public void queryProvinces(){
        titleView.setText(R.string.area_china);
        backButton.setVisibility(View.GONE);

        provinceList = MyApplication.getDataStoreHelper().queryProvinces();
        // 优先从数据库查询，
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList){
                dataList.add(province.getProvinceName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLeavel = LEAVEL_PROVINCE;
        }
        else {
            String address = "http://guolin.tech/api/china";
            queryAreaDataFromServer(address,"province");

        }
    }

    // 查询省所辖的城市列表
    public void queryCities(){
        cityList = MyApplication.getDataStoreHelper().queryCitiesForProvince(selectedProvince.getProvinceCode());
        if (cityList.size() > 0) {
            titleView.setText(selectedProvince.getProvinceName());
            backButton.setVisibility(View.VISIBLE);

            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLeavel = LEAVEL_CITY;
        }
        else {
            String address = "http://guolin.tech/api/china/"+selectedProvince.getProvinceCode();
            queryAreaDataFromServer(address,"city");
        }
    }

    // 查询市对应的县列表
    public void queryCounties(){
        countyList = MyApplication.getDataStoreHelper().queryCountiesForCity(selectedCity.getCityCode());
        if (countyList.size() > 0) {
            titleView.setText(selectedCity.getCityName());
            backButton.setVisibility(View.VISIBLE);

            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();

            listView.setSelection(0);
            currentLeavel = LEAVEL_COUNTY;
        }
        else {
            String address = "http://guolin.tech/api/china/"+selectedProvince.getProvinceCode()+"/"+selectedCity.getCityCode();
            queryAreaDataFromServer(address,"county");
        }
    }


    /// 根据传入的地址和类型从服务器上查询省市县数据
    private void queryAreaDataFromServer(final String address,final String type) {
        // 显示加载指示
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: 从服务器获取数据成功");
                String responseText = response.body().string();
                boolean result = false;
                if (type.equals("province")) {
                    result = JsonHandle.handleProvinceResponse(responseText);
                }
                else if (type.equals("city")) {
                    result = JsonHandle.handleCityResponse(responseText,selectedProvince.getProvinceCode());
                }
                else if (type.equals("county")) {
                    result = JsonHandle.handleCountyResponse(responseText,selectedCity.getCityCode());
                }

                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            if (type.equals("province")) {
                                queryProvinces();
                            }
                            else if (type.equals("city")) {
                                queryCities();
                            }
                            else {
                                queryCounties();
                            }
                        }
                    });
                }
            }

        });
    }

   // 显示加载指示符
    private void showProgressDialog(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null){
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage(getActivity().getResources().getString(R.string.progress_hint));
                    progressDialog.setCanceledOnTouchOutside(false);
                }
                progressDialog.show();
            }
        });
    }
}