// IService_AIDL.aidl
package com.example.service.iservice;

//  注意 ： 需要自己加上 import
import com.example.service.iservice.IService_CallBack;
import com.example.service.iservice.Book;
/**
* 接口暴露类
* 供外部调用 的 接口
* */

interface IService_AIDL {

// ---- 页面 获取 远程Service 返回值
    List<Book> getBooks();

    // 成功 回调  用来向页面传递 信息
    void successCallBack (IService_CallBack callback);
    // 注销回调
    void unRegisterCallBack (IService_CallBack callback);

//-----  页面 向 AIDL 传递参数
    void start(String name, int phoneNum);

    int result (int a, int b);

    void stop();

    void destroy();
}
