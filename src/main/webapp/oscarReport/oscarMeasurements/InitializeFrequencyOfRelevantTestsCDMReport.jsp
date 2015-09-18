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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.oscarReport.oscarMeasurements.pageUtil.*"%>
<%@ page import="java.util.*, java.sql.*, java.text.*, java.net.*"%>
<%

    GregorianCalendar now=new GregorianCalendar(); 
    int curYear = now.get(Calendar.YEAR);
    int curMonth = (now.get(Calendar.MONTH)+1);
    int curDay = now.get(Calendar.DAY_OF_MONTH);
%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarReport.CDMReport.msgFrequencyOfRelevantTestsBeingPerformed" />
</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<script language="javascript">
function isArray(elementInQuestion) {
    if (elementInQuestion.length) {
        return true;
    } 
    else {
        return false;
    }
}

function checkAll(field){            
    var i;    
    
    if(isArray(field)){
        for(i=0; i<field.length; i++){
            field[i].checked=true;
        }
    }
    else{
        field.checked=true;
    }
 }

function unCheckAll(field){            
    var i;    
    
    if(isArray(field)){
        for(i=0; i<field.length; i++){
            field[i].checked=false;
        }
    }
    else{
        field.checked=false;
    }
 }

    
</script>
<link rel="stylesheet" type="text/css"
	href="../../oscarEncounter/encounterStyles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF"
	onload="window.focus();">
<html:errors />
<html:form
	action="oscarReport/oscarMeasurements/InitializeFrequencyOfRelevantTestsCDMReport.do">
	<table class="MainTable" id="scrollNumber1" name="encounterTable">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn"><bean:message
				key="oscarReport.CDMReport.msgReport" /></td>
			<td class="MainTableTopRowRightColumn">
			<table class="TopStatusBar">
				<tr>
					<td><bean:message key="oscarReport.CDMReport.msgTitle" />: <bean:write
						name="CDMGroup" /></td>
					<td></td>
					<td style="text-align: right"><oscar:help keywords="report" key="app.top1"/> | <a
						href="javascript:popupStart(300,400,'About.jsp')"><bean:message
						key="global.about" /></a> | <a
						href="javascript:popupStart(300,400,'License.jsp')"><bean:message
						key="global.license" /></a></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableLeftColumn">&nbsp;</td>
			<td class="MainTableRightColumn">
			<table border=0 cellspacing=4 width=900>
				<tr>
					<td>
					<table>
						<tr>
							<td class="nameBox" colspan='4'><bean:message
								key="oscarReport.CDMReport.msgNumberOfPatientsSeen" /></td>
						</tr>
						<tr>
							<th align="left" class="subTitles" width="2"></th>
							<th align="left" class="subTitles" width="120"><bean:message
								key="oscarReport.CDMReport.msgStartDate" /></th>
							<th align="left" class="subTitles" width="120"><bean:message
								key="oscarReport.CDMReport.msgEndDate" /></th>
							<th align="left" class="subTitles" width="650"></th>
						</tr>
						<tr>
							<td width="2" class="fieldBox" bgcolor="#ddddff"><input
								type="checkbox" name="patientSeenCheckbox" value="ctr"
								checked="checked" /></td>
							<td width="120" class="fieldBox" bgcolor="#ddddff"><input
								type="text" name='startDateA'
								value='<bean:write name="lastYear"/>' size="10"> <img
								src="../img/calendar.gif" border="0"
								onClick="window.open('../../oscarReport/oscarReportCalendarPopup.jsp?type=startDateA&amp;year=<%=curYear%>&amp;month=<%=curMonth%>&amp;form=<%="RptInitializeFrequencyOfRelevantTestsCDMReportForm"%>','','width=300,height=300')" />
							</td>
							<td width="120" class="fieldBox" bgcolor="#ddddff"><input
								type="text" name='endDateA' value='<bean:write name="today"/>'
								size="10"> <img src="../img/calendar.gif" border="0"
								onClick="window.open('../../oscarReport/oscarReportCalendarPopup.jsp?type=endDateA&amp;year=<%=curYear%>&amp;month=<%=curMonth%>&amp;form=<%="RptInitializeFrequencyOfRelevantTestsCDMReportForm"%>','','width=300,height=300')" />
							</td>
							<td width="450" class="fieldBox" bgcolor="#ddddff"></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td>
					<table>
						<tr>
							<logic:present name="messages">
								<logic:iterate id="msg" name="messages">
									<bean:write name="msg" />
									<br>
								</logic:iterate>
							</logic:present>
						</tr>
						<tr>
							<td>
						<tr>
							<td class="nameBox" colspan='9'><bean:message
								key="oscarReport.CDMReport.msgFrequencyOfRelevantTestsBeingPerformed" />
							</td>
						</tr>
						<tr>
							<th align="left" class="subTitles" width="2"></th>
							<th align="left" class="subTitles" width="4"><bean:message
								key="oscarReport.CDMReport.msgTest" /></th>
							<th align="left" class="subTitles" width="200"><bean:message
								key="oscarReport.CDMReport.msgTestDescription" /></th>
							<th align="left" class="subTitles" width="200"><bean:message
								key="oscarReport.CDMReport.msgMeasuringInstruction" /></th>
							<th align="left" class="subTitles" width="80"><bean:message
								key="oscarReport.CDMReport.msgExactly" /></th>
							<th align="left" class="subTitles" width="80"><bean:message
								key="oscarReport.CDMReport.msgMoreThan" /></th>
							<th align="left" class="subTitles" width="80"><bean:message
								key="oscarReport.CDMReport.msgLessThan" /></th>
							<th align="left" class="subTitles" width="120"><bean:message
								key="oscarReport.CDMReport.msgStartDate" /></th>
							<th align="left" class="subTitles" width="120"><bean:message
								key="oscarReport.CDMReport.msgEndDate" /></th>
						</tr>
						<logic:iterate id="measurementType" name="measurementTypes"
							property="measurementTypeVector" indexId="ctr">
							<tr>
								<td width="2" class="fieldBox" bgcolor="#ddddff"><input
									type="checkbox" name="frequencyCheckbox" value="<%=ctr%>" /></td>
								<td width="4" class="fieldBox" bgcolor="#ddddff"><bean:write
									name="measurementType" property="typeDisplayName" /></td>
								<td width="200" class="fieldBox" bgcolor="#ddddff"><bean:write
									name="measurementType" property="typeDesc" /></td>
								<td width="200" class="fieldBox" bgcolor="#ddddff"></td>
								<td width="80" class="fieldBox" bgcolor="#ddddff"><input
									type="text" name="exactly" size="6" /></td>
								<td width="80" class="fieldBox" bgcolor="#ddddff"><input
									type="text" name="moreThan" size="6" /></td>
								<td width="80" class="fieldBox" bgcolor="#ddddff"><input
									type="text" name="lessThan" size="6" /></td>
								<td width="120" class="fieldBox" bgcolor="#ddddff"><input
									type="text" name="startDateD"
									value='<bean:write name="lastYear"/>' size="10"> <img
									src="../img/calendar.gif" border="0"
									onClick="window.open('../../oscarReport/oscarReportCalendarPopup.jsp?type=<%="startDateD[" + ctr + "]"%>&amp;year=<%=curYear%>&amp;month=<%=curMonth%>&amp;form=<%="RptInitializeFrequencyOfRelevantTestsCDMReportForm"%>','','width=300,height=300')" />
								</td>
								<td width="120" class="fieldBox" bgcolor="#ddddff"><input
									type="text" name="endDateD" value='<bean:write name="today"/>'
									size="10"> <img src="../img/calendar.gif" border="0"
									onClick="window.open('../../oscarReport/oscarReportCalendarPopup.jsp?type=<%="endDateD[" + ctr + "]"%>&amp;year=<%=curYear%>&amp;month=<%=curMonth%>&amp;form=<%="RptInitializeFrequencyOfRelevantTestsCDMReportForm"%>','','width=300,height=300')" />
								</td>
								<input type="hidden"
									name='<%="value(measurementTypeD"+ctr+")"%>'
									value="<bean:write name="measurementType" property="type" />" />
							</tr>
							<tr>
								<td width="2" class="fieldBox" bgcolor="#ddddff"></td>
								<td width="4" class="fieldBox" bgcolor="#ddddff" width="5"></td>
								<td width="200" class="fieldBox" bgcolor="#ddddff"></td>
								<td width="200" class="fieldBox" bgcolor="#ddddff">
								<table>
									<%int k=0;%>
									<logic:iterate id="mInstrc" name='<%="mInstrcs" + ctr%>'
										property="measuringInstrcVector" indexId="index">
										<tr>
											<td><input type="checkbox"
												name='<%="value(mInstrcsCheckboxD"+ctr+index+")"%>'
												checked="checked"
												value='<bean:write name="mInstrc" property="measuringInstrc" />' /><bean:write
												name="mInstrc" property="measuringInstrc" /></td>
										</tr>
										<%k++;%>
									</logic:iterate>
								</table>
								</td>
								<input type="hidden"
									name='<%= "value(mNbInstrcsD" + ctr+ ")" %>' value='<%=k%>' />
								<td width="80" class="fieldBox" bgcolor="#ddddff"></td>
								<td width="80" class="fieldBox" bgcolor="#ddddff"></td>
								<td width="80" class="fieldBox" bgcolor="#ddddff"></td>
								<td width="120" class="fieldBox" bgcolor="#ddddff"></td>
								<td width="120" class="fieldBox" bgcolor="#ddddff"></td>
							</tr>
						</logic:iterate>
						<tr>
						</tr>

						</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableBottomRowLeftColumn"></td>
			<td class="MainTableBottomRowRightColumn">
			<table>
				<tr>
					<td align="left"><input type="submit" name="submitBtn"
						value="<bean:message key="oscarReport.CDMReport.btnGenerateReport"/>" /></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>

</html:form>

</body>
</html:html>
