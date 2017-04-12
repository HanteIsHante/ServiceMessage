package com.example.service.servicemessage.jobservice;


import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Message;

/**
 * API 21 ( Android 5.0，即Lollipop )中 才出现的JobService
 * 这个job service运行在你的主线程，这意味着你需要使用子线程，handler
 */

@SuppressLint("NewApi")
public class MyJobSchedule extends JobService {


    // 用来执行耗时操作
    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage (Message msg) {
            // do something
            switch(msg.what){
                case 1:

                    break;
                case 2:

                    break;
            }
            // 任务执行完毕调用finish， 通知系统任务结束(必须调用)
            jobFinished((JobParameters) msg.obj, false);
            return true;
        }
    });

    @Override
    public boolean onStartJob (JobParameters params) {
        myHandler.sendMessage(Message.obtain(myHandler, 1, params));
        return true;
    }

    /**
     * 返回值为false时：表示这个Job结束，且不会被重新调用。
     * 返回值为true时，表示这个Job结束，但是会重新请求，会在之后再次执行。
     */
    @Override
    public boolean onStopJob (JobParameters params) {
        myHandler.removeMessages(1);
        return false;
    }
}
