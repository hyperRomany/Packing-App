package com.example.packingapp.model.ReprintAWBModules;

import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;

public class ItemsOrderDataDetails_Scanned_Reprint {


    @ColumnInfo(name = "TRACKING_NO")
    @SerializedName("TRACKING_NO")
    private String TRACKING_NO;

//    @ColumnInfo(name = "Name_of_item")
//    @SerializedName("Name_of_item")
//    private String Name_of_item;

    @ColumnInfo(name = "ITEM_NAME")
    @SerializedName("ITEM_NAME")
    private String ITEM_NAME;

    @ColumnInfo(name = "ITEM_BARCODE")
    @SerializedName("ITEM_BARCODE")
    private String ITEM_BARCODE;


    @ColumnInfo(name = "ITEM_PRICE")
    @SerializedName("ITEM_PRICE")
    private float ITEM_PRICE;


    @ColumnInfo(name = "ITEM_QUANTITY")
    @SerializedName("ITEM_QUANTITY")
    private float ITEM_QUANTITY;

    @ColumnInfo(name = "ITEM_UNIT")
    @SerializedName("ITEM_UNIT")
    private String ITEM_UNIT;

    public ItemsOrderDataDetails_Scanned_Reprint() {
    }

    public String getTRACKING_NO() {
        return TRACKING_NO;
    }

    public void setTRACKING_NO(String TRACKING_NO) {
        this.TRACKING_NO = TRACKING_NO;
    }

    public String getITEM_NAME() {
        return ITEM_NAME;
    }

    public void setITEM_NAME(String ITEM_NAME) {
        this.ITEM_NAME = ITEM_NAME;
    }

    public String getITEM_BARCODE() {
        return ITEM_BARCODE;
    }

    public void setITEM_BARCODE(String ITEM_BARCODE) {
        this.ITEM_BARCODE = ITEM_BARCODE;
    }

    public float getITEM_PRICE() {
        return ITEM_PRICE;
    }

    public void setITEM_PRICE(float ITEM_PRICE) {
        this.ITEM_PRICE = ITEM_PRICE;
    }

    public float getITEM_QUANTITY() {
        return ITEM_QUANTITY;
    }

    public void setITEM_QUANTITY(float ITEM_QUANTITY) {
        this.ITEM_QUANTITY = ITEM_QUANTITY;
    }

    public String getITEM_UNIT() {
        return ITEM_UNIT;
    }

    public void setITEM_UNIT(String ITEM_UNIT) {
        this.ITEM_UNIT = ITEM_UNIT;
    }
}
