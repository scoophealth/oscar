<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat" errorPage="errorpage.jsp" %>
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
  String timeStamp = MyDateFormat.getDigitalXX(curYear) + MyDateFormat.getDigitalXX(curMonth) + MyDateFormat.getDigitalXX(curDay) + MyDateFormat.getDigitalXX(now.get(Calendar.HOUR_OF_DAY)) + MyDateFormat.getDigitalXX(now.get(Calendar.MINUTE)) + MyDateFormat.getDigitalXX(now.get(Calendar.SECOND));     
  String proFirst="", proLast="", proOHIP="", proNo="";
   int Count = 0;
   ResultSet rslocal=null;
    rslocal = null;
   rslocal = apptMainBean.queryResults(request.getParameter("creator"), "search_provider_name");
   while(rslocal.next()){
   
   proFirst = rslocal.getString("first_name");
   proLast = rslocal.getString("last_name");
   proOHIP = rslocal.getString("provider_no");
}
%>
<html>
<head>
<title>DOCUMENT MANAGEMENT SYSTEM</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<script language="JavaScript">
<!--

function setfocus() {
  this.focus();
  document.aDoc.docfilename.focus();
  document.aDoc.docfilename.select();
}

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
function ScriptAttach() {
  f0 = escape(document.aDoc.docfilename.value);
  awnd=rs('att','../dms/zadddocument.htm' ,400,200,1);
  awnd.focus();
}




function addRedirect()
{       

var newD = document.aDoc.hyperlink.value;
	document.aDoc.docxml.value = "<HTML><HEAD><META HTTP-EQUIV='REFRESH' CONTENT = '1; URL="+newD+"'></HEAD><BODY BGCOLOR=FFFFFF VLINK=000066 LINK=000066></BODY></HTML>";
document.aDoc.docdesc.value = document.aDoc.docdesc.value + " (Link)"; 
}


//-->
</script>
<link rel="stylesheet" href="../web.css" />
</head>

<body  background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr bgcolor="#486ebd">
    <th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">ADD HTML DOCUMENT</font></th>
  </tr>
</table>
<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%" BGCOLOR="#C4D9E7"  >
  <FORM NAME="aDoc" ACTION="dbhtmlDocument.jsp" METHOD="POST">
    <tr valign="top"> 
      <td rowspan="2" ALIGN="right" valign="middle"> 
        <div align="center"> 
          <p>&nbsp;</p>
          <table width="80%" border="1" cellspacing="0" cellpadding="0">
            <tr> 
              <td colspan="2"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"></font></td>
            </tr>
            <tr> 
              <td width="16%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000">Function</font></td>
              <td width="84%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                <input type="text" name="function" value="<%=request.getParameter("function")%> " size="20">
                </font></td>
            </tr>
            <tr> 
              <td width="16%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000">Function 
                ID</font></td>
              <td width="84%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                <input type="text" name="functionid" value="<%=request.getParameter("functionid")%>" size="20">
                </font></td>
            </tr>
            <tr> 
              <td width="16%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000">Document 
                Type</font></td>
              <td width="84%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                <select name="doctype">
                  <option value="">Select Type</option>
                  <%
                    ResultSet rsdemo2 = null;
		  	   
		  		 	          	     String[] param4=new String[1];
		  		 	          	    	 
		  		 	          	    	   
		  		 	          	    		   param4[0] = request.getParameter("function");
		  					        
		  		 	          
		  		 	          	    rsdemo2 = apptMainBean.queryResults(param4, "search_doctype_by_module");
		  		 	             while (rsdemo2.next()) {   
		  		 	 %>
                  <option value="<%=rsdemo2.getString("doctype")%>" selected><%=rsdemo2.getString("doctype")%></option>
                  <%
		 	          }
                  %>
                </select>
                </font></td>
            </tr>
            <tr> 
              <td width="16%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000">Document 
                Description</font></td>
              <td width="84%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                <input type="text" name="docdesc"  size="20">    URL: <input type="text" name="hyperlink" value=""><a href=# onClick="addRedirect()">Redirect</a>
                </font></td>
            </tr>
            <tr> 
              <td width="16%" valign="top"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">HTML 
                Source</font></td>
              <td width="84%"> 
                <textarea name="docxml" cols="80" rows="20"></textarea>
              </td>
            </tr>
            <tr> 
              <td width="16%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000">Create 
                Date</font></td>
              <td width="84%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                <input type="text" name="docdate" readonly value="<%=nowDate%>" size="20" >
                </font></td>
            </tr>
            <tr> 
              <td width="16%"><font face="Verdana, Arial, Helvetica, sans-serif" color="#000000" size="1">Creator 
                </font></td>
              <td width="84%"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                <input type="text" name="dispcreator"  readonly value="<%=proLast%>, <%=proFirst%>" size="20">
                <input type="hidden" name="doccreator" value="<%=request.getParameter("creator")%>" size="20">
                    <input type="hidden" name="docfilename" value="<%=timeStamp%>" size="20">
                <input type="hidden" name="orderby" value="updatedatetime desc" size="20">
                </font></td>
            </tr>
            <tr> 
              <td colspan="2"><font face="Verdana, Arial, Helvetica, sans-serif" color="#0000FF" size="1"><b><i> 
                <input type="SUBMIT" value="Submit" name="SUBMIT">
                </i></b></font><font face="Verdana, Arial, Helvetica, sans-serif" size="1"></font></td>
            </tr>
          </table>
          <p><font face="Verdana" color="#0000FF"><b><i> </i></b></font> <br>
          </p>
        </div>
      </td>
    </tr>
  </form>
</table>
<br>
<br>
<form>
  <input type="button" name="Button" value="Cancel" onclick=self.close();>
</form>
</body>
</html>