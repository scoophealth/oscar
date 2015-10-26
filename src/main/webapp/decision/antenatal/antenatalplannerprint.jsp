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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%

  String demographic_no = request.getParameter("demographic_no")!=null?request.getParameter("demographic_no"):("null") ;
  String form_no = request.getParameter("formId")!=null?request.getParameter("formId"):("0") ;
  String curUser_no = (String) session.getAttribute("user");
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*,java.io.*"
	errorPage="../../appointment/errorpage.jsp"%>

<jsp:useBean id="riskDataBean" class="java.util.Properties" scope="page" />
<jsp:useBean id="risks"
	class="oscar.decision.DesAntenatalPlannerRisks_99_12" scope="page" />
<jsp:useBean id="checklist"
	class="oscar.decision.DesAntenatalPlannerChecklist_99_12" scope="page" />
<%@ include file="../../admin/dbconnection.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Desaprisk" %>
<%@page import="org.oscarehr.common.dao.DesapriskDao" %>
<%
	DesapriskDao desapriskDao = SpringUtils.getBean(DesapriskDao.class);
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Antenatal Planner</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script type="text/javascript" language="Javascript">

</script>
</head>
<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">
<%-- @ include file="zgetarriskdata.jsp" --%>
<%
  //get the risk data from formAR1
  String finalEDB = null, wt=null, ht=null;
  String patientName = null;

  ResultSet rsdemo = null ;
  if(!form_no.equals("0")) {
	  rsdemo = oscar.oscarDB.DBHandler.GetSQL("select * from formAR where ID = " + form_no);
	 
      ResultSetMetaData resultsetmetadata = rsdemo.getMetaData();
      while (rsdemo.next()) {
          finalEDB = rsdemo.getString("c_finalEDB");
          patientName = rsdemo.getString("c_pName");
	      wt = rsdemo.getString("pg1_wt");
	      ht = rsdemo.getString("pg1_ht");
          for(int k = 1; k <= resultsetmetadata.getColumnCount(); k++) {
              //String name = resultsetmetadata.getColumnName(k);
              //String value = null;
              if(resultsetmetadata.getColumnTypeName(k).equalsIgnoreCase("TINY")) {
                  if(rsdemo.getInt(k) == 1) riskDataBean.setProperty(resultsetmetadata.getColumnName(k), "checked"); //"55", "risk_cinca"
              }
          }
      }
  }

  //get the risk data from table desaprisk for other risk factors
  Desaprisk darp = desapriskDao.search(Integer.parseInt(form_no),Integer.parseInt(demographic_no));

  if (darp != null) {
    String risk_content = darp.getRiskContent();
    String checklist_content = darp.getChecklistContent();
%>
<xml id="xml_list">
<planner>
<%=risk_content%>
<%=checklist_content%>
</planner>
</xml>
<%


    String riskFilePath = "../webapps/"+oscarVariables.getProperty("project_home")+"/decision/antenatal/desantenatalplannerrisks_99_12.xml";

    File file = new File(OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"desantenatalplannerrisks_99_12.xml");
    if(file.isFile() || file.canRead()) {
        riskFilePath = OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"desantenatalplannerrisks_99_12.xml";
    }


    //set the riskdata bean from xml file
    Properties savedar1risk1 = risks.getRiskName(riskFilePath); //risk_55
  	StringBuffer tt;

    for (Enumeration e = savedar1risk1.propertyNames() ; e.hasMoreElements() ;) {
      tt = new StringBuffer().append(e.nextElement());
      if(SxmlMisc.getXmlContent(risk_content, "risk_"+tt.toString())!= null) {
        riskDataBean.setProperty(tt.toString(), "checked");
	  }
    }
  }
%>
<table bgcolor='silver' width='100%' cellspacing=0 cellpadding=0>
	<tr>
		<td><font color='blue'><%=patientName%> | EDB: <%=finalEDB%></font></td>
		<td align="right"><input type="button" name="submit"
			value="Print" onclick="window.print();" /> <input type="button"
			value="  Exit  " onclick="javascript:window.close();" /></td>
	</tr>
</table>

<%
if(finalEDB==null || finalEDB=="") out.println("************No EDB, no check list!**************");
else {
  riskDataBean.setProperty("finalEDB", finalEDB);
    if (wt!= null && ht!= null && !wt.equals("") && !ht.equals("") ) {
	    boolean bNum = true;
	    for(int ii=0; ii<wt.length();ii++) {
        if (wt.charAt(ii) == '.') { ii++; continue; }
	  	  if ((wt.charAt(ii) < '0') || (wt.charAt(ii) > '9') ) {
		    	bNum = false;
			    break;
		    }
		  }
	    for(int ii=0; ii<ht.length();ii++) {
        if (ht.charAt(ii) == '.') { ii++; continue; }
		    if ((ht.charAt(ii) < '0') || (ht.charAt(ii) > '9') ) {
			    bNum = false;
			    break;
		    }
		  }
	    if(bNum) {
	      if(Float.parseFloat(wt)<=150 && Float.parseFloat(wt)>=40 && Float.parseFloat(ht)<=3 && Float.parseFloat(ht)>=1) {
          float bmi = Float.parseFloat(wt) / Float.parseFloat(ht) * Float.parseFloat(ht);
          if ((bmi > 0) && (bmi < 20)) { riskDataBean.setProperty("97", "checked"); }
          if ((bmi > 19) && (bmi < 26)) { riskDataBean.setProperty("98", "checked"); }
          if (bmi > 25) { riskDataBean.setProperty("99", "checked");  }
		    }
		  }
    }

    String checkListFilePath = "../webapps/"+oscarVariables.getProperty("project_home")+"/decision/antenatal/desantenatalplannerchecklist_99_12.xml";

    File file = new File(OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"desantenatalplannerchecklist_99_12.xml");
    if(file.isFile() || file.canRead()) {
        checkListFilePath = OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"desantenatalplannerchecklist_99_12.xml";
    }

  out.println(checklist.doStuff(new String(checkListFilePath), riskDataBean));

}
%>

</body>
</html>
