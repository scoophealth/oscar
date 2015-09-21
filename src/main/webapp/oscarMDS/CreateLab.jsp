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
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page contentType="text/html" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lab Creator</title>
        <link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/calendar/calendar.css" title="win2k-cold-1" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar-setup.js"></script>
        
        <script src="<%=request.getContextPath()%>/js/jquery.js"></script>
		<script>
			jQuery.noConflict();
		</script>
	
		<script>
			function addTest() {
				var total = jQuery("#test_num").val();
				total++;
				jQuery("#test_num").val(total);
				jQuery.ajax({url:'CreateLabTest.jsp?id='+total,async:false, success:function(data) {
					  jQuery("#test_container").append(data);					 	
				}});		
			}
			
			function deleteTest(id) {	
				var testId = jQuery("input[name='test_"+id+".id']").val();
				jQuery("form[name='testForm']").append("<input type=\"hidden\" name=\"test.delete\" value=\""+testId+"\"/>");
				jQuery("#test_"+id).remove();	
				
			}
			
			function confirmSave() {
	        	var c = confirm("Are you sure you want to submit this lab to the system?");	
	        	return c;
	        }
			
		</script>
        
    </head>
    <body>
        <div>
        	
        	<form name="testForm" method="post" action="<%=request.getContextPath()%>/oscarMDS/SubmitLab.do?method=saveManage" onsubmit="return confirmSave();">
        	
        	<table width="100%">        	
			<tr><td valign="top">
            <fieldset>            	
                <legend>Laboratory Information</legend>
                <table>
	                <tr>
	                	<td><label>Lab Name:</label></td>
	                	<td>
	                		<select name="labname" id="labname">
	                			<option value="MDS">MDS</option>
	                			<option value="CML">CML</option>
	                			<option value="GDML">GDML</option>
	                		</select>
	                	</td>
	                </tr>                	               	                
	                <tr><td><label>Accession #</label></td><td><input type="text" name="accession" id="accession"/></td></tr>
                    <tr><td><label>Lab Req Date/Time:</label></td><td><input type="text" name="lab_req_date" id="lab_req_date"/><img src="<%=request.getContextPath()%>/images/cal.gif" id="lab_req_date_cal" /></td></tr>   
                </table>
            </fieldset>            
        	</td><td valign="top">
			
            <fieldset>
                <legend>Ordering Provider</legend>
                <table>
	                <tr><td><label>Billing #</label></td><td><input type="text" name="billingNo" id="billingNo"/></td></tr>
	                <tr><td><label>Last Name</label></td><td><input type="text" name="pLastname" id="pLastname"/></td></tr>
	                <tr><td><label>First Name</label></td><td><input type="text" name="pFirstname" id="pFirstname"/></td></tr>
	                <tr><td><label>CC</label></td><td><input type="text" name="cc" id="cc" size="50"/></td></tr>	                
                </table>
            </fieldset>
			</td></tr></table>
			
			
            <fieldset>            	
                <legend>Patient Information</legend>
                <table>
	                <tr>
	                	<td><label>Last Name:</label></td><td><input type="text" name="lastname" id="lastname"/></td>
	                	<td><label>First Name:</label></td><td><input type="text" name="firstname" id="firstname"/></td>
	                	<td><label>Sex:</label></td>
	                	<td><select  name="sex" id="sex">
	                    	<option value="M">M</option>
	                        <option value="F">F</option>
	                        </select>
	                    </td>
	                </tr>
	                <tr>
	               <td><label>DOB:</label></td><td><input type="text" name="dob" id="dob"/><img src="<%=request.getContextPath()%>/images/cal.gif" id="dob_cal" /></td>
	               <td><label>HIN:</label></td><td><input type="text" name="hin" id="hin"/></td>
	                
	                 <td><label>Phone:</label></td><td><input type="text" name="phone" id="phone"/></td></tr>
                </table>
            </fieldset>
			
			
			<b>Tests:</b>
			<br />
			<div id="test_container"></div>
			<input type="hidden" id="test_num" name="test_num" value="0"/>	
			<a href="#" onclick="addTest();">[ADD]</a>
			           
			<br/><br/>
			<input type="submit" value="Submit to OSCAR"/>
			</form>			           
        </div>
        

<script>
Calendar.setup({ inputField : "lab_req_date", ifFormat : "%Y-%m-%d %H:%M", showsTime :true, button : "lab_req_date_cal" });
Calendar.setup({ inputField : "dob", ifFormat : "%Y-%m-%d", showsTime :true, button : "dob_cal" });

</script>
    </body>
</html>
