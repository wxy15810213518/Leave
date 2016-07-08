package com.xhly.leave.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.xhly.leave.R;

public class SplashActivity extends Activity {
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler.sendEmptyMessageDelayed(0,2000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

   /* public void teacherC(View v) {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }


    public void studentC(View v) {
        startActivity(new Intent(SplashActivity.this, LeaveRecordActivity.class));
        finish();
    }*/

}
