<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String provider_name = (String) session.getAttribute("userlastname")+", "+(String) session.getAttribute("userfirstname");
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
%>
<%@ page import="java.util.*, java.text.*,java.sql.*, java.net.*" errorPage="errorpage.jsp" %>

<html>
<head><title> UPDATE PREFERENCES</title></head>
<meta http-equiv="Cache-Control" content="no-cache" >

<script language="javascript">
<!-- start javascript ---- check to see if it is really empty in database
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
	  alert ("You must type in a number in the field.");
	}
}
function checkTypeInAll() {
  var checkin = false;
  var s=0;
  var e=0;
  if(checkTypeNum(document.UPDATEPRE.start_hour.value) && checkTypeNum(document.UPDATEPRE.end_hour.value) && checkTypeNum(document.UPDATEPRE.every_min.value)) {
    s=eval(document.UPDATEPRE.start_hour.value);
    e=eval(document.UPDATEPRE.end_hour.value);
    if(e < 24 && s <e ) {
	    checkin = true;
	  } else {
	    alert ("Something is wrong with your data!!!");
	  }
	} else {
      alert ("You must type in numbers in some fields."); 
	}
	return checkin;
}

function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=5,left=5";//360,680
  var popup=window.open(page, "Receptmygroup", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}

// stop javascript -->
</script>

<body bgproperties="fixed"  onLoad="setfocus()" topmargin="0"leftmargin="0" rightmargin="0">
<FORM NAME = "UPDATEPRE" METHOD="post" ACTION="providercontrol.jsp" onSubmit="return(checkTypeInAll())">
<table border=0 cellspacing=0 cellpadding=0 width="100%" >
  <tr bgcolor="<%=deepcolor%>"> 
      <th><font face="Helvetica">PREFERENCE</font></th>
  </tr>
</table>


        <table BORDER="0" WIDTH="100%">
          <tr BGCOLOR="<%=weakcolor%>"> 
            <td width="20%"> 
              <div align="right"><font face="arial"> Start Hour :</font></div>
            </td>
            <td width="20%"> 
              <INPUT TYPE="TEXT" NAME="start_hour" VALUE='<%=request.getParameter("start_hour")%>' WIDTH="25" HEIGHT="20" border="0" hspace="2" size="8" maxlength="2" onBlur="checkTypeIn(this)">
              <font size="-2">hr.</font> </td>
            <td width="20%"> 
              <div align="right"><font face="arial"> End Hour<font size='-2' color='red'>(<=23)</font> :</font></div>
            </td>
            <td width="25%"> 
              <INPUT TYPE="TEXT" NAME="end_hour" VALUE='<%=request.getParameter("end_hour")%>' WIDTH="25" HEIGHT="20" border="0" hspace="2" size="8" maxlength="2" onBlur="checkTypeIn(this)">
              <font size="-2"> hr. </font></td>
          </tr>
          <tr  BGCOLOR="<%=weakcolor%>"> 
            <td width="20%"> 
              <div align="right"><font face="arial"> Period :</font></div>
            </td>
            <td width="20%"> 
              <INPUT TYPE="TEXT" NAME="every_min" VALUE='<%=request.getParameter("every_min")%>' WIDTH="25" HEIGHT="20" border="0" hspace="2" size="8" maxlength="2" onBlur="checkTypeIn(this)">
              <font size="-2">min.</font> </td>
            <td width="20%"> 
              <div align="right"><font face="arial"><a href=# onClick ="popupPage(360,680,'providercontrol.jsp?displaymode=displaymygroup&dboperation=searchmygroupall' );return false;"> 
                <font size="-2">(View/Edit)</font>Group No. </a>:</font></div>
            </td>
            <td width="25%"> 
              <INPUT TYPE="TEXT" NAME="mygroup_no" VALUE='<%=request.getParameter("mygroup_no")%>' WIDTH="30" HEIGHT="20" border="0" size="12" maxlength="10">
            </td>
          </tr>
              <INPUT TYPE="hidden" NAME="provider_no" VALUE='<%=request.getParameter("provider_no")%>'>
              <INPUT TYPE="hidden" NAME="color_template" VALUE='deepblue'>
              <INPUT TYPE="hidden" NAME="dboperation" VALUE='updatepreference'>
              <INPUT TYPE="hidden" NAME="displaymode" VALUE='updatepreference'>

        </table>

<table width="100%">
  <tr bgcolor="<%=deepcolor%>">
    <TD align="center">
      <INPUT TYPE="submit" VALUE="Update" SIZE="7">
      <INPUT TYPE = "RESET" VALUE = " Cancel " onClick="window.close();"></TD>
  </tr>
</TABLE>

</FORM>

<table width="100%" BGCOLOR="eeeeee">
  <tr> 
    <TD align="center"><a href=# onClick ="popupPage(230,600,'providerchangepassword.jsp');return false;">Change Your Password</a> &nbsp;&nbsp;&nbsp; <!--| a href=# onClick ="popupPage(350,500,'providercontrol.jsp?displaymode=savedeletetemplate');return false;">Add/Delete 
      a Template</a> | <a href=# onClick ="popupPage(200,500,'providercontrol.jsp?displaymode=savedeleteform');return false;">Add/Delete a Form</a></td>
  </tr>
   <tr> 
    <TD align="center">  <a href="#" ONCLICK ="popupPage(550,800,'../schedule/scheduletemplatesetting1.jsp?provider_no=<%=request.getParameter("provider_no")%>&provider_name=<%=URLEncoder.encode(provider_name)%>');return false;" title="Holiday and Schedule Setting" >Schedule Setting</a> 
      &nbsp;&nbsp;&nbsp; | <a href="#" ONCLICK ="popupPage(550,800,'http://oscar1.mcmaster.ca:8888/oscarResource/manage?username=oscarfp&pw=oscarfp');return false;" title="Resource Management" >Manage Clinical Resource</a--> </td>
  </tr>
  <tr>
     <td bgcolor="#ffffff">
        &nbsp;
     </td>
  </tr>
  <tr>
    <td align="center"><a href=# onClick ="popupPage(230,600,'providerSignature.jsp');return false;">Edit your Signature</a>
    </td>
  </tr>

</table>

</body>
</html>
