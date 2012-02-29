package org.oscarehr.common.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="provider_facility")
public class ProviderFacility extends AbstractModel<ProviderFacilityPK> {

	@EmbeddedId
	ProviderFacilityPK id;

	public ProviderFacilityPK getId() {
    	return id;
    }

	public void setId(ProviderFacilityPK id) {
    	this.id = id;
    }


}
