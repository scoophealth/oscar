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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page
	import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarReport.ClinicalReports.*,oscar.oscarWorkflow.*"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%
    
    String provider = (String) session.getValue("user");

    
%>


<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>WorkFlow</title>
<html:base />

<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />
<script language="javascript" type="text/javascript"
	src="../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>


<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script type="text/javascript">
    var denominator_fields = new Array ();
    var denom_xtras;
    denominator_fields[denominator_fields.length] = "denominator_provider_no";
    
    
    function processExtraFields(t){
       var currentDenom = t.options[t.selectedIndex].value; 
       //Hide all extra denom fields
       for (  i = 0 ; i < denominator_fields.length; i++) {
          document.getElementById(denominator_fields[i]).style.display = 'none'; 
       }
       try{
           var fields_to_turn_on = denom_xtras[currentDenom];

           //get list of extra 
           for (  i = 0 ; i < fields_to_turn_on.length; i++) {
              document.getElementById(fields_to_turn_on[i]).style.display = ''; 
           }
       }catch(e){
        alert(e);
       }
          
    
    }
</script>

</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="100">workFlow</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><%=  request.getAttribute("name") != null ?request.getAttribute("name"):""%>
				</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="workflow" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">&nbsp;</td>
		<td valign="top" class="MainTableRightColumn">

		<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

		<%
                    
                     
                     
                     
                    String workflowType = "RH";//request.getParameter("workflowType");
                    //WorkFlowState workFlow = new WorkFlowState();  
                    WorkFlowFactory flowFactory = new WorkFlowFactory();
                    WorkFlow flow = flowFactory.getWorkFlow(workflowType);
                    ArrayList workList =    flow.getActiveWorkFlowList();
                  
                  if (workList != null && workList.size() > 0){
                        DemographicNameAgeString deName = DemographicNameAgeString.getInstance();
                  %>

		<table class="sortable tabular_list results" id="results_table">
			<thead>
				<tr>
					<th>Type</th>
					<th>Create Date</th>
					<th>Demographic</th>
					<th>EDD</th>
					<th>State</th>
					<th>Weeks</th>
					<th>Next Appt</th>
				</tr>
			</thead>
			<%      
                          WorkFlowDS wfDS = flow.getWorkFlowDS();
          
                          for (int j = 0; j < workList.size(); j++){
                          Hashtable h = (Hashtable) workList.get(j);
                          Hashtable demoHash = deName.getNameAgeSexHashtable(LoggedInInfo.getLoggedInInfoFromSession(request), ""+h.get("demographic_no"));
                          String colour = "";
                          
                          WorkFlowInfo wfi = flow.executeRules(wfDS,h);
                          colour = wfi.getColour();
                          String gestAge = "";
                          try{
                             gestAge = ""+ UtilDateUtilities.calculateGestationAge( new Date(), (Date) h.get("completion_date"));
                          }catch(Exception e){}
                %>
			<tr style="background-color: <%=colour%>">
				<td><a
					href="<%=flow.getLink(""+h.get("demographic_no"),""+h.get("ID"))%>"
					target="_blank"> <%=h.get("workflow_type")%> </a></td>
				<td><%=h.get("create_date_time")%></td>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onclick="popup(700,1000,'../demographic/demographiccontrol.jsp?demographic_no=<%=(String) h.get("demographic_no")%>&displaymode=edit&dboperation=search_detail','master')"
					title="Master File"> <%=demoHash.get("lastName")%>, <%=demoHash.get("firstName")%>
				</a></td>
				<td><%=h.get("completion_date")%></td>
				<td><%=flow.getState(""+h.get("current_state"))%></td>
				<td><%=gestAge%></td>
				<td><oscar:nextAppt
					demographicNo='<%=(String) h.get("demographic_no")%>' /></td>
			</tr>
			<%}%>
		</table>
		<%}else{%>
		<div>None in List</div>
		<%}%>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>

<script type="text/javascript">
//Calendar.setup( { inputField : "asOfDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );


</script>
<script language="javascript" src="../commons/scripts/sort_table/css.js">
<script language="javascript" src="../commons/scripts/sort_table/common.js">
<script language="javascript" src="../commons/scripts/sort_table/standardista-table-sorting.js">
</body>
</html:html>
<%!

String completed(boolean b){
    String ret ="";
    if(b){ret="checked";}
    return ret;
    }
     
String refused(boolean b){
    String ret ="";
    if(!b){ret="checked";}
    return ret;
    }
        
String str(String first,String second){
    String ret = "";
    if(first != null){
       ret = first;    
    }else if ( second != null){
       ret = second;    
    }
    return ret;
  }

String checked(String first,String second){
    String ret = "";
    if(first != null && second != null){
       if(first.equals(second)){
           ret = "checked";
       }
    }
    return ret;
  }


String sel(String s1,String s2){
     String ret = "";
     if (s1 != null && s2 != null && s1.equals(s2)){
        ret = "selected";    
     }
     return ret;  
  } 

String replaceHeading(String s){
    if ( s != null && s.equals("_demographic_no") ){
        return "Demographic #";    	 
    }else if (s.equals("_report_result")){
        return "Report Result";
    }
    return s;
}
       

%>
