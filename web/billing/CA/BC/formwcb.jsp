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
<%@ page import="oscar.form.*, java.util.*,oscar.oscarBilling.ca.bc.pageUtil.*,oscar.oscarDB.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<% 
    boolean readonly = false;
    String readOnlyParam = request.getParameter("readonly");
    if(readOnlyParam != null && readOnlyParam.equals("true")){
       readonly = true;   
    }
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
<% oscar.oscarBilling.ca.bc.pageUtil.WCBForm form = (oscar.oscarBilling.ca.bc.pageUtil.WCBForm) request.getSession().getAttribute("WCBForm"); %>
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
		<td colspan="2" align="center" valign="top" height="25" class="SmallerText">
			Physician's First Report <input name="w_reporttype" type="radio" id="w_reporttype" value="F" checked> or The worker's
				condition or treatment has changed: <input name="w_reporttype" type="radio" id="w_reporttype" value="C">
		</td>
	</tr>
	<tr>
		<td width="50%" align="left" valign="top">
			<table border="0" cellspacing="0" cellpading="0" width="100%">
				<tr>
					<td class="SmallerText">
					Employer's Name:</td><td><input name="w_empname" type="text" maxlength="25" id="w_empname" value="<%=form.getW_empname()%>"/>
					</td>
				</tr>
				<tr>
					<td class="SmallerText">
					Operating Address:</td><td><input name="w_opaddress" type="text" id="w_opaddress" maxlength="25" value="<%=form.getW_opaddress()%>" />
					</td>
				</tr>
				<tr>
					<td class="SmallerText">
					Operating City:</td><td><input name="w_opcity" type="text" id="w_opcity" maxlength="25" value="<%=form.getW_opcity()%>" />
					</td>
				</tr>
				<tr>
					<td class="SmallerText">
					Employers Telephone No:</td><td><input name="w_emparea" type="text" id="w_emparea" maxlength="3" size="3" value="<%=form.getW_emparea()%>" />-<input name="w_empphone" type="text" id="w_empphone" maxlength="7" size="10" value="<%=form.getW_empphone()%>"/></td>
					</td>
				</tr>
				<tr>
					<td class="SmallerText">
					Date of Injury:</td><td><input name="w_doi" type="text" id="w_doi"  value="<%=form.getW_doi()%>" readonly="true">
            			<a id="hlDDate"><img title="Calendar" src="../../../images/cal.gif" alt="Calendar" border="0" /></a>
					</td>
				</tr>
			</table>
		</td>
		<td width="50%" align="left" valign="top">
			<table cellspacing="0" cellpading="0" border="0" width="100%">
				<tr>
					<td colspan="4" class="SmallerText">
					WCB Claim Number: <input name="w_wcbno" type="text" maxlength="25" id="w_wcbno" value="<%=form.getW_wcbno()%>" />
					</td>
				</tr>
				<tr>
					<td colspan="4" class="SmallerText">Worker's <b>Last Name:</b> <input name="w_lname" type="text" maxlength="18" id="w_lname" size="20" value="<%=form.getW_lname()%>" />
					</td>
				</tr>
				<tr>
					<td class="SmallerText"><b>First Name:</b> <input name="w_fname" type="text" maxlength="12" id="w_fname" size="20" value="<%=form.getW_fname()%>" />
					</td>
					<td class="SmallerText">Initial: <input name="w_mname" type="text" maxlength="1" id="w_mname" size="1" value="<%=form.getW_mname()%>"/>
					</td>
					<td class="SmallerText">Gender: <input name="w_gender" type="text" maxlength="1" readonly id="w_gender" size="1" value="<%=form.getW_gender()%>" />
					</td>
					<td class="SmallerText">Date of Birth: <input name="w_dob" type="text" maxlength="10" readonly id="w_dob" size="10" value="<%=form.getW_dob()%>" />
					</td>
				</tr>
				<tr>
					<td colspan="4" class="SmallerText">
					Address: <input name="w_address" type="text" id="w_address" size="20" maxlength="25" value="<%=form.getW_address()%>" />
					City: <input name="w_city" type="text" id="w_city" size="20" maxlength="20" value="<%=form.getW_city()%>" />
					Postal Code: <input name="w_postal" type="text" id="w_postal" maxlength="6" size="6" value="<%=form.getW_postal()%>" />
					</td>
				</tr>
				<tr>
					<td colspan="4" class="SmallerText">Worker's Telephone No: <input name="w_area" type="text" maxlength="3" id="w_area" size="3" value="<%=form.getW_area()%>" />
            		<input name="w_phone" type="text" id="w_phone" size="7" maxlength="7" value="<%=form.getW_phone()%>"/>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="SmallerText">PHN: <input name="w_phn" type="text" id="w_phn" maxlength="12" size="12" value="<%=form.getW_phn()%>" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr class="LightBG">
		<td colspan="2">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>Are you the worker's regular physician? </td><td>
              <input type="radio" name="w_rphysician" value="Y" checked>
              Yes <input type="radio" name="w_rphysician" value="N"> No
		</td>
	</tr>
	<tr>
		<td>If yes, how long has the worker been your patient?</td><td><input type="radio" name="w_duration" value="1" checked>
              0-6 months <input type="radio" name="w_duration" value="2">
              7-12 months <input type="radio" name="w_duration" value="9">&gt; 12 months
		</td>
	</tr>
	<tr>
		<td>Who rendered the first treatment?</td><td><input name="w_ftreatment" type="text" id="w_ftreatment" maxlength="25" length="25" value="<%=form.getW_ftreatment()%>">
		</td>
	</tr>
	<tr>
		<td>
		Prior/Other Problems Affecting Injury, Recovery and Disability</td><td>
              <textarea name="w_problem" cols="50" id="w_problem" style="height:50px;width:100%;" ><%=form.getW_problem()%></textarea>
		</td>
	</tr>
	<tr class="LightBG">
		<td colspan="2">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="LightBG">
		Injury Codes and Descriptions</td>
          <td> Date of Service
            <input name="w_servicedate" size="10" type="text" id="w_servicedate"  value="<%=form.getW_servicedate()%>" readonly="true">
            <a id="hlSDate"><img title="Calendar" src="../../../images/cal.gif" alt="Calendar" border="0" /></a>
		</td>
	</tr>
	<tr>
		<td>
			Diagnosis:</td>
          <td><input name="w_diagnosis" type="text" maxlength="120" size="120" id="w_diagnosis" value="<%=form.getW_diagnosis()%>">
		</td>
	</tr>
	<tr>
		<td>WCB Fee Item:</td>
		<td>
			<input name="w_feeitem" type="text" maxlength="5" size="5" id="w_feeitem" value="<%=form.getW_feeitem()%>"> 
                	<a onClick="popup('400', '400', 'support/billingfeeitem.jsp?form=WCBForm&field=w_feeitem', 'w_feeitem');">Search</a>
		</td>
	</tr>
	<tr>
		<td>Extra Fee Item:</td>
		<td>
			<input name="w_extrafeeitem" type="text" maxlength="5" size="5" id="w_extrafeeitem" value="<%=form.getW_extrafeeitem()%>"> 
                	<a onClick="popup('400', '400', 'support/billingfeeitem.jsp?info=all&form=WCBForm&field=w_extrafeeitem', 'w_extrafeeitem');">Search</a>
		</td>
	</tr>
	<tr>
		<td>
			ICD9:</td>
          <td><input name="w_icd9" size="5" maxlength="5" type="text" id="w_icd9" value="<%=form.getW_icd9()%>">
                	<a onClick="popup('400', '400', 'support/icd9.jsp?form=WCBForm&field=w_icd9', 'w_bp');">Search</a>
		</td>
	</tr>
	<tr>
		<td>
			Body Part:</td>
          <td><input name="w_bp" type="text" size="5" maxlength="5" id="w_bp" value="<%=form.getW_bp()%>"> <a onClick="popup('400', '600', 'support/bodypart.jsp?form=WCBForm&field=w_bp', 'w_bp');">Search</a>
		</td>
	</tr>
	<tr>
		<td>
			Side:</td>
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
			Nature of Injury</td>
          <td><input name="w_noi" maxlength="5" size="5" type="text" id="w_noi" value="<%=form.getW_noi()%>"> 
                <a onClick="popup('400', '650', 'support/natureinjury.jsp?form=WCBForm&field=w_noi', 'w_noi');">Search</a>
		</td>
	</tr>
	<tr>
		<td>
			From injury or since last report, has the worker been disabled from work? </td><td>
              <input type="radio" name="w_work" value="Y" checked>Yes 
              <input type="radio" name="w_work" value="N">No</p>
		</td>
	</tr>
	<tr>
		<td>
		If Yes, as of what date? (if known) </td><td align="left" valign="top">
              <input  onClick="window.WCBForm.w_workdate.value = '';" name="w_workdate" type="text" id="w_workdate" readonly="true" value="<%=form.getW_workdate()%>"> 
              <a id="hlWDate"><img title="Calendar" src="../../../images/cal.gif" alt="Calendar" border="0" /></a>
		</td>
	</tr>
	<tr>
		<td td align="left" valign="top">
		Clinical Information:<br><small>What happened?<br>Subjective Symptoms<br>Examination<br>Investigations<br>Treatment, Meds</small>
		</td><td td align="left" valign="top">
		<textarea name="w_clinicinfo" class="mhAssTextarea" id="w_clinicinfo" style="height:80px;width:100%;" ><%=form.getW_clinicinfo()%></textarea> 
		</td>
	</tr>
	<tr>
		<td colspan="2" class="LightBG">
			Return to Work Planning
		</td>
	</tr>
	<tr>
		<td>
			Is the worker now medically capable of working full duties, full time?
		</td>
		<td>
		<input type="radio" name="w_capability" value="Y" checked> Yes <input type="radio" name="w_capability" value="N"> No
		</td>
	</tr>
	<tr>
		<td>
			If No: What are the current physical and/or psychological restrictions?
		</td>
		<td>
			<input name="w_capreason" type="text" id="w_capreason" size="50" value="<%=form.getW_capreason()%>">
		</td>
	</tr>
	<tr>
		<td valign="top">
		Estimated time before the worker will be able to return to the workplace. </td>
                <td>
					<input type="radio" name="w_estimate" value="0" checked>At Work<br> 
					<input type="radio" name="w_estimate" value="1">1-6 days 
					<input type="radio" name="w_estimate" value="2">7-13 days<br> 
					<input type="radio" name="w_estimate" value="3">14-20 days 
					<input type="radio" name="w_estimate" value="9">&gt;20 days
				</td>
	</tr>
	<tr>
		<td>
		If appropriate, is the worker now ready for a rehabilitation program?</td>
                <td><input type="radio" name="w_rehab" value="Y" > Yes <input type="radio" name="w_rehab" value="N" checked> No
		</td>
	</tr>
	<tr>
		<td>
		If Yes, Select &quot;WCP&quot; or Other</td><td><input type="radio" name="w_rehabtype" value="C">WCP
                  <input type="radio" name="w_rehabtype" value="O">Other
		</td>
	</tr>
	<tr>
		<td>
		Do you wish to consult with WCB physician or nurse advisor?</td>
                <td><input type="radio" name="w_wcbadvisor" value="Y" checked> Yes <input checked type="radio" name="w_wcbadvisor" value="N"> No
		</td>
	</tr>
	<tr>
		<td>
		If possible, please estimate date of Maximal Medical Recovery</td>
                <td><input onClick="window.WCBForm.w_estimatedate.value = '';" name="w_estimatedate" type="text" readonly id="w_estimatedate" value="<%=form.getW_estimatedate()%>"><a id="hlEDate">
                <img title="Calendar" src="../../../images/cal.gif" alt="Calendar" border="0" /></a>
		</td>
	</tr>
	<tr>
		<td>
		Further Correspondence to Follow: (2nd electronic form or paper) </td>
                <td><input type="radio" name="w_tofollow" value="Y"> Yes <input checked type="radio" name="w_tofollow" value="N">  No
		</td>
	</tr>
    <tr> 
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
</body>
</html:html>
