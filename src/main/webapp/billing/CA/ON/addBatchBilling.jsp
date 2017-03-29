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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.SpringUtils"%>


<%@ page import="java.util.*"%>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao, org.oscarehr.common.model.Provider" %>
<%@ page import="oscar.SxmlMisc" %>

<%@include  file="../../../casemgmt/taglibs.jsp"%>

<%
GregorianCalendar now=new GregorianCalendar();
int curYear = now.get(Calendar.YEAR);
int curMonth = (now.get(Calendar.MONTH)+1);
int curDay = now.get(Calendar.DAY_OF_MONTH);

String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);

int Count = 0;

ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
Provider creator = providerDao.getProvider(request.getParameter("creator"));

%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="billing.batchbilling.title"/></title>
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
  awnd=rs('att','billingDigSearch.jsp?name='+f0 + '&search=' + f1,600,600,1);
  awnd.focus();
}

function OtherScriptAttach() {
  t0 = escape(document.serviceform.xml_other1.value);
 // t1 = escape(document.serviceform.xml_other2.value);
 // t2 = escape(document.serviceform.xml_other3.value);
 // f1 = document.serviceform.xml_dig_search1.value;
 // f2 = escape(document.serviceform.elements["File2Data"].value);
 // fname = escape(document.Compose.elements["FName"].value);
  awnd=rs('att','billingCodeSearch.jsp?name='+t0 + '&name1=' + "" + '&name2=' + "" + '&search=',600,600,1);
  awnd.focus();
}

function checkCodes() {
	var serviceCodeLength = 5;
	var dxCodeLength = 3;
	var ret = true;
	var msg = "";
	var tmp = document.serviceform.xml_other1.value;
	document.serviceform.xml_other1.value = tmp.replace(/\s+/g,"");
	tmp = document.serviceform.xml_diagnostic_detail.value;
	document.serviceform.xml_diagnostic_detail.value = tmp.replace(/^\s+|\s+$/g,"");
	
	if( document.serviceform.xml_other1.value.length != serviceCodeLength ) {
		msg += "<bean:message key="billing.batchbilling.noServicecodeErr"/>";
		ret = false;
	}
	
	if( document.serviceform.xml_diagnostic_detail.value.length != dxCodeLength ) {
		if( document.serviceform.xml_diagnostic_detail.value.indexOf("|") != dxCodeLength  ) {
			msg += "\n<bean:message key="billing.batchbilling.noDxCodeErr"/>";
			ret = false;
		}
	}
	
	if( document.serviceform.provider[document.serviceform.provider.selectedIndex].value == "" ) {
		msg += "\n<bean:message key="billing.batchbilling.noProviderErr"/>";
		ret = false;
	}
	
	if( msg.length > 0 ) {
		alert(msg);
	}
	return ret;
	
}
//-->
</script>

</head>

<body
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">
			<bean:message key="billing.batchbilling.header"/></font></th>
	</tr>
</table>
<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%"
	BGCOLOR="#C4D9E7">
	<FORM NAME="serviceform" ACTION="BatchBill.do" METHOD="POST" onsubmit="return checkCodes();">
	<input type="hidden" name="method" value="add">
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
					color="#000000"><bean:message key="billing.batchbilling.demographic"/></font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="demographic_no" readonly
					value="<%=request.getParameter("demographic_no").trim()%> " size="20">
				</font></td>
				<td rowspan="8" width="21%" valign="middle">
				<p><br>
				</p>
				</td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000"><bean:message key="billing.batchbilling.demographicName"/></font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="demo_name" readonly
					value="<%=request.getParameter("demographic_name")%>" size="20">
				</font></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000"><bean:message key="billing.batchbilling.demographicDOB"/></font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="demo_dob" readonly
					value="<%=request.getParameter("dob")%>" size="20"> </font></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000"><bean:message key="billing.batchbilling.demographicHIN"/></font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="demo_hin" readonly
					value="<%=request.getParameter("hin")%>" size="20"> </font></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000"><bean:message key="billing.batchbilling.billingProvider"/></font></td>
				<td width="50%"><select name="provider">
					<option value=""
						<%=request.getParameter("creator").equals("")?"selected":""%>>Select
					Provider</option>

					<% 

List<Provider>providers = providerDao.getBillableProviders();
String proFirst, proLast, proNo;
for(Provider p: providers){
	proFirst = p.getFirstName();
	proLast = p.getLastName();
	
	//  billinggroup_no= SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_billinggroup_no>","</xml_p_billinggroup_no>");
	// specialty_code = SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_specialty_code>","</xml_p_specialty_code>");
	proNo = p.getProviderNo(); 
%>
					<option value="<%=proNo%>"
						<%=request.getParameter("creator").equals(proNo)?"selected":""%>><%=proLast%>,
					<%=proFirst%></option>
					<% 
}
%>
				</select> </font></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000"><bean:message key="billing.batchbilling.serviceCode"/></font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="xml_other1" size="20" value=""> <a
					href="javascript:OtherScriptAttach()"><img
					src="../../../images/search_code.jpg" border="0"></a> </font></td>
			</tr>
			<tr>
				<td width="29%"><font size="1"><bean:message key="billing.batchbilling.DxCode"/></font></td>
				<td width="50%"><input type="text" name="xml_diagnostic_detail"
					size="20" value=""><input type="hidden"
					name="xml_dig_search1"> <a
					href="javascript:ScriptAttach()"><img
					src="../../../images/search_dx_code.jpg" border="0"></a></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000"><bean:message key="billing.batchbilling.CreateDate"/></font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="createdate" readonly value="<%=nowDate%>" size="20">
				</font></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" color="#000000"
					size="1"><bean:message key="billing.batchbilling.Creator"/></font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="dispcreator" readonly
					value="<%=creator.getFormattedName()%>" size="20"> <input
					type="hidden" name="creator"
					value="<%=request.getParameter("creator")%>" size="20">
				</font></td>
			</tr>
			<tr>
				<td colspan="2"><font
					face="Verdana, Arial, Helvetica, sans-serif" color="#0000FF"
					size="1"><b><i> <input type="SUBMIT" value="<bean:message key="billing.batchbilling.submit"/>"
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
<input type="button" name="Button" value="<bean:message key="billing.batchbilling.cancel"/>" onclick=self.close();>
</body>
</html>
