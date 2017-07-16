package com.eatd.baou.eatd_hackathon;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.eatd.baou.eatd_hackathon.InfoData.InfoItem;
import com.eatd.baou.eatd_hackathon.InfoData.InfoItemList;
import com.eatd.baou.eatd_hackathon.ServiceManager.DataManager;
import com.eatd.baou.eatd_hackathon.ServiceManager.MapViewInfo;
import com.eatd.baou.eatd_hackathon.ServiceManager.NickyService;
import com.eatd.baou.eatd_hackathon.YU_CHENG.GetLocation;
import com.eatd.baou.eatd_hackathon.YU_CHENG.ItemRecyclerViewAcivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleApiClient.OnConnectionFailedListener, Observer {

    private static final String TAG = "========EATD========";
//    private static final String DIRECTIONS_API_KEY = "AIzaSyB2nQBcegphRKwvXHGLCGAlbepeQLrr3p8";
    private static final String DISTANCE_MATRIX_API_KEY = "&key=AIzaSyCViexOOOv_dn2Au6w9Z5By33yx3oufUtk";
    private static final String LANGUAGE = "&language=zh-CN";
//    private static final String DESTINATION_ADDRESSES = "destination_addresses";
    public static List<String> address_total = new ArrayList<>();
    public static ArrayList<JSONObject> viewList = new ArrayList<JSONObject>();
    public static boolean hasOnWindowFocus = false;
    private LatLng my_place_latlng;
    String URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric";
    String ORIGINS = "&origins=";
    String DESTINATIONS = "&destinations=";
    // 註冊 景點距離資訊
    Observable mapViewInfoSubject;
    private GoogleMap mMap;
    private JSONObject staticJson = new JSONObject();
    private GoogleApiClient mGoogleApiClient;
    Boolean IS_FIRST_IN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        IS_FIRST_IN = true;

        // 註冊監聽 Service 更新事件
        this.mapViewInfoSubject = DataManager.getInstace().getMapViewInfoSubject();
        this.mapViewInfoSubject.addObserver(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i = 0; i < InfoItemList.InfoList.size(); i++) {
            address_total.add(InfoItemList.InfoList.get(i).Add);
        }

        /** getMyPlace 流程
         *
         * 方法簡易流程
         * getMyPlace{             取得自己位置
         *     loadViewData()      取得資料
         *     calDistance(){      計算距離
         *         parseJSON()     解析 Json
         *         addMarker()     標記景點
         *     }
         * }
         *
         * getMyPlace -> loadViewData -> calDistance -> parseJSON -> addMarker
         *
         *
         * getMyPlace 取得自己位置後 loadViewData 取得資料
         * 取到資料後 calDistance 使用 volley 呼叫 google api 計算本身位置與附近景點距離
         * 當成功返回時將 api 回傳的 Json 透過 parseJSON 組成我們需要的格式
         * 組合完畢後藉由 addMarker 執行標記景點位置
         */
        getMyPlace();
    }

    private void getMyPlace() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
        }
        // 確認權限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {

                if (likelyPlaces != null){
                    try {
                        my_place_latlng = likelyPlaces.get(0).getPlace().getLatLng();
                    }catch (Exception e) {
                        my_place_latlng = GetLocation.getCurrentLocation(MapsActivity.this);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my_place_latlng, 16));
                    }
                    loadViewData();
                    calDistance();
                }
            }
        });

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ItemRecyclerViewAcivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Name", marker.getTitle());
            intent.putExtras(bundle);
            startActivity(intent);
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private void loadViewData() {
        GetData data = new GetData(this);
        data.Start();//START DOWNLOAD
        data.LoadData();//LOAD DATA
    }

    private void calDistance() {

        if (InfoItemList.InfoList.size() == 0)
            return;

        String destinations_place = "";
        for (int i = 0; i < InfoItemList.InfoList.size() - 1; i++) {
            destinations_place += InfoItemList.InfoList.get(i).Py + "," + InfoItemList.InfoList.get(i).Px;
            if (i < InfoItemList.InfoList.size() - 2)
                destinations_place += "|";
        }
        String my_place_latlng_parse = my_place_latlng.latitude + "," + my_place_latlng.longitude;
        String url = URL + ORIGINS + my_place_latlng_parse + DESTINATIONS + destinations_place + LANGUAGE + DISTANCE_MATRIX_API_KEY;
        //  https://maps.googleapis.com/maps/api/distancematrix/json?units=metric
        // &origins=600台灣嘉義市西區民生北路228號
        // &destinations=120.44510,23.48810|120.42210,23.47700|120.47530,23.47040|120.47530,23.47040|120.34580,23.55299
        // &language=zh-CN
        // &key=AIzaSyBhLUjf0mnkXP3bdFAeN5jOikJFL6oMuLU
        Log.e(TAG, url);
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, staticJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        staticJson = response;
                        Log.e(TAG, response.toString());
                        parseJSON(response);
                        addMarker();
                        DataManager.getInstace().SetDisViewList(viewList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
            }
        });
        // Adding JsonObject request to request queue
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq);
    }

    public void parseJSON(JSONObject JSON_DATA) {
        try {
            String s = JSON_DATA.get("destination_addresses").toString();
            s = s.substring(1, s.length() - 1);
            Log.e("JSON", s);
            String[] destination_addresses = s.split(",");
            JSONArray arr = (JSONArray) JSON_DATA.get("destination_addresses");
            for (int i = 0; i < arr.length(); i++)
                Log.e("ARR", (String) arr.get(i));
            for (String ss : destination_addresses)
                Log.e("destination_addresses", ss);
            // 取得 row
            JSONArray rows = JSON_DATA.getJSONArray("rows");
            // 取得 elements
            JSONObject row = rows.getJSONObject(0);
            JSONArray elements = row.getJSONArray("elements");
            Log.e("JSON", "LENGTH " + elements.length());
            viewList.clear();
            for (int i = 0; i < elements.length(); i++) {
                JSONObject elements_obj = elements.getJSONObject(i);
                JSONObject distance = elements_obj.getJSONObject("distance");
                distance.put("address", arr.get(i));
                distance.put("name", InfoItemList.InfoList.get(i).Name);
                viewList.add(distance);
            }
            Log.e("JSON", "not sort" + viewList.toString());
            Collections.sort(viewList, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    int v1 = 0;//todo:  to int
                    int v2 = 0;//todo:  to int
                    try {
                        v1 = Integer.parseInt(o1.get("value").toString());
                        v2 = Integer.parseInt(o2.get("value").toString());
                    } catch (JSONException e) {
                        Log.e("JSON", "e " + e);
                        e.printStackTrace();
                    }
                    return new Integer(v1).compareTo(new Integer(v2));
                }
            });
            Log.e("JSON", "sort" + viewList.toString());
        } catch (JSONException e) {
            Log.e(TAG, "ERROR " + e.toString());
            e.printStackTrace();
        }
    }

    private void addMarker() {
        Log.e(TAG, viewList.size() + " BBB ");
        Log.e(TAG, viewList.toString());

        Marker marker;
        mMap.clear();

        for (int i = 0; i < 10 && i < viewList.size() ; i++) {
            try {
                String name = viewList.get(i).getString("name");
                InfoItem info = InfoItemList.getItem(name);
                if (info != null) {
                    LatLng a = new LatLng(Double.valueOf(info.Py), Double.valueOf(info.Px));
                    marker = mMap.addMarker(new MarkerOptions().position(a).title(info.Name));
                    marker.setTag(0);
                } else {
                    Log.e(TAG, "========NULL========");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (IS_FIRST_IN){
            mMap.setMyLocationEnabled(true);
            // Set a listener for marker click.
            mMap.setOnMarkerClickListener(this);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my_place_latlng, 16));
            IS_FIRST_IN = false;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "Activity Check Service Runngin ? " + isServiceRunning(NickyService.class) + " ObserableCount =" + this.mapViewInfoSubject.countObservers());
        Intent intent = new Intent(MapsActivity.this, NickyService.class);
        if (isServiceRunning(NickyService.class))
            stopService(intent);

        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mapViewInfoSubject.deleteObserver(this);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        // Loop through the running services
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                // If the service is running then return true
                return true;
            }
        }
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        hasOnWindowFocus = hasFocus;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MapViewInfo) {

            getMyPlace();

//            MapViewInfo data = (MapViewInfo)o;
//            // tv_lab is TextView
//            if(tv_lab!=null)
//                tv_lab.setText(data.getViewListInfo());
        }
    }
}
