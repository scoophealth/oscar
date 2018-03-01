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
package org.oscarehr.rx.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;



import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.RemoteDrugAllergyHelper;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.UserDSMessagePrefsDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.UserDSMessagePrefs;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.to.model.RxDsMessageTo1;

import oscar.OscarProperties;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.pageUtil.RxMyDrugrefInfoAction;
import oscar.oscarRx.util.MyDrugrefComparator;
import oscar.oscarRx.util.RxDrugRef;

public class DrugrefUtil {
	private static final Logger logger = MiscUtils.getLogger();
	UserPropertyDAO  propDAO =  (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
    UserDSMessagePrefsDao  dsmessageDao =  (UserDSMessagePrefsDao) SpringUtils.getBean("userDSMessagePrefsDao");
    
    DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
    DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	
   
    //List regionalIdentifiers = bean.getRegionalIdentifier();
	public  List<RxDsMessageTo1> getMessages(LoggedInInfo loggedInInfo,String provider,int demographicNo,List<String> atcCodes,List regionalIdentifiers,Locale locale ) throws Exception {
		ResourceBundle mr  = ResourceBundle.getBundle("uiResources", locale);
		List<RxDsMessageTo1> returnList = new ArrayList<RxDsMessageTo1>();

        		RxMyDrugrefInfoAction rxMyDrugrefInfoAction = new RxMyDrugrefInfoAction();

	        long start = System.currentTimeMillis();
	        
	        for(String s :atcCodes) {
	        		logger.error("ATC:"+s);
	        }
	        
	        for(Object s :regionalIdentifiers) {
        		logger.error("Dins:"+(String)s);
        }

	        Hashtable dsPrefs= dsmessageDao.getHashofMessages(provider,UserDSMessagePrefs.MYDRUGREF);
	        //? not sure if this works, it was inside an if before
	        
	        UserProperty prop = propDAO.getProp(provider, UserProperty.MYDRUGREF_ID);
	        String myDrugrefId = null;
	        //get from system first
	        myDrugrefId  = OscarProperties.getInstance().getProperty("mydrugref_id");

	        //override with user pref
	        if (prop != null && prop.getValue().length()>0){
	            myDrugrefId = prop.getValue();
	        }

	        
	        Vector codes = new Vector(atcCodes);

	        if(Boolean.valueOf(OscarProperties.getInstance().getProperty("drug_allergy_interaction_warnings", "false"))) {
	        	RxDrugRef d = new RxDrugRef();
	        	Allergy[]  allerg = RxPatientData.getPatient(loggedInInfo, demographicNo).getActiveAllergies();
	        	Vector vec = new Vector();
	            for (int i =0; i < allerg.length; i++){
	               Hashtable<String,String> h = new Hashtable<String,String>();
	               h.put("id",""+i);
	               h.put("description",allerg[i].getDescription());
	               h.put("type",""+allerg[i].getTypeCode());
	               vec.add(h);
	            }
	        	codes.addAll(d.getAllergyClasses(vec));
	        }
	        //String[] str = new String[]{"warnings_byATC","bulletins_byATC","interactions_byATC"};
	        String[] str = new String[]{"atcfetch/getWarnings","atcfetch/getBulletins","atcfetch/getInteractions"};   //NEW more efficent way of sending multiple requests at the same time.
	        

	        logger.debug("Interaction, local drug atc codes : "+codes);

	        if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()){
	        		ArrayList<String> remoteDrugAtcCodes=RemoteDrugAllergyHelper.getAtcCodesFromRemoteDrugs(loggedInInfo, demographicNo);
	        		codes.addAll(remoteDrugAtcCodes);
	        		logger.debug("remote drug atc codes : "+remoteDrugAtcCodes);
	        }        
	        logger.debug("Interaction, local + remote drug atc codes : "+codes);   

	        List all = new ArrayList();
	        for (String command : str){
	            try{
	                List v = rxMyDrugrefInfoAction.getMyDrugrefInfo(loggedInInfo,command, codes, provider, myDrugrefId);

	                if (v !=null && v.size() > 0){
	                    all.addAll(v);
	                }

	            }catch(Exception e){
	                logger.debug("command :"+command+" "+e.getMessage());
	                MiscUtils.getLogger().error("Error", e);
	            }
	        }
	        logger.error("rx local interaction "+OscarProperties.getInstance().getProperty("RX_INTERACTION_LOCAL_DRUGREF_REGIONAL_IDENTIFIER"));
	        if(OscarProperties.getInstance().isPropertyActive("RX_INTERACTION_LOCAL_DRUGREF_REGIONAL_IDENTIFIER")){
	        	
	        	if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()){
	        		ArrayList<String> remoteDrugRegionalIdentiferCodes=RemoteDrugAllergyHelper.getRegionalIdentiferCodesFromRemoteDrugs(loggedInInfo, demographicNo);
	        		regionalIdentifiers.addAll(remoteDrugRegionalIdentiferCodes);
	        	}
	        
		        try{
		        	RxDrugRef rxDrugRef = new RxDrugRef();
		        	List localInteractions =  rxDrugRef.interactionByRegionalIdentifier(regionalIdentifiers,0);
		        	logger.error("local interactions size = "+localInteractions.size());
		        	all.addAll(localInteractions);
		        }catch(Exception e){
		        	logger.error("Error calling interactions by regional identifier",e);
		        }
	        }else{
	        	logger.error("RX_INTERACTION_LOCAL_DRUGREF_REGIONAL_IDENTIFIER was null" );
	        }
	        
	        
	        Collections.sort(all, new MyDrugrefComparator());
	        
	     
	        


	        MiscUtils.getLogger().error(all);
	        //loop through all to add interaction to each warning
	        List<String> currentIdWarnings=new ArrayList<String>();
	        try{
	            for(int i=0;i<all.size();i++){
	                Map ht=(Map)all.get(i);
	                
	                //////
	                Date dt=(Date)ht.get("updated_at");
		            Long time=dt.getTime();
		            String idWarning=ht.get("id")+"."+time;
		            if(!currentIdWarnings.contains(idWarning)){
		                currentIdWarnings.add(idWarning);
		                RxDsMessageTo1 dsMessage = new RxDsMessageTo1(ht,mr,locale);
		                returnList.add(dsMessage);

		            }
	            }
	        }catch(NullPointerException npe){
	            MiscUtils.getLogger().error("Error", npe);
	        }
	        
	        MiscUtils.getLogger().debug("currentIdWarnings is  "+currentIdWarnings);
	        //set session attribute hiddenResources if it was null
	        
	        if(dsPrefs!=null && dsPrefs.size()>0){
	            Map<String,String> hiddenR=new HashMap<String,String>();
	            
	            Enumeration em=dsPrefs.keys();
	            while(em.hasMoreElements()){
	                String resId=(String)em.nextElement();
	                resId=resId.replace(UserDSMessagePrefs.MYDRUGREF, "");
	                for(String warning:currentIdWarnings){
	                    if(warning.contains(resId)){
	                    	    logger.error("WARNING "+warning);
	                        String[] arr=warning.split("\\.");
	                        hiddenR.put(UserDSMessagePrefs.MYDRUGREF+resId, arr[1]);
	                    }
	                }
	            }
	            //request.getSession().setAttribute("hideResources", hiddenR);
	        }
	        
	        //if hideResources are not in warnings, remove them from hiddenResource and set them to archived=0 in database;
	        /*Map hiddenResAttribute=(Map)request.getSession().getAttribute("hideResources");
	        if(hiddenResAttribute==null){
	            Map<String,String> emptyHiddenRes=new HashMap<String,String>();
	            request.getSession().setAttribute("hideResources", emptyHiddenRes);
	        }else{
	        Enumeration hiddenResKeys=hiddenResAttribute.keys();
	        while(hiddenResKeys.hasMoreElements()){
	            String key=(String)hiddenResKeys.nextElement();
	            String value=(String)hiddenResAttribute.get(key);
	            Date updatedatId=new Date();
	            updatedatId.setTime(Long.parseLong(value));
	            String resId=key.replace(UserDSMessagePrefs.MYDRUGREF, "");
	            String id=resId+"."+value;
	            if(!currentIdWarnings.contains(id)){
	                hiddenResAttribute.remove(key);
	                //update database
	                //setShowDSMessage(dsmessageDao, provider, resId, updatedatId);
	            }
	        }
	        */
	   
	        
	        logger.error("MyDrugref return time " + (System.currentTimeMillis() - start) );  ///turn back to debug
	        return returnList;
	    }
	    

		

	 
	public int getWarningLevel(LoggedInInfo loggedInInfo,Integer demographicNo) {
		   //filter out based on significance by facility, provider, demographic
        int level = 0;
        int orgLevel = loggedInInfo.getCurrentFacility().getRxInteractionWarningLevel();
        level = orgLevel;
        MiscUtils.getLogger().debug("orgLevel="+orgLevel);

        UserProperty uprop = propDAO.getProp(loggedInInfo.getLoggedInProviderNo(), "rxInteractionWarningLevel");
        if(uprop!=null) {
	        	if(uprop.getValue()!=null&&uprop.getValue().length()>0) {
	        		int providerLevel = Integer.parseInt(uprop.getValue());
	        		MiscUtils.getLogger().debug("providerLevel="+providerLevel);
	        		if(providerLevel>0)
	        			level = providerLevel;
	        	}
        }

        DemographicExt demoWarn = demographicExtDao.getLatestDemographicExt(demographicNo, "rxInteractionWarningLevel");
        if(demoWarn!=null) {
	        	if(demoWarn.getValue()!=null&&demoWarn.getValue().length()>0) {
	        		int demoLevel = Integer.valueOf(demoWarn.getValue());
	        		MiscUtils.getLogger().debug("demoLevel="+demoLevel);
	        		if(demoLevel>0)
	        			level = demoLevel;
	        	}
        }
        return level;
	}
	
	
}
