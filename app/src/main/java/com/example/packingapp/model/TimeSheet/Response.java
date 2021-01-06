package com.example.packingapp.model.TimeSheet;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response{
	@SerializedName("id")
	private String id;


	@SerializedName("records")
	private List<RecordsItem> records;

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