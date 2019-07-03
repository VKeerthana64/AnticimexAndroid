package com.surveyor.app.surveyorapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class JobAllSyncAllSurveyBean implements Parcelable {

    private ArrayList<JobAllSyncImageslistBean> imageslist;
    private String postalcode;
    private String feedbacksubstantiated;
    private String bincenter;
    private String remarks;
    private String isdraft;
    private String geoaddress;
    private String id;
    private String findings;
    private String habitate;
    private String imagefile;
    private String noofburrows;
    private String noofactiveburrows;
    private String noofnonactiveburrows;
    private String noofbinchute;
    private String noofcrc;
    private String longitude;
    private String neajobid;
    private String constituency;
    private String contractordateresolved;
    private String noofdefects;
    private String locationremarks;
    private String time_of_finding;
    private String probablecauseofburrows;
    private String landowner;
    private String actiontaken;
    private String latitude;
    private String jo_scheduleid;
    private String date_of_finding;
    private String towncouncil;
    private String iscomplete;

    int isSync ;

    public JobAllSyncAllSurveyBean() {
    }

    protected JobAllSyncAllSurveyBean(Parcel in) {
        imageslist = in.createTypedArrayList(JobAllSyncImageslistBean.CREATOR);
        postalcode = in.readString();
        feedbacksubstantiated = in.readString();
        bincenter = in.readString();
        remarks = in.readString();
        isdraft = in.readString();
        iscomplete = in.readString();
        geoaddress = in.readString();
        id = in.readString();
        findings = in.readString();
        habitate = in.readString();
        imagefile = in.readString();
        noofburrows = in.readString();
        noofactiveburrows = in.readString();
        noofnonactiveburrows = in.readString();
        noofbinchute = in.readString();
        noofcrc = in.readString();
        longitude = in.readString();
        neajobid = in.readString();
        constituency = in.readString();
        contractordateresolved = in.readString();
        noofdefects = in.readString();
        locationremarks = in.readString();
        time_of_finding = in.readString();
        probablecauseofburrows = in.readString();
        landowner = in.readString();
        actiontaken = in.readString();
        latitude = in.readString();
        jo_scheduleid = in.readString();
        date_of_finding = in.readString();
        towncouncil = in.readString();
        isSync = in.readInt();
    }




    public static final Creator<JobAllSyncAllSurveyBean> CREATOR = new Creator<JobAllSyncAllSurveyBean>() {
        @Override
        public JobAllSyncAllSurveyBean createFromParcel(Parcel in) {
            return new JobAllSyncAllSurveyBean(in);
        }

        @Override
        public JobAllSyncAllSurveyBean[] newArray(int size) {
            return new JobAllSyncAllSurveyBean[size];
        }
    };

    public String getIscomplete() {
        return iscomplete;
    }

    public void setIscomplete(String iscomplete) {
        this.iscomplete = iscomplete;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public ArrayList<JobAllSyncImageslistBean> getImageslist() {
        return imageslist;
    }

    public void setImageslist(ArrayList<JobAllSyncImageslistBean> imageslist) {
        this.imageslist = imageslist;
    }

    public String getNoofbinchute() {
        return noofbinchute;
    }

    public void setNoofbinchute(String noofbinchute) {
        this.noofbinchute = noofbinchute;
    }

    public String getNoofcrc() {
        return noofcrc;
    }

    public void setNoofcrc(String noofcrc) {
        this.noofcrc = noofcrc;
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

    public String getIsdraft() {
        return isdraft;
    }

    public void setIsdraft(String isdraft) {
        this.isdraft = isdraft;
    }

    public String getGeoaddress() {
        return geoaddress;
    }

    public void setGeoaddress(String geoaddress) {
        this.geoaddress = geoaddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImagefile() {
        return imagefile;
    }

    public void setImagefile(String imagefile) {
        this.imagefile = imagefile;
    }

    public String getNoofburrows() {
        return noofburrows;
    }

    public void setNoofburrows(String noofburrows) {
        this.noofburrows = noofburrows;
    }

    public String getNoofactiveburrows() {
        return noofactiveburrows;
    }

    public void setNoofactiveburrows(String noofactiveburrows) {
        this.noofactiveburrows = noofactiveburrows;
    }
    public String getNoofnonactiveburrows() {
        return noofnonactiveburrows;
    }

    public void setNoofnonactiveburrows(String noofnonactiveburrows) {
        this.noofnonactiveburrows = noofnonactiveburrows;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNeajobid() {
        return neajobid;
    }

    public void setNeajobid(String neajobid) {
        this.neajobid = neajobid;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

    public String getContractordateresolved() {
        return contractordateresolved;
    }

    public void setContractordateresolved(String contractordateresolved) {
        this.contractordateresolved = contractordateresolved;
    }

    public String getNoofdefects() {
        return noofdefects;
    }

    public void setNoofdefects(String noofdefects) {
        this.noofdefects = noofdefects;
    }

    public String getLocationremarks() {
        return locationremarks;
    }

    public void setLocationremarks(String locationremarks) {
        this.locationremarks = locationremarks;
    }

    public String getTime_of_finding() {
        return time_of_finding;
    }

    public void setTime_of_finding(String time_of_finding) {
        this.time_of_finding = time_of_finding;
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

    public String getActiontaken() {
        return actiontaken;
    }

    public void setActiontaken(String actiontaken) {
        this.actiontaken = actiontaken;
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

    public String getDate_of_finding() {
        return date_of_finding;
    }

    public void setDate_of_finding(String date_of_finding) {
        this.date_of_finding = date_of_finding;
    }

    public String getTowncouncil() {
        return towncouncil;
    }

    public void setTowncouncil(String towncouncil) {
        this.towncouncil = towncouncil;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(imageslist);
        parcel.writeString(postalcode);
        parcel.writeString(feedbacksubstantiated);
        parcel.writeString(bincenter);
        parcel.writeString(remarks);
        parcel.writeString(isdraft);
        parcel.writeString(iscomplete);
        parcel.writeString(geoaddress);
        parcel.writeString(id);
        parcel.writeString(findings);
        parcel.writeString(habitate);
        parcel.writeString(imagefile);
        parcel.writeString(noofburrows);
        parcel.writeString(noofactiveburrows);
        parcel.writeString(noofnonactiveburrows);
        parcel.writeString(noofbinchute);
        parcel.writeString(noofcrc);
        parcel.writeString(longitude);
        parcel.writeString(neajobid);
        parcel.writeString(constituency);
        parcel.writeString(contractordateresolved);
        parcel.writeString(noofdefects);
        parcel.writeString(locationremarks);
        parcel.writeString(time_of_finding);
        parcel.writeString(probablecauseofburrows);
        parcel.writeString(landowner);
        parcel.writeString(actiontaken);
        parcel.writeString(latitude);
        parcel.writeString(jo_scheduleid);
        parcel.writeString(date_of_finding);
        parcel.writeString(towncouncil);
        parcel.writeInt(isSync);
    }
}
