package com.qzf.jackson.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Lion extends Animal{

	private String name;

	@JsonCreator
	public Lion(@JsonProperty("name") String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public String getSound() {
		return "Roar";
	}
	
	public String getType() {
		return "carnviorous";
	}
	
	public boolean isEndangered() {
		return true;
	}

//	@Override
//	public String toString() {
//		return "Lion [name=" + name + ", sound=" + sound + ", type=" + type + ", endangered=" + endangered + ", getName()=" + getName() + ", getSound()=" + getSound() + ", getType()=" + getType() + ", isEndangered()=" + isEndangered() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
//	}
	@Override
	public String toString() {
		return "Lion " + name;
	}

	
}
