// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.pageUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.util.UtilDateUtilities;

//import oscar.oscarSecurity.CookieSecurity;

public class EctIncomingEncounterAction extends Action {
    
  private static Logger log = MiscUtils.getLogger();
    
  public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response) throws IOException, ServletException {
				  
        UtilDateUtilities dateConvert = new UtilDateUtilities();
        oscar.oscarSecurity.CookieSecurity cs   = new oscar.oscarSecurity.CookieSecurity();
        EctSessionBean bean = new EctSessionBean();
        if(cs.FindThisCookie(request.getCookies(),cs.providerCookie)){ //pass security???
            if(request.getParameter("appointmentList")!=null){
                    bean = (EctSessionBean) request.getSession().getAttribute("EctSessionBean") ;
                    bean.setUpEncounterPage(request.getParameter("appointmentNo"));
                    bean.template = "";                    
            } else if(request.getParameter("demographicSearch")!=null){
            //Coming in from the demographicSearch page
                    bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean") ;
                    //demographicNo is passed from search screen
                    bean.demographicNo=request.getParameter("demographicNo");
                    //no curProviderNo when viewing eCharts from search screen
                    //bean.curProviderNo="";
                    //no reason when viewing eChart from search screen
                    bean.reason="";
                    //userName is already set
                    //bean.userName=request.getParameter("userName");
                    //no appointmentDate from search screen keep old date
                    //bean.appointmentDate="";
                    //no startTime from search screen
                    bean.startTime="";
                    //no status from search screen
                    bean.status="";
                    //no date from search screen-keep old date
                    //bean.date="";
                    bean.check= "myCheck";                    
                    bean.setUpEncounterPage();                    
                    request.getSession().setAttribute("EctSessionBean",bean);                    
            } else {
                bean = new EctSessionBean();
                bean.currentDate = UtilDateUtilities.StringToDate(request.getParameter("curDate"));
                
                if (bean.currentDate == null){
                    bean.currentDate = UtilDateUtilities.Today();
                }
                
                bean.providerNo=request.getParameter("providerNo");
                if(bean.providerNo == null){
                    bean.providerNo = (String) request.getSession().getAttribute("user");
                }
                bean.demographicNo=request.getParameter("demographicNo");
                bean.appointmentNo=request.getParameter("appointmentNo");
                bean.curProviderNo=request.getParameter("curProviderNo");
                bean.reason=request.getParameter("reason");
                bean.encType=request.getParameter("encType");
                bean.userName=request.getParameter("userName");
                if (bean.userName == null){
                     bean.userName =  ( (String) request.getSession().getAttribute("userfirstname") ) + " " + ( (String) request.getSession().getAttribute("userlastname") );
                }
                
                bean.appointmentDate=request.getParameter("appointmentDate");
                bean.startTime=request.getParameter("startTime");
                bean.status=request.getParameter("status");
                bean.date=request.getParameter("date");
                bean.check= "myCheck";
                bean.oscarMsgID = request.getParameter("msgId");
                bean.setUpEncounterPage();               
                request.getSession().setAttribute("EctSessionBean",bean);
                request.getSession().setAttribute("eChartID", bean.eChartId);
                if(request.getParameter("source")!=null) {
                	bean.source = request.getParameter("source");
                }
            }
        }
        else{
            return (mapping.findForward("failure"));
        }
        
        ArrayList newDocArr = (ArrayList) request.getSession().getServletContext().getAttribute("newDocArr");
        Boolean useNewEchart = (Boolean) request.getSession().getServletContext().getAttribute("useNewEchart");
        
        String proNo = (String) request.getSession().getAttribute("user");
        if (proNo != null  && newDocArr != null && Collections.binarySearch(newDocArr,proNo) >= 0){
            return (mapping.findForward("success2"));
        }else if( useNewEchart != null && useNewEchart.equals(Boolean.TRUE) ){
            return (mapping.findForward("success2"));
        }
        else {
           return (mapping.findForward("success"));
        }
        
  }
}
