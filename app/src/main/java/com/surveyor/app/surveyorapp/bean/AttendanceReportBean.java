package com.surveyor.app.surveyorapp.bean;

import java.util.ArrayList;

/**
 * Created by DELL on 27-Nov-18.
 */

public class AttendanceReportBean {

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
        private String address;
        private String imagefile;
        private String timein;
        private String date;
        private String timeout;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getImagefile() {
            return imagefile;
        }

        public void setImagefile(String imagefile) {
            this.imagefile = imagefile;
        }

        public String getTimein() {
            return timein;
        }

        public void setTimein(String timein) {
            this.timein = timein;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTimeout() {
            return timeout;
        }

        public void setTimeout(String timeout) {
            this.timeout = timeout;
        }
    }
}
