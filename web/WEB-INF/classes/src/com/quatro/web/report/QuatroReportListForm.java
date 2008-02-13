package com.quatro.web.report;

import java.util.*;
import org.apache.struts.action.ActionForm;
import com.quatro.util.*;
import com.quatro.model.*;

public class QuatroReportListForm extends ActionForm{
	List reportGroups;
	String provider;
	
	public List getReportGroups() {
		return reportGroups;
	}

	public void setReportGroups(List reportGroups) {
		this.reportGroups = reportGroups;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
	
}
