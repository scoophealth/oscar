package org.oscarehr.common.model;

import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FunctionalCentre extends AbstractModel<String> {

	public static final Comparator<FunctionalCentre> ACCOUNT_ID_COMPARATOR=new Comparator<FunctionalCentre>()
	{
		public int compare(FunctionalCentre o1, FunctionalCentre o2) {
			return(o1.accountId.compareTo(o2.accountId));
		}	
	};

	@Id
	private String accountId;
    private String description;
	
	@Override
    public String getId() {
		return accountId;
	}

	public String getAccountId() {
		return accountId;
	}
	
	public String getDescription() {
		return description;
	}
}
