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
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="w" reverse="<%=true%>">
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
  String query_name = request.getParameter("query_name")!=null?request.getParameter("query_name"):("") ;
  String curUser_no = (String) session.getAttribute("user");

%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*,java.io.*" %>
<jsp:useBean id="riskDataBean" class="java.util.Properties" scope="page" />
<jsp:useBean id="risks" class="oscar.decision.DesAntenatalPlannerRisks_99_12" scope="page" />
<jsp:useBean id="checklist" class="oscar.decision.DesAntenatalPlannerChecklist_99_12" scope="page" />
<%@ include file="../../admin/dbconnection.jsp" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Desaprisk" %>
<%@page import="org.oscarehr.common.dao.DesapriskDao" %>
<%
	DesapriskDao desapriskDao = SpringUtils.getBean(DesapriskDao.class);
%>


<html>
<head>
    <title>Antenatal Planner</title>
<script type="text/javascript" language="Javascript">
function onExit() {
  if(confirm("Are you sure you wish to exit without saving your changes?")==true)  {
    window.close();
  }
  return(false);
}
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=5,left=5";//360,680
  var popup=window.open(page, "editxml", windowprops);
}
</script>
</head>
<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">
<form name="planner" method="post" action="antenatalplanner.jsp?demographic_no=<%=demographic_no%>&formId=<%=form_no%>" >
<%-- @ include file="zgetarriskdata.jsp" --%>
<%
  //save risk&checklist data if required
  if(request.getParameter("submit")!=null && (request.getParameter("submit").equals(" Save ") || request.getParameter("submit").equals("Save and Exit")) ) {
    String risk_content="", checklist_content="";
    risk_content=SxmlMisc.createXmlDataString(request, "risk_");
    checklist_content=SxmlMisc.createXmlDataString(request, "checklist_");
    Desaprisk darp = new Desaprisk();
    darp.setDesapriskDate(new java.util.Date());
    darp.setDesapriskTime(new java.util.Date());
    darp.setProviderNo(curUser_no);
    darp.setRiskContent(risk_content);
    darp.setChecklistContent(checklist_content);
    darp.setDemographicNo(Integer.parseInt(demographic_no));
    darp.setFormNo(Integer.parseInt(form_no));
    desapriskDao.persist(darp);
  }

  //save & exit if required
  if(request.getParameter("submit")!=null && request.getParameter("submit").equals("Save and Exit") ) {
    out.print("<script type='text/javascript' language='Javascript'>window.close();</script>");
    return;
  }

  
  //get the risk data from formAR1
  String finalEDB = null, wt=null, ht=null;

  ResultSet rsdemo = null ;
  if(!form_no.equals("0")) {
	  //we don't have forms converted at this time
	  rsdemo = oscar.oscarDB.DBHandler.GetSQL("select * from formONAR where ID = " + form_no);
     
      ResultSetMetaData resultsetmetadata = rsdemo.getMetaData();
      while (rsdemo.next()) {
          finalEDB = rsdemo.getString("c_finalEDB");
	      wt = rsdemo.getString("pg1_wt");
	      ht = rsdemo.getString("pg1_ht");
          for(int k = 1; k <= resultsetmetadata.getColumnCount(); k++) {
              //String name = resultsetmetadata.getColumnName(k);
              //String value = null;
              if(resultsetmetadata.getColumnTypeName(k).equalsIgnoreCase("TINY")) {
                  if(rsdemo.getInt(k) == 1) riskDataBean.setProperty(resultsetmetadata.getColumnName(k), "checked"); //"55", "risk_cinca"

%>
<input type="hidden" name="<%=resultsetmetadata.getColumnName(k)%>" value="checked" >
<%            }
          }
      }
  }
  //get the risk data from table desaprisk for other risk factors
  Desaprisk darp = desapriskDao.search(Integer.parseInt(form_no),Integer.parseInt(demographic_no));

  HashMap dataMap = new HashMap();
  if (darp != null) {
    String risk_content = darp.getRiskContent();
    String checklist_content = darp.getChecklistContent();
%>
<script type="text/javascript">
   xmlText = "<xml><planner><%=risk_content%><%=checklist_content%></planner></xml>";
</script>
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
<table bgcolor='silver' width='100%'>
  <tr>
    <td align="left">
    <input type="submit" name="submit" value=" Save "  />
    <input type="submit" name="submit" value="Save and Exit"  />
    <input type="button" value="  Exit  "  onclick="javascript:return onExit();" />
    <input type="button" name="submit" value="Print" onclick="popupPage(700,800,'antenatalplannerprint.jsp?demographic_no=<%=demographic_no%>&formId=<%=form_no%>');return false;" />
    </td>
    <td align="right">
      <a href=# onClick ="popupPage(600,930,'obarriskedit_99_12.jsp');return false;">Edit OB Risks</a> |
      <a href=# onClick ="popupPage(600,930,'obarchecklistedit_99_12.jsp');return false;">Edit CheckList</a>
    </td>
  </tr>
</table>

<table bgcolor='silver' width='100%'>
  <tr>
    <td width="10%" valign='top'>
<%
    String riskFilePath = "../webapps/"+oscarVariables.getProperty("project_home")+"/decision/antenatal/desantenatalplannerrisks_99_12.xml";

    File file = new File(OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"/desantenatalplannerrisks_99_12.xml");
    if(file.isFile() || file.canRead()) {
        riskFilePath = OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"/desantenatalplannerrisks_99_12.xml";
    }

    out.println(risks.doStuff(new String(riskFilePath)));
%>
    </td><td>
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

    file = new File(OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"/desantenatalplannerchecklist_99_12.xml");
    if(file.isFile() || file.canRead()) {
        checkListFilePath = OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"/desantenatalplannerchecklist_99_12.xml";
    }

  out.println(checklist.doStuff(new String(checkListFilePath), riskDataBean));
}
%>
  </tr>
</table>
<table bgcolor='silver' width='100%'>
  <tr>
    <td align="left">
    <input type="submit" name="submit" value=" Save "  />
    <input type="submit" name="submit" value="Save and Exit"  />
    <input type="button" value="  Exit  "  onclick="javascript:return onExit();" />
    <input type="button" name="submit" value="Print" onclick="popupPage(700,800,'antenatalplannerprint.jsp?demographic_no=<%=demographic_no%>&formId=<%=form_no%>');return false;" />
    </td>
    <td align="right">
      <a href=# onClick ="popupPage(600,930,'obarriskedit_99_12.jsp');return false;">Edit OB Risks</a> |
      <a href=# onClick ="popupPage(600,930,'obarchecklistedit_99_12.jsp');return false;">Edit CheckList</a>
    </td>
  </tr>
</table>

</form>
    <script type="text/javascript">
        // code for IE
        //console.log(xmlText);
if (window.ActiveXObject)
  {
  var doc=new ActiveXObject("Microsoft.XMLDOM");
  doc.async="false";
  doc.loadXML(xmlText);
  }
// code for Mozilla, Firefox, Opera, etc.
else
  {
  var parser=new DOMParser();
  var doc=parser.parseFromString(xmlText,"text/xml");
  }

  //console.log(doc.nodeName+" "+doc.nodeType);



  var x=doc.getElementsByTagName("planner");

  //console.log(x.nodeName);
  for (var i=0;i < x.length; i++){
     //console.log("WHAT IS X "+x[i]);
     var childs = x[i].childNodes;
     //console.log("WHAT IS "+childs);
     for (var j=0; j < childs.length;j++){
        //console.log(childs[j].nodeName);
        var ele = document.getElementById(childs[j].nodeName);
        ele.checked = true;
     }
  }

    </script>
</body>
</html>
