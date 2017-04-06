package com.example.service.servicemessage;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.service.iservice.Book;
import com.example.service.iservice.IService_AIDL;
import com.example.service.iservice.IService_CallBack;
import com.example.service.iservice.MyIService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private List<Book> mBooks = new ArrayList<>();
    private IService_AIDL iService_aidl;
    private TextView show_text;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindRemoteService();
        initView();
    }


    /**
     * 绑定远程服务
     */
    private void bindRemoteService () {
        Intent intent = new Intent(getApplicationContext(),
                MyIService.class);// 需 先将iservice Module 作为app 的 引用库
        this.bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    //  接收 远程端发送信息
    private IService_CallBack.Stub mCallBack = new IService_CallBack.Stub() {
        @Override
        public void onSuccess (String message) throws RemoteException {
            show_text.setText(message);
        }

        @Override
        public void onFailed (int code, String meg) throws RemoteException {

        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected (ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ServiceConnected ");
            try {
                iService_aidl = IService_AIDL.Stub.asInterface(service);
                iService_aidl.successCallBack(mCallBack);
                mBooks = iService_aidl.getBooks();
                for(Book book : mBooks) {
                    Log.d(TAG, "onServiceConnected: " + book.toString());
                }
                iService_aidl.start("可口可乐", 111);
            } catch(RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected (ComponentName name) {
            try {
                iService_aidl.failedCallBack(mCallBack);
            } catch(RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private void initView () {
        show_text = (TextView) findViewById(R.id.show_text);
    }
}
