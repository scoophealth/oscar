package org.oscarehr.common.model;

public class Mortalities {
	
	private PopulationReportStatistic mortalities;

	public Mortalities(int count, int size) {
		mortalities = new PopulationReportStatistic(count, size);
	}
	
	public int getCount() {
		return mortalities.getCount();
	}

	public String getPercent() {
		return mortalities.getPercent();
	}
	
}