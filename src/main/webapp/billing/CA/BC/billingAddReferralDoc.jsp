<!DOCTYPE html>
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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

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
<title><bean:message key="admin.admin.ManageReferralDoc"/></title>


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
</head>

<body>
	<h3><bean:message key="admin.admin.ManageReferralDoc"/></h3>

	<div class="container-fluid well">
		<% if (request.getAttribute("Error") != null){ %> 
		<div class="alert alert-error">
		 <button type="button" class="close" data-dismiss="alert">&times;</button>
		 <%=request.getAttribute("Error") %>
		</div>
		<% }%> 

	<html:form action="/billing/CA/BC/AddReferralDoc" onsubmit="return checkBillingNumber();" styleId="addReferralDocform">

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

			<fieldset>
				<legend><%=(id == null)?"Add":"Update"%> Referral Doctor</legend> 
				Billing #:<html:text property="referral_no" /><br>
				Last Name:<html:text property="last_name"/>	First Name:<html:text property="first_name" /><br />
				Specialty:<html:text property="specialty" /></br>
				Address 1:<html:text property="address1" size="30" /><br />
				Address 2:<html:text property="address2" size="30" /><br />
				City:<html:text property="city" />
				Province:<html:text property="province" /><br />
				Postal:<html:text property="postal" /><br />
				Phone:<html:text property="phone" />
				Fax:<html:text property="fax" /><br />
				<input class="btn btn-primary" type="submit" value="Save" />
			</fieldset>
		</html:form>
	</div>
<script>
registerFormSubmit('addReferralDocform', 'dynamic-content');
</script>
</body>
</html:html>
