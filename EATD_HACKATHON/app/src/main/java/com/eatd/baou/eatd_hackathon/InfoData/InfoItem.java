package com.eatd.baou.eatd_hackathon.InfoData;

/**
 * Created by TeddyYin on 2017/7/15.
 */
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;

/**
 * Created by QwQ on 2017/7/12.
 */
/* Example
* Id="C1_376600000A_000001"
* Name="埤子頭植物園"
* Zone="" Toldescribe="休閒、遊憩、教學、研究四大功能場域&#xD;&#xA;&#xD;&#xA;．埤子頭植物園網站：http://cytfri.tfri.gov.tw/Bizihtou/"
* Description=""
* Tel="886-5-2322921"
* Add="嘉義市博愛路一段144-4號"
* Zipcode="600"
* Travellinginfo=""
* Opentime="08:30-16:00"
* Picture1="http://travel.chiayi.gov.tw/upload/14/2012051415585716901.jpg"
* Picdescribe1="埤子頭植物園入口　"
* Picture2="http://travel.chiayi.gov.tw/upload/14/2012051415591197789.jpg"
* Picdescribe2="埤子頭植物園　"
* Picture3="http://travel.chiayi.gov.tw/upload/14/2012051415592455407.jpg"
* Picdescribe3="埤子頭植物園園區"
* Map="http://goo.gl/maps/szopm"
* Gov="376600000A"
* Px="120.44510"
* Py="23.48810"
* Orgclass=""
* Class1="11"
* Class2=""
* Class3=""
* Level=""
* Website="林業試驗所-埤子頭植物園http://www.tfri.gov.tw"
* Parkinginfo=""
* Parkinginfo_px="000.00000"
* Parkinginfo_py="00.00000"
* Ticketinfo=""
* Remarks=""
* Keyword=""
* Changetime="2013/08/12 13:44:38" />
* */
public class InfoItem {
    public String Id;
    public String Name;
    public String Zone;
    public String Toldescribe;
    public String Description;
    public String Tel;
    public String Add;
    public String Zipcode;
    public String Travellinginfo;
    public String Opentime;
    public String Picture1;
    public String Picdescribe1;
    public String Picture2;
    public String Picdescribe2;
    public String Picture3;
    public String Picdescribe3;
    public String Map;
    public String Gov;
    public String Px;
    public String Py;
    public String Orgclass;
    public String Class1;
    public String Class2;
    public String Class3;
    public String Level;
    public String Website;
    public String Parkinginfo;
    public String Parkinginfo_px;
    public String Parkinginfo_py;
    public String Ticketinfo;
    public String Remarks;
    public String Keyword;
    public String Changetime;
    private XmlReader XR = null;

    public InfoItem() {
        XR = new XmlReader();
    }

    public InfoItem(String Info) {
        XR = new XmlReader();
        try {
            XR.setXML(Info);
            int eventType = XR.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    int count = XR.attributeCount();
                    Id = XR.getAttributeValue("Id");
                    Name = XR.getAttributeValue("Name");
                    Zone= XR.getAttributeValue("Zone");
                    Toldescribe= XR.getAttributeValue("Toldescribe");
                    Description= XR.getAttributeValue("Description");
                    Tel= XR.getAttributeValue("Tel");
                    Add= XR.getAttributeValue("Add");
                    Zipcode= XR.getAttributeValue("Zipcode");
                    Travellinginfo= XR.getAttributeValue("Travellinginfo");
                    Opentime= XR.getAttributeValue("Opentime");
                    Picture1= XR.getAttributeValue("Picture1");
                    Picdescribe1= XR.getAttributeValue("Picdescribe1");
                    Picture2= XR.getAttributeValue("Picture2");
                    Picdescribe2= XR.getAttributeValue("Picdescribe2");
                    Picture3= XR.getAttributeValue("Picture3");
                    Picdescribe3= XR.getAttributeValue("Picdescribe3");
                    Map= XR.getAttributeValue("Map");
                    Gov= XR.getAttributeValue("Gov");
                    Px= XR.getAttributeValue("Px");
                    Py= XR.getAttributeValue("Py");
                    Orgclass= XR.getAttributeValue("Orgclass");
                    Class1= XR.getAttributeValue("Class1");
                    Class2= XR.getAttributeValue("Class2");
                    Class3= XR.getAttributeValue("Class3");
                    Level= XR.getAttributeValue("Level");
                    Website= XR.getAttributeValue("Website");
                    Parkinginfo= XR.getAttributeValue("Parkinginfo");
                    Parkinginfo_px= XR.getAttributeValue("Parkinginfo_px");
                    Parkinginfo_py= XR.getAttributeValue("Parkinginfo_py");
                    Ticketinfo= XR.getAttributeValue("Ticketinfo");
                    Remarks= XR.getAttributeValue("Remarks");
                    Keyword= XR.getAttributeValue("Keyword");
                    Changetime= XR.getAttributeValue("Changetime");
                }
                eventType = XR.getNextEvent();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
