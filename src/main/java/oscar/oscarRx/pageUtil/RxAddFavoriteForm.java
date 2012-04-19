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


public final class RxAddFavoriteForm extends ActionForm {
    
    private String drugId = null;
    private String stashId = null;
    private String favoriteName = null;
    private String returnParams = null;
    
    public String getDrugId() {
        return (this.drugId);
    }
    
    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }
    
    public String getStashId() {
        return (this.stashId);
    }
    
    public void setStashId(String stashId) {
        this.stashId = stashId;
    }
    
    public String getFavoriteName() {
        return (this.favoriteName);
    }
    
    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }
    
    public String getReturnParams() {
        if (this.returnParams == null){
            this.returnParams = "";
        }
        return (this.returnParams);
    }
    
    public void setReturnParams(String RHS) {
        this.returnParams = RHS;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.drugId = null;
        this.stashId = null;
        this.favoriteName = null;
        this.returnParams =  null;
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
