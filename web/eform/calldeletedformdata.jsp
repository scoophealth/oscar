<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  //int demographic_no =new Integer(request.getParameter("demographic_no")).intValue(); 
  String demographic_no =request.getParameter("demographic_no") ; 
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>  

<%@ page import = "java.net.*,java.sql.*"   errorPage="../errorpage.jsp"%> 
<jsp:useBean id="myFormBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String param = request.getParameter("orderby")!=null?request.getParameter("orderby"):"form_date desc";
  String [][] dbQueries=new String[][] { 
{"search_deleted", "select * from eform_data where status = 0 and demographic_no= ?  order by " +
                    param + ", form_date desc, form_time desc" }, 
  };
  myFormBean.doConfigure(dbParams,dbQueries);
 
  ResultSet rs = myFormBean.queryResults(demographic_no, "search_deleted");
%>
<%
	String country = request.getLocale().getCountry();
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
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
<title>
<bean:message key="eform.calldeletedformdata.title"/>
</title>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">

<style type="text/css">
	table.outline{
	   margin-top:50px;
	   border-bottom: 1pt solid #888888;
	   border-left: 1pt solid #888888;
	   border-top: 1pt solid #888888;
	   border-right: 1pt solid #888888;
	}
	table.grid{
	   border-bottom: 1pt solid #888888;
	   border-left: 1pt solid #888888;
	   border-top: 1pt solid #888888;
	   border-right: 1pt solid #888888;
	}
	td.gridTitles{
		border-bottom: 2pt solid #888888;
		font-weight: bold;
		text-align: center;
	}
        td.gridTitlesWOBottom{
                font-weight: bold;
                text-align: center;
        }
	td.middleGrid{
	   border-left: 1pt solid #888888;	   
	   border-right: 1pt solid #888888;
           text-align: center;
	}	
</style>
</head>
<script language="javascript">
<!--
function newWindow(file,window) {
    msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
    if (msgWindow.opener == null) msgWindow.opener = self;
} 
//-->
</script>
<body class="BodyStyle" vlink="#0000FF">
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <bean:message key="eform.calldeletedformdata.msgMyform"/>
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
						<bean:message key="eform.calldeletedform.msgtitle"/>
                        </td>
                        <td  >&nbsp;
							
                        </td>
                        <td style="text-align:right">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  ><bean:message key="global.help" /></a> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn" valign="top">
               
                <%  if (country.equals("BR")) { %>
                    <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail_ptbr"><bean:message key="global.btnBack" /> &nbsp;</a>
                <%}else{%>
                    <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail"><bean:message key="global.btnBack" /> &nbsp;</a>
                <%}%>
                <br>
                <a href="showmyform.jsp?demographic_no=<%=demographic_no%>"><bean:message key="eform.calldeletedformdata.btnGoToForm"/></a>
                
            </td>
            <td class="MainTableRightColumn">
<table border="0" cellspacing="2" cellpadding="2" width="98%">
  <tr bgcolor="<%=deepColor%>" >
  <th width=20%><a href="calldeletedformdata.jsp?demographic_no=<%=demographic_no%>&orderby=form_name"><bean:message key="eform.showmyform.btnFormName"/></a></th>
  <th><a href="calldeletedformdata.jsp?demographic_no=<%=demographic_no%>"><bean:message key="eform.showmyform.formDate"/></a></th>
  <th><a href="calldeletedformdata.jsp?demographic_no=<%=demographic_no%>"><bean:message key="eform.showmyform.formTime"/></a></th> 
  <th><bean:message key="eform.showmyform.msgAction"/></th>
  </tr> 
<%
  String bgcolor = null;
  if (rs.next()){ 
    rs.beforeFirst();
    while (rs.next()){
      bgcolor = rs.getRow()%2==0?"white":weakColor ;
%>
  <tr bgcolor="<%=bgcolor%>">
  <td><a href="JavaScript:newWindow('showmyformdata.jsp?fdid=<%=rs.getInt("fdid")%>','window2')"><%=rs.getString("form_name")%></a></td>
  <td nowrap align='center'><%=rs.getString("form_date")%></td>
  <td nowrap align='center'><%=rs.getString("form_time")%></td>
  <td nowrap align='center'><a href="undeleteformdata.jsp?fdid=<%=rs.getInt("fdid")%>&demographic_no=<%=demographic_no%>"><bean:message key="eform.calldeletedformdata.btnUndelete"/></a></td>
  </tr>
<%
    }  
  }else {
%>
    <tr><td><bean:message key="eform.showmyform.msgNoData"/></td></tr>
<%
  }
  myFormBean.closePstmtConn();
%>               
</table>
			</td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">
            </td>
            <td class="MainTableBottomRowRightColumn">

            </td>
        </tr>
    </table>
</body>
</html:html>