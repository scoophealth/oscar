package org.oscarehr.common.dao;

import java.util.SortedSet;

public interface PopulationReportDAO {
	
	public static final int LOW = 0;
	public static final int MEDIUM = 1;
	public static final int HIGH = 2;

	public int getCurrentPopulationSize();
	public int getCurrentAndHistoricalPopulationSize(int numYears);
	
	public int[] getUsages(int numYears);
	public int getMortalities(int numYears);
	public int getPrevalence(SortedSet<String> icd10Codes);
	public int getIncidence(SortedSet<String> icd10Codes);
	
}
