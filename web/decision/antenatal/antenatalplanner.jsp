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

<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String demographic_no = request.getParameter("demographic_no")!=null?request.getParameter("demographic_no"):("null") ;
  String form_no = request.getParameter("formId")!=null?request.getParameter("formId"):("0") ;
  String query_name = request.getParameter("query_name")!=null?request.getParameter("query_name"):("") ;
  String curUser_no = (String) session.getAttribute("user");
  
  //System.out.println("demo "+demographic_no+" form_no "+form_no+" queryname "+query_name+" curUser "+curUser_no);
  
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*,java.io.*" %>
<jsp:useBean id="plannerBean" class="oscar.AppointmentMainBean" scope="page" />
<jsp:useBean id="riskDataBean" class="java.util.Properties" scope="page" />
<jsp:useBean id="risks" class="oscar.decision.DesAntenatalPlannerRisks_99_12" scope="page" />
<jsp:useBean id="checklist" class="oscar.decision.DesAntenatalPlannerChecklist_99_12" scope="page" />
<%@ include file="../../admin/dbconnection.jsp" %>
<% 
String [][] dbQueries=new String[][] { 
{"search_formarrisk", "select * from formAR where ID = ?" }, 
{"search_formonarrisk", "select * from formONAR where ID = ?" }, 
{"search_desaprisk", "select * from desaprisk where form_no <= ? and demographic_no = ? order by form_no desc, desaprisk_date desc, desaprisk_time desc limit 1 " }, 
{"save_desaprisk", "insert into desaprisk (desaprisk_date,desaprisk_time,provider_no,risk_content,checklist_content,demographic_no,form_no) values (?,?,?,?,?,?,? ) " }, 
};
plannerBean.doConfigure(dbQueries);
//System.out.println("TOP");
%>
 
<html>
<% response.setHeader("Cache-Control","no-cache");%>
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
    GregorianCalendar now=new GregorianCalendar();
    String form_date =now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH);
    String form_time =now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);
    String risk_content="", checklist_content="";
    risk_content=SxmlMisc.createXmlDataString(request, "risk_");
    checklist_content=SxmlMisc.createXmlDataString(request, "checklist_");
    String[] param = {form_date,form_time,curUser_no,risk_content,checklist_content,demographic_no,form_no};
    int rowsAffected = plannerBean.queryExecuteUpdate(param, "save_desaprisk" );
  }

  //save & exit if required
  if(request.getParameter("submit")!=null && request.getParameter("submit").equals("Save and Exit") ) {
    out.print("<script type='text/javascript' language='Javascript'>window.close();</script>");
    return;
  }

  //initial prop bean with "0" 
  //for(int i=0;i<riskdataname.length;i++) {
	//  riskDataBean.setProperty(riskdataname[i][0],"0");
  //}
//System.out.println("HERHE 2");
  //get the risk data from formAR1
  String finalEDB = null, wt=null, ht=null;
  
  ResultSet rsdemo = null ;
  if(!form_no.equals("0")) {
      //if(query_name.equalsIgnoreCase("search_formonarrisk") ) {
          rsdemo = plannerBean.queryResults(form_no, "search_formonarrisk");
      //} else {
     //         rsdemo = plannerBean.queryResults(form_no, "search_formarrisk");                  
     // }
          
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
//System.out.println("ABOVE HASHMAP form "+form_no+ " demographic " +demographic_no);
  //get the risk data from table desaprisk for other risk factors
  String[] param2 = {form_no, demographic_no};
  rsdemo = plannerBean.queryResults(param2, "search_desaprisk");
  HashMap dataMap = new HashMap();
  //System.out.println("ABOVE WHILELOOP searched for formPnbo "+form_no+" dmoe "+demographic_no);
  while (rsdemo.next()) { 
      //System.out.println("START OF WHILE LOOP");
    String risk_content = rsdemo.getString("risk_content");
    String checklist_content = rsdemo.getString("checklist_content");
    
    //System.out.println("IN WHILE LOOP");
%>
<script type="text/javascript">
   xmlText = "<xml><planner><%=risk_content%><%=checklist_content%></planner></xml>";
</script>
<%
//System.out.println("above riskFilePath :"+OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"/desantenatalplannerrisks_99_12.xml");
    String riskFilePath = "../webapps/"+oscarVariables.getProperty("project_home")+"/decision/antenatal/desantenatalplannerrisks_99_12.xml";
    
        File file = new File(OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"desantenatalplannerrisks_99_12.xml");
        if(file.isFile() || file.canRead()) {
			   riskFilePath = OscarProperties.getInstance().getProperty("DOCUMENT_DIR")+"desantenatalplannerrisks_99_12.xml";
        }

    //System.out.println("riskFilePath:"+riskFilePath);
    //set the riskdata bean from xml file
    Properties savedar1risk1 = risks.getRiskName(riskFilePath); //risk_55
  	StringBuffer tt; 

    for (Enumeration e = savedar1risk1.propertyNames() ; e.hasMoreElements() ;) {
      tt = new StringBuffer().append(e.nextElement());
      //if(SxmlMisc.getXmlContent(risk_content, savedar1risk1.getProperty(tt.toString()))!= null) 
      //  riskDataBean.setProperty(tt.toString(), savedar1risk1.getProperty(tt.toString()));
      if(SxmlMisc.getXmlContent(risk_content, "risk_"+tt.toString())!= null) {
        riskDataBean.setProperty(tt.toString(), "checked");
            
	    //System.out.println("risk from xml file checked "+tt.toString());
	  }
    }

  }
  plannerBean.closePstmtConn();
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
    
    
    //System.out.println("riskFilePAth "+riskFilePath);
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
    //System.out.println("CHECJ "+checkListFilePath);
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
