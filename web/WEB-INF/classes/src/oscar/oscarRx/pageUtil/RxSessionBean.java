/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarRx.pageUtil;

import oscar.OscarProperties;
import oscar.oscarRx.data.*;

import java.util.*;

public class RxSessionBean {
    private String providerNo = null;
    private int demographicNo = 0;
    
    private ArrayList stash = new ArrayList();
    private int stashIndex = -1;
    private Hashtable allergyWarnings = new Hashtable();
    private Hashtable workingAllergyWarnings = new Hashtable();
    
    
    
    
    //--------------------------------------------------------------------------
    
    public String getProviderNo() {
        return this.providerNo;
    }
    public void setProviderNo(String RHS) {
        this.providerNo = RHS;
    }
    
    public int getDemographicNo() {
        return this.demographicNo;
    }
    public void setDemographicNo(int RHS) {
        this.demographicNo = RHS;
    }
    
    //--------------------------------------------------------------------------
    
    public int getStashIndex() {
        return this.stashIndex;
    }
    public void setStashIndex(int RHS) {
        if(RHS < this.getStashSize()) {
            this.stashIndex = RHS;
        }
    }
    
    public int getStashSize() {
        return this.stash.size();
    }
    
    public RxPrescriptionData.Prescription[] getStash() {
        RxPrescriptionData.Prescription[] arr = {};
        
        arr = (RxPrescriptionData.Prescription[])stash.toArray(arr);
        
        return arr;
    }
    
    public RxPrescriptionData.Prescription getStashItem(int index) {
        return (RxPrescriptionData.Prescription)stash.get(index);
    }
    
    public void setStashItem(int index, RxPrescriptionData.Prescription item) {
        //this.clearDAM();
        //this.clearDDI();
        stash.set(index, item);
    }
    
    public int addStashItem(RxPrescriptionData.Prescription item) {
        //this.clearDDI();
        //this.clearDAM();
        int ret = -1;
        
        int i;
        RxPrescriptionData.Prescription rx;
        
        //check to see if the item already exists
        //by checking for duplicate brandname and gcn seq no
        //if it exists, return it, else add it.
        for(i=0;i<this.getStashSize(); i++) {
            rx = this.getStashItem(i);
            
            if(item.isCustom()) {
                if(rx.isCustom() && rx.getCustomName() !=null && item.getCustomName() != null) {
                    if(rx.getCustomName().equals(item.getCustomName())) {
                        ret = i;
                        break;
                    }
                }
            }
            else {
                if(rx.getBrandName()!=null && item.getBrandName() !=null) {
                    if(rx.getBrandName().equals(item.getBrandName())
                    && rx.getGCN_SEQNO()==item.getGCN_SEQNO()) {
                        ret = i;
                        break;
                    }
                }
            }
        }
        
        if(ret>-1) {
            return ret;
        }
        else {
            stash.add(item);
            preloadInteractions();
            preloadAllergyWarnings(item.getAtcCode());
            return this.getStashSize()-1;
        }
    }
    
    public void removeStashItem(int index) {
    //    this.clearDDI();
    //    this.clearDAM();
        stash.remove(index);
    }
    
    public void clearStash() {
    //    this.clearDDI();
    //    this.clearDAM();
        stash = new ArrayList();
    }
    
    
    //--------------------------------------------------------------------------
    
    public boolean isValid() {
        if(this.demographicNo > 0
        && this.providerNo != null
        && this.providerNo.length() > 0) {
            return true;
        }
        return false;
    }
    
    private void preloadInteractions(){
       RxInteractionData interact = RxInteractionData.getInstance();
       interact.preloadInteraction(this.getAtcCodes());
    }

    public void clearAllergyWarnings(){
       allergyWarnings =null;
       allergyWarnings = new Hashtable();
    }    
    
    
    private void preloadAllergyWarnings(String atccode){
       try{
         oscar.oscarRx.data.RxPatientData.Patient.Allergy[] allergies = new oscar.oscarRx.data.RxPatientData().getPatient(getDemographicNo()).getAllergies();
         RxAllergyWarningWorker worker = new RxAllergyWarningWorker(this,atccode,allergies);
         addToWorkingAllergyWarnings(atccode,worker);       
         worker.start();         
       }catch( Exception e ){e.printStackTrace();}
    }
    
    public void addAllergyWarnings(String atc,oscar.oscarRx.data.RxPatientData.Patient.Allergy[] allergy){
       allergyWarnings.put(atc, allergy);
    }
    
    public void addToWorkingAllergyWarnings(String atc,RxAllergyWarningWorker worker){
       workingAllergyWarnings.put(atc,worker);
    }
    public void removeFromWorkingAllergyWarnings(String atc){
       workingAllergyWarnings.remove(atc);
    }
    
    
    public oscar.oscarRx.data.RxPatientData.Patient.Allergy[] getAllergyWarnings(String atccode){
      oscar.oscarRx.data.RxPatientData.Patient.Allergy[] allergies = null;      
      
      //Check to see if Allergy checking property is on and if atccode is not null and if atccode is not "" or "null"
      
      if (OscarProperties.getInstance().getBooleanProperty("RX_ALLERGY_CHECKING","yes") && atccode != null && !atccode.equals("") && !atccode.equals("null")){
          if (allergyWarnings.containsKey(atccode) ){
             System.out.println("Allergy has Already been searched!");
             allergies = (oscar.oscarRx.data.RxPatientData.Patient.Allergy[]) allergyWarnings.get(atccode);
          }else if(workingAllergyWarnings.contains(atccode) ){
             System.out.println("Allergy has Already been searched but not finished !");
             RxAllergyWarningWorker worker = (RxAllergyWarningWorker) workingAllergyWarnings.get(atccode);
             if (worker != null){
                 try {
                    worker.join();
                    System.out.println("Allergy has Already been searched now finished!");
                    // Finished
                 } catch (InterruptedException e) {
                    // Thread was interrupted
                    System.out.println("Already been searched PROBLEM!");
                    e.printStackTrace();
                 }


             }
             allergies = (oscar.oscarRx.data.RxPatientData.Patient.Allergy[]) allergyWarnings.get(atccode);

          }else{
             System.out.println("NEW ATC CODE for allergy");
             try{                                
                RxDrugData drugData = new RxDrugData();
                oscar.oscarRx.data.RxPatientData.Patient.Allergy[]  allAllergies = new oscar.oscarRx.data.RxPatientData().getPatient(getDemographicNo()).getAllergies();
                allergies = drugData.getAllergyWarnings(atccode,allAllergies);                 
                    if (allergies != null){                   
                       addAllergyWarnings(atccode,allergies);            
                    }
             }catch(Exception e){
                 e.printStackTrace();
             }         
          }
      }
      return allergies;
   }
    
    
    
    public Vector getAtcCodes(){
       RxPrescriptionData rxData = new RxPrescriptionData();                    
       Vector atcCodes = rxData.getCurrentATCCodesByPatient(this.getDemographicNo());      
       RxPrescriptionData.Prescription rx;                
       for(int i=0;i<this.getStashSize(); i++) {
          rx = this.getStashItem(i);
          atcCodes.add(rx.getAtcCode());
       }
       return atcCodes;
    }
    
    public RxDrugData.Interaction[] getInteractions(){
       RxDrugData.Interaction[] interactions = null;
       long start = System.currentTimeMillis();
       long start2 = 0;
       long end2 = 0;
       try{
       start2 = System.currentTimeMillis();
          RxPrescriptionData rxData = new RxPrescriptionData();
          RxDrugData drugData = new RxDrugData();
          RxInteractionData rxInteract =  RxInteractionData.getInstance();
          Vector atcCodes = rxData.getCurrentATCCodesByPatient(this.getDemographicNo());

          System.out.println("atccode "+atcCodes.hashCode());
          RxPrescriptionData.Prescription rx;                
          for(int i=0;i<this.getStashSize(); i++) {
             rx = this.getStashItem(i);
             if (rx.isValidAtcCode()){
                atcCodes.add(rx.getAtcCode());
             }
          }
          System.out.println("atccode 2"+atcCodes.hashCode());
          if (atcCodes != null && atcCodes.size() > 1){
             try{        
                interactions = rxInteract.getInteractions(atcCodes);
                System.out.println("interactions "+interactions.length);
                 for(int i =0 ; i < interactions.length;i++){
                    System.out.println(interactions[i].affectingatc+" "+interactions[i].effect+" "+interactions[i].affectedatc);
                 }
                 Arrays.sort(interactions);
              }catch(Exception e){
                 e.printStackTrace();
              }
          }
          
       end2 = System.currentTimeMillis() - start2;         
       }catch(Exception e2){}                 
       long end = System.currentTimeMillis() - start;      
       
       
       System.out.println("took "+end+ "milliseconds vs "+end2);
       return interactions;
    }
}