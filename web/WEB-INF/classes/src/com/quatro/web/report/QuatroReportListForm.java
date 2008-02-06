package com.quatro.web.report;

import java.util.*;
import org.apache.struts.action.ActionForm;
import com.quatro.util.*;
import com.quatro.model.*;

public class QuatroReportListForm extends ActionForm{
	List reports;
	String provider;
	
	public List getReports() {
		return reports;
	}

	public void setReports(List reports) {
		this.reports = reports;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
	
}
