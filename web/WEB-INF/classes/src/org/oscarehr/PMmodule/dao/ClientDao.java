package org.oscarehr.PMmodule.dao;

import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.DemographicExt;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;

public interface ClientDao {
	
	/**
	 * Does client with given id exist
	 * 
	 * @param demographicNo id
	 * @return true if client exists
	 */
	public boolean clientExists(Integer demographicNo);
	
	public Demographic getClientByDemographicNo(Integer demographicNo);

	public List getClients();

	public List search(ClientSearchFormBean criteria);

	public Date getMostRecentIntakeADate(Integer demographicNo);

	public Date getMostRecentIntakeCDate(Integer demographicNo);

	public String getMostRecentIntakeAProvider(Integer demographicNo);

	public String getMostRecentIntakeCProvider(Integer demographicNo);

	public void saveClient(Demographic client);

	public DemographicExt getDemographicExt(Integer id);

	public List getDemographicExtByDemographicNo(Integer demographicNo);

	public DemographicExt getDemographicExt(Integer demographicNo, String key);

	public void updateDemographicExt(DemographicExt de);

	public void saveDemographicExt(Integer demographicNo, String key, String value);

	public void removeDemographicExt(Integer id);

	public void removeDemographicExt(Integer demographicNo, String key);

	public List getProgramIdByDemoNo(String demoNo);
	
}
