/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * MeasurementFlowSheet.java
 *
 * Created on January 29, 2006, 7:59 PM
 *
 */

package oscar.oscarEncounter.oscarMeasurements;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.net.*;
import java.util.Hashtable;
import org.drools.*;
import org.drools.io.*;
import oscar.OscarProperties;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementTypesBean;



/**
 *
 * @author jay
 */
public class MeasurementFlowSheet {
    
    ArrayList list = null;
    String name = null;
    private String displayName = null;
    RuleBase ruleBase = null;
    boolean rulesLoaded = false;
    Hashtable measurementsInfo = null;
    Hashtable measurementsFlowSheetInfo = null;
    String demographic = null;
    String[] dxTriggers = null;
    private String warningColour = null;
    private String recommendationColour = null;
    
    public void parseDxTriggers(String s){
        dxTriggers = s.split(","); //TODO: what do about different coding systems.
    }
    
    public String[] getDxTriggers(){
        return dxTriggers;
    }
    
    
    
    /** Creates a new instance of MeasurementFlowSheet */
    public MeasurementFlowSheet() {
    }
    
    public void addMeasurement(String measurement){
        if (list == null){
           list = new ArrayList();    
        }
        list.add(measurement);
    }
    
    
    public void addMeasurementInfo(String measurement,Object obj){
        if ( measurementsInfo == null){
            measurementsInfo = new Hashtable();
        }
        measurementsInfo.put(measurement,obj);
    }
    
    public void addMeasurementFlowSheetInfo(String measurement,Object obj){
        if ( measurementsFlowSheetInfo == null){
            measurementsFlowSheetInfo = new Hashtable();
        }
        measurementsFlowSheetInfo.put(measurement,obj);
    }
    
    public Hashtable getMeasurementFlowSheetInfo(String measurement){
        if ( measurementsFlowSheetInfo == null){
            measurementsFlowSheetInfo = new Hashtable();
        }
        return (Hashtable) measurementsFlowSheetInfo.get(measurement); 
    }
    
    public EctMeasurementTypesBean getMeasurementInfo(String measurement){
        if ( measurementsInfo == null){
            measurementsInfo = new Hashtable();
        }
        return (EctMeasurementTypesBean) measurementsInfo.get(measurement); 
    }
    
    public ArrayList getMeasurementList(){
        return list;
    }
    
    
    
    
    public String getName(){
       return name;
    }
    public void setName(String s){
        name = s;
    }
    
    
    /////
	   	                   
      public void loadRuleBase(String string){
      try{
        boolean fileFound = false;
        String measurementDirPath = OscarProperties.getInstance().getProperty("MEASUREMENT_DS_DIRECTORY");
        
        if ( measurementDirPath != null){
        //if (measurementDirPath.charAt(measurementDirPath.length()) != /)
        File file = new File(OscarProperties.getInstance().getProperty("MEASUREMENT_DS_DIRECTORY")+string);
           if(file.isFile() || file.canRead()) {
               System.out.println("Loading from file "+file.getName());
               FileInputStream fis = new FileInputStream(file);
               ruleBase = RuleBaseLoader.loadFromInputStream(fis);
               fileFound = true;
           }
        }
        
        if (!fileFound){                  
         URL url = MeasurementFlowSheet.class.getResource( "/oscar/oscarEncounter/oscarMeasurements/flowsheets/"+string );  //TODO: change this so it is configurable;
         System.out.println("loading from URL "+url.getFile());            
         ruleBase = RuleBaseLoader.loadFromUrl( url );
        }
      }catch(Exception e){
         e.printStackTrace();                
      }
      rulesLoaded = true;             
   }
   
   
   public MeasurementInfo getMessages(MeasurementInfo mi) throws Exception{
      if (!rulesLoaded){
          throw new Exception("No Drools file loaded");
          //loadRuleBase();
      } 
      try{
         WorkingMemory workingMemory = ruleBase.newWorkingMemory();
         workingMemory.assertObject(mi);
         workingMemory.fireAllRules();
      }catch(Exception e){
          e.printStackTrace(); 
          //throw new Exception("ERROR: Drools ",e);
      }
      return mi;   
   }
    /////

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getWarningColour() {
        return warningColour;
    }

    public void setWarningColour(String warningColour) {
        this.warningColour = warningColour;
    }

    public String getRecommendationColour() {
        return recommendationColour;
    }

    public void setRecommendationColour(String recommendationColour) {
        this.recommendationColour = recommendationColour;
    }

  
    
}
