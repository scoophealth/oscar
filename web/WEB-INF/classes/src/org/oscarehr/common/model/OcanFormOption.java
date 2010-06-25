package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 * This entity represents the CDS options on the cds form. The CdsFormVersion should be something like "4.05" or possibly just "4" for something like "CDS-MH 4.05". We can use the full name but I currently don't see the need. We should probably start with
 * "4" assuming that all "4.x" versions are compatible. The CdsDataCategory is as an example "016-06" for "Eating Disorders", to select all "Disorders" it will be expected to use "select * from CdsFormOptions where CdsDataCatrogy like '016-%'.
 */
@Entity
public class OcanFormOption extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String ocanFormVersion = null;
	private String ocanDataCategory = null;
	private String ocanDataCategoryValue = null;
	private String ocanDataCategoryName = null;

	public Integer getId() {
		return id;
	}

	public String getOcanFormVersion() {
		return ocanFormVersion;
	}

	public String getOcanDataCategory() {
		return ocanDataCategory;
	}

	public String getOcanDataCategoryValue() {
		return ocanDataCategoryValue;
	}
	
	public String getOcanDataCategoryName() {
		return ocanDataCategoryName;
	}

	public boolean equals(OcanFormOption o) {
		try {
			return (id != null && id.intValue() == o.id.intValue());
		} catch (Exception e) {
			return (false);
		}
	}

	public int hashCode() {
		return (id != null ? id.hashCode() : 0);
	}

	@PreRemove
	protected void jpaPreventDelete() {
		throw (new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

	@PreUpdate
	protected void jpaPreventUpdate() {
		throw (new UnsupportedOperationException("Update is not allowed for this type of item."));
	}

}
