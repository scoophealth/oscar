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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page
	import="oscar.util.*, oscar.form.*, oscar.form.data.*,java.util.*,oscar.oscarPrevention.*"%>
<%@ page
	import="oscar.oscarProvider.data.*,oscar.oscarWorkflow.*,oscar.oscarEncounter.oscarMeasurements.bean.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="org.oscarehr.util.LoggedInInfo" %>

<%--
 //TODO: Mother's Information Doesn't save
 //TODO: Reason for injection needs to save better
 //TODO: Injection Input Needs styling
 //TODO: Page shuts when saving an Injection

 This Form works in two modes  no form started mode and started
 
 Use case 1 No form started 
 
 User comes into this 
 
 
 
 What happens when users look at the form from the form history??
 
 
--%>
<%
    String formClass = "RhImmuneGlobulin";
    String formLink = "formRhImmuneGlobulin.jsp";
    
    String demographicNo = request.getParameter("demographic_no");
    if (demographicNo == null){
        demographicNo = (String) request.getAttribute("demographic_no");
    }    
    int demoNo = Integer.parseInt(demographicNo);
    
    
    String formIdStr = "0";
    if(request.getParameter("formId") != null ){   ////TEMPORARY
        formIdStr = request.getParameter("formId");     
    }
    
    int formId = Integer.parseInt(formIdStr);
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    String project_home = request.getContextPath().substring(1);
    boolean bView = false;
    if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true;
    
    List providers = ProviderData.getProviderList();
    String prevDate = UtilDateUtilities.getToday("yyyy-MM-dd");
    String providerName = "";
    String provider = (String) session.getAttribute("user");
    

%>
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
 * GNU General Public License for more details. * 
 * You should have received a copy of the GNU General Public License 
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

<%@page import="org.oscarehr.util.MiscUtils"%><html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Rh Immune Globulin Injection Reporting Form</title>
<html:base />

<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />

<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    
        var choiceFormat  = new Array(6,7,8,9,12,13);        
        var allNumericField = new Array(14,15);
        var allMatch = null;
        var action = "/<%=project_home%>/form/formname.do";        

    </script>

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
    </script>

<script type="text/javascript" src="formScripts.js">
    
    </script>


<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0"
	onload="window.resizeTo(807,833)">
<!--
        @oscar.formDB Table="formAdf" 
        @oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
        @oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'" 
        @oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL" 
        @oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL" 
        @oscar.formDB Field="formEdited" Type="timestamp"  
        -->




<div class="title">Rh Immune Globulin Injection Reporting Form</div>


<%   
             Hashtable h = null;
             String newFlowNeeded = (String) request.getAttribute("newWorkFlowNeeded"); 
           %>
<div>
<fieldset><legend>Current Pregnancy </legend> <% 
                String workflowType = "RH";//request.getParameter("workflowType");
                    //WorkFlowState workFlow = new WorkFlowState();  
                    WorkFlowFactory flowFactory = new WorkFlowFactory();
                    WorkFlow flow = flowFactory.getWorkFlow(workflowType);
                    
                    ArrayList currentWorkFlows = flow.getActiveWorkFlowList(demographicNo);

                    if(currentWorkFlows != null && currentWorkFlows.size() > 0){
                        request.setAttribute("currentWorkFlow",currentWorkFlows.get(0));
                         h = (Hashtable) currentWorkFlows.get(0);
                    }
                    
                if(h != null){
                    String gestAge = "";
                    try{
                       gestAge = ""+UtilDateUtilities.calculateGestationAge( new Date(), (Date) h.get("completion_date"));
                    }catch(Exception gestAgeEx){}
                    %> <span style="margin-right: 20px;">EDD: <%=h.get("completion_date")%></span>
<!-- span style="margin-right:20px;">Start date: <%=h.get("create_date_time")%> </span -->
<span style="margin-right: 20px;">Current State:<%=flow.getState(""+h.get("current_state"))%>
</span> <span style="margin-right: 20px;">Weeks: <%=gestAge%></span> <%}else{%> <span
	style="margin-right: 20px;">No Current Pregnancy</span> <%}%> <br />
<html:form action="/form/RHPrevention">

	<%-- input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" / --%>
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="submit" value="exit" />
	<input type="hidden" name="demographic_no" value="<%=demographicNo%>" />

	<%if (h != null) { %>
	<input type="hidden" name="workflowId" value="<%=h.get("ID")%>" />


	<label>Change State:</label>
	<select name="state">
		<%ArrayList states = new ArrayList(flow.getStates());
                    for (int i = 0; i < states.size(); i++){
                      WFState state = (WFState) states.get(i);
                    %>
		<option value="<%=state.getKey()%>"
			<%= ( state.getKey().equals(h.get("current_state")) ? " selected" : "" )%>><%=state.getName()%></option>

		<%}%>

	</select>
	<%}%>
</fieldset>

<fieldset><legend>Mother's Information</legend> <label>Date
of Referral:</label> <input type="text" name="dateOfReferral"
	id="dateOfReferral" size="9"
	value="<%=props.getProperty("dateOfReferral","")%>" /> <a
	id="dateOfRefButton"><img title="Calendar" src="../images/cal.gif"
	alt="Calendar" border="0" /></a> <label>EDD:</label> <input type="text"
	name="edd" id="end_date" size="9"
	value="<%=props.getProperty("edd","")%>"> <a id="date"><img
	title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a>
<br />

<label>Last Name:</label> <input type="text" name="motherSurname"
	value="<%=props.getProperty("motherSurname","")%>" /> <label>First
Name:</label> <input type="text" name="motherFirstname"
	value="<%=props.getProperty("motherFirstname","")%>" /> <br />
<label>Date of Birth:</label> <input type="text" name="dob" size="9"
	id="dob" value="<%=props.getProperty("dob","")%>" /> <a id="dateOB"><img
	title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a>
<br />

<label>Health Card #:</label> <input type="text" name="motherHIN"
	value="<%=props.getProperty("motherHIN","")%>" /> <label>VC:</label> <input
	type="text" name="motherVC" size="3"
	value="<%=props.getProperty("motherVC","")%>" /> <br />
<label>Address:</label> <input type="text" name="motherAddress"
	value="<%=props.getProperty("motherAddress","")%>" /> <label>City:</label>
<input type="text" name="motherCity"
	value="<%=props.getProperty("motherCity","")%>" /> <br />
<label>Province:</label> <input type="text" name="motherProvince"
	value="<%=props.getProperty("motherProvince","")%>" /> <label>Postal
Code:</label> <input type="text" name="motherPostalCode"
	value="<%=props.getProperty("motherPostalCode","")%>" /> <br />
<label>ABO:</label> <select name="motherABO">
	<option>Not Set</option>
	<option value="A"
		<%=props.getProperty("motherABO", "").equalsIgnoreCase("A")?"selected":""%>>A</option>
	<option value="B"
		<%=props.getProperty("motherABO", "").equalsIgnoreCase("B")?"selected":""%>>B</option>
	<option value="o"
		<%=props.getProperty("motherABO", "").equalsIgnoreCase("o")?"selected":""%>>O</option>
	<option value="AB"
		<%=props.getProperty("motherABO", "").equalsIgnoreCase("AB")?"selected":""%>>AB</option>
</select> <label class="smallmargin">Rh type:</label> <select name="motherRHtype">
	<option>Not Set</option>
	<option value="N"
		<%=props.getProperty("motherRHtype", "").equalsIgnoreCase("N")?"selected":""%>>Neg</option>
	<option value="P"
		<%=props.getProperty("motherRHtype", "").equalsIgnoreCase("P")?"selected":""%>>Pos</option>
</select> <label class="smallmargin">Antibodies Detected:</label> <select
	name="motherAntibodies">
	<option>Not Set</option>
	<option value="Y"
		<%=props.getProperty("motherAntibodies", "").equalsIgnoreCase("Y")?"selected":""%>>Yes</option>
	<option value="N"
		<%=props.getProperty("motherAntibodies", "").equalsIgnoreCase("N")?"selected":""%>>No</option>
</select> <br />
<label>Hospital for Delivery:</label> <input type="text"
	name="hospitalForDelivery"
	value="<%=props.getProperty("hospitalForDelivery","")%>" /></fieldset>


<fieldset><legend>Physician (OB) / Midwife</legend> <label>Last
Name:</label> <input type="text" name="refPhySurname"
	value="<%=props.getProperty("refPhySurname","")%>" /> <label>First
Name:</label> <input type="text" name="refPhyFirstname"
	value="<%=props.getProperty("refPhyFirstname","")%>" /> <br />
<label>Address:</label> <input type="text" name="refPhyAddress"
	size="20" value="<%=props.getProperty("refPhyAddress","")%>" /> <label>City:</label>
<input type="text" name="refPhyCity"
	value="<%=props.getProperty("refPhyCity","")%>" /> <br />
<label>Province:</label> <input type="text" name="refPhyProvince"
	value="<%=props.getProperty("refPhyProvince","")%>" /> <label>Postal
Code:</label> <input type="text" name="refPhyPostalCode"
	value="<%=props.getProperty("refPhyPostalCode","")%>" /> <br />
<label>Telephone:</label> <input type="text" name="refPhyPhone"
	value="<%=props.getProperty("refPhyPhone","")%>" /> <label>Fax:</label>
<input type="text" name="refPhyFax"
	value="<%=props.getProperty("refPhyFax","")%>" /> <br />
</fieldset>


<fieldset><legend>Family Doctor</legend> <label>Last
Name:</label> <input type="text" name="famPhySurname"
	value="<%=props.getProperty("famPhySurname","")%>" /> <label>First
Name:</label> <input type="text" name="famPhyFirstname"
	value="<%=props.getProperty("famPhyFirstname","")%>" /> <br />
<label>Address:</label> <input type="text" name="famPhyAddress"
	size="20" value="<%=props.getProperty("famPhyAddress","")%>" /> <label>City:</label>
<input type="text" name="famPhyCity"
	value="<%=props.getProperty("famPhyCity","")%>" /> <br />
<label>Province:</label> <input type="text" name="famPhyProvince"
	value="<%=props.getProperty("famPhyProvince","")%>" /> <label>Postal
Code:</label> <input type="text" name="famPhyPostalCode"
	value="<%=props.getProperty("famPhyPostalCode","")%>" /> <br />
<label>Telephone:</label> <input type="text" name="famPhyPhone"
	value="<%=props.getProperty("famPhyPhone","")%>" /> <label>Fax:</label>
<input type="text" name="famPhyFax"
	value="<%=props.getProperty("famPhyFax","")%>" /> <br />
</fieldset>





<fieldset class="obsHist"><legend>Obstetrical
History</legend> <label>G</label> <input type="text" name="obsHisG" size="2"
	value="<%=props.getProperty("obsHisG","")%>" /> <label>P</label> <input
	type="text" name="obsHisP" size="2"
	value="<%=props.getProperty("obsHisP","")%>" /> <label>T</label> <input
	type="text" name="obsHisT" size="2"
	value="<%=props.getProperty("obsHisT","")%>" /> <label>A</label> <input
	type="text" name="obsHisA" size="2"
	value="<%=props.getProperty("obsHisA","")%>" /> <label>L</label> <input
	type="text" name="obsHisL" size="2"
	value="<%=props.getProperty("obsHisL","")%>" /> <br />

<input type="checkbox" name="obsHisTubMolPregYes"
	<%=props.getProperty("obsHisTubMolPregYes","")%>>Yes</input> <input
	type="checkbox" name="obsHisTubMolPregNo"
	<%=props.getProperty("obsHisTubMolPregNo","")%>>No</input> <label>Any
previous tubal or molar pregnancy?</label> <br />
<input type="checkbox" name="obsHisMisAbortionYes"
	<%=props.getProperty("obsHisMisAbortionYes","")%>>Yes</input> <input
	type="checkbox" name="obsHisMisAbortionNo"
	<%=props.getProperty("obsHisMisAbortionNo","")%>>No</input> <label
	style="float: none;">Any previous miscarriage, pregnancy loss,
or therapeutic abortions?</label> <br />
<input type="checkbox" name="obsHisReceiveAntiDYes"
	<%=props.getProperty("obsHisReceiveAntiDYes","")%>>Yes</input> <input
	type="checkbox" name="obsHisReceiveAntiDNo"
	<%=props.getProperty("obsHisReceiveAntiDNo","")%>>No</input> <label>Did
you receive Anti-D during each of these pregnancies or following the
pregnancy loss?</label> <br />



</fieldset>



<fieldset class="obsHist"><legend>Past Medical
History</legend> <input type="checkbox" name="pmHisBlClDisordersYes"
	<%=props.getProperty("pmHisBlClDisordersYes","")%>>Yes</input> <input
	type="checkbox" name="pmHisBlClDisordersNo"
	<%=props.getProperty("pmHisBlClDisordersNo","")%>>No</input> <label>Do
you have any bleeding or clotting disorders?</label> If yes, describe<input
	type="text" name="pmHisBlClDisordersComment"
	value="<%=props.getProperty("pmHisBlClDisordersComment","")%>" /> <br />
<input type="checkbox" name="pmHisBlPlTransfusYes"
	<%=props.getProperty("pmHisBlPlTransfusYes","")%>>Yes</input> <input
	type="checkbox" name="pmHisBlPlTransfusNo"
	<%=props.getProperty("pmHisBlPlTransfusNo","")%>>No</input> <label>Have
you had any blood or platelet transfusions?</label> If yes, when<input
	type="text" name="pmHisBlPlTransfusComment"
	value="<%=props.getProperty("pmHisBlPlTransfusComment","")%>" /></fieldset>


<fieldset class="obsHist"><legend>Allergies</legend> <input
	type="checkbox" name="allReactionsYes"
	<%=props.getProperty("allReactionsYes","")%>>Yes</input> <input
	type="checkbox" name="allReactionsNo"
	<%=props.getProperty("allReactionsNo","")%>>No</input> <label>Any
adverse reactions to previous immune globulin or other blood products?</label>
If yes, describe<input type="text" name="allReactionsComment"
	value="<%=props.getProperty("allReactionsComment","")%>" /> <br />
</fieldset>


<fieldset class="obsHist"><legend>Current Pregnancy</legend>

<label>Father's ABO:</label> <select style="float: none;"
	name="fatherABO">
	<option>Not Set</option>
	<option value="A"
		<%=props.getProperty("fatherABO", "").equalsIgnoreCase("A")?"selected":""%>>A</option>
	<option value="B"
		<%=props.getProperty("fatherABO", "").equalsIgnoreCase("B")?"selected":""%>>B</option>
	<option value="o"
		<%=props.getProperty("fatherABO", "").equalsIgnoreCase("o")?"selected":""%>>O</option>
	<option value="AB"
		<%=props.getProperty("fatherABO", "").equalsIgnoreCase("AB")?"selected":""%>>AB</option>
	<option value="U"
		<%=props.getProperty("fatherABO", "").equalsIgnoreCase("U")?"selected":""%>>Unknown</option>
</select> <label class="smallmargin">Father's Rh type:</label> <select
	style="float: none;" name="fatherRHtype">
	<option>Not Set</option>
	<option value="N"
		<%=props.getProperty("fatherRHtype", "").equalsIgnoreCase("N")?"selected":""%>>Neg</option>
	<option value="P"
		<%=props.getProperty("fatherRHtype", "").equalsIgnoreCase("P")?"selected":""%>>Pos</option>
</select> <br />

<input type="checkbox" name="curPregDueDateChangeYes"
	<%=props.getProperty("curPregDueDateChangeYes","")%>>Yes</input> <input
	type="checkbox" name="curPregDueDateChangeNo"
	<%=props.getProperty("curPregDueDateChangeNo","")%>>No</input> <label>Has
your due date changed during this pregnancy?</label> Comment<input type="text"
	name="curPregDueDateChangeComment"
	value="<%=props.getProperty("curPregDueDateChangeComment","")%>" /> <br />

<input type="checkbox" name="curPregProceduresYes"
	<%=props.getProperty("curPregProceduresYes","")%>>Yes</input> <input
	type="checkbox" name="curPregProceduresNo"
	<%=props.getProperty("curPregProceduresNo","")%>>No</input> <label>Any
procedures during this pregnancy such as amniocentesis, chorionic
villous sampling, cordocentesis, or external cephalic version?</label> If yes,
when<input type="text" name="curPregProceduresComment"
	value="<%=props.getProperty("curPregProceduresComment","")%>" /> <br />

<input type="checkbox" name="curPregBleedingYes"
	<%=props.getProperty("curPregBleedingYes","")%>>Yes</input> <input
	type="checkbox" name="curPregBleedingNo"
	<%=props.getProperty("curPregBleedingNo","")%>>No</input> <label>Any
bleeding or threatened miscarriage during this pregnancy?</label> <br />
If yes, when<input type="text" name="curPregBleedingComment"
	value="<%=props.getProperty("curPregBleedingComment","")%>" /> <br />

<input type="checkbox" name="curPregBleedingContYes"
	<%=props.getProperty("curPregBleedingContYes","")%>>Yes</input> <input
	type="checkbox" name="curPregBleedingContNo"
	<%=props.getProperty("curPregBleedingContNo","")%>>No</input> <label>Has
the bleeding continued?</label> <br />


<input type="checkbox" name="curPregTraumaYes"
	<%=props.getProperty("curPregTraumaYes","")%>>Yes</input> <input
	type="checkbox" name="curPregTraumaNo"
	<%=props.getProperty("curPregTraumaNo","")%>>No</input> <label>Any
abdominal trauma, serious fall or car accident?</label> <br />

<input type="checkbox" name="curPregAntiDYes"
	<%=props.getProperty("curPregAntiDYes","")%>>Yes</input> <input
	type="checkbox" name="curPregAntiDNo"
	<%=props.getProperty("curPregAntiDNo","")%>>No</input> <label>Have
you received any Anti-D during this pregnancy?</label> If yes, when<input
	type="text" name="curPregAntiDComment"
	value="<%=props.getProperty("curPregAntiDComment","")%>" /> <br />

<input type="checkbox" name="curPregAntiDReactionYes"
	<%=props.getProperty("curPregAntiDReactionYes","")%>>Yes</input> <input
	type="checkbox" name="curPregAntiDReactionNo"
	<%=props.getProperty("curPregAntiDReactionNo","")%>>No</input> <label>Any
adverse reaction?</label> <br />

<input type="checkbox" name="curPregBloodDrawnYes"
	<%=props.getProperty("curPregBloodDrawnYes","")%>>Yes</input> <input
	type="checkbox" name="curPregBloodDrawnNo"
	<%=props.getProperty("curPregBloodDrawnNo","")%>>No</input> <label>Blood
sample drawn?</label></fieldset>



<fieldset><legend>Comments</legend> <textarea name="comments"
	style="width: 45em;"><%=props.getProperty("comments","")%></textarea></fieldset>

<input type="submit" value="Save" /> <% 
                if ( h != null && h.get("ID") != null){ %> <input
	type="button"
	onClick="javascript: popup(700,600,'addRhInjection.jsp?demographic_no=<%=demographicNo%>&amp;workflowId=<%=h.get("ID")%>&amp;formId=<%=formId%>','addInjection');"
	value="Add Injection" /> <%-- a style="color:blue; " href="javascript: function myFunction() {return false; }" onClick="popup(700,600,'addRhInjection.jsp?demographic_no=<%=demographicNo%>&amp;workflowId=<%=h.get("ID")%>&amp;formId=<%=formId%>','addInjection')">Add Injection</a --%>
<%}%> </html:form>

<div id="injectionInfo"></div>

<html:form
	action="/oscarPrevention/AddPrevention" styleId="deleteForm"
	target="_blank">

	<input type="hidden" name="id" id="deleteId" />
	<input type="hidden" name="demographic_no" value="<%=demographicNo%>" />

	<input type="hidden" name="delete" value="delete" />
</html:form> <script type="text/javascript">
                
             <%
             //Hack to display injections after a workflow has been closed.
             if (h == null && !props.getProperty("workflowId","").equals("") && !props.getProperty("workflowId","").equals("-1")){
                try{                  
                    h = new  Hashtable();
                    h.put("ID", props.getProperty("workflowId",""));
                    String ddate = props.getProperty("edd","");
                    ddate = ddate.substring(0,10);
                    h.put("completion_date",  new  java.sql.Date( UtilDateUtilities.StringToDate(ddate , "yyyy-MM-dd").getTime() )  );
                        
                }catch(Exception eo){
                	MiscUtils.getLogger().error("Error", eo);
                }
             }

             %>
    
                
                
                
            <%if (h != null) { %>  
            function getInjectionInformation(origRequest){
               //console.log("calling get renal dosing information");
               var url = "../form/RhInjectionDisplay.jsp";
               var ran_number=Math.round(Math.random()*1000000);
               var params = "demographicNo=<%=demographicNo%>&id=<%=h.get("ID")%>&date=<%=(Date) h.get("completion_date")%>&rand="+ran_number;  //hack to get around ie caching the page
               //console.log("params" + params);
               new Ajax.Updater('injectionInfo',url, {method:'get',parameters:params,asynchronous:true}); 
               //alert(origRequest.responseText);
            }
            getInjectionInformation();    
                
                
            function refreshInfo(){
            getInjectionInformation();    
            
            }
                <%}%>
                
                
                function deleteCall(){
                    var url = "../oscarPrevention/AddPrevention.do";
                var data = Form.serialize('deleteForm'); 
                console.log("deleteCall "+data);
                new Ajax.Request(url, {method: 'post',postBody: data,asynchronous:true,onComplete: getInjectionInformation}); 
                }
                
            function deleteInjection(idval){
               var delId = document.getElementById('deleteId');
               delId.value = idval;
               //console.log("calling deleteInjection "+idval);
               
               deleteCall();
               
               //alert(delId.value);
               //document.getElementById('deleteForm').submit();
               
            }
                
            Calendar.setup( { inputField : "prevDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
            Calendar.setup( { inputField : "end_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
            Calendar.setup( { inputField : "dob", ifFormat : "%Y-%m-%d", showsTime :false, button : "dateOB", singleClick : true, step : 1 } );
            Calendar.setup( { inputField : "dateOfReferral", ifFormat : "%Y-%m-%d", showsTime :false, button : "dateOfRefButton", singleClick : true, step : 1 } );
            hideExtraName(document.getElementById('providerDrop'));                                               
        </script>
</body>
</html:html>
