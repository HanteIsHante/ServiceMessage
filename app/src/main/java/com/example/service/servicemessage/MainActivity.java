package com.example.service.servicemessage;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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
import com.example.service.servicemessage.jobservice.MyJobSchedule;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private List<Book> mBooks = new ArrayList<>();
    private IService_AIDL iService_aidl;
    private TextView show_text;
    private Button button;
    private Button button_AIDL;
    private Button button_stop;
    private InComingHandlerMessage mHandlerMessage;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindRemoteService();
        mHandlerMessage = new InComingHandlerMessage(this);
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
        public void onSuccess (final String message) throws RemoteException {
            Log.d(TAG, "onSuccess: " + message);
            runOnUiThread(new TimerTask() {
                @Override
                public void run () {
                    show_text.setText(message);
                }
            });
        }

        @Override
        public void onFailed (int code, final String meg) throws RemoteException {
            Log.d(TAG, "onFailed: " + code + " ; " + meg);
            runOnUiThread(new TimerTask() {
                @Override
                public void run () {
                    show_text.setText(meg);
                }
            });
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
            } catch(RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected (ComponentName name) {
            try {
                if(iService_aidl != null) {
                    iService_aidl.unRegisterCallBack(mCallBack);
                    iService_aidl = null;
                }
            } catch(RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private void initView () {
        show_text = (TextView) findViewById(R.id.show_text);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        button_AIDL = (Button) findViewById(R.id.button_AIDL);
        button_AIDL.setOnClickListener(this);
        button_stop = (Button) findViewById(R.id.button_stop);
        button_stop.setOnClickListener(this);
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
            case R.id.button_AIDL:
                if(iService_aidl == null) {
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run () {
                        try {
                            iService_aidl.start("清明时节雨纷纷", 66666);
                        } catch(RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.button_stop:

                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    JobScheduler jobScheduler = (JobScheduler)getSystemService
                            (Context.JOB_SCHEDULER_SERVICE);
//                  第一个参数是你要运行的任务的标识符，第二个是这个Service组件的类名。
                    JobInfo.Builder builderInfo = new JobInfo.Builder(1,
                            new ComponentName(getPackageName(),
                            MyJobSchedule.class.getName()));
                    builderInfo.setPeriodic(3000);// 设置运行周期，每三秒执行一次
                    builderInfo.setPersisted(true); //通知系统系统当你的设备重启之后你的任务是否还要继续执行。
                    builderInfo.setRequiresCharging(true);//只有当设备在充电时这个任务才会被执行。
                    int schedule = jobScheduler.schedule(builderInfo.build());// 执行任务
                    if(schedule <= 0) {// schedule 应该返回的是 builder 的标识码
                        // schedule 如果 < 0, 则发生了错误
                        // do something
                        // 如果失败 取消 指定任务
                        jobScheduler.cancel(1);
                        // 取消所有任务
                        jobScheduler.cancelAll();
                    }
                }

                break;
        }
    }


    private static class InComingHandlerMessage extends Handler{

        private WeakReference<MainActivity> mActivityWeakReference;

        InComingHandlerMessage (MainActivity activityWeakReference) {
            this.mActivityWeakReference = new WeakReference<>(activityWeakReference);
        }

        @Override
        public void handleMessage (Message msg) {
            MainActivity mainActivity = mActivityWeakReference.get();
            if(mainActivity == null) {
                return;
            }
            Message m;
            switch(msg.what){
                case 1:
                    m = Message.obtain(this, 1);
                    sendMessage(m);
                    break;
                case 2:
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
