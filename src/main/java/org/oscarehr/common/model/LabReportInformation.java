package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="labReportInformation")
public class LabReportInformation extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="location_id")
	private String locationId;

	@Column(name="print_date")
	private String printDate;

	@Column(name="print_time")
	private String printTime;

	@Column(name="total_BType")
	private String totalBType;

	@Column(name="total_CType")
	private String totalCType;

	@Column(name="total_DType")
	private String totalDType;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getLocationId() {
    	return locationId;
    }

	public void setLocationId(String locationId) {
    	this.locationId = locationId;
    }

	public String getPrintDate() {
    	return printDate;
    }

	public void setPrintDate(String printDate) {
    	this.printDate = printDate;
    }

	public String getPrintTime() {
    	return printTime;
    }

	public void setPrintTime(String printTime) {
    	this.printTime = printTime;
    }

	public String getTotalBType() {
    	return totalBType;
    }

	public void setTotalBType(String totalBType) {
    	this.totalBType = totalBType;
    }

	public String getTotalCType() {
    	return totalCType;
    }

	public void setTotalCType(String totalCType) {
    	this.totalCType = totalCType;
    }

	public String getTotalDType() {
    	return totalDType;
    }

	public void setTotalDType(String totalDType) {
    	this.totalDType = totalDType;
    }


}
