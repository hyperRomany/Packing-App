package com.example.packingapp.model.DriverModules;

public class DriverPackages_Details_ForAdapter {

    private  Boolean Checked_Item;

    private int uid;


   private String ORDER_NO;


    private String TRACKING_NO;


    private String PACKAGE_PRICE;

    //TODO this for ITEM_QUANTITY

    private String COUNT_ITEMS_PACKAGE;


    private String STATUS;


    private String REASON;

    public Boolean getChecked_Item() {
        return Checked_Item;
    }

    public void setChecked_Item(Boolean checked_Item) {
        Checked_Item = checked_Item;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getORDER_NO() {
        return ORDER_NO;
    }

    public void setORDER_NO(String ORDER_NO) {
        this.ORDER_NO = ORDER_NO;
    }

    public String getTRACKING_NO() {
        return TRACKING_NO;
    }

    public void setTRACKING_NO(String TRACKING_NO) {
        this.TRACKING_NO = TRACKING_NO;
    }

    public String getPACKAGE_PRICE() {
        return PACKAGE_PRICE;
    }

    public void setPACKAGE_PRICE(String PACKAGE_PRICE) {
        this.PACKAGE_PRICE = PACKAGE_PRICE;
    }

    public String getCOUNT_ITEMS_PACKAGE() {
        return COUNT_ITEMS_PACKAGE;
    }

    public void setCOUNT_ITEMS_PACKAGE(String COUNT_ITEMS_PACKAGE) {
        this.COUNT_ITEMS_PACKAGE = COUNT_ITEMS_PACKAGE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getREASON() {
        return REASON;
    }

    public void setREASON(String REASON) {
        this.REASON = REASON;
    }
}
