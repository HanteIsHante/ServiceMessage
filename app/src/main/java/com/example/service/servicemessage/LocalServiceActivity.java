package com.example.service.servicemessage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.example.service.servicemessage.service.LocalServiceMessenger;

public class LocalServiceActivity extends AppCompatActivity {

    private LocalServiceMessenger mLocalServiceMessenger;// 用来调用 LocalService 中 的方法
    private boolean mBound = false;
    private ServiceConnection mLocalService = new ServiceConnection() {
        @Override
        public void onServiceConnected (ComponentName name, IBinder service) {
            mBound = true;
            LocalServiceMessenger.LocalBinder binder  = (LocalServiceMessenger.LocalBinder)
                    service;
            mLocalServiceMessenger = binder.getLocalService();
        }

        @Override
        public void onServiceDisconnected (ComponentName name) {
            mBound = false;
        }
    };
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_service);


    }

    private void bindService (){
        Intent bindIntent = new Intent(this, LocalServiceMessenger.class);
        bindService(bindIntent, mLocalService, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        if(mBound) {
            unbindService(mLocalService);
            mBound = false;
        }
    }
}
