package com.qzf.jackson.model;

public class Bird {

	private String name;
	private String sound;
	private String habitat;
	
	public Bird(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSound() {
		return sound;
	}
	public void setSound(String sound) {
		this.sound = sound;
	}
	public String getHabitat() {
		return habitat;
	}
	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}
	@Override
	public String toString() {
		return "Bird [name=" + name + ", sound=" + sound + ", habitat=" + habitat + ", getName()=" + getName() + ", getSound()=" + getSound() + ", getHabitat()=" + getHabitat() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
}
