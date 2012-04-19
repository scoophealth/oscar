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


public class BillingClaimsErrorReportBean{

    String techSpec= null;
    String MOHoffice= null;
    String providerNumber= null;
    String groupNumber= null;
    String operatorNumber= null;
    String specialtyCode= null;
    String stationNumber= null;
    String claimProcessDate= null;

    String hin= "";
    String ver= "";
    String dob= "";
    String account= "";
    String billtype= "";
    String payee= "";
    String referNumber= "";
    String facilityNumber= "";
    String admitDate= "";
    String referLab = "";
    String location = "";
    String heCode1 = "";
    String heCode2 = "";
    String heCode3 = "";
    String heCode4 = "";
    String heCode5 = "";

    String regNumber= null;
    String patient_last= null;
    String patient_first= null;
    String patient_sex= null;
    String province_code= null;
    String reCode1 =  null;
    String reCode2 =  null;
    String reCode3 =  null;
    String reCode4 =  null;
    String reCode5 =  null;

    String servicecode= null;
    String amountsubmit= null;
    String serviceno= null;
    String servicedate= null;
    String dxcode= null;
    String code1 =  null;
    String code2 =  null;
    String code3 =  null;
    String code4 =  null;
    String code5 =  null;
    
    String explain =  null;
    String error =  null;
    
    String header1Count =  null;
    String header2Count =  null;
    String itemCount =  null;
    String messageCount =  null;
    
       public BillingClaimsErrorReportBean(){}


       public String getTechSpec(){
           return techSpec;
       }
       public void setTechSpec(String techSpec){
           this.techSpec=techSpec;
       }
       
       public String getMOHoffice(){
           return MOHoffice;
       }
       public void setMOHoffice(String MOHoffice){
           this.MOHoffice=MOHoffice;
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
       
       public String getOperatorNumber(){
           return operatorNumber;
       }
       public void setOperatorNumber(String operatorNumber){
           this.operatorNumber=operatorNumber;
       }
       
       public String getSpecialtyCode(){
           return specialtyCode;
       }
       public void setSpecialtyCode(String specialtyCode){
           this.specialtyCode=specialtyCode;
       }
       
       public String getStationNumber(){
           return stationNumber;  
       }
       public void setStationNumber(String stationNumber){
           this.stationNumber=stationNumber;  
       }
       
       public String getClaimProcessDate(){
           return claimProcessDate;       
       }
       public void setClaimProcessDate(String claimProcessDate){
           this.claimProcessDate=claimProcessDate;       
       }
       
       
       public String getHin(){
           return hin;    
       }
       public void setHin(String hin){
           this.hin=hin;    
       }
       
       public String getVer(){
           return ver;   
       }
       public void setVer(String ver){
           this.ver=ver;   
       }
       
       public String getDob(){
           return dob;
       }
       public void setDob(String dob){
           this.dob=dob;
       }
       
       public String getAccount(){
           return account;
       }
       public void setAccount(String account){
           this.account=account;
       }
       
       public String getBilltype(){
           return billtype;
       }
       public void setBilltype(String billtype){
           this.billtype=billtype;
       }
       
       public String getPayee(){
           return payee;
       }
       public void setPayee(String payee){
           this.payee=payee;
       }
    
       public String getReferNumber(){
           return referNumber;
       }
       public void setReferNumber(String referNumber){
           this.referNumber=referNumber;
       }
    
        public String getFacilityNumber(){
           return facilityNumber;
        }
        public void setFacilityNumber(String facilityNumber){
           this.facilityNumber=facilityNumber;
        }
    
        public String getAdmitDate(){
           return admitDate;
        }
        public void setAdmitDate(String admitDate){
           this.admitDate=admitDate;
        }
    
        public String getReferLab(){
           return referLab;
        }
        public void setReferLab(String referLab){
           this.referLab=referLab;
        }

        public String getLocation(){
           return location;
        }
        public void setLocation(String location){
           this.location=location;
        }

        public String getHeCode1(){
           return heCode1;
        }
        public void setHeCode1(String heCode1){
           this.heCode1=heCode1;
        }
    
            public String getHeCode2(){
           return heCode2;
        }
        public void setHeCode2(String heCode2){
           this.heCode2=heCode2;
        }
    
        public String getHeCode3(){
           return heCode3;
        }
        public void setHeCode3(String heCode3){
           this.heCode3=heCode3;
        }
        
        public String getHeCode4(){
           return heCode4;
        }
        public void setHeCode4(String heCode4){
           this.heCode4=heCode4;
        }
            
        public String getHeCode5(){
           return heCode5;
        }
        public void setHeCode5(String heCode5){
           this.heCode5=heCode5;
        }
       
        public String getRegNumber(){
           return regNumber;
        }
        public void setRegNumber(String regNumber){
           this.regNumber=regNumber;
        }

        public String getPatient_last(){
           return patient_last;
        }
        public void setPatient_last(String patient_last){
           this.patient_last=patient_last;
        }    
    
        public String getPatient_first(){
           return patient_first;
        }
        public void setPatient_first(String patient_first){
           this.patient_first=patient_first;
        }
        
        public String getPatient_sex(){
           return patient_sex;
        }
        public void setPatient_sex(String patient_sex){
           this.patient_sex=patient_sex;
        }
    
        public String getProvince_code(){
           return province_code;
        }
        public void setProvince_code(String province_code){
           this.province_code=province_code;
        }

        public String getReCode1(){
           return reCode1;
        }
        public void setReCode1(String reCode1){
           this.reCode1=reCode1;
        }

        public String getReCode2(){
           return reCode2;
        }
        public void setReCode2(String reCode2){
           this.reCode2=reCode2;
        }
        
        public String getReCode3(){
           return reCode3;
        }
        public void setReCode3(String reCode3){
           this.reCode3=reCode3;
        }        

        public String getReCode4(){
           return reCode4;
        }
        public void setReCode4(String reCode4){
           this.reCode4=reCode4;
        }

        public String getReCode5(){
           return reCode5;
        }
        public void setReCode5(String reCode5){
           this.reCode5=reCode5;
        }

        public String getServicecode(){
           return servicecode;
        }
        public void setServicecode(String servicecode){
           this.servicecode=servicecode;
        }    

        public String getAmountsubmit(){
           return amountsubmit;
        }
        public void setAmountsubmit(String amountsubmit){
           this.amountsubmit=amountsubmit;
        }  
    
        public String getServiceno(){
           return serviceno;
        }
        public void setServiceno(String serviceno){
           this.serviceno=serviceno;
        }      

        public String getServicedate(){
           return servicedate;
        }
        public void setServicedate(String servicedate){
           this.servicedate=servicedate;
        }  
        
        public String getDxcode(){
           return dxcode;
        }
        public void setDxcode(String dxcode){
           this.dxcode=dxcode;
        }  

        public String getCode1(){
           return code1;
        }
        public void setCode1(String code1){
           this.code1=code1;
        } 

        public String getCode2(){
           return code2;
        }
        public void setCode2(String code2){
           this.code2=code2;
        } 

        public String getCode3(){
           return code3;
        }
        public void setCode3(String code3){
           this.code3=code3;
        }         

        public String getCode4(){
           return code4;
        }
        public void setCode4(String code4){
           this.code4=code4;
        }         

        public String getCode5(){
           return code5;
        }
        public void setCode5(String code5){
           this.code5=code5;
        }  
        
        public String getExplain(){
           return explain;
        }
        public void setExplain(String explain){
           this.explain=explain;
        }   
        
        public String getError(){
           return error;
        }
        public void setError(String error){
           this.error=error;
        }   

        public String getHeader1Count(){
           return header1Count;
        }
        public void setHeader1Count(String header1Count){
           this.header1Count=header1Count;
        }
        
        public String getHeader2Count(){
           return header2Count;
        }
        public void setHeader2Count(String header2Count){
           this.header2Count=header2Count;
        }

        public String getItemCount(){
           return itemCount;
        }
        public void setItemCount(String itemCount){
           this.itemCount=itemCount;
        }
        
        public String getMessageCount(){
           return messageCount;
        }
        public void setMessageCount(String messageCount){
           this.messageCount=messageCount;
        }

}
