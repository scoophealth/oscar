/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package org.oscarehr.myoscar.managers;

import org.apache.log4j.Logger;
import org.oscarehr.myoscar_server.ws.LoginResultTransfer;
import org.oscarehr.myoscar_server.ws.LoginWs;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.myoscar_server.ws.PersonPreferencesTransfer3;
import org.oscarehr.myoscar_server.ws.PersonTransfer;
import org.oscarehr.phr.util.MyOscarServerWebServicesManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.QueueCache;

import oscar.OscarProperties;

public final class MyOscarAccountManager
{
	private static final Logger logger = MiscUtils.getLogger();

	private static final int MAX_OBJECTS_TO_CACHE = Integer.parseInt(OscarProperties.getInstance().getProperty("myoscar.account_manager_people_cache_size"));
	private static final long MAX_TIME_TO_CACHE = Long.parseLong(OscarProperties.getInstance().getProperty("myoscar.account_manager_people_cache_time_ms"));

	public static final String MYOSCAR_CLIENT_PREFERENCE_PREFIX = "MyOscarClient.";

	/**
	 * Cache key must be qualified so 2 people don't share a cached item, even if it's the same thing,
	 * the request must be made to the server so the access is logged and authentication is checked.
	 * 
	 * MAX_CACHE_ITEM_SIZE*items to queue = max memory used for cache
	 */
	private static QueueCache<String, PersonTransfer> personCache = new QueueCache<String, PersonTransfer>(4, MAX_OBJECTS_TO_CACHE, MAX_TIME_TO_CACHE);
	private static QueueCache<String, PersonPreferencesTransfer3> personPreferencesCache = new QueueCache<String, PersonPreferencesTransfer3>(4, MAX_OBJECTS_TO_CACHE, MAX_TIME_TO_CACHE);

	private static String getCacheKey(LoggedInInfo loggedInInfo, Long personId)
	{
		// we're qualifying session and person because
		// the "loggedInPersonId" is important because if some one manages to logs in with our clearing the session we might see old data from the last person logged in, in theory not possible as we clear the session on both logout and login but lets be double safe. 
		// the "loggedInSessionId" is desirable because if the same person logs in and logs out and logs back in, we want to ignore previously cached data, if effectively lets people clear the cache.  
		return(loggedInInfo.session.getId() + ':' + loggedInInfo.loggedInSecurity.getSecurityNo() + ':' + personId);
	}

	/**
	 * @return the logged in person or null if invalid login.
	 */
	public static LoginResultTransfer login(String userName, String password) throws NotAuthorisedException_Exception
	{
		try
		{
			LoginWs loginWs = MyOscarServerWebServicesManager.getLoginWs();
			LoginResultTransfer loginResultTransfer = loginWs.login2(userName, password);
			return(loginResultTransfer);
		}
		catch (NotAuthorisedException_Exception e)
		{
			logger.warn("Invalid Login Request userName=" + userName);
			throw(e);
		}
	}
	
//	public static PersonPreferencesTransfer3 getPersonPreferences(LoggedInInfo loggedInInfo, Long personId) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
//	{
//		String cacheKey = getCacheKey(loggedInInfo, personId);
//		PersonPreferencesTransfer3 personPreferencesTransfer = personPreferencesCache.get(cacheKey);
//
//		if (personPreferencesTransfer == null)
//		{
//			AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//			personPreferencesTransfer = accountWs.getPersonPreferences3(loggedInInfo.getLoggedInPersonId());
//			personPreferencesCache.put(cacheKey, personPreferencesTransfer);
//		}
//
//		return(personPreferencesTransfer);
//	}
//
//	public static String getCurrentUserPersonPreference(LoggedInInfo loggedInInfo, String preferenceKey) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
//	{
//		PersonPreferencesTransfer3 personPreferencesTransfer = getCurrentUserPersonPreferences(loggedInInfo);
//
//		List<PersonPreferencesKeyValueTransfer> kvPairs = personPreferencesTransfer.getKeyValuePairs();
//		for (PersonPreferencesKeyValueTransfer personPreferencesKeyValueTransfer : kvPairs)
//		{
//			if (preferenceKey.equals(personPreferencesKeyValueTransfer.getKey())) return(personPreferencesKeyValueTransfer.getValue());
//		}
//
//		return(null);
//	}
//
//	public static void setCurrentPersonPreference(LoggedInInfo loggedInInfo, String preferenceKey, String preferenceValue) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
//	{
//		logger.debug("Updating updatePersonPreferences, personId=" + loggedInInfo.getLoggedInPersonId() + ", preferenceKey=" + preferenceKey + ", preferenceValue=" + preferenceValue);
//
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		accountWs.setPersonPreferences3(loggedInInfo.getLoggedInPersonId(), preferenceKey, preferenceValue);
//
//		String cacheKey = getCacheKey(loggedInInfo, loggedInInfo.getLoggedInPersonId());
//		personPreferencesCache.remove(cacheKey);
//	}
//
//	public static boolean isInternalComponentVisible(LoggedInInfo loggedInInfo, InternalComponent internalComponent) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
//	{
//		String temp = AccountManager.getCurrentUserPersonPreference(loggedInInfo, MYOSCAR_CLIENT_PREFERENCE_PREFIX + "Internal." + internalComponent.name());
//
//		// default internal Components to visible, i.e. null
//		return(temp == null || Boolean.parseBoolean(temp));
//	}
//
//	public static void setInternalComponentVisible(LoggedInInfo loggedInInfo, InternalComponent internalComponent, boolean visible) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
//	{
//		// null defaults to show
//		AccountManager.setCurrentPersonPreference(loggedInInfo, MYOSCAR_CLIENT_PREFERENCE_PREFIX + "Internal." + internalComponent.name(), visible ? null : "false");
//	}
//
//	public static boolean isExternalComponentVisible(LoggedInInfo loggedInInfo, String externalComponentName) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
//	{
//		String temp = AccountManager.getCurrentUserPersonPreference(loggedInInfo, MYOSCAR_CLIENT_PREFERENCE_PREFIX + "External." + externalComponentName);
//
//		// default external Components to hidden, i.e. null==false==hidden
//		return(Boolean.parseBoolean(temp));
//	}
//
//	public static void setExternalComponentVisible(LoggedInInfo loggedInInfo, String externalComponentName, boolean visible) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
//	{
//		// null defaults to hide
//		AccountManager.setCurrentPersonPreference(loggedInInfo, MYOSCAR_CLIENT_PREFERENCE_PREFIX + "External." + externalComponentName, visible ? "true" : null);
//	}
//
//	public static void changeCurrentUserPassword(LoggedInInfo loggedInInfo, String oldPassword, String newPassword) throws InvalidRequestException_Exception, NotAuthorisedException_Exception, NoSuchItemException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		Long personId = loggedInInfo.getLoggedInPersonId();
//		accountWs.changePassword(personId, oldPassword, newPassword);
//
//		// this is to remove the require change password date preference or update the required date. 
//		String cacheKey = getCacheKey(loggedInInfo, loggedInInfo.getLoggedInPersonId());
//		personPreferencesCache.remove(cacheKey);
//	}
//
//	public static PersonTransfer getPerson(LoggedInInfo loggedInInfo, Long personId) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
//	{
//		String cacheKey = getCacheKey(loggedInInfo, personId);
//		PersonTransfer personTransfer = personCache.get(cacheKey);
//
//		if (personTransfer == null)
//		{
//			AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//			personTransfer = accountWs.getPerson(personId);
//
//			personCache.put(cacheKey, personTransfer);
//		}
//
//		return(personTransfer);
//	}
//
//	public static PersonTransfer getPerson(LoggedInInfo loggedInInfo, String personUserName) throws NotAuthorisedException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		PersonTransfer personTransfer = accountWs.getPersonByUserName(personUserName, true);
//		return(personTransfer);
//	}
//
//	public static void updatePerson(LoggedInInfo loggedInInfo, PersonTransfer personTransfer) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		accountWs.updatePerson(personTransfer);
//
//		String cacheKey = getCacheKey(loggedInInfo, personTransfer.getId());
//		personCache.put(cacheKey, personTransfer);
//	}
//
//	public static PersonTransfer createPerson(LoggedInInfo loggedInInfo, PersonTransfer personTransfer, String password) throws NotAuthorisedException_Exception, ItemAlreadyExistsException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		PersonTransfer result = accountWs.addPerson(personTransfer, password);
//		return(result);
//	}
//
//	public static void changePasswordNoVerify(LoggedInInfo loggedInInfo, Long userId, String newPassword) throws InvalidRequestException_Exception, NotAuthorisedException_Exception, NoSuchItemException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		accountWs.changePasswordNoVerifyById(userId, newPassword);
//	}
//
//	public static List<PersonTransfer> searchPeople(LoggedInInfo loggedInInfo, PersonTransfer personTransfer, int startIndex, int itemsToReturn) throws NoSuchItemException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		List<PersonTransfer> results = accountWs.searchPeople(personTransfer, startIndex, itemsToReturn);
//		return(results);
//	}
//
//	public static List<PersonHealthInsuranceTransfer> getPersonHealthInsurance(LoggedInInfo loggedInInfo, Long personId, HealthInsuranceType insuranceType)
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		List<PersonHealthInsuranceTransfer> results = accountWs.getPersonHealthInsurances(personId, insuranceType, true);
//		return(results);
//	}
//
//	public static PersonHealthInsuranceTransfer updatePersonHealthInsurance(LoggedInInfo loggedInInfo, PersonHealthInsuranceTransfer personHealthInsuranceTransfer) throws NotAuthorisedException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		personHealthInsuranceTransfer = accountWs.updatePersonHealthInsurance(personHealthInsuranceTransfer);
//		return(personHealthInsuranceTransfer);
//	}
//
//	public static PersonHealthInsuranceTransfer addPersonHealthInsurance(LoggedInInfo loggedInInfo, PersonHealthInsuranceTransfer personHealthInsuranceTransfer) throws NotAuthorisedException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		personHealthInsuranceTransfer = accountWs.addPersonHealthInsurance(personHealthInsuranceTransfer);
//		return(personHealthInsuranceTransfer);
//	}
//
//	public static List<PersonEmergencyContactTransfer> getPersonEmergencyContacts(LoggedInInfo loggedInInfo, Long personId)
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		List<PersonEmergencyContactTransfer> results = accountWs.getPersonEmergencyContacts(personId, true);
//		return(results);
//	}
//
//	public static PersonEmergencyContactTransfer updatePersonEmergencyContact(LoggedInInfo loggedInInfo, PersonEmergencyContactTransfer personEmergencyContactTransfer) throws NotAuthorisedException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		personEmergencyContactTransfer = accountWs.updatePersonEmergencyContact(personEmergencyContactTransfer);
//		return(personEmergencyContactTransfer);
//	}
//
//	public static PersonEmergencyContactTransfer addPersonEmergencyContact(LoggedInInfo loggedInInfo, PersonEmergencyContactTransfer personEmergencyContactTransfer) throws NotAuthorisedException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		personEmergencyContactTransfer = accountWs.addPersonEmergencyContact(personEmergencyContactTransfer);
//		return(personEmergencyContactTransfer);
//	}
//
//	public static void removeRelationship(LoggedInInfo loggedInInfo, Long relationshipId) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		accountWs.removeRelationship(relationshipId);
//	}
//
//	public static int getPrimaryRelationshipCount(LoggedInInfo loggedInInfo, Long primaryPersonId)
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		return(accountWs.getPrimaryRelationshipCount(primaryPersonId));
//	}
//
//	public static int getRelatedRelationshipCount(LoggedInInfo loggedInInfo, Long relatedPersonId)
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		return(accountWs.getRelatedRelationshipCount(relatedPersonId));
//	}
//
//	public static PeopleGroupTransfer createPeopleGroup(LoggedInInfo loggedInInfo, PeopleGroupTransfer peopleGroupTransfer) throws NotAuthorisedException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		return(accountWs.createPeopleGroup(peopleGroupTransfer));
//	}
//
//	public static PeopleGroupTransfer getPeopleGroup(LoggedInInfo loggedInInfo, Long peopleGroupId) throws NoSuchItemException_Exception, NotAuthorisedException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		return(accountWs.getPeopleGroup(peopleGroupId));
//	}
//
//	public static void updatePeopleGroup(LoggedInInfo loggedInInfo, PeopleGroupTransfer peopleGroupTransfer) throws NoSuchItemException_Exception, NotAuthorisedException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		accountWs.updatePeopleGroup(peopleGroupTransfer);
//	}
//
//	public static List<PeopleGroupTransfer> searchGroups(LoggedInInfo loggedInInfo, String namePortion, Boolean active, Boolean publiclyVisible, int startIndex, int itemsToReturn)
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		return(accountWs.searchGroups(namePortion, active, publiclyVisible, startIndex, itemsToReturn));
//	}
//
//	public static PeopleGroupMemberTransfer addMemberToPeopleGroup(LoggedInInfo loggedInInfo, PeopleGroupMemberTransfer peopleGroupMemberTransfer) throws NotAuthorisedException_Exception, InvalidRequestException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		return(accountWs.addMemberToPeopleGroup(peopleGroupMemberTransfer));
//	}
//
//	public static void removeMemberFromPeopleGroup(LoggedInInfo loggedInInfo, Long peopleGroupMemberId) throws NoSuchItemException_Exception, NotAuthorisedException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		accountWs.removeMemberFromPeopleGroup(peopleGroupMemberId);
//	}
//
//	public static List<PeopleGroupMemberTransfer> getMembersByPeopleGroupId(LoggedInInfo loggedInInfo, Long groupId, int startIndex, int itemsToReturn) throws NoSuchItemException_Exception, NotAuthorisedException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		return(accountWs.getMembersByPeopleGroupId(groupId, startIndex, itemsToReturn));
//	}
//
//	public static List<PeopleGroupMemberTransfer> getMembershipsByPersonId(LoggedInInfo loggedInInfo, Long personId) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		return(accountWs.getMembershipsByPersonId(personId));
//	}
//
//	public static String generateSecurityToken(LoggedInInfo loggedInInfo) throws NotAuthorisedException_Exception
//	{
//		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(loggedInInfo);
//		return(accountWs.generateSecurityToken(loggedInInfo.getLoggedInPersonId()));
//	}
}
