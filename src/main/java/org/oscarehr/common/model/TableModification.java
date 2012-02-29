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
@Table(name="table_modification")
public class TableModification extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="demographic_no")
	private int demographicNo;

	@Column(name="provider_no")
	private String providerNo;

	@Column(name="modification_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modificationDate;

	@Column(name="modification_type")
	private String modificationType;

	@Column(name="table_name")
	private String tableName;

	@Column(name="row_id")
	private String rowId;

	private String resultSet;

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

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public Date getModificationDate() {
    	return modificationDate;
    }

	public void setModificationDate(Date modificationDate) {
    	this.modificationDate = modificationDate;
    }

	public String getModificationType() {
    	return modificationType;
    }

	public void setModificationType(String modificationType) {
    	this.modificationType = modificationType;
    }

	public String getTableName() {
    	return tableName;
    }

	public void setTableName(String tableName) {
    	this.tableName = tableName;
    }

	public String getRowId() {
    	return rowId;
    }

	public void setRowId(String rowId) {
    	this.rowId = rowId;
    }

	public String getResultSet() {
    	return resultSet;
    }

	public void setResultSet(String resultSet) {
    	this.resultSet = resultSet;
    }


}
