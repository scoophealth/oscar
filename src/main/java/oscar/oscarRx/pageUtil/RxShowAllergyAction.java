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


package oscar.oscarRx.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarRx.data.RxPatientData;

public final class RxShowAllergyAction extends DispatchAction {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	private AllergyDao allergyDao = (AllergyDao) SpringUtils.getBean("allergyDao");



	public ActionForward reorder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		reorder(request);
		ActionForward fwd = mapping.findForward("success-redirect");
		return new ActionForward(fwd.getPath()+ "?demographicNo="+request.getParameter("demographicNo"),true );
	}

    public ActionForward unspecified(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {

    	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_allergy", "r", null)) {
			throw new RuntimeException("missing required security object (_allergy)");
		}
		
        boolean useRx3=false;
        String rx3 = OscarProperties.getInstance().getProperty("RX3");
        if(rx3!=null&&rx3.equalsIgnoreCase("yes")) {
        	useRx3=true;
        }
        UserPropertyDAO userPropertyDAO = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
        String provider = (String) request.getSession().getAttribute("user");
        UserProperty propUseRx3 = userPropertyDAO.getProp(provider, UserProperty.RX_USE_RX3);
        if(propUseRx3!=null && propUseRx3.getValue().equalsIgnoreCase("yes"))
                useRx3=true;


        String user_no = (String) request.getSession().getAttribute("user");
        String demo_no = request.getParameter("demographicNo");
        String view = request.getParameter("view");

        if(demo_no == null){
           return mapping.findForward("failure");
        }
        // Setup bean
        RxSessionBean bean;

        if(request.getSession().getAttribute("RxSessionBean")!=null) {
            bean = (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");
            if( (bean.getProviderNo() != user_no) || (bean.getDemographicNo() != Integer.parseInt(demo_no) ) ) {
                bean = new RxSessionBean();
            }

        } else {
            bean = new RxSessionBean();
        }


        bean.setProviderNo(user_no);
        bean.setDemographicNo(Integer.parseInt(demo_no));
        if(view != null) {
        	bean.setView(view);
    	}

        request.getSession().setAttribute("RxSessionBean", bean);

        if(request.getParameter("method")!=null && request.getParameter("method").equals("reorder")) {
        	reorder(request);
        }

        RxPatientData.Patient patient = RxPatientData.getPatient(loggedInInfo, bean.getDemographicNo());

        String forward="success";
        if(useRx3) {
        	forward="successRX3";
        }
        if(patient!=null) {
            request.getSession().setAttribute("Patient", patient);
            return (mapping.findForward(forward));
        } else {//no records found
            response.sendRedirect("error.html");
            return null;
        }
    }

    private void reorder(HttpServletRequest request) {
    	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
    	
    	String direction = request.getParameter("direction");
    	String demographicNo = request.getParameter("demographicNo");
    	int allergyId = Integer.parseInt(request.getParameter("allergyId"));
    	try {
    		Allergy[] allergies = RxPatientData.getPatient(loggedInInfo, demographicNo).getActiveAllergies();
    		for(int x=0;x<allergies.length;x++) {
    			if(allergies[x].getAllergyId() == allergyId) {
    				if(direction.equals("up")) {
    					if(x==0) { continue;}
    					//move ahead
    					int myPosition = allergies[x].getPosition();
    					int swapPosition = allergies[x-1].getPosition();
    					allergies[x].setPosition(swapPosition);
    					allergies[x-1].setPosition(myPosition);
    					allergyDao.merge(allergies[x]);
    					allergyDao.merge(allergies[x-1]);
    				}
    				if(direction.equals("down")) {
    					if(x==(allergies.length-1)) { continue;}
    					int myPosition = allergies[x].getPosition();
    					int swapPosition = allergies[x+1].getPosition();
    					allergies[x].setPosition(swapPosition);
    					allergies[x+1].setPosition(myPosition);
    					allergyDao.merge(allergies[x]);
    					allergyDao.merge(allergies[x+1]);
    				}
    			}
    		}

    	}catch(Exception e) {
    		MiscUtils.getLogger().error("error",e);
    	}

    }
}
