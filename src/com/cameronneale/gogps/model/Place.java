package com.cameronneale.gogps.model;

public class Place {
	private int id;
	private String name;
	private String description;
	private String longitude;
	private String latitude;
	private int def;
	private int color;
	
	public Place(){}
	
	public Place(String name, String description, String longitude, String latitude, int def, int color){
		super();
		this.name = name;
		this.description = description;
		this.longitude = longitude;
		this.latitude = latitude;
		this.def = def;
		this.color = color;
		
	}
	
	@Override
	public String toString() {
		return "Place [id=" + id + ", name=" + name + ", description=" + description + ", longitude=" + longitude + ", latitude=" + latitude + ", default=" + def + ", color=" + color + "]";
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDesc() {
		return this.description;
	}
	
	public String getLong() {
		return this.longitude;
	}
	
	public String getLat() {
		return this.latitude;
	}
	
	public int isDefault() {
		return this.def;
	}
	
	public int getColor() {
		return this.color;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDesc(String description) {
		this.description = description;
	}
	
	public void setLong(String longitude) {
		this.longitude = longitude;
	}
	
	public void setLat(String latitude) {
		this.latitude = latitude;
	}
	
	public void setDefault(int i) {
		this.def = i;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
}
