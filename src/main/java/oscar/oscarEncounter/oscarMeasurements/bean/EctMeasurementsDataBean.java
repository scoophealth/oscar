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


package oscar.oscarEncounter.oscarMeasurements.bean;

import java.util.Calendar;
import java.util.Date;

import org.oscarehr.util.MiscUtils;

public class EctMeasurementsDataBean{

       int id = 0;
       String type = "";
       String typeDisplayName = "";
       String typeDescription = "";
       String demo = "";
       String providerFirstName = "";
       String providerLastName = "";
       String dataField = "";
       String measuringInstrc = "";
       String comments = "";
       String dateObserved = "";
       String dateEntered = "";
       String canPlot = null;
       private Date dateObservedAsDate = null;
       private Date dateEnteredAsDate = null;
       private String indicationColour = null;
       String remoteFacility = null;
    
	public EctMeasurementsDataBean(){
       }

       public EctMeasurementsDataBean(int id, String type, String typeDisplayName, String typeDescription, String demo, String providerFirstName, String providerLastName, 
                                      String dataField, String measuringInstrc, String comments, String dateObserved, 
                                      String dateEntered, String canPlot){
            this.id = id;
            this.type = type;
            this.typeDisplayName = typeDisplayName;
            this.typeDescription = typeDescription;
            this.demo = demo;
            this.providerFirstName = providerFirstName;
            this.providerLastName = providerLastName;
            this.dataField = dataField;
            this.measuringInstrc = measuringInstrc;
            this.comments = comments;
            this.dateObserved = dateObserved;
            this.dateEntered = dateEntered;
            this.canPlot = canPlot;
       }
       
       public EctMeasurementsDataBean(int id, String type, String typeDisplayName, String typeDescription, String demo, String providerFirstName, String providerLastName, 
                                      String dataField, String measuringInstrc, String comments, String dateObserved, 
                                      String dateEntered, String canPlot, Date dateObservedAsDate, Date dateEnteredAsDate){
            this.id = id;
            this.type = type;
            this.typeDisplayName = typeDisplayName;
            this.typeDescription = typeDescription;
            this.demo = demo;
            this.providerFirstName = providerFirstName;
            this.providerLastName = providerLastName;
            this.dataField = dataField;
            this.measuringInstrc = measuringInstrc;
            this.comments = comments;
            this.dateObserved = dateObserved;
            this.dateEntered = dateEntered;
            this.canPlot = canPlot;
            this.setDateObservedAsDate(dateObservedAsDate);
            this.setDateEnteredAsDate(dateEnteredAsDate);
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
       public String getTypeDescription(){
           return typeDescription;
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
           //return dateEntered.substring(0,10);
           return dateEntered;
       }       
       public String getCanPlot(){           
           return canPlot;
       }
       
       public void setId(int id){
           this.id = id;
       }
       public void setType(String type){
           this.type = type;
       } 
       public void setTypeDisplayName(String typeDisplayName){
           this.typeDisplayName = typeDisplayName;
       }        
       public void setTypeDescription(String typeDescription){
           this.typeDescription = typeDescription;
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
       public void setCanPlot (String canPlot){
           this.canPlot = canPlot;
       }

    public Date getDateObservedAsDate() {
        return dateObservedAsDate;
    }

    public void setDateObservedAsDate(Date dateObservedAsDate) {
        this.dateObservedAsDate = dateObservedAsDate;
    }

    public Date getDateEnteredAsDate() {
        return dateEnteredAsDate;
    }

    public void setDateEnteredAsDate(Date dateEnteredAsDate) {
        this.dateEnteredAsDate = dateEnteredAsDate;
    }
    
    public String getRemoteFacility() {
    	return remoteFacility;
    }

	public void setRemoteFacility(String remoteFacility) {
    	this.remoteFacility = remoteFacility;
    }
    
    public int getNumMonthSinceObserved(){
        return getNumMonths(dateObservedAsDate,Calendar.getInstance().getTime());
    }
    public int getNumMonthsSinceObserved(Date d){
        return getNumMonths(dateObservedAsDate,d); 
    }
    
    private int getNumMonths(Date dStart, Date dEnd) {
        int i = 0;
        MiscUtils.getLogger().debug("Getting the number of months between "+dStart.toString()+ " and "+dEnd.toString() );        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dStart);
        while (calendar.getTime().before(dEnd) || calendar.getTime().equals(dEnd)) {
            calendar.add(Calendar.MONTH, 1);
            i++;
        }
        i--;
        if (i < 0) { i = 0; }
        return i;
   }

    public String getIndicationColour() {
        return indicationColour;
    }

    public void setIndicationColour(String indicationColour) {
        this.indicationColour = indicationColour;
    }
    
    
    public String toString(){
        return " id " +id+ " type: " +type+  
            " typeDisplayName: "+typeDisplayName+
            "typeDescription: "+typeDescription+
            "demo: "+demo+
            "providerFirstName: "+providerFirstName+
            "providerLastName: "+providerLastName +
            "dataField: "+dataField +
            "measuringInstrc: "+measuringInstrc +
            "comments: "+comments +
            "dateObserved: "+dateObserved +
            "dateEntered: "+dateEntered +
            "canPlot: "+canPlot  +
            "dateObservedAsDate: "+dateObservedAsDate  +
            "dateEnteredAsDate: "+dateEnteredAsDate  +
            "indicationColour: "+indicationColour  ;
    }
    
}
