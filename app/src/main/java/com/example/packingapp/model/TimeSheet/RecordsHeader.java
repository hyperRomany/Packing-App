package com.example.packingapp.model.TimeSheet;

import com.google.gson.annotations.SerializedName;

public class RecordsHeader {
    @SerializedName("ORDER_NO")
    private String ORDER_NO;

    @SerializedName("CUSTOMER_NAME")
    private String CUSTOMER_NAME;

    @SerializedName("CUSTOMER_PHONE")
    private String CUSTOMER_PHONE;

    @SerializedName("GRAND_TOTAL")
    private String GRAND_TOTAL;

    @SerializedName("Reedemed_Points_Amount")
    private String Reedemed_Points_Amount;

    @SerializedName("Payment_Method")
    private String Payment_Method;

    @SerializedName("Delivery_Method")
    private String Delivery_Method;

    public String getPayment_Method() {
        return Payment_Method;
    }

    public void setPayment_Method(String payment_Method) {
        Payment_Method = payment_Method;
    }

    public String getDelivery_Method() {
        return Delivery_Method;
    }

    public void setDelivery_Method(String delivery_Method) {
        Delivery_Method = delivery_Method;
    }

    public String getReedemed_Points_Amount() {
        return Reedemed_Points_Amount;
    }

    public void setReedemed_Points_Amount(String reedemed_Points_Amount) {
        Reedemed_Points_Amount = reedemed_Points_Amount;
    }

    public String getORDER_NO() {
        return ORDER_NO;
    }

    public void setORDER_NO(String ORDER_NO) {
        this.ORDER_NO = ORDER_NO;
    }

    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }

    public String getGRAND_TOTAL() {
        return GRAND_TOTAL;
    }

    public void setGRAND_TOTAL(String GRAND_TOTAL) {
        this.GRAND_TOTAL = GRAND_TOTAL;
    }

    public String getCUSTOMER_PHONE() {
        return CUSTOMER_PHONE;
    }

    public void setCUSTOMER_PHONE(String CUSTOMER_PHONE) {
        this.CUSTOMER_PHONE = CUSTOMER_PHONE;
    }



}
