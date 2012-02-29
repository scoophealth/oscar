package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="reportConfig")
public class ReportConfig extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="report-id")
	private int reportId;

	private String name;

	private String caption;

	@Column(name="order_no")
	private int orderNo;

	@Column(name="table_name")
	private String tableName;

	private String save;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getReportId() {
    	return reportId;
    }

	public void setReportId(int reportId) {
    	this.reportId = reportId;
    }

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public String getCaption() {
    	return caption;
    }

	public void setCaption(String caption) {
    	this.caption = caption;
    }

	public int getOrderNo() {
    	return orderNo;
    }

	public void setOrderNo(int orderNo) {
    	this.orderNo = orderNo;
    }

	public String getTableName() {
    	return tableName;
    }

	public void setTableName(String tableName) {
    	this.tableName = tableName;
    }

	public String getSave() {
    	return save;
    }

	public void setSave(String save) {
    	this.save = save;
    }


}
