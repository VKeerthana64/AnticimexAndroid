package com.surveyor.app.surveyorapp.bean;

import java.util.ArrayList;

/**
 * Created by DELL on 09-Dec-18.
 */

public class OptionBean {
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

        private ArrayList<Findings> findings;
        private ArrayList<Habitate> habitate;
        private ArrayList<ActionTaken> actionTaken;
        private ArrayList<Remarks> remarks;
        private ArrayList<ProbableCauseOfBurrows> probableCauseOfBurrows;
        private ArrayList<BinCenter> binCenter;

        public ArrayList<Findings> getFindings() {
            return findings;
        }

        public void setFindings(ArrayList<Findings> findings) {
            this.findings = findings;
        }

        public ArrayList<Habitate> getHabitate() {
            return habitate;
        }

        public void setHabitate(ArrayList<Habitate> habitate) {
            this.habitate = habitate;
        }

        public ArrayList<ActionTaken> getActionTaken() {
            return actionTaken;
        }

        public void setActionTaken(ArrayList<ActionTaken> actionTaken) {
            this.actionTaken = actionTaken;
        }

        public ArrayList<Remarks> getRemarks() {
            return remarks;
        }

        public void setRemarks(ArrayList<Remarks> remarks) {
            this.remarks = remarks;
        }

        public ArrayList<ProbableCauseOfBurrows> getProbableCauseOfBurrows() {
            return probableCauseOfBurrows;
        }

        public void setProbableCauseOfBurrows(ArrayList<ProbableCauseOfBurrows> probableCauseOfBurrows) {
            this.probableCauseOfBurrows = probableCauseOfBurrows;
        }

        public ArrayList<BinCenter> getBinCenter() {
            return binCenter;
        }

        public void setBinCenter(ArrayList<BinCenter> binCenter) {
            this.binCenter = binCenter;
        }
    }

    public class ActionTaken {
        private String id;
        private String sortorder;
        private String fieldType;
        private String optionname;
        boolean isSelected ;
        int isSync ;

        public int getIsSync() {
            return isSync;
        }

        public void setIsSync(int isSync) {
            this.isSync = isSync;
        }


        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSortorder() {
            return sortorder;
        }

        public void setSortorder(String sortorder) {
            this.sortorder = sortorder;
        }

        public String getFieldType() {
            return fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }

        public String getOptionname() {
            return optionname;
        }

        public void setOptionname(String optionname) {
            this.optionname = optionname;
        }
    }


    public class Remarks
    {
        private String id;

        private String sortorder;

        private String fieldType;

        private String optionname;

        boolean isSelected ;

        int isSync ;

        public int getIsSync() {
            return isSync;
        }

        public void setIsSync(int isSync) {
            this.isSync = isSync;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getSortorder ()
        {
            return sortorder;
        }

        public void setSortorder (String sortorder)
        {
            this.sortorder = sortorder;
        }

        public String getFieldType ()
        {
            return fieldType;
        }

        public void setFieldType (String fieldType)
        {
            this.fieldType = fieldType;
        }

        public String getOptionname ()
        {
            return optionname;
        }

        public void setOptionname (String optionname)
        {
            this.optionname = optionname;
        }


    }

    public class ProbableCauseOfBurrows
    {
        private String id;

        private String sortorder;

        private String fieldType;

        private String optionname;

        boolean isSelected ;

        int isSync ;

        public int getIsSync() {
            return isSync;
        }

        public void setIsSync(int isSync) {
            this.isSync = isSync;
        }


        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }


        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getSortorder ()
        {
            return sortorder;
        }

        public void setSortorder (String sortorder)
        {
            this.sortorder = sortorder;
        }

        public String getFieldType ()
        {
            return fieldType;
        }

        public void setFieldType (String fieldType)
        {
            this.fieldType = fieldType;
        }

        public String getOptionname ()
        {
            return optionname;
        }

        public void setOptionname (String optionname)
        {
            this.optionname = optionname;
        }

    }

    public class Habitate
    {
        private String id;

        private String sortorder;

        private String fieldType;

        private String optionname;

        boolean isSelected ;

        int isSync ;

        public int getIsSync() {
            return isSync;
        }

        public void setIsSync(int isSync) {
            this.isSync = isSync;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }


        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getSortorder ()
        {
            return sortorder;
        }

        public void setSortorder (String sortorder)
        {
            this.sortorder = sortorder;
        }

        public String getFieldType ()
        {
            return fieldType;
        }

        public void setFieldType (String fieldType)
        {
            this.fieldType = fieldType;
        }

        public String getOptionname ()
        {
            return optionname;
        }

        public void setOptionname (String optionname)
        {
            this.optionname = optionname;
        }

    }

    public class Findings
    {
        private String id;
        private String sortorder;
        private String fieldType;
        private String optionname;
        boolean isSelected ;
        int isSync ;

        public int getIsSync() {
            return isSync;
        }

        public void setIsSync(int isSync) {
            this.isSync = isSync;
        }


        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }


        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getSortorder ()
        {
            return sortorder;
        }

        public void setSortorder (String sortorder)
        {
            this.sortorder = sortorder;
        }

        public String getFieldType ()
        {
            return fieldType;
        }

        public void setFieldType (String fieldType)
        {
            this.fieldType = fieldType;
        }

        public String getOptionname ()
        {
            return optionname;
        }

        public void setOptionname (String optionname)
        {
            this.optionname = optionname;
        }

    }

    public class BinCenter
    {
        private String id;

        private String sortorder;

        private String fieldType;

        private String optionname;

        boolean isSelected ;

        int isSync ;

        public int getIsSync() {
            return isSync;
        }

        public void setIsSync(int isSync) {
            this.isSync = isSync;
        }


        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }


        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getSortorder ()
        {
            return sortorder;
        }

        public void setSortorder (String sortorder)
        {
            this.sortorder = sortorder;
        }

        public String getFieldType ()
        {
            return fieldType;
        }

        public void setFieldType (String fieldType)
        {
            this.fieldType = fieldType;
        }

        public String getOptionname ()
        {
            return optionname;
        }

        public void setOptionname (String optionname)
        {
            this.optionname = optionname;
        }


    }


}
