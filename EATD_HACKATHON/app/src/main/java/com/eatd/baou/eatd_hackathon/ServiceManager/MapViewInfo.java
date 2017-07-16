package com.eatd.baou.eatd_hackathon.ServiceManager;

/**
 * Created by TeddyYin on 2017/7/15.
 */

import android.provider.ContactsContract;
import android.util.Log;

import com.eatd.baou.eatd_hackathon.MapsActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

/**
 * Created by User on 2017/7/9.
 */
public class MapViewInfo extends Observable {
    private String zavier_tag = "zavier_tag";
    ArrayList<ViewPoint> viewList = new ArrayList<ViewPoint>();
    public final int targetDistance = 1000; // 靠近到多少距離內就通知 ========================  !!!!!!!!!!!!!!
    public final int MAX_CHECK_VIEW_COUNT = 10; // 最大搜尋個數

    public void setViewList(ArrayList<ViewPoint> list) {
        if (list == null || list.size() == 0)
            return;

        this.viewList.clear();
        for (int i = 0; i < list.size(); i++) {
            if (i >= MAX_CHECK_VIEW_COUNT)
                break;
            this.viewList.add(list.get(i));
        }

        setChanged();

        boolean hasCloseViewPonit = viewList.get(0).distance <= targetDistance;
        notifyObservers(hasCloseViewPonit); // 參數: 是否有靠近的 景點
    }

    public void UpateLocationDetail() {
        // fake data
//        setViewList(getFakeViewList());

        // 1. update user position
//        DataManager.getInstace().SetDisViewList(Jsons);
//        MapsActivity.

        // 2. get disViewlist
        setViewList(DataManager.getInstace().GetDisViewList());
    }


    private ArrayList<ViewPoint> getFakeViewList() {
        ArrayList<ViewPoint> list = new ArrayList<ViewPoint>();
        int viewCount = (int) (Math.random() * targetDistance) + 1;
        for (int i = 0; i < viewCount; i++) {
            int dis = (int) (Math.random() * targetDistance) + targetDistance;
            String name = "景點 " + i;
            list.add(new ViewPoint(name, dis));
        }

        Collections.sort(list);
        return list;
    }

    public String getViewListInfo() {
        String info = "";
        for (ViewPoint point : viewList) {
            info += "\n" + point.getViewInfo();
        }
        return info;
    }
}
