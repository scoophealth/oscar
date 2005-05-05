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
if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
%>

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat" errorPage="errorpage.jsp" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%@ include file="dbDMS.jsp" %>
<%
String user_no = (String) session.getAttribute("user");
String docdownload = oscarVariables.getProperty("project_home") ;;
session.setAttribute("homepath", docdownload);

OscarProperties props = OscarProperties.getInstance();

%>
<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);

  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);
     //                String docdownload = oscarVariables.getProperty("project_home") ;;
     //    session.setAttribute("homepath", docdownload);

  String proFirst="", proLast="", proOHIP="", proNo="";
   int Count = 0;
   ResultSet rslocal=null;
    rslocal = null;
   rslocal = apptMainBean.queryResults(user_no, "search_provider_name");
   while(rslocal.next()){

   proFirst = rslocal.getString("first_name");
   proLast = rslocal.getString("last_name");
   proOHIP = rslocal.getString("provider_no");
}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">

<html:html locale="true">
<head>
<title><bean:message key="dms.addDocument.title"/></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<script language="JavaScript">
<!--

function setfocus() {
  this.focus();
  document.aDoc.docfilename.focus();
  document.aDoc.docfilename.select();
}

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
  f0 = escape(document.aDoc.docfilename.value);
  awnd=rs('att','../dms/zadddocument.jsp' ,500,150,1);
  awnd.focus();
}

function checkSel(sel){
  theForm = sel.form;
  theForm.docdesc.value = theForm.doctype.value;
}
//-->
</script>
<link rel="stylesheet" href="../web.css" />
</head>

<body bgcolor="#C4D9E7" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr bgcolor="#486ebd">
    <th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message key="dms.addDocument.msgAddDocument"/></font></th>
  </tr>
</table>
<FORM NAME="aDoc" ACTION="dbDocument.jsp" METHOD="POST">
<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%" BGCOLOR="#C4D9E7"  >
    <tr valign="top">
      <td rowspan="2" ALIGN="right" valign="middle">
        <div align="center">
          <p>&nbsp;</p>
          <table width="80%" border="1" cellspacing="0" cellpadding="0">
            <tr>
              <td colspan="3"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"></font></td>
            </tr>
            <tr>
              <td width="19%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000"><bean:message key="dms.addDocument.msgFunction"/></font></td>
              <td width="50%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1">
                <input type="text" name="function" value="<%=request.getParameter("function")%> " size="20">
                </font></td>
              <td rowspan="8" width="31%" valign="middle">
                <p><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000066"><bean:message key="dms.addDocument.msgInstructionTitle"/><br>
                  <bean:message key="dms.addDocument.msgInstructions"/></font></p>
                <p><br>
                </p>
                </td>
            </tr>
            <tr>
              <td width="19%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000"><bean:message key="dms.addDocument.msgID"/></font></td>
              <td width="50%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1">
                <input type="text" name="functionid" value="<%=request.getParameter("functionid")%>" size="20">
                </font></td>
            </tr>
            <tr>
              <td width="19%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000"><bean:message key="dms.addDocument.msgDocType"/></font></td>
              <td width="50%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1">
                <select name="doctype" onchange="checkSel(this)">
                  <option value=""><bean:message key="dms.addDocument.formSelect"/></option>
                  <%
                    ResultSet rsdemo2 = null;
					String[] param4=new String[1];
					param4[0] = request.getParameter("function");
					rsdemo2 = apptMainBean.queryResults(param4, "search_doctype_by_module");
					while (rsdemo2.next()) {
		  		 	 %>
		  		 	   <option value="<%=rsdemo2.getString("doctype")%>" <%=props.getProperty("eDocAddTypeDefault", "").startsWith(rsdemo2.getString("doctype"))?"selected":""%> ><%=rsdemo2.getString("doctype")%></option>
		  		 	 <%
					}
                  %>
                </select>
                </font></td>
            </tr>
            <tr>
              <td width="19%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000"><bean:message key="dms.addDocument.formDocDesc"/></font></td>
              <td width="50%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1">
                <input type="text" name="docdesc"  size="20" value="<%=props.getProperty("eDocAddTypeDefault", "")%>" >
                </font></td>
            </tr>
            <tr>
              <td width="19%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000"><bean:message key="dms.addDocument.msgDate"/></font></td>
              <td width="50%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1">
                <input type="text" name="docdate" readonly value="<%=nowDate%>" size="20" >
                </font></td>
            </tr>
            <tr>
              <td width="19%"><font face="Verdana, Arial, Helvetica, sans-serif" color="#000000" size="1"><bean:message key="dms.addDocument.msgCreator"/>
                </font></td>
              <td width="50%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1">
                <input type="text" name="dispcreator"  readonly value="<%=proLast%>, <%=proFirst%>" size="20">
                <input type="hidden" name="doccreator" value="<%=user_no%>" size="20">
                <input type="hidden" name="orderby" value="updatedatetime desc" size="20">
                </font></td>
            </tr>
            <tr>
              <td width="19%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000"><bean:message key="dms.addDocument.msgFileName" /> </font></td>
              <td width="50%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1">
                <input type="text" name="docfilename" value=""  size="20" >
                <a href=javascript:ScriptAttach()><bean:message key="dms.addDocument.btnUpload"/></a></font></td>
            </tr>
            <tr>
              <td colspan="2"><font face="Verdana, Arial, Helvetica, sans-serif" color="#0000FF" size="1"><b><i>
                <input type="SUBMIT" name="Submit" value="<bean:message key="dms.addDocument.btnSubmit"/>">
                <input type="button" name="Button" value="<bean:message key="global.btnCancel"/>" onclick="self.close();">
                </i></b></font><font face="Verdana, Arial, Helvetica, sans-serif" size="1"></font></td>
            </tr>
          </table>
          <p><font face="Verdana" color="#0000FF"><b><i> </i></b></font> <br>
          </p>
        </div>
      </td>
    </tr>
</table>
</form>
</body>
</html:html>
