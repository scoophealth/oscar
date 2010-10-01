<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>	
<script>
function setDischarge(){
	if (document.inputForm.ack1.checked==true){
		document.inputForm.dischargeFlag.value='true';
		document.inputForm.ack1.value='checked';
	}
	else {
		document.inputForm.dischargeFlag.value='false';
		document.inputForm.ack1.value='';
	}
}

function setStat(){
	if (document.inputForm.ack2.checked==true){
		document.inputForm.statFlag.value='true';
		document.inputForm.ack2.value='checked';
	}
	else {
		document.inputForm.statFlag.value='false';
		document.inputForm.ack2.value='';
	}
}

function setOpt(){
	if (document.inputForm.ack3.checked==true){
		document.inputForm.optFlag.value='true';
		document.inputForm.ack3.value='checked';
	}
	else {
		document.inputForm.optFlag.value='false';
		document.inputForm.ack3.value='';
	}


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
	jQuery.ajax({
		type: 'POST',
		url: ctx+'/eyeform/NoteData.do?method=save',
		data: jQuery("form[name='caseManagementEntryForm']").serialize(),
		success: function (){},
		dataType: 'html'	
	});
	
 }
</script>

<span note_addon="saveEyeformNote"></span>

<table border="0">
           <tbody><tr>
            <td width="25%">Follow up:

             <input name="followNo" value="4"  style="width: 25px;" class="special" type="text">
             <select name="followFrame" style="width: 50px;" class="special">
             	<option value="days" selected="selected">days</option>
            	<option value="weeks">weeks</option>
            	<option value="months">months</option>
            </select>
            </td> 
			<td width="45%">
            Doctor:
            
            <select property="internalNo">
            	<c:forEach var="item" items="${internalList}">
            		<option value="<c:out value="${item.providerNo}"/>"><c:out value="${item.formattedName}"/></option>
            	</c:forEach>            	
			</select>
	  		
            </td>
            <td>
            	
            	 
            	<input tabindex="236" style="width: 10%;" value="" id="ack1" name="ack1" onchange="setDischarge()" type="checkbox">
            	
            	discharge
           <br>
            
            	 
            	<input tabindex="237" style="width: 10%;" checked="checked" id="ack2" value="checked" name="ack2" onchange="setSaveflag(true);setStat();" type="checkbox">
            	
            	
            	STAT/PRN
           <br>

            
            	
            	 
            	<input tabindex="240" style="width: 10%;" value="" id="ack3" name="ack3" onchange="setOpt();" type="checkbox">
            	
            	optom routine
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
				Integer noteId = (Integer)request.getAttribute("noteId");
			%>            
            <a href="javascript:void(0)" onclick="popupPageOne('<c:out value="${ctx}"/>/eyeform/ProcedureBook.do?method=form&amp;appNo=4&amp;demographicNo=1&amp;noteId=<%=noteId%>','eyeProbook');">[arrange procedure]</a>
            &nbsp;            	
        </div>

        <div>
            Nothing found to display.
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
            
            <a href="javascript:void(0)" onclick="popupPageOne('<c:out value="${ctx}"/>/eyeform/TestBook.do?method=form&amp;appNo=4&amp;demographicNo=1','eyeProbook');">[arrange test]</a>
            	&nbsp;
            	
        </div>
        <div>
            Nothing found to display.
        </div>

</div>
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
            
			<input tabindex="251" value="send tickler" onclick="markTickler();touchColor();AjaxSubmitAction('sendEyeTickler');return false;" id="stickler" style="color: black;" type="submit">
			 
            </td>
            <td width="45%"></td>
            
            </tr>
            </tbody></table>
        </div>

</div>
</td>
</tr>

           
           
      </table>


</span>



<div style="margin: 0px;">&nbsp;</div>
