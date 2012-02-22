package org.oscarehr.ws;

import org.apache.log4j.Logger;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.message.token.UsernameToken;
import org.apache.ws.security.validate.UsernameTokenValidator;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.quatro.dao.security.SecurityDao;
import com.quatro.model.security.Security;

/**
 * Validation on a per-request basis is done against the Security table on the ID and password (not username). This is for efficiency purposes and immutability purposes of the ID. To get the ID some one can always use the LoginWs first which should supply
 * you with the ID from the userName. The loginWs may also provide you with a security token which can then be used in place of the password. WS authorisation is only based on password and expiry time, all other fields are ignored as they don't make sense
 * for this usage.
 */
public class OscarUsernameTokenValidator extends UsernameTokenValidator {
	private static final Logger logger = MiscUtils.getLogger();
	private SecurityDao securityDao = (SecurityDao) SpringUtils.getBean("securityDao");

	@Override
	protected void verifyPlaintextPassword(UsernameToken usernameToken, RequestData data) throws WSSecurityException {
		logger.debug("userIdString=" + usernameToken.getName());
		logger.debug("password=" + usernameToken.getPassword());

		try {
			Integer securityUserId = Integer.parseInt(usernameToken.getName());
			Security security = securityDao.findById(securityUserId);
			
			// if it's all good just return
			if (WsUtils.checkAuthenticationAndSetLoggedInInfo(security, usernameToken.getPassword())) return;
		} catch (NumberFormatException e) {
			logger.error("userIdString is not a number? usernameToken.getName()='" + usernameToken.getName() + '\'');
		}

		throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION);
	}
}