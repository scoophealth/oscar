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

<%@ page
	import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbReport.jspf"%>
<%    
 
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int xcount = 0, ycount =0;
  String nowDate = String.valueOf(curYear)+"-"+String.valueOf(curMonth) + "-" + String.valueOf(curDay);
//String nowDate = "2002-08-21";
int dob_yy=0, dob_dd=0, dob_mm=0, age=0;
//int  recordAffected = apptMainBean.queryExecuteUpdate(nowDate,"delete_reportagesex_bydate");
String demo_no="", demo_sex="", provider_no="", roster="", patient_status="", status="";
String demographic_dob="1800";
String action = request.getParameter("action");
String last_name="", first_name="", mygroup="";
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Manage Provider</title>
<link rel="stylesheet" href="oscarReport.css">
<link rel="stylesheet" href="tree.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript">
<!--

function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}
function openBrWindow(theURL,winName,features) { 
  window.open(theURL,winName,features);
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

<script type="text/javascript" src="tree.js"></script>
<script type="text/javascript">
		<!--
		var Tree = new Array;
		// nodeId | parentNodeId | nodeName | nodeUrl

<% 

    ResultSet rsdemo = null;
  boolean bodd=true;	
	    ResultSet rsdemo2 = null;
	        ResultSet rsdemo3 = null;
	    int count1 = 0;
ycount=1; 
		 String param4 = "%";
 				String[] param =new String[3];
		 	          	    	
		 	          	    	   
		 	          	 
		 	          
		 	          	    rsdemo = apptMainBean.queryResults(param4, "search_mygroup");
		 	             while (rsdemo.next()) { 
	xcount = ycount;
 
%>
Tree[<%=ycount-1%>]  = "<%=ycount%>|0|<%=rsdemo.getString("mygroup_no")%>  |#";
<%	 	             
ycount = ycount + 1; 
			               
		 	             
		 	             
				     		 	          	    rsdemo2 = apptMainBean.queryResults(rsdemo.getString("mygroup_no"), "search_mygroup_provider");
		 	             while (rsdemo2.next()) { 
		 	             param[0] = rsdemo2.getString("provider_no");
		 	             param[1] = rsdemo2.getString("mygroup_no");
		 	             param[2] = action;
		 	             status = "";
		 	             rsdemo3 = apptMainBean.queryResults(param, "search_reportprovider_check");
		 	                    while (rsdemo3.next()) {
		 	                    status = rsdemo3.getString("status");
		 	                    }
		 	   
		 	             
		 	             
%>




Tree[<%=ycount-1%>]  = "<%=ycount%>|<%=xcount%>|<%=rsdemo2.getString("last_name")%>, <%=rsdemo2.getString("first_name")%>|#";  


<%
count1 = count1 + 1;
ycount = ycount + 1;
		 	          }
             
             

}
 	    
%>

		
		
		//-->
	</script>
</head>

<body>

<div id="tree"><script type="text/javascript">
<!--
	createTree(Tree); 
//-->
</script></div>

<br />
<br />

<a href="mailto:drop@destroydrop.com">drop@destroydrop.com</a>

</body>

</html>
