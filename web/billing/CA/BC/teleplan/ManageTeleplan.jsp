<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
     * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity test2
 * Hamilton 
 * Ontario, Canada 
 */
-->
<%@page
	import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarBilling.ca.bc.Teleplan.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<% TeleplanUserPassDAO dao = new TeleplanUserPassDAO(); %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>oscarPrevention</title>
<!--I18n-->
<html:base />
<link rel="stylesheet" type="text/css"
	href="../../../../share/css/OscarStandardLayout.css" />
<script type="text/javascript"
	src="../../../../share/javascript/Oscar.js"></script>
<script type="text/javascript"
	src="../../../../share/javascript/prototype.js"></script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<link rel="stylesheet" type="text/css"
	href="../../../../share/css/niftyCorners.css" />
<link rel="stylesheet" type="text/css"
	href="../../../../share/css/niftyPrint.css" media="print" />
<link rel="stylesheet" type="text/css" href="preventPrint.css"
	media="print" />
<script type="text/javascript"
	src="../../../../share/javascript/nifty.js"></script>
<script type="text/javascript">
window.onload=function(){
if(!NiftyCheck())
    return;

//Rounded("div.news","all","transparent","#FFF","small border #999");
Rounded("div.headPrevention","all","#CCF","#efeadc","small border blue");
Rounded("div.preventionProcedure","all","transparent","#F0F0E7","small border #999");

Rounded("div.leftBox","top","transparent","#CCCCFF","small border #ccccff");
Rounded("div.leftBox","bottom","transparent","#EEEEFF","small border #ccccff");

}

function display(elements) {

    for( var idx = 0; idx < elements.length; ++idx )
        elements[idx].style.display = 'block';
}

function EnablePrint(button) {
    if( button.value == "Enable Print" ) {
        button.value = "Print";        
        var checkboxes = document.getElementsByName("printHP");
        display(checkboxes);
        var spaces = document.getElementsByName("printSp");
        display(spaces);
    }
    else { 
        if( onPrint() )
            document.printFrm.submit();
    }
}

function onPrint() {    
    var checked = document.getElementsByName("printHP");    
    var thereIsData = false;
    
    for( var idx = 0; idx < checked.length; ++idx ) {
        if( checked[idx].checked ) {
            thereIsData = true;
            break;
        }
    }
        
    if( !thereIsData ) {   
        alert("You should check at least one prevention by selecting a checkbox next to a prevention");
        return false;
    }
    
    return true;
}
</script>




<script type="text/javascript">
<!--
//if (document.all || document.layers)  window.resizeTo(790,580);
function newWindow(file,window) {
  msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
  if (msgWindow.opener == null) msgWindow.opener = self;
} 
//-->
</script>




<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

</head>

<body class="BodyStyle">
<!--  -->

<table class="MainTable" id="scrollNumber1">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">teleplan</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>teleplan <%
                            TeleplanSequenceDAO seq = new TeleplanSequenceDAO();
                            oscar.OscarProperties op = oscar.OscarProperties.getInstance();
                            %> Last Sequence # = <%=seq.getLastSequenceNumber()%>
				Current Datacenter # = <%=op.getProperty("dataCenterId","Not Set")%>
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
		<td class="MainTableLeftColumn" valign="top">&nbsp;</td>
		<td valign="top" class="MainTableRightColumn">
		<%if (request.getAttribute("error") != null) { %>
		<div><%=request.getAttribute("error")%></div>
		<%}%> <oscar:oscarPropertiesCheck property="BILLING_SUPERUSER"
			value="<%=""+session.getAttribute("user")%>">
			<div class="leftBox">
			<h3>&nbsp;Manually Set Sequence #</h3>
			<div style="background-color: #EEEEFF;"><html:form
				action="/billing/CA/BC/ManageTeleplan">
				<input type="hidden" name="method" value="setSequenceNumber" /> 
                                Sequence #: <input type="text"
					name="num" />
				<input type="submit" />
			</html:form></div>
			</div>
		</oscar:oscarPropertiesCheck> <oscar:oscarPropertiesCheck property="BILLING_SUPERUSER"
			value="<%=""+session.getAttribute("user")%>">

			<div class="leftBox">
			<h3>&nbsp;Set Teleplan UserName Password</h3>
			<div style="background-color: #EEEEFF;"><html:form
				action="/billing/CA/BC/ManageTeleplan">
				<input type="hidden" name="method" value="setUserName" />
                         Username: <input type="text" name="user" />
                         Password: <input type="password" name="pass" />
				<input type="submit" />
			</html:form></div>
			</div>
		</oscar:oscarPropertiesCheck> <% if ( dao.hasUsernamePassword()){ %> <oscar:oscarPropertiesCheck
			property="BILLING_SUPERUSER"
			value="<%=""+session.getAttribute("user")%>">

			<div class="leftBox">
			<h3>&nbsp;Get Teleplan Sequence #</h3>
			<div style="background-color: #EEEEFF;"><html:form
				action="/billing/CA/BC/ManageTeleplan">
				<input type="hidden" name="method" value="getSequenceNumber" />
				<input type="submit" />
			</html:form></div>
			</div>
		</oscar:oscarPropertiesCheck>

		<div class="leftBox">
		<h3>&nbsp;Update Billing Codes</h3>
		<div style="background-color: #EEEEFF;"><html:form
			action="/billing/CA/BC/ManageTeleplan">
			<input type="hidden" name="method" value="updateBillingCodes" />
			<input type="submit" />
		</html:form></div>
		</div>




		<div class="leftBox">
		<h3>&nbsp;Change Teleplan Password</h3>
		<div style="background-color: #EEEEFF;"><html:form
			action="/billing/CA/BC/ManageTeleplan">
			<input type="hidden" name="method" value="changePass" />
                         Current Password: <input type="password"
				name="oldpass" />
			<br>
                         New Password:     <input type="password"
				name="newpass" />
                         Confirm Password: <input type="password"
				name="confpass" />
			<input type="submit" />
		</html:form></div>
		</div>

		<div class="leftBox">
		<h3>&nbsp;Set Teleplan Password</h3>
		<div style="background-color: #EEEEFF;"><html:form
			action="/billing/CA/BC/ManageTeleplan">
			<input type="hidden" name="method" value="setPass" />
                         New Password:     <input type="password"
				name="newpass" />
			<input type="submit" />
		</html:form></div>
		</div>


		<div class="leftBox">
		<h3>&nbsp;Get Remittance</h3>
		<div style="background-color: #EEEEFF;"><html:form
			action="/billing/CA/BC/ManageTeleplan">
			<input type="hidden" name="method" value="remit" />
			<input type="submit" />
		</html:form></div>
		</div>
		<%}%>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"><input type="button"
			class="noPrint" name="printButton" onclick="EnablePrint(this)"
			value="Enable Print"> &nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>


</form>
</body>
</html:html>