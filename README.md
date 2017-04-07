# ServiceMessage
Service  相关属性


[绑定Service通信](https://developer.android.com/guide/components/bound-services.html?hl=zh-cn)

##### Service的通信方式：


- 通过BroadCastReceiver：这种方式是最简单的，只能用来交换简单的数据；
 
- 通过Messager：这种方式是通过一个传递一个Messager给对方，通过这个它来发送Message对象。这种方式只能单向传递数据。可以是Service到Activity，也可以是从Activity发送数据给Service。一个Messeger不能同时双向发送；
 
- 通过Binder来实现远程调用(IPC)：这种方式是Android的最大特色之一，让你调用远程Service的接口，就像调用本地对象一样，实现非常灵活，写起来也相对复杂。




##### 跨进程通信

1.AIDL 方式

2.MessengerService 方式

##### 同一进程

1.普通绑定Service


### Messenger与AIDL的异同

> Messenger的底层也是用AIDL实现的，但用起来还是有些不同的，这里总结了几点区别：

1. Messenger本质也是AIDL，只是进行了封装，开发的时候不用再写.aidl文件。

结合我自身的使用，因为不用去写.aidl文件，相比起来，Messenger使用起来十分简单。但前面也说了，Messenger本质上也是AIDL，故在底层进程间通信这一块，两者的效率应该是一样的。

2. 在service端，Messenger处理client端的请求是单线程的，而AIDL是多线程的。

使用AIDL的时候，service端每收到一个client端的请求时，就在BInder线程池中取一个线程去执行相应的操作。而Messenger，service收到的请求是放在Handler的MessageQueue里面，Handler大家都用过，它需要绑定一个Thread，然后不断poll message执行相关操作，这个过程是同步执行的。

3. client的方法，使用AIDL获取返回值是同步的，而Messenger是异步的。

Messenger只提供了一个方法进行进程间通信，就是send(Message msg)方法，发送的是一个Message，没有返回值，要拿到返回值，需要把client的Messenger作为msg.replyTo参数传递过去，service端处理完之后，在调用客户端的Messenger的send(Message msg)方法把返回值传递回client，这个过程是异步的，而AIDL你可以自己指定方法，指定返回值，它获取返回值是同步的（如果没有用oneway修饰方法的话）。

总的来说，AIDL灵活性更高，如果需要IPC通信的地方比较多，还是更推荐自定义AIDL一点。

### aidl

> aidl中有onway,in,out,inout关键字修饰

###### oneway

正常情况下Client调用AIDL接口方法时会阻塞，直到Server进程中该方法被执行完。oneway可以修饰AIDL文件里的方法，oneway修饰的方法在用户请求相应功能时不需要等待响应可直接调用返回，非阻塞效果，该关键字可以用来声明接口或者声明方法，如果接口声明中用到了oneway关键字，则该接口声明的所有方法都采用oneway方式。（注意,如果client和Server在同一进程中,oneway修饰的方法还是会阻塞），oneway只能针对某一个接口，不能直接定义方法。 方法必须是void类型的返回值类型

###### in

非基本数据类型和string的参数类型必须加参数修饰符,in的意思是只输入,既最终server端执行完后不会影响到参数对象


###### out

与in相反,out修饰的参数只能由server写入并传递到client,而client传入的值并不会传递到server，使用out修饰，如果参数是自定义了类型，必须实现parcelable接口，并且实现public void readFromParcel(Parcel in)方法

###### inout

被inout修饰的参数,既可以从client传递到server,也可以server传递到client，使用out修饰，如果参数是自定义了类型，必须实现Parcelable接口，并且实现public void readFromParcel(Parcel in)方法

> 如果aidl中使用了自定义类型，必须实现Parcelable接口，并新建一个和自定义类型名相同的aidl，内容就是parcelable 接口名称

##### aidl支持的文件类型
1. Java 编程语言中的基本类型, 如 int、long、boolean 等, 不需要 import.

2. String、List、Map 和 CharSequence, 不需要 import.

3. AIDL 生成的 interface, 需要 import, 同一个包中也需要导入, 传递的是引用.
　　如果定义的接口方法中有参数, 则需在前面加上 in, out 或 inout, 但是对于基本类型的参数, 默认就是 in, 并且不会是其他值.

4. Parcelable 接口的自定义类, 需要 import, 同一个包中也需要导入, 其传递的是值.

另外, 自定义类还需要用一个aidl文件来声明该类型.
如定义了一个类, 它的源代码在RoadLine.java中：
~~~
　　package com.communicate;
　　import android.os.Parcel;
　　import android.os.Parcelable;
　　public class RoadLine implements Parcelable {
~~~
需要在RoadLine.aidl文件中做如下声明： 
~~~
　　package com.communicate;
　　import com.communicate.RoadLine;
　　parcelable RoadLine;
~~~











