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


package oscar.oscarPrevention.tld;

import java.util.ArrayList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarPrevention.Prevention;
import oscar.oscarPrevention.PreventionDS;
import oscar.oscarPrevention.PreventionData;


/**
 *
 * @author Jay Gallagher
 */
public class PreventionTag extends TagSupport {


   public PreventionTag() {
	numWarnings = 0;
   }

   public int doStartTag() throws JspException    {
	Prevention p = PreventionData.getPrevention( LoggedInInfo.getLoggedInInfoFromSession(super.pageContext.getSession()), Integer.valueOf(demographicNo));
        try{
	PreventionDS pf = SpringUtils.getBean(PreventionDS.class);
	pf.getMessages(p);
        }catch(Exception e){
           MiscUtils.getLogger().error("Error", e);
        }
	ArrayList<String> warnings = p.getWarnings();

        StringBuilder sb = new StringBuilder();
        if (warnings != null){
           numWarnings = warnings.size();
           for (int i = 0; i < warnings.size(); i++){
              sb.append( warnings.get(i));
              sb.append("\n");
           }

        }
        String title = sb.toString();
        try{
            JspWriter out = super.pageContext.getOut();
            if(numWarnings > 0)
                out.print("<span style=\"color:red;\" title=\""+title+"\">  ");
            else
                out.print("<span>  ");
        } catch(Exception eWriter) {
        	MiscUtils.getLogger().error("Error", eWriter);
        }
        return(EVAL_BODY_INCLUDE);
    }


    public void setDemographicNo(String demographicNo)    {
       this.demographicNo = demographicNo;
    }

    public String getDemographicNo()    {
        return demographicNo;
    }



    public int doEndTag()        throws JspException    {
       try{
          JspWriter out = super.pageContext.getOut();
          out.print("</span>");
       }catch(Exception p) {MiscUtils.getLogger().error("Error",p);
       }
       return EVAL_PAGE;
    }

    private String demographicNo;
    private int numWarnings;
    private int numReminders;
}
