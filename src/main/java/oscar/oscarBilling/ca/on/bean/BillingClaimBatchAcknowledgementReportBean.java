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


public class BillingClaimBatchAcknowledgementReportBean{

       String batchNumber= "";
       String operatorNumber= "";
       String providerNumber= "";
       String groupNumber = "";
       String batchCreateDate = "";
       String batchSequenceNumber = "";
       String microStart = "";  
       String microEnd = "";       
       String microType = "";    
       String claimNumber = "";   
       String recordNumber = ""; 
       String batchProcessDate = "";
       String explain = "";
       
       public BillingClaimBatchAcknowledgementReportBean(  String batchNumber,
                                                       String operatorNumber,
                                                       String providerNumber,
                                                       String groupNumber,
                                                       String batchCreateDate,
                                                       String batchSequenceNumber,
                                                       String microStart, 
                                                       String microEnd,      
                                                       String microType,   
                                                       String claimNumber,  
                                                       String recordNumber,
                                                       String batchProcessDate,
                                                       String explain){           
           this.batchNumber = batchNumber;
           this.operatorNumber = operatorNumber;
           this.providerNumber = providerNumber;
           this.groupNumber = groupNumber;
           this.batchCreateDate = batchCreateDate;
           this.batchSequenceNumber = batchSequenceNumber;
           this.microStart = microStart;  
           this.microEnd = microEnd;       
           this.microType = microType;    
           this.claimNumber = claimNumber;   
           this.recordNumber = recordNumber; 
           this.batchProcessDate = batchProcessDate;
           this.explain = explain;
       }


       public String getBatchNumber(){
           return batchNumber;
       }
       public void setBatchNumber(String batchNumber){
           this.batchNumber=batchNumber;
       }
       
       public String getOperatorNumber(){
           return operatorNumber;
       }
       public void setOperatorNumber(String operatorNumber){
           this.operatorNumber=operatorNumber;
       }
       
       public String getProviderNumber(){
           return providerNumber;
       }
       public void setProviderNumber(String providerNumber){
           this.providerNumber=providerNumber;
       }
       
       public String getGroupNumber(){
           return groupNumber;
       }
       public void setGroupNumber(String groupNumber){
           this.groupNumber=groupNumber;
       }
       
       public String getBatchCreateDate(){
           return batchCreateDate;
       }
       public void setBatchCreateDate(String batchCreateDate){
           this.batchCreateDate=batchCreateDate;
       }
       
       public String getBatchSequenceNumber(){
           return batchSequenceNumber;
       }
       public void setBatchSequenceNumber(String batchSequenceNumber){
           this.batchSequenceNumber=batchSequenceNumber;
       }
       
       public String getMicroStart(){
           return microStart;  
       }
       public void setMicroStart(String microStart){
           this.microStart=microStart;  
       }
       
       public String getMicroEnd(){
           return microEnd;       
       }
       public void setMicroEnd(String microEnd){
           this.microEnd=microEnd;       
       }
       
       
       public String getMicroType(){
           return microType;    
       }
       public void setMicroType(String microType){
           this.microType=microType;    
       }
       
       public String getRecordNumber(){
           return recordNumber;   
       }
       public void setRecordNumber(String recordNumber){
           this.recordNumber=recordNumber;   
       }
       
       public String getClaimNumber(){
           return claimNumber;
       }
       public void setClaimNumber(String claimNumber){
           this.claimNumber=claimNumber;
       }
       
       public String getBatchProcessDate(){
           return batchProcessDate;
       }
       public void setBatchProcessDate(String batchProcessDate){
           this.batchProcessDate=batchProcessDate;
       }
       
        public String getExplain(){
            return explain;
        }
        public void setExplain(String explain){
            this.explain=explain;
        }

       
}
