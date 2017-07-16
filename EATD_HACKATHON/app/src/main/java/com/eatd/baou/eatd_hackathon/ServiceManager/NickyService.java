package com.eatd.baou.eatd_hackathon.ServiceManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.eatd.baou.eatd_hackathon.MapsActivity;
import com.eatd.baou.eatd_hackathon.R;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by User on 2017/7/8.
 */

public class NickyService extends Service implements Observer {

    private Handler handler = new Handler();
    private NotificationManager mNotificationManager;
    private PowerManager.WakeLock wakeLock;
    private long checkDelayTime = 3000; // 每隔幾毫秒執行一次
    private final int serviceDoMaxCount = 10; // service 最大執行次數 // Test
    private int serviceDoCount = 0; // service 目前執行次數

    private DataManager dataManager;
    private String zavier_tag = "zavier_tag";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(zavier_tag, "Service onCreate...");
//        Toast.makeText(this, "Service On Create!", Toast.LENGTH_SHORT).show();
        // 讓 Service 在 背影持續執行
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();

        serviceDoCount = 0;
        dataManager = DataManager.getInstace();
        dataManager.getMapViewInfoSubject().addObserver(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String msg = String.format("Service onStartCommand  flag: %d  startId: %d", flags, startId);
        Log.i(zavier_tag, msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        handler.postDelayed(processMapViewDistance, checkDelayTime);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        handler.removeCallbacks(processMapViewDistance);
        dataManager.getMapViewInfoSubject().deleteObserver(this);
        Log.i(zavier_tag, "Serivce on Destroy");
        Toast.makeText(this, "service onDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    private Runnable processMapViewDistance = new Runnable() {
        @Override
        public void run() {

            ++serviceDoCount;
            String msg = String.format("Runnable ing ... %d / %d", serviceDoCount, serviceDoMaxCount);
            Log.i(zavier_tag, msg);
            if (serviceDoMaxCount == serviceDoCount)// 避免 service 失控一直跑
            {
                Log.i(zavier_tag, "Service MaxCount Will Stop...");
                stopSelf();
                return;
            }

            // 觸發 Google Map 更新用戶位置 與 所在地和各景點距離
            dataManager.mapViewInfo.UpateLocationDetail();

            handler.postDelayed(processMapViewDistance, checkDelayTime);
        }
    };

    private void useNotification(String contentText) {
        //Step2. 設定當按下這個通知之後要執行的activity
        Intent notifyIntent = new Intent(this, MapsActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//        PendingIntent appIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0);
        PendingIntent appIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long[] vibrate = {1000};
//                Bitmap
//                Notification
        //Step3. 透過 Notification.Builder 來建構 notification，
        //並直接使用其.build() 的方法將設定好屬性的 Builder 轉換
        //成 notification，最後開始將顯示通知訊息發送至狀態列上。
        Notification notification
                = new Notification.Builder(this)
                .setContentIntent(appIntent)
                .setSmallIcon(R.drawable.ic_loyalty_black_24dp) // 設置狀態列裡面的圖示（小圖示）　　
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.firerice)) // 下拉下拉清單裡面的圖示（大圖示）
                .setTicker("有新景點!! 快來瞧瞧") // 設置狀態列的顯示的資訊
                .setWhen(System.currentTimeMillis())// 設置時間發生時間
                .setAutoCancel(false) // 設置通知被使用者點擊後是否清除  //notification.flags = Notification.FLAG_AUTO_CANCEL;
                .setContentTitle("有新景點!! 快來瞧瞧") // 設置下拉清單裡的標題
                .setContentText(contentText)// 設置上下文內容
                .setOngoing(true)      //true使notification變為ongoing，用戶不能手動清除// notification.flags = Notification.FLAG_ONGOING_EVENT; notification.flags = Notification.FLAG_NO_CLEAR;
                .setDefaults(Notification.DEFAULT_ALL) //使用所有默認值，比如聲音，震動，閃屏等等
                //                 .setDefaults(Notification.DEFAULT_VIBRATE) //使用默認手機震動提示
                //                 .setDefaults(Notification.DEFAULT_SOUND) //使用默認聲音提示
                //                 .setDefaults(Notification.DEFAULT_LIGHTS) //使用默認閃光提示
                //                 .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND) //使用默認閃光提示 與 默認聲音提示

                .setVibrate(vibrate) //自訂震動長度
                //                 .setSound(uri) //自訂鈴聲
                //                 .setLights(0xff00ff00, 300, 1000) //自訂燈光閃爍 (ledARGB, ledOnMS, ledOffMS)
                .build();

        // 將此通知放到通知欄的"Ongoing"即"正在運行"組中
        notification.flags = Notification.FLAG_ONGOING_EVENT;

        // 表明在點擊了通知欄中的"清除通知"後，此通知不清除，
        // 經常與FLAG_ONGOING_EVENT一起使用
        notification.flags = Notification.FLAG_NO_CLEAR;

        //閃爍燈光
        notification.flags = Notification.FLAG_SHOW_LIGHTS;

        // 重複的聲響,直到用戶響應。
//                notification.flags = Notification.FLAG_INSISTENT; //


        // 把指定ID的通知持久的發送到狀態條上.
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, notification);
    }

    @Override
    public void update(Observable o, Object arg) { // 接受 google map 景點距離的 更新資訊
        if (o instanceof MapViewInfo) {
            MapViewInfo info = ((MapViewInfo) o);
            String closeViewInfo = info.viewList.get(0).getViewInfo();
            String msg = String.format("偵測距離 %s 最近點 %s \n執行次數 %d/%d  UI是否開啟 %s", info.targetDistance, closeViewInfo, serviceDoCount, serviceDoMaxCount, MapsActivity.hasOnWindowFocus);

//            if (MapsActivity.hasOnWindowFocus) {
//                if (MapsActivity.tv_info != null)
//                    MapsActivity.tv_info.setText(msg);
//            } else
            Log.i(zavier_tag, msg);
//            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

            // 夠近 且UI沒有開啟畫面的時候 觸發通知
            boolean hasCloseViewPonit = ((boolean) arg);
            if (hasCloseViewPonit) {
                if (!MapsActivity.hasOnWindowFocus)
                    useNotification(closeViewInfo);
            }
        }
    }
}