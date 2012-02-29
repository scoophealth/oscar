package org.oscarehr.ws;

import java.util.List;

import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.NotAuthorisedException;
import org.oscarehr.ws.transfer_objects.LoginResultTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quatro.dao.security.SecurityDao;
import com.quatro.model.security.Security;

@WebService
@Component
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
