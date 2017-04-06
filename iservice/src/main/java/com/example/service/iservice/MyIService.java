package com.example.service.iservice;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


/**
 * Created By HanTe
 */

public class MyIService extends Service{


    private IService mIService_aidl = new IService();

    //在onBind()返回继承自Binder的Stub类型的Binder，非常重要
    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return mIService_aidl;
    }

    @Override
    public void onCreate () {
        super.onCreate();
        Book book = new Book();
        book.setName("阿里巴巴与四十大盗");
        book.setPrice(9999);
        mIService_aidl.mBooks.add(book);
    }

    //Service销毁的时候，需要清除掉mCallbacks中的所有的对象
    @Override
    public void onDestroy () {
        super.onDestroy();// 取消所有回调
        mIService_aidl.mRemoteCallbackList.kill();
    }
}
