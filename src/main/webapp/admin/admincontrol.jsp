<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.model.ProviderPreference"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.userAdmin,_admin.torontoRfq" rights="r"
	reverse="<%=true%>">
	<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<%
if(session.getAttribute("user") == null ) //|| !((String) session.getValue("userprofession")).equalsIgnoreCase("admin"))
	response.sendRedirect("../logout.jsp");
  //instatiate/configure the main bean, forward the request to the output file
%>

<%
    if(session.getAttribute("user") == null ) response.sendRedirect("../logout.jsp");
    String curProvider_no = (String) session.getAttribute("user");

    boolean isSiteAccessPrivacy=false;
%>

<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isSiteAccessPrivacy=true; %>
</security:oscarSec>

<%@ page errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />


<%
  String orderby="", limit="", limit1="", limit2="";
  if(request.getParameter("orderby")!=null) orderby="order by "+request.getParameter("orderby");
  if(request.getParameter("limit1")!=null) limit1=request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) {
    limit2=request.getParameter("limit2");
    limit="limit "+limit2 + " offset "+limit1;
  }
  String strDbType = oscar.OscarProperties.getInstance().getProperty("db_type").trim();
  if("oracle".equalsIgnoreCase(strDbType)){
  	limit = "";
  }

  String fieldname="", regularexp="like"; // exactly search is not required by users, e.g. regularexp="=";
  if(request.getParameter("search_mode")!=null) {
	  if(request.getParameter("keyword").indexOf("*")!=-1 || request.getParameter("keyword").indexOf("%")!=-1) regularexp="like";
    if(request.getParameter("search_mode").equals("search_address")) fieldname="address";
    if(request.getParameter("search_mode").equals("search_phone")) fieldname="phone";
    if(request.getParameter("search_mode").equals("search_hin")) fieldname="hin";
    if(request.getParameter("search_mode").equals("search_providerno")) fieldname="provider_no";
    if(request.getParameter("search_mode").equals("search_preferenceno")) fieldname="preference_no";
    if(request.getParameter("search_mode").equals("search_username")) fieldname="user_name";
    if(request.getParameter("search_mode").equals("search_dob")) fieldname="year_of_birth "+regularexp+" ?"+" and month_of_birth "+regularexp+" ?"+" and date_of_birth ";
    if(request.getParameter("search_mode").equals("search_name")) {
      if(request.getParameter("keyword").indexOf(",")==-1)  fieldname="lower(last_name)";
      else if(request.getParameter("keyword").trim().indexOf(",")==(request.getParameter("keyword").trim().length()-1)) fieldname="lower(last_name)";
      else fieldname="lower(last_name) "+regularexp+" ?"+" and lower(first_name) ";
    }


  }
    //We find out if search is limited to active or inactive providers
    String[] status = request.getParameterValues("search_status");
    String inactive = "0";
    String active = "0";

    if( status != null ) {
        int numConditions = status.length;
        String sql = new String();
        if( status.length == 1 ) {
            if( status[0].equals("0") ) {
                sql = "status = 0 and ";
                inactive = "1";
            }
            else if( status[0].equals("1") ) {
                sql = "status = 1 and ";
                active = "1";
            }
        }
        else if( status.length == 2 ) {
            inactive = "1";
            active = "1";
        }
        fieldname = sql + fieldname;

    }
    //we save results in request to maintain state of form
    request.setAttribute("inactive",inactive);
    request.setAttribute("active",active);


  //operation available to the client - dboperation
  String [][] dbQueries=null;
  if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
  dbQueries=new String[][] {
    {"provider_search_titlename", "select provider_no,first_name,last_name,specialty,sex,team,phone,status from provider where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},
    {"provider_search_detail", "select * from provider where provider_no=?"},
    {"search_provider_doc", "select * from provider where provider_type='doctor' and status='1' order by last_name"},

    {"demographic_search_titlename", "select demographic_no,first_name,last_name,roster_status,sex,year_of_birth,month_of_birth,date_of_birth  from demographic where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},
    {"demographic_search_merged", "select d.demographic_no,first_name,last_name,roster_status,sex,year_of_birth,month_of_birth,date_of_birth  from demographic d join demographic_merged dm on d.demographic_no = dm.demographic_no and dm.deleted = 0 where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},
    {"demographic_search_detail", "select * from demographic where demographic_no=?"},
    {"demographic_search_detail_ptbr", "select * from demographic d left outer join demographic_ptbr dptbr on dptbr.demographic_no = d.demographic_no where d.demographic_no=?"},
    {"demographic_search_demoaddno", "select demographic_no from demographic where last_name=? and first_name =? and year_of_birth=? and month_of_birth=? and date_of_birth=? and hin=? and ver=?"},
    {"search_lastfirstnamedob", "select demographic_no from demographic where last_name=? and first_name=? and year_of_birth=? and month_of_birth=? and date_of_birth=?"},

    {"security_search_titlename", "select * from security where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},
    {"security_search_detail", "select * from security where security_no=?"},

    {"searchmygroupcount", "select count(provider_no) from mygroup where mygroup_no=? "},
    {"searchmygroupprovider", "select provider_no, last_name, first_name from mygroup where mygroup_no=? "},
    {"searchmygroupall", "select * from mygroup order by mygroup_no"},
    {"searchprovider", "select provider_no, last_name, first_name from provider where provider_type='doctor' and status='1' order by last_name"},
    {"searchproviderall", "select provider_no, last_name, first_name from provider where status='1' order by last_name"},

    {"search_ptstatus", "select distinct patient_status from demographic where patient_status != '' and patient_status != 'AC' and patient_status != 'IN' and patient_status != 'DE' and patient_status != 'MO' and patient_status != 'FI'"},
    {"search_rsstatus", "select distinct roster_status from demographic where roster_status != '' and roster_status != 'RO' and roster_status != 'NR' and roster_status != 'TE' and roster_status != 'FS' "},
    {"search_wlstatus", "select * from waitingList where demographic_no=? AND is_history='N' order by onListSince DESC"},
    {"search_waiting_list", "select * from waitingListName where group_no='" + ((ProviderPreference)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE)).getMyGroupNo() +"' AND is_history='N' order by name"},
    //muti-site query
    {"site_searchproviderall","select p.provider_no, p.last_name, p.first_name from provider p INNER JOIN providersite s ON p.provider_no = s.provider_no WHERE s.site_id IN (SELECT site_id from providersite where provider_no=?) AND p.status='1' order by last_name"},
    {"site_searchmygroupall" ,"select p.* from mygroup p INNER JOIN providersite s ON p.provider_no = s.provider_no WHERE s.site_id IN (SELECT site_id from providersite where provider_no=?) order by mygroup_no"},
    {"site_preference_search_titlename", "select p.* from preference p INNER JOIN providersite s ON p.provider_no = s.provider_no where p."+fieldname+ " "+regularexp+" ?  AND s.site_id IN (SELECT site_id from providersite where provider_no= ? ) " +orderby + " "+limit},
    {"site_security_search_titlename", "select p.* from security p INNER JOIN providersite s ON p.provider_no = s.provider_no where p."+fieldname+ " "+regularexp+" ?  AND s.site_id IN (SELECT site_id from providersite where provider_no= " + curProvider_no + " ) " +orderby + " "+limit},
    {"site_provider_search_titlename", "select p.provider_no,p.first_name,p.last_name,p.specialty,p.sex,p.team,p.phone,p.status from provider p where "+fieldname+ " "+regularexp+" ? and exists(select * from providersite s where p.provider_no = s.provider_no and s.site_id IN (SELECT site_id from providersite where provider_no=" + curProvider_no + ")) "  +orderby + " "+limit},
    {"demographic_admin_reports","SELECT demographic_no,first_name,last_name,roster_status,sex,chart_no,year_of_birth,month_of_birth,date_of_birth,provider_no FROM demographic WHERE LOWER(last_name) REGEXP ?  AND patient_status NOT IN ('IN','DE','IC','ID','MO','FI') ORDER BY last_name, first_name"},
  };
	}else{
	dbQueries=new String[][] {
    {"provider_search_titlename", "select provider_no,first_name,last_name,specialty,sex,team,phone,status from provider where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},
    {"provider_search_detail", "select * from provider where provider_no=?"},
    {"search_provider_doc", "select * from provider where provider_type='doctor' and status='1' order by last_name"},

    {"demographic_search_titlename", "select demographic_no,first_name,last_name,roster_status,sex,year_of_birth,month_of_birth,date_of_birth  from demographic where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},
    {"demographic_search_merged", "select d.demographic_no,first_name,last_name,roster_status,sex,year_of_birth,month_of_birth,date_of_birth  from demographic d join demographic_merged dm on d.demographic_no = dm.demographic_no and dm.deleted = 0 where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},
    {"demographic_search_detail", "select * from demographic where demographic_no=?"},
    {"demographic_search_demoaddno", "select demographic_no from demographic where last_name=? and first_name =? and year_of_birth=? and month_of_birth=? and date_of_birth=? and hin=? and ver=?"},
    {"search_lastfirstnamedob", "select demographic_no from demographic where last_name=? and first_name=? and year_of_birth=? and month_of_birth=? and date_of_birth=?"},

    {"security_search_titlename", "select * from security where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},
    {"security_search_detail", "select * from security where security_no=?"},

    {"searchmygroupcount", "select count(provider_no) from mygroup where mygroup_no=? "},
    {"searchmygroupprovider", "select provider_no, last_name, first_name from mygroup where mygroup_no=? "},
    {"searchmygroupall", "select * from mygroup order by mygroup_no"},
    {"searchprovider", "select provider_no, last_name, first_name from provider where provider_type='doctor' and status='1' order by last_name"},
    {"searchproviderall", "select provider_no, last_name, first_name from provider where status='1' order by last_name"},

    {"search_ptstatus", "select distinct patient_status from demographic where patient_status != '' and patient_status != 'AC' and patient_status != 'IN' and patient_status != 'DE' and patient_status != 'MO' and patient_status != 'FI'"},
    {"search_rsstatus", "select distinct roster_status from demographic where roster_status != '' and roster_status != 'RO' and roster_status != 'NR' and roster_status != 'TE' and roster_status != 'FS' "},
    {"search_wlstatus", "select * from waitingList where demographic_no=? AND is_history='N' order by onListSince DESC"},
    {"search_waiting_list", "select * from waitingListName where group_no='" + ((ProviderPreference)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE)).getMyGroupNo() +"' AND is_history='N' order by name"},
    //muti-site query
    {"site_searchproviderall","select p.provider_no, p.last_name, p.first_name from provider p INNER JOIN providersite s ON p.provider_no = s.provider_no WHERE s.site_id IN (SELECT site_id from providersite where provider_no=?) AND p.status='1' order by last_name"},
    {"site_searchmygroupall" ,"select p.* from mygroup p INNER JOIN providersite s ON p.provider_no = s.provider_no WHERE s.site_id IN (SELECT site_id from providersite where provider_no=?) order by mygroup_no"},
    {"site_preference_search_titlename", "select p.* from preference p INNER JOIN providersite s ON p.provider_no = s.provider_no where p."+fieldname+ " "+regularexp+" ?  AND s.site_id IN (SELECT site_id from providersite where provider_no=?) " +orderby + " "+limit},
    {"site_security_search_titlename", "select p.* from security p INNER JOIN providersite s ON p.provider_no = s.provider_no where p."+fieldname+ " "+regularexp+" ?  AND s.site_id IN (SELECT site_id from providersite where provider_no= " + curProvider_no + " ) " +orderby + " "+limit},
    {"site_provider_search_titlename", "select p.provider_no,p.first_name,p.last_name,p.specialty,p.sex,p.team,p.phone,p.status from provider p where "+fieldname+ " "+regularexp+" ? and exists(select * from providersite s where p.provider_no = s.provider_no and s.site_id IN (SELECT site_id from providersite where provider_no=" + curProvider_no + ")) "  +orderby + " "+limit},
    {"demographic_admin_reports","SELECT demographic_no,first_name,last_name,roster_status,sex,chart_no,year_of_birth,month_of_birth,date_of_birth,provider_no FROM demographic WHERE LOWER(last_name) REGEXP ?  AND patient_status NOT IN ('IN','DE','IC','ID','MO','FI') ORDER BY last_name, first_name"},
  };
}

  //associate each operation with an output JSP file - displaymode
  String[][] responseTargets=new String[][] {
    {"Provider_Add_Record" , "provideraddarecord.jsp"},
    {"Provider_Search" , "providersearchresults.jsp"},
    {"Provider_Update" , "providerupdateprovider.jsp"},
    {"Provider_Update_Record" , "providerupdate.jsp"},
    {"Demographic_Edit2" , "../demographic/demographiceditdemographic.jsp"},
    {"Demographic_Update" , "demographicupdatearecord.jsp"},
    {"Demographic_Merge" , "demographicmergerecord.jsp"},
    {"Security_Add_Record" , "securityaddsecurity.jsp"},
    {"Security_Search" , "securitysearchresults.jsp"},
    {"Security_Update" , "securityupdatesecurity.jsp"},
    {"Security_Delete" , "securitydelete.jsp"},
    {"Security_Update_Record" , "securityupdate.jsp"},
    {"Preference_Add_Record" , "preferenceaddpreference.jsp"},
    {"Preference_Search" , "preferencesearchresults.jsp"},
    {"Preference_Delete" , "preferencedelete.jsp"},
    {"Preference_Update_Record" , "preferenceupdate.jsp"},
    {"Demographic_Admin_Reports" , "../demographic/demographicsearch2reportresults.jsp"},

    {"displaymygroup" , "admindisplaymygroup.jsp"},
    {"newgroup" , "adminnewgroup.jsp"},
    {"savemygroup" , "adminsavemygroup.jsp"},

    {"reboot_confirmation", "rebootConfirmation.jsp"},
  };
  apptMainBean.doConfigure(dbQueries,responseTargets);
  apptMainBean.doCommand(request); //store request to a help class object Dict - function&params
  if(true) {
    out.clear();
    String xx=apptMainBean.whereTo();
    pageContext.forward(apptMainBean.whereTo()); //forward request&response to the target page
    return;
  }
%>
