package org.oscarehr.common.service;

import java.util.Map;

import org.oscarehr.common.model.Mortalities;
import org.oscarehr.common.model.ReportStatistic;
import org.oscarehr.common.model.ShelterPopulation;
import org.oscarehr.common.model.ShelterUsage;

public interface PopulationReportManager {

	public ShelterPopulation getShelterPopulation();
	public ShelterUsage getShelterUsage();
	public Mortalities getMortalities();
	public Map<String, ReportStatistic> getMajorMedicalConditions();
	public Map<String, ReportStatistic> getMajorMentalIllnesses();
	public Map<String, ReportStatistic> getSeriousMedicalConditions();
	public Map<String, Map<String, String>> getCategoryCodeDescriptions();
	
}
