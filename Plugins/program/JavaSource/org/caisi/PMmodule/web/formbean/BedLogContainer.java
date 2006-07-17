package org.caisi.PMmodule.web.formbean;

import java.util.Iterator;
import java.util.List;

import org.caisi.PMmodule.model.BedLog;

public class BedLogContainer {
	
	private List bedlogs;
	
	public BedLogContainer(List bedlogs) {
		this.bedlogs = bedlogs;
	}
	
	public BedLog getBedLog(Integer clientId, String time) {
		for(Iterator iter=bedlogs.iterator();iter.hasNext();) {
			BedLog bedlog = (BedLog)iter.next();
			if(bedlog.getDemographicNo().intValue() == clientId.intValue() && time.equals(bedlog.getTime())) {
				return bedlog;
			}
		}
		return null;
	}
}
