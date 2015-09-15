<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />

<%
	String formClass = "Invoice";
	String formLink = "formInvoice.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();

	//get project_home
	String project_home = request.getContextPath().substring(1);
	//get provider name
	String providerName = providerBean.getProperty(""+provNo, "");
	String[] temp = providerName.split(",");
	if(temp.length>1) {
		providerName = temp[1] + " " + temp[0];
	}

	if(props!=null) {
		if(!props.getProperty("provider_no", ""+provNo).equals(""+provNo)) return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Personal Invoice</title>
<link rel="stylesheet" type="text/css" href="bcArStyle.css">
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />

<!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>

<!-- language for the calendar -->
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<html:base />
</head>

<script type="text/javascript" language="Javascript">
    function reset() {
        document.forms[0].target = "";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
	}
    function onPrint() {
        var ret = checkAllDates();
        if(ret==true) {
			if (document.all){
				document.all.FrmForm.action="../form/createpdf?__title=Invoice&__cfgfile=invoice&__template=invoice";
			}else{
				//document.getElementById('FrmForm').action="../form/createpdf?__title=Invoice&__cfgfile=invoice&__template=invoice";
				document.forms[0].action="../form/createpdf?__title=Invoice&__cfgfile=invoice&__template=invoice";
	        }
			document.forms[0].target="_blank";
        }
        return ret;
    }

function setfocus() {
    this.focus();
    document.forms[0].invoiceTo.focus();
}

    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true) {
            reset();
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    
    function onSaveExit() {
        //document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true) {
            reset();
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        return ret;
    }

	function isNumber(ss){
		var s = ss.value;
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i);
			if (c == '.') {
				continue;
			} else if (((c < "0") || (c > "9"))) {
                alert('Invalid '+s+' in field ' + ss.name);
                ss.focus();
                return false;
			}
        }
        // All characters are numbers.
        return true;
    }
    function checkTypeIn(obj) {
      if(!isNumber(obj.value) ) {
          alert ("You need type in a number in the field.");
        }
    }
    function checkAllDates(){
        var b = true;

        for (var i =0; i <document.forms[0].elements.length; i++) {
            if (document.forms[0].elements[i].name.indexOf("age_")>=0) {
               if(!isNumber(document.forms[0].elements[i]))
                 b=false;
    	    }
	    }
        return b;
    }
</script>

<body bgproperties="fixed" topmargin=0 leftmargin=1 rightmargin=1
	onLoad="setfocus()">
<!--
@oscar.formDB Table="formInvoice"
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'"
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL"
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL"
@oscar.formDB Field="formEdited" Type="timestamp"
-->
<html:form action="/form/formname">
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="submit" value="exit" />

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();" />
			<input type="submit" value="Print"
				onclick="javascript:return onPrint();return false;" /></td>
		</tr>
	</table>

	<center>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>


			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="50%"></td>
					<td valign="top">

					<table width="10%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td nowrap>Patient name:</td>
							<td><input type="text" name="patientName" size="60"
								maxlength="80"
								value="<%= props.getProperty("patientName", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td>Address:</td>
							<td><input type="text" name="c_address" size="60"
								maxlength="80" value="<%= props.getProperty("c_address", "") %>"
								@oscar.formDB /> <input type="text" name="c_address2" size="60"
								maxlength="80"
								value="<%= props.getProperty("c_address2", "") %>" @oscar.formDB />
							</td>
						</tr>
						<tr>
							<td>Phone:</td>
							<td><input type="text" name="c_phone" size="60"
								maxlength="60" value="<%= props.getProperty("c_phone", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td>DOB:</td>
							<td><input type="text" name="dateOfBirth" size="10"
								maxlength="10"
								value="<%= props.getProperty("dateOfBirth", "") %>"
								@oscar.formDB /> <input type="text" name="patientSex" size="1"
								maxlength="6" value="<%= props.getProperty("patientSex", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td nowrap>Health Number:</td>
							<td><input type="text" name="c_phn" size="20" maxlength="20"
								value="<%= props.getProperty("c_phn", "") %>" @oscar.formDB />
							</td>
						</tr>
					</table>

					</td>
				</tr>
			</table>

			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				bgcolor="#99CCFF">
				<tr>
					<th><font size="+2">INVOICE</font></th>
				</tr>
			</table>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td nowrap width="10%">Date:</td>
					<td><input type="text" name="date_invoice" id="date_invoice"
						readonly size="8" maxlength="10"
						value="<%= props.getProperty("date_invoice", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="date_invoice_cal" /></td>
				</tr>
				<tr>
					<td nowrap>To:</td>
					<td><input type="text" name="invoiceTo" size="50"
						maxlength="80" value="<%= props.getProperty("invoiceTo", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td nowrap>Ref.:</td>
					<td><input type="text" name="invoiceRef" size="50"
						maxlength="80" value="<%= props.getProperty("invoiceRef", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td nowrap>Service date:</td>
					<td><input type="text" name="date_service" id="date_service"
						readonly size="8" maxlength="10"
						value="<%= props.getProperty("date_service", "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif"
						id="date_service_cal" /></td>
				</tr>
				<tr>
					<td nowrap>Service type:</td>
					<td><input type="text" name="serviceType" size="50"
						maxlength="80" value="<%= props.getProperty("serviceType", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td nowrap>Amount billed: $</td>
					<td><input type="text" name="amountBilled"
						onChange="checkTypeIn()" size="10" maxlength="10"
						value="<%= props.getProperty("amountBilled", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td nowrap valign="top">Comments:</td>
					<td><textarea name="comments" style="width: 100%" cols="100"
						rows="12" @oscar.formDB dbType="text"> <%= props.getProperty("comments", "") %> </textarea>
					</td>
				</tr>
				<tr>
					<td align="right">Dr.</td>
					<td><input type="text" name="signature" size="50"
						maxlength="60"
						value="<%= props.getProperty("signature", providerName) %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td nowrap>Date:</td>
					<td><input type="text" name="date_signature"
						id="date_signature" readonly size="8" maxlength="10"
						value="<%= props.getProperty("date_signature", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="date_signature_cal" /></td>
				</tr>
			</table>

			</td>
		</tr>
	</table>

	</center>

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();" />
			<input type="submit" value="Print"
				onclick="javascript:return onPrint();return false;" /></td>
		</tr>
	</table>

</html:form>
<script type="text/javascript">
Calendar.setup({ inputField : "date_invoice", ifFormat : "%Y/%m/%d", showsTime :false, button : "date_invoice_cal" });
Calendar.setup({ inputField : "date_service", ifFormat : "%Y/%m/%d", showsTime :false, button : "date_service_cal" });
Calendar.setup({ inputField : "date_signature", ifFormat : "%Y/%m/%d", showsTime :false, button : "date_signature_cal" });
</script>
</body>
</html:html>
