package org.oscarehr.hospitalReportManager.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.oscarehr.common.model.AbstractModel;

@Entity
public class HRMCategory extends AbstractModel<Integer> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String categoryName;
	private String subClassNameMnemonic;

	public HRMCategory() {

	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSubClassNameMnemonic() {
		return (subClassNameMnemonic);
	}

	public void setSubClassNameMnemonic(String subClassNameMnemonic) {
		this.subClassNameMnemonic = subClassNameMnemonic;
	}

}
