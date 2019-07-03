package com.surveyor.app.surveyorapp.bean;

/**
 * Created by Sunil on 14-May-17.
 */
public class DrawerBean {

    int drawerIcons;
    boolean isSelected;

    public DrawerBean( int drawerIcons ) {
        this.drawerIcons = drawerIcons;
    }

    public DrawerBean(int drawerIcons, boolean isSelected) {
        this.drawerIcons = drawerIcons;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getDrawerIcons() {
        return drawerIcons;
    }

    public void setDrawerIcons(int drawerIcons) {
        this.drawerIcons = drawerIcons;
    }
}
