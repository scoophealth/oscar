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
@Table(name="labRequestReportLink")
public class LabRequestReportLink extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="request_table")
	private String requestTable;

	@Column(name="request_id")
	private int requestId;

	@Column(name="request_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestDate;

	@Column(name="report_table")
	private String reportTable;

	@Column(name="report_id")
	private int reportId;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getRequestTable() {
    	return requestTable;
    }

	public void setRequestTable(String requestTable) {
    	this.requestTable = requestTable;
    }

	public int getRequestId() {
    	return requestId;
    }

	public void setRequestId(int requestId) {
    	this.requestId = requestId;
    }

	public Date getRequestDate() {
    	return requestDate;
    }

	public void setRequestDate(Date requestDate) {
    	this.requestDate = requestDate;
    }

	public String getReportTable() {
    	return reportTable;
    }

	public void setReportTable(String reportTable) {
    	this.reportTable = reportTable;
    }

	public int getReportId() {
    	return reportId;
    }

	public void setReportId(int reportId) {
    	this.reportId = reportId;
    }


}
