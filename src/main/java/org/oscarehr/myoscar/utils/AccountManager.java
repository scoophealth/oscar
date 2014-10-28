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
package org.oscarehr.myoscar.utils;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.AccountWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.AddressException_Exception;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.Dimension;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.DuplicateUniqueKeyException_Exception;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.IOException_Exception;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.InvalidRequestException_Exception;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.ItemAlreadyExistsException_Exception;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.LoginResultTransfer3;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.LoginWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MinimalPersonTransfer3;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.NoSuchAlgorithmException_Exception;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.NotAuthorisedException_Exception;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.PersonEmergencyContactTransfer2;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.PersonHealthInsuranceTransfer2;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.PersonPermissionTransfer;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.PersonPhotoTransfer;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.PersonPreferencesKeyValueTypeTransfer;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.PersonTransfer3;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.UnexpectedErrorException_Exception;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.UnsupportedEncodingException_Exception;
import org.oscarehr.util.ConfigXmlUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.QueueCache;

public final class AccountManager {
	private static Logger logger = MiscUtils.getLogger();

	public static final int PERSON_PHOTO_SMALL_DIMENSIONS = 240;
	public static final int PERSON_PHOTO_LARGE_DIMENSIONS = 1024;

	private static final int MAX_PEOPLE_CACHE_SIZE = ConfigXmlUtils.getPropertyInt("myoscar_client", "account_manager_people_cache_size");
	private static final long MAX_PEOPLE_CACHE_TIME = ConfigXmlUtils.getPropertyInt("myoscar_client", "account_manager_people_cache_time_ms");
	private static final int MAX_HAS_PHOTO_CACHE_SIZE = ConfigXmlUtils.getPropertyInt("myoscar_client", "has_person_photo_cache_size");
	private static final long MAX_HAS_PHOTO_CACHE_TIME = ConfigXmlUtils.getPropertyInt("myoscar_client", "has_person_photo_cache_time_ms");
	private static final int MAX_MINIMAL_PEOPLE_CACHE_SIZE = ConfigXmlUtils.getPropertyInt("myoscar_client", "account_manager_minimal_people_cache_size");
	private static final long MAX_MINIMAL_PEOPLE_CACHE_TIME = ConfigXmlUtils.getPropertyInt("myoscar_client", "account_manager_minimal_people_cache_time_ms");
	private static final int MAX_PREFERENCE_CACHE_SIZE = ConfigXmlUtils.getPropertyInt("myoscar_client", "preference_cache_size");
	private static final long MAX_PREFERENCE_CACHE_TIME = ConfigXmlUtils.getPropertyInt("myoscar_client", "preference_cache_time_ms");

	/**
	 * Cache key must be qualified so 2 people don't share a cached item, even if it's the same thing,
	 * the request must be made to the server so the access is logged and authentication is checked.
	 * 
	 * MAX_CACHE_ITEM_SIZE*items to queue = max memory used for cache
	 */
	private static QueueCache<String, PersonTransfer3> personCache = new QueueCache<String, PersonTransfer3>(4, MAX_PEOPLE_CACHE_SIZE, MAX_PEOPLE_CACHE_TIME, null);

	/**
	 *  by sharing the value object having 2 caches doesn't take up more memory as it will key to the same memory location
	 */
	private static QueueCache<Long, MinimalPersonTransfer3> minimalPersonCache = new QueueCache<Long, MinimalPersonTransfer3>(4, MAX_MINIMAL_PEOPLE_CACHE_SIZE, MAX_MINIMAL_PEOPLE_CACHE_TIME, null);

	/**
	 *  by sharing the value object having 2 caches doesn't take up more memory as it will key to the same memory location
	 */
	private static QueueCache<String, MinimalPersonTransfer3> minimalPersonUsernameCache = new QueueCache<String, MinimalPersonTransfer3>(4, MAX_MINIMAL_PEOPLE_CACHE_SIZE, MAX_MINIMAL_PEOPLE_CACHE_TIME, null);

	private static QueueCache<Long, Boolean> hasPhotoCache = new QueueCache<Long, Boolean>(4, MAX_HAS_PHOTO_CACHE_SIZE, MAX_HAS_PHOTO_CACHE_TIME, null);

	private static QueueCache<String, String> preferenceStringCache = new QueueCache<String, String>(4, MAX_PREFERENCE_CACHE_SIZE, MAX_PREFERENCE_CACHE_TIME, null);
	private static QueueCache<String, Integer> preferenceIntCache = new QueueCache<String, Integer>(4, MAX_PREFERENCE_CACHE_SIZE, MAX_PREFERENCE_CACHE_TIME, null);
	private static QueueCache<String, Calendar> preferenceCalCache = new QueueCache<String, Calendar>(4, MAX_PREFERENCE_CACHE_SIZE, MAX_PREFERENCE_CACHE_TIME, null);
	private static QueueCache<String, Boolean> preferenceBooleanCache = new QueueCache<String, Boolean>(4, MAX_PREFERENCE_CACHE_SIZE, MAX_PREFERENCE_CACHE_TIME, null);

	/**
	 * @return the id of the new permissions entry added or null if no new entry was added due to already existing permissions 
	 */
	public static Long addPersonPermissions(MyOscarLoggedInInfo credentials, PersonPermissionTransfer personPermissionTransfer) throws NotAuthorisedException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		Long result = accountWs.addPersonPermissions(personPermissionTransfer);
		return (result);
	}

	private static void addToMinimalPersonCache(MinimalPersonTransfer3 minimalPersonTransfer) {
		minimalPersonCache.put(minimalPersonTransfer.getPersonId(), minimalPersonTransfer);
		minimalPersonUsernameCache.put(minimalPersonTransfer.getUserName(), minimalPersonTransfer);
	}

	private static void removeFromMinimalPersonCache(Long personId, String userName) {
		if (personId != null) minimalPersonCache.remove(personId);
		if (userName != null) minimalPersonUsernameCache.remove(userName);
	}

	private static void addToMinimalPersonCache(List<MinimalPersonTransfer3> minimalPersonTransfers) {
		for (MinimalPersonTransfer3 minimalPersonTransfer : minimalPersonTransfers) {
			addToMinimalPersonCache(minimalPersonTransfer);
		}
	}

	public static MinimalPersonTransfer3 getMinimalPerson(MyOscarLoggedInInfo credentials, Long userId) {
		// minimal person records have no security so global caching is allowed.
		MinimalPersonTransfer3 minimalPersonTransfer = minimalPersonCache.get(userId);

		if (minimalPersonTransfer == null) {
			AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
			minimalPersonTransfer = accountWs.getMinimalPerson3(userId);
			if (minimalPersonTransfer != null) addToMinimalPersonCache(minimalPersonTransfer);
		}

		return (minimalPersonTransfer);
	}

	public static MinimalPersonTransfer3 getMinimalPerson(MyOscarLoggedInInfo credentials, String userName) {
		if (userName == null) return (null);

		// minimal person records have no security so global caching is allowed.
		MinimalPersonTransfer3 minimalPersonTransfer = minimalPersonUsernameCache.get(userName);

		if (minimalPersonTransfer == null) {
			AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
			minimalPersonTransfer = accountWs.getMinimalPersonByUserName3(userName);
			if (minimalPersonTransfer != null) addToMinimalPersonCache(minimalPersonTransfer);
		}

		return (minimalPersonTransfer);
	}

	public static Long getUserId(MyOscarLoggedInInfo credentials, String userName) {
		MinimalPersonTransfer3 minimalPersonTransfer = getMinimalPerson(credentials, userName);
		if (minimalPersonTransfer == null) return (null);
		else return (minimalPersonTransfer.getPersonId());
	}

	private static String getPersonCacheKey(MyOscarLoggedInInfo credentials, Long personId) {
		// we're qualifying session and person because
		// the "loggedInPersonId" is important because if some one manages to logs in with our clearing the session we might see old data from the last person logged in, in theory not possible as we clear the session on both logout and login but lets be double safe. 
		// the "loggedInSessionId" is desirable because if the same person logs in and logs out and logs back in, we want to ignore previously cached data, if effectively lets people clear the cache.  
		return (credentials.getLoggedInSessionId() + ':' + credentials.getLoggedInPersonId() + ':' + personId);
	}

	/**
	 * @return the logged in person or null if invalid login.
	 */
	public static LoginResultTransfer3 login(String myOscarServerBaseUrl, String userName, String password) throws NotAuthorisedException_Exception {
		try {
			LoginWs loginWs = MyOscarServerWebServicesManager.getLoginWs(myOscarServerBaseUrl);
			LoginResultTransfer3 loginResultTransfer = loginWs.login4(userName, password);
			return (loginResultTransfer);
		} catch (NotAuthorisedException_Exception e) {
			logger.warn("Invalid Login Request userName=" + userName);
			throw (e);
		}
	}

	/**
	 * @return the logged in person or null if invalid login.
	 */
	public static LoginResultTransfer3 login(String myOscarServerBaseUrl, Long userId, String password) throws NotAuthorisedException_Exception {
		try {
			LoginWs loginWs = MyOscarServerWebServicesManager.getLoginWs(myOscarServerBaseUrl);
			LoginResultTransfer3 loginResultTransfer = loginWs.loginById2(userId, password);
			return (loginResultTransfer);
		} catch (NotAuthorisedException_Exception e) {
			logger.warn("Invalid Login Request userId=" + userId);
			throw (e);
		}
	}

	public static List<PersonPreferencesKeyValueTypeTransfer> getPersonPreferences(MyOscarLoggedInInfo credentials, Long preferenceOwnerUserId, String qualifier, byte[] startKey, int itemsToReturn, boolean excludeStartKey) throws NotAuthorisedException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		List<PersonPreferencesKeyValueTypeTransfer> personPreferencesTransfer = accountWs.getPersonPreferences(preferenceOwnerUserId, qualifier, startKey, itemsToReturn, excludeStartKey);

		return (personPreferencesTransfer);
	}

	private static String makePersonPreferenceCacheKey(Long preferenceOwnerUserId, String qualifier, String key) {
		String cacheKey = "" + preferenceOwnerUserId + ":" + qualifier + "." + key;
		return (cacheKey);
	}

	public static Boolean getPersonPreferenceBoolean(MyOscarLoggedInInfo credentials, Long preferenceOwnerUserId, String qualifier, String key) throws NotAuthorisedException_Exception {
		Boolean result = null;
		String cacheKey = makePersonPreferenceCacheKey(preferenceOwnerUserId, qualifier, key);

		// only check cache if it's their own preference (for security purposes)
		if (preferenceOwnerUserId.equals(credentials.getLoggedInPersonId())) {
			result = preferenceBooleanCache.get(cacheKey);
		}

		if (result == null) {
			AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
			result = accountWs.getPersonPreferenceBoolean(preferenceOwnerUserId, qualifier, key);

			// you can always put preference back in cache regardless of who retrieved it
			if (result != null) preferenceBooleanCache.put(cacheKey, result);
		}

		return (result);
	}

	public static String getPersonPreferenceString(MyOscarLoggedInInfo credentials, Long preferenceOwnerUserId, String qualifier, String key) throws NotAuthorisedException_Exception {
		String result = null;
		String cacheKey = makePersonPreferenceCacheKey(preferenceOwnerUserId, qualifier, key);

		// only check cache if it's their own preference (for security purposes)
		if (preferenceOwnerUserId.equals(credentials.getLoggedInPersonId())) {
			result = preferenceStringCache.get(cacheKey);
		}

		if (result == null) {
			AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
			result = accountWs.getPersonPreferenceString(preferenceOwnerUserId, qualifier, key);

			// you can always put preference back in cache regardless of who retrieved it
			if (result != null) preferenceStringCache.put(cacheKey, result);
		}

		return (result);
	}

	public static Integer getPersonPreferenceInt(MyOscarLoggedInInfo credentials, Long preferenceOwnerUserId, String qualifier, String key) throws NotAuthorisedException_Exception {
		Integer result = null;
		String cacheKey = makePersonPreferenceCacheKey(preferenceOwnerUserId, qualifier, key);

		// only check cache if it's their own preference (for security purposes)
		if (preferenceOwnerUserId.equals(credentials.getLoggedInPersonId())) {
			result = preferenceIntCache.get(cacheKey);
		}

		if (result == null) {
			AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
			result = accountWs.getPersonPreferenceInt(preferenceOwnerUserId, qualifier, key);

			// you can always put preference back in cache regardless of who retrieved it
			if (result != null) preferenceIntCache.put(cacheKey, result);
		}

		return (result);
	}

	public static Calendar getPersonPreferenceCalendar(MyOscarLoggedInInfo credentials, Long preferenceOwnerUserId, String qualifier, String key) throws NotAuthorisedException_Exception {
		Calendar result = null;
		String cacheKey = makePersonPreferenceCacheKey(preferenceOwnerUserId, qualifier, key);

		// only check cache if it's their own preference (for security purposes)
		if (preferenceOwnerUserId.equals(credentials.getLoggedInPersonId())) {
			result = preferenceCalCache.get(cacheKey);
		}

		if (result == null) {
			AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
			result = accountWs.getPersonPreferenceCalendar(preferenceOwnerUserId, qualifier, key);

			// you can always put preference back in cache regardless of who retrieved it
			if (result != null) preferenceCalCache.put(cacheKey, result);
		}

		return (result);
	}

	public static boolean getDataSyncPreferenceForCallingEntity(MyOscarLoggedInInfo credentials, Long patientPersonId) throws NotAuthorisedException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		return (accountWs.getDataSyncPreferenceForCallingEntity(patientPersonId));
	}

	public static void setPersonPreference(MyOscarLoggedInInfo credentials, Long preferenceOwnerUserId, String qualifier, String key, String value) throws NotAuthorisedException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		accountWs.setPersonPreferenceString(preferenceOwnerUserId, qualifier, key, value);

		String cacheKey = makePersonPreferenceCacheKey(preferenceOwnerUserId, qualifier, key);
		preferenceStringCache.remove(cacheKey);
	}

	public static void setPersonPreference(MyOscarLoggedInInfo credentials, Long preferenceOwnerUserId, String qualifier, String key, int value) throws NotAuthorisedException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		accountWs.setPersonPreferenceInt(preferenceOwnerUserId, qualifier, key, value);

		String cacheKey = makePersonPreferenceCacheKey(preferenceOwnerUserId, qualifier, key);
		preferenceIntCache.remove(cacheKey);
	}

	public static void setPersonPreference(MyOscarLoggedInInfo credentials, Long preferenceOwnerUserId, String qualifier, String key, boolean value) throws NotAuthorisedException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		accountWs.setPersonPreferenceBoolean(preferenceOwnerUserId, qualifier, key, value);

		String cacheKey = makePersonPreferenceCacheKey(preferenceOwnerUserId, qualifier, key);
		preferenceBooleanCache.remove(cacheKey);
	}

	public static void setPersonPreference(MyOscarLoggedInInfo credentials, Long preferenceOwnerUserId, String qualifier, String key, Calendar value) throws NotAuthorisedException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		accountWs.setPersonPreferenceCalendar(preferenceOwnerUserId, qualifier, key, value);

		String cacheKey = makePersonPreferenceCacheKey(preferenceOwnerUserId, qualifier, key);
		preferenceCalCache.remove(cacheKey);
	}

	public static void removePersonPreference(MyOscarLoggedInInfo credentials, Long preferenceOwnerUserId, String qualifier, String key) throws NotAuthorisedException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		accountWs.removePersonPreference(preferenceOwnerUserId, qualifier, key);

		String cacheKey = makePersonPreferenceCacheKey(preferenceOwnerUserId, qualifier, key);
		preferenceStringCache.remove(cacheKey);
		preferenceBooleanCache.remove(cacheKey);
		preferenceCalCache.remove(cacheKey);
		preferenceIntCache.remove(cacheKey);
	}

	public static void changeCurrentUserPassword(MyOscarLoggedInInfo credentials, String oldPassword, String newPassword) throws InvalidRequestException_Exception, NotAuthorisedException_Exception, UnsupportedEncodingException_Exception, DuplicateUniqueKeyException_Exception, NoSuchAlgorithmException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		Long personId = credentials.getLoggedInPersonId();
		accountWs.changePassword(personId, oldPassword, newPassword);
	}

	public static PersonTransfer3 getPerson(MyOscarLoggedInInfo credentials, Long requestedPersonId) throws NotAuthorisedException_Exception {
		String cacheKey = getPersonCacheKey(credentials, requestedPersonId);
		PersonTransfer3 personTransfer = personCache.get(cacheKey);

		if (personTransfer == null) {
			AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
			personTransfer = accountWs.getPerson3(requestedPersonId);

			personCache.put(cacheKey, personTransfer);
		}

		return (personTransfer);
	}

	public static PersonTransfer3 getPerson(MyOscarLoggedInInfo credentials, String personUserName) throws NotAuthorisedException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		PersonTransfer3 personTransfer = accountWs.getPersonByUserName3(personUserName, true);

		String cacheKey = getPersonCacheKey(credentials, personTransfer.getId());
		personCache.put(cacheKey, personTransfer);

		return (personTransfer);
	}

	public static List<PersonTransfer3> getPeopleByRole(MyOscarLoggedInInfo credentials, String role, Boolean active, int startIndex, int itemsToReturn) {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		List<PersonTransfer3> results = accountWs.getPeopleByRole(role, active, startIndex, itemsToReturn);

		for (PersonTransfer3 personTransfer : results) {
			String cacheKey = getPersonCacheKey(credentials, personTransfer.getId());
			personCache.put(cacheKey, personTransfer);
		}

		return (results);
	}

	public static List<MinimalPersonTransfer3> getMinimalPeopleByRole(MyOscarLoggedInInfo credentials, String role, Boolean active, int startIndex, int itemsToReturn) {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		List<MinimalPersonTransfer3> results = accountWs.getMinimalPeopleByRole2(role, active, startIndex, itemsToReturn);

		addToMinimalPersonCache(results);

		return (results);
	}

	public static void updatePerson(MyOscarLoggedInInfo credentials, PersonTransfer3 personTransfer) throws NotAuthorisedException_Exception, AddressException_Exception, DuplicateUniqueKeyException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		accountWs.updatePerson3(personTransfer);

		String cacheKey = getPersonCacheKey(credentials, personTransfer.getId());
		personCache.put(cacheKey, personTransfer);
		removeFromMinimalPersonCache(personTransfer.getId(), personTransfer.getUserName());
	}

	public static PersonTransfer3 createPerson(MyOscarLoggedInInfo credentials, PersonTransfer3 personTransfer, String password, boolean active) throws NotAuthorisedException_Exception, ItemAlreadyExistsException_Exception, AddressException_Exception {
		personTransfer.setActive(active);
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		PersonTransfer3 result = accountWs.addPerson3(personTransfer, password);
		return (result);
	}

	public static List<MinimalPersonTransfer3> searchPeople(MyOscarLoggedInInfo credentials, PersonTransfer3 personTransfer, Boolean active, int startIndex, int itemsToReturn) {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		List<MinimalPersonTransfer3> results = accountWs.searchPeople5(personTransfer, active, startIndex, itemsToReturn);

		addToMinimalPersonCache(results);

		return (results);
	}

	public static List<PersonHealthInsuranceTransfer2> getPersonHealthInsurances(MyOscarLoggedInInfo credentials, Long personId) throws NotAuthorisedException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		List<PersonHealthInsuranceTransfer2> results = accountWs.getAllPersonHealthInsurances(personId, true);
		return (results);
	}

	public static PersonHealthInsuranceTransfer2 setPersonHealthInsurance(MyOscarLoggedInInfo credentials, PersonHealthInsuranceTransfer2 personHealthInsuranceTransfer, boolean active) throws NotAuthorisedException_Exception, UnexpectedErrorException_Exception {
		personHealthInsuranceTransfer.setActive(active);
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		personHealthInsuranceTransfer = accountWs.setPersonHealthInsurance(personHealthInsuranceTransfer);
		return (personHealthInsuranceTransfer);
	}

	public static PersonEmergencyContactTransfer2 getPersonEmergencyContact(MyOscarLoggedInInfo credentials, Long personId) throws NotAuthorisedException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		PersonEmergencyContactTransfer2 result = accountWs.getPersonEmergencyContact(personId);
		return (result);
	}

	public static void setPersonEmergencyContact(MyOscarLoggedInInfo credentials, PersonEmergencyContactTransfer2 personEmergencyContactTransfer) throws NotAuthorisedException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		accountWs.setPersonEmergencyContact(personEmergencyContactTransfer);
	}

	public static PersonPhotoTransfer getPersonPhotoByPerson(MyOscarLoggedInInfo credentials, Long personId, Dimension maxDimension) {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		PersonPhotoTransfer result = accountWs.getPersonPhotoByPerson(personId, maxDimension);

		return (result);
	}

	public static PersonPhotoTransfer getPersonPhotoByPerson(MyOscarLoggedInInfo credentials, Long personId, int maxDimension) {
		Dimension d = new Dimension();
		d.setWidth(maxDimension);
		d.setHeight(maxDimension);

		return (getPersonPhotoByPerson(credentials, personId, d));
	}

	public static boolean hasPersonPhoto(MyOscarLoggedInInfo credentials, Long personId) {
		Boolean result = hasPhotoCache.get(personId);

		if (result == null) {
			PersonPhotoTransfer temp = getPersonPhotoByPerson(credentials, personId, PERSON_PHOTO_SMALL_DIMENSIONS);
			result = temp != null;
			hasPhotoCache.put(personId, result);
		}

		return (result);
	}

	/**
	 * This method will remove previous images of this person from the cache if they are of size PERSON_PHOTO_SMALL_DIMENSIONS, or PERSON_PHOTO_LARGE_DIMENSIONS.
	 * If you are using a custom size you will either have to remove it from the cache manually or wait for the cache to flush by itself.
	 */
	public static void setPersonPhoto(MyOscarLoggedInInfo credentials, PersonPhotoTransfer personPhotoTransfer) throws NotAuthorisedException_Exception, IOException_Exception {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		accountWs.setPersonPhoto(personPhotoTransfer);

		Long photoOwnerId = personPhotoTransfer.getOwnerPersonId();

		hasPhotoCache.remove(photoOwnerId);
	}

	public static void removePersonPhoto(MyOscarLoggedInInfo credentials, byte[] personPhotoId) throws NotAuthorisedException_Exception {
		PersonPhotoTransfer personPhotoTransfer = getPersonPhoto(credentials, personPhotoId, PERSON_PHOTO_SMALL_DIMENSIONS);
		if (personPhotoTransfer == null) return;

		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		accountWs.removePersonPhoto(personPhotoId);

		hasPhotoCache.remove(personPhotoTransfer.getOwnerPersonId());
	}

	public static PersonPhotoTransfer getPersonPhoto(MyOscarLoggedInInfo credentials, byte[] personPhotoId, int maxDimension) {
		Dimension d = new Dimension();
		d.setWidth(maxDimension);
		d.setHeight(maxDimension);

		return (getPersonPhoto(credentials, personPhotoId, d));
	}

	public static PersonPhotoTransfer getPersonPhoto(MyOscarLoggedInInfo credentials, byte[] personPhotoId, Dimension maxDimension) {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(credentials);
		PersonPhotoTransfer result = accountWs.getPersonPhoto(personPhotoId, maxDimension);

		return (result);
	}
}
