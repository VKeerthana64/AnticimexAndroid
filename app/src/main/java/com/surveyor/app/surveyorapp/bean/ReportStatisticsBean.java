package com.surveyor.app.surveyorapp.bean;

/**
 * Created by DELL on 24-Nov-18.
 */

public class ReportStatisticsBean {

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
        private String totalreports;
        private String totalsubmitted;
        private String totalongoing;

        public String getTotalreports() {
            return totalreports;
        }

        public void setTotalreports(String totalreports) {
            this.totalreports = totalreports;
        }

        public String getTotalsubmitted() {
            return totalsubmitted;
        }

        public void setTotalsubmitted(String totalsubmitted) {
            this.totalsubmitted = totalsubmitted;
        }

        public String getTotalongoing() {
            return totalongoing;
        }

        public void setTotalongoing(String totalongoing) {
            this.totalongoing = totalongoing;
        }
    }


}


