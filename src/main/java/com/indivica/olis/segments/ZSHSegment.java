/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

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
