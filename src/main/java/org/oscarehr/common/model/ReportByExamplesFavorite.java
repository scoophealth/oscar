package org.oscarehr.common.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="reportByExamplesFavorite")
public class ReportByExamplesFavorite  extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String providerNo;

	private String query;

	private String name;

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

	public String getQuery() {
    	return query;
    }

	public void setQuery(String query) {
    	this.query = query;
    }

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }



}
