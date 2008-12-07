package org.oscarehr.PMmodule.web.admin;

import javax.servlet.http.HttpServletRequest;
import org.oscarehr.PMmodule.web.BaseAction;

import com.quatro.common.KeyConstants;
import com.quatro.model.security.NoAccessException;
import com.quatro.service.security.SecurityManager;

public class BaseAdminAction extends BaseAction {    
	protected String getAccess(HttpServletRequest request,String functionName) throws NoAccessException
	{
		SecurityManager sec = super.getSecurityManager(request);
		String acc = sec.GetAccess(functionName, "");
		if (acc.equals(KeyConstants.ACCESS_NONE)) throw new NoAccessException();
		return acc;
	}
	protected String getAccess(HttpServletRequest request,String functionName, String rights) throws NoAccessException
	{
		SecurityManager sec = super.getSecurityManager(request);
		String acc = sec.GetAccess(functionName, "");
		if (acc.compareTo(rights) < 0) throw new NoAccessException();
		return acc;
	}
	public boolean isReadOnly(HttpServletRequest request,String funName) throws NoAccessException{
		boolean readOnly =false;
		
		SecurityManager sec = getSecurityManager(request);
		String r = sec.GetAccess(funName, null); 
		if (r.compareTo(KeyConstants.ACCESS_READ) < 0) throw new NoAccessException(); 
		if (r.compareTo(KeyConstants.ACCESS_READ) == 0) readOnly=true;
		return readOnly;
	}

}
