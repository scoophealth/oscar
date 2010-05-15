<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<% 
 

 rslocation = null;
 rslocation = apptMainBean.queryResults(billNo, "search_bill");
 while(rslocation.next()){
 DemoNo = rslocation.getString("demographic_no");
 DemoName = rslocation.getString("demographic_name");
 UpdateDate = rslocation.getString("update_date");
 // hin = rslocation.getString("hin");
 location = rslocation.getString("clinic_ref_code");
 // DemoDOB = rslocation.getString("dob");
 BillDate = rslocation.getString("billing_date");
 BillType = rslocation.getString("status");
 Provider = rslocation.getString("provider_no");
  BillTotal = rslocation.getString("total");
  visitdate = rslocation.getString("visitdate");
  visittype = rslocation.getString("visittype");
 r_status= SxmlMisc.getXmlContent(rslocation.getString("content"),"<xml_referral>","</xml_referral>")==null?"":SxmlMisc.getXmlContent(rslocation.getString("content"),"<xml_referral>","</xml_referral>");
// HCTYPE= SxmlMisc.getXmlContent(rslocation.getString("content"),"<hctype>","</hctype>")==null?"":SxmlMisc.getXmlContent(rslocation.getString("content"),"<hctype>","</hctype>");
// HCSex= SxmlMisc.getXmlContent(rslocation.getString("content"),"<demosex>","</demosex>")==null?"":SxmlMisc.getXmlContent(rslocation.getString("content"),"<demosex>","</demosex>");
 m_review = SxmlMisc.getXmlContent(rslocation.getString("content"),"<mreview>","</mreview>")==null?"":SxmlMisc.getXmlContent(rslocation.getString("content"),"<mreview>","</mreview>");
specialty = SxmlMisc.getXmlContent(rslocation.getString("content"),"<specialty>","</specialty>")==null?"":SxmlMisc.getXmlContent(rslocation.getString("content"),"<specialty>","</specialty>");

 }

 rsPatient = null;
 rsPatient = apptMainBean.queryResults(DemoNo, "search_demographic_details");
 while(rsPatient.next()){
 DemoSex = rsPatient.getString("sex");
 DemoAddress = rsPatient.getString("address");
 DemoCity = rsPatient.getString("city");
 DemoProvince = rsPatient.getString("province");
 DemoPostal = rsPatient.getString("postal");
 DemoDOB = MyDateFormat.getStandardDate(Integer.parseInt(rsPatient.getString("year_of_birth")),Integer.parseInt(rsPatient.getString("month_of_birth")),Integer.parseInt(rsPatient.getString("date_of_birth")));
 hin = rsPatient.getString("hin") + rsPatient.getString("ver");
  if (rsPatient.getString("family_doctor") == null){ r_doctor = "N/A"; r_doctor_ohip="000000";}else{
   r_doctor=SxmlMisc.getXmlContent(rsPatient.getString("family_doctor"),"rd")==null?"":SxmlMisc.getXmlContent(rsPatient.getString("family_doctor"),"rd");
   r_doctor_ohip=SxmlMisc.getXmlContent(rsPatient.getString("family_doctor"),"rdohip")==null?"":SxmlMisc.getXmlContent(rsPatient.getString("family_doctor"),"rdohip");
  }
  HCTYPE = rsPatient.getString("hc_type")==null?"ON":rsPatient.getString("hc_type");
if (DemoSex.equals("M")) HCSex = "1";
if (DemoSex.equals("F")) HCSex = "2";
 roster_status = rsPatient.getString("roster_status");
   }
   

   

 %>