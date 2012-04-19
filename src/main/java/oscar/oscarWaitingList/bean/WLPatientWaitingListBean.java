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


package oscar.oscarWaitingList.bean;


public class WLPatientWaitingListBean{

       String demographicNo;
       String waitingList;
       String waitingListID;
       String position;
       String onListSince;
       String phoneNumber;
       String patientName;
       String note;
       
       public WLPatientWaitingListBean(String demographicNo, String waitingListID, String waitingList, String position, String note, String onListSince){
           this.demographicNo=demographicNo;
           this.waitingListID=waitingListID;
           this.waitingList = waitingList;
           this.note = note;
           this.position = position;
           this.onListSince = onListSince;
       }
       
       public WLPatientWaitingListBean(String demographicNo, String waitingListID, String position, String patientName, String phoneNumber, String note, String onListSince){
           this.demographicNo = demographicNo;
           this.waitingListID=waitingListID;
           this.position=position;
           this.patientName=patientName;
           this.phoneNumber=phoneNumber;
           this.onListSince=onListSince;      
           this.note=note;
       }
       
       public String getDemographicNo(){
           return demographicNo;
       }
       
       public String getWaitingListID(){
           return waitingListID;
       }
       
       public String getWaitingList(){
           return waitingList;
       }
    
       public String getPosition(){
           return position;
       }
       
       public String getOnListSince(){
           return onListSince;
       }
       
       public String getPhoneNumber(){
           return phoneNumber;
       }
       
       public String getPatientName(){
           return patientName;
       }
       
       public String getNote(){
           return note;
       }
}
