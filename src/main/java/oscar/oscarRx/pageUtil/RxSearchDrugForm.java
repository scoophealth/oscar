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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;


public final class RxSearchDrugForm extends ActionForm {
    
    
    private String demographicNo = null;
    private String searchString = null;
    private String searchRoute = null;
    private String genericString = null;
    private String otcExcluded = null;
    private String ahfsString = null;
    
            
    
    public String getAction(){
        MiscUtils.getLogger().debug("Can i be deleted GETTER getAction RxSearchDrugForm");
        return "";
    }
    public void getAction(String d){
        MiscUtils.getLogger().debug("Can i be deleted SETTER getAction RxSearchDrugForm");
    }
    
    public String getAhfsSearch(){
        return ahfsString;
    }
    
    public void setAhfsSearch(String str){
        ahfsString = str;
    }
    
    public String getOtcExcluded(){
        if (otcExcluded == null){
            otcExcluded = "0";
        }
        return otcExcluded;
    }
    
    public void setOtcExcluded(String str){
        otcExcluded = str;
    }
    
    
    public String getDemographicNo() {
        return (this.demographicNo);
    }
    
    public void setDemographicNo(String demographicNo) {
        this.demographicNo = demographicNo;
    }
    
    public String getSearchString() {
        return (this.searchString);
    }
    
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
    
    public String getSearchRoute() {
        return (this.searchRoute);
    }
    
    public void setSearchRoute(String searchRoute) {
        this.searchRoute = searchRoute;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.demographicNo = null;
        this.searchString=null;
    }
    
    
    public String getGenericSearch(){
        return genericString;
    }
    
    public void setGenericSearch(String str){
        genericString = str;
    }
    
    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
    HttpServletRequest request) {
        
        ActionErrors errors = new ActionErrors();
        
        return errors;
        
    }
}
