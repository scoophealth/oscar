package oscar.oscarSecurity;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

public abstract class SecurityTokenManager {
	
	static SecurityTokenManager instance = null;
	
	public static SecurityTokenManager getInstance() {
		if(instance != null) {
			return instance;
		}
		
		String managerName = OscarProperties.getInstance().getProperty("security.token.manager");
		if(managerName != null) {
			try {
				instance = (SecurityTokenManager)Class.forName(managerName).newInstance();
			}catch(Exception e) {
				MiscUtils.getLogger().error("Unable to load token manager");
			}
		}
		
		return instance;
	}
	
	/**
	 * Set the "token" attribute in the request if successful.
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	public abstract void requestToken(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;
	
	/**
	 * Check token, do the login if successful.
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	public abstract boolean handleToken(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;
		
}
