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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String providerNo=loggedInInfo.getLoggedInProviderNo();

  String providername = request.getParameter("providername")!=null?request.getParameter("providername"):"";
  String year = request.getParameter("pyear")!=null?request.getParameter("pyear"):"2002";
  String month = request.getParameter("pmonth")!=null?request.getParameter("pmonth"):"5";
  String day = request.getParameter("pday")!=null?request.getParameter("pday"):"8";
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.MyGroupDao" %>
<%@page import="org.oscarehr.common.model.MyGroup" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.common.dao.MyGroupAccessRestrictionDao" %>
<%@page import="org.oscarehr.common.model.MyGroupAccessRestriction" %>
<%
	MyGroupDao myGroupDao = SpringUtils.getBean(MyGroupDao.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	MyGroupAccessRestrictionDao myGroupAccessRestrictionDao = SpringUtils.getBean(MyGroupAccessRestrictionDao.class);
%>

<%
  String curUser_no = (String) session.getAttribute("user");

  ProviderPreference providerPreference=(ProviderPreference)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE);
  int startHour=providerPreference.getStartHour();
  int endHour=providerPreference.getEndHour();
  int everyMin=providerPreference.getEveryMin();

  String defaultServiceType = (String) session.getAttribute("default_servicetype");
  if( defaultServiceType == null ) {
	ProviderPreference providerPreferenceInDb=ProviderPreferencesUIBean.getProviderPreference(providerNo);
      if (providerPreferenceInDb!=null) {
        defaultServiceType = providerPreference.getDefaultServiceType();
      }
  }
  
  if( defaultServiceType == null ) {
    defaultServiceType = "";
  }

  String n_t_w_w=null;
  if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
  	n_t_w_w = (String) session.getAttribute("newticklerwarningwindow");
  }
  boolean caisi = Boolean.valueOf(request.getParameter("caisi")).booleanValue();
  
  String form = null;
  String elementName = null;
  String elementId = null;
 
  String custom = request.getParameter("custom");
  if(custom !=null && custom.equals("true")) {
	  form = request.getParameter("form");
	  elementName = request.getParameter("elementName");
	  elementId = request.getParameter("elementId");
  }
  
  List<MyGroupAccessRestriction> restrictions = myGroupAccessRestrictionDao.findByProviderNo(curUser_no);
 
%>

<%@page import="org.oscarehr.common.model.ProviderPreference"%>
<%@page import="org.oscarehr.web.admin.ProviderPreferencesUIBean"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="receptionist.receptionistfindprovider.title" /></title>
<link rel="stylesheet" href="../web.css">
<script language="JavaScript">


function selectProvider(p,pn) {
	  newGroupNo = p;
<%if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){%>
	  this.location.href = "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&new_tickler_warning_window=<%=n_t_w_w%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&default_servicetype=<%=defaultServiceType%>&mygroup_no="+newGroupNo;
<%}else{%>
	  this.location.href = "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&default_servicetype=<%=defaultServiceType%>&mygroup_no="+newGroupNo;
<%}%>
}

function selectProviderCaisi(p,pn) {	
	opener.document.ticklerForm.elements['tickler.taskAssignedToName'].value=pn;
	opener.document.ticklerForm.elements['tickler.taskAssignedTo'].value=p;
	self.close();
}

function selectProviderCustom(p,pn) {
	opener.document.<%=form%>.elements['<%=elementName%>'].value=pn;
	opener.document.<%=form%>.elements['<%=elementId%>'].value=p;
	self.close();
}
</SCRIPT>
</head>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()"
	topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<th NOWRAP bgcolor="#CCCCFF"><font face="Helvetica"><bean:message
			key="receptionist.receptionistfindprovider.2ndtitle" /></font></th>
	</tr>
</table>

<table width="100%" border="0">
	<tr>
		<td align="left"><i><bean:message
			key="receptionist.receptionistfindprovider.keywords" /></i> <%=providername%></td>
		<td align="right"><INPUT TYPE="SUBMIT" NAME="displaymode"
			VALUE="<bean:message key="receptionist.receptionistfindprovider.btnExit"/>"
			SIZE="17" onClick="window.close();"></td>
	</tr>
</table>

<CENTER>
<table width="100%" border="1" bgcolor="#ffffff" cellspacing="1"
	cellpadding="0">
	<tr bgcolor="#CCCCFF">
		<TH width="20%"><bean:message
			key="receptionist.receptionistfindprovider.no" /></TH>
		<TH width="40%"><bean:message
			key="receptionist.receptionistfindprovider.lastname" /></TH>
		<TH width="40%"><bean:message
			key="receptionist.receptionistfindprovider.firstname" /></TH>
	</tr>
<%
  boolean bGrpSearch = providername.startsWith(".")?true:false ;
  String dboperation = bGrpSearch?"search_providersgroup":"search_provider" ;
  String field1 = bGrpSearch?"mygroup_no":"provider_no" ;
  providername = bGrpSearch?providername.substring(1):providername ;

  String bgcolordef = "#EEEEFF" ;
  boolean bColor = true;
  String [] param = new String[2];
  if(providername.indexOf(",")>0) {
    param[0]= providername.substring(0,providername.indexOf(",")).trim() + "%";
    param[1]= providername.substring(providername.indexOf(",")+1).trim() + "%";
  } else {
    param[0]= providername.trim() + "%";
    param[1]= "%" ;
  }
  
  int nItems = 0;
  String sp =null, spnl =null, spnf =null;
  
  Collection results = null;
  if(bGrpSearch) {
	  results = myGroupDao.search_providersgroup(param[0],param[1]);
  } else {
	  results = providerDao.getActiveProviderLikeFirstLastName(param[1],param[0]);
  }
  
  Iterator iter = results.iterator();
  
  while(iter.hasNext()) {
	  Object o = iter.next();
	  Provider p = null;
	  MyGroup g = null;
	  if(bGrpSearch) {
		  g = (MyGroup)o;
		  sp = String.valueOf(g.getId().getMyGroupNo());
		  spnl = String.valueOf(p.getLastName());
		  spnf = String.valueOf(p.getFirstName());
		  if(checkRestriction(restrictions,g.getId().getMyGroupNo())) {
			  continue;
		  }
  
	  }
	  else {
		  p = (Provider)o;
		  sp = String.valueOf(p.getProviderNo());
		  spnl = String.valueOf(p.getLastName());
		  spnf = String.valueOf(p.getFirstName());
		  if(checkRestriction(restrictions,p.getProviderNo())) {
			  continue;
		  }
	  }
     bColor = bColor?false:true ;
   
%>
	<tr bgcolor="<%=bColor?bgcolordef:"white"%>">
		<td>
		<%if(caisi) { %> <a href=#
			onClick="selectProviderCaisi('<%=sp%>','<%=spnl+", "+spnf%>')"><%=sp%></a></td>
		<% } else if(custom != null && custom.equals("true")) { %>
			<a href="#" onClick="selectProviderCustom('<%=sp%>','<%=spnl+", "+spnf%>')"><%=sp%></a></td>
		<%} else { %>
		<a href=#
			onClick="selectProvider('<%=sp%>','<%=URLEncoder.encode(spnl+", "+spnf)%>')"><%=sp%></a>
		</td>
		<%} %>
		<td><%=spnl%></td>
		<td><%=spnf%></td>
		<caisi:isModuleLoad moduleName="ticklerplus">
			<input type="hidden" name="<%=sp%>_name" id="<%=sp%>_name"
				value="<%=new String(spnl+","+ spnf)%>" />
		</caisi:isModuleLoad>
	</tr>
	<%
    nItems++;
  }
  
  //find a group name only if there is no ',' in the search word 
  if(providername.indexOf(',') == -1 ) {
	for(MyGroup mg:myGroupDao.search_mygroup(providername+"%")) {
	
		if(checkRestriction(restrictions,mg.getId().getMyGroupNo())) {
			  continue;
		  }
		
      sp = String.valueOf(mg.getId().getMyGroupNo());
%>
	<tr bgcolor="#CCCCFF">
		<td colspan='3'>
		<%if(caisi) { %> <a href=# onClick="selectProviderCaisi('<%=sp%>','')"><%=sp%></a></td>
		<%} else { %>
		<a href=# onClick="selectProvider('<%=sp%>','')"><%=sp%></a>
		</td>
		<%} %>
	</tr>
	<%
      nItems++;
    }
  }
  
  if(nItems==1) { //if there is only one search result, it should go to the appoint page directly.
%>
	<script language="JavaScript">
<!--
  <%if(caisi) {%>
  	var nodes = document.getElementsByName("<%=sp%>_name");
  	var name = '';
  	if(nodes.length == 1) {
  		name = nodes[0].value;
  	}
  	selectProviderCaisi('<%=sp%>',name) ;
  	 <%} else if(custom != null && custom.equals("true")){%>
  	 selectProviderCustom('<%=sp%>',name);
  <%} else {%>
  selectProvider('<%=sp%>','') ;
  <%}%>
//-->
</SCRIPT>
<%
  }
%>
</table>
<br>

<p><bean:message
	key="receptionist.receptionistfindprovider.msgSelect" /></p>
</center>
</body>
</html>

<%!public boolean checkRestriction(List<MyGroupAccessRestriction> restrictions, String name) {
     for(MyGroupAccessRestriction restriction:restrictions) {
             if(restriction.getMyGroupNo().equals(name))
                     return true;
     }
     return false;
  }
%>

        