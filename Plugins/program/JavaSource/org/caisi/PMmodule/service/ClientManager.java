package org.caisi.PMmodule.service;

import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;

import org.caisi.PMmodule.dao.Database_Service;
import org.caisi.PMmodule.model.ClientReferral;
import org.caisi.PMmodule.model.Demographic;
import org.caisi.PMmodule.model.Formintakea;
import org.caisi.PMmodule.web.formbean.ClientSearchFormBean;


public interface ClientManager 
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
	
	public List getAllClientsWithinProgramDomain(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List searchedClientIds,
			int rowCountPass, int totalRowDisplay);
	
	public List getMatchedClientsWithinProgramDomain(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List searchedClientIds, 
			String clientFirstName, String clientSurname,
	        String clientDateOfBirth, String healthCardNum, String healthCardVer, 
	        String searchForClient, String retrieveAll, 
	        int rowCountPass, int totalRowDisplay);

	public List getAllClientsOutsideFacilityDomainWithSharingOn(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List facilityDomainClientIds,
			int rowCountPass, int totalRowDisplay);

	public List getAllClientsWithinFacilityDomainAndOutsideProgramDomain(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List programDomainClientIds, List facilityDomainClientIds,
			int rowCountPass, int totalRowDisplay);
	
	public List getMatchedClientsWithinFacilityDomainAndOutsideProgramDomain(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List programDomainClientIds, List facilityDomainClientIds,
			String clientFirstName, String clientSurname,
			String clientDateOfBirth, String healthCardNum, String healthCardVer, 
			String searchForClient, String retrieveAll, 
			int rowCountPass, int totalRowDisplay);
	
	public List getMatchedClientsOutsideFacilityDomainWithSharingOn(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List facilityDomainClientIds,
			String clientFirstName, String clientSurname,
			String clientDateOfBirth, String healthCardNum, String healthCardVer, 
			String searchForClient, String retrieveAll, 
			int rowCountPass, int totalRowDisplay);
	
	public List getAllClientsOutsideDomainWithSharingOff(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List groupDomainClientIds,
			int rowCountPass, int totalRowDisplay);

	public List getMatchedClientsOutsideDomainWithSharingOff(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List groupDomainClientIds,
			String clientFirstName, String clientSurname,
			String clientDateOfBirth, String healthCardNum, String healthCardVer, 
			String searchForClient, String retrieveAll, 
			int rowCountPass, int totalRowDisplay);
	
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

	public List search(ClientSearchFormBean criteria);

	public java.util.Date getMostRecentIntakeADate(String demographicNo);
	
	public java.util.Date getMostRecentIntakeBDate(String demographicNo);

	public java.util.Date getMostRecentIntakeCDate(String demographicNo);

	/* V2.0 */
	public List getReferrals(String clientId);
	public List getActiveReferrals(String clientId);
	public ClientReferral getClientReferral(String id);
	public void saveClientReferral(ClientReferral referral);
}

