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
package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import oscar.oscarDB.DBHandler;

public class EctMeasurementsDataBean{

       int id;
       String type;
       String demo;
       String providerFirstName;
       String providerLastName;
       String dataField;
       String measuringInstrc;
       String comments;
       String dateObserved;
       String dateEntered;
       
       public EctMeasurementsDataBean(){
       }

       public EctMeasurementsDataBean(int id, String type, String demo, String providerFirstName, String providerLastName, String dataField, String measuringInstrc, String comments, String dateObserved, String dateEntered){
            this.id = id;
            this.type = type;
            this.demo = demo;
            this.providerFirstName = providerFirstName;
            this.providerLastName = providerLastName;
            this.dataField = dataField;
            this.measuringInstrc = measuringInstrc;
            this.comments = comments;
            this.dateObserved = dateObserved;
            this.dateEntered = dateEntered;
       }

       public int getId(){
           return id;
       }
       public String getType(){
           return type;
       }
       public String getDemo(){
           return demo;
       }
       public String getProviderFirstName(){
           return providerFirstName;
       }
       public String getProviderLastName(){
           return providerLastName;
       }
       public String getDataField(){           
           return dataField;
       }       
       public String getMeasuringInstrc(){
           return measuringInstrc;
       }
       public String getComments(){
           return comments;
       }
       public String getDateObserved(){
           //Display the date only (not time)
           return dateObserved.substring(0,10);
       }
       public String getDateEntered(){
           return dateEntered.substring(0,10);
       }       

       public void setId(int id){
           this.id = id;
       }
       public void setType(String type){
           this.type = type;
       }      
       public void setDemo(String demo){
           this.demo = demo;
       }
       public void setProviderFirstName(String providerFirstName){
           this.providerFirstName = providerFirstName;
       }      
       public void setProviderLastName(String providerLastName){
           this.providerLastName = providerLastName;
       }
       public void setDataField(String dataField){
           this.dataField = dataField;
       }
       public void setMeasuringInstrc(String measuringInstrc){
           this.measuringInstrc = measuringInstrc;
       }      
       public void setComments(String comments){
           this.comments = comments;
       }
       public void setDateObserved(String dateObserved){
           this.dateObserved = dateObserved;
       }      
       public void setDateEntered(String dateEntered){
           this.dateEntered = dateEntered;
       }
}
