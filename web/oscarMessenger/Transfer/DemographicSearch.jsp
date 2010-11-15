<%--  
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
--%>


<%@ page 
	import="oscar.oscarMessenger.docxfer.send.*, oscar.oscarMessenger.docxfer.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<%String aId = null;%>

<html:html locale="true">


<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />

<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<title>Demographic Search</title>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script language="JavaScript">
<!--
function conf()
{
    var ret;
    ret = window.confirm("Are you sure this is the right patient?  \n This is a permanent action!");

    return ret;
}
//-->
</script>

</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">oscarComm</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Demographic Search</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a
					href="javascript:popupStart(300,400,'Help.jsp')">Help</a> | <a
					href="javascript:popupStart(300,400,'About.jsp')">About</a> | <a
					href="javascript:popupStart(300,400,'License.jsp')">License</a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn"></td>
		<td class="MainTableRightColumn"><html:form
			action="/oscarMessenger/SearchDemographic">
			<%
                                aId = (String) request.getAttribute("IDenc");
                                oscar.oscarMessenger.pageUtil.MsgSessionBean bean;
                                bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean)request.getSession().getAttribute("msgSessionBean");
                                aId = bean.getMessageId();
                                %>
			<input type=hidden name="id" value="<%=aId%>" />

			<table>
				<tr>
					<td class="tite4">First Name:</td>
					<td><html:text property="firstName" /></td>

					<td class="tite4">Last Name:</td>
					<td><html:text property="lastName" /></td>
					<td class="tite4">sex:</td>
					<td>
					<table height="100%">
						<tr>
							<td>M</td>
							<td><html:radio property="sex" value="M" /></td>
							<td>F</td>
							<td><html:radio property="sex" value="F" /></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="tite4">Year Of Birth:</td>
					<td><html:text property="yearOfBirth" /></td>

					<td class="tite4">Month Of Birth:</td>
					<td><html:text property="monthOfBirth" /></td class="tite4">

					<td class="tite4">Day Of Birth:</td>
					<td><html:text property="dayOfBirth" /></td>
				</tr>

				<tr>
					<td class="tite4">Address:</td>
					<td><html:text property="address" /></td>
					<td class="tite4">City:</td>
					<td colspan="3"><html:text property="city" /></td>
				</tr>
				<tr>
					<td class="tite4">Phone:</td>
					<td colspan="5"><html:text property="phone" /></td>
				</tr>
				<tr>
					<td class="tite4">HIN:</td>
					<td colspan="5"><html:text property="hin" /></td>
				</tr>
				<tr>
					<td class="tite4">Chart Number:</td>
					<td colspan="5"><html:text property="chartNumber" /></td>
				</tr>




				<tr>
					<td><input type="submit" name="Submit" value="Search" /> <input
						type="reset" value="Reset" /></td>
				</tr>
			</table>
		</html:form> <%
                java.util.Vector searchVec = (java.util.Vector) request.getAttribute("searchVector");
                if (searchVec != null){

                    if (searchVec.size() != 0){

                    %>
		<table>
			<tr>
				<th class="title">Demo #</th>
				<th class="title">Last Name</th>
				<th class="title">First Name</th>
				<th class="title">Address</th>
				<th class="title">City</th>
				<th class="title">Province</th>
				<th class="title">Sex</th>
				<th class="title">Hin</th>
				<th class="title">DOB</th>
				<th class="title">Phone</th>
			</tr>
			<%

                        for ( int i = 0; i < searchVec.size(); i++){
                            oscar.oscarEncounter.search.data.EctDemographicData demo = (oscar.oscarEncounter.search.data.EctDemographicData) searchVec.elementAt(i);
                            String forClass;


                            if ( (i % 2) == 0){ forClass="even"; }else{ forClass="odd"; }
                        %>
			<tr class="<%=forClass%>">
				<td><a
					href="../../oscarMessenger/Transfer/Proceed.do?demoId=<%=demo.demographicNo%>&id=<%=aId%>"
					onclick="javascript:return conf()"; > <%=demo.demographicNo%> </a>
				</td>
				<td><%=demo.lastName%></td>
				<td><%=demo.firstName%></td>
				<td><%=demo.address%></td>
				<td><%=demo.city%></td>
				<td><%=demo.province%></td>
				<td><%=demo.sex%></td>
				<td><%=demo.hin%></td>
				<td><%=demo.yearOfBirth%>/<%=demo.monthOfBirth%>/<%=demo.dayOfBirth%>
				</td>
				<td><%=demo.phone%></td>
			</tr>

			<%}/*for*/%>
		</table>
		<%}else{%>
		<h3 class="noFound">Your Search produced no results</h3>
		<%}
                }%>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
