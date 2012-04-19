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


package oscar.oscarBilling.ca.on.bean;


public class BillingEDTOBECOutputSpecificationBean{

       String healthNo;
       String version;
       String responseCode;
       String identifier = "";
       String sex = "";
       String DOB = "";
       String expiry = "";  
       String lastName = "";       
       String firstName = "";    
       String secondName = "";   
       String MOH = "";       
       
       public BillingEDTOBECOutputSpecificationBean( String healthNo, String version, String responseCode){           
           this.healthNo = healthNo;
           this.version = version;
           this.responseCode = responseCode;
       }


       public String getHealthNo(){
           return healthNo;
       }
       public void setHealthNo(String healthNo){
           this.healthNo=healthNo;
       }
       
       public String getVersion(){
           return version;
       }
       public void setVersion(String version){
           this.version=version;
       }
       
       public String getResponseCode(){
           return responseCode;
       }
       public void setResponseCode(String responseCode){
           this.responseCode=responseCode;
       }
       
       public String getIdentifier(){
           return identifier;
       }
       public void setIdentifier(String identifier){
           this.identifier=identifier;
       }
       
       public String getSex(){
           return sex;
       }
       public void setSex(String sex){
           this.sex=sex;
       }
       
       public String getDOB(){
           return DOB;
       }
       public void setDOB(String DOB){
           this.DOB=DOB;
       }
       
       public String getExpiry(){
           return expiry;  
       }
       public void setExpiry(String expiry){
           this.expiry=expiry;  
       }
       
       public String getLastName(){
           return lastName;       
       }
       public void setLastName(String lastName){
           this.lastName=lastName;       
       }
       
       
       public String getFirstName(){
           return firstName;    
       }
       public void setFirstName(String firstName){
           this.firstName=firstName;    
       }
       
       public String getSecondName(){
           return secondName;   
       }
       public void setSecondName(String secondName){
           this.secondName=secondName;   
       }
       
       public String getMOH(){
           return MOH;
       }
       public void setMOH(String MOH){
           this.MOH=MOH;
       }
       

       
}
