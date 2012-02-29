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
@Table(name="recycle_bin")
public class RecycleBinBilling extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="provider_no")
	private String providerNo;

	@Column(name="table_name")
	private String tableName;

	@Column(name="table_content")
	private String tableContent;

	@Column(name="updatedatetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDateTime;

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

	public String getTableName() {
    	return tableName;
    }

	public void setTableName(String tableName) {
    	this.tableName = tableName;
    }

	public String getTableContent() {
    	return tableContent;
    }

	public void setTableContent(String tableContent) {
    	this.tableContent = tableContent;
    }

	public Date getUpdateDateTime() {
    	return updateDateTime;
    }

	public void setUpdateDateTime(Date updateDateTime) {
    	this.updateDateTime = updateDateTime;
    }


}
