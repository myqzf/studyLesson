package com.qzf.jackson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qzf.jackson.model.Dataset;

public class Album {

	public Album() {
		
	}
	public Album(String title) {
		this.title = title;
	}
	
	private String title;
	private String[] links;
	private List<String> songs;
	private Artist artist;
	private Map<String, String> musicians = new HashMap<String, String>();
	
	private Dataset[] dataset;
	
	public String getTitle() {
		return title;
	}
	public String[] getLinks() {
		return links;
	}
	public void setLinks(String[] links) {
		this.links = links;
	}
	public List<String> getSongs() {
		return songs;
	}
	public void setSongs(List<String> songs) {
		this.songs = songs;
	}
	public Artist getArtist() {
		return artist;
	}
	public void setArtist(Artist artist) {
		this.artist = artist;
	}
	public Map<String, String> getMusicians() {
		return Collections.unmodifiableMap(musicians);
	}
	public void addMusician(String key, String value) {
		this.musicians.put(key, value);
	}
	public Dataset[] getDataset() {
		return dataset;
	}
	public void setDataset(Dataset[] dataset) {
		this.dataset = dataset;
	}
	
	
}

class Artist {
	public String name;
	public Date birthDate;
	public int age;
	public String homeTown;
	public List<String> awardsWon = new ArrayList<>();
}
