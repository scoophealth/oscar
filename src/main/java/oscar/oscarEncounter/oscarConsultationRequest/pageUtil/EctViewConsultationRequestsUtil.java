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


package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.ConsultationRequestDao;
import org.oscarehr.common.dao.ConsultationServiceDao;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class EctViewConsultationRequestsUtil {         
   
   public boolean estConsultationVecByTeam(LoggedInInfo loggedInInfo,String team) {   
      return estConsultationVecByTeam(loggedInInfo,team,false,null,null);
   }
   public boolean estConsultationVecByTeam(LoggedInInfo loggedInInfo,String team,boolean showCompleted) {   
      return estConsultationVecByTeam(loggedInInfo,team,showCompleted,null,null);
   }   
   public boolean estConsultationVecByTeam(LoggedInInfo loggedInInfo, String team,boolean showCompleted,Date startDate, Date endDate) {
      return estConsultationVecByTeam(loggedInInfo,team,showCompleted,null,null,null);
   }   
   public boolean estConsultationVecByTeam(LoggedInInfo loggedInInfo, String team,boolean showCompleted,Date startDate, Date endDate,String orderby) {   
      return estConsultationVecByTeam(loggedInInfo,team,showCompleted,null,null,null,null);
   }   
   public boolean estConsultationVecByTeam(LoggedInInfo loggedInInfo, String team,boolean showCompleted,Date startDate, Date endDate,String orderby,String desc) { 
      return estConsultationVecByTeam(loggedInInfo,team,showCompleted,null,null,null,null,null,null,null);
   }  
            
   public boolean estConsultationVecByTeam(LoggedInInfo loggedInInfo, String team,boolean showCompleted,Date startDate, Date endDate,String orderby,String desc,String searchDate, Integer offset, Integer limit) {       
      ids = new Vector<String>();
      status = new Vector<String>();
      patient = new Vector<String>();
      provider = new Vector<String>();
      providerNo = new Vector();
      teams = new Vector<String>();
      service = new Vector<String>();
      vSpecialist = new Vector<String>();
      urgency = new Vector<String>();
      date = new Vector<String>();
      demographicNo = new Vector<String>();
      siteName = new Vector<String>();
      this.patientWillBook = new Vector<String>();
      apptDate = new Vector<String>();
      followUpDate = new Vector<String>();
      boolean verdict = true;

      try {
          ConsultationRequestDao consultReqDao = (ConsultationRequestDao) SpringUtils.getBean("consultationRequestDao");
          DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
          ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
          ConsultationServiceDao serviceDao = (ConsultationServiceDao) SpringUtils.getBean("consultationServiceDao");
          ConsultationRequest consult;
          Demographic demo;
          Provider prov;
          ProfessionalSpecialist specialist;
          ConsultationServices services;
          Calendar cal = Calendar.getInstance();
          Date date1, date2;
          String providerId, providerName, specialistName;
          List consultList = consultReqDao.getConsults(team, showCompleted, startDate, endDate, orderby, desc, searchDate, offset, limit);

          for( int idx = 0; idx < consultList.size(); ++idx ) {
              consult = (ConsultationRequest)consultList.get(idx);
              demo = demographicManager.getDemographic(loggedInInfo, consult.getDemographicId());
              services = serviceDao.find(consult.getServiceId());

              providerId = demo.getProviderNo();
              if( providerId != null && !providerId.equals("")) {
                  prov = providerDao.getProvider(demo.getProviderNo());
                  providerName = prov.getFormattedName();
                  providerNo.add(prov.getProviderNo());
              }
              else {
                  providerName = "N/A";
                  providerNo.add("-1");
              }

              if( consult.getProfessionalSpecialist() == null ) {
                  specialistName = "N/A";
              }
              else {
                  specialist = consult.getProfessionalSpecialist();
                  specialistName = specialist.getLastName() + ", " + specialist.getFirstName();
              }

              demographicNo.add(consult.getDemographicId().toString());
              date.add(DateFormatUtils.ISO_DATE_FORMAT.format(consult.getReferralDate()));
              ids.add(consult.getId().toString());
              status.add(consult.getStatus());
              patient.add(demo.getFormattedName());
              provider.add(providerName);
              service.add(services.getServiceDesc());
              vSpecialist.add(specialistName);
              urgency.add(consult.getUrgency());
              siteName.add(consult.getSiteName());
              teams.add(consult.getSendTo());
              
              date1 = consult.getAppointmentDate();
              date2 = consult.getAppointmentTime();
              
              String apptDateStr = "";
              if( date1 == null ) {
            	  apptDateStr = "N/A";
              } else if( date1 != null && date2 == null ) {
            	  apptDateStr = DateFormatUtils.ISO_DATE_FORMAT.format(date1) + " T00:00:00";
              } else {
            	  apptDateStr = DateFormatUtils.ISO_DATE_FORMAT.format(date1) + " " +  DateFormatUtils.ISO_TIME_FORMAT.format(date2);
              }
              
              apptDate.add(apptDateStr);
              patientWillBook.add(""+consult.isPatientWillBook());
              
              date1 = consult.getFollowUpDate();
              if( date1 == null ) {
                  followUpDate.add("N/A");
              }
              else {
                followUpDate.add(DateFormatUtils.ISO_DATE_FORMAT.format(date1));
              }
          }
      } catch(Exception e) {            
         MiscUtils.getLogger().error("Error", e);            
         verdict = false;            
      }                     
      return verdict;      
   }      
   
      
   public boolean estConsultationVecByDemographic(LoggedInInfo loggedInInfo, String demoNo) {      
      ids = new Vector<String>();
      status = new Vector<String>();
      patient = new Vector<String>();
      provider = new Vector<String>();
      service = new Vector<String>();
      date = new Vector<String>();
      this.patientWillBook = new Vector<String>();
      urgency = new Vector<String>();
      apptDate = new Vector<String>();
      boolean verdict = true;      
      try {                           

          ConsultationRequestDao consultReqDao = (ConsultationRequestDao) SpringUtils.getBean("consultationRequestDao");

          ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
          DemographicManager demoManager = SpringUtils.getBean(DemographicManager.class);
          ConsultationServiceDao serviceDao = (ConsultationServiceDao) SpringUtils.getBean("consultationServiceDao");
          ConsultationRequest consult;
          Provider prov;
          Demographic demo;
          ConsultationServices services;
          String providerId, providerName;

          List consultList = consultReqDao.getConsults(Integer.parseInt(demoNo));
          for( int idx = 0; idx < consultList.size(); ++idx ) {
              consult = (ConsultationRequest)consultList.get(idx);
              demo = demoManager.getDemographic(loggedInInfo, consult.getDemographicId());
              providerId = demo.getProviderNo();
              if( providerId != null && !providerId.equals("")) {
              prov = providerDao.getProvider(demo.getProviderNo());
                providerName = prov.getFormattedName();
              }
              else {
                  providerName = "N/A";
              }

              services = serviceDao.find(consult.getServiceId());

              ids.add(consult.getId().toString());
              status.add(consult.getStatus());
              patient.add(demo.getFormattedName());
              provider.add(providerName);
              service.add(services.getServiceDesc());
              urgency.add(consult.getUrgency());
              patientWillBook.add(""+consult.isPatientWillBook());
              date.add(DateFormatUtils.ISO_DATE_FORMAT.format(consult.getReferralDate()));
          }
      } catch(Exception e) {         
         MiscUtils.getLogger().error("Error", e);         
         verdict = false;         
      }      
      return verdict;      
   }
   
      
   public Vector<String> ids;
   public Vector<String> status;
   public Vector<String> patient;
   public Vector<String> teams;
   public Vector<String> provider;
   public Vector<String> service;
   public Vector<String> vSpecialist;
   public Vector<String> date;
   public Vector<String> demographicNo;
   public Vector<String> apptDate;
   public Vector<String> patientWillBook;
   public Vector<String> urgency;
   public Vector<String> followUpDate;
   public Vector<String> providerNo;   
   public Vector<String> siteName;
   
}
