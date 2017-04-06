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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.service.iservice.Book;
import com.example.service.iservice.IService_AIDL;
import com.example.service.iservice.IService_CallBack;
import com.example.service.iservice.MyIService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private List<Book> mBooks = new ArrayList<>();
    private IService_AIDL iService_aidl;
    private TextView show_text;
    private Button button;

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
            Log.d(TAG, "onSuccess: " + message);
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
                int result = iService_aidl.result(11, 99);
                for(Book book : mBooks) {
                    Log.d(TAG, "onServiceConnected: result 结果" + result + " | "
                            + book.toString());
                }
                for(int i = 0; i < 20; i++) {
                    iService_aidl.start("清明时节雨纷纷", 66666);
                }
            } catch(RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected (ComponentName name) {
            try {
                iService_aidl.unRegisterCallBack(mCallBack);
            } catch(RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private void initView () {
        show_text = (TextView) findViewById(R.id.show_text);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    protected void onStop () {
        super.onStop();
        unbindService(mServiceConnection); // 解绑
    }

    @Override
    public void onClick (View v) {
        switch(v.getId()) {
            case R.id.button:
                Intent intent = new Intent(this, MessengerServiceActivity.class);
                startActivity(intent);
                break;
        }
    }
}
