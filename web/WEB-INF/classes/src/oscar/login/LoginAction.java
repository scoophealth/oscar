/*
 * Copyright (c) 2005. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. This program is free
 * software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version. * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. * * You should have
 * received a copy of the GNU General Public License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * <OSCAR
 * TEAM> This software was written for the Department of Family Medicine McMaster University
 * Hamilton Ontario, Canada
 */
package oscar.login;

import java.io.IOException;
import java.util.List;
import java.util.Hashtable;
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
import org.oscarehr.PMmodule.dao.FacilityDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.common.IsPropertiesOn;
import org.oscarehr.util.SessionConstants;
import org.oscarehr.util.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarDB.DBHandler;
import oscar.oscarSecurity.CRHelper;
import oscar.util.AlertTimer;

public final class LoginAction extends DispatchAction {
    private static final Logger _logger = Logger.getLogger(LoginAction.class);
    private static final String LOG_PRE = "Login!@#$: ";

    private ProviderManager providerManager = (ProviderManager) SpringUtils.getBean("providerManager");
    private FacilityDao facilityDao = (FacilityDao) SpringUtils.getBean("facilityDao");
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String ip = request.getRemoteAddr();

        String nextPage=request.getParameter("nextPage");
        if (nextPage!=null) {
            // set current facility
            String facilityIdString=request.getParameter(SessionConstants.CURRENT_FACILITY_ID);
            Facility facility=facilityDao.getFacility(Integer.parseInt(facilityIdString));
            request.getSession().setAttribute(SessionConstants.CURRENT_FACILITY_ID, Integer.parseInt(facilityIdString));
            request.getSession().setAttribute(SessionConstants.CURRENT_FACILITY, facility);
            String username=(String)request.getSession().getAttribute("user");
            LogAction.addLog(username, LogConst.LOGIN, LogConst.CON_LOGIN, "facilityId="+facilityIdString, ip);
            return mapping.findForward(nextPage);
        }

        String where = "failure";
        // String userName, password, pin, propName;
        String userName = ((LoginForm) form).getUsername();
        String password = ((LoginForm) form).getPassword();
        String pin = ((LoginForm) form).getPin();
        String propName = request.getContextPath().substring(1) + ".properties";
        if (userName.equals("")) {
            return mapping.findForward(where);
        }

        LoginCheckLogin cl = new LoginCheckLogin(propName);
        if (!cl.propFileFound) {
            String newURL = mapping.findForward("error").getPath();
            newURL = newURL + "?errormsg=Unable to open the properties file " + cl.propFileName + ".";
            return(new ActionForward(newURL));
        }

        if (cl.isBlock(ip, userName)) {
            _logger.info(LOG_PRE + " Blocked: " + userName);
            // return mapping.findForward(where); //go to block page
            // change to block page
            String newURL = mapping.findForward("error").getPath();
            newURL = newURL + "?errormsg=Your account is locked. Please contact your administrator to unlock.";
            return(new ActionForward(newURL));
        }

        String[] strAuth;
        try {
            strAuth = cl.auth(userName, password, pin, ip);
        }
        catch (Exception e) {
            String newURL = mapping.findForward("error").getPath();
            if (e.getMessage() != null && e.getMessage().startsWith("java.lang.ClassNotFoundException")) {
                newURL = newURL + "?errormsg=Database driver " + e.getMessage().substring(e.getMessage().indexOf(':') + 2) + " not found.";
            }
            else {
                newURL = newURL + "?errormsg=Database connection error: " + e.getMessage() + ".";
            }
            return(new ActionForward(newURL));
        }

        if (strAuth != null && strAuth.length != 1) { // login successfully
            // invalidate the existing sesson
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
                session = request.getSession(); // Create a new session for this user
            }

            _logger.info("Assigned new session for: " + strAuth[0] + " : " + strAuth[3] + " : " + strAuth[4]);
            LogAction.addLog(strAuth[0], LogConst.LOGIN, LogConst.CON_LOGIN, "", ip);

            // initial db setting
            Properties pvar = cl.getOscarVariable();
            session.setAttribute("oscarVariables", pvar);
            if (!DBHandler.isInit()) {
                DBHandler.init(pvar.getProperty("db_name"), pvar.getProperty("db_driver"), pvar.getProperty("db_uri"), pvar.getProperty("db_username"), pvar.getProperty("db_password"));
            }

            // get View Type
            String viewType = LoginViewTypeHlp.getInstance().getProperty(strAuth[3].toLowerCase());
            String providerNo = strAuth[0];
            session.setAttribute("user", strAuth[0]);
            session.setAttribute("userfirstname", strAuth[1]);
            session.setAttribute("userlastname", strAuth[2]);
            session.setAttribute("userprofession", viewType);
            session.setAttribute("userrole", strAuth[4]);
            session.setAttribute("oscar_context_path", request.getContextPath());
            session.setAttribute("expired_days", strAuth[5]);
            
            // initiate security manager
            com.quatro.service.security.UserAccessManager userAccessManager = (com.quatro.service.security.UserAccessManager) getAppContext().getBean("userAccessManager");
            com.quatro.service.security.SecurityManager secManager = userAccessManager.getUserUserSecurityManager(providerNo);
            session.setAttribute("securitymanager", secManager);
            
            String default_pmm = null;
            if (viewType.equalsIgnoreCase("receptionist") || viewType.equalsIgnoreCase("doctor")) {
                // get preferences from preference table
                String[] strPreferAuth = cl.getPreferences();
                session.setAttribute("starthour", strPreferAuth[0]);
                session.setAttribute("endhour", strPreferAuth[1]);
                session.setAttribute("everymin", strPreferAuth[2]);
                session.setAttribute("groupno", strPreferAuth[3]);
                if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable()) {
                    session.setAttribute("newticklerwarningwindow", strPreferAuth[4]);
                    session.setAttribute("default_pmm", strPreferAuth[5]);
                    default_pmm = strPreferAuth[5];
                }
            }

            if (viewType.equalsIgnoreCase("receptionist")) { // go to receptionist view
                // where =
                // "receptionist";//receptionistcontrol.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday";
                where = "provider";
            }
            else if (viewType.equalsIgnoreCase("doctor")) { // go to provider view
                where = "provider"; // providercontrol.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday";
            }
            else if (viewType.equalsIgnoreCase("admin")) { // go to admin view
                where = "admin";
            }

            if (where.equals("provider") && default_pmm != null && "enabled".equals(default_pmm)) {
                where = "caisiPMM";
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
            session.setAttribute("provider", provider);

            List<Integer> facilityIds = ProviderDao.getFacilityIds(provider.getProvider_no());
            if (facilityIds.size() > 1) {
                return(new ActionForward("/select_facility.jsp?nextPage=" + where));
            }
            else if (facilityIds.size() == 1) {
                // set current facility
                Facility facility=facilityDao.getFacility(facilityIds.get(0));
                request.getSession().setAttribute("currentFacility", facility);
                request.getSession().setAttribute(SessionConstants.CURRENT_FACILITY_ID, facility.getId());
                LogAction.addLog(strAuth[0], LogConst.LOGIN, LogConst.CON_LOGIN, "facilityId="+facilityIds.get(0), ip);
            }
            else {
                // do nothing, not in any facilities
            	//CAISI:
            	//If the provider is not in any facilities, we should put him/her into the default facility which is 1st one in the db.
            	if(IsPropertiesOn.isCaisiEnable()) {
            		List facilities = facilityDao.getFacilities();
            		if(facilities!=null && facilities.size()>=1) {
            			Facility fac = (Facility)facilities.get(0);
            			int first_id = fac.getId();
            			ProviderDao.addProviderToFacility(providerNo, first_id);
            			Facility facility=facilityDao.getFacility(first_id);
            			request.getSession().setAttribute("currentFacility", facility);
            			request.getSession().setAttribute(SessionConstants.CURRENT_FACILITY_ID, first_id);
            			LogAction.addLog(strAuth[0], LogConst.LOGIN, LogConst.CON_LOGIN, "facilityId="+first_id, ip);
            		}
            	}
            }
        }
        // expired password
        else if (strAuth != null && strAuth.length == 1 && strAuth[0].equals("expired")) {
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
