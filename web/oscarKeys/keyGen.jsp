
<% 

%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page
	import="java.util.*,
java.io.*,
oscar.oscarLab.ca.all.util.KeyPairGen"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<% 
String name = request.getParameter("name");
String type = request.getParameter("type");
if (type != null && type.equals("OTHER"))
    type = request.getParameter("otherType");

String message = null;
String failDisplay = "none";

String error = "false";

if (name != null){
    if (name.equals("oscar")){
        KeyPairGen k = new KeyPairGen();
        if(k.checkName(name)){
            message = "Failed: The oscar key pair has already been created";
            error = "true";
        }else if(k.createKeys(name, null).equals("success")){
            message = "Oscar key pair created successfully";
        }else{
            message = "Failed: Could not create the oscar key pair";
            error = "true";
        }
    }else{

        KeyPairGen k = new KeyPairGen();
        if(k.checkName(name)){
            message = "Failed: Key pair has already been created for the service '"+name+"'";
            error = "true";
        }else{
            String clientKey = k.createKeys(name, type);
            String oscarKey = k.getPublic();

            if (clientKey == null){
                message = "Failed: Could not create key pair";
                error = "true";
            }else if(oscarKey == null){
                message = "Failed: Could not retrieve public key from oscar";
                error = "true";
            }else{

                String keyPairOut = "-------- Service Name --------\n"+name+"\n------------------------------\n"+
                        "----- Client Private Key -----\n"+clientKey+"\n------------------------------\n"+
                        "------ Oscar Public Key ------\n"+oscarKey+"\n------------------------------";
                response.setContentType("text");
                response.setContentLength(keyPairOut.length());
                response.setHeader("Content-Disposition","attachment; filename=keyPair.key");
                ServletOutputStream output = null;

                try{
                    output = response.getOutputStream();
                    output.print(keyPairOut);
                    output.flush();
                }catch(IOException e){
                    message = "Failed: Could not save key pair";
                    error = "true";
                }finally{
                    if (output != null){
                        try{
                            output.close();
                        }catch(IOException e){
                            message = "Failed: Could not close output stream";
                            error = "true";
                        }
                    }
                }
            }
        }
    }
}

if (message != null){
    failDisplay = "block";
}
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Oscar - Key Pair Creation</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<script type="text/javascript">
            function selectOther(){                
                if (document.getElementById('selection').value == "OTHER")
                    document.getElementById('OTHER').style.visibility = "visible";
                else
                    document.getElementById('OTHER').style.visibility = "hidden";                
            }
            function checkInput(){
                
                if (document.getElementById('name').value ==""){
                    alert("Please enter a service name");
                    return false;
                }else if (document.getElementById('selection').value == "OTHER" && document.getElementById('otherType').value == ""){
                    alert("Please specify the other message type");
                    return false;
                }
                document.getElementById('success').style.display = "block";
                document.getElementById('fail').style.display = "none";
                return true;
            }
        </script>
</head>
<body>
<form method='POST' action='keyGen.jsp'>
<table align="center" class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="175">Key Pair
		Creation</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>
				<div id="success" style="display: none;">Key pair created
				successfully</div>
				<div id="fail" style="display:<%= failDisplay %>;">
				<%
                                        if (message != null){
                                            if(error.equals("false")){
                                                out.print(message);
                                            }else{
                                                %><font color="red"><%= message %></font>
				<%
                                            }
                                        }
                                        // reset the message after it has been used
                                        message = null;
                                        %>
				</div>
				</td>
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
		<td><input type="submit" value="Create Key Pair"
			onclick="return checkInput()"></td>
		<td>
		<table>
			<tr>
				<td>Your service name:</td>
				<td><input type="text" id="name" name="name"></td>
			</tr>
			<tr>
				<td>Lab type:</td>
				<td><select name="type" id="selection" onClick="selectOther()">
					<option value="CML">CML</option>
					<option value="GDML">GDML</option>
					<option value="ICL">ICL</option>
					<option value="MDS">MDS</option>
					<option value="PATHL7">EXCELLERIS</option>
					<option value="OTHER">Other</option>
				</select></td>
			</tr>
			<tr id="OTHER" style="visibility: hidden;">
				<td>Please specify the other message type:</td>
				<td><input type="text" id="otherType" name="otherType"></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
