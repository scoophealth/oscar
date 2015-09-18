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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import ="oscar.oscarReport.data.RptSearchData,java.util.*"%>


<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">

<%
    oscar.oscarReport.data.RptSearchData searchData  = new oscar.oscarReport.data.RptSearchData();
    java.util.ArrayList rosterArray;
    java.util.ArrayList patientArray;
    java.util.ArrayList providerArray;
    java.util.ArrayList queryArray;
    rosterArray   = searchData.getRosterTypes();
    patientArray  = searchData.getPatientTypes();
    providerArray = searchData.getProvidersWithDemographics();
    queryArray    = searchData.getQueryTypes();
    
    String studyId = request.getParameter("studyId");
    if( studyId == null ) {
		studyId = (String)request.getAttribute("studyId");
    }
%>

<html:html>
<head>
<title>
Demographic Report tool
</title>
<html:base/>
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" /> 
     
<script type="text/javascript" src="../share/calendar/calendar.js" ></script>      
<script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>" ></script>      
<script type="text/javascript" src="../share/calendar/calendar-setup.js" ></script>      

<script language="JavaScript">
function checkQuery() {
    var ret = false;
    var chks = document.forms[0].select;
    for (var i=0; i<chks.length; i++) {
	if (chks[i].checked) {
	    ret = true;
	    break;
	}
    }
    if (!ret) alert("Please select at least one field");
    return ret;
}
</script>

<style type="text/css" media="print">
.MainTable {
    display:none;
}
.hiddenInPrint{
    display:none;
}
//.header INPUT {
//    display:none;
//}
//
//.header A {
//    display:none;
//}

</style>

<style type="text/css">
     #provider{
        margin-top: 0;
        margin-left: 0;
        margin-bottom: 3px;
        padding: 0;
     }
 
     #provider li{
        display: inline;
        float:left;
        list-style-type: none;
        //padding-left: 30px;
        padding: 4px;
        padding-top: 6px;
        padding-bottom: 0px;
        border-bottom: 1px solid #666;
        border-right: 1px solid #666;
        border-top: 1px solid #666;
        border-left: 1px solid #666;
        margin-right: 2px;
        margin-bottom: 2px;
     }


 </style>
                    

</head>

<body class="BodyStyle" vlink="#0000FF">

    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                oscarReport
            </td>
            <td class="MainTableTopRowRightColumn">
            <html:form action="/report/DemographicReport" onsubmit="return checkQuery();">
            <html:hidden property="studyId" value='<%=studyId == null ? "" : studyId%>'/>
                <table class="TopStatusBar">
                    <tr>
                        <td >
                        Demographic Search
                        </td>
                        <td>
                            <html:select property="savedQuery">
                            <%
                            for (int i =0 ; i < queryArray.size(); i++){
                            RptSearchData.SearchCriteria sc = (RptSearchData.SearchCriteria) queryArray.get(i);
                            String qId = sc.id;
                            String qName = sc.queryName;%>
                            <html:option value="<%=qId%>"><%=qName%></html:option>

                            <%}%>
                            </html:select>
                            <input type="submit" value="Load Query" name="query"/>
                            <a  href="ManageDemographicQueryFavourites.jsp">manage</a>
                        </td>
                        <td style="text-align:right">
                                <oscar:help keywords="report" key="app.top1"/> | <a href="javascript:popupStart(300,400,'About.jsp')" >About</a> | <a href="javascript:popupStart(300,400,'License.jsp')" >License</a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>

            <td class="MainTableLeftColumn">
           &nbsp;
            </td>
            <td class="MainTableRightColumn">

<%
if (request.getAttribute("formBean") != null){
oscar.oscarReport.pageUtil.RptDemographicReportForm thisForm;
thisForm = (oscar.oscarReport.pageUtil.RptDemographicReportForm)request.getAttribute("RptDemographicReportForm");
thisForm.copyConstructor((oscar.oscarReport.pageUtil.RptDemographicReportForm)request.getAttribute("formBean"));

}
oscar.oscarReport.pageUtil.RptDemographicReportForm thisForm;
thisForm = (oscar.oscarReport.pageUtil.RptDemographicReportForm)request.getAttribute("RptDemographicReportForm");


if ( thisForm != null || thisForm.getAgeStyle() == null || thisForm.getAgeStyle().equals("2") ){
    thisForm.setAgeStyle("1");
 }
%>

<table border=1>
<tr>
    <td>    
    <table>
        <tr>
            <th colspan="2">
                Search For
            </th>
        </tr>
        <tr>
            <td>
            <html:multibox property="select" value="demographic_no"/>
            </td>
            <td>
            Demographic #
            </td>
        </tr>
        <tr>
            <td>
            <html:multibox property="select" value="last_name"/>
            </td>
            <td>
            Last Name
            </td>
        </tr>
        <tr>
            <td>
            <html:multibox property="select" value="first_name"/>
            </td>
            <td>
            First Name
            </td>
        </tr>
        <tr>
            <td>
            <html:multibox property="select" value="address"/>
            </td>
            <td>
            Address
            </td>
        </tr>
        <tr>
            <td>
            <html:multibox property="select" value="city"/>
            </td>
            <td>
            City
            </td>
        </tr>
        <tr>
            <td>
            <html:multibox property="select" value="province"/>
            </td>
            <td>
            Province
            </td>
        </tr>
        <tr>
            <td>
            <html:multibox property="select" value="postal"/>
            </td>
            <td>
            Postal Code
            </td>
        </tr>
        <tr>
            <td>
            <html:multibox property="select" value="phone"/>
            </td>
            <td>
            Phone
            </td>
        </tr>
       <tr>
            <td>
            <html:multibox property="select" value="phone2"/>
            </td>
            <td>
            Phone 2
            </td>
        </tr>
        <tr>
            <td>
            <html:multibox property="select" value="email"/>
            </td>
            <td>
                <bean:message key="oscarReport.oscarReportscpbDemo.msgEmail"/>
            </td>
        </tr>
       <tr>
            <td>
            <html:multibox property="select" value="year_of_birth"/>
            </td>
            <td>
            Year of Birth
            </td>
        </tr>
       <tr>
            <td>
            <html:multibox property="select" value="month_of_birth"/>
            </td>
            <td>
            Month of Birth
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="date_of_birth"/>
            </td>
            <td>
            Date of Birth
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="hin"/>
            </td>
            <td>
            HIN
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="ver"/>
            </td>
            <td>
            Version Code
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="roster_status"/>
            </td>
            <td>
            Roster Status
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="patient_status"/>
            </td>
            <td>
            Patient Status
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="date_joined"/>
            </td>
            <td>
            Date Joined
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="chart_no"/>
            </td>
            <td>
            Chart #
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="provider_no"/>
            </td>
            <td>
            Provider #
            </td>
        </tr>
        <tr>
            <td>
            <html:multibox property="select" value="provider_name"/>
            </td>
            <td>
            Provider Name
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="sex"/>
            </td>
            <td>
            Sex
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="end_date"/>
            </td>
            <td>
            End Date
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="eff_date"/>
            </td>
            <td>
            Eff. Date
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="pcn_indicator"/>
            </td>
            <td>
            Pcn indicator
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="hc_type"/>
            </td>
            <td>
            Health Card Type
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="hc_renew_date"/>
            </td>
            <td>
            HC Renew Date
            </td>
        </tr>
      <tr>
            <td>
            <html:multibox property="select" value="family_doctor"/>

            </td>
            <td>
            Family Doctor
            </td>
        </tr>
        <tr>
            <td>
            <html:multibox property="select" value="newsletter"/>

            </td>
            <td>
                <bean:message key="oscarReport.oscarReportDemoReport.frmNewsletter" />
            </td>
        </tr>
    </table>
<html:text property="queryName"/><br>
<input type="submit" value="Save Query" name="query"/>
<input type="submit" value="Run Query"  name="query"/><br/>
<%if( studyId != null && !studyId.equals("") && !studyId.equalsIgnoreCase("null")) {%>
<input type="submit" value="Add to Study" name="query"/>
<%} %>
    </td>
    <td valign=top>

        <table border=1>
            
            <tr>
                <th colspan="4">
                    Where
                </th>
            </tr>
            <tr>
                <td>
                    AGE
                </td>
                <td>
                    <html:select property="age">
                        <html:option value="0">---NO AGE SPECIFIED---</html:option>
                        <html:option value="1">younger than</html:option>
                        <html:option value="2">older than</html:option>
                        <html:option value="3">equal too</html:option>
                        <html:option value="4">ages between</html:option>
                    </html:select>
                </td>
                <td>
                    <html:text  property="startYear" size="4"/>
                    <html:text property="endYear" size="4"/>
                </td>
                <td>

                    Age Style:
                    Exact:
                    <html:radio property="ageStyle" value="1" />
                    In the year
                    <html:radio property="ageStyle" value="2" />
                    As of : <html:text property="asofDate" size="9" styleId="asofDate" /> <a id="date"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a> <br>                        
                </td>
            </tr>

            <tr>
                <td>
                    First Name
                </td>
                <td>
                    <html:text property="firstName"/>
                </td>
                <td>
                    Last Name
                </td>
                <td>
                    <html:text property="lastName"/>
                </td>
            </tr>

            <tr>
                <td>
                    Roster Status
                </td>
                <td >
                    <table border=1>
                        <tr>


                    <%
                    for (int i =0 ; i < rosterArray.size(); i++){
                    String ros = (String) rosterArray.get(i);%>
                    <td> <%=ros%><br>
                    <html:multibox property="rosterStatus" value="<%=ros%>"/>
                    </td>
                    <%}
                    %>


                        </tr>
                    </table>
                </td>
                <td colspan='2'>
                &nbsp;
                </td>
            </tr>

            <tr>
                <td>
                    Sex
                </td>
                <td >
                    <html:select property="sex">
                        <html:option value="0">---NO SEX SPECIFIED---</html:option>
                        <html:option value="1">Female</html:option>
                        <html:option value="2">Male</html:option>
                    </html:select>
                </td>
                <td colspan='2'>
                &nbsp;
                </td>
            </tr>
        <tr>
                <td>
                    Provider No
                </td>
                <td colspan="3">
                    <ul id="provider">

                    <%
                    for (int i =0 ; i < providerArray.size(); i++){
                    	String pro = (String) providerArray.get(i);
                    	if( pro != null && !"".equals(pro) ) {
                    %>
                     <li > <%=providerBean.getProperty(pro,pro)%>
                       <html:multibox property="providerNo" value="<%=pro%>"/>
                     </li>
                    <%}
                    }
                    %>
                    </ul>
                </td>                
            </tr>
        <tr>
                <td>
                    Patient Status
                </td>
                <td  colspan="3">                
                    <table border=1>
                        <tr>


                    <%
                    for (int i =0 ; i < patientArray.size(); i++){
                    String pat = (String) patientArray.get(i);%>
                    <td> <%=pat%><br>
                    <html:multibox property="patientStatus" value="<%=pat%>"/>
                    </td>
                    <%}
                    %>


                        </tr>
                    </table>                    
                </td>                
            </tr>
            <tr>
            	<td>
             		Demographic ID(s):
				</td>
				<td colspan="3">
					<html:textarea property="demoIds" cols="60" rows="5"> </html:textarea>
				</td>
			</tr>            
            <tr>
                <td>
                    Order By
                </td>
                <td >
                    <html:select property="orderBy">
                        <html:option value="0">---NO ORDER---</html:option>
                        <html:option value="Demographic #">Demographic #</html:option>
                        <html:option value="Last Name">Last Name</html:option>
                        <html:option value="First Name">First Name</html:option>
                        <html:option value="Address">Address</html:option>
                        <html:option value="City">City</html:option>
                        <html:option value="Province">Province</html:option>
                        <html:option value="Postal Code">Postal Code</html:option>
                        <html:option value="Phone">Phone</html:option>
                        <html:option value="Phone 2">Phone 2</html:option>
                        <html:option value="Year of Birth">Year of Birth</html:option>
                        <html:option value="Month of Birth">Month of Birth</html:option>
                        <html:option value="Date of Birth">Date of Birth</html:option>
                        <html:option value="HIN">HIN</html:option>
                        <html:option value="Version Code">Version Code</html:option>
                        <html:option value="Roster Status">Roster Status</html:option>
                        <html:option value="Patient Status">Patient Status</html:option>
                        <html:option value="Date Joined">Date Joined</html:option>
                        <html:option value="Chart #">Chart #</html:option>
                        <html:option value="Provider #">Provider #</html:option>
                        <html:option value="Sex">Sex</html:option>
                        <html:option value="End Date">End Date</html:option>
                        <html:option value="Eff. Date">Eff. Date</html:option>
                        <html:option value="Pcn indicator">Pcn indicator</html:option>
                        <html:option value="HC Type">HC Type</html:option>
                        <html:option value="HC Renew Date">HC Renew Date</html:option>
                        <html:option value="Family Doctor">Family Doctor</html:option>
                        <html:option value="Random">Random</html:option>
                    </html:select>
                </td>
                <td colspan='2'>
                &nbsp;
                </td>
            </tr>
            <tr>
                <td>
                    Limit Results to:
                </td>
                <td >
                    <html:select property="resultNum">
                        <html:option value="0">---NO LIMIT---</html:option>
                        <html:option value="10">10</html:option>
                        <html:option value="50">50</html:option>
                        <html:option value="100">100</html:option>
                        <html:option value="150">150</html:option>
                        <html:option value="200">200</html:option>
                        <html:option value="250">250</html:option>
                        <html:option value="300">300</html:option>

                    </html:select>
                </td>
                <td colspan='2'>

                </td>
            </tr>

        </table>
</html:form>
</td>
</tr>
</table>




            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">
            &nbsp;
            </td>
            <td class="MainTableBottomRowRightColumn">
            &nbsp;
            </td>
        </tr>
    </table>
    <%
        String[] selectArray        = (String[]) request.getAttribute("selectArray");
        java.util.ArrayList searchList   = (java.util.ArrayList) request.getAttribute("searchedArray");
        if (searchList != null){
        oscar.oscarReport.data.RptDemographicColumnNames dcn = new oscar.oscarReport.data.RptDemographicColumnNames();
        %>

        Search Returned : <%=searchList.size()%> Results
        <html:form action="/report/CreateDemographicSet" >
        <% boolean includesDemo = false;
           if (selectArray[0].equals("demographic_no")){ 
              includesDemo = true; %>      
              <div class="hiddenInPrint">
              Set Name:<input type="text" name="setName"/>
              <input type="submit" value="Save Patient Set"/>
              <input type="hidden" name="size" value="<%=searchList.size()%>"/>
              </div>
        <% } %>
        
        <table border=1>
        <tr>
            <%for (int i = 0; i < selectArray.length;i++){%>
                <th>                    
                    <%=dcn.getColumnTitle(selectArray[i])%>
                </th>
            <%}%>
        </tr>
            <%

            for (int i = 0; i < searchList.size();i++){
            java.util.ArrayList al = (java.util.ArrayList) searchList.get(i);
            %>
                <tr>
                    <%for (int j = 0; j < al.size(); j++ ){
                    String str = (String) al.get(j);
                    if (str == null || str.equals("")){
                        str = "&nbsp;";
                    }
                    %>
                    <td>
                        <%=str%>
                        <%if (includesDemo && j == 0){ %>
                           <input type="hidden" name="demoNo<%=i%>" value="<%=str%>"/>
                        <%}%>
                    </td>

                    <%}%>
                </tr>
            <%}%>

        </table>        
              </html:form>        
            

    <%}%>
    <script type="text/javascript">
    Calendar.setup( { inputField : "asofDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
    </script>    
</body>
</html:html>
