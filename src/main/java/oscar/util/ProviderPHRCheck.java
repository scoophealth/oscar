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


package oscar.util;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.oscarehr.managers.AppManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarProvider.data.ProviderMyOscarIdData;

/** Tag class for checking if provider and demographic have Indivo Ids
 * If they do, the jsp code will be included in the page.
 *
 */
public class ProviderPHRCheck extends TagSupport {

    protected String providerNo = null;
    
    private AppManager appManager = SpringUtils.getBean(AppManager.class);

    public String getProvider() {
        return (this.providerNo);
    }

    public void setProvider(String value) {
        this.providerNo = value;
    }


    public int doStartTag() throws JspException {

        boolean conditionMet = false ;
       
          try {     
	        	  if(appManager.hasAppDefinition(LoggedInInfo.getLoggedInInfoFromSession(this.pageContext.getSession()), "PHR")) {
	        		  if( !ProviderMyOscarIdData.idIsSet(providerNo) ) {
	        			  conditionMet = true;
	        		  }
	        	  }
          }catch(NullPointerException e) {
        	  	MiscUtils.getLogger().debug("INVALID provider");
            MiscUtils.getLogger().error("Error", e);
          }
        
        if (conditionMet)         
            return (EVAL_BODY_INCLUDE);
        else
            return (SKIP_BODY);        

    }

    public int doEndTag() throws JspException {

        return (EVAL_PAGE);

    }
   
    public void release() {

        providerNo = null;        
    }

}
