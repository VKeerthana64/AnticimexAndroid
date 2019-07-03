package com.surveyor.app.surveyorapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class JobAllSyncAssigneduserBean implements Parcelable {

    private String username;
    private String userid;

    protected JobAllSyncAssigneduserBean(Parcel in) {
        username = in.readString();
        userid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(userid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JobAllSyncAssigneduserBean> CREATOR = new Creator<JobAllSyncAssigneduserBean>() {
        @Override
        public JobAllSyncAssigneduserBean createFromParcel(Parcel in) {
            return new JobAllSyncAssigneduserBean(in);
        }

        @Override
        public JobAllSyncAssigneduserBean[] newArray(int size) {
            return new JobAllSyncAssigneduserBean[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
