package com.example.service.servicemessage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.service.iservice.MessengerService;

import static com.example.service.iservice.MessengerService.MSG_SAY_HELLO;

public class MessengerServiceActivity extends AppCompatActivity {

    private static final String TAG = "MessengerServiceActivit";
    Messenger mMessenger;
    boolean mBound;

    private Messenger mRespMessenger = new Messenger(new Handler(){
        @Override
        public void handleMessage (Message msg) {
            switch(msg.what){
                case MSG_SAY_HELLO:
                    Log.d(TAG, "handleMessage: 接收返回" + msg.arg1 + " ; " + msg.arg2);
                    break;
            }
            super.handleMessage(msg);
        }
    });
    private ServiceConnection  mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected (ComponentName name, IBinder service) {
            mMessenger = new Messenger(service);
            SayHello ();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected (ComponentName name) {
            mBound = false;
            mMessenger = null;
        }
    };

    @Override
    protected void onStart () {
        super.onStart();
        bindService(new Intent(this, MessengerService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_service);
    }

    private void SayHello (){
        try {
            int arg0 = 1;
            int arg1 = 1;
            Message obtain = Message.obtain(null, MSG_SAY_HELLO, arg0, arg1);
            obtain.replyTo = mRespMessenger;
            mMessenger.send(obtain);// 发送到服务端
        } catch(RemoteException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStop () {
        super.onStop();
        if(mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
}
