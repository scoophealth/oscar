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


public final class RxAddAllergyForm extends ActionForm {

    String reactionDescription = null;
    String name =null;
    String type = null;
    String startDate = null;
    String ageOfOnset = null;
    String lifeStage = null;
    String severityOfReaction = null;
    String onSetOfReaction = null;
    String allergyToArchive = null;
	String ID = null; //drugref_id
    
    public String getLifeStage() {
    	return lifeStage;
    }
	public void setLifeStage(String lifeStage) {
    	this.lifeStage = lifeStage;
    }

	
    ///
    public String getReactionDescription(){
        return (this.reactionDescription);
    }
    public void setReactionDescription(String reactionDescription){
        this.reactionDescription = reactionDescription;
    }
    ///
    public String getName(){
        return (this.name);
    }
    public void setName(String name){
        this.name = name;
    }
    ///
    public String getType(){
        return (this.type);
    }
    public void setType(String type){
        this.type = type;
    }
   

    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
      
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
    
    public String getStartDate() {
	return startDate;
    }
    
    public void setStartDate(String startDate) {
	this.startDate = startDate;
    }
    
        /**
     * Getter for property ageOfOnset.
     * @return Value of property ageOfOnset.
     */
    public java.lang.String getAgeOfOnset() {
       return ageOfOnset;
    }
    
    /**
     * Setter for property ageOfOnset.
     * @param ageOfOnset New value of property ageOfOnset.
     */
    public void setAgeOfOnset(java.lang.String ageOfOnset) {
       this.ageOfOnset = ageOfOnset;
    }
    
    /**
     * Getter for property severityOfReaction.
     * @return Value of property severityOfReaction.
     */
    public java.lang.String getSeverityOfReaction() {
       return severityOfReaction;
    }
    
    /**
     * Setter for property severityOfReaction.
     * @param severityOfReaction New value of property severityOfReaction.
     */
    public void setSeverityOfReaction(java.lang.String severityOfReaction) {
       this.severityOfReaction = severityOfReaction;
    }
    
    /**
     * Getter for property onSetOfReaction.
     * @return Value of property onSetOfReaction.
     */
    public java.lang.String getOnSetOfReaction() {
       return onSetOfReaction;
    }
    
    /**
     * Setter for property onSetOfReaction.
     * @param onSetOfReaction New value of property onSetOfReaction.
     */
    public void setOnSetOfReaction(java.lang.String onSetOfReaction) {
       this.onSetOfReaction = onSetOfReaction;
    }
    
    public String getAllergyToArchive() {
    	return allergyToArchive;
    }
    
    public void setAllergyToArchive(String allergyToArchive) {
    	this.allergyToArchive = allergyToArchive;
    }
    
    public String getID() {
    	return ID;
    }
    
    public void setID(String ID) {
    	this.ID = ID;
    }
}
