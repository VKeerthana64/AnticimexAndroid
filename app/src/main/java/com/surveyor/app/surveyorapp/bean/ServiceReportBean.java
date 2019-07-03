package com.surveyor.app.surveyorapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DELL on 09-Dec-18.
 */

public class ServiceReportBean {

    private ArrayList<Data> data;
    private String msg;
    private String success;

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
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

    public class Data implements Parcelable {
        private ArrayList<ImageListBean> imageslist;

        private String neajobid;

        private String postalcode;

        private String feedbacksubstantiated;

        private String contractordateresolved;

        private String constituency;

        private String noofdefects;

        private String bincenter;

        private String remarks;

        private String geoaddress;

        private String time_of_finding;

        private String id;

        private String probablecauseofburrows;

        private String landowner;

        private String findings;

        private String habitate;

        private String actiontaken;

        private String longitude;

        private String noofburrows;

        private String latitude;

        private String jo_scheduleid;

        private String towncouncil;

        private String date_of_finding;
        private String isdraft;

        public Data(Parcel in) {

            imageslist = in.createTypedArrayList(ImageListBean.CREATOR);
            neajobid = in.readString();
            postalcode = in.readString();
            feedbacksubstantiated = in.readString();
            contractordateresolved = in.readString();
            constituency = in.readString();
            noofdefects = in.readString();
            bincenter = in.readString();
            remarks = in.readString();
            geoaddress = in.readString();
            time_of_finding = in.readString();
            id = in.readString();
            probablecauseofburrows = in.readString();
            landowner = in.readString();
            findings = in.readString();
            habitate = in.readString();
            actiontaken = in.readString();
            longitude = in.readString();
            noofburrows = in.readString();
            latitude = in.readString();
            jo_scheduleid = in.readString();
            towncouncil = in.readString();
            date_of_finding = in.readString();
            isdraft = in.readString();
        }


        public  final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        public ArrayList<ImageListBean> getImageslist() {
            return imageslist;
        }

        public void setImageslist(ArrayList<ImageListBean> imageslist) {
            this.imageslist = imageslist;
        }

        public String getIsdraft() {
            return isdraft;
        }

        public void setIsdraft(String isdraft) {
            this.isdraft = isdraft;
        }

        public String getNeajobid() {
            return neajobid;
        }

        public void setNeajobid(String neajobid) {
            this.neajobid = neajobid;
        }

        public String getPostalcode() {
            return postalcode;
        }

        public void setPostalcode(String postalcode) {
            this.postalcode = postalcode;
        }

        public String getFeedbacksubstantiated() {
            return feedbacksubstantiated;
        }

        public void setFeedbacksubstantiated(String feedbacksubstantiated) {
            this.feedbacksubstantiated = feedbacksubstantiated;
        }

        public String getContractordateresolved() {
            return contractordateresolved;
        }

        public void setContractordateresolved(String contractordateresolved) {
            this.contractordateresolved = contractordateresolved;
        }

        public String getConstituency() {
            return constituency;
        }

        public void setConstituency(String constituency) {
            this.constituency = constituency;
        }

        public String getNoofdefects() {
            return noofdefects;
        }

        public void setNoofdefects(String noofdefects) {
            this.noofdefects = noofdefects;
        }

        public String getBincenter() {
            return bincenter;
        }

        public void setBincenter(String bincenter) {
            this.bincenter = bincenter;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getGeoaddress() {
            return geoaddress;
        }

        public void setGeoaddress(String geoaddress) {
            this.geoaddress = geoaddress;
        }

        public String getTime_of_finding() {
            return time_of_finding;
        }

        public void setTime_of_finding(String time_of_finding) {
            this.time_of_finding = time_of_finding;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProbablecauseofburrows() {
            return probablecauseofburrows;
        }

        public void setProbablecauseofburrows(String probablecauseofburrows) {
            this.probablecauseofburrows = probablecauseofburrows;
        }

        public String getLandowner() {
            return landowner;
        }

        public void setLandowner(String landowner) {
            this.landowner = landowner;
        }

        public String getFindings() {
            return findings;
        }

        public void setFindings(String findings) {
            this.findings = findings;
        }

        public String getHabitate() {
            return habitate;
        }

        public void setHabitate(String habitate) {
            this.habitate = habitate;
        }

        public String getActiontaken() {
            return actiontaken;
        }

        public void setActiontaken(String actiontaken) {
            this.actiontaken = actiontaken;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getNoofburrows() {
            return noofburrows;
        }

        public void setNoofburrows(String noofburrows) {
            this.noofburrows = noofburrows;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getJo_scheduleid() {
            return jo_scheduleid;
        }

        public void setJo_scheduleid(String jo_scheduleid) {
            this.jo_scheduleid = jo_scheduleid;
        }

        public String getTowncouncil() {
            return towncouncil;
        }

        public void setTowncouncil(String towncouncil) {
            this.towncouncil = towncouncil;
        }

        public String getDate_of_finding() {
            return date_of_finding;
        }

        public void setDate_of_finding(String date_of_finding) {
            this.date_of_finding = date_of_finding;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeTypedList(imageslist);
            dest.writeString(neajobid);
            dest.writeString(postalcode);
            dest.writeString(feedbacksubstantiated);
            dest.writeString(contractordateresolved);
            dest.writeString(constituency);
            dest.writeString(noofdefects);
            dest.writeString(bincenter);
            dest.writeString(remarks);
            dest.writeString(geoaddress);
            dest.writeString(time_of_finding);
            dest.writeString(id);
            dest.writeString(probablecauseofburrows);
            dest.writeString(landowner);
            dest.writeString(findings);
            dest.writeString(habitate);
            dest.writeString(actiontaken);
            dest.writeString(longitude);
            dest.writeString(noofburrows);
            dest.writeString(latitude);
            dest.writeString(jo_scheduleid);
            dest.writeString(towncouncil);
            dest.writeString(date_of_finding);
            dest.writeString(isdraft);
        }
    }

    public static class ImageListBean implements Parcelable{
        String id;
        String imagename;
        String fullpath;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImagename() {
            return imagename;
        }

        public void setImagename(String imagename) {
            this.imagename = imagename;
        }

        public String getFullpath() {
            return fullpath;
        }

        public void setFullpath(String fullpath) {
            this.fullpath = fullpath;
        }

        protected ImageListBean(Parcel in) {
            id = in.readString();
            imagename = in.readString();
            fullpath = in.readString();
        }

        public static final Creator<ImageListBean> CREATOR = new Creator<ImageListBean>() {
            @Override
            public ImageListBean createFromParcel(Parcel in) {
                return new ImageListBean(in);
            }

            @Override
            public ImageListBean[] newArray(int size) {
                return new ImageListBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(imagename);
            dest.writeString(fullpath);
        }
    }
}
