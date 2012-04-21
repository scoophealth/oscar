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
//  
//  String curUser_no = (String) session.getAttribute("user");
%>
<%@ page
	import="java.util.*, java.sql.*,java.io.*, oscar.*, java.text.*, java.lang.*,java.net.*"
	errorPage="../appointment/errorpage.jsp"%>
<jsp:useBean id="daySheetBean" class="oscar.AppointmentMainBean"
	scope="page" />
<jsp:useBean id="nameBean" class="java.util.Properties" scope="page" />

<% 
  String [][] dbQueries=new String[][] { 
{"search_encounter", "select e.*, d.* from encounter e LEFT JOIN demographicaccessory d ON e.demographic_no=d.demographic_no where e.encounter_no<? order by e.demographic_no, e.encounter_date, e.encounter_time" }, 
{"searchallprovider", "select * from provider order by last_name" }, 
  };
  String[][] responseTargets=new String[][] {  };
  daySheetBean.doConfigure(dbQueries,responseTargets);
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ECHART SHEET</title>
</head>
<body>
busy ...
<% 
  String asql="", ectno, timestamp, demographic_no, provider_no;
  String socialhistory, familyhistory, medicalhistory, ongoingconcerns, reminders, encounter="";

  SimpleDateFormat inform = new SimpleDateFormat ("yyyy-MM-ddHH:mm:ss", request.getLocale());
  SimpleDateFormat outform = new SimpleDateFormat ("yyyyMMddHHmmss", request.getLocale());

  FileWriter inf = new FileWriter("echart.sql");
  ResultSet rsdemo = null ;
  boolean bNewDemo = false;
  String temp="",content="";

  rsdemo = daySheetBean.queryResults("searchallprovider");
  while (rsdemo.next()) { 
    nameBean.setProperty(rsdemo.getString("provider_no"), new String( rsdemo.getString("first_name")+" "+rsdemo.getString("last_name") ));
  }
  
  rsdemo = daySheetBean.queryResults(5000, "search_encounter");
  while (rsdemo.next()) { 
    demographic_no = rsdemo.getString("e.demographic_no") ;
    if(temp.equals(demographic_no) ) bNewDemo = false ;
    else {
      bNewDemo = true ;
      temp = demographic_no ;
      encounter = "";
    }

    if(bNewDemo && asql.length()>1) inf.write(asql);

    ectno = rsdemo.getString("e.encounter_no") ;
    timestamp = outform.format(inform.parse(rsdemo.getString("e.encounter_date") + rsdemo.getString("e.encounter_time") ));
    provider_no = rsdemo.getString("e.provider_no") ;

    content = rsdemo.getString("d.content") ;
    socialhistory = "" ;
    familyhistory =  SxmlMisc.getXmlContent( content , "xml_Family_Social_History");
    medicalhistory = SxmlMisc.getXmlContent( content , "xml_Medication"); 
    ongoingconcerns = SxmlMisc.getXmlContent( content , "xml_Problem_List"); 
    reminders = "Allergy: " + SxmlMisc.getXmlContent( content , "xml_Alert"); 
    encounter += "\n[" + rsdemo.getString("e.encounter_date") +" " + rsdemo.getString("e.subject").substring(0,rsdemo.getString("e.subject").length()-1) + "]\n";
    encounter += SxmlMisc.getXmlContent( rsdemo.getString("e.content"), "xml_content" )==null?"":SxmlMisc.getXmlContent( rsdemo.getString("e.content"), "xml_content" ) ;
    encounter += "\n[Signed on " + rsdemo.getString("e.encounter_date")  + " " + rsdemo.getString("e.encounter_time")+ " by " + nameBean.get(provider_no) + "]\n\n" ;
    asql = "insert into eChart values ("+ ectno +", '"+ timestamp + "', " + demographic_no +", '"+ provider_no +"', '"
	      +Misc.charEscape( socialhistory, '\'' ) + "', '" +Misc.charEscape( familyhistory,'\'')  + "', '" +Misc.charEscape( medicalhistory,'\'' ) +"', '" +Misc.charEscape( ongoingconcerns, '\'') +"', '" +Misc.charEscape( reminders, '\'')  +"', '" + Misc.charEscape( encounter, '\'' ) + "' ); \n" ;
    
  }

  inf.write(asql);
  inf.close();
  
%>

done.
</body>
</html>
