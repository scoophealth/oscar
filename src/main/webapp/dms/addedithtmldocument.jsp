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
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
String user_no = (String) session.getAttribute("user");
String userfirstname = (String) session.getAttribute("userfirstname");
String userlastname = (String) session.getAttribute("userlastname");
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@ page
	import="java.util.*, oscar.*, oscar.util.*, oscar.dms.*, oscar.dms.data.*, oscar.oscarProvider.data.ProviderData, org.oscarehr.util.SpringUtils, org.oscarehr.common.dao.CtlDocClassDao"%><%
String mode = "";
if (request.getAttribute("mode") != null) {
    mode = (String) request.getAttribute("mode");
} else if (request.getParameter("mode") != null) {
    mode = request.getParameter("mode");
}

String editDocumentNo = "";
if (request.getAttribute("editDocumentNo") != null) {
    editDocumentNo = (String) request.getAttribute("editDocumentNo");
    mode = editDocumentNo;
} else if (request.getParameter("editDocumentNo") != null) {
    editDocumentNo = request.getParameter("editDocumentNo");
    mode = editDocumentNo;
}

String module = "";
String moduleid = "";
if (request.getParameter("function") != null) {
    module = request.getParameter("function");
    moduleid = request.getParameter("functionid");
} else if (request.getAttribute("function") != null) {
    module = (String) request.getAttribute("function");
    moduleid = (String) request.getAttribute("functionid");
}

OscarProperties props = OscarProperties.getInstance();
String defaultType = props.getProperty("eDocAddTypeDefault", "");
String defaultDesc = "Enter Title"; //if defaultType isn't defined, this value is used for the title/description

Hashtable linkhtmlerrors = new Hashtable();
if (request.getAttribute("linkhtmlerrors") != null) {
    linkhtmlerrors = (Hashtable) request.getAttribute("linkhtmlerrors");
}

   String lastUpdate = "", fileName = "";
   boolean oldDoc = true;
   AddEditDocumentForm formdata = new AddEditDocumentForm();
if (request.getAttribute("completedForm") != null) {
    formdata = (AddEditDocumentForm) request.getAttribute("completedForm");
    lastUpdate = EDocUtil.getDmsDateTime();
} else if ((editDocumentNo != null) && (!editDocumentNo.equals(""))) {
    EDoc currentDoc = EDocUtil.getDoc(editDocumentNo);
    formdata.setFunction(currentDoc.getModule());
    formdata.setFunctionId(currentDoc.getModuleId());
    formdata.setDocType(currentDoc.getType());
    formdata.setDocClass(currentDoc.getDocClass());
    formdata.setDocSubClass(currentDoc.getDocSubClass());
    formdata.setDocDesc(currentDoc.getDescription());
    formdata.setDocPublic((currentDoc.getDocPublic().equals("1"))?"checked":"");
    formdata.setDocCreator(currentDoc.getCreatorId());
    formdata.setResponsibleId(currentDoc.getResponsibleId());
    formdata.setSource(currentDoc.getSource());
    formdata.setSourceFacility(currentDoc.getSourceFacility());
    formdata.setObservationDate(currentDoc.getObservationDate());
    formdata.setReviewerId(currentDoc.getReviewerId());
    formdata.setReviewDateTime(currentDoc.getReviewDateTime());
    formdata.setContentDateTime(UtilDateUtilities.DateToString(currentDoc.getContentDateTime(),EDocUtil.CONTENT_DATETIME_FORMAT));
    formdata.setHtml(UtilMisc.htmlEscape(currentDoc.getHtml()));
    lastUpdate = currentDoc.getDateTimeStamp();
    fileName = currentDoc.getFileName();
} else {
    formdata.setFunction(module);  //"module" and "function" are the same
    formdata.setFunctionId(moduleid);
    formdata.setDocType(defaultType);
    formdata.setDocDesc(defaultType.equals("")?defaultDesc:defaultType);
    formdata.setDocCreator(user_no);
    formdata.setObservationDate(UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
    lastUpdate = "--";
    oldDoc = false;
}

List<Map<String,String>> pdList = new ProviderData().getProviderList();
ArrayList<String> doctypes = EDocUtil.getDoctypes(module);
String annotation_display = org.oscarehr.casemgmt.model.CaseManagementNoteLink.DISP_DOCUMENT;
String annotation_tableid = editDocumentNo;
Long now = new Date().getTime();
String annotation_attrib = "anno"+now;

CtlDocClassDao docClassDao = (CtlDocClassDao)SpringUtils.getBean("ctlDocClassDao");
List<String> reportClasses = docClassDao.findUniqueReportClasses();
ArrayList<String> subClasses = new ArrayList<String>();
ArrayList<String> consultA = new ArrayList<String>();
ArrayList<String> consultB = new ArrayList<String>();
for (String reportClass : reportClasses) {
    List<String> subClassList = docClassDao.findSubClassesByReportClass(reportClass);
    if (reportClass.equals("Consultant ReportA")) consultA.addAll(subClassList);
    else if (reportClass.equals("Consultant ReportB")) consultB.addAll(subClassList);
    else subClasses.addAll(subClassList);

    if (!consultA.isEmpty() && !consultB.isEmpty()) {
        for (String partA : consultA) {
            for (String partB : consultB) {
                subClasses.add(partA+" "+partB);
            }
        }
    }
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Edit Document</title>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>
<script type="text/javascript" src="../share/javascript/scriptaculous.js"></script>

<link rel="stylesheet" type="text/css"
	href="../share/css/niftyCorners.css" />
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css" />
<link rel="stylesheet" type="text/css" href="dms.css" />
<link rel="stylesheet" type="text/css"
	href="../share/css/niftyPrint.css" media="print" />
<script type="text/javascript" src="../share/javascript/nifty.js"></script>
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />
<style type="text/css">
    .autocomplete_style {
        background: #fff;
        text-align: left;
    }

    .autocomplete_style ul {
        border: 1px solid #aaa;
        margin: 0px;
        padding: 2px;
        list-style: none;
    }

    .autocomplete_style ul li.selected {
        background-color: #ffa;
        text-decoration: underline;
    }
</style>

<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<script type="text/javascript">
            window.onload=function(){
                if(!NiftyCheck()) return;

                    //Rounded("div.leftplane","top", "transparent", "#CCCCFF","small border #ccccff");
                    //Rounded("div.leftplane","bottom","transparent","#EEEEFF","small border #ccccff");
            }
            function prepare() {
                new Autocompleter.Local('docSubClass', 'docSubClass_list', docSubClassList);
            }

            function submitUpload(object) {
                object.Submit.disabled = true;
		var ans = true;
		if (object.reviewerId.value!="") {
		    ans = confirm("Re-submitting this HTML will remove reviewer information. Confirm?");
		}
                if (ans && !validDate("observationDate")) {
                    alert("Invalid Date: must be in format yyyy/mm/dd");
                    ans = false;
                }
		object.Submit.disabled = false;
                return ans;
            }
            function checkDefaultValue(object) {
              //selectBoxType = object.form.docType
              //var selectedType = selectBoxType.options[selectBoxType.selectedIndex].value;
              if ((object.value == "<%= defaultDesc%>") || (object.value == "<%= defaultType%>")) {
                  object.value = "";
              }
            }
            function checkSel(sel){
              theForm = sel.form;
              if ((theForm.docDesc.value == "") || (theForm.docDesc.value == "<%= defaultDesc%>")) {
                   theForm.docDesc.value = theForm.docType.value;
                   theForm.docDesc.focus();
                   theForm.docDesc.select();
              }
            }
	    function reviewed(ths) {
		thisForm = ths.form;
		thisForm.reviewDoc.value = true;
		thisForm.submit();
	    }

            var docSubClassList = [
<% for (int i=0; i<subClasses.size(); i++) { %>
            "<%=subClasses.get(i)%>"<%=(i<subClasses.size()-1)?",":""%>
<% } %>
            ];
            
function newDocType(){ 
	var newOpt = prompt("Please enter new document type:", ""); 
	if (newOpt != "") { 
		document.getElementById("docType").options[document.getElementById("docType").length] = new Option(newOpt, newOpt); 
		document.getElementById("docType").options[document.getElementById("docType").length-1].selected = true;                 
	} else { 
		alert("Invalid entry"); 
	}
} 

        </script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body class="mainbody" onLoad="prepare();">
<div class="maindiv">
<div class="maindivheading">&nbsp;&nbsp;&nbsp; Edit Document</div>
<%-- Lists linkhtmlerrors --%> <% for (Enumeration errorkeys = linkhtmlerrors.keys(); errorkeys.hasMoreElements();) {%>
<font class="warning">Error: <bean:message
	key="<%=(String) linkhtmlerrors.get(errorkeys.nextElement())%>" /></font><br />
<% } %> <html:form action="/dms/addEditHtml" method="POST"
	enctype="multipart/form-data" styleClass="form"
	onsubmit="return submitUpload(this);">
	<input type="hidden" name="function"
		value="<%=formdata.getFunction()%>" size="20" />
	<input type="hidden" name="functionId"
		value="<%=formdata.getFunctionId()%>" size="20" />
	<input type="hidden" name="functionid" value="<%=moduleid%>" size="20" />
	<input type="hidden" name="mode" value="<%=mode%>" />
	<input type="hidden" name="docCreator"
		value="<%=formdata.getDocCreator()%>" />
	<input type="hidden" name="reviewerId" value="<%=formdata.getReviewerId()%>" />
	<input type="hidden" name="reviewDateTime" value="<%=formdata.getReviewDateTime()%>" />
	<input type="hidden" name="reviewDoc" value="false" />
	<input type="hidden" name="annotation_attrib" value="<%=annotation_attrib%>" />

	<table width="100%" height="100%" class="layouttable">
		<tr>
			<td width="180px">Type:</td>
				<td> 
					<select id="docType" name="docType" style="width: 160" > 
					<option value=""><bean:message key="dms.addDocument.formSelect" /></option> 
					<% for (int i=0; i<doctypes.size(); i++) { 
						String doctype = doctypes.get(i); %> 
						<option value="<%= doctype%>" <%=(formdata.getDocType().equals(doctype))?" selected":""%>><%= doctype%></option> 
					<%}%> 
					</select> 
					<input id="docTypeinput" type="button" size="20" onClick="newDocType();" value="<bean:message key="dms.documentEdit.formAddNewDocType"/> " /> 
				</td> 
                </tr>
                <tr>
                        <td><bean:message key="dms.addDocument.msgDocClass"/>:</td>
                        <td><select name="docClass" id="docClass">
                                <option value=""><bean:message key="dms.addDocument.formSelectClass"/></option>
<% boolean consultShown = false;
for (String reportClass : reportClasses) {
    if (reportClass.startsWith("Consultant Report")) {
        if (consultShown) continue;
        reportClass = "Consultant Report";
        consultShown = true;
    }
%>
                                <option value="<%=reportClass%>" <%=reportClass.equals(formdata.getDocClass())?"selected":""%>><%=reportClass%></option>
<% } %>
                            </select>
                        </td>
		</tr>
                <tr>
                        <td><bean:message key="dms.addDocument.msgDocSubClass"/>:</td>
                        <td><input type="text" name="docSubClass" id="docSubClass" value="<%=formdata.getDocSubClass()%>" style="width:330px">
                            <div class="autocomplete_style" id="docSubClass_list"></div>
                        </td>
		</tr>
		<tr>
			<td>Description:</td>
			<td><input <% if (linkhtmlerrors.containsKey("descmissing")) {%>
				class="warning" <%}%> type="text" name="docDesc" size="30"
				onfocus="checkDefaultValue(this)" value="<%=formdata.getDocDesc()%>"></td>
		</tr>
		<tr>
			<td>Added By:</td>
			<td><%=EDocUtil.getProviderName(formdata.getDocCreator())%></td>
		</tr>
		<tr>
			<td>Responsible Provider:</td>
			<td>
			    <select name="responsibleId">
				<option value="">---</option>
		<% for (Map pd : pdList) {
			String selected = "";
			if (formdata.getResponsibleId().equals(pd.get("providerNo"))) selected = "selected";
			%>
				<option value="<%=pd.get("providerNo")%>" <%=selected%>><%=pd.get("lastName")%>, <%=pd.get("firstName")%></option>
		<% } %>
			    </select>
			</td>
		</tr>
		<tr>
			<td>Date Added/Updated:</td>
			<td><%=lastUpdate%></td>
		</tr>
                <tr>
			<td><bean:message key="dms.addDocument.formContentAddedUpdated"/>:</td>
			<td><%=formdata.getContentDateTime()%></td>
		</tr>
		<tr>
			<td>Source Author:</td>
			<td><input type="text" name="source" size="15" value="<%=formdata.getSource()%>"/></td>
		</tr>
		<tr>
			<td>Source Facility:</td>
			<td><input type="text" name="sourceFacility" size="15" value="<%=formdata.getSourceFacility()%>"/></td>
		</tr>
		<tr>
			<td>Observation Date <font class="comment">(yyyy/mm/dd):</font></td>
			<td><input type="text" name="observationDate"
				id="observationDate" value="<%=formdata.getObservationDate()%>"><a
				id="obsdate"><img title="Calendar" src="../images/cal.gif"
				alt="Calendar" border="0" /></a></td>
		</tr>
		<% if (module.equals("provider")) {%>
		<tr>
			<td>Public?</td>
			<td><input type="checkbox" name="docPublic"
				<%=formdata.getDocPublic() + " "%> value="checked"></td>
		</tr>
		<% }
		if (oldDoc) { %>
		<tr>
			<td colspan="2">
			    <% if (formdata.getReviewerId()!=null && !formdata.getReviewerId().equals("")) { %>
			    Reviewed: &nbsp; <%=EDocUtil.getProviderName(formdata.getReviewerId())%>
			    &nbsp; [<%=formdata.getReviewDateTime()%>]
			    <% } else { %>
			    <input type="button" value="Reviewed" title="Click to set Reviewed" onclick="reviewed(this);" />
			    <% } %>
			</td>
		</tr>
		<% } %>
		<tr>
			<td colspan="2">
			    <input type="button" value="Annotation"
			    onclick="window.open('../annotation/annotation.jsp?atbname=<%=annotation_attrib%>&display=<%=annotation_display%>&table_id=<%=annotation_tableid%>&demo=<%=moduleid%>','anwin','width=400,height=500');" />
			</td>
		</tr>
		<tr>
			<td colspan="2">Html:</td>
		</tr>
		<tr>
			<td colspan="2">
			    <textarea name="html" <% if (linkhtmlerrors.containsKey("uploaderror")) {%>
				class="warning" <%}%> wrap="off" style="width: 98%; height: 200px;"><%=formdata.getHtml()%>
			    </textarea>
			</td>
		</tr>
	</table>
	<center>
	<div><input type="submit" name="Submit" value="Submit"><input
		type="button" value="Cancel" onclick="window.close();"></div>
	</center>
</html:form> <script type="text/javascript">
               Calendar.setup( { inputField : "observationDate", ifFormat : "%Y/%m/%d", showsTime :false, button : "obsdate", singleClick : true, step : 1 } );
           </script></div>
</body>
</html>
