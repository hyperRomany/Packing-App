package com.example.packingapp.model.TimeSheet;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response{
	@SerializedName("ORDER_NO")
	private String ORDER_NO;
	@SerializedName("CUSTOMER_NAME")
	private String CUSTOMER_NAME;
	@SerializedName("CUSTOMER_PHONE")
	private String CUSTOMER_PHONE;
	@SerializedName("GRAND_TOTAL")
	private String GRAND_TOTAL;


	@SerializedName("id")
	private String id;


	@SerializedName("records")
	private List<RecordsItem> records;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setRecords(List<RecordsItem> records){
		this.records = records;
	}

	public List<RecordsItem> getRecords(){
		return records;
	}
}