<%@ page language="java" errorPage="../provider/errorpage.jsp" %>
<%@ page import="java.util.*,oscar.oscarLab.ca.on.*,oscar.oscarDemographic.data.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%
if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");


System.out.println(" test name "+request.getParameter("testName"));
System.out.println(" demo "+request.getParameter("demo"));
System.out.println(" labType  "+request.getParameter("labType"));

String labType = request.getParameter("labType");
String demographicNo = request.getParameter("demo");
String testName = request.getParameter("testName");
String identifier = request.getParameter("identifier");
if (identifier == null) identifier = "NULL";

String highlight = "#E0E0FF";

DemographicData dData = new DemographicData();

DemographicData.Demographic demographic =  dData.getDemographic(demographicNo);

CommonLabTestValues comVal =  new CommonLabTestValues();
ArrayList list = null;

if (!demographicNo.equals("null")){
    System.out.println("sdfsdf");
 list = comVal.findValuesForTest(labType, demographicNo, testName, identifier);
 
}

System.out.println("idididid "+list);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
<html>
<head>
<html:base/>
<title><%=""/*lab.pLastName*/%>, <%=""/*lab.pFirstName*/%> <bean:message key="oscarMDS.segmentDisplay.title"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" type="text/css" href="../../../share/css/OscarStandardLayout.css">
<style type="text/css">
<!--
.RollRes     { font-weight: 700; font-size: 8pt; color: white; font-family: 
               Verdana, Arial, Helvetica }
.RollRes a:link { color: white }
.RollRes a:hover { color: white }
.RollRes a:visited { color: white }
.RollRes a:active { color: white }
.AbnormalRollRes { font-weight: 700; font-size: 8pt; color: red; font-family: 
               Verdana, Arial, Helvetica }
.AbnormalRollRes a:link { color: red }
.AbnormalRollRes a:hover { color: red }
.AbnormalRollRes a:visited { color: red }
.AbnormalRollRes a:active { color: red }
.CorrectedRollRes { font-weight: 700; font-size: 8pt; color: yellow; font-family: 
               Verdana, Arial, Helvetica }
.CorrectedRollRes a:link { color: yellow }
.CorrectedRollRes a:hover { color: yellow }
.CorrectedRollRes a:visited { color: yellow }
.CorrectedRollRes a:active { color: yellow }
.AbnormalRes { font-weight: bold; font-size: 8pt; color: red; font-family: 
               Verdana, Arial, Helvetica }
.AbnormalRes a:link { color: red }
.AbnormalRes a:hover { color: red }
.AbnormalRes a:visited { color: red }
.AbnormalRes a:active { color: red }
.NormalRes   { font-weight: bold; font-size: 8pt; color: black; font-family: 
               Verdana, Arial, Helvetica }
.NormalRes a:link { color: black }
.NormalRes a:hover { color: black }
.NormalRes a:visited { color: black }
.NormalRes a:active { color: black }
.HiLoRes     { font-weight: bold; font-size: 8pt; color: blue; font-family: 
               Verdana, Arial, Helvetica }
.HiLoRes a:link { color: blue }
.HiLoRes a:hover { color: blue }
.HiLoRes a:visited { color: blue }
.HiLoRes a:active { color: blue }
.CorrectedRes { font-weight: bold; font-size: 8pt; color: #E000D0; font-family: 
               Verdana, Arial, Helvetica }
.CorrectedRes a:link { color: #6da997 }
.CorrectedRes a:hover { color: #6da997 }
.CorrectedRes a:visited { color: #6da997 }
.CorrectedRes a:active { color: #6da997 }
.Field       { font-weight: bold; font-size: 8.5pt; color: black; font-family: 
               Verdana, Arial, Helvetica }
div.Field a:link { color: black }
div.Field a:hover { color: black }
div.Field a:visited { color: black }
div.Field a:active { color: black }
.Field2      { font-weight: bold; font-size: 8pt; color: #ffffff; font-family: 
               Verdana, Arial, Helvetica }
div.Field2   { font-weight: bold; font-size: 8pt; color: #ffffff; font-family: 
               Verdana, Arial, Helvetica }
div.FieldData { font-weight: normal; font-size: 8pt; color: black; font-family: 
               Verdana, Arial, Helvetica }
div.Field3   { font-weight: normal; font-size: 8pt; color: black; font-style: italic; 
               font-family: Verdana, Arial, Helvetica }
div.Title    { font-weight: 800; font-size: 10pt; color: white; font-family: 
               Verdana, Arial, Helvetica; padding-top: 4pt; padding-bottom: 
               2pt }
div.Title a:link { color: white }
div.Title a:hover { color: white }
div.Title a:visited { color: white }
div.Title a:active { color: white }
div.Title2   { font-weight: bolder; font-size: 9pt; color: black; text-indent: 5pt; 
               font-family: Verdana, Arial, Helvetica; padding-bottom: 2pt }
div.Title2 a:link { color: black }
div.Title2 a:hover { color: black }
div.Title2 a:visited { color: black }
div.Title2 a:active { color: black }
.Cell        { background-color: #9999CC; border-left: thin solid #CCCCFF; 
               border-right: thin solid #6666CC; 
               border-top: thin solid #CCCCFF; 
               border-bottom: thin solid #6666CC }
.Cell2       { background-color: #376c95; border-left-style: none; border-left-width: medium; 
               border-right-style: none; border-right-width: medium; 
               border-top: thin none #bfcbe3; border-bottom-style: none; 
               border-bottom-width: medium }
.Cell3       { background-color: #add9c7; border-left: thin solid #dbfdeb; 
               border-right: thin solid #5d9987; 
               border-top: thin solid #dbfdeb; 
               border-bottom: thin solid #5d9987 }
.CellHdr     { background-color: #cbe5d7; border-right-style: none; border-right-width: 
               medium; border-bottom-style: none; border-bottom-width: medium }
.Nav         { font-weight: bold; font-size: 8pt; color: black; font-family: 
               Verdana, Arial, Helvetica }
.PageLink a:link { font-size: 8pt; color: white }
.PageLink a:hover { color: red }
.PageLink a:visited { font-size: 9pt; color: yellow }
.PageLink a:active { font-size: 12pt; color: yellow }
.PageLink    { font-family: Verdana }
.text1       { font-size: 8pt; color: black; font-family: Verdana, Arial, Helvetica }
div.txt1     { font-size: 8pt; color: black; font-family: Verdana, Arial }
div.txt2     { font-weight: bolder; font-size: 6pt; color: black; font-family: Verdana, Arial }
div.Title3   { font-weight: bolder; font-size: 12pt; color: black; font-family: 
               Verdana, Arial }
.red         { color: red }
.text2       { font-size: 7pt; color: black; font-family: Verdana, Arial }
.white       { color: white }
.title1      { font-size: 9pt; color: black; font-family: Verdana, Arial }
div.Title4   { font-weight: 600; font-size: 8pt; color: white; font-family: 
               Verdana, Arial, Helvetica }
-->
</style>
</head>

<script language="JavaScript">
function getComment() {    
    var commentVal = prompt('<bean:message key="oscarMDS.segmentDisplay.msgComment"/>', '');
    document.acknowledgeForm.comment.value = commentVal;    
    return true;
}

function popupStart(vheight,vwidth,varpage,windowname) {
    var page = varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
    var popup=window.open(varpage, windowname, windowprops);
}
</script>

<body>
<form name="acknowledgeForm" method="post" action="../../../oscarMDS/UpdateStatus.do">

<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td valign="top">
            
            <table width="100%" border="1" cellspacing="0" cellpadding="3" bgcolor="#9999CC" bordercolordark="#bfcbe3">
                <tr>
                    <td width="66%" align="middle" class="Cell">
                        <div class="Field2">
                             <bean:message key="oscarMDS.segmentDisplay.formDetailResults"/> 
                        </div>
                    </td>                    
                </tr>
                <tr>
                    <td bgcolor="white" valign="top">
                        <table valign="top" border="0" cellpadding="2" cellspacing="0" width="100%">
                            <tr valign="top">
                                <td valign="top" width="33%" align="left">
                                    <table width="100%" border="0" cellpadding="2" cellspacing="0" valign="top">
                                        <tr>
                                            <td valign="top" align="left">
                                                <table valign="top" border="0" cellpadding="3" cellspacing="0" width="50%">
                                                    <tr>
                                                        <td colspan="2" nowrap>
                                                            <div class="FieldData">
                                                                <strong><bean:message key="oscarMDS.segmentDisplay.formPatientName"/>: </strong>
                                                                <%=demographic.getLastName()%>, <%=demographic.getFirstName()%>                                                                     
                                                            </div>
                                                            
                                                        </td>
                                                        <td colspan="2" nowrap>
                                                            <div class="FieldData" nowrap="nowrap">                                                                                                                                 
                                                                <strong><bean:message key="oscarMDS.segmentDisplay.formSex"/>: </strong><%=demographic.getSex()%>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="2" nowrap>
                                                            <div class="FieldData">
                                                                <strong><bean:message key="oscarMDS.segmentDisplay.formDateBirth"/>: </strong>
                                                                <%=demographic.getDob("-")%>
                                                            </div>
                                                        </td>
                                                        <td colspan="2" nowrap>
                                                            <div class="FieldData" nowrap="nowrap">
                                                                <strong><bean:message key="oscarMDS.segmentDisplay.formAge"/>: </strong><%=demographic.getAge()%>                                                                    
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    
                                                    
                                                </table>
                                            </td>
                                            <td width="33%" valign="top">
                                                
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                    
                </tr>
                
                <tr>
                    <td align="center" bgcolor="white" colspan="2">
                        <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td align="center" bgcolor="white">
                                    <div class="FieldData">
                                        <center>
                                  
                                        </center>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
      
        
           
                         <table style="page-break-inside:avoid;" bgcolor="#003399" border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tr>
                                <td colspan="4" height="7">&nbsp;</td>
                            </tr>
                            <tr>
                                <td colspan="4" height="7">&nbsp;</td>
                            </tr>
                            <!--tr>
                                <td bgcolor="#FFCC00" width="200" height="22" valign="bottom">
                                    <div class="Title2">
                                        <%=""/*gResults.groupName*/%>
                                        
                                    </div>
                                </td>
                                <td align="right" bgcolor="#FFCC00" width="100">&nbsp;</td>
                                <td width="9">&nbsp;</td>
                                <td width="*">&nbsp;</td>
                            </tr-->
                        </table>
                        
                        <table width="100%" border="0" cellspacing="0" cellpadding="2" bgcolor="#CCCCFF" bordercolor="#9966FF" bordercolordark="#bfcbe3" name="tblDiscs" id="tblDiscs">
                            <tr class="Field2">
                                <td width="25%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formTestName"/></td>
                                <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formResult"/></td>
                                <td width="5%"  align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formAbn"/></td>
                                <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formReferenceRange"/></td>
                                <td width="10%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formUnits"/></td>
                                <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formDateTimeCompleted"/></td>                                
                            </tr>                                                                                  
                        <%  int linenum = 0;
                            if (list != null){ 
                                System.out.println("list . size "+list.size());
                               for (int i = 0 ;  i < list.size(); i++){
                                   Hashtable h = (Hashtable) list.get(i);
                                   String lineClass = "NormalRes";
                                   if ( h.get("abn") != null && h.get("abn").equals("A")){
                                      lineClass = "AbnormalRes";
                                   }
%>

                            <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
                                <td valign="top" align="left"><%=h.get("testName") %></td>                                         
                                <td align="right"><%=h.get("result") %></td>
                                <td align="center"><%=h.get("abn") %></td>
                                <td align="left"><%=h.get("range")%></td>
                                <td align="left"><%=h.get("units") %></td>                                
                                <td align="center"><%=h.get("collDate")%></td>                                
                            </tr>     
                        
                        <%     }
                            } %>
           
                       </table>
                                           
            <table width="100%" border="0" cellspacing="0" cellpadding="3" class="MainTableBottomRowRightColumn" bgcolor="#003399">
                <tr>
                    <td align="left" >
                    
                        <input type="button" value=" <bean:message key="global.btnClose"/> " onClick="window.close()">
                        <input type="button" value=" <bean:message key="global.btnPrint"/> " onClick="window.print()">
                    
                    </td>                    
                </tr>
            </table>
        </td>
    </tr>
</table>

</form>

</body>
</html>
