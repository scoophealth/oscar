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
    //props.setProperty("c_lastVisited", "p2");

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

	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr class="titleBar">
			<th><bean:message
				key="oscarEncounter.formRourke2006_2.msgRourkeBabyRecord" /></th>
		</tr>
	</table>	
	<div id="patientInfop2">
		<bean:message key="oscarEncounter.formRourke1.msgName" />: <input
				type="text" maxlength="60" size="30"
				value="<%= props.getProperty("c_pName", "") %>" readonly="true" />
			&nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke1.msgBirthDate" /> (d/m/yyyy): <input
				type="text" id="c_birthDate2" size="10" maxlength="10"
				value="<%= props.getProperty("c_birthDate", "") %>" readonly="true">
			&nbsp;&nbsp; 
			Age: <input type="text" id="currentAge2" size="10" maxlength="10" readonly="true" ondblclick="calcAge();">
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
			<!-- td class="column"><a><bean:message
				key="oscarEncounter.formRourke2006_1.visitDate" /></a></td-->
			<td colspan="4" class="row"><a><bean:message
				key="oscarEncounter.formRourke2006_2.msg2mos" /></a></td>
			<td colspan="4" class="row"><a><bean:message
				key="oscarEncounter.formRourke2006_2.msg4mos" /></a></td>
			<td colspan="4" class="row"><a><bean:message
				key="oscarEncounter.formRourke2006_2.msg6mos" /></a></td>
		</tr>
		<tr align="center">
			<!-- td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgDate" /></a></td-->
			<td colspan="4"><input readonly type="text" id="p2_date2m"
				ondblclick="resetDate(this)" name="p2_date2m" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p2_date2m", ""))%>" />
			<img src="../images/cal.gif" id="p2_date2m_cal"></td>
			<td colspan="4"><input readonly type="text" id="p2_date4m"
				ondblclick="resetDate(this)" name="p2_date4m" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p2_date4m", ""))%>" />
			<img src="../images/cal.gif" id="p2_date4m_cal"></td>
			<td colspan="4"><input readonly type="text" id="p2_date6m"
				ondblclick="resetDate(this)" name="p2_date6m" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p2_date6m", ""))%>" />
			<img src="../images/cal.gif" id="p2_date6m_cal"></td>
		</tr>
		<tr align="center" id="growthAp2">
			<!-- td class="column" rowspan="2"><a><bean:message
                            key="oscarEncounter.formRourke1.btnGrowth" />*</a><br>
                            <bean:message
                            key="oscarEncounter.formRourke2009_1.btnGrowthmsg" />
                        </td-->
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td colspan="2"><bean:message
				key="oscarEncounter.formRourke2006_3.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td colspan="2"><bean:message
				key="oscarEncounter.formRourke2006_3.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke2006_2.formWt6m" /></td>
			<td colspan="2"><bean:message
				key="oscarEncounter.formRourke2006_3.formHdCirc" /></td>
		</tr>
		<tr align="center" id="growthBp2">
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p2_ht2m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_ht2m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p2_wt2m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_wt2m", "") %>"></td>
			<td colspan="2"><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p2_hc2m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_hc2m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p2_ht4m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_ht4m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p2_wt4m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_wt4m", "") %>"></td>
			<td colspan="2"><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p2_hc4m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_hc4m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p2_ht6m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_ht6m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p2_wt6m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_wt6m", "") %>"></td>
			<td colspan="2"><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p2_hc6m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_hc6m", "") %>"></td>
		</tr>	
		<tr align="center">
			<!-- td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.formParentalConcerns" /></a></td-->
			<td colspan="4"><textarea id="p2_pConcern2m"
				name="p2_pConcern2m" class="wide" cols="10" rows="5"><%= props.getProperty("p2_pConcern2m", "") %></textarea>
			</td>
			<td colspan="4"><textarea id="p2_pConcern4m"
				name="p2_pConcern4m" class="wide" cols="10" rows="5"><%= props.getProperty("p2_pConcern4m", "") %></textarea>
			</td>
			<td colspan="4"><textarea id="p2_pConcern6m"
				name="p2_pConcern6m" class="wide" cols="10" rows="5"><%= props.getProperty("p2_pConcern6m", "") %></textarea>
			</td>
		</tr>	
		<tr align="center" id="nutritionp2">

			<!-- td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgNutrition" />*:</a></td-->

			<td colspan="4">
			<table id="ntp21" cellpadding="0" cellspacing="0" width="100%">				
				<tr>
                                
                                            <td style="padding-right: 5pt" valign="top"><img height="15"
                                                        width="20" src="graphics/Checkmark_L.gif"></td>
                                            <td class="edcol" valign="top">X</td>
                                            <td class="edcol" valign="top"><bean:message
														key="oscarEncounter.formRourke2009.formNo" /></td>
                                    		<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td> 
                                </tr>
                                <tr>
                     <td valign="top"><input type="radio" id="p2_breastFeeding2mOk"
						name="p2_breastFeeding2mOk" onclick="onCheck(this,'p2_breastFeeding2m')"
						<%= props.getProperty("p2_breastFeeding2mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_breastFeeding2mOkConcerns"
						name="p2_breastFeeding2mOkConcerns" onclick="onCheck(this,'p2_breastFeeding2m')"
						<%= props.getProperty("p2_breastFeeding2mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_breastFeeding2mNo"
						name="p2_breastFeeding2mNo" onclick="onCheck(this,'p2_breastFeeding2m')"
						<%= props.getProperty("p2_breastFeeding2mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p2_breastFeeding2mNotDiscussed"
						name="p2_breastFeeding2mNotDiscussed" onclick="onCheck(this,'p2_breastFeeding2m')"
						<%= props.getProperty("p2_breastFeeding2mNotDiscussed", "") %>></td>
					<td><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"
						onclick="popPage('<%=resource%>n_breastFeeding');return false"><bean:message
						key="oscarEncounter.formRourke2006_1.btnBreastFeeding" /><br />
					</a><span
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.msgBreastFeedingDescr" /></span></b></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p2_formulaFeeding2mOk"
						name="p2_formulaFeeding2mOk" onclick="onCheck(this,'p2_formulaFeeding2m')"
						<%= props.getProperty("p2_formulaFeeding2mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_formulaFeeding2mOkConcerns"
						name="p2_formulaFeeding2mOkConcerns" onclick="onCheck(this,'p2_formulaFeeding2m')"
						<%= props.getProperty("p2_formulaFeeding2mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_formulaFeeding2mNo"
						name="p2_formulaFeeding2mNo" onclick="onCheck(this,'p2_formulaFeeding2m')"
						<%= props.getProperty("p2_formulaFeeding2mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p2_formulaFeeding2mNotDiscussed"
						name="p2_formulaFeeding2mNotDiscussed" onclick="onCheck(this,'p2_formulaFeeding2m')"
						<%= props.getProperty("p2_formulaFeeding2mNotDiscussed", "") %>></td>
					<td><a  href="javascript:showNotes()"
                                                onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><i><bean:message
						key="oscarEncounter.formRourke2009_2.msgFormulaFeeding2m" /></i></a></td>
				</tr>
                                
                <tr>
                	<td style="vertical-align: bottom;" colspan="5">
						<textarea id="p2_nutrition2m"
							name="p2_nutrition2m" class="wide" rows="5" cols="25"><%= props.getProperty("p2_nutrition2m", "") %></textarea>
					</td>   
				</tr>           
			</table>
			
			</td>
			<td colspan="4">
			<table id="ntp22" cellpadding="0" cellspacing="0" width="100%">
				
                                <tr>
                                            <td style="padding-right: 5pt" valign="top"><img height="15"
                                                        width="20" src="graphics/Checkmark_L.gif"></td>
                                            <td class="edcol" valign="top">X</td>
                                            <td class="edcol" valign="top"><bean:message
														key="oscarEncounter.formRourke2009.formNo" /></td>
                                    		<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
                                </tr>
                                <tr>
                    <td valign="top"><input type="radio" id="p2_breastFeeding4mOk"
						name="p2_breastFeeding4mOk" onclick="onCheck(this,'p2_breastFeeding4m')"
						<%= props.getProperty("p2_breastFeeding4mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_breastFeeding4mOkConcerns"
						name="p2_breastFeeding4mOkConcerns" onclick="onCheck(this,'p2_breastFeeding4m')"
						<%= props.getProperty("p2_breastFeeding4mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_breastFeeding4mNo"
						name="p2_breastFeeding4mNo" onclick="onCheck(this,'p2_breastFeeding4m')"
						<%= props.getProperty("p2_breastFeeding4mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p2_breastFeeding4mNotDiscussed"
						name="p2_breastFeeding4mNotDiscussed" onclick="onCheck(this,'p2_breastFeeding4m')"
						<%= props.getProperty("p2_breastFeeding4mNotDiscussed", "") %>></td>
					<td><b><a
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><bean:message
						key="oscarEncounter.formRourke2006_1.btnBreastFeeding" /></a><br />
					<span
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.msgBreastFeedingDescr" /></span></b></td>
				</tr>
				<tr>
                   	<td valign="top"><input type="radio" id="p2_formulaFeeding4mOk"
						name="p2_formulaFeeding4mOk" onclick="onCheck(this,'p2_formulaFeeding4m')"
						<%= props.getProperty("p2_formulaFeeding4mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_formulaFeeding4mOkConcerns"
						name="p2_formulaFeeding4mOkConcerns" onclick="onCheck(this,'p2_formulaFeeding4m')"
						<%= props.getProperty("p2_formulaFeeding4mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_formulaFeeding4mNo"
						name="p2_formulaFeeding4mNo" onclick="onCheck(this,'p2_formulaFeeding4m')"
						<%= props.getProperty("p2_formulaFeeding4mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p2_formulaFeeding4mNotDiscussed"
						name="p2_formulaFeeding4mNotDiscussed" onclick="onCheck(this,'p2_formulaFeeding4m')"
						<%= props.getProperty("p2_formulaFeeding4mNotDiscussed", "") %>></td>
					<td><a  href="javascript:showNotes()"
                                                onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><i><bean:message
						key="oscarEncounter.formRourke2009_2.msgFormulaFeeding4m" /></i></a></td>
				</tr>
                                
             	<tr>
             		<td style="vertical-align: bottom;" colspan="5"><textarea id="p2_nutrition4m"
						name="p2_nutrition4m" class="wide" rows="5" cols="25"><%= props.getProperty("p2_nutrition4m", "") %></textarea>
					</td>
				</tr>
			</table>
			</td>
			<td colspan="4">
			<table id="ntp23" cellpadding="0" cellspacing="0" width="100%">				
                                <tr>
                                            <td style="padding-right: 5pt" valign="top"><img height="15"
                                                        width="20" src="graphics/Checkmark_L.gif"></td>
                                            <td class="edcol" valign="top">X</td>
                                             <td class="edcol" valign="top"><bean:message
														key="oscarEncounter.formRourke2009.formNo" /></td>
                                    		<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
                                </tr>
                                <tr>
                    <td valign="top"><input type="radio" id="p2_breastFeeding6mOk"
						name="p2_breastFeeding6mOk" onclick="onCheck(this,'p2_breastFeeding6m')"
						<%= props.getProperty("p2_breastFeeding6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_breastFeeding6mOkConcerns"
						name="p2_breastFeeding6mOkConcerns" onclick="onCheck(this,'p2_breastFeeding6m')"
						<%= props.getProperty("p2_breastFeeding6mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_breastFeeding6mNo"
						name="p2_breastFeeding6mNo" onclick="onCheck(this,'p2_breastFeeding6m')"
						<%= props.getProperty("p2_breastFeeding6mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p2_breastFeeding6mNotDiscussed"
						name="p2_breastFeeding6mNotDiscussed" onclick="onCheck(this,'p2_breastFeeding6m')"
						<%= props.getProperty("p2_breastFeeding6mNotDiscussed", "") %>></td>
					<td><b><a
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><bean:message
						key="oscarEncounter.formRourke2006_1.btnBreastFeeding" /></a><br />
					<span
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_2.msgBreastFeedingDescr6m" /></span></b></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p2_formulaFeeding6mOk"
						name="p2_formulaFeeding6mOk" onclick="onCheck(this,'p2_formulaFeeding6m')"
						<%= props.getProperty("p2_formulaFeeding6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_formulaFeeding6mOkConcerns"
						name="p2_formulaFeeding6mOkConcerns" onclick="onCheck(this,'p2_formulaFeeding6m')"
						<%= props.getProperty("p2_formulaFeeding6mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_formulaFeeding6mNo"
						name="p2_formulaFeeding6mNo" onclick="onCheck(this,'p2_formulaFeeding6m')"
						<%= props.getProperty("p2_formulaFeeding6mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p2_formulaFeeding6mNotDiscussed"
						name="p2_formulaFeeding6mNotDiscussed" onclick="onCheck(this,'p2_formulaFeeding6m')"
						<%= props.getProperty("p2_formulaFeeding6mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.msgFormulaFeedingLong6m" /></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p2_bottle6mOk"
						name="p2_bottle6mOk" onclick="onCheck(this,'p2_bottle6m')"
						<%= props.getProperty("p2_bottle6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_bottle6mOkConcerns"
						name="p2_bottle6mOkConcerns" onclick="onCheck(this,'p2_bottle6m')"
						<%= props.getProperty("p2_bottle6mOkConcerns", "") %>></td>	
					<td>&nbsp;</td>				
					<td valign="top"><input type="radio" id="p2_bottle6mNotDiscussed"
						name="p2_bottle6mNotDiscussed" onclick="onCheck(this,'p2_bottle6m')"
						<%= props.getProperty("p2_bottle6mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_2.msgBottle" /></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p2_liquids6mOk"
						name="p2_liquids6mOk" onclick="onCheck(this,'p2_liquids6m')"
						<%= props.getProperty("p2_liquids6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_liquids6mOkConcerns"
						name="p2_liquids6mOkConcerns" onclick="onCheck(this,'p2_liquids6m')"
						<%= props.getProperty("p2_liquids6mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p2_liquids6mNotDiscussed"
						name="p2_liquids6mNotDiscussed" onclick="onCheck(this,'p2_liquids6m')"
						<%= props.getProperty("p2_liquids6mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.msgLiquids" /></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p2_iron6mOk"
						name="p2_iron6mOk" onclick="onCheck(this,'p2_iron6m')"
						<%= props.getProperty("p2_iron6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_iron6mOkConcerns"
						name="p2_iron6mOkConcerns" onclick="onCheck(this,'p2_iron6m')"
						<%= props.getProperty("p2_iron6mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p2_iron6mNotDiscussed"
						name="p2_iron6mNotDiscussed" onclick="onCheck(this,'p2_iron6m')"
						<%= props.getProperty("p2_iron6mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_2.msgIronFoods" /></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p2_vegFruit6mOk"
						name="p2_vegFruit6mOk" onclick="onCheck(this,'p2_vegFruit6m')"
						<%= props.getProperty("p2_vegFruit6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_vegFruit6mOkConcerns"
						name="p2_vegFruit6mOkConcerns" onclick="onCheck(this,'p2_vegFruit6m')"
						<%= props.getProperty("p2_vegFruit6mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p2_vegFruit6mNotDiscussed"
						name="p2_vegFruit6mNotDiscussed" onclick="onCheck(this,'p2_vegFruit6m')"
						<%= props.getProperty("p2_vegFruit6mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_2.msgVegFruits" /></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p2_egg6mOk"
						name="p2_egg6mOk" onclick="onCheck(this,'p2_egg6m')"
						<%= props.getProperty("p2_egg6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_egg6mOkConcerns"
						name="p2_egg6mOkConcerns" onclick="onCheck(this,'p2_egg6m')"
						<%= props.getProperty("p2_egg6mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p2_egg6mNotDiscussed"
						name="p2_egg6mNotDiscussed" onclick="onCheck(this,'p2_egg6m')"
						<%= props.getProperty("p2_egg6mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_2.msgEggWhites" /></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p2_choking6mOk"
						name="p2_choking6mOk" onclick="onCheck(this,'p2_choking6m')"
						<%= props.getProperty("p2_choking6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_choking6mOkConcerns"
						name="p2_choking6mOkConcerns" onclick="onCheck(this,'p2_choking6m')"
						<%= props.getProperty("p2_choking6mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p2_choking6mNotDiscussed"
						name="p2_choking6mNotDiscussed" onclick="onCheck(this,'p2_choking6m')"
						<%= props.getProperty("p2_choking6mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.msgChoking" />*</a></td>
				</tr>
               
                <tr align="center" style="vertical-align:bottom;">
               		<td colspan="5"><textarea id="p2_nutrition6m"
						name="p2_nutrition6m" class="wide" rows="5" cols="25"><%= props.getProperty("p2_nutrition6m", "") %></textarea>
					</td>
				</tr>
			</table>
			</td>			
		</tr>		
		<tr id="educationp2">
			<!-- td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgEducational" /></a><br />
			<br />
			<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif"><bean:message
				key="oscarEncounter.formRourke2006.msgEducationalLegend" /></td-->
			<td colspan="12">
			<table id="edt2" style="font-size: 8pt;" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td colspan="16">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="16"><bean:message
						key="oscarEncounter.formRourke2006_1.formInjuryPrev" /></td>
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
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_carSeatOk"
						name="p2_carSeatOk" onclick="onCheck(this,'p2_carSeat')"
						<%= props.getProperty("p2_carSeatOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_carSeatOkConcerns"
						name="p2_carSeatOkConcerns" onclick="onCheck(this,'p2_carSeat')"
						<%= props.getProperty("p2_carSeatOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_carSeatNotDiscussed"
						name="p2_carSeatNotDiscussed" onclick="onCheck(this,'p2_carSeat')"
						<%= props.getProperty("p2_carSeatNotDiscussed", "") %>></td>
					<td valign="top"><b><a
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><bean:message
						key="oscarEncounter.formRourke1.formCarSeat" /></a>*</b></td>
					<td valign="top"><input type="radio" id="p2_sleepPosOk"
						name="p2_sleepPosOk" onclick="onCheck(this,'p2_sleepPos')"
						<%= props.getProperty("p2_sleepPosOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_sleepPosOkConcerns"
						name="p2_sleepPosOkConcerns" onclick="onCheck(this,'p2_sleepPos')"
						<%= props.getProperty("p2_sleepPosOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_sleepPosNotDiscussed"
						name="p2_sleepPosNotDiscussed" onclick="onCheck(this,'p2_sleepPos')"
						<%= props.getProperty("p2_sleepPosNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formSleepPos" /></a></b></td>
					<td valign="top"><input type="radio" id="p2_poisonsOk"
						name="p2_poisonsOk" onclick="onCheck(this,'p2_poisons')"
						<%= props.getProperty("p2_poisonsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_poisonsOkConcerns"
						name="p2_poisonsOkConcerns" onclick="onCheck(this,'p2_poisons')"
						<%= props.getProperty("p2_poisonsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_poisonsNotDiscussed"
						name="p2_poisonsNotDiscussed" onclick="onCheck(this,'p2_poisons')"
						<%= props.getProperty("p2_poisonsNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formPoisons" /></a></b></td>
					<td valign="top"><input type="radio" id="p2_firearmSafetyOk"
						name="p2_firearmSafetyOk"
						onclick="onCheck(this,'p2_firearmSafety')"
						<%= props.getProperty("p2_firearmSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_firearmSafetyOkConcerns"
						name="p2_firearmSafetyOkConcerns"
						onclick="onCheck(this,'p2_firearmSafety')"
						<%= props.getProperty("p2_firearmSafetyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_firearmSafetyNotDiscussed"
						name="p2_firearmSafetyNotDiscussed"
						onclick="onCheck(this,'p2_firearmSafety')"
						<%= props.getProperty("p2_firearmSafetyNotDiscussed", "") %>></td>
					<td valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formFireArm" />*</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_electricOk"
						name="p2_electricOk" onclick="onCheck(this,'p2_electric')"
						<%= props.getProperty("p2_electricOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_electricOkConcerns"
						name="p2_electricOkConcerns" onclick="onCheck(this,'p2_electric')"
						<%= props.getProperty("p2_electricOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_electricNotDiscussed"
						name="p2_electricNotDiscussed" onclick="onCheck(this,'p2_electric')"
						<%= props.getProperty("p2_electricNotDiscussed", "") %>></td>
					<td valign="top"><i><bean:message
						key="oscarEncounter.formRourke2006_2.formElectric" /></i></td>
					<td valign="top"><input type="radio" id="p2_smokeSafetyOk"
						name="p2_smokeSafetyOk" onclick="onCheck(this,'p2_smokeSafety')"
						<%= props.getProperty("p2_smokeSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_smokeSafetyOkConcerns"
						name="p2_smokeSafetyOkConcerns" onclick="onCheck(this,'p2_smokeSafety')"
						<%= props.getProperty("p2_smokeSafetyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_smokeSafetyNotDiscussed"
						name="p2_smokeSafetyNotDiscussed" onclick="onCheck(this,'p2_smokeSafety')"
						<%= props.getProperty("p2_smokeSafetyNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSmokeSafety" />*</a></td>
					<td valign="top"><input type="radio" id="p2_hotWaterOk"
						name="p2_hotWaterOk" onclick="onCheck(this,'p2_hotWater')"
						<%= props.getProperty("p2_hotWaterOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_hotWaterOkConcerns"
						name="p2_hotWaterOkConcerns" onclick="onCheck(this,'p2_hotWater')"
						<%= props.getProperty("p2_hotWaterOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_hotWaterNotDiscussed"
						name="p2_hotWaterNotDiscussed" onclick="onCheck(this,'p2_hotWater')"
						<%= props.getProperty("p2_hotWaterNotDiscussed", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_2.formHotWater" />*</a></i></td>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_fallsOk"
						name="p2_fallsOk" onclick="onCheck(this,'p2_falls')"
						<%= props.getProperty("p2_fallsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_fallsOkConcerns"
						name="p2_fallsOkConcerns" onclick="onCheck(this,'p2_falls')"
						<%= props.getProperty("p2_fallsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_fallsNotDiscussed"
						name="p2_fallsNotDiscussed" onclick="onCheck(this,'p2_falls')"
						<%= props.getProperty("p2_fallsNotDiscussed", "") %>></td>
					<td colspan="5" valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formFalls" />*</a></i></td>
					<td valign="top"><input type="radio" id="p2_safeToysOk"
						name="p2_safeToysOk" onclick="onCheck(this,'p2_safeToys')"
						<%= props.getProperty("p2_safeToysOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_safeToysOkConcerns"
						name="p2_safeToysOkConcerns" onclick="onCheck(this,'p2_safeToys')"
						<%= props.getProperty("p2_safeToysOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_safeToysNotDiscussed"
						name="p2_safeToysNotDiscussed" onclick="onCheck(this,'p2_safeToys')"
						<%= props.getProperty("p2_safeToysNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSafeToys" />*</a></td>
					<td colspan="8">&nbsp;</td>
				</tr>
				
				<!-- </table>
          <br/>
          <table style="font-size:9pt;" cellpadding="0" cellspacing="0" width="100%"  >                    
          -->
				<tr>
					<td colspan="16">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="16"><bean:message
						key="oscarEncounter.formRourke2006_1.formBehaviour" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_sleepCryOk"
						name="p2_sleepCryOk" onclick="onCheck(this,'p2_sleepCry')"
						<%= props.getProperty("p2_sleepCryOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_sleepCryOkConcerns"
						name="p2_sleepCryOkConcerns" onclick="onCheck(this,'p2_sleepCry')"
						<%= props.getProperty("p2_sleepCryOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_sleepCryNotDiscussed"
						name="p2_sleepCryNotDiscussed" onclick="onCheck(this,'p2_sleepCry')"
						<%= props.getProperty("p2_sleepCryNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formsleepCry" />**</a></td>
					<td valign="top"><input type="radio" id="p2_soothabilityOk"
						name="p2_soothabilityOk" onclick="onCheck(this,'p2_soothability')"
						<%= props.getProperty("p2_soothabilityOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_soothabilityOkConcerns"
						name="p2_soothabilityOkConcerns" onclick="onCheck(this,'p2_soothability')"
						<%= props.getProperty("p2_soothabilityOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_soothabilityNotDiscussed"
						name="p2_soothabilityNotDiscussed" onclick="onCheck(this,'p2_soothability')"
						<%= props.getProperty("p2_soothabilityNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formSoothability" /></td>
					<td valign="top"><input type="radio" id="p2_homeVisitOk"
						name="p2_homeVisitOk" onclick="onCheck(this,'p2_homeVisit')"
						<%= props.getProperty("p2_homeVisitOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_homeVisitOkConcerns"
						name="p2_homeVisitOkConcerns" onclick="onCheck(this,'p2_homeVisit')"
						<%= props.getProperty("p2_homeVisitOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_homeVisitNotDiscussed"
						name="p2_homeVisitNotDiscussed" onclick="onCheck(this,'p2_homeVisit')"
						<%= props.getProperty("p2_homeVisitNotDiscussed", "") %>></td>
					<td colspan="7" valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_2.formHomeVisit" />**</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_bondingOk"
						name="p2_bondingOk" onclick="onCheck(this,'p2_bonding')"
						<%= props.getProperty("p2_bondingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_bondingOkConcerns"
						name="p2_bondingOkConcerns" onclick="onCheck(this,'p2_bonding')"
						<%= props.getProperty("p2_bondingOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_bondingNotDiscussed"
						name="p2_bondingNotDiscussed" onclick="onCheck(this,'p2_bonding')"
						<%= props.getProperty("p2_bondingNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formBonding" /></td>
					<td valign="top"><input type="radio" id="p2_pFatigueOk"
						name="p2_pFatigueOk" onclick="onCheck(this,'p2_pFatigue')"
						<%= props.getProperty("p2_pFatigueOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_pFatigueOkConcerns"
						name="p2_pFatigueOkConcerns" onclick="onCheck(this,'p2_pFatigue')"
						<%= props.getProperty("p2_pFatigueOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_pFatigueNotDiscussed"
						name="p2_pFatigueNotDiscussed" onclick="onCheck(this,'p2_pFatigue')"
						<%= props.getProperty("p2_pFatigueNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formParentFatigue" />**</a></td>
					<td valign="top"><input type="radio" id="p2_famConflictOk"
						name="p2_famConflictOk" onclick="onCheck(this,'p2_famConflict')"
						<%= props.getProperty("p2_famConflictOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_famConflictOkConcerns"
						name="p2_famConflictOkConcerns" onclick="onCheck(this,'p2_famConflict')"
						<%= props.getProperty("p2_famConflictOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_famConflictNotDiscussed"
						name="p2_famConflictNotDiscussed" onclick="onCheck(this,'p2_famConflict')"
						<%= props.getProperty("p2_famConflictNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formFamConflict" /></td>
					<td valign="top"><input type="radio" id="p2_siblingsOk"
						name="p2_siblingsOk" onclick="onCheck(this,'p2_siblings')"
						<%= props.getProperty("p2_siblingsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_siblingsOkConcerns"
						name="p2_siblingsOkConcerns" onclick="onCheck(this,'p2_siblings')"
						<%= props.getProperty("p2_siblingsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_siblingsNotDiscussed"
						name="p2_siblingsNotDiscussed" onclick="onCheck(this,'p2_siblings')"
						<%= props.getProperty("p2_siblingsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formSiblings" /></td>
					<td valign="top"><input type="radio" id="p2_childCareOk"
						name="p2_childCareOk" onclick="onCheck(this,'p2_childCare')"
						<%= props.getProperty("p2_childCareOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_childCareOkConcerns"
						name="p2_childCareOkConcerns" onclick="onCheck(this,'p2_childCare')"
						<%= props.getProperty("p2_childCareOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_childCareNotDiscussed"
						name="p2_childCareNotDiscussed" onclick="onCheck(this,'p2_childCare')"
						<%= props.getProperty("p2_childCareNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
                                                onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><i><bean:message
						key="oscarEncounter.formRourke2009_2.formChildCare" /></i></a></td>
				</tr>
				
				<!-- </table>
          <br/>
          <table style="font-size:9pt;" cellpadding="0" cellspacing="0" width="100%"  >
             -->
				<tr>
					<td colspan="20">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="20"><bean:message
						key="oscarEncounter.formRourke2006_1.formOtherIssues" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_2ndSmokeOk"
						name="p2_2ndSmokeOk" onclick="onCheck(this,'p2_2ndSmoke')"
						<%= props.getProperty("p2_2ndSmokeOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_2ndSmokeOkConcerns"
						name="p2_2ndSmokeOkConcerns" onclick="onCheck(this,'p2_2ndSmoke')"
						<%= props.getProperty("p2_2ndSmokeOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_2ndSmokeNotDiscussed"
						name="p2_2ndSmokeNotDiscussed" onclick="onCheck(this,'p2_2ndSmoke')"
						<%= props.getProperty("p2_2ndSmokeNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formSecondHandSmoke" />*</a></b></td>
					<td valign="top"><input type="radio" id="p2_teethingOk"
						name="p2_teethingOk" onclick="onCheck(this,'p2_teething')"
						<%= props.getProperty("p2_teethingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_teethingOkConcerns"
						name="p2_teethingOkConcerns" onclick="onCheck(this,'p2_teething')"
						<%= props.getProperty("p2_teethingOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_teethingNotDiscussed"
						name="p2_teethingNotDiscussed" onclick="onCheck(this,'p2_teething')"
						<%= props.getProperty("p2_teethingNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formTeething" />*</a></b></td>
					<td valign="top"><input type="radio" id="p2_altMedOk"
						name="p2_altMedOk" onclick="onCheck(this,'p2_altMed')"
						<%= props.getProperty("p2_altMedOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_altMedOkConcerns"
						name="p2_altMedOkConcerns" onclick="onCheck(this,'p2_altMed')"
						<%= props.getProperty("p2_altMedOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_altMedNotDiscussed"
						name="p2_altMedNotDiscussed" onclick="onCheck(this,'p2_altMed')"
						<%= props.getProperty("p2_altMedNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_2.formAltMed" />*</a></td>
					<td valign="top"><input type="radio" id="p2_pacifierOk"
						name="p2_pacifierOk" onclick="onCheck(this,'p2_pacifier')"
						<%= props.getProperty("p2_pacifierOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_pacifierOkConcerns"
						name="p2_pacifierOkConcerns" onclick="onCheck(this,'p2_pacifier')"
						<%= props.getProperty("p2_pacifierOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_pacifierNotDiscussed"
						name="p2_pacifierNotDiscussed" onclick="onCheck(this,'p2_pacifier')"
						<%= props.getProperty("p2_pacifierNotDiscussed", "") %>></td>
					<td colspan="5" valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formPacifierUse" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_tmpControlOk"
						name="p2_tmpControlOk" onclick="onCheck(this,'p2_tmpControl')"
						<%= props.getProperty("p2_tmpControlOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_tmpControlOkConcerns"
						name="p2_tmpControlOkConcerns" onclick="onCheck(this,'p2_tmpControl')"
						<%= props.getProperty("p2_tmpControlOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_tmpControlNotDiscussed"
						name="p2_tmpControlNotDiscussed" onclick="onCheck(this,'p2_tmpControl')"
						<%= props.getProperty("p2_tmpControlNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formTempCtrl" />*</a></td>
					<td valign="top"><input type="radio" id="p2_feverOk"
						name="p2_feverOk" onclick="onCheck(this,'p2_fever')"
						<%= props.getProperty("p2_feverOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_feverOkConcerns"
						name="p2_feverOkConcerns" onclick="onCheck(this,'p2_fever')"
						<%= props.getProperty("p2_feverOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_feverNotDiscussed"
						name="p2_feverNotDiscussed" onclick="onCheck(this,'p2_fever')"
						<%= props.getProperty("p2_feverNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formFever" />*</a></td>
					<td valign="top"><input type="radio" id="p2_sunExposureOk"
						name="p2_sunExposureOk" onclick="onCheck(this,'p2_sunExposure')"
						<%= props.getProperty("p2_sunExposureOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_sunExposureOkConcerns"
						name="p2_sunExposureOkConcerns" onclick="onCheck(this,'p2_sunExposure')"
						<%= props.getProperty("p2_sunExposureOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_sunExposureNotDiscussed"
						name="p2_sunExposureNotDiscussed" onclick="onCheck(this,'p2_sunExposure')"
						<%= props.getProperty("p2_sunExposureNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSunExposure" />*</a></td>
					<td valign="top"><input type="radio" id="p2_pesticidesOk"
						name="p2_pesticidesOk" onclick="onCheck(this,'p2_pesticides')"
						<%= props.getProperty("p2_pesticidesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_pesticidesOkConcerns"
						name="p2_pesticidesOkConcerns" onclick="onCheck(this,'p2_pesticides')"
						<%= props.getProperty("p2_pesticidesOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_pesticidesNotDiscussed"
						name="p2_pesticidesNotDiscussed" onclick="onCheck(this,'p2_pesticides')"
						<%= props.getProperty("p2_pesticidesNotDiscussed", "") %>></td>
					<td colspan="5" valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formPesticides" />*</a></i></td>
				</tr>
                <tr>
					<td valign="top"><input type="radio" id="p2_readingOk"
						name="p2_readingOk" onclick="onCheck(this,'p2_reading')"
						<%= props.getProperty("p2_readingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_readingOkConcerns"
						name="p2_readingOkConcerns" onclick="onCheck(this,'p2_reading')"
						<%= props.getProperty("p2_readingOkConcerns", "") %>></td>
                    <td valign="top"><input type="radio" id="p2_readingNotDiscussed"
						name="p2_readingNotDiscussed" onclick="onCheck(this,'p2_reading')"
						<%= props.getProperty("p2_readingNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_2.formReading" />**</a></td>
                    <td valign="top"><input type="radio" id="p2_noCoughMedOk"
						name="p2_noCoughMedOk" onclick="onCheck(this,'p2_noCoughMed')"
						<%= props.getProperty("p2_noCoughMedOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_noCoughMedOkConcerns"
						name="p2_noCoughMedOkConcerns" onclick="onCheck(this,'p2_noCoughMed')"
						<%= props.getProperty("p2_noCoughMedOkConcerns", "") %>></td>
                    <td valign="top"><input type="radio" id="p2_noCoughMedNotDiscussed"
						name="p2_noCoughMedNotDiscussed" onclick="onCheck(this,'p2_noCoughMed')"
						<%= props.getProperty("p2_noCoughMedNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_1.formCough" />*</a></td>
                                        <td colspan="12">&nbsp;</td>
				
				<tr>
					<td colspan="20">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="20" style="vertical-align:bottom;">
						<textarea id="p2_education"
							name="p2_education" class="wide" rows="5"><%= props.getProperty("p2_education", "") %></textarea>					
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr id="developmentp2">
			<!-- td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgDevelopment" />**</a><br>
			<bean:message
				key="oscarEncounter.formRourke2009_1.msgDevelopmentDesc" /><br />
			<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif"><bean:message
				key="oscarEncounter.formRourke2006_1.msgDevelopmentLegend" /></td-->
			<td colspan="4" align="center">
			<table id="dt21" cellpadding="0" cellspacing="0" width="100%">				
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_eyesMoveOk"
						name="p2_eyesOk" onclick="onCheck(this,'p2_eyesMove')"
						<%= props.getProperty("p2_eyesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_eyesMoveOkConcerns"
						name="p2_eyesOkConcerns" onclick="onCheck(this,'p2_eyesMove')"
						<%= props.getProperty("p2_eyesOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_eyesMoveNotDiscussed"
						name="p2_eyesNotDiscussed" onclick="onCheck(this,'p2_eyesMove')"
						<%= props.getProperty("p2_eyesNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_2.formEyesMove" /></td>
				</tr>
				
                <tr>
					<td valign="top"><input type="radio" id="p2_coosOk"
						name="p2_coosOk" onclick="onCheck(this,'p2_coos')"
						<%= props.getProperty("p2_coosOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_coosOkConcerns"
						name="p2_coosOkConcerns" onclick="onCheck(this,'p2_coos')"
						<%= props.getProperty("p2_coosOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_coosNotDiscussed"
						name="p2_coosNotDiscussed" onclick="onCheck(this,'p2_coos')"
						<%= props.getProperty("p2_coosNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.formCoos" /></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio" id="p2_headUpTummyOk"
						name="p2_headUpTummyOk" onclick="onCheck(this,'p2_headUpTummy')"
						<%= props.getProperty("p2_headUpTummyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_headUpTummyOkConcerns"
						name="p2_headUpTummyOkConcerns" onclick="onCheck(this,'p2_headUpTummy')"
						<%= props.getProperty("p2_headUpTummyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_headUpTummyNotDiscussed"
						name="p2_headUpTummyNotDiscussed" onclick="onCheck(this,'p2_headUpTummy')"
						<%= props.getProperty("p2_headUpTummyNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.formHeadUp" /></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio" id="p2_cuddledOk"
						name="p2_cuddledOk" onclick="onCheck(this,'p2_cuddled')"
						<%= props.getProperty("p2_cuddledOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_cuddledOkConcerns"
						name="p2_cuddledOkConcerns" onclick="onCheck(this,'p2_cuddled')"
						<%= props.getProperty("p2_cuddledOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_cuddledNotDiscussed"
						name="p2_cuddledNotDiscussed" onclick="onCheck(this,'p2_cuddled')"
						<%= props.getProperty("p2_cuddledNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.formCuddled" /></td>
				</tr>
				
                <tr>
					<td valign="top"><input type="radio" id="p2_2sucksOk"
						name="p2_2sucksOk" onclick="onCheck(this,'p2_2sucks')"
						<%= props.getProperty("p2_2sucksOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_2sucksOkConcerns"
						name="p2_2sucksOkConcerns" onclick="onCheck(this,'p2_2sucks')"
						<%= props.getProperty("p2_2sucksOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_2sucksNotDiscussed"
						name="p2_2sucksNotDiscussed" onclick="onCheck(this,'p2_2sucks')"
						<%= props.getProperty("p2_2sucksNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.form2sucks" /></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio" id="p2_smilesOk"
						name="p2_smilesOk" onclick="onCheck(this,'p2_smiles')"
						<%= props.getProperty("p2_smilesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_smilesOkConcerns"
						name="p2_smilesOkConcerns" onclick="onCheck(this,'p2_smiles')"
						<%= props.getProperty("p2_smilesOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_smilesNotDiscussed"
						name="p2_smilesNotDiscussed" onclick="onCheck(this,'p2_smiles')"
						<%= props.getProperty("p2_smilesNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_2.formSmiles" /></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns2mOk" name="p2_noParentsConcerns2mOk"
						onclick="onCheck(this,'p2_noParentsConcerns2m')"
						<%= props.getProperty("p2_noParentsConcerns2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns2mOkConcerns" name="p2_noParentsConcerns2mOkConcerns"
						onclick="onCheck(this,'p2_noParentsConcerns2m')"
						<%= props.getProperty("p2_noParentsConcerns2mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns2mNotDiscussed" name="p2_noParentsConcerns2mNotDiscussed"
						onclick="onCheck(this,'p2_noParentsConcerns2m')"
						<%= props.getProperty("p2_noParentsConcerns2mNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
				</tr>
				
				<tr align="center">
					<td colspan="4" style="vertical-align:bottom;"><textarea id="p2_development2m"
						name="p2_development2m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_development2m", "") %></textarea></td>
				</tr>
			</table>
			</td>
			<td colspan="4" align="center">
			<table id="dt22" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
                <tr>
					<td valign="top"><input type="radio" id="p2_movingObjOk"
						name="p2_movingObjOk" onclick="onCheck(this,'p2_movingObj')"
						<%= props.getProperty("p2_movingObjOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_movingObjOkConcerns"
						name="p2_movingObjOkConcerns" onclick="onCheck(this,'p2_movingObj')"
						<%= props.getProperty("p2_movingObjOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_movingObjNotDiscussed"
						name="p2_movingObjNotDiscussed" onclick="onCheck(this,'p2_movingObj')"
						<%= props.getProperty("p2_movingObjNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.formMovingObj" /></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio" id="p2_respondsOk"
						name="p2_respondsOk" onclick="onCheck(this,'p2_responds')"
						<%= props.getProperty("p2_respondsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_respondsOkConcerns"
						name="p2_respondsOkConcerns" onclick="onCheck(this,'p2_responds')"
						<%= props.getProperty("p2_respondsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_respondsNotDiscussed"
						name="p2_respondsNotDiscussed" onclick="onCheck(this,'p2_responds')"
						<%= props.getProperty("p2_respondsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.formResponds" /></td>
				</tr>
				
                <tr>
					<td valign="top"><input type="radio" id="p2_headSteadyOk"
						name="p2_headSteadyOk" onclick="onCheck(this,'p2_headSteady')"
						<%= props.getProperty("p2_headSteadyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_headSteadyOkConcerns"
						name="p2_headSteadyOkConcerns" onclick="onCheck(this,'p2_headSteady')"
						<%= props.getProperty("p2_headSteadyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_headSteadyNotDiscussed"
						name="p2_headSteadyNotDiscussed" onclick="onCheck(this,'p2_headSteady')"
						<%= props.getProperty("p2_headSteadyNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.formHeadSteady" /></td>
				</tr>
				
                <tr>
					<td valign="top"><input type="radio" id="p2_holdsObjOk"
						name="p2_holdsObjOk" onclick="onCheck(this,'p2_holdsObj')"
						<%= props.getProperty("p2_holdsObjOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_holdsObjOkConcerns"
						name="p2_holdsObjOkConcerns" onclick="onCheck(this,'p2_holdsObj')"
						<%= props.getProperty("p2_holdsObjOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_holdsObjNotDiscussed"
						name="p2_holdsObjNotDiscussed" onclick="onCheck(this,'p2_holdsObj')"
						<%= props.getProperty("p2_holdsObjNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.formholdsObj" /></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio" id="p2_laughsOk"
						name="p2_laughsOk" onclick="onCheck(this,'p2_laughs')"
						<%= props.getProperty("p2_laughsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_laughsOkConcerns"
						name="p2_laughsOkConcerns" onclick="onCheck(this,'p2_laughs')"
						<%= props.getProperty("p2_laughsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_laughsNotDiscussed"
						name="p2_laughsNotDiscussed" onclick="onCheck(this,'p2_laughs')"
						<%= props.getProperty("p2_laughsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.formLaughs" /></td>
				</tr>
										
				<tr>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns4mOk" name="p2_noParentsConcerns4mOk"
						onclick="onCheck(this,'p2_noParentsConcerns4m')"
						<%= props.getProperty("p2_noParentsConcerns4mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns4mOkConcerns" name="p2_noParentsConcerns4mOkConcerns"
						onclick="onCheck(this,'p2_noParentsConcerns4m')"
						<%= props.getProperty("p2_noParentsConcerns4mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns4mNotDiscussed" name="p2_noParentsConcerns4mNotDiscussed"
						onclick="onCheck(this,'p2_noParentsConcerns4m')"
						<%= props.getProperty("p2_noParentsConcerns4mNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
				</tr>
				
				<tr align="center">
					<td colspan="4" style="vertical-align:bottom;"><textarea id="p2_development4m"
						name="p2_development4m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_development4m", "") %></textarea></td>
				</tr>				
			</table>
			</td>
			<td colspan="4">
			<table id="dt23" cellpadding="0" cellspacing="0" width="100%">				
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
                <tr>
					<td valign="top"><input type="radio" id="p2_turnsHeadOk"
						name="p2_turnsHeadOk" onclick="onCheck(this,'p2_turnsHead')"
						<%= props.getProperty("p2_turnsHeadOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_turnsHeadOkConcerns"
						name="p2_turnsHeadOkConcerns" onclick="onCheck(this,'p2_turnsHead')"
						<%= props.getProperty("p2_turnsHeadOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_turnsHeadNotDiscussed"
						name="p2_turnsHeadNotDiscussed" onclick="onCheck(this,'p2_turnsHead')"
						<%= props.getProperty("p2_turnsHeadNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_2.formTurnsHead" /></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio" id="p2_makesSoundOk"
						name="p2_makesSoundOk" onclick="onCheck(this,'p2_makesSound')"
						<%= props.getProperty("p2_makesSoundOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_makesSoundOkConcerns"
						name="p2_makesSoundOkConcerns" onclick="onCheck(this,'p2_makesSound')"
						<%= props.getProperty("p2_makesSoundOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_makesSoundNotDiscussed"
						name="p2_makesSoundNotDiscussed" onclick="onCheck(this,'p2_makesSound')"
						<%= props.getProperty("p2_makesSoundNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.formmakesSound" /></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio" id="p2_vocalizesOk"
						name="p2_vocalizesOk" onclick="onCheck(this,'p2_vocalizes')"
						<%= props.getProperty("p2_vocalizesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_vocalizesOkConcerns"
						name="p2_vocalizesOkConcerns" onclick="onCheck(this,'p2_vocalizes')"
						<%= props.getProperty("p2_vocalizesOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_vocalizesNotDiscussed"
						name="p2_vocalizesNotDiscussed" onclick="onCheck(this,'p2_vocalizes')"
						<%= props.getProperty("p2_vocalizesNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.formVocalizes" /></td>
				</tr>
				
                <tr>
					<td valign="top"><input type="radio" id="p2_rollsOk"
						name="p2_rollsOk" onclick="onCheck(this,'p2_rolls')"
						<%= props.getProperty("p2_rollsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_rollsOkConcerns"
						name="p2_rollsOkConcerns" onclick="onCheck(this,'p2_rolls')"
						<%= props.getProperty("p2_rollsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_rollsNotDiscussed"
						name="p2_rollsNotDiscussed" onclick="onCheck(this,'p2_rolls')"
						<%= props.getProperty("p2_rollsNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.formRolls" /></td>
				</tr>
											
				<tr>
					<td valign="top"><input type="radio" id="p2_sitsOk"
						name="p2_sitsOk" onclick="onCheck(this,'p2_sits')"
						<%= props.getProperty("p2_sitsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_sitsOkConcerns"
						name="p2_sitsOkConcerns" onclick="onCheck(this,'p2_sits')"
						<%= props.getProperty("p2_sitsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_sitsNotDiscussed"
						name="p2_sitsNotDiscussed" onclick="onCheck(this,'p2_sits')"
						<%= props.getProperty("p2_sitsNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.formSits" /></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio" id="p2_reachesGraspsOk"
						name="p2_reachesGraspsOk" onclick="onCheck(this,'p2_reachesGrasps')"
						<%= props.getProperty("p2_reachesGraspsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_reachesGraspsOkConcerns"
						name="p2_reachesGraspsOkConcerns" onclick="onCheck(this,'p2_reachesGrasps')"
						<%= props.getProperty("p2_reachesGraspsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_reachesGraspsNotDiscussed"
						name="p2_reachesGraspsNotDiscussed" onclick="onCheck(this,'p2_reachesGrasps')"
						<%= props.getProperty("p2_reachesGraspsNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.formreachesGrasps" /></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns6mOk" name="p2_noParentsConcerns6mOk"
						onclick="onCheck(this,'p2_noParentsConcerns6m')"
						<%= props.getProperty("p2_noParentsConcerns6mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns6mOkConcerns" name="p2_noParentsConcerns6mOkConcerns"
						onclick="onCheck(this,'p2_noParentsConcerns6m')"
						<%= props.getProperty("p2_noParentsConcerns6mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns6mNotDiscussed" name="p2_noParentsConcerns6mNotDiscussed"
						onclick="onCheck(this,'p2_noParentsConcerns6m')"
						<%= props.getProperty("p2_noParentsConcerns6mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
				</tr>
				
				<tr align="center">
					<td colspan="4" style="vertical-align:bottom;"><textarea id="p2_development6m"
						name="p2_development6m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_development6m", "") %></textarea></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr id="physicalExamp2">
			<!-- td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgPhysicalExamination" /></a><br>
			<bean:message
				key="oscarEncounter.formRourke1.msgPhysicalExaminationDesc" /><br>
			<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif"><bean:message
				key="oscarEncounter.formRourke2009.msgPhysicalExaminationLegend" />
			</td-->
			<td colspan="4">
			<table id="pt21" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td colspan="3">&nbsp;</td>
				</tr>
                                <tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
                <tr>
					<td valign="top"><input type="radio"
						id="p2_fontanelles2mOk" name="p2_fontanelles2mOk"
						onclick="onCheck(this,'p2_fontanelles2m')"
						<%= props.getProperty("p2_fontanelles2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_fontanelles2mOkConcerns" name="p2_fontanelles2mOkConcerns"
						onclick="onCheck(this,'p2_fontanelles2m')"
						<%= props.getProperty("p2_fontanelles2mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_fontanelles2mNotDiscussed" name="p2_fontanelles2mNotDiscussed"
						onclick="onCheck(this,'p2_fontanelles2m')"
						<%= props.getProperty("p2_fontanelles2mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				
                <tr>
					<td valign="top"><input type="radio"
						id="p2_eyes2mOk" name="p2_eyes2mOk"
						onclick="onCheck(this,'p2_eyes2m')"
						<%= props.getProperty("p2_eyes2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_eyes2mOkConcerns" name="p2_eyes2mOkConcerns"
						onclick="onCheck(this,'p2_eyes2m')"
						<%= props.getProperty("p2_eyes2mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_eyes2mNotDiscussed" name="p2_eyes2mNotDiscussed"
						onclick="onCheck(this,'p2_eyes2m')"
						<%= props.getProperty("p2_eyes2mNotDiscussed", "") %>></td>
					<td><b><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke1.formRedReflex"/>*</a></b></td>
				</tr>
				
                <tr>
					<td valign="top"><input type="radio"
						id="p2_corneal2mOk" name="p2_corneal2mOk"
						onclick="onCheck(this,'p2_corneal2m')"
						<%= props.getProperty("p2_corneal2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_corneal2mOkConcerns" name="p2_corneal2mOkConcerns"
						onclick="onCheck(this,'p2_corneal2m')"
						<%= props.getProperty("p2_corneal2mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_corneal2mNotDiscussed" name="p2_corneal2mNotDiscussed"
						onclick="onCheck(this,'p2_corneal2m')"
						<%= props.getProperty("p2_corneal2mNotDiscussed", "") %>></td>
					<td><b><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formCornealReflex"/>*</a></b></td>
				</tr>
				
                <tr>
					<td valign="top"><input type="radio"
						id="p2_hearing2mOk" name="p2_hearing2mOk"
						onclick="onCheck(this,'p2_hearing2m')"
						<%= props.getProperty("p2_hearing2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hearing2mOkConcerns" name="p2_hearing2mOkConcerns"
						onclick="onCheck(this,'p2_hearing2m')"
						<%= props.getProperty("p2_hearing2mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hearing2mNotDiscussed" name="p2_hearing2mNotDiscussed"
						onclick="onCheck(this,'p2_hearing2m')"
						<%= props.getProperty("p2_hearing2mNotDiscussed", "") %>></td>
					<td><i><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formHearingInquiry"/>*</a></i></td>
				</tr>
				
               	<tr>
					<td valign="top"><input type="radio"
						id="p2_heart2mOk" name="p2_heart2mOk"
						onclick="onCheck(this,'p2_heart2m')"
						<%= props.getProperty("p2_heart2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_heart2mOkConcerns" name="p2_heart2mOkConcerns"
						onclick="onCheck(this,'p2_heart2m')"
						<%= props.getProperty("p2_heart2mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_heart2mNotDiscussed" name="p2_heart2mNotDiscussed"
						onclick="onCheck(this,'p2_heart2m')"
						<%= props.getProperty("p2_heart2mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_2.formHeart" /></td>
				</tr>
				
                <tr>
					<td valign="top"><input type="radio"
						id="p2_hips2mOk" name="p2_hips2mOk"
						onclick="onCheck(this,'p2_hips2m')"
						<%= props.getProperty("p2_hips2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hips2mOkConcerns" name="p2_hips2mOkConcerns"
						onclick="onCheck(this,'p2_hips2m')"
						<%= props.getProperty("p2_hips2mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hips2mNotDiscussed" name="p2_hips2mNotDiscussed"
						onclick="onCheck(this,'p2_hips2m')"
						<%= props.getProperty("p2_hips2mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_2.formHips"/>*</a></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio"
						id="p2_muscleTone2mOk" name="p2_muscleTone2mOk"
						onclick="onCheck(this,'p2_muscleTone2m')"
						<%= props.getProperty("p2_muscleTone2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_muscleTone2mOkConcerns" name="p2_muscleTone2mOkConcerns"
						onclick="onCheck(this,'p2_muscleTone2m')"
						<%= props.getProperty("p2_muscleTone2mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_muscleTone2mNotDiscussed" name="p2_muscleTone2mNotDiscussed"
						onclick="onCheck(this,'p2_muscleTone2m')"
						<%= props.getProperty("p2_muscleTone2mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formMuscleTone"/>*</a></td>
				</tr>
						
				<tr>
					<td colspan="4" style="vertical-align:bottom;">
						<textarea id="p2_physical2m"
							name="p2_physical2m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_physical2m", "") %></textarea>
						
					</td>
				</tr>		
			</table>
			</td>
			<td colspan="4">
			<table id="pt22" cellpadding="0" cellspacing="0" width="100%">
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
						id="p2_fontanelles4mOk" name="p2_fontanelles4mOk"
						onclick="onCheck(this,'p2_fontanelles4m')"
						<%= props.getProperty("p2_fontanelles4mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_fontanelles4mOkConcerns" name="p2_fontanelles4mOkConcerns"
						onclick="onCheck(this,'p2_fontanelles4m')"
						<%= props.getProperty("p2_fontanelles4mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_fontanelles4mNotDiscussed" name="p2_fontanelles4mNotDiscussed"
						onclick="onCheck(this,'p2_fontanelles4m')"
						<%= props.getProperty("p2_fontanelles4mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				
                <tr>
					<td valign="top"><input type="radio"
						id="p2_eyes4mOk" name="p2_eyes4mOk"
						onclick="onCheck(this,'p2_eyes4m')"
						<%= props.getProperty("p2_eyes4mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_eyes4mOkConcerns" name="p2_eyes4mOkConcerns"
						onclick="onCheck(this,'p2_eyes4m')"
						<%= props.getProperty("p2_eyes4mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_eyes4mNotDiscussed" name="p2_eyes4mNotDiscussed"
						onclick="onCheck(this,'p2_eyes4m')"
						<%= props.getProperty("p2_eyes4mNotDiscussed", "") %>></td>
					<td><b><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke1.formRedReflex"/>*</a></b></td>
				</tr>
				
               	<tr>
					<td valign="top"><input type="radio"
						id="p2_corneal4mOk" name="p2_corneal4mOk"
						onclick="onCheck(this,'p2_corneal4m')"
						<%= props.getProperty("p2_corneal4mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_corneal4mOkConcerns" name="p2_corneal4mOkConcerns"
						onclick="onCheck(this,'p2_corneal4m')"
						<%= props.getProperty("p2_corneal4mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_corneal4mNotDiscussed" name="p2_corneal4mNotDiscussed"
						onclick="onCheck(this,'p2_corneal4m')"
						<%= props.getProperty("p2_corneal4mNotDiscussed", "") %>></td>
					<td><b><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formCornealReflex"/>*</a></b></td>
				</tr>
				
                <tr>
					<td valign="top"><input type="radio"
						id="p2_hearing4mOk" name="p2_hearing4mOk"
						onclick="onCheck(this,'p2_hearing4m')"
						<%= props.getProperty("p2_hearing4mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hearing4mOkConcerns" name="p2_hearing4mOkConcerns"
						onclick="onCheck(this,'p2_hearing4m')"
						<%= props.getProperty("p2_hearing4mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hearing4mNotDiscussed" name="p2_hearing4mNotDiscussed"
						onclick="onCheck(this,'p2_hearing4m')"
						<%= props.getProperty("p2_hearing4mNotDiscussed", "") %>></td>
					<td><i><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formHearingInquiry"/>*</a></i></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio"
						id="p2_hips4mOk" name="p2_hips4mOk"
						onclick="onCheck(this,'p2_hips4m')"
						<%= props.getProperty("p2_hips4mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hips4mOkConcerns" name="p2_hips4mOkConcerns"
						onclick="onCheck(this,'p2_hips4m')"
						<%= props.getProperty("p2_hips4mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hips4mNotDiscussed" name="p2_hips4mNotDiscussed"
						onclick="onCheck(this,'p2_hips4m')"
						<%= props.getProperty("p2_hips4mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_2.formHips"/>*</a></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio"
						id="p2_muscleTone4mOk" name="p2_muscleTone4mOk"
						onclick="onCheck(this,'p2_muscleTone4m')"
						<%= props.getProperty("p2_muscleTone4mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_muscleTone4mOkConcerns" name="p2_muscleTone4mOkConcerns"
						onclick="onCheck(this,'p2_muscleTone4m')"
						<%= props.getProperty("p2_muscleTone4mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_muscleTone4mNotDiscussed" name="p2_muscleTone4mNotDiscussed"
						onclick="onCheck(this,'p2_muscleTone4m')"
						<%= props.getProperty("p2_muscleTone4mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formMuscleTone"/>*</a></td>
				</tr>
               	
				<tr>
					<td colspan="4" style="vertical-align:bottom;">
						<textarea id="p2_physical4m"
							name="p2_physical4m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_physical4m", "") %></textarea>						
					</td>
				</tr>
			</table>
			</td>
			<td colspan="4">				
			<table id="pt23" cellpadding="0" cellspacing="0" width="100%">
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
						id="p2_fontanelles6mOk" name="p2_fontanelles6mOk"
						onclick="onCheck(this,'p2_fontanelles6m')"
						<%= props.getProperty("p2_fontanelles6mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_fontanelles6mOkConcerns" name="p2_fontanelles6mOkConcerns"
						onclick="onCheck(this,'p2_fontanelles6m')"
						<%= props.getProperty("p2_fontanelles6mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_fontanelles6mNotDiscussed" name="p2_fontanelles6mNotDiscussed"
						onclick="onCheck(this,'p2_fontanelles6m')"
						<%= props.getProperty("p2_fontanelles6mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
               	
               	<tr>
					<td valign="top"><input type="radio"
						id="p2_eyes6mOk" name="p2_eyes6mOk"
						onclick="onCheck(this,'p2_eyes6m')"
						<%= props.getProperty("p2_eyes6mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_eyes6mOkConcerns" name="p2_eyes6mOkConcerns"
						onclick="onCheck(this,'p2_eyes6m')"
						<%= props.getProperty("p2_eyes6mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_eyes6mNotDiscussed" name="p2_eyes6mNotDiscussed"
						onclick="onCheck(this,'p2_eyes6m')"
						<%= props.getProperty("p2_eyes6mNotDiscussed", "") %>></td>
					<td><b><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke1.formRedReflex"/>*</a></b></td>
				</tr>
				
               	<tr>
					<td valign="top"><input type="radio"
						id="p2_corneal6mOk" name="p2_corneal6mOk"
						onclick="onCheck(this,'p2_corneal6m')"
						<%= props.getProperty("p2_corneal6mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_corneal6mOkConcerns" name="p2_corneal6mOkConcerns"
						onclick="onCheck(this,'p2_corneal6m')"
						<%= props.getProperty("p2_corneal6mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_corneal6mNotDiscussed" name="p2_corneal6mNotDiscussed"
						onclick="onCheck(this,'p2_corneal6m')"
						<%= props.getProperty("p2_corneal6mNotDiscussed", "") %>></td>
					<td><b><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formCornealReflex"/>*</a></b></td>
				</tr>
				
               	<tr>
					<td valign="top"><input type="radio"
						id="p2_hearing6mOk" name="p2_hearing6mOk"
						onclick="onCheck(this,'p2_hearing6m')"
						<%= props.getProperty("p2_hearing6mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hearing6mOkConcerns" name="p2_hearing6mOkConcerns"
						onclick="onCheck(this,'p2_hearing6m')"
						<%= props.getProperty("p2_hearing6mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hearing6mNotDiscussed" name="p2_hearing6mNotDiscussed"
						onclick="onCheck(this,'p2_hearing6m')"
						<%= props.getProperty("p2_hearing6mNotDiscussed", "") %>></td>
					<td><i><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formHearingInquiry"/>*</a></i></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio"
						id="p2_hips6mOk" name="p2_hips6mOk"
						onclick="onCheck(this,'p2_hips6m')"
						<%= props.getProperty("p2_hips6mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hips6mOkConcerns" name="p2_hips6mOkConcerns"
						onclick="onCheck(this,'p2_hips6m')"
						<%= props.getProperty("p2_hips6mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hips6mNotDiscussed" name="p2_hips6mNotDiscussed"
						onclick="onCheck(this,'p2_hips6m')"
						<%= props.getProperty("p2_hips6mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_2.formHips"/>*</a></td>
				</tr>
				
				<tr>
					<td valign="top"><input type="radio"
						id="p2_muscleTone6mOk" name="p2_muscleTone6mOk"
						onclick="onCheck(this,'p2_muscleTone6m')"
						<%= props.getProperty("p2_muscleTone6mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_muscleTone6mOkConcerns" name="p2_muscleTone6mOkConcerns"
						onclick="onCheck(this,'p2_muscleTone6m')"
						<%= props.getProperty("p2_muscleTone6mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_muscleTone6mNotDiscussed" name="p2_muscleTone6mNotDiscussed"
						onclick="onCheck(this,'p2_muscleTone6m')"
						<%= props.getProperty("p2_muscleTone6mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formMuscleTone"/>*</a></td>
				</tr>
                
				<tr>
					<td colspan="4" style="vertical-align:bottom;">
						<textarea id="p2_physical6m"
							name="p2_physical6m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_physical6m", "") %></textarea>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr id="problemsPlansp2">
			<!-- td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgProblemsAndPlans" /></a>
				<br>
				<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif">
                <bean:message key="oscarEncounter.formRourke2009.msgProblemsLegendp2" />
            </td-->
			<td colspan="4" style="vertical-align:bottom;"><textarea id="p2_problems2m"
				name="p2_problems2m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_problems2m", "") %></textarea></td>
			
			<td colspan="4" style="vertical-align:bottom;"><textarea id="p2_problems4m"
				name="p2_problems4m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_problems4m", "") %></textarea></td>
			
			<td colspan="4">
			<table id="prbt23" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDone"/></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_tb6mOk"
						name="p2_tb6mOk" onclick="onCheck(this,'p2_tb6m')" <%= props.getProperty("p2_tb6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_tb6mOkConcerns"
						name="p2_tb6mOkConcerns" onclick="onCheck(this,'p2_tb6m')" <%= props.getProperty("p2_tb6mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p2_tb6mNotDiscussed"
						name="p2_tb6mNotDiscussed" onclick="onCheck(this,'p2_tb6m')" <%= props.getProperty("p2_tb6mNotDiscussed", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2006_2.formTB" /></td>
				</tr>
				 
				<tr align="center">
					<td colspan="4" style="vertical-align:bottom;"><textarea id="p2_problems6m"
						name="p2_problems6m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_problems6m", "") %></textarea></td>
				</tr>
				
			</table>
			</td>
		</tr>
		<tr id="immunizationp2">
			<!-- td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgImmunization" /></a><br>
			<bean:message key="oscarEncounter.formRourke1.msgImmunizationDesc" />
			<br>
				<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif">
                <bean:message key="oscarEncounter.formRourke2009.msgProblemsLegend" />
			</td -->
			<td colspan="4" style="text-align: center" colspan="3" valign="top"><b><bean:message
				key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b></td>
			<td colspan="4" style="text-align: center" colspan="3" valign="top"><b><bean:message
				key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b></td>
			
			<td colspan="4">			
			<table cellpadding="0" cellspacing="0" width="100%" height="100%">
				<tr>
					<td style="text-align: center" colspan="3"><b><bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b><br />
					<bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationHepatitis" />
					</td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>	
					<td>&nbsp;</td>				
				</tr>
				<tr>
					<td><input type="radio" id="p2_hepatitisVaccine6mOk"
						name="p2_hepatitisVaccine6mOk"  onclick="onCheck(this,'p2_hepatitisVaccine6m')"
						<%= props.getProperty("p2_hepatitisVaccine6mOk", "") %>></td>
					<td><input type="radio" id="p2_hepatitisVaccine6mNo"
						name="p2_hepatitisVaccine6mNo"  onclick="onCheck(this,'p2_hepatitisVaccine6m')"
						<%= props.getProperty("p2_hepatitisVaccine6mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationHepatitisVaccine" /></td>
				</tr>
				
				<tr>
					<td colspan="3">
						<textarea id="p2_immunization6m"
							name="p2_immunization6m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_immunization6m", "") %></textarea>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<!-- td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.formSignature" /></a></td-->
			<td colspan="4"><input type="text" class="wide"
				style="width: 100%" name="p2_signature2m"
				value="<%= props.getProperty("p2_signature2m", "") %>" /></td>
			<td colspan="4"><input type="text" class="wide" maxlength="42"
				style="width: 100%" name="p2_signature4m"
				value="<%= props.getProperty("p2_signature4m", "") %>" /></td>
			<td colspan="4"><input type="text" class="wide"
				style="width: 100%" name="p2_signature6m"
				value="<%= props.getProperty("p2_signature6m", "") %>" /></td>
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
    Calendar.setup({ inputField : "p2_date2m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p2_date2m_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "p2_date4m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p2_date4m_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "p2_date6m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p2_date6m_cal", singleClick : true, step : 1 });
</script>
