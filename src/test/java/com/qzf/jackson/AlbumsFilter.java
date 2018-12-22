package com.qzf.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.qzf.jackson.model.DatasetFilter;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.PUBLIC_ONLY)
public class AlbumsFilter {

	private String title;
	private DatasetFilter[] datasetFilter;
	public  String total_pages;
	
	protected String getTotal_pages() {
		return total_pages;
	}
	public String getTitle() {
		return title;
	}
	//这个getter方法用于“dataset”属性
	@JsonProperty("dataset")
	public DatasetFilter[] getDatasetFilter() {
		return datasetFilter;
	}
	
}
