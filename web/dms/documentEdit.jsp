<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat" errorPage="errorpage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<%@ include file="dbDMS.jsp" %>
<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);
                     String docdownload = oscarVariables.getProperty("project_home") ;;

  String proFirst="", proLast="", proOHIP="", proNo="";
   int Count = 0;
   ResultSet rslocal=null;
    rslocal = null;

%>
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
<html:html locale="true">
<head>
<title><bean:message key="dms.documentEdit.title"/></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
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
  f0 = escape(document.aDoc.docfilename.value);
  awnd=rs('att','../dms/zadddocument.jsp' ,400,200,1);
  awnd.focus();
}
//-->
</script>
<link rel="stylesheet" href="../web.css" />
</head>

<body bgcolor="#C4D9E7" bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr bgcolor="#486ebd">
    <th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message key="dms.documentEdit.msgAddDocument"/></font></th>
  </tr>
</table>
<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%" BGCOLOR="#C4D9E7"  >
  <FORM NAME="aDoc" ACTION="dbDocumentEdit.jsp" METHOD="POST">
    <tr valign="top"> 
      <td rowspan="2" ALIGN="right" valign="middle"> 
        <div align="center"> 
          <p>&nbsp;</p>
          <table width="80%" border="1" cellspacing="0" cellpadding="0">
                  <tr> 
                    <td width="19%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000"><bean:message key="dms.documentEdit.formDocDesc"/></font></td>
                    <td width="50%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                      <input type="text" name="docdesc"  value="<%=request.getParameter("desc")%>" size="20">
                                            <input type="hidden" name="document_no"  value="<%=request.getParameter("document_no")%>" size="20">
                                                         <input type="hidden" name="doccreator"  value="<%=request.getParameter("doccreator")%>" size="20">
                      </font></td>
            </tr>
                   <tr> 
              <td width="19%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000"><bean:message key="dms.documentEdit.formDocType"/></font></td>
              <td width="50%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                <select name="doctype">     
                  <option value=""><bean:message key="dms.documentEdit.formSelType"/></option>
                  <% 
                    ResultSet rsdemo2 = null;
		  	   
		  		 	          	     String[] param4=new String[1];
		  		 	          	    	 
		  		 	          	    	   
		  		 	          	    		   param4[0] = request.getParameter("function");
		  					        
		  		 	          
		  		 	          	    rsdemo2 = apptMainBean.queryResults(param4, "search_doctype_by_module");
		  		 	             while (rsdemo2.next()) {   
		  		 	 %>
		  		 	   <option value="<%=rsdemo2.getString("doctype")%>" <%=rsdemo2.getString("doctype").compareTo(request.getParameter("doctype"))==0?"selected":""%>><%=rsdemo2.getString("doctype")%></option>
		  		 	 <%
		 	          }
                  %>
                </select>
                </font></td>
            </tr>

            <tr> 
              <td width="19%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000"><bean:message key="dms.documentEdit.formDate"/></font></td>
              <td width="50%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                <input type="text" name="docdate" readonly value="<%=nowDate%>" size="20" >
                </font></td>
            </tr>
                   <tr> 
	                 <td colspan="2"><input type="submit" name="submit" value="<bean:message key="dms.documentEdit.btnChange"/>">  <input type="button" name="Button" value="<bean:message key="global.btnCancel"/>" onclick=self.close();></td>
            </tr>
  </form>
</table>
</body>
</html:html>
