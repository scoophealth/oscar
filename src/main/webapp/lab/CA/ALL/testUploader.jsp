<% 
if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
%><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%
String outcome = (String) request.getAttribute("outcome");
if(outcome != null){
    if(outcome.equals("success")){
%><script type="text/javascript">alert("Lab uploaded successfully");</script>
<%
    }else if(outcome.equals("uploaded previously")){
%><script type="text/javascript">alert("Lab has already been uploaded");</script>
<%    
    }else if(outcome.equals("exception")){
%><script type="text/javascript">alert("Exception uploading the lab");</script>
<%
    }else{
%><script type="text/javascript">alert("Failed to upload lab");</script>
<%
    }
}
%>


<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Lab Upload Utility</title>
<link rel="stylesheet" type="text/css" href="../../../share/css/OscarStandardLayout.css">
<script type="text/javascript" src="../../../share/javascript/Oscar.js"></script>
<script type="text/javascript">
            function selectOther(){                
                if (document.UPLOAD.type.value == "OTHER")
                    document.getElementById('OTHER').style.visibility = "visible";
                else
                    document.getElementById('OTHER').style.visibility = "hidden";                
            }
            function checkInput(){
                if (document.UPLOAD.lab.value ==""){
                    alert("Please select a lab for upload");
                    return false;
                }else if (document.UPLOAD.type.value == "OTHER" && document.UPLOAD.otherType.value == ""){
                    alert("Please specify the other message type");
                    return false;
                }else{
                    var lab = document.UPLOAD.lab.value;
                    var ext = lab.substring((lab.length - 3), lab.length);
                    if (ext != 'hl7' && ext != 'xml'){
                        alert("Error: The lab must be either a .xml or .hl7 file");
                        return false;
                    }
                }
                return true;
            }
        </script>
</head>

<body>
<form method='POST' name="UPLOAD" enctype="multipart/form-data"
	action='insideLabUpload.do'>
<table align="center" class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="175"><bean:message
			key="demographic.demographiceditdemographic.msgPatientDetailRecord" />
		</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Upload <!--i18n--></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a
					href="javascript:popupStart(300,400,'Help.jsp')"><bean:message
					key="global.help" /></a> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td><input type="submit" value="Upload the lab"
			onclick="return checkInput()"></td>
		<td>
		<table>
			<tr>
				<td>Please select the lab file:</td>
				<td><input type="file" name="importFile"></td>
			</tr>
			<tr>
				<td>Lab type:</td>
				<td><select name="type" onClick="selectOther()">
					<option value="CML">CML</option>
					<option value="GDML">GDML</option>
					<option value="ICL">ICL</option>
					<option value="MDS">MDS</option>
					<option value="PATHL7"
						<oscar:oscarPropertiesCheck property="PATHNET_LABS" value="yes">Selected</oscar:oscarPropertiesCheck>>EXCELLERIS</option>
					<option value="OTHER">Other</option>
                                        <option value="HHSEMR">HHS Emr Download</option>
                                        <option value="IHA">IHA</option>
				</select></td>
			</tr>
			<tr id="OTHER" style="visibility: hidden;">
				<td>Please specify the other lab type:</td>
				<td><input type="text" name="otherType"></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>

</body>
</html>
