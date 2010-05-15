<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page
	import="java.sql.*, oscar.login.*, java.util.*,oscar.*,oscar.oscarDB.*,oscar.util.SqlUtils,oscar.oscarProvider.data.ProviderBillCenter"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
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
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.providerupdate.title" /></title>
</head>
<link rel="stylesheet" href="../web.css" />

<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.providerupdate.description" /></font></th>
	</tr>
</table>

<% 
  ProviderBillCenter billCenter = new ProviderBillCenter();
  billCenter.updateBillCenter(request.getParameter("provider_no"),request.getParameter("billcenter")); 
  
  DBPreparedHandlerParam[] param =new DBPreparedHandlerParam[20];
  param[0]=new DBPreparedHandlerParam(request.getParameter("last_name"));
  param[1]=new DBPreparedHandlerParam(request.getParameter("first_name"));
  param[2]=new DBPreparedHandlerParam(request.getParameter("provider_type"));
  param[3]=new DBPreparedHandlerParam(request.getParameter("specialty"));
  param[4]=new DBPreparedHandlerParam(request.getParameter("team"));
  param[5]=new DBPreparedHandlerParam(request.getParameter("sex"));
  
  param[6]=new DBPreparedHandlerParam(MyDateFormat.getSysDate(request.getParameter("dob")));
//  String strDbType = oscar.OscarProperties.getInstance().getProperty("db_type").trim();
//  if("oracle".equalsIgnoreCase(strDbType)){
//  	param[6] = SqlUtils.isoToOracleDate(param[6]);
//  }

  param[7]=new DBPreparedHandlerParam(request.getParameter("address"));
  param[8]=new DBPreparedHandlerParam(request.getParameter("phone"));
  param[9]=new DBPreparedHandlerParam(request.getParameter("workphone"));
  param[10]=new DBPreparedHandlerParam(request.getParameter("email"));
  param[11]=new DBPreparedHandlerParam(request.getParameter("ohip_no"));
  param[12]=new DBPreparedHandlerParam(request.getParameter("rma_no"));
  param[13]=new DBPreparedHandlerParam(request.getParameter("billing_no"));
  param[14]=new DBPreparedHandlerParam(request.getParameter("hso_no"));
  param[15]=new DBPreparedHandlerParam(request.getParameter("status"));
  param[16]=new DBPreparedHandlerParam(SxmlMisc.createXmlDataString(request,"xml_p"));
  param[17]=new DBPreparedHandlerParam(request.getParameter("provider_activity"));
  param[18]=new DBPreparedHandlerParam(request.getParameter("practitionerNo"));
  param[19]=new DBPreparedHandlerParam(request.getParameter("provider_no"));
  
  int rowsAffected = apptMainBean.queryExecuteUpdate(param, request.getParameter("dboperation"));
  if (rowsAffected ==1) {
	  if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) {
		String[] sites = request.getParameterValues("sites");
		DBPreparedHandler dbObj = new DBPreparedHandler();
		String provider_no = request.getParameter("provider_no");
		dbObj.queryExecuteUpdate("delete from providersite where provider_no = ?", new String[]{provider_no});
		if (sites!=null) {
			for (int i=0; i<sites.length; i++) {
				dbObj.queryExecuteUpdate("insert into providersite (provider_no, site_id) values (?,?)", new String[] {provider_no, String.valueOf(sites[i])});
			}
		}
	  }
%>
<p>
<h2><bean:message key="admin.providerupdate.msgUpdateSuccess" /><a
	href="admincontrol.jsp?keyword=<%=request.getParameter("provider_no")%>&displaymode=Provider_Update&dboperation=provider_search_detail"><%= request.getParameter("provider_no") %></a>
</h2>
<%  
  } else {
%>
<h1><bean:message key="admin.providerupdate.msgUpdateFailure" /><%= request.getParameter("provider_no") %>.
<%  
  }
  apptMainBean.closePstmtConn(); 
%>
<p></p>
<%@ include file="footer2htm.jsp"%>
</center>
</body>
</html:html>
