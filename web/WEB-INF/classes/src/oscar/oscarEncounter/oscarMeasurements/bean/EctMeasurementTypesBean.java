// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.oscarMeasurements.bean;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import oscar.oscarDB.DBHandler;

public class EctMeasurementTypesBean{

       int id;
       String type;
       String typeDisplayName;
       String typeDesc;
       String measuringInstrc;
       String validation;
       String lastProviderFirstName = null;
       String lastProviderLastName = null;  
       String lastData = null;       
       String lastMInstrc = null;    
       String lastComments = null;   
       String lastDateObserved =null;
       String lastDateEntered = null;
       
       public EctMeasurementTypesBean(int id, String type, String typeDisplayName, String typeDesc, String measuringInstrc, String validation){
           this.id = id;
           this.type = type;
           this.typeDisplayName = typeDisplayName;
           this.typeDesc = typeDesc;
           this.measuringInstrc = measuringInstrc;
           this.validation = validation;
       }

       public EctMeasurementTypesBean( int id, String type, String typeDisplayName, String typeDesc, String measuringInstrc, 
                                       String validation, String lastProviderFirstName,
                                       String lastProviderLastName,  
                                       String lastData,       
                                       String lastMInstrc,    
                                       String lastComments,   
                                       String lastDateObserved,
                                       String lastDateEntered){
           this.id = id;
           this.type = type;
           this.typeDisplayName = typeDisplayName;
           this.typeDesc = typeDesc;
           this.measuringInstrc = measuringInstrc;
           this.validation = validation;
           this.lastProviderFirstName = lastProviderFirstName;
           this.lastProviderLastName = lastProviderLastName;
           this.lastData = lastData;
           this.lastMInstrc = lastMInstrc;
           this.lastComments = lastComments;
           this.lastDateObserved = lastDateObserved;
           this.lastDateEntered = lastDateEntered;       
       }
       
       public int getId(){
           return id;
       }
       public String getType(){
           return type;
       }       
       public String getTypeDisplayName(){
           return typeDisplayName;
       }
    
       public String getTypeDesc(){
           return typeDesc;
       }
       
       public String getMeasuringInstrc(){
           return measuringInstrc;
       }
       
       public String getValidation(){
           return validation;
       }
       
       public String getLastProviderFirstName(){
           return lastProviderFirstName;
       }
       
       public String getLastProviderLastName(){
           return lastProviderLastName;  
       }
       
       public String getLastData(){
           return lastData;       
       }
       
       public String getLastMInstrc(){
           return lastMInstrc;    
       }
       
       public String getLastComments(){
           return lastComments;   
       }
       
       public String getLastDateObserved(){
           return lastDateObserved;
       }
       
       public String getLastDateEntered(){
           return lastDateEntered.substring(0,10);
       }
       
}
