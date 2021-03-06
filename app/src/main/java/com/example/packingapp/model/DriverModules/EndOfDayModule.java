package com.example.packingapp.model.DriverModules;

import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;

public class EndOfDayModule {

    @ColumnInfo(name = "TRACKING_NO")
    @SerializedName("TRACKING_NO")
    private String  TRACKING_NO ;


    @ColumnInfo(name = "ITEM_PRICE")
    @SerializedName("ITEM_PRICE")
    private String  ITEM_PRICE ;


    @ColumnInfo(name = "STATUS")
    @SerializedName("STATUS")
    private String  STATUS ;

    public EndOfDayModule(String TRACKING_NO, String ITEM_PRICE, String STATUS) {
        this.TRACKING_NO = TRACKING_NO;
        this.ITEM_PRICE = ITEM_PRICE;
        this.STATUS = STATUS;
    }

    public String getTRACKING_NO() {
        return TRACKING_NO;
    }

    public void setTRACKING_NO(String TRACKING_NO) {
        this.TRACKING_NO = TRACKING_NO;
    }

    public String getITEM_PRICE() {
        return ITEM_PRICE;
    }

    public void setITEM_PRICE(String ITEM_PRICE) {
        this.ITEM_PRICE = ITEM_PRICE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }
}
