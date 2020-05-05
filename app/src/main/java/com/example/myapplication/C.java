package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class C implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public C() {
    }

    protected C(Parcel in) {
    }

    public static final Creator<C> CREATOR = new Creator<C>() {
        @Override
        public C createFromParcel(Parcel source) {
            return new C(source);
        }

        @Override
        public C[] newArray(int size) {
            return new C[size];
        }
    };
}
