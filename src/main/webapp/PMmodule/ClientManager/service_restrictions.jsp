
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>



<%-- Updated by Eugene Petruhin on 10 dec 2008 while fixing #2389527 --%>

<%@ page import="java.util.*,
				 org.oscarehr.util.SpringUtils,
				 org.oscarehr.PMmodule.model.*,
				 org.oscarehr.common.model.Provider,			 
				 org.oscarehr.PMmodule.dao.ProviderDao"
%>
<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<script>
    function resetClientFields() {
        var form = document.clientManagerForm;
        form.elements['program.name'].value='';
    }

    function search_programs() {
        var form = document.clientManagerForm;

        form.method.value='restrict_select_program';
        var programName = form.elements['program.name'].value;
        var typeEl = form.elements['program.type'];
        var programType = typeEl.options[typeEl.selectedIndex].value;
        var manOrWomanEl = form.elements['program.manOrWoman'];
        var manOrWoman = manOrWomanEl.options[manOrWomanEl.selectedIndex].value;
        var transgender = form.elements['program.transgender'].checked;
        var firstNation = form.elements['program.firstNation'].checked;
        var bedProgramAffiliated = form.elements['program.bedProgramAffiliated'].checked;
        var alcohol = form.elements['program.alcohol'].checked;
        var abstinenceSupportEl = form.elements['program.abstinenceSupport'];
        var abstinenceSupport = abstinenceSupportEl.options[abstinenceSupportEl.selectedIndex].value;
        var physicalHealth = form.elements['program.physicalHealth'].checked;
        var mentalHealth = form.elements['program.mentalHealth'].checked;
        var housing = form.elements['program.housing'].checked;

        var url = '<html:rewrite action="/PMmodule/ClientManager.do"/>';
        url += '?method=search_programs&program.name=' + programName + '&program.type=' + programType;
        url += '&program.manOrWoman='+manOrWoman+'&program.transgender='+transgender+'&program.firstNation='+firstNation+'&program.bedProgramAffiliated='+bedProgramAffiliated+'&program.alcohol='+alcohol+'&program.abstinenceSupport='+abstinenceSupport+'&program.physicalHealth='+physicalHealth+'&program.mentalHealth='+mentalHealth+'&program.housing='+housing;
			//url += '&program.manOrWoman='+manOrWoman;
        url += '&formName=clientManagerForm&formElementName=program.name&formElementId=program.id&formElementType=program.type&submit=true';

        window.open(url, "program_search", "width=800, height=600, scrollbars=1,location=1,status=1");
    }

    function do_service_restriction() {
        var form = document.clientManagerForm;
        form.method.value='service_restrict';
        form.submit();
    }
    
    function terminateEarly(restrictionId)
    {
    	if (confirm('Do you wish to terminate this service restriction?'))
    	{
	        var form = document.clientManagerForm;
	        form.method.value='terminate_early';
	        form.restrictionId.value=restrictionId;
	        form.submit();
    	}
    }
</script>
<div class="tabs" id="tabs">
    <table cellpadding="3" cellspacing="0" border="0">
        <tr>
            <th title="Service Restrictions">Service Restrictions</th>
        </tr>
    </table>
</div>

<input type="hidden" name="restrictionId" value="" />
<display:table class="simple" cellspacing="2" cellpadding="3" id="service_restriction" name="serviceRestrictions" export="false" pagesize="0" requestURI="/PMmodule/ClientManager.do">
<%
	boolean allowTerminateEarly=false;
	ProgramClientRestriction temp=null;
	ProviderDao providerDao=(ProviderDao)SpringUtils.getBean("providerDao");
%>
    <display:setProperty name="paging.banner.placement" value="bottom" />
    <display:column property="program.name" sortable="true" title="Program Name" />
    <display:column property="provider.formattedName" sortable="true" title="Restricted By"/>
    <display:column property="comments" sortable="true" title="Comments" />
    <display:column property="startDate" sortable="true" title="Start date" />
    <display:column property="endDate" sortable="true" title="End date" />
    <display:column sortable="true" title="Status">
    	<%
    		temp=(ProgramClientRestriction)service_restriction;
    		String status="";
    		allowTerminateEarly=false;
    		if (temp.getEarlyTerminationProvider()!=null)
    		{
    			Provider providerTermination=providerDao.getProvider(temp.getEarlyTerminationProvider());
    			status="terminated early by "+providerTermination.getFormattedName();
    		}
    		else if (temp.getEndDate().getTime()<System.currentTimeMillis()) status="completed";
    		else if (temp.getStartDate().getTime()<=System.currentTimeMillis() && temp.getEndDate().getTime()>=System.currentTimeMillis())
    		{
    			status="in progress";
    			allowTerminateEarly=true;
    		}
    	%>
    	<%=status%>
    </display:column>
    <display:column sortable="true" title="">
    	<input type="button" <%=allowTerminateEarly?"":"disabled=\"disabled\""%> value="Terminate Early" onclick="terminateEarly(<%=temp.getId()%>)" />
    </display:column>
</display:table>
<br/>
<br/>
<div class="tabs" id="tabs">
    <table cellpadding="3" cellspacing="0" border="0">
        <tr>
            <th title="Programs">Find a program to add a service restriction to...</th>
        </tr>
    </table>
</div>
<html:hidden property="program.id" />
<table width="100%" border="1" cellspacing="2" cellpadding="3">
    <tr class="b">
        <td width="20%">Program Name</td>
        <td><html:text property="program.name" /></td>
    </tr>
    <tr class="b">
        <td width="20%">Program Type</td>
        <td><html:select property="program.type">
            <html:option value="">&nbsp;</html:option>
            <html:option value="Bed">Bed</html:option>
            <html:option value="Service">Service</html:option>
        </html:select></td>
    </tr>

    <tr class="b">
        <td width="20%">Man or Woman:</td>
        <td>
            <html:select property="program.manOrWoman">
                <html:option value="">&nbsp;</html:option>
                <html:option value="Man">Man</html:option>
                <html:option value="Woman">Woman</html:option>
            </html:select>
        </td>
    </tr>

    <tr class="b">
        <td width="20%">Transgender:</td>
        <td><html:checkbox property="program.transgender" /></td>
    </tr>
    <tr class="b">
        <td width="20%">First Nation:</td>
        <td><html:checkbox property="program.firstNation" /></td>
    </tr>
    <tr class="b">
        <td width="20%">Bed Program Affiliated:</td>
        <td><html:checkbox property="program.bedProgramAffiliated" /></td>
    </tr>
    <tr class="b">
        <td width="20%">Alcohol:</td>
        <td><html:checkbox property="program.alcohol" /></td>
    </tr>
    <tr class="b">
        <td width="20%">Abstinence Support?</td>
        <td>
            <html:select property="program.abstinenceSupport">
                <html:option value="">&nbsp;</html:option>
                <html:option value="Harm Reduction" />
                <html:option value="Abstinence Support" />
                <html:option value="Not Applicable" />
            </html:select>
        </td>
    </tr>
    <tr class="b">
        <td width="20%">Physical Health:</td>
        <td><html:checkbox property="program.physicalHealth" /></td>
    </tr>
    <tr class="b">
        <td width="20%">Mental Health:</td>
        <td><html:checkbox property="program.mentalHealth" /></td>
    </tr>
    <tr class="b">
        <td width="20%">Housing:</td>
        <td><html:checkbox property="program.housing" /></td>
    </tr>




</table>
<table>
    <tr>
        <td align="center"><input type="button" value="search" onclick="search_programs()" /></td>
        <td align="center"><input type="button" name="reset" value="reset" onclick="javascript:resetClientFields();" /></td>
    </tr>
</table>
<br />
<c:if test="${requestScope.do_restrict != null}">
    <table class="b" border="0" width="100%">
        <tr>
            <th style="color:black">Program Name</th>
            <th style="color:black">Type</th>
            <th style="color:black">Participation</th>
            <th style="color:black">Phone</th>
            <th style="color:black">Email</th>
        </tr>
        <tr>
            <td><c:out value="${program.name }" /></td>
            <td><c:out value="${program.type }" /></td>
            <td><c:out value="${program.numOfMembers}" />/<c:out value="${program.maxAllowed}" />&nbsp;(<c:out value="${program.queueSize}" /> waiting)</td>
            <td><c:out value="${program.phone }" /></td>
            <td><c:out value="${program.email }" /></td>
        </tr>
    </table>
</c:if>
<br />
<c:if test="${requestScope.do_restrict != null}">
    <c:choose>
        <c:when test="${requestScope.can_restrict}">
            <table width="100%" border="1" cellspacing="2" cellpadding="3">
                <tr class="b">
                    <td width="20%">Reason for service restriction:</td>
                    <td>
                    	<html:select property="serviceRestriction.commentId">
						<c:forEach var="restriction" items="${serviceRestrictionList}">
							<html-el:option value="${restriction.code}"><c:out value="${restriction.description}"/></html-el:option>
						</c:forEach>
						</html:select>
                    </td>
                    
                    <!--html:textarea cols="50" rows="7" property="serviceRestriction.comments" /></td -->
                </tr>
                <tr class="b">
                    <td width="20%">Length of restriction (in days)</td>
                    <td><html:text size="4" property="serviceRestrictionLength" /></td>
                </tr>
                <tr class="b">
                    <td colspan="2"><input type="button" value="Add Service Restriction" onclick="do_service_restriction()" /> <input type="button" value="Cancel" onclick="document.clientManagerForm.submit()" /></td>
                </tr>
            </table>
        </c:when>
        <c:otherwise>
            <b style="color: red">You do not have permission to apply service restrictions to "<c:out value="${program.name}"/>."</b>
        </c:otherwise>
    </c:choose>
</c:if>
