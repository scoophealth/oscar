package org.oscarehr.common.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FunctionalCentre extends AbstractModel<String> {

    @Id
	private String accountId;
    private String description;
	
	@Override
    public String getId() {
		return accountId;
	}

	public String getDescription() {
		return description;
	}
}
