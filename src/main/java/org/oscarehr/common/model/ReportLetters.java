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
@Table(name="report_letters")
public class ReportLetters extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private Integer id;

	@Column(name="provider_no")
	private String providerNo;

	@Column(name="report_name")
	private String reportName;

	@Column(name="file_name")
	private String fileName;

	@Column(name="report_file")
	private String reportFile;

	@Column(name="date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;

	private String archive;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public String getReportName() {
    	return reportName;
    }

	public void setReportName(String reportName) {
    	this.reportName = reportName;
    }

	public String getFileName() {
    	return fileName;
    }

	public void setFileName(String fileName) {
    	this.fileName = fileName;
    }

	public String getReportFile() {
    	return reportFile;
    }

	public void setReportFile(String reportFile) {
    	this.reportFile = reportFile;
    }

	public Date getDateTime() {
    	return dateTime;
    }

	public void setDateTime(Date dateTime) {
    	this.dateTime = dateTime;
    }

	public String getArchive() {
    	return archive;
    }

	public void setArchive(String archive) {
    	this.archive = archive;
    }


}
