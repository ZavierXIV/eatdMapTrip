package com.eatd.baou.eatd_hackathon.YU_CHENG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.eatd.baou.eatd_hackathon.InfoData.InfoItem;
import com.eatd.baou.eatd_hackathon.InfoData.InfoItemList;
import com.eatd.baou.eatd_hackathon.MapsActivity;
import com.eatd.baou.eatd_hackathon.MySingleton;
import com.eatd.baou.eatd_hackathon.R;
import com.eatd.baou.eatd_hackathon.ServiceManager.DataManager;
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
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cn.bingoogolapple.bgabanner.BGABanner;

public class ItemRecyclerViewAcivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private BGABanner mBanner;
    private RecyclerView mDataRv;
    private ItemRecycleViewAdapter itemRecycleViewAdapter;
    private InfoItem infoItem;

    LatLng my_latlng;

    private GoogleMap itemMap;
    private GoogleApiClient mGoogleApiClient;

    String TAG = "ItemRecyclerViewAcivity  ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_show_layout);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);
        Bundle b = getIntent().getExtras();
        infoItem = InfoItemList.getItem(b.getString("Name"));
        initIntBanner();
        initRecycleView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        itemMap = googleMap;
        LatLng a = new LatLng(Double.valueOf(infoItem.Py), Double.valueOf(infoItem.Px));
        itemMap.addMarker(new MarkerOptions()
                .position(a)
                .title("Marker"));

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
                        my_latlng = likelyPlaces.get(0).getPlace().getLatLng();
                    }catch (Exception e) {
                        my_latlng = GetLocation.getCurrentLocation(ItemRecyclerViewAcivity.this);

                    }
                }
                Log.e("MY latlng",my_latlng.toString());
            }
        });

        itemMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(infoItem.Py),Double.valueOf(infoItem.Px)), 16));

    }

    private void initIntBanner(){
        mBanner = (BGABanner)findViewById(R.id.banner);
        mBanner.setData(Arrays.asList(infoItem.Picture1,infoItem.Picture2,infoItem.Picture3), Arrays.asList(infoItem.Picdescribe1,infoItem.Picdescribe2, infoItem.Picdescribe3));
        mBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(ItemRecyclerViewAcivity.this)
                        .load(model)
                        .placeholder(R.drawable.ic_loyalty_black_24dp)
                        .error(R.drawable.ic_loyalty_black_24dp)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });

    }
    private void initRecycleView(){
        mDataRv = (RecyclerView)findViewById(R.id.data);
        mDataRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ArrayList<String> al = InfoItemList.getString(infoItem);
        itemRecycleViewAdapter = new ItemRecycleViewAdapter(ItemRecyclerViewAcivity.this,al);
        mDataRv.setAdapter(itemRecycleViewAdapter);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
