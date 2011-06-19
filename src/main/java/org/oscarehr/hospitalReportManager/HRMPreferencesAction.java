package org.oscarehr.hospitalReportManager;

//import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;



public class HRMPreferencesAction extends DispatchAction  {

	 @Override
	    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	     	
		 
		 	String userName = request.getParameter("userName");
	     	String location = request.getParameter("location");
	     	String privateKey = request.getParameter("privateKey");
	     	String decryptionKey = request.getParameter("decryptionKey");
	     	HRMPreferences hrmprefs = new HRMPreferences();
	     	
	     	
	     	try{
	     		//hrmprefs.setUserName(userName);
	     		hrmprefs.setLocation(location);
	     		hrmprefs.setPrivateKey(privateKey);
	     		hrmprefs.setDecryptionKey(decryptionKey);
	     		
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
