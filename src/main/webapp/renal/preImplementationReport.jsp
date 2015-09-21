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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.renal.ReportHelper" %>
<%@page import="org.oscarehr.renal.ReportDataContainer" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ include file="/taglibs.jsp"%>

<%
	ReportDataContainer r = ReportHelper.getPreImplementationReportData();
	DecimalFormat df = new DecimalFormat("0.00");
	 
%>
<html>
	<head>
		<title>Pre-Implementation Report</title>
		
<style type="text/css">

table
{
border-collapse:collapse;
width: 70%;
    margin-left:auto; 
    margin-right:auto;

}
 
table, td
{
border: 1px solid #707070;
font-family: "Arial", Verdana;
padding:5px;

}

td:first-child
{

width: 280px;
}

th
{
background-color: #707070;
}

.inner
{
width: 100%;
}

.inner, .inner th, .inner td
{
border: none ; 

}

.inner tr
{
border-bottom: 1px solid #707070;

}

.inner td:first-child
{
/* border: 1px solid red; */
width: 400px; 
}


</style>		
	</head>

	<body>
		<center>CKD Pre-Implementation Report</center>
		
		<br/>
		
		<table style="width:100%" border="1">
			<thead>
				<tr>
					<th></th>
					<th></th>
					
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>Risk Factors</td>
					<td>
						<table>
							<tr>
								<td># of Diabetics</td>
								<td><%=r.getTotalDiabetic() %></td>
							</tr>
							<tr>
								<td># of Hypertensives</td>
								<td><%=r.getTotalHypertensive() %></td>
							</tr>
							<tr>
								<td># of BP>140/90</td>
								<td><%=r.getTotalBp() %></td>
							</tr>
							<tr>
								<td># w/ Family History</td>
								<td><%=r.getTotalFamHx()%></td>
							</tr>
							<tr>
								<td># of Aboriginals</td>
								<td><%=r.getTotalAboriginals()%></td>
							</tr>
							<tr>
								<td># of Patients > 55 </td>
								<td><%=r.getTotalAge()%></td>
							</tr>
						</table>
					</td>
					
				</tr>
				
				
				
				<tr>
					<td>Screened in Last Year</td>
					<td>
						<table>
							<tr>
								<td># of Diabetics</td>
								<td><%=r.getDiabeticScreened1yr() %></td>
							</tr>
							<tr>
								<td># of Hypertensives</td>
								<td><%=r.getHypertensiveScreened1yr() %></td>
							</tr>
							<tr>
								<td># of BP>140/90</td>
								<td><%=r.getBpScreened1yr() %></td>
							</tr>
							<tr>
								<td># w/ Family History</td>
								<td><%=r.getFamHxScreened1yr()%></td>
							</tr>
							<tr>
								<td># of Aboriginals</td>
								<td><%=r.getAboriginalScreened1yr()%></td>
							</tr>
							<tr>
								<td># of Patients > 55 </td>
								<td><%=r.getAgeScreened1yr()%></td>
							</tr>
						</table>
					</td>
					
				</tr>
				
				
								<tr>
					<td>Screened Ever</td>
					<td>
						<table>
							<tr>
								<td># of Diabetics</td>
								<td><%=r.getDiabeticScreened() %></td>
							</tr>
							<tr>
								<td># of Hypertensives</td>
								<td><%=r.getHypertensiveScreened() %></td>
							</tr>
							<tr>
								<td># of BP>140/90</td>
								<td><%=r.getBpScreened() %></td>
							</tr>
							<tr>
								<td># w/ Family History</td>
								<td><%=r.getFamHxScreened()%></td>
							</tr>
							<tr>
								<td># of Aboriginals</td>
								<td><%=r.getAboriginalScreened()%></td>
							</tr>
							<tr>
								<td># of Patients > 55 </td>
								<td><%=r.getAgeScreened()%></td>
							</tr>
						</table>
					</td>
					
				</tr>
				

				<tr>
					<td>Current Screening Rates</td>
					<td>
						<table>
							<tr>
								<td>Diabetics</td>
								<td><%=df.format(r.getDiabeticScreenedPerc()) %>%</td>
							</tr>
							<tr>
								<td>Hypertensives</td>
								<td><%=df.format(r.getHypertensiveScreenedPerc()) %>%</td>
							</tr>
							<tr>
								<td>BP>140/90</td>
								<td><%=df.format(r.getBpScreenedPerc()) %>%</td>
							</tr>
							<tr>
								<td>w/ Family History</td>
								<td><%=df.format(r.getFamHxScreenedPerc())%>%</td>
							</tr>
							<tr>
								<td>Aboriginals</td>
								<td><%=df.format(r.getAboriginalScreenedPerc())%>%</td>
							</tr>
							<tr>
								<td>Patients > 55 </td>
								<td><%=df.format(r.getAgeScreenedPerc())%>%</td>
							</tr>
						</table>
					</td>
					
				</tr>
				
				
				<tr>
					<td>Current Screening Rates (1yr)</td>
					<td>
						<table>
							<tr>
								<td>Diabetics</td>
								<td><%=df.format(r.getDiabeticScreenedPerc1yr()) %>%</td>
							</tr>
							<tr>
								<td>Hypertensives</td>
								<td><%=df.format(r.getHypertensiveScreenedPerc1yr()) %>%</td>
							</tr>
							<tr>
								<td>BP>140/90</td>
								<td><%=df.format(r.getBpScreenedPerc1yr()) %>%</td>
							</tr>
							<tr>
								<td>w/ Family History</td>
								<td><%=df.format(r.getFamHxScreenedPerc1yr())%>%</td>
							</tr>
							<tr>
								<td>Aboriginals</td>
								<td><%=df.format(r.getAboriginalScreenedPerc1yr())%>%</td>
							</tr>
							<tr>
								<td>Patients > 55 </td>
								<td><%=df.format(r.getAgeScreenedPerc1yr())%>%</td>
							</tr>
						</table>
					</td>
					
				</tr>

				<tr>
					<td>CKD Patients</td>
					<td>
						<table>
							<tr>
								<td>Stage 1: "Kidney Damage with normal eGFR"</td>
								<td><%=r.getCkdStage1()%></td>
							</tr>
							<tr>
								<td>Stage 2: "Kidney Damage with mild decline in eGFR"</td>
								<td><%=r.getCkdStage2()%></td>
							</tr>
							<tr>
								<td>Stage 3: "Moderate decline in eGFR"</td>
								<td><%=r.getCkdStage3()%></td>
							</tr>
							<tr>
								<td>Stage 4: "Severe decline in eGFR"</td>
								<td><%=r.getCkdStage4()%></td>
							</tr>
							<tr>
								<td>Stage 5: "Kidney Failure"</td>
								<td><%=r.getCkdStage5()%></td>
							</tr>
							
						</table>
					</td>
					
				</tr>
				
				
				<tr>
					<td>eGFR tests</td>
					<td>
						<table>
							<tr>
								<td>Ordered</td>
								<td><%=r.getEgfrTestsOrdered()%></td>
							</tr>
							<tr>
								<td>Received</td>
								<td><%=r.getEgfrTestsReceived()%></td>
							</tr>
							<tr>
								<td>Ratio</td>
								<td><%=r.getEgfrTestsRatio()%></td>
							</tr>		
						</table>
					</td>					
				</tr>
				
				<tr>
					<td>ACR Tests</td>
					<td>
						<table>
							<tr>
								<td>Ordered</td>
								<td><%=r.getAcrTestsOrdered()%></td>
							</tr>
							<tr>
								<td>Received</td>
								<td><%=r.getAcrTestsReceived()%></td>
							</tr>
							<tr>
								<td>Ratio</td>
								<td><%=r.getAcrTestsRatio()%></td>
							</tr>		
						</table>
					</td>					
				</tr>
				
				<tr>
					<td>PCR Tests</td>
					<td>
						<table>
							<tr>
								<td>Ordered</td>
								<td><%=r.getPcrTestsOrdered()%></td>
							</tr>
							<tr>
								<td>Received</td>
								<td><%=r.getPcrTestsReceived()%></td>
							</tr>
							<tr>
								<td>Ratio</td>
								<td><%=r.getPcrTestsRatio()%></td>
							</tr>		
						</table>
					</td>					
				</tr>				
				
				<tr>
					<td># of patients in clinic</td>
					<td>
						<%=r.getTotalPatients() %>
					</td>					
				</tr>								
					
					
				<tr>
					<td>Patients on ACE Inhibitors, ARB, or RAS</td>
					<td>
						<table>
							<tr>
								<td>Diabetics</td>
								<td><%=r.getDiabetesAndDrugs()%></td>
							</tr>
							<tr>
								<td>High BP</td>
								<td><%=r.getBpAndDrugs()%></td>
							</tr>
									
						</table>
					</td>					
				</tr>
					
					
				<tr>
					<td>Patients meeting BP targets</td>
					<td>
						<table>
							<tr>
								<td>Diabetics</td>
								<td><%=r.getDiabeticAndBpTarget()%></td>
							</tr>
							
						</table>
					</td>					
				</tr>
				
					
					
				<tr>
					<td>Average eGFR</td>
					<td>
						<table>
							<tr>
								<td>Diabetics</td>
								<td>
									Average:<%=r.getDiabeticScreened1yr() %><br/>
									Median:<%=r.getDiabeticScreened1yr() %><br/>
									Low:<%=r.getDiabeticScreened1yr() %><br/>
									High:<%=r.getDiabeticScreened1yr() %><br/>
								</td>
							</tr>
							<tr>
								<td>Hypertensives</td>
								<td><%=r.getHypertensiveScreened1yr() %></td>
							</tr>
							<tr>
								<td>BP>140/90</td>
								<td><%=r.getBpScreened1yr() %></td>
							</tr>
							<tr>
								<td>Family History</td>
								<td><%=r.getFamHxScreened1yr()%></td>
							</tr>
							<tr>
								<td>Aboriginals</td>
								<td><%=r.getAboriginalScreened1yr()%></td>
							</tr>
							<tr>
								<td>Patients age > 55 </td>
								<td><%=r.getAgeScreened1yr()%></td>
							</tr>
						</table>
					</td>
					
				</tr>							
			</tbody>
		</table>
		
	</body>
</html>