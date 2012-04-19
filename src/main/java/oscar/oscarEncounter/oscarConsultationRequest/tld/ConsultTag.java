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


package oscar.oscarEncounter.oscarConsultationRequest.tld;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.oscarehr.common.dao.ConsultationRequestDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 *
 * @author Jay Gallagher
 */
public class ConsultTag extends TagSupport {


   public ConsultTag() {
	numNewLabs = 0;
   }

   public int doStartTag() throws JspException    {
            numNewLabs = 0;
	    if(providerNo!=null){
                try{
	       ConsultationRequestDao tcm = (ConsultationRequestDao) WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext()).getBean("consultationRequestDao");
               UserPropertyDAO pref = (UserPropertyDAO) WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext()).getBean("UserPropertyDAO");
               
               // There are to two properties that could be set. One is the referal date and the other is the team
               // Date set /  team set      == search on  both
               // Date not set /  team set  == default to one month
               // Date set /  team not set  == search on the whole clinic
               // Date not set /  team not set  == do nothing
               // BUT If the team is -1 it should search on all teams.
               
               UserProperty up = pref.getProp(providerNo, UserProperty.CONSULTATION_TIME_PERIOD_WARNING);
               UserProperty up2 = pref.getProp(providerNo, UserProperty.CONSULTATION_TEAM_WARNING);
               
               String timeperiod = null;
               String team = null;
               
               if ( up != null && up.getValue() != null && !up.getValue().trim().equals("")){
                  timeperiod = up.getValue(); 
               }
               
               if ( up2 != null && up2.getValue() != null && !up2.getValue().trim().equals("")){
                  team = up2.getValue(); 
               }
                
               boolean allTeams = false;
               if (team != null && team.equals("-1")){
                   team = null;
                   allTeams = true;
               }
               
               Calendar cal = Calendar.getInstance();
               int countback = -1;
               if (timeperiod != null){
                       countback = Integer.parseInt(timeperiod);
                       countback = countback * -1;
                       cal.add(Calendar.MONTH,countback );
                        
               }
               cal.add(Calendar.MONTH,countback );
               Date cutoffDate = cal.getTime();
               
               
               if (allTeams){
                   numNewLabs= tcm.getCountReferralsAfterCutOffDateAndNotCompleted(cutoffDate);
               }else if (team != null){    
                   numNewLabs= tcm.getCountReferralsAfterCutOffDateAndNotCompleted(cutoffDate, team);
               }

               
                }catch(Exception ee){
                    MiscUtils.getLogger().error("Error", ee);
                }
	    }  
	   
        try        {
            JspWriter out = super.pageContext.getOut();
            if(numNewLabs > 0) 
                out.print("<span class='tabalert'>  ");
            else
                out.print("<span>  ");
        } catch(Exception p) {MiscUtils.getLogger().error("Error",p);
        }
        return(EVAL_BODY_INCLUDE);
    }


    public void setProviderNo(String providerNo1)    {
       providerNo = providerNo1;
    }

    public String getProviderNo()    {
        return providerNo;
    }


    public int doEndTag()        throws JspException    {
       try{
          JspWriter out = super.pageContext.getOut();
          if (numNewLabs>0)
              out.print("<sup>"+numNewLabs+"</sup></span>");
          else
              out.print("</span>");
       }catch(Exception p) {MiscUtils.getLogger().error("Error",p);
       }
       return EVAL_PAGE;
    }

    private String providerNo;
    private int numNewLabs;
}
