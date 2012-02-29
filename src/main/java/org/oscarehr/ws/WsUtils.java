package org.oscarehr.ws;

import java.util.Date;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import com.quatro.model.security.Security;

public final class WsUtils
{
	private static ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");

	/**
	 * This method will check to see if the person is allowed to login, i.e. it will check username/expiry/password.
	 * If the person is allowed it will setup the loggedInInfo data.
	 * 
	 * @param security can be null, it will return false for null. 
	 * @param securityToken can be the SecurityId's password, or a valid securityToken.
	 */
	public static boolean checkAuthenticationAndSetLoggedInInfo(Security security, String securityToken)
	{
		if (security != null)
		{
			if (security.getDateExpiredate()!=null && security.getDateExpiredate().before(new Date())) return(false);
			
			if (checkToken(security, securityToken) || security.checkPassword(securityToken))
			{
				LoggedInInfo x = new LoggedInInfo();
				x.loggedInSecurity = security;
				if (security.getProviderNo() != null) {
					x.loggedInProvider = providerDao.getProvider(security.getProviderNo());
				}

				LoggedInInfo.loggedInInfo.set(x);
				return(true);
			}
		}
		
		return(false);
	}

	private static boolean checkToken(Security security, String securityToken) {
// will sort this out later when we setup tokens
	    return false;
    }
}