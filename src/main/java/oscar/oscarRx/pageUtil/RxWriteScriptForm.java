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

public final class RxWriteScriptForm extends ActionForm {
    String action = "";
    int drugId = 0;
    int demographicNo = 0;
    String rxDate = null;       //cnv to Date
    String endDate = null;      //cnv to Date
    String writtenDate = null;	//cnv to Date
    String GN = null;
    String BN = null;
    int GCN_SEQNO = 0;
    String customName = null;
    String takeMin = null;
    String takeMax = null;
    String frequencyCode = null;
    String duration = null;
    String durationUnit = null;
    String quantity = null;
    String dispensingUnits = null;
    int repeat = 0;
    String lastRefillDate = null;
    boolean nosubs = false;
    boolean prn = false;
    boolean customInstr = false;
    boolean longTerm = false;
    boolean shortTerm = false;
    boolean pastMed = false;
    boolean dispenseInternal = false;
    boolean patientComplianceY=false;
    boolean patientComplianceN=false;
    String special = null;
    String atcCode = null;
    String regionalIdentifier = null;
    String method = null;
    String unit = null;
    String unitName = null;
    String route = null;
    String dosage = null;
    String outsideProviderName = null;
    String outsideProviderOhip = null;
    
    
    public String getAction() {
        return this.action;
    }
    
    public void setAction(String RHS) {
        this.action = RHS;
    }
    
    public int getDrugId() {
        return this.drugId;
    }
    
    public void setDrugID(int RHS) {
        this.drugId = RHS;
    }
    
    public int getDemographicNo() {
        return this.demographicNo;
    }
    
    public void setDemographicNo(int RHS) {
        this.demographicNo = RHS;
    }
    
    public String getRxDate() {
        return this.rxDate;
    }
    
    public void setRxDate(String RHS) {
        this.rxDate = RHS;
    }
    
    public String getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(String RHS) {
        this.endDate = RHS;
    }
    
    public String getWrittenDate() {
        return this.writtenDate;
    }
    
    public void setWrittenDate(String RHS) {
        this.writtenDate = RHS;
    }
    
    public String getGenericName() {
        return this.GN;
    }
    
    public void setGenericName(String RHS) {
        this.GN = RHS;
    }
    
    public String getBrandName() {
        return this.BN;
    }
    
    public void setBrandName(String RHS) {
        this.BN = RHS;
    }
    
    public int getGCN_SEQNO() {
        return this.GCN_SEQNO;
    }
    
    public void setGCN_SEQNO(int RHS) {
        this.GCN_SEQNO = RHS;
    }
    
    public String getCustomName() {
        return this.customName;
    }
    public void setCustomName(String RHS) {
        this.customName = RHS;
    }
    
    public String getTakeMin() {
        return this.takeMin;
    }
    
    public void setTakeMin(String RHS) {
        this.takeMin = RHS;
    }
    
    public float getTakeMinFloat() {
        float i = -1;
        try {
            i = Float.parseFloat(this.takeMin);
        }
        catch (Exception e) {
        }
        return i;
    }
    
    public String getTakeMax() {
        return this.takeMax;
    }
    
    public void setTakeMax(String RHS) {
        this.takeMax = RHS;
    }
    
    public float getTakeMaxFloat() {
        float i = -1;
        try {
            i = Float.parseFloat(this.takeMax);
        }
        catch (Exception e) {
        }
        return i;
    }
    
    public String getFrequencyCode() {
        return this.frequencyCode;
    }
    
    public void setFrequencyCode(String RHS) {
        this.frequencyCode = RHS;
    }
    
    public String getDuration() {
        return this.duration;
    }
    
    public void setDuration(String RHS) {
        this.duration = RHS;
    }
    
    public String getDurationUnit() {
        return this.durationUnit;
    }
    
    public void setDurationUnit(String RHS) {
        this.durationUnit = RHS;
    }
    
    public String getQuantity() {
        return this.quantity;
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

    public int getRepeat() {
        return this.repeat;
    }
    
    public void setRepeat(int RHS) {
        this.repeat = RHS;
    }
    
    public String getLastRefillDate() {
	return this.lastRefillDate;
    }
    
    public void setLastRefillDate(String RHS) {
	this.lastRefillDate = RHS;
    }
    
    public boolean getNosubs() {
        return this.nosubs;
    }
    
    public void setNosubs(boolean RHS) {
        this.nosubs = RHS;
    }
    
    public boolean getPrn() {
        return this.prn;
    }
    
    public void setPrn(boolean RHS) {
        this.prn = RHS;
    }
    
    public String getSpecial() {
        return this.special;
    }
    
    public void setSpecial(String RHS) {
    	
    	if (RHS==null || RHS.length()<6) MiscUtils.getLogger().error("drug special is either null or empty : "+RHS, new IllegalArgumentException("special is null or empty"));
    	
        this.special = RHS;
    }
    
    public boolean getCustomInstr() {
        return this.customInstr;
    }
    
    public void setCustomInstr(boolean c) {
        this.customInstr = c;        
    }
    
    public boolean getLongTerm() {
	return this.longTerm;
    }
    
    public void setLongTerm(boolean l) {
	this.longTerm = l;
    }
    
    public boolean getShortTerm() {
	return this.shortTerm;
    }
    
    public void setShortTerm(boolean st) {
	this.shortTerm = st;
    }
    
    public boolean getPastMed() {
	return this.pastMed;
    }
    
    public void setPastMed(boolean p) {
	this.pastMed = p;
    }
    
    public boolean getDispenseInternal() {	
    	return dispenseInternal;
    }
    	
    public boolean isDispenseInternal() {
    	return dispenseInternal;
    }

    public void setDispenseInternal(boolean dispenseInternal) {
    	this.dispenseInternal = dispenseInternal;
    }
    
    public boolean getPatientComplianceY() {
	return this.patientComplianceY;
    }
    
    public void setPatientComplianceY(boolean c) {
	this.patientComplianceY = c;
    }
    
    public boolean getPatientComplianceN() {
	return this.patientComplianceN;
    }
    
    public void setPatientComplianceN(boolean c) {
	this.patientComplianceN = c;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.action = "";
        this.drugId = 0;
        this.demographicNo = 0;
        this.rxDate = null;
        this.endDate = null;
	this.writtenDate = null;
        this.BN = null;
        this.GCN_SEQNO = 0;
        this.customName = null;
        this.takeMin = null;
        this.takeMax = null;
        this.frequencyCode = null;
        this.duration = null;
        this.durationUnit = null;
        this.quantity = null;
        this.dispensingUnits = null;
        this.repeat = 0;
	this.lastRefillDate = null;
        this.nosubs = false;
        this.prn = false;
        this.special = null;
        this.unitName = null;
        this.customInstr = false;
	this.longTerm = false;
	this.shortTerm = false;
	this.pastMed = false;
	this.patientComplianceY = false;
	this.patientComplianceN = false;
	this.outsideProviderName = null;
	this.outsideProviderOhip = null;
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
    
    /** Getter for property atcCode.
     * @return Value of property atcCode.
     *
     */
    public java.lang.String getAtcCode() {
        return atcCode;
    }
    
    /** Setter for property atcCode.
     * @param atcCode New value of property atcCode.
     *
     */
    public void setAtcCode(java.lang.String atcCode) {
        this.atcCode = atcCode;
    }
    
    /** Getter for property regionalIdentifier.
     * @return Value of property regionalIdentifier.
     *
     */
    public java.lang.String getRegionalIdentifier() {
        return regionalIdentifier;
    }
    
    /** Setter for property regionalIdentifier.
     * @param regionalIdentifier New value of property regionalIdentifier.
     *
     */
    public void setRegionalIdentifier(java.lang.String regionalIdentifier) {
        this.regionalIdentifier = regionalIdentifier;
    }
    
    /**
     * Getter for property method.
     * @return Value of property method.
     */
    public java.lang.String getMethod() {
       return method;
    }
    
    /**
     * Setter for property method.
     * @param method New value of property method.
     */
    public void setMethod(java.lang.String method) {
       this.method = method;
    }
    
    /**
     * Getter for property unit.
     * @return Value of property unit.
     */
    public java.lang.String getUnit() {
       return unit;
    }
    
    /**
     * Setter for property unit.
     * @param unit New value of property unit.
     */
    public void setUnit(java.lang.String unit) {
       this.unit = unit;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    
    /**
     * Getter for property route.
     * @return Value of property route.
     */
    public java.lang.String getRoute() {
       return route;
    }
    
    /**
     * Setter for property route.
     * @param route New value of property route.
     */
    public void setRoute(java.lang.String route) {
       this.route = route;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    
    public String getOutsideProviderName() {
	return outsideProviderName;
    }
    
    public void setOutsideProviderName(String outsideProviderName) {
	this.outsideProviderName = outsideProviderName;
    }
    
    public String getOutsideProviderOhip() {
	return outsideProviderOhip;
    }
    
    public void setOutsideProviderOhip(String outsideProviderOhip) {
	this.outsideProviderOhip = outsideProviderOhip;
    }
}
