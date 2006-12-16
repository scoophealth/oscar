package org.oscarehr.PMmodule.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseRoomBedHistoricalPK;

public class RoomBedHistoricalPK extends BaseRoomBedHistoricalPK {
	
	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	
	public RoomBedHistoricalPK() {
	}

	public RoomBedHistoricalPK(java.lang.Integer roomId, java.lang.Integer bedId, java.util.Date containStart) {
		super(roomId, bedId, containStart);
	}
	
	/* [CONSTRUCTOR MARKER END] */
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
