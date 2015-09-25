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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page
	import="oscar.util.UtilMisc,oscar.oscarEncounter.data.*,java.net.*,java.util.*"%>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String provNo = request.getParameter("provider_no");
	String demoNo = request.getParameter("demographic_no");
    String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF", tableTitle = "#99ccff";
	String strLimit1="0";
	String strLimit2="10";  
	if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");  
	if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
%>

<%
	EctFormData.Form[] forms = EctFormData.getForms();
	oscar.util.UtilDateUtilities dateConvert = new oscar.util.UtilDateUtilities();
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarEncounter.formlist.title" /></title>
<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<script type="text/javascript" language=javascript>

function popupPageK(page) {
    windowprops = "height=700,width=960,location=no,"
    + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
    var popup = window.open(page, "formhistory", windowprops);
    popup.focus();
    
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
    function popupStart(vheight,vwidth,varpage) {
      var page = varpage;
      windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
      var popup=window.open(varpage, "", windowprops);
      if (popup != null) {
        if (popup.opener == null) {
          popup.opener = self;
        }
      }
    }
</script>
<body bgcolor="ivory" onLoad="setfocus()" topmargin="0" leftmargin="0"
	rightmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="<%=deepcolor%>">
		<th><font face="Helvetica"><bean:message
			key="oscarEncounter.formlist.msgFormList" /></font></th>
	</tr>
</table>
<center>
<table BORDER="0" CELLPADDING="2" CELLSPACING="2" WIDTH="60%"
	BGCOLOR="white">
	<tr BGCOLOR="<%=tableTitle%>">
		<th width=30% nowrap><bean:message
			key="oscarEncounter.formlist.formName" /></th>
		<th width=11% nowrap><bean:message
			key="oscarEncounter.formlist.formCreated" /></th>
		<th width=11% nowrap><bean:message
			key="oscarEncounter.formlist.formEditedTime" /></th>
	</tr>

	<%
	for(int j=0; j<forms.length; j++) {
		EctFormData.Form frm = forms[j];
		String table = frm.getFormTable();
                table = org.apache.commons.lang.StringEscapeUtils.escapeSql(table);
                                
		EctFormData.PatientForm[] pforms;
                if( table.length() == 0 ) {
                    pforms = new EctFormData.PatientForm[0];
                }
                else {
                    pforms = EctFormData.getPatientFormsFromLocalAndRemote(loggedInInfo, demoNo, table);
                }
		int nItems = 0;

		for(int i=0; i<pforms.length; i++) {
			nItems++;
			EctFormData.PatientForm pfrm = pforms[i];
%>
	<tr
		bgcolor='<%= j%2 == 0 ? (i%2 == 0 ?weakcolor:deepcolor) : (i%2 == 0 ?"white":"#eeeeee")%>'>
		<td><a href=#
			onClick="popupPageK('<%=frm.getFormPage()+demoNo+"&formId="+pfrm.getFormId()+"&provNo="+provNo+(pfrm.getRemoteFacilityId()!=null?"&remoteFacilityId="+pfrm.getRemoteFacilityId():"")%>'); return false;"><%=frm.getFormName()+(pfrm.getRemoteFacilityId()!=null?" (remote)":"")%></a></td>
		<td align='center'><%=pfrm.getCreated()%></td>
		<td align='center'><%=pfrm.getEdited()%></td>
	</tr>
	<%
		}
%>
	<%
	int nLastPage=0,nNextPage=0;
	nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
	nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
	int intLimit2 = Integer.parseInt(strLimit2);
	if((nLastPage >= 0 || nItems == intLimit2) && nItems != 0) {
		out.println("<tr><td colspan=3  align='center'>");
		if(nLastPage >= 0) {
%>
	<a
		href="formlist.jsp?demographic_no=<%=demoNo%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>"><bean:message
		key="oscarEncounter.formlist.formLastpage" /></a>
	|
	<%
		}
		if(nItems == intLimit2) {
%>
	<a
		href="formlist.jsp?demographic_no=<%=demoNo%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
	<bean:message key="oscarEncounter.formlist.formNextPage" /></a>
	</td>
	</tr>
	<%
		}else { out.println("</td></tr>"); }
%>
	<%
	}
	}
%>

</table>
</center>
</body>
</html:html>
