package com.example.service.iservice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created By HanTe
 */

public class Book implements Parcelable{

    private String name;
    private int price;

    @Override
    public String toString () {
        return "Book{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public int getPrice () {
        return price;
    }

    public void setPrice (int price) {
        this.price = price;
    }

    public Book () {
    }

    protected Book (Parcel in) {
        name = in.readString();
        price = in.readInt();
    }



    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel (Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray (int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(price);
    }

    /**
     * 自己创建 读取 方法
     * @param parcel 存储与 传输 数据
     */
    public void readFromParcel (Parcel parcel){
        // 顺序 同 writeToParcel  一致
        name = parcel.readString();
        price = parcel.readInt();
    }
}
