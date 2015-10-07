<%@page import="java.sql.*" errorPage=""%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script>
//Global variables


function popup( height, width, url, windowName){
  var page = url;
  windowprops = "height="+height+",width="+width+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(url, windowName, windowprops);
  if (popup != null){
    if (popup.opener == null){
      popup.opener = self;
    }
  }
  popup.focus();
  return false;
}

 function popFeeItemList(form,field){
     var width = 575;
     var height = 400;
     var str = document.forms[form].elements[field].value;
     var url = '<rewrite:reWrite jspPage="support/billingfeeitem.jsp"/>'+'?form=' +form+ '&field='+field+'&searchStr=' +str;
     var windowName = field;
     popup(height,width,url,windowName);
  }

function setMode(mode){
	document.forms[0].actionMode.value = mode;
}
function editAssociation(primary,secondary){
  var frm = document.forms[0];
  frm.primaryCode.value = primary;
  frm.secondaryCode.value = secondary;
}

function deleteAssociation(id){
  var frm = document.forms[0];
  if(confirm("Do you really want to delete this entry?")){
    frm.actionMode.value = "delete";
    frm.id.value = id;
    frm.submit();
  }
}
</script>
<title>Manage Procedure and Tray Fee Associations</title>
</head>
<body>
<html:errors />
<html:form action="/billing/CA/BC/supServiceCodeAssocAction">
	<html:hidden property="actionMode" />
	<html:hidden property="id" />
	<fieldset><legend> Edit Procedure/Tray Fee
	Associations </legend>
	<p><label for="primaryCode"> Procedure Fee Code: </label> <html:text
		property="primaryCode" styleId="primaryCode" /> <a href="#"
		onClick="popFeeItemList('supServiceCodeAssocActionForm','primaryCode'); return false;">Search</a>
	</p>
	<p><label for="secondaryCode"> Tray Fee Code: </label> <html:text
		property="secondaryCode" styleId="secondaryCode" /> <a href="#"
		onClick="popFeeItemList('supServiceCodeAssocActionForm','secondaryCode'); return false;">Search</a>
	</p>
	<input type="submit" name="submitButton" value="Save Association"
		onclick="setMode('edit');" /> <input type="reset" value="Clear" /></fieldset>
</html:form>
<p /><display:table class="displayGrid" name="list" pagesize="50"
	defaultsort="1" defaultorder="descending"
	decorator="oscar.oscarBilling.ca.bc.pageUtil.BillCodesTableWrapper">
	<display:column property="billingServiceNo" title="Procedure Fee Code" />
	<display:column property="billingServiceTrayNo" title="Tray Fee Code" />
	<display:column property="associationStatus" title="Options" />
</display:table>
</body>
</html:html>
