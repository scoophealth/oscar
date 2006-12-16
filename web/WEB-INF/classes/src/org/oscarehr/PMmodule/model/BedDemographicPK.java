package org.oscarehr.PMmodule.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseBedDemographicPK;

public class BedDemographicPK extends BaseBedDemographicPK {
	
	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	
	public BedDemographicPK() {
	}

	public BedDemographicPK(java.lang.Integer demographicNo, java.lang.Integer bedId) {
		super(demographicNo, bedId);
	}
	
	/* [CONSTRUCTOR MARKER END] */
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}