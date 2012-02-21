package org.oscarehr.ws;

import org.apache.log4j.Logger;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.message.token.UsernameToken;
import org.apache.ws.security.validate.UsernameTokenValidator;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.quatro.dao.security.SecurityDao;
import com.quatro.model.security.Security;

public class OscarUsernameTokenValidator extends UsernameTokenValidator
{
	private static final Logger logger = MiscUtils.getLogger();
	private SecurityDao securityDao = (SecurityDao) SpringUtils.getBean("securityDao");

	@Override
	protected void verifyPlaintextPassword(UsernameToken usernameToken, RequestData data) throws WSSecurityException
	{
		Security security = checkAuthentication(usernameToken.getName(), usernameToken.getPassword());
		if (security == null) throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION);
		
		LoggedInInfo x=new LoggedInInfo();
		x.loggedInSecurity=security;
		LoggedInInfo.loggedInInfo.set(x);
	}

	private Security checkAuthentication(String userIdString, String password)
	{
		logger.debug("userIdString=" + userIdString);
		logger.debug("password=" + password);

		if (userIdString == null || password == null) return(null);

		Integer securityUserId = null;
		try
		{
			securityUserId = Integer.parseInt(userIdString);

			// check security token
//			Long tokenId = AccountManager.checkSecurityToken(personIdLong, password);
//			logger.debug("tokenId=" + tokenId);
//			if (tokenId != null)
//			{
//				AccountManager.updateSecurityTokenLastUsedDate(tokenId);
//				Person person = personDao.find(personIdLong);
//				return(person);
//			}

			// check regular password
			Security security = securityDao.findById(securityUserId);
			if (security != null && security.checkPassword(password)) return(security);
		}
		catch (NumberFormatException e)
		{
			logger.error("userIdString is not a number? userIdString='" + userIdString + '\'');
		}

		return(null);
	}
}