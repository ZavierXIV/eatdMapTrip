package com.eatd.baou.eatd_hackathon.InfoData;

/**
 * Created by TeddyYin on 2017/7/15.
 */


import java.util.ArrayList;

/**
 * Created by AU on 2017/7/13.
 */

public class InfoItemList {
    public static ArrayList<InfoItem> InfoList = new ArrayList<InfoItem>();
    private static ArrayList<String> NameList = null;
    private static ArrayList<String> AddList = null;
    private static ArrayList<Double> PxList;
    private static ArrayList<Double> PyList;

    public static InfoItem getItem(String title) {
        for (int i = 0; i < InfoList.size(); i++) {
            if (InfoList.get(i).Name.equals(title))
                return InfoList.get(i);
        }
        return null;
    }

    public static ArrayList<String> getNameList() {
        if (NameList == null) {
            NameList = new ArrayList<>();
            for (int i = 0; i < InfoList.size(); i++)
                NameList.add(InfoList.get(i).Name);
        }
        return NameList;
    }

    public static ArrayList<String> getAddList() {
        if (AddList == null) {
            AddList = new ArrayList<>();
            for (int i = 0; i < InfoList.size(); i++)
                AddList.add(InfoList.get(i).Name);
        }
        return AddList;
    }

    public static ArrayList<Double> getPxList() {
        if (PxList == null) {
            PxList = new ArrayList<>();
            for (int i = 0; i < InfoList.size(); i++)
                PxList.add(Double.parseDouble(InfoList.get(i).Px));
        }
        return PxList;
    }

    public static ArrayList<Double> getPyList() {
        if (PyList == null) {
            PyList = new ArrayList<>();
            for (int i = 0; i < InfoList.size(); i++)
                PyList.add(Double.parseDouble(InfoList.get(i).Py));
        }
        return PyList;
    }

    public static void getNameList(ArrayList<String> temp) {
        if (temp != null) NameList = temp;
    }

    public static void getAddList(ArrayList<String> temp) {
        if (temp != null) AddList = temp;
    }

    public static void getPxList(ArrayList<Double> temp) {
        if (temp != null) PxList = temp;
    }

    public static void getPyList(ArrayList<Double> temp) {
        if (temp != null) PyList = temp;
    }

    //////new//////
    public static ArrayList<String> getString(InfoItem infoItem) {
        ArrayList<String> temp = new ArrayList<>();

        temp.add(infoItem.Name);
        temp.add(infoItem.Tel);
        temp.add(infoItem.Add);
        temp.add(infoItem.Opentime);
        temp.add(infoItem.Website);
        return temp;
    }
}