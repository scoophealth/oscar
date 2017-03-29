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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>


<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

<%
				Integer noteId = Integer.parseInt(request.getParameter("noteId"));
				String aptNo = request.getParameter("appointmentNo");
				String demographicNo = request.getParameter("demographicNo");
			%>         
				
<script>

function setDischarge(){	
	saveFlags();
}

function setStat(){
	saveFlags();
}

function setOpt(){	
	saveFlags();
}

function popupPageOne(varpage,name) {
    var page = "" + varpage;
    windowprops = "height=600,width=700,location=no,"
      + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
    window.open(page, name, windowprops);
}

function popupPageOne(varpage,name,height,width) {
    var page = "" + varpage;
    windowprops = "height="+height+",width="+width+",location=no,"
      + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
    window.open(page, name, windowprops);
}


</script>

<span style="float: left;">
<script>
function saveEyeformNoteNoGenerate() {
	saveFlags();
}

function saveEyeformNote() {
	//alert("save function called for eyeform - " + savedNoteId);
	//do the ajax call to save form values 
	saveFlags();

	var notetext = '';
	//get consults/procedures/tests/checkboxes to generate text
	jQuery.ajax({ url: ctx+"/eyeform/FollowUp.do?method=getNoteText&appointmentNo="+<%=aptNo%>, async:false, success: function(data){
        notetext += data;
       // if(data.length>0) {notetext+='\n';}
    }});
	jQuery.ajax({ url: ctx+"/eyeform/ProcedureBook.do?method=getNoteText&appointmentNo="+<%=aptNo%>, async:false, success: function(data){
        notetext += data;
        //if(data.length>0) {notetext+='\n';}
    }});
	jQuery.ajax({ url: ctx+"/eyeform/TestBook.do?method=getNoteText&appointmentNo="+<%=aptNo%>, async:false, success: function(data){
        notetext += data;
        //if(data.length>0) {notetext+='\n';}
    }});
	jQuery.ajax({ url: ctx+"/eyeform/NoteData.do?method=getNoteText&appointmentNo="+<%=aptNo%>, async:false, success: function(data){
        notetext += data;
       // if(data.length>0) {notetext+='\n';}
    }});

	
	var noteTa = document.getElementById('caseNote_note<%=request.getParameter("noteId") %>');
	var noteTaVal = noteTa.value;
	noteTaVal = noteTaVal + '\n' + notetext;
	noteTa.value = noteTaVal;
	
 }


function saveNoteAndSendTickler() {
	//alert("save function called for eyeform - " + savedNoteId);
	//do the ajax call to save form values 	
	saveFlags();

	var notetext = '';
	//get consults/procedures/tests/checkboxes to generate text
	jQuery.ajax({ url: ctx+"/eyeform/FollowUp.do?method=getTicklerText&appointmentNo="+<%=aptNo%>, async:false, success: function(data){
        notetext += data;
        if(data.length>0) {notetext+='\n';}
    }});
	jQuery.ajax({ url: ctx+"/eyeform/ProcedureBook.do?method=getTicklerText&appointmentNo="+<%=aptNo%>, async:false, success: function(data){
        notetext += data;
        if(data.length>0) {notetext+='\n';}
    }});
	jQuery.ajax({ url: ctx+"/eyeform/TestBook.do?method=getTicklerText&appointmentNo="+<%=aptNo%>, async:false, success: function(data){
        notetext += data;
        if(data.length>0) {notetext+='\n';}
    }});

	var ticklerRecip = document.getElementById('ticklerRecip');
	
	jQuery.ajax({ url: ctx+"/eyeform/NoteData.do?method=sendTickler&appointmentNo="+<%=aptNo%>+"&text="+notetext+"&recip=" + ticklerRecip.value +"&demographicNo=<%=demographicNo %>", async:false, success: function(data){
        alert('tickler sent');
    }});
	
 }


function saveFlags() {

	var ack1El = document.getElementById("ack1");
	var ack2El = document.getElementById("ack2");
	var ack3El = document.getElementById("ack3");
	
	jQuery.ajax({
		type: 'GET',
		url: ctx+'/eyeform/NoteData.do?method=save&ack1_checked=' + ack1El.checked + '&ack2_checked=' + ack2El.checked + '&ack3_checked=' + ack3El.checked + '&appointmentNo=' + <%=aptNo%> ,
		success: function (){},
		dataType: 'html'	
	});
	
	
 }

</script>

<style>
.plan td {
	font-size: 9px;
}
</style>

<span note_addon="saveEyeformNoteNoGenerate"></span>
<span><input type="button" onclick="popupPageOne('<c:out value="${ctx}"/>/eyeform/EyeformPlan.do?method=form&amp;followup.demographicNo=<%=demographicNo %>&amp;noteId=<%=noteId%>&amp;followup.appointmentNo=<%=aptNo%>','eyeFormPlan',600,1200);" value="Arrange Plan"/>
 <%
	oscar.OscarProperties props1 = oscar.OscarProperties.getInstance();
	String impression_history_show = props1.getProperty("cme_js", "eyeform3");
	if(("eyeform3".equals(impression_history_show)) || ("eyeform3.1".equals(impression_history_show)) ||( "eyeform3.2".equals(impression_history_show))){
%>
           <input value="Impression History" onclick="popupPage(500,900,'Impressionhistory1','<c:out value="${ctx}"/>/eyeform/Impression_History.jsp')" id="stickler1" style="color: black;" type="button"> 
   <%} %>
</span>

<table width="100%" class="plan">
<tr>
<td width="85%">
<table border="0">
           <tbody>
           
<tr>
<td colspan="3">
<div>
	<c:forEach items="${followUps}" var="item">
		<c:choose>
			<c:when test="${item.timespan == 0}">
			<span style="font-size:10pt"><c:out value="${item.typeStr}"/>&nbsp;Dr.&nbsp;<c:out value="${item.provider.firstName}"/>&nbsp;<c:out value="${item.provider.lastName}"/> | <c:out value="${item.urgency}"/> | <span title="<c:out value="${item.comment}"/>"><c:out value="${item.commentStr}"/></span></span>		
			</c:when>
			<c:otherwise>
		<span style="font-size:10pt"><c:out value="${item.typeStr}"/>&nbsp;<c:out value="${item.timespan}"/>&nbsp;<c:out value="${item.timeframe}"/>&nbsp;Dr.&nbsp;<c:out value="${item.provider.firstName}"/>&nbsp;<c:out value="${item.provider.lastName}"/> | <c:out value="${item.urgency}"/> | <span title="<c:out value="${item.comment}"/>"><c:out value="${item.commentStr}"/></span></span>
		</c:otherwise>
		</c:choose>
		<br/>
	</c:forEach>
</div>
</td>
</tr>
        
<tr>
<td colspan="3">
<div>
	
	<c:if test="${not empty procedures}"><span style="font-size:10pt;font-weight:bold">Procs:</span><br/></c:if>

	<c:forEach items="${procedures}" var="item">
		<span style="font-size:10pt"><c:out value="${item.eye}"/>&nbsp;<c:out value="${item.procedureName}"/> at <c:out value="${item.location}"/> | <c:out value="${item.urgency}"/> | <span title="<c:out value="${item.comment}"/>"><c:out value="${item.commentStr}"/></span></span><br/>
	</c:forEach>
 
</div>
</td>
</tr>

<tr>
<td colspan="3">
<div>

	<c:if test="${not empty testBookRecords}"><span style="font-size:10pt;font-weight:bold">Diags:</span><br/></c:if>

	<c:forEach items="${testBookRecords}" var="item">
		<span style="font-size:10pt"><c:out value="${item.eye}"/>&nbsp;<c:out value="${item.testname}"/> | <c:out value="${item.urgency}"/> | <span title="<c:out value="${item.comment}"/>"><c:out value="${item.commentStr}"/></span></span><br/>
	</c:forEach>


</div>
</td>
</tr>

<tr><td>&nbsp;</td></tr>
<%
	org.oscarehr.eyeform.model.EyeForm eyeform = (org.oscarehr.eyeform.model.EyeForm)request.getAttribute("eyeform");
	String a1c = (eyeform!=null&&eyeform.getDischarge()!=null&&eyeform.getDischarge().equals("true"))?"checked":"";
	String a2c = (eyeform!=null&&eyeform.getStat() != null&&eyeform.getStat().equals("true"))?"checked":"";
	String a3c = (eyeform!=null&&eyeform.getOpt() != null&&eyeform.getOpt().equals("true"))?"checked":"";	
%>


<tr>
<td colspan="3">
<div>
        <div>
            
            <table>
           <tbody><tr>
           
            <td nowrap="nowrap" width="40%">
            
           <input tabindex="302" value="Generate Note" onclick="saveEyeformNote();return false;" id="stickler0" style="color: black;" type="button">			
           &nbsp;
           <b>Send tickler to:</b>

            
            

	        <select name="front" tabindex="303" class="special2" id="ticklerRecip">
	        	<c:forEach var="item" items="${internalList}">
            		<option value="<c:out value="${item.providerNo}"/>"><c:out value="${item.formattedName}"/></option>
            	</c:forEach>           
	        </select>
       

    
           </td>
           <td width="15%" nowrap="nowrap">            
			<input tabindex="304" value="Send Tickler" onclick="saveNoteAndSendTickler();return false;" id="stickler" style="color: black;" type="button">
			 
            </td>
            <td width="45%"></td>
            
            </tr>
            </tbody></table>
        </div>

</div>
</td>
</tr>

           <!-- 
           <tr>
            <td nowrap="nowrap" width="40%">
            	 </td>
           </tr>
           -->
      </table>

</td>
<td width="15%" valign="top">
<table>
 <tr>
            <td nowrap="nowrap">          	 
				<%if(a1c.equals("checked")) {  %>
            	<input type="checkbox" style="width: 10%;" value="true" id="ack1" onchange="setDischarge()" checked="checked"/>
            	<%} else { %>
            	<input type="checkbox" style="width: 10%;" value="true" id="ack1" onchange="setDischarge()" />            	
            	<% } %>            
            	<span style="font-size:10pt">Discharge</span>
           </td>
           </tr>
           <tr>
           <td nowrap="nowrap">
           		<%if(a2c.equals("checked")) {  %>	 
            	<input type="checkbox" style="width: 10%;" id="ack2" value="true" onchange="setStat();" checked="checked"/>
            	<% } else { %>
            	<input type="checkbox" style="width: 10%;" id="ack2" value="true" onchange="setStat();"/>            	
            	<% } %>            	            	
            	<span style="font-size:10pt">STAT/PRN</span>
           </td>
           </tr>
           <tr>
           <td nowrap="nowrap">
           		<%if(a3c.equals("checked")) {  %>	 
            	<input type="checkbox" style="width: 10%;" value="true" id="ack3" onchange="setOpt();" checked="checked" />
            	<%} else { %>
            	<input type="checkbox" style="width: 10%;" value="true" id="ack3" onchange="setOpt();" />            	
            	<% } %>            	
            	<span style="font-size:10pt">optom routine</span>
            </td>
           
</tr>
</table>
</td>
</tr>
</table>

</span>



<div style="margin: 0px;">&nbsp;</div>
