package com.surveyor.app.surveyorapp.bean;

/**
 * Created by DELL on 24-Nov-18.
 */

public class LoginBean {

    private Data data;
    private String msg;
    private String success;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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
        private String token;

        private Userinfo userinfo;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Userinfo getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(Userinfo userinfo) {
            this.userinfo = userinfo;
        }

    }

    public class Userinfo {
        private String username;
        private String userid;
        private String userrole;
        private String useremail;
        private String profileimage;
        private String usercontactno;
        private String dispayname;
        private int intervalTime;

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

        public String getUserrole() {
            return userrole;
        }

        public void setUserrole(String userrole) {
            this.userrole = userrole;
        }

        public String getUseremail() {
            return useremail;
        }

        public void setUseremail(String useremail) {
            this.useremail = useremail;
        }

        public String getProfileimage() {
            return profileimage;
        }

        public void setProfileimage(String profileimage) {
            this.profileimage = profileimage;
        }

        public String getUsercontactno() {
            return usercontactno;
        }

        public void setUsercontactno(String usercontactno) {
            this.usercontactno = usercontactno;
        }

        public String getDispayname() {
            return dispayname;
        }

        public void setDispayname(String dispayname) {
            this.dispayname = dispayname;
        }

        public int getIntervalTime() {
            return intervalTime;
        }

        public void setIntervalTime(int intervalTime) {
            this.intervalTime = intervalTime;
        }
    }

}


