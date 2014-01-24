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
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<%
        long loadPage = System.currentTimeMillis();
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

%>


<%@ page
	import="java.util.*, java.sql.*, java.net.URLEncoder, oscar.*, oscar.util.*, oscar.oscarDemographic.data.DemographicMerged"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />

<%
	if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
        Boolean isMobileOptimized = session.getAttribute("mobileOptimized") != null;

	String strLimit1="0";
	String strLimit2="18";
	String deepColor = "#CCCCFF", weakColor = "#EEEEFF" ;
	if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
	if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
      //  boolean fromMessenger = request.getParameter("fromMessenger") == null ? false : (request.getParameter("fromMessenger")).equalsIgnoreCase("true")?true:false;

        GregorianCalendar now=new GregorianCalendar();
        int curYear = now.get(Calendar.YEAR);
        int curMonth = (now.get(Calendar.MONTH)+1);
        int curDay = now.get(Calendar.DAY_OF_MONTH);
        String curProvider_no = (String) session.getAttribute("user");
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<c:out value="${ctx}/share/javascript/Oscar.js"/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
   <script>
     jQuery.noConflict();
   </script>
<oscar:customInterface section="demoSearch"/>

<title><bean:message
	key="demographic.demographicsearchresults.title" /></title>
<% if (isMobileOptimized) { %>
   <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width" />
   <link rel="stylesheet" type="text/css" href="../mobile/searchdemographicstyle.css">
<% } else { %>
   <link rel="stylesheet" type="text/css" media="all" href="../demographic/searchdemographicstyle.css"  />
   <link rel="stylesheet" type="text/css" media="all" href="../share/css/searchBox.css"  />
   <style type="text/css">
    .deep { background-color: <%= deepColor %>; }
    .weak { background-color: <%= weakColor %>; }
   </style>
   <!-- Alternative display for IE 7 and below, since display:table properties are not understood -->
    <!--[if lt IE 8]>
        <style type="text/css">
            body { min-width: 1024px; }
            #searchResults li div { border: none !important; float: left; }
            div.demoIdSearch { clear: both; }
        </style>
    <![endif]-->
    <!-- Min-width doesn't work properly in IE6, so we simulate it using JavaScript.
    It's important to set a min-width since many elements will be floating, and
    resizing may otherwise cause elements to collapse in strange ways
    -->
    <!--[if lt IE 7]>
<script language="JavaScript">
            window.onresize = function() { setMinWidth(1024); }
        </script>
    <![endif]-->
<% } %>
<script language="JavaScript">
function showHideItem(id){
if(document.getElementById(id).style.display == 'inline')
    document.getElementById(id).style.display = 'none';
else
    document.getElementById(id).style.display = 'inline';
}
function setfocus() {
  document.titlesearch.keyword.focus();
  document.titlesearch.keyword.select();
}

function checkTypeIn() {
  var dob = document.titlesearch.keyword; typeInOK = true;

  if (dob.value.indexOf('%b610054') == 0 && dob.value.length > 18){
     document.titlesearch.keyword.value = dob.value.substring(8,18);
     document.titlesearch.search_mode[4].checked = true;
  }
  	if(document.titlesearch.search_mode[0].checked) {
		var keyword = document.titlesearch.keyword.value;
      	var keywordLowerCase = keyword.toLowerCase();
      	document.titlesearch.keyword.value = keywordLowerCase;
	}
  if(document.titlesearch.search_mode[2].checked) {
    if(dob.value.length==8) {
      dob.value = dob.value.substring(0, 4)+"-"+dob.value.substring(4, 6)+"-"+dob.value.substring(6, 8);
    }
    if(dob.value.length != 10) {
      alert("<bean:message key="demographic.search.msgWrongDOB"/>");
      typeInOK = false;
    }

    return typeInOK ;
  } else {
    return true;
  }
}

function popup(vheight,vwidth,varpage) {
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="global.oscarRx"/>_demosearch", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}

function popupEChart(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=20,left=20";
  var popup=window.open(page, "encounter", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}


</SCRIPT>
</head>
<body bgproperties="fixed" onLoad="setfocus()"
      topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0">

<div id="demographicSearch">
    <a href="#" onclick="showHideItem('demographicSearch');" id="cancelButton" class="leftButton top">
                <bean:message key="global.btnCancel" />
    </a>
<%@ include file="zdemographicfulltitlesearch.jsp"%>
</div>

<center>
<div id="searchResults">
    <a href="#" onclick="showHideItem('demographicSearch');" id="searchPopUpButton" class="rightButton top">Search</a>
    <div class="header deep">
        <div class="title">
            <% if (isMobileOptimized) { %> <bean:message key="demographic.demographicsearch2apptresults.findPatient" />
            <% } else { %>  <bean:message key="demographic.demographicsearchresults.msgSearchPatient" />
            <% } %>
        </div>
    </div>
<table width="95%" border="0">
	<tr>
		<td align="left"><i><bean:message
			key="demographic.demographicsearchresults.msgSearchKeys" /></i> : <%=java.net.URLDecoder.decode(request.getParameter("keyword"), "UTF-8")%></td>
	</tr>
</table>
    <ul bgcolor="#ffffff">
        <li class="tableHeadings deep">

                <div class="demoIdSearch">
                    <a href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=demographic_no&limit1=0&limit2=<%=strLimit2%>"><bean:message
                        key="demographic.demographicsearchresults.btnDemoNo" /></a>
                </div>
		<% if(!fromMessenger)  { %>
		<div class="links">
			<a href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=demographic_no&limit1=0&limit2=<%=strLimit2%>">&nbsp Links<sup>*</sup></a></div>
		<% } %>

		<div class="name"><a
                    href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=last_name&limit1=0&limit2=<%=strLimit2%>"><bean:message
                    key="demographic.demographicsearchresults.btnDemoName"/><sup>1</sup></a>
                </div>
		<div class="chartNo"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=chart_no&limit1=0&limit2=<%=strLimit2%>"><bean:message
			key="demographic.demographicsearchresults.btnChart" /></a>
                </div>
		<div class="sex"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=sex&limit1=0&limit2=<%=strLimit2%>"><bean:message
			key="demographic.demographicsearchresults.btnSex" /></a>
                </div>
		<div class="dob"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=year_of_birth,month_of_birth,date_of_birth&limit1=0&limit2=<%=strLimit2%>"><bean:message
			key="demographic.demographicsearchresults.btnDOB" /></a>
                </div>
		<div class="doctor"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=provider_no&limit1=0&limit2=<%=strLimit2%>"><bean:message
			key="demographic.demographicsearchresults.btnDoctor" /></a>
                </div>
                <div class="rosterStatus"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=roster_status&limit1=0&limit2=<%=strLimit2%>"><font
			size='-1'><bean:message
                        key="demographic.demographicsearchresults.btnRosSta" /></font></a>
                </div>
		<div class="patientStatus"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=patient_status&limit1=0&limit2=<%=strLimit2%>"><font
			size='-1'><bean:message
                        key="demographic.demographicsearchresults.btnPatSta" /></font></a>
                </div>
                <div class="phone"><a
			href="demographiccontrol.jsp?fromMessenger=<%=fromMessenger%>&keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=phone&limit1=0&limit2=<%=strLimit2%>"><bean:message
			key="demographic.demographicsearchresults.btnPhone" /></a>
                </div>
	</li>
	<%
    java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);

	int age=0;
	ResultSet rs=null ;

	String dboperation = request.getParameter("dboperation");
	String keyword=request.getParameter("keyword").trim();
	//keyword.replace('*', '%').trim();

    int iRSOffSet=0;
    int iPageSize=10;
    int iRow=0;
    if(request.getParameter("limit1")!=null) iRSOffSet= Integer.parseInt(request.getParameter("limit1"));
    if(request.getParameter("limit2")!=null) iPageSize = Integer.parseInt(request.getParameter("limit2"));

    String sDbType = oscar.OscarProperties.getInstance().getProperty("db_type").trim();
    if (sDbType.equalsIgnoreCase("oracle")) {
	  if(request.getParameter("search_mode").equals("search_name")) {
	 	 if(keyword.indexOf(",")==-1)
		    rs = apptMainBean.queryResults_paged("%" + keyword + "%", dboperation, iRSOffSet) ; //lastname
		 else if(keyword.indexOf(",")==(keyword.length()-1))
		    rs = apptMainBean.queryResults_paged("%" + keyword.substring(0,(keyword.length()-1)) + "%", dboperation, iRSOffSet);//lastname
		 else { //lastname,firstname
            String[] param =new String[2];
            int index = keyword.indexOf(",");
            param[0]="%" + keyword.substring(0,index).trim() + "%";
            param[1]=keyword.substring(index+1).trim() + "%";
            rs = apptMainBean.queryResults_paged(param, dboperation, iRSOffSet);
   		 }
	  } else if(request.getParameter("search_mode").equals("search_dob")) {
		  String[] param = searchDOBParams(keyword);
		 rs = apptMainBean.queryResults_paged(param, dboperation, iRSOffSet);
      } else if(request.getParameter("search_mode").equals("search_address") || request.getParameter("search_mode").equals("search_phone") ||
         request.getParameter("search_mode").equals("search_hin")) {
         if(keyword.length()>0) keyword="%" + request.getParameter("keyword") + "%";
         keyword = keyword.replaceAll("-", "%-%");
         rs = apptMainBean.queryResults_paged(keyword, dboperation, iRSOffSet);
	  } else {
		 keyword="%" + request.getParameter("keyword") + "%";
		 rs = apptMainBean.queryResults_paged(keyword, dboperation, iRSOffSet);
	  }

    }else{  //MySQL
          dboperation += "_mysql";
	  if(request.getParameter("search_mode").equals("search_name")) {
		 keyword="^"+keyword;
		 if(keyword.indexOf(",")==-1)
		    rs = apptMainBean.queryResults(keyword, dboperation) ; //lastname
		 else if(keyword.indexOf(",")==(keyword.length()-1))
		    rs = apptMainBean.queryResults(keyword.substring(0,(keyword.length()-1)), dboperation);//lastname
		 else { //lastname,firstname
            String[] param =new String[2];
            int index = keyword.indexOf(",");
            param[0]=keyword.substring(0,index).trim(); // already has an "^" at the front, so no need to add another
            param[1]="^"+keyword.substring(index+1).trim();

            rs = apptMainBean.queryResults(param, dboperation);
   		 }
	  } else if(request.getParameter("search_mode").equals("search_dob")) {
		 String[] param = searchDOBParams(keyword);
		 rs = apptMainBean.queryResults(param, dboperation);

      } else if(request.getParameter("search_mode").equals("search_address") || request.getParameter("search_mode").equals("search_phone")) {
         keyword = keyword.replaceAll("-", "-?");
         if (keyword.length() < 1) keyword="^";
         rs = apptMainBean.queryResults(keyword, dboperation);
	  } else {
		 keyword="^"+request.getParameter("keyword");
		 rs = apptMainBean.queryResults(keyword, dboperation);
	  }
    }

	boolean bodd=false;
	int nItems=0;

	if(rs==null) {
		out.println("failed!!!");
	} else {
		while (rs.next()) {
			bodd=bodd?false:true; //for the color of rows
			nItems++; //to calculate if it is the end of records

			//if( !(apptMainBean.getString(rs,"month_of_birth").equals("")) && !apptMainBean.getString(rs,"year_of_birth").equals("") && !apptMainBean.getString(rs,"date_of_birth").equals("") ) {
    		//age = UtilDateUtilities.calcAge(apptMainBean.getString(rs,"year_of_birth"), apptMainBean.getString(rs,"month_of_birth"), apptMainBean.getString(rs,"date_of_birth"));
			//}
%>
	<li style="background-color: <%=bodd?"white":"#EEEEFF"%>">
		<div class="demoIdSearch">
		<%DemographicMerged dmDAO = new DemographicMerged();
            String dem_no = apptMainBean.getString(rs,"demographic_no");
            String head = dmDAO.getHead(dem_no);

            if(head != null && !head.equals(dem_no)) {
            	//skip non head records
            	continue;
            }
           if (vLocale.getCountry().equals("BR")) { %> <a
			href="demographiccontrol.jsp?demographic_no=<%= head %>&displaymode=edit&dboperation=search_detail_ptbr"><%=dem_no%></a>
		<!-- Link to Oscar Message with display mode = linkMsg2Demo --> </div> <%}else if ( fromMessenger ) {%>
		<a
			href="demographiccontrol.jsp?keyword=<%=Misc.toUpperLowerCase(apptMainBean.getString(rs,"last_name")+", "+apptMainBean.getString(rs,"first_name"))%>&demographic_no=<%= dem_no %>&displaymode=linkMsg2Demo&dboperation=search_detail"><%=apptMainBean.getString(rs,"demographic_no")%></a>
		<!-- Link to Oscar Message with display mode = edit ( default) --> </div><%}else{%>
		<a title="Master Demo File" href="#"
			onclick="popup(600,900,'demographiccontrol.jsp?demographic_no=<%= head %>&displaymode=edit&dboperation=search_detail')"><%=dem_no%></a></div>
		<!-- Rights -->
		<div class="links"><security:oscarSec roleName="<%=roleName$%>"
			objectName="_eChart" rights="r">
			<a class="encounterBtn" title="Encounter" href="#"
				onclick="popupEChart(710,1024,'<c:out value="${ctx}"/>/oscarEncounter/IncomingEncounter.do?providerNo=<%=curProvider_no%>&appointmentNo=&demographicNo=<%=dem_no%>&curProviderNo=&reason=<%=URLEncoder.encode("Tel-Progress Notes")%>&encType=&curDate=<%=""+curYear%>-<%=""+curMonth%>-<%=""+curDay%>&appointmentDate=&startTime=&status=');return false;">E</a>
		</security:oscarSec> <!-- Rights --> <security:oscarSec roleName="<%=roleName$%>"
			objectName="_rx" rights="r">
			<a class="rxBtn" title="Prescriptions" href="#" onclick="popup(700,1027,'../oscarRx/choosePatient.do?providerNo=<%=rs.getString("provider_no")%>&demographicNo=<%=dem_no%>')">Rx</a>
		</security:oscarSec></div>
		<%}%>
		<% if (OscarProperties.getInstance().isPropertyActive("new_eyeform_enabled")) { %>
		<security:oscarSec roleName="<%=roleName$%>"
			objectName="_eChart" rights="r">
			<a title="Eyeform" href="#" onclick="popup(800, 1280, '../eyeform/eyeform.jsp?demographic_no=<%=dem_no %>&reason=')">EF</a>
		</security:oscarSec>
		<% } %>
		<div class="name"><%=Misc.toUpperLowerCase(rs.getString("last_name"))%>, <%=Misc.toUpperLowerCase(rs.getString("first_name"))%></div>
		<div class="chartNo"><%=apptMainBean.getString(rs,"chart_no")==null||apptMainBean.getString(rs,"chart_no").equals("")?"&nbsp;":apptMainBean.getString(rs,"chart_no")%></div>
		<div class="sex"><%=apptMainBean.getString(rs,"sex")%></div>
		<div class="dob"><%=apptMainBean.getString(rs,"year_of_birth")+"-"+apptMainBean.getString(rs,"month_of_birth")+"-"+apptMainBean.getString(rs,"date_of_birth")%></div>
		<div class="doctor"><%=Misc.getShortStr(providerBean.getProperty(apptMainBean.getString(rs,"provider_no")),"_",12 )%></div>
		<div class="rosterStatus"><%=apptMainBean.getString(rs,"roster_status")==null||apptMainBean.getString(rs,"roster_status").equals("")?"&nbsp;":apptMainBean.getString(rs,"roster_status")%></div>
		<div class="patientStatus"><%=apptMainBean.getString(rs,"patient_status")==null||apptMainBean.getString(rs,"patient_status").equals("")?"&nbsp;":apptMainBean.getString(rs,"patient_status")%></div>
		<div class="phone"><%=apptMainBean.getString(rs,"phone")==null||apptMainBean.getString(rs,"phone").equals("")?"&nbsp;":(apptMainBean.getString(rs,"phone").length()==10?(apptMainBean.getString(rs,"phone").substring(0,3)+"-"+apptMainBean.getString(rs,"phone").substring(3)):apptMainBean.getString(rs,"phone"))%></div>
	</li>
	<%
		}
	}
%>
</ul>
<div id="legend">
<sup>*</sup><bean:message key="demographic.demographicsearchresults.msgSortDemographic"/>  <br>
<sup>1</sup><bean:message key="demographic.demographicsearchresults.msgSortsLastName"/>   <br>
<br>
</div>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%> <a
	href="demographiccontrol.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>&ptstatus=<%=request.getParameter("ptstatus")%>"><bean:message
	key="demographic.demographicsearchresults.btnLastPage" /></a> <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
      if (nLastPage>=0) {
%> | <%    } %> <a
	href="demographiccontrol.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>&ptstatus=<%=request.getParameter("ptstatus")%>">
<bean:message key="demographic.demographicsearchresults.btnNextPage" /></a>
<%
}
%>

<div class="createNew">
	<a href="demographiccontrol.jsp?displaymode=Create"><b><font size="+1"><bean:message key="demographic.search.btnCreateNew" /></font></b></a>
	<br>
</div>

<p><bean:message key="demographic.demographicsearchresults.msgClick" /></p>
</center>
</body>
</html:html>
<%!
	String[] searchDOBParams(String keyword) {
		String[] param =new String[3];

		if( keyword.indexOf("-") != -1 ) {
	 		param[0] = keyword.substring(0,4);
	 		param[1] = keyword.substring(keyword.indexOf("-")+1, keyword.lastIndexOf("-"));
	 		param[2] = keyword.substring(keyword.lastIndexOf("-")+1);
		}
		else if( keyword.length() == 8 ) {
			param[0] = keyword.substring(0,4);
			param[1] = keyword.substring(4,6);
			param[2] = keyword.substring(6);
		}
		else {
			param[0] = "%";
			param[1] = "%";
			param[2] = "%";
		}

		return param;
	}

%>
