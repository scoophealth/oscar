package oscar.oscarEncounter.oscarMeasurements.dao;

import java.util.List;

import org.oscarehr.common.model.Demographic;



public interface DemographicDao {
	
	public List<Demographic> getActiveDemosByHealthCardNo(String hcn, String hcnType); 

}
