package com.qzf.jackson.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BirdMixIn {

	public BirdMixIn(@JsonProperty("name") String name) {
	
	}
	
	@JsonProperty("sound")
	abstract String getSound();
	
	@JsonProperty("habitat")
	abstract String getHabitat();
}
