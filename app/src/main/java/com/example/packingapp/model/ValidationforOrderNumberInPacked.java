package com.example.packingapp.model;

public class ValidationforOrderNumberInPacked {


    private String order2;

    private float sumqty2;
    private float sumqty1;
    private float diff;
    private String sku2;

    public ValidationforOrderNumberInPacked(String order2, float sumqty2, float sumqty1, float diff, String sku2) {
        this.order2 = order2;
        this.sumqty2 = sumqty2;
        this.sumqty1 = sumqty1;
        this.diff = diff;
        this.sku2 = sku2;
    }

    public String getOrder2() {
        return order2;
    }

    public void setOrder2(String order2) {
        this.order2 = order2;
    }

    public float getSumqty2() {
        return sumqty2;
    }

    public void setSumqty2(float sumqty2) {
        this.sumqty2 = sumqty2;
    }

    public float getSumqty1() {
        return sumqty1;
    }

    public void setSumqty1(float sumqty1) {
        this.sumqty1 = sumqty1;
    }

    public float getDiff() {
        return diff;
    }

    public void setDiff(float diff) {
        this.diff = diff;
    }

    public String getSku2() {
        return sku2;
    }

    public void setSku2(String sku2) {
        this.sku2 = sku2;
    }
}
