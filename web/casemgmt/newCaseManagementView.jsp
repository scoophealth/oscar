<%--
/*
*
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* This software was written for
* Centre for Research on Inner City Health, St. Michael's Hospital,
* Toronto, Ontario, Canada
*/
--%>

<%@include file="/casemgmt/taglibs.jsp"%>
<%@taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@page import="java.util.Enumeration"%>
<%@page import="oscar.oscarEncounter.pageUtil.NavBarDisplayDAO"%>
<%@page	import="java.util.Arrays,java.util.Properties,java.util.List,java.util.Set,java.util.ArrayList,java.util.Enumeration,java.util.HashSet,java.util.Iterator,java.text.SimpleDateFormat,java.util.Calendar,java.util.Date,java.text.ParseException"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.common.model.UserProperty,org.oscarehr.casemgmt.model.*,org.oscarehr.casemgmt.service.* "%>
<%@page import="org.oscarehr.casemgmt.web.formbeans.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="oscar.util.DateUtils"%>
<%@page import="oscar.dms.EDocUtil"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.caisi.model.Role"%>
<%@page import="org.oscarehr.casemgmt.common.Colour"%>
<%@page import="oscar.dms.EDoc"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.quatro.dao.security.*,com.quatro.model.security.Secrole"%>
<%@page import="org.oscarehr.util.EncounterUtil"%>
<%@page import="org.apache.cxf.common.i18n.UncheckedException"%>
<%@page import="org.oscarehr.casemgmt.web.NoteDisplay"%>
<%@page import="org.oscarehr.casemgmt.web.CaseManagementViewAction"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="oscar.oscarRx.data.RxPrescriptionData"%>
<%@page import="org.oscarehr.casemgmt.dao.CaseManagementNoteLinkDAO"%>
<%@page import="org.oscarehr.common.dao.ProfessionalSpecialistDao"%>

<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="oscar.util.UtilDateUtilities"%>
<%@page import="org.oscarehr.casemgmt.web.NoteDisplayNonNote"%>
<%@page import="org.oscarehr.common.dao.EncounterTemplateDao"%><jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />

<%
try
{
	ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao)SpringUtils.getBean("professionalSpecialistDao");
    CaseManagementManager caseManagementManager=(CaseManagementManager)SpringUtils.getBean("caseManagementManager");
      
	String demographicNo = request.getParameter("demographicNo");
	oscar.oscarEncounter.pageUtil.EctSessionBean bean = null;
	String strBeanName = "casemgmt_oscar_bean" + demographicNo;
	if ((bean = (oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute(strBeanName)) == null)
	{
		response.sendRedirect("error.jsp");
		return;
	}

	String provNo = bean.providerNo;

	String pId = (String)session.getAttribute("case_program_id");
	if (pId == null) pId = "";

	String dateFormat = "dd-MMM-yyyy H:mm";
	long savedId = 0;
	boolean found = false;
	String bgColour = "color:#000000;background-color:#CCCCFF;";
	ArrayList<Integer> lockedNotes = new ArrayList<Integer>();
	ArrayList<Integer> unLockedNotes = new ArrayList<Integer>();
	ArrayList<Integer> unEditableNotes = new ArrayList<Integer>();

	@SuppressWarnings("unchecked")
	ArrayList<NoteDisplay> notesToDisplay = (ArrayList<NoteDisplay>)request.getAttribute("notesToDisplay");
	int noteSize = notesToDisplay.size();

	SimpleDateFormat jsfmt = new SimpleDateFormat("MMM dd, yyyy");
	Date dToday = new Date();
	String strToday = jsfmt.format(dToday);

	String frmName = "caseManagementEntryForm" + demographicNo;
	CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean)session.getAttribute(frmName);

	if (request.getParameter("caseManagementEntryForm") == null)
	{
		request.setAttribute("caseManagementEntryForm", cform);
	}
%>
<script type="text/javascript">
    numNotes = <%=noteSize%>; //How many saved notes do we have?
    ctx = "<c:out value="${ctx}"/>";
    imgPrintgreen.src = ctx + "/oscarEncounter/graphics/printerGreen.png"; //preload green print image so firefox will update properly
    providerNo = "<%=provNo%>";
    demographicNo = "<%=demographicNo%>";
    case_program_id = "<%=pId%>";

    <caisi:isModuleLoad moduleName="caisi">
        caisiEnabled = true;
    </caisi:isModuleLoad>
    <c:if test="${sessionScope.passwordEnabled=='true'}">

        passwordEnabled = true;
    </c:if>

    var savedNoteId=0;
</script>

	<div id="cppBoxes">
		<div id="divR1" style="width: 100%; height: 75px; margin: 0px; background-color: #FFFFFF;">		
			<div id="divR1I1" class="topBox"
				style="float: left; width: 49%; margin-left: 3px;">
			</div>
		
			<!-- This is the Medical History cell ...mh...-->
			<div id="divR1I2" class="topBox"
				style="float: right; width: 49%; margin-right: 3px;">
			</div>
		</div>
	
		<div id="divR2" style="width: 100%; height: 75px; margin-top: 0px; background-color: #FFFFFF;">		
			<!--Ongoing Concerns cell -->
			<div id="divR2I1" class="topBox"
				style="clear: left; float: left; width: 49%; margin-left: 3px;">	
			</div>	
			<!--Reminders cell -->
			<div id="divR2I2" class="topBox"
				style="clear: right; float: right; width: 49%; margin-right: 3px;">
		
			</div>
		</div>
	</div>
	
	
	<div id="notCPP"
		style="height: 70%; margin-left: 2px; background-color: #FFFFFF;">
  <html:form action="/CaseManagementView" method="post">
	<html:hidden property="demographicNo" value="<%=demographicNo%>" />
	<html:hidden property="providerNo" value="<%=provNo%>" />
	<html:hidden property="tab" value="Current Issues" />
	<html:hidden property="hideActiveIssue" />
	<html:hidden property="ectWin.rowOneSize" styleId="rowOneSize" />
	<html:hidden property="ectWin.rowTwoSize" styleId="rowTwoSize" />
	<input type="hidden" name="chain" value="list" >
	<input type="hidden" name="method" value="view" >
	<input type="hidden" id="check_issue" name="check_issue">
	<input type="hidden" id="serverDate" value="<%=strToday%>">
	<input type="hidden" id="resetFilter" name="resetFilter" value="false">
	<div id="topContent" style="float: left; width: 100%; margin-right: -2px; padding-bottom: 10px; background-color: #CCCCFF; font-size: 10px;">
		<nested:notEmpty name="caseManagementViewForm" property="filter_providers">
			<div style="float: left; margin-left: 10px; margin-top: 0px;"><u><bean:message key="oscarEncounter.providers.title" />:</u><br>
				<nested:iterate type="String" id="filter_provider" property="filter_providers">
					<c:choose>
						<c:when test="${filter_provider == 'a'}">All</c:when>
						<c:otherwise>
							<nested:iterate id="provider" name="providers">
								<c:if test="${filter_provider==provider.providerNo}">
									<nested:write name="provider" property="formattedName" />
									<br>
								</c:if>
							</nested:iterate>
						</c:otherwise>
					</c:choose>
				</nested:iterate>
			</div>
		</nested:notEmpty>
		
		<nested:notEmpty name="caseManagementViewForm" property="filter_roles">
		<div style="float: left; margin-left: 10px; margin-top: 0px;"><u><bean:message key="oscarEncounter.roles.title" />:</u><br>
			<nested:iterate type="String" id="filter_role" property="filter_roles">
				<c:choose>
					<c:when test="${filter_role == 'a'}">All</c:when>
					<c:otherwise>
						<nested:iterate id="role" name="roles">
							<c:if test="${filter_role==role.id}">
								<nested:write name="role" property="name" />
								<br>
							</c:if>
						</nested:iterate>
					</c:otherwise>
				</c:choose>
			</nested:iterate>
		</div>

		</nested:notEmpty> <nested:notEmpty name="caseManagementViewForm" property="note_sort">
			<div style="float: left; margin-left: 10px; margin-top: 0px;"><u><bean:message key="oscarEncounter.sort.title" />:</u><br>
			<nested:write property="note_sort" /><br>
			</div>
		</nested:notEmpty>

		<div id="filter" style="display:none;background-color:#ddddff;padding:8px">
			<input type="button" value="<bean:message key="oscarEncounter.showView.title" />" onclick="return filter(false);" />
			<input type="button" value="<bean:message key="oscarEncounter.resetFilter.title" />" onclick="return filter(true);" />

			<table style="border-collapse:collapse;width:100%;margin-left:auto;margin-right:auto">
				<tr>
					<td style="font-size:inherit;background-color:#bbbbff;font-weight:bold">
						<bean:message key="oscarEncounter.providers.title" />
					</td>
					<td style="font-size:inherit;background-color:#bbbbff;border-left:solid #ddddff 4px;border-right:solid #ddddff 4px;font-weight:bold">
						Role
					</td>
					<td style="font-size:inherit;background-color:#bbbbff;font-weight:bold">
						<bean:message key="oscarEncounter.sort.title" />
					</td>
				</tr>
				<tr>
					<td style="font-size:inherit;background-color:#ccccff">
						<div style="height:150px;overflow:auto">
							<ul style="padding:0px;margin:0px;list-style:none inside none">
								<li><html:multibox property="filter_providers" value="a" onclick="filterCheckBox(this)"></html:multibox><bean:message key="oscarEncounter.sortAll.title" /></li>
								<%
									@SuppressWarnings("unchecked")
										Set<Provider> providers = (Set<Provider>)request.getAttribute("providers");
						
										String providerNo;
										Provider prov;
										Iterator<Provider> iter = providers.iterator();
										while (iter.hasNext())
										{
											prov = iter.next();
											providerNo = prov.getProviderNo();
								%>
								<li><html:multibox property="filter_providers" value="<%=providerNo%>" onclick="filterCheckBox(this)"></html:multibox><%=prov.getFormattedName()%></li>
								<%
									}
								%>
							</ul>
						</div>
					</td>
					<td style="font-size:inherit;background-color:#ccccff;border-left:solid #ddddff 4px;border-right:solid #ddddff 4px">
						<div style="height:150px;overflow:auto">
							<ul style="padding:0px;margin:0px;list-style:none inside none">
								<li><html:multibox property="filter_roles" value="a" onclick="filterCheckBox(this)"></html:multibox><bean:message key="oscarEncounter.sortAll.title" /></li>
								<%
									@SuppressWarnings("unchecked")
										List roles = (List)request.getAttribute("roles");
										for (int num = 0; num < roles.size(); ++num)
										{
											Secrole role = (Secrole)roles.get(num);
								%>
								<li><html:multibox property="filter_roles" value="<%=String.valueOf(role.getId())%>" onclick="filterCheckBox(this)"></html:multibox><%=role.getName()%></li>
								<%
									}
								%>
							</ul>
						</div>
					</td>
					<td style="font-size:inherit;background-color:#ccccff">
						<div style="height:150px;overflow:auto">
							<ul style="padding:0px;margin:0px;list-style:none inside none">
								<li><html:radio property="note_sort" value="observation_date_asc">
									<bean:message key="oscarEncounter.sortDateAsc.title" />
								</html:radio></li>
								<li><html:radio property="note_sort" value="observation_date_desc">
									<bean:message key="oscarEncounter.sortDateDesc.title" />
								</html:radio></li>
								<li><html:radio property="note_sort" value="providerName">
									<bean:message key="oscarEncounter.provider.title" />
								</html:radio></li>
								<li><html:radio property="note_sort" value="programName">
									<bean:message key="oscarEncounter.program.title" />
								</html:radio></li>
								<li><html:radio property="note_sort" value="roleName">
									<bean:message key="oscarEncounter.role.title" />
								</html:radio></li>
							</ul>
						</div>
					</td>
				</tr>
			</table>
		</div>
	
		<div style="float: left; clear: both; margin-top: 5px; margin-bottom: 5px; width: 100%; text-align: center;">
			<div style="display:inline-block">
				<img alt="<bean:message key="oscarEncounter.msgFind"/>" src="<c:out value="${ctx}/oscarEncounter/graphics/edit-find.png"/>"> 
				<input id="enTemplate" tabindex="6" size="16" type="text" value="" onkeypress="return grabEnterGetTemplate(event)">
				
				<div class="enTemplate_name_auto_complete" id="enTemplate_list" style="z-index: 1; display: none">&nbsp;</div>
				
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
				
				<select id="channel">
					<option value="http://resource.oscarmcmaster.org/oscarResource/OSCAR_search/OSCAR_search_results?title="><bean:message key="oscarEncounter.Index.oscarSearch" /></option>
					<option value="http://www.google.com/search?q="><bean:message key="global.google" /></option>
					<option value="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?SUBMIT=y&amp;CDM=Search&amp;DB=PubMed&amp;term="><bean:message key="global.pubmed" /></option>
					<option value="http://search.nlm.nih.gov/medlineplus/query?DISAMBIGUATION=true&amp;FUNCTION=search&amp;SERVER2=server2&amp;SERVER1=server1&amp;PARAMETER="><bean:message key="global.medlineplus" /></option>
                    <option value="tripsearch.jsp?searchterm=">Trip Database</option>
    	        </select>
				
				<input type="text" id="keyword" name="keyword" value="" onkeypress="return grabEnter('searchButton',event)"> 
				<input type="button" id="searchButton" name="button" value="<bean:message key="oscarEncounter.Index.btnSearch"/>" onClick="popupPage(600,800,'<bean:message key="oscarEncounter.Index.popupSearchPageWindow"/>',$('channel').options[$('channel').selectedIndex].value+urlencode($F('keyword')) ); return false;">
			</div>
			&nbsp;&nbsp;
			<div style="display:inline-block">
				<input type="button" value="<bean:message key="oscarEncounter.Filter.title"/>" onclick="showFilter();" />
				<%
					String roleName = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
					String pAge = Integer.toString(UtilDateUtilities.calcAge(bean.yearOfBirth,bean.monthOfBirth,bean.dateOfBirth));
				%>
				<security:oscarSec roleName="<%=roleName%>" objectName="_newCasemgmt.calculators" rights="r" reverse="false">
					<%@include file="calculatorsSelectList.jspf" %>
				</security:oscarSec>
				<security:oscarSec roleName="<%=roleName%>" objectName="_newCasemgmt.templates" rights="r" reverse="false"> 
					<select>
						<option><bean:message key="oscarEncounter.Header.Templates"/></option>
						<option>------------------</option>
						<option onClick="popupPage(700,700,'Templates','<%=request.getContextPath()%>/admin/providertemplate.jsp');">New Template</option>
						<option>------------------</option>
						<%
							EncounterTemplateDao encounterTemplateDao=(EncounterTemplateDao)SpringUtils.getBean("encounterTemplateDao");
							List<EncounterTemplate> allTemplates=encounterTemplateDao.findAll();
				  
							for (EncounterTemplate encounterTemplate : allTemplates) 
							{
								String templateName=StringEscapeUtils.escapeHtml(encounterTemplate.getEncounterTemplateName());
								%>
									<option onClick="popupPage(700,700,'Templates','<%=request.getContextPath()+"/admin/providertemplate.jsp?dboperation=Edit&name="+templateName%>');"><%=templateName%></option>
								<%
							}
						%>
					</select>
				</security:oscarSec>
			</div>
		</div>
	</div>
</html:form>

<nested:form action="/CaseManagementEntry" style="display:inline; margin-top:0; margin-bottom:0;">
	<html:hidden property="demographicNo" value="<%=demographicNo%>" />
	<html:hidden property="includeIssue" value="off" />
	<%
		String apptNo = request.getParameter("appointmentNo");
		if (apptNo == null || apptNo.equals(""))
		{
			apptNo = "0";
		}

		String apptDate = request.getParameter("appointmentDate");
		if (apptDate == null || apptDate.equals(""))
		{
			apptDate = oscar.util.UtilDateUtilities.getToday("yyyy-MM-dd");
		}

		String startTime = request.getParameter("start_time");
		if (startTime == null || startTime.equals(""))
		{
			startTime = "0:00";
		}

		String apptProv = request.getParameter("apptProvider");
		if (apptProv == null || apptProv.equals("") || apptProv.equals("null"))
		{
			apptProv = "none";
		}

		String provView = request.getParameter("providerview");
		if (provView == null || provView.equals("") || provView.equals("null"))
		{
			provView = "1";
		}
	%>
	<html:hidden property="appointmentNo" value="<%=apptNo%>" />
	<html:hidden property="appointmentDate" value="<%=apptDate%>" />
	<html:hidden property="start_time" value="<%=startTime%>" />
	<html:hidden property="billRegion" value="<%=((String )oscarVariables.getProperty(\"billregion\",\"\")).trim().toUpperCase()%>" />
	<html:hidden property="apptProvider" value="<%=apptProv%>" />
	<html:hidden property="providerview" value="<%=provView%>" />
	<input type="hidden" name="toBill" id="toBill" value="false">
	<input type="hidden" name="deleteId" value="0">
	<input type="hidden" name="lineId" value="0">
	<input type="hidden" name="from" value="casemgmt">
	<input type="hidden" name="method" value="save">
	<input type="hidden" name="change_diagnosis" value="<c:out value="${change_diagnosis}"/>">
	<input type="hidden" name="change_diagnosis_id" value="<c:out value="${change_diagnosis_id}"/>">
	<input type="hidden" name="newIssueId" id="newIssueId">
	<input type="hidden" name="newIssueName" id="newIssueName">
	<input type="hidden" name="ajax" value="false">
	<input type="hidden" name="chain" value="">
	<input type="hidden" name="caseNote.program_no" value="<%=pId%>">
	<input type="hidden" name="noteId" value="0">
	<input type="hidden" name="note_edit" value="new">
	<input type="hidden" name="sign" value="off">
	<input type="hidden" name="verify" value="off">
	<input type="hidden" name="forceNote" value="false">
	<input type="hidden" name="newNoteIdx" value="">
	<input type="hidden" name="notes2print" id="notes2print" value="">
	<input type="hidden" name="printCPP" id="printCPP" value="false">
	<input type="hidden" name="printRx" id="printRx" value="false">
	<input type="hidden" name="encType" id="encType" value="">

	<div id="mainContent" style="background-color: #FFFFFF; width: 100%; margin-right: -2px; display: inline; float: left;">
	<div id="issueList" style="background-color: #FFFFFF; height: 440px; width: 350px; position: absolute; z-index: 1; display: none; overflow: auto;">
	<table id="issueTable" class="enTemplate_name_auto_complete" style="position: relative; left: 0px; display: none;">
		<tr>
			<td style="height: 430px; vertical-align: bottom;">
				<div class="enTemplate_name_auto_complete" id="issueAutocompleteList" style="position: relative; left: 0px; display: none;"></div>
			</td>
		</tr>
	</table>
	</div>
	<div id="encMainDiv" style="width: 99%; border-top: thin groove #000000; border-right: thin groove #000000; border-left: thin groove #000000; background-color: #FFFFFF; height: 410px; overflow: auto; margin-left: 2px;">
	<c:if test="${not empty notesToDisplay}">
	<%
		int idx = 0;

		//Notes list will contain all notes including most recently saved
		//we need to skip this one when displaying

		//if we're editing a note, display it
		//else check for last unsigned note and use it if present
		if (cform.getCaseNote().getId() != null)
		{
			savedId = cform.getCaseNote().getId();
		}

		//Check user property for stale date and show appropriately
		UserProperty uProp = (UserProperty)request.getAttribute(UserProperty.STALE_NOTEDATE);

		Date dStaleDate = null;
		int numToDisplay = 5;
		int numDisplayed = 0;
		Calendar cal = Calendar.getInstance();
		if (uProp != null)
		{
			String strStaleDate = uProp.getValue();
			if (strStaleDate.equalsIgnoreCase("A"))
			{
				cal.set(0, 1, 1);
			}
			else
			{
				int pastMths = Integer.parseInt(strStaleDate);
				cal.add(Calendar.MONTH, pastMths);
			}

		}
		else
		{
			cal.add(Calendar.YEAR, -1);
		}

		dStaleDate = cal.getTime();

		String noteStr;
		int length;

		/*
		 *  Cycle through notes starting from the most recent and marking them for full inclusion or one line display
		 *  Need to do this now as we only count face to face encounters against limit of how many to fully display
		 *  If no user preference, show at most five face to face encounter notes
		 *  Else show all notes withing the user preference
		*/
		ArrayList<Boolean> fullTxtFormat = new ArrayList<Boolean>(noteSize);
		int pos;
		idx = 0;

		for (pos = noteSize - 1; pos >= 0; --pos)
		{
			NoteDisplay cmNote = notesToDisplay.get(pos);

			if (cmNote.isCpp())
			{
				fullTxtFormat.add(Boolean.FALSE);
				continue;
			}

			if (noteSize > numToDisplay)
			{
				if (uProp == null)
				{
					if (numDisplayed < numToDisplay && cmNote.getObservationDate().compareTo(dStaleDate) >= 0)
					{
						fullTxtFormat.add(Boolean.TRUE);

						if (EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue().equalsIgnoreCase(cmNote.getEncounterType()))
						{
							++numDisplayed;
						}
					}
					else
					{
						fullTxtFormat.add(Boolean.FALSE);
					}
				}
				else
				{
					if (cmNote.getObservationDate().compareTo(dStaleDate) >= 0)
					{
						fullTxtFormat.add(Boolean.TRUE);
					}
					else
					{
						fullTxtFormat.add(Boolean.FALSE);
					}
				}
			}
			else
			{
				fullTxtFormat.add(Boolean.TRUE);
			}
		} //end of for loop

		boolean fulltxt;
		pos = noteSize - 1;
		for (idx = 0; idx < noteSize; ++idx)
		{
			NoteDisplay note = notesToDisplay.get(idx);
			noteStr = note.getNote();
			Integer noteId = note.getNoteId();
			EDoc doc = new EDoc();
			String dispDocNo = "";
			String dispFilename = "";
			String dispStatus = " ";
			if (noteId!=null)
			{
				doc = EDocUtil.getDocFromNote((long)noteId.intValue());
				if (doc != null)
				{
					dispDocNo = doc.getDocId();
					dispFilename = doc.getFileName();
					Character status = doc.getStatus();
	
					if (status == 'A')
					{
						dispStatus = "active";
					}
					//find docname, docno and docstatus
				}
			}
			noteStr = StringEscapeUtils.escapeHtml(noteStr);
			// for remote notes, the full text is always shown.
			fulltxt = fullTxtFormat.get(pos) || note.getRemoteFacilityId()!=null;
			bgColour = CaseManagementViewAction.getNoteColour(note);
			if (fulltxt)
			{
				noteStr = noteStr.replaceAll("\n", "<br>");
			}
			else
			{
				length = noteStr.length() > 50?50:noteStr.length();
				noteStr = noteStr.substring(0, length);
			}

			boolean editWarn = !note.isSigned() && !note.getProviderNo().equals(provNo);
	%>
		<div id="nc<%=idx+1%>" class="note<%=note.isDocument()||note.isCpp()||note.isEformData()||note.isEncounterForm()?"":" noteRounded"%>">
			<input type="hidden" id="signed<%=note.getNoteId()%>" value="<%=note.isSigned()%>"> 
			<input type="hidden" id="full<%=note.getNoteId()%>" value="<%=fulltxt || (note.getNoteId() !=null && note.getNoteId().equals(savedId))%>"> 
			<input type="hidden" id="bgColour<%=note.getNoteId()%>" value="<%=bgColour%>">
			<input type="hidden" id="editWarn<%=note.getNoteId()%>" value="<%=editWarn%>">

	  		<div id="n<%=note.getNoteId()%>">
			<%
				//display last saved note for editing
				if (note.getNoteId()!=null && !"".equals(note.getNoteId()) && note.getNoteId().intValue() == savedId )
				{
					found = true;
					%>
						<script>
							savedNoteId=<%=note.getNoteId()%>;							
						</script> 
						<img title="<bean:message key="oscarEncounter.print.title"/>" id='print<%=note.getNoteId()%>' alt="<bean:message key="oscarEncounter.togglePrintNote.title"/>" onclick="togglePrint(<%=note.getNoteId()%>, event)" style='float: right; margin-right: 5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/printer.png'>
						<textarea tabindex="7" cols="84" rows="10" class="txtArea" wrap="hard" style="line-height: 1.1em;" name="caseNote_note" id="caseNote_note<%=savedId%>"><nested:write property="caseNote.note" /></textarea>
						<div class="sig" style="display:inline;<%=bgColour%>" id="sig<%=note.getNoteId()%>"><%@ include file="noteIssueList.jsp"%></div>
	
						<c:if test="${sessionScope.passwordEnabled=='true'}">
							<p style='background-color: #CCCCFF; display: none; margin: 0px;' id='notePasswd'>Password:&nbsp;<html:password property="caseNote.password" /></p>
						</c:if> 
					<%
		 		}
				else //else display contents of note for viewing
				{
					if (note.isLocked())
					{
					%>
						<span id="txt<%=note.getNoteId()%>">
							<bean:message key="oscarEncounter.Index.msgLocked" /> <%=DateUtils.getDate(note.getUpdateDate(), dateFormat, request.getLocale()) + " " + note.getProviderName()%>
						</span>
					<%
					}
					else
					{
						String rev = note.getRevision();
						if (note.getRemoteFacilityId()==null) // always display full note for remote notes
						{
							if (note.isDocument() || note.isCpp() || note.isEformData() || note.isEncounterForm())
							{
								// blank if so it never displays min/max icon for documents
							}
							else if (fulltxt)
							{
							%> 
	 							<img title="<bean:message key="oscarEncounter.MinDisplay.title"/>" id='quitImg<%=note.getNoteId()%>' alt="<bean:message key="oscarEncounter.MinDisplay.title"/>" onclick="minView(event)"
								style='float: right; margin-right: 5px; margin-bottom: 3px; margin-top: 2px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif'> 
							<%
		 					}
							else
							{
							%> 
								<img title="<bean:message key="oscarEncounter.MaxDisplay.title"/>" id='quitImg<%=note.getNoteId()%>' alt="Maximize Display" onclick="fullView(event)" style='float: right; margin-right: 5px; margin-top: 2px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_down.gif'> 
							<%
							}
						}
					
						if (note.getRemoteFacilityId()!=null) // if it's a remote note, say where if came from on the top of the note
						{
					 	%> 
						 	<div style="background-color:#ffcccc; text-align:right"><bean:message key="oscarEncounter.noteFrom.label" /> <%=note.getLocation()%></div>
						<%
						}
					
						if (note.isGroupNote()) // if it's a remote note, say where if came from on the top of the note
						{
					 	%> 
						 	<div style="background-color:#33FFCC; text-align:right">Group Note - Editable note in this <a target="_blank" href="<html:rewrite action="/PMmodule/ClientManager.do"/>?id=<%=note.getLocation() %>">client</a></div>
						<%
						}
			
						if (note.getRemoteFacilityId()==null && !note.isDocument() && !note.isCpp() && !note.isEformData() && !note.isEncounterForm()) // only allow printing for local notes and disallow for documents (was told they should open the document then print it that way)
						{
					 	%> 
						 	<img title="<bean:message key="oscarEncounter.print.title"/>" id='print<%=note.getNoteId()%>' alt="<bean:message key="oscarEncounter.togglePrintNote.title"/>" onclick="togglePrint(<%=note.getNoteId()%>   , event)" style='float: right; margin-right: 5px; margin-top: 2px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/printer.png'> 
						<%
						}
				
					 	if (!note.isDocument() && !note.isRxAnnotation())
					 	{
					 		// only allow editing for local notes
					 		// also disallow editing of cpp's inline (can be edited in the cpp area) 
					 		if (note.getRemoteFacilityId()==null && !note.isCpp() && !note.isEformData() && !note.isEncounterForm())
							{
					 			if(!note.isReadOnly()) 
					 			{
						 		%>
							 		<a title="<bean:message key="oscarEncounter.edit.msgEdit"/>" id="edit<%=note.getNoteId()%>" 
							 		href="#" onclick="<%=editWarn?"noPrivs(event)":"editNote(event)"%> ;return false;" style="float: right; margin-right: 5px; font-size: 8px;">
							 		<bean:message key="oscarEncounter.edit.msgEdit" />
							 		</a> 
								<%
								}
			 			
					 			if (professionalSpecialistDao.hasRemoteCapableProfessionalSpecialists())
					 			{
					 			%>
					 				<a href="" onclick="window.open('<%=request.getContextPath()+"/lab/CA/ALL/sendOruR01.jsp?noteId="+note.getNoteId()%>', 'eSend');return(false);" title="<bean:message key="oscarEncounter.eSendTitle"/>" style="float: right; margin-right: 5px; font-size: 8px;"><bean:message key="oscarEncounter.eSend" /></a>
					 			<%
					 			}
					 		}
					 	}
					 	else if(note.isRxAnnotation())//prescription note
					 	{
	                        String winName="dummie";
	                        int hash = Math.abs(winName.hashCode());
	                        //get drug from note id.
	                        RxPrescriptionData.Prescription rx=note.getRxFromAnnotation(note.getNoteLink());
	                                   
	                        if (note.getRemoteFacilityId()==null) // only allow editing for local notes
							{
	                      		if(!note.isReadOnly()) 
	                      		{
								%>
							 		<a title="<bean:message key="oscarEncounter.edit.msgEdit"/>" id="edit<%=note.getNoteId()%>"
							 		href="javascript:void(0);" onclick="<%=editWarn?"noPrivs(event)":"editNote(event)"%> ;return false;" style="float: right; margin-right: 5px; font-size: 8px;">
							 		<bean:message key="oscarEncounter.edit.msgEdit" />
							 		</a>
						 		<%
								}
	                   		}
	                           
		                    if(rx!=null)
	       		            {
	               		        String url="popupPage(700,800,'" + hash + "', '" + request.getContextPath() + "/oscarRx/StaticScript2.jsp?regionalIdentifier="+rx.getRegionalIdentifier()+"&cn="+response.encodeURL(rx.getCustomName())+"');";
		                        %>
		                        	<a class="links" title="<%=rx.getSpecial()%>" id="view<%=note.getNoteId()%>" href="javascript:void(0);" onclick="<%=url%>" style="float: right; margin-right: 5px; font-size: 8px;"> <bean:message key="oscarEncounter.view.rxView" /> </a>
		                        <%
	                        }
		                }
						else if (note.isDocument() && !note.getProviderNo().equals("-1"))
						{ 
							//document annotation
							String url;
					
							Enumeration em = request.getAttributeNames();
					
							String winName = "docs" + demographicNo;
							int hash = Math.abs(winName.hashCode());
					
							url = "popupPage(700,800,'" + hash + "', '" + request.getContextPath() + "/dms/documentGetFile.jsp?document=" + StringEscapeUtils.escapeJavaScript(dispFilename) + "&type=" + dispStatus + "&doc_no=" + dispDocNo + "');";
							url = url + "return false;";
					
							if (note.getRemoteFacilityId()==null) // only allow editing for local notes
							{
								if(!note.isReadOnly()) 
								{
								%> 
							 		<a title="<bean:message key="oscarEncounter.edit.msgEdit"/>" id="edit<%=note.getNoteId()%>"
							 		href="javascript:void(0);" onclick="<%=editWarn?"noPrivs(event)":"editNote(event)"%> ;return false;" style="float: right; margin-right: 5px; font-size: 8px;">
							 		<bean:message key="oscarEncounter.edit.msgEdit" />
							 		</a>
						 		<%
								}
							}
			 				%>
								<a class="links" title="<bean:message key="oscarEncounter.view.docView"/>" id="view<%=note.getNoteId()%>" href="#" onclick="<%=url%>" style="float: right; margin-right: 5px; font-size: 8px;"> <bean:message key="oscarEncounter.view" /> </a> 
							<%
			 			}
						else
						{ //document note
							String url;
						
							Enumeration em = request.getAttributeNames();
							String winName = "docs" + demographicNo;
							int hash = Math.abs(winName.hashCode());
					
							url = "popupPage(700,800,'" + hash + "', '" + request.getContextPath() + "/dms/documentGetFile.jsp?document=" + StringEscapeUtils.escapeJavaScript(dispFilename) + "&type=" + dispStatus + "&doc_no=" + dispDocNo + "');";
							url = url + "return false;";
						 	%> 
							 	<a class="links" title="<bean:message key="oscarEncounter.view.docView"/>" id="view<%=note.getNoteId()%>" href="javascript:void(0);" onclick="<%=url%>" style="float: right; margin-right: 5px; font-size: 8px;color:black">
							 		<bean:message key="oscarEncounter.view" /> 
								</a> 
							<%
					 	}
	
					 	if (note.isEformData())
						{
							String winName = "eforms"+demographicNo;
							int hash = Math.abs(winName.hashCode());
							String url = "popupPage(700,800,'"+hash+"','"+request.getContextPath()+"/eform/efmshowform_data.jsp?fdid=";
		
							CaseManagementNoteLink noteLink = note.getNoteLink();
							if (noteLink!=null) url += noteLink.getTableId();
							else url+=note.getNoteId();
							
							url += "'); return false;";
							%>
								<a class="links" title="<bean:message key="oscarEncounter.view.eformView"/>" id="view<%=note.getNoteId()%>" href="#" onclick="<%=url%>" style="float: right; margin-right: 5px; font-size: 8px;"> <bean:message key="oscarEncounter.view" /> </a> 
							<%
						} else if (note.isEncounterForm())
						{
							String winName = "encounterforms"+demographicNo;
							int hash = Math.abs(winName.hashCode());
							String url = "popupPage(700,800,'"+hash+"','"+request.getContextPath()+"/form/forwardshortcutname.jsp?demographic_no="+demographicNo+"&amp;formname="+StringEscapeUtils.escapeHtml(((NoteDisplayNonNote)note).getLinkInfo())+"'); return false;";
							%>
								<a class="links" title="<bean:message key="oscarEncounter.view.eformView"/>" id="view<%=note.getNoteId()%>" href="#" onclick="<%=url%>" style="float: right; margin-right: 5px; font-size: 8px;"> <bean:message key="oscarEncounter.view" /> </a> 
							<%
						}	
						
						%>
							<%-- render the note contents here --%>
			  				<div id="txt<%=note.getNoteId()%>" style="<%=(note.isDocument()||note.isCpp()||note.isEformData()||note.isEncounterForm())?(bgColour+";color:white;font-size:9px"):""%>">
		  						<%=noteStr%>
		  						<%
		  							if (note.isCpp()||note.isEformData()||note.isEncounterForm())
		  							{
		  								%>
											<div id="observation<%=note.getNoteId()%>" style="float: right; margin-right: 3px;">
												<i>
													Date:&nbsp;
													<span id="obs<%=note.getNoteId()%>"><%=DateUtils.getDate(note.getObservationDate(), dateFormat, request.getLocale())%></span>
													<%
														if (note.isCpp())
														{
															%>
																&nbsp;
																<bean:message key="oscarEncounter.noteRev.title" />
															<%

															if (rev!=null)
															{
																%>
																	<a style="color:#ddddff" href="#" onclick="return showHistory('<%=note.getNoteId()%>', event);"><%=rev%></a>		
																<%
															}
															else
															{
																%>
																	N/A
																<%
															}
														}
													%>
												</i>
											</div>
		  								<%
		  							}
		  						%>
			  				</div>
						<%
							
			 			if (largeNote(noteStr))
						{
			 			%> 
						 	<img title="<bean:message key="oscarEncounter.MinDisplay.title"/>" id='bottomQuitImg<%=note.getNoteId()%>' alt="<bean:message key="oscarEncounter.MinDisplay.title"/>" onclick="minView(event)" style='float: right; margin-right: 5px; margin-bottom: 3px;'
							src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif'> 
						<%
				 		}
				
						if (!note.isDocument() && !note.isCpp() && !note.isEformData() && !note.isEncounterForm())
						{
						%>
							<div id="sig<%=note.getNoteId()%>" class="sig" style="clear:both;<%=bgColour%>">
								<div id="sumary<%=note.getNoteId()%>">
									<div id="observation<%=note.getNoteId()%>" style="float: right; margin-right: 3px;">
										<i>
											Date:&nbsp;
											<span id="obs<%=note.getNoteId()%>"><%=DateUtils.getDate(note.getObservationDate(), dateFormat, request.getLocale())%></span>&nbsp;
											<bean:message key="oscarEncounter.noteRev.title" />
											<%
												if (rev!=null)
												{
													%>
														<a href="#" onclick="return showHistory('<%=note.getNoteId()%>', event);"><%=rev%></a>		
													<%
												}
												else
												{
													%>
														N/A
													<%
												}
											%>
										</i>
									</div>
							
									<div>
										<span style="float: left;"><bean:message key="oscarEncounter.editors.title" />:</span>
										<ul style="list-style: none inside none; margin: 0px;">
											<%
												ArrayList<String> editorNames = note.getEditorNames();
												Iterator<String> it = editorNames.iterator();
												int count = 0;
												int MAXLINE = 2;
												while (it.hasNext())
												{
													String providerName = it.next();
								
													if (count % MAXLINE == 0)
													{
														out.print("<li>" + providerName + "; ");
													}
													else
													{
														out.print(providerName + "</li>");
													}
													if (it.hasNext()) ++count;
												}
												if (count % MAXLINE == 0) out.print("</li>");
											%>
										</ul>
									</div>
							
									<div style="clear: right; margin-right: 3px; float: right;">Enc Type:&nbsp;<span id="encType<%=note.getNoteId()%>"><%=note.getEncounterType().equals("")?"":"&quot;" + note.getEncounterType() + "&quot;"%></span></div>
				
									<div style="display: block;">
										<span style="float: left;"><bean:message key="oscarEncounter.assignedIssues.title" /></span>
										<%
											ArrayList<String> issueDescriptions = note.getIssueDescriptions();
										
											if (issueDescriptions.size() > 0)
											{
												%>
													<ul style="float: left; list-style: circle inside none; margin: 0px;">
														<%
															for (String issueDescription : issueDescriptions)
															{
																%>
																	<li><%=issueDescription.trim()%></li>
																<%
															}
														%>
													</ul>
												<%
											}
										%>
										<br style="clear: both;">
									</div>
								</div>
							</div>
						<%
						}
					}
				}
				%>
			</div>
		</div>

					<%
						//if we are not editing note, remember note ids for setting event listeners
						//Internet Explorer does not play nice with inserting javascript between divs
						//so we store the ids here and list the event listeners at the end of this script
						if (note.getNoteId()!=null && note.getNoteId() != savedId)
						{
							if (note.isLocked())
							{
								lockedNotes.add(note.getNoteId());
							}
							else if (!fulltxt)
							{
								unLockedNotes.add(note.getNoteId());
							}
						}
			
					} //end for */
					%>
				</c:if> <%
 	if (!found)
 		{
 			savedId = 0;
 %>
 						<script>
							savedNoteId=<%=savedId%>;							
						</script> 
	<div id="nc<%=savedId%>" class="note noteRounded">
		<input type="hidden" id="signed<%=savedId%>" value="false"> 
		<input type="hidden" id="full<%=savedId%>" value="true"> 
		<input type="hidden" id="bgColour<%=savedId%>" value="color:#000000;background-color:#CCCCFF;"> 
		<input type="hidden" id="editWarn<%=savedId%>" value="false">
		<div id="n<%=savedId%>" style="line-height: 1.1em;">
			 <textarea tabindex="7" cols="84" rows="10" class="txtArea" wrap="hard" style="line-height: 1.1em;" name="caseNote_note" id="caseNote_note<%=savedId%>">
  			 <nested:write property="caseNote_note" /></textarea>
			<div class="sig" id="sig0"><%@ include file="noteIssueList.jsp"%></div>

			<c:if test="${sessionScope.passwordEnabled=='true'}">
				<p style='background-color: #CCCCFF; display: none; margin: 0px;' id='notePasswd'>Password:&nbsp;<html:password property="caseNote.password" /></p>
			</c:if>
		</div>
	</div>
	<%
		}
	%> 
	
	<%-- The BRs are here because the drop down list is not in the scrolling pane view so we need some padding at the end so when the drop down occurs it's in the view--%> <br />
	&nbsp;<br />
	&nbsp;<br />
	&nbsp;<br />

	</div>
	<script type="text/javascript">
		if (parseInt(navigator.appVersion)>3) {
			var windowHeight=750;
			if (navigator.appName=="Netscape") {
				windowHeight = window.innerHeight;
			}
			if (navigator.appName.indexOf("Microsoft")!=-1) {
				windowHeight = document.body.offsetHeight;
			}

			var divHeight=windowHeight-280;
			$("encMainDiv").style.height = divHeight+'px'; 
		}
	</script>
	
	<div id='save' style="width: 99%; background-color: #CCCCFF; padding-top: 5px; margin-left: 2px; border-left: thin solid #000000; border-right: thin solid #000000; border-bottom: thin solid #000000;">
		<span style="float: right; margin-right: 5px;">
		<%
			Facility facility = org.oscarehr.util.LoggedInInfo.loggedInInfo.get().currentFacility;
			if(facility.isEnableGroupNotes()) {
		%>
		<script>
 function selectGroup() {
	 	var noteId='<%=((CaseManagementEntryFormBean)session.getAttribute(frmName)).getNoteId()%>';
	 	var noteId = document.forms["caseManagementEntryForm"].noteId.value;
    	alert(noteId);
    	popupPage(600,700,'group','groupNoteSelect.jsp?programId='+case_program_id + '&demographicNo='+demographicNo);
    
    }
</script>
			<input tabindex="09" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/group-gnote.png"/>" id="groupNoteImg" onclick="Event.stop(event);return selectGroup();" title='<bean:message key="oscarEncounter.Index.btnGroupNote"/>'>&nbsp;
		<%  } %>			 
			<input tabindex="10" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/media-floppy.png"/>" id="saveImg" onclick="Event.stop(event);return savePage('save', 'list');" title='<bean:message key="oscarEncounter.Index.btnSave"/>'>&nbsp; 
			<input tabindex="11" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/document-new.png"/>" id="newNoteImg" onclick="newNote(event); return false;" title='<bean:message key="oscarEncounter.Index.btnNew"/>'>&nbsp;
			<input tabindex="12" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/note-save.png"/>" onclick="document.forms['caseManagementEntryForm'].sign.value='on';Event.stop(event);return savePage('saveAndExit', '');" title='<bean:message key="oscarEncounter.Index.btnSignSave"/>'>&nbsp; 
			<input tabindex="13" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/verify-sign.png"/>" onclick="document.forms['caseManagementEntryForm'].sign.value='on';document.forms['caseManagementEntryForm'].verify.value='on';Event.stop(event);return savePage('save', 'list');" title='<bean:message key="oscarEncounter.Index.btnSign"/>'>&nbsp;
			<%
				if(bean.source == null)  { 
				%>
					<input tabindex="13" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/dollar-sign-icon.png"/>" onclick="document.forms['caseManagementEntryForm'].sign.value='on';document.forms['caseManagementEntryForm'].toBill.value='true';Event.stop(event);return savePage('saveAndExit', '');" title='<bean:message key="oscarEncounter.Index.btnBill"/>'>&nbsp;
				<%
				}
			%>
			
			<c:if test="${sessionScope.passwordEnabled=='true'}">
				<input tabindex="14" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/lock-note.png"/>" onclick="return toggleNotePasswd();" title='<bean:message key="oscarEncounter.Index.btnLock"/>'>&nbsp;
	    	</c:if>
	    	
	    	<input tabindex="15" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/system-log-out.png"/>" onclick='closeEnc(event);return false;' title='<bean:message key="global.btnExit"/>'>&nbsp; 
	    	<input tabindex="16" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/document-print.png"/>" onclick="return printSetup(event);" title='<bean:message key="oscarEncounter.Index.btnPrint"/>'> 
    	</span> 
    	<input type='image' id='toggleIssue' onclick="return showIssues(event);" src="<c:out value="${ctx}/oscarEncounter/graphics/issues.png"/>" title='<bean:message key="oscarEncounter.Index.btnDisplayIssues"/>'>&nbsp; 
    	<input tabindex="8" type="text" id="issueAutocomplete" name="issueSearch" style="z-index: 2;" onkeypress="return submitIssue(event);" size="25">&nbsp; <input tabindex="9" type="button" id="asgnIssues" value="<bean:message key="oscarEncounter.assign.title"/>"> 
    	<span id="busy" style="display: none">
    		<img style="position: absolute;" src="<c:out value="${ctx}/oscarEncounter/graphics/busy.gif"/>" alt="<bean:message key="oscarEncounter.Index.btnWorking" />">
    	</span>
    </div>
</div>
</nested:form>
</div>

<script type="text/javascript">
    document.forms["caseManagementEntryForm"].noteId.value = "<%=savedId%>";


    caseNote = "caseNote_note" + "<%=savedId%>";
    //are we editing existing note?  if not init newNoteIdx as we are dealing with a new note
   //save initial note to determine whether save is necessary
   origCaseNote = $F(caseNote);
   <%if (!bean.oscarMsg.equals(""))
			{%>
        $(caseNote).value +="\n\n<%=org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(bean.oscarMsg)%>";
   <%bean.reason = "";
				bean.oscarMsg = "";
			}

			if (request.getParameter("noteBody") != null)
			{
				String noteBody = request.getParameter("noteBody");
				noteBody = noteBody.replaceAll("<br>|<BR>", "\n");%>
        $(caseNote).value +="\n\n<%=org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(noteBody)%>";
   <%}

			if (found != true)
			{%>
        document.forms["caseManagementEntryForm"].newNoteIdx.value = <%=savedId%>;
   <%}
			else
			{%>
        document.forms["caseManagementEntryForm"].note_edit.value = "existing";
    <%}%>
    setupNotes();
    Element.observe(caseNote, "keyup", monitorCaseNote);
    Element.observe(caseNote, 'click', getActiveText);
    <%Integer num;
			Iterator<Integer> iterator = lockedNotes.iterator();
			while (iterator.hasNext())
			{
				num = iterator.next();%>
            Element.observe('n<%=num%>', 'click', unlockNote);
    <%}

			iterator = unLockedNotes.iterator();
			while (iterator.hasNext())
			{
				num = iterator.next();%>
            Element.observe('n<%=num%>', 'click', fullView);
    <%}%>

    //flag for determining if we want to submit case management entry form with enter key pressed in auto completer text box
    var submitIssues = false;
   //AutoCompleter for Issues
    <c:url value="/CaseManagementEntry.do?method=issueList&demographicNo=${demographicNo}&providerNo=${param.providerNo}" var="issueURL" />
    var issueAutoCompleter = new Ajax.Autocompleter("issueAutocomplete", "issueAutocompleteList", "<c:out value="${issueURL}"/>", {minChars: 3, indicator: 'busy', afterUpdateElement: saveIssueId, onShow: autoCompleteShowMenu, onHide: autoCompleteHideMenu});

    <%int MaxLen = 20;
			int TruncLen = 17;
			String ellipses = "...";
			for (int j = 0; j < bean.templateNames.size(); j++)
			{
				String encounterTmp = (String)bean.templateNames.get(j);
				encounterTmp = oscar.util.StringUtils.maxLenString(encounterTmp, MaxLen, TruncLen, ellipses);
				encounterTmp = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(encounterTmp);%>
     autoCompleted["<%=encounterTmp%>"] = "ajaxInsertTemplate('<%=encounterTmp%>')";
     autoCompList.push("<%=encounterTmp%>");
     itemColours["<%=encounterTmp%>"] = "99CCCC";
   <%}%>
   //set default event for assigning issues
   //we do this here so we can change event listener when changing diagnosis
   var obj = { };
   var makeIssue = "makeIssue";
   var defaultDiv = "sig<%=savedId%>";
   var changeIssueFunc;  //set in changeDiagnosis function above
   var addIssueFunc = updateIssues.bindAsEventListener(obj, makeIssue, defaultDiv);
   Element.observe('asgnIssues', 'click', addIssueFunc);
   new Autocompleter.Local('enTemplate', 'enTemplate_list', autoCompList, { colours: itemColours, afterUpdateElement: menuAction }  );

   //start timer for autosave
   setTimer();

    //$("encMainDiv").scrollTop = $("n<%=savedId%>").offsetTop - $("encMainDiv").offsetTop;
    reason = "<%=insertReason(request, bean)%>";    //function defined bottom of file
   </script>

<%
}
catch (Exception e)
{
	MiscUtils.getLogger().error("Unexpected error.", e);
}
%>

<%!/*
																						 *Insert encounter reason for new note
																						 */
	protected String insertReason(HttpServletRequest request, oscar.oscarEncounter.pageUtil.EctSessionBean bean)
	{
		String encounterText = "";
		if (bean != null)
		{
			String apptDate = convertDateFmt(bean.appointmentDate);
			if (bean.eChartTimeStamp == null)
			{
				encounterText = "\n[" + oscar.util.UtilDateUtilities.DateToString(bean.currentDate, "dd-MMM-yyyy", request.getLocale()) + " .: " + bean.reason + "] \n";
			}
			else
			{
				encounterText = "\n[" + ("".equals(bean.appointmentDate)?oscar.util.UtilDateUtilities.getToday("dd-MMM-yyyy"):apptDate) + " .: " + bean.reason + "]\n";
			}
		}
		encounterText = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(encounterText);
		return encounterText;
	}

	protected String convertDateFmt(String strOldDate)
	{
		String strNewDate = "";
		if (strOldDate != null && strOldDate.length() > 0)
		{
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			try
			{

				Date tempDate = fmt.parse(strOldDate);
				strNewDate = new SimpleDateFormat("dd-MMM-yyyy").format(tempDate);

			}
			catch (ParseException ex)
			{
				MiscUtils.getLogger().error("Error", ex);
			}
		}

		return strNewDate;
	}

	protected boolean largeNote(String note)
	{
		final int THRESHOLD = 10;
		boolean isLarge = false;
		int pos = -1;

		for (int count = 0; (pos = note.indexOf("\n", pos + 1)) != -1; ++count)
		{
			if (count == THRESHOLD)
			{
				isLarge = true;
				break;
			}
		}

		return isLarge;
	}
%>
