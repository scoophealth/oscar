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

<%@page import="org.oscarehr.common.model.Consent"%>
<%@page import="org.oscarehr.common.dao.ConsentDao"%>
<%@page import="org.oscarehr.common.model.CVCMapping"%>
<%@page import="org.oscarehr.common.dao.CVCImmunizationDao"%>
<%@page import="org.oscarehr.common.dao.CVCMappingDao"%>
<%@page import="org.oscarehr.common.model.CVCMedicationLotNumber"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.common.model.CVCImmunization"%>
<%@page import="org.oscarehr.managers.CanadianVaccineCatalogueManager"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.oscarProvider.data.ProviderData"%>
<%@ page import="oscar.oscarDemographic.data.DemographicData,java.text.SimpleDateFormat, java.util.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*"%>
<%@ page import="org.oscarehr.casemgmt.model.CaseManagementNoteLink" %>
<%@ page import="org.oscarehr.casemgmt.service.CaseManagementManager" %>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DemographicExtDao" %>
<%@page import="org.oscarehr.common.dao.PreventionsLotNrsDao" %>
<%@page import="org.oscarehr.common.model.PreventionsLotNrs" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_prevention" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_prevention");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
 
CVCImmunizationDao cvcImmunizationDao = SpringUtils.getBean(CVCImmunizationDao.class);

  String prevention = request.getParameter("prevention");
String demographicNo = request.getParameter("demographic_no");
String prevResultDesc = request.getParameter("prevResultDesc");
 
CVCMappingDao cvcMappingDao = SpringUtils.getBean(CVCMappingDao.class);

List<CVCMapping> mappings = cvcMappingDao.findMultipleByOscarName(prevention);

//calc age at time of prevention
 String dateFmt = "yyyy-MM-dd HH:mm";
 String prevDate = UtilDateUtilities.getToday(dateFmt);
Date dob = PreventionData.getDemographicDateOfBirth(LoggedInInfo.getLoggedInInfoFromSession(request), Integer.valueOf(demographicNo));
SimpleDateFormat fmt = new SimpleDateFormat(dateFmt);
Date dateOfPrev = fmt.parse(prevDate);
String age = UtilDateUtilities.calcAgeAtDate(dob, dateOfPrev);
DemographicData demoData = new DemographicData();
String[] demoInfo = demoData.getNameAgeSexArray(LoggedInInfo.getLoggedInInfoFromSession(request), Integer.valueOf(demographicNo));
String nameage = demoInfo[0] + ", " + demoInfo[1] + " " + demoInfo[2] + " " + age;


%>


<html:html locale="true">

<head>
<title>
<bean:message key="oscarprevention.index.oscarpreventiontitre" />
</title><!--I18n-->
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../share/calendar/calendar.js" ></script>
<script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>" ></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js" ></script>

<style type="text/css">
  div.ImmSet { background-color: #ffffff; }
  div.ImmSet h2 {  }
  div.ImmSet ul {  }
  div.ImmSet li {  }
  div.ImmSet li a { text-decoration:none; color:blue;}
  div.ImmSet li a:hover { text-decoration:none; color:red; }
  div.ImmSet li a:visited { text-decoration:none; color:blue;}


  ////////
  div.prevention {  background-color: #999999; }
  div.prevention fieldset {width:35em; font-weight:bold; }
  div.prevention legend {font-weight:bold; }

  ////////
</style>



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


label{
float: left;
width: 120px;
font-weight: bold;
}

label.checkbox{
float: left;
width: 116px;
font-weight: bold;
}

label.fields{
float: left;
width: 80px;
font-weight: bold;
}

span.labelLook{
font-weight:bold;

}

input, textarea,select{

//margin-bottom: 5px;
}

textarea{
width: 450px;
height: 100px;
}


.boxes{
width: 1em;
}

#submitbutton{
margin-left: 120px;
margin-top: 5px;
width: 90px;
}

br{
clear: left;
}
</style>

</head>

<body class="BodyStyle" vlink="#0000FF" onload="disableifchecked(document.getElementById('neverWarn'),'nextDate');">
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn" width="100" >
               <bean:message key="oscarprevention.index.oscarpreventiontitre" />
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            <%=nameage%>
                        </td>
                        <td  >&nbsp;

                        </td>
                        <td style="text-align:right">
                                <oscar:help keywords="prevention" key="app.top1"/> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn" valign="top">


               &nbsp;

            </td>
            <td valign="top" class="MainTableRightColumn">
				<h4>Please choose the immunization you would like to add.</h4>
				<ul>
					<%for(CVCMapping mapping:mappings) { 
						CVCImmunization i = cvcImmunizationDao.findBySnomedConceptId(mapping.getCvcSnomedId());
						if(i != null) {
					%>
					
					<li><a href="<%=request.getContextPath()%>/oscarPrevention/AddPreventionData.jsp?snomedId=<%=mapping.getCvcSnomedId() %>&prevention=<%=prevention %>&demographic_no=<%=demographicNo %>&prevResultDesc=<%=prevResultDesc%>"><%=i.getPicklistName()%></a></li>
					<% } } %>
					
				</ul>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">
            &nbsp;
            </td>
            <td class="MainTableBottomRowRightColumn" valign="top">
            &nbsp;
            </td>
        </tr>
    </table>
  
</body>
</html:html>

