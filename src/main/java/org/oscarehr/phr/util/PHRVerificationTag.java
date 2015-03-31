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


package org.oscarehr.phr.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarProvider.data.ProviderMyOscarIdData;


public class PHRVerificationTag extends TagSupport {

	
    public PHRVerificationTag()    {
        
    }

    public void setDemographicNo(String demoNo1)    {
       demoNo = demoNo1;
    }

    public String getDemographicNo()    {
        return demoNo;
    }
    
    public void setStyleId(String styleId){
    	this.styleId = styleId;
    }
    
    public String getStyleId(){
    	return styleId;
    }
    
    public int doStartTag() throws JspException    {        
       try{
    	   HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    	   conditionMet = false ;
                     
               if( ProviderMyOscarIdData.idIsSet((String)request.getSession().getAttribute("user")) ) {
                   if( demoNo != null ) {
                      org.oscarehr.common.model.Demographic demo = new DemographicData().getDemographic(LoggedInInfo.getLoggedInInfoFromSession(pageContext.getSession()),demoNo); 
                      String myOscarUserName = demo.getMyOscarUserName();
                      if( myOscarUserName != null && !myOscarUserName.equals("") ) 
                           conditionMet = true;
                   }                                                    
               }                                        
         
    	 
    	   if(!conditionMet){
    		   return (SKIP_BODY);    
    	   }
    	   
    	   JspWriter out = super.pageContext.getOut();  
    	   String contextPath = request.getContextPath();
          
    	   if(styleId != null){
        	  styleId= " id=\""+styleId+"\" ";
    	   }
          
    	   out.print("<a "+styleId+" href=\"javascript: void(0);\" onclick=\"popup2(500, 600, 20, 30,'"+contextPath+"/phr/PHRVerification.jsp?demographic_no="+demoNo+"','myoscarVerification');\" >");                          
          
       } catch(Exception p) {
    	   MiscUtils.getLogger().error("Error",p);
       }
       return(EVAL_BODY_INCLUDE);
    }

    public int doEndTag()        throws JspException    {
    	if(conditionMet){
    		try{
    			JspWriter out = super.pageContext.getOut();   
    			HttpServletRequest request=(HttpServletRequest)pageContext.getRequest();
    			LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    			
    			DemographicManager demographicManager = (DemographicManager)SpringUtils.getBean("demographicManager"); 
    			out.print("<sup>"+demographicManager.getPhrVerificationLevelByDemographicId(loggedInInfo,Integer.parseInt(demoNo))+"</sup></a>");
    		}catch(Exception p) {
    			MiscUtils.getLogger().error("Error",p);
    		}
    	 }
        return EVAL_PAGE;
     } 

    private String demoNo;
    private String styleId = null;
    private boolean conditionMet;
}
