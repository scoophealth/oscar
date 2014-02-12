<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="org.oscarehr.util.SessionConstants"%>
<%@ page import="org.oscarehr.common.model.ProviderPreference"%>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
	String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>"
objectName="_admin,_admin.reporting" rights="r" reverse="<%=true%>">
    <%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<%@ page import="org.oscarehr.common.dao.DxresearchDAO"%>
<%@ page import="org.oscarehr.common.model.Dxresearch"%>
<%@ page import="oscar.oscarResearch.oscarDxResearch.util.*"%>
<%@ page import="java.util.*, java.sql.*"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.common.dao.MyGroupDao" %>
<%@ page import="org.oscarehr.common.model.MyGroup" %>

<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	MyGroupDao myGroupDao = SpringUtils.getBean(MyGroupDao.class);
%>

<html:html locale="true">
    <head>
        <script type="text/javascript" src="<%= request.getContextPath()%>/js/global.js"></script>
        <title>Dx Register Report</title>
        <link rel="stylesheet" type="text/css"
              href="../share/css/OscarStandardLayout.css">

        <script type="text/javascript" language="JavaScript"
        src="../share/javascript/prototype.js"></script>
        <script type="text/javascript" language="JavaScript"
        src="../share/javascript/Oscar.js"></script>
        <link href="<html:rewrite page='/css/displaytag.css'/>" rel="stylesheet" ></link>
        	<link rel="stylesheet" type="text/css" href="../css/jquery.autocomplete.css" />
	<script src="http://www.google.com/jsapi"></script>
	<script>
		google.load("jquery", "1");
	</script>
	<script src="../js/jquery.autocomplete.js"></script>

	<script type="text/javascript">
    	function setAction(target)
    	{
     		document.forms[0].action.value=target;
		};
	</script>

	<style>
		input {
			font-size: 100%;
		}
	</style>
	
	<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <%
			    ProviderPreference providerPreference=(ProviderPreference)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE);
                String curUser_no = (String) session.getAttribute("user");
                String mygroupno = providerPreference.getMyGroupNo();
                String radiostatus = (String) session.getAttribute("radiovaluestatus");
                if (radiostatus==null || radiostatus=="")
                    radiostatus="patientRegistedAll";
                String formAction =  "/report/DxresearchReport?method=" + radiostatus;
                request.setAttribute("listview", request.getSession().getAttribute("listview"));
                request.setAttribute("codeSearch", request.getSession().getAttribute("codeSearch"));
                //request.setAttribute("editingCode", request.getSession().getAttribute("editingCode"));
                String editingCodeType = (String) session.getAttribute("editingCodeType");
                String editingCodeCode = (String) session.getAttribute("editingCodeCode");
                String editingCodeDesc = (String) session.getAttribute("editingCodeDesc");

    %>
    <body vlink="#0000FF" class="BodyStyle">

 <h3>Patient Disease Registry Report</h3>

<br>

                    <table border="1" width="100%">
                            <tbody>
                                <html:form action="/report/DxresearchReport?method=addSearchCode">
                                <tr> <input type="hidden" name="action" value="NA" /> </tr>
                                <tr>
                                    <td>
                                        <html:select property="quicklistname">
                                            <option  value="">Add From QuickList</option>
                                            <logic:iterate id="quickLists" name="allQuickLists" property="dxQuickListBeanVector">
							<option
								value="<bean:write name="quickLists" property="quickListName" />"
								<bean:write name="quickLists" property="lastUsed" />><bean:write
								name="quickLists" property="quickListName" />
                                                        </option>
                                            </logic:iterate>
                                        </html:select>
                                    </td>
                                    <td>
                                        OR
                                        <html:select property="codesystem">
                                            <option  value="">Select Coding System</option>
						<logic:iterate id="codingSys" name="codingSystem"
							property="codingSystems">
							<option value="<bean:write name="codingSys"/>"><bean:write
								name="codingSys" /></option>
						</logic:iterate>
					</html:select>
                                    </td>

                                    <td>
                                        <input type="text" id="codesearch" name="codesearch" size="45" />

                                        <script>
                                            $("#codesearch").autocomplete("../oscarReport/oscarReportDxRegHelper.jsp");
                                        </script>
                                    </td>

                                    <td>
                                        <nested:submit onclick="setAction('edit');submit();" style="border:1px solid #666666;">EDIT</nested:submit>
                                    </td>

                                    <td>
                                        <nested:submit style="border:1px solid #666666;">ADD</nested:submit>
                                    </td>

                                </html:form>


                                <html:form action="/report/DxresearchReport?method=clearSearchCode">

                                        <td>
                                            <nested:submit style="border:1px solid #666666;">CLEAR</nested:submit>
                                        </td>
                                    </tr>
                                </html:form>
                            </tbody>
                        </table>

                    <table border="0">
                        <tbody>
                            <tr>
                                <td>Search Patients Who Registed With Below Codes:(ichppccode/icd10 not supported yet)</td>
                            </tr>
                        </tbody>
                    </table>




                    <nested:form action='<%=formAction%>'>

                        <table border="1" width="100%">
                            <tbody>
                                <tr>
                                    <td>
                                        <display:table name="codeSearch" id="codeSearch" class="simple" style="border:1px solid #666666; width:99%;margin-top:2px;">
                                            <display:column property="type" title="Code System" />
                                            <display:column property="dxSearchCode" title="Code" />
                                            <display:column property="description" title="Description" />
                                        </display:table>
                                    </td>

                                </tr>
                            </tbody>
                        </table>
                        <table border="0">
                            <thead>
                                <tr>
                                    <th>Search Result: </th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td></td>
                                </tr>
                            </tbody>
                        </table>
                        <table border="0">
                            <thead>
                                <tr>
                                    <th>Total Number(s): <%=session.getAttribute("Counter")%></th>
                                </tr>
                            </thead>
                        </table>
                        Filter: &nbsp;&nbsp;
                        <label>
                            <input type="radio" name="SearchBy" value="radio" id="SearchBy_0" <%="patientRegistedDistincted".equals(radiostatus)?"checked":""%> onclick="javascript:this.form.action='<%= request.getContextPath()%>/report/DxresearchReport.do?method=patientRegistedDistincted'">
                            ALL(distincted)</label>
                        <label>
                            <input type="radio" name="SearchBy" value="radio" id="SearchBy_1" <%="patientRegistedAll".equals(radiostatus)?"checked":""%> onclick="javascript:this.form.action='<%= request.getContextPath()%>/report/DxresearchReport.do?method=patientRegistedAll'">
                            ALL</label>
                        <label>
                            <input type="radio" name="SearchBy" value="radio" id="SearchBy_0" <%="patientRegistedActive".equals(radiostatus)?"checked":""%> onclick="javascript:this.form.action='<%= request.getContextPath()%>/report/DxresearchReport.do?method=patientRegistedActive'">
                            Active</label>
                        <label>
                            <input type="radio" name="SearchBy" value="radio" id="SearchBy_0" <%="patientRegistedDeleted".equals(radiostatus)?"checked":""%> onclick="javascript:this.form.action='<%= request.getContextPath()%>/report/DxresearchReport.do?method=patientRegistedDeleted'">
                            Deleted</label>
                        <label>
                            <input type="radio" name="SearchBy" value="radio" id="SearchBy_1" <%="patientRegistedResolve".equals(radiostatus)?"checked":""%> onclick="javascript:this.form.action='<%= request.getContextPath()%>/report/DxresearchReport.do?method=patientRegistedResolve'">
                            Resolved</label>


                        <select id="provider_no" name="provider_no">

                            <security:oscarSec roleName="<%=roleName$%>"
                                               objectName="_team_schedule_only" rights="r" reverse="false">
                                <%
                                       for(Provider p : providerDao.getActiveProviders()) {
                                %>
                                <option value="<%=p.getProviderNo()%>" <%=mygroupno.equals(p.getProviderNo())?"selected":""%>>
                                    <%=p.getFormattedName()%></option>
                                    <%
                                            }
                                    %>

                            </security:oscarSec>
                            <security:oscarSec roleName="<%=roleName$%>"
                                               objectName="_team_schedule_only" rights="r" reverse="true">
                                <%
                                		for(MyGroup g : myGroupDao.searchmygroupno() ){
                                      
                                %>
                                <option value="<%="_grp_"+g.getId().getMyGroupNo()%>"
                                        <%=mygroupno.equals(g.getId().getMyGroupNo())?"selected":""%>><%=g.getId().getMyGroupNo()%></option>
                                <%
                                        }

                                        for(Provider p: providerDao.getActiveProviders()) {
                                %>
                                <option value="<%=p.getProviderNo()%>" <%=mygroupno.equals(p.getProviderNo())?"selected":""%>>
                                    <%=p.getFormattedName()%></option>
                                    <%
                                            }
                                    %>
                                </security:oscarSec>
                                <option value="*"><bean:message
                                        key="report.reportindex.formAllProviders" />
                                </option>
                        </select>

                        &nbsp;&nbsp;
                        <nested:submit style="border:1px solid #666666;">Search Patients</nested:submit>

                        <display:table name="listview" id="listview" class="mars" style="border:1px solid #666666; width:99%;margin-top:2px;">
                            <display:column property="strFirstName" title="First Name" />
                            <display:column property="strLastName" title="Last Name" />
                            <display:column property="strSex" title="Sex" />
                            <display:column property="strDOB" title="DOB" />
                            <display:column property="strPhone" title="Phone" />
                            <display:column property="strHIN" title="HIN" />
                            <display:column property="strCodeSys" title="Code System" />
                            <display:column property="strCode" title="Code" />
                            <display:column property="strStartDate" title="Start Date" />
                            <display:column property="strUpdateDate" title="Update Date" />
                            <display:column property="strStatus" title="Status" />
                        </display:table>

			<%
	if(request.getAttribute("listview") != null && (request.getAttribute("listview").getClass().getCanonicalName().contains("ArrayList"))) {
%>
	<br/>
	<input type="button" value=" Excel " style="border:1px solid #666666;" onclick="javascript:this.form.action='<%= request.getContextPath()%>/report/DxresearchReport.do?method=patientExcelReport';this.form.submit()">
<%		
	}
%>
                    </nested:form>

   

    </html:html>
