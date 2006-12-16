package org.oscarehr.PMmodule.model;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseBedDemographicHistoricalPK;

public class BedDemographicHistoricalPK extends BaseBedDemographicHistoricalPK {
	
	private static final long serialVersionUID = 1L;
	
	public static BedDemographicHistoricalPK create(BedDemographicPK bedDemographicPK, Date usageStart) {
		BedDemographicHistoricalPK historicalPK = new BedDemographicHistoricalPK();
		
		historicalPK.setBedId(bedDemographicPK.getBedId());
		historicalPK.setDemographicNo(bedDemographicPK.getDemographicNo());
		historicalPK.setUsageStart(usageStart);
		
		return historicalPK;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	
	public BedDemographicHistoricalPK() {
	}

	public BedDemographicHistoricalPK(java.lang.Integer bedId, java.lang.Integer demographicNo, java.util.Date usageStart) {
		super(bedId, demographicNo, usageStart);
	}
	
	/* [CONSTRUCTOR MARKER END] */
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}