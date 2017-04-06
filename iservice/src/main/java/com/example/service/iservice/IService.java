package com.example.service.iservice;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By HanTe
 * 实例化AIDL的Stub类(Binder的子类)
 */

class IService extends IService_AIDL.Stub {

    private static final String TAG = "IService";
    RemoteCallbackList<IService_CallBack> mRemoteCallbackList = new RemoteCallbackList<>();
    List<Book> mBooks = new ArrayList<>();
    @Override
    public List<Book> getBooks () throws RemoteException {
        Log.d(TAG, "getBooks: 获取 book集合");
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
    public void failedCallBack (IService_CallBack callback) throws RemoteException {
        mRemoteCallbackList.unregister(callback);
    }

    @Override
    public void start (String name, int phoneNum) throws RemoteException {
        remoteSucCallBackList(name);
    }

    @Override
    public int result (int a, int b) throws RemoteException {
        Log.d(TAG, "result: " + a + " ; " + b);
        return 0;
    }

    @Override
    public void stop () throws RemoteException {

    }

    @Override
    public void destroy () throws RemoteException {

    }


    private synchronized void remoteSucCallBackList (String msg){
        int i = mRemoteCallbackList.beginBroadcast();
        while(i > 0){
            i --;
            try {
                mRemoteCallbackList.getBroadcastItem(i).onSuccess(msg);// 通知回调
            } catch(RemoteException e) {
                e.printStackTrace();
            }
        }
        mRemoteCallbackList.finishBroadcast();
    }

    private synchronized void remoteFailCallBackList (int code, String msg){
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
    }
}
