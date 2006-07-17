package org.caisi.PMmodule.service.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.PMmodule.dao.ClientDao;
import org.caisi.PMmodule.dao.ClientReferralDAO;
import org.caisi.PMmodule.dao.Database_Service;
import org.caisi.PMmodule.model.ClientReferral;
import org.caisi.PMmodule.model.Demographic;
import org.caisi.PMmodule.model.Formintakea;
import org.caisi.PMmodule.model.ProgramQueue;
import org.caisi.PMmodule.service.ClientManager;
import org.caisi.PMmodule.service.ProgramQueueManager;
import org.caisi.PMmodule.web.formbean.ClientSearchFormBean;

public class ClientManagerImpl implements ClientManager
{
	private static Log log = LogFactory.getLog(ClientManagerImpl.class);
	private ClientDao dao;
	private ClientReferralDAO referralDAO;
	private ProgramQueueManager queueManager;
	
	
	public void setClientDao(ClientDao dao)
	{
		this.dao = dao;
	}
	
	public void setClientReferralDAO(ClientReferralDAO dao) {
		this.referralDAO = dao;
	}
	
	public void setProgramQueueManager(ProgramQueueManager mgr) {
		this.queueManager = mgr;
	}
	
	public Demographic getClient(String firstName, String lastName)
	{
		return dao.getClient(firstName, lastName);
	}

	public Demographic getClientByDemographicNo(String demographicNo)
	{
		return dao.getClientByDemographicNo(demographicNo);
	}

	public Demographic getClient(String queryStr)
	{
		return dao.getClient(queryStr);
	}

	public List getClients()
	{
		return dao.getClients();
	}

	public List getClients(String queryStr)
	{
		return dao.getClients(queryStr);
	}
	
	public int updateDemographicPmmSharingValue(
			Database_Service databaseService, DataSource dataSource, 
			String demographicNo, String sharingValue)
	{
		return dao.updateDemographicPmmSharingValue(
				databaseService, dataSource, demographicNo, sharingValue);
	}

	public List getAllSharingClients()
	{
		return dao.getAllSharingClients();
	}
	
	public List getAllSharingOffClients()
	{
		return dao.getAllSharingOffClients();
	}
	
//	################################################################################
	/*
	 * Used to get matched clients within domain when retrieveAll is 'Y'
	 */
	public List getAllClientsWithinProgramDomain(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List searchedClientIds,
			int rowCountPass, int totalRowDisplay) 
	{
	/*
	 *-	Use  provider_agency_program  table to  ensure that  providers logged in can only view  his/her  Program Domain's  clients.
	 *--	use   providerNo   to filter out all the   programIds   that this provider  is associated to.
	 *--	use   programIds to filter out all the clients that are in them.
	 *--	use   clientMgr.isSharingOn(demographicNo)    to  check  whether  outside  GroupIds  clients  can be viewed by this provider
	 *
	 */
	    if( clientMgr == null  ||   
	    	searchedClientIds == null  ||  searchedClientIds.size() <= 0)
	    {
	        return null;
	    }

	    Demographic client = new Demographic();
	    List allClientsWithinProgramDomain = new ArrayList();
	    List filteredForDisplay = new ArrayList();
	    int maxDisplayNum = rowCountPass + totalRowDisplay;
	    
	    String clientId = "";
	    boolean resetOK = false;
	    int numAdded = 0;

	    int columns = databaseService.colCount(dataSource, "demographic");

	    if(columns <= 0)
	    {
	        return null;
	    }
	    
		for(int i=0;  i < searchedClientIds.size(); i++)
		{
			try
			{
					clientId = ((Long)searchedClientIds.get(i)).toString();
	    			client = clientMgr.getClientByDemographicNo(clientId);
	   
	    			if(client == null)
	    			{
	    				throw  new Exception("clientId: " + searchedClientIds.get(i)+ " is null.");
	    			}
	    			
	 			
	    			allClientsWithinProgramDomain.add(client);
	    			numAdded++;
	    			
			}
			catch(Exception ex)
			{
//				log.warn(ex);
			}
		}//end of  for(int i=0;  i < searchedClientIds.size(); i++)
		
		for(int i=rowCountPass;  
		    allClientsWithinProgramDomain != null  && 
			allClientsWithinProgramDomain.size() > 0  &&  
			i < maxDisplayNum  && 
			i < allClientsWithinProgramDomain.size(); 
			i++)
		{
			
			filteredForDisplay.add(allClientsWithinProgramDomain.get(i));
			
		}//end of for(...)

	    return filteredForDisplay;
	}

//	################################################################################
	/*
	 * Used to get matched clients within domain when searchForClient is 'Y'
	 */
	public List getMatchedClientsWithinProgramDomain(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List searchedClientIds, 
			String clientFirstName, String clientSurname,
	        String clientDateOfBirth, String healthCardNum, String healthCardVer, 
	        String searchForClient, String retrieveAll, 
	        int rowCountPass, int totalRowDisplay) 
	{
	/*
	 *-	Use  provider_role_program  table to  ensure that  providers logged in can only view  his/her  Group�s  clients.
	 *--	use   providerNo   to filter out all the   groupIds   that this provider  is associated to.
	 *--	use   groupIds   to filter out all the programIds  that this provider can view
	 *--	use   programIds to filter out all the clients that are in them.
	 *--	use   clientMgr.isSharingOn(demographicNo)    to  check  whether  outside  GroupIds  clients  can be viewed by this provider
	 *
	 */
	    if( clientMgr == null  ||   
	    	searchedClientIds == null  ||  searchedClientIds.size() <= 0)
	    {
	        return null;
	    }

	    Demographic client = new Demographic();
	    List allClientsWithinProgramDomain = new ArrayList();
	    List filteredForDisplay = new ArrayList();
	    int maxDisplayNum = rowCountPass + totalRowDisplay;
	    
	    String clientId = "";
	    boolean resetOK = false;
	    int numAdded = 0;

	    int columns = databaseService.colCount(dataSource, "demographic");

	    if(columns <= 0)
	    {
	        return null;
	    }
	    
		for(int i=0;  i < searchedClientIds.size(); i++)
		{
			try
			{
					clientId = ((Long)searchedClientIds.get(i)).toString();
	    			client = clientMgr.getClientByDemographicNo(clientId);

	    			if(client == null)
	    			{
	    				throw  new Exception("clientId: " + searchedClientIds.get(i)+ " is null.");
	    			}
	    			
	 			
	    			if( (!clientFirstName.equals("")  &&  
	    				  client.getFirstName().toLowerCase().indexOf(clientFirstName.toLowerCase()) >= 0)  ||
	    				(!clientSurname.equals("")  && 
	    				  client.getLastName().toLowerCase().indexOf(clientSurname.toLowerCase()) >= 0	)
	    				 )
	    			{
	    				allClientsWithinProgramDomain.add(client);
	    				numAdded++;
	    			}
			}
			catch(Exception ex)
			{
//				log.warn(ex);
			}
		}//end of  for(int i=0;  i < searchedClientIds.size(); i++)
		

		for(int i=rowCountPass;  
		    allClientsWithinProgramDomain != null  && 
			allClientsWithinProgramDomain.size() > 0  &&  
			i < maxDisplayNum  && 
			i < allClientsWithinProgramDomain.size(); 
			i++)
		{
			
			filteredForDisplay.add(allClientsWithinProgramDomain.get(i));
			
		}//end of for(...)


	    return filteredForDisplay;
	}

	

	public boolean isDemographicNoInDomain(String demographicNo, List clientIds)
	{
		if( demographicNo == null  ||  demographicNo.length() <= 0  || 
			clientIds == null  ||  clientIds.size() <= 0)
		{
			return false;
		}
		
		String clientId = "";
		
		for(int i=0; i < clientIds.size(); i++)
		{
			
			clientId = ((Long)clientIds.get(i)).toString();
			
			if(demographicNo.equals(clientId))
			{
				return true;
			}
		}
		
		return false;
	}
	


	public List getAllClientsWithinFacilityDomainAndOutsideProgramDomain(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List programDomainClientIds, List facilityDomainClientIds,
			int rowCountPass, int totalRowDisplay)
	{
		
		if(clientMgr == null)//don't place facilityDomainClientIds == null  here
		{
			return null;
		}
		
		List allClientsWithinFacilityDomainAndOutsideProgramDomain = new ArrayList();
		
	    List filteredForDisplay = new ArrayList();
	    int maxDisplayNum = rowCountPass + totalRowDisplay;

		boolean inProgramDomain = false;
		
		
		if( facilityDomainClientIds != null  &&  facilityDomainClientIds.size() > 0 )
		{
			for(int i=0; i < facilityDomainClientIds.size(); i++)
			{
			
				inProgramDomain = false;
				for(int j=0; programDomainClientIds != null  &&  j < programDomainClientIds.size(); j++)
				{
					
					if( (facilityDomainClientIds.get(i).toString()).equals( (programDomainClientIds.get(j)).toString() ) )
					{
						inProgramDomain = true;
						break;
					}
				}//end of for(int j=0; j < gourpDomainClientIds.size(); j++)
				
				if(!inProgramDomain)
				{
					Demographic client = new Demographic();
					
					client = clientMgr.getClientByDemographicNo(facilityDomainClientIds.get(i).toString());
					
					allClientsWithinFacilityDomainAndOutsideProgramDomain.add(  client );
				}
				
			}//end of for(int i=0; i < facilityDomainClientIds.size(); i++)
		}//end of if(facilityDomainClientIds != null  &&  facilityDomainClientIds.size() > 0)
		
		if(allClientsWithinFacilityDomainAndOutsideProgramDomain == null  ||  allClientsWithinFacilityDomainAndOutsideProgramDomain.size() <= 0)
		{
			return null;
		}
		
		for(int i=rowCountPass;  
		allClientsWithinFacilityDomainAndOutsideProgramDomain != null  && 
		allClientsWithinFacilityDomainAndOutsideProgramDomain.size() > 0  &&  
		i < maxDisplayNum  && 
		i < allClientsWithinFacilityDomainAndOutsideProgramDomain.size(); 
		i++)
		{
		
			filteredForDisplay.add(allClientsWithinFacilityDomainAndOutsideProgramDomain.get(i));
		
		}//end of for(...)

		return filteredForDisplay;

	}
	
//################################################################################
	public List getMatchedClientsWithinFacilityDomainAndOutsideProgramDomain(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List programDomainClientIds, List facilityDomainClientIds,
			String clientFirstName, String clientSurname,
			String clientDateOfBirth, String healthCardNum, String healthCardVer, 
			String searchForClient, String retrieveAll, 
			int rowCountPass, int totalRowDisplay)
	{
		
		List matchedClientsWithinFacilityDomainAndOutsideProgramDomain = new ArrayList();
		
	    List filteredForDisplay = new ArrayList();
	    int maxDisplayNum = rowCountPass + totalRowDisplay;

		boolean inProgramDomain = false;
		
		if(facilityDomainClientIds != null  &&  facilityDomainClientIds.size() > 0)
		{
			for(int i=0; i < facilityDomainClientIds.size(); i++)
			{
			
				inProgramDomain = false;
				for(int j=0; programDomainClientIds != null  &&  j < programDomainClientIds.size(); j++)
				{
					
					if( (facilityDomainClientIds.get(i).toString()).equals( (programDomainClientIds.get(j)).toString() ) ) 
					{
						inProgramDomain = true;
						break;
					}
				}//end of for(int j=0; j < gourpDomainClientIds.size(); j++)
				
				if(!inProgramDomain)
				{
    				Demographic client = new Demographic();

					client = clientMgr.getClientByDemographicNo(facilityDomainClientIds.get(i).toString());

	    			if( (!clientFirstName.equals("")  &&  
	    				  (client.getFirstName()).toLowerCase().indexOf(clientFirstName.toLowerCase()) >= 0)  ||
	      				(!clientSurname.equals("")  && 
	      				  (client.getLastName()).toLowerCase().indexOf(clientSurname.toLowerCase()) >= 0	)
	      				 )
	      			{
	    				matchedClientsWithinFacilityDomainAndOutsideProgramDomain.add(  client );
	      			}
				}
				
			}//end of for(int i=0; i < facilityDomainClientIds.size(); i++)
		}//end of if(facilityDomainClientIds != null  &&  facilityDomainClientIds.size() > 0)
		
		if(matchedClientsWithinFacilityDomainAndOutsideProgramDomain == null  ||  matchedClientsWithinFacilityDomainAndOutsideProgramDomain.size() <= 0)
		{
			return null;
		}
		
		for(int i=rowCountPass;  
		matchedClientsWithinFacilityDomainAndOutsideProgramDomain != null  && 
		matchedClientsWithinFacilityDomainAndOutsideProgramDomain.size() > 0  &&  
		i < maxDisplayNum  && 
		i < matchedClientsWithinFacilityDomainAndOutsideProgramDomain.size(); 
		i++)
		{
		
			filteredForDisplay.add(matchedClientsWithinFacilityDomainAndOutsideProgramDomain.get(i));
		
		}//end of for(...)

		return filteredForDisplay;


		
	}
//################################################################################
/*
 *  This refers to clients outside Program Domain but within Facility Domain!
 */
	public List getAllClientsOutsideFacilityDomainWithSharingOn(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List facilityDomainClientIds,
			int rowCountPass, int totalRowDisplay) 
	{//the method should work even if  facilityDomainClientIds == null

		if(clientMgr == null)//don't place facilityDomainClientIds == null  here
		{
			return null;
		}
//		List allSharingClients = clientMgr.getAllSharingClients();//from table 'demographic_pmm'
		
		List allSharingClients = clientMgr.getAllSharingClientsFromInnerJoinTables(databaseService, dataSource);
		
		List allClientsOutsideFacilityDomainWithSharingOn = new ArrayList();
		
	    List filteredForDisplay = new ArrayList();
	    int maxDisplayNum = rowCountPass + totalRowDisplay;

		boolean inFacilityDomain = false;
		int num = 0;
		
		if(allSharingClients != null  &&  allSharingClients.size() > 0)
		{
			for(int i=0; i < allSharingClients.size(); i++)
			{
			
				inFacilityDomain = false;
				for(int j=0; facilityDomainClientIds != null  &&  j < facilityDomainClientIds.size(); j++)
				{
					
					if( (((List)allSharingClients.get(i)).get(0)).toString().equals( (facilityDomainClientIds.get(j)).toString()) ) 
					{
						inFacilityDomain = true;
						break;
					}
				}//end of for(int j=0; j < gourpDomainClientIds.size(); j++)
				
				if(!inFacilityDomain)
				{
					num++;

					Demographic client = new Demographic();
					
					client.setDemographicNo( (Integer)(((List)allSharingClients.get(i)).get(0)) );
					
					client.setLastName( (String)(((List)allSharingClients.get(i)).get(1)) );
					client.setFirstName( (String)(((List)allSharingClients.get(i)).get(2)) );
					client.setYearOfBirth( (String)(((List)allSharingClients.get(i)).get(3)) );
					client.setMonthOfBirth( (String)(((List)allSharingClients.get(i)).get(4)) );
					client.setDateOfBirth( (String)(((List)allSharingClients.get(i)).get(5)) );
					client.setHin( (String)(((List)allSharingClients.get(i)).get(6)) );
					client.setVer( (String)(((List)allSharingClients.get(i)).get(7)) );
					client.setPatientStatus( (String)(((List)allSharingClients.get(i)).get(8)) );
					client.setProviderNo( (String)(((List)allSharingClients.get(i)).get(9)) );
					client.setFamilyDoctor( (String)(((List)allSharingClients.get(i)).get(10)) );

					allClientsOutsideFacilityDomainWithSharingOn.add(  client );
				}
				
			}//end of for(int i=0; i < allSharingClients.size(); i++)
		}//end of if(allSharingClients != null  &&  allSharingClients.size() > 0)
		
		if(allClientsOutsideFacilityDomainWithSharingOn == null  ||  allClientsOutsideFacilityDomainWithSharingOn.size() <= 0)
		{
			return null;
		}
		
		for(int i=rowCountPass;  
		allClientsOutsideFacilityDomainWithSharingOn != null  && 
		allClientsOutsideFacilityDomainWithSharingOn.size() > 0  &&  
		i < maxDisplayNum  && 
		i < allClientsOutsideFacilityDomainWithSharingOn.size(); 
		i++)
		{
		
			filteredForDisplay.add(allClientsOutsideFacilityDomainWithSharingOn.get(i));
		
		}//end of for(...)

		return filteredForDisplay;

	}

//################################################################################
	/*
	 *  This refers to clients outside Program Domain but within Facility Domain!
	 */
	public List getMatchedClientsOutsideFacilityDomainWithSharingOn(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List facilityDomainClientIds,
			String clientFirstName, String clientSurname,
			String clientDateOfBirth, String healthCardNum, String healthCardVer, 
			String searchForClient, String retrieveAll, 
			int rowCountPass, int totalRowDisplay) 
	{//the method should work even if  facilityDomainClientIds == null

//		List allSharingClients = clientMgr.getAllSharingClients();//from table 'demographic_pmm'
		
		List allSharingClients = clientMgr.getAllSharingClientsFromInnerJoinTables(databaseService, dataSource);
		
		
		List matchedClientsOutsideFacilityDomainWithSharingOn = new ArrayList();
		
	    List filteredForDisplay = new ArrayList();
	    int maxDisplayNum = rowCountPass + totalRowDisplay;

		boolean inFacilityDomain = false;
		int num = 0;
		
		if(allSharingClients != null  &&  allSharingClients.size() > 0)
		{
			for(int i=0; i < allSharingClients.size(); i++)
			{
			
				inFacilityDomain = false;
				for(int j=0; facilityDomainClientIds != null  &&  j < facilityDomainClientIds.size(); j++)
				{
					
					if( (((List)allSharingClients.get(i)).get(0)).toString().equals( (facilityDomainClientIds.get(j)).toString() ) ) 
					{
						inFacilityDomain = true;
						break;
					}
				}//end of for(int j=0; j < gourpDomainClientIds.size(); j++)
				
				if(!inFacilityDomain)
				{
					
	    			if( (!clientFirstName.equals("")  &&  
	    				  ((String)((List)allSharingClients.get(i)).get(2)).toLowerCase().indexOf(clientFirstName.toLowerCase()) >= 0)  ||
	      				(!clientSurname.equals("")  && 
	      				  ((String)((List)allSharingClients.get(i)).get(1)).toLowerCase().indexOf(clientSurname.toLowerCase()) >= 0	)
	      				 )
	      			{


	    				num++;

	    				Demographic client = new Demographic();
	    				
	    				client.setDemographicNo( (Integer)(((List)allSharingClients.get(i)).get(0)) );
	    				
	    				client.setLastName( (String)(((List)allSharingClients.get(i)).get(1)) );
	    				client.setFirstName( (String)(((List)allSharingClients.get(i)).get(2)) );
	    				client.setYearOfBirth( (String)(((List)allSharingClients.get(i)).get(3)) );
	    				client.setMonthOfBirth( (String)(((List)allSharingClients.get(i)).get(4)) );
	    				client.setDateOfBirth( (String)(((List)allSharingClients.get(i)).get(5)) );
	    				client.setHin( (String)(((List)allSharingClients.get(i)).get(6)) );
	    				client.setVer( (String)(((List)allSharingClients.get(i)).get(7)) );
	    				client.setPatientStatus( (String)(((List)allSharingClients.get(i)).get(8)) );
	    				client.setProviderNo( (String)(((List)allSharingClients.get(i)).get(9)) );
	    				client.setFamilyDoctor( (String)(((List)allSharingClients.get(i)).get(10)) );

	    				matchedClientsOutsideFacilityDomainWithSharingOn.add(  client );
					
	      			}
				}
				
			}//end of for(int i=0; i < allSharingClients.size(); i++)
		}//end of if(allSharingClients != null  &&  allSharingClients.size() > 0)
		
		if(matchedClientsOutsideFacilityDomainWithSharingOn == null  ||  matchedClientsOutsideFacilityDomainWithSharingOn.size() <= 0)
		{
			return null;
		}
		
		for(int i=rowCountPass;  
		matchedClientsOutsideFacilityDomainWithSharingOn != null  && 
		matchedClientsOutsideFacilityDomainWithSharingOn.size() > 0  &&  
		i < maxDisplayNum  && 
		i < matchedClientsOutsideFacilityDomainWithSharingOn.size(); 
		i++)
		{
		
			filteredForDisplay.add(matchedClientsOutsideFacilityDomainWithSharingOn.get(i));
		
		}//end of for(...)

		return filteredForDisplay;

	}

//################################################################################

	public List getAllClientsOutsideDomainWithSharingOff(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List groupDomainClientIds,
			int rowCountPass, int totalRowDisplay) 
	{//the method should work even if  groupDomainClientIds == null

		if(clientMgr == null  ||  totalRowDisplay < 0)//don't place groupDomainClientIds == null  here
		{
			return null;
		}
//		List allSharingOffClients = clientMgr.getAllSharingOffClients();//from table 'demographic_pmm'
		
		List allSharingOffClients = clientMgr.getAllSharingOffClientsFromInnerJoinTables(databaseService, dataSource);
		
		List allClientsOutsideDomainWithSharingOff = new ArrayList();
		
	    List filteredForDisplay = new ArrayList();
	    int maxDisplayNum = rowCountPass + totalRowDisplay;

		boolean inDomain = false;
		int num = 0;
		
		if(allSharingOffClients != null  &&  allSharingOffClients.size() > 0)
		{
			for(int i=0; i < allSharingOffClients.size(); i++)
			{
			
				inDomain = false;
				for(int j=0; groupDomainClientIds != null  &&  j < groupDomainClientIds.size(); j++)
				{
					
					if( (((List)allSharingOffClients.get(i)).get(0)).toString().equals( (groupDomainClientIds.get(j)).toString() ) ) 
					{
						inDomain = true;
						break;
					}
				}//end of for(int j=0; j < gourpDomainClientIds.size(); j++)
				
				if(!inDomain)
				{
					num++;

					Demographic client = new Demographic();
					
					client.setDemographicNo( (Integer)(((List)allSharingOffClients.get(i)).get(0)) );
					
					client.setLastName( (String)(((List)allSharingOffClients.get(i)).get(1)) );
					client.setFirstName( (String)(((List)allSharingOffClients.get(i)).get(2)) );
					client.setYearOfBirth( (String)(((List)allSharingOffClients.get(i)).get(3)) );
					client.setMonthOfBirth( (String)(((List)allSharingOffClients.get(i)).get(4)) );
					client.setDateOfBirth( (String)(((List)allSharingOffClients.get(i)).get(5)) );
					client.setHin( (String)(((List)allSharingOffClients.get(i)).get(6)) );
					client.setVer( (String)(((List)allSharingOffClients.get(i)).get(7)) );
					client.setPatientStatus( (String)(((List)allSharingOffClients.get(i)).get(8)) );
					client.setProviderNo( (String)(((List)allSharingOffClients.get(i)).get(9)) );
					client.setFamilyDoctor( (String)(((List)allSharingOffClients.get(i)).get(10)) );

					allClientsOutsideDomainWithSharingOff.add(  client );
				}
				
			}//end of for(int i=0; i < allSharingClients.size(); i++)
		}//end of if(allSharingClients != null  &&  allSharingClients.size() > 0)
		
		if(allClientsOutsideDomainWithSharingOff == null  ||  allClientsOutsideDomainWithSharingOff.size() <= 0)
		{
			return null;
		}
		
		for(int i=rowCountPass;  
		allClientsOutsideDomainWithSharingOff != null  && 
		allClientsOutsideDomainWithSharingOff.size() > 0  &&  
		i < maxDisplayNum  && 
		i < allClientsOutsideDomainWithSharingOff.size(); 
		i++)
		{
		
			filteredForDisplay.add(allClientsOutsideDomainWithSharingOff.get(i));
		
		}//end of for(...)

		return filteredForDisplay;

	}
	
//	################################################################################

	public List getMatchedClientsOutsideDomainWithSharingOff(
			Database_Service databaseService, DataSource dataSource, 
			ClientManager clientMgr, List groupDomainClientIds,
			String clientFirstName, String clientSurname,
			String clientDateOfBirth, String healthCardNum, String healthCardVer, 
			String searchForClient, String retrieveAll, 
			int rowCountPass, int totalRowDisplay) 
	{//the method should work even if  groupDomainClientIds == null

//		List allSharingOffClients = clientMgr.getAllSharingOffClients();//from table 'demographic_pmm'
		
		List allSharingOffClients = clientMgr.getAllSharingOffClientsFromInnerJoinTables(databaseService, dataSource);
		
		
		List allClientsOutsideDomainWithSharingOff = new ArrayList();
		
	    List filteredForDisplay = new ArrayList();
	    int maxDisplayNum = rowCountPass + totalRowDisplay;

		boolean inDomain = false;
		int num = 0;
		
		if(allSharingOffClients != null  &&  allSharingOffClients.size() > 0)
		{
			for(int i=0; i < allSharingOffClients.size(); i++)
			{
			
				inDomain = false;
				for(int j=0; groupDomainClientIds != null  &&  j < groupDomainClientIds.size(); j++)
				{
					
					if( (((List)allSharingOffClients.get(i)).get(0)).toString().equals( (groupDomainClientIds.get(j)).toString() ) ) 
					{
						inDomain = true;
						break;
					}
				}//end of for(int j=0; j < gourpDomainClientIds.size(); j++)
				
				if(!inDomain)
				{
					
	    			if( (!clientFirstName.equals("")  &&  
	    				  ((String)((List)allSharingOffClients.get(i)).get(2)).toLowerCase().indexOf(clientFirstName.toLowerCase()) >= 0)  ||
	      				(!clientSurname.equals("")  && 
	      				  ((String)((List)allSharingOffClients.get(i)).get(1)).toLowerCase().indexOf(clientSurname.toLowerCase()) >= 0	)
	      				 )
	      			{


	    				num++;

	    				Demographic client = new Demographic();
	    				
	    				client.setDemographicNo( (Integer)(((List)allSharingOffClients.get(i)).get(0)) );
	    				
	    				client.setLastName( (String)(((List)allSharingOffClients.get(i)).get(1)) );
	    				client.setFirstName( (String)(((List)allSharingOffClients.get(i)).get(2)) );
	    				client.setYearOfBirth( (String)(((List)allSharingOffClients.get(i)).get(3)) );
	    				client.setMonthOfBirth( (String)(((List)allSharingOffClients.get(i)).get(4)) );
	    				client.setDateOfBirth( (String)(((List)allSharingOffClients.get(i)).get(5)) );
	    				client.setHin( (String)(((List)allSharingOffClients.get(i)).get(6)) );
	    				client.setVer( (String)(((List)allSharingOffClients.get(i)).get(7)) );
	    				client.setPatientStatus( (String)(((List)allSharingOffClients.get(i)).get(8)) );
	    				client.setProviderNo( (String)(((List)allSharingOffClients.get(i)).get(9)) );
	    				client.setFamilyDoctor( (String)(((List)allSharingOffClients.get(i)).get(10)) );

	    				allClientsOutsideDomainWithSharingOff.add(  client );
					
	      			}
				}
				
			}//end of for(int i=0; i < allSharingClients.size(); i++)
		}//end of if(allSharingClients != null  &&  allSharingClients.size() > 0)
		
		if(allClientsOutsideDomainWithSharingOff == null  ||  allClientsOutsideDomainWithSharingOff.size() <= 0)
		{
			return null;
		}
		
		for(int i=rowCountPass;  
		allClientsOutsideDomainWithSharingOff != null  && 
		allClientsOutsideDomainWithSharingOff.size() > 0  &&  
		i < maxDisplayNum  && 
		i < allClientsOutsideDomainWithSharingOff.size(); 
		i++)
		{
		
			filteredForDisplay.add(allClientsOutsideDomainWithSharingOff.get(i));
		
		}//end of for(...)

		return filteredForDisplay;

	}
	
//################################################################################



//	################################################################################

	
	public List getAllSharingClientsFromInnerJoinTables(
			Database_Service databaseService, DataSource dataSource)
	{
		return dao.getAllSharingClientsFromInnerJoinTables(
				          databaseService, dataSource);
	}
	
	public List getAllSharingOffClientsFromInnerJoinTables(
			Database_Service databaseService, DataSource dataSource)
	{
		return dao.getAllSharingOffClientsFromInnerJoinTables(
				          databaseService, dataSource);
	}

	public List getClients(Database_Service databaseService, 
                           DataSource dataSource, String queryStr)
	{
		return dao.getClients(databaseService, dataSource, queryStr);
	}
	
	public String getDemographicNo(String firstName, String lastName)
	{
		return dao.getDemographicNo(firstName, lastName);
	}

	public List getDemographicNos(String firstName, String lastName)
	{
		return dao.getDemographicNos(firstName, lastName);
	}

	public boolean isClientExistAlready(String firstName, String lastName)
	{
		return dao.isClientExistAlready(firstName, lastName);
	}

	public boolean isSharingOn(String demographicNo)
	{
		return dao.isSharingOn(demographicNo);
	}
	

	public int getTotalRecordCount(Database_Service databaseService, DataSource dataSource)
	{
		return dao.getTotalRecordCount(databaseService, dataSource);
	}

	public Demographic setClientObj(Demographic clientInfo, ResultSet rs)
	{
		return dao.setClientObj(clientInfo, rs);
	}

	public String getQuerySqlStr(String clientFirstName, String clientSurname,
			                     String clientDateOfBirth, String healthCardNum,
			                     String healthCardVer, String retrieveAll)
	{
		return dao.getQuerySqlStr(clientFirstName, clientSurname,
                   clientDateOfBirth, healthCardNum, healthCardVer, retrieveAll);
	}

	public String getQuerySqlStr(String clientFirstName, String clientSurname,
			                     String clientDateOfBirth, String healthCardNum,
			                     String healthCardVer, String retrieveAll, 
			                     int rowCountPass, int totalRowDisplay)
	{
		return dao.getQuerySqlStr(clientFirstName, clientSurname,
                					clientDateOfBirth, healthCardNum,
                					healthCardVer, retrieveAll, 
                					rowCountPass, totalRowDisplay);
	}

	public boolean addClient(Demographic client)
	{
		return dao.addClient(client);
	}

	public boolean addClientFromFormintakea(Database_Service databaseService, DataSource dataSource, Formintakea intakeA)
	{
		return dao.addClientFromFormintakea(databaseService, dataSource, intakeA);
	}
	
	public boolean addClientIntoFormintakea(Database_Service databaseService, DataSource dataSource, Demographic client)
	{
		return dao.addClientIntoFormintakea(databaseService, dataSource, client);
	}

	public boolean updateClientDateOfBirth(Database_Service databaseService, DataSource dataSource, Demographic client)
	{
		return dao.updateClientDateOfBirth(databaseService, dataSource, client);
	}

	public boolean updateClientHealthCardNum(
			Database_Service databaseService, DataSource dataSource, 
			Demographic client, String healthCardNum, String healthCardVer) 
	{
		return dao.updateClientHealthCardNum(databaseService, dataSource, 
				client, healthCardNum, healthCardVer);
	}

	public boolean updateClientDoctor(
			Database_Service databaseService, DataSource dataSource, Demographic client)
	{
		return dao.updateClientDoctor(databaseService, dataSource, client);
	}

	public boolean updateClient(Demographic client)
	{
		return dao.updateClient(client);
	}


	public boolean removeClient(String demographicNo)
	{
		return dao.removeClient(demographicNo);
	}

	public List search(ClientSearchFormBean criteria) {
		return dao.search(criteria);
	}

	public java.util.Date getMostRecentIntakeADate(String demographicNo) {
		return dao.getMostRecentIntakeADate(Long.valueOf(demographicNo));
	}
	
	public java.util.Date getMostRecentIntakeBDate(String demographicNo) {
		return dao.getMostRecentIntakeBDate(Long.valueOf(demographicNo));	
	}
	
	public java.util.Date getMostRecentIntakeCDate(String demographicNo) {
		return dao.getMostRecentIntakeCDate(Long.valueOf(demographicNo));	
	}
	
	public List getReferrals(String clientId) {
		return referralDAO.getReferrals(Long.valueOf(clientId));
	}
	public List getActiveReferrals(String clientId) {
		return referralDAO.getActiveReferrals(Long.valueOf(clientId));
	}
	
	
	public ClientReferral getClientReferral(String id) {
		return referralDAO.getClientReferral(Long.valueOf(id));
	}
	/*
	 * This should always be a new one.
	 * add the queue to the program.
	 */
	public void saveClientReferral(ClientReferral referral) {
		
		referralDAO.saveClientReferral(referral);
		
		ProgramQueue queue = new ProgramQueue();
		queue.setAgencyId(referral.getAgencyId());
		queue.setClientId(referral.getClientId());
		queue.setNotes(referral.getNotes());
		queue.setProgramId(referral.getProgramId());
		queue.setProviderNo(referral.getProviderNo());
		queue.setReferralDate(referral.getReferralDate());
		queue.setStatus("active");
		queue.setReferralId(referral.getId());
		queueManager.saveProgramQueue(queue);
	}
	
}
