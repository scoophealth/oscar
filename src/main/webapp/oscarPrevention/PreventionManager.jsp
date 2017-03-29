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

<%@page import="oscar.oscarPrevention.*"%>
<%@page import="org.oscarehr.common.model.Property" %>
<%@page import="org.oscarehr.provider.model.PreventionManager" %>
<%@page import="org.oscarehr.common.dao.PropertyDao"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="oscar.OscarProperties" %>

<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.regex.Matcher" %>
<%@page import="java.util.regex.Pattern" %>
<%@page import="java.util.regex.*" %>
<%@page import="java.util.*" %>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_prevention" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_prevention");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html:html locale="true">
<head>

<title><bean:message key="oscarprevention.index.oscarpreventiontitre" /> - <bean:message key="admin.admin.preventionNotification.title" /></title>

<script type="text/javascript">
<!--

//-->
</script>


<style type="text/css">

body, td {
	font-size: 10pt;
	font-family:"Arial", Verdana;
}

h3{
margin-bottom:1px;
font-size: 16pt;
font-family:"Arial", Verdana;
}


table.legend{
border:0;
padding-top:10px;
width:370px;
}

table.legend td{
font-size:8;
text-align:left;
}

table.colour_codes{
width:8px;
height:10px;
border:1px solid #999999;
}

</style>

<!--[if IE]>
<style type="text/css">

table.legend{
border:0;
margin-top:10px;
width:370px;
}

table.legend td{
font-size:10;
text-align:left;
}

</style>
<![endif]-->

</head>

<body bgcolor="#B7B18D">

<%
String getStatus=""; //initialize getStatus
String formAction = request.getParameter("formAction");
String setPropValue;

String vProp="hide_prevention_stop_signs";

//create preventions list
PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance(loggedInInfo);
ArrayList<HashMap<String,String>> prevList = pdc.getPreventions(loggedInInfo);

	
	PropertyDao propDao = (PropertyDao)SpringUtils.getBean("propertyDao");
	List<Property> pList = propDao.findByName(vProp); 
	
  	Iterator<Property> i = pList.iterator();

  	while (i.hasNext()) {
  	Property item = i.next();
  	getStatus  = item.getValue();
  	
  	}
	
//checking if hide stop signs have been set in the database if not then check to see if
//show stop signs have been turned off in the property file
if(getStatus=="" && OscarProperties.getInstance().getProperty("SHOW_PREVENTION_STOP_SIGNS","false").equals("false") ) { 
  		//for users who have SHOW_PREVENTION_STOP_SIGNS disabled
  		getStatus="master";
}
  	
//--------------------------UPDATE START-----------------------------
if(formAction!=null ){

String new_value ="";
  	
if(formAction.equals("update")){
setPropValue=request.getParameter("master_radio");	
	if(formAction.equals("update")){
		
 		if(setPropValue!=null){
 		new_value=setPropValue;
 		}	
	}
	
}else if (formAction.equals("custom") ){
	String v; 
	
	int count=0;  
	for(int c=0;c<prevList.size();c++){
		v = request.getParameter("onOff"+c);
		
		if(!v.equals("0")){
		count++;
		}
	}
    
	String outMe="";
	int idisable = 0;
	for(int d=0;d<prevList.size();d++){
		
		v = request.getParameter("onOff"+d);
		if(!v.equals("0")){
		outMe=outMe+"["+v+"]";
		idisable++;
		}
		
	}
	
	if(idisable>0){
	new_value=outMe;
	}else{
		new_value="false";
	}
	
}

if(pList.size()>0){
	Property p = propDao.checkByName(vProp);
	p.setValue(new_value);
	propDao.merge(p);
}else{
	//if hide_prevention_stop_signs does not exist then create it and store the value
	Property x = new Property();
	x.setName(vProp);
	x.setValue(new_value);
	propDao.persist(x);
}

getStatus = new_value;

}
//-----------------------------UPDATE END---------------------------	

//search and find start+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Pattern pattern = Pattern.compile("(\\[)(.*?)(\\])");
Matcher matcher = pattern.matcher(getStatus);
List<String> listMatches = new ArrayList<String>();

while(matcher.find()){

listMatches.add(matcher.group(2));

}

int listCount =listMatches.size();

//start a counter when the value is found
int matchCount=0;
String r="";

//search and find end+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

String masterOff;
String masterOn;
String stopSign;
String rowBgcolor;
String isDisabled;

if(getStatus!=null && getStatus.equals("master")){
	masterOff="checked";
	masterOn="";
	stopSign="images/stop_sign_grey.png";
	rowBgcolor="#F2F2F2";
	isDisabled="disabled";
}else{
	masterOff="";
	masterOn="checked";
	stopSign="images/stop_sign.png";
	rowBgcolor="#D7FFD7";
	isDisabled="";
}

%>
<!-- main table other table wrapper -->
<table width="530" align="center" cellpadding="0" cellspacing="0">
<tr>
	<td>		
		<!-- Master Control: to enable or disable stop sign warnings -->
		<h3>Appointment <bean:message key="admin.admin.preventionNotification.title" />:</h3>
		These settings will set the "stop sign" notifications you see displayed on the appointment screen.
		
		<form name="masterForm" action="PreventionManager.jsp?formAction=update" method="post">
		<table bgcolor="#666666" cellspacing="0" cellpadding="0" border="0" width="530">
			<td>
			<table cellspacing="1" cellpadding="6" border="0" width="530">
		
			<tr bgcolor="<%=rowBgcolor%>">
				<td valign="middle" align="center" width="40"><img src="<%=stopSign%>" border="0"></img></td> <td width="260"><div title="This is a global setting that will affect all users."> Display on Appointment Screen</div> </td> <td align="center"> <table width="240"><td width="50%"><input type="radio" name="master_radio" value="false" <%=masterOn%> > Enabled </td> <td width="50%"> <input type="radio" name="master_radio" value="master" <%=masterOff%> onClick="return confirm('Are you sure you want to disable this option?\n\nThis will disable all prevention notifications from displaying on the appointment screen.');"> Disabled </td></table> </td> 
			</tr>
			</table>
			</td>
		</table>
		<p align="right"><input type="submit" value="Save" ></input></p>
		</form>
</td>
</tr> <!-- end row / new row main wrapper table -->
<tr>
<td>		
		<hr bgcolor="#666666" width="530" align="left"></hr>
		
		<!-- Customize each prevention warnings/reminders -->
		<h3>Customize <bean:message key="admin.admin.preventionNotification.title" />:</h3>
		To customize the notifications below, "Display on Appointment Screen" must be enabled.
		
		<form name="prevForm" action="PreventionManager.jsp?formAction=custom" method="post">
		<table bgcolor="#666666" cellspacing="0" cellpadding="0" border="0" width="530">
			<td>
			
			<table cellspacing="1" cellpadding="6" border="0" width="530">

			<%for (int e = 0 ; e < prevList.size(); e++){ 
				HashMap<String,String> h = prevList.get(e);
                String prevName = h.get("name");
                String prevDesc = h.get("desc");
                
                
	                if(!getStatus.equals("master") && (getStatus!=null || getStatus!="")){
	                	for(String s : listMatches){
		
			                if(prevName.equals(s)){
			
			                	masterOff="checked";
			                	masterOn="";
			                	stopSign="images/stop_sign_grey.png";
			                	rowBgcolor="#F2F2F2";
			                	
			                matchCount++;
			                r=s;
			
			                }
		                }
	                }	
             %>
			<tr bgcolor="<%=rowBgcolor%>">
				<td valign="middle" align="center" width="40"><img src="<%=stopSign%>" border="0"></img></td> <td width="260"><div title="<%=prevDesc%>"><%=prevName%></div> </td> <td align="center"> <table width="240" ><tr><td width="50%"><input type="radio" name="onOff<%=e%>" value="0" <%=isDisabled%> <%=masterOn%>> Enabled </td> <td width="50%"> <input type="radio" name="onOff<%=e%>" value="<%=prevName%>" <%=isDisabled%>  <%=masterOff%>> Disabled </td></tr></table> </td> 
			</tr>
			
			<%
			if(!getStatus.equals("master")){
			masterOff="";
        	masterOn="checked";
        	stopSign="images/stop_sign.png";
        	rowBgcolor="#D7FFD7";
			}
			
			}%>
			</table>
			</td>
		</table>
		<p align="right"><input type="submit" value="Save Custom" <%=isDisabled%> ></input></p>
		</form>
		
<!-- end of main wrapper table -->

</td>
</tr>
</table>		
	
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script>
$( document ).ready(function() {	
    parent.parent.resizeIframe($('html').height());	
	
});
</script>		
</body>
</html:html>
