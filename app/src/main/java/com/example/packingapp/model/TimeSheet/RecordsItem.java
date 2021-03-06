package com.example.packingapp.model.TimeSheet;

import com.google.gson.annotations.SerializedName;

public class RecordsItem {
	@SerializedName("OUTBOUND_DELIVERY")
	private String OUTBOUND_DELIVERY;
	@SerializedName("STATUS")
	private String STATUS;
	@SerializedName("NO_OF_PACKAGES")
	private String NO_OF_PACKAGES;
	@SerializedName("CUSTOMER_NAME")
	private String CUSTOMER_NAME;
	@SerializedName("TRACKING_NO")
	private String TRACKING_NO;
	@SerializedName("ORDER_NO")
	private String ORDER_NO;
	@SerializedName("ADDRESS_DETAILS")
	private String ADDRESS_DETAILS;
	@SerializedName("ITEM_PRICE")
	private String ITEM_PRICE;
	@SerializedName("CUSTOMER_PHONE")
	private String CUSTOMER_PHONE;

	@SerializedName("Payment_Method")
	private String Payment_Method;

	@SerializedName("Delivery_Method")
	private String Delivery_Method;

	public String getPayment_Method() {
		return Payment_Method;
	}

	public void setPayment_Method(String payment_Method) {
		Payment_Method = payment_Method;
	}

	public String getDelivery_Method() {
		return Delivery_Method;
	}

	public void setDelivery_Method(String delivery_Method) {
		Delivery_Method = delivery_Method;
	}



	public String getCUSTOMER_PHONE() {
		return CUSTOMER_PHONE;
	}

	public void setCUSTOMER_PHONE(String CUSTOMER_PHONE) {
		this.CUSTOMER_PHONE = CUSTOMER_PHONE;
	}

	public String getOUTBOUND_DELIVERY() {
		return OUTBOUND_DELIVERY;
	}

	public void setOUTBOUND_DELIVERY(String OUTBOUND_DELIVERY) {
		this.OUTBOUND_DELIVERY = OUTBOUND_DELIVERY;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String STATUS) {
		this.STATUS = STATUS;
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

	public String getTRACKING_NO() {
		return TRACKING_NO;
	}

	public void setTRACKING_NO(String TRACKING_NO) {
		this.TRACKING_NO = TRACKING_NO;
	}

	public String getORDER_NO() {
		return ORDER_NO;
	}

	public void setORDER_NO(String ORDER_NO) {
		this.ORDER_NO = ORDER_NO;
	}

	public String getADDRESS_DETAILS() {
		return ADDRESS_DETAILS;
	}

	public void setADDRESS_DETAILS(String ADDRESS_DETAILS) {
		this.ADDRESS_DETAILS = ADDRESS_DETAILS;
	}

	public String getITEM_PRICE() {
		return ITEM_PRICE;
	}

	public void setITEM_PRICE(String ITEM_PRICE) {
		this.ITEM_PRICE = ITEM_PRICE;
	}
}
