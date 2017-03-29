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

package org.oscarehr.phr.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.BadPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.dao.ProviderPreferenceDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProviderPreference;
import org.oscarehr.myoscar.client.ws_manager.AccountManager;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.myoscar_server.ws.LoginResultTransfer3;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.DeamonThreadFactory;
import org.oscarehr.util.EncryptionUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;


public final class MyOscarUtils {
	private static final Logger logger = MiscUtils.getLogger();

	private static final String MANGLED_SECRET_KEY_SESSION_KEY=MyOscarUtils.class.getName()+".MANGLED_SECRET_KEY";

	private static ExecutorService asyncAutoLoginThreadPool=Executors.newFixedThreadPool(4, new DeamonThreadFactory("asyncAutoLoginThreadPool", Thread.MIN_PRIORITY));
	
	public static Demographic getDemographicByMyOscarUserName(String myOscarUserName) {
		DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
		Demographic demographic = demographicDao.getDemographicByMyOscarUserName(myOscarUserName);
		return (demographic);
	}

	public static boolean isMyOscarEnabled(String providerNo)
	{
		return isMyOscarUserNameSet(providerNo);
	}
	
	public static String getDisabledStringForMyOscarSendButton(MyOscarLoggedInInfo myOscarLoggedInInfo, Integer demographicId) {
		boolean enabled = isMyOscarSendButtonEnabled(myOscarLoggedInInfo, demographicId);

		return (WebUtils.getDisabledString(enabled));
	}

	public static String getDisabledStringForMyOscarSendButton(MyOscarLoggedInInfo myOscarLoggedInInfo, Demographic demographic) {
		boolean enabled = isMyOscarSendButtonEnabled(myOscarLoggedInInfo, demographic);

		return (WebUtils.getDisabledString(enabled));
	}

	public static boolean isMyOscarSendButtonEnabled(MyOscarLoggedInInfo myOscarLoggedInInfo, Integer demographicId) {
		DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
		Demographic demographic = demographicDao.getDemographicById(demographicId);

		return (isMyOscarSendButtonEnabled(myOscarLoggedInInfo, demographic));
	}

	public static boolean isMyOscarSendButtonEnabled(MyOscarLoggedInInfo myOscarLoggedInInfo, Demographic demographic) {
		return (myOscarLoggedInInfo != null && myOscarLoggedInInfo.isLoggedIn() && demographic != null && demographic.getMyOscarUserName() != null);
	}

	public static void attemptMyOscarAutoLoginIfNotAlreadyLoggedInAsynchronously(final LoggedInInfo loggedInInfo, final boolean forceReLogin) {
		if (!isMyOscarEnabled(loggedInInfo.getLoggedInProviderNo())) return;

		HttpSession session = loggedInInfo.getSession();
		MyOscarLoggedInInfo myOscarLoggedInInfo = MyOscarLoggedInInfo.getLoggedInInfo(session);
		if (myOscarLoggedInInfo != null && myOscarLoggedInInfo.isLoggedIn()) return;

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					attemptMyOscarAutoLoginIfNotAlreadyLoggedIn(loggedInInfo, forceReLogin);
				} finally {
					DbConnectionFilter.releaseAllThreadDbResources();
				}
			}
		};

		asyncAutoLoginThreadPool.submit(runnable);
	}

	public static String getMyOscarUserNameFromOscar(String providerNo)
	{
		PropertyDao propertyDao = (PropertyDao) SpringUtils.getBean("propertyDao");
		List<org.oscarehr.common.model.Property> myOscarUserNameProperties=propertyDao.findByNameAndProvider("MyOscarId", providerNo); 
		if (myOscarUserNameProperties.size()>0) return(myOscarUserNameProperties.get(0).getValue());
		return(null);
	}
	
	public static boolean isMyOscarUserNameSet(String providerNo){
		if(getMyOscarUserNameFromOscar(providerNo) != null){
			return true;
		}
		return false;
	}
	
	
	public static Long getMyOscarUserIdFromOscarProviderNo(MyOscarLoggedInInfo myOscarLoggedInInfo, String providerNo)
	{
		String userName =getMyOscarUserNameFromOscar(providerNo);
		return(AccountManager.getUserId(myOscarLoggedInInfo, userName));
	}
	
	public static Long getMyOscarUserIdFromOscarDemographicId(MyOscarLoggedInInfo myOscarLoggedInInfo, Integer demographicId)
	{
		DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
		Demographic demographic = demographicDao.getDemographicById(demographicId);
		return(AccountManager.getUserId(myOscarLoggedInInfo, demographic.getMyOscarUserName()));
	}
	
	public static void attemptMyOscarAutoLoginIfNotAlreadyLoggedIn(LoggedInInfo loggedInInfo, boolean forceReLogin) {
		HttpSession session = loggedInInfo.getSession();

		ProviderPreferenceDao providerPreferenceDao = (ProviderPreferenceDao) SpringUtils.getBean("providerPreferenceDao");
		ProviderPreference providerPreference = providerPreferenceDao.find(loggedInInfo.getLoggedInProviderNo());
		String myOscarUserName = null;
		
		MyOscarLoggedInInfo myOscarLoggedInInfo = MyOscarLoggedInInfo.getLoggedInInfo(session);
		if (!forceReLogin && myOscarLoggedInInfo != null && myOscarLoggedInInfo.isLoggedIn()) return;

		try {
			myOscarUserName = getMyOscarUserNameFromOscar(loggedInInfo.getLoggedInProviderNo());
			if (myOscarUserName == null) return;

			byte[] encryptedMyOscarPassword = providerPreference.getEncryptedMyOscarPassword();
			if (encryptedMyOscarPassword == null) return;

			SecretKeySpec key = getDeterministicallyMangledPasswordSecretKeyFromSession(session);
			byte[] decryptedMyOscarPasswordBytes = EncryptionUtils.decrypt(key, encryptedMyOscarPassword);
			String decryptedMyOscarPasswordString = new String(decryptedMyOscarPasswordBytes, "UTF-8");

			LoginResultTransfer3 loginResultTransfer = AccountManager.login(MyOscarLoggedInInfo.getMyOscarServerBaseUrl(), myOscarUserName, decryptedMyOscarPasswordString);
			
			if(loginResultTransfer.getPerson().getRole() != null && !loginResultTransfer.getPerson().getRole().equals("PATIENT")){
				myOscarLoggedInInfo = new MyOscarLoggedInInfo(loginResultTransfer.getPerson().getId(), loginResultTransfer.getSecurityTokenKey(), session.getId(), loggedInInfo.getLocale());
				MyOscarLoggedInInfo.setLoggedInInfo(session, myOscarLoggedInInfo);
			}else{
				logger.error("ERROR :"+loginResultTransfer.getPerson().getUserName()+" can not login with role "+loginResultTransfer.getPerson().getRole());
			}
		} catch (NotAuthorisedException_Exception e) {
			logger.warn("Could not login to MyOscar, invalid credentials. chances are myoscar pw changed, myOscarUserName=" + myOscarUserName);

			// login failed, remove myoscar saved password
			providerPreference.setEncryptedMyOscarPassword(null);
			providerPreferenceDao.merge(providerPreference);
		} catch (BadPaddingException e) {
			logger.debug("stored password no longer valid due to change in encryption keys");

			// remove myoscar saved password
			providerPreference.setEncryptedMyOscarPassword(null);
			providerPreferenceDao.merge(providerPreference);
		} catch (Exception t) {
			logger.error("Error attempting auto-myoscar login", t);
		}
	}
	
	/**
	 * deterministically mangle the byte input, doesn't have to be complex, just something different.
	 * Reason being we don't want to just sha1 the oscar_password as the encryption key because
	 * that's already stored in the db as the password record.
	 */
	public static String deterministicallyMangle(String s)
	{
		Random random=new Random(s.length());

		StringBuilder sb=new StringBuilder();
		
		for (int i=0; i< s.length(); i++)
		{
			sb.append(random.nextInt(s.charAt(i)));
			
		}
				
		return(sb.toString());
	}
	
	public static SecretKeySpec getSecretKeyFromDeterministicallyMangledPassword(String unmangledPassword)
	{
		String mangledPassword=deterministicallyMangle(unmangledPassword);
		SecretKeySpec secretKeySpec=EncryptionUtils.generateEncryptionKey(mangledPassword);
		return(secretKeySpec);
	}
	
	public static void setDeterministicallyMangledPasswordSecretKeyIntoSession(HttpSession session, String unmangledPassword)
	{
		SecretKeySpec secretKeySpec=getSecretKeyFromDeterministicallyMangledPassword(unmangledPassword);
		session.setAttribute(MANGLED_SECRET_KEY_SESSION_KEY, secretKeySpec);
	}
	
	public static SecretKeySpec getDeterministicallyMangledPasswordSecretKeyFromSession(HttpSession session)
	{
		return (SecretKeySpec) (session.getAttribute(MANGLED_SECRET_KEY_SESSION_KEY));
	}
	
	public static String encodeBase64EscapeUrl(byte[] id) throws UnsupportedEncodingException
	{
		String s=Base64.encodeBase64String(id);
		s=URLEncoder.encode(s, MiscUtils.DEFAULT_UTF8_ENCODING);
		return(s);
	}
	
	public static byte[] decodeBase64Id(String s)
	{
		return(Base64.decodeBase64(s));
	}
}
