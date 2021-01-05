package com.example.packingapp.model.ReprintAWBModules;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseReprintAWB {
    @SerializedName("records")
    private List<OrderDataModuleHeader_Reprint> Header;

    @SerializedName("details")
    private List<ItemsOrderDataDetails_Scanned_Reprint> Details;

    public List<OrderDataModuleHeader_Reprint> getHeader() {
        return Header;
    }

    public void setHeader(List<OrderDataModuleHeader_Reprint> header) {
        Header = header;
    }

    public List<ItemsOrderDataDetails_Scanned_Reprint> getDetails() {
        return Details;
    }

    public void setDetails(List<ItemsOrderDataDetails_Scanned_Reprint> details) {
        Details = details;
    }
}