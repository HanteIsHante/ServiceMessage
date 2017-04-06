// IService_CallBack.aidl
package com.example.service.iservice;

// 回调

interface IService_CallBack {


    void onSuccess (String message);

    void onFailed (int code, String meg);

}
