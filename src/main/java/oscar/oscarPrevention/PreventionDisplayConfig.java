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


package oscar.oscarPrevention;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarDemographic.data.DemographicData;

public class PreventionDisplayConfig {
    private static Logger log = MiscUtils.getLogger();
    static PreventionDisplayConfig preventionDisplayConfig = new PreventionDisplayConfig();
   
    HashMap<String,HashMap<String,String>> prevHash = null;
    ArrayList<HashMap<String,String>> prevList = null;

    Hashtable<String,Map<String,Object>> configHash = null;
    ArrayList<Map<String,Object>> configList = null;

    private PreventionDisplayConfig() {
    	// use getInstance()
    }
    
    static public PreventionDisplayConfig getInstance(){
       if (preventionDisplayConfig.prevList == null) {
         preventionDisplayConfig.loadPreventions();
       }
       return preventionDisplayConfig;
    }

    public ArrayList<HashMap<String,String>> getPreventions() {
        if (prevList == null) {
            loadPreventions();
        }
        return prevList;
    }

    public HashMap<String,String> getPrevention(String s) {
        if (prevHash == null) {
            loadPreventions();
        }
        log.debug("getting " + s);
        return prevHash.get(s);
    }
    
    public void loadPreventions() {
      prevList = new ArrayList<HashMap<String,String>>();
      prevHash = new HashMap<String,HashMap<String,String>>(); 
      log.debug("STARTING2");
      
      try{
         InputStream is = this.getClass().getClassLoader().getResourceAsStream("oscar/oscarPrevention/PreventionItems.xml");               
         
         if (OscarProperties.getInstance().getProperty("PREVENTION_ITEMS") != null){
            String filename = OscarProperties.getInstance().getProperty("PREVENTION_ITEMS");
            is = new FileInputStream(filename) ;                                                
         }
                  
         SAXBuilder parser = new SAXBuilder();
         Document doc = parser.build(is);        
         Element root = doc.getRootElement();                  
         List items = root.getChildren("item");                  
         for (int i = 0; i < items.size(); i++){           
            Element e = (Element) items.get(i);
            List attr = e.getAttributes();
            HashMap<String,String> h = new HashMap<String,String>();
            for (int j = 0; j < attr.size(); j++){           
               Attribute att = (Attribute) attr.get(j);
               h.put(att.getName(),att.getValue() );
            }            
            prevList.add(h);            
            prevHash.put(h.get("name"), h);            
         }                
      }catch(Exception e ){
         MiscUtils.getLogger().error("Error", e);
      }
   }


    public ArrayList<Map<String,Object>> getConfigurationSets() {
        log.debug("returning config sets");
        if (configList == null) {
            loadConfigurationSets();
        }
        log.debug("returning config sets" + configList);
        return configList;
    }

   
    public void loadConfigurationSets() {
       getPreventions();
       configHash = new Hashtable<String,Map<String,Object>>();
       configList = new ArrayList<Map<String,Object>>();
       try{
         InputStream is = this.getClass().getClassLoader().getResourceAsStream("oscar/oscarPrevention/PreventionConfigSets.xml");               
         if (OscarProperties.getInstance().getProperty("PREVENTION_CONFIG_SETS") != null){
            String filename = OscarProperties.getInstance().getProperty("PREVENTION_CONFIG_SETS");
            is = new FileInputStream(filename) ;                                                
         }
         SAXBuilder parser = new SAXBuilder();
         Document doc = parser.build(is);        
         Element root = doc.getRootElement();                  
         List items = root.getChildren("set");                  
         for (int i = 0; i < items.size(); i++){           
            Element e = (Element) items.get(i);
            List attr = e.getAttributes();
            Map<String,Object> h = new HashMap<String,Object>();
            for (int j = 0; j < attr.size(); j++){           
               Attribute att = (Attribute) attr.get(j);
               if(att.getName().equals("prevList")){
                  h.put(att.getName(),att.getValue().split(","));
               }else{
                  h.put(att.getName(),att.getValue() );
               }
                              
            }                        
            configList.add(h);        
            configHash.put((String)h.get("title"), h);            
         }                
      }catch(Exception e ){
         MiscUtils.getLogger().error("Error", e);
      }
       
    }


    public String getDisplay(Map<String,Object> setHash, String Demographic_no) {
        String display = "style=\"display:none;\"";
        DemographicData dData = new DemographicData();
        log.debug("demoage " + Demographic_no);
        org.oscarehr.common.model.Demographic demograph = dData.getDemographic(Demographic_no);
        try {
            String minAgeStr = (String) setHash.get("minAge");
            String maxAgeStr = (String) setHash.get("maxAge");
            String sex = (String) setHash.get("sex");
            int demoAge = demograph.getAgeInYears();
            String demoSex = demograph.getSex();
            boolean inAgeGroup = true;
            log.debug("min age " + minAgeStr + " max age " + maxAgeStr + " sex " + sex + " demoAge " + demoAge
                    + " demoSex " + demoSex);
            if (minAgeStr != null && maxAgeStr != null) { // between ages
                log.debug("HERE1");
                int minAge = Integer.parseInt(minAgeStr);
                int maxAge = Integer.parseInt(maxAgeStr);
                if (minAge <= demoAge && maxAge >= demoAge) {
                    display = "";
                } else {
                    inAgeGroup = false;
                }
            } else if (minAgeStr != null) { // older than
                log.debug("HERE2");
                int minAge = Integer.parseInt(minAgeStr);
                if (minAge <= demoAge) {
                    display = "";
                } else {
                    inAgeGroup = false;
                }
            } else if (maxAgeStr != null) { // younger than
                log.debug("HERE3");
                int maxAge = Integer.parseInt(maxAgeStr);
                if (maxAge >= demoAge) {
                    display = "";
                } else {
                    inAgeGroup = false;
                }
            }// else? neither defined should the default be to display it or
                // not?

            if (sex != null && inAgeGroup) {
                log.debug("HERE4");
                if (sex.equals(demoSex)) {
                    display = "";
                } else {
                    display = "style=\"display:none;\"";
                }
            }
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return display;
    }

    public boolean display(Map<String,String> setHash, String Demographic_no,int numberOfPrevs) {
        boolean display = false;
        DemographicData dData = new DemographicData();
        log.debug("demoage " + Demographic_no);
        org.oscarehr.common.model.Demographic demograph = dData.getDemographic(Demographic_no);
        try {
            String minAgeStr = setHash.get("minAge");
            String maxAgeStr = setHash.get("maxAge");
            String sex = setHash.get("sex");
            String minNumPrevs = setHash.get("showIfMinRecordNum");
            int demoAge = demograph.getAgeInYears();
            String demoSex = demograph.getSex();
            boolean inAgeGroup = true;
            //log.debug("min age " + minAgeStr + " max age " + maxAgeStr + " sex " + sex + " demoAge " + demoAge
            //        + " demoSex " + demoSex);
            
            if (minNumPrevs != null){
               int minNum = Integer.parseInt(minNumPrevs);
               if (numberOfPrevs >= minNum){
                  display = true;
               }
            }
            
            if(!display){
            
               if (minAgeStr != null && maxAgeStr != null) { // between ages
                   //log.debug("HERE1");
                   int minAge = Integer.parseInt(minAgeStr);
                   int maxAge = Integer.parseInt(maxAgeStr);
                   if (minAge <= demoAge && maxAge >= demoAge) {
                       display = true;
                   } else {
                       inAgeGroup = false;
                   }
               } else if (minAgeStr != null) { // older than
                   //log.debug("HERE2");
                   int minAge = Integer.parseInt(minAgeStr);
                   if (minAge <= demoAge) {
                       display = true;
                   } else {
                       inAgeGroup = false;
                   }
               } else if (maxAgeStr != null) { // younger than
                   //log.debug("HERE3");
                   int maxAge = Integer.parseInt(maxAgeStr);
                   if (maxAge >= demoAge) {
                       display = true;
                   } else {
                       inAgeGroup = false;
                   }
               }// else? neither defined should the default be to display it or
                   // not?
            
               if (sex != null && inAgeGroup) {
                   //log.debug("HERE4");
                   if (sex.equals(demoSex)) {
                       display = true;
                   } else {
                       display = false;
                   }
               }
            }
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return display;
    }
}
