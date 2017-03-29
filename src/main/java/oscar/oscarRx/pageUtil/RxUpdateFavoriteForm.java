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


public final class RxUpdateFavoriteForm extends ActionForm {
    
    private String favoriteId = null;
    private String favoriteName = null;
    private String customName = null;
    private String takeMin = null;
    private String takeMax = null;
    private String frequencyCode = null;
    private String duration = null;
    private String durationUnit = null;
    private String quantity = null;
    private String dispensingUnits = null;
    private String repeat = null;
    private boolean nosubs = false;
    private boolean prn = false;
    private boolean customInstr = false;
    private String special = null;
    
    public boolean getCustomInstr() {
        return this.customInstr;
    }
    
    public void setCustomInstr(boolean customInstr) {
        this.customInstr = customInstr;
    }
    
    public String getFavoriteId() {
        return (this.favoriteId);
    }
    
    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }
    
    public String getFavoriteName() {
        return (this.favoriteName);
    }
    
    public void setFavoriteName(String RHS) {
        this.favoriteName = RHS;
    }
    
    public String getCustomName() {
        return this.customName;
    }
    
    public void setCustomName(String RHS) {
        this.customName = RHS;
    }
    
    public String getTakeMin() {
        return (this.takeMin);
    }
    
    public void setTakeMin(String RHS) {
        this.takeMin = RHS;
    }
    
    public String getTakeMax() {
        return (this.takeMax);
    }
    
    public void setTakeMax(String RHS) {
        this.takeMax = RHS;
    }
    
    public String getFrequencyCode() {
        return (this.frequencyCode);
    }
    
    public void setFrequencyCode(String RHS) {
        this.frequencyCode = RHS;
    }
    
    public String getDuration() {
        return (this.duration);
    }
    
    public void setDuration(String RHS) {
        this.duration = RHS;
    }
    
    public String getDurationUnit() {
        return (this.durationUnit);
    }
    
    public void setDurationUnit(String RHS) {
        this.durationUnit = RHS;
    }
    
    public String getQuantity() {
        return (this.quantity);
    }

    public void setQuantity(String RHS) {
        this.quantity = RHS;
    }

    public String getDispensingUnits() {
        return (this.dispensingUnits);
    }

    public void setDispensingUnits(String RHS) {
        this.dispensingUnits = RHS;
    }

    public String getRepeat() {
        return (this.repeat);
    }
    
    public void setRepeat(String RHS) {
        this.repeat = RHS;
    }
    
    public boolean getNosubs() {
        return (this.nosubs);
    }
    
    public void setNosubs(boolean RHS) {
        this.nosubs = RHS;
    }
    
    public boolean getPrn() {
        return (this.prn);
    }
    
    public void setPrn(boolean RHS) {
        this.prn = RHS;
    }
    
    public String getSpecial() {
        return (this.special);
    }
    
    public void setSpecial(String RHS) {
        this.special = RHS;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.favoriteId = null;
        this.favoriteName = null;
        this.customName = null;
        this.takeMin = null;
        this.takeMax = null;
        this.frequencyCode = null;
        this.duration = null;
        this.durationUnit = null;
        this.quantity = null;
        this.dispensingUnits = null;
        this.repeat = null;
        this.nosubs = false;
        this.prn = false;
        this.customInstr = false;
        this.special = null;
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
