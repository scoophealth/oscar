<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat" %>
<%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<%@ include file="dbDMS.jsp" %>
 <%   
  if(session.getValue("user") == null)
    response.sendRedirect("../../logout.jsp");
  String user_no;
  user_no = (String) session.getAttribute("user");
           String docdownload = oscarVariables.getProperty("project_home") ;;
           session.setAttribute("homepath", docdownload);      

%>

<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);
   

%>
<html>
<head>
<title><bean:message key="dms.documentList.title"/></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<script language="JavaScript">
<!--

var remote=null;

function rs(n,u,w,h,x) {
  args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
  remote=window.open(u,n,args);
  if (remote != null) {
    if (remote.opener == null)
      remote.opener = self;
  }
  if (x == 1) { return remote; }
}


var awnd=null;
function popPage(url) {
  
  awnd=rs('att',url ,400,200,1);
  awnd.focus();
}

 function closeit() {
    self.opener.refresh();
      close();
    }   
//-->
</script>
<link rel="stylesheet" href="dms.css" />
</head>
<body  background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
    <tr bgcolor="#486ebd">
     <th align='LEFT'>
		<input type='button' name='print' value='<bean:message key="global.btnPrint"/>' onClick='window.print()'> </th> 
    <th align='CENTER'  ><font face="Arial, Helvetica, sans-serif" color="#FFFFFF"><bean:message key="dms.documentList.msgDocActivity"/> </font></th>
      <th align='RIGHT'><input type='button' name='close' value='<bean:message key="global.btnClose"/>' onClick='closeit()'></th>
  </tr>
</table>
<script LANGUAGE="JavaScript">
      self.close();
      self.opener.refresh();
</script>

<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%" BGCOLOR="#C4D9E7">
<tr valign="top"> 
      
    <td rowspan="2" ALIGN="center" valign="middle"> 
 
          <table width="100%" border="1" cellspacing="0" cellpadding="0">  
<tr>
          <td width="34%"><bean:message key="dms.documentList.msgDocDesc"/></td>
          <td width="15%"><bean:message key="dms.documentList.msgDocType"/></td>
          <td width="17%"><bean:message key="dms.documentList.msgCreator"/></td>
          <td width="21%"><bean:message key="dms.documentList.msgUpdate"/></td> 
          <td width="13%"><bean:message key="dms.documentList.msgStatus"/></td>
  </tr>
  
  <%String creator="%", docfilename="",doctype="", docdesc="", dispDesc="", dispType="", dispFunction="", dispCreator="", dispUpdatedate="", dispFilename="", dispStatus="";
 if(request.getParameter("creator") != null) creator= request.getParameter("creator") ;
  if(request.getParameter("docfilename") != null) docfilename= request.getParameter("docfilename") ;
   if(request.getParameter("doctype") != null) doctype= request.getParameter("doctype") ;
   if(request.getParameter("docdesc") != null) docdesc= request.getParameter("docdesc") ;
   
   
   
   if (docdesc.compareTo("") == 0 || docdesc == null) docdesc="";
      if (doctype.compareTo("") == 0 || doctype == null) doctype="";
         if (docfilename.compareTo("") == 0 || docfilename == null) docfilename="";
            if (creator.compareTo("") == 0 || creator == null) creator="";
            
    String proFirst="", proLast="", proOHIP="", proNo="";
     int Count = 0;
     ResultSet rslocal=null;
     
     
     ResultSet rslocal2 = null;
          String[] param2 = new String[4];
                  param2[0] = docfilename;
                   param2[1] = ""; // doctype;
                   param2[2] = docdesc;
     		  param2[3] = creator;
   rslocal2 = apptMainBean.queryResults(param2, "search_document");
   while(rslocal2.next()){
   dispDesc = rslocal2.getString("docdesc");
    dispFilename = rslocal2.getString("docfilename");
     dispType = rslocal2.getString("doctype");
      dispCreator = rslocal2.getString("doccreator");
       dispUpdatedate = rslocal2.getString("updatedatetime");
       dispStatus = rslocal2.getString("status");
       if (dispStatus.compareTo("A") == 0) dispStatus="active"; 
       
       
       rslocal = null;
     rslocal = apptMainBean.queryResults(creator, "search_provider_name");
     while(rslocal.next()){
     
     proFirst = rslocal.getString("first_name");
     proLast = rslocal.getString("last_name");
     proOHIP = rslocal.getString("provider_no");
%>
  <tr>
          <td width="34%"><a href=# onClick="javascript:rs('new','documentGetFile.jsp?document=<%=dispFilename%>', 480,480,1)"><%=dispDesc%></td>
          <td width="15%"><%=dispType%></td>
          <td width="17%"><%=proLast%>, <%=proFirst%></td>
          <td width="21%"><%=dispUpdatedate%></td>
          <td width="13%"><%=dispStatus%></td>
  </tr>

<%

}
}

  %>
  

</table>
</td>
</tr>
</table>
<br>
<form>
  <input type="button" name="Button" value="<bean:message key="global.btnClose"/>" onclick=closeit();>
</form>
</body>
</html:html>