<%@ page language="java" errorPage="../provider/errorpage.jsp"%>
<%@ page
	import="java.util.*,oscar.oscarLab.ca.on.*,oscar.oscarDemographic.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%



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
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title><%=""/*lab.pLastName*/%>, <%=""/*lab.pFirstName*/%> <bean:message
	key="oscarMDS.segmentDisplay.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" type="text/css"
	href="../../../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
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
<form name="acknowledgeForm" method="post"
	action="../../../oscarMDS/UpdateStatus.do">

<table width="100%" height="100%" border="0" cellspacing="0"
	cellpadding="0">
	<tr>
		<td valign="top">

		<table width="100%" border="1" cellspacing="0" cellpadding="3"
			bgcolor="#9999CC" bordercolordark="#bfcbe3">
			<tr>
				<td width="66%" align="middle" class="Cell">
				<div class="Field2"><bean:message
					key="oscarMDS.segmentDisplay.formDetailResults" /></div>
				</td>
			</tr>
			<tr>
				<td bgcolor="white" valign="top">
				<table valign="top" border="0" cellpadding="2" cellspacing="0"
					width="100%">
					<tr valign="top">
						<td valign="top" width="33%" align="left">
						<table width="100%" border="0" cellpadding="2" cellspacing="0"
							valign="top">
							<tr>
								<td valign="top" align="left">
								<table valign="top" border="0" cellpadding="3" cellspacing="0"
									width="50%">
									<tr>
										<td colspan="2" nowrap>
										<div class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formPatientName" />: </strong> <%=demographic.getLastName()%>,
										<%=demographic.getFirstName()%></div>

										</td>
										<td colspan="2" nowrap>
										<div class="FieldData" nowrap="nowrap"><strong><bean:message
											key="oscarMDS.segmentDisplay.formSex" />: </strong><%=demographic.getSex()%>
										</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" nowrap>
										<div class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formDateBirth" />: </strong> <%=demographic.getDob("-")%>
										</div>
										</td>
										<td colspan="2" nowrap>
										<div class="FieldData" nowrap="nowrap"><strong><bean:message
											key="oscarMDS.segmentDisplay.formAge" />: </strong><%=demographic.getAge()%>
										</div>
										</td>
									</tr>


								</table>
								</td>
								<td width="33%" valign="top"></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>

			</tr>

			<tr>
				<td align="center" bgcolor="white" colspan="2">
				<table width="100%" height="20" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td align="center" bgcolor="white">
						<div class="FieldData">
						<center></center>
						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>



		<table style="page-break-inside: avoid;" bgcolor="#003399" border="0"
			cellpadding="0" cellspacing="0" width="100%">
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

		<table width="100%" border="0" cellspacing="0" cellpadding="2"
			bgcolor="#CCCCFF" bordercolor="#9966FF" bordercolordark="#bfcbe3"
			name="tblDiscs" id="tblDiscs">
			<tr class="Field2">
				<td width="25%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formTestName" /></td>
				<td width="15%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formResult" /></td>
				<td width="5%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formAbn" /></td>
				<td width="15%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formReferenceRange" /></td>
				<td width="10%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formUnits" /></td>
				<td width="15%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formDateTimeCompleted" /></td>
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

			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="<%=lineClass%>">
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

		<table width="100%" border="0" cellspacing="0" cellpadding="3"
			class="MainTableBottomRowRightColumn" bgcolor="#003399">
			<tr>
				<td align="left"><input type="button"
					value=" <bean:message key="global.btnClose"/> "
					onClick="window.close()"> <input type="button"
					value=" <bean:message key="global.btnPrint"/> "
					onClick="window.print()">
                               <%-- <input type="button" value="Plot" 
                                onclick="window.open('../../../oscarEncounter/GraphMeasurements.do?method=actualLab&demographic_no=<%=demographicNo%>&labType=<%=labType%>&identifier=<%=identifier%>&testName=<%=testName%>');"/>
                                --%>        
                               <input type="button" value="Plot" 
                                onclick="window.location = 'labValuesGraph.jsp?demographic_no=<%=demographicNo%>&labType=<%=labType%>&identifier=<%=identifier%>&testName=<%=testName%>';"/>
                
                                </td>
			</tr>
		</table>
		</td>
	</tr>
</table>

</form>

</body>
</html>
