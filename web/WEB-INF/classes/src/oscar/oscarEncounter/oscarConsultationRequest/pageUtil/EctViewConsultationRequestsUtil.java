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
// * McMaster University
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------

package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class EctViewConsultationRequestsUtil {         
   
   public boolean estConsultationVecByTeam(String team) {   
      return estConsultationVecByTeam(team,false,null,null);
   }
   public boolean estConsultationVecByTeam(String team,boolean showCompleted) {   
      return estConsultationVecByTeam(team,showCompleted,null,null);
   }   
   public boolean estConsultationVecByTeam(String team,boolean showCompleted,Date startDate, Date endDate) {
      return estConsultationVecByTeam(team,showCompleted,null,null,null);
   }   
   public boolean estConsultationVecByTeam(String team,boolean showCompleted,Date startDate, Date endDate,String orderby) {   
      return estConsultationVecByTeam(team,showCompleted,null,null,null,null);
   }   
   public boolean estConsultationVecByTeam(String team,boolean showCompleted,Date startDate, Date endDate,String orderby,String desc) { 
      return estConsultationVecByTeam(team,showCompleted,null,null,null,null,null);
   }  
            
   public boolean estConsultationVecByTeam(String team,boolean showCompleted,Date startDate, Date endDate,String orderby,String desc,String searchDate) {       
      ids = new Vector();
      status = new Vector();
      patient = new Vector();
      provider = new Vector();
      service = new Vector();
      urgency = new Vector();
      date = new Vector();
      demographicNo = new Vector();
      this.patientWillBook = new Vector();
      apptDate = new Vector();
      boolean verdict = true;
            
      StringBuffer sql = new StringBuffer();
      sql.append(" select demo.demographic_no, cr.status, cr.referalDate, cr.requestId,  cr.patientWillBook, cr.urgency, cr.appointmentDate, cr.appointmentTime, demo.last_name, demo.first_name,  pro.last_name as lName, pro.first_name as fName, ser.serviceDesc ");
      sql.append("from consultationRequests cr,  demographic demo, provider pro, consultationServices ser ");
      sql.append("where  demo.demographic_no = cr.demographicNo and pro.provider_no = cr.providerNo and  ser.serviceId = cr.serviceId ");
            
      if(!showCompleted){         
         sql.append("and cr.status != 4 "); //don't show compleded
      }
      
      if(!team.equals("-1")) {
         sql.append("and sendTo ='" +team+ "' ");
      }
      
      if(startDate != null){
         if (searchDate != null && searchDate.equals("1")){
            sql.append("and appointmentDate >= '" +UtilDateUtilities.DateToString(startDate)+ "' ");            
         }else{
            sql.append("and referalDate >= '" +UtilDateUtilities.DateToString(startDate)+ "' ");
         }        
      }
      
      if(endDate != null){
         if (searchDate != null && searchDate.equals("1")){
            sql.append("and appointmentDate <= '" +UtilDateUtilities.DateToString(endDate)+ "' ");            
         }else{
            sql.append("and referalDate <= '" +UtilDateUtilities.DateToString(endDate)+ "' ");
         }
      }
                  
      if (orderby == null){
         sql.append("order by cr.referalDate desc ");            
      }else if(orderby.equals("1")){               //1 = msgStatus
         sql.append("order by cr.status ");            
      }else if(orderby.equals("2")){               //2 = msgPatient
         sql.append("order by demo.last_name ");  
      }else if(orderby.equals("3")){               //3 = msgProvider
         sql.append("order by pro.last_name ");   
      }else if(orderby.equals("4")){               //4 = msgService
         sql.append("order by ser.serviceDesc");            
      }else if(orderby.equals("5")){               //5 = msgRefDate
         sql.append("order by cr.referalDate");            
      }else if(orderby.equals("6")){               //6 = Appointment Date
         sql.append("order by cr.appointmentDate");            
      }else{
         sql.append("order by cr.referalDate desc");            
      }            
      
      if (desc != null && desc.equals("1")){
         sql.append(" desc");            
      }            
      
      if(orderby != null && orderby.equals("2")){  //HACK to make order by "desc" to work on lastname
         sql.append(" , demo.first_name");
      }else if( orderby != null && orderby.equals("3")){
         sql.append(" , pro.first_name");
      }                        
                     
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs= db.GetSQL(sql.toString());
         while(rs.next()) {
            demographicNo.add(db.getString(rs,"demographic_no"));
            date.add(db.getString(rs,"referalDate"));               
            ids.add(db.getString(rs,"requestId"));               
            status.add(db.getString(rs,"status"));
            patient.add(db.getString(rs,"last_name") +", "+ db.getString(rs,"first_name")) ;
            provider.add(db.getString(rs,"lName") +", "+ db.getString(rs,"fName"));
            service.add(db.getString(rs,"serviceDesc"));
            urgency.add(db.getString(rs,"urgency"));
            apptDate.add(db.getString(rs,"appointmentDate")+" "+db.getString(rs,"appointmentTime"));
            this.patientWillBook.add(db.getString(rs,"patientWillBook"));
         }            
         rs.close();            
      } catch(SQLException e) {            
         System.out.println(e.getMessage());            
         verdict = false;            
      }                     
      return verdict;      
   }      
   
      
   public boolean estConsultationVecByDemographic(String demoNo) {      
      ids = new Vector();      
      status = new Vector();      
      patient = new Vector();      
      provider = new Vector();      
      service = new Vector();      
      date = new Vector();
      this.patientWillBook = new Vector();     
      urgency = new Vector();
      apptDate = new Vector();
      boolean verdict = true;      
      try {         
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);         
         String sql = " select cr.status, cr.referalDate, cr.requestId, cr.patientWillBook, cr.urgency, demo.last_name, demo.first_name,  pro.last_name as lName, pro.first_name as fName, ser.serviceDesc from consultationRequests cr,  demographic demo, provider pro, consultationServices ser where  demo.demographic_no = cr.demographicNo and pro.provider_no = cr.providerNo and  ser.serviceId = cr.serviceId and demographicNo ='"+demoNo+"' order by cr.referalDate ";         
         ResultSet rs;         
         for(rs = db.GetSQL(sql); rs.next(); date.add(db.getString(rs,"referalDate"))){            
            ids.add(db.getString(rs,"requestId"));            
            status.add(db.getString(rs,"status"));            
            patient.add(db.getString(rs,"last_name")+", "+db.getString(rs,"first_name"));            
            provider.add(db.getString(rs,"lName")+", "+db.getString(rs,"fName"));            
            service.add(db.getString(rs,"serviceDesc"));
            urgency.add(db.getString(rs,"urgency"));
            
            patientWillBook.add(db.getString(rs,"patientWillBook"));            
         }                  
         rs.close();         
      } catch(SQLException e) {         
         System.out.println(e.getMessage());         
         verdict = false;         
      }      
      return verdict;      
   }
   
      
   public Vector ids;   
   public Vector status;   
   public Vector patient;   
   public Vector provider;   
   public Vector service;   
   public Vector date;   
   public Vector demographicNo;
   public Vector apptDate;
   public Vector patientWillBook;
   public Vector urgency;
   
}
