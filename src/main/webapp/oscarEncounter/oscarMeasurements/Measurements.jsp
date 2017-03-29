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
<security:oscarSec roleName="<%=roleName$%>" objectName="_measurement" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_measurement");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ page import="oscar.oscarEncounter.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.bean.EctMeasuringInstructionBeanHandler, oscar.oscarEncounter.oscarMeasurements.bean.EctMeasuringInstructionBean"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.oscarehr.managers.MeasurementManager"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%
    String demo = (String) request.getAttribute("demographicNo"); //bean.getDemographicNo();
    
    MeasurementManager measurementManager = SpringUtils.getBean(MeasurementManager.class); 
    String groupName = (String) request.getAttribute("groupName");
%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><logic:present name="groupName">
	<bean:write name="groupName" />
</logic:present> <bean:message key="oscarEncounter.Index.measurements" /></title>

<html:base />


<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>

</head>

<script type="text/javascript">

function write2Parent(text){
    
    self.close();
    opener.document.encForm.enTextarea.value = opener.document.encForm.enTextarea.value + text;
 }

function getDropboxValue(ctr){   
    var selectedItem = document.forms[0].value(inputMInstrc-ctr).options[document.forms[0].value(inputMInstrc-ctr).selectedIndex].value;
    alert("hello!");
}

function popupPage(vheight,vwidth,page) { //open a new popup window
    
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(page, "blah", windowprops);  
}

parentChanged = false;

function check() {
	var ret = true;
    
    if( parentChanged ) {
        document.forms[0].elements["value(parentChanged)"].value = "true";
        
        if( !confirm("<bean:message key="oscarEncounter.oscarMeasurements.Measurements.msgParentChanged"/> <oscar:nameage demographicNo="<%=demo%>"/>") ) 
            ret = false;        
    }
    
    if(ret) {

      	 $.post('<%=request.getContextPath()%>/oscarEncounter/Measurements.do?ajax=true&skipCreateNote=true',$('#theForm').serialize(),function(data){
			opener.postMessage(data,"*");
      		window.close();
      	 });

    }
}
</script>
<body class="BodyStyle" vlink="#0000FF" onload="window.focus();">
<html:form action="/oscarEncounter/Measurements" styleId="theForm">
	<logic:present name="css">
		<link rel="stylesheet" type="text/css"
			href="<bean:write name="css" />">
	</logic:present>
	<logic:notPresent name="css">
		<link rel="stylesheet" type="text/css"
			href="styles/measurementStyle.css">
	</logic:notPresent>
		
	<table class="MainTable" id="scrollNumber1" name="encounterTable">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn"><logic:present
				name="groupName">
				<bean:write name="groupName" />
			</logic:present></td>
			<td class="MainTableTopRowRightColumn">
			<table class="TopStatusBar">
				<tr>
					<td width=70% class="Header" NOWRAP><oscar:nameage
						demographicNo="<%=demo%>" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableLeftColumn">
			<table>
				<tr>
					<td><a
						href="javascript: function myFunction() {return false; }"
						onClick="popupPage(150,200,'../calculators.jsp?demo=<%=demo%>'); return false;"><bean:message
						key="oscarEncounter.Index.calculators" /></a></td>
				</tr>
			</table>
			</td>
			<td class="MainTableRightColumn">
			<%=measurementManager.getDShtml(groupName)%>
			<table border=0 cellspacing=0>
				<tr>
					<td>
					<table>
						<tr>
							<td>
							<table>
								<html:errors />
								<tr>
									<td>
								<tr class="Header">
									<td align="left" width="100"><bean:message
										key="oscarEncounter.oscarMeasurements.Measurements.headingType" />
									</td>
									<td align="left" width="160"><bean:message
										key="oscarEncounter.oscarMeasurements.Measurements.headingMeasuringInstrc" />
									</td>
									<td align="left" width="50"><bean:message
										key="oscarEncounter.oscarMeasurements.Measurements.headingValue" />
									</td>
									<td align="left" width="130"><bean:message
										key="oscarEncounter.oscarMeasurements.Measurements.headingObservationDate" />
									</td>
									<td align="left" width="300"><bean:message
										key="oscarEncounter.oscarMeasurements.Measurements.headingComments" />
									</td>
									<td align="left" width="10"></td>
								</tr>
								<% int i = 0;%>
								<logic:iterate id="measurementType" name="measurementTypes"
									property="measurementTypeVector" indexId="ctr">
									<tr class="data">
										<td width="5"><a
											title="<bean:write name="measurementType" property="typeDesc" />"><bean:write
											name="measurementType" property="typeDisplayName" /></a></td>
										<td><logic:iterate id="mInstrc"
											name="<%=\"mInstrcs\"+ ctr%>"
											property="measuringInstructionList">
											<input type="radio"
												name='<%= "value(inputMInstrc-" + ctr + ")" %>'
												value="<bean:write name="mInstrc" property="measuringInstrc"/>"
												checked />
											<bean:write name="mInstrc" property="measuringInstrc" />
											<br>
										</logic:iterate></td>
										<%
										EctMeasuringInstructionBeanHandler mInstrh = (EctMeasuringInstructionBeanHandler) session.getAttribute("mInstrcs"+i);
										EctMeasuringInstructionBean mInstrBean = mInstrh.getMeasuringInstructionList().get(0);
										Integer index;
										String[] options;
										String measuringInstruction = mInstrBean.getMeasuringInstrc();
										if( measuringInstruction.startsWith("Choose radio") ) {
										   index = 12;
										   measuringInstruction = measuringInstruction.substring(index);
										   options = measuringInstruction.split(",");
										%>
										<td>
										<%
											for( int idx = 0; idx < options.length; ++idx ) {
										%>	
										<html:radio property='<%= "value(inputValue-" + ctr + ")" %>' value="<%=options[idx]%>"></html:radio><%=options[idx]%>&nbsp;										
									
										<%}%>
										</td>
										<%}else { %>
										<td><html:text
											property='<%= "value(inputValue-" + ctr + ")" %>' size="5" /></td>
										<%} %>
										<td><html:text
											property='<%= "value(date-" + ctr + ")" %>' size="20" /></td>
										<td><html:text
											property='<%= "value(comments-" + ctr + ")" %>' size="45" /></td>
										<td width="10"></td>
										<input type="hidden"
											name='<%= "value(inputType-" + ctr + ")" %>'
											value="<bean:write name="measurementType" property="type" />" />
										<input type="hidden"
											name='<%= "value(inputTypeDisplayName-" + ctr + ")" %>'
											value="<bean:write name="measurementType" property="typeDisplayName" />" />
										<input type="hidden"
											name='<%= "value(validation-" + ctr + ")" %>'
											value="<bean:write name="measurementType" property="validation" />" />
										<% i++; %>
									</tr>
									<logic:present name='measurementType' property='lastMInstrc'>
										<tr class="note">
											<td><bean:message
												key="oscarEncoutner.oscarMeasurements.msgTheLastValue" />:</td>
											<td><bean:write name='measurementType'
												property='lastMInstrc' /></td>
											<td><bean:write name='measurementType'
												property='lastData' /></td>
											<td><bean:write name='measurementType'
												property='lastDateEntered' /></td>
											<td><bean:write name='measurementType'
												property='lastComments' /></td>
											<td><img src="img/history.gif"
												title='<bean:message key="oscarEncounter.Index.oldMeasurements"/>'
												onClick="popupPage(300,800,'SetupDisplayHistory.do?type=<bean:write name="measurementType" property="type" />'); return false;" /></td>
										</tr>
									</logic:present>
								</logic:iterate>
								<input type="hidden" name="value(numType)"
									value="<%=String.valueOf(i)%>" />
								<input type="hidden" name="value(groupName)"
									value="<bean:write name="groupName"/>" />
								<input type="hidden" name="value(parentChanged)" value="false" />
								<input type="hidden" name="value(demographicNo)"
									value="<%=demo%>" />
								<input type="hidden" name="demographic_no" value="<%=demo%>" />
								<logic:present name="css">
									<input type="hidden" name="value(css)"
										value="<bean:write name="css"/>" />
								</logic:present>
								<logic:notPresent name="css">
									<input type="hidden" name="value(css)" value="" />
								</logic:notPresent>
								</td>
								</tr>
							</table>
							<table>
								<tr>
									<td><input type="button" name="Button"
										value="<bean:message key="global.btnCancel"/>"
										onClick="window.close()"></td>
									<td><input type="button" name="Button"
										value="<bean:message key="global.btnSubmit"/>"
										onclick="check();" /></td>
								</tr>
							</table>
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
			<td class="MainTableBottomRowRightColumn"></td>
		</tr>
	</table>
</html:form>
</body>
</html:html>
