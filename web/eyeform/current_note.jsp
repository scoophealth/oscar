<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>


<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

<%
				Integer noteId = (Integer)request.getAttribute("noteId");
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


</script>

<span style="float: left;">
<script>
function saveEyeformNote() {
	//alert("save function called for eyeform - " + savedNoteId);
	//do the ajax call to save form values 
	saveFlags();

	var notetext = '';
	//get consults/procedures/tests/checkboxes to generate text
	jQuery.ajax({ url: ctx+"/eyeform/FollowUp.do?method=getNoteText&appointmentNo="+<%=aptNo%>, async:false, success: function(data){
        notetext += data;
        if(data.length>0) {notetext+='\n';}
    }});
	jQuery.ajax({ url: ctx+"/eyeform/ProcedureBook.do?method=getNoteText&appointmentNo="+<%=aptNo%>, async:false, success: function(data){
        notetext += data;
        if(data.length>0) {notetext+='\n';}
    }});
	jQuery.ajax({ url: ctx+"/eyeform/TestBook.do?method=getNoteText&appointmentNo="+<%=aptNo%>, async:false, success: function(data){
        notetext += data;
        if(data.length>0) {notetext+='\n';}
    }});
	jQuery.ajax({ url: ctx+"/eyeform/NoteData.do?method=getNoteText&appointmentNo="+<%=aptNo%>, async:false, success: function(data){
        notetext += data;
        if(data.length>0) {notetext+='\n';}
    }});

	alert(notetext);
 }


function saveNoteAndSendTickler() {
	//alert("save function called for eyeform - " + savedNoteId);
	//do the ajax call to save form values 	
	saveFlags();
	
	
	
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

<span note_addon="saveEyeformNote"></span>

<table border="0">
           <tbody>
           
<tr>
<td colspan="3">
<div>

        <div>
            
            <b>Follow Up/Consult:</b>
            
            
            <span>&nbsp;&nbsp;</span>
			   
            <a href="javascript:void(0)" onclick="popupPageOne('<c:out value="${ctx}"/>/eyeform/FollowUp.do?method=form&amp;followup.demographicNo=<%=demographicNo %>&amp;noteId=<%=noteId%>&amp;followup.appointmentNo=<%=aptNo%>','eyeFollowUp');">[arrange]</a>
            &nbsp;            	
        </div>

        <div>
            <display:table name="requestScope.followUps"
            requestURI="/EyeForm.do" class="display" id="tb" pagesize="5">

                <display:setProperty name="paging.banner.one_item_found" value=""/>
                <display:setProperty name="paging.banner.all_items_found" value=""/>
                <display:setProperty name="paging.banner.some_items_found" value=""/>
                
                <display:column title="Time Span">
                                        <c:out value="${tb.timespan}"/>
                                </display:column>
                                <display:column title="Time Frame">
                                <c:out value="${tb.timeframe}"/>
                                </display:column>
                                <display:column title="Date">
                                <c:out value="${tb.date}"/>
                                </display:column>
                                <display:column title="Provider">
                                <c:out value="${tb.provider.formattedName}"/>
                                </display:column>
                               
			</display:table>           
        </div>
</div>
</td>
</tr>

           </tbody>
    

<% //book procedure, book test %>

<tr><td>&nbsp;</td></tr>
<tr>
<td colspan="3">
<div>

        <div>
            
            <b>Book Procedure:</b>
            
            
            <span>&nbsp;&nbsp;</span>
			<%
				noteId = (Integer)request.getAttribute("noteId");
			%>            
            <a href="javascript:void(0)" onclick="popupPageOne('<c:out value="${ctx}"/>/eyeform/ProcedureBook.do?method=form&amp;data.appointmentNo=<%=aptNo%>&amp;data.demographicNo=<%=demographicNo %>&amp;noteId=<%=noteId%>','eyeProbook');">[arrange procedure]</a>
            &nbsp;            	
        </div>

        <div>
			<display:table name="requestScope.procedures"
            	requestURI="/EyeForm.do" class="display" id="tb" pagesize="5">

                <display:setProperty name="paging.banner.one_item_found" value=""/>
                <display:setProperty name="paging.banner.all_items_found" value=""/>
                <display:setProperty name="paging.banner.some_items_found" value=""/>
                
                 <display:column title="Procedure">
                 	<c:out value="${tb.procedureName}"/>
                 </display:column>
				<display:column title="Eye" >
		      		<c:out value="${tb.eye}"/>
				</display:column>
				<display:column title="Urgency" >
		      		<c:out value="${tb.urgency}"/>
				</display:column>
				<display:column title="Location" >
		      		<c:out value="${tb.location}"/>
				</display:column>
				<display:column title="comment" >
		      		<c:out value="${tb.comment}"/>
				</display:column>
				                 
            </display:table>            
        </div>
</div>
</td>
</tr>
<tr><td>&nbsp;</td></tr>
<tr>
<td colspan="3">
<div>
        <div>
            

            <b>Book Test:</b>

           
            <span>&nbsp;&nbsp;</span>
            
            <a href="javascript:void(0)" onclick="popupPageOne('<c:out value="${ctx}"/>/eyeform/TestBook.do?method=form&amp;data.appointmentNo=<%=aptNo%>&amp;data.demographicNo=<%=demographicNo %>','eyeTestbook');">[arrange test]</a>
            	&nbsp;
            	
        </div>
        <div>
			<display:table name="requestScope.testBookRecords"
            	requestURI="/EyeForm.do" class="display" id="tb2" pagesize="5">

                <display:setProperty name="paging.banner.one_item_found" value=""/>
                <display:setProperty name="paging.banner.all_items_found" value=""/>
                <display:setProperty name="paging.banner.some_items_found" value=""/>
                                 
            	<display:column title="Test Name"  >
			   		<c:out value="${tb2.testname}"/>
				</display:column>
				<display:column title="Eye" >
		      		<c:out value="${tb2.eye}"/>
				</display:column>
				<display:column title="Urgency" >
		      		<c:out value="${tb2.urgency}"/>
				</display:column>
				<display:column title="comment" >
		      		<c:out value="${tb2.comment}"/>
				</display:column>
            </display:table>                    
        </div>

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
            <td>          	 
				<%if(a1c.equals("checked")) {  %>
            	<input type="checkbox" style="width: 10%;" value="true" id="ack1" onchange="setDischarge()" checked="checked"/>
            	<%} else { %>
            	<input type="checkbox" style="width: 10%;" value="true" id="ack1" onchange="setDischarge()" />            	
            	<% } %>            
            	Discharge            	            
           <br>   
           		<%if(a2c.equals("checked")) {  %>	 
            	<input type="checkbox" style="width: 10%;" id="ack2" value="true" onchange="setStat();" checked="checked"/>
            	<% } else { %>
            	<input type="checkbox" style="width: 10%;" id="ack2" value="true" onchange="setStat();"/>            	
            	<% } %>            	            	
            	STAT/PRN
           <br>
           		<%if(a3c.equals("checked")) {  %>	 
            	<input type="checkbox" style="width: 10%;" value="true" id="ack3" onchange="setOpt();" checked="checked" />
            	<%} else { %>
            	<input type="checkbox" style="width: 10%;" value="true" id="ack3" onchange="setOpt();" />            	
            	<% } %>            	
            	optom routine
            </td>
</tr>


<tr><td>&nbsp;</td></tr>

<tr>
<td colspan="3">
<div>
        <div>
            
            <table>
           <tbody><tr>
            <td nowrap="nowrap" width="40%"><b>Send tickler to:</b>

            
            

	        <select name="front" tabindex="250" class="special2">
	        	<c:forEach var="item" items="${internalList}">
            		<option value="<c:out value="${item.providerNo}"/>"><c:out value="${item.formattedName}"/></option>
            	</c:forEach>           
	        </select>
       

    
           </td>
           <td width="15%">
            
			<input tabindex="251" value="Send Tickler" onclick="saveNoteAndSendTickler();return false;" id="stickler" style="color: black;" type="submit">
			 
            </td>
            <td width="45%"></td>
            
            </tr>
            </tbody></table>
        </div>

</div>
</td>
</tr>

           
           <tr>
            <td nowrap="nowrap" width="40%">
            	<input tabindex="251" value="Generate Note" onclick="saveEyeformNote();return false;" id="stickler0" style="color: black;" type="button">			
            </td>
           </tr>
           
      </table>


</span>



<div style="margin: 0px;">&nbsp;</div>
