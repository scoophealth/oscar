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
<%@ page import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*" errorPage="errorpage.jsp" %>
<%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%@ include file="dbReport.jsp" %>
<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  String clinic="";
  String clinicview = oscarVariables.getProperty("clinic_view");
   ResultSet rslocal2;
          rslocal2 = null;
   rslocal2 = apptMainBean.queryResults(clinicview, "search_visit_location");
   while(rslocal2.next()){
 clinic = rslocal2.getString("clinic_location_name");
  }
  
  
  
  %><% 
  	int flag = 0, rowCount=0;
  String reportAction=request.getParameter("reportAction")==null?"":request.getParameter("reportAction");
   String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
   String xml_appointment_date = request.getParameter("xml_appointment_date")==null?"":request.getParameter("xml_appointment_date");
%>
<html>
<head>
<title>oscarReport -  Visit Report</title>
<link rel="stylesheet" href="oscarReport.css" >
<style type="text/css">
<!--
.bodytext
{
  font-family: Arial, Helvetica, sans-serif;
  font-size: 14px;
  font-style: bold;
  line-height: normal;
  font-weight: normal;
  font-variant: normal;
  text-transform: none;
  color: #FFFFFF;
  text-decoration: none;
}
-->
</style>
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
function MM_openBrWindow(theURL,winName,features) { //v2.0
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
//-->
</script>


</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0" topmargin="5">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#FFFFFF"> 
    <div align="right"><a href="manageProvider.jsp?action=visitreport" target="_blank"><font face="Arial, Helvetica, sans-serif" size="1">Manage Provider List </font></a></div>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#000000"> 
    <td height="40" width="10%">
      <input type='button' name='print' value='Print' onClick='window.print()'>
    </td>
    <td width="90%" align="left"> 
      <p><font face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF"><b><font face="Arial, Helvetica, sans-serif" size="4">oscar<font size="3">Report</font></font></b></font></p>
    </td>
  </tr>
</table>

<table width="100%" border="0" bgcolor="#EEEEFF">
  <form name="serviceform" method="get" action="oscarReportVisitControl.jsp">
    <tr> 
      <td width="50%" align="left"> <font size="1" color="#333333" face="Verdana, Arial, Helvetica, sans-serif"> 
       <input type="radio" name="reportAction" value="lk"  <%=reportAction.equals("lk")?"checked":""%>>
        Larry Kain Report
        <input type="radio" name="reportAction" value="vr"  <%=reportAction.equals("vr")?"checked":""%>>
       Visit Report</font> </td>
      <td width="40%"> 
  <!--     <div align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#333333"><b>Select 
            provider </b></font> 
          <select name="providerview">
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
        </div> -->
      </td>
      <td width="10%"> <font color="#333333" size="1" face="Verdana, Arial, Helvetica, sans-serif"> 
        <input type="submit" name="Submit" value="Create Report">
        </font></td>
    </tr>
<tr> 
      <td width="50%"> 
        <div align="left"><font color="#003366"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><b> 
          
          <font color="#333333">Service Date-Range</font></b></font></font> &nbsp; &nbsp;  <font size="1" face="Arial, Helvetica, sans-serif"><a href="#" onClick="MM_openBrWindow('../billing/billingCalendarPopup.jsp?type=admission&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')">Begin Date:</a></font> <input type="text" name="xml_vdate" value="<%=xml_vdate%>">
          
   </div>
      </td>
      <td colspan='2' > 
        <div align="left">    <font size="1" face="Arial, Helvetica, sans-serif"><a href="#" onClick="MM_openBrWindow('../billing/billingCalendarPopup.jsp?type=end&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')">End Date:</a></font> <input type="text" name="xml_appointment_date" value="<%=xml_appointment_date%>">
         

              </div>
      </td>
       </tr>
  </form>
</table>
<% if (reportAction.compareTo("") == 0 || reportAction == null){%>

  <p>&nbsp; </p>
<% } else {  
%>
<%
if (reportAction.compareTo("lk") == 0) {
%> 
<%@ include file="oscarReportVisit_lk.jsp" %> 
<%
}else{
if (reportAction.compareTo("vr") == 0) {
%>
<%@ include file="oscarReportVisit_vr.jsp" %> 
<% }}}
%>

  
    <%
 apptMainBean.closePstmtConn();
  %>

<%@ include file="../demographic/zfooterbackclose.htm" %> 

</body>
</html>
