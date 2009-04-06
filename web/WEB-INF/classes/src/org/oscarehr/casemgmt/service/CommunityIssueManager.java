package org.oscarehr.casemgmt.service;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.jboss.logging.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.IssueTransfer;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementCommunityIssue;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class CommunityIssueManager {

	private static Logger log = Logger.getLogger(CommunityIssueManager.class);
	private CaseManagementManager cMan = (CaseManagementManager)SpringUtils.getBean("caseManagementManager");
	private CaisiIntegratorManager ciMan = (CaisiIntegratorManager)SpringUtils.getBean("caisiIntegratorManager");
	private IssueDAO issueDao = (IssueDAO)SpringUtils.getBean("IssueDAO");
	private CaseManagementIssueDAO cmiDao = (CaseManagementIssueDAO)SpringUtils.getBean("caseManagementIssueDAO");
	
	/**
	 * 
	 * @param providerNo
	 * @param demographicNo
	 * @param programId
	 * @param facilityId
	 * @param getRemote
	 * @param active
	 * @return
	 * @throws MalformedURLException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public List<CaseManagementCommunityIssue> getCommunityIssues(String providerNo, String demographicNo, String programId, Integer facilityId, boolean getRemote, boolean active) throws MalformedURLException, InvocationTargetException, IllegalAccessException
    {
    	List<CaseManagementIssue> localIssues = null;
    	if (!active) {
			localIssues = cMan.getIssues(providerNo, demographicNo);
		}
		else {
			localIssues = cMan.getActiveIssues(providerNo, demographicNo);
		}
    	//filter out issues from other facilities in this CAISI instance
    	localIssues = cMan.filterIssues(localIssues, providerNo, programId, facilityId);
    	List<IssueTransfer> remoteIssues = new ArrayList<IssueTransfer>();
		// check facility integrator status
    	if(getRemote)
		{
			// get remote issues
			CaisiIntegratorManager ciMan = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");
			remoteIssues = ciMan.getRemoteIssues(facilityId, Integer.valueOf(demographicNo));
		}
		
		// combine local and remote
		//logger.debug("Combine local and remote");
		List<CaseManagementCommunityIssue> communityIssues = combineLocalAndRemoteIssues(localIssues, remoteIssues);
		
		//logger.debug("Filter Issues");
		communityIssues = cMan.filterCommunityIssues(communityIssues, providerNo, programId, facilityId);
		this.assignCheckboxID(communityIssues);
    	return communityIssues;
    }
	
	/**
	 * 
	 * @param local
	 * @param remote
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public List<CaseManagementCommunityIssue> combineLocalAndRemoteIssues(List<CaseManagementIssue> local, List<IssueTransfer> remote) throws InvocationTargetException, IllegalAccessException
    {
    	List<CaseManagementCommunityIssue> communityIssues = new ArrayList<CaseManagementCommunityIssue>();
    	String communityType = OscarProperties.getInstance().getProperty("COMMUNITY_ISSUE_CODETYPE");
    	for(CaseManagementIssue localIssue: local)
    	{
    		CaseManagementCommunityIssue newLocalIssue = new CaseManagementCommunityIssue();
    		BeanUtils.copyProperties(newLocalIssue, localIssue);
    		communityIssues.add(newLocalIssue);
    		log.debug("Found local issue "+localIssue.getIssue().getCode()+"/"+localIssue.getIssue().getDescription()+" in program "+localIssue.getProgram_id());
    	}
    	
    	String type = OscarProperties.getInstance().getProperty("COMMUNITY_ISSUE_CODETYPE");
		// add roles to remote issues, drop all remote issues with codes not found in the local issues table
    	for(IssueTransfer remoteIssue: remote)
    	{
    		log.debug("Found remote issue "+remoteIssue.getIssueCode()+"/"+remoteIssue.getIssueDescription());
    		Issue matchingLocalIssue = issueDao.findIssueByCode(remoteIssue.getIssueCode());
    		if(matchingLocalIssue != null)
    		{
    			//filter out remote issues that have identical parameters to used local issue (req 6.2)
    			boolean found = false;
    			Iterator<CaseManagementIssue> it = local.iterator();
	    		while (!found && it.hasNext())
	    		{
	    			if(isIdentical(it.next(), remoteIssue))
	    			{
	    				found = true;
	    			}
	    		}
	    		if (!found) // no duplicates found 
	    		{ 
	    			CaseManagementCommunityIssue newRemoteIssue = new CaseManagementCommunityIssue();
		    		newRemoteIssue.setRemote(true);
		
		    		newRemoteIssue.setFacilityId(remoteIssue.getFacilityId());
		    		newRemoteIssue.setFacilityName(remoteIssue.getFacilityName());
		    		newRemoteIssue.setDemographic_no(remoteIssue.getDemographicId().toString());
		    		newRemoteIssue.setAcute(remoteIssue.isAcute());
		    		newRemoteIssue.setCertain(remoteIssue.isCertain());
		    		newRemoteIssue.setMajor(remoteIssue.isMajor());
		    		newRemoteIssue.setResolved(remoteIssue.isResolved());
		    		newRemoteIssue.setType(matchingLocalIssue.getRole());
		    		
		    		Issue issue = new Issue();
		    		issue.setCode(remoteIssue.getIssueCode());
		    		issue.setDescription(remoteIssue.getIssueDescription());
		    		issue.setRole(matchingLocalIssue.getRole());
		    		issue.setType(type);
		    		newRemoteIssue.setIssue(issue);
		    		
		    		communityIssues.add(newRemoteIssue);
	    		}
	    		else
	    		{
	    			log.debug("Remote issue "+remoteIssue.getIssueCode()+" is identical to a local issue, dropping");
	    		}
    		}
    		else
    		{
    			log.debug("Dropping remote issue with code "+remoteIssue.getIssueCode()+" - no matching local issue code found (it mustn't be a community issue");
    		}
    	}
    	return communityIssues;
    }
	
	/**
	 * 
	 * @param facilityId
	 * @param type
	 * @return
	 */
	public List<String> getCommunityIssueCodes(int facilityId, String type) 
	{
		try
		{
			List<String> communityCodes = issueDao.getLocalCodesByCommunityType(type);
			if(communityCodes == null)
			{
				communityCodes = ciMan.getCommunityIssueCodeList(facilityId, type);
			}
			return communityCodes;
		}
		catch(MalformedURLException e)
		{
			log.error("Error connecting to Integrator for Issue Code List", e);
			return new ArrayList<String>();
		}
	}
	public boolean isIdentical(CaseManagementIssue local, IssueTransfer remote)
	{
		if (!remote.getIssueCode().equals(local.getIssue().getCode())) return false;
		if (!remote.getIssueDescription().equals(local.getIssue().getDescription())) return false;
		if (remote.isAcute() != local.isAcute()) return false;
		if (remote.isCertain() != local.isCertain()) return false;
		if (remote.isMajor() != local.isMajor()) return false;
		if (remote.isResolved() != local.isResolved()) return false;
		
		return true;
	}
	
	/**
	 * Used when a remote COmmunity Issue is checked while adding a new note in the CaseManagementEntry(.jsp) screen
	 * @param issue a remote CaseManagementCommunityIssue copied into a CaseManagementIssue
	 */
	public void copyRemoteCommunityIssueToLocal(CaseManagementIssue issue)
	{
		// look up issue code/description/type
		Issue iss = issueDao.findIssueByCode(issue.getIssue().getCode());
		if(iss == null || iss.getType() == null || !iss.getType().equalsIgnoreCase(OscarProperties.getInstance().getProperty("COMMUNITY_ISSUE_CODETYPE")))
		{
			// if not found, or not a community type, or no type make issue in issue table			
			iss = issue.getIssue();
			issueDao.saveIssue(issue.getIssue());
			log.debug("Issue "+iss.getCode()+"/"+iss.getDescription()+" saved as Issue "+iss.getId());
		}
		else
		{
			// otherwise copy the ID over.
			issue.setIssue(iss);
			log.debug("Issue "+iss.getCode()+"/"+iss.getDescription()+" exists as Issue "+iss.getId());
		}
				
		// save to casemgmt_issue table so we have an ID to attach to a note
		issue.setIssue_id(iss.getId());
		cmiDao.saveIssue(issue);
		log.debug("CaseManagementIssue created with ID "+issue.getIssue_id());
	}
	
	private void assignCheckboxID(List<CaseManagementCommunityIssue> issues)
	{
		for(CaseManagementCommunityIssue issue: issues)
		{
			// something needs to go here... but what?  it needs to be consistent for each pull, and unique to the issue itself.
			StringBuffer buff = new StringBuffer();
			// facilityID, or 0 for local + issue code type
			if(issue.isRemote()) 
			{ 
				buff.append(issue.getFacilityId());
				buff.append("-");
				buff.append(OscarProperties.getInstance().getProperty("COMMUNITY_ISSUE_CODETYPE"));
			}
			else 
			{
				buff.append(0);
				buff.append("-");
				buff.append(issue.getIssue().getType());
			}
			// +issue code
			buff.append("-");
			buff.append(issue.getIssue().getCode());
			// + boolean values
			buff.append("-");
			buff.append(intMe(issue.isAcute()));
			buff.append(intMe(issue.isCertain()));
			buff.append(intMe(issue.isMajor()));
			buff.append(intMe(issue.isResolved()));
			issue.setCheckboxID(buff.toString());
			log.debug(issue.getCheckboxID());
		}
		
	}
	
	private int intMe(boolean boo)
	{
		if (boo) return 1;
		else return 0;
	}
}
