 <%    
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
  String user_no;
  user_no = (String) session.getAttribute("user");
  int  nItems=0;
     String strLimit1="0";
    String strLimit2="5"; 
    if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  String providerview = request.getParameter("providerview")==null?"all":request.getParameter("providerview") ;
%>
<%@ page import="java.util.*,java.text.*, java.sql.*, oscar.*, java.net.*" %>
<%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%@ include file="dbTicker.jsp" %>
<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  
  
  %><% //String providerview=request.getParameter("provider")==null?"":request.getParameter("provider");
  String ticklerview=request.getParameter("ticklerview")==null?"A":request.getParameter("ticklerview");
   String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
   String xml_appointment_date = request.getParameter("xml_appointment_date")==null?MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay):request.getParameter("xml_appointment_date");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html:html locale="true">
<head>
<title><bean:message key="tickler.ticklerMain.title"/></title>
      <meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
      <meta http-equiv="Pragma" content="no-cache">
      
<script language="JavaScript">
<!--

function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
  var popup=window.open(page, "attachment", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}
function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}
function openBrWindow(theURL,winName,features) { 
  window.open(theURL,winName,features);
}
function setfocus() {
  this.focus();
}
function refresh() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}



function allYear()
{       
var newD = "9999-12-31";
var beginD = "0000-00-00"
	document.serviceform.xml_appointment_date.value = newD;
		document.serviceform.xml_vdate.value = beginD;
}
//-->
</script>
<script>
   
    function Check(e)
    {
	e.checked = true;
	//Highlight(e);
    }

    function Clear(e)
    {
	e.checked = false;
	//Unhighlight(e);
    }

    function CheckAll()
    {
	var ml = document.ticklerform;
	var len = ml.elements.length;
	for (var i = 0; i < len; i++) {
	    var e = ml.elements[i];
	    if (e.name == "checkbox") {
		Check(e);
	    }
	}
	//ml.toggleAll.checked = true;
    }

    function ClearAll()
    {
	var ml = document.ticklerform;
	var len = ml.elements.length;
	for (var i = 0; i < len; i++) {
	    var e = ml.elements[i];
	    if (e.name == "checkbox") {
		Clear(e);
	    }
	}
	//ml.toggleAll.checked = false;
    }

    function Highlight(e)
    {
	var r = null;
	if (e.parentNode && e.parentNode.parentNode) {
	    r = e.parentNode.parentNode;
	}
	else if (e.parentElement && e.parentElement.parentElement) {
	    r = e.parentElement.parentElement;
	}
	if (r) {
	    if (r.className == "msgnew") {
		r.className = "msgnews";
	    }
	    else if (r.className == "msgold") {
		r.className = "msgolds";
	    }
	}
    }

    function Unhighlight(e)
    {
	var r = null;
	if (e.parentNode && e.parentNode.parentNode) {
	    r = e.parentNode.parentNode;
	}
	else if (e.parentElement && e.parentElement.parentElement) {
	    r = e.parentElement.parentElement;
	}
	if (r) {
	    if (r.className == "msgnews") {
		r.className = "msgnew";
	    }
	    else if (r.className == "msgolds") {
		r.className = "msgold";
	    }
	}
    }

    function AllChecked()
    {
	ml = document.messageList;
	len = ml.elements.length;
	for(var i = 0 ; i < len ; i++) {
	    if (ml.elements[i].name == "Mid" && !ml.elements[i].checked) {
		return false;
	    }
	}
	return true;
    }

    function Delete()
    {
	var ml=document.messageList;
	ml.DEL.value = "1"; 
	ml.submit();
    }

    function SynchMoves(which) {
	var ml=document.messageList;
	if(which==1) {
	    ml.destBox2.selectedIndex=ml.destBox.selectedIndex;
	}
	else {
	    ml.destBox.selectedIndex=ml.destBox2.selectedIndex;
	}
    }

    function SynchFlags(which)
    {
	var ml=document.messageList;
	if (which == 1) {
	    ml.flags2.selectedIndex = ml.flags.selectedIndex;
	}
	else {
	    ml.flags.selectedIndex = ml.flags2.selectedIndex;
	}
    }

    function SetFlags()
    {
	var ml = document.messageList;
	ml.FLG.value = "1";
	ml.submit();
    }

    function Move() {
	var ml = document.messageList;
	var dbox = ml.destBox;
	if(dbox.options[dbox.selectedIndex].value == "@NEW") {
	    nn = window.prompt("<bean:message key="tickler.ticklerMain.msgFolderName"/>","");
	    if(nn == null || nn == "null" || nn == "") {
		dbox.selectedIndex = 0;
		ml.destBox2.selectedIndex = 0;
	    }
	    else {
		ml.NewFol.value = nn;
		ml.MOV.value = "1";
		ml.submit();
	    }
	}
	else {
	    ml.MOV.value = "1";
	    ml.submit();
	}
    }

</script>
<style type="text/css">
	<!--
	A, BODY, INPUT, OPTION ,SELECT , TABLE, TEXTAREA, TD, TR {font-family:tahoma,sans-serif; font-size:11px;}
	TD.black              {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #666699   ;}
	TD.lilac              {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilacRed              {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #EEEEFF  ;}
	
	TD.boldlilac          {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:link       {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:visited    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:hover      {font-weight: normal;                                                                            color: #000000; background-color: #CDCFFF  ;}
	TD.lilacRed A:link       {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #EEEEFF  ;}
	TD.lilacRed A:visited    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #EEEEFF  ;}
	TD.lilacRed A:hover      {font-weight: normal;                                                                            color: red; background-color: #CDCFFF  ;}
	TD.whiteRed              {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #FFFFFF;}
	
	TD.white              {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.heading            {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FDCB03; background-color: #666699   ;}
	H2                    {font-weight: bold  ; font-size: 12pt; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	H3                    {font-weight: bold  ; font-size: 10pt; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	H4                    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	H6                    {font-weight: bold  ; font-size: 7pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	A:link                {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #336666; }
	A:visited             {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #336666; }
	A:hover               {                                                                            color: red; background-color: #CDCFFF  ;}
	TD.cost               {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #FFFFFF;}
	TD.black A:link       {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #FFFFFF;}
	TD.black A:visited    {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #FFFFFF;}
	TD.black A:hover      {                                                                            color: #FDCB03; background-color: #FFFFFF;}
	TD.title              {font-weight: bold  ; font-size: 10pt; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white A:link       {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white A:visited    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white A:hover      {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #CDCFFF  ;}
	TD.whiteRed A:link       {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #FFFFFF;}
		TD.whiteRed A:visited    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #FFFFFF;}
		TD.whiteRed A:hover      {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #CDCFFF  ;}
	
	#navbar               {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FDCB03; background-color: #666699   ;}
	SPAN.navbar A:link    {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #666699   ;}
	SPAN.navbar A:visited {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #EFEFEF; background-color: #666699   ;}
	SPAN.navbar A:hover   {                                                                            color: #FDCB03; background-color: #666699   ;}
	SPAN.bold             {font-weight: bold  ;                                                                                            background-color: #666699   ;}
	.sbttn {background: #EEEEFF;border-bottom: 1px solid #104A7B;border-right: 1px solid #104A7B;border-left: 1px solid #AFC4D5;border-top:1px solid #AFC4D5;color:#000066;height:19px;text-decoration:none;cursor: hand}
.mbttn {background: #D7DBF2;border-bottom: 1px solid #104A7B;border-right: 1px solid #104A7B;border-left: 1px solid #AFC4D5;border-top:1px solid #AFC4D5;color:#000066;height:19px;text-decoration:none;cursor: hand}

	-->
</style>      
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0" topmargin="10">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#FFFFFF"> 
    <td height="10" width="70%"> </td>
    <td height="10" width="30%" align=right></td>
</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#000000"> 
    <td height="40" width="10%"><input type='button' name='print' value='<bean:message key="global.btnPrint"/>' onClick='window.print()' class="sbttn"></td>
    <td width="90%" align="left"> 
      <p><font face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF"><b><font face="Arial, Helvetica, sans-serif" size="4"><bean:message key="tickler.ticklerMain.msgTickler"/></font></b></font> 
      </p>
    </td>
  </tr>
</table>
<table width="100%" border="0" bgcolor="#EEEEFF">
  <form name="serviceform" method="get" action="ticklerMain.jsp">
    <tr>    
      <td width="19%"> 
        <div align="right"> <font color="#003366"><font face="Arial, Helvetica, sans-serif" size="2"><b> 
          <font color="#333333"><bean:message key="tickler.ticklerMain.formDateRange"/></font></b></font></font> </div>
      </td>
      <td width="41%"> 
        <div align="center">
          <input type="text" name="xml_vdate" value="<%=xml_vdate%>">
          <font size="1" face="Arial, Helvetica, sans-serif"><a href="#" onClick="openBrWindow('../billing/billingCalendarPopup.jsp?type=admission&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')"><bean:message key="tickler.ticklerMain.btnBegin"/>:</a></font> 
        </div>
      </td>
      <td width="40%"> 
        <input type="text" name="xml_appointment_date" value="<%=xml_appointment_date%>">
        <font size="1" face="Arial, Helvetica, sans-serif"><a href="#" onClick="openBrWindow('../billing/billingCalendarPopup.jsp?type=end&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')"><bean:message key="tickler.ticklerMain.btnEnd"/>:</a> &nbsp; &nbsp; <a href="#" onClick="allYear()"><bean:message key="tickler.ticklerMain.btnViewAll"/></a></font> 
      </td>
    </tr>
    <tr> 
    
      <td colspan="2"> 
        <font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#333333"><b><bean:message key="tickler.ticklerMain.formMoveTo"/> </b> 
        <select name="ticklerview">
         <option value="A" <%=ticklerview.equals("A")?"selected":""%>><bean:message key="tickler.ticklerMain.formActive"/></option>
         <option value="C" <%=ticklerview.equals("C")?"selected":""%>><bean:message key="tickler.ticklerMain.formCompleted"/></option>
         <option value="D" <%=ticklerview.equals("D")?"selected":""%>><bean:message key="tickler.ticklerMain.formDeleted"/></option>
        </select>
      &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#333333"><b><bean:message key="tickler.ticklerMain.formSelectProvider"/> </b></font> <select name="providerview">
           <option value="%" <%=providerview.equals("all")?"selected":""%>><bean:message key="tickler.ticklerMain.formAllProviders"/></option>
			<% String proFirst="";
           String proLast="";
           String proOHIP="";
           String specialty_code; 
String billinggroup_no; 
           int Count = 0; 
        ResultSet rslocal;
        rslocal = null;
 rslocal = apptMainBean.queryResults("%", "search_provider_all_dt");
 while(rslocal.next()){
 proFirst = rslocal.getString("first_name");
 proLast = rslocal.getString("last_name");
 proOHIP = rslocal.getString("provider_no"); 
 billinggroup_no= SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_billinggroup_no>","</xml_p_billinggroup_no>");
 specialty_code = SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_specialty_code>","</xml_p_specialty_code>");

%> 
            <option value="<%=proOHIP%>" <%=providerview.equals(proOHIP)?"selected":""%>><%=proLast%>, 
            <%=proFirst%></option>
            <%
      }      
   
  %>
          </select>   
   
      </td>
      <td width="40%"> <font color="#333333" size="2" face="Verdana, Arial, Helvetica, sans-serif"> 
        <input type="hidden" name="Submit" value="">
        <input type="button" value="<bean:message key="tickler.ticklerMain.btnCreateReport"/>" class="mbttn" onclick="document.forms['serviceform'].Submit.value='Create Report'; document.forms['serviceform'].submit();">
        </font></td>
    </tr>

  </form>
</table>
 
<table bgcolor=#666699 border=0 cellspacing=0 width=100%><form name="ticklerform" method="post" action="dbTicklerMain.jsp">
<tr><td>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<TR bgcolor=#666699>
<TD width="5%"><FONT FACE="verdana,arial,helvetica" COLOR="#FFFFFF" SIZE="-2"><B></B></FONT></TD>
<TD width="15%"><FONT FACE="verdana,arial,helvetica" COLOR="#FFFFFF" SIZE="-2"><B><bean:message key="tickler.ticklerMain.msgDemographicName"/></B></FONT></TD>
<TD width="20%"><FONT FACE="verdana,arial,helvetica" COLOR="#FFFFFF" SIZE="-2"><B><bean:message key="tickler.ticklerMain.msgDoctorName"/></B></FONT></TD>
<TD width="20%"><FONT FACE="verdana,arial,helvetica" COLOR="#FFFFFF" SIZE="-2"><B><bean:message key="tickler.ticklerMain.msgDate"/></B></FONT></TD>

<TD width="10%"><FONT FACE="verdana,arial,helvetica" COLOR="#FFFFFF" SIZE="-2"><B></B><bean:message key="tickler.ticklerMain.msgStatus"/></FONT></TD>
<TD width="30%"><FONT FACE="verdana,arial,helvetica" COLOR="#FFFFFF" SIZE="-2"><B><bean:message key="tickler.ticklerMain.msgMessage"/></B></FONT></TD>
</TR>
<%
 String dateBegin = xml_vdate;
   String dateEnd = xml_appointment_date;
String redColor = "", lilacColor = "" , whiteColor = "";
String vGrantdate = "1980-01-07 00:00:00.0";
DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm.SSS", request.getLocale()); 


  if (dateEnd.compareTo("") == 0) dateEnd = MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay);
   if (dateBegin.compareTo("") == 0) dateBegin="0000-00-00";

   ResultSet rs=null ;
  String[] param =new String[4];
   boolean bodd=false;
   param[0] = ticklerview;
 
 param[1] = dateBegin;
  param[2] = dateEnd;
   param[3] = request.getParameter("providerview")==null?"%": request.getParameter("providerview");
  rs = apptMainBean.queryResults(param, "search_tickler");
   while (rs.next()) {
       nItems = nItems +1;


   bodd=bodd?false:true;
   vGrantdate = rs.getString("service_date")+ " 00:00:00.0";
   java.util.Date grantdate = dateFormat.parse(vGrantdate); 
   java.util.Date toDate = new java.util.Date(); 
    long millisDifference = toDate.getTime() - grantdate.getTime(); 
long daysDifference = millisDifference / (1000 * 60 * 60 * 24); 
if (daysDifference > 0){
%>

<tr >
<TD width="5%"  ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>"><input type="checkbox" name="checkbox" value="<%=rs.getString("tickler_no")%>"></TD>
<TD width="20%" ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>"><a href=# onClick="popupPage(600,800,'../demographic/demographiccontrol.jsp?demographic_no=<%=rs.getString("demographic_no")%>&displaymode=edit&dboperation=search_detail')"><%=rs.getString("last_name")%>,<%=rs.getString("first_name")%></a></TD>
<TD width="20%" ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>"><%=rs.getString("provider_last")%>,<%=rs.getString("provider_first")%></TD>
<TD width="20%" ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>"><%=rs.getString("service_date")%> </TD>
<TD width="10%" ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>"><%=rs.getString("status").equals("A")?"Active":rs.getString("status").equals("C")?"Completed":rs.getString("status").equals("D")?"Deleted":rs.getString("status")%></TD>
<TD width="30%" ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>"><%=rs.getString("message")%></TD>
 </tr>
<%
}else {
%>
<tr >
<TD width="5%"  ROWSPAN="1" class="<%=bodd?"lilac":"white"%>"><input type="checkbox" name="checkbox" value="<%=rs.getString("tickler_no")%>"></TD>
<TD width="20%" ROWSPAN="1" class="<%=bodd?"lilac":"white"%>"><a href=# onClick="popupPage(600,800,'../demographic/demographiccontrol.jsp?demographic_no=<%=rs.getString("demographic_no")%>&displaymode=edit&dboperation=search_detail')"><%=rs.getString("last_name")%>,<%=rs.getString("first_name")%></a></TD>
<TD width="20%" ROWSPAN="1" class="<%=bodd?"lilac":"white"%>"><%=rs.getString("provider_last")%>,<%=rs.getString("provider_first")%></TD>
<TD width="20%" ROWSPAN="1" class="<%=bodd?"lilac":"white"%>"><%=rs.getString("service_date")%> </TD>
<TD width="10%" ROWSPAN="1" class="<%=bodd?"lilac":"white"%>"><%=rs.getString("status").equals("A")?"Active":rs.getString("status").equals("C")?"Completed":rs.getString("status").equals("D")?"Deleted":rs.getString("status")%></TD>
<TD width="30%" ROWSPAN="1" class="<%=bodd?"lilac":"white"%>"><%=rs.getString("message")%></TD>
 </tr>
<%
}

%>

<%}

apptMainBean.closePstmtConn();

if (nItems == 0) {
%>
<tr><td colspan="6" class="white"><bean:message key="tickler.ticklerMain.msgNoMessages"/></td></tr>
<%}%>
<tr bgcolor=#666699><td colspan="6" class="white"><a href="javascript:CheckAll();"><bean:message key="tickler.ticklerMain.btnCheckAll"/></a> - <a href="javascript:ClearAll();"><bean:message key="tickler.ticklerMain.btnClearAll"/></a> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
<input type="button" name="button" value="<bean:message key="tickler.ticklerMain.btnAddTickler"/>" onClick="popupPage('400','600', 'ticklerAdd.jsp')" class="sbttn">
<input type="hidden" name="submit_form" value="">
<% if (ticklerview.compareTo("D") == 0){%>
<input type="button" value="<bean:message key="tickler.ticklerMain.btnEraseCompletely"/>" class="sbttn" onclick="document.forms['ticklerform'].submit_form.value='Erase Completely'; document.forms['ticklerform'].submit();">
<%} else{%>
<input type="button" value="<bean:message key="tickler.ticklerMain.btnComplete"/>" class="sbttn" onclick="document.forms['ticklerform'].submit_form.value='Complete'; document.forms['ticklerform'].submit();">
<input type="button" value="<bean:message key="tickler.ticklerMain.btnDelete"/>" class="sbttn" onclick="document.forms['ticklerform'].submit_form.value='Delete'; document.forms['ticklerform'].submit();">
<%}%>
<input type="button" name="button" value="<bean:message key="global.btnCancel"/>" onClick="window.close()" class="sbttn"> </td></tr>
</table></td></tr></form></table> 
<p><font face="Arial, Helvetica, sans-serif" size="2"> </font></p>
  <p>&nbsp; </p>
<%@ include file="../demographic/zfooterbackclose.jsp" %> 

</body>
</html:html> 
