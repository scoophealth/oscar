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
-->

<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="scheduleMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="myTempBean" class="oscar.ScheduleTemplateBean" scope="page" />
<% //save or delete the settings
  int rowsAffected = 0;
  if(request.getParameter("dboperation")!=null && (request.getParameter("dboperation").compareTo(" Save ")==0 || request.getParameter("dboperation").equals("Delete") ) ) {
    String pre = request.getParameter("providerid").equals("Public")&&!request.getParameter("name").startsWith("P:")?"P:":"" ;
    String[] param1 =new String[4];
    param1[0]=request.getParameter("providerid");
    param1[1]=pre + request.getParameter("name");
    param1[2]=request.getParameter("summary");
    param1[3]=SxmlMisc.createDataString(request,"timecode","_", 256);
    String[] param2 =new String[2];
    param2[0]=request.getParameter("providerid");
    param2[1]= request.getParameter("name");
    rowsAffected = scheduleMainBean.queryExecuteUpdate(param2,"delete_scheduletemplate");
    if(request.getParameter("dboperation")!=null && request.getParameter("dboperation").equals(" Save ") ) 
      rowsAffected = scheduleMainBean.queryExecuteUpdate(param1,"add_scheduletemplate");
  }
  
%>

<html>
<head>
<title>DAY TEMPLATE SETTING</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<!--link rel="stylesheet" href="../web.css" /-->

<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  document.addtemplatecode.name.focus();
  document.addtemplatecode.name.select();
}
function go() {
  var s = document.reportform.startDate.value.replace('/', '-');
  s = s.replace('/', '-');
  var e = document.reportform.endDate.value.replace('/', '-');
  e = e.replace('/', '-');
  var u = 'reportedblist.jsp?startDate=' + s + '&endDate=' + e;
	popupPage(600,750,u);
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}
function checkInput() {
	if(document.schedule.holiday_name.value == "") {
	  alert("Please check your input!!!");
	  return false;
	} else {
	  return true;
	}
}
//-->
</script>
</head>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

  <table border="0" width="100%">
    <tr> 
      <td width="50" bgcolor="#009966">&nbsp;</td>
      <td align="center">
	  
        <table width="100%" border="0" cellspacing="0" cellpadding="5">
<form name="addtemplatecode1" method="post" action="scheduleedittemplate.jsp">
          <tr bgcolor="#CCFFCC"> 
            <td nowrap> 
              <p >Provider: <%=request.getParameter("providername")%></p>
            </td>
            <td align='right'>
      <select name="name" >
	<%
   ResultSet rsdemo = null;
   boolean bEdit=request.getParameter("dboperation")!=null&&request.getParameter("dboperation").equals(" Edit ")?true:false;
   String[] param =new String[2];
   param[0]=request.getParameter("providerid");
   param[1]=request.getParameter("name");
   if(bEdit) {
     rsdemo = scheduleMainBean.queryResults(param, "search_scheduletemplatesingle");
     while (rsdemo.next()) { 
       myTempBean.setScheduleTemplateBean(rsdemo.getString("provider_no"),rsdemo.getString("name"),rsdemo.getString("summary"),rsdemo.getString("timecode") );
       //System.out.println(":"+rsdemo.getString("timecode").length()+rsdemo.getString("timecode")+"|");
     }
   }
   param[1]="name";
   rsdemo = scheduleMainBean.queryResults(param, "search_scheduletemplate");
   while (rsdemo.next()) { 
	%>
        <option value="<%=rsdemo.getString("name")%>"><%=rsdemo.getString("name")+" |"+rsdemo.getString("summary")%></option>
  <%
     }
	%>
      </select>
                <input type="hidden" name="providerid" value="<%=request.getParameter("providerid")%>">
                <input type="hidden" name="providername" value="<%=request.getParameter("providername")%>">
            <td align='right'><input type="submit" name="dboperation" value=" Edit "></td>
          </tr>
</form>  			
        </table>

      <table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="95%">
  			<tr>
        	  <td width="50%" align="center" >&nbsp; </td>
  			</tr>
		</table>

          <table width="95%" border="1" cellspacing="0" cellpadding="2"  bgcolor="silver" >
<form name="addtemplatecode" method="post" action="scheduleedittemplate.jsp">
            <tr bgcolor="#FOFOFO" align="center"> 
              <td colspan=3><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="red">Add A Template</font></td>
            </tr>
            <tr bgcolor='ivory'>
             <td nowrap>Template Name:</td>
             <td><input type="text" name="name" size="30" maxlength="20" <%=bEdit?("value='"+myTempBean.getName()+"'"):"value=''"%> > <font size='-2'>(<20 chars)</font></td>
             <td></td>
            </tr>
            <tr bgcolor='ivory'>
             <td>Summary:</td>
             <td><input type="text" name="summary" size="30" maxlength="30" <%=bEdit?("value='"+myTempBean.getSummary()+"'"):"value=''"%> ></td>
             <td nowrap><a href=# title="	<%
   rsdemo = scheduleMainBean.queryResults("code", "search_scheduletemplatecode");
   while (rsdemo.next()) {   %>
 <%=rsdemo.getString("code")+" - "+rsdemo.getString("description")%>  <%}	%>
             ">Template Code</a></td>
            </tr>
            <tr bgcolor='ivory'>
             <td colspan='3' align='center'>
             <table>
             <%
             int cols=4, rows=6, step=bEdit?myTempBean.getStep():15;

             int icols=60/step, n=0;
             for(int i=0; i<rows; i++) {
             %>
               <tr>
             <% for(int j=0; j<cols; j++) { %>
               <td bgcolor='silver'><%=(n<10?"0":"")+n+":00"%></td>
             <%   for(int k=0; k<icols; k++) { %>
               <td><input type="text" name="timecode<%=i*(cols*icols)+j*icols+k%>" size="1" maxlength="1" <%=bEdit?("value='"+myTempBean.getTimecodeCharAt(i*(cols*icols)+j*icols+k)+"'"):"value=''"%> ></td>
             <%   } 
                n++;
                }%>
               </tr>
             <%} %>
             </table>
             </td>
            </tr>
          </table>
            
            
          <table width="100%" border="0" cellspacing="0" cellpadding="2"  bgcolor="silver" >
          <tr bgcolor="#FOFOFO">
            <td><input type="submit" name="dboperation" value="Delete"></td>
			<td align="right"> 
                <input type="hidden" name="providerid" value="<%=request.getParameter("providerid")%>">
                <input type="hidden" name="providername" value="<%=request.getParameter("providername")%>">
                <input type="submit" name="dboperation" value=" Save ">
                <input type="button" name="Button" value=" Exit " onClick="window.close()">
            </td>
          </tr>
</form>
        </table>

      </td>
    </tr>
  </table>

</form>
</body>
</html>