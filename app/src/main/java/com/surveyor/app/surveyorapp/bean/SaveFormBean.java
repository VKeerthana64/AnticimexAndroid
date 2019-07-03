package com.surveyor.app.surveyorapp.bean;

/**
 * Created by DELL on 09-Dec-18.
 */

public class SaveFormBean {

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
        private String uniqueid;

        public String getUniqueid() {
            return uniqueid;
        }

        public void setUniqueid(String uniqueid) {
            this.uniqueid = uniqueid;
        }
    }
}
