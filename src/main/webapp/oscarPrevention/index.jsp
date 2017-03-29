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
<%@page import="org.oscarehr.util.WebUtilsOld"%>
<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*"%>
<%@page import="org.oscarehr.phr.util.MyOscarUtils" %>
<%@page import="org.oscarehr.common.dao.DemographicDao, org.oscarehr.common.model.Demographic" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.util.LocaleUtils"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.PMmodule.model.ProgramProvider" %>
<%@page import="org.oscarehr.managers.ProgramManager2" %>
<%@page import="org.oscarehr.util.MiscUtils" %>
<%@page import="org.oscarehr.managers.PreventionManager" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
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
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    ProgramManager2 programManager2 = SpringUtils.getBean(ProgramManager2.class);
  //int demographic_no = Integer.parseInt(request.getParameter("demographic_no"));
  String demographic_no = request.getParameter("demographic_no");
  DemographicData demoData = new DemographicData();
  String nameAge = demoData.getNameAgeString(loggedInInfo, demographic_no);
  org.oscarehr.common.model.Demographic demo = demoData.getDemographic(loggedInInfo, demographic_no);
  String hin = demo.getHin()+demo.getVer();
  String mrp = demo.getProviderNo();
  PreventionManager preventionManager = SpringUtils.getBean(PreventionManager.class);

  PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance(loggedInInfo);
  ArrayList<HashMap<String,String>> prevList = pdc.getPreventions(loggedInInfo);
  
  
  ArrayList<Map<String,Object>> configSets = pdc.getConfigurationSets(loggedInInfo);



  Prevention p = PreventionData.getPrevention(loggedInInfo, Integer.valueOf(demographic_no));

  Integer demographicId=Integer.parseInt(demographic_no);
  PreventionData.addRemotePreventions(loggedInInfo, p, demographicId);
  Date demographicDateOfBirth=PreventionData.getDemographicDateOfBirth(loggedInInfo, Integer.valueOf(demographic_no));
  String demographicDob = oscar.util.UtilDateUtilities.DateToString(demographicDateOfBirth);

  PreventionDS pf = SpringUtils.getBean(PreventionDS.class);


  boolean dsProblems = false;
  try{
     pf.getMessages(p);
  }catch(Exception dsException){
	  MiscUtils.getLogger().error("Error running prevention rules",dsException);
      dsProblems = true;
  }

  ArrayList warnings = p.getWarnings();
  ArrayList recomendations = p.getReminder();

  boolean printError = request.getAttribute("printError") != null;
%>

<%!
	public String getFromFacilityMsg(Map<String,Object> ht)
	{
		if (ht.get("id")==null)	return("<br /><span style=\"color:#990000\">(At facility : "+ht.get("remoteFacilityName")+")<span>");
		else return("");
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">



<%@page import="org.oscarehr.util.SessionConstants"%><html:html
	locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarprevention.index.oscarpreventiontitre" /></title>
<!--I18n-->
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css" />
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>

<style type="text/css">
div.ImmSet {
	background-color: #ffffff;
	clear: left;
	margin-top: 10px;
}

div.ImmSet h2 {

}

div.ImmSet h2 span {
	font-size: smaller;
}

div.ImmSet ul {

}

div.ImmSet li {

}

div.ImmSet li a {
	text-decoration: none;
	color: blue;
}

div.ImmSet li a:hover {
	text-decoration: none;
	color: red;
}

div.ImmSet li a:visited {
	text-decoration: none;
	color: blue;
}

/*h3{font-size: 100%;margin:0 0 10px;padding: 2px 0;color: #497B7B;text-align: center}*/
div.onPrint {
	display: none;
}

span.footnote {
    background-color: #ccccee;
    border: 1px solid #000;
    width: 4px;
}
</style>

<link rel="stylesheet" type="text/css" href="../share/css/niftyCorners.css" />
<link rel="stylesheet" type="text/css" href="../share/css/niftyPrint.css" media="print" />
<link rel="stylesheet" type="text/css" href="preventPrint.css" media="print" />

<script type="text/javascript" src="../share/javascript/nifty.js"></script>
<script type="text/javascript">
window.onload=function(){
if(!NiftyCheck())
    return;

//Rounded("div.news","all","transparent","#FFF","small border #999");
Rounded("div.headPrevention","all","#CCF","#efeadc","small border blue");
Rounded("div.preventionProcedure","all","transparent","#F0F0E7","small border #999");

Rounded("div.leftBox","top","transparent","#CCCCFF","small border #ccccff");
Rounded("div.leftBox","bottom","transparent","#EEEEFF","small border #ccccff");

}

function display(elements) {

    for( var idx = 0; idx < elements.length; ++idx )
        elements[idx].style.display = 'block';
}

function EnablePrint(button) {
    if( button.value == "Enable Print" ) {
        button.value = "Print";
        var checkboxes = document.getElementsByName("printHP");
        display(checkboxes);
        var spaces = document.getElementsByName("printSp");
        display(spaces);
        button.form.sendToPhrButton.style.display = 'block';
    }
    else {
        if( onPrint() )
            document.printFrm.submit();
    }
}

function onPrint() {
    var checked = document.getElementsByName("printHP");
    var thereIsData = false;

    for( var idx = 0; idx < checked.length; ++idx ) {
        if( checked[idx].checked ) {
            thereIsData = true;
            break;
        }
    }

    if( !thereIsData ) {
        alert("You should check at least one prevention by selecting a checkbox next to a prevention");
        return false;
    }

    return true;
}

function sendToPhr(button) {
    var oldAction = button.form.action;
    button.form.action = "<%=request.getContextPath()%>/phr/SendToPhrPreview.jsp"
    button.form.submit();
    button.form.action = oldAction;
}
</script>




<script type="text/javascript">
<!--
//if (document.all || document.layers)  window.resizeTo(790,580);
function newWindow(file,window) {
  msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
  if (msgWindow.opener == null) msgWindow.opener = self;
}
//-->
</script>




<style type="text/css">
body {
	font-size: 100%
}

//
div.news {
	width: 100px;
	background: #FFF;
	margin-bottom: 20px;
	margin-left: 20px;
}

div.leftBox {
	width: 90%;
	margin-top: 2px;
	margin-left: 3px;
	margin-right: 3px;
	float: left;
}

div.leftBox h3 {
	background-color: #ccccff;
	/*font-size: 1.25em;*/
	font-size: 8pt;
	font-variant: small-caps;
	font: bold;
	margin-top: 0px;
	padding-top: 0px;
	margin-bottom: 0px;
	padding-bottom: 0px;
}

div.leftBox ul { /*border-top: 1px solid #F11;*/
	/*border-bottom: 1px solid #F11;*/
	font-size: 1.0em;
	list-style: none;
	list-style-type: none;
	list-style-position: outside;
	padding-left: 1px;
	margin-left: 1px;
	margin-top: 0px;
	padding-top: 1px;
	margin-bottom: 0px;
	padding-bottom: 0px;
}

div.leftBox li {
	padding-right: 15px;
	white-space: nowrap;
}

div.headPrevention {
	position: relative;
	float: left;
	width: 8.4em;
	height: 2.5em;
}

div.headPrevention p {
	background: #EEF;
	font-family: verdana, tahoma, sans-serif;
	margin: 0;
	padding: 4px 5px;
	line-height: 1.3;
	text-align: justify height : 2em;
	font-family: sans-serif;
	border-left: 0px;
}

div.headPrevention a {
	text-decoration: none;
}

div.headPrevention a:active {
	color: blue;
}

div.headPrevention a:hover {
	color: blue;
}

div.headPrevention a:link {
	color: blue;
}

div.headPrevention a:visited {
	color: blue;
}

div.preventionProcedure {
	width: 10em;
	float: left;
	margin-left: 3px;
	margin-bottom: 3px;
}

div.preventionProcedure p {
	font-size: 0.8em;
	font-family: verdana, tahoma, sans-serif;
	background: #F0F0E7;
	margin: 0;
	padding: 1px 2px;
	/*line-height: 1.3;*/ /*text-align: justify*/
}

div.preventionSection {
	width: 100%;
	postion: relative;
	margin-top: 5px;
	float: left;
	clear: left;
}

div.preventionSet {
	border: thin solid grey;
	clear: left;
}

div.recommendations {
	font-family: verdana, tahoma, sans-serif;
	font-size: 1.2em;
}

div.recommendations ul {
	padding-left: 15px;
	margin-left: 1px;
	margin-top: 0px;
	padding-top: 1px;
	margin-bottom: 0px;
	padding-bottom: 0px;
}

div.recommendations li {

}
table.legend{
border:0;
padding-top:10px;
width:420px;
}

table.legend td{
font-size:8;
text-align:left;

}


table.colour_codes{
width:8px;
height:10px;
border:1px solid #999999;
}


</style>

<!--[if IE]>
<style type="text/css">

table.legend{
border:0;
margin-top:10px;
width:370px;
}

table.legend td{
font-size:10;
text-align:left;
}

</style>
<![endif]-->

</head>

<body class="BodyStyle">
<!--  -->
<%=WebUtilsOld.popErrorAndInfoMessagesAsHtml(session)%>
<table class="MainTable" id="scrollNumber1">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message key="oscarprevention.index.oscarpreventiontitre" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><%=nameAge%></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="prevention" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">


		<div class="leftBox">
		<h3>&nbsp;Preventions</h3>
		<div style="background-color: #EEEEFF;">
		<ul>
			<%
			Map<String,Boolean> shown = new HashMap<String,Boolean>();
			List<ProgramProvider> programProviders = programManager2.getProgramDomain(loggedInInfo,loggedInInfo.getLoggedInProviderNo());
			
			for (int i = 0 ; i < prevList.size(); i++){
				HashMap<String,String> h = prevList.get(i);
                String prevName = h.get("name");
                
            	if(!StringUtils.isEmpty(h.get("private"))) {
                	String key = h.get("private");
					if(key != null) {
					
						String programs = OscarProperties.getInstance().getProperty(key);
						if(programs != null) {
							String[] programNos = programs.split(",");
							
							for(ProgramProvider programProvider:programProviders) {
								
								if(contains(programNos,String.valueOf(programProvider.getProgramId()))) {
									continue;
								}
							}
						} else {
							MiscUtils.getLogger().warn("property " + programs + " should have a comma separated list of programNos");
						}
					} else {
						MiscUtils.getLogger().warn("prevention " + h.get("name") + " has an invalid private attribute. It should map to a property name");
					}
            	}
            %>
            <%
            if(!preventionManager.hideItem(prevName) && shown.get(prevName) == null){
            %>
			<li style="margin-top: 2px;"><a
				href="javascript: function myFunction() {return false; }"
				onclick="javascript:popup(465,635,'AddPreventionData.jsp?prevention=<%= java.net.URLEncoder.encode(prevName) %>&amp;demographic_no=<%=demographic_no%>&amp;prevResultDesc=<%= java.net.URLEncoder.encode(h.get("resultDesc")) %>','addPreventionData<%=Math.abs(prevName.hashCode()) %>')" title="<%=h.get("desc")%>">
			<%=prevName%> </a></li>
			
			<%
			shown.put(prevName,true);
            }
            }
            %>
		</ul>
		</div>
		</div>
		<oscar:oscarPropertiesCheck property="IMMUNIZATION_IN_PREVENTION"
			value="yes">
			<a href="javascript: function myFunction() {return false; }"
				onclick="javascript:popup(700,960,'<rewrite:reWrite jspPage="../oscarEncounter/immunization/initSchedule.do"/>?demographic_no=<%=demographic_no%>','oldImms')">Old
			<bean:message key="global.immunizations" /></a>
			<br>
		</oscar:oscarPropertiesCheck></td>

		<form name="printFrm" method="post" onsubmit="return onPrint();"
			action="<rewrite:reWrite jspPage="printPrevention.do"/>">
		<td valign="top" class="MainTableRightColumn">
		<a href="#" onclick="popup(600,800,'http://www.phac-aspc.gc.ca/im/is-cv/index-eng.php')">Immunization Schedules - Public Health Agency of Canada</a>

		<%
				if (MyOscarUtils.isMyOscarEnabled((String) session.getAttribute("user")))
				{
					MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);
           		  	boolean enabledMyOscarButton=MyOscarUtils.isMyOscarSendButtonEnabled(myOscarLoggedInInfo, Integer.valueOf(demographic_no));
					if (enabledMyOscarButton)
					{
						String sendDataPath = request.getContextPath() + "/phr/send_medicaldata_to_myoscar.jsp?"
								+ "demographicId=" + demographic_no + "&"
								+ "medicalDataType=Immunizations" + "&"
								+ "parentPage=" + request.getRequestURI() + "?demographic_no=" + demographic_no;
		%>
		| | <a href="<%=sendDataPath%>"><%=LocaleUtils.getMessage(request, "SendToPHR")%></a>
		<%
					}
					else
					{
		%>
		| | <span style="color:grey;text-decoration:underline"><%=LocaleUtils.getMessage(request, "SendToPHR")%></span>
		<%
					}
				}
		%>

		<%
                if (warnings.size() > 0 || recomendations.size() > 0  || dsProblems) { %>
		<div class="recommendations">
		<%
                    if(printError) {
                   %>
		<p style="color: red; font-size: larger">An error occurred while
		trying to print</p>
		<%
                    }
                   %> <span style="font-size: larger;">Prevention
		Recommendations</span>
		<ul>
			<% for (int i = 0 ;i < warnings.size(); i++){
                       String warn = (String) warnings.get(i);%>
			<li style="color: red;"><%=warn%></li>
			<%}%>
			<% for (int i = 0 ;i < recomendations.size(); i++){
                       String warn = (String) recomendations.get(i);%>
			<li style="color: black;"><%=warn%></li>
			<%}%>
			<!--li style="color: red;">6 month TD overdue</li>
                 <li>12 month MMR due in 2 months</li-->
			<% if (dsProblems){ %>
			<li style="color: red;">Decision Support Had Errors Running.</li>
			<% } %>
		</ul>
		</div>
		<% }
	
	 String[] ColourCodesArray=new String[7];
	 ColourCodesArray[1]="#F0F0E7"; //very light grey - completed or normal
	 ColourCodesArray[2]="#FFDDDD"; //light pink - Refused
	 ColourCodesArray[3]="#FFCC24"; //orange - Ineligible
	 ColourCodesArray[4]="#FF00FF"; //dark pink - pending
	 ColourCodesArray[5]="#ee5f5b"; //dark salmon to match part of bootstraps danger - abnormal
	 ColourCodesArray[6]="#BDFCC9"; //green - other

	 //labels for colour codes
	 String[] lblCodesArray=new String[7];
	 lblCodesArray[1]="Completed or Normal";
	 lblCodesArray[2]="Refused";
	 lblCodesArray[3]="Ineligible";
	 lblCodesArray[4]="Pending";
	 lblCodesArray[5]="Abnormal";
	 lblCodesArray[6]="Other";

	 //Title ie: Legend or Profile Legend
	 String legend_title="Legend: ";

	 //creat empty builder string
	 String legend_builder=" ";


	 	for (int iLegend = 1; iLegend < 7; iLegend++){

			legend_builder +="<td> <table class='colour_codes' style=\"white-space:nowrap;\" bgcolor='"+ColourCodesArray[iLegend]+"'><tr><td> </td></tr></table> </td> <td align='center' style=\"white-space:nowrap;\">"+lblCodesArray[iLegend]+"</td>";

		}

	 	String legend = "<table class='legend' cellspacing='0'><tr><td><b>"+legend_title+"</b></td>"+legend_builder+" </tr></table>";

		out.print(legend);
%>

		<div>
		<input type="hidden" name="demographic_no" value="<%=demographic_no%>">
		<input type="hidden" name="hin" value="<%=hin%>"/>
		<input type="hidden" name="mrp" value="<%=mrp%>" />
                <input type="hidden" name="module" value="prevention">
		<%
                 if (!oscar.OscarProperties.getInstance().getBooleanProperty("PREVENTION_CLASSIC_VIEW","yes")){
                	 shown.clear();
                   ArrayList<Map<String,Object>> hiddenlist = new ArrayList<Map<String,Object>>();
                  for (int i = 0 ; i < prevList.size(); i++){
                  		HashMap<String,String> h = prevList.get(i);
                        String prevName = h.get("name");
                        
                        //TODO:
                        if(!StringUtils.isEmpty(h.get("private"))) {
                        	String key = h.get("private");
        					if(key != null) {
        					
        						String programs = OscarProperties.getInstance().getProperty(key);
        						if(programs != null) {
        							String[] programNos = programs.split(",");
        							
        							for(ProgramProvider programProvider:programProviders) {
        								
        								if(contains(programNos,String.valueOf(programProvider.getProgramId()))) {
        									continue;
        								}
        							}
        						} else {
        							MiscUtils.getLogger().warn("property " + programs + " should have a comma separated list of programNos");
        						}
        					} else {
        						MiscUtils.getLogger().warn("prevention " + h.get("name") + " has an invalid private attribute. It should map to a property name");
        					}
        					
                    	}
                        
                        ArrayList<Map<String,Object>> alist = PreventionData.getPreventionData(loggedInInfo, prevName, Integer.valueOf(demographic_no));
                        PreventionData.addRemotePreventions(loggedInInfo, alist, demographicId,prevName,demographicDateOfBirth);
                        boolean show = pdc.display(loggedInInfo, h, demographic_no,alist.size());
                        if(!show){
                            Map<String,Object> h2 = new HashMap<String,Object>();
                            h2.put("prev",h);
                            h2.put("list",alist);
                            hiddenlist.add(h2);
                            
                        }else{
                        	if(shown.get(prevName) != null) {
                        		continue;
                        	}
                        	shown.put(prevName, true);
               %>

		<div class="preventionSection">
		<%
                    if( alist.size() > 0 ) {
                    %>
		<div style="position: relative; float: left; padding-right: 10px;">
		<input style="display: none;" type="checkbox" name="printHP"
			value="<%=i%>" checked /> <%}else {%>
		<div style="position: relative; float: left; padding-right: 25px;">
		<span style="display: none;" name="printSp">&nbsp;</span> <%}%>
		</div>
		<div class="headPrevention">
		<p><a href="javascript: function myFunction() {return false; }"
			onclick="javascript:popup(465,635,'AddPreventionData.jsp?prevention=<%= response.encodeURL( h.get("name")) %>&amp;demographic_no=<%=demographic_no%>&amp;prevResultDesc=<%= java.net.URLEncoder.encode(h.get("resultDesc")) %>','addPreventionData<%=Math.abs( ( h.get("name")).hashCode() ) %>')">
		<span title="<%=h.get("desc")%>" style="font-weight: bold;"><%=h.get("name")%></span>
		</a>
		<br />
		</p>
		</div>
		<%              String result;
                        for (int k = 0; k < alist.size(); k++){
                        Map<String,Object> hdata = alist.get(k);
                        Map<String,String> hExt = PreventionData.getPreventionKeyValues((String)hdata.get("id"));
                        result = hExt.get("result");

                        String onClickCode="javascript:popup(465,635,'AddPreventionData.jsp?id="+hdata.get("id")+"&amp;demographic_no="+demographic_no+"','addPreventionData')";
                        if (hdata.get("id")==null) onClickCode="popup(300,500,'display_remote_prevention.jsp?remoteFacilityId="+hdata.get("integratorFacilityId")+"&remotePreventionId="+hdata.get("integratorPreventionId")+"&amp;demographic_no="+demographic_no+"')";
                        %>
             
		<div class="preventionProcedure" onclick="<%=onClickCode%>" title="fade=[on] header=[<%=hdata.get("age")%> -- Date:<%=hdata.get("prevention_date_no_time")%>] body=[<%=hExt.get("comments")%>&lt;br/&gt;Entered By: <%=hdata.get("provider_name")%>]">
		
		<!--this is setting the style <%=r(hdata.get("refused"),result)%>  -->
		<p <%=r(hdata.get("refused"),result)%> >Age: <%=hdata.get("age")%> <%if(result!=null && result.equals("abnormal")){out.print("result:"+result);}%> <br />
		<!--<%=refused(hdata.get("refused"))%>-->Date: <%=hdata.get("prevention_date_no_time")%>
		<%if (hExt.get("comments") != null && (hExt.get("comments")).length()>0) {
                    if (oscar.OscarProperties.getInstance().getBooleanProperty("prevention_show_comments","yes")){%>
                    <div class="comments"><span><%=hExt.get("comments")%></span></div>
               <%   } else { %>
            <span class="footnote">1</span>
            <%      }
                 }%>
		<%=getFromFacilityMsg(hdata)%></p>
		</div>
		<%}%>
		</div>
		<%
                        }
                    } %> <a href="#"
			onclick="Element.toggle('otherElements'); return false;"
			style="font-size: xx-small;">show/hide all other Preventions</a>
		<div style="display: none;" id="otherElements">
		<%for (int i = 0 ; i < hiddenlist.size(); i++){
						Map<String,Object> h2 = hiddenlist.get(i);
						HashMap<String,String> h = (HashMap<String,String>) h2.get("prev");
                        String prevName = h.get("name");
                        ArrayList<HashMap<String,String>> alist = (ArrayList<HashMap<String,String>>)  h2.get("list");
                        %>
		<div class="preventionSection">
		<%
                            if( alist.size() > 0 ) {
                            %>
		<div style="position: relative; float: left; padding-right: 10px;">
		<input style="display: none;" type="checkbox" name="printHP"
			value="<%=i%>" checked /> <%}else {%>
		<div style="position: relative; float: left; padding-right: 25px;">
		<span style="display: none;" name="printSp">&nbsp;</span> <%}%>
		</div>
		<div class="headPrevention">
		<p><a href="javascript: function myFunction() {return false; }"
			onclick="javascript:popup(465,635,'AddPreventionData.jsp?prevention=<%= response.encodeURL( h.get("name")) %>&amp;demographic_no=<%=demographic_no%>&amp;prevResultDesc=<%= java.net.URLEncoder.encode(h.get("resultDesc")) %>','addPreventionData<%=Math.abs( ( h.get("name")).hashCode() ) %>')">
		<span title="<%=h.get("desc")%>" style="font-weight: bold;"><%=h.get("name")%></span>
		</a>
		<br />
		</p>
		</div>
		<%
                            String result;
                            for (int k = 0; k < alist.size(); k++){
                           		Map<String,String> hdata = alist.get(k);
                            Map<String,String> hExt = PreventionData.getPreventionKeyValues(hdata.get("id"));
                            result = hExt.get("result");
                            %>
		<div class="preventionProcedure" onclick="javascript:popup(465,635,'AddPreventionData.jsp?id=<%=hdata.get("id")%>&amp;demographic_no=<%=demographic_no%>','addPreventionData')" title="fade=[on] header=[<%=hdata.get("age")%> -- Date:<%=hdata.get("prevention_date_no_time")%>] body=[<%=hExt.get("comments")%>&lt;br/&gt;Entered By: <%=hdata.get("provider_name")%>]">
		<p <%=r(hdata.get("refused"), result)%>>Age: <%=hdata.get("age")%> <br />
		<!--<%=refused(hdata.get("refused"))%>-->Date: <%=hdata.get("prevention_date_no_time")%>
		<%if (hExt.get("comments") != null && (hExt.get("comments")).length()>0) {
                     if (oscar.OscarProperties.getInstance().getBooleanProperty("prevention_show_comments","yes")){ %>
                     <div class="comments"><span><%=hExt.get("comments")%></span></div>
               <%   } else { %>                
            <span class="footnote">1</span>
            <%      }
                }%>
		</p>
		</div>
		<%}%>
		</div>

		<%}%>
		</div>
		<%}else{  //OLD
                    if (configSets == null )
                    {
                  	  configSets = new ArrayList<Map<String,Object>>();
                  	}

                    for ( int setNum = 0; setNum < configSets.size(); setNum++){
                  	  Map<String,Object> setHash = configSets.get(setNum);
                    String[] prevs = (String[]) setHash.get("prevList");
                    %>
		<div class="immSet">
		<h2 style="display: block;"><%=setHash.get("title")%> <span><%=setHash.get("effective")%></span></h2>
		<!--a style="font-size:xx-small;" onclick="javascript:showHideItem('<%="prev"+setNum%>')" href="javascript: function myFunction() {return false; }" >show/hide</a-->
		<a href="#"
			onclick="Element.toggle('<%="prev"+setNum%>'); return false;"
			style="font-size: xx-small;">show/hide</a>
		<div class="preventionSet"
			<%=pdc.getDisplay(loggedInInfo, setHash,demographic_no)%>;"  id="<%="prev"+setNum%>">
		<%for (int i = 0; i < prevs.length ; i++) {
			HashMap<String,String> h = pdc.getPrevention(loggedInInfo, prevs[i]); 
			if(h == null) { //this happens with private entries
				continue;
			}
			%>
		<div class="preventionSection">
		<div class="headPrevention">
		<p><a href="javascript: function myFunction() {return false; }"
			onclick="javascript:popup(465,635,'AddPreventionData.jsp?prevention=<%= response.encodeURL( h.get("name")) %>&amp;demographic_no=<%=demographic_no%>&amp;prevResultDesc=<%= java.net.URLEncoder.encode(h.get("resultDesc")) %>','addPreventionData<%=Math.abs(h.get("name").hashCode())%>')">
		<span title="<%=h.get("desc")%>" style="font-weight: bold;"><%=h.get("name")%></span>
		</a>  <br />
		</p>
		</div>
		<%
            String prevType=h.get("name");
            ArrayList<Map<String,Object>> alist = PreventionData.getPreventionData(loggedInInfo, prevType, Integer.valueOf(demographic_no));
            PreventionData.addRemotePreventions(loggedInInfo, alist, demographicId, prevType,demographicDateOfBirth);
            String result;
            for (int k = 0; k < alist.size(); k++){
            	Map<String,Object> hdata = alist.get(k);
          	  Map<String,String> hExt = PreventionData.getPreventionKeyValues((String)hdata.get("id"));
            result = hExt.get("result");

            String onClickCode="javascript:popup(465,635,'AddPreventionData.jsp?id="+hdata.get("id")+"&amp;demographic_no="+demographic_no+"','addPreventionData')";
            if (hdata.get("id")==null) onClickCode="popup(300,500,'display_remote_prevention.jsp?remoteFacilityId="+hdata.get("integratorFacilityId")+"&remotePreventionId="+hdata.get("integratorPreventionId")+"&amp;demographic_no="+demographic_no+"')";
        %>
		<div class="preventionProcedure" onclick="<%=onClickCode%>">
		<p <%=r(hdata.get("refused"),result)%>>Age: <%=hdata.get("age")%> <br />
		<!--<%=refused(hdata.get("refused"))%>-->Date: <%=hdata.get("prevention_date_no_time")%>
		<%=getFromFacilityMsg(hdata)%></p>
		</div>
		<%}%>
		</div>
		<%}%>
		</div>
		</div>
		<!--immSet--> <%}
                    }%>
		</div>
		<%=legend %>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">
			<input type="button" class="noPrint" name="printButton" onclick="EnablePrint(this)" value="Enable Print">
<!--
			<br>
			<input type="button" name="sendToPhrButton" value="Send To MyOscar (PDF)" style="display: none;" onclick="sendToPhr(this)">
-->
		</td>
            
                <input type="hidden" id="demographicNo" name="demographicNo" value="<%=demographic_no%>"/> 		

		<%
		    for (int i = 0 ; i < prevList.size(); i++){
		   	 	HashMap<String,String> h = prevList.get(i);
		        String prevName = h.get("name");
		        ArrayList<Map<String,Object>> alist = PreventionData.getPreventionData(loggedInInfo, prevName, Integer.valueOf(demographic_no));
		        PreventionData.addRemotePreventions(loggedInInfo, alist, demographicId, prevName,demographicDateOfBirth);
		        if( alist.size() > 0 ) { %>
		<input type="hidden" id="preventionHeader<%=i%>"
			name="preventionHeader<%=i%>" value="<%=h.get("name")%>">

		<%
		            for (int k = 0; k < alist.size(); k++){
		            	Map<String,Object> hdata = alist.get(k);
                                Map<String,String> hExt = PreventionData.getPreventionKeyValues((String)hdata.get("id"));
		    %>
		<input type="hidden" id="preventProcedureStatus<%=i%>-<%=k%>"
			name="preventProcedureStatus<%=i%>-<%=k%>"
			value="<%=hdata.get("refused")%>">
		<input type="hidden" id="preventProcedureAge<%=i%>-<%=k%>"
			name="preventProcedureAge<%=i%>-<%=k%>"
			value="<%=hdata.get("age")%>">
		<input type="hidden" id="preventProcedureDate<%=i%>-<%=k%>"
			name="preventProcedureDate<%=i%>-<%=k%>"
			value="<%=hdata.get("prevention_date_no_time")%>">
                    <%  String comments = hExt.get("comments");
                        if (comments != null && !comments.isEmpty() && OscarProperties.getInstance().getBooleanProperty("prevention_show_comments","true")) {%>      
                <input type="hidden" id="preventProcedureComments<%=i%>-<%=k%>"
			name="preventProcedureComments<%=i%>-<%=k%>"
			value="<%=comments%>">
                    <% }
                            	     }
                                       }
		    } //for there are preventions
		    %>
		</form>
	</tr>
</table>

<script type="text/javascript" src="../share/javascript/boxover.js"></script>
</body>
</html:html> 
<%!
String refused(Object re){
        String ret = "Given";
        if (re instanceof java.lang.String){

        if (re != null && re.equals("1")){
           ret = "Refused";
        }
        }
        return ret;
    }

String r(Object re, String result){
        String ret = "";
        if (re instanceof java.lang.String){
           if (re != null && re.equals("1")){
              ret = "style=\"background: #FFDDDD;\"";
           }else if(re !=null && re.equals("2")){
              ret = "style=\"background: #FFCC24;\"";
           }
           else if( result != null && result.equalsIgnoreCase("pending")) {
               ret = "style=\"background: #FF00FF;\"";
           }
           else if( result != null && result.equalsIgnoreCase("other")) {
               ret = "style=\"background: #BDFCC9;\"";
           }
           else if(result!=null && result.equals("abnormal")){
	        	   ret = "style=\"background: #ee5f5b;\"";
	           
           }
        }
        return ret;
    }

private boolean contains(String[] arr, String val) {
	for(String a:arr) {
		if(val.equals(a)) {
			return true;
		}
	}
	return false;
}
%>
