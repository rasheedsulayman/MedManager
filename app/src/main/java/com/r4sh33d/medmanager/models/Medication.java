package com.r4sh33d.medmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Medication  implements Parcelable {
     public  String name;
     public  String description;
     public String quantity;
     public long startTime;
     public long endTime;
     public  long interval;
     public  long dbRowId;


    public Medication(String name, String description, String quantity, long startTime, long endTime, long interval) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.interval = interval;
    }

    protected Medication(Parcel in) {
        name = in.readString();
        description = in.readString();
        quantity = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
        interval = in.readLong();
        dbRowId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(quantity);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeLong(interval);
        dest.writeLong(dbRowId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Medication> CREATOR = new Creator<Medication>() {
        @Override
        public Medication createFromParcel(Parcel in) {
            return new Medication(in);
        }

        @Override
        public Medication[] newArray(int size) {
            return new Medication[size];
        }
    };
}
