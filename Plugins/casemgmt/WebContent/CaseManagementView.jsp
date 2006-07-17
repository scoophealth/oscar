<%@ include file="/taglibs.jsp" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested" prefix="nested" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/plugin-tag.tld" prefix="plugin" %>
<%@ taglib uri="/WEB-INF/tld/caisirole-tag.tld" prefix="caisirole" %>
<%@ page import="org.caisi.casemgmt.model.*" %>
<%@ page import="org.caisi.casemgmt.web.formbeans.*" %>

<link rel="stylesheet" href="css/casemgmt.css" type="text/css">

<html:form action="/CaseManagementView" method="get">
<html:hidden property="demographicNo"/>
<html:hidden property="providerNo"/>
<html:hidden property="tab"/>
<input type="hidden" name="method" value="view"/>
<input type="hidden" name="hideActiveIssue" value="true">

<script>
	function clickTab(name) {
		document.caseManagementViewForm.tab.value=name;
		document.caseManagementViewForm.submit();
	}
	function popupNotePage(varpage) {
        var page = "" + varpage;
        windowprops = "height=800,width=800,location=no,"
          + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
        window.open(page, "", windowprops);
    }
    

function popupUploadPage(varpage,dn) {
        var page = "" + varpage+"?demographicNo="+dn;
        windowprops = "height=500,width=500,location=no,"
          + "scrollbars=no,menubars=no,toolbars=no,resizable=yes,top=50,left=50";
         var popup=window.open(page, "", windowprops);
         popup.focus();
        
    }
    
    
</script>

<div class="tabs" id="tabs">

<%
	String selectedTab = request.getParameter("tab");
	if(selectedTab==null || selectedTab.trim().equals("")) {
		selectedTab=CaseManagementViewFormBean.tabs[0];
		//System.out.println("selectnull");
	}
	//System.out.println("selectTab="+selectedTab);
	
	java.util.List aList=(java.util.List)request.getAttribute("Allergies"); 
	boolean allergies=false;
	if (aList!=null){
		allergies = aList.size() > 0;
	}
	//get programId
	String pId=(String)session.getAttribute("case_program_id");
	if (pId==null) pId="";
	System.out.println("pId="+pId);
	System.out.println("providerNo="+request.getParameter("providerNo"));
	System.out.println("demographicNo="+request.getParameter("demographicNo"));
%>
<table>
<tr>
<th width="8%"></th><th style="font-size: 20" colspan="2" width="80%"><b>Case Management Encounter</b></th>
<%
 String verno=org.caisi.comp.FrameworkFactory.getFramework().getComponent("casemgmtComp").getComponent().getVersion();
%>
<th width="12%" align="right" nowrap>version <%=verno %></th>
</tr>

</table>
<table cellpadding="0" cellspacing="0" border="0">

	<tr>
		<% for(int x=0;x<CaseManagementViewFormBean.tabs.length;x++) {%>
			<%
				String extra = "";
				if(allergies && CaseManagementViewFormBean.tabs[x].equals("Allergies")) {
					extra="color:red;";
				}
			%>
			<%if (CaseManagementViewFormBean.tabs[x].equals("Allergies") || CaseManagementViewFormBean.tabs[x].equals("Prescriptions")){%>
			<caisirole:SecurityAccess accessName="prescription Read" accessType="access" providerNo="<%=request.getParameter("providerNo")%>" demoNo="<%=request.getParameter("demographicNo")%>" programId="<%=pId%>">
			<%if(CaseManagementViewFormBean.tabs[x].equals(selectedTab)) { %>
				<td style="background-color: #555;<%=extra%>"><a href="javascript:void(0)" onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x] %>'); return false;"><%=CaseManagementViewFormBean.tabs[x] %></a></td>
			<%} else { %>
				<td><a style="<%=extra %>" href="javascript:void(0)" onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x] %>');return false;"><%=CaseManagementViewFormBean.tabs[x] %></a></td>
			<% } %>
			</caisirole:SecurityAccess>
			<%}else{ %>
			<%if(CaseManagementViewFormBean.tabs[x].equals(selectedTab)) { %>
				<td style="background-color: #555;<%=extra%>"><a href="javascript:void(0)" onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x] %>'); return false;"><%=CaseManagementViewFormBean.tabs[x] %></a></td>
			<%} else { %>
				<td><a style="<%=extra %>" href="javascript:void(0)" onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x] %>');return false;"><%=CaseManagementViewFormBean.tabs[x] %></a></td>
			<% } %>
			<%} %>
		<% } %>
	</tr>
</table>
</div>
<br/>
<table>
<tr>
<td>
<b>Client Name: &nbsp; <I><c:out value="${requestScope.casemgmt_demoName}" /></I><br>
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;       Age:  &nbsp; &nbsp; <I><c:out value="${requestScope.casemgmt_demoAge}" /></I><br>
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;       DOB:  &nbsp; &nbsp; <I><c:out value="${requestScope.casemgmt_demoDOB}" /></I>
</b>
<br>
<br>
</td>
<td>
<%
//gif
String gifimagefile=getServletContext().getRealPath("/")+"/images/client"+request.getParameter("demographicNo")+".gif";
String gifidfile="images/client"+request.getParameter("demographicNo")+".gif";
//jpg
String jpgimagefile=getServletContext().getRealPath("/")+"/images/client"+request.getParameter("demographicNo")+".jpg";
String jpgidfile="images/client"+request.getParameter("demographicNo")+".jpg";
//jpeg
String jpegimagefile=getServletContext().getRealPath("/")+"/images/client"+request.getParameter("demographicNo")+".jpeg";
String jpegidfile="images/client"+request.getParameter("demographicNo")+".jpeg";

String demo=request.getParameter("demographicNo");
//out.println(imagefile);
if((new java.io.File(gifimagefile)).exists()) {
%>
	<img style="cursor: pointer;" src="<%=gifidfile %>" alt="id_photo"  height="100" title="Click to upload new photo." OnMouseOver="window.status='Click to upload new photo'; return true;" onClick="popupUploadPage('uploadimage.jsp',<%=demo%>);return false;"/>	
<% 
request.getSession().setAttribute("image",gifidfile);
}
else if((new java.io.File(jpgimagefile)).exists()){	
%>
	<img style="cursor: pointer;" src="<%=jpgidfile %>" alt="id_photo"  height="100" title="Click to upload new photo." OnMouseOver="window.status='Click to upload new photo'; return true;" onClick="popupUploadPage('uploadimage.jsp',<%=demo%>);return false;"/>	
<%	
request.getSession().setAttribute("image",jpgidfile);
}
else if((new java.io.File(jpegimagefile)).exists()){	
	%>
	<img style="cursor: pointer;" src="<%=jpegidfile %>" alt="id_photo"  height="100" title="Click to upload new photo." OnMouseOver="window.status='Click to upload new photo'; return true;" onClick="popupUploadPage('uploadimage.jsp',<%=demo%>);return false;"/>	
	<%	
request.getSession().setAttribute("image",jpegidfile);
	}
else {
	%>
	<img style="cursor: pointer;" src="images/default_img.jpg" alt="default_id_photo" height="100" title="Click to upload new photo." OnMouseOver="window.status='Click to upload new photo';return true" onClick="popupUploadPage('uploadimage.jsp',<%=demo%>);return false;"/>
	<% 	
	request.getSession().setAttribute("image","images/default_img.jpg");
out.println("No ID photo");
}
%>

</td>
</tr>
</table>
<jsp:include page="<%="/"+selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp"%>"/>

<br/><br/>
Progress Note Report View:
<c:if test="${sessionScope.caseManagementViewForm.note_view!='detailed'}">
<html:hidden property="note_view" value="summary"/>
</c:if>
<c:if test="${sessionScope.caseManagementViewForm.note_view=='detailed'}">
<html:hidden property="note_view" value="detailed"/>
</c:if>
<c:if test="${sessionScope.caseManagementViewForm.prescipt_view!='all'}">
<html:hidden property="prescipt_view" value="current"/>
</c:if>
<c:if test="${sessionScope.caseManagementViewForm.prescipt_view=='all'}">
<html:hidden property="prescipt_view" value="all"/>
</c:if>
<c:if test="${sessionScope.caseManagementViewForm.note_view!='detailed'}">
<table id="test" width="100%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
	<tr class="title">
		<td>Date</td>
		<td>Provider</td>
		<td>status</td>
	</tr>
	<%int index=0; String bgcolor="white"; %>
	<c:forEach var="note" items="${Notes}">
		<%
			if(index++%2!=0) {
				bgcolor="white";
			} else {
				bgcolor="#EEEEFF";
			}
		java.util.List nList=(java.util.List)request.getAttribute("Notes");
		String nId=((CaseManagementNote)nList.get(index-1)).getId().toString();
		request.setAttribute("nId",nId);
		%>
		<tr bgcolor="<%=bgcolor %>" align="center">
			<td><fmt:formatDate pattern="MM/dd/yy hh:mm a" value="${note.update_date}"/></td>
			<c:if test="${note.provider!=null}">
			<td><c:out value="${note.provider.formattedName }"/></td>
			</c:if>
			<c:if test="${note.provider==null}">
			<td>deleted provider</td>
			</c:if>
			<td>
			<c:if test="${!note.signed }">
			<c:url value="/CaseManagementEntry.do?method=edit&from=casemgmt&noteId=${requestScope.nId}&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}" var="notesURL" />
			<input type="button" value="finish and sign note" onclick="popupNotePage('<c:out value="${notesURL}" escapeXml="false"/>')" >
			</c:if>
			<c:if test="${note.signed }">
			signed note
			</c:if>
			</td>
		</tr>
	</c:forEach>
</table>
</c:if>
<c:if test="${sessionScope.caseManagementViewForm.note_view=='detailed'}">
<table id="test" width="100%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
	<%int index1=0; String bgcolor1="white"; %>
	<c:forEach var="note" items="${Notes}">
		<%
			if(index1++%2!=0) {
				bgcolor1="white";
			} else {
				bgcolor1="#EEEEFF";
			}
			java.util.List noteList=(java.util.List)request.getAttribute("Notes");
			String noteId=((CaseManagementNote)noteList.get(index1-1)).getId().toString();
			request.setAttribute("noteId",noteId);
		%>
		<tr>
		<td>
		<table width="100%" border="0">
		<tr bgcolor="<%=bgcolor1 %>">
			<td width="7%">Provider</td>
			<td width="93%"><c:out value="${note.provider.formattedName }"/></td>
		</tr>
		<tr bgcolor="<%=bgcolor1 %>">
			<td width="7%">Date</td>
			<td width="93%"><c:out value="${note.update_date }"/></td>
		</tr>
		<tr bgcolor="<%=bgcolor1 %>">
			<td width="7%">Status</td>
			<td width="93%">
			<c:if test="${!note.signed }">
			<c:url value="/CaseManagementEntry.do?method=edit&from=casemgmt&noteId=${requestScope.noteId}&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}" var="notesURL" />
			<input type="button" value="finish and sign note" onclick="popupNotePage('<c:out value="${notesURL}" escapeXml="false"/>')" >
			</c:if>
			<c:if test="${note.signed }">
			signed note
			</c:if>
			</td>
		</tr>
		<tr bgcolor="<%=bgcolor1 %>">
			<td width="7%">Note</td>
			<td width="93%"><pre><c:out value="${note.note }"/></pre></td>
		</tr>
		</table>
		</td>
		</tr>
	</c:forEach>
</table>
</c:if>
<span style="text-decoration: underline;cursor:pointer;color: blue" onclick="document.caseManagementViewForm.note_view.value='summary';document.caseManagementViewForm.method.value='setViewType';document.caseManagementViewForm.submit(); return false;" >Summary</span>
&nbsp;|&nbsp;
<span style="text-decoration: underline;cursor:pointer;color: blue" onclick="document.caseManagementViewForm.note_view.value='detailed';document.caseManagementViewForm.method.value='setViewType';document.caseManagementViewForm.submit();return false;">Detailed</span>
<br/><br/>
<c:url value="/CaseManagementEntry.do?method=edit&note_edit=new&from=casemgmt&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}" var="noteURL" />
<input type="button" value="Create new Note" onclick="popupNotePage('<c:out value="${noteURL}" escapeXml="false"/>')" >

</html:form>
