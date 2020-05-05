package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class A implements Parcelable {
    private String name;
    private B b;//实现Serializable接口
    private C c;//实现Parcelable接口

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeSerializable(this.b);
        dest.writeParcelable(this.c, flags);
    }

    public A() {}

    protected A(Parcel in) {
        this.name = in.readString();
        this.b = (B) in.readSerializable();
        //c是另一个序列化对象，此方法序列需要该类的上下文类加载器
        this.c = in.readParcelable(C.class.getClassLoader());
    }

    public static final Creator<A> CREATOR = new Creator<A>() {
        @Override
        public A createFromParcel(Parcel source) {
            return new A(source);
        }

        @Override
        public A[] newArray(int size) {
            return new A[size];
        }
    };
}
