<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String provider_name = (String) session.getAttribute("userlastname")+", "+(String) session.getAttribute("userfirstname");
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ page import="java.util.*, java.text.*,java.sql.*, java.net.*" errorPage="errorpage.jsp" %>
<%@ page import="oscar.OscarProperties" %>
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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
--><head><title><bean:message key="provider.providerpreference.title"/></title></head>

<html:html locale="true">
<meta http-equiv="Cache-Control" content="no-cache" >

<script language="JavaScript">

function setfocus() {
  this.focus();
  document.UPDATEPRE.mygroup_no.focus();
  document.UPDATEPRE.mygroup_no.select();
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}

function checkTypeNum(typeIn) {
	var typeInOK = true;
	var i = 0;
	var length = typeIn.length;
	var ch;
	// walk through a string and find a number
	if (length>=1) {
	  while (i <  length) {
		ch = typeIn.substring(i, i+1);
		if (ch == ".") { i++; continue; }
		if ((ch < "0") || (ch > "9") ) {
			typeInOK = false;
			break;
		}
	    i++;
      }
	} else typeInOK = false;
	return typeInOK;
}

function checkTypeIn(obj) {
    if(!checkTypeNum(obj.value) ) {
	  alert ("<bean:message key="provider.providerpreference.msgMustBeNumber"/>");
	}
}

function checkTypeInAll() {
  var checkin = false;
  var s=0;
  var e=0;
  var i=0;
  if(isNumeric(document.UPDATEPRE.start_hour.value) && isNumeric(document.UPDATEPRE.end_hour.value) && isNumeric(document.UPDATEPRE.every_min.value)) {
    s=eval(document.UPDATEPRE.start_hour.value);
    e=eval(document.UPDATEPRE.end_hour.value);
    i=eval(document.UPDATEPRE.every_min.value);
    if(e < 24){
      if(s < e){
        if(i <= (e - s)*60 && i > 0){
          checkin = true;
        }else{
          alert ("<bean:message key="provider.providerpreference.msgPositivePeriod"/>");
          this.focus();
          document.UPDATEPRE.every_min.focus();
         }
      }else{
        alert ("<bean:message key="provider.providerpreference.msgStartHourErlierEndHour"/>");
        this.focus();
        document.UPDATEPRE.start_hour.focus();
       }
    }else{
      alert ("<bean:message key="provider.providerpreference.msgHourLess24"/>");
      this.focus();
      document.UPDATEPRE.end_hour.focus();
     }
  } else {
     alert ("<bean:message key="provider.providerpreference.msgTypeNumbers"/>"); 
  }
	return checkin;
}

function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=5,left=5";//360,680
  var popup=window.open(page, "<bean:message key="provider.providerpreference.titlePopup"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}
function isNumeric(strString){
        var validNums = "0123456789";
        var strChar;
        var retval = true;
        if(strString.length == 0){
          retval = false;
        }
        for (i = 0; i < strString.length && retval == true; i++){
           strChar = strString.charAt(i);
           if (validNums.indexOf(strChar) == -1){
              retval = false;
           }
        }
         return retval;
   }



</script>

<body bgproperties="fixed"  onLoad="setfocus();" topmargin="0"leftmargin="0" rightmargin="0">
<FORM NAME = "UPDATEPRE" METHOD="post" ACTION="providercontrol.jsp" onSubmit="return(checkTypeInAll())">
<table border=0 cellspacing=0 cellpadding=0 width="100%" >
  <tr bgcolor="<%=deepcolor%>"> 
      <th><font face="Helvetica"><bean:message key="provider.providerpreference.description"/></font></th>
  </tr>
</table>


        <table BORDER="0" WIDTH="100%">
          <tr BGCOLOR="<%=weakcolor%>"> 
            <td width="20%"> 
              <div align="right"><font face="arial"><bean:message key="provider.preference.formStartHour"/>:</font></div>
            </td>
            <td width="20%"> 
              <INPUT TYPE="TEXT" NAME="start_hour" VALUE='<%=request.getParameter("start_hour")%>' WIDTH="25" HEIGHT="20" border="0" hspace="2" size="8" maxlength="2" >
              <font size="-2"><bean:message key="provider.preference.hr"/></font> </td>
            <td width="20%"> 
              <div align="right"><font face="arial"><bean:message key="provider.preference.formEndHour"/><font size='-2' color='red'>(<=23)</font> :</font></div>
            </td>
            <td width="25%"> 
              <INPUT TYPE="TEXT" NAME="end_hour" VALUE='<%=request.getParameter("end_hour")%>' WIDTH="25" HEIGHT="20" border="0" hspace="2" size="8" maxlength="2" >
              <font size="-2"><bean:message key="provider.preference.hr"/></font></td>
          </tr>
          <tr  BGCOLOR="<%=weakcolor%>"> 
            <td width="20%"> 
              <div align="right"><font face="arial"><bean:message key="provider.preference.formPeriod"/>:</font></div>
            </td>
            <td width="20%"> 
              <INPUT TYPE="TEXT" NAME="every_min" VALUE='<%=request.getParameter("every_min")%>' WIDTH="25" HEIGHT="20" border="0" hspace="2" size="8" maxlength="2" >
              <font size="-2"><bean:message key="provider.preference.min"/></font> </td>
            <td width="20%"> 
              <div align="right"><font face="arial"><a href=# onClick ="popupPage(360,680,'providercontrol.jsp?displaymode=displaymygroup&dboperation=searchmygroupall' );return false;"> 
                <font size="-2">(<bean:message key="provider.providerpreference.viewedit"/>)</font><bean:message key="provider.preference.formGroupNo"/></a>:</font></div>
            </td>
            <td width="25%"> 
              <INPUT TYPE="TEXT" NAME="mygroup_no" VALUE='<%=request.getParameter("mygroup_no")%>' WIDTH="30" HEIGHT="20" border="0" size="12" maxlength="10">
            </td>
          </tr>
	<caisi:isModuleLoad moduleName="ticklerplus">
          <!-- check box of new-tickler-warnning-windows -->
          <tr BGCOLOR="<%=weakcolor%>"> 
            <td width="20%"> 
            </td>
            <td width="20%"> 
              <div align="right"><font face="arial">New Tickler Warning Window:</font></div>
            </td>
            <td width="20%"> 
             <%  String myCheck1 = "";
             	 String myCheck2 = "";
                 String myValue ="";
                  if((request.getParameter("new_tickler_warning_window")).equals("enabled"))
                  { myCheck1 = "checked";
                  	  myCheck2 = "unchecked";}
                  else
                  { myCheck1 = "unchecked";
              	  	  myCheck2 = "checked";}
     
               %>
              
   				<input type="radio" name="new_tickler_warning_window" value="enabled" <%= myCheck1 %> > Enabled
				<br>
				<input type="radio" name="new_tickler_warning_window" value="disabled" <%= myCheck2 %> > Disabled
   				
            </td>
            <td width="25%"> 
            </td>
          </tr>
      </caisi:isModuleLoad>
      
              <INPUT TYPE="hidden" NAME="provider_no" VALUE='<%=request.getParameter("provider_no")%>'>
              <INPUT TYPE="hidden" NAME="color_template" VALUE='deepblue'>
              <INPUT TYPE="hidden" NAME="dboperation" VALUE='updatepreference'>
              <INPUT TYPE="hidden" NAME="displaymode" VALUE='updatepreference'>

        </table>

<table width="100%">
  <tr bgcolor="<%=deepcolor%>">
    <TD align="center">
      <INPUT TYPE="submit" VALUE='<bean:message key="provider.providerpreference.btnSubmit"/>' SIZE="7">
      <INPUT TYPE = "RESET" VALUE ='<bean:message key="global.btnClose"/>' onClick="window.close();"></TD>
  </tr>
</TABLE>

</FORM>

<table width="100%" BGCOLOR="eeeeee">
  <tr> 
    <TD align="center"><a href=# onClick ="popupPage(230,600,'providerchangepassword.jsp');return false;"><bean:message key="provider.btnChangePassword"/></a> &nbsp;&nbsp;&nbsp; <!--| a href=# onClick ="popupPage(350,500,'providercontrol.jsp?displaymode=savedeletetemplate');return false;"><bean:message key="provider.btnAddDeleteTemplate"/></a> | <a href=# onClick ="popupPage(200,500,'providercontrol.jsp?displaymode=savedeleteform');return false;"><bean:message key="provider.btnAddDeleteForm"/></a></td>
  </tr>
   <tr> 
    <TD align="center">  <a href="#" ONCLICK ="popupPage(550,800,'../schedule/scheduletemplatesetting1.jsp?provider_no=<%=request.getParameter("provider_no")%>&provider_name=<%=URLEncoder.encode(provider_name)%>');return false;" title="Holiday and Schedule Setting" ><bean:message key="provider.btnScheduleSetting"/></a> 
      &nbsp;&nbsp;&nbsp; | <a href="#" ONCLICK ="popupPage(550,800,'http://oscar1.mcmaster.ca:8888/oscarResource/manage?username=oscarfp&pw=oscarfp');return false;" title="Resource Management" ><bean:message key="provider.btnManageClinicalResource"/></a--> </td>
  </tr>
  <tr>
     <td bgcolor="#ffffff">&nbsp;
        
     </td>
  </tr>
  <tr>
    <td align="center"><a href=# onClick ="popupPage(230,860,'providerSignature.jsp');return false;"><bean:message key="provider.btnEditSignature"/></a>
    </td>
  </tr>
  
  <tr>
    <td align="center"><a href=# onClick ="popupPage(230,400,'../billing/CA/BC/viewBillingPreferencesAction.do?providerNo=<%=request.getParameter("provider_no")%>');return false;">Edit Billing Preferences</a>
    </td>
  </tr>
  <tr>
    <td align="center"><a href=# onClick ="popupPage(230,860,'providerFax.jsp');return false;"><bean:message key="provider.btnEditFaxNumber"/></a>
  </tr>
  <tr>
    <td align="center"><a href=# onClick ="popupPage(230,860,'providerColourPicker.jsp');return false;"><bean:message key="provider.btnEditColour"/></a>
  </tr>
  
  <%
    if( OscarProperties.getInstance().getProperty("MY_OSCAR", "").equalsIgnoreCase("yes") ) {
  %>
        <tr>
            <td align="center"><a href=# onClick ="popupPage(230,860,'providerIndivoIdSetter.jsp');return false;"><bean:message key="provider.btnSetIndivoId"/></a>
        </tr>
  <%             
    }
  %>

</table>

</body>
</html:html>
