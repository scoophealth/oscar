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

 <%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"  %>
<%@ include file="../../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<%@ include file="dbDxResearch.jsp" %>
<%

GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay);
String module="", module_id="", doctype="", docdesc="", docxml="", doccreator="", docdate="", docfilename="";
int Count = 0;
         
          String dxcode ="";
    for (int i=1; i<6; i++){
    dxcode=request.getParameter("xml_research"+i)==null?"":request.getParameter("xml_research"+i);
    if (dxcode.compareTo("")!=0){

 ResultSet rsdemo2 = null;
      	  	  
      	  		 	          	     String[] param4=new String[2];
      	  		 	          	    	
      	  		 	          	    	       param4[0]=request.getParameter("demographicNo");
      	  		 	          	    		   param4[1] = dxcode;
      	  		 	          
      	  		 	          	   rsdemo2 = apptMainBean.queryResults(param4, "search_dxresearch_bycode");
      	  		 	             if(rsdemo2==null) {   

   }else{
	    while(rsdemo2.next()){
	    Count = Count +1;
	        String[] param =new String[3];
	    	  param[0]=nowDate;
	    	  param[1]="A";
	    	  param[2]=rsdemo2.getString("dxresearch_no");
	    	  int rowsAffected = apptMainBean.queryExecuteUpdate(param,"update_dxresearch");
	    	    
	    }}
	    
	    if (Count == 0){
	        String[] param =new String[5];
	              param[0]=request.getParameter("demographicNo");
	    	  param[1]=nowDate;
	    	  param[2]=nowDate;
	    	  param[3]="A";
	    	  param[4]=dxcode;
	    	  int rowsAffected = apptMainBean.queryExecuteUpdate(param,"save_dxresearch_code");
	    }	    
	 
	    }
	            
	    }




%>

             <jsp:forward page='dxResearch.jsp' >
	     <jsp:param name="demographicNo" value='<%=request.getParameter("demographicNo")%>' />
	   
</jsp:forward>