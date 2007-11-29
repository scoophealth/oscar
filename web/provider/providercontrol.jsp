<%--
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
--%>
<%-- add by caisi --%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ page  import="java.util.*,java.net.*"  errorPage="errorpage.jsp"%>
<%@page import="org.oscarehr.PMmodule.web.utils.UserRoleUtils" %>

<caisi:isModuleLoad moduleName="caisi"><%
    String isOscar = request.getParameter("infirmaryView_isOscar");
    if (session.getAttribute("infirmaryView_isOscar")==null) isOscar="false";
    if (isOscar!=null) session.setAttribute("infirmaryView_isOscar", isOscar);
    session.setAttribute("infirmaryView_programId",request.getParameter("infirmaryView_programId"));
    session.setAttribute("infirmaryView_OscarURL",request.getRequestURL());

%><c:import url="/infirm.do?action=getSig" />
</caisi:isModuleLoad>
<%-- add by caisi end --%>
<%
    if(session.getAttribute("userrole") == null ) {
        response.sendRedirect("../logout.jsp");
        return;
    }
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<%
    if(roleName$.indexOf(UserRoleUtils.Roles.er_clerk.name()) != -1) {
        response.sendRedirect("er_clerk.jsp");
        return;
    }
%>

<%
    if(roleName$.indexOf("Vaccine Provider") != -1) {
        response.sendRedirect("vaccine_provider.jsp");
        return;
    }
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment" rights="r" reverse="<%=true%>" >
    <%
        if (true)
        {
            response.sendRedirect("../logout.jsp");
            return;
        }
    %>
</security:oscarSec>


<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("../logout.jsp");
        return;
    }

    if(request.getParameter("year")==null && request.getParameter("month")==null && request.getParameter("day")==null && request.getParameter("displaymode")==null && request.getParameter("dboperation")==null) {
        GregorianCalendar now=new GregorianCalendar();
        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH)+1 ; //be care for the month +-1
        int nowDay = now.get(Calendar.DAY_OF_MONTH);
        response.sendRedirect("./providercontrol.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday");
        return;
    }
%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%@ include file="../admin/dbconnection.jsp" %>

<%
    //operation available to the client - dboperation
    String [][] dbOperation;
    if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
        dbOperation=new String[][] {
                {"search_tickler","select * from tickler where demographic_no=? and service_date<=? and status='A' order by service_date desc"},
                {"search_studycount","select count(ds.study_no) from demographicstudy ds, study s where ds.demographic_no=? and ds.study_no=s.study_no and s.current='1'"},
                {"search_study","select s.* from demographicstudy d, study s where demographic_no=? and d.study_no = s.study_no limit 1 "},
                {"searchappointmentday", "select appointment_no,provider_no, start_time,end_time,name,demographic_no,reason,notes,status from appointment where provider_no=? and appointment_date=? order by start_time, status desc "},
                {"searchmygroupcount", "select count(provider_no) from mygroup where mygroup_no=? "},
                {"searchmygroupprovider", "select provider_no, last_name, first_name from mygroup where mygroup_no=? "},
                {"searchmygroupall", "select * from mygroup order by mygroup_no"},
                // {"searchmygroupno", "select * from mygroup group by mygroup_no order by mygroup_no"},
                {"searchmygroupno", "select mygroup_no from mygroup group by mygroup_no order by mygroup_no"},
                {"deletegroupmember", "delete from mygroup where mygroup_no=? and provider_no=?"},
                {"savemygroup", "insert into mygroup (mygroup_no,provider_no,last_name,first_name) values(?,?,?,?)" },
                {"updateapptstatus", "update appointment set status=? where appointment_no=? "},
                {"updatepreference", "update preference set start_hour=?, end_hour=?, every_min=?, mygroup_no=?, default_servicetype=?, color_template=?, new_tickler_warning_window=? , default_caisi_pmm=? where provider_no=? "},
                {"add_preference", "insert into preference (provider_no, start_hour, end_hour, every_min, mygroup_no, default_servicetype, color_template, new_tickler_warning_window, default_caisi_pmm) values (?, ?, ?, ?, ?, ?, ?, ?, ?)"},
                {"search_demograph", "select *  from demographic where demographic_no=?"},
                {"search_encounter", "select * from encounter where demographic_no = ? order by encounter_date desc, encounter_time desc"},
                {"search_encounter_no", "select * from encounter where demographic_no = ? and encounter_date=? and encounter_time=? and provider_no=? order by encounter_no desc limit 1"},
                {"search_encountersingle", "select * from encounter where encounter_no = ?"},
                {"search_previousenc", "select * from encounter where demographic_no = ? and provider_no=? order by encounter_date desc, encounter_time desc limit 1"},
                {"delete_encounter1", "insert into recyclebin (provider_no,updatedatetime,table_name,keyword,table_content) values(?,?,'encounter',?,?)"},
                {"delete_encounter2", "delete from encounter where encounter_no = ?"},
                {"search_encounterform", "select * from encounterform where encounterform_name like ? order by encounterform_name"},
                {"search_form", "select * from form where form_no=? "}, //new?delete
                {"search_form_no", "select form_no, content from form where demographic_no=? and form_name like ? order by form_date desc, form_time desc,form_no desc limit 1"}, //new?delete
                {"compare_form", "select form_no, form_name, content from form where demographic_no=? and form_name like ? order by form_date desc, form_time desc,form_no desc limit 1"},
                {"save_form", "insert into form (demographic_no, provider_no, form_date, form_time, form_name, content) values(?,?,?,?,?,?)"},
                {"search_template", "select * from encountertemplate where encountertemplate_name like ? order by encountertemplate_name"},
                //{"search_templatevalue", "select * from encountertemplate where encountertemplate_name like ?"},
                {"add_encounter", "insert into encounter (demographic_no, encounter_date, encounter_time, provider_no, subject, content, encounterattachment) values(?,?,?,?,?,?,?)"},
                {"save_prescribe", "insert into prescribe (demographic_no, provider_no, prescribe_date, prescribe_time, content) values(?,?,?,?,?)"},
                {"search_prescribe", "select * from prescribe where prescribe_no= ?"},
                {"search_prescribe_no", "select prescribe_no from prescribe where demographic_no=?  order by prescribe_date desc, prescribe_time desc limit 1"},
                {"search_demographicaccessory", "select * from demographicaccessory where demographic_no=?"},
                {"search_demographicaccessorycount", "select count(demographic_no) from demographicaccessory where demographic_no=?"},
                {"add_demographicaccessory", "insert into demographicaccessory values(?,?)"},
                {"update_demographicaccessory", "update demographicaccessory set content=? where demographic_no=?"},
                {"add_template", "insert into encountertemplate values(?,?,?,?)"},
                {"delete_template", "delete from encountertemplate where encountertemplate_name = ?"},
                {"search_templatename", "select encountertemplate_name from encountertemplate order by encountertemplate_name"},
                {"add_encounterform", "insert into encounterform values(?,?)"},
                {"delete_encounterform", "delete from encounterform where encounterform_name = ?"},
                {"search_encounterformname", "select encounterform_name from encounterform order by encounterform_name"},
                {"search_provider_slp", "select comments from provider where provider_no=?"},

                {"searchprovider", "select provider_no, last_name, first_name from provider where provider_type='doctor' and status='1' order by last_name"},
                //{"searchallprovider", "select * from provider order by ?"}, ^M
                {"searchallprovider", "select * from provider where status='1' order by last_name"},
                {"search_scheduleholiday", "select * from scheduleholiday where sdate > ?" },
                {"search_scheduledate_datep", "select * from scheduledate where sdate between ? and ? and status = 'A' order by sdate" },
                {"search_scheduledate_singlep", "select * from scheduledate where sdate between ? and ? and provider_no=? and status = 'A' order by sdate" },
                {"search_scheduledate_single", "select * from scheduledate where sdate=? and provider_no=? and status = 'A'" },

                {"search_appttimecode", "select scheduledate.provider_no, scheduletemplate.timecode, scheduledate.sdate from scheduletemplate, scheduledate where scheduletemplate.name=scheduledate.hour and scheduledate.sdate=? and  scheduledate.provider_no=? and scheduledate.status = 'A' and (scheduletemplate.provider_no=scheduledate.provider_no or scheduletemplate.provider_no='Public') order by scheduledate.sdate"},
                // {"search_timecode", "select * from scheduletemplatecode order by ?"},
                {"search_timecode", "select * from scheduletemplatecode order by code"},
                {"search_resource_baseurl", "select * from property where name = ?"},

                {"search_numgrpscheduledate", "select count(scheduledate.provider_no) from mygroup, scheduledate where mygroup_no = ? and scheduledate.sdate=? and mygroup.provider_no=scheduledate.provider_no and scheduledate.available = '1'  and scheduledate.status = 'A'"},
        };
    }else{
        dbOperation=new String[][] {
                {"search_tickler","select * from tickler where demographic_no=? and service_date<=? and status='A' order by service_date desc"},
                {"search_studycount","select count(ds.study_no) from demographicstudy ds, study s where ds.demographic_no=? and ds.study_no=s.study_no and s.current='1'"},
                {"search_study","select s.* from demographicstudy d, study s where demographic_no=? and d.study_no = s.study_no limit 1 "},
                {"searchappointmentday", "select appointment_no,provider_no, start_time,end_time,name,demographic_no,reason,notes,status from appointment where provider_no=? and appointment_date=? order by start_time, status desc "},
                {"searchmygroupcount", "select count(provider_no) from mygroup where mygroup_no=? "},
                {"searchmygroupprovider", "select provider_no, last_name, first_name from mygroup where mygroup_no=? "},
                {"searchmygroupall", "select * from mygroup order by mygroup_no"},
                // {"searchmygroupno", "select * from mygroup group by mygroup_no order by mygroup_no"},
                {"searchmygroupno", "select mygroup_no from mygroup group by mygroup_no order by mygroup_no"},
                {"deletegroupmember", "delete from mygroup where mygroup_no=? and provider_no=?"},
                {"savemygroup", "insert into mygroup (mygroup_no,provider_no,last_name,first_name) values(?,?,?,?)" },
                {"updateapptstatus", "update appointment set status=? where appointment_no=? "},
                {"updatepreference", "update preference set start_hour=?, end_hour=?, every_min=?, mygroup_no=?, default_servicetype=?, color_template=? where provider_no=? "},
                {"add_preference", "insert into preference (provider_no, start_hour, end_hour, every_min, mygroup_no, default_servicetype, color_template) values (?, ?, ?, ?, ?, ?, ?)"},
                {"search_demograph", "select *  from demographic where demographic_no=?"},
                {"search_encounter", "select * from encounter where demographic_no = ? order by encounter_date desc, encounter_time desc"},
                {"search_encounter_no", "select * from encounter where demographic_no = ? and encounter_date=? and encounter_time=? and provider_no=? order by encounter_no desc limit 1"},
                {"search_encountersingle", "select * from encounter where encounter_no = ?"},
                {"search_previousenc", "select * from encounter where demographic_no = ? and provider_no=? order by encounter_date desc, encounter_time desc limit 1"},
                {"delete_encounter1", "insert into recyclebin (provider_no,updatedatetime,table_name,keyword,table_content) values(?,?,'encounter',?,?)"},
                {"delete_encounter2", "delete from encounter where encounter_no = ?"},
                {"search_encounterform", "select * from encounterform where encounterform_name like ? order by encounterform_name"},
                {"search_form", "select * from form where form_no=? "}, //new?delete
                {"search_form_no", "select form_no, content from form where demographic_no=? and form_name like ? order by form_date desc, form_time desc,form_no desc limit 1"}, //new?delete
                {"compare_form", "select form_no, form_name, content from form where demographic_no=? and form_name like ? order by form_date desc, form_time desc,form_no desc limit 1"},
                {"save_form", "insert into form (demographic_no, provider_no, form_date, form_time, form_name, content) values(?,?,?,?,?,?)"},
                {"search_template", "select * from encountertemplate where encountertemplate_name like ? order by encountertemplate_name"},
                //{"search_templatevalue", "select * from encountertemplate where encountertemplate_name like ?"},
                {"add_encounter", "insert into encounter (demographic_no, encounter_date, encounter_time, provider_no, subject, content, encounterattachment) values(?,?,?,?,?,?,?)"},
                {"save_prescribe", "insert into prescribe (demographic_no, provider_no, prescribe_date, prescribe_time, content) values(?,?,?,?,?)"},
                {"search_prescribe", "select * from prescribe where prescribe_no= ?"},
                {"search_prescribe_no", "select prescribe_no from prescribe where demographic_no=?  order by prescribe_date desc, prescribe_time desc limit 1"},
                {"search_demographicaccessory", "select * from demographicaccessory where demographic_no=?"},
                {"search_demographicaccessorycount", "select count(demographic_no) from demographicaccessory where demographic_no=?"},
                {"add_demographicaccessory", "insert into demographicaccessory values(?,?)"},
                {"update_demographicaccessory", "update demographicaccessory set content=? where demographic_no=?"},
                {"add_template", "insert into encountertemplate values(?,?,?,?)"},
                {"delete_template", "delete from encountertemplate where encountertemplate_name = ?"},
                {"search_templatename", "select encountertemplate_name from encountertemplate order by encountertemplate_name"},
                {"add_encounterform", "insert into encounterform values(?,?)"},
                {"delete_encounterform", "delete from encounterform where encounterform_name = ?"},
                {"search_encounterformname", "select encounterform_name from encounterform order by encounterform_name"},
                {"search_provider_slp", "select comments from provider where provider_no=?"},

                {"searchprovider", "select provider_no, last_name, first_name from provider where provider_type='doctor' and status='1' order by last_name"},
                //{"searchallprovider", "select * from provider order by ?"}, ^M
                {"searchallprovider", "select * from provider where status='1' order by last_name"},
                {"search_scheduleholiday", "select * from scheduleholiday where sdate > ?" },
                {"search_scheduledate_datep", "select * from scheduledate where sdate between ? and ? and status = 'A' order by sdate" },
                {"search_scheduledate_singlep", "select * from scheduledate where sdate between ? and ? and provider_no=? and status = 'A' order by sdate" },
                {"search_scheduledate_single", "select * from scheduledate where sdate=? and provider_no=? and status = 'A'" },

                {"search_appttimecode", "select scheduledate.provider_no, scheduletemplate.timecode, scheduledate.sdate from scheduletemplate, scheduledate where scheduletemplate.name=scheduledate.hour and scheduledate.sdate=? and  scheduledate.provider_no=? and scheduledate.status = 'A' and (scheduletemplate.provider_no=scheduledate.provider_no or scheduletemplate.provider_no='Public') order by scheduledate.sdate"},
                // {"search_timecode", "select * from scheduletemplatecode order by ?"},
                {"search_timecode", "select * from scheduletemplatecode order by code"},
                {"search_resource_baseurl", "select * from property where name = ?"},

                {"search_numgrpscheduledate", "select count(scheduledate.provider_no) from mygroup, scheduledate where mygroup_no = ? and scheduledate.sdate=? and mygroup.provider_no=scheduledate.provider_no and scheduledate.available = '1' and scheduledate.status = 'A' "},
        };

    }
    //associate each operation with an output JSP file - displaymode
    String[][] toFile=new String[][] {
            {"day" , "appointmentprovideradminday.jsp"},
            {"month" , "appointmentprovideradminmonth.jsp"},
            {"addstatus" , "provideraddstatus.jsp"},
            {"updatepreference" , "providerupdatepreference.jsp"},
            {"displaymygroup" , "providerdisplaymygroup.jsp"},
            {"encounter" , "providerencounter.jsp"},
            {"prescribe" , "providerprescribe.jsp"},
            {"encountersingle" , "providerencountersingle.jsp"},
            {"vary" , request.getParameter("displaymodevariable")==null?"":URLDecoder.decode(request.getParameter("displaymodevariable")) },
            {"saveform" , "providersaveform.jsp"},
            {"saveencounter" , "providersaveencounter.jsp"},
            {"savebill" , "providersavebill.jsp"},
            {"saveprescribe" , "providersaveprescribe.jsp"},
            {"savedemographicaccessory" , "providersavedemographicaccessory.jsp"},
            {"encounterhistory" , "providerencounterhistory.jsp"},
            {"savedeletetemplate" , "providertemplate.jsp"},
            {"savetemplate" , "providersavetemplate.jsp"},
            {"savedeleteform" , "providerform.jsp"},
            {"save_encounterform" , "providersaveencounterform.jsp"},
            {"ar1" , "formar1_99_12.jsp"},
            {"ar2" , "formar2_99_08.jsp"},
            {"newgroup" , "providernewgroup.jsp"},
            {"savemygroup" , "providersavemygroup.jsp"},
//    {"billingobstetric" , "billingobstetric.jsp"},

    };
    apptMainBean.doConfigure(dbParams,dbOperation,toFile);
    apptMainBean.doCommand(request); //store request to a help class object Dict - function&params
    if(true) {
        out.clear();
        pageContext.forward(apptMainBean.whereTo()); //forward request&response to the target page
        return;
    }
%>
