package com.surveyor.app.surveyorapp.bean;

import java.io.File;

public class FormImages {
    String imgpath;
    File imgFile;
    boolean isFromServer;

    public FormImages(String imagePath, File file, boolean isFromServer) {
        this.imgpath = imagePath;
        this.imgFile = file;
        this.isFromServer= isFromServer;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public File getImgFile() {
        return imgFile;
    }

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }

    public boolean isFromServer() {
        return isFromServer;
    }

    public void setFromServer(boolean fromServer) {
        isFromServer = fromServer;
    }
}
