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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.oscarProvider.data.ProviderData"%>
<%@ page import="oscar.oscarDemographic.data.DemographicData,java.text.SimpleDateFormat, java.util.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*"%>
<%@ page import="org.oscarehr.casemgmt.model.CaseManagementNoteLink" %>
<%@ page import="org.oscarehr.casemgmt.service.CaseManagementManager" %>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DemographicExtDao" %>
<%@page import="org.oscarehr.common.dao.PreventionsLotNrsDao" %>
<%@page import="org.oscarehr.common.model.PreventionsLotNrs" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_prevention" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_prevention");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
  LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
  DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
      		
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  //int demographic_no = Integer.parseInt(request.getParameter("demographic_no"));
  String demographic_no = request.getParameter("demographic_no");
  String id = request.getParameter("id");
  Map<String,Object> existingPrevention = null;

  String providerName ="";
  String lot ="";
  String provider = (String) session.getValue("user");
  String dateFmt = "yyyy-MM-dd HH:mm";
  String prevDate = UtilDateUtilities.getToday(dateFmt);
  String completed = "0";
  String nextDate = "";
  String summary = "";
  String creatorProviderNo = "";
  String creatorName = "";
  boolean never = false;
  Map<String,String> extraData = new HashMap<String,String>();
	boolean hasImportExtra = false;
	String annotation_display = CaseManagementNoteLink.DISP_PREV;

  if (id != null){

     existingPrevention = PreventionData.getPreventionById(id);

     prevDate = (String) existingPrevention.get("preventionDate");
     providerName = (String) existingPrevention.get("providerName");
     provider = (String) existingPrevention.get("provider_no");
     creatorProviderNo = (String) existingPrevention.get("creator");
     
     if ( existingPrevention.get("refused") != null && ((String)existingPrevention.get("refused")).equals("1") ){
        completed = "1";
     }else if ( existingPrevention.get("refused") != null && ((String)existingPrevention.get("refused")).equals("2") ){
        completed = "2";
     }
     if ( existingPrevention.get("never") != null && ((String)existingPrevention.get("never")).equals("1") ){
        never = true;
     }
     nextDate = (String) existingPrevention.get("next_date");
     if ( nextDate == null || nextDate.equalsIgnoreCase("null") || nextDate.equals("0000-00-00")){
        nextDate = "";
     }
     summary = (String) existingPrevention.get("summary");
     extraData = PreventionData.getPreventionKeyValues(id);
     lot = (String) extraData.get("lot");
     
	CaseManagementManager cmm = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");
	List<CaseManagementNoteLink> cml = cmm.getLinkByTableId(CaseManagementNoteLink.PREVENTIONS, Long.valueOf(id));
	hasImportExtra = (cml.size()>0);
  }

  String prevention = request.getParameter("prevention");
  if (prevention == null && existingPrevention != null){
      prevention = (String) existingPrevention.get("preventionType");
  }

  PreventionsLotNrsDao PreventionsLotNrsDao = (PreventionsLotNrsDao)SpringUtils.getBean(PreventionsLotNrsDao.class);
  List<String> lotNrList = PreventionsLotNrsDao.findLotNrs(prevention, false);
  
  String prevResultDesc = request.getParameter("prevResultDesc");

  PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance(loggedInInfo);
  HashMap<String,String> prevHash = pdc.getPrevention(loggedInInfo,prevention);

  String layoutType = prevHash.get("layout");
  if ( layoutType == null){
      layoutType = "default";
  }

  List<Map<String, String>>  providers = ProviderData.getProviderList();
  if (creatorProviderNo == "")
  { 
	  creatorProviderNo = provider;
  }
  for (int i=0; i < providers.size(); i++) {
       Map<String,String> h = providers.get(i);
	   if (h.get("providerNo").equals(creatorProviderNo))
	   {
	   		creatorName = h.get("lastName") + " " +  h.get("firstName");
	   }
  }
  
  //calc age at time of prevention
  Date dob = PreventionData.getDemographicDateOfBirth(loggedInInfo, Integer.valueOf(demographic_no));
  SimpleDateFormat fmt = new SimpleDateFormat(dateFmt);
  Date dateOfPrev = fmt.parse(prevDate);
  String age = UtilDateUtilities.calcAgeAtDate(dob, dateOfPrev);
  DemographicData demoData = new DemographicData();
  String[] demoInfo = demoData.getNameAgeSexArray(loggedInInfo, Integer.valueOf(demographic_no));
  String nameage = demoInfo[0] + ", " + demoInfo[1] + " " + demoInfo[2] + " " + age;

  HashMap<String,String> genders = new HashMap<String,String>();
  genders.put("M", "Male");
  genders.put("F", "Female");
  genders.put("U", "Unknown");
%>


<html:html locale="true">

<head>
<title>
<bean:message key="oscarprevention.index.oscarpreventiontitre" />
</title><!--I18n-->
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />

<script type="text/javascript" src="../share/calendar/calendar.js" ></script>
<script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>" ></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js" ></script>

<style type="text/css">
  div.ImmSet { background-color: #ffffff; }
  div.ImmSet h2 {  }
  div.ImmSet ul {  }
  div.ImmSet li {  }
  div.ImmSet li a { text-decoration:none; color:blue;}
  div.ImmSet li a:hover { text-decoration:none; color:red; }
  div.ImmSet li a:visited { text-decoration:none; color:blue;}


  ////////
  div.prevention {  background-color: #999999; }
  div.prevention fieldset {width:35em; font-weight:bold; }
  div.prevention legend {font-weight:bold; }

  ////////
</style>

<SCRIPT LANGUAGE="JavaScript">

function showHideItem(id){
    if(document.getElementById(id).style.display == 'none')
        document.getElementById(id).style.display = '';
    else
        document.getElementById(id).style.display = 'none';
}

function showItem(id){
        document.getElementById(id).style.display = '';
}

function hideItem(id){
        document.getElementById(id).style.display = 'none';
}

function showHideNextDate(id,nextDate,nexerWarn){
    if(document.getElementById(id).style.display == 'none'){
        showItem(id);
    }else{
        hideItem(id);
        document.getElementById(nextDate).value = "";
        document.getElementById(nexerWarn).checked = false ;

    }
}

function disableifchecked(ele,nextDate){
    if(ele.checked == true){
       document.getElementById(nextDate).disabled = true;
    }else{
       document.getElementById(nextDate).disabled = false;
    }
}
</SCRIPT>

<style type="text/css">
	table.outline{
	   margin-top:50px;
	   border-bottom: 1pt solid #888888;
	   border-left: 1pt solid #888888;
	   border-top: 1pt solid #888888;
	   border-right: 1pt solid #888888;
	}
	table.grid{
	   border-bottom: 1pt solid #888888;
	   border-left: 1pt solid #888888;
	   border-top: 1pt solid #888888;
	   border-right: 1pt solid #888888;
	}
	td.gridTitles{
		border-bottom: 2pt solid #888888;
		font-weight: bold;
		text-align: center;
	}
        td.gridTitlesWOBottom{
                font-weight: bold;
                text-align: center;
        }
	td.middleGrid{
	   border-left: 1pt solid #888888;
	   border-right: 1pt solid #888888;
           text-align: center;
	}


label{
float: left;
width: 120px;
font-weight: bold;
}

label.checkbox{
float: left;
width: 116px;
font-weight: bold;
}

label.fields{
float: left;
width: 80px;
font-weight: bold;
}

span.labelLook{
font-weight:bold;

}

input, textarea,select{

//margin-bottom: 5px;
}

textarea{
width: 450px;
height: 100px;
}


.boxes{
width: 1em;
}

#submitbutton{
margin-left: 120px;
margin-top: 5px;
width: 90px;
}

br{
clear: left;
}
</style>

<script type="text/javascript">
  function hideExtraName(ele){
   //alert(ele);
    if (ele.options[ele.selectedIndex].value != -1){
       hideItem('providerName');
       //alert('hidding');
    }else{
       showItem('providerName');
       document.getElementById('providerName').focus();
       //alert('showing');
    }
  }
</script>

<script type="text/javascript">
  function updateLotNr(elem){
	if (elem.options[elem.selectedIndex].value != -1)
	{
		hideItem('lot');
	}
   //show "other" in drop-down
   else if (elem.options[elem.selectedIndex].value == -1) 
   {
	    document.getElementById('lot').value = "";
   		showItem('lot');
      	document.getElementById('lot').focus();
   }
  }
  </script>
  <script type="text/javascript">
  function hideLotDrop(elem){
	  var bFound = 0;
	  var LotNr = document.getElementById('lot').value;
	  var summary =  document.getElementById('summary');
	  //existing prevention record
	  if (typeof(summary) != 'undefined' && summary != null)
	  { 
	     if (LotNr.length == 0)
	     {
	    	 if (elem.options[0].value != -1) //table exists
	    	 {
	    		 elem.options[elem.options.length-1].selected = true;
	    		 return;
	    	 }	    	 
	    	 else
	    	 {
	  	       hideItem('lotDrop');
	  		   showItem('lot');
	  	       return;
	  	    }
	     }	  
	  }	  
	  if (LotNr.length >0)
	  {
		  for (var i = 0; i < elem.length; i++) {
		        if (elem.options[i].value == LotNr){
		        	bFound = 1;
					break;
				}
		    }
	  }
	  if (elem.options[0].value == -1)
	  //no preventionslotnrs table
	  {
		   hideItem('lotDrop');
		   showItem('lot');	
	  }
	 // not in drop-down
	 else if (!bFound && LotNr.length >0)
	 {
		 elem.options[elem.options.length-1].selected = true;
	 }
	  //exists in dd
	  else if (elem.options[elem.selectedIndex].value != -1)
	  {
	   		hideItem('lot');
	  }
	  }


var warnOnWindowClose=true;

function cancelCloseWarning(){
warnOnWindowClose=false;
}

window.onbeforeunload = displayCloseWarning;

function displayCloseWarning(){
	if(warnOnWindowClose){
		return 'Are you sure you want to close this window?';
	}
}
</script>
</head>

<body class="BodyStyle" vlink="#0000FF" onload="disableifchecked(document.getElementById('neverWarn'),'nextDate');">
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn" width="100" >
               <bean:message key="oscarprevention.index.oscarpreventiontitre" />
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            <%=nameage%>
                        </td>
                        <td  >&nbsp;

                        </td>
                        <td style="text-align:right">
                                <oscar:help keywords="prevention" key="app.top1"/> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn" valign="top">
               &nbsp;
<!--
               <%
                 org.oscarehr.common.model.DemographicExt ineligx = demographicExtDao.getDemographicExt(Integer.parseInt(demographic_no),prevention+"Inelig");
				 String inelig = "";
				 if(ineligx != null) {
					 inelig = ineligx.getValue();
				 }

                 if (inelig.equals("yes")){ %>
                    Patient Ineligible<br>
                    <a href="setPatientIneligible.jsp?prev=<%=prevention%>&demo=<%=demographic_no%>&elig=yes">Set Patient Eligible</a>
                 <%}else{%>
                    <a href="setPatientIneligible.jsp?prev=<%=prevention%>&demo=<%=demographic_no%>">Set Patient Ineligible</a>
                 <%}%>
-->
            </td>
            <td valign="top" class="MainTableRightColumn">
               <html:form action="/oscarPrevention/AddPrevention" onsubmit="return cancelCloseWarning()">
               <input type="hidden" name="prevention" value="<%=prevention%>"/>
               <input type="hidden" name="demographic_no" value="<%=demographic_no%>"/>
               <% if ( id != null ) { %>
               <input type="hidden" name="id" value="<%=id%>"/>
               <input type="hidden" name="layoutType" value="<%=layoutType%>"/>
	       <div class="prevention">
		   <fieldset>
		       <legend>Summary</legend>
		       <textarea name="summary" readonly><%=summary%></textarea>
<%if (hasImportExtra) { %>
				<a href="javascript:void(0);" title="Extra data from Import" onclick="window.open('../annotation/importExtra.jsp?display=<%=annotation_display %>&amp;table_id=<%=id %>&amp;demo=<%=demographic_no %>','anwin','width=400,height=250');">
				 <img src="../images/notes.gif" align="right" alt="Extra data from Import" height="16" width="13" border="0"> </a>
<%} %>
		   </fieldset>
	       </div>
               <% } %>
               <%if (layoutType.equals("injection")) {%>
               <div class="prevention">
                   <fieldset>
                      <legend>Prevention : <%=prevention%></legend>
                         <div style="float:left;">
                            <input name="given" type="radio" value="given"      <%=checked(completed,"0")%>>Completed</input><br/>
                            <input name="given" type="radio" value="refused"    <%=checked(completed,"1")%>>Refused</input><br/>
                            <input name="given" type="radio" value="ineligible" <%=checked(completed,"2")%>>Ineligible</input>
                         </div>
                         <div style="float:left;margin-left:30px;">
                            <label for="prevDate" class="fields" >Date:</label>    <input readonly='readonly' type="text" name="prevDate" id="prevDate" value="<%=prevDate%>" size="15" > <a id="date"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a> <br>
                            <label for="provider" class="fields">Provider:</label> <input type="text" name="providerName" id="providerName" value="<%=providerName%>"/>
                                  <select onchange="javascript:hideExtraName(this);" id="providerDrop" name="provider">
                                      <%
										boolean provInList=false;
										for (int i=0; i < providers.size(); i++) {
                                           Map<String,String> h = providers.get(i);String sel = "";
                                           if(h.get("providerNo").equals(provider)){
											   sel = " selected";
											   provInList=true;
										   }%>
                                        <option value="<%= h.get("providerNo")%>" <%= sel %>><%= h.get("lastName") %> <%= h.get("firstName") %></option>
                                      <%}%>
                                      <option value="-1" <%= ( !provInList ? " selected" : "" ) %> >Other</option>
                                  </select>
                                  <br/>
                             <label for="creator" class="fields" >Creator:</label> <input type="text" name="creator" value="<%=creatorName%>" readonly/> <br/>
                         </div>
                   </fieldset>
                   <fieldset >
                      <legend >Result</legend>
             			 <label for="name">Name:</label> <input type="text" name="name" value="<%=str((extraData.get("name")),"")%>"/> <br/>
                         <label for="location">Location:</label> <input type="text" name="location" value="<%=str((extraData.get("location")),"")%>"/> <br/>
                         <label for="route">Route:</label> <input type="text" name="route"   value="<%=str((extraData.get("route")),"")%>"/><br/>
                         <label for="dose">Dose:</label> <input type="text" name="dose"  value="<%=str((extraData.get("dose")),"")%>"/><br/>
                         <label for="lot">Lot:</label>  <input type="text" name="lot" id="lot" value="<%=str(lot,"")%>" />
                        <select onchange="javascript:updateLotNr(this);" id="lotDrop" name="lotItem" >
                             <%for(String lotnr:lotNrList) {
							 %>
                               <option value="<%=lotnr%>" <%= ( lotnr.equals(lot) ? " selected" : "" ) %>><%=lotnr%> </option>
                             <%}%>
                             <option value="-1"  >Other</option>
                         </select><br/>
                         <label for="manufacture">Manufacture:</label> <input type="text" name="manufacture" id="manufacture"  value="<%=str((extraData.get("manufacture")),"")%>"/><br/>
                   </fieldset>
                   <fieldset >
                      <legend >Comments</legend>
                      <textarea name="comments" ><%=str((extraData.get("comments")),"")%></textarea>
                   </fieldset>
               </div>
               <script type="text/javascript">
                  hideExtraName(document.getElementById('providerDrop'));
               </script>
               <script type="text/javascript">
               hideLotDrop(document.getElementById('lotDrop'));
               </script>
               <%} else if(layoutType.equals("h1n1")) {%>
               <div class="prevention">
                   <fieldset>
                      <legend>Prevention : <%=prevention%></legend>
                         <div style="float:left;">
                            <input name="given" type="radio" value="given"      <%=checked(completed,"0")%>>Completed</input><br/>
                            <input name="given" type="radio" value="refused"    <%=checked(completed,"1")%>>Refused</input><br/>
                            <input name="given" type="radio" value="ineligible" <%=checked(completed,"2")%>>Ineligible</input>
                         </div>
                         <div style="float:left;margin-left:30px;">
                            <label for="prevDate" class="fields" >Date:</label>    <input type="text" name="prevDate" id="prevDate" value="<%=prevDate%>" size="9" > <a id="date"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a> <br>
                            <label for="provider" class="fields">Provider:</label> <input type="text" name="providerName" id="providerName" value="<%=providerName%>"/>
                                  <select onchange="javascript:hideExtraName(this);" id="providerDrop" name="provider">
                                      <%
										boolean provInList=false;
										for (int i=0; i < providers.size(); i++) {
                                           Map<String,String> h = providers.get(i);String sel = "";
                                           if(h.get("providerNo").equals(provider)){
											   sel = " selected";
											   provInList=true;
										   }%>
                                        <option value="<%= h.get("providerNo")%>" <%= sel %>><%= h.get("lastName") %> <%= h.get("firstName") %></option>
                                      <%}%>
                                      <option value="-1" <%= ( !provInList ? " selected" : "" ) %> >Other</option>
                                  </select>
                                  <br/>
                             <label for="creator" class="fields" >Creator:</label> <input type="text" name="creator" value="<%=creatorName%>" readonly/> <br/>
                         </div>
                   </fieldset>
                   <fieldset>
                      <legend >Result</legend>
                         <label for="location">Location:</label> <input type="text" name="location" value="<%=str((extraData.get("location")),"")%>"/> <br/>
                         <label for="route">Route:</label> <input type="text" name="route"   value="<%=str((extraData.get("route")),"")%>"/><br/>
                         <label for="dose">Dose:</label> <input type="text" name="dose"  value="<%=str((extraData.get("dose")),"")%>"/><br/>
			 <label for="dose1">Dose 1:</label> <input type="checkbox" name="dose1" value="true" <%=checked(str((extraData.get("dose1")),""),"true")%>/><br/>
                         <label for="dose2">Dose 2:</label> <input type="checkbox" name="dose2"  value="true" <%=checked(str((extraData.get("dose2")),""),"true")%>/><br/>
                         <label for="lot">Lot:</label> <input type="text" name="lot"  value="<%=str((extraData.get("lot")),"")%>"/><br/>
                         <label for="manufacture">Manufacture:</label> <input type="text" name="manufacture"   value="<%=str((extraData.get("manufacture")),"")%>"/><br/>
                   </fieldset>
                   <fieldset>
                       <legend>Info</legend>
                       <% String gender = genders.get(demoInfo[2]);
                          if( gender == null ) {
                              gender = genders.get("U");
                          }

                       %>
                         <label for="gender">Gender:</label> <input type="text" name="gender" readonly value="<%=gender%>"/> <br/>
                         <label for="age">Age:</label> <input type="text" name="age" readonly value="<%=age%>"/><br/>
                         <label for="chronic">Chronic Condition:</label>
                         <select name="chronic">
                             <option value="false">No</option>
                             <option value="true" <%= str((extraData.get("chronic")),"").equalsIgnoreCase("true") ? "selected" : "" %> >Yes</option>
                             <option value="cardiac disorder" <%= str((extraData.get("chronic")),"").equalsIgnoreCase("cardiac disorder") ? "selected" : "" %> >Cardiac Disorder</option>
                             <option value="diabetes" <%= str((extraData.get("chronic")),"").equalsIgnoreCase("diabetes") ? "selected" : "" %> >Diabetes</option>
                             <option value="cancer" <%= str((extraData.get("chronic")),"").equalsIgnoreCase("cancer") ? "selected" : "" %> >Cancer</option>
                             <option value="immunodeficiency" <%= str((extraData.get("chronic")),"").equalsIgnoreCase("immunodeficiency") ? "selected" : "" %> >Immunodeficiency</option>
                             <option value="immunosuppression" <%= str((extraData.get("chronic")),"").equalsIgnoreCase("immunosuppression") ? "selected" : "" %> >Immunosuppression</option>
                             <option value="renal disease" <%= str((extraData.get("chronic")),"").equalsIgnoreCase("renal disease") ? "selected" : "" %> >Renal Disease</option>
                             <option value="anemia or hemoglobinopathy" <%= str((extraData.get("chronic")),"").equalsIgnoreCase("anemia or hemoglobinopathy") ? "selected" : "" %> >Anemia or Hemoglobinopathy</option>
                             <option value="compromised management of respiratory secretions" <%= str((extraData.get("chronic")),"").equalsIgnoreCase("compromised management of respiratory secretions") ? "selected" : "" %> >Compromised Management of Respiratory Secretions</option>
                             <option value="Children/Adolescent with Longterm Acetylsalicylic Acid" <%= str((extraData.get("chronic")),"").equalsIgnoreCase("Children/Adolescent with Longterm Acetylsalicylic Acid") ? "selected" : "" %> >Children/Adolescent with Longterm Acetylsalicylic Acid</option>
                         </select><br/>
                         <label for="pregnant">Pregnant:</label> <input type="checkbox" name="pregnant"  value="true" <%=checked(str((extraData.get("pregnant")),""),"true")%>/><br/>
                         <label for="remote">Remote Setting:</label> <input type="checkbox" name="remote"  value="true" <%=checked(str((extraData.get("remote")),""),"true")%>/><br/>
                         <label for="healthcareworker">Health Care Worker:</label>
                         <select name="healthcareworker">
                             <option value="false">No</option>
                             <option value="true" <%= str((extraData.get("healthcareworker")),"").equalsIgnoreCase("true") ? "selected" : "" %> >Yes</option>
                             <option value="acute care" <%= str((extraData.get("healthcareworker")),"").equalsIgnoreCase("acute care") ? "selected" : "" %> >Acute Care</option>
                             <option value="chronic care" <%= str((extraData.get("healthcareworker")),"").equalsIgnoreCase("chronic care") ? "selected" : "" %> >Chronic Care</option>
                             <option value="community care" <%= str((extraData.get("healthcareworker")),"").equalsIgnoreCase("community care") ? "selected" : "" %> >Ambulatory/Community Care</option>
                             <option value="emergency medical services" <%= str((extraData.get("healthcareworker")),"").equalsIgnoreCase("emergency medical services") ? "selected" : "" %> >Emergency Medical Services</option>
                             <option value="laboratory" <%= str((extraData.get("healthcareworker")),"").equalsIgnoreCase("laboratory") ? "selected" : "" %> >Laboratory</option>
                             <option value="public health" <%= str((extraData.get("healthcareworker")),"").equalsIgnoreCase("public health") ? "selected" : "" %> >Public Health</option>
                             <option value="pharmacy" <%= str((extraData.get("healthcareworker")),"").equalsIgnoreCase("pharmacy") ? "selected" : "" %> >Pharmacy</option>
                             <option value="vaccine manufacturer" <%= str((extraData.get("healthcareworker")),"").equalsIgnoreCase("vaccine manufacturer") ? "selected" : "" %> >Vaccine Mfr</option>
                         </select><br/>

                         <label for="householdcontact">Household Contact or Care Provider:</label> <input type="checkbox" name="householdcontact" value="true" <%=checked(str((extraData.get("householdcontact")),""),"true")%>/><br/>
                         <%
                            boolean bothfirstresponders = false;
                            if( str((extraData.get("firstresponder")),"").equalsIgnoreCase("true")) {
                                bothfirstresponders = true;
                           }

                         %>
                         <label for="firstresponderpolice">First Responder Police:</label> <input type="checkbox" name="firstresponderpolice" value="true" <%=bothfirstresponders == true ? "checked" : checked(str((extraData.get("firstresponderpolice")),""),"true")%>/><br/>
                         <label for="firstresponderfire">First Responder Fire:</label> <input type="checkbox" name="firstresponderfire" value="true" <%=bothfirstresponders == true ? "checked" : checked(str((extraData.get("firstresponderfire")),""),"true")%>/><br/>
                         <label for="swineworker">Swine Worker:</label> <input type="checkbox" name="swineworker" value="true" <%=checked(str((extraData.get("swineworker")),""),"true")%>/><br/>
                         <label for="poultryworker">Poultry Worker:</label> <input type="checkbox" name="poultryworker" value="true" <%=checked(str((extraData.get("poultryworker")),""),"true")%>/><br/>
                         <label for="firstnations">First Nations:</label> <input type="checkbox" name="firstnations" value="true" <%=checked(str((extraData.get("firstnations")),""),"true")%>/><br/>
                   </fieldset>
                   <fieldset >
                      <legend >Comments</legend>
                      <textarea name="comments" ><%=str((extraData.get("comments")),"")%></textarea>
                   </fieldset>
               </div>
               <script type="text/javascript">
                  hideExtraName(document.getElementById('providerDrop'));
               </script>
               <%}else if(layoutType.equals("PAPMAM")){/*next layout type*/%>
               <div class="prevention">
                   <fieldset>
                      <legend>Prevention : <%=prevention%></legend>
                         <div style="float:left;">
                            <input name="given" type="radio" value="given"   <%=checked(completed,"0")%>  >Completed</input><br/>
                            <input name="given" type="radio" value="refused" <%=checked(completed,"1")%>>Refused</input><br/>
                            <input name="given" type="radio" value="ineligible" <%=checked(completed,"2")%>>Ineligible</input><br/>
                         </div>
                         <div style="float:left;margin-left:30px;">
                            <label for="prevDate" class="fields" >Date:</label>    <input type="text" name="prevDate" id="prevDate" value="<%=prevDate%>" size="9" > <a id="date"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a> <br>
                            <label for="provider" class="fields">Provider:</label> <input type="text" name="providerName" id="providerName"/>
                                  <select onchange="javascript:hideExtraName(this);" id="providerDrop" name="provider">
                                      <%
										boolean provInList=false;
										for (int i=0; i < providers.size(); i++) {
                                           Map<String,String> h = providers.get(i);String sel = "";
                                           if(h.get("providerNo").equals(provider)){
											   sel = " selected";
											   provInList=true;
										   }%>
                                        <option value="<%= h.get("providerNo")%>" <%= sel %>><%= h.get("lastName") %> <%= h.get("firstName") %></option>
                                      <%}%>
                                      <option value="-1" <%= ( !provInList ? " selected" : "" ) %> >Other</option>
                                  </select>
                                  <br/>
                                  <label for="creator" class="fields" >Creator:</label> <input type="text" name="creator" value="<%=creatorName%>" readonly/> <br/>
                         </div>
                   </fieldset>
                   <fieldset >
                      <legend >Result</legend>
                      <% if (extraData.get("result") == null ){ extraData.put("result","pending");} %>
                      <%=str(prevResultDesc,"")%><br />
                      <input type="radio" name="result" value="pending" <%=checked( (extraData.get("result")) ,"pending")%> >Pending</input><br/>
                      <input type="radio" name="result" value="normal"  <%=checked((extraData.get("result")),"normal")%> >Normal</input><br/>
                      <input type="radio" name="result" value="abnormal" <%=checked((extraData.get("result")),"abnormal")%> >Abnormal</input><br/>
                      <input type="radio" name="result" value="other" <%=checked((extraData.get("result")),"other")%> >Other</input> &nbsp; &nbsp; Reason: <input type="text" name="reason" value="<%=str((extraData.get("reason")),"")%>"/>
                   </fieldset>
                   <fieldset >
                      <legend >Comments</legend>
                      <textarea name="comments" ><%=str((extraData.get("comments")),"")%></textarea>
                   </fieldset>
               </div>
               <script type="text/javascript">
                  hideExtraName(document.getElementById('providerDrop'));
               </script>
               <%} else if(layoutType.equals("history")) {%>
               		 <div class="prevention">
                   <fieldset>
                      <legend>Prevention : <%=prevention%></legend>
                         <div style="float:left;">
                            <input name="given" type="radio" value="yes"      <%=checked(completed,"0")%>>Yes</input><br/>
                            <input name="given" type="radio" value="never"    <%=checked(completed,"1")%>>Never</input><br/>
                            <input name="given" type="radio" value="previous" <%=checked(completed,"2")%>>Previous</input>
                         </div>
                         <div style="float:left;margin-left:30px;">
                            <label for="prevDate" class="fields" >Date:</label>    <input type="text" name="prevDate" id="prevDate" value="<%=prevDate%>" size="9" > <a id="date"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a> <br>
                            <label for="provider" class="fields">Provider:</label> <input type="hidden" name="providerName" id="providerName" value="<%=providerName%>"/>
                                  <select onchange="javascript:hideExtraName(this);" id="providerDrop" name="provider">
                                      <%for (int i=0; i < providers.size(); i++) {
                                           Map<String,String> h = providers.get(i);%>
                                        <option value="<%= h.get("providerNo")%>" <%= ( h.get("providerNo").equals(provider) ? " selected" : "" ) %>><%= h.get("lastName") %> <%= h.get("firstName") %></option>
                                      <%}%>
                                      <option value="-1" <%= ( "-1".equals(provider) ? " selected" : "" ) %> >Other</option>
                                  </select>
                         </div>
                   </fieldset>
                   <fieldset >
                      <legend >Comments</legend>
                      <textarea name="comments" ><%=str((extraData.get("comments")),"")%></textarea>
                   </fieldset>
                   </div>
               <%} %>


               <div class="prevention">
                   <fieldset>
                      <legend><a onclick="showHideNextDate('nextDateDiv','nextDate','nexerWarn')" href="javascript: function myFunction() {return false; }"   >Set Next Date</a></legend>
                        <div id="nextDateDiv" style="display:none;">
                         <div>
                            <label for="nextDate" >Next Date:</label><input type="text" name="nextDate"  value="<%=nextDate%>" id="nextDate" size="9"><a id="nextDateCal"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a>
                         </div>
                         <div>
                            <label for="neverWarn" class="checkbox" >Never Remind:</label><input type="checkbox" name="neverWarn" id="neverWarn" value="neverRemind" onchange="disableifchecked(this,'nextDate');"  <%=completed(never)%>/> Reason: <input type="text" name="neverReason" value="<%=str((extraData.get("neverReason")),"")%>"/>
                         </div>
                        </div>
                   </fieldset>
               </div>
               <br/>
               <input type="submit" value="Save">
               <% if ( id != null ) { %>
               <input type="submit" name="delete" value="Delete"/>
               <% } %>
               </html:form>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">
            &nbsp;
            </td>
            <td class="MainTableBottomRowRightColumn" valign="top">
            &nbsp;
            </td>
        </tr>
    </table>
<script type="text/javascript">
Calendar.setup( { inputField : "prevDate", ifFormat : "%Y-%m-%d %H:%M", showsTime :true, button : "date", singleClick : true, step : 1 } );
Calendar.setup( { inputField : "nextDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "nextDateCal", singleClick : true, step : 1 } );
</script>
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
%>
