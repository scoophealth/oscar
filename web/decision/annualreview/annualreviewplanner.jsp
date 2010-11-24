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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<%
  if (session.getAttribute("user") == null) {
    response.sendRedirect("../logout.jsp");
  }

  String demographic_no = request.getParameter("demographic_no") != null
                          ? request.getParameter("demographic_no")
                          : ("null");
  String form_no        = request.getParameter("formId") != null
                          ? request.getParameter("formId")
                          : ("0");
  String curUser_no     = (String)session.getAttribute("user");
%>
<%@ page errorPage="../../appointment/errorpage.jsp"
	import="java.util.*,
                                                              java.sql.*,
                                                              oscar.*,
                                                              oscar.util.*"%>
<jsp:useBean id="plannerBean" class="oscar.AppointmentMainBean"
	scope="page" />
<jsp:useBean id="riskDataBean" class="java.util.Properties" scope="page" />
<!--jsp:useBean id="risks" class="oscar.decision.DesAnnualReviewPlannerRisk" scope="page" /-->
<jsp:useBean id="risks"
	class="oscar.decision.DesAntenatalPlannerRisks_99_12" scope="page" />
<jsp:useBean id="checklist"
	class="oscar.decision.DesAnnualReviewPlannerChecklist" scope="page" />
<%@ include file="../../admin/dbconnection.jsp"%>
<%
  String[][] dbQueries = new String[][]{{"search_demographic", 
          "select sex,year_of_birth,month_of_birth,date_of_birth from demographic where demographic_no = ?"}, {"search_des", 
          "select * from desannualreviewplan where form_no <= ? and demographic_no = ? order by form_no desc, des_date desc, des_time desc limit 1 "
          }, {"save_des", 
          "insert into desannualreviewplan (des_date,des_time,provider_no,risk_content,checklist_content,demographic_no,form_no) values (?,?,?,?,?,?,? ) "
          }};

  plannerBean.doConfigure(dbQueries);
%>
<html><head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ANNUAL HEALTH REVIEW PLANNER</title>
<script type="text/javascript" language="Javascript">
        function onExit() {
          if (confirm("Are you sure you wish to exit without saving your changes?") == true) {
            window.close();
          }

          return (false);
        }

        function popupPage(vheight, vwidth, varpage) {
          //open a new popup window
          var page = "" + varpage;

          windowprops = "height=" + vheight + ",width=" + vwidth + 
                  ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=5,left=5";

          //360,680
          var popup = window.open(page, "editxml", windowprops);
        }
      </script>
</head>
<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">
<form name="planner" method="post"
	action="annualreviewplanner.jsp?demographic_no=<%=demographic_no%>&formId=<%=form_no%>">
<!--%@ include file="zgetarriskdata.jsp" % --> <%
      //save risk&checklist data if required
      if (request.getParameter("submit") != null && 
              (request.getParameter("submit").equals(" Save ") || request.getParameter("submit").equals("Save and Exit"))) {
        GregorianCalendar now          = new GregorianCalendar();
        String            form_date    = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(
                Calendar.DAY_OF_MONTH);
        String            form_time    = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(
                Calendar.SECOND);
        String            risk_content = "", checklist_content = "";

        risk_content = SxmlMisc.createXmlDataString(request, "risk_");
        checklist_content = SxmlMisc.createXmlDataString(request, "checklist_");

        String[] param        = {form_date, form_time, curUser_no, risk_content, checklist_content, demographic_no, form_no};
        int      rowsAffected = plannerBean.queryExecuteUpdate(param, "save_des");
      }

      //save & exit if required
      if (request.getParameter("submit") != null && request.getParameter("submit").equals("Save and Exit")) {
        out.print("<script type='text/javascript' language='Javascript'>window.close();</script>");

        return;
      }

      //initial prop bean with "0"
      //  for(int i=0;i<riskdataname.length;i++) {
      //	  riskDataBean.setProperty(riskdataname[i][0],"0");
      //  }
      //get the risk data from table desaprisk for other risk factors
      String[]  param2 = {form_no, demographic_no};
      ResultSet rsdemo = plannerBean.queryResults(param2, "search_des");

      while (rsdemo.next()) {
        String risk_content      = rsdemo.getString("risk_content");
        String checklist_content = rsdemo.getString("checklist_content");
%> <xml id="xml_list"> <planner> <%= risk_content %> <%= checklist_content %>
</planner> </xml> <%
        //set the riskdata bean from xml file
        Properties   savedar1risk1 = risks.getRiskName("../../decision/annualreview/desannualreviewplannerrisk.xml");
        StringBuffer tt;

        for (Enumeration e = savedar1risk1.propertyNames(); e.hasMoreElements(); ) {
          tt = new StringBuffer().append(e.nextElement());

          if (SxmlMisc.getXmlContent(risk_content, "risk_"+tt.toString()) != null) {
            riskDataBean.setProperty(tt.toString(), savedar1risk1.getProperty(tt.toString()));
          }
        }
      }
      //find the age and sex of the patient from demographic table
      rsdemo = plannerBean.queryResults(demographic_no, "search_demographic");

      int    age = 0;
      String sex = null;

      while (rsdemo.next()) {
        age = UtilDateUtilities.calcAge(rsdemo.getString("year_of_birth"), rsdemo.getString("month_of_birth"), 
                rsdemo.getString("date_of_birth"));
        sex = rsdemo.getString("sex");
      }

      if (age >= 65 && age <= 85) {
        riskDataBean.setProperty("999", "checked");

        if (sex.equals("f") || sex.equals("F")) {
          riskDataBean.setProperty("998", "checked");
        }

        if (sex.equals("m") || sex.equals("M")) {
          riskDataBean.setProperty("997", "checked");
        }

        if (age >= 65 && age <= 69) {
          riskDataBean.setProperty("996", "checked");
        }
      }
%>
<table bgcolor='silver' width='100%'>
	<tr>
		<td align="left"><input type="submit" name="submit"
			value=" Save " /> <input type="submit" name="submit"
			value="Save and Exit" /> <input type="button" value="  Exit  "
			onclick="javascript:return onExit();" /> <input type="button"
			name="submit" value="Print"
			onclick="popupPage(700,800,'annualreviewplannerprint.jsp?demographic_no=<%=demographic_no%>&formId=<%=form_no%>');return false;" />
		</td>
		<td align="right"><a href=#
			onClick="popupPage(600,930,'riskedit.jsp');return false;"> Edit
		Risk </a> | <a href=#
			onClick="popupPage(600,930,'checklistedit.jsp');return false;">
		Edit CheckList </a></td>
	</tr>
</table>
<table bgcolor='silver' width='100%'>
	<tr>
		<td width="20%" valign='top'>
		<%
            out.println(risks.doStuff(oscarVariables.getProperty("tomcat_path") +"webapps/"+ oscarVariables.getProperty("project_home") + "/decision/annualreview/desannualreviewplannerrisk.xml"));
%>
		</td>
		<td>
		<%
            out.println(checklist.doStuff(oscarVariables.getProperty("tomcat_path") +"webapps/"+ oscarVariables.getProperty("project_home") + "/decision/annualreview/desannualreviewplannerriskchecklist.xml", riskDataBean));
%>
		
	</tr>
</table>
<table bgcolor='silver' width='100%'>
	<tr>
		<td align="left"><input type="submit" name="submit"
			value=" Save " /> <input type="submit" name="submit"
			value="Save and Exit" /> <input type="button" value="  Exit  "
			onclick="javascript:return onExit();" /> <input type="button"
			name="submit" value="Print"
			onclick="popupPage(700,800,'annualreviewplannerprint.jsp?demographic_no=<%=demographic_no%>&formId=<%=form_no%>');return false;" />
		</td>
		<td align="right"><a href=#
			onClick="popupPage(600,930,'riskedit.jsp');return false;"> Edit
		Risks </a> | <a href=#
			onClick="popupPage(600,930,'checklistedit.jsp');return false;">
		Edit CheckList </a></td>
	</tr>
</table>
</form>
</body>
</html>
