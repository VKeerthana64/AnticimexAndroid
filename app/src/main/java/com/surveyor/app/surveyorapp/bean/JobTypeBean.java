package com.surveyor.app.surveyorapp.bean;

import java.util.ArrayList;

/**
 * Created by DELL on 29-Nov-18.
 */

public class JobTypeBean {

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

        private String jobtypeid;
        private String jobtypename;

        @Override
        public String toString() {
            return jobtypename;
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
    }


}
