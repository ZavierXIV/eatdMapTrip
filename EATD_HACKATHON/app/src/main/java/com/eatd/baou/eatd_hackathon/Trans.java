package com.eatd.baou.eatd_hackathon;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Created by TeddyYin on 2017/7/16.
 */

public class Trans extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans);
        Button BRTButton = (Button)findViewById(R.id.BRTButton);
        Button BUSButton = (Button)findViewById(R.id.BUSButton);
        Button TransButton = (Button)findViewById(R.id.TransButton);
        BRTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webviewsetting("http://e-bus.chiayi.gov.tw/Home/RouteMap?rid=560&sec=0");
            }
        });
        BUSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webviewsetting("http://taiwanbus.tw/Route.aspx?bus=%E5%98%89%E7%BE%A9%E5%AE%A2%E9%81%8B&Lang=");
            }
        });
        TransButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webviewsetting("http://61.60.38.53/ATIS_CYC/");
            }
        });
    }
    private void init(){
        b = false;
        setContentView(R.layout.trans);
        Button BRTButton = (Button)findViewById(R.id.BRTButton);
        Button BUSButton = (Button)findViewById(R.id.BUSButton);
        Button TransButton = (Button)findViewById(R.id.TransButton);
    }
    private void webviewsetting(String website){
        b = true;
        setContentView(R.layout.webviewlayout);
        WebView web = (WebView)findViewById(R.id.webview1);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.loadUrl(website);
    }
    boolean b = false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && b) {
            init();
            return true;
        }

        return super.onKeyDown(keyCode, event);

    }
}
