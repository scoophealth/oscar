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
<%
 String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String providerNo=loggedInInfo.getLoggedInProviderNo();
   			

  String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.*,java.text.*,java.sql.*,java.net.*" errorPage="errorpage.jsp" %>
<%@ page import="oscar.OscarProperties" %>
<%@ page import="org.oscarehr.common.dao.UserPropertyDAO"%>
<%@ page import="org.oscarehr.common.model.UserProperty"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>

<%@page import="org.oscarehr.common.model.ProviderPreference"%>
<%@page import="org.oscarehr.web.admin.ProviderPreferencesUIBean"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.web.PrescriptionQrCodeUIBean"%>
<%@page import="org.oscarehr.common.model.EForm"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.common.model.EncounterForm"%>
<%@page import="org.oscarehr.common.dao.CtlBillingServiceDao" %>
<%@page import="org.oscarehr.common.model.CtlBillingService" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="org.oscarehr.common.model.Provider" %>

<%
	CtlBillingServiceDao ctlBillingServiceDao = SpringUtils.getBean(CtlBillingServiceDao.class);
%>

<html:html locale="true">

<head>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="provider.providerpreference.title" /></title>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>
<script language="JavaScript">

function setfocus() {
  this.focus();
  document.UPDATEPRE.mygroup_no.focus();
  document.UPDATEPRE.mygroup_no.select();
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}

function checkTypeNum(typeIn) {
	var typeInOK = true;
	var i = 0;
	var length = typeIn.length;
	var ch;
	// walk through a string and find a number
	if (length>=1) {
	  while (i <  length) {
		ch = typeIn.substring(i, i+1);
		if (ch == ".") { i++; continue; }
		if ((ch < "0") || (ch > "9") ) {
			typeInOK = false;
			break;
		}
	    i++;
      }
	} else typeInOK = false;
	return typeInOK;
}

function checkTypeIn(obj) {
    if(!checkTypeNum(obj.value) ) {
	  alert ("<bean:message key="provider.providerpreference.msgMustBeNumber"/>");
	}
}

function checkTypeInAll() {
  var checkin = false;
  var s=0;
  var e=0;
  var i=0;
  if(isNumeric(document.UPDATEPRE.start_hour.value) && isNumeric(document.UPDATEPRE.end_hour.value) && isNumeric(document.UPDATEPRE.every_min.value)) {
    s=eval(document.UPDATEPRE.start_hour.value);
    e=eval(document.UPDATEPRE.end_hour.value);
    i=eval(document.UPDATEPRE.every_min.value);
    if(e < 24){
      if(s < e){
        if(i <= (e - s)*60 && i > 0){
          checkin = true;
        }else{
          alert ("<bean:message key="provider.providerpreference.msgPositivePeriod"/>");
          this.focus();
          document.UPDATEPRE.every_min.focus();
         }
      }else{
        alert ("<bean:message key="provider.providerpreference.msgStartHourErlierEndHour"/>");
        this.focus();
        document.UPDATEPRE.start_hour.focus();
       }
    }else{
      alert ("<bean:message key="provider.providerpreference.msgHourLess24"/>");
      this.focus();
      document.UPDATEPRE.end_hour.focus();
     }
  } else {
     alert ("<bean:message key="provider.providerpreference.msgTypeNumbers"/>");
  }
	return checkin;
}

function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=5,left=5";//360,680
  var popup=window.open(page, "<bean:message key="provider.providerpreference.titlePopup"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}

function isNumeric(strString) {
    var validNums = "0123456789";
    var strChar;
    var retval = true;
    if(strString.length == 0){
    retval = false;
    }
    for (i = 0; i < strString.length && retval == true; i++){
	strChar = strString.charAt(i);
	if (validNums.indexOf(strChar) == -1){
	    retval = false;
	}
    }
    return retval;
}

function showHideBillPref() {
    $("billingONpref").toggle();
}

function showHideERxPref() {
    //$("eRxPref").toggle();
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
	
	table.eRxTableCenter
	{
        width:50%; 
		margin-left:25%; 
		margin-right:25%;
    }
	
</style>
</head>

<%
	ProviderPreference providerPreference=ProviderPreferencesUIBean.getProviderPreference(providerNo);

	if (providerPreference == null) {
	    providerPreference = new ProviderPreference();
	}
	
	String startHour = request.getParameter("start_hour")!=null?request.getParameter("start_hour"):providerPreference.getStartHour().toString();	
	String endHour = request.getParameter("end_hour")!=null?request.getParameter("end_hour"):providerPreference.getEndHour().toString();
	String everyMin = request.getParameter("every_min")!=null?request.getParameter("every_min"):providerPreference.getEveryMin().toString();
	String myGroupNo = request.getParameter("mygroup_no")!=null?request.getParameter("mygroup_no"):providerPreference.getMyGroupNo();
	String newTicklerWarningWindow =request.getParameter("new_tickler_warning_window")!=null?request.getParameter("new_tickler_warning_window"):providerPreference.getNewTicklerWarningWindow();
	String ticklerProviderNo = request.getParameter("tklerproviderno");
	String defaultPMM = request.getParameter("default_pmm")!=null?request.getParameter("default_pmm"):providerPreference.getDefaultCaisiPmm();
	String caisiBillingNotDelete = request.getParameter("caisiBillingPreferenceNotDelete")!=null?request.getParameter("caisiBillingPreferenceNotDelete"):String.valueOf(providerPreference.getDefaultDoNotDeleteBilling());
	
%>

<body bgproperties="fixed"  onLoad="setfocus();showHideBillPref();showHideERxPref();" topmargin="0"leftmargin="0" rightmargin="0" style="font-family:sans-serif">
	<FORM NAME = "UPDATEPRE" METHOD="post" ACTION="providerupdatepreference.jsp" onSubmit="return(checkTypeInAll())">

		<div style="background-color:<%=deepcolor%>;text-align:center;font-weight:bold">
			<bean:message key="provider.providerpreference.description"/>
		</div>

		<table class="preferenceTable" style="width:100%;border-collapse:collapse;background-color:<%=weakcolor%>;">
			<tr>
				<td class="preferenceLabel">
					<bean:message key="provider.preference.formStartHour" />
					<span class="preferenceUnits">(0-23)</span>
				</td>
				<td class="preferenceValue">
					<INPUT TYPE="TEXT" NAME="start_hour" VALUE='<%=startHour%>' size="2" maxlength="2">
				</td>
			</tr>
			<tr>
				<td class="preferenceLabel">
					<bean:message key="provider.preference.formEndHour" />
					<span class="preferenceUnits">(0-23)</span>
				</td>
				<td class="preferenceValue">
					<INPUT TYPE="TEXT" NAME="end_hour" VALUE='<%=endHour%>' size="2" maxlength="2">
				</td>
			</tr>
			<tr>
				<td class="preferenceLabel">
					<bean:message key="provider.preference.formPeriod" />
					<span class="preferenceUnits"><bean:message key="provider.preference.min" /></span>
				</td>
				<td class="preferenceValue">
					<INPUT TYPE="TEXT" NAME="every_min" VALUE='<%=everyMin%>' size="2" maxlength="2">
				</td>
			</tr>
			<tr>
				<td class="preferenceLabel">
					<bean:message key="provider.preference.formGroupNo" />
				</td>
				<td class="preferenceValue">
					<INPUT TYPE="TEXT" NAME="mygroup_no" VALUE='<%=myGroupNo%>' size="12" maxlength="10">
					<input type="button" value="<bean:message key="provider.providerpreference.viewedit" />" onClick="popupPage(360,680,'providerdisplaymygroup.jsp' );return false;" />
				</td>
			</tr>
			<caisi:isModuleLoad moduleName="ticklerplus">
				<tr id="ticklerPlus">
					<!-- check box of new-tickler-warnning-windows -->
					<td class="preferenceLabel">
						New Tickler Warning Window
					</td>
					<td class="preferenceValue">
						<%
							String myCheck1 = "";
							String myCheck2 = "";
							String myValue ="";
							if("enabled".equals(newTicklerWarningWindow)) {
								myCheck1 = "checked";
								myCheck2 = "unchecked";
							}
							else {
								myCheck1 = "unchecked";
								myCheck2 = "checked";
							}
						%>
						
						<script type="text/javascript">
						function ticklerwarningchange(){
							var tickRadios = document.getElementsByName("new_tickler_warning_window");
							for (var i=0;i<tickRadios.length;i++) {
								if (tickRadios[i].checked==true) {
									if (tickRadios[i].value=="enabled") {
										// hidden ticklerforprovider row 
										document.getElementById("ticklerProvider").style.display="table-row";
									} else {
										// show ticklerforprovider row
										document.getElementById("ticklerProvider").style.display="none";
									}
								}									
							}
						}
						</script>
			            
			            <input type="radio" name="new_tickler_warning_window" value="enabled" <%= myCheck1 %> onchange="ticklerwarningchange()"> Enabled </input>
			            <br>
						<input type="radio" name="new_tickler_warning_window" value="disabled" <%= myCheck2 %> onchange="ticklerwarningchange()"> Disabled </input>
					</td>
				</tr>
				
				<tr id="ticklerProvider" style=<%=myCheck1=="checked"?"display:table-row;":"display:none" %>>
					<td class="preferenceLabel">
						Tickler Warning Window for which provider?
					</td>
					<td class="preferenceValue">
						<select id="ticklerforprovider" name="ticklerforproviderno">
						<%
								String ticklerforproviderNo = ticklerProviderNo;
								if (ticklerforproviderNo == null) {
									ticklerforproviderNo = loggedInInfo.getLoggedInProviderNo();
								}
								ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
								List<Provider> listProvider = new ArrayList<Provider>();
								if (providerDao != null) {
									listProvider = providerDao.getProviders();
								}							
								for (Provider provider : listProvider) {
									String selected = "";
									if (ticklerforproviderNo.equals(provider.getProviderNo())) {
										selected ="selected";
									}
									String strOption = String.format("<option value=\"%s\" %s>%s</option>"
											, provider.getProviderNo(), selected, provider.getFormattedName());
									out.print(strOption);
								}
						%>
						</select>
					</td>
				</tr>

				<!-- check box of the default PMM window -->
				<tr>
					<td class="preferenceLabel">
						Default PMM
					</td>
					<td class="preferenceValue">
						<%
							String myCheck3 = "";
							String myCheck4 = "";
							if("enabled".equals(defaultPMM)) {
								myCheck3 = "checked";
								myCheck4 = "unchecked";
							}
							else {
								myCheck3 = "unchecked";
								myCheck4 = "checked";
							}
						%>
			            <input type="radio" name="default_pmm" value="enabled" <%= myCheck3 %>> Enabled
			            <br>
						<input type="radio" name="default_pmm" value="disabled" <%= myCheck4 %>> Disabled
					</td>
				</tr>
				
				 <tr>
		            <td class="preferenceLabel">
		            <bean:message key="provider.btnCaisiBillPreferenceNotDelete"/>
		            </td>
		            <td class="preferenceValue">

		             <%  String myCheck5 = "";
		                 String myCheck6 = "";
		                 String value1 = caisiBillingNotDelete;
		                  if(value1!=null && value1.equals("1"))
		                  { 	myCheck5 = "checked";
		                        myCheck6 = "unchecked";}
		                  else
		                  { 	myCheck5 = "unchecked";
		                  		myCheck6 = "checked";}

		               %>

		                                <input type="radio" name="caisiBillingPreferenceNotDelete" value="1" <%= myCheck5 %> > Enabled
		                                <br>
		                                <input type="radio" name="caisiBillingPreferenceNotDelete" value="0" <%= myCheck6 %> > Disabled

		            </td>
		          </tr>

			</caisi:isModuleLoad>

			<!-- QR Code on prescriptions setting -->
			<tr>
				<td class="preferenceLabel">
					<bean:message key="provider.providerpreference.qrCodeOnPrescriptions" />
				</td>
				<td class="preferenceValue">
					<%
	            		boolean checked=PrescriptionQrCodeUIBean.isPrescriptionQrCodeEnabledForProvider(providerNo);
	            	%>
	            	<input type="checkbox" name="prescriptionQrCodes" <%=checked?"checked=\"checked\"":""%> />
	            </td>
			</tr>

			<%-- links to display on the appointment screen --%>
			<tr>
				<td class="preferenceLabel">
					<bean:message key="provider.providerpreference.appointmentScreenLinkNameDisplayLength" />
				</td>
				<td class="preferenceValue">
					<input type="text" name="appointmentScreenFormsNameDisplayLength" value='<%=providerPreference.getAppointmentScreenLinkNameDisplayLength()%>' size="2">
	            </td>
			</tr>
			<tr>
				<td class="preferenceLabel">
					<bean:message key="provider.providerpreference.formsToDisplayOnAppointmentScreen" />
				</td>
				<td class="preferenceValue">
					<div style="height:10em;border:solid grey 1px;overflow:auto;white-space:nowrap;width:45em">
					<%
						List<EncounterForm> encounterForms=ProviderPreferencesUIBean.getAllEncounterForms();
						Collection<String> checkedEncounterFormNames=ProviderPreferencesUIBean.getCheckedEncounterFormNames(providerNo);
						for(EncounterForm encounterForm : encounterForms)
						{
							String nameEscaped=StringEscapeUtils.escapeHtml(encounterForm.getFormName());
							String checkedString=(checkedEncounterFormNames.contains(encounterForm.getFormName())?"checked=\"checked\"":"");
							%>
								<input type="checkbox" name="encounterFormName" value="<%=nameEscaped%>" <%=checkedString%> /> <%=nameEscaped%>
								<br />
							<%
						}
	            	%>
					</div>
	            </td>
			</tr>
			<tr>
				<td class="preferenceLabel">
					<bean:message key="provider.providerpreference.eFormsToDisplayOnAppointmentScreen" />
				</td>
				<td class="preferenceValue">
					<div style="height:10em;border:solid grey 1px;overflow:auto;white-space:nowrap;width:45em">
					<%
						List<EForm> eforms=ProviderPreferencesUIBean.getAllEForms();
						Collection<Integer> checkedEFormIds=ProviderPreferencesUIBean.getCheckedEFormIds(providerNo);
						for(EForm eform : eforms)
						{
							String checkedString=(checkedEFormIds.contains(eform.getId())?"checked=\"checked\"":"");
							%>
								<input type="checkbox" name="eformId" value="<%=eform.getId()%>" <%=checkedString%> /> <%=StringEscapeUtils.escapeHtml(eform.getFormName())%>
								<br />
							<%
						}
	            	%>
					</div>
	            </td>
			</tr>
			<tr>
				<td class="preferenceLabel">
					<bean:message key="provider.providerpreference.quickLinksToDisplayOnAppointmentScreen" />
				</td>
				<td class="preferenceValue">
					<div style="height:10em;border:solid grey 1px;overflow:auto;white-space:nowrap;width:45em">
					<%
						Collection<ProviderPreference.QuickLink> quickLinks=ProviderPreferencesUIBean.getQuickLinks(providerNo);
						for(ProviderPreference.QuickLink quickLink : quickLinks)
						{
							%>
								<input type="button" value="<bean:message key="REMOVE"/>" onclick="document.location='providerPreferenceQuickLinksAction.jsp?action=remove&name='+escape('<%=StringEscapeUtils.escapeHtml(quickLink.getName())%>')" />
								<%=StringEscapeUtils.escapeHtml(quickLink.getName())%> : <%=StringEscapeUtils.escapeHtml(quickLink.getUrl())%>
								<br />
							<%
						}
	            	%>
					</div>
					<table style="border:none;border-collapse:collapse">
						<tr>
							<td style="border:none;text-align:right"><bean:message key="NAME"/></td>
							<td style="border:none"><input type="text" name="quickLinkName" /></td>
						</tr>
						<tr>
							<td style="border:none;text-align:right;vertical-align:top"><bean:message key="URL"/></td>
							<td style="border:none">
								<input type="text" name="quickLinkUrl" />
								<div style="font-size:9px">(expanded tokens in the url are ${contextPath} and ${demographicId})</div>
							</td>
						</tr>
						<tr>
							<td style="border:none"></td>
							<td style="border:none">
								<script type="text/javascript">
									function addQuickLink()
									{
										name=escape(document.UPDATEPRE.quickLinkName.value);
										url=escape(document.UPDATEPRE.quickLinkUrl.value);
										document.location="providerPreferenceQuickLinksAction.jsp?action=add&name="+name+"&url="+url;
									}
								</script>
								<input type="button" value="<bean:message key="ADD"/>" onclick="addQuickLink()" />
							</td>
						</tr>
					</table>
	            </td>
			</tr>

			<tr>
				<%
					UserPropertyDAO propertyDao = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
					UserProperty prop = propertyDao.getProp(providerNo,"rxInteractionWarningLevel");
					String warningLevel = "0";
					if(prop!=null) {
						warningLevel = prop.getValue();
					}
				%>
				<td class="preferenceLabel">
					<bean:message key="provider.providerpreference.rxInteractionWarningLevel" />
				</td>
				<td class="preferenceValue">
					<select id="rxInteractionWarningLevel">
						<option value="0" <%=(warningLevel.equals("0")?"selected=\"selected\"":"") %>>Not Specified</option>
						<option value="1" <%=(warningLevel.equals("1")?"selected=\"selected\"":"") %>>Low</option>
						<option value="2" <%=(warningLevel.equals("2")?"selected=\"selected\"":"") %>>Medium</option>
						<option value="3" <%=(warningLevel.equals("3")?"selected=\"selected\"":"") %>>High</option>
						<option value="4" <%=(warningLevel.equals("4")?"selected=\"selected\"":"") %>>None</option>
					</select>
	            </td>
        <script>
Event.observe('rxInteractionWarningLevel', 'change', function(event) {
	var value = $('rxInteractionWarningLevel').getValue();

	new Ajax.Request('<c:out value="${ctx}"/>/provider/rxInteractionWarningLevel.do?method=update&value='+value, {
		  method: 'get',
		  onSuccess: function(transport) {
		  }
		});

});

</script>
			</tr>

 <tr>
     <%
         Integer h = 0;
         Integer mins = 0;
         prop = propertyDao.getProp(providerNo,UserProperty.OSCAR_MSG_RECVD);
         if( prop != null ) {
            String[] tmp = prop.getValue().split(":");
            h = Integer.valueOf(tmp[0]);
            mins = Integer.valueOf(tmp[1]);
         }
     %>
        <td class="preferenceLabel">
            Select when you want to receive Review Messages
            
         </td>
         <td preferenceValue>
             <select id="reviewMsg" name="reviewMsg">                 
                 <%
                     for( int hr = 0; hr < 24; ++hr ) {
                         for( int min = 0; min < 60; min+=30 ) {
                 %>
                 <option value="<%=String.valueOf(hr)+":"+String.valueOf(min) %>" <%= hr == h && min == mins ? "selected" : ""%> ><%=String.valueOf(hr) + " : " + String.valueOf(min) + (min == 0 ? "0" :"") %></option>
                 <%
                        }
                    }
                 %>
             </select>                          
         </td>
        </tr>
        <script>
        Event.observe('reviewMsg', 'change', function(event) {
	var value = $('reviewMsg').getValue();

	new Ajax.Request('<c:out value="${ctx}"/>/setProviderStaleDate.do?method=OscarMsgRecvd&value='+value+'&provider_no=<%=providerNo%>', {
		  method: 'get',
		  onSuccess: function(transport) {
		  }
		});

});
        </script>
		</table>

		<div style="background-color:<%=deepcolor%>;text-align:center;font-weight:bold">
			<INPUT TYPE="submit" VALUE='<bean:message key="provider.providerpreference.btnSubmit"/>' SIZE="7">
			<INPUT TYPE = "RESET" VALUE ='<bean:message key="global.btnClose"/>' onClick="window.close();">
  		</div>

		<INPUT TYPE="hidden" NAME="color_template" VALUE='deepblue'>


<table width="100%" BGCOLOR="eeeeee">

<caisi:isModuleLoad moduleName="NEW_CME_SWITCH">
  <oscar:oscarPropertiesCheck property="TORONTO_RFQ" value="no">
	<tr>
    <TD align="center"><a href=# onClick ="popupPage(230,600,'../casemgmt/newCaseManagementEnable.jsp');return false;">Enable OSCAR CME UI</a> &nbsp;&nbsp;&nbsp;
    </tr>
  </oscar:oscarPropertiesCheck>
  </caisi:isModuleLoad>

  <tr>
	<td align="center"><a href=# onClick ="popupPage(230,600,'providerDefaultDxCode.jsp?provider_no=<%=request.getParameter("provider_no") %>');return false;">Edit Default Billing Diagnostic Code</a>&nbsp;&nbsp;&nbsp; </td>
	</tr>
  <tr>

    <TD align="center"><a href=# onClick ="popupPage(370,700,'providerchangepassword.jsp');return false;"><bean:message key="provider.btnChangePassword"/></a> &nbsp;&nbsp;&nbsp;</td>
  </tr>
  <tr>
      <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewDefaultSex');return false;"><bean:message key="provider.btnSetDefaultSex" /></a></td>
      </tr>
  <tr>
    <td align="center"><a href=# onClick ="popupPage(230,860,'providerSignature.jsp');return false;"><bean:message key="provider.btnEditSignature"/></a>
    </td>
  </tr>
  <oscar:oscarPropertiesCheck property="TORONTO_RFQ" value="no" defaultVal="true">
  <security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r">
  <tr>
    <td align="center">
<% String br = OscarProperties.getInstance().getProperty("billregion");
   if (br.equals("BC")) { %>
	<a href=# onClick ="popupPage(230,400,'../billing/CA/BC/viewBillingPreferencesAction.do?providerNo=<%=providerNo%>');return false;"><bean:message key="provider.btnBillPreference"/></a>
<% } else { %>
	<a href=# onClick ="showHideBillPref();return false;"><bean:message key="provider.btnBillPreference"/></a>
<% } %>
    </td>
  </tr>
  <tr>
      <td align="center">
	  <div id="billingONpref">
          <bean:message key="provider.labelDefaultBillForm"/>:
	  <select name="default_servicetype">
	      <option value="no">-- no --</option>
<%
	if (providerPreference!=null) {
		String def = providerPreference.getDefaultServiceType();
		for(Object[] result : ctlBillingServiceDao.getUniqueServiceTypes("A")) {

%>
				<option value="<%=(String)result[0]%>"
					<%=((String)result[0]).equals(def)?"selected":""%>>
					<%=(String)result[1]%></option>
<%
		}
	} else {
		for(Object[] result : ctlBillingServiceDao.getUniqueServiceTypes("A")) {
%>
		<option value="<%=(String)result[0]%>"><%=(String)result[1]%></option>
<%
		}
	}
%>
	  </select>
	  </div>
      </td>
  </tr>
</security:oscarSec>
	  <tr>
          <td align="center"><a href=# onClick ="popupPage(400,860,'providerAddress.jsp');return false;"><bean:message key="provider.btnEditAddress"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(400,860,'providerPhone.jsp');return false;"><bean:message key="provider.btnEditPhoneNumber"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(400,860,'providerFax.jsp');return false;"><bean:message key="provider.btnEditFaxNumber"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'providerColourPicker.jsp');return false;"><bean:message key="provider.btnEditColour"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(500,860,'providerPrinter.jsp');return false;"><bean:message key="provider.btnSetDefaultPrinter"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewRxPageSize');return false;"><bean:message key="provider.btnSetRxPageSize"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewUseRx3');return false;"><bean:message key="provider.btnSetRx3"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewCppSingleLine');return false;"><bean:message key="provider.btnSetCppSingleLine"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewShowPatientDOB');return false;"><bean:message key="provider.btnSetShowPatientDOB"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewDefaultQuantity');return false;"><bean:message key="provider.SetDefaultPrescriptionQuantity"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=view&provider_no=<%=providerNo%>');return false;"><bean:message key="provider.btnEditStaleDate"/></a></td>
      </tr>

      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewMyDrugrefId');return false;"><bean:message key="provider.btnSetmyDrugrefID"/></a></td>
      </tr>

      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewConsultationRequestCuffOffDate');return false;"><bean:message key="provider.btnSetConsultationCutoffTimePeriod"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewConsultationRequestTeamWarning');return false;"><bean:message key="provider.btnSetConsultationTeam"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewWorkLoadManagement');return false;"><bean:message key="provider.btnSetWorkLoadManagement"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewConsultPasteFmt');return false;"><bean:message key="provider.btnSetConsultPasteFmt"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewFavouriteEformGroup');return false;"><bean:message key="provider.btnSetEformGroup"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewHCType');return false;"><bean:message key="provider.btnSetHCType" /></a></td>
      </tr>
      <% if(OscarProperties.getInstance().hasProperty("ONTARIO_MD_INCOMINGREQUESTOR")){%>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewOntarioMDId');return false;"><bean:message key="provider.btnSetmyOntarioMD"/></a></td>
      </tr>
      <%}%>
  </oscar:oscarPropertiesCheck>
        <tr>
            <td align="center"><a href=# onClick ="popupPage(230,860,'providerIndivoIdSetter.jsp');return false;"><bean:message key="provider.btnSetIndivoId"/></a></td>
        </tr>
        <tr>
            <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewUseMyMeds');return false;"><bean:message key="provider.btnSetUseMyMeds"/></a></td>
        </tr>
  
  		<tr>
          <td align="center"><a href=# onClick ="popupPage(400,860,'../provider/CppPreferences.do');return false;"><bean:message key="provider.cppPrefs" /></a></td>
      	</tr>

      	<tr>
          <td align="center"><a href=# onClick ="popupPage(400,860,'../provider/OlisPreferences.do');return false;"><bean:message key="provider.olisPrefs" /></a></td>
      	</tr>
      	<tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewCommentLab');return false;"><bean:message key="provider.btnDisableAckCommentLab"/></a></td>
      </tr>
       <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewEncounterWindowSize');return false;"><bean:message key="provider.btnEditDefaultEncounterWindowSize"/></a></td>
      </tr>
       <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewQuickChartSize');return false;"><bean:message key="provider.btnEditDefaultQuickChartSize"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewEDocBrowserInDocumentReport');return false;"><bean:message key="provider.btnSetEDocBrowserInDocumentReport"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewEDocBrowserInMasterFile');return false;"><bean:message key="provider.btnSetEDocBrowserInMasterFile"/></a></td>
      </tr>
      <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewPatientNameLength');return false;"><bean:message key="provider.btnEditSetPatientNameLength"/></a></td>
      </tr>
       <tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../admin/displayDocumentDescriptionTemplate.jsp');return false;"><bean:message key="provider.btnSetDocumentDescriptionTemplate"/></a></td>
      </tr>
	  <tr>
          <td align="center"><a href=# onClick ="popupPage(500,900,'clients.jsp');return false;"><bean:message key="provider.btnEditClients"/></a></td>
      </tr>
    <tr>
        <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewDisplayDocumentAs');return false;"><bean:message key="provider.btnSetDisplayDocumentAs"/></a></td>
    </tr>
     <tr>
        <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewCobalt');return false;"><bean:message key="provider.btnSetCobalt"/></a></td>
    </tr>
    <% if(OscarProperties.getInstance().isPropertyActive("SINGLE_PAGE_CHART")){%>
    <tr>
    	<td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewHideOldEchartLinkInAppt');return false;"><bean:message key="provider.btnHideOldEchartLinkInAppt"/></a></td>
    </tr>
    <% } %>
    
	<tr>
          <td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewAppointmentCardPrefs');return false;"><bean:message key="provider.btnEditSetAppointmentCardPrefs"/></a></td>
      </tr>
<tr>
    	<td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewBornPrefs');return false;"><bean:message key="provider.btnViewBornPrefs"/></a></td>
    </tr>
    
	 <oscar:oscarPropertiesCheck property="util.erx.enabled" value="true">
	 	<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r">
        <tr>
        	<td align="center">
            	<a href=# onClick ="showHideERxPref();return false;"><bean:message key="provider.eRx.btnPrefLink"/></a>
            </td>
        </tr>
        <tr>
			<td align="center">
            	<div id="eRxPref">
                <%  
            	String eRxEnabledChecked="unchecked";
                String eRxTrainingModeChecked="unchecked";
                                        
				boolean eRxEnabled = false;
                String eRx_SSO_URL = "";
                String eRxUsername = "";
                String eRxPassword = "";
                String eRxFacility = "";
                boolean eRxTrainingMode = false;
                                                        
                if (providerPreference != null){                                       
                	eRxEnabled = providerPreference.isERxEnabled();
                    if(eRxEnabled) eRxEnabledChecked = "checked";
                                
                    eRx_SSO_URL = providerPreference.getERx_SSO_URL();
                    eRxUsername = providerPreference.getERxUsername();
                    eRxPassword = providerPreference.getERxPassword();
                    eRxFacility = providerPreference.getERxFacility();
                                
                    eRxTrainingMode = providerPreference.isERxTrainingMode();
                    if(eRxTrainingMode) eRxTrainingModeChecked = "checked";
                                
                    if(eRx_SSO_URL==null || "null".equalsIgnoreCase(eRx_SSO_URL)) eRx_SSO_URL=OscarProperties.getInstance().getProperty("util.erx.oscarerx_sso_url");
                    if(eRxUsername==null || "null".equalsIgnoreCase(eRxUsername)) eRxUsername="";
                    if(eRxPassword==null || "null".equalsIgnoreCase(eRxPassword)) eRxPassword="";
                    if(eRxFacility==null || "null".equalsIgnoreCase(eRxFacility)) eRxFacility="";
                }
                %>
                	<table class="eRxTableCenter">
                    	<tr>
                        	<td><bean:message key="provider.eRx.labelEnable"/>:</td>
                          	<td><input name="erx_enable" title="Enable the External Prescriber" type="checkbox" <%=eRxEnabledChecked%> /></td>
                        </tr>
                        <tr>
                          	<td><bean:message key="provider.eRx.labelUser"/>:</td>
                      		<td><input name="erx_username" type="text" value="<%=eRxUsername%>" title="Username to access the External Prescriber"/></td>
                        </tr>
                        <tr>
                          	<td><bean:message key="provider.eRx.labelPassword"/>:</td>
                          	<td><input name="erx_password" type="password" value="<%=eRxPassword%>" title="Password to access the External Prescriber" /></td>
                        <tr>
                        </tr>
                          	<td><bean:message key="provider.eRx.labelFacility"/>:</td>
                          	<td><input name="erx_facility" type="text" value="<%=eRxFacility%>" title="The Facility ID assigned to you by the External Prescriber" /><br></td>
                        <tr>
                        </tr>
                          	<td><bean:message key="provider.eRx.labelTrainingMode"/>:</td>
                          	<td><input name="erx_training_mode" type="checkbox" title="Enable Training Mode" <%=eRxTrainingModeChecked%> /></td>
                        </tr>
                        <tr>
                          	<td><bean:message key="provider.eRx.labelURL"/>:</td>
                          	<td><input name="erx_sso_url" type="text" value="<%=eRx_SSO_URL%>" title="The URL to access the Web Interface from OSCAR Rx" /></td>
                        </tr>
                        
                     </table>
                  </div>
              </td>
          </tr>
        </security:oscarSec>
  </oscar:oscarPropertiesCheck>

<oscar:oscarPropertiesCheck property="tickler_email_provider_enabled" value="true">
		<tr>
        	<td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewTicklerProviderEmailPrefs');return false;"><bean:message key="provider.btnTicklerEmailProviderPrefs"/></a></td>
        </tr>
</oscar:oscarPropertiesCheck>
      
 <tr>
    	<td align="center"><a href=# onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewDashboardPrefs');return false;"><bean:message key="provider.btnViewDashboardPrefs"/></a></td>
    </tr>

</table>
</FORM>

</body>
</html:html>
