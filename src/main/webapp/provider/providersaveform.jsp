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
  if(request.getParameter("cmd")!=null && request.getParameter("cmd").compareTo("Print Preview")==0) {
    //    response.sendRedirect("form"+request.getParameter("form_name")+ "print.jsp");
    if(true) {
      out.clear();
      pageContext.forward("form"+request.getParameter("form_name")+ "print.jsp"); //forward request&response to the target page
      return;
    }
  }
%>
<%@ page import="java.sql.*, java.util.*, oscar.*, java.net.*"
	errorPage="errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.DemographicAccessoryDao" %>
<%@page import="org.oscarehr.common.model.DemographicAccessory" %>
<%
	DemographicAccessoryDao demographicAccessoryDao = (DemographicAccessoryDao)SpringUtils.getBean("demographicAccessoryDao");
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    //-->
</script>
</head>
<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">ADD
		AN ENCOUNTER/FORM RECORD</font></th>
	</tr>
</table>
<%
  GregorianCalendar now=new GregorianCalendar();
  String form_date =now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH);
  String form_time =now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);

  String content="";//default is not null temp=null, subject="",
  content=SxmlMisc.createXmlDataString(request, "xml_");

  //save form
  String[] param =new String[6];
  param[0]=request.getParameter("demographic_no");
  param[1]=request.getParameter("user_no");
  param[2]=form_date;
  param[3]=form_time; //MyDateFormat.getTimeXX_XX_XX(request.getParameter("form_time"));
  param[4]=request.getParameter("form_name");
  param[5]=content;
  int rowsAffected = oscarSuperManager.update("providerDao", request.getParameter("dboperation"), param);

  if (rowsAffected == 1) { //if success
    String[] param2 = new String[2];
    param2[0]=request.getParameter("demographic_no");
    param2[1]=request.getParameter("form_name");
    List<Map<String,Object>> resultList = oscarSuperManager.find("providerDao", "search_form_no", param2);
    for (Map form : resultList) {
      //save as an encounter
      if(request.getParameter("formtype")!=null && request.getParameter("formtype").compareTo("direct")==0 ) {
        if(request.getParameter("cmd")!=null && request.getParameter("cmd").compareTo("Save & Exit")==0) {
         String[] param1 =new String[7];
         param1[0]=request.getParameter("demographic_no");
      	 param1[1]=form_date;
      	 param1[2]=form_time;
      	 param1[3]=request.getParameter("user_no");
      	 param1[4]="No Subject"+ " |"+ "Form:"+request.getParameter("form_name");
      	 param1[5]=""; //content
      	 param1[6]="<form>providercontrol.jsp?form_no=" +form.get("form_no")+ "&dboperation=search_form&displaymodevariable=form" +request.getParameter("form_name").toLowerCase()+ ".jsp&displaymode=vary&bNewForm=0</form>";
   	     int rowsAffected1 = oscarSuperManager.update("providerDao", "add_encounter", param1);
         if (rowsAffected1 != 1) break;
        }
        //save as an encounter attachment
        if(request.getParameter("cmd")!=null && request.getParameter("cmd").compareTo("Save & Enc")==0) {
          response.sendRedirect("providercontrol.jsp?appointment_no="+request.getParameter("appointment_no")+"&demographic_no="+request.getParameter("demographic_no")+"&curProvider_no=&status=T"+"&reason="+request.getParameter("reason")+"&appointment_date="+form_date+"&start_time="+form_time+"&displaymode=encounter&dboperation=search_demograph&template="+
            "&encounterattachment="+URLEncoder.encode("<form>form"+request.getParameter("form_name")+".jsp?form_no="+form.get("form_no")+"&bNewForm=0</form>") +
            "&attachmentdisplay=" +request.getParameter("xml_subject") );
        }

        //update or add a record to the demoaccess
        String content1=null;
        String alert = SxmlMisc.replaceHTMLContent(request.getParameter("xml_Alert_demographicaccessory"));  //SxmlMisc.getXmlContent(endWith("_demographicaccessory"));
        String medication = SxmlMisc.replaceHTMLContent(request.getParameter("xml_Medication_demographicaccessory"));
        int rowsAffected2=0;
        String[] param3 = new String[2];

        DemographicAccessory da = demographicAccessoryDao.find(Integer.parseInt(request.getParameter("demographic_no")));
        if(da != null) {
        	 content1 = da.getContent();
             content1=SxmlMisc.replaceOrAddXmlContent(content1, "<xml_Alert>","</xml_Alert>",alert);
             content1=SxmlMisc.replaceOrAddXmlContent(content1, "<xml_Medication>","</xml_Medication>",medication);
             da.setContent(content1);
             demographicAccessoryDao.merge(da);
             rowsAffected2=1;
        } else {
        	 da = new DemographicAccessory();
        	 da.setDemographicNo(Integer.parseInt(request.getParameter("demographic_no")));
        	 da.setContent("<xml_Alert>" +alert+ "</xml_Alert>"+"<xml_Medication>" +medication+ "</xml_Medication>");
        	 demographicAccessoryDao.persist(da);
        	 rowsAffected2=1;
        }


        if (rowsAffected2 ==1) out.println("<script LANGUAGE='JavaScript'>self.close();</script>");
        break;
      } //end if

%> <!--p><h1>Successful Addition of an appointment Record.</h1></p-->
<script LANGUAGE="JavaScript">
      self.close();
      self.opener.document.encounter.encounterattachment.value +="<%=URLEncoder.encode("<form>form"+request.getParameter("form_name")+".jsp?form_no="+form.get("form_no")+"&bNewForm=0</form>") %>";
      self.opener.document.encounter.attachmentdisplay.value += "<%=request.getParameter("xml_subject")%> "; //form_name")%> ";
     	//self.opener.refresh();
</script>
<%
      break; //get only one form_no
    }//end of while
  }  else {
%>
<p>
<h1>Sorry, addition has failed.</h1>
</p>
<%
  }
%>
<p></p>
<hr width="90%"/>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>
