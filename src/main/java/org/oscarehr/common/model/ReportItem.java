package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="reportItem")
public class ReportItem extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="report_name")
	private String reportName;

	private int status;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getReportName() {
    	return reportName;
    }

	public void setReportName(String reportName) {
    	this.reportName = reportName;
    }

	public int getStatus() {
    	return status;
    }

	public void setStatus(int status) {
    	this.status = status;
    }


}
