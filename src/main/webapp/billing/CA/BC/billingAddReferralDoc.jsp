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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page
	import="java.util.*,oscar.oscarBilling.ca.bc.data.*,oscar.oscarBilling.ca.bc.pageUtil.*,org.apache.commons.beanutils.*"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Billingreferral" %>
<%@page import="org.oscarehr.common.dao.BillingreferralDao" %>
<%
	BillingreferralDao billingReferralDao = (BillingreferralDao)SpringUtils.getBean("BillingreferralDAO");
%>

<%@page import="org.oscarehr.util.MiscUtils"%><html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Manage Referral Docs</title>
<link rel="stylesheet" type="text/css"
	href="../../../oscarEncounter/encounterStyles.css">
<script type="text/javascript">

function isNumeric(strString){
    var validNums = "0123456789.";
    var strChar;
    var retval = true;

    for (i = 0; i < strString.length && retval == true; i++){
       strChar = strString.charAt(i);
       if (validNums.indexOf(strChar) == -1){
          retval = false;
       }
    }
     return retval;
}

function checkUnits(){
	if  (!isNumeric(document.BillingAddCodeForm.value.value)){
		alert("Price has to be a numeric value");
	        document.BillingAddCodeForm.value.focus();
		return false;
	}
	return true;
}

function checkBillingNumber(){
    //alert(">"+document.AddReferralDocForm.referral_no.value+"<");
    if( document.AddReferralDocForm.referral_no.value.length == 0){
        alert("You must enter a Billing Number");
        return false;
    }else if  (!isNumeric(document.AddReferralDocForm.referral_no.value)){
        alert("Billing Number has to be a numeric value");
	document.AddReferralDocForm.referral_no.value.focus();
        return false;
    }else if( document.AddReferralDocForm.referral_no.value.length != 5){
       if( document.AddReferralDocForm.referral_no.value.length < 5){
          //need to addzeros
	  document.AddReferralDocForm.referral_no.value = forwardZero(document.AddReferralDocForm.referral_no.value, 5);
       }else{
	  alert("Billing Number must be digits");
          return false;
       }

    }

	return true;
}

function forwardZero(str, len) {
        returnZeroValue = "";
        for(var i= str.length; i < len; i++) {
            returnZeroValue += "0";
        }
        //return cutFrontString(returnZeroValue+y,x);
        return (returnZeroValue+str);
    }

</script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">billing</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Manage Referral Billing</td>
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
		<td class="MainTableLeftColumn" valign="top">&nbsp; &nbsp;</td>
		<td class="MainTableRightColumn">
		<% if (request.getAttribute("Error") != null){ %> <span
			style="font: bold 15pt sans-serif; color: red;"><%=request.getAttribute("Error") %></span>
		<% }%> <html:form action="/billing/CA/BC/AddReferralDoc"
			onsubmit="return checkBillingNumber();">

			<%
            String id = request.getParameter("id");
            if ( id != null ){
                try{
                  AddReferralDocForm frm = (AddReferralDocForm) request.getAttribute("AddReferralDocForm");
                 Billingreferral billingReferral = billingReferralDao.getById(Integer.parseInt(id));
                 if(billingReferral!=null) {
                	 frm.setReferral_no(billingReferral.getReferralNo());
                	 frm.setAddress1(billingReferral.getAddress1());
                	 frm.setAddress2(billingReferral.getAddress2());
                	 frm.setCity(billingReferral.getCity());
                	 frm.setFax(billingReferral.getFax());
                	 frm.setFirst_name(billingReferral.getFirstName());
                	 frm.setLast_name(billingReferral.getLastName());
                	 frm.setPhone(billingReferral.getPhone());
                	 frm.setPostal(billingReferral.getPostal());
                	 frm.setProvince(billingReferral.getProvince());
                	 frm.setSpecialty(billingReferral.getSpecialty());
                 }
                }catch(Exception e){
                	MiscUtils.getLogger().error("Error", e);
                }%>
			<input type="hidden" name="id" value="<%=id%>" />
			<%
            }
           %>

			<fieldset><legend><%=(id == null)?"Add":"Update"%>
			Referral Doctor</legend> <label for="referral_no">Billing #:</label> <html:text
				property="referral_no" /><br>
			<label for="last_name">Last Name:</label><html:text
				property="last_name" /><b>First Name:</b><html:text
				property="first_name" /> <br />
			<label for="specialty">Specialty:</label> <html:text
				property="specialty" /></br>
			<label for="address1">Address 1:</label> <html:text
				property="address1" size="30" /><br />
			<label for="address2">Address 2:</label> <html:text
				property="address2" size="30" /><br />
			<label for="city">City:</label> <html:text property="city" /><b>Province:</b><html:text
				property="province" /><br />
			<label for="postal">Postal:</label><html:text property="postal" /><br />
			<label for="phone">Phone:</label> <html:text property="phone" /><b>Fax:<b /><html:text
				property="fax" /><br />




			<input type="submit" value="Save" /> <input type="button"
				value="Cancel"
				onclick="window.location = 'billingManageReferralDoc.jsp';" /></fieldset>
		</html:form></td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
