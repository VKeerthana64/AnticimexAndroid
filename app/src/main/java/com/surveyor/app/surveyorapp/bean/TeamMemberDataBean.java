package com.surveyor.app.surveyorapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DELL on 10-Dec-18.
 */

public class TeamMemberDataBean implements Parcelable {

    private String id;
    private String username;
    private String lastname;
    private String userrole;
    private String firstname;
    private String profileimage;
    private boolean assigned;
    private int isSync;

    public TeamMemberDataBean() {
    }

    protected TeamMemberDataBean(Parcel in) {
        id = in.readString();
        username = in.readString();
        lastname = in.readString();
        userrole = in.readString();
        firstname = in.readString();
        profileimage = in.readString();
        assigned = in.readByte() != 0;
        isSync = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(lastname);
        dest.writeString(userrole);
        dest.writeString(firstname);
        dest.writeString(profileimage);
        dest.writeByte((byte) (assigned ? 1 : 0));
        dest.writeInt(isSync);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeamMemberDataBean> CREATOR = new Creator<TeamMemberDataBean>() {
        @Override
        public TeamMemberDataBean createFromParcel(Parcel in) {
            return new TeamMemberDataBean(in);
        }

        @Override
        public TeamMemberDataBean[] newArray(int size) {
            return new TeamMemberDataBean[size];
        }
    };

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUserrole() {
        return userrole;
    }

    public void setUserrole(String userrole) {
        this.userrole = userrole;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }


}
