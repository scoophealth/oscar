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
<!DOCTYPE html>
<%@ page import="oscar.eform.data.*, oscar.eform.*, java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
String status = (String) request.getAttribute("status");    		
%>
<html:html locale="true">

<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>

<script>
$(function() {
    $( document ).tooltip();
  });
</script>

</head>

<style>
body{background-color:#f5f5f5;}

.uploadEformTitle{display:inline-block;}
</style>

<body>

<%if(status != null){ %>
    <div class="alert alert-success">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <strong>Success!</strong> Your eform was uploaded.
    </div>

<script>
window.top.location.href = "<%=request.getContextPath()%>/administration/?show=Forms";
</script>

<%}else{%>

<html:form action="/eform/uploadHtml" method="POST" onsubmit="return checkFormAndDisable()" enctype="multipart/form-data">
<div class="alert alert-error" style="display:none"> <html:errors /> </div>
                                       
                                        <div class='uploadEformTitle'>
                                        <bean:message key="eform.uploadhtml.formName" /> <span class="text-error textExists" style='display:none;'>Name already exists</span><br>
                                        <input type="text" name="formName" size="30" class="check" required>
                                        </div>
                                        
                                        <div class='uploadEformTitle'>
                                        <bean:message key="eform.uploadhtml.formSubject" /><br>
                                        <input type="text" name="formSubject" size="30">
                                        </div>
                                        
                                        <div class='uploadEformTitle'>
                                        <bean:message key="eform.uploadhtml.btnRoleType"/><br>
                                        <select name="roleType">
                                        <option value="" >- select one (optional) -</option>
                                       <%  ArrayList roleList = EFormUtil.listSecRole();
  											for (int i=0; i<roleList.size(); i++) {    
  										%>  											
                                        	
                                        		<option value="<%=roleList.get(i) %>"><%=roleList.get(i) %></option>
                                        	
                                        <%} %>
                                        </select>
                                        </div>
                                        
                                   
                                        <div class='uploadEformTitle'>
                                        <bean:message key="eform.uploadhtml.btnProgram"/><br>
                                        <select name="programNo">
                                        <option value="" >- select one (optional) -</option>
                                       <%  List<org.oscarehr.PMmodule.model.Program> pList = EFormUtil.listPrograms();
  											for (int i=0; i<pList.size(); i++) {    
  										%>  											
                                        	
                                        		<option value="<%=pList.get(i).getId() %>"><%=pList.get(i).getName() %></option>
                                        	
                                        <%} %>
                                        </select>
                                        </div>
                                        
                                        <div class='uploadEformTitle'>
                                        <input type="checkbox" name="showLatestFormOnly" value="true"/><bean:message key="eform.uploadhtml.showLatestFormOnly"/><br>
                                        <input type="checkbox" name="patientIndependent" value="true"/><bean:message key="eform.uploadhtml.patientIndependent"/><br/>
                                        <input type="checkbox" name="restrictByProgram" value="true"/><bean:message key="eform.uploadhtml.restrictByProgram"/><br/>
                                        <input type="checkbox" name="disableUpdate" value="true"/><bean:message key="eform.uploadhtml.disableUpdate"/>
                                        </div>
                                        
                                      
                                        
                                        <input type="file" name="formHtml" id="formHtml" class="check" size="50" required>
                                        <span title="<bean:message key="global.uploadWarningBody"/>" style="vertical-align:middle;font-family:arial;font-size:20px;font-weight:bold;color:#ABABAB;cursor:pointer"><img border="0" src="../../images/icon_alertsml.gif"/></span></span>
                                        
                                        
                                        
                                        <input type="submit" name="subm" class="btn btn-primary upload" value="<bean:message key="eform.uploadhtml.btnUpload"/>" disabled>
               
</html:form>
<%}%>
<div style="font-size:0%; line-height:0%">&nbsp;</div>

<script src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>  

<script>
$( document ).ready(function() {
$(".check").change(validate).keyup(validate);
});

function validate()
{
var v = $(this).val();
var id = $(this).attr("id");
var formHtml = $("#formHtml").val();

var inputCheck=checkRow(v);

if (v!="" && inputCheck=="" && formHtml!="") {
	    $('.upload').removeAttr("disabled");
		$('.upload').addClass("btn-success");
		$('.textExists').hide();
    } else if(inputCheck=="exists"){
	    $('.upload').attr("disabled", "disabled");
		$('.upload').removeClass("btn-success");
		$('.textExists').show();
    }else{
	    $('.upload').attr("disabled", "disabled");
		$('.upload').removeClass("btn-success");
		$('.textExists').hide();
    } 
}

function checkRow(textInput)
{
var result="";
window.parent.$('#eformTbl tbody').find('tr').each(function(){

    if($('td:nth(1)',$(this)).attr("title")===textInput){
	result="exists";
        return false;
    }
});

return result
}
</script>
</body>
</html:html>
