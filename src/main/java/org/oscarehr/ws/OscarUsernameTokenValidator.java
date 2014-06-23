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


package org.oscarehr.ws;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.message.token.UsernameToken;
import org.apache.ws.security.validate.UsernameTokenValidator;
import org.oscarehr.common.dao.SecurityDao;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;


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
			Security security = securityDao.find(securityUserId);
			
			// if it's all good just return
			SoapMessage soapMessage = (SoapMessage) data.getMsgContext();
			HttpServletRequest request = (HttpServletRequest) soapMessage.get(AbstractHTTPDestination.HTTP_REQUEST);
			if (WsUtils.checkAuthenticationAndSetLoggedInInfo(request, security, usernameToken.getPassword())) return;
		} catch (NumberFormatException e) {
			logger.error("userIdString is not a number? usernameToken.getName()='" + usernameToken.getName() + '\'');
		}

		throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION);
	}
}
