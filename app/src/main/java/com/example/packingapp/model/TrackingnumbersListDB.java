package com.example.packingapp.model;

import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TrackingnumbersListDB")
public class TrackingnumbersListDB {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "OrderNumber")
    @SerializedName("OrderNumber")
    private String OrderNumber;

    @ColumnInfo(name = "TrackingNumber")
    @SerializedName("TrackingNumber")
    private String TrackingNumber;

    public TrackingnumbersListDB() {
    }

    public TrackingnumbersListDB(String orderNumber, String trackingNumber) {
        OrderNumber = orderNumber;
        TrackingNumber = trackingNumber;
    }

    public TrackingnumbersListDB(String trackingNumber) {
        TrackingNumber = trackingNumber;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getTrackingNumber() {
        return TrackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        TrackingNumber = trackingNumber;
    }
}
