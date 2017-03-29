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
package oscar.login.jaas;

import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.log4j.Logger;

/**
 * Common base for a login module.
 */
public class BaseLoginModule implements LoginModule {

	/**
	 * Option key for the authorization.
	 */
	public static final String OPTION_ATN_ENABLED = "authorizationEnabled";

	private static Logger logger = Logger.getLogger(BaseLoginModule.class);

	private Subject subject;

	private Map<String, ?> sharedState;

	private Map<String, ?> options;

	private CallbackHandler callbackHandler;

	private OscarPrincipal principal;

	private Group rolesGroup;

	private Group callerPrincipal;

	private Group authPrincipal;

	private boolean authorizationEnabled = false;

	private List<String> defaultRoles = new ArrayList<String>();

	/**
	 * Sets parameters for further user.
	 * 
	 * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject, javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
	 */
	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
		setSharedState(sharedState);
		setSubject(subject);
		setCallbackHandler(callbackHandler);
		setOptions(options);
		
		setAuthorizationEnabled(Boolean.parseBoolean("" + options.get(OPTION_ATN_ENABLED)));
	}

	/**
	 * Carries out the login process.
	 * 
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	@Override
	public boolean login() throws LoginException {
		if (logger.isDebugEnabled()) {
			logger.debug("Initiating login for " + this);
		}

		NameCallback nameCallback = new NameCallback("Username");
		PasswordCallback passwordCallback = new PasswordCallback("Password", false);

		try {
			getCallbackHandler().handle(new Callback[] { nameCallback, passwordCallback });
		} catch (IOException e1) {
			throw new LoginException("Unable to retrieve user name or password");
		} catch (UnsupportedCallbackException e1) {
			throw new LoginException("Invalid callbacks for retrieving user name or password");
		}

		String loginName = nameCallback.getName();
		char[] password = null;

		// since password is an array, getting a shared instance won't fly - copy it
		char[] tempPassword = passwordCallback.getPassword();
		if (tempPassword != null) {
			password = new char[tempPassword.length];
			System.arraycopy(tempPassword, 0, password, 0, tempPassword.length);
			passwordCallback.clearPassword();
		}
		passwordCallback.clearPassword();

		if (logger.isDebugEnabled()) {
			logger.debug("Started authentication for " + loginName);
		}

		long loginDuration = System.currentTimeMillis();
		try {
			// carry out authentication
			OscarPrincipal principal = authenticate(loginName, password);
			if (principal == null) throw new LoginException("Invalid login name or password");

			loginDuration = System.currentTimeMillis() - loginDuration;
			Arrays.fill(password, ' ');// clear password
			setPrincipal(principal);

			if (logger.isInfoEnabled()) {
				logger.info("Authenticated user as: " + principal);
			}
		} catch (LoginException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Unable to complete authentication", e);
			throw new LoginException("Unable to authenticate: " + e.getMessage());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Logged in " + loginName + " successfully in {1} ms" + loginDuration);
		}

		if (isAuthorizationEnabled()) {
			// CallerPrincipal group is necessary for making custom principal as the
			// principal returned by the getUserPrincipal() methods in web apps
			OscarGroup callerPrincipal = new OscarGroup("CallerPrincipal");
			callerPrincipal.addMember(getPrincipal());
			setCallerPrincipal(callerPrincipal);

			OscarGroup authPrincipal = new OscarGroup("AuthPrincipal");
			authPrincipal.addMember(getPrincipal());
			setAuthPrincipal(authPrincipal);

			Group rolesGroup = new OscarGroup("Roles");
			for (OscarRole role : getRoles(loginName)) {
				rolesGroup.addMember(role);
			}
			setRolesGroup(rolesGroup);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Completed login for " + this);
		}

		return true;
	}

	@SuppressWarnings("unused")
	protected List<OscarRole> getRoles(String loginName) {
		return new ArrayList<OscarRole>();
	}

	@SuppressWarnings("unused")
	protected OscarPrincipal authenticate(String loginName, char[] password) throws Exception, LoginException {
		if (loginName == null) throw new LoginException("Empty login or password");
		return null;
	}

	public OscarPrincipal getPrincipal() {
		return principal;
	}

	public void setPrincipal(OscarPrincipal principal) {
		this.principal = principal;
	}

	private Principal[] getPrincipals() {
		return new Principal[] { getPrincipal(), getRolesGroup(), getCallerPrincipal(), getAuthPrincipal() };
	}

	@Override
	public boolean commit() throws LoginException {
		Set<Principal> principals = getSubject().getPrincipals();
		for (Principal principal : getPrincipals()) {
			if (principal != null) {
				principals.add(principal);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Completed commit for " + this);
		}
		return true;
	}

	@Override
	public boolean abort() throws LoginException {
		setSubject(null);
		setCallbackHandler(null);
		setPrincipal(null);
		setRolesGroup(null);
		setCallerPrincipal(null);
		setAuthPrincipal(null);

		if (logger.isDebugEnabled()) {
			logger.debug("Completed abort for " + this);
		}
		return true;
	}

	@Override
	public boolean logout() throws LoginException {
		Set<Principal> principals = getSubject().getPrincipals();
		for (Principal principal : getPrincipals())
			principals.remove(principal);

		if (logger.isDebugEnabled()) {
			logger.debug("Completed logout for " + this);
		}
		return true;
	}

	private Subject getSubject() {
		return subject;
	}

	private void setSubject(Subject subject) {
		this.subject = subject;
	}

	private CallbackHandler getCallbackHandler() {
		return callbackHandler;
	}

	private void setCallbackHandler(CallbackHandler callbackHandler) {
		this.callbackHandler = callbackHandler;
	}

	public List<String> getDefaultRoles() {
		return defaultRoles;
	}

	public void setDefaultRoles(List<String> defaultRoles) {
		this.defaultRoles = defaultRoles;
	}

	public Map<String, ?> getSharedState() {
		return sharedState;
	}

	public void setSharedState(Map<String, ?> sharedState) {
		this.sharedState = sharedState;
	}

	public Group getRolesGroup() {
		return rolesGroup;
	}

	public void setRolesGroup(Group rolesGroup) {
		this.rolesGroup = rolesGroup;
	}

	public Group getCallerPrincipal() {
		return callerPrincipal;
	}

	public void setCallerPrincipal(Group callerPrincipal) {
		this.callerPrincipal = callerPrincipal;
	}

	public Group getAuthPrincipal() {
		return authPrincipal;
	}

	public void setAuthPrincipal(Group authPrincipal) {
		this.authPrincipal = authPrincipal;
	}

	public Map<String, ?> getOptions() {
		return options;
	}

	public void setOptions(Map<String, ?> options) {
		this.options = options;
	}

	/**
	 * Checks if this login module should provide authorization information 
	 * after successful authentication is complete.
	 * 
	 * @return
	 * 		Returns true if role and group information should be populated and false otherwise.
	 */
	public boolean isAuthorizationEnabled() {
		return authorizationEnabled;
	}

	public void setAuthorizationEnabled(boolean authorizationEnabled) {
		this.authorizationEnabled = authorizationEnabled;
	}
}
