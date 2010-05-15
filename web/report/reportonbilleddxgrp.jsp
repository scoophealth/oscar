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
<%
  if (session.getAttribute("user") == null) {
    response.sendRedirect("../logout.jsp");
  }

  String curUser_no = (String)session.getAttribute("user");
%>
<%@ page errorPage="../errorpage.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.apache.commons.lang.*"%>
<%@ page import="oscar.oscarBilling.data.BillingONDataHelp"%>
<%
  BillingONDataHelp dbObj = new BillingONDataHelp();

  // save the dx grp list
  if (request.getParameter("submit") != null && request.getParameter("submit").equals(" Add ")) {
    Properties prop  = new Properties();
    String     query = "select dxcode from dxphcpgroup ";
    ResultSet  rs    = dbObj.searchDBRecord(query);

    while (rs.next()) {
      prop.setProperty(dbObj.getString(rs,"dxcode"), "");
    }

    String dxcode = request.getParameter("dxcode");
    dxcode = dxcode!=null? dxcode.trim() : dxcode;

    if (!prop.containsKey(dxcode)) {
      String description = request.getParameter("description");
      String level1      = request.getParameter("level1");
      String level2      = request.getParameter("level2");
      String sql         = "insert into dxphcpgroup(dxcode, level1, level2, description) values(" + dxcode + ", '" +
              StringEscapeUtils.escapeSql(level1.trim()) + "', '" + StringEscapeUtils.escapeSql(level2.trim()) + "', '" +
              StringEscapeUtils.escapeSql(description.trim()) + "')";

      dbObj.updateDBRecord(sql);
    } else {
      String description = request.getParameter("description");
      String level1      = request.getParameter("level1");
      String level2      = request.getParameter("level2");
      String sql         = "update dxphcpgroup set level1='" + StringEscapeUtils.escapeSql(level1.trim())
      	+ "', level2='" + StringEscapeUtils.escapeSql(level2.trim())
      	+ "', description='" + StringEscapeUtils.escapeSql(description.trim())
      	+ "' where dxcode=" + dxcode ;

      dbObj.updateDBRecord(sql);
    }
  }
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Diagnostic Categories</title>
<link rel="stylesheet" href="../receptionist/receptionistapptstyle.css">
<script language="JavaScript">

                <!--
function setfocus() {
	this.focus();
	//  document.titlesearch.keyword.select();
}
function submit(form) {
	form.submit();
}
function fillIn(dxcode, level1, level2, description) {
	document.forms[0].dxcode.value=dxcode;
	document.forms[0].level1.value=level1;
	document.forms[0].level2.value=level2;
	document.forms[0].description.value=description;
}

//-->

      </script>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER" width="90%"><font face="Helvetica"
			color="#FFFFFF"> Diagnostic Categories Headings </font></th>
	</tr>
</table>
<%
      String     color = "#ccCCFF";
      Properties prop  = new Properties();
      Vector     vec   = new Vector();
      String     query = "select * from dxphcpgroup order by dxcode, level1, level2 ";
      ResultSet  rs    = dbObj.searchDBRecord(query);

      while (rs.next()) {
        prop = new Properties();

        prop.setProperty("dxcode", "" + rs.getInt("dxcode"));
        prop.setProperty("level1", dbObj.getString(rs,"level1"));
        prop.setProperty("level2", dbObj.getString(rs,"level2"));
        prop.setProperty("description", dbObj.getString(rs,"description"));
        vec.add(prop);
      }

      System.out.println(" 3." + curUser_no);
%>
<table width="100%" border="0" bgcolor="ivory" cellspacing="1"
	cellpadding="1">
	<tr bgcolor="silver">
		<th width="10%" nowrap><b>Code</b></th>
		<th nowrap><b>Description</b></th>
	</tr>
	<%
        String catName = "";

        for (int i = 0; i < vec.size(); i++) {
          boolean bNewCat    = false;
          String  curCatName = ((Properties)vec.get(i)).getProperty("level1", "").toUpperCase() + " - " +
                  ((Properties)vec.get(i)).getProperty("level2", "");

          if (!curCatName.equals(catName)) {
            // new level1
            if (catName.indexOf('-') > 0 && !curCatName.startsWith(catName.substring(0, catName.indexOf('-')))) {
              color = "#00FF99";
            } else {
              color = "mediumaquamarine";
            }

            // new level2
            bNewCat = true;
            catName = curCatName;
%>
	<tr bgcolor="<%=color%>">
		<td></td>
		<td><%= curCatName %></td>
	</tr>
	<%
          }
          color = "#ccCCFF";
%>
	<tr bgcolor="<%=color%>">
		<td align="right"
			onClick="fillIn('<%= ((Properties)vec.get(i)).getProperty("dxcode", "") %>', '<%= StringEscapeUtils.escapeJavaScript(((Properties)vec.get(i)).getProperty("level1", "")) %>','<%= StringEscapeUtils.escapeJavaScript(((Properties)vec.get(i)).getProperty("level2", "")) %>','<%= StringEscapeUtils.escapeJavaScript(((Properties)vec.get(i)).getProperty("description", "")) %>')">
		<%= ((Properties)vec.get(i)).getProperty("dxcode", "") %></td>
		<td><%= ((Properties)vec.get(i)).getProperty("description", "") %>
		</td>
	</tr>
	<%
        }
%>
</table>
<hr>
<form name="myform" action="reportonbilleddxgrp.jsp" method="POST">
<table width="100%" border="0" bgcolor="ivory" cellspacing="1"
	cellpadding="1">
	<tr bgcolor="silver">
		<td width="10%" nowrap>Dx Code: <input type="text" name="dxcode"
			size="6"></td>
	</tr>
	<tr>
		<td nowrap>Category - level1: <input type="text" name="level1"
			size="90"></td>
	</tr>
	<tr>
		<td nowrap>Category - level2: <input type="text" name="level2"
			size="90"></td>
	</tr>
	<tr>
		<td>Description: <input type="text" name="description"
			size="120"></td>
	</tr>
	<tr bgcolor="silver">
		<td><input type="submit" name="submit" value=" Add "></td>
	</tr>
</table>
</form>
</body>
</html>
