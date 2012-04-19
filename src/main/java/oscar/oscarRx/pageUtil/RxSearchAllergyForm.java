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


public final class RxSearchAllergyForm extends ActionForm {
    
    private String searchString = null;
    private boolean type5 = false;
    private boolean type4 = false;
    private boolean type3 = false;
    private boolean type2 = false;
    private boolean type1 = false;
    
    public String getSearchString() {
        return (this.searchString);
    }
    
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
    
    public boolean getType5() {
        return (this.type5);
    }
    public void setType5(boolean RHS) {
        this.type5 = RHS;
    }
    
    public boolean getType4() {
        return (this.type4);
    }
    public void setType4(boolean RHS) {
        this.type4 = RHS;
    }
    
    public boolean getType3() {
        return (this.type3);
    }
    public void setType3(boolean RHS) {
        this.type3 = RHS;
    }
    
    public boolean getType2() {
        return (this.type2);
    }
    public void setType2(boolean RHS) {
        this.type2 = RHS;
    }
    
    public boolean getType1() {
        return (this.type1);
    }
    public void setType1(boolean RHS) {
        this.type1 = RHS;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.searchString = null;
        this.type5 = false;
        this.type4 = false;
        this.type3 = false;
        this.type2 = false;
        this.type1 = false;
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
