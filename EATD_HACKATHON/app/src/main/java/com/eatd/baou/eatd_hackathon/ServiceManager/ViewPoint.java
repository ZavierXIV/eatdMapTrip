package com.eatd.baou.eatd_hackathon.ServiceManager;

/**
 * Created by TeddyYin on 2017/7/15.
 */

import android.support.annotation.NonNull;

/**
 * Created by User on 2017/7/9.
 */

public class ViewPoint implements Comparable<ViewPoint> {
    @Override
    public int compareTo(@NonNull ViewPoint o) { // 依照距離做排序
        return new Integer(this.distance).compareTo(new Integer(o.distance));
    }

    public ViewPoint(String name, int dis) {
        this.name = name;
        this.distance = dis;
    }

    public String name; // 地名
    public int distance;

    public String getViewInfo() {
        return String.format("[%s]-%d公尺", name, distance);
    }
}
