/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * WorkFlowDSFactory.java
 *
 * Created on November 22, 2006, 11:47 AM
 *
 */

package oscar.oscarWorkflow;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import org.drools.RuleBase;
import org.drools.io.RuleBaseLoader;

import oscar.OscarProperties;

/**
 *
 * @author jay
 */
public class WorkFlowDSFactory {
    
    /** Creates a new instance of WorkFlowDSFactory */
    public WorkFlowDSFactory() {
    }
    
    public static WorkFlowDS getWorkFlowDS(String workflow){
        RuleBase ruleBase = null;
        ruleBase = loadRuleBase(workflow);
        return new WorkFlowDS(ruleBase);
    }
    
    
    public static RuleBase loadRuleBase(String string){
        RuleBase ruleBase = null;
        try{
            boolean fileFound = false;
            String workflowDirPath = OscarProperties.getInstance().getProperty("WORKFLOW_DS_DIRECTORY");
        
            if ( workflowDirPath != null){
            //if (measurementDirPath.charAt(measurementDirPath.length()) != /)
            File file = new File(OscarProperties.getInstance().getProperty("WORKFLOW_DS_DIRECTORY")+string);
               if(file.isFile() || file.canRead()) {
                   System.out.println("Loading from file "+file.getName());
                   FileInputStream fis = new FileInputStream(file);
                   ruleBase = RuleBaseLoader.loadFromInputStream(fis);
                   fileFound = true;
               }
            }
        
            if (!fileFound){  
                System.out.println("/oscar/oscarWorkFlow/rules/"+string);
                URL url = WorkFlowDSFactory.class.getResource( "/oscar/oscarWorkflow/rules/"+string );  //TODO: change this so it is configurable;
                System.out.println("is URL instantiated "+url);            
                System.out.println("loading from URL "+url.getFile());            
                ruleBase = RuleBaseLoader.loadFromUrl( url );
            }
        }catch(Exception e){
            e.printStackTrace();                
        }
        return ruleBase;             
   }
}
