package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="report")
public class Report extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="reportno")
	private Integer id;

	private String title;

	private String description;

	@Column(name="orgapplicable")
	private int orgApplicable;

	@Column(name="reporttype")
	private String reportType;

	@Column(name="dateoption")
	private String dateOption;

	@Column(name="datepart")
	private String datePart;

	@Column(name="reportgroup")
	private int reportGroup;

	private String notes;

	@Column(name="tablename")
	private String tableName;

	@Column(name="updatedby")
	private String updatedBy;

	@Column(name="updateddate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	@Column(name="sptorun")
	private String spToRun;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getTitle() {
    	return title;
    }

	public void setTitle(String title) {
    	this.title = title;
    }

	public String getDescription() {
    	return description;
    }

	public void setDescription(String description) {
    	this.description = description;
    }

	public int getOrgApplicable() {
    	return orgApplicable;
    }

	public void setOrgApplicable(int orgApplicable) {
    	this.orgApplicable = orgApplicable;
    }

	public String getReportType() {
    	return reportType;
    }

	public void setReportType(String reportType) {
    	this.reportType = reportType;
    }

	public String getDateOption() {
    	return dateOption;
    }

	public void setDateOption(String dateOption) {
    	this.dateOption = dateOption;
    }

	public String getDatePart() {
    	return datePart;
    }

	public void setDatePart(String datePart) {
    	this.datePart = datePart;
    }

	public int getReportGroup() {
    	return reportGroup;
    }

	public void setReportGroup(int reportGroup) {
    	this.reportGroup = reportGroup;
    }

	public String getNotes() {
    	return notes;
    }

	public void setNotes(String notes) {
    	this.notes = notes;
    }

	public String getTableName() {
    	return tableName;
    }

	public void setTableName(String tableName) {
    	this.tableName = tableName;
    }

	public String getUpdatedBy() {
    	return updatedBy;
    }

	public void setUpdatedBy(String updatedBy) {
    	this.updatedBy = updatedBy;
    }

	public Date getUpdatedDate() {
    	return updatedDate;
    }

	public void setUpdatedDate(Date updatedDate) {
    	this.updatedDate = updatedDate;
    }

	public String getSpToRun() {
    	return spToRun;
    }

	public void setSpToRun(String spToRun) {
    	this.spToRun = spToRun;
    }


}
