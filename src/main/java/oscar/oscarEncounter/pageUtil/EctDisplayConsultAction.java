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


package oscar.oscarEncounter.pageUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.util.DateUtils;
import oscar.util.StringUtils;


/**
 *
 * Retrieves consultation requests for demographic
 */
public class EctDisplayConsultAction extends EctDisplayAction {
    private String cmd = "consultation";
 
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
    	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

    	String appointmentNo = bean.appointmentNo;
    	
       if(!securityInfoManager.hasPrivilege(loggedInInfo, "_con", "r", null)) {
            return true; //Consultations link won't show up on new CME screen.
       } else {
            //set lefthand module heading and link
            String winName = "Consultation" + bean.demographicNo;
            String url = "popupPage(700,960,'" + winName + "','" + request.getContextPath() + "/oscarEncounter/oscarConsultationRequest/DisplayDemographicConsultationRequests.jsp?de=" + bean.demographicNo + "&appNo=" + appointmentNo + "')";            
            Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.LeftNavBar.Consult"));
            Dao.setLeftURL(url);
            
            //set the right hand heading link\
            winName = "newConsult" + bean.demographicNo;
            url = "popupPage(700,960,'" + winName + "','" + request.getContextPath() + "/oscarEncounter/oscarConsultationRequest/ConsultationFormRequest.jsp?de=" + bean.demographicNo + "&teamVar=&appNo="+appointmentNo+"'); return false;";
            Dao.setRightURL(url);        
            Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action 
            
            //grab all consultations for patient and add list item for each
            oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctViewConsultationRequestsUtil theRequests;
            theRequests = new  oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctViewConsultationRequestsUtil();
            theRequests.estConsultationVecByDemographic(loggedInInfo, bean.demographicNo);
            
            //determine cut off period for highlighting
            UserPropertyDAO pref = (UserPropertyDAO) WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("UserPropertyDAO");     

            UserProperty up = pref.getProp(bean.providerNo, UserProperty.CONSULTATION_TIME_PERIOD_WARNING);           
            String timeperiod = null;           

            if ( up != null && up.getValue() != null && !up.getValue().trim().equals("")){
              timeperiod = up.getValue(); 
            }

            Calendar cal = Calendar.getInstance();
            int countback = -1;
            if (timeperiod != null){
                   countback = Integer.parseInt(timeperiod);
                   countback = countback * -1;
            }
            cal.add(Calendar.MONTH,countback );
            Date cutoffDate = cal.getTime();
            
            String red = "red";
            String dbFormat = "yyyy-MM-dd";
            String serviceDateStr;
            Date date;
            for (int idx = theRequests.ids.size() - 1; idx >= 0; --idx ){
                NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
                String service =  theRequests.service.get(idx);
                String dateStr    =  theRequests.date.get(idx);
                String status = theRequests.status.get(idx);
                DateFormat formatter = new SimpleDateFormat(dbFormat);
                try {
                    date = formatter.parse(dateStr);
                    serviceDateStr = DateUtils.formatDate(date, request.getLocale());
                    //if we are after cut off date and not completed set to red
                    if( date.before(cutoffDate) && !status.equals("4") ) {
                        item.setColour(red);
                    }
                }
                catch(ParseException ex ) {
                    MiscUtils.getLogger().debug("EctDisplayConsultationAction: Error creating date " + ex.getMessage());
                    serviceDateStr = "Error";
                    date = null;
                }
                url = "popupPage(700,960,'" + winName + "','" + request.getContextPath() + "/oscarEncounter/ViewRequest.do?de=" + bean.demographicNo + "&requestId=" + theRequests.ids.get(idx) + "&appNo=" + appointmentNo + "'); return false;";
                
                item.setLinkTitle(service + " " + serviceDateStr);
                service = StringUtils.maxLenString(service, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
                item.setTitle(service);
                item.setURL(url);
                item.setDate(date);
                Dao.addItem(item);
            } 
            
            //Display consultation requests order by by date
			Dao.setInternalDateSort(false);
			Dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);
            
            return true;
         }
    }    
    public String getCmd() {
         return cmd;
    }
}
