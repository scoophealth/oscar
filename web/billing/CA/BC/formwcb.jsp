<%
  	if (session.getAttribute("user") == null){
		response.sendRedirect("../logout.jsp");
	}
%>

<!--  
/* Copyright (c) 2001-2002. Andromedia. All Rights Reserved. *
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
 * This software was written for 
 * Andromedia, to be provided as
 * part of the OSCAR McMaster
 * EMR System
 */
-->
<%@ page import="oscar.form.*, java.util.*,oscar.oscarBilling.ca.bc.pageUtil.*,oscar.oscarDB.*,oscar.oscarBilling.ca.bc.MSP.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<% 
   boolean readonly = false;
   String readOnlyParam = request.getParameter("readonly");
   if(readOnlyParam != null && readOnlyParam.equals("true")){
      readonly = true;   
   }

   oscar.oscarBilling.ca.bc.pageUtil.WCBForm form = (oscar.oscarBilling.ca.bc.pageUtil.WCBForm) request.getSession().getAttribute("WCBForm"); 
   WcbHelper wcbHelper = new WcbHelper(form.getDemographic());
   ArrayList claims = (ArrayList) wcbHelper.getClaimInfo(form.getDemographic());
   ArrayList emps = (ArrayList) wcbHelper.getEmployers(form.getDemographic());
   
   boolean haveClaims = isEmpty(claims);
   boolean haveEmps   = isEmpty(emps);
      
%>

<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>
<head>
<html:base/>
<title>OSCAR BC Billing - WCB</title>
<link rel="stylesheet" href="../../../share/css/oscar.css">
<link rel="stylesheet" href="../../../share/css/reporting.css">
<link rel="stylesheet" href="../../../share/calendar/calendar.css">
<script src="../../../share/javascript/Oscar.js"></script>
<script src="../../../share/calendar/calendar.js"></script>
<script src="../../../share/calendar/lang/<bean:message key="global.javascript.calendar"/>" type="text/javascript"></script>
<script src="../../../share/calendar/calendar-setup.js" type="text/javascript"></script>

<script language="JavaScript">
function popup( height, width, url, windowName){   
  var page = url;  
  windowprops = "height="+height+",width="+width+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";  
  var popup=window.open(url, windowName, windowprops);  
  if (popup != null){  
    if (popup.opener == null){  
      popup.opener = self;  
    }  
  }  
  popup.focus();  
  return false;  
}

function isformNeeded(){
  if (document.WCBForm.formNeeded.checked){
     //alert("show elements");
     ShowElementById('secondSection1');
     ShowElementById('secondSection2');
     ShowElementById('secondSection3');
     ShowElementById('secondSection4');
     ShowElementById('thirdSection1');
     ShowElementById('thirdSection8');
     ShowElementById('thirdSection9');
     ShowElementById('thirdSection10');
     ShowElementById('forthSectionTitle');
     ShowElementById('forthSection1');
     ShowElementById('forthSection2');
     ShowElementById('forthSection3');
     ShowElementById('forthSection4');
     ShowElementById('forthSection5');
     ShowElementById('forthSection6');
     ShowElementById('forthSection7');
     ShowElementById('forthSection8');
     ShowElementById('secondSectionHeader');
     ShowElementById('thirdSectionHeader');
     ShowElementById('employerSectionTitle');
     ShowElementById('employerSection');
     ShowElementById('reportTypeSection');
     ShowElementById('workersAddressSection');
     
  }else{
     //alert("hide elements");
     HideElementById('secondSection1');
     HideElementById('secondSection2');
     HideElementById('secondSection3');
     HideElementById('secondSection4');
     HideElementById('thirdSection1');
     HideElementById('thirdSection8');
     HideElementById('thirdSection9');
     HideElementById('thirdSection10');
     HideElementById('forthSectionTitle');
     HideElementById('forthSection1');
     HideElementById('forthSection2');
     HideElementById('forthSection3');
     HideElementById('forthSection4');
     HideElementById('forthSection5');
     HideElementById('forthSection6');
     HideElementById('forthSection7');
     HideElementById('forthSection8');
     HideElementById('secondSectionHeader');
     HideElementById('thirdSectionHeader');
     HideElementById('employerSection');
     HideElementById('employerSectionTitle');
     HideElementById('reportTypeSection');
     HideElementById('workersAddressSection');
     if (document.WCBForm.w_feeitem.value == "" && document.WCBForm.w_extrafeeitem.value != ""){
        document.WCBForm.w_feeitem.value = document.WCBForm.w_extrafeeitem.value ;
        document.WCBForm.w_extrafeeitem.value = "";
     }
  }
  }
  
  function HideElementById(ele){
     document.getElementById(ele).style.display='none';
  }

  function ShowElementById(ele){
     document.getElementById(ele).style.display='';
  }

  function showpic(picture,id){
     if (document.getElementById){ // Netscape 6 and IE 5+      
        var targetElement = document.getElementById(picture);                
        var bal = document.getElementById(id);

        var offsetTrail = document.getElementById(id);
        var offsetLeft = 0;
        var offsetTop = 0;
        while (offsetTrail) {
           offsetLeft += offsetTrail.offsetLeft;
           offsetTop += offsetTrail.offsetTop;
           offsetTrail = offsetTrail.offsetParent;
        }
        if (navigator.userAgent.indexOf("Mac") != -1 && 
           typeof document.body.leftMargin != "undefined") {
           offsetLeft += document.body.leftMargin;
           offsetTop += document.body.topMargin;
        }
            
        targetElement.style.left = offsetLeft +bal.offsetWidth;        
        targetElement.style.top = offsetTop;	
        targetElement.style.visibility = 'visible';        
     }
  }

  function hidepic(picture){
     if (document.getElementById){ // Netscape 6 and IE 5+
        var targetElement = document.getElementById(picture);
        targetElement.style.visibility = 'hidden';
     }
  }
  
  function quickPickWCBcode(code){
  document.WCBForm.w_feeitem.value = code;
  }
  
  function popFeeItemList(form,field){
     var width = 575;
     var height = 400;
     var str = document.forms[form].elements[field].value;
     var url = "support/billingfeeitem.jsp?form=" +form+ "&field="+field+"&searchStr=" +str;     
     var windowName = field;  
     popup(height,width,url,windowName);  
  }

  function popICD9List(form,field){
     var width = 575;
     var height = 400;
     var str = document.forms[form].elements[field].value;
     var url = "support/icd9.jsp?form=" +form+ "&field="+field+"&searchStr=" +str;     
     var windowName = field;  
     popup(height,width,url,windowName);    
  }
  
  function popBodyPartList(form,field){
     var width = 650;
     var height = 400;
     var str = document.forms[form].elements[field].value;
     var url = "support/bodypart.jsp?form=" +form+ "&field="+field+"&searchStr=" +str;     
     var windowName = field;  
     popup(height,width,url,windowName);        
  }
  
  function popNOIList(form,field){  
     var width = 800;
     var height = 400;
     var str = document.forms[form].elements[field].value;
     var url = "support/natureinjury.jsp?form=" +form+ "&field="+field+"&searchStr=" +str;     
     var windowName = field;  
     popup(height,width,url,windowName);        
  }
  
  function checkTextLimit(textField, maximumlength) {
     if (textField.value.length > maximumlength + 1){   
        alert("Maximun "+maximumlength+" characters");
     }
     if (textField.value.length > maximumlength){
        textField.value = textField.value.substring(0, maximumlength);
     }
  }
  
  function setClaim(w_wcbno,w_empname,w_opaddress,w_opcity,w_emparea,w_empphone){    
     document.WCBForm.w_wcbno.value = w_wcbno;
     setEmployer(w_empname,w_opaddress,w_opcity,w_emparea,w_empphone);
  }
  
  function setEmployer(w_empname,w_opaddress,w_opcity,w_emparea,w_empphone){    
    document.WCBForm.w_empname.value = w_empname;
    document.WCBForm.w_opaddress.value = w_opaddress;
    document.WCBForm.w_opcity.value = w_opcity;
    document.WCBForm.w_emparea.value = w_emparea;
    document.WCBForm.w_empphone.value = w_empphone;
  }
</script>
</head>
<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<html:form action="/billing/CA/BC/formwcb" >
<input name="w_servicelocation" type="hidden" id="w_servicelocation"  value="<%=form.getW_servicelocation()%>" readonly="true">
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#D3D3D3">
  <tr> 
    <td height="40" width="25"></td>
    <td width="90%" align="left"> 
      <p><font face="Verdana" color="#4D4D4D"><b><font size="4">oscar<font size="3"><bean:message key="billing.bc.title"/> - Workers Compensation Board - Physician Report</font></font></b></font> 
      </p>
    </td>
  </tr>
</table>

<br>
<table border="0" cellspacing="0" cellpading="0" width="100%" class="SmallerText">
	<tr>
	   <td>
	      Form Needed <input type="checkbox" value="1" name="formNeeded" onclick="isformNeeded();"  <%=checked("1",form.getFormNeeded(),true)%> />
	   </td>
		<td colspan="1" valign="top" height="25" class="SmallerText" id="reportTypeSection">
			Physician's First Report <input name="w_reporttype" type="radio" id="w_reporttype" value="F" <%=checked("F",form.getW_reporttype(),true)%>> or The worker's
				condition or treatment has changed: <input name="w_reporttype" type="radio" id="w_reporttype" value="C" <%=checked("C",form.getW_reporttype(),false)%> >
	      </span>
		</td>
	</tr>
	
	<tr class="LightBG">
		<td colspan="2">
		   <%if(!haveClaims){%>
			<a href="javascript: function myFunction() {return false; }"  onClick="showpic('claimLayer','claimId');"  id="claimId" >
			<%}%>
         <b>WCB Claim Number:</B> <input name="w_wcbno" type="text" maxlength="25" id="w_wcbno" value="<%=form.getW_wcbno()%>" />
         <%if(!haveClaims){%>
         </a>
         <%}%>
		</td>
	</tr>
	
	<tr class="LightBG" id="employerSectionTitle">
		<td colspan="2">
			Employer's Info
		</td>
	</tr>
	<tr id="employerSection">
		<td width="100%" align="left" valign="top" colspan=2>
		   
			<table border="0" cellspacing="0" cellpading="0" width="100%">
				<tr id="firstSection1">
					<td class="SmallerText">
					   <%if(!haveClaims){%>
					   <a href="javascript: function myFunction() {return false; }"  onClick="showpic('employerLayer','employerId');"  id="employerId" >
					   <%}%>
                     Employer's Name:
                  <%if(!haveClaims){%>
					   </a>
					   <%}%>
					</td>
               <td><input name="w_empname" type="text" maxlength="25" id="w_empname" value="<%=form.getW_empname()%>"/></td>
				<!--</tr>
				<tr id="firstSection2">-->
					<td class="SmallerText">Operating Address:</td>
               <td><input name="w_opaddress" type="text" id="w_opaddress" maxlength="25" size="25" value="<%=form.getW_opaddress()%>" /></td>
				</tr>
				<tr id="firstSection3">
					<td class="SmallerText">Operating City:</td>
               <td><input name="w_opcity" type="text" id="w_opcity" maxlength="25" value="<%=form.getW_opcity()%>" /></td>
				<!--</tr>
				<tr id="firstSection4">-->
					<td class="SmallerText">Employers Telephone No:</td>
               <td><input name="w_emparea" type="text" id="w_emparea" maxlength="3" size="3" value="<%=form.getW_emparea()%>" />-<input name="w_empphone" type="text" id="w_empphone" maxlength="7" size="10" value="<%=form.getW_empphone()%>"/></td>
					</td>
				</tr>
				
			</table>
	    </td>
	  </tr>
	
	  <tr>
		<td width="100%" align="left" valign="top" colspan=2>
		   
		
			<table cellspacing="0" cellpading="0" border="0" width="100%">
				<tr class="LightBG">
					<td colspan="4" class="SmallerText">
					   Worker's Info
					</td>
				</tr>
				<tr>
					<td colspan="1" class="SmallerText"><b>Last Name:</b> <input name="w_lname" type="text" maxlength="18" id="w_lname" size="20" value="<%=form.getW_lname()%>" />
					</td>
					<td class="SmallerText"><b>First Name:</b> <input name="w_fname" type="text" maxlength="12" id="w_fname" size="20" value="<%=form.getW_fname()%>" />
					</td>
					<td class="SmallerText" colspan="2">Initial: <input name="w_mname" type="text" maxlength="1" id="w_mname" size="1" value="<%=form.getW_mname()%>"/>
					</td>
					
				</tr>
				<tr >					
					
					<td class="SmallerText"><b>Date of Birth:</b> <input name="w_dob" type="text" maxlength="10" readonly id="w_dob" size="10" value="<%=form.getW_dob()%>" />
					</td>
					<td class="SmallerText"><b>Gender:</b> <input name="w_gender" type="text" maxlength="1" readonly id="w_gender" size="1" value="<%=form.getW_gender()%>" />
					</td>
					<td  class="SmallerText"><b>PHN:</b> <input name="w_phn" type="text" id="w_phn" maxlength="12" size="12" value="<%=form.getW_phn()%>" />
					</td>
					<td >&nbsp;
					</td>
				</tr>
				<tr id="workersAddressSection">
					<td  class="SmallerText">
					Worker's Telephone No: <input name="w_area" type="text" maxlength="3" id="w_area" size="3" value="<%=form.getW_area()%>" />
            		<input name="w_phone" type="text" id="w_phone" size="7" maxlength="7" value="<%=form.getW_phone()%>"/>
					</td>	
					<td class="SmallerText">
					Address: <input name="w_address" type="text" id="w_address" size="20" maxlength="25" value="<%=form.getW_address()%>" />
					</td>
					<td class="SmallerText">
					City: <input name="w_city" type="text" id="w_city" size="20" maxlength="20" value="<%=form.getW_city()%>" />
					</td>
					<td class="SmallerText">
					Postal Code: <input name="w_postal" type="text" id="w_postal" maxlength="6" size="6" value="<%=form.getW_postal()%>" />
					</td>
					
					
					</td>
				</tr>
				<tr>
					<td colspan="4" class="SmallerText">
					&nbsp;
					</td>
				</tr>
				
			</table>
			</span>
		</td>
		
	</tr>
	<tr class="LightBG" id="secondSectionHeader">
		<td colspan="2">
			&nbsp;
		</td>
	</tr>
	
	<tr id="secondSection1">
		<td>Are you the worker's regular physician? </td><td>
              <input type="radio" name="w_rphysician" value="Y" <%=checked("Y",form.getW_rphysician(),true)%>>
              Yes <input type="radio" name="w_rphysician" value="N" <%=checked("N",form.getW_rphysician(),false)%>> No
		</td>
	</tr>
	<tr id="secondSection2">
		<td>If yes, how long has the worker been your patient?</td><td><input type="radio" name="w_duration" value="1" <%=checked("1",form.getW_duration(),true)%>>
              0-6 months <input type="radio" name="w_duration" value="2" <%=checked("2",form.getW_duration(),false)%>>
              7-12 months <input type="radio" name="w_duration" value="9" <%=checked("9",form.getW_duration(),false)%>>&gt; 12 months
		</td>
	</tr>
	<tr id="secondSection3">
		<td>Who rendered the first treatment?</td><td><input name="w_ftreatment" type="text" id="w_ftreatment" maxlength="25" length="25" value="<%=form.getW_ftreatment()%>">
		</td>
	</tr>
	<tr id="secondSection4">
		<td>
		Prior/Other Problems Affecting Injury, Recovery and Disability</td><td>
              <textarea name="w_problem" cols="50" id="w_problem" style="height:50px;width:100%;" onkeyup="checkTextLimit(this.form.w_problem,160);"><%=form.getW_problem()%></textarea>
		</td>
	</tr>
	
	<tr class="LightBG" id="thirdSectionHeader">
		<td colspan="2">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="LightBG">Injury Codes and Descriptions</td>
      <td> <b>Date of Service</b>
            <input name="w_servicedate" size="10" type="text" id="w_servicedate"  value="<%=form.getW_servicedate()%>" readonly="true">
            <a id="hlSDate"><img title="Calendar" src="../../../images/cal.gif" alt="Calendar" border="0" /></a>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <b>Date of Injury:</b>
            <input name="w_doi" type="text" id="w_doi"  value="<%=form.getW_doi()%>" readonly="true" size="12">
            <a id="hlDDate"><img title="Calendar" src="../../../images/cal.gif" alt="Calendar" border="0" /></a>
					
		</td>
	</tr>
	<tr id="thirdSection1">
		<td>
			Diagnosis:</td>
          <td><input name="w_diagnosis" type="text" maxlength="120" size="120" id="w_diagnosis" value="<%=form.getW_diagnosis()%>">
		</td>
	</tr>
	<tr>     
		<td>
		   <a href="javascript: function myFunction() {return false; }"  onClick="showpic('Layer1','Calcs');"  id="Calcs" >WCB Fee Item: </a>
                                                         
		</td>
		<td>
			<input name="w_feeitem" type="text" maxlength="5" size="5" id="w_feeitem" value="<%=form.getW_feeitem()%>">                 
                	<a onClick="popFeeItemList('WCBForm','w_feeitem');">Search</a>
                	
		</td>
	</tr>
	<tr>
		<td>Extra Fee Item:</td>
		<td>
			<input name="w_extrafeeitem" type="text" maxlength="5" size="5" id="w_extrafeeitem" value="<%=form.getW_extrafeeitem()%>">                 	
                	<a onClick="popFeeItemList('WCBForm','w_extrafeeitem');">Search</a>
		</td>
	</tr>
	<tr>
		<td><b>ICD9:</b></td>
      <td><input name="w_icd9" size="5" maxlength="5" type="text" id="w_icd9" value="<%=form.getW_icd9()%>">                	
                	<a onClick="popICD9List('WCBForm','w_icd9');">Search</a>                	
		</td>
	</tr>
	<tr>
		<td>
			<b>Body Part:</b></td>
          <td><input name="w_bp" type="text" size="5" maxlength="5" id="w_bp" value="<%=form.getW_bp()%>">                 
                <a onClick="popBodyPartList('WCBForm','w_bp');">Search</a> 
		</td>
	</tr>
	<tr>
		<td>
			<b>Side:</b></td>
          <td><select name="w_side">
<%
                oscar.oscarDB.DBHandler db = new oscar.oscarDB.DBHandler(oscar.oscarDB.DBHandler.OSCAR_DATA);  //TODO no sql statements in the jsp!
                java.sql.ResultSet rs = db.GetSQL("SELECT sidetype, sidedesc FROM wcb_side");
                while (rs.next())
                {
%>
					<option value="<%=rs.getString("sidetype")%>" <%=((form.getW_side().compareTo(rs.getString("sidetype")) == 0) ? "selected" : "")%>><%=rs.getString("sidedesc")%></option>
<%
				}
				rs.close();
				db.CloseConn();
%>
				</select>
		</td>
	</tr>
	<tr>
		<td>
			<b>Nature of Injury</b></td>
          <td><input name="w_noi" maxlength="5" size="5" type="text" id="w_noi" value="<%=form.getW_noi()%>">                 
                <a onClick="popNOIList('WCBForm','w_noi');">Search</a>
                
		</td>
	</tr>
	<tr id="thirdSection8">
		<td>
			From injury or since last report, has the worker been disabled from work? </td><td>
              <input type="radio" name="w_work" value="Y" <%=checked("Y",form.getW_work(),true)%>>Yes 
              <input type="radio" name="w_work" value="N" <%=checked("N",form.getW_work(),false)%>>No</p>
		</td>
	</tr>
	<tr id="thirdSection9">
		<td>
		If Yes, as of what date? (if known) </td><td align="left" valign="top">
              <input  onClick="window.WCBForm.w_workdate.value = '';" name="w_workdate" type="text" id="w_workdate" readonly="true" value="<%=form.getW_workdate()%>"> 
              <a id="hlWDate"><img title="Calendar" src="../../../images/cal.gif" alt="Calendar" border="0" /></a>
		</td>
	</tr>
	<tr id="thirdSection10">
		<td td align="left" valign="top">
		Clinical Information:<br><small>What happened?<br>Subjective Symptoms<br>Examination<br>Investigations<br>Treatment, Meds</small>
		</td><td td align="left" valign="top">
		<textarea name="w_clinicinfo" class="mhAssTextarea" id="w_clinicinfo" style="height:80px;width:100%;"  onkeyup="checkTextLimit(this.form.w_clinicinfo,800);" ><%=form.getW_clinicinfo()%></textarea> 
		</td>
	</tr>
	<tr id="forthSectionTitle">
		<td colspan="2" class="LightBG">
			Return to Work Planning
		</td>
	</tr>
	<tr id="forthSection1">
		<td>
			Is the worker now medically capable of working full duties, full time?
		</td>
		<td>
		<input type="radio" name="w_capability" value="Y" <%=checked("Y",form.getW_capability(),true)%>> Yes <input type="radio" name="w_capability" value="N" <%=checked("N",form.getW_capability(),false)%>> No
		</td>
	</tr>
	<tr id="forthSection2">
		<td valign="top">
			If No: What are the current physical and/or psychological restrictions?
		</td>
		<td>
		   <textarea name="w_capreason"  class="mhAssTextarea" id="w_capreason" style="height:80px;width:100%;"  onkeyup="checkTextLimit(this.form.w_capreason,240);" ><%=form.getW_capreason()%></textarea> 			
		</td>
	</tr>
	<tr id="forthSection3">
		<td valign="top">
		Estimated time before the worker will be able to return to the workplace. </td>
                <td>
					<input type="radio" name="w_estimate" value="0" <%=checked("0",form.getW_estimate(),true)%>>At Work<br> 
					<input type="radio" name="w_estimate" value="1" <%=checked("1",form.getW_estimate(),false)%>>1-6 days 
					<input type="radio" name="w_estimate" value="2" <%=checked("2",form.getW_estimate(),false)%>>7-13 days<br> 
					<input type="radio" name="w_estimate" value="3" <%=checked("3",form.getW_estimate(),false)%>>14-20 days 
					<input type="radio" name="w_estimate" value="9" <%=checked("9",form.getW_estimate(),false)%>>&gt;20 days
				</td>
	</tr>
	<tr id="forthSection4">
		<td>
		If appropriate, is the worker now ready for a rehabilitation program?</td>
                <td><input type="radio" name="w_rehab" value="Y" <%=checked("Y",form.getW_rehab(),false)%> > Yes <input type="radio" name="w_rehab" value="N" <%=checked("N",form.getW_rehab(),true)%>> No
		</td>
	</tr>
	<tr id="forthSection5">
		<td>
		If Yes, Select &quot;WCP&quot; or Other</td><td><input type="radio" name="w_rehabtype" value="C" <%=checked("C",form.getW_rehabtype(),false)%>>WCP
                  <input type="radio" name="w_rehabtype" value="O" <%=checked("O",form.getW_rehabtype(),false)%>>Other
		</td>
	</tr>
	<tr id="forthSection6">
		<td>
		Do you wish to consult with WCB physician or nurse advisor?</td>
                <td><input type="radio" name="w_wcbadvisor" value="Y" <%=checked("Y",form.getW_wcbadvisor(),false)%>> Yes <input type="radio" name="w_wcbadvisor" value="N" <%=checked("N",form.getW_wcbadvisor(),true)%>> No
		</td>
	</tr>
	<tr id="forthSection7">
		<td>
		If possible, please estimate date of Maximal Medical Recovery</td>
                <td><input onClick="window.WCBForm.w_estimatedate.value = '';" name="w_estimatedate" type="text" readonly id="w_estimatedate" value="<%=form.getW_estimatedate()%>"><a id="hlEDate">
                <img title="Calendar" src="../../../images/cal.gif" alt="Calendar" border="0" /></a>
		</td>
	</tr>
	<tr id="forthSection8">
		<td>
		Further Correspondence to Follow: (2nd electronic form or paper) </td>
                <td><input type="radio" name="w_tofollow" value="Y" <%=checked("Y",form.getW_tofollow(),false)%> > Yes <input type="radio" name="w_tofollow" value="N" <%=checked("N",form.getW_tofollow(),true)%>>  No
		</td>
	</tr>
    <tr > 
      <td>Payee Number: <input name="w_payeeno" type="text" readonly id="w_payeeno" size="5" value="<%=form.getW_payeeno()%>"> 
      </td>
      <td>Provider No: <input name="w_pracno" type="text" readonly id="w_pracno" size="5" value="<%=form.getW_pracno()%>"> (<%=form.getW_pracname()%>)</td>
    </tr>
    <tr>
    	<td colspan="2" align="right" valign="top" class="DarkBG">
    	   <%if(!readonly){%><input type="button" name="Submit3" value="Go Back" onClick="location.href='billingBC.jsp?loadFromSession=yes'"/>|<%}%>
			<input type="button" name="Button" value="Cancel" onClick="window.close();"/> | 
			<input type="button" name="Button" value="Print" onClick="window.print()"/> |
         <%if(!readonly){%><input type="submit" value="Save"/><%}%>
    	</td>
    </tr>
</table> 
<script language='javascript'>
   Calendar.setup({inputField:"w_servicedate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlSDate",singleClick:true,step:1});
	//Calendar.setup({inputField:"w_servicedate",ifFormat:"y-m-d",button:"hlSDate",align:"Bl",singleClick:true});
	
	Calendar.setup({inputField:"w_estimatedate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlEDate",singleClick:true,step:1});
	//Calendar.setup({inputField:"w_estimatedate",ifFormat:"y-m-d",button:"hlEDate",align:"Tl",singleClick:true});
	
   Calendar.setup({inputField:"w_workdate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlWDate",singleClick:true,step:1});
   //Calendar.setup({inputField:"w_workdate",ifFormat:"y-m-d",button:"hlWDate",align:"Bl",singleClick:true});
   
	Calendar.setup({inputField:"w_doi",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlDDate",singleClick:true,step:1});
   //Calendar.setup({inputField:"w_doi",ifFormat:"y-m-d",button:"hlDDate",align:"Bl",singleClick:true});
</script>
</html:form>



<style type="text/css">
td.wcblayerTitle{
   font-family: Verdana, Arial, Helvetica, sans-serif;
  	font-size: xx-small;
  	font-weight:bold;
}

td.wcblayerItem{
   font-family: Verdana, Arial, Helvetica, sans-serif;
  	font-size: xx-small;
  	font-weight:bold;
  	color: #7A388D;
}
</style>

<div id="Layer1" style="position:absolute; left:1px; top:1px; width:350px; height:311px; visibility: hidden; z-index:1"   >
<!--  This should be changed to automagically fill if this changes often -->                             
<table width="98%" border="0" cellspacing="1" cellpadding="1" align=center>
    <tr class="LightBG"> 
      <td class="wcblayerTitle">Code</td>
      <td class="wcblayerTitle">&nbsp;</td>
      <td class="wcblayerTitle">Description</td>
      <td class="wcblayerTitle" align="right">
             <a href="javascript: function myFunction() {return false; }" onclick="hidepic('Layer1');" style="text-decoration: none;">X</a>           
      </td>
    </tr>
   
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
               <a href="#" onClick="quickPickWCBcode('19937');hidepic('Layer1');return false;">
                   19937             
               </a>
          </td>
          <td class="wcblayerItem">&nbsp;</td>
	       <td colspan="2" class="wcblayerItem" >
                    WCB FIRST TELEPLAN E-FORM REPORT OF INJURY
	       </td>
    </tr>                  
  
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
               <a href="#" onClick="quickPickWCBcode('19938');hidepic('Layer1');return false;">
                   19938             
               </a>
          </td>
          <td class="wcblayerItem">&nbsp;</td>
	       <td colspan="2" class="wcblayerItem" >
                    WCB TELEPLAN E-FORM REC'D WITHIN 6-7 DAYS
	       </td>
    </tr>                  
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
               <a href="#" onClick="quickPickWCBcode('19939');hidepic('Layer1');return false;">
                   19939             
               </a>
          </td>
          <td class="wcblayerItem">&nbsp;</td>
	       <td colspan="2" class="wcblayerItem" >
                    WCB TELEPLAN E-FORM 8 REC'D WITHIN 10 DAYS OF FAX
	       </td>
    </tr>                  
	                        
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
               <a href="#" onClick="quickPickWCBcode('19940');hidepic('Layer1');return false;">
                   19940             
               </a>
          </td>
          <td class="wcblayerItem">&nbsp;</td>
	       <td colspan="2" class="wcblayerItem" >
                    WCB TELEPLAN E-FORM 11 REC'D WITHIN 5 DAYS OF SERV
	       </td>
    </tr>                  
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
               <a href="#" onClick="quickPickWCBcode('19941');hidepic('Layer1');return false;">
                   19941             
               </a>
          </td>
          <td class="wcblayerItem">&nbsp;</td>
	       <td colspan="2" class="wcblayerItem" >
                    WCB TELEPLAN E-FORM 11 REC'D WITHIN 6-7 DAYS
	       </td>
    </tr>                  	                          
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
               <a href="#" onClick="quickPickWCBcode('19943');hidepic('Layer1');return false;">
                   19943             
               </a>
          </td>
          <td class="wcblayerItem">&nbsp;</td>
	       <td colspan="2" class="wcblayerItem" >
                    WCB E-FORM 8 RESUBMISSION, NO CHARGE
	       </td>
    </tr>                  	                          
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
               <a href="#" onClick="quickPickWCBcode('19944');hidepic('Layer1');return false;">
                   19944             
               </a>
          </td>
          <td class="wcblayerItem">&nbsp;</td>
	       <td colspan="2" class="wcblayerItem" >
                    WCB E-FORM 11 SUBMISSION
	       </td>
    </tr>                  	                                                                                        
  </table>
                                
</div>



<div id="claimLayer" style="position:absolute; left:1px; top:1px; width:350px; height:311px; visibility: hidden; z-index:1"   >
<!--  This should be changed to automagically fill if this changes often -->                             
<table width="98%" border="0" cellspacing="1" cellpadding="1" align=center>
    <tr class="LightBG"> 
      <td class="wcblayerTitle">Claim #</td>      
      <td class="wcblayerTitle" align="right">
             <a href="javascript: function myFunction() {return false; }" onclick="hidepic('claimLayer');" style="text-decoration: none;">X</a>           
      </td>
    </tr>
   
    <% for (int i = 0 ; i < claims.size(); i++){ 
      WcbHelper.WCBClaim claim = (WcbHelper.WCBClaim) claims.get(i);  
      WcbHelper.WCBEmployer emp = claim.wcbEmp;
    %>
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
               <a href="#" onClick="setClaim('<%=claim.getClaimNumber()%>','<%=emp.w_empname%>','<%=emp.w_opaddress%>','<%=emp.w_opcity%>','<%=emp.w_emparea%>','<%=emp.w_empphone%>');hidepic('claimLayer');return false;">
                   <%=claim.getClaimNumber()%>
               </a>
          </td>
          <td class="wcblayerItem">&nbsp;</td>
	       
    </tr>                                        	
    <%} %>                                                                                        
  </table>
                                
</div>


<div id="employerLayer" style="position:absolute; left:1px; top:1px; width:350px; height:311px; visibility: hidden; z-index:1"   >
<!--  This should be changed to automagically fill if this changes often -->                             
<table width="98%" border="0" cellspacing="1" cellpadding="1" align=center>
    <tr class="LightBG"> 
    
    

      <td class="wcblayerTitle">Employer's Name</td>      
      <td class="wcblayerTitle">Operating Address</td>      
      <td class="wcblayerTitle">Operating City</td>      
      <td class="wcblayerTitle">Employers Telephone No</td>      
      <td class="wcblayerTitle" align="right">
             <a href="javascript: function myFunction() {return false; }" onclick="hidepic('employerLayer');" style="text-decoration: none;">X</a>           
      </td>
    </tr>
   
    <% for (int i = 0 ; i < emps.size(); i++){ 
      WcbHelper.WCBEmployer emp = (WcbHelper.WCBEmployer) emps.get(i);  
    %>
    <tr class="LightBG"> 
          <td class="wcblayerTitle">
               <a href="#" onClick="setEmployer('<%=emp.w_empname%>','<%=emp.w_opaddress%>','<%=emp.w_opcity%>','<%=emp.w_emparea%>','<%=emp.w_empphone%>');hidepic('employerLayer');return false;">
                   <%=emp.w_empname%>
               </a>
          </td>          
          <td class="wcblayerTitle"><%=emp.w_opaddress%></td>      
          <td class="wcblayerTitle"><%=emp.w_opcity%></td>      
          <td class="wcblayerTitle"><%=emp.w_emparea%>-<%=emp.w_empphone%></td>      
	       <td colspan="2" class="wcblayerItem" >
                   &nbsp;
	       </td>
    </tr>                                        	
    <%}%>                                                                                        
  </table>
                                
</div>



</body>
</html:html>


<%!

String checked(String val,String str,boolean dfault){
    String retval = "";
    if(str == null || str.equals("null")){
        str = "";
    }
    if (str.equals("")  && dfault){
        retval = "CHECKED";
    }else if (str != null && str.equalsIgnoreCase(val) ){
        retval = "CHECKED";
    }
    return retval;
}

boolean isEmpty(ArrayList a){
   boolean isEmpty = false;
   if ( a.size() == 0 ) isEmpty = true;
   return isEmpty;
}
%>