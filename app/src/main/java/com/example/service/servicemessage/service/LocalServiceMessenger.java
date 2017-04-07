package com.example.service.servicemessage.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By HanTe
 */

public class LocalServiceMessenger extends Service {

    private LocalBinder mLocalBinder;

    public class LocalBinder extends Binder{
        public LocalServiceMessenger getLocalService(){
            return LocalServiceMessenger.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return this.mLocalBinder;
    }

    @Override
    public void onCreate () {
        super.onCreate();
        mLocalBinder = new LocalBinder();
    }

    public List<String> getList(){
        List<String> mList = new ArrayList<>();
        mList.add("A");
        return mList;
    }
}
