package org.oscarehr.hospitalReportManager;

//import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;



public class HRMPreferencesAction extends DispatchAction  {

	 @Override
	    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	     	
		 
		 	String userName = request.getParameter("userName");
	     	String location = request.getParameter("location");
	     	String privateKey = request.getParameter("privateKey");
	     	String decryptionKey = request.getParameter("decryptionKey");
	     	String interval = request.getParameter("interval");
	     	
	     	
	     	UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
	     	
	     	try{
	     		UserProperty prop = new UserProperty();
	     		prop.setName("hrm_username");
	     		prop.setValue(userName);
	     		userPropertyDao.saveProp(prop);
	     		
	     		
	     		prop.setName("hrm_location");
	     		prop.setValue(location);
	     		userPropertyDao.saveProp(prop);
	     		
	     		prop.setName("hrm_privateKey");
	     		prop.setValue(privateKey);
	     		userPropertyDao.saveProp(prop);
	     		
	     		prop.setName("hrm_decryptionKey");
	     		prop.setValue(decryptionKey);
	     		userPropertyDao.saveProp(prop);
	     		
	     		prop.setName("hrm_interval");
	     		prop.setValue(interval);
	     		
	     		
	     		SFTPConnector.setDownloadsDirectory(location);
	     		SFTPConnector.setOMD_keyLocation(privateKey);
	     		SFTPConnector.setDecryptionKey(decryptionKey);
	     		
	     		
	     		
	     		request.setAttribute("success", true);
	     	} catch (Exception e){
	     		MiscUtils.getLogger().error("Changing Preferences failed", e); 
				request.setAttribute("success", false);
	     	}
		 
	     	return mapping.findForward("success");
		 
	    }
	 
	 
}
