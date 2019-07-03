package com.surveyor.app.surveyorapp.bean;

import java.util.ArrayList;

/**
 * Created by DELL on 29-Nov-18.
 */

public class JobTeamLeaderBean {

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

    public class Data {
        private String joborder;

        private String jobtypeid;

        private String joborderid;

        private String location;

        private String constituency;

        private String remarks;

        private String scheduledate;

        private ArrayList<Teams> teams;
        private ArrayList<AssignedUser> assigneduser;

        private String refno;

        private String jobtypename;

        private String josurveytypeid;

        private String locationid;

        private String customerid;

        private String classification;

        private String deadlinedate;

        private String towncouncilid;

        private String constituencyid;

        private String jo_scheduleid;

        private String deadline;

        private String towncouncil;

        private String customername;

        public ArrayList<AssignedUser> getAssigneduser() {
            return assigneduser;
        }

        public void setAssigneduser(ArrayList<AssignedUser> assigneduser) {
            this.assigneduser = assigneduser;
        }

        public String getJoborder() {
            return joborder;
        }

        public void setJoborder(String joborder) {
            this.joborder = joborder;
        }

        public String getJobtypeid() {
            return jobtypeid;
        }

        public void setJobtypeid(String jobtypeid) {
            this.jobtypeid = jobtypeid;
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

        public String getConstituency() {
            return constituency;
        }

        public void setConstituency(String constituency) {
            this.constituency = constituency;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getScheduledate() {
            return scheduledate;
        }

        public void setScheduledate(String scheduledate) {
            this.scheduledate = scheduledate;
        }

        public ArrayList<Teams> getTeams() {
            return teams;
        }

        public void setTeams(ArrayList<Teams> teams) {
            this.teams = teams;
        }

        public String getRefno() {
            return refno;
        }

        public void setRefno(String refno) {
            this.refno = refno;
        }

        public String getJobtypename() {
            return jobtypename;
        }

        public void setJobtypename(String jobtypename) {
            this.jobtypename = jobtypename;
        }

        public String getJosurveytypeid() {
            return josurveytypeid;
        }

        public void setJosurveytypeid(String josurveytypeid) {
            this.josurveytypeid = josurveytypeid;
        }

        public String getLocationid() {
            return locationid;
        }

        public void setLocationid(String locationid) {
            this.locationid = locationid;
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

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public String getTowncouncil() {
            return towncouncil;
        }

        public void setTowncouncil(String towncouncil) {
            this.towncouncil = towncouncil;
        }

        public String getCustomername() {
            return customername;
        }

        public void setCustomername(String customername) {
            this.customername = customername;
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

    public class AssignedUser {
        private String userid;
        private String username;

        public AssignedUser(String userid, String username) {
            this.userid = userid;
            this.username = username;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
