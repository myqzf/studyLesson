package com.qzf.jackson.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties("album_type") //忽略属性album_type
public class DatasetFilter {

	private String album_id;
	private String album_title;
	private Map<String,Object> otherProperties = new HashMap<>();
	private String album_comments;
	
	@JsonCreator
	public DatasetFilter(@JsonProperty("album_id") String album_id, @JsonProperty("album_title") String album_title) {
		this.album_id = album_id;
		this.album_title = album_title;
	}
	
	public String getAlbum_id() {
		return album_id;
	}
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}
	public String getAlbum_title() {
		return album_title;
	}
	public void setAlbum_title(String album_title) {
		this.album_title = album_title;
	}
	public Map<String, Object> getOtherProperties() {
		return otherProperties;
	}
	public void setOtherProperties(Map<String, Object> otherProperties) {
		this.otherProperties = otherProperties;
	}
	//忽略此getter指定的属性。
	@JsonIgnore
	public String getAlbum_comments() {
		return album_comments;
	}
	public void setAlbum_comments(String album_comments) {
		this.album_comments = album_comments;
	}
	
	public Object get(String name) {
		return otherProperties.get(name);
	}
	@JsonAnyGetter
	public Map<String, Object> any(){
		return otherProperties;
	}
	@JsonAnySetter
	public void set(String name, Object value) {
		otherProperties.put(name, value);
	}
	
}
