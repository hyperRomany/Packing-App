package com.example.packingapp.model.RecievePacked;

import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RecievePackedModule")
public class RecievePackedModule {

        @PrimaryKey(autoGenerate = true )
        private int uid;

        @ColumnInfo(name ="ORDER_NO")
        @SerializedName("ORDER_NO")
        private String ORDER_NO;

        @ColumnInfo(name ="NO_OF_PACKAGES")
        @SerializedName("NO_OF_PACKAGES")
        private String NO_OF_PACKAGES;

       @ColumnInfo(name ="STATUS")
        @SerializedName("STATUS")
        private String STATUS;

        @ColumnInfo(name ="Tracking_Number")
        @SerializedName("Tracking_Number")
        private String Tracking_Number;

    @ColumnInfo(name ="DRIVER_ID")
    @SerializedName("DRIVER_ID")
    private String DRIVER_ID;


    @ColumnInfo(name ="Zone")
        @SerializedName("Zone")
        private String Zone;

    //for sms data
    @ColumnInfo(name ="NameArabic")
    @SerializedName("NameArabic")
    private String NameArabic;

    @ColumnInfo(name ="phone")
    @SerializedName("phone")
    private String phone;

    @ColumnInfo(name ="CUSTOMER_NAME")
    @SerializedName("CUSTOMER_NAME")
    private String CUSTOMER_NAME;

    @ColumnInfo(name ="CUSTOMER_PHONE")
    @SerializedName("CUSTOMER_PHONE")
    private String CUSTOMER_PHONE;

    @ColumnInfo(name ="GRAND_TOTAL")
    @SerializedName("GRAND_TOTAL")
    private String GRAND_TOTAL;

    public RecievePackedModule() {
    }

    public RecievePackedModule(String ORDER_NO, String NO_OF_PACKAGES,
                               String tracking_Number) {
        this.ORDER_NO = ORDER_NO;
        this.NO_OF_PACKAGES = NO_OF_PACKAGES;
        Tracking_Number = tracking_Number;
    }

    public RecievePackedModule(String ORDER_NO, String NO_OF_PACKAGES, String tracking_Number,
                               String nameArabic, String phone, String CUSTOMER_NAME,
                               String CUSTOMER_PHONE, String GRAND_TOTAL) {
        this.ORDER_NO = ORDER_NO;
        this.NO_OF_PACKAGES = NO_OF_PACKAGES;
        Tracking_Number = tracking_Number;
        NameArabic = nameArabic;
        this.phone = phone;
        this.CUSTOMER_NAME = CUSTOMER_NAME;
        this.CUSTOMER_PHONE = CUSTOMER_PHONE;
        this.GRAND_TOTAL = GRAND_TOTAL;
    }

    public RecievePackedModule(String ORDER_NO, String NO_OF_PACKAGES,
                               String tracking_Number, String Zone) {
        this.ORDER_NO = ORDER_NO;
        this.NO_OF_PACKAGES = NO_OF_PACKAGES;
        Tracking_Number = tracking_Number;
        this.Zone=Zone;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getORDER_NO() {
        return ORDER_NO;
    }

    public void setORDER_NO(String ORDER_NO) {
        this.ORDER_NO = ORDER_NO;
    }

    public String getNO_OF_PACKAGES() {
        return NO_OF_PACKAGES;
    }

    public void setNO_OF_PACKAGES(String NO_OF_PACKAGES) {
        this.NO_OF_PACKAGES = NO_OF_PACKAGES;
    }

    public String getTracking_Number() {
        return Tracking_Number;
    }

    public void setTracking_Number(String tracking_Number) {
        Tracking_Number = tracking_Number;
    }

    public String getDRIVER_ID() {
        return DRIVER_ID;
    }

    public void setDRIVER_ID(String DRIVER_ID) {
        this.DRIVER_ID = DRIVER_ID;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }

    public String getNameArabic() {
        return NameArabic;
    }

    public void setNameArabic(String nameArabic) {
        NameArabic = nameArabic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }

    public String getCUSTOMER_PHONE() {
        return CUSTOMER_PHONE;
    }

    public void setCUSTOMER_PHONE(String CUSTOMER_PHONE) {
        this.CUSTOMER_PHONE = CUSTOMER_PHONE;
    }

    public String getGRAND_TOTAL() {
        return GRAND_TOTAL;
    }

    public void setGRAND_TOTAL(String GRAND_TOTAL) {
        this.GRAND_TOTAL = GRAND_TOTAL;
    }
}
