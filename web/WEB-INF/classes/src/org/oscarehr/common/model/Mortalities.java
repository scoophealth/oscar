package org.oscarehr.common.model;

public class Mortalities {
	
	private ReportStatistic mortalities;

	public Mortalities(int count, int size) {
		mortalities = new ReportStatistic(count, size);
	}
	
	public int getCount() {
		return mortalities.getCount();
	}

	public String getPercent() {
		return mortalities.getPercent();
	}
	
}