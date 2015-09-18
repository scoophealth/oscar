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
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarReport.data.*,oscar.oscarPrevention.pageUtil.*,java.net.*,oscar.eform.*"%>
<%@page import="oscar.OscarProperties, org.oscarehr.util.SpringUtils, org.oscarehr.common.dao.BillingONCHeader1Dao" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

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
  String demographic_no = request.getParameter("demographic_no");

  oscar.oscarReport.data.RptSearchData searchData  = new oscar.oscarReport.data.RptSearchData();
  ArrayList queryArray = searchData.getQueryTypes();

  String preventionText = "";

  String eformSearch = (String) request.getAttribute("eformSearch");
  //EfmData efData = new EfmData();
  BillingONCHeader1Dao bCh1Dao = (BillingONCHeader1Dao)SpringUtils.getBean("billingONCHeader1Dao");
%>

<html:html locale="true">

<head>
<html:base/>
<title><bean:message key="oscarprevention.index.oscarpreventiontitre" /></title><!-- i18n -->

<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" >

<script type="text/javascript" src="../share/calendar/calendar.js" ></script>
<script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>" ></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js" ></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>
<script type="text/javascript" src="../share/javascript/sortable.js"></script>
<style type="text/css">
  div.ImmSet { background-color: #ffffff; }
  div.ImmSet h2 {  }
  div.ImmSet ul {  }
  div.ImmSet li {  }
  div.ImmSet li a { text-decoration:none; color:blue;}
  div.ImmSet li a:hover { text-decoration:none; color:red; }
  div.ImmSet li a:visited { text-decoration:none; color:blue;}
</style>

<script type="text/javascript">

//update all selected patient's records with next contact method
//still need to generate before values are saved
function setNextContactMethod(selectElem) {
	var nextSelectedContactMethod = selectElem.options[selectElem.selectedIndex].value;
	
	var chckbxSelectedContactMethod = document.getElementsByName("nsp");
	var displayId;
	var currentValue;
	var idNum;
	var indexPos;
	
	if( nextSelectedContactMethod == "other" ) {
		nextSelectedContactMethod = prompt("Enter next contact method: ");
		if( nextSelectedContactMethod == null ) {
			return;
		}
	}
	
	for( var idx = 0; idx < chckbxSelectedContactMethod.length; ++idx ) {
		if( chckbxSelectedContactMethod[idx].checked ) {
			currentValue = chckbxSelectedContactMethod[idx].value.split(",");		
			currentValue[0] += "," + nextSelectedContactMethod;
			chckbxSelectedContactMethod[idx].value = currentValue[0];		
			
			idNum = chckbxSelectedContactMethod[idx].id.substr(9);
			
			displayId = "nextSuggestedProcedure" + idNum;
			$(displayId).update(nextSelectedContactMethod);
		}
	}
	
}

var nspChecked = false;
function selectAllnsp() {
	var chckbxSelectedContactMethod = document.getElementsByName("nsp");
	
	for( var idx = 0; idx < chckbxSelectedContactMethod.length; ++idx ) {
		if( nspChecked ) {
			chckbxSelectedContactMethod[idx].checked = false;
		}
		else {
			chckbxSelectedContactMethod[idx].checked = true;			
		}
	}
	
	nspChecked = !nspChecked;
}

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

function batchBill() {
    var frm = document.forms["frmBatchBill"];
    var url = "<c:out value="${ctx}"/>" + "/billing/CA/ON/BatchBill.do";

    new Ajax.Request(
        url,
        {
            method: 'post',
            postBody: Form.serialize(frm),
            asynchronous: true,
            onSuccess: function(ret) {
                alert("Billing Complete!");
            },
            onFailure: function(ret) {
                alert( ret.status + " Billing Failed");
            }
        }

    );

    return false;
}

function saveContacts() {
	var frm = document.forms["frmBatchBill"];
	var url = "<c:out value="${ctx}"/>" + "/oscarMeasurement/AddShortMeasurement.do?method=addMeasurements";
	
    new Ajax.Request(
            url,
            {
                method: 'post',
                postBody: Form.serialize(frm),
                asynchronous: true,
                onSuccess: function(ret) {
                    window.location.reload();
                },
                onFailure: function(ret) {
                    alert( ret.status + " There was a problem saving contacts.");
                }
            }

        );

        return false;

}

</script>


<script type="text/javascript">



    //Function sends AJAX request to action
    function completedProcedure(idval,followUpType,procedure,demographic){
       var comment = prompt('Are you sure you want to added this to patients record \n\nAdd Comment Below ','');
       if (comment != null){
          var params = "id="+idval+"&followupType="+followUpType+"&followupValue="+procedure+"&demos="+demographic+"&message="+comment;
          var url = "../oscarMeasurement/AddShortMeasurement.do";

          new Ajax.Request(url, {method: 'get',parameters:params,asynchronous:true,onComplete: followUp});
       }
       return false;
    }

    function followUp(origRequest){
        //alert(origRequest.responseText);
        var hash = origRequest.responseText.parseQuery();
        //alert( hash['id'] + " " + hash['followupValue']+" "+hash['Date'] );
        //("id="+id+"&followupValue="+followUpValue+"&Date=
        var lastFollowupTD = $('lastFollowup'+hash['id']);
        var nextProcedureTD = $('nextSuggestedProcedure'+hash['id']);
        //alert(nextProcedureTD);
        nextProcedureTD.innerHTML = "----";
        lastFollowupTD.innerHTML = hash['followupValue']+" "+hash['Date'];

        //alert(nextProcedureTD.innerText);

    }
</script>



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

span.labelLook{
font-weight:bold;

}

input, textarea,select{

margin-bottom: 5px;
}

textarea{
width: 250px;
height: 150px;
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

table.ele {

   border-collapse:collapse;
}

table.ele td{
    border:1px solid grey;
    padding:2px;
}

/* Sortable tables */
table.ele thead {
    background-color:#eee;
    color:#666666;
    font-size: x-small;
    cursor: default;
}
</style>

<style type="text/css" media="print">
.MainTable {
    display:none;
}
.hiddenInPrint{
    display:none;
}
.shownInPrint{
    display:block;
}

</style>


</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
    <table  class="MainTable" id="scrollNumber1" >
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn" width="100" >
               <bean:message key="oscarprevention.index.oscarpreventiontitre" />
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            Prevention Reporting
                        </td>
                        <td  >&nbsp;
		               <a href="../report/ManageLetters.jsp?goto=success_manage_from_prevention" target="_blank">manage letters</a>
                        </td>
                        <td style="text-align:right">
                                <oscar:help keywords="report" key="app.top1"/> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn" valign="top">
               &nbsp;
            </td>
            <td valign="top" class="MainTableRightColumn">
               <html:form action="/oscarPrevention/PreventionReport" method="get">
               <div>
                  Patient Set:
                  <html:select property="patientSet">
                      <html:option value="-1" >--Select Set--</html:option>
                      <%for (int i =0 ; i < queryArray.size(); i++){
                        RptSearchData.SearchCriteria sc = (RptSearchData.SearchCriteria) queryArray.get(i);
                        String qId = sc.id;
                        String qName = sc.queryName;%>
                        <html:option value="<%=qId%>"><%=qName%></html:option>
                      <%}%>
                  </html:select>
               </div>
               <div>
                  Prevention Query:
                  <html:select property="prevention">
                      <html:option value="-1" >--Select Query--</html:option>
                      <html:option value="PAP" >PAP</html:option>
                      <html:option value="Mammogram" >Mammogram</html:option>
                      <html:option value="Flu" >Flu</html:option>
                      <html:option value="ChildImmunizations" >Child Immunizations</html:option>
                      <html:option value="FOBT" >FOBT</html:option>
                  </html:select>
               </div>
               <div>
                  As of:
                    <html:text property="asofDate" size="9" styleId="asofDate" /> <a id="date"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a> <br>



               </div>
               <input type="submit" />
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

    <div>
                <%ArrayList overDueList = new ArrayList();
                  ArrayList firstLetter = new ArrayList();
                  ArrayList secondLetter = new ArrayList();
                  ArrayList refusedLetter = new ArrayList();
                  ArrayList phoneCall = new ArrayList();
                  String type = (String) request.getAttribute("ReportType");
                  String ineligible = (String) request.getAttribute("inEligible");
                  String done = (String) request.getAttribute("up2date");
                  String percentage = (String) request.getAttribute("percent");
                  String percentageWithGrace = (String) request.getAttribute("percentWithGrace");
                  String followUpType = (String) request.getAttribute("followUpType");
                  String billCode =  (String) request.getAttribute("BillCode");
                  ArrayList list = (ArrayList) request.getAttribute("returnReport");
                  Date asDate = (Date) request.getAttribute("asDate");
                  if (asDate == null){ asDate = Calendar.getInstance().getTime(); }

                  if (list != null ){ %>
                  <form name="frmBatchBill" action="" method="post">
                      <input type="hidden" name="clinic_view" value="<%=OscarProperties.getInstance().getProperty("clinic_view","")%>">
                      <input type="hidden" name="followUpType" value="<%=followUpType%>">
              <table class="ele" width="90%">
                       <tr>
                       <td>&nbsp;</td>
                       <td style="10%;">Total patients: <%=list.size()%><br/>Ineligible:<%=ineligible%></td>
                       <td style="10%;">Up to Date: <%=done%> = <%=percentage %> %
                         <%if (percentageWithGrace != null){  %>
                           <%-- <br/> With Grace <%=percentageWithGrace%> %
                           --%>
                         <%}%>
                       </td>
                       
                       <td style="40%;">&nbsp;<%=request.getAttribute("patientSet")%> </td>                       
                       <td>	
                       		<select onchange="setNextContactMethod(this)">
                       			<option value="----">Select Contact Method</option>
                       			<option value="Email">Email</option>
                       			<option value="L1">Letter 1</option>
                       			<option value="L2">Letter 2</option>
                       			<option value="myOSCARmsg">MyOSCAR Message</option>
                       			<option value="Newsletter">Newsletter</option>
                       			<option value="other">Other</option>
                       	  	</select>
                       	  	&nbsp;&nbsp;
                       	  	<input type="button" value="Save Contacts" onclick="return saveContacts();">
                       </td>                                                                                                                   
                       <td style="10%;"><input style="float: right" type="button" value="Bill" onclick="return batchBill();"></td>
                       </tr>
             </table>
             <table id="preventionTable" class="sortable ele" width="80%">
                       <thead>
                       <tr>
                          <th class="unsortable">&nbsp;</th>
                          <th>DemoNo</th>
                          <th>DOB</th>
                          <th>Age as of <br/><%=UtilDateUtilities.DateToString(asDate)%></th>
                          <th>Sex</th>
                          <th>Lastname</th>
                          <th>Firstname</th>
                          <th>HIN</th>
                          <%if (type != null ){ %>
                          <th>Guardian</th>
                          <%}%>
                          <th>Phone</th>
                          <th>Address</th>
                          <th>Next Appt.</th>
                          <th>Status</th>
                          <%if (type != null ){ %>
                          <th>Shot #</th>
                          <%}%>                          
                          <th>Bonus Stat</th>
                          <th>Since Last Procedure Date</th>
                          <th>Last Procedure Date</th>
                          <th>Last Contact Method</th>
                          <th>Next Contact Method</th>
                          <th class="unsortable">Select Contact<br><input type="checkbox" onclick="selectAllnsp()"></th>
                          <th>Roster Physician</th>
                          <th class="unsortable">Bill</th>
                       </tr>
                       </thead>
                       <tbody>
                       <%DemographicNameAgeString deName = DemographicNameAgeString.getInstance();
                         DemographicData demoData= new DemographicData();
                         boolean setBill;
                         String enabled = "";
                         int numDays;

                         for (int i = 0; i < list.size(); i++){
                             setBill = false;
                            PreventionReportDisplay dis = (PreventionReportDisplay) list.get(i);
                            Hashtable h = deName.getNameAgeSexHashtable(LoggedInInfo.getLoggedInInfoFromSession(request), dis.demographicNo.toString());
                            org.oscarehr.common.model.Demographic demo = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request),  dis.demographicNo.toString());

                            if ( dis.nextSuggestedProcedure != null ){
                                if (dis.nextSuggestedProcedure.equals("L1")){
                                    firstLetter.add(dis.demographicNo);
                                }else if (dis.nextSuggestedProcedure.equals("L2")){
                                    secondLetter.add(dis.demographicNo);
                                }else if (dis.nextSuggestedProcedure.equals("P1")){
                                    phoneCall.add(dis.demographicNo);
                                    setBill = true;
                                }
                            }

                            if (dis.state != null && dis.state.equals("Refused")){
                                refusedLetter.add(dis.demographicNo);
                                setBill = true;
                            }

                            if (dis.state != null && dis.state.equals("Overdue")){
                               overDueList.add(dis.demographicNo);
                            }

                            if( dis.state != null && dis.billStatus.equals("Y")) {
                              setBill = true;
                            }
                            %>
                       <tr>
                          <td><%=i+1%></td>
                          <td>
                              <a href="javascript: return false;" onClick="popup(724,964,'../demographic/demographiccontrol.jsp?demographic_no=<%=dis.demographicNo%>&amp;displaymode=edit&amp;dboperation=search_detail','MasterDemographic')"><%=dis.demographicNo%></a>
                          </td>
                          <td><%=DemographicData.getDob(demo,"-")%></td>

                          <%if (type == null ){ %>
                          <td><%=demo.getAgeAsOf(asDate)%></td>
                          <td><%=h.get("sex")%></td>
                          <td><%=h.get("lastName")%></td>
                          <td><%=h.get("firstName")%></td>
                          <td><%=demo.getHin()+demo.getVer()%></td>
                          <td><%=demo.getPhone()%> </td>
                          <td><%=demo.getAddress()+" "+demo.getCity()+" "+demo.getProvince()+" "+demo.getPostal()%> </td>
                          <td><oscar:nextAppt demographicNo="<%=demo.getDemographicNo().toString()%>"/></td>
                          <td bgcolor="<%=dis.color%>"><%=dis.state%></td>                          
                          <td bgcolor="<%=dis.color%>"><%=dis.bonusStatus%></td>
                          <td bgcolor="<%=dis.color%>"><%=dis.numMonths%></td>
                          <td bgcolor="<%=dis.color%>"><%=dis.lastDate%></td>


                          <% }else {
                              org.oscarehr.common.model.Demographic demoSDM = demoData.getSubstituteDecisionMaker(LoggedInInfo.getLoggedInInfoFromSession(request), dis.demographicNo.toString());%>
                          <td><%=demo.getAgeAsOf(asDate)%></td>
                          <td><%=h.get("sex")%></td>
                          <td><%=h.get("lastName")%></td>
                          <td><%=h.get("firstName")%></td>
                          <td><%=demo.getHin()+demo.getVer()%></td>
                          <td><%=demoSDM==null?"":demoSDM.getLastName()%><%=demoSDM==null?"":","%> <%= demoSDM==null?"":demoSDM.getFirstName() %>&nbsp;</td>
                          <td><%=demoSDM==null?"":demoSDM.getPhone()%> &nbsp;</td>
                          <td><%=demoSDM==null?"":demoSDM.getAddress()+" "+demoSDM==null?"":demoSDM.getCity()+" "+demoSDM==null?"":demoSDM.getProvince()+" "+demoSDM==null?"":demoSDM.getPostal()%> &nbsp;</td>
                          <td><oscar:nextAppt demographicNo="<%=demo.getDemographicNo().toString()%>"/></td>
                          <td bgcolor="<%=dis.color%>"><%=dis.state%></td>
                          <td bgcolor="<%=dis.color%>"><%=dis.numShots%></td>                          
                          <td bgcolor="<%=dis.color%>"><%=dis.bonusStatus%></td>
                          <td bgcolor="<%=dis.color%>"><%=dis.numMonths%></td>
                          <td bgcolor="<%=dis.color%>"><%=dis.lastDate%></td>

                          <%}%>
                          <td bgcolor="<%=dis.color%>" id="lastFollowup<%=i+1%>">
                             <% if (dis.lastFollowup != null ){ %>
                                 <%=dis.lastFollupProcedure%>
                                 <%=UtilDateUtilities.DateToString(dis.lastFollowup)%>
                                 <%=UtilDateUtilities.getNumMonths(dis.lastFollowup,new Date())%>M
                             <% }else{ %>
                                ----
                             <% } %>
                          </td>
                          <td bgcolor="<%=dis.color%>" id="nextSuggestedProcedure<%=i+1%>">
                              <%if ( dis.nextSuggestedProcedure != null && dis.nextSuggestedProcedure.equals("P1")){ %>
                                 <a href="javascript: return false;" onclick="return completedProcedure('<%=i+1%>','<%=followUpType%>','<%=dis.nextSuggestedProcedure%>','<%=dis.demographicNo%>');"><%=dis.nextSuggestedProcedure%></a>                              
                              <%}else{%>
                                    <%=dis.nextSuggestedProcedure%>
                              <%}%>
                          </td>
                          <td bgcolor="<%=dis.color%>">		
                          	<%if( !setBill ) {%>					                          
                          		<input type="checkbox"  id="selectnsp<%=i+1%>" name="nsp" value="<%=dis.demographicNo%>">
                          	<%} else { %>
                          		&nbsp;
                          	<%} %>
                          </td>
                          <%
                          	String providerName=providerBean.getProperty(demo.getProviderNo());
                          	providerName=StringUtils.trimToEmpty(providerName);
                          %>
                          <td bgcolor="<%=dis.color%>"><%=providerName%></td>
                          <td bgcolor="<%=dis.color%>">
                              <% if( billCode != null && setBill ) {
                                  numDays = bCh1Dao.getDaysSinceBilled(billCode, dis.demographicNo);
                                  //we only want to enable billing if it has been a year since the last invoice was created
                                  enabled = numDays >= 0 && numDays < 365 ? "disabled" : "checked";
                              %>
                              <input type="checkbox" name="bill" <%=enabled%> value="<%=billCode + ";" + dis.demographicNo + ";" + demo.getProviderNo()%>">
                              <%}%>
                          </td>

                       </tr>
                      <%}%>
                    	</tbody>
                    </table>
                    <table class="ele" style="width:80%;">
                      <tr>
                          <td style="text-align:right;"><input type="button" value="Bill" onclick="return batchBill();"></td>

                      </tr>
                    </table>

                    </form>

                  <%}%>
                  <%--
                  <% if ( overDueList.size() > 0 ) {
                        String queryStr = getUrlParamList(overDueList, "demo");
                        %>
                        <a target="_blank" href="../report/GenerateEnvelopes.do?<%=queryStr%>&amp;message=<%=java.net.URLEncoder.encode(request.getAttribute("prevType")+" is due","UTF-8")%>">Add Tickler for Overdue</a>
                  <%}%>
                  --%>

                 <%-- if ( firstLetter.size() > 0 ) {
                        String queryStr = getUrlParamList(firstLetter, "demo");
                        %>
                    <a target="_blank" href="../report/GenerateEnvelopes.do?<%=queryStr%>&message=<%=java.net.URLEncoder.encode("Letter 1 Reminder Letter sent for :"+request.getAttribute("prevType"),"UTF-8")%>">Generate First Envelopes</a>
                  <%}
                    --%>


                  <% if ( firstLetter.size() > 0 ) {
                        String queryStr = getUrlParamList(firstLetter, "demo");
                        %>
                    <a target="_blank" href="../report/GenerateLetters.jsp?<%=queryStr%>&amp;message=<%=java.net.URLEncoder.encode("Letter 1 Reminder Letter sent for :"+request.getAttribute("prevType"),"UTF-8")%>&amp;followupType=<%=followUpType%>&amp;followupValue=L1">Generate First Letter</a>
                  <%}%>

                  <% if ( secondLetter.size() > 0 ) {
                        String queryStr = getUrlParamList(secondLetter, "demo");
                        %>
                    <a target="_blank" href="../report/GenerateLetters.jsp?<%=queryStr%>&amp;message=<%=java.net.URLEncoder.encode("Letter 2 Reminder Letter sent for :"+request.getAttribute("prevType"),"UTF-8")%>&amp;followupType=<%=followUpType%>&amp;followupValue=L2">Generate Second Letter</a>
                  <%}%>

                  <% if ( refusedLetter.size() > 0 ) {
                        String queryStr = getUrlParamList(refusedLetter, "demo");
                        %>
                    <a target="_blank" href="../report/GenerateLetters.jsp?<%=queryStr%>&amp;message=<%=java.net.URLEncoder.encode("Letter 1 Reminder Letter sent for :"+request.getAttribute("prevType"),"UTF-8")%>&amp;followupType=<%=followUpType%>&amp;followupValue=L1">Generate Refused Letter</a>
                  <%}%>


                  <%--
                  <% if ( phoneCall.size() > 0 ) {
                        String queryStr = getUrlParamList(phoneCall, "demo");
                        %>
                        <a target="_blank" href="../report/GenerateSpreadsheet.do?<%=queryStr%>&message=<%=java.net.URLEncoder.encode("Phone call 1 made for : "+request.getAttribute("prevType"),"UTF-8")%>followupType=<%=followUpType%>&followupValue=P1">Generate Phone Call list</a>
                  <%}%>
                  --%>

               </div>

<script type="text/javascript">
    Calendar.setup( { inputField : "asofDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
</script>

</body>
</html:html>

<%!
    String getUrlParamList(ArrayList list,String paramName){
        String queryStr = "";
        for (int i = 0; i < list.size(); i++){
            String demo = String.valueOf(list.get(i));
            if (i == 0){
              queryStr += paramName+"="+demo;
            }else{
              queryStr += "&amp;"+paramName+"="+demo;
            }
        }
        return queryStr;
  }
%>
