<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String curProvider_no = (String) session.getAttribute("user");
  String demographic_no = request.getParameter("demographic_no");
  String strLimit1="0";
  String strLimit2="25";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  String demolastname = request.getParameter("last_name")==null?"":request.getParameter("last_name");
  String demofirstname = request.getParameter("first_name")==null?"":request.getParameter("first_name");
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>
<%@ page import="java.util.*, java.sql.*, java.net.*, oscar.*" errorPage="errorpage.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
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
<bean:message key="demographic.demographicappthistory.title"/>
</title>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<meta http-equiv="Pragma" content="no-cache">
<script type="text/javascript">
<!--
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";//360,680
  var popup=window.open(page, "appthist", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}
function refresh() {
  history.go(0);
}

//-->


</script>

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

<body class="BodyStyle"demographic.demographicappthistory.msgTitle= vlink="#0000FF" onLoad="setValues()" >
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <bean:message key="demographic.demographicappthistory.msgHistory"/>
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
						<bean:message key="demographic.demographicappthistory.msgResults"/>: <%=demolastname%>,<%=demofirstname%> (<%=request.getParameter("demographic_no")%>)
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
            <% if (country.equals("BR")) { %>
                    <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail_ptbr"><bean:message key="global.btnBack" /> &nbsp;</a>
            <%}else{%>
                    <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail"><bean:message key="global.btnBack" /> &nbsp;</a>
            <%}%>   
                
                
            </td>
            <td class="MainTableRightColumn">
<table width="95%" border="0" bgcolor="#ffffff"> 
<tr bgcolor="<%=deepColor%>">
      <TH width="10%"><b><bean:message key="demographic.demographicappthistory.msgApptDate"/></b></TH>
      <TH width="10%"><b><bean:message key="demographic.demographicappthistory.msgFrom"/></b></TH>      
      <TH width="10%"><b><bean:message key="demographic.demographicappthistory.msgTo"/></b></TH>
      <TH width="15%"><b><bean:message key="demographic.demographicappthistory.msgReason"/></b></TH>
      <TH width="15%"><b><bean:message key="demographic.demographicappthistory.msgProvider"/></b></TH>
      <TH width="10%"><b><bean:message key="demographic.demographicappthistory.msgComments"/></b></TH>
</tr>
<%
  ResultSet rs=null ;
  rs = apptMainBean.queryResults(Integer.parseInt(request.getParameter("demographic_no")), request.getParameter("dboperation"));

  boolean bodd=false;
  int nItems=0;
  
  if(rs==null) {
    out.println("failed!!!");
  } else {
    while (rs.next()) {
      bodd=bodd?false:true; //for the color of rows
      nItems++; //to calculate if it is the end of records
       
%> 
<tr bgcolor="<%=bodd?weakColor:"white"%>">
      <td align="center"><a href=# onClick ="popupPage(360,680,'../appointment/appointmentcontrol.jsp?appointment_no=<%=apptMainBean.getString(rs,"appointment_no")%>&displaymode=edit&dboperation=search');return false;" ><%=apptMainBean.getString(rs,"appointment_date")%></a></td>
      <td align="center"><%=apptMainBean.getString(rs,"start_time")%></td>
      <td align="center"><%=apptMainBean.getString(rs,"end_time")%></td>
      <td><%=apptMainBean.getString(rs,"reason")%></td>
      <td><%=apptMainBean.getString(rs,"last_name")+","+apptMainBean.getString(rs,"first_name")%></td>
      <td>&nbsp;<%=apptMainBean.getString(rs,"status")==null?"":(apptMainBean.getString(rs,"status").equals("N")?"No Show":(apptMainBean.getString(rs,"status").equals("C")?"Cancelled":"") ) %></td>
</tr>
<%
    }
  }
  apptMainBean.closePstmtConn();
  
%> 
  
</table>
<br>
<%
  int nPrevPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nPrevPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nPrevPage>=0) {
%>
<a href="demographiccontrol.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&last_name=<%=URLEncoder.encode(demolastname,"UTF-8")%>&first_name=<%=URLEncoder.encode(demofirstname,"UTF-8")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nPrevPage%>&limit2=<%=strLimit2%>"><bean:message key="demographic.demographicappthistory.btnPrevPage"/></a> 
<%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%>
<a href="demographiccontrol.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&last_name=<%=URLEncoder.encode(demolastname,"UTF-8")%>&first_name=<%=URLEncoder.encode(demofirstname,"UTF-8")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>"> <bean:message key="demographic.demographicappthistory.btnNextPage"/></a>
<%
}
%>
<p>

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