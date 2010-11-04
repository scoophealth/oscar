package org.oscarehr.eyeform.model;

import java.text.ParseException;
import java.util.Date;

public class SpecsHistory {

	private Integer id;
	private int demographicNo;
	private String provider;
	private String status;
	private Date date;
	private String doctor;
	private String type;
	private String odSph;
	private String odCyl;
	private String odAxis;
	private String odAdd;
	private String odPrism;
	private String sdSph;
	private String osSph;
	private String osCyl;
	private String osAxis;
	private String osAdd;
	private String osPrism;
	private Date updateTime;
	private String dateStr;
	private int appointmentNo;
	
	
	
	public int getAppointmentNo() {
		return appointmentNo;
	}
	public void setAppointmentNo(int appointmentNo) {
		this.appointmentNo = appointmentNo;
	}
	public String getOsSph() {
		return osSph;
	}
	public void setOsSph(String osSph) {
		this.osSph = osSph;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOdSph() {
		return odSph;
	}
	public void setOdSph(String odSph) {
		this.odSph = odSph;
	}
	public String getOdCyl() {
		return odCyl;
	}
	public void setOdCyl(String odCyl) {
		this.odCyl = odCyl;
	}
	public String getOdAxis() {
		return odAxis;
	}
	public void setOdAxis(String odAxis) {
		this.odAxis = odAxis;
	}
	public String getOdAdd() {
		return odAdd;
	}
	public void setOdAdd(String odAdd) {
		this.odAdd = odAdd;
	}
	public String getOdPrism() {
		return odPrism;
	}
	public void setOdPrism(String odPrism) {
		this.odPrism = odPrism;
	}
	public String getSdSph() {
		return sdSph;
	}
	public void setSdSph(String sdSph) {
		this.sdSph = sdSph;
	}
	public String getOsCyl() {
		return osCyl;
	}
	public void setOsCyl(String osCyl) {
		this.osCyl = osCyl;
	}
	public String getOsAxis() {
		return osAxis;
	}
	public void setOsAxis(String osAxis) {
		this.osAxis = osAxis;
	}
	public String getOsAdd() {
		return osAdd;
	}
	public void setOsAdd(String osAdd) {
		this.osAdd = osAdd;
	}
	public String getOsPrism() {
		return osPrism;
	}
	public void setOsPrism(String osPrism) {
		this.osPrism = osPrism;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getDateStr() {
		if(getDate()==null) return "";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(getDate());
	}
	
	public void setDateStr(String d) {		
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		try {
			setDate(sdf.parse(d));
		}catch(ParseException e) {}
	}	
	
	
}
