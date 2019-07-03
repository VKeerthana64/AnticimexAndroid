package com.surveyor.app.surveyorapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class JobAllSyncTeamBean implements Parcelable {

    private String id;

    private String teamname;

    protected JobAllSyncTeamBean(Parcel in) {
        id = in.readString();
        teamname = in.readString();
    }

    public static final Creator<JobAllSyncTeamBean> CREATOR = new Creator<JobAllSyncTeamBean>() {
        @Override
        public JobAllSyncTeamBean createFromParcel(Parcel in) {
            return new JobAllSyncTeamBean(in);
        }

        @Override
        public JobAllSyncTeamBean[] newArray(int size) {
            return new JobAllSyncTeamBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(teamname);
    }
}
