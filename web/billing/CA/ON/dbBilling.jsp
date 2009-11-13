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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<%   
  //operation available to the client - dboperation
  String orderby="", limit="", limit1="", limit2="";
  if(request.getParameter("orderby")!=null) orderby="order by "+request.getParameter("orderby");
  if(request.getParameter("limit1")!=null) limit1=request.getParameter("limit1")+", ";
  if(request.getParameter("limit2")!=null) {
    limit2=request.getParameter("limit2");
    limit="limit "+limit1+limit2;
  }
  
  
  String [][] dbQueries=new String[][] {
    {"search_provider_all_dt", "select * from provider where provider_type='doctor' and provider_no like ? order by last_name"},
    {"search_provider_dt", "select * from provider where status='1' and ohip_no || null and provider_no like ? order by last_name"},
 {"search_provider_dt_checkstatus", "select * from provider where provider_type='doctor' and status='1' and ohip_no || null and provider_no like ? order by last_name"},

    {"search_provider_ohip_dt", "select * from provider where ohip_no like ? and ohip_no || null order by last_name"},
    {"search_demographic_details", "select * from demographic where demographic_no=?"},
    {"search_provider_name", "select * from provider where provider_no=?"},
    {"search_visit_location", "select clinic_location_name from clinic_location where clinic_location_no=?"},
    {"save_bill", "insert into billing values('\\N',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"},
    {"search_billing_no", "select billing_no from billing where demographic_no=?  order by update_date desc, update_time desc limit 0, 1"},
    {"search_billing_no_by_appt", "select max(billing_no) billing_no from billing where status <> 'D' and demographic_no=? and appointment_no=?  order by update_date desc, update_time desc limit 0, 1"},
    {"search_bill_location", "select * from clinic_location where clinic_no=1 and clinic_location_no=?"},    
    {"search_clinic_location", "select * from clinic_location where clinic_no=? order by clinic_location_no"},  
    {"save_clinic_location","insert into clinic_location values(?,?,?)"},
    {"search_bill_center", "select * from billcenter where billcenter_desc like ?"},    
    {"search_bill_history", "select distinct provider.last_name, provider.first_name, billing.billing_no, billing.billing_date, billing.billing_time, billing.status, billing.appointment_no from billing, provider, appointment where provider.provider_no=appointment.provider_no and billing.appointment_no=appointment.appointment_no and billing.status <> 'D' and billing.demographic_no =? order by billing.billing_date desc, billing.billing_time desc "+ limit },    
    {"search_bill_beforedelete", "select billing_no, status from billing where appointment_no=?"},
    {"search_unbill_history", "select * from appointment where provider_no=? and appointment_date<=? and (status='P' or status='H' or status='PV' or status='PS') and demographic_no <> 0 order by appointment_date desc, start_time desc "+ limit },    
    {"search_unbill_history_daterange", "select * from appointment where provider_no=? and appointment_date >=? and appointment_date<=? and (status='P' or status='H' or status='PV' or status='PS') and demographic_no <> 0 order by appointment_date desc, start_time desc "+ limit },    
    {"search_bill_history_daterange", "select * from billing where provider_no=? and billing_date >=? and billing_date<=? and (status<>'D' and status<>'S' and status<>'B') and demographic_no <> 0 order by billing_date desc, billing_time desc "+ limit },    
    {"search_unsettled_history_daterange", "select * from billing where provider_no=? and billing_date >=? and billing_date<=? and (status='B') and demographic_no <> 0 order by billing_date desc, billing_time desc "+ limit },    
    {"search_allbill_daterange", "select * from billing where provider_no=? and billing_date >=? and billing_date<=? and (status<>'D') and demographic_no <> 0 order by billing_date desc, billing_time desc "+ limit },    
    {"search_ctlbillservice", "select distinct servicetype_name, servicetype from ctl_billingservice where status='A' and servicetype like ?"},
    {"search_ctlbillservice_detail", "select service_group_name, service_group, servicetype_name, servicetype, service_code, service_order from ctl_billingservice where status='A' and service_group=? and servicetype=?"},
    {"search_ctldiagcode_detail", "select * from ctl_diagcode where servicetype=?"},
    {"save_ctlbillservice","insert into ctl_billingservice values(?,?,?,?,?,?,?)"},
    {"save_ctldiagcode","insert into ctl_diagcode values(?,?,?)"},
    {"save_ctlbilltype","insert into ctl_billingtype values(?,?)"},
    {"delete_ctlbillservice","delete from ctl_billingservice where servicetype=?"},
    {"delete_ctldiagcode","delete from ctl_diagcode where servicetype=?"},
    {"delete_ctlbilltype","delete from ctl_billingtype where servicetype=?"},
    {"search_ctlbilltype","select * from ctl_billingtype where servicetype=?"},
    {"update_ctlbilltype","update ctl_billingtype set billtype=? where servicetype=?"},
    {"delete_bill", "update billing set status='D' where billing_no=?" },    
    {"delete_bill_detail", "update billingdetail set status='D' where billing_no=?" },  
    {"search_bill_mismatch", "select distinct a.appointment_no, a.appointment_date, a.start_time, d.first_name, d.last_name, p.first_name, p.last_name, b.provider_no, b.billing_no from billing b, appointment a, demographic d, provider p where a.provider_no=? and a.appointment_no=b.appointment_no and a.demographic_no=d.demographic_no and p.provider_no=b.provider_no and b.status<>'B' and b.status<>'D' order by a.appointment_date desc, a.start_time desc;"},  
    {"search_servicecode", "select c.service_group_name, c.service_order,b.service_code, b.description, b.value, b.percentage from billingservice b, ctl_billingservice c where c.service_code=b.service_code and c.status='A' and c.servicetype = ? and c.service_group =? order by c.service_order"},    
    {"search_servicecode_detail", "select b.service_code, b.description, b.value, b.percentage from billingservice b where b.service_code=?"},
    {"save_bill_record", "insert into billingdetail values('\\N',?,?,?,?,?,?,?,?)"},
    {"updatediagnostic", "update diagnosticcode set description=? where diagnostic_code=?"},
    {"searchapptstatus", "select status from appointment where appointment_no=? "}, 
    {"updateapptstatus", "update appointment set status=? where appointment_no=? "}, //provider_no=? and appointment_date=? and start_time=? and demographic_no=?"},
    {"search_bill", "select * from billing where billing_no= ?"},
    {"search_bill_short", "select billing_no, clinic_no,demographic_no, provider_no,appointment_no ,organization_spec_code, demographic_name, hin, update_date, update_time, billing_date, billing_time, clinic_ref_code, total, status, dob, visitdate, visittype, provider_ohip_no,provider_rma_no,apptProvider_no, asstProvider_no, creator from billing where billing_no= ?"},
    {"search_bill_record", "select * from billingdetail where billing_no=? and status <> 'D'"},
    {"search_ctl_diagnostic_code", "select diagnosticcode.diagnostic_code dcode, diagnosticcode.description des from diagnosticcode, ctl_diagcode where ctl_diagcode.diagnostic_code=diagnosticcode.diagnostic_code and ctl_diagcode.servicetype=? order by diagnosticcode.description"},
    {"search_diagnostic_code", "select * from diagnosticcode where diagnostic_code like ?"},
    {"search_diagnostic_text", "select * from diagnosticcode where description like ?"},
    {"search_diagnostic_desc", "select description from diagnosticcode where diagnostic_code = ?"},
    {"searchappointmentday", "select appointment_no,provider_no, start_time,end_time,name,demographic_no,reason,notes,status from appointment where provider_no=? and appointment_date=? order by start_time "}, 
    {"search_demograph", "select *  from demographic where demographic_no=?"},
    {"search_encounter", "select * from encounter where demographic_no = ? order by encounter_date desc, encounter_time desc"},
    {"search_demographicaccessory", "select * from demographicaccessory where demographic_no=?"},
    {"archive_bill", "insert into recycle_bin values(?,'billing',?,?)"},
    {"update_bill_header", "update billing set hin=?,dob=?,visittype=?,visitdate=?,clinic_ref_code=?,provider_no=?,status=?, update_date=?, total=? , content=? where billing_no=?"},  
    {"search_bill_generic", "select distinct demographic.last_name dl, demographic.first_name df, provider.last_name pl, provider.first_name pf, billing.billing_no, billing.billing_date, billing.billing_time, billing.status, billing.appointment_no, billing.hin from billing, provider, appointment, demographic where provider.provider_no=appointment.provider_no and demographic.demographic_no= billing.demographic_no and billing.appointment_no=appointment.appointment_no and billing.status <> 'D' and billing.billing_no=?"},
    {"save_rahd", "insert into raheader values('\\N',?,?,?,?,?,?,?,?,?)"},
    {"save_radt", "insert into radetail values('\\N',?,?,?,?,?,?,?,?,?,?,?)"},
    {"search_all_rahd", "select raheader_no, totalamount, status, paymentdate, payable, records, claims, readdate from raheader where status <> ? order by paymentdate desc, readdate desc"},
    {"search_rahd", "select raheader_no, totalamount, status, paymentdate, payable, records, claims, readdate from raheader where filename=? and paymentdate=? and status <> 'D' order by paymentdate"},
    {"search_radt", "select count(raheader_no) from radetail where raheader_no=?"},
    {"search_rahd_content", "select * from raheader where raheader_no=? and status <>'D'"},
    {"search_rahd_short", "select filename from raheader where raheader_no=? and status <>'D'"},
    {"update_rahd", "update raheader set totalamount=?, records=?,claims=?, content=? where paymentdate=? and filename=?"},
    {"search_raprovider", "select r.providerohip_no, p.last_name,p.first_name from radetail r, provider p where p.ohip_no=r.providerohip_no and r.raheader_no=? group by r.providerohip_no"},
    {"search_raerror", "select * from radetail where raheader_no=? and error_code<>'' and error_code<>? and providerohip_no like ?"},
    {"search_ranoerror", "select distinct billing_no from radetail where raheader_no=? and (error_code='' or error_code=?) and providerohip_no like ?"},
    {"search_raerror35", "select * from radetail where raheader_no=? and error_code<>'' and error_code<>? and error_code<>? and error_code<>'EV' and error_code<>'55' and error_code<>'57' and error_code<>'HM' and (service_code<>'Q200A' or error_code<>'I9') and providerohip_no like ?"},
    {"search_ranoerror35", "select distinct billing_no from radetail where raheader_no=? and (error_code='' or error_code=? or error_code=? or error_code='EV' or error_code='55' or error_code='57' or error_code='HM' or (service_code='Q200A' and error_code='I9')) and providerohip_no like ?"},
    {"search_ranoerrorQ", "select distinct billing_no from radetail where raheader_no=? and (service_code='Q011A' or service_code='Q020A' or service_code='Q130A' or service_code='Q131A' or service_code='Q132A' or service_code='Q133A' or service_code='Q140A' or service_code='Q141A' or service_code='Q142A') and error_code='30' and providerohip_no like ?"},
    {"search_rasummary_dt", "select billing_no, service_count, error_code, amountclaim, service_code,service_date, providerohip_no, amountpay, hin from radetail where raheader_no=? and providerohip_no like ?"},
    {"search_rabillno", "select * from radetail where raheader_no=? and billing_no=?"},
    {"search_rasummary", "select r.service_count, r.error_code, r.amountclaim, b.visittype, b.billing_no, r.service_code, r.service_date, r.providerohip_no, r.amountpay, p.last_name, p.first_name from radetail r, billing b, provider p where r.raheader_no=? and b.billing_no=r.billing_no and p.ohip_no=r.providerohip_no and r.providerohip_no like ?"},
    {"search_service_code", "select service_code, description from billingservice where service_code like ? or service_code like ? or service_code like ? or description like ? or description like ? or description like ?"},
    {"search_research_code", "select ichppccode, description from ichppccode where ichppccode like ? or ichppccode like ? or ichppccode like ? or description like ? or description like ? or description like ?"},
    {"save_billactivity", "insert into billactivity values(?,?,?,?,?,?,?,?,?,?,?,?,?)"},
    {"search_billactivity", "select * from billactivity where updatedatetime >= ? and updatedatetime <=? and status <> 'D' order by updatedatetime desc"},
    {"search_billactivity_short", "select htmlfilename, ohipfilename, providerohipno, groupno, creator, claimrecord, updatedatetime, total  from billactivity where updatedatetime >= ? and updatedatetime <=? and status <> 'D' order by updatedatetime desc"},
    {"search_billactivity_monthCode", "select * from billactivity where monthCode=? and providerohipno=? and updatedatetime > ? and status <> 'D' order by batchcount"},
    {"search_billactivity_monthCodeshort", "select batchcount from billactivity where monthCode=? and providerohipno=? and updatedatetime > ? and status <> 'D' order by batchcount"},
    {"search_billactivity_group_monthCode", "select * from billactivity where monthCode=? and groupno=? and updatedatetime > ? and status <> 'D' order by batchcount"},
    {"updatebillservice", "update billingservice set description=? where service_code=?"},
    {"update_billhd","update billing set status='S' where billing_no=? and status<>'D'"},
    {"update_rahd_status","update raheader set status=? where raheader_no=?"},
    {"update_rahd_content","update raheader set content=? where raheader_no=?"},
    {"search_billob", "select distinct b.billing_no,b.total,b.status,b.billing_date, b.demographic_name from billing b, billingdetail bd where bd.billing_no=b.billing_no and b.status<>'D' and( bd.service_code='P006A' or bd.service_code='P011A' or bd.service_code='P009A'or bd.service_code='P020A' or bd.service_code='P022A' or bd.service_code='P028A' or bd.service_code='P023A' or bd.service_code='P007A' or bd.service_code='P008B' or bd.service_code='P018B' or bd.service_code='E502A' or bd.service_code='C989A' or bd.service_code='E409A' or bd.service_code='E410A' or bd.service_code='E411A' or bd.service_code='H001A') and b.provider_no like ? and b.billing_date>=? and b.billing_date<=?"},
    {"search_billflu", "select distinct b.content, b.billing_no,b.total,b.status,b.billing_date, b.demographic_name from billing b, billingdetail bd where bd.billing_no=b.billing_no and b.status<>'D' and( bd.service_code='G590A' or bd.service_code='G591A') and b.creator like ? and b.billing_date>=? and b.billing_date<=? order by b.demographic_name"},
    {"search_allbill_history", "select distinct provider.last_name, provider.first_name, billing.apptProvider_no, billing.billing_no, billing.billing_date, billing.billing_time, billing.status, billing.appointment_no from billing, provider where provider.provider_no = billing.provider_no and billing.status <> 'D' and billing.demographic_no =? order by billing.billing_date desc, billing.billing_time desc " + limit},
    {"search_raob", "select distinct billing_no from radetail where raheader_no=? and (service_code='P006A' or service_code='P020A' or service_code='P022A' or service_code='P028A' or service_code='P023A' or service_code='P007A' or service_code='P009A' or service_code='P011A' or service_code='P008B' or service_code='P018B' or service_code='E502A' or service_code='C989A' or service_code='E409A' or service_code='E410A' or service_code='E411A' or service_code='H001A')"},
    {"search_racolposcopy", "select distinct billing_no from radetail where raheader_no=? and (service_code='A004A' or service_code='A005A' or service_code='Z731A' or service_code='Z666A' or service_code='Z730A' or service_code='Z720A')"},
    {"search_billingservice_premium_dt", "select * from ctl_billingservice_premium where status=?"},
    {"search_billingservice_premium", "select status from ctl_billingservice_premium where service_code=?"},
    {"search_ctlpremium", "select b.service_code, c.description service_desc from ctl_billingservice_premium b, billingservice c where b.service_code=c.service_code and b.status=?"},
    {"save_ctlpremium", "insert into ctl_billingservice_premium values(?,?,?,?)"},
   {"delete_ctlpremium", "delete from ctl_billingservice_premium where service_code=?"},
   
    {"search_billingform","select distinct  servicetype_name, servicetype from ctl_billingservice where servicetype like ?"},
    {"search_reportprovider","select p.last_name, p.first_name, p.provider_no, r.team from provider p,reportprovider r where r.provider_no=p.provider_no and r.status<>'D' and r.action=? order by team"},
      
    {"search_demo_fd", "select provider_no from demographic where hin=?"},
 
 };
  
  //associate each operation with an output JSP file - displaymode
  String[][] responseTargets=new String[][] {
    {"day" , "appointmentprovideradminday.jsp"},
    {"month" , "appointmentprovideradminmonth.jsp"},
    {"addstatus" , "provideraddstatus.jsp"},
    {"updatepreference" , "providerupdatepreference.jsp"},
    {"displaymygroup" , "providerdisplaymygroup.jsp"},
    {"encounter" , "providerencounter.jsp"},
    {"prescribe" , "providerprescribe.jsp"},
    {"vary" , request.getParameter("displaymodevariable")==null?"":request.getParameter("displaymodevariable") },
    {"saveencounter" , "providersaveencounter.jsp"},
    {"savebill" , "providersavebill.jsp"},
    {"encounterhistory" , "providerencounterhistory.jsp"},
  };
  apptMainBean.doConfigure(dbQueries,responseTargets);
%>