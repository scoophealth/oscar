<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@ page import="java.util.*, java.sql.*" errorPage="errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.common.model.Provider"%>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao"%>

<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
%>


<%
GregorianCalendar now=new GregorianCalendar();
int curYear = now.get(Calendar.YEAR);
int curMonth = (now.get(Calendar.MONTH)+1);
int curDay = now.get(Calendar.DAY_OF_MONTH);

String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);

String proFirst1="", proLast1="", proOHIP1="", proNo="";
int Count = 0;
Provider p = providerDao.getProvider(request.getParameter("creator"));
if(p != null) {
	proFirst1 = p.getFirstName();
	proLast1 = p.getLastName();
	proOHIP1 = p.getProviderNo();
}

%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>INR BILLING</title>
<script language="JavaScript">
<!--



var remote=null;

function rs(n,u,w,h,x) {
  args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
  remote=window.open(u,n,args);
  if (remote != null) {
    if (remote.opener == null)
      remote.opener = self;
  }
  if (x == 1) { return remote; }
}


var awnd=null;
function ScriptAttach() {
  f0 = escape(document.serviceform.xml_diagnostic_detail.value);
  f1 = document.serviceform.xml_dig_search1.value;
 // f2 = escape(document.serviceform.elements["File2Data"].value);
 // fname = escape(document.Compose.elements["FName"].value);
  awnd=rs('att','../billingDigSearch.jsp?name='+f0 + '&search=' + f1,600,600,1);
  awnd.focus();
}

function OtherScriptAttach() {
  t0 = escape(document.serviceform.xml_other1.value);
 // t1 = escape(document.serviceform.xml_other2.value);
 // t2 = escape(document.serviceform.xml_other3.value);
 // f1 = document.serviceform.xml_dig_search1.value;
 // f2 = escape(document.serviceform.elements["File2Data"].value);
 // fname = escape(document.Compose.elements["FName"].value);
  awnd=rs('att','../billingCodeSearch.jsp?name='+t0 + '&name1=' + "" + '&name2=' + "" + '&search=',600,600,1);
  awnd.focus();
}
//-->
</script>
<link rel="stylesheet" href="../../web.css" />
</head>

<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">ADD
		INR BILLING</font></th>
	</tr>
</table>
<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%"
	BGCOLOR="#C4D9E7">
	<FORM NAME="serviceform" ACTION="reviewINRbilling.jsp" METHOD="POST">
	<tr valign="top">
		<td rowspan="2" ALIGN="right" valign="middle">
		<div align="center">
		<p>&nbsp;</p>
		<table width="90%" border="1" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="3"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"></font></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000">Demographic id</font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="functionid"
					value="<%=request.getParameter("functionid")%> " size="20">
				</font></td>
				<td rowspan="8" width="21%" valign="middle">
				<p><br>
				</p>
				</td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000">Demographic Name </font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="demo_name"
					value="<%=request.getParameter("demographic_name")%>" size="20">
				</font></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000">Demographic DOB </font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="demo_dob"
					value="<%=request.getParameter("dob")%>" size="20"> </font></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000">Health Number </font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="demo_hin"
					value="<%=request.getParameter("hin")%>" size="20"> </font></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000">Billing Provider</font></td>
				<td width="50%"><select name="provider">
					<option value=""
						<%=request.getParameter("creator").equals("")?"selected":""%>>Select
					Provider</option>

					<% 
String proFirst="";
String proLast="";
String proOHIP="";
String specialty_code; 
String billinggroup_no;

for(Provider prov: providerDao.getActiveProviders()){
	if(prov.getOhipNo() == null || prov.getOhipNo().isEmpty())
		continue;
	
	proFirst = prov.getFirstName();
	proLast =prov.getLastName();
	proOHIP = prov.getOhipNo();
	specialty_code = prov.getProviderNo();
%>
					<option value="<%=proOHIP%>|<%=specialty_code%>"
						<%=request.getParameter("creator").equals(specialty_code)?"selected":""%>><%=proLast%>,
					<%=proFirst%></option>
					<% 
}
%>
				</select> </font></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000">Service Code </font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="xml_other1" size="20" value="G271A"> <a
					href="javascript:OtherScriptAttach()"><img
					src="../../../../images/search_code.jpg" border="0"></a> </font></td>
			</tr>
			<tr>
				<td width="29%"><font size="1">Diagnostic Code</font></td>
				<td width="50%"><input type="text" name="xml_diagnostic_detail"
					size="20" value="451"><input type="hidden"
					name="xml_dig_search1"> <a
					href="javascript:ScriptAttach()"><img
					src="../../../../images/search_dx_code.jpg" border="0"></a></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000">Create Date</font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="docdate" readonly value="<%=nowDate%>" size="20">
				</font></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" color="#000000"
					size="1">Creator </font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="dispcreator" readonly
					value="<%=proLast1%>, <%=proFirst1%>" size="20"> <input
					type="hidden" name="doccreator"
					value="<%=request.getParameter("creator")%>" size="20"> <input
					type="hidden" name="orderby" value="updatedatetime desc" size="20">
				</font></td>
			</tr>
			<tr>
				<td colspan="2"><font
					face="Verdana, Arial, Helvetica, sans-serif" color="#0000FF"
					size="1"><b><i> <input type="SUBMIT" value="Submit"
					name="SUBMIT"> </i></b></font><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"></font></td>
			</tr>
		</table>
		<p><font face="Verdana" color="#0000FF"><b><i> </i></b></font> <br>
		</p>
		</div>
		</td>
	</tr>
	</form>
</table>
<input type="button" name="Button" value="Cancel" onclick=self.close();>
</body>
</html>
