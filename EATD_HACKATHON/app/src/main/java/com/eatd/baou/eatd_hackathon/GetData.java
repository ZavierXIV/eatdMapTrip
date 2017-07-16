package com.eatd.baou.eatd_hackathon;

/**
 * Created by TeddyYin on 2017/7/15.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eatd.baou.eatd_hackathon.InfoData.InfoItem;
import com.eatd.baou.eatd_hackathon.InfoData.InfoItemList;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by QwQ on 2017/7/14.
 */

public class GetData {
    private String TAG = "InfoItem";
    private Context context;
    private int statusCode;

    //START = 0;
    //OK = 200;
    //FAIL = 404;
    public GetData(Context context) {
        this.context = context;

    }
    public void Start() {
        statusCode = 0;
        getData();
    }
    public int getStatusCode(){return statusCode;}
    private void getData() {
        String DATA_URL = "http://travel.chiayi.gov.tw/xml/C1_376600000A.xml";
        RequestQueue mQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            PreParseData(new String(s.getBytes("ISO-8859-1"), "utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //FUCK U YIN
                    }
                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Log.i(TAG, response.statusCode + "");
                statusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        mQueue.add(getRequest);
    }
    private SharedPreferences settings;
    private static final String data = "DATA";
    public void PreParseData(String Result) {
        if (statusCode == 200) {
            settings = context.getSharedPreferences(data,0);
            settings.edit().putString(data,Result).commit();
            LoadData();
        }else if(statusCode == 304){
            if ( settings.getString(data,"").equals(""))
                settings.edit().putString(data,Result).commit();
        }
    }
    public void LoadData() {
        settings = context.getSharedPreferences(data,0);
        String Result = settings.getString(data,"");
        if(Result.equals("")) {
            Log.i(TAG,"Result is empty");
            return;
        }
        ArrayList<InfoItem> al = new ArrayList<>();
        int temp = 0;
        for (int i = 0; i < Result.length() - 8; i++) {
            if (Result.substring(i, i + 8).equals("<Info Id")) {
                if (temp != 0) { //NOT FIRST TIME
                    String sub_string = Result.substring(temp, i);
                    al.add(new InfoItem(sub_string));
                }
                temp = i;
            }
        }
        InfoItemList.InfoList = al;
        Log.i(TAG,InfoItemList.InfoList.get(0).Add);
    }
}
