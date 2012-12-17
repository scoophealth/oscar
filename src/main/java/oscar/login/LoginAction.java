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


package oscar.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.web.OcanForm;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.dao.ProviderPreferenceDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderPreference;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.decisionSupport.service.DSService;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.EncryptionUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.LoggedInUserFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SessionConstants;
import org.oscarehr.util.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarSecurity.CRHelper;
import oscar.util.AlertTimer;

public final class LoginAction extends DispatchAction {
	
	/**
	 * This variable is only intended to be used by this class and the jsp which sets the selected facility.
	 * This variable represents the queryString key used to pass the facility ID to this class.
	 */
    public static final String SELECTED_FACILITY_ID="selectedFacilityId";

    private static final Logger logger = MiscUtils.getLogger();
    private static final String LOG_PRE = "Login!@#$: ";

    private ProviderManager providerManager = (ProviderManager) SpringUtils.getBean("providerManager");
    private FacilityDao facilityDao = (FacilityDao) SpringUtils.getBean("facilityDao");
    private ProviderPreferenceDao providerPreferenceDao = (ProviderPreferenceDao) SpringUtils.getBean("providerPreferenceDao");
    private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        Boolean isMobileOptimized = request.getSession().getAttribute("mobileOptimized") != null;
        String nextPage=request.getParameter("nextPage");
        logger.debug("nextPage: "+nextPage);
        if (nextPage!=null) {
            // set current facility
            String facilityIdString=request.getParameter(SELECTED_FACILITY_ID);
            Facility facility=facilityDao.find(Integer.parseInt(facilityIdString));
            request.getSession().setAttribute(SessionConstants.CURRENT_FACILITY, facility);
            String username=(String)request.getSession().getAttribute("user");
            LogAction.addLog(username, LogConst.LOGIN, LogConst.CON_LOGIN, "facilityId="+facilityIdString, ip);
            if(facility.isEnableOcanForms()) {
            	request.getSession().setAttribute("ocanWarningWindow", OcanForm.getOcanWarningMessage(facility.getId()));
            }
            return mapping.findForward(nextPage);
        }

        String where = "failure";
        // String userName, password, pin, propName;
        String userName = ((LoginForm) form).getUsername();
        String password = ((LoginForm) form).getPassword();
        String pin = ((LoginForm) form).getPin();

        LoginCheckLogin cl = new LoginCheckLogin();
        if (cl.isBlock(ip, userName)) {
            logger.info(LOG_PRE + " Blocked: " + userName);
            // return mapping.findForward(where); //go to block page
            // change to block page
            String newURL = mapping.findForward("error").getPath();
            newURL = newURL + "?errormsg=Your account is locked. Please contact your administrator to unlock.";
            return(new ActionForward(newURL));
        }
        logger.debug("ip was not blocked: "+ip);
        String[] strAuth;
        try {
            strAuth = cl.auth(userName, password, pin, ip);
        }
        catch (Exception e) {
        	logger.error("Error", e);
            String newURL = mapping.findForward("error").getPath();
            if (e.getMessage() != null && e.getMessage().startsWith("java.lang.ClassNotFoundException")) {
                newURL = newURL + "?errormsg=Database driver " + e.getMessage().substring(e.getMessage().indexOf(':') + 2) + " not found.";
            }
            else {
                newURL = newURL + "?errormsg=Database connection error: " + e.getMessage() + ".";
            }
            return(new ActionForward(newURL));
        }
        
        logger.debug("strAuth : "+strAuth);
        if (strAuth != null && strAuth.length != 1) { // login successfully
            // invalidate the existing sesson
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            session = request.getSession(); // Create a new session for this user
            LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
            loggedInInfo.session=session;

            logger.debug("Assigned new session for: " + strAuth[0] + " : " + strAuth[3] + " : " + strAuth[4]);
            LogAction.addLog(strAuth[0], LogConst.LOGIN, LogConst.CON_LOGIN, "", ip);

            // initial db setting
            Properties pvar = OscarProperties.getInstance();
            EncryptionUtils.setDeterministicallyMangledPasswordSecretKeyIntoSession(session, password);
            

            String providerNo = strAuth[0];
            session.setAttribute("user", strAuth[0]);
            session.setAttribute("userfirstname", strAuth[1]);
            session.setAttribute("userlastname", strAuth[2]);
            session.setAttribute("userrole", strAuth[4]);
            session.setAttribute("oscar_context_path", request.getContextPath());
            session.setAttribute("expired_days", strAuth[5]);
            // If a new session has been created, we must set the mobile attribute again
            if (isMobileOptimized) session.setAttribute("mobileOptimized","true");
            // initiate security manager
            String default_pmm = null;
            
            // get preferences from preference table
        	ProviderPreference providerPreference=providerPreferenceDao.find(providerNo);
        	
            
                
        	if (providerPreference==null) providerPreference=new ProviderPreference();
         	
        	session.setAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE, providerPreference);
        	
            if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable()) {
                session.setAttribute("newticklerwarningwindow", providerPreference.getNewTicklerWarningWindow());
                session.setAttribute("default_pmm", providerPreference.getDefaultCaisiPmm());
                session.setAttribute("caisiBillingPreferenceNotDelete", String.valueOf(providerPreference.getDefaultDoNotDeleteBilling()));
                
                default_pmm = providerPreference.getDefaultCaisiPmm();
                @SuppressWarnings("unchecked")
                ArrayList<String> newDocArr = (ArrayList<String>)request.getSession().getServletContext().getAttribute("CaseMgmtUsers");    
                if("enabled".equals(providerPreference.getDefaultNewOscarCme())) {
                	newDocArr.add(providerNo);
                	session.setAttribute("CaseMgmtUsers", newDocArr);
                }
            }
            session.setAttribute("starthour", providerPreference.getStartHour().toString());
            session.setAttribute("endhour", providerPreference.getEndHour().toString());
            session.setAttribute("everymin", providerPreference.getEveryMin().toString());
            session.setAttribute("groupno", providerPreference.getMyGroupNo());
                
            where = "provider";

            if (where.equals("provider") && default_pmm != null && "enabled".equals(default_pmm)) {
                where = "caisiPMM";
            }
            
            if (where.equals("provider")) {
                WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
                UserPropertyDAO  propDAO =  (UserPropertyDAO) ctx.getBean("UserPropertyDAO");
                UserProperty drugrefProperty = propDAO.getProp(UserProperty.MYDRUGREF_ID);
                if (drugrefProperty != null) {
                   
                    DSService service =  (DSService) ctx.getBean("dsService");
                    service.fetchGuidelinesFromServiceInBackground(providerNo);
                }
            }


            String quatroShelter = OscarProperties.getInstance().getProperty("QUATRO_SHELTER");
            if(quatroShelter!= null && quatroShelter.equals("on")) {
            	where = "shelterSelection";
            }
        
            /*
             * if (OscarProperties.getInstance().isTorontoRFQ()) { where = "caisiPMM"; }
             */
            // Lazy Loads AlertTimer instance only once, will run as daemon for duration of server runtime
            if (pvar.getProperty("billregion").equals("BC")) {
                String alertFreq = pvar.getProperty("ALERT_POLL_FREQUENCY");
                if (alertFreq != null) {
                    Long longFreq = new Long(alertFreq);
                    String[] alertCodes = OscarProperties.getInstance().getProperty("CDM_ALERTS").split(",");
                    AlertTimer.getInstance(alertCodes, longFreq.longValue());
                }
            }
            CRHelper.recordLoginSuccess(userName, strAuth[0], request);

            // setup caisi stuff
            String username = (String) session.getAttribute("user");
            Provider provider = providerManager.getProvider(username);
            session.setAttribute(SessionConstants.LOGGED_IN_PROVIDER, provider);
            session.setAttribute(SessionConstants.LOGGED_IN_SECURITY, cl.getSecurity());

	    loggedInInfo = LoggedInUserFilter.generateLoggedInInfoFromSession(request);
	    MyOscarUtils.attemptMyOscarAutoLoginIfNotAlreadyLoggedIn(loggedInInfo);
            
            List<Integer> facilityIds = providerDao.getFacilityIds(provider.getProviderNo());
            if (facilityIds.size() > 1) {
                return(new ActionForward("/select_facility.jsp?nextPage=" + where));
            }
            else if (facilityIds.size() == 1) {
                // set current facility
                Facility facility=facilityDao.find(facilityIds.get(0));
                request.getSession().setAttribute("currentFacility", facility);
                LogAction.addLog(strAuth[0], LogConst.LOGIN, LogConst.CON_LOGIN, "facilityId="+facilityIds.get(0), ip);
                if(facility.isEnableOcanForms()) {
                	request.getSession().setAttribute("ocanWarningWindow", OcanForm.getOcanWarningMessage(facility.getId()));
                }
            }
            else {
        		List<Facility> facilities = facilityDao.findAll(true);
        		if(facilities!=null && facilities.size()>=1) {
        			Facility fac = facilities.get(0);
        			int first_id = fac.getId();
        			ProviderDao.addProviderToFacility(providerNo, first_id);
        			Facility facility=facilityDao.find(first_id);
        			request.getSession().setAttribute("currentFacility", facility);
        			LogAction.addLog(strAuth[0], LogConst.LOGIN, LogConst.CON_LOGIN, "facilityId="+first_id, ip);
            	}
            }

            if( pvar.getProperty("LOGINTEST","").equalsIgnoreCase("yes")) {
                String proceedURL = mapping.findForward(where).getPath();
                request.getSession().setAttribute("proceedURL", proceedURL);               
                return mapping.findForward("LoginTest");
            }
        }
        // expired password
        else if (strAuth != null && strAuth.length == 1 && strAuth[0].equals("expired")) {
        	logger.warn("Expired password");
            cl.updateLoginList(ip, userName);
            String newURL = mapping.findForward("error").getPath();
            newURL = newURL + "?errormsg=Your account is expired. Please contact your administrator.";
            return(new ActionForward(newURL));
        }
        else { // go to normal directory
            // request.setAttribute("login", "failed");
            // LogAction.addLog(userName, "failed", LogConst.CON_LOGIN, "", ip);
            cl.updateLoginList(ip, userName);
            CRHelper.recordLoginFailure(userName, request);
            return mapping.findForward(where);
        }

        return mapping.findForward(where);
    }
    
	public ApplicationContext getAppContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServlet().getServletContext());
	}
}
