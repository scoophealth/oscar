package com.indivica.olis.segments;

import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;

public class ZSHSegment implements Segment {

	@Override
	public String getSegmentHL7String() {
		try {
			Provider provider = LoggedInInfo.loggedInInfo.get().loggedInProvider;	
			return "ZSH|" + provider.getProviderNo() + "|" + provider.getLastName() + " " + provider.getFirstName();
		} catch (Exception e) {
			return "ZSH|-1|system";
		}
	}
}
