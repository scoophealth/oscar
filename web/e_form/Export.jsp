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

<jsp:useBean id="beanDBOperator" scope="session" class="bean.DBOperator" />
<jsp:useBean id="beanUtility" scope="session" class="bean.Utility_old" /> 
<%  
  if(session.getValue("user") == null)  response.sendRedirect("../logout.jsp");

   String delemitor; 
 
    int fid = new Integer(request.getParameter("fid")).intValue();
      

   if (request.getParameter("delemitor").length()!=0){
      delemitor = request.getParameter("delemitor");
   }else {
      delemitor = "|";
   }  

    String startdate = request.getParameter("startdate");
    String enddate = request.getParameter("enddate");
    String saveString = beanDBOperator.getFormfields(fid,startdate,enddate,delemitor);
    saveString = beanUtility.moveQuote(saveString); 

 
%>
 
<script language="javascript">
 function saveIt(saveString) {
   msgWindow=window.open("","displayWindow","menubar=no,scrollbars=no,status=no,width=1,height=1");
   msgWindow.document.write("<"+saveString+">");
   msgWindow.document.execCommand("SaveAs");
   msgWindow.close();
  }

  saveIt('<%=saveString%>');

  window.close();

</script> 
 

  
 