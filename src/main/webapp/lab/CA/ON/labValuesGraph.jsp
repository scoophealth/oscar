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
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page
    import="java.util.*,oscar.oscarLab.ca.on.*,oscar.oscarDemographic.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%

            String labType = request.getParameter("labType");
            String demographicNo = request.getParameter("demographic_no");
            String testName = request.getParameter("testName");
            String identifier = request.getParameter("identifier");
            if (identifier == null) {
                identifier = "NULL";
            }

            DemographicData dData = new DemographicData();

            org.oscarehr.common.model.Demographic demographic = dData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNo);

            StringBuffer sb = new StringBuffer();
            Hashtable h = new Hashtable();
            String drugForGraph = "";
            if(request.getParameterValues("drug")!=null){
                String[] drugs = request.getParameterValues("drug");

                for(String d:drugs){
                    sb.append("&drug="+d);
                    h.put(d,"drug");
                }
               drugForGraph = sb.toString();
            }



%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="org.oscarehr.util.MiscUtils"%><html>
    <head>
        <script type="text/javascript" src="<%= request.getContextPath()%>/js/global.js"></script>
        <html:base />
        <title><%=""/*lab.pLastName*/%>, <%=""/*lab.pFirstName*/%> <bean:message
            key="oscarMDS.segmentDisplay.title" /></title>
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
                                    <table valign="top" border="0" cellpadding="2" cellspacing="0" width="100%">
                                        <tr valign="top">
                                            <td valign="top" width="33%" align="left">
                                                <table width="100%" border="0" cellpadding="2" cellspacing="0"valign="top">
                                                    <tr>
                                                        <td valign="top" align="left">
                                                            <table valign="top" border="0" cellpadding="3" cellspacing="0" width="50%">
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
                                                                                key="oscarMDS.segmentDisplay.formDateBirth" />: </strong> <%=DemographicData.getDob(demographic,"-")%>
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

                        <img src="../../../oscarEncounter/GraphMeasurements.do?method=actualLab&demographic_no=<%=demographicNo%>&labType=<%=labType%>&identifier=<%=identifier%>&testName=<%=testName%><%=drugForGraph%>"/>


                        <table width="100%" border="0" cellspacing="0" cellpadding="3"
                               class="MainTableBottomRowRightColumn" bgcolor="#003399">
                            <tr>
                                <td align="left"><input type="button"
                                                            value=" <bean:message key="global.btnClose"/> "
                                                            onClick="window.close()"> <input type="button"
                                                                     value=" <bean:message key="global.btnPrint"/> "
                                                                     onClick="window.print()">

                                </td>
                            </tr>
                        </table>
                        <form action="labValuesGraph.jsp">
                            <input type="hidden" name="labType" value="<%=labType%>" />
                            <input type="hidden" name="demographic_no" value="<%=demographicNo%>" />
                            <input type="hidden" name="testName" value="<%=testName%>" />
                            <input type="hidden" name="identifier" value="<%=identifier%>" />
                        <ul>
                        <%
                        oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
                        oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = {};
                        arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(demographicNo));

                        if (arr != null){
                        	MiscUtils.getLogger().error("ARR "+arr.length);
                        }

                        long now = System.currentTimeMillis();
                        long month = 1000L * 60L * 60L * 24L * 30L;
                        for(int idx = 0; idx < arr.length; ++idx ) {
                            oscar.oscarRx.data.RxPrescriptionData.Prescription drug = arr[idx];
                            if( drug.isArchived() ){
                                continue;
                            }

                            String styleColor = "";
                            if (drug.isCurrent() && (drug.getEndDate().getTime() - now <= month)) {
                                styleColor="style=\"color:orange;font-weight:bold;\"";
                            }else if (drug.isCurrent() )  {
                                styleColor="style=\"color:red;\"";
                            }
                            %>
                            <li><input type="checkbox"  <%=getChecked( h,drug.getRegionalIdentifier())%> name="drug" value="<%=drug.getRegionalIdentifier()%>" /> <%=drug.getFullOutLine().replaceAll(";", " ")%> </li>
                            <%
                         }
                        %>
                        </ul>
                        <input type="submit" value="Add Meds to Graph"/>
                        </form>

                    </td>
                </tr>
            </table>

    </body>
</html>

<%!
String getChecked(Hashtable h,String reg){
    if (h != null && reg != null && h.containsKey(reg)){
        return "checked";
    }
    return "";
}
%>
