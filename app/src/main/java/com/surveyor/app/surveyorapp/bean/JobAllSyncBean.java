package com.surveyor.app.surveyorapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DELL on 21-Jan-19.
 */

public class JobAllSyncBean implements Parcelable {

    private ArrayList<JobAllSyncDataBean> data;
    private String msg;
    private String success;

    protected JobAllSyncBean(Parcel in) {
        data = in.createTypedArrayList(JobAllSyncDataBean.CREATOR);
        msg = in.readString();
        success = in.readString();
    }

    public static final Creator<JobAllSyncBean> CREATOR = new Creator<JobAllSyncBean>() {
        @Override
        public JobAllSyncBean createFromParcel(Parcel in) {
            return new JobAllSyncBean(in);
        }

        @Override
        public JobAllSyncBean[] newArray(int size) {
            return new JobAllSyncBean[size];
        }
    };

    public ArrayList<JobAllSyncDataBean> getData() {
        return data;
    }

    public void setData(ArrayList<JobAllSyncDataBean> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(data);
        parcel.writeString(msg);
        parcel.writeString(success);
    }
}
