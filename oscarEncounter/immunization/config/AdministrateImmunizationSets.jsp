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

<%
oscar.oscarEncounter.immunization.config.data.EctImmImmunizationSetData immuSets = new oscar.oscarEncounter.immunization.config.data.EctImmImmunizationSetData();
immuSets.estImmunizationVecs();

%>

<html:html locale="true">


<head>
<title>
Administrate Immunization Sets
</title>
<html:base/>

<style type="text/css">
td.tite {

background-color: #bbbbFF;
color : black;
font-size: 12pt;

}

td.tite1 {

background-color: #ccccFF;
color : black;
font-size: 12pt;

}

th,td.tite2 {

background-color: #BFBFFF;
color : black;
font-size: 12pt;

}

td.tite3 {

background-color: #B8B8FF;
color : black;
font-size: 12pt;

}

td.tite4 {

background-color: #ddddff;
color : black;
font-size: 12pt;

}


</style>
</head>
<script language="javascript">
function BackToOscar(){
       window.close();
}

function popupImmunizationSet(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "immSet", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }

}
</script>
<link rel="stylesheet" type="text/css" href="../../encounterStyles.css">




<body class="BodyStyle" vlink="#0000FF" onload="window.focus();" >
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                immunizations
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >

                        </td>
                        <td  >

                        </td>
                        <td style="text-align:right">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  >Help</a> | <a href="javascript:popupStart(300,400,'About.jsp')" >About</a> | <a href="javascript:popupStart(300,400,'License.jsp')" >License</a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">
            </td>
            <td class="MainTableRightColumn">
                 <table border=0 cellspacing=1>
                    <tr>
                            <th>
                            Immunization Set Name
                            </th>
                            <th>
                            Date Created
                            </th>
                    </tr>
                    <%for ( int i = 0; i < immuSets.setNameVec.size();i++){
                        String name = (String) immuSets.setNameVec.elementAt(i);
                        String id = (String) immuSets.setIdVec.elementAt(i);
                        String createDate = (String) immuSets.createDateVec.elementAt(i);
                    %>
                        <tr>
                            <td class="tite4">

                            <a href="javascript:popupImmunizationSet(768,1024,'ImmunizationSetDisplay.do?setId=<%=id%>')">
                            <%=name%>
                            </a>
                            </td>
                            <td class="tite4">
                            <%=createDate%>
                            </td>
                        </tr>
                    <%}%>
                </table>
                <br/>
                <a href="CreateImmunizationSetInit.jsp">Add New</a>
                <a href="javascript:window.close()">Close</a>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">
            </td>
            <td class="MainTableBottomRowRightColumn">
            </td>
        </tr>
    </table>
</body>
</html:html>

