<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ include file="/taglibs.jsp"%>

<%@page import="java.util.List"%>
<%@page import="org.oscarehr.PMmodule.model.Formintakea"%>

<%
	String numOfIntakeAs = "";
	if (request.getAttribute("numOfIntakeAs") != null) {
		numOfIntakeAs = (String) request.getAttribute("numOfIntakeAs");
	}

	String numOfReadmission = "";
	if (request.getAttribute("numOfReadmission") != null) {
		numOfReadmission = (String) request.getAttribute("numOfReadmission");
	}

	String proportionOfReadmission = "";
	if (request.getAttribute("proportionOfReadmission") != null) {
		proportionOfReadmission = (String) request.getAttribute("proportionOfReadmission");
	}

	String rateOfAdmission = "";
	if (request.getAttribute("rateOfAdmission") != null) {
		rateOfAdmission = (String) request.getAttribute("rateOfAdmission");
	}

	String rateOfReadmission = "";
	if (request.getAttribute("rateOfReadmission") != null) {
		rateOfReadmission = (String) request.getAttribute("rateOfReadmission");
	}

	String avgDOB = "";
	if (request.getAttribute("avgDOB") != null) {
		avgDOB = (String) request.getAttribute("avgDOB");
	}

	List intakeA4Stats = null;
	if (request.getAttribute("intakeA4Stats") != null) {
		intakeA4Stats = (List) request.getAttribute("intakeA4Stats");
	}

	Formintakea intakeAStats1 = new Formintakea();
	Formintakea intakeAStats1RadioUncertain = new Formintakea();
	Formintakea intakeAStats2 = new Formintakea();
	Formintakea intakeAStats2RadioUncertain = new Formintakea();

	if (intakeA4Stats != null && intakeA4Stats.size() > 0) {
		intakeAStats1 = (Formintakea) intakeA4Stats.get(0);
		intakeAStats1RadioUncertain = (Formintakea) intakeA4Stats.get(1);
		intakeAStats2 = (Formintakea) intakeA4Stats.get(2);
		intakeAStats2RadioUncertain = (Formintakea) intakeA4Stats.get(3);
	}
%>

<html:html xhtml="true" locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Untitled Document</title>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite page="/css/intakeA.css"/>"></link>
<script language="JavaScript"
	src="<html:rewrite page="/js/ClientSearch.js" />"></script>
<html:base></html:base>
</head>
<body>

<html:form action="/PMmodule/IntakeAReport1Action.admit" method="post">
	<table height="15" align="center">
		<tr>
			<td class="style76" align="center"><input type="button"
				name="backToClientSearch" value="Back"
				onclick="javascript:history.back()" /></td>
		</tr>
	</table>
	<table border="1" cellpadding="1" cellspacing="1" width="95%"
		align="center">
		<tr>
			<td class="style76">1</td>
			<td class="style76" colspan="2" align="center">Section</td>
			<td class="style76" width="50">Rate per year</td>
			<td class="style76" width="69">Proportion in time period</td>
			<td class="style76" width="59">Number in Time Period</td>
			<td class="style76" width="69">Proportion of clients admitted</td>
			<td class="style76" width="62">&nbsp;</td>
			<td class="style76" width="68">Number of Clients<br />
			admitted in time period</td>
			<td class="style76" width="62">&nbsp;</td>
			<td class="style76" width="55">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">2</td>
			<td class="style76" width="41">&nbsp;</td>
			<td class="style76" width="389">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">Yes</td>
			<td class="style76">Uncertain</td>
			<td class="style76">Yes</td>
			<td class="style76">Uncertain</td>
			<td class="style76">Average</td>
		</tr>
		<tr>
			<td class="style76" height="10" colspan="11">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">3</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">Date of Birth (age)</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=avgDOB%>&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">4</td>
			<td class="style76">A</td>
			<td class="style76">Clients admitted into Seaton House</td>
			<td class="style76"><%=rateOfAdmission%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=numOfIntakeAs%></td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">5</td>
			<td class="style76">A</td>
			<td class="style76">Clients who are re-admission to Seaton House</td>
			<td class="style76"><%=rateOfReadmission%>&nbsp;</td>
			<td class="style76"><%=proportionOfReadmission%>&nbsp;</td>
			<td class="style76"><%=numOfReadmission%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">6</td>
			<td class="style76">2</td>
			<td class="style76">The client reports requiring assistance with
			any of the following:</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">7</td>
			<td class="style76">2</td>
			<td class="style76">Physical or Mental Health, including
			medication</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxAssistinhealth()%>&nbsp;&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxAssistinhealth()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">8</td>
			<td class="style76">2</td>
			<td class="style76">Obtaining Identification</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxAssistinidentification()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxAssistinidentification()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">9</td>
			<td class="style76">2</td>
			<td class="style76">Addictions</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxAssistinaddictions()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxAssistinaddictions()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">10</td>
			<td class="style76">2</td>
			<td class="style76">Housing issues</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxAssistinhousing()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxAssistinhousing()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">11</td>
			<td class="style76">2</td>
			<td class="style76">Education Issues</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxAssistineducation()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxAssistineducation()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">12</td>
			<td class="style76">2</td>
			<td class="style76">Employment issues</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxAssistinemployment()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxAssistinemployment()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">13</td>
			<td class="style76">2</td>
			<td class="style76">Financial issues</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxAssistinfinance()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxAssistinfinance()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">14</td>
			<td class="style76">2</td>
			<td class="style76">Legal issues</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxAssistinlegal()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxAssistinlegal()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">15</td>
			<td class="style76">2</td>
			<td class="style76">Immigration issues</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxAssistinimmigration()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxAssistinimmigration()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">16</td>
			<td class="style76">3</td>
			<td class="style76">What identification does client have?</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">17</td>
			<td class="style76">3</td>
			<td class="style76">SIN Card:</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxSincard()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxSincard()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">18</td>
			<td class="style76">3</td>
			<td class="style76">Ontario Health Card#</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxHealthcard()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxHealthcard()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">19</td>
			<td class="style76">3</td>
			<td class="style76">Birth Certificate</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxBirthcertificate()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxBirthcertificate()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">20</td>
			<td class="style76">3</td>
			<td class="style76">Citizenship Card</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxCitzenshipcard()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxCitzenshipcard()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">21</td>
			<td class="style76">3</td>
			<td class="style76">Landed Immigrant</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxImmigrant()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxImmigrant()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">22</td>
			<td class="style76">3</td>
			<td class="style76">Convention Refugee</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxRefugee()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxRefugee()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">23</td>
			<td class="style76">3</td>
			<td class="style76">Other (specify)</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxOtherid()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxOtherid()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">24</td>
			<td class="style76">4</td>
			<td class="style76">What has been your main source of income in
			the past 12 months (check one)</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">25</td>
			<td class="style76">4</td>
			<td class="style76">OW</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxOw()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxOw()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">26</td>
			<td class="style76">4</td>
			<td class="style76">ODSP</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxOdsp()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxOdsp()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">27</td>
			<td class="style76">4</td>
			<td class="style76">WSIB</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxWsib()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxWsib()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">28</td>
			<td class="style76">4</td>
			<td class="style76">Employment</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxEmployment()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxEmployment()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">29</td>
			<td class="style76">4</td>
			<td class="style76">EI</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxEi()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxEi()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">30</td>
			<td class="style76">4</td>
			<td class="style76">OAS</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxOas()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxOas()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">31</td>
			<td class="style76">4</td>
			<td class="style76">CPP</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxCpp()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxCpp()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">32</td>
			<td class="style76">4</td>
			<td class="style76">Other</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxOtherincome()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxOtherincome()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">33</td>
			<td class="style76">5</td>
			<td class="style76">Health</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">34</td>
			<td class="style76">5</td>
			<td class="style76">Does client have a regular medical doctor or
			specialist?</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioHasdoctor()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioHasdoctor()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioHasdoctor()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioHasdoctor()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">35</td>
			<td class="style76">5</td>
			<td class="style76">Does client have any health issue that we
			should know about in the event of an emergency?</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioHealthissue()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioHealthissue()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioHealthissue()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioHealthissue()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">36</td>
			<td class="style76">5</td>
			<td class="style76">Do you have any of the following (read list
			and check all that apply):</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">37</td>
			<td class="style76">5</td>
			<td class="style76">Diabetes.</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxHasdiabetes()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxHasdiabetes()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">38</td>
			<td class="style76">5</td>
			<td class="style76">Epilepsy</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxEpilepsy()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxEpilepsy()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">39</td>
			<td class="style76">5</td>
			<td class="style76">Bleeding Disorder</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxBleeding()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxBleeding()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">40</td>
			<td class="style76">5</td>
			<td class="style76">Hearing Impairment</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxHearingimpair()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxHearingimpair()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">41</td>
			<td class="style76">5</td>
			<td class="style76">Visual Impairment</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxVisualimpair()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxVisualimpair()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">42</td>
			<td class="style76">5</td>
			<td class="style76">Mobility impairment.</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxMobilityimpair()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxMobilityimpair()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">43</td>
			<td class="style76">5</td>
			<td class="style76">Are you presently taking any medications?</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioTakemedication()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioTakemedication()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioTakemedication()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioTakemedication()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">44</td>
			<td class="style76">5</td>
			<td class="style76">Do you need help obtaining medication?</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioHelpobtainmedication()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioHelpobtainmedication()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioHelpobtainmedication()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioHelpobtainmedication()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">45</td>
			<td class="style76">5</td>
			<td class="style76">Are you allergic to any medications?</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioAllergictomedication()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioAllergictomedication()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioAllergictomedication()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioAllergictomedication()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">46</td>
			<td class="style76">5</td>
			<td class="style76">Do you have any mental health concerns that
			you wish to share with us?</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioMentalhealthconcerns()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioMentalhealthconcerns()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioMentalhealthconcerns()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioMentalhealthconcerns()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">47</td>
			<td class="style76">6</td>
			<td class="style76">Not counting when you were an overnight
			patient, in the past 12 months, how many times have you seen a
			general practitioner or family physician about your physical,
			emotional or mental health? 20 TIMES</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">48</td>
			<td class="style76">6</td>
			<td class="style76">Would you be able to see this doctor again
			if you needed to see a doctor?</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioSeesamedoctor()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioSeesamedoctor()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioSeesamedoctor()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioSeesamedoctor()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">49</td>
			<td class="style76">6</td>
			<td class="style76">During the past 12 months, was there ever a
			time when you needed health care or advice but did not receive it?</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioDidnotreceivehealthcare()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioDidnotreceivehealthcare()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioDidnotreceivehealthcare()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioDidnotreceivehealthcare()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">50</td>
			<td class="style76">6</td>
			<td class="style76">Of those who said yes:</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">51</td>
			<td class="style76">6</td>
			<td class="style76">Treatment of a physical health problem</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxTreatphysicalhealth()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxTreatphysicalhealth()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">52</td>
			<td class="style76">6</td>
			<td class="style76">Treatment of an emotional or mental health
			problem</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxTreatmentalhealth()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxTreatmentalhealth()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">53</td>
			<td class="style76">6</td>
			<td class="style76">A regular check-up</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxRegularcheckup()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxRegularcheckup()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">54</td>
			<td class="style76">6</td>
			<td class="style76">Any other reason (specify)</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxTreatotherreasons()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxTreatotherreasons()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">55</td>
			<td class="style76">6</td>
			<td class="style76">Care of injury</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxTreatinjury()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxTreatinjury()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">56</td>
			<td class="style76">6</td>
			<td class="style76">If you had a physical, emotional or mental
			health problem that you needed help with, where would you go for
			help? (Read list. Mark one only)</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">57</td>
			<td class="style76">6</td>
			<td class="style76">Walk-in clinic</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxGotowalkinclinic()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxGotowalkinclinic()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">58</td>
			<td class="style76">6</td>
			<td class="style76">Community health centre</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxGotohealthcenter()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxGotohealthcenter()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">59</td>
			<td class="style76">6</td>
			<td class="style76">Hospital emergency room</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxGotoemergencyroom()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxGotoemergencyroom()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">60</td>
			<td class="style76">6</td>
			<td class="style76">Other (specify)</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxGotoothers()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxGotoothers()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">61</td>
			<td class="style76">6</td>
			<td class="style76">Health Professionals office</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getCboxHealthoffice()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getCboxHealthoffice()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">62</td>
			<td class="style76">6</td>
			<td class="style76">Clients who have an appointment to see a
			general practitioner or family doctor in the next 3 months?</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioAppmtseedoctorin3mths()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioAppmtseedoctorin3mths()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioAppmtseedoctorin3mths()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioAppmtseedoctorin3mths()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">63</td>
			<td class="style76">6</td>
			<td class="style76">Clients who would benefit from having a
			regular doctor or need a regular doctor?</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioNeedregulardoctor()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioNeedregulardoctor()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioNeedregulardoctor()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioNeedregulardoctor()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">64</td>
			<td class="style76">6</td>
			<td class="style76">Clients who would object to having an
			appointment with a regular doctor in the next 4 weeks?</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioObjecttoregulardoctorin4wks()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioObjecttoregulardoctorin4wks()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioObjecttoregulardoctorin4wks()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioObjecttoregulardoctorin4wks()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">65</td>
			<td class="style76">6</td>
			<td class="style76">Clients who reported their health as poor or
			less than poor</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioRateoverallhealth()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioRateoverallhealth()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">66</td>
			<td class="style76">8</td>
			<td class="style76">Proportion of clients who staff have
			concerns about</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">67</td>
			<td class="style76">8</td>
			<td class="style76">uncontrolled severe mental illness</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioHasmentalillness()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioHasmentalillness()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioHasmentalillness()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioHasmentalillness()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">68</td>
			<td class="style76">8</td>
			<td class="style76">uncontrolled drinking</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioHasdrinkingproblem()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioHasdrinkingproblem()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioHasdrinkingproblem()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioHasdrinkingproblem()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">69</td>
			<td class="style76">8</td>
			<td class="style76">Uncontrolled drug use</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioHasdrugproblem()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioHasdrugproblem()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioHasdrugproblem()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioHasdrugproblem()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">70</td>
			<td class="style76">8</td>
			<td class="style76">Uncontrolled physical health problem</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioHashealthproblem()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioHashealthproblem()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioHashealthproblem()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioHashealthproblem()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">71</td>
			<td class="style76">8</td>
			<td class="style76">Severe behaviours problems</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioHasbehaviorproblem()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioHasbehaviorproblem()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioHasbehaviorproblem()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioHasbehaviorproblem()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
		<tr>
			<td class="style76">72</td>
			<td class="style76">8</td>
			<td class="style76">This person will need Seaton House services
			for more than 60 days</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76">&nbsp;</td>
			<td class="style76"><%=intakeAStats2.getRadioNeedseatonservice()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats2RadioUncertain.getRadioNeedseatonservice()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1.getRadioNeedseatonservice()%>&nbsp;</td>
			<td class="style76"><%=intakeAStats1RadioUncertain.getRadioNeedseatonservice()%>&nbsp;</td>
			<td class="style76">&nbsp;</td>
		</tr>
		<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
	</table>
</html:form>
</body>
</html:html>
