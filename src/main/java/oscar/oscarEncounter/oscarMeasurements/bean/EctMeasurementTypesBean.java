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


package oscar.oscarEncounter.oscarMeasurements.bean;

import java.util.Vector;

public class EctMeasurementTypesBean{
    
    int id;
    String type;
    String typeDisplayName;
    String typeDesc;
    String measuringInstrc;
    String validation;
    String lastProviderFirstName = null;
    String lastProviderLastName = null;
    String lastData = null;
    String lastMInstrc = null;
    String lastComments = null;
    String lastDateObserved =null;
    String lastDateEntered = null;
    Vector validationRules = new Vector();
    private String validationName = null;
    
    //for forms connecting to miles, determine whether to prefill the data or not when a new form is opened
    boolean canPrefill = false;
    
    public EctMeasurementTypesBean(){}
    
    public EctMeasurementTypesBean(int id, String type, String typeDisplayName, String typeDesc, String measuringInstrc, String validation){
        this.id = id;
        this.type = type;
        this.typeDisplayName = typeDisplayName;
        this.typeDesc = typeDesc;
        this.measuringInstrc = measuringInstrc;
        this.validation = validation;
    }
    
    public EctMeasurementTypesBean( int id, String type, String typeDisplayName, String typeDesc, String measuringInstrc,
            String validation, String lastProviderFirstName,
            String lastProviderLastName,
            String lastData,
            String lastMInstrc,
            String lastComments,
            String lastDateObserved,
            String lastDateEntered){
        this.id = id;
        this.type = type;
        this.typeDisplayName = typeDisplayName;
        this.typeDesc = typeDesc;
        this.measuringInstrc = measuringInstrc;
        this.validation = validation;
        this.lastProviderFirstName = lastProviderFirstName;
        this.lastProviderLastName = lastProviderLastName;
        this.lastData = lastData;
        this.lastMInstrc = lastMInstrc;
        this.lastComments = lastComments;
        this.lastDateObserved = lastDateObserved;
        this.lastDateEntered = lastDateEntered;
    }
    
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
    
    public String getTypeDisplayName(){
        return typeDisplayName;
    }
    public void setTypeDisplayName(String typeDisplayName){
        this.typeDisplayName=typeDisplayName;
    }
    
    public String getTypeDesc(){
        return typeDesc;
    }
    public void setTypeDesc(String typeDesc){
        this.typeDesc=typeDesc;
    }
    
    public String getMeasuringInstrc(){
        return measuringInstrc;
    }
    public void setMeasuringInstrc(String measuringInstrc){
        this.measuringInstrc=measuringInstrc;
    }
    
    public String getValidation(){
        return validation;
    }
    
    public void setValidation(String validation){
           this.validation=validation;
    }
    
    public Vector getValidationRules(){
        return this.validationRules;
    }
    
    public void addValidationRule(EctValidationsBean validationRule){
        this.validationRules.addElement(validationRule);
    }
    
    public String getLastProviderFirstName(){
        return lastProviderFirstName;
    }
    
    public void setLastProviderFirstName(String lastProviderFirstName){
        this.lastProviderFirstName = lastProviderFirstName;
    }
    
    public String getLastProviderLastName(){
        return lastProviderLastName;
    }
    
    public void setLastProviderLastName(String lastProviderLastName){
        this.lastProviderLastName = lastProviderLastName;
    }
    
    public String getLastData(){
        return lastData;
    }
    
    public void setLastData(String lastData){
        this.lastData = lastData;
    }
    
    public String getLastMInstrc(){
        return lastMInstrc;
    }
    
    public void setLastMInstrc(String lastMInstrc){
        this.lastMInstrc = lastMInstrc;
    }
    
    public String getLastComments(){
        return lastComments;
    }
    
    public void setLastComments(String lastComments){
        this.lastComments = lastComments;
    }
    
    public String getLastDateObserved(){
        return lastDateObserved;
    }
    
    public void setLastDateObserved(String lastDateObserved){
        this.lastDateObserved = lastDateObserved;
    }
    
    public String getLastDateEntered(){
        lastDateEntered = (lastDateEntered==null?null:lastDateEntered.substring(0,10));
        return lastDateEntered;
    }
    
    public void setLastDateEntered(String lastDateEntered){
        this.lastDateEntered = lastDateEntered;
    }
    
    public boolean getCanPrefill(){
        return canPrefill;
    }
    
    public void setCanPrefill(boolean canPrefill){
        this.canPrefill = canPrefill;
    }

    public String getValidationName() {
        return validationName;
    }

    public void setValidationName(String validationName) {
        this.validationName = validationName;
    }
}
