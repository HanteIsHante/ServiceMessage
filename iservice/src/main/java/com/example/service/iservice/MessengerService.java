package com.example.service.iservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created By HanTe
 */

public class MessengerService extends Service{

    private static final String TAG = "MessengerService";
    public static final int MSG_SAY_HELLO = 1;
//    服务实现一个 Handler，由其接收来自客户端的每个调用的回调

    private static class InComingHandler extends Handler{

        @Override
        public void handleMessage (Message msg) {

            Message obtain = Message.obtain(msg);// 返回客户端消息

//            服务在其 Handler 中（具体地讲，是在 handleMessage() 方法中）接收每个 Message。
            switch(msg.what){
                case MSG_SAY_HELLO:
                    try {
                        obtain.what = MSG_SAY_HELLO;
                        Log.d(TAG, "handleMessage: 客户端接收 " + msg.arg1 + " ; " + msg.arg2);
                        obtain.arg1 = msg.arg1 + msg.arg2;
                        obtain.arg2 = msg.arg2 + msg.arg2 + msg.arg1;
                        msg.replyTo.send(obtain);
                    } catch(RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
// Handler 用于创建 Messenger 对象（对 Handler 的引用）

    final Messenger mMessenger = new Messenger(new InComingHandler());

//Messenger 创建一个 IBinder，服务通过 onBind() 使其返回客户端
    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return mMessenger.getBinder();
    }
}
