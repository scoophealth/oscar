
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

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.oscarEncounter.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.pageUtil.*"%>
<%@ page import="java.util.Vector;"%>
<%
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", 0); //prevents caching at the proxy   
%>

<html:html locale="true">

<%
    java.util.Calendar calender = java.util.Calendar.getInstance();
    String day =  Integer.toString(calender.get(java.util.Calendar.DAY_OF_MONTH));
    String month =  Integer.toString(calender.get(java.util.Calendar.MONTH)+1);
    String year = Integer.toString(calender.get(java.util.Calendar.YEAR));
    String formattedDate = year+"/"+month+"/"+day;
    String input = null;

%>

<head>
<title>
<bean:message key="global.measurements.general"/>
</title>
<html:base/>

</head>

<script language="javascript">

function write2Parent(text){
    
    self.close();
    opener.document.encForm.enTextarea.value = opener.document.encForm.enTextarea.value + text;
 }

</script>



<link rel="stylesheet" type="text/css" href="../styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF" onload="window.focus();">
<html:errors/>
<html:form action="/oscarEncounter/Measurements" enctype="multipart/form-data">
<table>
    <tr>
        <td>
            <table>
                <tr>
                <logic:present name="messages">
                    <logic:iterate id="msg" name="messages">
                    <bean:write name="msg"/><br>
                    </logic:iterate>
                </logic:present>
                </tr>
                <tr>
                    <td>               
                        <tr>
                            <th align="left" class="td.tite" width="5">
                                <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingType"/>
                            </th>
                            <th align="left" class="td.tite">
                                <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingTypeDesc"/>
                            </th>
                            <th align="left" class="td.tite">
                                <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingMeasuringInstrc"/>
                            </th>
                            <th align="left" class="td.tite" width="5">
                                <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingValue"/>
                            </th>
                            <th align="left" class="td.tite" width="50">
                                <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingComments"/>
                            </th>
                            <th align="left" class="td.tite">
                                <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingObservationDate"/>
                            </th>
                         </tr>
                        <% int i = 0;%>
                        <logic:iterate id="measurementType" name="measurementTypes" property="measurementTypeVector" indexId = "ctr" >
                        <tr>
                            <td width="5"><bean:write name="measurementType" property="typeDisplayName" /></td>
                            <td><bean:write name="measurementType" property="typeDesc" /></td>
                            <td>                           
                                <html:select property='<%= "value(inputMInstrc-" + ctr + ")" %>'>
                                    <html:options collection='<%="mInstrcs"+ ctr%>' property="measuringInstrc" labelProperty="measuringInstrc"/>
                                </html:select>
                            </td>
                            <td><html:text property='<%= "value(inputValue-" + ctr + ")" %>' size="5" />     
                            <td><html:text property='<%= "value(comments-" + ctr + ")" %>' size="50"/>
                            <td><select name='<%= "value(month-" + ctr + ")" %>'>
                                    <% for (int j=1; j < 13; j++){
                                        String monthObserved = Integer.toString(j);
                                        if (j < 10){ 
                                            monthObserved = "0"+monthObserved;
                                        }
                                        if (Integer.parseInt(month)==j){%>
                                            <option value="<%=monthObserved%>" selected ><%=monthObserved%></option>
                                        <%;}
                                          else{%>
                                            <option value="<%=monthObserved%>" ><%=monthObserved%></option>
                                        <%;}
                                        }%>
                                </select> / 
                                <select name='<%= "value(day-" + ctr + ")" %>'>
                                    <% for (int j=1; j < 32; j++){
                                        String dayObserved = Integer.toString(j);
                                        if (j < 10){ 
                                            dayObserved = "0"+dayObserved;
                                        }
                                        if (Integer.parseInt(day)==j){%>
                                            <option value="<%=dayObserved%>" selected ><%=dayObserved%></option>
                                        <%;}
                                          else{%>
                                            <option value="<%=dayObserved%>" ><%=dayObserved%></option>
                                        <%;}
                                        }%>
                                </select> / 
                                <select name='<%= "value(year-" + ctr + ")" %>'>
                                    <% for (int j=1; j < 30; j++){
                                        String yearObserved = Integer.toString(j+2000);
                                        if (yearObserved.compareTo(year) == 0){%>
                                            <option value="<%=yearObserved%>" selected ><%=yearObserved%></option>
                                        <%;}
                                          else{%>
                                            <option value="<%=yearObserved%>" ><%=yearObserved%></option>
                                        <%;}
                                        }%>
                                </select>
                            </td>
                            <input type="hidden" name='<%= "value(inputType-" + ctr + ")" %>' value="<bean:write name="measurementType" property="type" />"/>                            
                            <input type="hidden" name='<%= "value(validation-" + ctr + ")" %>' value="<bean:write name="measurementType" property="validation" />"/></td>
                            <% i++; %>
                        </tr>
                        </logic:iterate>
                        <input type="hidden" name="value(numType)" value="<%=String.valueOf(i)%>"/>
                        <tr>
                            <td><input type="button" name="Button" value="<bean:message key="global.btnCancel"/>" onClick="window.close()"></td>
                            <td><input type="button" name="Button" value="<bean:message key="global.btnSubmit"/>" onclick="document.forms['EctMeasurementsForm'].submit();"/></td>
                        </tr>
                        
                    </td>
                </tr>
            </table>
        </td>   
    </tr>
</table>

<html:hidden property="value(dateEntered)" value="<%=formattedDate%>"/>
<input type="hidden" name="msgBetween" value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.between"/>    
<input type="hidden" name="msgBetween" value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.mustBe"/>   
<input type="hidden" name="msgBetween" value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.and"/> 
<input type="hidden" name="msgBetween" value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.numericValue"/> 
<input type="hidden" name="msgBetween" value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.inThisFormat"/> 
<input type="hidden" name="msgBetween" value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.bloodPressure"/>

</html:form>




</body>
</html:html>
                             
                                   
                                
