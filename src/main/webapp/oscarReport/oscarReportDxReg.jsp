<!DOCTYPE html>
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
<%@ page import="org.oscarehr.util.SessionConstants"%>
<%@ page import="org.oscarehr.common.model.ProviderPreference"%>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

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
        <title><bean:message key="admin.admin.DiseaseRegistry"/></title>
    
    <link rel="stylesheet" type="text/css" href="../css/jquery.autocomplete.css" />
    <script src="https://www.google.com/jsapi"></script>    
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
<script src="../js/jquery.autocomplete.js"></script>
	<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
	
	<style>
	label {display:inline; margin-right:6px;}
	.sel {width:180px}
	</style>
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
<body>
<h3><bean:message key="admin.admin.DiseaseRegistry"/></h3>
	
<div class="container-fluid">
<div class="well well-small">
	<html:form action="/report/DxresearchReport?method=addSearchCode">
		<input type="hidden" name="action" value="NA"/>
		<html:select property="quicklistname" styleClass="sel">
			<option  value="">Add From QuickList</option>
			<logic:iterate id="quickLists" name="allQuickLists" property="dxQuickListBeanVector">
				<option value="<bean:write name="quickLists" property="quickListName"/>" <bean:write name="quickLists" property="lastUsed" />>
					<bean:write name="quickLists" property="quickListName"/>
				</option>
			</logic:iterate>
		</html:select>
        OR
        <html:select property="codesystem" styleClass="sel">
        	<option  value="">Select Coding System</option>
			<logic:iterate id="codingSys" name="codingSystem" property="codingSystems">
				<option value="<bean:write name="codingSys"/>"><bean:write name="codingSys"/></option>
			</logic:iterate>
		</html:select>
        <input type="text" id="codesearch" name="codesearch" class="span4"/>
        <script>
            $("#codesearch").autocomplete("../oscarReport/oscarReportDxRegHelper.jsp");
        </script>
        <br>
        <nested:submit styleClass="btn" onclick="setAction('edit');submit();">EDIT</nested:submit>
        <nested:submit styleClass="btn">ADD</nested:submit>
	</html:form>

    <html:form action="/report/DxresearchReport?method=clearSearchCode">
		<nested:submit styleClass="btn">CLEAR</nested:submit>
    </html:form>
</div>

	Search Patients Who Registed With Below Codes:(ichppccode/icd10 not supported yet)
	<br>
    <nested:form action='<%=formAction%>'>

		<display:table name="codeSearch" id="codeSearch">
		    <display:column property="type" title="Code System" />
		    <display:column property="dxSearchCode" title="Code" />
		    <display:column property="description" title="Description" />
		</display:table>
                        
       	Search Result:<br>
                                    
        Total Number(s): <%=session.getAttribute("Counter")%><br>

        Filter: 
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


        <select id="provider_no" name="provider_no" class="sel">
            <security:oscarSec roleName="<%=roleName$%>" objectName="_team_schedule_only" rights="r" reverse="false">
                <%
                       for(Provider p : providerDao.getActiveProviders()) {
                %>
                <option value="<%=p.getProviderNo()%>" <%=mygroupno.equals(p.getProviderNo())?"selected":""%>>
                    <%=p.getFormattedName()%></option>
                    <%
                            }
                    %>
            </security:oscarSec>
            <security:oscarSec roleName="<%=roleName$%>" objectName="_team_schedule_only" rights="r" reverse="true">
                <%
                		for(MyGroup g : myGroupDao.searchmygroupno() ){
                      
                %>
                <option value="<%="_grp_"+g.getId().getMyGroupNo()%>" <%=mygroupno.equals(g.getId().getMyGroupNo())?"selected":""%>><%=g.getId().getMyGroupNo()%></option>
                <%
                        }

                        for(Provider p: providerDao.getActiveProviders()) {
                %>
                <option value="<%=p.getProviderNo()%>" <%=mygroupno.equals(p.getProviderNo())?"selected":""%>><%=p.getFormattedName()%></option>
                    <%
                            }
                    %>
            </security:oscarSec>
                <option value="*"><bean:message key="report.reportindex.formAllProviders"/></option>
        </select>


		<nested:submit styleClass="btn btn-primary" style="margin-bottom:10px;">Search Patients</nested:submit>
		
		<display:table name="listview" id="listview" class="table table-striped table-hover table-condensed">
			<display:column property="strFirstName" title="First Name"/>
			<display:column property="strLastName" title="Last Name"/>
			<display:column property="strSex" title="Sex"/>
			<display:column property="strDOB" title="DOB"/>
			<display:column property="strPhone" title="Phone"/>
			<display:column property="strHIN" title="HIN"/>
			<display:column property="strCodeSys" title="Code System"/>
			<display:column property="strCode" title="Code"/>
			<display:column property="strStartDate" title="Start Date"/>
			<display:column property="strUpdateDate" title="Update Date"/>
			<display:column property="strStatus" title="Status"/>
		</display:table>

<%
	if(request.getAttribute("listview") != null && (request.getAttribute("listview").getClass().getCanonicalName().contains("ArrayList"))) {
%>

	<input type="button" class="btn" value="Download Excel" onclick="javascript:this.form.action='<%= request.getContextPath()%>/report/DxresearchReport.do?method=patientExcelReport';this.form.submit()">
<%		
	}
%>
	</nested:form>
</div>
</body>
</html:html>
