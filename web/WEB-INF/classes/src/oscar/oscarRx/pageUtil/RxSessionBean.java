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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarRx.data.RxAllergyWarningWorker;
import oscar.oscarRx.data.RxDrugData;
import oscar.oscarRx.data.RxInteractionData;
import oscar.oscarRx.data.RxPrescriptionData;

public class RxSessionBean {
    private String providerNo = null;
    private int demographicNo = 0;

    private ArrayList<RxPrescriptionData.Prescription> stash = new ArrayList();
   // private ArrayList stash=new ArrayList();
    private int stashIndex = -1;
    private Hashtable allergyWarnings = new Hashtable();
    private Hashtable workingAllergyWarnings = new Hashtable();
    private ArrayList attributeNames = new ArrayList();
    private String interactingDrugList="";//contains hash tables, each hashtable has the a
    private List<String> reRxDrugIdList=new ArrayList();
    private HashMap randomIdDrugIdPair=new HashMap();
    private List<HashMap<String,String>> listMedHistory=new ArrayList();





    //--------------------------------------------------------------------------
    public List<HashMap<String,String>> getListMedHistory(){
        return listMedHistory;
    }
    public void setListMedHistory(List<HashMap<String,String>> l){
        listMedHistory=l;
    }
    public HashMap getRandomIdDrugIdPair(){
        return randomIdDrugIdPair;
    }

    public void setRandomIdDrugIdPair(HashMap hm){
        randomIdDrugIdPair=hm;
    }

    public void addRandomIdDrugIdPair(long r,int d){
        randomIdDrugIdPair.put(r, d);
    }
    public void addReRxDrugIdList(String s){
        reRxDrugIdList.add(s);
    }
    public void setReRxDrugIdList(List<String> sList){
        reRxDrugIdList=sList;
    }
    public List<String> getReRxDrugIdList(){
        return reRxDrugIdList;
    }
    public void clearReRxDrugIdList(){
        reRxDrugIdList=new ArrayList();
    }
    public String getInteractingDrugList(){
        return interactingDrugList;
    }
    public void setInteractingDrugList(String s){
        interactingDrugList=s;
    }

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

    public ArrayList getAttributeNames() {
	return this.attributeNames;
    }
    public void setAttributeNames(ArrayList RHS) {
	this.attributeNames = RHS;
    }
    public void addAttributeName(String RHS) {
	this.attributeNames.add(RHS);
    }

    public void addAttributeName(String RHS, int index) {
        this.attributeNames.set(index, RHS);
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

    public int getIndexFromRx(int randomId){
        int ret=-1;
        for(int i=0;i<stash.size();i++){
            if( ((RxPrescriptionData.Prescription)stash.get(i)).getRandomId()==randomId) {
                ret=i;
                break;
        }
        }
        MiscUtils.getLogger().debug("in getIndexFromRx="+ret);
        return ret;
    }
    public RxPrescriptionData.Prescription[] getStash() {
        RxPrescriptionData.Prescription[] arr = {};

        arr = (RxPrescriptionData.Prescription[])stash.toArray(arr);

        return arr;
    }

    public RxPrescriptionData.Prescription getStashItem(int index) {
        return (RxPrescriptionData.Prescription)stash.get(index);
    }

    //return rx from its random id
    public RxPrescriptionData.Prescription getStashItem2(int randomId) {;
        RxPrescriptionData.Prescription psp=null;
        for (RxPrescriptionData.Prescription rx:stash){
            if(rx.getRandomId()==randomId){
                psp=rx;
            }
        }
        return psp;
    }

    public void setStashItem(int index, RxPrescriptionData.Prescription item) {
        //this.clearDAM();
        //this.clearDDI();
        stash.set(index, item);
    }

    public int addStashItem(RxPrescriptionData.Prescription item) {

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
       }catch( Exception e ){MiscUtils.getLogger().error("Error", e);}
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

             allergies = (oscar.oscarRx.data.RxPatientData.Patient.Allergy[]) allergyWarnings.get(atccode);
          }else if(workingAllergyWarnings.contains(atccode) ){

             RxAllergyWarningWorker worker = (RxAllergyWarningWorker) workingAllergyWarnings.get(atccode);
             if (worker != null){
                 try {
                    worker.join();

                    // Finished
                 } catch (InterruptedException e) {
                    // Thread was interrupted

                    MiscUtils.getLogger().error("Error", e);
                 }


             }
             allergies = (oscar.oscarRx.data.RxPatientData.Patient.Allergy[]) allergyWarnings.get(atccode);

          }else{
             MiscUtils.getLogger().debug("NEW ATC CODE for allergy");
             try{
                RxDrugData drugData = new RxDrugData();
                oscar.oscarRx.data.RxPatientData.Patient.Allergy[]  allAllergies = new oscar.oscarRx.data.RxPatientData().getPatient(getDemographicNo()).getAllergies();
                allergies = drugData.getAllergyWarnings(atccode,allAllergies);
                    if (allergies != null){
                       addAllergyWarnings(atccode,allergies);
                    }
             }catch(Exception e){
                 MiscUtils.getLogger().error("Error", e);
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
          
          RxInteractionData rxInteract =  RxInteractionData.getInstance();
          Vector atcCodes = rxData.getCurrentATCCodesByPatient(this.getDemographicNo());

          MiscUtils.getLogger().debug("atccode "+atcCodes);
          RxPrescriptionData.Prescription rx;
          for(int i=0;i<this.getStashSize(); i++) {
             rx = this.getStashItem(i);
             if (rx.isValidAtcCode()){
                atcCodes.add(rx.getAtcCode());
             }
          }
          MiscUtils.getLogger().debug("atccode 2"+atcCodes);
          if (atcCodes != null && atcCodes.size() > 1){
             try{
                interactions = rxInteract.getInteractions(atcCodes);
                MiscUtils.getLogger().debug("interactions "+interactions.length);
                 for(int i =0 ; i < interactions.length;i++){
                    MiscUtils.getLogger().debug(interactions[i].affectingatc+" "+interactions[i].effect+" "+interactions[i].affectedatc);
                 }
                 Arrays.sort(interactions);
              }catch(Exception e){
                 MiscUtils.getLogger().error("Error", e);
              }
          }

       end2 = System.currentTimeMillis() - start2;
       }catch(Exception e2){}
       long end = System.currentTimeMillis() - start;


       MiscUtils.getLogger().debug("took "+end+ "milliseconds vs "+end2);
       return interactions;
    }
}