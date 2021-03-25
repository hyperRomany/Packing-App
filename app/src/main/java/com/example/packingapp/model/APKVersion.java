package com.example.packingapp.model;

import com.google.gson.annotations.SerializedName;

public class APKVersion {
    @SerializedName("ID")
    private String ID;

    @SerializedName("Version_Code")
    private String Version_Code;

    @SerializedName("Version_Name")
    private String Version_Name;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getVersion_Code() {
        return Version_Code;
    }

    public void setVersion_Code(String version_Code) {
        Version_Code = version_Code;
    }

    public String getVersion_Name() {
        return Version_Name;
    }

    public void setVersion_Name(String version_Name) {
        Version_Name = version_Name;
    }
}
