package com.surveyor.app.surveyorapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DELL on 29-Nov-18.
 */

public class JobBean {

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

    public class Data implements Parcelable{

        private ArrayList<Teams> teams;
        private String deadline;
        private String customername;
        private String customerid;
        private String joborder;
        private String joborderid;
        private String location;
        private String locationid;
        private String scheduledate;
        private String remarks;
        private String jobtypeid;
        private String jobtypename;
        private String towncouncil;
        private String josurveytypeid;
        private String refno;
        private String constituency;
        private String deadlinedate;
        private String towncouncilid;
        private String constituencyid;
        private String jo_scheduleid;
        private String classification;
        private String iscomplete;
        private String formId;
        private String zone;
        private String division;
        private String geoaddress;


        protected Data(Parcel in) {
            formId = in.readString();
            deadline = in.readString();
            customername = in.readString();
            customerid = in.readString();
            joborder = in.readString();
            joborderid = in.readString();
            location = in.readString();
            locationid = in.readString();
            scheduledate = in.readString();
            remarks = in.readString();
            jobtypeid = in.readString();
            jobtypename = in.readString();
            towncouncil = in.readString();
            josurveytypeid = in.readString();
            refno = in.readString();
            constituency = in.readString();
            deadlinedate = in.readString();
            towncouncilid = in.readString();
            constituencyid = in.readString();
            jo_scheduleid = in.readString();
            classification = in.readString();
            iscomplete = in.readString();
            zone = in.readString();
            division = in.readString();
            geoaddress = in.readString();
        }

        public final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };


        public String getGeoaddress() {
            return geoaddress;
        }

        public void setGeoaddress(String geoaddress) {
            this.geoaddress = geoaddress;
        }

        public String getDivision() {
            return division;
        }

        public void setDivision(String division) {
            this.division = division;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public String getIscomplete() {
            return iscomplete;
        }

        public void setIscomplete(String iscomplete) {
            this.iscomplete = iscomplete;
        }

        public ArrayList<Teams> getTeams() {
            return teams;
        }

        public void setTeams(ArrayList<Teams> teams) {
            this.teams = teams;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public String getCustomername() {
            return customername;
        }

        public void setCustomername(String customername) {
            this.customername = customername;
        }

        public String getCustomerid() {
            return customerid;
        }

        public void setCustomerid(String customerid) {
            this.customerid = customerid;
        }

        public String getJoborder() {
            return joborder;
        }

        public void setJoborder(String joborder) {
            this.joborder = joborder;
        }

        public String getJoborderid() {
            return joborderid;
        }

        public void setJoborderid(String joborderid) {
            this.joborderid = joborderid;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLocationid() {
            return locationid;
        }

        public void setLocationid(String locationid) {
            this.locationid = locationid;
        }

        public String getScheduledate() {
            return scheduledate;
        }

        public void setScheduledate(String scheduledate) {
            this.scheduledate = scheduledate;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getJobtypeid() {
            return jobtypeid;
        }

        public void setJobtypeid(String jobtypeid) {
            this.jobtypeid = jobtypeid;
        }

        public String getJobtypename() {
            return jobtypename;
        }

        public void setJobtypename(String jobtypename) {
            this.jobtypename = jobtypename;
        }

        public String getTowncouncil() {
            return towncouncil;
        }

        public void setTowncouncil(String towncouncil) {
            this.towncouncil = towncouncil;
        }

        public String getJosurveytypeid() {
            return josurveytypeid;
        }

        public void setJosurveytypeid(String josurveytypeid) {
            this.josurveytypeid = josurveytypeid;
        }

        public String getRefno() {
            return refno;
        }

        public void setRefno(String refno) {
            this.refno = refno;
        }

        public String getConstituency() {
            return constituency;
        }

        public void setConstituency(String constituency) {
            this.constituency = constituency;
        }

        public String getDeadlinedate() {
            return deadlinedate;
        }

        public void setDeadlinedate(String deadlinedate) {
            this.deadlinedate = deadlinedate;
        }

        public String getTowncouncilid() {
            return towncouncilid;
        }

        public void setTowncouncilid(String towncouncilid) {
            this.towncouncilid = towncouncilid;
        }

        public String getConstituencyid() {
            return constituencyid;
        }

        public void setConstituencyid(String constituencyid) {
            this.constituencyid = constituencyid;
        }

        public String getJo_scheduleid() {
            return jo_scheduleid;
        }

        public void setJo_scheduleid(String jo_scheduleid) {
            this.jo_scheduleid = jo_scheduleid;
        }

        public String getClassification() {
            return classification;
        }

        public void setClassification(String classification) {
            this.classification = classification;
        }

        public String getFormId() {
            return formId;
        }

        public void setFormId(String formId) {
            this.formId = formId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(formId);
            dest.writeString(deadline);
            dest.writeString(customername);
            dest.writeString(customerid);
            dest.writeString(joborder);
            dest.writeString(joborderid);
            dest.writeString(location);
            dest.writeString(locationid);
            dest.writeString(scheduledate);
            dest.writeString(remarks);
            dest.writeString(jobtypeid);
            dest.writeString(jobtypename);
            dest.writeString(towncouncil);
            dest.writeString(josurveytypeid);
            dest.writeString(refno);
            dest.writeString(constituency);
            dest.writeString(deadlinedate);
            dest.writeString(towncouncilid);
            dest.writeString(constituencyid);
            dest.writeString(jo_scheduleid);
            dest.writeString(classification);
            dest.writeString(iscomplete);
            dest.writeString(zone);
            dest.writeString(division);
            dest.writeString(geoaddress);
        }
    }

    public class Teams {
        private String id;

        private String teamname;

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
    }
}
