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


package org.oscarehr.provider.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.Property;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.QueueCache;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oscar.oscarPrevention.Prevention;
import oscar.oscarPrevention.PreventionDS;
import oscar.oscarPrevention.PreventionData;


/**
 *
 * @author rjonasz
 */
@Component
public class PreventionManager {
	private static Logger logger = MiscUtils.getLogger();
	private static final QueueCache<String, String> dataCache=new QueueCache<String, String>(4, 500, DateUtils.MILLIS_PER_HOUR, null);
	
    @Autowired
    private PreventionDS pf = null;
    
  

    public  String getWarnings(LoggedInInfo loggedInInfo, String demo) {
        String ret = dataCache.get(demo);
      
        if( ret == null ) {
                try {

                	Prevention prev = PreventionData.getLocalandRemotePreventions(loggedInInfo, Integer.parseInt(demo));
                    pf.getMessages(prev);
                    
                    @SuppressWarnings("unchecked")
                    Map<String,Object> m = prev.getWarningMsgs();
                    
                    @SuppressWarnings("rawtypes")
                    Set set = m.entrySet();
                    
                    @SuppressWarnings("rawtypes")
                    Iterator i = set.iterator();
                    // Display elements
                    String k="";
                    if(ret==null || ret.equals("null")){
                    	ret="";
                    }
                    
	                 while(i.hasNext()) {
	                 @SuppressWarnings("rawtypes")
	                 Map.Entry me = (Map.Entry)i.next();

	                 k="["+me.getKey()+"="+me.getValue()+"]";
	                 boolean prevCheck = PreventionManager.isPrevDisabled(me.getKey().toString());
	                 	if(prevCheck==false){
		                 ret=ret+k;
	                 	}

	                 } 	
                                         
	                 dataCache.put(demo, ret);

                } catch(Exception e) {
                    ret = "";
                    MiscUtils.getLogger().error("Error", e);
                }
            
        }
        
        return ret;
        
    }

     public void removePrevention(String demo) {
    	 dataCache.remove(demo);
     }

   

public static String checkNames(String k){
	String rebuilt="";
  	Pattern pattern = Pattern.compile("(\\[)(.*?)(\\])");
  	Matcher matcher = pattern.matcher(k);
  	
  	while(matcher.find()){
  		String[] key = matcher.group(2).split("=");
	  	boolean prevCheck = PreventionManager.isPrevDisabled(key[0]);
	  	
	  	if(prevCheck==false){
	  		rebuilt=rebuilt+"["+key[1]+"]";
	  	}
  	} 
  	
	return rebuilt;
}
     
     
public static boolean isDisabled(){
	String getStatus="";
	PropertyDao propDao = (PropertyDao)SpringUtils.getBean("propertyDao");
	List<Property> pList = propDao.findByName("hide_prevention_stop_signs"); 
	
  	Iterator<Property> i = pList.iterator();

  	while (i.hasNext()) {
  	Property item = i.next();
  	getStatus  = item.getValue();
  	
  	}
  	
  	//disable all preventions warnings if result is master
  	if(getStatus.equals("master")){
  		return true;
  	}else{
  		return false;
  	}
	
}


public static boolean isCreated(){
	String getStatus="";
	PropertyDao propDao = (PropertyDao)SpringUtils.getBean("propertyDao");
	List<Property> pList = propDao.findByName("hide_prevention_stop_signs"); 
	
  	Iterator<Property> i = pList.iterator();

  	while (i.hasNext()) {
  	Property item = i.next();
  	getStatus  = item.getName();
  	
  	}
  	
  	if(getStatus.equals("hide_prevention_stop_signs")){
  		return true;
  	}else{
  		return false;
  	}
	
}

public static boolean isPrevDisabled(String name){
	String getStatus="";
	PropertyDao propDao = (PropertyDao)SpringUtils.getBean("propertyDao");
	List<Property> pList = propDao.findByName("hide_prevention_stop_signs"); 
	
  	Iterator<Property> i = pList.iterator();

  	while (i.hasNext()) {
  	Property item = i.next();
  	getStatus  = item.getValue();
  	
  	}
  	
  	Pattern pattern = Pattern.compile("(\\[)(.*?)(\\])");
  	Matcher matcher = pattern.matcher(getStatus);
  	List<String> listMatches = new ArrayList<String>();

  	while(matcher.find()){

  	listMatches.add(matcher.group(2));

  	} 
  	
  	int x=0;
  	for(String s : listMatches){
		
        if(name.equals(s)){
        	x++;
        }
	
  	}
  	
  	if(x>0){
  		return true;
  	}else{
  		return false;
  	}
  	
  	
}


  	
}
