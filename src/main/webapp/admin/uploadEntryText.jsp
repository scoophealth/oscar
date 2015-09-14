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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>"> 
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.WebUtils,org.oscarehr.common.service.AcceptableUseAgreementManager,org.oscarehr.common.model.Property" %>
<%
		
Property latestProperty = AcceptableUseAgreementManager.findLatestProperty();
String checked = "";
String fromDate = "";
String period = "year";
String duration = "1";

    		if(latestProperty != null && "aua_valid_from".equals(latestProperty.getName())){
    			checked= "checked";
    			fromDate = latestProperty.getValue();
             }else if(latestProperty != null){
             	String val = latestProperty.getValue();
             	if(val != null){
             		String[] splitVal = val.split(" ");
             		if(splitVal != null && splitVal.length == 2){
             			duration = splitVal[0];
             			period = splitVal[1];
             		}
             	}
             }
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title><bean:message key="admin.admin.uploadEntryTxt"/></title>
        <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
        <link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />

		<!-- main calendar program -->
		<script type="text/javascript" src="../share/calendar/calendar.js"></script>

		<!-- language for the calendar -->
		<script type="text/javascript" src="../share/calendar/lang/calendar-en.js"></script>

		<!-- the following script defines the Calendar.setup helper function, which makes adding a calendar a matter of 1 or 2 lines of code. -->
		<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>

		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css"/>
		<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>
		<script>
		$(function() {
		    $( document ).tooltip();
		  });
		</script>

    <body>
        <table class="MainTable">
            <tr class="MainTableTopRow">
                <td class="MainTableTopRowLeftColumn" width="175">&nbsp;</td>
                <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                <tr>
                    <td><bean:message key="admin.admin.uploadEntryTxt"/></td>
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
                <td class="MainTableLeftColumn" valign="top">&nbsp;</td>
                <td class="MainTableRightColumn">
                	<html:form action="/admin/uploadEntryText" method="POST" enctype="multipart/form-data">
                    
                	How long is agreement valid?<br>
                	
                	<select name="validDurationNumber">
                		<option value="1" <%=WebUtils.getSelectedString("1".equals(duration))%> >1</option>
                		<option value="2" <%=WebUtils.getSelectedString("2".equals(duration))%>>2</option>
                		<option value="3" <%=WebUtils.getSelectedString("3".equals(duration))%>>3</option>
                		<option value="4" <%=WebUtils.getSelectedString("4".equals(duration))%>>4</option>
                		<option value="5" <%=WebUtils.getSelectedString("5".equals(duration))%>>5</option>
                		<option value="6" <%=WebUtils.getSelectedString("6".equals(duration))%>>6</option>
                		<option value="7" <%=WebUtils.getSelectedString("7".equals(duration))%>>7</option>
                		<option value="8" <%=WebUtils.getSelectedString("8".equals(duration))%>>8</option>
                		<option value="9" <%=WebUtils.getSelectedString("9".equals(duration))%>>9</option>
                		<option value="10" <%=WebUtils.getSelectedString("10".equals(duration))%>>10</option>
                		<option value="11" <%=WebUtils.getSelectedString("11".equals(duration))%>>11</option>
                		<option value="12" <%=WebUtils.getSelectedString("12".equals(duration))%>>12</option>
                		<option value="13" <%=WebUtils.getSelectedString("13".equals(duration))%>>13</option>
                		<option value="14" <%=WebUtils.getSelectedString("14".equals(duration))%>>14</option>
                		<option value="15" <%=WebUtils.getSelectedString("15".equals(duration))%>>15</option>
                		<option value="16" <%=WebUtils.getSelectedString("16".equals(duration))%>>16</option>
                		<option value="17" <%=WebUtils.getSelectedString("17".equals(duration))%>>17</option>
                		<option value="18" <%=WebUtils.getSelectedString("18".equals(duration))%>>18</option>
                	</select>
                	
                	<select name="validDurationPeriod">
	                	<option value="year" <%=WebUtils.getSelectedString("year".equals(period))%>>Year</option>
	                	<option value="month" <%=WebUtils.getSelectedString("month".equals(period))%>>Month</option>
	                	<option value="weeks" <%=WebUtils.getSelectedString("weeks".equals(period))%>>Weeks</option>
	                	<option value="days" <%=WebUtils.getSelectedString("days".equals(period))%>>Days</option>
                	</select>
                	
                	<br>OR<br>
                	<input type="checkbox" name="validForever" value="forever" <%=checked%>> Forever with an agreement past 
                	<input name="foreverFrom" type="text" id="foreverFrom" value="<%=fromDate%>"/>
                	<img src="../images/cal.gif" id="foreverFrom_cal">
                	
                	
                	
                	<br>
                	<br>
                        Agreement file (txt file)<input type="file" name="importFile">
                        <span title="<bean:message key="global.uploadWarningBody"/>" style="vertical-align:middle;font-family:arial;font-size:20px;font-weight:bold;color:#ABABAB;cursor:pointer"><img border="0" src="../images/icon_alertsml.gif"/></span></span>
        
                        <br>
                        <input type="submit" value="<bean:message key="admin.admin.uploadEntryTxt"/>">
                    </html:form>
                </td>
            </tr>
            <% 
            Boolean error = (Boolean)request.getAttribute("error");                                
            if( error != null ) {
            %>
            <tr>
                <td class="MainTableLeftColumn" valign="top">&nbsp;</td>
                <td class="MainTableRightColumn">
                    <% if( error == true ) { %>
                    <span style="color:red;"><bean:message key="admin.admin.ErrorUploadEntryTxt"/></span>
                    <%}%>
                     <!--   span style="color:green;"><bean:message key="admin.admin.SuccessUploadEntryTxt"/></span -->
                    
                </td>
                
            </tr>
            <%}%>
            <tr>
                <td class="MainTableBottomRowLeftColumn">&nbsp;</td>
                <td class="MainTableBottomRowRightColumn" valign="top">
                <hr>
                <%if (AcceptableUseAgreementManager.hasAUA()){ %>
                	<div style="float:right;text-align:center;" id="auaText">
        					<div style="margin-left:auto; margin-right:auto; text-align:left; width:70%; padding:5px; border:2px groove black;"><%=AcceptableUseAgreementManager.getAUAText()%></div>
        			</div>
                <%}else{%>
                	No text
                <%} %>
                </td>
            </tr>
        </table>
        
        
        <script type="text/javascript">
		Calendar.setup({ inputField : "foreverFrom", ifFormat : "%Y-%m-%d %H:%M:%S", showsTime :true, button : "foreverFrom_cal", singleClick : true, step : 1 });
        </script>
    </body>
</html>
<%!

	

%>