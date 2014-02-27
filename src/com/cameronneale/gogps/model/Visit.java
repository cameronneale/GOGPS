package com.cameronneale.gogps.model;

public class Visit {
	private int vid;
	private String vlongitude;
	private String vlatitude;
	private String date;
	private String memo;
	private int gosa;
	private String nloc;
	private int vcol;

	
	public Visit(){}
	
	public Visit(String vlongitude, String vlatitude, String date, String memo, int gosa, String nloc, int vcol){
		super();
		this.vlongitude = vlongitude;
		this.vlatitude = vlatitude;
		this.date = date;
		this.memo = memo;
		this.gosa = gosa;
		this.nloc = nloc;
		this.vcol = vcol;
		
	}
	
	@Override
	public String toString() {
		return "Visit [vid=" + vid + ", longitude=" + vlongitude + ", latitude=" + vlatitude + ", date=" + date + ", memo=" + memo + ", gosa=" + gosa + ", nloc=" + nloc + ", vcol=" + vcol + "]";
	}
	
	public int getId() {
		return this.vid;
	}
	
	public String getLongitude() {
		return this.vlongitude;
	}
	
	public String getLatitude() {
		return this.vlatitude;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public String getMemo() {
		return this.memo;
	}
	
	public int getGosa() {
		return this.gosa;
	}
	
	public String getNearestLocation() {
		return this.nloc;
	}
	
	public int getVcol() {
		return this.vcol;
	}
	
	public void setId(int vid){
		this.vid = vid;
	}
	
	public void setLongitude(String vlongitude){
		this.vlongitude = vlongitude;
	}
	
	public void setLatitude(String vlatitude){
		this.vlatitude = vlatitude;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public void setGosa(int gosa) {
		this.gosa = gosa;
	}
	
	public void setNearestLocation(String nloc) {
		this.nloc = nloc;
	}
	
	public void setVcol(int vcol) {
		this.vcol = vcol;
	}
}
