package org.oscarehr.common.service;

import java.util.Map;

import org.oscarehr.common.model.Mortalities;
import org.oscarehr.common.model.PopulationReportStatistic;
import org.oscarehr.common.model.ShelterPopulation;
import org.oscarehr.common.model.ShelterUsage;

public interface PopulationReportManager {

	public ShelterPopulation getShelterPopulation();
	public ShelterUsage getShelterUsage();
	public Mortalities getMortalities();
	public Map<String, PopulationReportStatistic> getMajorMedicalConditions();
	public Map<String, PopulationReportStatistic> getMajorMentalIllnesses();
	public Map<String, PopulationReportStatistic> getSeriousMedicalConditions();
	public Map<String, Map<String, String>> getCategoryCodeDescriptions();
	
}
