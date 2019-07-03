package com.surveyor.app.surveyorapp.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class JobAllSyncImageslistBean implements Parcelable {

    private String id;
    private String fullpath;
    private String imagename;
    Bitmap bitmap ;
    boolean isFromLocal ;

    public JobAllSyncImageslistBean(String imagename, Bitmap bitmap) {
        this.imagename = imagename;
        this.bitmap = bitmap;
    }

    public JobAllSyncImageslistBean(String fullpath, String imagename, boolean isFromLocal) {
        this.fullpath = fullpath;
        this.imagename = imagename;
        this.isFromLocal = isFromLocal;
    }

    protected JobAllSyncImageslistBean(Parcel in) {
        id = in.readString();
        fullpath = in.readString();
        imagename = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        isFromLocal = in.readByte() != 0;
    }

    public static final Creator<JobAllSyncImageslistBean> CREATOR = new Creator<JobAllSyncImageslistBean>() {
        @Override
        public JobAllSyncImageslistBean createFromParcel(Parcel in) {
            return new JobAllSyncImageslistBean(in);
        }

        @Override
        public JobAllSyncImageslistBean[] newArray(int size) {
            return new JobAllSyncImageslistBean[size];
        }
    };

    public boolean isFromLocal() {
        return isFromLocal;
    }

    public void setFromLocal(boolean fromLocal) {
        isFromLocal = fromLocal;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullpath() {
        return fullpath;
    }

    public void setFullpath(String fullpath) {
        this.fullpath = fullpath;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(fullpath);
        parcel.writeString(imagename);
        parcel.writeParcelable(bitmap, i);
        parcel.writeByte((byte) (isFromLocal ? 1 : 0));
    }
}
