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

import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.annotations.GZIP;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.SecurityDao;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.NotAuthorisedException;
import org.oscarehr.ws.transfer_objects.LoginResultTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@WebService
@Component
@GZIP(threshold=AbstractWs.GZIP_THRESHOLD)
public class LoginWs extends AbstractWs {
	private static final Logger logger = MiscUtils.getLogger();

	@Autowired
	private SecurityDao securityDao = null;

	/**
	 * Returns LoginResultTransfer on valid login, will be provided with a security token too.
	 * @throws NotAuthorisedException if password is incorrect
	 */
	public org.oscarehr.ws.transfer_objects.LoginResultTransfer login(String userName, String password) throws NotAuthorisedException {
		logger.info("Login attempt : user=" + userName);
		logger.debug("Login attempt : p =" + password);

		List<Security> securities = securityDao.findByUserName(userName);
		Security security = null;

		if (securities.size() > 0) security = securities.get(0);

		if (WsUtils.checkAuthenticationAndSetLoggedInInfo(security, password)) {
			LoginResultTransfer result = new LoginResultTransfer();
			result.setSecurityId(security.getSecurityNo());

			// we haven't sorted out the token framework so we'll just return the pw for now
			result.setSecurityTokenKey(password);

			return (result);
		}

		throw (new NotAuthorisedException("Invalid Username/Password"));
	}
}
