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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
  
  String user_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*, java.io.*, oscar.*"%>
<% java.util.Properties oscarVariables = OscarProperties.getInstance(); %>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ANTENATAL CHECK LIST</title>
<link rel="stylesheet" href="antenatalrecord.css">
<script language="JavaScript">
<!--		


function onExit() {
  if(confirm("Are you sure to exit WITHOUT saving the form?")) window.close();
}
//-->
</SCRIPT>
</head>
<body onLoad="setfocus()" bgcolor="#c4e9f6" bgproperties="fixed"
	topmargin="0" leftmargin="1" rightmargin="1">
<form name="checklistedit" action="obarriskedit_99_12.jsp" method="POST">
<%    
  char sep = oscarVariables.getProperty("file_separator").toCharArray()[0];
  String str = null;
  if(request.getParameter("submit")!=null && request.getParameter("submit").compareTo(" Save ")==0) {  
    //FileWriter inf = new FileWriter(".."+sep+"webapps"+sep+oscarVariables.getProperty("project_home")+sep+"decision"+sep+"antenatal"+sep+"desantenatalplannerrisks_99_12.xml");
    FileWriter inf = new FileWriter(OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"desantenatalplannerrisks_99_12.xml");
    str = request.getParameter("checklist");
	 str = SxmlMisc.replaceString(str," & "," &amp; ");
  	 str = SxmlMisc.replaceString(str," > "," &gt; ");
  	 str = SxmlMisc.replaceString(str," < "," &lt; ");
    inf.write(str);
    inf.close();
  }			
%>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER><font face="Arial, Helvetica, sans-serif"
			color="#FFFFFF">Antenatal Add-On Risk List</font></th>
		<th width="25%" nowrap>
		<div align="right"><a href=#
			onClick="popupPage(450,900,'ar1risk_99_12.htm')"><font
			color="#FFFF66">View Risk Number</font></a> <input type='submit'
			name='submit' value=' Save '> <input type="button"
			name="Button"
			value="&nbsp;<%=request.getParameter("submit")!=null?" Exit ":"Cancel"%>&nbsp;"
			onClick="onExit();">&nbsp;</div>
		</th>
	</tr>
	<tr>
		<td align=CENTER colspan="2"><font
			face="Times New Roman, Times, serif"> <textarea
			name="checklist" cols="100" rows="38" style="width: 100%">
<%
        boolean fileFound = true;
        File file = new File(OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"desantenatalplannerrisks_99_12.xml");
        if(!file.isFile() || !file.canRead()) {
			   file = new File(".."+sep+"webapps"+sep+oscarVariables.getProperty("project_home")+sep+"decision"+sep+"antenatal"+sep+"desantenatalplannerrisks_99_12.xml");
            if(!file.isFile() || !file.canRead()) {
				   fileFound = false; //throw new IOException();
			   }
        }

        if(fileFound){
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            String aline=""; //, temp="";
            while (true) {
                aline = raf.readLine(); 
                if(aline!=null){
                    out.println(aline);
                }else {
                    break;
                }
            }
            raf.close();
        }
%>
</textarea> </font></td>
	</tr>
	<TR>
		<td><b>*</b> The Symbols ("&", "<", or ">") should be written as
		" & ", " < ", or " > " in the content. Or use ("&amp;amp;","&amp;lt;",
		or "&amp;gt;") instead.</td>
	</tr>
</table>
<input type='submit' name='submit' value=' Save '> <input
	type="button" name="Button" value=" Exit " onClick="onExit();">
</form>
</body>
</html>
