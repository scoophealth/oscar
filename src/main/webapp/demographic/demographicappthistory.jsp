
<%
  
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
<%@ page
	import="java.util.*, java.sql.*, java.net.*, oscar.*, oscar.oscarDB.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%
	String country = request.getLocale().getCountry();
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
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

<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="demographic.demographicappthistory.title" /></title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<script type="text/javascript">
<!--

function popupPageNew(vheight,vwidth,varpage) {
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
  var popup=window.open(page, "demographicprofile", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}
  

//-->


</script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle"
	demographic.demographicappthistory.msgTitle=vlink=
	"#0000FF" onLoad="setValues()">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="demographic.demographicappthistory.msgHistory" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><bean:message
					key="demographic.demographicappthistory.msgResults" />: <%=demolastname%>,<%=demofirstname%>
				(<%=request.getParameter("demographic_no")%>)</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a
					href="javascript:popupStart(300,400,'Help.jsp')"><bean:message
					key="global.help" /></a> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top"><a
			href="javascript:history.go(-1)"
			onMouseOver="self.status=document.referrer;return true"><bean:message
			key="global.btnBack" /></a> <!--
            <% if (country.equals("BR")) { %>
                    <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail_ptbr"><bean:message key="global.btnBack" /> &nbsp;</a>
            <%}else{%>
                   <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail"><bean:message key="global.btnBack" /> &nbsp;</a>
          <%}%>   
    --></td>
		<td class="MainTableRightColumn">
		<table width="95%" border="0" bgcolor="#ffffff">
			<tr bgcolor="<%=deepColor%>">
				<TH width="10%"><b><bean:message
					key="demographic.demographicappthistory.msgApptDate" /></b></TH>
				<TH width="10%"><b><bean:message
					key="demographic.demographicappthistory.msgFrom" /></b></TH>
				<TH width="10%"><b><bean:message
					key="demographic.demographicappthistory.msgTo" /></b></TH>
				<TH width="15%"><b><bean:message
					key="demographic.demographicappthistory.msgReason" /></b></TH>
				<TH width="15%"><b><bean:message
					key="demographic.demographicappthistory.msgProvider" /></b></TH>
				<plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
					<TH><b>EYE FORM</b></TH>
				</plugin:hideWhenCompExists>
				<TH width="10%"><b><bean:message
					key="demographic.demographicappthistory.msgComments" /></b></TH>
			</tr>
			<%
  int iRSOffSet=0;
  int iPageSize=10;
  int iRow=0;
  if(request.getParameter("limit1")!=null) iRSOffSet= Integer.parseInt(request.getParameter("limit1"));
  if(request.getParameter("limit2")!=null) iPageSize = Integer.parseInt(request.getParameter("limit2"));

  ResultSet rs=null ;
  DBPreparedHandlerParam[] params= new DBPreparedHandlerParam[1];
  params[0]= new DBPreparedHandlerParam(Integer.parseInt(request.getParameter("demographic_no")));
  rs = apptMainBean.queryResults_paged(params, request.getParameter("dboperation"), iRSOffSet);

  boolean bodd=false;
  int nItems=0;
  
  if(rs==null) {
    out.println("failed!!!");
  } else {
    while (rs.next()) {
      iRow ++;
      if(iRow>iPageSize) break;
      bodd=bodd?false:true; //for the color of rows
      nItems++; //to calculate if it is the end of records
       
%> 
<tr bgcolor="<%=bodd?weakColor:"white"%>">
      <td align="center"><a href=# onClick ="popupPageNew(360,680,'../appointment/appointmentcontrol.jsp?appointment_no=<%=apptMainBean.getString(rs,"appointment_no")%>&displaymode=edit&dboperation=search');return false;" ><%=apptMainBean.getString(rs,"appointment_date")%></a></td>
      <td align="center"><%=apptMainBean.getString(rs,"start_time")%></td>
      <td align="center"><%=apptMainBean.getString(rs,"end_time")%></td>
      <td><%=apptMainBean.getString(rs,"reason")%></td>
      <td><%=apptMainBean.getString(rs,"last_name")+","+apptMainBean.getString(rs,"first_name")%></td>
      <plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
      <special:SpecialEncounterTag moduleName="eyeform">      
      <td><a href="#" onclick="popupPage(800,1000,'<%=request.getContextPath()%>/mod/specialencounterComp/EyeForm.do?method=view&appHis=true&demographicNo=<%=request.getParameter("demographic_no")%>&appNo=<%=rs.getString("appointment_no")%>')">eyeform</a></td>
      </special:SpecialEncounterTag>
      </plugin:hideWhenCompExists>
      <td>&nbsp;<%=apptMainBean.getString(rs,"status")==null?"":(apptMainBean.getString(rs,"status").contains("N")?"No Show":(apptMainBean.getString(rs,"status").equals("C")?"Cancelled":"") ) %></td>
</tr>
<%
    }
  }
  
%>

		</table>
		<br>
		<%
  int nPrevPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nPrevPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nPrevPage>=0) {
%> <a
			href="demographiccontrol.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&last_name=<%=URLEncoder.encode(demolastname,"UTF-8")%>&first_name=<%=URLEncoder.encode(demofirstname,"UTF-8")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nPrevPage%>&limit2=<%=strLimit2%>"><bean:message
			key="demographic.demographicappthistory.btnPrevPage" /></a> <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <a
			href="demographiccontrol.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&last_name=<%=URLEncoder.encode(demolastname,"UTF-8")%>&first_name=<%=URLEncoder.encode(demofirstname,"UTF-8")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
		<bean:message key="demographic.demographicappthistory.btnNextPage" /></a>
		<%
}
%>
		<p>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
