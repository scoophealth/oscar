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

<%@ page language="java" %>
<%@ page import="java.util.*,oscar.oscarReport.data.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">
<%
if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");
int count = 0;

String bgcolor = "#ddddff";
String years = "2003";
String pros = "";
if (request.getParameter("numMonth") != null ){
years = (String) request.getParameter("numMonth");
}

if (request.getParameter("proNo") != null ){
pros = (String) request.getParameter("proNo");
}

oscar.oscarReport.data.RptFluReportData fluData  = new oscar.oscarReport.data.RptFluReportData();
fluData.fluReportGenerate(pros,years);
ArrayList proList = fluData.providerList();
%>
<%!
  String selled (String i,String years){
         String retval = "";
         if ( i.equals(years) ){
            retval = "selected";
         }
     return retval;
  }
%>



<html:html locale="true">
<head>
<title>
<bean:message key="oscarReport.oscarReportFluBilling.title"/> <%= years %>
</title>
<style type="text/css">
   td.nameBox {
      border-bottom: 1pt solid #888888;
      font-family: tahoma, helvetica; ;
      font-size: 12pt;
   }
   td.sideLine {
      border-right: 1pt solid #888888;
   }
   td.fieldBox {
      font-family: tahoma, helvetica;
   }
   th.subTitles{
      font-family: tahoma, helvetica ;
      font-size:10pt;
   }
</style>

<script type="text/javascript">
   var remote=null;

   function rs(n,u,w,h,x) {
      args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
      remote=window.open(u,n,args);
     // if (remote != null) {
     //    if (remote.opener == null)
     //        remote.opener = self;
     // }
     // if (x == 1) { return remote; }
   }

   function popupOscarFluConfig(vheight,vwidth,varpage) { //open a new popup window
     var page = varpage;
     windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
     var popup=window.open(varpage, "OscarFluConfig", windowprops);
     if (popup != null) {
       if (popup.opener == null) {
        popup.opener = self;
       }
    }
  }
</script>

</head>

<body class="BodyStyle" vlink="#0000FF" >
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <bean:message key="oscarReport.oscarReportFluBilling.msgReport"/>
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar" >
                 <form action="oscarReportFluBilling.jsp">
                    <tr>
                        <td ><bean:message key="oscarReport.oscarReportFluBilling.msgFluReport"/></td>
                        <td>
                           <select name="numMonth">
                              <option value="2001" <%=selled("2001",years)%> >2001</option>
                              <option value="2002" <%=selled("2002",years)%> >2002</option>
                              <option value="2003" <%=selled("2003",years)%> >2003</option>
                              <option value="2004" <%=selled("2004",years)%> >2004</option>
                              <option value="2005" <%=selled("2005",years)%> >2005</option>

                           </select>
                           <select name="proNo">
                                 <option value="-1" <%=selled("-1",pros)%> ><bean:message key="oscarReport.oscarReportFluBilling.msgAllProviders"/></option>
                              <%
                                  for( int i = 0; i < proList.size(); i++){
                                     ArrayList w = (ArrayList) proList.get(i);
                                     String proNum  = (String) w.get(0);
                                     String proName = (String) w.get(1);
                              %>
                                  <option value="<%=proNum%>" <%=selled(proNum,pros)%>  ><%=proName%></option>
                              <% 
                                  }
                              %>
                           </select>
                           <input type=submit value="<bean:message key="oscarReport.oscarReportFluBilling.btnUpdate"/>"/>
                        </td>
                        <td style="text-align:right">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  ><bean:message key="global.help"/></a> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about"/></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license"/></a>
                        </td>
                    </tr>
                  </form>
                </table>
            </td>
        </tr>
        </table>
        
                 <table border=0 cellspacing=3 >
                 <tr>
	                  <td colspan=7 class=nameBox><bean:message key="oscarReport.oscarReportFluBilling.msgTitle"/></td>
	         </tr>
	         <tr>
	                     <th width=100 class="subTitles" align=left><bean:message key="oscarReport.oscarReportFluBilling.msgName"/></th>
	                     <th width=120 class="subTitles" align=left><bean:message key="oscarReport.oscarReportFluBilling.msgDOB"/></th>
	                     <th width=80 class="subTitles" align=left><bean:message key="oscarReport.oscarReportFluBilling.msgAge"/></th>
	                      <th width=100 class="subTitles" align=left><bean:message key="oscarReport.oscarReportFluBilling.msgRoster"/></th>
	                           <th width=100 class="subTitles" align=left><bean:message key="oscarReport.oscarReportFluBilling.msgPatientStatus"/></th>
	                  <th width=100 class="subTitles" align=left><bean:message key="oscarReport.oscarReportFluBilling.msgPhone"/></th>
	                     
	                     <th width=100 class="subTitles" align=left><bean:message key="oscarReport.oscarReportFluBilling.msgBillingDate"/></th>
	                  </tr>
	                  <%
	                  RptFluReportData.DemoFluDataStruct demoData;
	             for (int i = 0; i < fluData.demoList.size(); i++ ){
demoData = (RptFluReportData.DemoFluDataStruct)  fluData.demoList.get(i);
if (demoData.getBillingDate().compareTo("&nbsp;")==0) {
bgcolor="#ffffcc";
}else{
bgcolor = "#ddddff";
}
  count = count + 1;
	                  %>
	                  <tr>
	                     <td class="fieldBox" bgcolor="<%=bgcolor%>"><%=demoData.demoName%></td></a>
	                     <td class="fieldBox" bgcolor="<%=bgcolor%>"><%=demoData.getDemoDOB()%></td>
	                     <td class="fieldBox" bgcolor="<%=bgcolor%>"><%=demoData.getDemoAge()%></td>
	                     	                     <td class="fieldBox" bgcolor="<%=bgcolor%>"><%=demoData.demoRosterStatus %></td>
	                     <td class="fieldBox" bgcolor="<%=bgcolor%>"><%=demoData.demoPatientStatus %></td>
	          
	                     <td class="fieldBox" bgcolor="<%=bgcolor%>"><%=demoData.getDemoPhone() %></td>
	                     <td class="fieldBox" bgcolor="<%=bgcolor%>"><%=demoData.getBillingDate() %></td>
	                  </tr>
	                  <%
	                  }
  if (count == 0){

     %>
         <tr>
     	                  <td colspan=7 class=nameBox><bean:message key="oscarReport.oscarReportFluBilling.msgNoMatch"/></td>
	         </tr>
	         <% } %>
</body>
</html:html>
