package com.example.packingapp.model.TimeSheet;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response{



	@SerializedName("id")
	private String id;

	@SerializedName("H_records")
	private List<RecordsHeader> recordsHeader;


	@SerializedName("records")
	private List<RecordsItem> records;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<RecordsHeader> getRecordsHeader() {
		return recordsHeader;
	}

	public void setRecordsHeader(List<RecordsHeader> recordsHeader) {
		this.recordsHeader = recordsHeader;
	}

	public void setRecords(List<RecordsItem> records){
		this.records = records;
	}

	public List<RecordsItem> getRecords(){
		return records;
	}
}