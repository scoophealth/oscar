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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"  %>
<%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<%@ include file="dbDMS.jsp" %>
<%

GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);

String module="", module_id="", doctype="", docdesc="", docxml="", doccreator="", docdate="", docfilename="";

module = request.getParameter("function");
module_id = request.getParameter("functionid");
doctype = request.getParameter("doctype");
docdesc = request.getParameter("docdesc");
doccreator = request.getParameter("doccreator");
docdate = request.getParameter("docdate");
docfilename =request.getParameter("docfilename");
%>

<html:html locale="true">
<%
if (docdesc.compareTo("") == 0 || docfilename.compareTo("") == 0 || doctype.compareTo("") == 0 ) {

%>
<jsp:forward page='errorpage.jsp' >
<jsp:param name="msg" value='' />
<jsp:param name="doctype" value='<%=doctype%>' />
<jsp:param name="docfilename" value='<%=docfilename%>' />
<jsp:param name="docdesc" value='<%=docdesc%>' />

</jsp:forward>
<%

}
else {  

	    ResultSet rsdemo2 = null;
	    int count1 = 0;
		 	          	     String[] param4=new String[4];
		 	          	    	
		 	          	    	   
		 	          	    		   param4[0] = docfilename;
					                   param4[1] = "";
					                   param4[2] = "";
     							   param4[3] = "";
		 	          
		 	          	    rsdemo2 = apptMainBean.queryResults(param4, "search_document");
		 	             while (rsdemo2.next()) {   
		 	           
		 	             count1 = count1 + 1;
		 	          }
             if ( count1 > 0){
             %>
             
             <jsp:forward page='documentList.jsp' >
	     <jsp:param name="orderby" value='updatedatetime desc' />
	     <jsp:param name="creator" value='<%=doccreator%>' />
	     <jsp:param name="doctype" value='' />
	     <jsp:param name="docdesc" value='' />
	     <jsp:param name="docfilename" value='' />
</jsp:forward>
             
             
       <%
             }
             else {
             
             


    String[] param =new String[7];
	  param[0]=doctype;
	  param[1]=docdesc;
	  param[2]=docxml;
	  param[3]=docfilename;
	  param[4]=doccreator;
	  param[5]=docdate;
	  param[6]="A";
	  int rowsAffected = apptMainBean.queryExecuteUpdate(param,"save_document");
	    
	          String docNo = null;
	         	 
		 	          	    ResultSet rsdemo = null;
		 	          	     String[] param3 =new String[4];
		 	          	    	
		 	          	    	   
		 	          	    	  param3[0]=docfilename;
		 	          	    	  param3[1]="";
		 	          	    	  param3[2]="";
		 	          	    	  param3[3]="";
		 	          
		 	          	    rsdemo = apptMainBean.queryResults(param3, "search_document");
		 	             while (rsdemo.next()) {   
		 	             docNo = rsdemo.getString("document_no");
		 	          }
		 	       
		 	       
		 	       int recordAffected=0;
		 	      
		 	     
		 	           String[] param2 = new String[4];
		 	           param2[0] = module;
		 	           param2[1] = module_id;
		 	           param2[2] = docNo;
		 	           param2[3] = "A";
		 	          
		 	           
	           recordAffected = apptMainBean.queryExecuteUpdate(param2,"save_ctl_document");
	           
	    
	    




%>
<jsp:forward page='documentList.jsp' >
<jsp:param name="orderby" value='updatedatetime desc' />
<jsp:param name="creator" value='<%=doccreator%>' />
<jsp:param name="doctype" value='' />
<jsp:param name="docdesc" value='' />
<jsp:param name="docfilename" value='' />
</jsp:forward>
<%
}
}

%> 
</html:html>
