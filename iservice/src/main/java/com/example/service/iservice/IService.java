package com.example.service.iservice;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created By HanTe
 * 实例化AIDL的Stub类(Binder的子类)
 */

class IService extends IService_AIDL.Stub {

    private static final String TAG = "IService";
    private static final int CODE = 1;
    RemoteCallbackList<IService_CallBack> mRemoteCallbackList = new RemoteCallbackList<>();
    private Lock mLock = new ReentrantLock();
    List<Book> mBooks = new ArrayList<>();
    @Override
    public List<Book> getBooks () throws RemoteException {
        Book book = new Book();
        book.setName("Android 开发与探索发现");
        book.setPrice(88);
        mBooks.add(book);
        return mBooks;
    }

    @Override
    public void successCallBack (IService_CallBack callback) throws RemoteException {
        mRemoteCallbackList.register(callback);
    }

    @Override
    public void unRegisterCallBack (IService_CallBack callback) throws RemoteException {
        mRemoteCallbackList.unregister(callback);
    }

    @Override
    public void start (final String name, int phoneNum) throws RemoteException {
        new Thread(new Runnable() {
            @Override
            public void run () {
                remoteSucCallBackList(name);
            }
        }).start();
       new Thread(new Runnable() {
           @Override
           public void run () {
               remoteFailCallBackList(CODE, "春城无处不飞花");
           }
       }).start();
    }

    @Override
    public int result (int a, int b) throws RemoteException {
        Log.d(TAG, "result: " + a + " ; " + b);
        return a + b + 140;
    }

    @Override
    public void stop () throws RemoteException {

    }

    @Override
    public void destroy () throws RemoteException {

    }


    private synchronized void remoteSucCallBackList (String msg){
        mLock.lock();
        int i = mRemoteCallbackList.beginBroadcast();
        while(i > 0){
            i --;
            try {
                for(int j = 0; j < 20 ; j++) {
                    mRemoteCallbackList.getBroadcastItem(i).onSuccess(msg);// 通知回调
                }
           } catch(RemoteException e) {
               e.printStackTrace();
           }
       }
        try {
            Thread.sleep(10000);
          mRemoteCallbackList.finishBroadcast();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
       mLock.unlock();
    }

    private synchronized void remoteFailCallBackList (int code, String msg){
        mLock.lock();
        int i = mRemoteCallbackList.beginBroadcast();
        while(i > 0){
            i --;
            try {
                mRemoteCallbackList.getBroadcastItem(i).onFailed(code, msg);
            } catch(RemoteException e) {
                e.printStackTrace();
            }
        }
        mRemoteCallbackList.finishBroadcast();
        mLock.unlock();
    }
}
