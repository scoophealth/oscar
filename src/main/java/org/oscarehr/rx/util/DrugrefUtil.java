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
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
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
    
    
    public String getEvidence(ResourceBundle mr,String str) {
    		if(str == null) {
    			return mr.getString("oscarRx.MyDrugref.InteractingDrugs.evidence.Unknown");
    		}
        switch(str) {
        case "P":
            	return mr.getString("oscarRx.MyDrugref.InteractingDrugs.evidence.Poor");
        case "F":
        		return mr.getString("oscarRx.MyDrugref.InteractingDrugs.evidence.Fair");
        case "G":
    			return mr.getString("oscarRx.MyDrugref.InteractingDrugs.evidence.Good");
		default:
				return mr.getString("oscarRx.MyDrugref.InteractingDrugs.evidence.Unknown");
        }
    }
    
    public List<RxDsMessageTo1> convertLocalDS(List list,ResourceBundle mr,Locale locale, List<RxDsMessageTo1> returnList,Map hiddenList){
    		List<String> currentIdWarnings=new ArrayList<String>();
    		try{
            for(int i=0;i<list.size();i++){
                Map ht=(Map)list.get(i);
                
                //////
                Date dt=(Date)ht.get("updated_at");
	            Long time=dt.getTime();
	            String idWarning=ht.get("id")+"."+time;
	            if(!currentIdWarnings.contains(idWarning)){
	                currentIdWarnings.add(idWarning);
	                RxDsMessageTo1 dsMessage = new RxDsMessageTo1();
	                dsMessage.setMessageSource("MediSpan");
	                dsMessage.setName((String) ht.get("name"));
	                dsMessage.setUpdated_at((Date)ht.get("updated_at"));
	                String id = null;
	                MiscUtils.getLogger().error("WHATS inn local ht"+ht);
		        		if(ht.get("id") instanceof String) {
		        			id = (String) ht.get("id");
		        		}else if(ht.get("id") instanceof Integer) {
		        			id = ((Integer) ht.get("id")).toString();
		        		}
		        		dsMessage.setId(id);
		        		String significanceStr = (String)ht.get("significance");
		        		int significance = 0;
		        		try {
		        			significance = Integer.valueOf(significanceStr);
		        		}catch(Exception e) {
		        			significance = 0;
		        		}
	        		
	        		
		        		if(hiddenList != null && hiddenList.containsKey("medispan"+dsMessage.getId())) {
	                		logger.error("idWARNING medispan"+dsMessage.getId()+"  time value in warning "+hiddenList.get("medispan"+dsMessage.getId())+" medispan update time "+dsMessage.getUpdated_at().getTime());
	                		//if(hiddenList.get("mydrugref"+dsMessage.getId()).equals(dsMessage.getUpdated_at().getTime())) {
	                			dsMessage.setHidden(true);
	                		//}
	                }else {
	                		logger.error("hidden list was empty or didn't contain :medispan"+dsMessage.getId()+"<"+hiddenList);
	                }
		        	
		        	
		        		dsMessage.setEffect((String)ht.get("effectdesc"));
		        		dsMessage.setManagement((String)ht.get("management"));
		        		
		        		dsMessage.setType((String) ht.get("type"));
		        		// need to set by type dsMessage.setSummary(interactStr);
		        		dsMessage.setAuthor((String) ht.get("author"));
		        		dsMessage.setUpdated_by((Integer) ht.get("updated_by"));
		        		dsMessage.setAtc((String) ht.get("atc"));
		        		dsMessage.setAtc2((String) ht.get("atc2"));	            
	            		dsMessage.setCreated_by((Integer) ht.get("created_by"));	       
	            		dsMessage.setReference((String) ht.get("reference"));	 
	            		String bodyMarkDown = (String) ht.get("bodyMarkdown");
	            		Parser parser = Parser.builder().build();
	            		Node document = parser.parse(bodyMarkDown);
	            	    HtmlRenderer renderer = HtmlRenderer.builder().build();
	            		String bodyHtml = renderer.render(document); 
	            		
	            	    dsMessage.setBody(bodyHtml);	       
	            		dsMessage.setDrug2((String) ht.get("drug2"));
	            		dsMessage.setCreated_at((Date) ht.get("created_at"));	       
	            		dsMessage.setTrustedResource(true);
	            		dsMessage.setAgree((Boolean) ht.get("agree"));
	            		dsMessage.setEvidence((String) ht.get("evidence"));

	            		
	          
	            		
		        			
		        			dsMessage.setSummary(dsMessage.getName());
		        
	             
	         
	                 
	                
	                
	                returnList.add(dsMessage);

	            }
            }
        }catch(NullPointerException npe){
        		logger.error("Error", npe);
        }
    	
    		return returnList;
    }
	
    public List<RxDsMessageTo1> convertMyDrugrefDS(List list,ResourceBundle mr,Locale locale, List<RxDsMessageTo1> returnList,Map hiddenList){
    		List<String> currentIdWarnings=new ArrayList<String>();
        try{
            for(int i=0;i<list.size();i++){
                Map ht=(Map)list.get(i);
                
                //////
                Date dt=(Date)ht.get("updated_at");
	            Long time=dt.getTime();
	            String idWarning=ht.get("id")+"."+time;
	            if(!currentIdWarnings.contains(idWarning)){
	                currentIdWarnings.add(idWarning);
	                
	                RxDsMessageTo1 dsMessage = new RxDsMessageTo1();
	                dsMessage.setMessageSource("K2A");
	                dsMessage.setName((String) ht.get("name"));
	                dsMessage.setUpdated_at((Date)ht.get("updated_at"));
	                          
	                String id = null;
	                logger.error("WHATS inn drugref ht"+ht);
		        		if(ht.get("id") instanceof String) {
		        			id = (String) ht.get("id");
		        		}else if(ht.get("id") instanceof Integer) {
		        			id = ((Integer) ht.get("id")).toString();
		        		}
		        		dsMessage.setId(id);
		        		
		        		logger.info("hiddenlist size "+hiddenList.size());
		        		for(Object s: hiddenList.keySet()) {
		        			logger.info("keys: "+s+" looking for :"+"mydrugref"+dsMessage.getId()+" found "+hiddenList.containsKey("mydrugref"+dsMessage.getId()));
		        		}
		        		
		        		if(hiddenList != null && hiddenList.containsKey("mydrugref"+dsMessage.getId())) {
	                		logger.error("idWARNING mydrugref"+dsMessage.getId()+"  time value in warning "+hiddenList.get("mydrugref"+dsMessage.getId())+" mydrugref update time "+dsMessage.getUpdated_at().getTime());
	                		//if(hiddenList.get("mydrugref"+dsMessage.getId()).equals(dsMessage.getUpdated_at().getTime())) {
	                			dsMessage.setHidden(true);
	                		//}
	                }else {
	                		logger.error("hidden list was empty or didn't contain :mydrugref"+dsMessage.getId()+"<"+hiddenList);
	                }
	                              
		        		
		        		String significanceStr = (String)ht.get("significance");
		        		int significance = 0;
		        		try {
		        			significance = Integer.valueOf(significanceStr);
		        		}catch(Exception e) {
		        			significance = 0;
		        		}
	        		    dsMessage.setSignificance(significance);
	        		
		        		String effect=(String)ht.get("effect");
		        		String effectStr=(String)ht.get("effect");
		        		String interactStr = "";
		        		if(effect!=null){
		        			if(effect.equals("a"))
		        				effect=mr.getString("oscarRx.interactions.msgAugmentsNoClinical");
		        			else if(effect.equals("A"))
		        				effect=mr.getString("oscarRx.interactions.msgAugments");
		        			else if(effect.equals("i"))
		        				effect=mr.getString("oscarRx.interactions.msgInhibitsNoClinical");
		        			else if(effect.equals("I"))
		        				effect=mr.getString("oscarRx.interactions.msgInhibits");
		        			else if(effect.equals("n"))
		        				effect=mr.getString("oscarRx.interactions.msgNoEffect");
		        			else if(effect.equals("N"))
		        				effect=mr.getString("oscarRx.interactions.msgNoEffect");
		        			else if(effect.equals(" "))
		        				effect=mr.getString("oscarRx.interactions.msgUnknownEffect");
		        				interactStr=ht.get("name")+" "+effect+" "+ht.get("drug2");
		        		}
		        		dsMessage.setInteractStr(interactStr);
		        		dsMessage.setEffect(effect);
		        		dsMessage.setType((String) ht.get("type"));
		        		// need to set by type dsMessage.setSummary(interactStr);
		        		dsMessage.setAuthor((String) ht.get("author"));
		        		dsMessage.setUpdated_by((Integer) ht.get("updated_by"));
		        		dsMessage.setAtc((String) ht.get("atc"));
		        		dsMessage.setAtc2((String) ht.get("atc2"));	            
	            		dsMessage.setCreated_by((Integer) ht.get("created_by"));	       
	            		dsMessage.setReference((String) ht.get("reference"));
	            		dsMessage.setEvidence((String) ht.get("evidence"));
	            	    dsMessage.setBody((String) ht.get("body")+"\n\n Evidence:"+getEvidence(mr,dsMessage.getEvidence()));	       
	            		dsMessage.setDrug2((String) ht.get("drug2"));
	            		dsMessage.setCreated_at((Date) ht.get("created_at"));	       
	            		dsMessage.setTrustedResource((Boolean) ht.get("trusted"));
	            		dsMessage.setAgree((Boolean) ht.get("agree"));
	            		

	            		
	            		if(dsMessage.getType() != null && "Bulletin".equals(dsMessage.getType())) {
		        			dsMessage.setHeading(dsMessage.getName());
		        			dsMessage.setSummary(dsMessage.getBody());
		        		}
	            		
	            		if(dsMessage.getType() != null && "Interaction".equals(dsMessage.getType())) {
		        			dsMessage.setHeading(dsMessage.getInteractStr());
		        			//dsMessage.setSummary(dsMessage.getBody());
		        		}
	            		
	            		if(dsMessage.getType() != null && "Warning".equals(dsMessage.getType())) {
		        			dsMessage.setHeading(dsMessage.getName());
		        			dsMessage.setSummary((String) ht.get("body"));
		        		}
	             
	         
	                 
	                
	                
	                returnList.add(dsMessage);

	            }
            }
        }catch(NullPointerException npe){
            MiscUtils.getLogger().error("Error", npe);
        }
    	
    		return returnList;
    }
    
    
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


        for (String command : str){
            try{
                List v = rxMyDrugrefInfoAction.getMyDrugrefInfo(loggedInInfo,command, codes, provider, myDrugrefId);

                if (v !=null && v.size() > 0){
                		convertMyDrugrefDS(v, mr, locale,  returnList,dsPrefs ); 
                		logger.error("size after mydrugref "+returnList.size());
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
			        	Map<String,Long> dsLocalPrefs= dsmessageDao.getHashofMessages(provider,UserDSMessagePrefs.MEDISPAN);
			        	convertLocalDS( localInteractions, mr, locale,  returnList,dsLocalPrefs);
			        	logger.error("size after local interactions "+returnList.size());
	
		        }catch(Exception e){
		        		logger.error("Error calling interactions by regional identifier",e);
		        }
	        }else{
	        	logger.error("RX_INTERACTION_LOCAL_DRUGREF_REGIONAL_IDENTIFIER was null" );
	        }
	        
	        
	       // Collections.sort(all, new MyDrugrefComparator());
	        
	     
	        
/*

	        MiscUtils.getLogger().error(returnList);
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
	     */   
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

    
   
    //List regionalIdentifiers = bean.getRegionalIdentifier();
	public  List<RxDsMessageTo1> getMessages2(LoggedInInfo loggedInInfo,String provider,int demographicNo,List<String> atcCodes,List regionalIdentifiers,Locale locale ) throws Exception {
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
