package com.example.packingapp.model;

public class ItemDetailForRecycle {
    private String barcode;
    private String name;

    public ItemDetailForRecycle(String str, String str2) {
        this.name = str;
        this.barcode = str2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String str) {
        this.barcode = str;
    }
}
