package com.surveyor.app.surveyorapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DELL on 21-Jan-19.
 */

public class JobAllSyncDataBean implements Parcelable {

    public JobAllSyncDataBean() {
    }

    private String jobtypeid;
    private String iscomplete;
    private String location;
    private String remarks;
    private ArrayList<JobAllSyncAssigneduserBean> assigneduser;
    private ArrayList<TeamMemberDataBean> surveyteammember;
    private String refno;
    private String locationid;
    private String josurveytypeid;
    private String division;
    private ArrayList<JobAllSyncAllSurveyBean> allSurvey;
    private String towncouncilid;
    private String deadlinedate;
    private String constituencyid;
    private String customername;
    private String joborder;
    private String joborderid;
    private String constituency;
    private String scheduledate;
    private ArrayList<JobAllSyncTeamBean> teams;
    private String jobtypename;
    private String customerid;
    private String classification;
    private String jo_scheduleid;
    private String towncouncil;
    private String deadline;
    private String zone;


    protected JobAllSyncDataBean(Parcel in) {
        jobtypeid = in.readString();
        iscomplete = in.readString();
        location = in.readString();
        remarks = in.readString();
        assigneduser = in.createTypedArrayList(JobAllSyncAssigneduserBean.CREATOR);
        surveyteammember = in.createTypedArrayList(TeamMemberDataBean.CREATOR);
        refno = in.readString();
        locationid = in.readString();
        josurveytypeid = in.readString();
        division = in.readString();
        allSurvey = in.createTypedArrayList(JobAllSyncAllSurveyBean.CREATOR);
        towncouncilid = in.readString();
        deadlinedate = in.readString();
        constituencyid = in.readString();
        customername = in.readString();
        joborder = in.readString();
        joborderid = in.readString();
        constituency = in.readString();
        scheduledate = in.readString();
        teams = in.createTypedArrayList(JobAllSyncTeamBean.CREATOR);
        jobtypename = in.readString();
        customerid = in.readString();
        classification = in.readString();
        jo_scheduleid = in.readString();
        towncouncil = in.readString();
        deadline = in.readString();
        zone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jobtypeid);
        dest.writeString(iscomplete);
        dest.writeString(location);
        dest.writeString(remarks);
        dest.writeTypedList(assigneduser);
        dest.writeTypedList(surveyteammember);
        dest.writeString(refno);
        dest.writeString(locationid);
        dest.writeString(josurveytypeid);
        dest.writeString(division);
        dest.writeTypedList(allSurvey);
        dest.writeString(towncouncilid);
        dest.writeString(deadlinedate);
        dest.writeString(constituencyid);
        dest.writeString(customername);
        dest.writeString(joborder);
        dest.writeString(joborderid);
        dest.writeString(constituency);
        dest.writeString(scheduledate);
        dest.writeTypedList(teams);
        dest.writeString(jobtypename);
        dest.writeString(customerid);
        dest.writeString(classification);
        dest.writeString(jo_scheduleid);
        dest.writeString(towncouncil);
        dest.writeString(deadline);
        dest.writeString(zone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JobAllSyncDataBean> CREATOR = new Creator<JobAllSyncDataBean>() {
        @Override
        public JobAllSyncDataBean createFromParcel(Parcel in) {
            return new JobAllSyncDataBean(in);
        }

        @Override
        public JobAllSyncDataBean[] newArray(int size) {
            return new JobAllSyncDataBean[size];
        }
    };

    public String getJobtypeid() {
        return jobtypeid;
    }

    public void setJobtypeid(String jobtypeid) {
        this.jobtypeid = jobtypeid;
    }

    public String getIscomplete() {
        return iscomplete;
    }

    public void setIscomplete(String iscomplete) {
        this.iscomplete = iscomplete;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ArrayList<JobAllSyncAssigneduserBean> getAssigneduser() {
        return assigneduser;
    }

    public void setAssigneduser(ArrayList<JobAllSyncAssigneduserBean> assigneduser) {
        this.assigneduser = assigneduser;
    }

    public ArrayList<TeamMemberDataBean> getSurveyteammember() {
        return surveyteammember;
    }

    public void setSurveyteammember(ArrayList<TeamMemberDataBean> surveyteammember) {
        this.surveyteammember = surveyteammember;
    }

    public String getRefno() {
        return refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public String getLocationid() {
        return locationid;
    }

    public void setLocationid(String locationid) {
        this.locationid = locationid;
    }

    public String getJosurveytypeid() {
        return josurveytypeid;
    }

    public void setJosurveytypeid(String josurveytypeid) {
        this.josurveytypeid = josurveytypeid;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public ArrayList<JobAllSyncAllSurveyBean> getAllSurvey() {
        return allSurvey;
    }

    public void setAllSurvey(ArrayList<JobAllSyncAllSurveyBean> allSurvey) {
        this.allSurvey = allSurvey;
    }

    public String getTowncouncilid() {
        return towncouncilid;
    }

    public void setTowncouncilid(String towncouncilid) {
        this.towncouncilid = towncouncilid;
    }

    public String getDeadlinedate() {
        return deadlinedate;
    }

    public void setDeadlinedate(String deadlinedate) {
        this.deadlinedate = deadlinedate;
    }

    public String getConstituencyid() {
        return constituencyid;
    }

    public void setConstituencyid(String constituencyid) {
        this.constituencyid = constituencyid;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
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

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

    public String getScheduledate() {
        return scheduledate;
    }

    public void setScheduledate(String scheduledate) {
        this.scheduledate = scheduledate;
    }

    public ArrayList<JobAllSyncTeamBean> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<JobAllSyncTeamBean> teams) {
        this.teams = teams;
    }

    public String getJobtypename() {
        return jobtypename;
    }

    public void setJobtypename(String jobtypename) {
        this.jobtypename = jobtypename;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
