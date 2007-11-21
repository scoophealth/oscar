package org.oscarehr.PMmodule.web.reports.custom;

import java.util.ArrayList;
import java.util.List;

public class UCRConfiguration {
	private List<DataSource> dataSources = new ArrayList<DataSource>();

	public List<DataSource> getDataSources() {
		return dataSources;
	}

	public void setDataSources(List<DataSource> dataSources) {
		this.dataSources = dataSources;
	}
	
	public void addDataSource(DataSource ds) {
		dataSources.add(ds);
	}
	
}
