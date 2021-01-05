package com.example.packingapp.model.ReprintAWBModules;


import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;

public class OrderDataModuleHeader_Reprint {

	@ColumnInfo(name ="ORDER_NO")
	@SerializedName("ORDER_NO")
	private String ORDER_NO;

	@ColumnInfo(name ="NO_OF_PACKAGES")
	@SerializedName("NO_OF_PACKAGES")
	private String NO_OF_PACKAGES;

	@ColumnInfo(name = "CUSTOMER_NAME")
	@SerializedName("CUSTOMER_NAME")
	private String CUSTOMER_NAME;

	@ColumnInfo(name = "CUSTOMER_PHONE")
	@SerializedName("CUSTOMER_PHONE")
	private String CUSTOMER_PHONE;

	@ColumnInfo(name = "ADDRESS_CITY")
	@SerializedName("ADDRESS_CITY")
	private String ADDRESS_CITY;

	@ColumnInfo(name = "ADDRESS_GOVERN")
	@SerializedName("ADDRESS_GOVERN")
	private String ADDRESS_GOVERN;


	@ColumnInfo(name = "ADDRESS_DISTRICT")
	@SerializedName("ADDRESS_DISTRICT")
	private String ADDRESS_DISTRICT;


	@ColumnInfo(name = "ADDRESS_DETAILS")
	@SerializedName("ADDRESS_DETAILS")
	private String ADDRESS_DETAILS;

	@ColumnInfo(name = "GRAND_TOTAL")
	@SerializedName("GRAND_TOTAL")
	private String GRAND_TOTAL;


	@ColumnInfo(name = "SHIPPING_FEES")
	@SerializedName("SHIPPING_FEES")
	private String SHIPPING_FEES;

	public OrderDataModuleHeader_Reprint() {
	}

	public OrderDataModuleHeader_Reprint(String ORDER_NO, String NO_OF_PACKAGES, String CUSTOMER_NAME, String CUSTOMER_PHONE, String ADDRESS_CITY, String ADDRESS_GOVERN,
										 String ADDRESS_DISTRICT, String ADDRESS_DETAILS, String GRAND_TOTAL, String SHIPPING_FEES) {
		this.ORDER_NO = ORDER_NO;
		this.NO_OF_PACKAGES = NO_OF_PACKAGES;
		this.CUSTOMER_NAME = CUSTOMER_NAME;
		this.CUSTOMER_PHONE = CUSTOMER_PHONE;
		this.ADDRESS_CITY = ADDRESS_CITY;
		this.ADDRESS_GOVERN = ADDRESS_GOVERN;
		this.ADDRESS_DISTRICT = ADDRESS_DISTRICT;
		this.ADDRESS_DETAILS = ADDRESS_DETAILS;
		this.GRAND_TOTAL = GRAND_TOTAL;
		this.SHIPPING_FEES = SHIPPING_FEES;
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

	public String getADDRESS_CITY() {
		return ADDRESS_CITY;
	}

	public void setADDRESS_CITY(String ADDRESS_CITY) {
		this.ADDRESS_CITY = ADDRESS_CITY;
	}

	public String getADDRESS_GOVERN() {
		return ADDRESS_GOVERN;
	}

	public void setADDRESS_GOVERN(String ADDRESS_GOVERN) {
		this.ADDRESS_GOVERN = ADDRESS_GOVERN;
	}

	public String getADDRESS_DISTRICT() {
		return ADDRESS_DISTRICT;
	}

	public void setADDRESS_DISTRICT(String ADDRESS_DISTRICT) {
		this.ADDRESS_DISTRICT = ADDRESS_DISTRICT;
	}

	public String getADDRESS_DETAILS() {
		return ADDRESS_DETAILS;
	}

	public void setADDRESS_DETAILS(String ADDRESS_DETAILS) {
		this.ADDRESS_DETAILS = ADDRESS_DETAILS;
	}

	public String getGRAND_TOTAL() {
		return GRAND_TOTAL;
	}

	public void setGRAND_TOTAL(String GRAND_TOTAL) {
		this.GRAND_TOTAL = GRAND_TOTAL;
	}

	public String getSHIPPING_FEES() {
		return SHIPPING_FEES;
	}

	public void setSHIPPING_FEES(String SHIPPING_FEES) {
		this.SHIPPING_FEES = SHIPPING_FEES;
	}
}
