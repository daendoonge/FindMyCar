package com.example.findmycar;

public class ParkedLocation {

	private long id;
	private double latitude;
	private double longitude;
	
	public ParkedLocation (double latitude, double longitude)
	{
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}
	
	public double getLattitude(){
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
