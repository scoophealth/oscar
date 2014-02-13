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

<%
  
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*" errorPage="../appointment/errorpage.jsp"%>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>


<%
    String curProvider_no = (String) session.getAttribute("user");
	String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<%  
  GregorianCalendar now=new GregorianCalendar();
  int year = now.get(Calendar.YEAR);
  int month = (now.get(Calendar.MONTH)+1);
  int day = now.get(Calendar.DAY_OF_MONTH);
  
%>

<%
    boolean isSiteAccessPrivacy=false;
    boolean isTeamAccessPrivacy=false; 
    
%>
<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%
		isSiteAccessPrivacy=true;
		
	%>
</security:oscarSec>
<security:oscarSec objectName="_team_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%
		isTeamAccessPrivacy=true; 
		
	%>
</security:oscarSec>


<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="schedule.scheduletemplatesetting.title" /></title>
<link rel="stylesheet" href="../web.css" />

<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
}

function selectprovider(s) {
	self.location.href = "scheduletemplateapplying.jsp?provider_no="+s.options[s.selectedIndex].value+"&provider_name="+urlencode(s.options[s.selectedIndex].text);
}
function urlencode(str) {
	var ns = (navigator.appName=="Netscape") ? 1 : 0;
	if (ns) { return escape(str); }
	var ms = "%25#23 20+2B?3F<3C>3E{7B}7D[5B]5D|7C^5E~7E`60";
	var msi = 0;
	var i,c,rs,ts ;
	while (msi < ms.length) {
		c = ms.charAt(msi);
		rs = ms.substring(++msi, msi +2);
		msi += 2;
		i = 0;
		while (true)	{ 
			i = str.indexOf(c, i);
			if (i == -1) break;
			ts = str.substring(0, i);
			str = ts + "%" + rs + str.substring(++i, str.length);
		}
	}
	return str;
}

function go() {
  var s = document.schedule.providerid.value ;
  var u = 'scheduleedittemplate.jsp?providerid=' + s +'&providername='+urlencode(document.schedule.providerid.options[document.schedule.providerid.selectedIndex].text);
	popupPage(390,700,u);
}
//-->
</script>
</head>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()"
	topmargin="0" leftmargin="0" rightmargin="0">
<form method="post" name="schedule" action="schedulecreatedate.jsp">

<table border="0" width="100%">
	<tr>
		<td width="150" bgcolor="#009966"><!--left column-->
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr bgcolor="#486ebd">
				<th align="CENTER" bgcolor="#009966">
				<p>&nbsp;</p>
				<p><font face="Helvetica" color="#FFFFFF"><bean:message
					key="schedule.scheduletemplatesetting.msgMainLabel" /></font></p>
				</th>
			</tr>
		</table>
		<table width="98%" border="0" cellspacing="0" cellpadding="0"
			height="500">
			<tr>
				<td>
				<p>&nbsp;</p>
				<p><font size="-1"><bean:message
					key="schedule.scheduletemplatesetting.msgStepOne" /></font></p>
				<p><font size="-1"><bean:message
					key="schedule.scheduletemplatesetting.msgStepTwo" /></font></p>
				<p>&nbsp;</p>
				<p>&nbsp;</p>
				<p>&nbsp;</p>
				<p>&nbsp;</p>
				</td>
			</tr>
		</table>

		</td>
		<td>

		<center>
		<table width="95%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><bean:message
					key="schedule.scheduletemplatesetting.formSelectProvider" />:</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td bgcolor="#CCFFCC">&nbsp; <select name="provider_no"
					onChange="selectprovider(this)">
					<option value=""><bean:message
						key="schedule.scheduletemplatesetting.msgNoProvider" /></option>
						
						<%
							ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
							List<Provider> providers = providerDao.getActiveProviders();
							//TODO: filter by site/team if necessary
							
							for(Provider p:providers) {
						%>
							<option value="<%=p.getProviderNo()%>"><%=p.getFormattedName()%></option>
							
						<% } %>
				
				</select></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
				<p><bean:message key="schedule.scheduletemplatesetting.formOrDo" />:</p>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		<%if (!( isSiteAccessPrivacy  || isTeamAccessPrivacy )) {%>
			<tr>
				<td nowrap bgcolor="#CCFFCC">&nbsp; <a HREF="#"
					ONCLICK="popupPage(440,530,'scheduleholidaysetting.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>')"
					TITLE='<bean:message key="schedule.scheduletemplatesetting.msgHolidaySettingTip"/>;return true'><bean:message
					key="schedule.scheduletemplatesetting.btnHolidaySetting" /></a></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>

				<td nowrap bgcolor="#CCFFFF">&nbsp; <a HREF="#"
					ONCLICK="popupPage(600,700,'scheduletemplatecodesetting.jsp')"><bean:message
					key="schedule.scheduletemplatesetting.btnTemplateCodeSetting" /></a></td>
					

			</tr>
		<%} %>				
			<tr>
				<td nowrap bgcolor="#CCFFFF">&nbsp; <a HREF="#" onClick="go()"><bean:message
					key="schedule.scheduletemplatesetting.btnTemplateSetting" /></a>&nbsp;<bean:message
					key="schedule.scheduletemplatesetting.msgForProvider" />&nbsp; <select
					name="providerid">
					<option value="Public"><bean:message
						key="schedule.scheduletemplatesetting.msgPublic" /></option>
<%
							for(Provider p:providers) {
						%>
							<option value="<%=p.getProviderNo()%>"><%=p.getFormattedName()%></option>
							
						<% } %>
						
				</select></td>
			</tr>
			<tr>
				<td>
				<div align="left"></div>
				</td>
			</tr>
			<tr>
				<td>
				<div align="right"></div>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<p>
		</center>
		</td>
	</tr>
</table>

</form>
</body>
</html:html>
