<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%
  if(session.getValue("user") == null || !((String) session.getValue("userprofession")).equalsIgnoreCase("admin"))
    response.sendRedirect("../logout.jsp");
  String curProvider_no,userfirstname,userlastname;
  curProvider_no = (String) session.getAttribute("user");
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
  //display the main provider page
  //includeing the provider name and a month calendar
%>
<html:html locale="true">
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title><bean:message key="admin.provideraddrecordhtm.title"/></title>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
<!--
function setfocus() {
  document.searchprovider.provider_no.focus();
  document.searchprovider.provider_no.select();
}

function onsub() {
  if(document.searchprovider.provider_no.value=="" ||
     document.searchprovider.last_name.value=="" ||
	 document.searchprovider.first_name.value=="" ||
     document.searchprovider.provider_type.value==""  ) {
     alert("<bean:message key="global.msgInputKeyword"/>");
     return false;
  } else {
    document.forms['searchprovider'].displaymode.value='Provider_Add_Record';
    document.forms['searchprovider'].submit();
    return true;
      // do nothing at the moment
      // check input data in the future 
  }
}
function upCaseCtrl(ctrl) {
  ctrl.value = ctrl.value.toUpperCase();
}
//-->
</script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<center>
   <table border="0" cellspacing="0" cellpadding="0" width="100%" >
      <tr bgcolor="#486ebd"> 
            <th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message key="admin.provideraddrecordhtm.description"/></font></th>
      </tr>
   </table>
  <table cellspacing="0" cellpadding="2" width="90%" border="0">
    <form method="post" action="admincontrol.jsp" name="searchprovider" onsubmit="return onsub()">
      <tr> 
        <td width="50%" align="right"><bean:message key="admin.provider.formProviderNo"/><font color="red">:</font> </td>
        <td>
          <input type="text" name="provider_no" maxlength="6">
        </td>
      </tr>
      <tr> 
        <td> 
          <div align="right"><bean:message key="admin.provider.formLastName"/><font color="red">:</font> </div>
        </td>
        <td>
          <input type="text" name="last_name">
        </td>
      </tr>
      <tr> 
        <td> 
          <div align="right"><bean:message key="admin.provider.formFirstName"/><font color="red">:</font> </div>
        </td>
        <td>
          <input type="text" name="first_name">
        </td>
      </tr>
      <tr>
        <td align="right">Type (<font size="-2"><bean:message key="admin.provider.formType"/></font>)<font color="red">:</font>        </td>
        <td>
          <!--input type="text" name="provider_type" -->
          <select name="provider_type">
            <option value="receptionist"><bean:message key="admin.provider.formType.optionReceptionist"/></option>
            <option value="doctor"><bean:message key="admin.provider.formType.optionDoctor"/></option>
            <option value="doctor"><bean:message key="admin.provider.formType.optionNurse"/></option>
            <option value="doctor"><bean:message key="admin.provider.formType.optionResident"/></option>
            <option value="admin"><bean:message key="admin.provider.formType.optionAdmin"/></option>
          </select>
        </td>
      </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formSpecialty"/>: </td>
        <td>
          <input type="text" name="specialty"  onBlur="upCaseCtrl(this)">
        </td>
      </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formTeam"/>: </td>
        <td>
          <input type="text" name="team" >
        </td>
      </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formSex"/>: </td>
        <td> 
          <input type="text" name="sex" maxlength="1"  onBlur="upCaseCtrl(this)">
        </td>
      </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formDOB"/>(<font size="-1"><i><bean:message key="admin.provideraddrecordhtm.dateFormat"/></i></font>): </td>
        <td> 
          <input type="text" name="dob" value="0000-00-00" >
        </td>
      </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formAddress"/>: </td>
        <td>
          <input type="text" name="address" size="40">
        </td>
      </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formHomePhone"/>: </td>
        <td>
          <input type="text" name="phone" ><bean:message key="admin.provider.formWorkPhone"/>: <input type="text" name="workphone" value="">
        </td>
      </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formPager"/>: </td>
    <td>
          <input type="text" name="xml_p_pager" value="">
        </td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formCell"/>: </td>
    <td>
          <input type="text" name="xml_p_cell" value="">
        </td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formOtherPhone"/>: </td> 
    <td>
          <input type="text" name="xml_p_phone2" value="">
        </td> 
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formFax"/>: </td>
    <td>
          <input type="text" name="xml_p_fax" value="">
        </td>
  </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formOhipNo"/>: </td>
        <td>
          <input type="text" name="ohip_no" >
        </td>
      </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formRmaNo"/>: </td>
        <td>
          <input type="text" name="rma_no" >
        </td>
      </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formBillingNo"/>: </td>
        <td>
          <input type="text" name="billing_no" >
        </td>
      </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formHsoNo"/>: </td>
        <td>
          <input type="text" name="hso_no" >
        </td>
      </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formStatus"/>: </td>
        <td>
          <input type="text" name="status" value='1'>
        </td>
      </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formSpecialtyCode"/>: </td>
        <td>
          <input type="text" name="xml_p_specialty_code" >
        </td>
      </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formBillingGroupNo"/>: </td>
        <td>
          <input type="text" name="xml_p_billinggroup_no" >
        </td>
      </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formSlpUsername"/>: </td>
        <td>
          <input type="text" name="xml_p_slpusername" >
        </td>
      </tr>
      <tr> 
        <td align="right"><bean:message key="admin.provider.formSlpPassword"/>: </td>
        <td>
          <input type="text" name="xml_p_slppassword" >
        </td>
      </tr>
      <tr> 
        <td colspan="2">
          <div align="center"> 
            <input type="hidden" name="dboperation" value="provider_add_record">
            <input type="submit" name="displaymode" value="<bean:message key="admin.provideraddrecordhtm.btnProviderAddRecord"/>">
          </div>
        </td>
      </tr>
    </form>
  </table>
  
  <p></p>
  <hr width="100%" color="orange">
  <table border="0" cellspacing="0" cellpadding="0" width="100%">
    <tr>
      <td><a href="admin.jsp"> <img src="../images/leftarrow.gif" border="0" width="25" height="20" align="absmiddle"><bean:message key="global.btnBack"/></a></td>
      <td align="right"><a href="../logout.jsp"><bean:message key="global.btnLogout"/><img src="../images/rightarrow.gif"  border="0" width="25" height="20" align="absmiddle"></a></td>
    </tr>
  </table>

</center>
</body>
</html:html>
