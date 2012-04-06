package org.oscarehr.common.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="providersite")
public class ProviderSite extends AbstractModel<ProviderSitePK> {

	@EmbeddedId
	private ProviderSitePK id;

	public ProviderSitePK getId() {
    	return id;
    }

	public void setId(ProviderSitePK id) {
    	this.id = id;
    }
}
