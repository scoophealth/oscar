package org.oscarehr.provider.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ProviderRxInteractionWarningLevelAction extends DispatchAction {

	private static final Logger logger = MiscUtils.getLogger();

	private UserPropertyDAO propertyDao = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
	
    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String value = request.getParameter("value");
    	Provider provider = LoggedInInfo.loggedInInfo.get().loggedInProvider;
    	UserProperty prop = propertyDao.getProp(provider.getProviderNo(), "rxInteractionWarningLevel");
    	if(prop == null) {
    		prop = new UserProperty();
    		prop.setName("rxInteractionWarningLevel");
    		prop.setProviderNo(provider.getProviderNo());    		 	
    	}
    	prop.setValue(value);   
    	propertyDao.saveProp(prop);
    	
    	
        try {
        	response.getWriter().println("ok");
        }catch(IOException e) {
        	logger.error("error",e);
        }
        return null;
    }
}
