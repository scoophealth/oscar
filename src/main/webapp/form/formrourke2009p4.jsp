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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ page import="oscar.oscarEncounter.data.EctFormData" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
    String formClass = "Rourke2009";    
   		 
   	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    java.util.Properties props = (java.util.Properties)request.getAttribute("frmProperties"); //rec.getFormRecord(demoNo, formId);
    FrmRecord rec = (FrmRecord) request.getAttribute("frmRecord"); 
    String []growthCharts = new String[2];
    
    if( ((FrmRourke2009Record)rec).isFemale(loggedInInfo, demoNo) ) {
    	growthCharts[0] = new String("GrowthChartRourke2009Girls&__cfgfile=GrowthChartRourke2009Girls3&__cfgGraphicFile=GrowthChartRourke2009GirlGraphic&__cfgGraphicFile=GrowthChartRourke2009GirlGraphic2&__cfgGraphicFile1=GrowthChartRourke2009GirlGraphic5&__cfgGraphicFile1=GrowthChartRourke2009GirlGraphic6&__numPages=2&__graphType=LENGTH&__template=GrowthChartRourke2009Girls");
        growthCharts[1] = new String("GrowthChartRourke2009Girls2&__cfgGraphicFile=GrowthChartRourke2009GirlGraphic3&__cfgGraphicFile=GrowthChartRourke2009GirlGraphic4&__graphType=HEAD_CIRC&__template=GrowthChartRourke2009Girlspg2");
    }
    else {
        growthCharts[0] = new String("GrowthChartRourke2009Boys&__cfgfile=GrowthChartRourke2009Boys3&__cfgGraphicFile=GrowthChartRourke2009BoyGraphic&__cfgGraphicFile=GrowthChartRourke2009BoyGraphic2&__cfgGraphicFile1=GrowthChartRourke2009BoyGraphic5&__cfgGraphicFile1=GrowthChartRourke2009BoyGraphic6&__numPages=2&__graphType=LENGTH&__template=GrowthChartRourke2009Boys");
        growthCharts[1] = new String("GrowthChartRourke2009Boys2&__cfgGraphicFile=GrowthChartRourke2009BoyGraphic3&__cfgGraphicFile=GrowthChartRourke2009BoyGraphic4&__graphType=HEAD_CIRC&__template=GrowthChartRourke2009Boyspg2");
    }
    
    FrmData fd = new FrmData();
    String resource = fd.getResource();    
    
    //get project_home
    String project_home = request.getContextPath().substring(1); 
    
    String formTable = "formGrowth0_36";
    String formName = "Growth 0-36m";
    String growthChartURL = "";
    EctFormData.PatientForm[] pforms = EctFormData.getPatientFormsFromLocalAndRemote(loggedInInfo, String.valueOf(demoNo), formTable);
    if (pforms.length > 0) {
    	EctFormData.PatientForm pfrm = pforms[0];
    	growthChartURL = request.getContextPath() + "/form/forwardshortcutname.jsp?formname=" + formName + "&demographic_no=" + demoNo + (pfrm.getRemoteFacilityId()!=null?"&remoteFacilityId="+pfrm.getRemoteFacilityId()+"&formId="+pfrm.getFormId():"");
    }
    
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true;  
%>

<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
    <div style="display:block; width:100%; text-align:center; background-color: #FFFFFF;"><img alt="copyright" src="graphics/banner.png" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2009.formCopyRight" />')"
                                                   onMouseOut="hideLayer()">
    </div>
<div id="object1"
	style="position: absolute; background-color: FFFFDD; color: black; border-color: black; border-width: 20; left: 25px; top: -100px; z-index: +1"
	onmouseover="overdiv=1;"
	onmouseout="overdiv=0; setTimeout('hideLayer()',1000)">pop up
description layer</div>

	<table cellpadding="0" cellspacing="0" class="Header" class="hidePrint">
		<tr>
			<td nowrap="true"><input type="button"
				value="<bean:message key="oscarEncounter.formRourke1.btnSave"/>"
				onclick="javascript:onSave(); return false;" /> <input type="button"
				value="<bean:message key="oscarEncounter.formRourke1.btnSaveExit"/>"
				onclick="javascript:onSaveExit(); return false;" /> <input type="button"
				value="<bean:message key="oscarEncounter.formRourke1.btnExit"/>"
				onclick="javascript:onExit(); return false;"> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnPrint"/>"
				onclick="javascript:return onPrint();" /> <input type="button"
				value="About"
				onclick="javascript:return popPage('http://rourkebabyrecord.ca','About Rourke');" />
			</td>
			<td align="center" nowrap="true" width="100%">
			<% if( growthChartURL.length() > 0 ) {%>
				<a style="color:red; font-weight:bold; text-decoration:underline; cursor:pointer;" "href="#" onclick="popPage('<%=growthChartURL%>','growthChart')">Growth Chart Avail</a>
			
			<%} else { %>
				&nbsp; 
			<% }%>
			</td>
			<td align="center" nowrap="true" width="100%">
			<% if(formId > 0)
           { %> <a name="length" href="#"
				onclick="onGraph('<%=request.getContextPath()%>/form/formname.do?submit=graph&form_class=Rourke2009&__title=Baby+Growth+Graph1&__cfgfile=<%=growthCharts[0]%>&demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>','<%= "growth1" + demoNo %>');return false;">
			<bean:message key="oscarEncounter.formRourke1.btnGraphLenghtWeight" /></a><br>
			<a name="headCirc" href="#"
				onclick="onGraph('<%=request.getContextPath()%>/form/formname.do?submit=graph&form_class=Rourke2009&__title=Baby+Head+Circumference&__cfgfile=<%=growthCharts[1]%>&demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>','<%= "growth2" + demoNo %>');return false;">
			<bean:message key="oscarEncounter.formRourke1.btnGraphHead" /></a> <% }else { %>
			&nbsp; <% } %>
			</td>
			
		</tr>
	</table>

	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr class="titleBar">
			<th><bean:message
				key="oscarEncounter.formRourke2006_4.msgRourkeBabyRecord" /></th>
		</tr>
	</table>
	<div id="patientInfop4">
		<bean:message key="oscarEncounter.formRourke1.msgName" />: <input
				type="text" maxlength="60" size="30"
				value="<%= props.getProperty("c_pName", "") %>" readonly="true" />
			&nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke1.msgBirthDate" /> (d/m/yyyy): <input
				type="text" id="c_birthDate4" size="10" maxlength="10"
				value="<%= props.getProperty("c_birthDate", "") %>" readonly="true">
			&nbsp;&nbsp; 
			Age: <input type="text" id="currentAge4" size="10" maxlength="10" readonly="true" ondblclick="calcAge();">
				<% if(! ((FrmRourke2009Record)rec).isFemale(loggedInInfo, demoNo))
                {
                    %>(<bean:message
				key="oscarEncounter.formRourke1.msgMale" />)
				<%
                }else
                {
                    %>(<bean:message
				key="oscarEncounter.formRourke1.msgFemale" />)
				<%
                }
                %>                
				
	</div>
	<table cellpadding="0" cellspacing="0" width="100%" border="1">
		<tr align="center">			
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke2006_4.msg18mos" /></a></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke2006_4.msg2yrs" /></a></td>
			<td colspan="2" class="row"><a><bean:message
				key="oscarEncounter.formRourke2006_4.msg4yrs" /></a></td>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke2006_1.visitDate" /></a></td>
		</tr>
		<tr align="center">			
			<td colspan="3"><input readonly type="text" id="p4_date18m"
				name="p4_date18m" ondblclick="resetDate(this)" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p4_date18m", ""))%>" />
			<img src="../images/cal.gif" id="p4_date18m_cal"></td>
			<td colspan="3"><input readonly type="text" id="p4_date24m"
				name="p4_date24m" ondblclick="resetDate(this)" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p4_date24m", ""))%>" />
			<img src="../images/cal.gif" id="p4_date24m_cal"></td>
			<td colspan="2"><input readonly type="text" id="p4_date48m"
				name="p4_date48m" ondblclick="resetDate(this)" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p4_date48m", ""))%>" />
			<img src="../images/cal.gif" id="p4_date48m_cal"></td>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgDate" /></a></td>
		</tr>
		<tr align="center" id="growthAp4">			
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message
				key="oscarEncounter.formRourke2006_3.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message
				key="oscarEncounter.formRourke2006_3.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td class="column" rowspan="2"><a><bean:message
				key="oscarEncounter.formRourke1.btnGrowth" />*<br>
                        <bean:message key="oscarEncounter.formRourke2009_1.btnGrowthmsg"/>
            </td>
		</tr>
		<tr align="center" id="growthBp4">
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p4_ht18m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_ht18m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p4_wt18m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_wt18m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p4_hc18m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_hc18m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p4_ht24m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_ht24m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p4_wt24m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_wt24m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p4_hc24m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_hc24m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p4_ht48m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_ht48m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p4_wt48m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_wt48m", "") %>"></td>
		</tr>
		<tr align="center">
			<td colspan="3"><textarea id="p4_pConcern18m"
				name="p4_pConcern18m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_pConcern18m", "") %></textarea>
			</td>
			<td colspan="3"><textarea id="p4_pConcern24m"
				name="p4_pConcern24m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_pConcern24m", "") %></textarea>
			</td>
			<td colspan="2"><textarea id="p4_pConcern48m"
				name="p4_pConcern48m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_pConcern48m", "") %></textarea>
			</td>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.formParentalConcerns" /></a></td>
			
		</tr>
		<tr align="center" id="nutritionp4">
			<td colspan="3">			
			<table id="ntp41" cellpadding="0" cellspacing="0" width="100%">
				<tr>
                    <td style="padding-right: 5pt" valign="top"><img height="15"
                         width="20" src="graphics/Checkmark_L.gif"></td>
                    <td class="edcol" valign="top">X</td>
                 	<td class="edcol" valign="top"><bean:message key="oscarEncounter.formRourke2009.formNo" /></td>
                    <td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td> 
                 </tr>
                 <tr>
                 	<td valign="top"><input type="radio" id="p4_breastFeeding18mOk"
						name="p4_breastFeeding18mOk" onclick="onCheck(this,'p4_breastFeeding18m')"
						<%= props.getProperty("p4_breastFeeding18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_breastFeeding18mOkConcerns"
						name="p4_breastFeeding18mOkConcerns" onclick="onCheck(this,'p4_breastFeeding18m')"
						<%= props.getProperty("p4_breastFeeding18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_breastFeeding18mNo"
						name="p4_breastFeeding18mNo" onclick="onCheck(this,'p4_breastFeeding18m')"
						<%= props.getProperty("p4_breastFeeding18mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p4_breastFeeding18mNotDiscussed"
						name="p4_breastFeeding18mNotDiscussed" onclick="onCheck(this,'p4_breastFeeding18m')"
						<%= props.getProperty("p4_breastFeeding18mNotDiscussed", "") %>></td>
					<td><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.btnBreastFeeding" /></a></b></td>
				</tr>
				<tr>
                   	<td valign="top"><input type="radio" id="p4_homoMilk18mOk"
						name="p4_homoMilk18mOk" onclick="onCheck(this,'p4_homoMilk18m')"
						<%= props.getProperty("p4_homoMilk18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_homoMilk18mOkConcerns"
						name="p4_homoMilk18mOkConcerns" onclick="onCheck(this,'p4_homoMilk18m')"
						<%= props.getProperty("p4_homoMilk18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_homoMilk18mNo"
						name="p4_homoMilk18mNo" onclick="onCheck(this,'p4_homoMilk18m')"
						<%= props.getProperty("p4_homoMilk18mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p4_homoMilk18mNotDiscussed"
						name="p4_homoMilk18mNotDiscussed" onclick="onCheck(this,'p4_homoMilk18m')"
						<%= props.getProperty("p4_homoMilk18mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_4.formHomoMilk" /></td>
				</tr>
				<tr>
                	<td valign="top"><input type="radio" id="p4_bottle18mOk"
						name="p4_bottle18mOk" onclick="onCheck(this,'p4_bottle18m')"
						<%= props.getProperty("p4_bottle18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_bottle18mOkConcerns"
						name="p4_bottle18mOkConcerns" onclick="onCheck(this,'p4_bottle18m')"
						<%= props.getProperty("p4_bottle18mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p4_bottle18mNotDiscussed"
						name="p4_bottle18mNotDiscussed" onclick="onCheck(this,'p4_bottle18m')"
						<%= props.getProperty("p4_bottle18mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_4.formNoBottle" /></td>
				</tr>
                
                <tr>
                	<td colspan="5" style="vertical-align:bottom;">
                		<textarea id="p4_nutrition18m"
							name="p4_nutrition18m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_nutrition18m", "") %></textarea>
					</td>
				</tr>
			</table>
			</td>				
			<td colspan="3">			
			<table id="ntp42" cellpadding="0" cellspacing="0" width="100%">
				<tr>
                    <td style="padding-right: 5pt" valign="top"><img height="15"
                    	width="20" src="graphics/Checkmark_L.gif"></td>
                    <td class="edcol" valign="top">X</td>
                    <td class="edcol" valign="top"><bean:message key="oscarEncounter.formRourke2009.formNo" /></td>
                    <td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td> 
               	</tr>
                <tr>
                	<td valign="top"><input type="radio" id="p4_homo2percent24mOk"
						name="p4_homo2percent24mOk" onclick="onCheck(this,'p4_homo2percent24m')"
						<%= props.getProperty("p4_homo2percent24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_homo2percent24mOkConcerns"
						name="p4_homo2percent24mOkConcerns" onclick="onCheck(this,'p4_homo2percent24m')"
						<%= props.getProperty("p4_homo2percent24mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_homo2percent24mNo"
						name="p4_homo2percent24mNo" onclick="onCheck(this,'p4_homo2percent24m')"
						<%= props.getProperty("p4_homo2percent24mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p4_homo2percent24mNotDiscussed"
						name="p4_homo2percent24mNotDiscussed" onclick="onCheck(this,'p4_homo2percent24m')"
						<%= props.getProperty("p4_homo2percent24mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_4.One2percent" /></td>
				</tr>
				<tr>
                 	<td valign="top"><input type="radio" id="p4_lowerfatdiet24mOk"
						name="p4_lowerfatdiet24mOk" onclick="onCheck(this,'p4_lowerfatdiet24m')"
						<%= props.getProperty("p4_lowerfatdiet24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_lowerfatdiet24mOkConcerns"
						name="p4_lowerfatdiet24mOkConcerns" onclick="onCheck(this,'p4_lowerfatdiet24m')"
						<%= props.getProperty("p4_lowerfatdiet24mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p4_lowerfatdiet24mNotDiscussed"
						name="p4_lowerfatdiet24mNotDiscussed" onclick="onCheck(this,'p4_lowerfatdiet24m')"
						<%= props.getProperty("p4_lowerfatdiet24mNotDiscussed", "") %>></td>
					<td><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formLowerFatDiet" />*</a></i></td>
				</tr>
				<tr>
                   	<td valign="top"><input type="radio" id="p4_foodguide24mOk"
						name="p4_foodguide24mOk" onclick="onCheck(this,'p4_foodguide24m')"
						<%= props.getProperty("p4_foodguide24mOk", "") %>></td>
					<td>&nbsp;</td>										
					<td valign="top"><input type="radio" id="p4_foodguide24mNo"
						name="p4_foodguide24mNo" onclick="onCheck(this,'p4_foodguide24m')"
						<%= props.getProperty("p4_foodguide24mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p4_foodguide24mNotDiscussed"
						name="p4_foodguide24mNotDiscussed" onclick="onCheck(this,'p4_foodguide24m')"
						<%= props.getProperty("p4_foodguide24mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_4.formFoodGuide" /></a></td>
				</tr>
                
                <tr>
                	<td colspan="5" style="vertical-align:bottom;">
                		<textarea id="p4_nutrition24m"
							name="p4_nutrition24m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_nutrition24m", "") %></textarea>
					</td>
				</tr>
			</table>
			</td>
			<td colspan="2">			
			<table id="ntp43" cellpadding="0" cellspacing="0" width="100%">
				<tr>
                	<td style="padding-right: 5pt" valign="top"><img height="15"
                    	width="20" src="graphics/Checkmark_L.gif"></td>
                    <td class="edcol" valign="top">X</td>
                    <td class="edcol" valign="top"><bean:message key="oscarEncounter.formRourke2009.formNo" /></td>
                    <td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td> 
              	</tr>
                <tr>
                 	<td valign="top"><input type="radio" id="p4_2pMilk48mOk"
						name="p4_2pMilk48mOk" onclick="onCheck(this,'p4_2pMilk48m')"
						<%= props.getProperty("p4_2pMilk48mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_2pMilk48mOkConcerns"
						name="p4_2pMilk48mOkConcerns" onclick="onCheck(this,'p4_2pMilk48m')"
						<%= props.getProperty("p4_2pMilk48mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_2pMilk48mNo"
						name="p4_2pMilk48mNo" onclick="onCheck(this,'p4_2pMilk48m')"
						<%= props.getProperty("p4_2pMilk48mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p4_2pMilk48mNotDiscussed"
						name="p4_2pMilk48mNotDiscussed" onclick="onCheck(this,'p4_2pMilk48m')"
						<%= props.getProperty("p4_2pMilk48mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_4.One2percent" /></td>
				</tr>
				<tr>
        			<td valign="top"><input type="radio" id="p4_foodguide48mOk"
						name="p4_foodguide48mOk" onclick="onCheck(this,'p4_foodguide48m')"
						<%= props.getProperty("p4_foodguide48mOk", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p4_foodguide48mNo"
						name="p4_foodguide48mNo" onclick="onCheck(this,'p4_foodguide48m')"
						<%= props.getProperty("p4_foodguide48mNo", "") %>></td>				
					<td valign="top"><input type="radio" id="p4_foodguide48mNotDiscussed"
						name="p4_foodguide48mNotDiscussed" onclick="onCheck(this,'p4_foodguide48m')"
						<%= props.getProperty("p4_foodguide48mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_4.formFoodGuide" /></a></td>
				</tr>
                
		       	<tr>
		       		<td colspan="5" style="vertical-align:bottom;">
		       			<textarea id="p4_nutrition48m"
							name="p4_nutrition48m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_nutrition48m", "") %></textarea>
					</td>
				</tr>
			</table>
			</td>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgNutrition" />*</a></td>		
		</tr>
		<tr id="educationp4">			
			<td colspan="3">
			<table id="edt41" style="font-size: 8pt;" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="4"><bean:message
						key="oscarEncounter.formRourke2006_1.formInjuryPrev" /></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_carSeat18mOk"
						name="p4_carSeat18mOk" onclick="onCheck(this,'p4_carSeat18m')"
						<%= props.getProperty("p4_carSeat18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_carSeat18mOkConcerns"
						name="p4_carSeat18mOkConcerns" onclick="onCheck(this,'p4_carSeat18m')"
						<%= props.getProperty("p4_carSeat18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_carSeat18mNotDiscussed"
						name="p4_carSeat18mNotDiscussed" onclick="onCheck(this,'p4_carSeat18m')"
						<%= props.getProperty("p4_carSeat18mNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formCarSeatChild" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_bathSafetyOk"
						name="p4_bathSafetyOk" onclick="onCheck(this,'p4_bathSafety')"
						<%= props.getProperty("p4_bathSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_bathSafetyOkConcerns"
						name="p4_bathSafetyOkConcerns" onclick="onCheck(this,'p4_bathSafety')"
						<%= props.getProperty("p4_bathSafetyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_bathSafetyNotDiscussed"
						name="p4_bathSafetyNotDiscussed" onclick="onCheck(this,'p4_bathSafety')"
						<%= props.getProperty("p4_bathSafetyNotDiscussed", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formBathSafety" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_safeToysOk"
						name="p4_safeToysOk" onclick="onCheck(this,'p4_safeToys')"
						<%= props.getProperty("p4_safeToysOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_safeToysOkConcerns"
						name="p4_safeToysOkConcerns" onclick="onCheck(this,'p4_safeToys')"
						<%= props.getProperty("p4_safeToysOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_safeToysNotDiscussed"
						name="p4_safeToysNotDiscussed" onclick="onCheck(this,'p4_safeToys')"
						<%= props.getProperty("p4_safeToysNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSafeToys" />*</a></td>
				</tr>
				
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="4"><bean:message
						key="oscarEncounter.formRourke2006_4.formBehaviour" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_parentChild18mOk"
						name="p4_parentChild18mOk"
						onclick="onCheck(this,'p4_parentChild18m')"
						<%= props.getProperty("p4_parentChild18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_parentChild18mOkConcerns"
						name="p4_parentChild18mOkConcerns"
						onclick="onCheck(this,'p4_parentChild18m')"
						<%= props.getProperty("p4_parentChild18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_parentChild18mNotDiscussed"
						name="p4_parentChild18mNotDiscussed"
						onclick="onCheck(this,'p4_parentChild18m')"
						<%= props.getProperty("p4_parentChild18mNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formParentChild" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_discipline18mOk"
						name="p4_discipline18mOk"
						onclick="onCheck(this,'p4_discipline18m')"
						<%= props.getProperty("p4_discipline18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_discipline18mOkConcerns"
						name="p4_discipline18mOkConcerns"
						onclick="onCheck(this,'p4_discipline18m')"
						<%= props.getProperty("p4_discipline18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_discipline18mNotDiscussed"
						name="p4_discipline18mNotDiscussed"
						onclick="onCheck(this,'p4_discipline18m')"
						<%= props.getProperty("p4_discipline18mNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_4.formDiscipline" />**</a></td>
				</tr>
				
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="4"><bean:message
						key="oscarEncounter.formRourke2006_4.formFamily" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_pFatigue18mOk"
						name="p4_pFatigue18mOk" onclick="onCheck(this,'p4_pFatigue18m')"
						<%= props.getProperty("p4_pFatigue18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_pFatigue18mOkConcerns"
						name="p4_pFatigue18mOkConcerns" onclick="onCheck(this,'p4_pFatigue18m')"
						<%= props.getProperty("p4_pFatigue18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_pFatigue18mNotDiscussed"
						name="p4_pFatigue18mNotDiscussed" onclick="onCheck(this,'p4_pFatigue18m')"
						<%= props.getProperty("p4_pFatigue18mNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formParentFatigue" />**</a></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_highRisk18mOk"
						name="p4_highRisk18mOk" onclick="onCheck(this,'p4_highRisk18m')"
						<%= props.getProperty("p4_highRisk18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_highRisk18mOkConcerns"
						name="p4_highRisk18mOkConcerns" onclick="onCheck(this,'p4_highRisk18m')"
						<%= props.getProperty("p4_highRisk18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_highRisk18mNotDiscussed"
						name="p4_highRisk18mNotDiscussed" onclick="onCheck(this,'p4_highRisk18m')"
						<%= props.getProperty("p4_highRisk18mNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formHighRisk" />**</a></td>
				</tr>
				
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="4" valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formOther" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_socializing18mOk"
						name="p4_socializing18mOk"
						onclick="onCheck(this,'p4_socializing18m')"
						<%= props.getProperty("p4_socializing18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_socializing18mOkConcerns"
						name="p4_socializing18mOkConcerns"
						onclick="onCheck(this,'p4_socializing18m')"
						<%= props.getProperty("p4_socializing18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_socializing18mNotDiscussed"
						name="p4_socializing18mNotDiscussed"
						onclick="onCheck(this,'p4_socializing18m')"
						<%= props.getProperty("p4_socializing18mNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formSocPeerPlay" /></td>
				</tr>
              	<tr>
					<td valign="top"><input type="radio" id="p4_weanPacifier18mOk"
						name="p4_weanPacifier18mOk" onclick="onCheck(this,'p4_weanPacifier18m')"
						<%= props.getProperty("p4_weanPacifier18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_weanPacifier18mOkConcerns"
						name="p4_weanPacifier18mOkConcerns" onclick="onCheck(this,'p4_weanPacifier18m')"
						<%= props.getProperty("p4_weanPacifier18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_weanPacifier18mNotDiscussed"
						name="p4_weanPacifier18mNotDiscussed" onclick="onCheck(this,'p4_weanPacifier18m')"
						<%= props.getProperty("p4_weanPacifier18mNotDiscussed", "") %>></td>
					<td valign="top"><b><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_4.formWeanPacifier" />*</a></i></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_dentalCareOk"
						name="p4_dentalCareOk" onclick="onCheck(this,'p4_dentalCare')"
						<%= props.getProperty("p4_dentalCareOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_dentalCareOkConcerns"
						name="p4_dentalCareOkConcerns" onclick="onCheck(this,'p4_dentalCare')"
						<%= props.getProperty("p4_dentalCareOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_dentalCareNotDiscussed"
						name="p4_dentalCareNotDiscussed" onclick="onCheck(this,'p4_dentalCare')"
						<%= props.getProperty("p4_dentalCareNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_4.formDentalCleaning" />*</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p4_toiletLearning18mOk" name="p4_toiletLearning18mOk"
						onclick="onCheck(this,'p4_toiletLearning18m')"
						<%= props.getProperty("p4_toiletLearning18mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p4_toiletLearning18mOkConcerns" name="p4_toiletLearning18mOkConcerns"
						onclick="onCheck(this,'p4_toiletLearning18m')"
						<%= props.getProperty("p4_toiletLearning18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p4_toiletLearning18mNotDiscussed" name="p4_toiletLearning18mNotDiscussed"
						onclick="onCheck(this,'p4_toiletLearning18m')"
						<%= props.getProperty("p4_toiletLearning18mNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formToiletLearning" />**</a></td>
				</tr>
                <tr>
					<td valign="top"><input type="radio" id="p4_encourageReading18mOk"
						name="p4_encourageReading18mOk" onclick="onCheck(this,'p4_encourageReading18m')"
						<%= props.getProperty("p4_encourageReading18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_encourageReading18mOkConcerns"
						name="p4_encourageReading18mOkConcerns" onclick="onCheck(this,'p4_encourageReading18m')"
						<%= props.getProperty("p4_encourageReading18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_encourageReading18mNotDiscussed"
						name="p4_encourageReading18mNotDiscussed" onclick="onCheck(this,'p4_encourageReading18m')"
						<%= props.getProperty("p4_encourageReading18mNotDiscussed", "") %>></td>
					<td valign="top"><b><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_2.formReading" />**</a></i></b></td>
				</tr>
				
				<tr>
					<td colspan="4" style="vertical-align:bottom">
						<textarea id="p4_education18m"
							name="p4_education18m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_education18m", "") %></textarea>
					</td>
				</tr>	
			</table>			
			</td>
			<td colspan="5">
			<table id="edt42" style="font-size: 8pt;" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td colspan="12">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="12">&nbsp;</td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_carSeat24mOk"
						name="p4_carSeat24mOk" onclick="onCheck(this,'p4_carSeat24m')"
						<%= props.getProperty("p4_carSeat24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_carSeat24mOkConcerns"
						name="p4_carSeat24mOkConcerns" onclick="onCheck(this,'p4_carSeat24m')"
						<%= props.getProperty("p4_carSeat24mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_carSeat24mNotDiscussed"
						name="p4_carSeat24mNotDiscussed" onclick="onCheck(this,'p4_carSeat24m')"
						<%= props.getProperty("p4_carSeat24mNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formCarSeatChildBooster" /></a>*</b></td>
					<td valign="top"><input type="radio" id="p4_bikeHelmetsOk"
						name="p4_bikeHelmetsOk" onclick="onCheck(this,'p4_bikeHelmets')"
						<%= props.getProperty("p4_bikeHelmetsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_bikeHelmetsOkConcerns"
						name="p4_bikeHelmetsOkConcerns" onclick="onCheck(this,'p4_bikeHelmets')"
						<%= props.getProperty("p4_bikeHelmetsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_bikeHelmetsNotDiscussed"
						name="p4_bikeHelmetsNotDiscussed" onclick="onCheck(this,'p4_bikeHelmets')"
						<%= props.getProperty("p4_bikeHelmetsNotDiscussed", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formBikeHelmet" />*</a></i></td>
					<td valign="top"><input type="radio" id="p4_firearmSafetyOk"
						name="p4_firearmSafetyOk"
						onclick="onCheck(this,'p4_firearmSafety')"
						<%= props.getProperty("p4_firearmSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_firearmSafetyOkConcerns"
						name="p4_firearmSafetyOkConcerns"
						onclick="onCheck(this,'p4_firearmSafety')"
						<%= props.getProperty("p4_firearmSafetyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_firearmSafetyNotDiscussed"
						name="p4_firearmSafetyNotDiscussed"
						onclick="onCheck(this,'p4_firearmSafety')"
						<%= props.getProperty("p4_firearmSafetyNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formFireArm" />*</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_smokeSafetyOk"
						name="p4_smokeSafetyOk" onclick="onCheck(this,'p4_smokeSafety')"
						<%= props.getProperty("p4_smokeSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_smokeSafetyOkConcerns"
						name="p4_smokeSafetyOkConcerns" onclick="onCheck(this,'p4_smokeSafety')"
						<%= props.getProperty("p4_smokeSafetyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_smokeSafetyNotDiscussed"
						name="p4_smokeSafetyNotDiscussed" onclick="onCheck(this,'p4_smokeSafety')"
						<%= props.getProperty("p4_smokeSafetyNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSmokeSafety" />*</a></td>
					<td valign="top"><input type="radio" id="p4_matchesOk"
						name="p4_matchesOk" onclick="onCheck(this,'p4_matches')"
						<%= props.getProperty("p4_matchesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_matchesOkConcerns"
						name="p4_matchesOkConcerns" onclick="onCheck(this,'p4_matches')"
						<%= props.getProperty("p4_matchesOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_matchesNotDiscussed"
						name="p4_matchesNotDiscussed" onclick="onCheck(this,'p4_matches')"
						<%= props.getProperty("p4_matchesNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formMatches" /></td>
					<td valign="top"><input type="radio" id="p4_waterSafetyOk"
						name="p4_waterSafetyOk" onclick="onCheck(this,'p4_waterSafety')"
						<%= props.getProperty("p4_waterSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_waterSafetyOkConcerns"
						name="p4_waterSafetyOkConcerns" onclick="onCheck(this,'p4_waterSafety')"
						<%= props.getProperty("p4_waterSafetyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_waterSafetyNotDiscussed"
						name="p4_waterSafetyNotDiscussed" onclick="onCheck(this,'p4_waterSafety')"
						<%= props.getProperty("p4_waterSafetyNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formWaterSafety" /></td>
				</tr>
				
				<tr>
					<td colspan="12">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_parentChild24mOk"
						name="p4_parentChild24mOk"
						onclick="onCheck(this,'p4_parentChild24m')"
						<%= props.getProperty("p4_parentChild24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_parentChild24mOkConcerns"
						name="p4_parentChild24mOkConcerns"
						onclick="onCheck(this,'p4_parentChild24m')"
						<%= props.getProperty("p4_parentChild24mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_parentChild24mNotDiscussed"
						name="p4_parentChild24mNotDiscussed"
						onclick="onCheck(this,'p4_parentChild24m')"
						<%= props.getProperty("p4_parentChild24mNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formParentChild" /></td>
					<td valign="top"><input type="radio" id="p4_discipline24mOk"
						name="p4_discipline24mOk"
						onclick="onCheck(this,'p4_discipline24m')"
						<%= props.getProperty("p4_discipline24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_discipline24mOkConcerns"
						name="p4_discipline24mOkConcerns"
						onclick="onCheck(this,'p4_discipline24m')"
						<%= props.getProperty("p4_discipline24mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_discipline24mNotDiscussed"
						name="p4_discipline24mNotDiscussed"
						onclick="onCheck(this,'p4_discipline24m')"
						<%= props.getProperty("p4_discipline24mNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_4.formDiscipline" />**</a></td>
					<td valign="top"><input type="radio" id="p4_highRisk24mOk"
						name="p4_highRisk24mOk" onclick="onCheck(this,'p4_highRisk24m')"
						<%= props.getProperty("p4_highRisk24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_highRisk24mOkConcerns"
						name="p4_highRisk24mOkConcerns" onclick="onCheck(this,'p4_highRisk24m')"
						<%= props.getProperty("p4_highRisk24mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_highRisk24mNotDiscussed"
						name="p4_highRisk24mNotDiscussed" onclick="onCheck(this,'p4_highRisk24m')"
						<%= props.getProperty("p4_highRisk24mNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formHighRisk" />**</a></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_pFatigue24mOk"
						name="p4_pFatigue24mOk" onclick="onCheck(this,'p4_pFatigue24m')"
						<%= props.getProperty("p4_pFatigue24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_pFatigue24mOkConcerns"
						name="p4_pFatigue24mOkConcerns" onclick="onCheck(this,'p4_pFatigue24m')"
						<%= props.getProperty("p4_pFatigue24mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_pFatigue24mNotDiscussed"
						name="p4_pFatigue24mNotDiscussed" onclick="onCheck(this,'p4_pFatigue24m')"
						<%= props.getProperty("p4_pFatigue24mNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formParentFatigue" />**</a></td>
					<td valign="top"><input type="radio" id="p4_famConflictOk"
						name="p4_famConflictOk" onclick="onCheck(this,'p4_famConflict')"
						<%= props.getProperty("p4_famConflictOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_famConflictOkConcerns"
						name="p4_famConflictOkConcerns" onclick="onCheck(this,'p4_famConflict')"
						<%= props.getProperty("p4_famConflictOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_famConflictNotDiscussed"
						name="p4_famConflictNotDiscussed" onclick="onCheck(this,'p4_famConflict')"
						<%= props.getProperty("p4_famConflictNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formFamConflict" /></td>
					<td valign="top"><input type="radio" id="p4_siblingsOk"
						name="p4_siblingsOk" onclick="onCheck(this,'p4_siblings')"
						<%= props.getProperty("p4_siblingsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_siblingsOkConcerns"
						name="p4_siblingsOkConcerns" onclick="onCheck(this,'p4_siblings')"
						<%= props.getProperty("p4_siblingsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_siblingsNotDiscussed"
						name="p4_siblingsNotDiscussed" onclick="onCheck(this,'p4_siblings')"
						<%= props.getProperty("p4_siblingsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formSiblings" /></td>
				</tr>
				
				<tr>
					<td colspan="12">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_2ndSmokeOk"
						name="p4_2ndSmokeOk" onclick="onCheck(this,'p4_2ndSmoke')"
						<%= props.getProperty("p4_2ndSmokeOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_2ndSmokeOkConcerns"
						name="p4_2ndSmokeOkConcerns" onclick="onCheck(this,'p4_2ndSmoke')"
						<%= props.getProperty("p4_2ndSmokeOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_2ndSmokeNotDiscussed"
						name="p4_2ndSmokeNotDiscussed" onclick="onCheck(this,'p4_2ndSmoke')"
						<%= props.getProperty("p4_2ndSmokeNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formSecondHandSmoke" />*</a></b></td>
					<td valign="top"><input type="radio" id="p4_dentalCleaningOk"
						name="p4_dentalCleaningOk"
						onclick="onCheck(this,'p4_dentalCleaning')"
						<%= props.getProperty("p4_dentalCleaningOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_dentalCleaningOkConcerns"
						name="p4_dentalCleaningOkConcerns"
						onclick="onCheck(this,'p4_dentalCleaning')"
						<%= props.getProperty("p4_dentalCleaningOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_dentalCleaningNotDiscussed"
						name="p4_dentalCleaningNotDiscussed"
						onclick="onCheck(this,'p4_dentalCleaning')"
						<%= props.getProperty("p4_dentalCleaningNotDiscussed", "") %>></td>
					<td valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formDentalCleaning" />*</a></b></td>
                    <td valign="top"><input type="radio" id="p4_noPacifier24mOk"
						name="p4_noPacifier24mOk"
						onclick="onCheck(this,'p4_noPacifier24m')"
						<%= props.getProperty("p4_noPacifier24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_noPacifier24mOkConcerns"
						name="p4_noPacifier24mOkConcerns"
						onclick="onCheck(this,'p4_noPacifier24m')"
						<%= props.getProperty("p4_noPacifier24mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_noPacifier24mNotDiscussed"
						name="p4_noPacifier24mNotDiscussed"
						onclick="onCheck(this,'p4_noPacifier24m')"
						<%= props.getProperty("p4_noPacifier24mNotDiscussed", "") %>></td>
					<td valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_4.formnoPacifier" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_altMedOk"
						name="p4_altMedOk" onclick="onCheck(this,'p4_altMed')"
						<%= props.getProperty("p4_altMedOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_altMedOkConcerns"
						name="p4_altMedOkConcerns" onclick="onCheck(this,'p4_altMed')"
						<%= props.getProperty("p4_altMedOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_altMedNotDiscussed"
						name="p4_altMedNotDiscussed" onclick="onCheck(this,'p4_altMed')"
						<%= props.getProperty("p4_altMedNotDiscussed", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formAltMed" />*</a></i></td>
					<td valign="top"><input type="radio"
						id="p4_toiletLearning24mOk" name="p4_toiletLearning24mOk"
						onclick="onCheck(this,'p4_toiletLearning24m')"
						<%= props.getProperty("p4_toiletLearning24mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p4_toiletLearning24mOkConcerns" name="p4_toiletLearning24mOkConcerns"
						onclick="onCheck(this,'p4_toiletLearning24m')"
						<%= props.getProperty("p4_toiletLearning24mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p4_toiletLearning24mNotDiscussed" name="p4_toiletLearning24mNotDiscussed"
						onclick="onCheck(this,'p4_toiletLearning24m')"
						<%= props.getProperty("p4_toiletLearning24mNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formToiletLearning" />**</a></td>
                   <td valign="top"><input type="radio"
						id="p4_noCough24mOk" name="p4_noCough24mOk"
						onclick="onCheck(this,'p4_noCough24m')"
						<%= props.getProperty("p4_noCough24mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p4_noCough24mOkConcerns" name="p4_noCough24mOkConcerns"
						onclick="onCheck(this,'p4_noCough24m')"
						<%= props.getProperty("p4_noCough24mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p4_noCough24mNotDiscussed" name="p4_noCough24mNotDiscussed"
						onclick="onCheck(this,'p4_noCough24m')"
						<%= props.getProperty("p4_noCough24mNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_1.formCough" />*</a></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_activeOk"
						name="p4_activeOk" onclick="onCheck(this,'p4_active')"
						<%= props.getProperty("p4_activeOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_activeOkConcerns"
						name="p4_activeOkConcerns" onclick="onCheck(this,'p4_active')"
						<%= props.getProperty("p4_activeOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_activeNotDiscussed"
						name="p4_activeNotDiscussed" onclick="onCheck(this,'p4_active')"
						<%= props.getProperty("p4_activeNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_3.formactiveLife" />*</a></td>
					<td valign="top"><input type="radio" id="p4_socializing24mOk"
						name="p4_socializing24mOk"
						onclick="onCheck(this,'p4_socializing24m')"
						<%= props.getProperty("p4_socializing24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_socializing24mOkConcerns"
						name="p4_socializing24mOkConcerns"
						onclick="onCheck(this,'p4_socializing24m')"
						<%= props.getProperty("p4_socializing24mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_socializing24mNotDiscussed"
						name="p4_socializing24mNotDiscussed"
						onclick="onCheck(this,'p4_socializing24m')"
						<%= props.getProperty("p4_socializing24mNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formSocializing" /></td>
					<td valign="top"><input type="radio" id="p4_readingOk"
						name="p4_readingOk" onclick="onCheck(this,'p4_reading')"
						<%= props.getProperty("p4_readingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_readingOkConcerns"
						name="p4_readingOkConcerns" onclick="onCheck(this,'p4_reading')"
						<%= props.getProperty("p4_readingOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_readingNotDiscussed"
						name="p4_readingNotDiscussed" onclick="onCheck(this,'p4_reading')"
						<%= props.getProperty("p4_readingNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formEncourageReading" />**</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_dayCareOk"
						name="p4_dayCareOk" onclick="onCheck(this,'p4_dayCare')"
						<%= props.getProperty("p4_dayCareOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_dayCareOkConcerns"
						name="p4_dayCareOkConcerns" onclick="onCheck(this,'p4_dayCare')"
						<%= props.getProperty("p4_dayCareOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_dayCareNotDiscussed"
						name="p4_dayCareNotDiscussed" onclick="onCheck(this,'p4_dayCare')"
						<%= props.getProperty("p4_dayCareNotDiscussed", "") %>></td>
					<td colspan="9" valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formDayCare" />**</a></b></td>
				</tr>
				
				<tr>
					<td colspan="12">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="12" valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formEnvHealth" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_sunExposureOk"
						name="p4_sunExposureOk" onclick="onCheck(this,'p4_sunExposure')"
						<%= props.getProperty("p4_sunExposureOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_sunExposureOkConcerns"
						name="p4_sunExposureOkConcerns" onclick="onCheck(this,'p4_sunExposure')"
						<%= props.getProperty("p4_sunExposureOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_sunExposureNotDiscussed"
						name="p4_sunExposureNotDiscussed" onclick="onCheck(this,'p4_sunExposure')"
						<%= props.getProperty("p4_sunExposureNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSunExposure" />*</a></td>
					<td valign="top"><input type="radio" id="p4_pesticidesOk"
						name="p4_pesticidesOk" onclick="onCheck(this,'p4_pesticides')"
						<%= props.getProperty("p4_pesticidesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_pesticidesOkConcerns"
						name="p4_pesticidesOkConcerns" onclick="onCheck(this,'p4_pesticides')"
						<%= props.getProperty("p4_pesticidesOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_pesticidesNotDiscussed"
						name="p4_pesticidesNotDiscussed" onclick="onCheck(this,'p4_pesticides')"
						<%= props.getProperty("p4_pesticidesNotDiscussed", "") %>></td>
					<td colspan="5" valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formPesticides" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_checkSerumOk"
						name="p4_checkSerumOk" onclick="onCheck(this,'p4_checkSerum')"
						<%= props.getProperty("p4_checkSerumOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_checkSerumOkConcerns"
						name="p4_checkSerumOkConcerns" onclick="onCheck(this,'p4_checkSerum')"
						<%= props.getProperty("p4_checkSerumOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_checkSerumNotDiscussed"
						name="p4_checkSerumNotDiscussed" onclick="onCheck(this,'p4_checkSerum')"
						<%= props.getProperty("p4_checkSerumNotDiscussed", "") %>></td>
					<td colspan="9" valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_4.formCheckSerum" />*</a></i></td>
				</tr>
				
				<tr>
					<td colspan="12" style="vertical-align:bottom">
						<textarea id="p4_education48m"
							name="p4_education48m" class="wide" rows="5"><%= props.getProperty("p4_education48m", "") %></textarea>
					</td>
				</tr>
			</table>			
			</td>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgEducational" /></a><br />
			<br />
			<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif"><bean:message
				key="oscarEncounter.formRourke2006.msgEducationalLegend" /></td>
		</tr>
		<tr id="developmentp4">						
			<td colspan="3" align="center">
			<table id="dt41" cellpadding="0" cellspacing="0" width="300px" height="100%">
				<tr>
					<td colspan="4">
						<bean:message key="oscarEncounter.formRourke2009_4.msgNippissing" />				
						<textarea id="p4_nippisingattained"
							name="p4_nippisingattained" class="wide" cols="10" rows="2"><%= props.getProperty("p4_nippisingattained", "") %></textarea>
					</td>
				</tr>
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>				
				<tr>
					<td colspan="4"><bean:message
						key="oscarEncounter.formRourke2006_4.formSocialEmotion" /></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_manageableOk"
						name="p4_manageableOk" onclick="onCheck(this,'p4_manageable')"
						<%= props.getProperty("p4_manageableOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_manageableOkConcerns"
						name="p4_manageableOkConcerns" onclick="onCheck(this,'p4_manageable')"
						<%= props.getProperty("p4_manageableOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_manageableNotDiscussed"
						name="p4_manageableNotDiscussed" onclick="onCheck(this,'p4_manageable')"
						<%= props.getProperty("p4_manageableNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formManageable" /></td>
				</tr>
                <tr>
					<td valign="top"><input type="radio" id="p4_otherChildrenOk"
						name="p4_otherChildrenOk" onclick="onCheck(this,'p4_otherChildren')"
						<%= props.getProperty("p4_otherChildrenOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_otherChildrenOkConcerns"
						name="p4_otherChildrenOkConcerns" onclick="onCheck(this,'p4_otherChildren')"
						<%= props.getProperty("p4_otherChildrenOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_otherChildrenNotDiscussed"
						name="p4_otherChildrenNotDiscussed" onclick="onCheck(this,'p4_otherChildren')"
						<%= props.getProperty("p4_otherChildrenNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_4.formOtherChildren" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_soothabilityOk"
						name="p4_soothabilityOk" onclick="onCheck(this,'p4_soothability')"
						<%= props.getProperty("p4_soothabilityOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_soothabilityOkConcerns"
						name="p4_soothabilityOkConcerns" onclick="onCheck(this,'p4_soothability')"
						<%= props.getProperty("p4_soothabilityOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_soothabilityNotDiscussed"
						name="p4_soothabilityNotDiscussed" onclick="onCheck(this,'p4_soothability')"
						<%= props.getProperty("p4_soothabilityNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formSoothability" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_comfortOk"
						name="p4_comfortOk" onclick="onCheck(this,'p4_comfort')"
						<%= props.getProperty("p4_comfortOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_comfortOkConcerns"
						name="p4_comfortOkConcerns" onclick="onCheck(this,'p4_comfort')"
						<%= props.getProperty("p4_comfortOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_comfortNotDiscussed"
						name="p4_comfortNotDiscussed" onclick="onCheck(this,'p4_comfort')"
						<%= props.getProperty("p4_comfortNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formComfort" /></td>
				</tr>
				
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="4"><bean:message
						key="oscarEncounter.formRourke2006_4.formCommSkills" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_points2bodyOk"
						name="p4_pointsOk" onclick="onCheck(this,'p4_points2body')"
						<%= props.getProperty("p4_pointsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_points2bodyOkConcerns"
						name="p4_pointsOkConcerns" onclick="onCheck(this,'p4_points2body')"
						<%= props.getProperty("p4_pointsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_points2bodyNotDiscussed"
						name="p4_pointsNotDiscussed" onclick="onCheck(this,'p4_points2body')"
						<%= props.getProperty("p4_pointsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_4.formPoints" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_getAttnOk"
						name="p4_getAttnOk" onclick="onCheck(this,'p4_getAttn')"
						<%= props.getProperty("p4_getAttnOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_getAttnOkConcerns"
						name="p4_getAttnOkConcerns" onclick="onCheck(this,'p4_getAttn')"
						<%= props.getProperty("p4_getAttnOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_getAttnNotDiscussed"
						name="p4_getAttnNotDiscussed" onclick="onCheck(this,'p4_getAttn')"
						<%= props.getProperty("p4_getAttnNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_4.formGetAttn" /></td>
				</tr>
               	<tr>
					<td valign="top"><input type="radio" id="p4_recsNameOk"
						name="p4_recsNameOk" onclick="onCheck(this,'p4_recsName')"
						<%= props.getProperty("p4_recsNameOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_recsNameOkConcerns"
						name="p4_recsNameOkConcerns" onclick="onCheck(this,'p4_recsName')"
						<%= props.getProperty("p4_recsNameOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_recsNameNotDiscussed"
						name="p4_recsNameNotDiscussed" onclick="onCheck(this,'p4_recsName')"
						<%= props.getProperty("p4_recsNameNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_4.formRecsName" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_points2wantOk"
						name="p4_points2wantOk" onclick="onCheck(this,'p4_points2want')"
						<%= props.getProperty("p4_points2wantOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_points2wantOkConcerns"
						name="p4_points2wantOkConcerns" onclick="onCheck(this,'p4_points2want')"
						<%= props.getProperty("p4_points2wantOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_points2wantNotDiscussed"
						name="p4_points2wantNotDiscussed" onclick="onCheck(this,'p4_points2want')"
						<%= props.getProperty("p4_points2wantNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_4.formPoints2want" /></td>
				</tr>
                <tr>
					<td valign="top"><input type="radio" id="p4_looks4toyOk"
						name="p4_looks4toyOk" onclick="onCheck(this,'p4_looks4toy')"
						<%= props.getProperty("p4_looks4toyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_looks4toyOkConcerns"
						name="p4_looks4toyOkConcerns" onclick="onCheck(this,'p4_looks4toy')"
						<%= props.getProperty("p4_looks4toyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_looks4toyNotDiscussed"
						name="p4_looks4toyNotDiscussed" onclick="onCheck(this,'p4_looks4toy')"
						<%= props.getProperty("p4_looks4toyNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_4.formLooks4toy" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_initSpeechOk"
						name="p4_initSpeechOk" onclick="onCheck(this,'p4_initSpeech')"
						<%= props.getProperty("p4_initSpeechOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_initSpeechOkConcerns"
						name="p4_initSpeechOkConcerns" onclick="onCheck(this,'p4_initSpeech')"
						<%= props.getProperty("p4_initSpeechOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_initSpeechNotDiscussed"
						name="p4_initSpeechNotDiscussed" onclick="onCheck(this,'p4_initSpeech')"
						<%= props.getProperty("p4_initSpeechNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_4.formInitSpeech" /></td>
				</tr>
                <tr>
					<td valign="top"><input type="radio" id="p4_says20wordsOk"
						name="p4_says20wordsOk" onclick="onCheck(this,'p4_says20words')"
						<%= props.getProperty("p4_says20wordsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_says20wordsOkConcerns"
						name="p4_says20wordsOkConcerns" onclick="onCheck(this,'p4_says20words')"
						<%= props.getProperty("p4_says20wordsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_says20wordsNotDiscussed"
						name="p4_says20wordsNotDiscussed" onclick="onCheck(this,'p4_says20words')"
						<%= props.getProperty("p4_says20wordsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_4.formSays20words" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_4consonantsOk"
						name="p4_4consonantsOk" onclick="onCheck(this,'p4_4consonants')"
						<%= props.getProperty("p4_4consonantsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_4consonantsOkConcerns"
						name="p4_4consonantsOkConcerns" onclick="onCheck(this,'p4_4consonants')"
						<%= props.getProperty("p4_4consonantsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_4consonantsNotDiscussed"
						name="p4_4consonantsNotDiscussed" onclick="onCheck(this,'p4_4consonants')"
						<%= props.getProperty("p4_4consonantsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_4.form4consonants" /></td>
				</tr>
				
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="4"><bean:message
						key="oscarEncounter.formRourke2006_4.formMotorSkills" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_walksbackAloneOk"
						name="p4_walksbackOk" onclick="onCheck(this,'p4_walksbackAlone')"
						<%= props.getProperty("p4_walksbackOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_walksbackAloneOkConcerns"
						name="p4_walksbackOkConcerns" onclick="onCheck(this,'p4_walksbackAlone')"
						<%= props.getProperty("p4_walksbackOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_walksbackAloneNotDiscussed"
						name="p4_walksbackNotDiscussed" onclick="onCheck(this,'p4_walksbackAlone')"
						<%= props.getProperty("p4_walksbackNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_4.formWalksAlone" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_feedsSelfOk"
						name="p4_feedsSelfOk" onclick="onCheck(this,'p4_feedsSelf')"
						<%= props.getProperty("p4_feedsSelfOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_feedsSelfOkConcerns"
						name="p4_feedsSelfOkConcerns" onclick="onCheck(this,'p4_feedsSelf')"
						<%= props.getProperty("p4_feedsSelfOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_feedsSelfNotDiscussed"
						name="p4_feedsSelfNotDiscussed" onclick="onCheck(this,'p4_feedsSelf')"
						<%= props.getProperty("p4_feedsSelfNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formFeedsSelf" /></td>
				</tr>
				
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="4"><bean:message
						key="oscarEncounter.formRourke2006_4.formAdaptiv" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_removesHatOk"
						name="p4_removesHatOk" onclick="onCheck(this,'p4_removesHat')"
						<%= props.getProperty("p4_removesHatOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_removesHatOkConcerns"
						name="p4_removesHatOkConcerns" onclick="onCheck(this,'p4_removesHat')"
						<%= props.getProperty("p4_removesHatOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_removesHatNotDiscussed"
						name="p4_removesHatNotDiscussed" onclick="onCheck(this,'p4_removesHat')"
						<%= props.getProperty("p4_removesHatNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formRemovesHat" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p4_noParentsConcerns18mOk" name="p4_noParentsConcerns18mOk"
						onclick="onCheck(this,'p4_noParentsConcerns18m')"
						<%= props.getProperty("p4_noParentsConcerns18mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p4_noParentsConcerns18mOkConcerns" name="p4_noParentsConcerns18mOkConcerns"
						onclick="onCheck(this,'p4_noParentsConcerns18m')"
						<%= props.getProperty("p4_noParentsConcerns18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p4_noParentsConcerns18mNotDiscussed" name="p4_noParentsConcerns18mNotDiscussed"
						onclick="onCheck(this,'p4_noParentsConcerns18m')"
						<%= props.getProperty("p4_noParentsConcerns18mNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
				</tr>
				
				<tr>
					<td colspan="4" style="vertical-align:bottom;">
						<textarea id="p4_development18m"
							name="p4_development18m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_development18m", "") %></textarea>					
					</td>
				</tr>
			</table>
			</td>
			<td colspan="5" align="center">							
			<table cellpadding="0" cellspacing="2" border="1" width="100%" height="100%">								
				<tr>
					<td>
					<table cellpadding="0" cellspacing="0" width="100%" height="100%">
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4"><bean:message
								key="oscarEncounter.formRourke2006_4.form2yrs" /></td>
						</tr>
						<tr>
							<td style="padding-right: 5pt" valign="top"><img height="15"
								width="20" src="graphics/Checkmark_L.gif"></td>
							<td class="edcol" valign="top">X</td>
							<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
						</tr>
                        <tr>
							<td valign="top"><input type="radio" id="p4_2wSentenceOk"
								name="p4_2wSentenceOk" onclick="onCheck(this,'p4_2wSentence')"
								<%= props.getProperty("p4_2wSentenceOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_2wSentenceOkConcerns"
								name="p4_2wSentenceOkConcerns" onclick="onCheck(this,'p4_2wSentence')"
								<%= props.getProperty("p4_2wSentenceOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_2wSentenceNotDiscussed"
								name="p4_2wSentenceNotDiscussed" onclick="onCheck(this,'p4_2wSentence')"
								<%= props.getProperty("p4_2wSentenceNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.form2wordSentence" /></td>
						</tr>
                       	<tr>
							<td valign="top"><input type="radio" id="p4_one2stepdirectionsOk"
								name="p4_one2stepdirectionsOk" onclick="onCheck(this,'p4_one2stepdirections')"
								<%= props.getProperty("p4_one2stepdirectionsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_one2stepdirectionsOkConcerns"
								name="p4_one2stepdirectionsOkConcerns" onclick="onCheck(this,'p4_one2stepdirections')"
								<%= props.getProperty("p4_one2stepdirectionsOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_one2stepdirectionsNotDiscussed"
								name="p4_one2stepdirectionsNotDiscussed" onclick="onCheck(this,'p4_one2stepdirections')"
								<%= props.getProperty("p4_one2stepdirectionsNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formone2stepDirections" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_walksbackwardOk"
								name="p4_walksbackwardOk" onclick="onCheck(this,'p4_walksbackward')"
								<%= props.getProperty("p4_walksbackwardOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_walksbackwardOkConcerns"
								name="p4_walksbackwardOkConcerns" onclick="onCheck(this,'p4_walksbackward')"
								<%= props.getProperty("p4_walksbackwardOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_walksbackwardNotDiscussed"
								name="p4_walksbackwardNotDiscussed" onclick="onCheck(this,'p4_walksbackward')"
								<%= props.getProperty("p4_walksbackwardNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formwalksbackward" /></td>
						</tr>						
						<tr>
							<td valign="top"><input type="radio" id="p4_runsOk"
								name="p4_runsOk" onclick="onCheck(this,'p4_runs')"
								<%= props.getProperty("p4_runsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_runsOkConcerns"
								name="p4_runsOkConcerns" onclick="onCheck(this,'p4_runs')"
								<%= props.getProperty("p4_runsOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_runsNotDiscussed"
								name="p4_runsNotDiscussed" onclick="onCheck(this,'p4_runs')"
								<%= props.getProperty("p4_runsNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formTriestoRun" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio"
								id="p4_smallContainerOk" name="p4_smallContainerOk"
								onclick="onCheck(this,'p4_smallContainer')"
								<%= props.getProperty("p4_smallContainerOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_smallContainerOkConcerns" name="p4_smallContainerOkConcerns"
								onclick="onCheck(this,'p4_smallContainer')"
								<%= props.getProperty("p4_smallContainerOkConcerns", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_smallContainerNotDiscussed" name="p4_smallContainerNotDiscussed"
								onclick="onCheck(this,'p4_smallContainer')"
								<%= props.getProperty("p4_smallContainerNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formSmallContainer" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_pretendsPlayOk"
								name="p4_pretendsPlayOk"
								onclick="onCheck(this,'p4_pretendsPlay')"
								<%= props.getProperty("p4_pretendsPlayOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_pretendsPlayOkConcerns"
								name="p4_pretendsPlayOkConcerns"
								onclick="onCheck(this,'p4_pretendsPlay')"
								<%= props.getProperty("p4_pretendsPlayOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_pretendsPlayNotDiscussed"
								name="p4_pretendsPlayNotDiscussed"
								onclick="onCheck(this,'p4_pretendsPlay')"
								<%= props.getProperty("p4_pretendsPlayNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formpretendsPlay" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_newSkillsOk"
								name="p4_newSkillsOk" onclick="onCheck(this,'p4_newSkills')"
								<%= props.getProperty("p4_newSkillsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_newSkillsOkConcerns"
								name="p4_newSkillsOkConcerns" onclick="onCheck(this,'p4_newSkills')"
								<%= props.getProperty("p4_newSkillsOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_newSkillsNotDiscussed"
								name="p4_newSkillsNotDiscussed" onclick="onCheck(this,'p4_newSkills')"
								<%= props.getProperty("p4_newSkillsNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formNewSkills" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns24mOk" name="p4_noParentsConcerns24mOk"
								onclick="onCheck(this,'p4_noParentsConcerns24m')"
								<%= props.getProperty("p4_noParentsConcerns24mOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns24mOkConcerns" name="p4_noParentsConcerns24mOkConcerns"
								onclick="onCheck(this,'p4_noParentsConcerns24m')"
								<%= props.getProperty("p4_noParentsConcerns24mOkConcerns", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns24mNotDiscussed" name="p4_noParentsConcerns24mNotDiscussed"
								onclick="onCheck(this,'p4_noParentsConcerns24m')"
								<%= props.getProperty("p4_noParentsConcerns24mNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
						</tr>
						
						<tr>
							<td colspan="4" style="vertical-align:bottom;">
								<textarea id="p4_development24m"
											name="p4_development24m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_development24m", "") %></textarea>
							</td>
							</tr>															
					</table>
					</td>
					<td>
					<table cellpadding="0" cellspacing="0" height="100%">
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4"><bean:message
								key="oscarEncounter.formRourke2006_4.form4yrs" /></td>
						</tr>
						<tr>
							<td style="padding-right: 5pt" valign="top"><img height="15"
								width="20" src="graphics/Checkmark_L.gif"></td>
							<td class="edcol" valign="top">X</td>
							<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_3directionsOk"
								name="p4_3directionsOk" onclick="onCheck(this,'p4_3directions')"
								<%= props.getProperty("p4_3directionsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_3directionsOkConcerns"
								name="p4_3directionsOkConcerns" onclick="onCheck(this,'p4_3directions')"
								<%= props.getProperty("p4_3directionsOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_3directionsNotDiscussed"
								name="p4_3directionsNotDiscussed" onclick="onCheck(this,'p4_3directions')"
								<%= props.getProperty("p4_3directionsNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.form3Directions" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_asksQuestionsOk"
								name="p4_asksQuestionsOk"
								onclick="onCheck(this,'p4_asksQuestions')"
								<%= props.getProperty("p4_asksQuestionsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_asksQuestionsOkConcerns"
								name="p4_asksQuestionsOkConcerns"
								onclick="onCheck(this,'p4_asksQuestions')"
								<%= props.getProperty("p4_asksQuestionsOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_asksQuestionsNotDiscussed"
								name="p4_asksQuestionsNotDiscussed"
								onclick="onCheck(this,'p4_asksQuestions')"
								<%= props.getProperty("p4_asksQuestionsNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formAsksQuestions" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_upDownStairsOk"
								name="p4_upDownStairsOk" onclick="onCheck(this,'p4_upDownStairs')"
								<%= props.getProperty("p4_upDownStairsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_upDownStairsOkConcerns"
								name="p4_upDownStairsOkConcerns" onclick="onCheck(this,'p4_upDownStairs')"
								<%= props.getProperty("p4_upDownStairsOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_upDownStairsNotDiscussed"
								name="p4_upDownStairsNotDiscussed" onclick="onCheck(this,'p4_upDownStairs')"
								<%= props.getProperty("p4_upDownStairsNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formupDownStairs" /></td>
						</tr>						
						<tr>
							<td valign="top"><input type="radio" id="p4_undoesZippersOk"
								name="p4_undoesZippersOk"
								onclick="onCheck(this,'p4_undoesZippers')"
								<%= props.getProperty("p4_undoesZippersOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_undoesZippersOkConcerns"
								name="p4_undoesZippersOkConcerns"
								onclick="onCheck(this,'p4_undoesZippers')"
								<%= props.getProperty("p4_undoesZippersOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_undoesZippersNotDiscussed"
								name="p4_undoesZippersNotDiscussed"
								onclick="onCheck(this,'p4_undoesZippers')"
								<%= props.getProperty("p4_undoesZippersNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formundoesZippers" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_tries2comfortOk"
								name="p4_tries2comfortOk"
								onclick="onCheck(this,'p4_tries2comfort')"
								<%= props.getProperty("p4_tries2comfortOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_tries2comfortOkConcerns"
								name="p4_tries2comfortOkConcerns"
								onclick="onCheck(this,'p4_tries2comfort')"
								<%= props.getProperty("p4_tries2comfortOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_tries2comfortNotDiscussed"
								name="p4_tries2comfortNotDiscussed"
								onclick="onCheck(this,'p4_tries2comfort')"
								<%= props.getProperty("p4_tries2comfortNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formTries2comfort" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns48mOk" name="p4_noParentsConcerns48mOk"
								onclick="onCheck(this,'p4_noParentsConcerns48m')"
								<%= props.getProperty("p4_noParentsConcerns48mOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns48mOkConcerns" name="p4_noParentsConcerns48mOkConcerns"
								onclick="onCheck(this,'p4_noParentsConcerns48m')"
								<%= props.getProperty("p4_noParentsConcerns48mOkConcerns", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns48mNotDiscussed" name="p4_noParentsConcerns48mNotDiscussed"
								onclick="onCheck(this,'p4_noParentsConcerns48m')"
								<%= props.getProperty("p4_noParentsConcerns48mNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
						</tr>
						
						<tr>
							<td colspan="4" style="vertical-align:bottom;">
								<textarea id="p4_development48m"
											name="p4_development48m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_development48m", "") %></textarea>
							</td>
						</tr>	
					</table>
					</td>
				</tr>
				<tr>
					<td>
					<table cellpadding="0" cellspacing="0" width="100%" height="100%">
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4"><bean:message
								key="oscarEncounter.formRourke2006_4.form3yrs" /></td>
						</tr>
						<tr>
							<td style="padding-right: 5pt" valign="top"><img height="15"
								width="20" src="graphics/Checkmark_L.gif"></td>
							<td class="edcol" valign="top">X</td>
							<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_2directionsOk"
								name="p4_2directionsOk" onclick="onCheck(this,'p4_2directions')"
								<%= props.getProperty("p4_2directionsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_2directionsOkConcerns"
								name="p4_2directionsOkConcerns" onclick="onCheck(this,'p4_2directions')"
								<%= props.getProperty("p4_2directionsOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_2directionsNotDiscussed"
								name="p4_2directionsNotDiscussed" onclick="onCheck(this,'p4_2directions')"
								<%= props.getProperty("p4_2directionsNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.form2Directions" /></td>
						</tr>
                        <tr>
							<td valign="top"><input type="radio" id="p4_5ormoreWordsOk"
								name="p4_5ormoreWordsOk" onclick="onCheck(this,'p4_5ormoreWords')"
								<%= props.getProperty("p4_5ormoreWordsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_5ormoreWordsOkConcerns"
								name="p4_5ormoreWordsOkConcerns" onclick="onCheck(this,'p4_5ormoreWords')"
								<%= props.getProperty("p4_5ormoreWordsOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_5ormoreWordsNotDiscussed"
								name="p4_5ormoreWordsNotDiscussed" onclick="onCheck(this,'p4_5ormoreWords')"
								<%= props.getProperty("p4_5ormoreWordsNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.form5ormoreWords" /></td>
						</tr>
                        <tr>
							<td valign="top"><input type="radio" id="p4_walksUpStairsOk"
								name="p4_walksUpStairsOk" onclick="onCheck(this,'p4_walksUpStairs')"
								<%= props.getProperty("p4_walksUpStairsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_walksUpStairsOkConcerns"
								name="p4_walksUpStairsOkConcerns" onclick="onCheck(this,'p4_walksUpStairs')"
								<%= props.getProperty("p4_walksUpStairsOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_walksUpStairsNotDiscussed"
								name="p4_walksUpStairsNotDiscussed" onclick="onCheck(this,'p4_walksUpStairs')"
								<%= props.getProperty("p4_walksUpStairsNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formwalksUpStairs" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_twistslidsOk"
								name="p4_twistslidsOk" onclick="onCheck(this,'p4_twistslids')"
								<%= props.getProperty("p4_twistslidsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_twistslidsOkConcerns"
								name="p4_twistslidsOkConcerns" onclick="onCheck(this,'p4_twistslids')"
								<%= props.getProperty("p4_twistslidsOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_twistslidsNotDiscussed"
								name="p4_twistslidsNotDiscussed" onclick="onCheck(this,'p4_twistslids')"
								<%= props.getProperty("p4_twistslidsNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formTwistsLids" /></td>
						</tr>
                       	<tr>
							<td valign="top"><input type="radio"
								id="p4_sharesSometimeOk" name="p4_sharesSometimeOk"
								onclick="onCheck(this,'p4_sharesSometime')"
								<%= props.getProperty("p4_sharesSometimeOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_sharesSometimeOkConcerns" name="p4_sharesSometimeOkConcerns"
								onclick="onCheck(this,'p4_sharesSometime')"
								<%= props.getProperty("p4_sharesSometimeOkConcerns", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_sharesSometimeNotDiscussed" name="p4_sharesSometimeNotDiscussed"
								onclick="onCheck(this,'p4_sharesSometime')"
								<%= props.getProperty("p4_sharesSometimeNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formSharesSometimes" /></td>
						</tr>
                       	<tr>
							<td valign="top"><input type="radio"
								id="p4_playMakeBelieveOk" name="p4_playMakeBelieveOk"
								onclick="onCheck(this,'p4_playMakeBelieve')"
								<%= props.getProperty("p4_playMakeBelieveOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_playMakeBelieveOkConcerns" name="p4_playMakeBelieveOkConcerns"
								onclick="onCheck(this,'p4_playMakeBelieve')"
								<%= props.getProperty("p4_playMakeBelieveOkConcerns", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_playMakeBelieveNotDiscussed" name="p4_playMakeBelieveNotDiscussed"
								onclick="onCheck(this,'p4_playMakeBelieve')"
								<%= props.getProperty("p4_playMakeBelieveNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formplayMakeBelieve" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_turnsPagesOk"
								name="p4_turnsPagesOk" onclick="onCheck(this,'p4_turnsPages')"
								<%= props.getProperty("p4_turnsPagesOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_turnsPagesOkConcerns"
								name="p4_turnsPagesOkConcerns" onclick="onCheck(this,'p4_turnsPages')"
								<%= props.getProperty("p4_turnsPagesOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_turnsPagesNotDiscussed"
								name="p4_turnsPagesNotDiscussed" onclick="onCheck(this,'p4_turnsPages')"
								<%= props.getProperty("p4_turnsPagesNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formTurnsPages" /></td>
						</tr>						
						<tr>
							<td valign="top"><input type="radio" id="p4_listenMusikOk"
								name="p4_listenMusikOk" onclick="onCheck(this,'p4_listenMusik')"
								<%= props.getProperty("p4_listenMusikOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_listenMusikOkConcerns"
								name="p4_listenMusikOkConcerns" onclick="onCheck(this,'p4_listenMusik')"
								<%= props.getProperty("p4_listenMusikOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_listenMusikNotDiscussed"
								name="p4_listenMusikNotDiscussed" onclick="onCheck(this,'p4_listenMusik')"
								<%= props.getProperty("p4_listenMusikNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formListensMusik" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns36mOk" name="p4_noParentsConcerns36mOk"
								onclick="onCheck(this,'p4_noParentsConcerns36m')"
								<%= props.getProperty("p4_noParentsConcerns36mOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns36mOkConcerns" name="p4_noParentsConcerns36mOkConcerns"
								onclick="onCheck(this,'p4_noParentsConcerns36m')"
								<%= props.getProperty("p4_noParentsConcerns36mOkConcerns", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns36mNotDiscussed" name="p4_noParentsConcerns36mNotDiscussed"
								onclick="onCheck(this,'p4_noParentsConcerns36m')"
								<%= props.getProperty("p4_noParentsConcerns36mNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
						</tr>
						
						<tr>
							<td colspan="4" style="vertical-align:bottom;">
								<textarea id="p4_development36m"
											name="p4_development36m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_development36m", "") %></textarea>
							</td>
						</tr>
					</table>
					</td>
					<td>
					<table cellpadding="0" cellspacing="0" width="100%" height="100%">
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4"><bean:message
								key="oscarEncounter.formRourke2006_4.form5yrs" /></td>
						</tr>
						<tr>
							<td style="padding-right: 5pt" valign="top"><img height="15"
								width="20" src="graphics/Checkmark_L.gif"></td>
							<td class="edcol" valign="top">X</td>
							<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_countsOutloudOk"
								name="p4_countsOutloudOk" onclick="onCheck(this,'p4_countsOutloud')"
								<%= props.getProperty("p4_countsOutloudOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_countsOutloudOkConcerns"
								name="p4_countsOutloudOkConcerns" onclick="onCheck(this,'p4_countsOutloud')"
								<%= props.getProperty("p4_countsOutloudOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_countsOutloudNotDiscussed"
								name="p4_countsOutloudNotDiscussed" onclick="onCheck(this,'p4_countsOutloud')"
								<%= props.getProperty("p4_countsOutloudNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formCountsOutloud" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_speaksClearlyOk"
								name="p4_speaksClearlyOk"
								onclick="onCheck(this,'p4_speaksClearly')"
								<%= props.getProperty("p4_speaksClearlyOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_speaksClearlyOkConcerns"
								name="p4_speaksClearlyOkConcerns"
								onclick="onCheck(this,'p4_speaksClearly')"
								<%= props.getProperty("p4_speaksClearlyOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_speaksClearlyNotDiscussed"
								name="p4_speaksClearlyNotDiscussed"
								onclick="onCheck(this,'p4_speaksClearly')"
								<%= props.getProperty("p4_speaksClearlyNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formSpeaksClearly" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_throwsCatchesOk"
								name="p4_throwsCatchesOk"
								onclick="onCheck(this,'p4_throwsCatches')"
								<%= props.getProperty("p4_throwsCatchesOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_throwsCatchesOkConcerns"
								name="p4_throwsCatchesOkConcerns"
								onclick="onCheck(this,'p4_throwsCatches')"
								<%= props.getProperty("p4_throwsCatchesOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_throwsCatchesNotDiscussed"
								name="p4_throwsCatchesNotDiscussed"
								onclick="onCheck(this,'p4_throwsCatches')"
								<%= props.getProperty("p4_throwsCatchesNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formThrowsCatches" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_hops1footOk"
								name="p4_hops1footOk" onclick="onCheck(this,'p4_hops1foot')"
								<%= props.getProperty("p4_hops1footOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_hops1footOkConcerns"
								name="p4_hops1footOkConcerns" onclick="onCheck(this,'p4_hops1foot')"
								<%= props.getProperty("p4_hops1footOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_hops1footNotDiscussed"
								name="p4_hops1footNotDiscussed" onclick="onCheck(this,'p4_hops1foot')"
								<%= props.getProperty("p4_hops1footNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formHops1Foot" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio"
								id="p4_dressesUndressesOk" name="p4_dressesUndressesOk"
								onclick="onCheck(this,'p4_dressesUndresses')"
								<%= props.getProperty("p4_dressesUndressesOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_dressesUndressesOkConcerns" name="p4_dressesUndressesOkConcerns"
								onclick="onCheck(this,'p4_dressesUndresses')"
								<%= props.getProperty("p4_dressesUndressesOkConcerns", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_dressesUndressesNotDiscussed" name="p4_dressesUndressesNotDiscussed"
								onclick="onCheck(this,'p4_dressesUndresses')"
								<%= props.getProperty("p4_dressesUndressesNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formdressesUndresses" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_obeysAdultOk"
								name="p4_obeysAdultOk" onclick="onCheck(this,'p4_obeysAdult')"
								<%= props.getProperty("p4_obeysAdultOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_obeysAdultOkConcerns"
								name="p4_obeysAdultOkConcerns" onclick="onCheck(this,'p4_obeysAdult')"
								<%= props.getProperty("p4_obeysAdultOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_obeysAdultNotDiscussed"
								name="p4_obeysAdultNotDiscussed" onclick="onCheck(this,'p4_obeysAdult')"
								<%= props.getProperty("p4_obeysAdultNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formobeysAdult" /></td>
						</tr>
                        <tr>
							<td valign="top"><input type="radio" id="p4_retellsStoryOk"
								name="p4_retellsStoryOk" onclick="onCheck(this,'p4_retellsStory')"
								<%= props.getProperty("p4_retellsStoryOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_retellsStoryOkConcerns"
								name="p4_retellsStoryOkConcerns" onclick="onCheck(this,'p4_retellsStory')"
								<%= props.getProperty("p4_retellsStoryOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_retellsStoryNotDiscussed"
								name="p4_retellsStoryNotDiscussed" onclick="onCheck(this,'p4_retellsStory')"
								<%= props.getProperty("p4_retellsStoryNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formretellsStory" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_separatesOk"
								name="p4_separatesOk" onclick="onCheck(this,'p4_separates')"
								<%= props.getProperty("p4_separatesOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_separatesOkConcerns"
								name="p4_separatesOkConcerns" onclick="onCheck(this,'p4_separates')"
								<%= props.getProperty("p4_separatesOkConcerns", "") %>></td>
							<td valign="top"><input type="radio" id="p4_separatesNotDiscussed"
								name="p4_separatesNotDiscussed" onclick="onCheck(this,'p4_separates')"
								<%= props.getProperty("p4_separatesNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009_4.formSeparates" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns60mOk" name="p4_noParentsConcerns60mOk"
								onclick="onCheck(this,'p4_noParentsConcerns60m')"
								<%= props.getProperty("p4_noParentsConcerns60mOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns60mOkConcerns" name="p4_noParentsConcerns60mOkConcerns"
								onclick="onCheck(this,'p4_noParentsConcerns60m')"
								<%= props.getProperty("p4_noParentsConcerns60mOkConcerns", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns60mNotDiscussed" name="p4_noParentsConcerns60mNotDiscussed"
								onclick="onCheck(this,'p4_noParentsConcerns60m')"
								<%= props.getProperty("p4_noParentsConcerns60mNotDiscussed", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
						</tr>
						
						<tr>
							<td colspan="4" style="vertical-align:bottom;">
								<textarea id="p4_development60m"
											name="p4_development60m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_development60m", "") %></textarea>
							</td>
						</tr>
					</table>
					</td>
			</table>
			</td>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgDevelopment" />**</a><br>
                            <bean:message key="oscarEncounter.formRourke2009_1.msgDevelopmentDesc" />
                            <br>
			<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif"><bean:message
				key="oscarEncounter.formRourke2006_1.msgDevelopmentLegend" /></td>
		</tr>
		<tr id="physicalExamp4">
			<td colspan="3" id="physicalExamp4a">
			<table id="pt41" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
              	<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
                                <tr>
                                        <td valign="top"><input type="radio"
                                                id="p4_fontanellesClosedOk" name="p4_fontanellesClosedOk"
                                                onclick="onCheck(this,'p4_fontanellesClosed')"
                                                <%= props.getProperty("p4_fontanellesClosedOk", "") %>></td>
                                        <td valign="top"><input type="radio"
                                                id="p4_fontanellesClosedOkConcerns" name="p4_fontanellesClosedOkConcerns"
                                                onclick="onCheck(this,'p4_fontanellesClosed')"
                                                <%= props.getProperty("p4_fontanellesClosedOkConcerns", "") %>></td>
                                        <td valign="top"><input type="radio"
                                                id="p4_fontanellesClosedNotDiscussed" name="p4_fontanellesClosedNotDiscussed"
                                                onclick="onCheck(this,'p4_fontanellesClosed')"
                                                <%= props.getProperty("p4_fontanellesClosedNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
                                                key="oscarEncounter.formRourke2009_4.formFontanellesClosed" /></td>
                                </tr>
                                
                                <tr>
					<td valign="top"><input type="radio" id="p4_eyes18mOk"
						name="p4_eyes18mOk" onclick="onCheck(this,'p4_eyes18m')"
						<%= props.getProperty("p4_eyes18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_eyes18mOkConcerns"
						name="p4_eyes18mOkConcerns" onclick="onCheck(this,'p4_eyes18m')"
						<%= props.getProperty("p4_eyes18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_eyes18mNotDiscussed"
						name="p4_eyes18mNotDiscussed" onclick="onCheck(this,'p4_eyes18m')"
						<%= props.getProperty("p4_eyes18mNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_4.formEyes" />*</a></b></td>
				</tr>
                                
                                <tr>
					<td valign="top"><input type="radio" id="p4_corneal18mOk"
						name="p4_corneal18mOk" onclick="onCheck(this,'p4_corneal18m')"
						<%= props.getProperty("p4_corneal18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_corneal18mOkConcerns"
						name="p4_corneal18mOkConcerns" onclick="onCheck(this,'p4_corneal18m')"
						<%= props.getProperty("p4_corneal18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_corneal18mNotDiscussed"
						name="p4_corneal18mNotDiscussed" onclick="onCheck(this,'p4_corneal18m')"
						<%= props.getProperty("p4_corneal18mNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_4.formCorneal" />*</a></b></td>
				</tr>
                                
                                <tr>
					<td valign="top"><input type="radio" id="p4_hearing18mOk"
						name="p4_hearing18mOk" onclick="onCheck(this,'p4_hearing18m')"
						<%= props.getProperty("p4_hearing18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_hearing18mOkConcerns"
						name="p4_hearing18mOkConcerns" onclick="onCheck(this,'p4_hearing18m')"
						<%= props.getProperty("p4_hearing18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_hearing18mNotDiscussed"
						name="p4_hearing18mNotDiscussed" onclick="onCheck(this,'p4_hearing18m')"
						<%= props.getProperty("p4_hearing18mNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formHearingInquiry" /></td>
				</tr>
                                
				<tr>
					<td valign="top"><input type="radio" id="p4_tonsil18mOk"
						name="p4_tonsil18mOk" onclick="onCheck(this,'p4_tonsil18m')"
						<%= props.getProperty("p4_tonsil18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_tonsil18mOkConcerns"
						name="p4_tonsil18mOkConcerns" onclick="onCheck(this,'p4_tonsil18m')"
						<%= props.getProperty("p4_tonsil18mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_tonsil18mNotDiscussed"
						name="p4_tonsil18mNotDiscussed" onclick="onCheck(this,'p4_tonsil18m')"
						<%= props.getProperty("p4_tonsil18mNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formTonsilSize" />*</a></td>
				</tr>
                                
               <tr>
               		<td colspan="4" style="vertical-align:bottom;">
						<textarea id="p4_physical18m"
							name="p4_physical18m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_physical18m", "") %></textarea>
               		
               		</td>
               	</tr>
			</table>
			</td>
			<td colspan="3" id="physicalExamp4b">
			<table id="pt42" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
                <tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
                                <tr>
                                        <td valign="top"><input type="radio"
                                                id="p4_bloodpressure24mOk" name="p4_bloodpressure24mOk"
                                                onclick="onCheck(this,'p4_bloodpressure24m')"
                                                <%= props.getProperty("p4_bloodpressure24mOk", "") %>></td>
                                        <td valign="top"><input type="radio"
                                                id="p4_bloodpressure24mOkConcerns" name="p4_bloodpressure24mOkConcerns"
                                                onclick="onCheck(this,'p4_bloodpressure24m')"
                                                <%= props.getProperty("p4_bloodpressure24mOkConcerns", "") %>></td>
                                        <td valign="top"><input type="radio"
                                                id="p4_bloodpressure24mNotDiscussed" name="p4_bloodpressure24mNotDiscussed"
                                                onclick="onCheck(this,'p4_bloodpressure24m')"
                                                <%= props.getProperty("p4_bloodpressure24mNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
                                                key="oscarEncounter.formRourke2006_4.formBloodPressure" /></td>
                                </tr>
                               
                                <tr>
					<td valign="top"><input type="radio" id="p4_eyes24mOk"
						name="p4_eyes24mOk" onclick="onCheck(this,'p4_eyes24m')"
						<%= props.getProperty("p4_eyes24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_eyes24mOkConcerns"
						name="p4_eyes24mOkConcerns" onclick="onCheck(this,'p4_eyes24m')"
						<%= props.getProperty("p4_eyes24mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_eyes24mNotDiscussed"
						name="p4_eyes24mNotDiscussed" onclick="onCheck(this,'p4_eyes24m')"
						<%= props.getProperty("p4_eyes24mNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_4.formEyes" />*</a></b></td>
				</tr>
                                
                                <tr>
					<td valign="top"><input type="radio" id="p4_corneal24mOk"
						name="p4_corneal24mOk" onclick="onCheck(this,'p4_corneal24m')"
						<%= props.getProperty("p4_corneal24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_corneal24mOkConcerns"
						name="p4_corneal24mOkConcerns" onclick="onCheck(this,'p4_corneal24m')"
						<%= props.getProperty("p4_corneal24mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_corneal24mNotDiscussed"
						name="p4_corneal24mNotDiscussed" onclick="onCheck(this,'p4_corneal24m')"
						<%= props.getProperty("p4_corneal24mNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_4.formCorneal" />*</a></b></td>
				</tr>
                                
                                <tr>
					<td valign="top"><input type="radio" id="p4_hearing24mOk"
						name="p4_hearing24mOk" onclick="onCheck(this,'p4_hearing24m')"
						<%= props.getProperty("p4_hearing24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_hearing24mOkConcerns"
						name="p4_hearing24mOkConcerns" onclick="onCheck(this,'p4_hearing24m')"
						<%= props.getProperty("p4_hearing24mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_hearing24mNotDiscussed"
						name="p4_hearing24mNotDiscussed" onclick="onCheck(this,'p4_hearing24m')"
						<%= props.getProperty("p4_hearing24mNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formHearingInquiry" /></td>
				</tr>
                                
				<tr>
					<td valign="top"><input type="radio" id="p4_tonsil24mOk"
						name="p4_tonsil24mOk" onclick="onCheck(this,'p4_tonsil24m')"
						<%= props.getProperty("p4_tonsil24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_tonsil24mOkConcerns"
						name="p4_tonsil24mOkConcerns" onclick="onCheck(this,'p4_tonsil24m')"
						<%= props.getProperty("p4_tonsil24mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_tonsil24mNotDiscussed"
						name="p4_tonsil24mNotDiscussed" onclick="onCheck(this,'p4_tonsil24m')"
						<%= props.getProperty("p4_tonsil24mNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formTonsilSize" />*</a></td>
				</tr>
                                
            	<tr>
            		<td colspan="4" style="vertical-align:bottom;">
            			<textarea id="p4_physicalm"
							name="p4_physical24m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_physical24m", "") %></textarea>            		
            		</td>
            	</tr>
			</table>
			</td>
			<td colspan="2" id="physicalExamp4c">
			<table id="pt43" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
                                 <tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
                                <tr>
                                        <td valign="top"><input type="radio"
                                                id="p4_bloodpressure48mOk" name="p4_bloodpressure48mOk"
                                                onclick="onCheck(this,'p4_bloodpressure48m')"
                                                <%= props.getProperty("p4_bloodpressure48mOk", "") %>></td>
                                        <td valign="top"><input type="radio"
                                                id="p4_bloodpressure48mOkConcerns" name="p4_bloodpressure48mOkConcerns"
                                                onclick="onCheck(this,'p4_bloodpressure48m')"
                                                <%= props.getProperty("p4_bloodpressure48mOkConcerns", "") %>></td>
                                        <td valign="top"><input type="radio"
                                                id="p4_bloodpressure48mNotDiscussed" name="p4_bloodpressure48mNotDiscussed"
                                                onclick="onCheck(this,'p4_bloodpressure48m')"
                                                <%= props.getProperty("p4_bloodpressure48mNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
                                                key="oscarEncounter.formRourke2006_4.formBloodPressure" /></td>
                                </tr>
                                
                                <tr>
					<td valign="top"><input type="radio" id="p4_eyes48mOk"
						name="p4_eyes48mOk" onclick="onCheck(this,'p4_eyes48m')"
						<%= props.getProperty("p4_eyes48mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_eyes48mOkConcerns"
						name="p4_eyes48mOkConcerns" onclick="onCheck(this,'p4_eyes48m')"
						<%= props.getProperty("p4_eyes48mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_eyes48mNotDiscussed"
						name="p4_eyes48mNotDiscussed" onclick="onCheck(this,'p4_eyes48m')"
						<%= props.getProperty("p4_eyes48mNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_4.formEyes" />*</a></b></td>
				</tr>
                                
                                <tr>
					<td valign="top"><input type="radio" id="p4_corneal48mOk"
						name="p4_corneal48mOk" onclick="onCheck(this,'p4_corneal48m')"
						<%= props.getProperty("p4_corneal48mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_corneal48mOkConcerns"
						name="p4_corneal48mOkConcerns" onclick="onCheck(this,'p4_corneal48m')"
						<%= props.getProperty("p4_corneal48mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_corneal48mNotDiscussed"
						name="p4_corneal48mNotDiscussed" onclick="onCheck(this,'p4_corneal48m')"
						<%= props.getProperty("p4_corneal48mNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_4.formCorneal" />*</a></b></td>
				</tr>
                                
                                <tr>
					<td valign="top"><input type="radio" id="p4_hearing48mOk"
						name="p4_hearing48mOk" onclick="onCheck(this,'p4_hearing48m')"
						<%= props.getProperty("p4_hearing48mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_hearing48mOkConcerns"
						name="p4_hearing48mOkConcerns" onclick="onCheck(this,'p4_hearing48m')"
						<%= props.getProperty("p4_hearing48mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_hearing48mNotDiscussed"
						name="p4_hearing48mNotDiscussed" onclick="onCheck(this,'p4_hearing48m')"
						<%= props.getProperty("p4_hearing48mNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formHearingInquiry" /></td>
				</tr>
                                
				<tr>
					<td valign="top"><input type="radio" id="p4_tonsil48mOk"
						name="p4_tonsil48mOk" onclick="onCheck(this,'p4_tonsil48m')"
						<%= props.getProperty("p4_tonsil48mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_tonsil48mOkConcerns"
						name="p4_tonsil48mOkConcerns" onclick="onCheck(this,'p4_tonsil48m')"
						<%= props.getProperty("p4_tonsil48mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p4_tonsil48mNotDiscussed"
						name="p4_tonsil48mNotDiscussed" onclick="onCheck(this,'p4_tonsil48m')"
						<%= props.getProperty("p4_tonsil48mNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formTonsilSize" />*</a></td>
				</tr>
                                
              	<tr>
              		<td colspan="4" style="vertical-align:bottom;">
              			<textarea id="p4_physical48m"
							name="p4_physical48m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_physical48m", "") %></textarea>
              		</td>
              	</tr>
			</table>			
			</td>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgPhysicalExamination" /></a><br>
			<bean:message
				key="oscarEncounter.formRourke1.msgPhysicalExaminationDesc" /><br>
                        <img height="15" width="20" src="graphics/Checkmark_Lwhite.gif">
                        <bean:message key="oscarEncounter.formRourke2009.msgPhysicalExaminationLegend"/>
			
			</td>
		</tr>
		<tr id="problemsPlansp4">			
			<td colspan="3" valign="bottom"><textarea id="p4_problems18m"
				name="p4_problems18m" rows="5" cols="25" class="wide"><%= props.getProperty("p4_problems18m", "") %></textarea>
			</td>
			<td colspan="3" valign="bottom"><textarea id="p4_problems24m"
				name="p4_problems24m" rows="5" cols="25" class="wide"><%= props.getProperty("p4_problems24m", "") %></textarea>
			</td>
			<td colspan="2" valign="bottom"><textarea id="p4_problems48m"
				name="p4_problems48m" rows="5" cols="25" class="wide"><%= props.getProperty("p4_problems48m", "") %></textarea>
			</td>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgProblemsAndPlans" /></a></td>
		</tr>
		<tr id="immunizationp4">			
			<td style="text-align: center" colspan="3" valign="top"><b><bean:message
				key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b></td>
			<td style="text-align: center" colspan="3" valign="top"><b><bean:message
				key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b></td>
			
			<td style="text-align: center" colspan="2" valign="top"><b><bean:message
				key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b></td>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgImmunization" /></a><br>
			<bean:message key="oscarEncounter.formRourke1.msgImmunizationDesc" />
			</td>
		</tr>
		<tr>			
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p4_signature18m"
				value="<%= props.getProperty("p4_signature18m", "") %>" /></td>
			<td colspan="3"><input type="text" class="wide" maxlength="42"
				style="width: 100%" name="p4_signature24m"
				value="<%= props.getProperty("p4_signature24m", "") %>" /></td>
			<td colspan="2"><input type="text" class="wide"
				style="width: 100%" name="p4_signature48m"
				value="<%= props.getProperty("p4_signature48m", "") %>" /></td>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.formSignature" /></a></td>
		</tr>

	</table>

	<table cellpadding="0" cellspacing="0" class="Header" class="hidePrint">
		<tr>
			<td nowrap="true"><input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnSave"/>"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnSaveExit"/>"
				onclick="javascript:return onSaveExit();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnExit"/>"
				onclick="javascript:return onExit();"> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnPrint"/>"
				onclick="javascript:return onPrint();" /> <input type="button"
				value="About"
				onclick="javascript:return popPage('http://rourkebabyrecord.ca','About Rourke');" />
			</td>
			<td align="center" nowrap="true" width="100%">
			<% if( growthChartURL.length() > 0 ) {%>
				<a style="color:red; font-weight:bold; text-decoration:underline; cursor:pointer;" "href="#" onclick="popPage('<%=growthChartURL%>','growthChart')">Growth Chart Avail</a>
			
			<%} else { %>
				&nbsp; 
			<% }%>
			</td>
			<td align="center" nowrap="true" width="100%">
			<% if(formId > 0)
           { %> <a name="length" href="#"
				onclick="onGraph('<%=request.getContextPath()%>/form/formname.do?submit=graph&form_class=Rourke2009&__title=Baby+Growth+Graph1&__cfgfile=<%=growthCharts[0]%>&demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>','<%= "growth1" + demoNo %>');return false;">
			<bean:message key="oscarEncounter.formRourke1.btnGraphLenghtWeight" /></a><br>
			<a name="headCirc" href="#"
				onclick="onGraph('<%=request.getContextPath()%>/form/formname.do?submit=graph&form_class=Rourke2009&__title=Baby+Head+Circumference&__cfgfile=<%=growthCharts[1]%>&demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>','<%= "growth2" + demoNo %>');return false;">
			<bean:message key="oscarEncounter.formRourke1.btnGraphHead" /></a> <% }else { %>
			&nbsp; <% } %>
			</td>
			
		</tr>
	</table>
	<p style="font-size: 8pt;"><bean:message
		key="oscarEncounter.formRourke2009.footer" /><br />
	</p>

<script type="text/javascript">
    Calendar.setup({ inputField : "p4_date18m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p4_date18m_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "p4_date24m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p4_date24m_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "p4_date48m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p4_date48m_cal", singleClick : true, step : 1 });
</script>
