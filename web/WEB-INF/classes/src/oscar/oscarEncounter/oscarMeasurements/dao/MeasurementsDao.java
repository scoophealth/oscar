package oscar.oscarEncounter.oscarMeasurements.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean;
import oscar.oscarEncounter.oscarMeasurements.model.Measurements;

public interface MeasurementsDao {
	public void addMeasurements(Measurements measurements);

	public List<Date> getMeasurementsDatesByDateRange(Date date, int flag,
			String demo);

	public HashMap<String,EctMeasurementsDataBean> getMeasurementsByDate(Date date,
			String demo);
	
	public SortedMap<String,String> getPatientMeasurementTypeDescriptions(String demo);
	
	public HashMap<String,Measurements> getMeasurements(String demo, String[] types);
	
}
