package com.example.packingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseZoneName {
    @SerializedName("records")
    private List<RecordZoneName> records;

    public void setRecords(List<RecordZoneName> records){
        this.records = records;
    }

    public List<RecordZoneName> getRecords(){
        return records;
    }
}
