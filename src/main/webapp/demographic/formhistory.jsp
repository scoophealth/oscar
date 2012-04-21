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

<%
  if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
  String user_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*" errorPage="errorpage.jsp"%>
<jsp:useBean id="formHistBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.RecycleBin" %>
<%@ page import="org.oscarehr.common.dao.RecycleBinDao" %>
<%@ page import="org.oscarehr.common.model.Form" %>
<%@ page import="org.oscarehr.common.dao.FormDao" %>
<%
	RecycleBinDao recycleBinDao = SpringUtils.getBean(RecycleBinDao.class);
	FormDao formDao = SpringUtils.getBean(FormDao.class);
%>
<%
  String [][] dbQueries=new String[][] {
    {"search_form", "select * from form where demographic_no = ? order by form_date desc, form_time desc, form_no desc"},
    {"search_formdetail", "select * from form where form_no=?"},
   };
   formHistBean.doConfigure(dbQueries);
%>

<% //delete the selected records
  if(request.getParameter("submit")!=null && request.getParameter("submit").equals("Delete") ) {
    ResultSet rs = null;
    int ii = Integer.parseInt(request.getParameter("formnum"));
    String[] param =new String[4];
    String content=null, keyword=null, datetime=null;
    GregorianCalendar now=new GregorianCalendar();
    datetime  =now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH) +" "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);

    for(int i=0;i<=ii;i++) {
      if(request.getParameter("form_no"+i)==null) {
        continue;
      }

      rs = formHistBean.queryResults(request.getParameter("form_no"+i), "search_formdetail");
      while (rs.next()) {
        keyword = formHistBean.getString(rs,"form_name")+formHistBean.getString(rs,"form_date");
        content = "<form_no>"+formHistBean.getString(rs,"form_no")+"</form_no>"+ "<demographic_no>"+formHistBean.getString(rs,"demographic_no")+"</demographic_no>"+ "<provider_no>"+formHistBean.getString(rs,"provider_no")+"</provider_no>";
        content += "<form_date>"+formHistBean.getString(rs,"form_date")+"</form_date>"+ "<form_time>"+formHistBean.getString(rs,"form_time")+"</form_time>"+ "<form_name>"+formHistBean.getString(rs,"form_name")+"</form_name>";
        content += "<content>"+formHistBean.getString(rs,"content")+"</content>" ;
      }

	    param[0]=user_no;
	    param[1]=datetime;
	    param[2]=keyword;
	    param[3]=content;

	    RecycleBin recycleBin = new RecycleBin();
	    recycleBin.setKeyword(keyword);
	    recycleBin.setProviderNo(user_no);
	    recycleBin.setTableName("form");
	    recycleBin.setTableContent(content);
	    recycleBin.setUpdateDateTime(new java.util.Date());
	    recycleBinDao.persist(recycleBin);
        int rowsAffected = 1;
      if(rowsAffected ==1) {
    	  Form form = formDao.find(Integer.parseInt(request.getParameter("form_no"+i)));
    	  if(form != null) {
    		  formDao.remove(form.getId());
    	  }
      } else {
        response.sendRedirect("index.htm");
      }
    } //end for loop
  }
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PATIENT'S FORM</title>
<link rel="stylesheet" href="../web.css">
<script language="JavaScript">
<!--


//-->
</script>
</head>
<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="silver">
		<th align=CENTER NOWRAP><font face="Helvetica" color="navy">FORM
		HISTORY</font></th>
	</tr>
</table>

<form name="encounterrep" method="post" action="formhistory.jsp">
<table width="100%" border="0" bgcolor="ivory">
	<tr>
		<td><font size="-1"> </font></td>
	</tr>
	<tr>
		<td bgcolor="#FFFFFF" align="center">
		<%
   ResultSet rsdemo = null;
   rsdemo = formHistBean.queryResults(request.getParameter("demographic_no"), "search_form");
   int i=0;
   while (rsdemo.next()) {
     i++;
%> &nbsp;<%=rsdemo.getString("form_date")%> <%=rsdemo.getString("form_time")%>

		<input type="checkbox" name="<%="form_no"+i%>"
			value="<%=rsdemo.getString("form_no")%>"> <font color="blue">
		<a href=#
			onClick="popupPage(600,800,'../provider/providercontrol.jsp?form_no=<%=rsdemo.getString("form_no")%>&dboperation=search_form&displaymodevariable=form<%=rsdemo.getString("form_name")%>.jsp&displaymode=vary&bNewForm=0')">
		<%=rsdemo.getString("form_name")%></a></font> by <%=rsdemo.getString("provider_no")%><br>
		<%
   }
%>
		</td>
	</tr>
	<tr bgcolor="#eeeeee">
		<td align="center"><input type="hidden" name="formnum"
			value="<%=i%>"> <input type="hidden" name="demographic_no"
			value="<%=request.getParameter("demographic_no")%>"> <input
			type="submit" name="submit" value="Delete"><input
			type="button" name="button" value="Cancel" onClick="window.close()">
		</td>
	</tr>
</table>
</form>
<center></center>
</body>
</html>
