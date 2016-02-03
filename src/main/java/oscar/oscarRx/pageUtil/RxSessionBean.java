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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.phr.model.PHRMedication;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarRx.data.RxAllergyWarningWorker;
import oscar.oscarRx.data.RxDrugData;
import oscar.oscarRx.data.RxInteractionData;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxPrescriptionData;

public class RxSessionBean  implements java.io.Serializable {
	private static final Logger logger=MiscUtils.getLogger();

    private String providerNo = null;
    private int demographicNo = 0;
    private String view = "Active";

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
    private HashMap<Long,PHRMedication> pairPHRMed=new HashMap<Long,PHRMedication>();
    private HashMap<Long,PHRMedication> pairPrevViewedPHRMed=new HashMap<Long,PHRMedication>();//viewed meds but not saved, rethrieved from phr_document





    //--------------------------------------------------------------------------
    public HashMap<Long,PHRMedication> getPairPHRMed(){
        return pairPHRMed;
    }
    public void setPairPHRMed(HashMap<Long,PHRMedication> l){
        pairPHRMed=l;
    }
    public void clearPairPHRMed(){
        pairPHRMed=new HashMap<Long,PHRMedication>();
    }
    public HashMap<Long,PHRMedication> getPairPrevViewedPHRMed(){
        return pairPrevViewedPHRMed;
    }
    public void setPairPrevViewedPHRMed(HashMap<Long,PHRMedication> l){
        pairPrevViewedPHRMed=l;
    }
    public void clearPairPrevViewedPHRMed(){
        pairPrevViewedPHRMed=new HashMap<Long,PHRMedication>();
    }
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
    public String getView() {
    	return view;
    }
	public void setView(String view) {
    	this.view = view;
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
            if(stash.get(i).getRandomId()==randomId) {
                ret=i;
                break;
        }
        }
        logger.debug("in getIndexFromRx="+ret);
        return ret;
    }
    public RxPrescriptionData.Prescription[] getStash() {
        RxPrescriptionData.Prescription[] arr = {};

        arr = stash.toArray(arr);

        return arr;
    }

    public RxPrescriptionData.Prescription getStashItem(int index) {
        return stash.get(index);
    }

    //return rx from its random id
    public RxPrescriptionData.Prescription getStashItem2(int randomId) {
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

    public int addStashItem(LoggedInInfo loggedInInfo, RxPrescriptionData.Prescription item) {

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
            preloadAllergyWarnings(loggedInInfo, item.getAtcCode());


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


    private void preloadAllergyWarnings(LoggedInInfo loggedInInfo, String atccode){
       try{
         Allergy[] allergies = RxPatientData.getPatient(loggedInInfo, getDemographicNo()).getActiveAllergies();
         RxAllergyWarningWorker worker = new RxAllergyWarningWorker(this,atccode,allergies);
         addToWorkingAllergyWarnings(atccode,worker);
         worker.start();
       }catch( Exception e ){
      	 logger.error("Error for demographic " + getDemographicNo(), e);
       }
    }

    public void addAllergyWarnings(String atc,Allergy[] allergy){
       allergyWarnings.put(atc, allergy);
    }

    public void addToWorkingAllergyWarnings(String atc,RxAllergyWarningWorker worker){
       workingAllergyWarnings.put(atc,worker);
    }
    public void removeFromWorkingAllergyWarnings(String atc){
       workingAllergyWarnings.remove(atc);
    }


    public Allergy[] getAllergyWarnings(LoggedInInfo loggedInInfo, String atccode){
      Allergy[] allergies = null;

      //Check to see if Allergy checking property is on and if atccode is not null and if atccode is not "" or "null"

      if (OscarProperties.getInstance().getBooleanProperty("RX_ALLERGY_CHECKING","yes") && atccode != null && !atccode.equals("") && !atccode.equals("null")){
      	logger.debug("Checking allergy reaction : "+atccode);
      	if (allergyWarnings.containsKey(atccode) ){

             allergies = (Allergy[]) allergyWarnings.get(atccode);
          }else if(workingAllergyWarnings.contains(atccode) ){

             RxAllergyWarningWorker worker = (RxAllergyWarningWorker) workingAllergyWarnings.get(atccode);
             if (worker != null){
                 try {
                    worker.join();

                    // Finished
                 } catch (InterruptedException e) {
                    // Thread was interrupted

                    logger.error("Error", e);
                 }


             }
             allergies = (Allergy[]) allergyWarnings.get(atccode);

          }else{
         	 logger.debug("NEW ATC CODE for allergy");
             try{
                RxDrugData drugData = new RxDrugData();
                Allergy[]  allAllergies = RxPatientData.getPatient(loggedInInfo, getDemographicNo()).getActiveAllergies();
                allergies = drugData.getAllergyWarnings(atccode,allAllergies);
                    if (allergies != null){
                       addAllergyWarnings(atccode,allergies);
                    }
             }catch(Exception e){
            	 logger.error("Error", e);
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

    public List getRegionalIdentifier(){
    	RxPrescriptionData rxData = new RxPrescriptionData();
        List regionalIdentifierCodes = rxData.getCurrentRegionalIdentifiersCodesByPatient(this.getDemographicNo());
        RxPrescriptionData.Prescription rx;
        for(int i=0;i<this.getStashSize(); i++) {
           rx = this.getStashItem(i);
           regionalIdentifierCodes.add(rx.getRegionalIdentifier());
        }
        return regionalIdentifierCodes;
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

          logger.debug("atccode "+atcCodes);
          RxPrescriptionData.Prescription rx;
          for(int i=0;i<this.getStashSize(); i++) {
             rx = this.getStashItem(i);
             if (rx.isValidAtcCode()){
                atcCodes.add(rx.getAtcCode());
             }
          }
          logger.debug("atccode 2"+atcCodes);
          if (atcCodes != null && atcCodes.size() > 1){
             try{
                interactions = rxInteract.getInteractions(atcCodes);
                logger.debug("interactions "+interactions.length);
                 for(int i =0 ; i < interactions.length;i++){
               	  logger.debug(interactions[i].affectingatc+" "+interactions[i].effect+" "+interactions[i].affectedatc);
                 }
                 Arrays.sort(interactions);
              }catch(Exception e){
            	  logger.error("Error", e);
              }
          }

       end2 = System.currentTimeMillis() - start2;
       }catch(Exception e2){}
       long end = System.currentTimeMillis() - start;


       logger.debug("took "+end+ "milliseconds vs "+end2);
       return interactions;
    }
}
