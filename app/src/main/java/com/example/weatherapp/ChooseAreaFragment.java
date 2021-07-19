package com.example.weatherapp;

import android.app.ProgressDialog;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weatherapp.db.City;
import com.example.weatherapp.db.County;
import com.example.weatherapp.db.Province;

import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hello word
 * @desc 作用描述
 * @date 2021/7/19
 */
public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 0;
    public static final int LEVEL_COUNTY = 0;
    private ProgressDialog progressDialog;
    private Button backBtn;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    /**
     * 省列表
    * */
    private List<Province> provinceList;
    /**
     * 市列表
     * */
    private List<City> cityList;
    /**
     * 区列表
     * */
    private List<County> countyList;



























}
