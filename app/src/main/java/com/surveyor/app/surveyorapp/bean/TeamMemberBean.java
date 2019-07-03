package com.surveyor.app.surveyorapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DELL on 10-Dec-18.
 */

public class TeamMemberBean implements Parcelable {

    private ArrayList<TeamMemberDataBean> data;

    private String msg;

    private String success;

    protected TeamMemberBean(Parcel in) {
        data = in.createTypedArrayList(TeamMemberDataBean.CREATOR);
        msg = in.readString();
        success = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
        dest.writeString(msg);
        dest.writeString(success);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeamMemberBean> CREATOR = new Creator<TeamMemberBean>() {
        @Override
        public TeamMemberBean createFromParcel(Parcel in) {
            return new TeamMemberBean(in);
        }

        @Override
        public TeamMemberBean[] newArray(int size) {
            return new TeamMemberBean[size];
        }
    };

    public ArrayList<TeamMemberDataBean> getData() {
        return data;
    }

    public void setData(ArrayList<TeamMemberDataBean> data) {
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


}
