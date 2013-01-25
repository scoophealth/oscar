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


package oscar.oscarDemographic.tld;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDemographic.data.DemographicNameAgeString;

public class DemographicNameAgeTag extends TagSupport {

    public DemographicNameAgeTag()    {
    }

    public void setDemographicNo(String demoNo1)    {
       demoNo = demoNo1;
    }

    public String getDemographicNo()    {
        return demoNo;
    }

    public int doStartTag() throws JspException    {   
    	try {
    		if(demoNo == null || demoNo.equals(""))
    			return SKIP_BODY;
    		Integer.valueOf(demoNo);
    	} catch(NumberFormatException e) {
    		return SKIP_BODY;
    	}
    	
    	
       DemographicNameAgeString demoNameAge = DemographicNameAgeString.getInstance();       
       String nameage = demoNameAge.getNameAgeString(Integer.valueOf(demoNo));
            
       try{
          JspWriter out = super.pageContext.getOut();          
          out.print(nameage);                          
       } catch(Exception p) {MiscUtils.getLogger().error("Error",p);
       }
       return(SKIP_BODY);
    }

    public int doEndTag()        throws JspException    {
       return EVAL_PAGE;
    }

    private String demoNo;
}
