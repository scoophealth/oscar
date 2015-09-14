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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ page import="java.util.*,java.io.*,oscar.oscarLab.ca.all.util.KeyPairGen"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
	String roleName$ = (String) session.getAttribute("userrole") + ","
			+ (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
        if(KeyPairGen.checkName(name)){
            message = "Failed: The oscar key pair has already been created";
            error = "true";
        }else if(KeyPairGen.createKeys(name, null).equals("success")){
            message = "Oscar key pair created successfully";
        }else{
            message = "Failed: Could not create the oscar key pair";
            error = "true";
        }
    }else{
        if(KeyPairGen.checkName(name)){
            message = "Failed: Key pair has already been created for the service '"+name+"'";
            error = "true";
        }else{
            String clientKey = KeyPairGen.createKeys(name, type);
            String oscarKey = KeyPairGen.getPublic();

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

<%@page import="org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Oscar - Key Pair Creation</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/css/OscarStandardLayout.css">
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
<form method='POST' action=''>
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
				<td style="text-align: right"><oscar:help keywords="1.6.11" key="app.top1"/> | <a
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
					<%@ include file="../../lab/CA/ALL/labOptions.jspf"%>
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
