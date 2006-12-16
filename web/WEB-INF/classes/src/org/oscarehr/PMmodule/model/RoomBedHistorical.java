package org.oscarehr.PMmodule.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseRoomBedHistorical;

public class RoomBedHistorical extends BaseRoomBedHistorical {

	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public RoomBedHistorical() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public RoomBedHistorical(org.oscarehr.PMmodule.model.RoomBedHistoricalPK id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public RoomBedHistorical(org.oscarehr.PMmodule.model.RoomBedHistoricalPK id, java.util.Date containEnd) {
		super(id, containEnd);
	}

	/* [CONSTRUCTOR MARKER END] */

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
