package com.eatd.baou.eatd_hackathon.ServiceManager;

/**
 * Created by TeddyYin on 2017/7/15.
 */

import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONObject;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by User on 2017/7/9.
 */

public class DataManager {

    public static String zavier_tag = "zavier_tag";
    public MapViewInfo mapViewInfo;
    static DataManager instace;

    private DataManager() {
        mapViewInfo = new MapViewInfo();
    }

    public Observable getMapViewInfoSubject() {
        return (Observable) mapViewInfo;
    }

    public static DataManager getInstace() {
        if (instace == null)
            instace = new DataManager();

        return instace;
    }

    // 所有觀光景點 與 用戶的距離 資訊列表
    private ArrayList<ViewPoint> disViewLIst = new ArrayList<ViewPoint>();

    public void SetDisViewList(ArrayList<JSONObject> jsons) {
        if (jsons == null || jsons.size() == 0)
            return;

        try {
            disViewLIst.clear();
            JSONObject obj;
            ViewPoint point;
            int size = jsons.size();
            for (int i = 0; i < size; i++) {
                obj = jsons.get(i);
                String name = obj.getString("name");
                int dis = obj.getInt("value");
                disViewLIst.add(new ViewPoint(name, dis));
            }
        } catch (Exception e) {
            Log.i(zavier_tag, "SetDisViewList Exception: " + e.toString());
        }
    }

    public ArrayList<ViewPoint> GetDisViewList() {
        return disViewLIst;
    }

}
