package com.example.packingapp.model.RecievePacked;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.room.ColumnInfo;

public class ResponseFetchRuntimesheetID {

    @ColumnInfo(name ="records")
    @SerializedName("records")
    private List<RecievePackedModule> records;


    public ResponseFetchRuntimesheetID(List<RecievePackedModule> records) {
        this.records = records;
    }

    public List<RecievePackedModule> getRecords() {
        return records;
    }

    public void setRecords(List<RecievePackedModule> records) {
        this.records = records;
    }
}
