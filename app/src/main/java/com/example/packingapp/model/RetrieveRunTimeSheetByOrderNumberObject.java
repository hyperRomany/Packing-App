package com.example.packingapp.model;

import com.google.gson.annotations.SerializedName;

public class RetrieveRunTimeSheetByOrderNumberObject {
    @SerializedName("DRIVER_ID")
    private String DRIVER_ID;
    @SerializedName("STATUS")
    private String STATUS;
    @SerializedName("id")
    private String SheetID;
    @SerializedName("ORDER_NO")
    private String orderNumber;

    public RetrieveRunTimeSheetByOrderNumberObject(String str, String str2, String str3, String str4) {
        this.SheetID = str;
        this.DRIVER_ID = str2;
        this.STATUS = str3;
        this.orderNumber = str4;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public void setOrderNumber(String str) {
        this.orderNumber = str;
    }

    public String getSheetID() {
        return this.SheetID;
    }

    public void setSheetID(String str) {
        this.SheetID = str;
    }

    public String getDRIVER_ID() {
        return this.DRIVER_ID;
    }

    public void setDRIVER_ID(String str) {
        this.DRIVER_ID = str;
    }

    public String getSTATUS() {
        return this.STATUS;
    }

    public void setSTATUS(String str) {
        this.STATUS = str;
    }
}
