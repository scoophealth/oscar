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
<%@ page import="java.util.*,oscar.oscarReport.pageUtil.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<html:html locale="true">
<head>
<title>
<bean:message key="oscarEncounter.Measurements.msgDefineNewMeasurementGroup"/>
</title>
<style type="text/css">
   td.nameBox {
      border-bottom: 1pt solid #888888;
      font-family: tahoma, helvetica; ;
      font-size: 12pt;
   }
   td.sideLine {
      border-right: 1pt solid #888888;
   }
   td.fieldBox {
      font-family: tahoma, helvetica;
   }
   th.subTitles{
      font-family: tahoma, helvetica ;
      font-size:10pt;
   }
</style>

<script type="text/javascript">
    function set(target) {
     document.forms[0].forward.value=target;
};
</script>

</head>

<body class="BodyStyle" vlink="#0000FF" >
<!--  -->
    <html:errors/>
    <html:form action="/oscarEncounter/oscarMeasurements/DefineNewMeasurementGroup.do">
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <bean:message key="oscarEncounter.Measurements.msgMeasurements"/>
            </td>
            <td class="MainTableTopRowRightColumn" width="400">
                <table class="TopStatusBar" >                 
                    <tr>
                        <td ><bean:message key="oscarEncounter.Measurements.msgDefineNewMeasurementGroup"/></td>                        
                        <td style="text-align:right">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  ><bean:message key="global.help" /></a> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                        </td>
                    </tr>                  
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">             
            </td>
            <td class="MainTableRightColumn">
               <table border=0 cellspacing=4 width=400>
                <tr>
                    <td>
                        <table>
                            <tr>
                                <td> 
                                    <tr>
                                        <th align="left">
                                            <bean:message key="oscarEncounter.oscarMeasurements.addMeasurementGroup.createNewMeasurementGroupName"/>
                                        </th>
                                    </tr> 
                                    <tr>
                                        <td><html:text property="groupName"/></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <table>
                                                <tr>
                                                    <td><input type="button" name="Button" value="<bean:message key="global.btnClose"/>" onClick="window.close()"></td>
                                                    <td><input type="button" name="Button" value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.continueBtn"/>" onclick="submit();"/></td>
                                                </tr>
                                            </table>
                                        </td>
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
            <td class="MainTableBottomRowLeftColumn">

            </td>
            <td class="MainTableBottomRowRightColumn">

            </td>
        </tr>
    </table>
</html:form>
</body>
</html:html>
