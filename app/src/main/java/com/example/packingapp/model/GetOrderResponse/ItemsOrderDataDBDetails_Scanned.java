package com.example.packingapp.model.GetOrderResponse;

import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ItemsOrderDataDBDetails_Scanned")
public class ItemsOrderDataDBDetails_Scanned {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name ="Order_number")
    @SerializedName("Order_number")
    private String Order_number;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

//    @ColumnInfo(name = "Name_of_item")
//    @SerializedName("Name_of_item")
//    private String Name_of_item;

    @ColumnInfo(name = "price")
    @SerializedName("price")
    private float price;

    @ColumnInfo(name = "quantity")
    @SerializedName("quantity")
    private float quantity;


    @ColumnInfo(name = "sku")
    @SerializedName("sku")
    private String sku;


    @ColumnInfo(name = "unit_of_measurement")
    @SerializedName("unit_of_measurement")
    private String unite;

    @ColumnInfo(name = "TrackingNumber")
    @SerializedName("TrackingNumber")
    private String TrackingNumber;

    public ItemsOrderDataDBDetails_Scanned() {
    }

    public ItemsOrderDataDBDetails_Scanned(String name, float price, float quantity, String sku, String unite, String trackingNumber) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.sku = sku;
        this.unite = unite;
        TrackingNumber = trackingNumber;
    }

    public ItemsOrderDataDBDetails_Scanned(String OrderNumber , String name, float price,
                                           float quantity, String sku, String unite) {
        this.Order_number = OrderNumber;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.sku = sku;
        this.unite = unite;
    }

    public ItemsOrderDataDBDetails_Scanned(String order_number, String name, float price, float quantity,
                                           String sku, String unite, String trackingNumber) {
        Order_number = order_number;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.sku = sku;
        this.unite = unite;
        TrackingNumber = trackingNumber;
    }

    public ItemsOrderDataDBDetails_Scanned(String name, String sku, float price, float quantity, String unite) {
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.quantity = quantity;
        this.unite = unite;
    }



    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOrder_number() {
        return Order_number;
    }

    public void setOrder_number(String order_number) {
        Order_number = order_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String getTrackingNumber() {
        return TrackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        TrackingNumber = trackingNumber;
    }


}
