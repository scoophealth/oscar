/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarBilling.ca.on.pageUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
* @author Eugene Katyukhin
*/
public final class PatientEndYearStatementForm extends ActionForm {
    private static Logger logger = Logger.getLogger(PatientEndYearStatementForm.class);
	private String firstNameParam;
    private String lastNameParam;
    private String fromDateParam;
    private String toDateParam;
    private String demographicNoParam;
    
//    private Date fromDate;
//    private Date toDate;
    
    public PatientEndYearStatementForm() {}

	public String getDemographicNoParam() {
		return demographicNoParam;
	}
	public void setDemographicNoParam(String demographicNoParam) {
		this.demographicNoParam = demographicNoParam;
	}
	public String getFirstNameParam() {
		return firstNameParam;
	}
	public void setFirstNameParam(String name) {
		this.firstNameParam = name;
	}
	public String getLastNameParam() {
		return lastNameParam;
	}
	public void setLastNameParam(String name) {
		this.lastNameParam = name;
	}
	public String getFromDateParam() {
		return fromDateParam;
	}
	public void setFromDateParam(String fromDate) {
		this.fromDateParam = fromDate;
	}
    public Date getFromDate() {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	Date res = null;
 		   try {
	     		if(fromDateParam != null && fromDateParam.length()>0) res = df.parse(fromDateParam);    		
 		   } catch(ParseException ex) {
      	 	   logger.error("Can't parse date: "+fromDateParam);	
 			   return null;        	
		   }
    	return res;
	}
	public String getToDateParam() {
		return toDateParam;
	}
	public void setToDateParam(String toDate) {
		this.toDateParam = toDate;
	}
	public Date getToDate() {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	Date res = null;
 		   try {
	     		if(toDateParam != null && toDateParam.length()>0) res = df.parse(toDateParam);    		
 		   } catch(ParseException ex) {
      	 	   logger.error("Can't parse date: "+toDateParam);	
 			   return null;        	
		   }
    	return res;
	}

	public ActionErrors validate (ActionMapping mapping , HttpServletRequest request){
        ActionErrors errors = new ActionErrors();
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
 	   if(request.getParameter("search") != null) {
 		   if ((this.firstNameParam == null || this.firstNameParam.length()==0) &&
        		(this.lastNameParam == null || this.lastNameParam.length()==0)) {
        	 errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.billingReport.noPatient"));
 		   }
 		   try {
 			   if(fromDateParam != null && fromDateParam.length()>0) df.parse(fromDateParam);
 			   if(toDateParam != null  && toDateParam.length()>0) df.parse(toDateParam);
 		   } catch(ParseException ex) {
       	 		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.billingReport.invalidDateFormat"));        	
 		   }
 	    }   
        return errors;
    }
    
}
