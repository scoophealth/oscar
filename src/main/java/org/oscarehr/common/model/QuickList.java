package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "quickList")
public class QuickList extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	private String quickListName;

	private int createdByProvider;

	private String dxResearchCode;

	private String codingSystem;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getQuickListName() {
    	return quickListName;
    }

	public void setQuickListName(String quickListName) {
    	this.quickListName = quickListName;
    }

	public int getCreatedByProvider() {
    	return createdByProvider;
    }

	public void setCreatedByProvider(int createdByProvider) {
    	this.createdByProvider = createdByProvider;
    }

	public String getDxResearchCode() {
    	return dxResearchCode;
    }

	public void setDxResearchCode(String dxResearchCode) {
    	this.dxResearchCode = dxResearchCode;
    }

	public String getCodingSystem() {
    	return codingSystem;
    }

	public void setCodingSystem(String codingSystem) {
    	this.codingSystem = codingSystem;
    }


}
