package org.oscarehr.PMmodule.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseBedDemographicStatus;

public class BedDemographicStatus extends BaseBedDemographicStatus {

	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */

	public BedDemographicStatus() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BedDemographicStatus(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BedDemographicStatus(java.lang.Integer id, java.lang.String name, java.lang.Integer duration, boolean m_default) {
		super(id, name, duration, m_default);
	}

	/* [CONSTRUCTOR MARKER END] */

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}