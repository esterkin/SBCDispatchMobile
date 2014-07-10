package com.edwardsterkin;

public class Event {

	private int id; 
	private String description;
	private float lat;
    private float lng; 
    private String date;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}


	@Override
	public String toString() {
		System.out.println("toString()");
		System.out.println(description + " " + date);
		return description + " " + date;
	}
	
	
}
