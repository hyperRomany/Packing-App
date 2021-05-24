package com.example.packingapp.model.GetOrderResponse;

import com.google.gson.annotations.SerializedName;

public class delivery {

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("method")
    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
