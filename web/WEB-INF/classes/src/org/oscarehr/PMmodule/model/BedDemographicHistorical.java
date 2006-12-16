package org.oscarehr.PMmodule.model;

import java.util.Calendar;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseBedDemographicHistorical;

public class BedDemographicHistorical extends BaseBedDemographicHistorical {

	private static final long serialVersionUID = 1L;

	public static BedDemographicHistorical create(BedDemographic bedDemographic) {
		BedDemographicHistorical historical = new BedDemographicHistorical();

		historical.setId(BedDemographicHistoricalPK.create(bedDemographic.getId(), bedDemographic.getReservationStart()));
		historical.setUsageEnd(Calendar.getInstance().getTime());

		return historical;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */

	public BedDemographicHistorical() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BedDemographicHistorical(org.oscarehr.PMmodule.model.BedDemographicHistoricalPK id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BedDemographicHistorical(org.oscarehr.PMmodule.model.BedDemographicHistoricalPK id, java.util.Date usageEnd) {
		super(id, usageEnd);
	}

	/* [CONSTRUCTOR MARKER END] */

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}