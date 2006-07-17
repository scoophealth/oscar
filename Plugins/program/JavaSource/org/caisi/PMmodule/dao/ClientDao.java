

package org.caisi.PMmodule.dao;


import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.caisi.PMmodule.model.Demographic;
import org.caisi.PMmodule.model.Formintakea;
import org.caisi.PMmodule.web.formbean.ClientSearchFormBean;

//###############################################################################

public interface ClientDao
{
	public Demographic getClient(String firstName, String lastName);

	public Demographic getClientByDemographicNo(String demographicNo);

	public Demographic getClient(String queryStr);

	public List getClients();

	public int updateDemographicPmmSharingValue(
			Database_Service databaseService, DataSource dataSource, 
			String demographicNo, String sharingValue);
	
	public List getAllSharingClients();
	
	public List getAllSharingOffClients();
	
	public List getAllSharingClientsFromInnerJoinTables(
			Database_Service databaseService, DataSource dataSource);
	
	public List getAllSharingOffClientsFromInnerJoinTables(
			Database_Service databaseService, DataSource dataSource);
	
	public List getClients(String queryStr);
	    
	public List getClients(Database_Service databaseService, 
			               DataSource dataSource, String queryStr);
	
	public String getDemographicNo(String firstName, String lastName);

	public List getDemographicNos(String firstName, String lastName);

	public boolean isClientExistAlready(String firstName, String lastName);

	public boolean isSharingOn(String demographicNo);
	
	public int getTotalRecordCount(Database_Service databaseService, DataSource dataSource);

	public Demographic setClientObj(Demographic clientInfo, ResultSet rs);
	
	public String getQuerySqlStr(String clientFirstName, String clientSurname,
			                     String clientDateOfBirth, String healthCardNum,
			                     String healthCardVer, String retrieveAll);

	public String getQuerySqlStr(String clientFirstName, String clientSurname,
			                     String clientDateOfBirth, String healthCardNum,
			                     String healthCardVer, String retrieveAll, 
			                     int rowCountPass, int totalRowDisplay);

	public boolean addClient(Demographic client);

	public boolean addClientFromFormintakea(Database_Service databaseService, DataSource dataSource, Formintakea intakeA);
	
	public boolean addClientIntoFormintakea(Database_Service databaseService, DataSource dataSource, Demographic client);

	public boolean updateClientDateOfBirth(Database_Service databaseService, DataSource dataSource, Demographic client);

	public boolean updateClientHealthCardNum(
			Database_Service databaseService, DataSource dataSource, 
			Demographic client, String healthCardNum, String healthCardVer); 

	public boolean updateClientDoctor(
			Database_Service databaseService, DataSource dataSource, Demographic client); 

	public boolean updateClient(Demographic client);
	
	public boolean removeClient(String demographicNo);
	
	/* Marc's V2 methods */
	
	public List search(ClientSearchFormBean criteria);

	public Date getMostRecentIntakeADate(Long demographicNo);
	
	public Date getMostRecentIntakeBDate(Long demographicNo);

	public Date getMostRecentIntakeCDate(Long demographicNo);
	
	public void saveClient(Demographic client);
	
}

