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
<%@ include file="/taglibs.jsp"%>
<%@ page import="java.util.Map" %>
<%@ page import="org.oscarehr.provider.web.UserPreferenceAction" %>
<%@ page import="org.oscarehr.common.model.UserProperty" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>	
<%
	@SuppressWarnings("unchecked")
	Map<String,String> prefs = (Map<String,String>)request.getAttribute("prefs");
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="provider.pref.title" /></title>
<script src="<c:out value="${ctx}/js/checkPassword.js.jsp"/>"></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>
<script src="<c:out value="${ctx}/js/jquery.js"/>"></script>
<script>
jQuery.noConflict();
</script>
<script>
jQuery(document).ready(function(){
	//jQuery("#general").hide();
	jQuery("#scheduling").hide();
	jQuery("#encounter").hide();
	jQuery("#rx").hide();
	jQuery("#consultation").hide();
	jQuery("#myoscar").hide();
	jQuery("#caisi").hide();
	jQuery("#billing").hide();
	
	jQuery('.head').click(function(e) {
		e.preventDefault();
		jQuery(".pref_pane").each(function() {
			jQuery(this).hide();
		})	
		jQuery("#"+jQuery(this).attr('pane')).show();
	});
	
	colourUpdate();
});
</script>
<script type="text/javascript">
            function colourUpdate() {
                var elem = document.getElementById('cdisp');
                elem.style.backgroundColor=document.forms[0].elements['pref.colour'].value;
                setTimeout('colourUpdate()', 1000);
            }
</script>        
<script type="text/javascript" src="../share/javascript/picker.js"></script>

<script type="text/javascript">
var remote=null;
var awnd=null;
function rs(n,u,w,h,x) {
    args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
    remote=window.open(u,n,args);    
}
function dxScriptAttach(name2) {
	var ff = eval("document.forms[0].elements['pref.<%=UserProperty.DEFAULT_DX_CODE%>']");
	var f0 = ff.value;
	var f1 = escape("document.forms[0].elements[\'pref.<%=UserProperty.DEFAULT_DX_CODE%>\'].value");
	awnd=rs('att','../billing/CA/ON/billingDigSearch.jsp?name='+f0 + '&search=&name2='+f1,600,600,1);
	awnd.focus();
}
</script>

<script>
	function validateForm() {
		var form = document.getElementById("preferenceForm");
		
		if(jQuery("input[name='current_password']").val().length>0) {
			if(jQuery("input[name='new_password']").val().length == 0) {
				alert('Please enter a new password');
				jQuery("input[name='new_password']").focus();
				return false;
			}			
			//validate password to policy
			if(!validatePassword(jQuery("input[name='new_password']").val())) {
				jQuery("input[name='new_password']").focus();
				return false;
			}
			//check that new_password and confirm_password match
			if(!(jQuery("input[name='new_password']").val() == jQuery("input[name='confirm_password']").val())) {
				alert('Passwords do not match')
				jQuery("input[name='confirm_password']").focus();
				return false;
			}
		}
		return true;
	}
</script>

<style type="text/css">
	.preferenceTable td
	{
		border: solid white 2px;
	}
	
	.preferenceLabel
	{
		text-align:right;
		width:25%;
		padding-right:8px;
		font-size:13px;
		font-weight:bold;
		vertical-align:top;
	}

	.preferenceUnits
	{
		font-size:9px;
		font-weight:normal;
	}

	.preferenceValue
	{
		font-size:12px;
	}
	
	.head
	{
		font-weight:bold;
	}
</style>
</head>

<body style="font-family:sans-serif;margin:0px 5px 0px 5px">
<form id="preferenceForm" action="<%=request.getContextPath()%>/provider/UserPreference.do" method="post" onSubmit="return validateForm();">
		<input type="hidden" name="method" value="saveGeneral" />
		<div style="background-color:#CCCCFF;text-align:center;font-weight:bold;">
			<bean:message key="provider.pref.title" />		
			<span style="float:right;clear:right;text-align:right;font-weight:normal;font-size:10pt;margin-right:4px">
			<%=loggedInInfo.getLoggedInProvider().getFormattedName() %>
			</span>
		</div>
		<br/>

				<!-- TOC -->
				<div style="float:left;clear:left;width:20%;border:3px solid #EEEEFF;height:inherit;text-align:left;font-weight:normal;overflow:auto">
					<div id="accordion">
					    <h3 class="head" pane="general"><a href="#"><bean:message key="provider.pref.section.general" /></a></h3>
					    <h3 class="head" pane="scheduling"><a href="#"><bean:message key="provider.pref.section.scheduling" /></a></h3>
					    <h3 class="head" pane="billing"><a href="#"><bean:message key="provider.pref.section.billing" /></a></h3>
					    <h3 class="head" pane="encounter"><a href="#"><bean:message key="provider.pref.section.encounter" /></a></h3>
					    <h3 class="head" pane="rx"><a href="#"><bean:message key="provider.pref.section.rx" /></a></h3>					    
					    <h3 class="head" pane="consultation"><a href="#"><bean:message key="provider.pref.section.consultation" /></a></h3>
					    <h3 class="head" pane="myoscar"><a href="#"><bean:message key="provider.pref.section.phr" /></a></h3>
					    <h3 class="head" pane="caisi"><a href="#"><bean:message key="provider.pref.section.caisi" /></a></h3>
					</div>
				</div>
				
				
				<div style="float:right;clear:right;width:78%;border:3px solid #EEEEEE;background-color:#EEEEFF">
				
				
					<div id="general" class="pref_pane" >
						<h3 style="text-align:center">General Settings</h3>
						<!-- change password -->
						<br/><br/>
						<div style="float:left;clear:left;width:48%">
						<table border="0">
							<tr><td colspan="2"><b><bean:message key="provider.pref.changepw.title" /></b></td></tr>
							<tr><td nowrap="nowrap"><bean:message key="provider.pref.changepw.current" />:</td><td><input type="password" name="current_password" size="12"/></td></tr>
							<tr><td nowrap="nowrap"><bean:message key="provider.pref.changepw.new" />: </td><td><input name="new_password" type="password" size="12"/></td></tr>
							<tr><td nowrap="nowrap"><bean:message key="provider.pref.changepw.confirm" />: </td><td><input name="confirm_password" type="password" size="12"/></td></tr>
							<tr style="height:20px"><td colspan="2"></td></tr>													
							<tr><td><bean:message key="provider.pref.general.fax" />: </td><td><input type="text" size="25" <%=UserPreferenceAction.getTextData(prefs,"pref."+UserProperty.FAX)%>/></td></tr>		
							<tr><td><bean:message key="provider.pref.general.drugrefid" />: </td><td><input type="text" size="25" <%=UserPreferenceAction.getTextData(prefs,"pref."+UserProperty.MYDRUGREF_ID)%> /></td></tr>
							<tr style="height:20px"><td colspan="2"></td></tr>
							<tr><td><bean:message key="provider.pref.general.signature" />: </td><td><input type="text" size="45" <%=UserPreferenceAction.getTextData(prefs,"pref."+UserProperty.SIGNATURE)%>/></td></tr>
						</table>
						<br/><br/>
						<input type="submit" value="<bean:message key="provider.pref.btnSave"/>"/>
						</div>
						<div style="float:right;clear:right;width:48%">
						<table border="0" cellspacing="2" cellpadding="2">
							
							<tr>
								<td><bean:message key="provider.pref.general.colour" />:</td>
								<td>
									<input type="hidden" <%=UserPreferenceAction.getTextData(prefs,"pref."+UserProperty.COLOUR)%> />
									<span id='cdisp' style='width: 100%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
									<a href="javascript:TCP.popup(document.forms[0].elements['pref.colour'])"><img
										width="15" height="13" border="0" src="../images/sel.gif"></a>
								</td>
							</tr>
							<tr>
								<td><bean:message key="provider.pref.general.sex" />:</td>
								<td>
									<%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.SEX) %>
								</td>
							</tr>
							<tr>
								<td><bean:message key="provider.pref.general.hc_type" />:</td>
								<td>
									<%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.HC_TYPE) %>
								</td>
							</tr>
							<tr>
								<td><bean:message key="provider.pref.general.workload" />:</td>
								<td>
									<%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.WORKLOAD_MANAGEMENT) %>
								</td>
							</tr>						
						</table>
						</div>	<!-- right general -->					
					</div>	<!-- general -->
					
					
					
					<div id="scheduling" class="pref_pane">
						<h3 style="text-align:center"><bean:message key="provider.pref.scheduling.title" /></h3>
						<!-- change password -->
						<br/><br/>						
						<table border="0">							
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.scheduling.start_hour" />:</td>
								<td><%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.SCHEDULE_START_HOUR) %></td>
							</tr>
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.scheduling.end_hour" />: </td>
								<td><%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.SCHEDULE_END_HOUR) %></td>
							</tr>
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.scheduling.period" />: </td>
								<td><%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.SCHEDULE_PERIOD) %></td>
							</tr>
							<tr style="height:20px"><td colspan="2"></td></tr>													
							<tr>
								<td><bean:message key="provider.pref.scheduling.group_no" />: </td>
								<td>
									<%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.MYGROUP_NO) %>&nbsp;
									<input type="button" value="<bean:message key="provider.pref.scheduling.group_no.btn" />"/>
								</td>
							</tr>									
						</table>
						<br/><br/>
						<input type="submit" value="<bean:message key="provider.pref.btnSave"/>"/>									
					</div>	<!-- scheduling -->
					
					<div id="billing" class="pref_pane">
						<h3 style="text-align:center"><bean:message key="provider.pref.billing.title" /></h3>						
						<br/><br/>						
						<table border="0">	
							<tr>
								<td><bean:message key="provider.pref.billing.diag_code" />:</td>
								<td>
									<input type="text" size="5" maxlength="5" name="pref.<%=UserProperty.DEFAULT_DX_CODE%>" ondblClick="dxScriptAttach('dxCode')" value="" />
									<a href=# onclick="dxScriptAttach('dxCode');"><bean:message key="provider.pref.billing.search" /></a>
								</td>
							</tr>
			
							<oscar:oscarPropertiesCheck property="billregion" value="BC">
								<tr>
									<td nowrap="nowrap"><bean:message key="provider.pref.billing.bc.referral_type" />:</td>
									<td>
									<%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.DEFAULT_REFERRAL_TYPE) %>
									</td>
								</tr>
								<tr>
									<td nowrap="nowrap"><bean:message key="provider.pref.billing.bc.payee" />:</td>
									<td>
									<%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.DEFAULT_PAYEE) %>
									</td>
								</tr>
							</oscar:oscarPropertiesCheck>
							<oscar:oscarPropertiesCheck property="billregion" value="ON">
								<tr>
									<td nowrap="nowrap"><bean:message key="provider.pref.billing.on.form" />:</td>
									<td>
									<%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.DEFAULT_BILLING_FORM) %>
									</td>
								</tr>
							</oscar:oscarPropertiesCheck>						
						</table>
						<br/><br/>
						<input type="submit" value="<bean:message key="provider.pref.btnSave"/>"/>									
					</div>	<!-- billing -->											
					
					
					
					<div id="encounter" class="pref_pane">
						<h3 style="text-align:center"><bean:message key="provider.pref.encounter.title"/></h3>						
						<br/><br/>
						<table border="0">							
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.encounter.cme_ui"/>:</td>
								<td>
									<%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.NEW_CME) %>
								</td>
							</tr>
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.encounter.stale_date"/>: </td>
								<td>
									 <%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.STALE_NOTEDATE) %> (months)
								</td>
							</tr>											
							<tr>
								<td>F<bean:message key="provider.pref.encounter.eform_group"/>: </td>
								<td>
									<%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.EFORM_FAVOURITE_GROUP) %>
								</td>
							</tr>		
							
							<tr>
								<td><bean:message key="provider.pref.encounter.form_length"/>: </td>
								<td>
									<input type="text" size="5" <%=UserPreferenceAction.getTextData(prefs,"pref."+UserProperty.ENCOUNTER_FORM_LENGTH)%> />
								</td>
							</tr>
							
							<tr style="height:20px"><td colspan="2"></td></tr>
														
							<tr>
								<td valign="top"><bean:message key="provider.pref.encounter.forms"/></td>
								<td>

								<div style="height: 10em; border: 1px solid grey; overflow: auto; width: 25em;">
									<%=UserPreferenceAction.getEncounterFormHTML(prefs,"pref." + UserProperty.ENCOUNTER_FORM_NAME)%>
								</div>


								
								</td>
							</tr>
							
							<tr>
								<td><bean:message key="provider.pref.encounter.eforms"/></td>
								<td>
								
					<div style="height: 10em; border: 1px solid grey; overflow: auto; width: 25em;">
						<%=UserPreferenceAction.getEformHTML(prefs,"pref." + UserProperty.EFORM_NAME)%>
					</div>
								
								
								</td>
							</tr>							
						</table>
						<br/><br/>
						<input type="submit" value="<bean:message key="provider.pref.btnSave"/>"/>
										
					</div>	<!-- encounter -->
					
										

					<div id="rx" class="pref_pane">
						<h3 style="text-align:center"><bean:message key="provider.pref.rx.title"/></h3>
						<br/><br/>						
						<table border="0">							
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.rx.rx3"/>:</td>
								<td><%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.RX_USE_RX3) %></td>
							</tr>
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.rx.qr"/>: </td>
								<td><%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.RX_SHOW_QR_CODE) %></td>
							</tr>
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.rx.page_size"/>: </td>
								<td><%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.RX_PAGE_SIZE) %></td>
							</tr>
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.rx.dob"/>: </td>
								<td><%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.RX_SHOW_PATIENT_DOB) %></td>
							</tr>
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.rx.quantity"/>: </td>
								<td><input type="text" size="20" <%=UserPreferenceAction.getTextData(prefs,"pref."+UserProperty.RX_DEFAULT_QUANTITY)%>/></td>
							</tr>
							<tr style="height:20px"><td colspan="2"></td></tr>													
						</table>
						<br/><br/>
						<input type="submit" value="<bean:message key="provider.pref.btnSave"/>"/>									
					</div>	<!-- rx -->
					
					
					<div id="consultation" class="pref_pane">
						<h3 style="text-align:center"><bean:message key="provider.pref.consult.title"/></h3>
						<br/><br/>						
						<table border="0">							
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.consult.cutoff"/>:</td>
								<td><input type="text" size="20" <%=UserPreferenceAction.getTextData(prefs,"pref."+UserProperty.CONSULTATION_TIME_PERIOD_WARNING)%>/></td>
							</tr>
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.consult.team"/>: </td>
								<td><%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.CONSULTATION_TEAM_WARNING)%></td>
							</tr>
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.consult.paste"/>: </td>
								<td><%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.CONSULTATION_REQ_PASTE_FMT)%></td>
							</tr>							
							<tr style="height:20px"><td colspan="2"></td></tr>													
						</table>
						<br/><br/>
						<input type="submit" value="<bean:message key="provider.pref.btnSave"/>"/>									
					</div>	<!-- consultations -->							
										

					<div id="myoscar" class="pref_pane">
						<h3 style="text-align:center"><bean:message key="provider.pref.phr.title"/></h3>
						<br/><br/>						
						<table border="0">							
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.phr.id"/>:</td>
								<td><input type="text" size="20" <%=UserPreferenceAction.getTextData(prefs,"pref."+UserProperty.MYOSCAR_ID)%>/></td>
							</tr>
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.phr.mymeds"/>: </td>
								<td><%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.MYMEDS)%></td>
							</tr>
							<tr style="height:20px"><td colspan="2"></td></tr>													
						</table>
						<br/><br/>
						<input type="submit" value="<bean:message key="provider.pref.btnSave"/>"/>									
					</div>	<!-- myoscar -->		

					<div id="caisi" class="pref_pane">
						<h3 style="text-align:center"><bean:message key="provider.pref.caisi.title"/></h3>
						<br/><br/>						
						<table border="0">							
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.caisi.tickler_warning"/>:</td>
								<td><%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.NEW_TICKLER_WARNING_WINDOW)%></td>
							</tr>
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.caisi.pmm"/>: </td>
								<td><%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.CAISI_DEFAULT_PMM)%></td>
							</tr>
							<tr>
								<td nowrap="nowrap"><bean:message key="provider.pref.caisi.prev_billing"/>: </td>
								<td><%=UserPreferenceAction.getSelect(prefs,"pref."+UserProperty.CAISI_PREV_BILLING)%></td>
							</tr>
							<tr style="height:20px"><td colspan="2"></td></tr>													
						</table>
						<br/><br/>
						<input type="submit" value="<bean:message key="provider.pref.btnSave"/>"/>									
					</div>	<!-- caisi -->		
					
												
				</div> <!-- right of TOC -->
				
			
			
		
</form>
</body>

</html>
