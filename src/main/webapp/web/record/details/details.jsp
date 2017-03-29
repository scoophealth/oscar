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
<% String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user"); %>
<style>
	label {
		width: 125px;
	}
	.form-control-details {
		display: inline;
		width: 200px;
	}
	.flat-btn {
		padding-top: 0px;
		padding-bottom: 0px;
		font-size:x-small;
	}
	#photo {
		height: 150px;
		width: auto;
		cursor: pointer;
	}
</style>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<div class="col-md-12" ng-hide="page.canRead">
	<bean:message key="demographic.demographiceditdemographic.accessDenied"/>
</div>
<div ng-show="page.canRead">
<div class="col-md-8">
	<div class="alert alert-success" ng-show="page.saving">
		<bean:message key="web.record.details.saving"/>
	</div>
	
	<button type="button" class="btn {{needToSave()}}" ng-click="validateHCSave(true)" ng-disabled="page.dataChanged<1">Save</button>
	
	<div class="btn-group">
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				<bean:message key="demographic.demographicprintdemographic.btnPrint"/> <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a href="../report/GenerateEnvelopes.do?demos={{page.demo.demographicNo}}">PDF Envelope</a></li>
				<li><a class="hand-hover" ng-click="printLabel('PDFLabel')"><bean:message key="demographic.demographiceditdemographic.btnCreatePDFLabel"/></a></li>
				<li><a class="hand-hover" ng-click="printLabel('PDFAddress')"><bean:message key="demographic.demographiceditdemographic.btnCreatePDFAddressLabel"/></a></li>
				<li><a class="hand-hover" ng-click="printLabel('PDFChart')"><bean:message key="demographic.demographiceditdemographic.btnCreatePDFChartLabel"/></a></li>
				<li><a class="hand-hover" ng-click="printLabel('PrintLabel')"><bean:message key="demographic.demographiceditdemographic.btnPrintLabel"/></a></li>
				<li><a class="hand-hover" ng-click="printLabel('ClientLab')"><bean:message key="demographic.demographiceditdemographic.btnClientLabLabel"/></a></li>
			</ul>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" ng-show="page.integratorEnabled" style="color:{{page.integratorStatusColor}}" title="{{page.integratorStatusMsg}}">
				<bean:message key="web.record.details.integrator"/> <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li ng-show="page.integratorOffline"><a style="color:#FF5500">{{page.integratorStatusMsg}}</a></li>
				<li ng-hide="page.integratorOffline" title="{{page.integratorStatusMsg}}"><a style="color:{{page.integratorStatusColor}}" ng-click="integratorDo('ViewCommunity')"><bean:message key="web.record.details.viewIntegratedCommunity"/></a></li>
				<li><a ng-click="integratorDo('Linking')"><bean:message key="web.record.details.manageLinkedClients"/></a></li>
				<div ng-show="page.conformanceFeaturesEnabled && !page.integratorOffline">
					<li><a ng-click="integratorDo('Compare')"><bean:message key="web.record.details.compareWithIntegrator"/></a></li>
					<li><a ng-click="integratorDo('Update')"><bean:message key="web.record.details.updateFromIntegrator"/></a></li>
					<li><a ng-click="integratorDo('SendNote')"><bean:message key="web.record.details.sendNoteIntegrator"/></a></li>
				</div>
			</ul>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				<bean:message key="demographic.demographiceditdemographic.msgAppt"/> <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a class="hand-hover" ng-click="appointmentDo('ApptHistory')"><bean:message key="demographic.demographiceditdemographic.btnApptHist"/></a></li>
				<li><a class="hand-hover" ng-click="appointmentDo('WaitingList')"><bean:message key="demographic.demographiceditdemographic.msgWaitList"/></a></li>
			</ul>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				<bean:message key="admin.admin.billing" /><span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a class="hand-hover" ng-click="billingDo('BillingHistory')">{{page.billingHistoryLabel}}</a></li>
				<li><a class="hand-hover" ng-click="billingDo('CreateInvoice')"><bean:message key="demographic.demographiceditdemographic.msgCreateInvoice"/></a></li>
				<li><a class="hand-hover" ng-click="billingDo('FluBilling')"><bean:message key="demographic.demographiceditdemographic.msgFluBilling"/></a></li>
				<li><a class="hand-hover" ng-click="billingDo('HospitalBilling')"><bean:message key="demographic.demographiceditdemographic.msgHospitalBilling"/></a></li>
				<li><a class="hand-hover" ng-click="billingDo('AddBatchBilling')"><bean:message key="demographic.demographiceditdemographic.msgAddBatchBilling"/></a></li>
				<li><a class="hand-hover" ng-click="billingDo('AddINR')"><bean:message key="demographic.demographiceditdemographic.msgAddINR"/></a></li>
				<li><a class="hand-hover" ng-click="billingDo('BillINR')"><bean:message key="demographic.demographiceditdemographic.msgINRBill"/></a></li>
			</ul>
		</div>
		<div class="btn-group" ng-show="page.macPHRIdsSet">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				<bean:message key="global.personalHealthRecord"/> <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a class="hand-hover" ng-click="macPHRDo('SendMessage')"><bean:message key="demographic.demographiceditdemographic.msgSendMsgPHR"/></a></li>
				<li><a class="hand-hover" ng-click="macPHRDo('ViewRecord')"><bean:message key="web.record.details.viewPhrRecord"/></a></li>
			</ul>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				<bean:message key="export"/><span class="caret">
			</button>
			<ul class="dropdown-menu">
				<li><a class="hand-hover" ng-click="exportDemographic()"><bean:message key="export"/></a></li>
				<oscar:oscarPropertiesCheck property="E2EViewerEnabled" value="true">
					<security:oscarSec roleName="<%=roleName$%>" objectName="_demographicExport" rights="r" reverse="<%=false%>">
						<li><a class="hand-hover" ng-click="viewAsCDA()"><bean:message key="demographic.demographiceditdemographic.msgViewAsE2ECDA"/></a></li>
					</security:oscarSec>
				</oscar:oscarPropertiesCheck>
			</ul>
		</div>
		<button type="button" class="btn btn-default" ng-click="loadHistoryList()" ><bean:message key="web.record.details.history"/></button>
	</div>

	<button type="button" class="btn {{page.readyForSwipe}}" ng-show="page.workflowEnhance" ng-click="setSwipeReady()" title="Click for Card Swipe" style="padding-top: 0px; padding-bottom: 0px; font-size: small">
		{{page.swipecardMsg}}
	</button>
	<input type="text" id="swipecard" title="<bean:message key="web.record.details.clickCardSwipe"/>" ng-model="page.swipecard" ng-show="page.workflowEnhance" ng-focus="setSwipeReady()" ng-blur="setSwipeReady('off')" ng-keypress="healthCardHandler($event.keyCode)" style="width:0px; border:none"/>

	<div id="pd1" ng-click="checkAction($event)" ng-keypress="checkAction($event)">
	<div class="form-group row">
		<fieldset>
			<legend><bean:message key="demographic.demographiceditdemographic.msgDemographic"/></legend>
		</fieldset>
		<div class="col-md-6">
			<label title="Required Field"><bean:message key="demographic.demographiceditdemographic.formLastName"/> <span style="color:red">*</span></label>
			<input type="text" class="form-control form-control-details" placeholder="Family Name" title="Family Name" ng-model="page.demo.lastName" ng-change="formatLastName()" style="background-color:{{page.lastNameColor}}"/>
		</div>
		<div class="col-md-6">
			<label title="Required Field"><bean:message key="demographic.demographiceditdemographic.formFirstName"/> <span style="color:red">*</span></label>
			<input type="text" class="form-control form-control-details" placeholder="First Name" title="First Name" ng-model="page.demo.firstName" ng-change="formatFirstName()" style="background-color:{{page.firstNameColor}}"/>
		</div>
		<div class="col-md-6">
			<label title="Required Field"><bean:message key="web.record.details.dateOfBirth"/> <span style="color:red">*</span></label>
			<span style="white-space:nowrap">
				<input type="text" placeholder="YYYY" title="Birthday Year" class="form-control form-control-details" ng-model="page.demo.dobYear" ng-change="checkDate('DobY')" ng-blur="formatDate('DobY')" style="width:65px; background-color:{{page.dobYearColor}}" />
				<input type="text" placeholder="MM" title="Birthday Month" class="form-control form-control-details" ng-model="page.demo.dobMonth" ng-change="checkDate('DobM')" ng-blur="formatDate('DobM')" style="width:45px; background-color:{{page.dobMonthColor}}"/>
				<input type="text" placeholder="DD" title="Birthday Day" class="form-control form-control-details" ng-model="page.demo.dobDay" ng-change="checkDate('DobD')" ng-blur="formatDate('DobD')" style="width:45px; background-color:{{page.dobDayColor}}"/>
				({{page.demo.age}}y)
			</span>
		</div>
		<div class="col-md-6">
			<label title="Required Field"><bean:message key="demographic.demographiceditdemographic.formSex"/> <span style="color:red">*</span></label>
			<select class="form-control form-control-details" title="Sex" ng-model="page.demo.sex" ng-options="sexes.value as sexes.label for sexes in page.genders" style="background-color:{{page.sexColor}}"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.msgDemoTitle"/></label>
			<select class="form-control form-control-details" title="Title" ng-model="page.demo.title" ng-options="tt.value as tt.label for tt in page.titles">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="web.record.details.sin"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="web.record.details.sin"/>" title="<bean:message key="web.record.details.sin"/>" ng-model="page.demo.sin" ng-change="checkSin()" ng-blur="validateSin()"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.msgDemoLanguage"/></label>
			<select class="form-control form-control-details" title="Language" ng-model="page.demo.officialLanguage" ng-options="ef.value as ef.label for ef in page.engFre">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Spoken</label>
			<select class="form-control form-control-details" title="Spoken Language" ng-model="page.demo.spokenLanguage" ng-options="sl.value as sl.label for sl in page.spokenlangs">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.aboriginal"/></label>
			<select class="form-control form-control-details" title="Aboriginal" ng-model="page.demo.scrAboriginal">
				<option value="">--</option>
				<option value="Yes">Yes</option>
				<option value="No">No</option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.alias"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.alias"/>" title="<bean:message key="demographic.demographiceditdemographic.formPHRUserName"/>" ng-model="page.demo.alias"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formPHRUserName"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formPHRUserName"/>" title="<bean:message key="demographic.demographiceditdemographic.formPHRUserName"/>" ng-model="page.demo.myOscarUserName"/>
			<button type="button" class="btn flat-btn" ng-click="macPHRDo('Register')" ng-show="page.demo.myOscarUserName==null || page.demo.myOscarUserName==''"><bean:message key="demographic.demographiceditdemographic.msgRegisterPHR"/></button>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.msgCountryOfOrigin"/></label>
			<select class="form-control form-control-details" title="{{ (page.countries | filter : {value:page.demo.countryOfOrigin} : true)[0].label }}" ng-model="page.demo.countryOfOrigin" ng-options="cnty.value as cnty.label for cnty in page.countries">
				<option value="">--</option>
			</select>
		</div>
	</div>
 
	<div class="form-group row">
		<fieldset>
			<legend><bean:message key="demographic.demographiceditdemographic.msgContactInfo"/></legend>
		</fieldset>
		<div class="col-md-6">
		<label><bean:message key="demographic.demographiceditdemographic.formAddr"/></label>
		<input type="text" placeholder="<bean:message key="demographic.demographiceditdemographic.formAddr"/>" title="{{page.demo.address.address}}" class="form-control form-control-details" ng-model="page.demo.address.address"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formCity"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formCity"/>" title="<bean:message key="demographic.demographiceditdemographic.formCity"/>" ng-model="page.demo.address.city"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formProcvince"/></label>
			<select class="form-control form-control-details" title="<bean:message key="demographic.demographiceditdemographic.formProcvince"/>" ng-model="page.demo.address.province" ng-options="pv.value as pv.label for pv in page.provinces">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formPostal"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formPostal"/>" title="Postal Code" ng-model="page.demo.address.postal" ng-change="checkPostal()" ng-blur="isPostalComplete()"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formEmail"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formEmail"/>" title="<bean:message key="demographic.demographiceditdemographic.formEmail"/>" ng-model="page.demo.email" ng-blur="checkEmail()"/>
		</div>
		<div class="col-md-6">
			<label style="background-color: {{page.cellPhonePreferredColor}}" title="{{page.cellPhonePreferredMsg}}">
				<bean:message key="demographic.demographiceditdemographic.formPhoneC"/>
				<input type="checkbox" ng-model="page.demo.scrPreferredPhone" ng-change="setPreferredPhone()" ng-true-value="C" ng-disabled="isPhoneVoid(page.demo.scrCellPhone)"/>
			</label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formPhoneC"/>" title="<bean:message key="demographic.demographiceditdemographic.formPhoneC"/>" ng-model="page.demo.scrCellPhone" ng-change="checkPhone('C')"/>
		</div>
		<div class="col-md-6">
			<label style="background-color: {{page.homePhonePreferredColor}}" title="{{page.homePhonePreferredMsg}}">
				<bean:message key="demographic.demographiceditdemographic.formPhoneH"/>
				<input type="checkbox" ng-model="page.demo.scrPreferredPhone" ng-change="setPreferredPhone()" ng-true-value="H" ng-disabled="isPhoneVoid(page.demo.scrHomePhone)"/>
			</label>
			<span style="white-space:nowrap">
				<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formPhoneH"/>" title="<bean:message key="demographic.demographiceditdemographic.formPhoneH"/>" ng-model="page.demo.scrHomePhone" ng-change="checkPhone('H')" style="width:130px"/>
				<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.msgExt"/>" title="Home Phone Extension" ng-model="page.demo.scrHPhoneExt" ng-change="checkPhone('HX')" style="width:65px"/>
			</span>
		</div>
		<div class="col-md-6">
			<label style="background-color: {{page.workPhonePreferredColor}}" title="{{page.workPhonePreferredMsg}}">
				<bean:message key="demographic.demographiceditdemographic.formPhoneW"/>
				<input type="checkbox" ng-model="page.demo.scrPreferredPhone" ng-change="setPreferredPhone()" ng-true-value="W" ng-disabled="isPhoneVoid(page.demo.scrWorkPhone)"/>
			</label>
			<span style="white-space:nowrap">
				<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formPhoneW"/>" title="<bean:message key="demographic.demographiceditdemographic.formPhoneW"/>" ng-model="page.demo.scrWorkPhone" ng-change="checkPhone('W')" style="width:130px"/>
				<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.msgExt"/>" title="Work Phone Extension" ng-model="page.demo.scrWPhoneExt" ng-change="checkPhone('WX')" style="width:65px"/>
			</span>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographicaddrecordhtm.formPhoneComment"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographicaddrecordhtm.formPhoneComment"/>" title="{{page.demo.scrPhoneComment}}" ng-model="page.demo.scrPhoneComment"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formNewsLetter"/></label>
			<select class="form-control form-control-details" title="<bean:message key="demographic.demographiceditdemographic.formNewsLetter"/>" ng-model="page.demo.newsletter">
				<option value="">--</option>
				<option value="<bean:message key="demographic.demographicaddrecordhtm.formNewsLetter.optNo"/>"><bean:message key="demographic.demographicaddrecordhtm.formNewsLetter.optNo"/></option>
				<option value="<bean:message key="demographic.demographicaddrecordhtm.formNewsLetter.optPaper"/>"><bean:message key="demographic.demographicaddrecordhtm.formNewsLetter.optPaper"/></option>
				<option value="<bean:message key="demographic.demographicaddrecordhtm.formNewsLetter.optElectronic"/>"><bean:message key="demographic.demographicaddrecordhtm.formNewsLetter.optElectronic"/></option>
			</select>
		</div>
	</div>
	
	<div class="form-group row">
		<fieldset>
			<legend><bean:message key="demographic.demographiceditdemographic.msgHealthIns"/></legend>
		</fieldset>
		<div class="alert-warning" ng-show="page.HCValidation=='n/a'">
			Online Health Card Validation unavailable
		</div>
		<div class="col-md-6">
			<label>
				HIN
				<span ng-show="page.HCValidation=='valid'" title="HIN Valid" style="font-size:large; color:#009900">&#10004;</span>
				<span ng-show="page.HCValidation=='invalid'" title="HIN Invalid" style="font-size:large; color:red">&#10008;</span>
				<span ng-show="page.HCValidation=='n/a'" title="Online Health Card Validation unavailable" style="font-size:large; color:#ff5500">?</span>
				<button class="btn" title="Validate HIN #" ng-click="validateHC()" ng-hide="page.demo.hin==null || page.demo.hin=='' || page.demo.hcType!='ON'" style="padding: 0px 5px; font-size: small">Validate</button>
			</label>
			<span style="white-space:nowrap">
				<input type="text" class="form-control form-control-details" placeholder="HIN" title="Health Insurance Number" ng-model="page.demo.hin" ng-change="checkHin()" style="width:140px; background-color:{{page.hinColor}}"/>
				<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formVer"/>" title="HIN Version" ng-model="page.demo.ver" ng-change="checkHinVer()" style="width:55px; background-color:{{page.verColor}}"/>
			</span>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formHCType"/></label>
			<select class="form-control form-control-details" title="<bean:message key="demographic.demographiceditdemographic.formHCType"/>" ng-model="page.demo.hcType" ng-options="hct.value as hct.label for hct in page.provinces" style="background-color:{{page.hcTypeColor}}">
				<option value="" >--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formEFFDate"/></label>
			<input id="effDate" ng-model="page.demo.effDate" type="text" class="form-control form-control-details" 
				datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.effDatePicker" 
				ng-click="page.effDatePicker=true" title="YYYY-MM-DD" placeholder="<bean:message key="demographic.demographiceditdemographic.formEFFDate"/>" 
				style="background-color:{{page.effDateColor}}" ng-change="preventManualEffDate()"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formHCRenewDate"/></label>
			<input id="hcRenewDate" ng-model="page.demo.hcRenewDate" type="text" class="form-control form-control-details" 
				datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.hcRenewDatePicker" 
				ng-click="page.hcRenewDatePicker=true" title="YYYY-MM-DD" placeholder="<bean:message key="demographic.demographiceditdemographic.formHCRenewDate"/>" 
				style="background-color:{{page.hcRenewDateColor}}" ng-change="preventManualHcRenewDate()"/>
		</div>

		<oscar:oscarPropertiesCheck value="true" defaultVal="false" property="FIRST_NATIONS_MODULE">  
			<div class="col-md-6">
				<label><bean:message key="demographic.firstnations.bandNumber" /></label>
				<input type="text" id="statusNum" class="form-control form-control-details" ng-model="page.demo.scrStatusNum" />
			</div>
			
			<oscar:oscarPropertiesCheck value="false" defaultVal="true" property="showBandNumberOnly">
				
				<div class="col-md-6">
					<label><bean:message key="demographic.firstnations.bandName" /></label>
					<input type="text" id="fNationCom" class="form-control form-control-details" ng-model="page.demo.scrFNationCom" />
				</div>
				
				<div class="col-md-6">
					<label><bean:message key="demographic.firstnations.familyNumber" /></label>
					<input type="text" id="fNationFamilyNumber" class="form-control form-control-details" ng-model="page.demo.scrFNationFamilyNumber">	
				</div>
				
				<div class="col-md-6">
					<label><bean:message key="demographic.firstnations.familyPosition" /></label>
					<input type="text" id="fNationFamilyPosition" class="form-control form-control-details" ng-model="page.demo.scrFNationFamilyPosition">
				</div>
				
			</oscar:oscarPropertiesCheck>
			
			<oscar:oscarPropertiesCheck value="true" defaultVal="false" property="showBandNumberOnly">
				<input type="hidden" id="fNationCom" ng-model="page.demo.scrFNationCom" />
				<input type="hidden" id="fNationFamilyNumber" ng-model="page.demo.scrFNationFamilyNumber" />	
				<input type="hidden" id="fNationFamilyPosition" ng-model="page.demo.scrFNationFamilyPosition" />			
			</oscar:oscarPropertiesCheck>
			
			<div class="col-md-6">
				<label><bean:message key="demographic.firstnations.status" /></label>
				<select class="form-control form-control-details" ng-model="page.demo.scrEthnicity" >
					<option value="-1" ${ page.demo.scrEthnicity eq -1 ? 'selected' : '' } >Not Set</option>
					<option value="1" ${ page.demo.scrEthnicity eq 1 ? 'selected' : '' } >On-reserve</option>
					<option value="2" ${ page.demo.scrEthnicity eq 2 ? 'selected' : '' } >Off-reserve</option>
					<option value="3" ${ page.demo.scrEthnicity eq 3 ? 'selected' : '' } >Non-status On-reserve</option>
					<option value="4" ${ page.demo.scrEthnicity eq 4 ? 'selected' : '' } >Non-status Off-reserve</option>
					<option value="5" ${ page.demo.scrEthnicity eq 5 ? 'selected' : '' } >Metis</option>
					<option value="6" ${ page.demo.scrEthnicity eq 6 ? 'selected' : '' } >Inuit</option>
					<option value="11" ${ page.demo.scrEthnicity eq 11 ? 'selected' : '' } >Homeless</option>
					<option value="12" ${ page.demo.scrEthnicity eq 12 ? 'selected' : '' } >Out of Country Residents</option>
					<option value="13" ${ page.demo.scrEthnicity eq 13 ? 'selected' : '' } >Other</option>
				</select> 
			</div>
			
		</oscar:oscarPropertiesCheck>
		
	</div>
	
	<div class="form-group row">
		<fieldset>
			<legend><bean:message key="web.record.details.careTeam"/></legend>
		</fieldset>
		<div class="col-md-6">
			<label><bean:message key="web.record.details.mrp"/></label>
			<select class="form-control form-control-details" title="<bean:message key="web.record.details.mrp"/>" ng-model="page.demo.providerNo" ng-options="mrp.providerNo as mrp.name for mrp in page.demo.doctors">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formNurse"/></label>
			<select class="form-control form-control-details" title="<bean:message key="demographic.demographiceditdemographic.formNurse"/>" ng-model="page.demo.nurse" ng-options="ns.providerNo as ns.name for ns in page.demo.nurses">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="web.record.details.midwife"/></label>
			<select class="form-control form-control-details" title="<bean:message key="web.record.details.midwife"/>" ng-model="page.demo.midwife" ng-options="mw.providerNo as mw.name for mw in page.demo.midwives">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formResident"/></label>
			<select class="form-control form-control-details" title="<bean:message key="demographic.demographiceditdemographic.formResident"/>" ng-model="page.demo.resident" ng-options="res.providerNo as res.name for res in page.demo.doctors">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formRefDoc"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formRefDoc"/>" title="<bean:message key="demographic.demographiceditdemographic.formRefDoc"/>" ng-model="page.demo.scrReferralDoc"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formRefDocNo"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formRefDocNo"/>" title="<bean:message key="demographic.demographiceditdemographic.formRefDocNo"/>" ng-model="page.demo.scrReferralDocNo" style="width:130px" ng-change="checkReferralDocNo()"/>
			<button type="button" class="btn btn-sm" ng-click="showReferralDocList()"><bean:message key="demographic.demographiceditdemographic.btnSearch"/></button>
			<div style="position: absolute; right: 25px; z-index: 1; background-color: white" ng-show="page.showReferralDocList">
				<select class="form-control form-control-details" title="<bean:message key="web.record.details.pickReferralDoctor"/>" size="7" ng-model="page.referralDocObj" ng-options="rfd.label for rfd in page.demo.referralDoctors" ng-click="fillReferralDoc()">
					<option value="">--<bean:message key="web.record.details.pickReferralDoctor"/>--</option>
				</select>
			</div>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formRosterStatus"/></label>
			<select class="form-control form-control-details" title="<bean:message key="demographic.demographiceditdemographic.formRosterStatus"/>" ng-model="page.demo.rosterStatus" ng-options="rs.value as rs.label for rs in page.demo.rosterStatusList" style="width:150px">
				<option value="">--</option>
			</select>
			<button type="button" class="btn btn-sm" title="Add new roster status" ng-click="showAddNewRosterStatus()"><bean:message key="global.btnAdd"/></button>
			<button type="button" class="btn flat-btn" ng-click="showEnrollmentHistory()"><bean:message key="demographic.demographiceditdemographic.msgEnrollmentHistory"/></button>			<div style="position: absolute; right: 55px; top: 0px; z-index: 1; background-color: #EEEEEE; padding: 5px;" ng-show="page.showAddNewRosterStatus">
				<input type="text" class="form-control" placeholder="New Roster Status" ng-model="page.newRosterStatus"/>
				<button type="button" class="btn" ng-click="addNewRosterStatus()"><bean:message key="web.record.details.addStatus"/></button>
				<button type="button" class="btn" ng-click="showAddNewRosterStatus()"><bean:message key="global.btnCancel"/></button>
			</div>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.DateJoined"/></label>
			<input id="rosterDate" ng-model="page.demo.rosterDate" type="text" class="form-control form-control-details" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.rosterDatePicker" ng-click="page.rosterDatePicker=true" title="YYYY-MM-DD" placeholder="<bean:message key="demographic.demographiceditdemographic.DateJoined"/>" ng-change="preventManualRosterDate()"/>
		</div>
		<div class="col-md-6" ng-show="isRosterTerminated()">
			<label style="width:150px"><bean:message key="demographic.demographiceditdemographic.RosterTerminationDate"/></label>
			<input id="rosterTerminationDate" ng-model="page.demo.rosterTerminationDate" type="text" class="form-control form-control-details" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.rosterTerminationDatePicker" ng-click="page.rosterTerminationDatePicker=true" title="YYYY-MM-DD" placeholder="<bean:message key="web.record.details.rosterTerminationDate"/>" ng-change="preventManualRosterTerminationDate()" style="width:175px"/>
		</div>
		<div class="col-md-6" ng-show="isRosterTerminated()">
			<label style="width:150px"><bean:message key="demographic.demographiceditdemographic.RosterTerminationReason"/></label>
			<select class="form-control form-control-details" title="<bean:message key="web.record.details.rosterTerminationReason"/>" ng-model="page.demo.rosterTerminationReason" ng-options="rtr.value as rtr.label for rtr in page.rosterTermReasons" style="width:550px">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formPatientStatus"/></label>
			<select class="form-control form-control-details" title="Patient Status" ng-model="page.demo.patientStatus" ng-options="ps.value as ps.label for ps in page.demo.patientStatusList" ng-blur="checkPatientStatus()" style="width:150px">
				<option value="">--</option>
			</select>
			<button type="button" class="btn btn-sm" title="Add new patient status" ng-click="showAddNewPatientStatus()">Add</button>
			<div style="position: absolute; right: 55px; top: 0px; z-index: 1; background-color: #EEEEEE; padding: 5px;" ng-show="page.showAddNewPatientStatus">
				<input type="text" class="form-control" placeholder="New Patient Status" ng-model="page.newPatientStatus"/>
				<button type="button" class="btn" ng-click="addNewPatientStatus()">Add Status</button>
				<button type="button" class="btn" ng-click="showAddNewPatientStatus()">Cancel</button>
			</div>
		</div>
		<div class="col-md-6">
			<label style="width:140px"><bean:message key="demographic.demographiceditdemographic.PatientStatusDate"/></label>
			<input id="patientStatusDate" ng-model="page.demo.patientStatusDate" type="text" class="form-control form-control-details" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.patientStatusDatePicker" ng-click="page.patientStatusDatePicker=true" title="YYYY-MM-DD" placeholder="<bean:message key="demographic.demographiceditdemographic.PatientStatusDate"/>" ng-change="preventManualPatientStatusDate()" style="width:185px"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formDateJoined1"/></label>
			<input id="dateJoined" ng-model="page.demo.dateJoined" type="text" class="form-control form-control-details" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.dateJoinedPicker" ng-click="page.dateJoinedPicker = true" title="YYYY-MM-DD" placeholder="<bean:message key="demographic.demographiceditdemographic.formDateJoined1"/>" ng-change="preventManualDateJoined()"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formEndDate"/></label>
			<input id="endDate" ng-model="page.demo.endDate" type="text" class="form-control form-control-details" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.endDatePicker" ng-click="page.endDatePicker=true" title="YYYY-MM-DD" placeholder="<bean:message key="demographic.demographiceditdemographic.formEndDate"/>" ng-change="preventManualEndDate()"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formChartNo"/></label>
			<input type="text" class="form-control form-control-details" placeholder="Chart Number" title="Chart Number" ng-model="page.demo.chartNo" ng-change="checkChartNo()"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.cytolNum"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.cytolNum"/>" title="<bean:message key="demographic.demographiceditdemographic.cytolNum"/>" ng-model="page.demo.scrCytolNum" ng-change="checkCytoNum()"/>
		</div>
	</div>
	
	<div class="form-group row">
		<fieldset>
			<legend><bean:message key="web.record.details.addInformation"/></legend>
		</fieldset>
		<div class="col-md-6">
			<label style="width:150px"><bean:message key="web.record.details.archivedPaperChart"/></label>
			<select class="form-control form-control-details" title="Archived Paper Chart" ng-model="page.demo.scrPaperChartArchived" style="width:175px">
				<option value="">--</option>
				<option value="NO"><bean:message key="demographic.demographiceditdemographic.paperChartIndicator.no"/></option>
				<option value="YES"><bean:message key="demographic.demographiceditdemographic.paperChartIndicator.yes"/></option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.paperChartIndicator.dateArchived"/></label>
			<input id="paperChartArchivedDate" ng-model="page.demo.scrPaperChartArchivedDate" type="text" class="form-control form-control-details" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.paperChartArchivedDatePicker" ng-click="page.paperChartArchivedDatePicker=true" title="YYYY-MM-DD" placeholder="<bean:message key="demographic.demographiceditdemographic.paperChartIndicator.dateArchived"/>" ng-change="preventManualPaperChartArchivedDate()"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.msgWaitList"/></label>
			<select class="form-control form-control-details" title="Waiting List" ng-model="page.demo.waitingListID" ng-options="wln.id as wln.name for wln in page.demo.waitingListNames">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographicaddarecordhtm.msgDateOfReq"/></label>
			<input id="onWaitingListSinceDate" ng-model="page.demo.onWaitingListSinceDate" type="text" class="form-control form-control-details" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.onWaitingListSinceDatePicker" ng-click="page.onWaitingListSinceDatePicker=true" title="YYYY-MM-DD" placeholder="<bean:message key="demographic.demographicaddarecordhtm.msgDateOfReq"/>"  ng-change="preventManualOnWaitingListSinceDate()"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographicaddarecordhtm.msgWaitListNote"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographicaddarecordhtm.msgWaitListNote"/>" title="{{page.demo.waitingListNote}}" ng-model="page.demo.waitingListNote"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="web.record.details.privacyConsent"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.privacyConsent"/>" title="<bean:message key="demographic.demographiceditdemographic.privacyConsent"/>" ng-model="page.demo.scrPrivacyConsent"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="web.record.details.informedConsent"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.informedConsent"/>" title="<bean:message key="demographic.demographiceditdemographic.informedConsent"/>" ng-model="page.demo.scrInformedConsent"/>
		</div>
		<div class="col-md-6">
			<label style="width:195px"><bean:message key="demographic.demographiceditdemographic.usConsent"/></label>
			<input style="width:130px" type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.usConsent"/>" title="<bean:message key="demographic.demographiceditdemographic.usConsent"/>" ng-model="page.demo.scrUsSigned"/>
		</div>
		<div class="col-md-6">
			<label style="width:145px"><bean:message key="web.record.details.rxInteractionLevel"/></label>
			<select style="width:180px" class="form-control form-control-details" title="<bean:message key="web.record.details.rxInteractionLevel"/>" ng-model="page.demo.scrRxInteractionLevel" ng-options="r.value as r.name for r in page.rxInteractionLevels"/>
		</div>
		<div class="col-md-6" ng-show="page.showPrimaryCarePhysicianCheck">
			<label style="width:195px"><bean:message key="web.record.details.hasPrimaryCarePhysician"/></label>
			<select style="width:130px" class="form-control form-control-details" title="<bean:message key="web.record.details.hasPrimaryCarePhysician"/>" ng-model="page.demo.hasPrimaryCarePhysician" ng-options="r.value as r.name for r in page.hasPrimaryCarePhysician"/>
		</div>
		<div class="col-md-6" ng-show="page.showEmploymentStatus">
			<label style="width:145px"><bean:message key="web.record.details.employmentStatus"/></label>
			<select style="width:180px" class="form-control form-control-details" title="<bean:message key="web.record.details.employmentStatus"/>" ng-model="page.demo.employmentStatus" ng-options="r.value as r.name for r in page.employmentStatus"/>
		</div>
		<div class="clearfix"/>
		<div class="col-md-12">
			<br/>
			<label><bean:message key="web.record.details.securityQuestion"/></label>
			<select class="form-control form-control-details" title="<bean:message key="web.record.details.selectSecurityQuestion"/>" ng-model="page.demo.scrSecurityQuestion1" ng-options="sq.value as sq.label for sq in page.securityQuestions" style="width:400px">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-12">
			<label><bean:message key="web.record.details.answer"/></label>
			<input type="text" class="form-control form-control-details" title="<bean:message key="web.record.details.answerToSecurityQuestion"/>" placeholder="<bean:message key="web.record.details.answerToSecurityQuestion"/>" ng-model="page.demo.scrSecurityAnswer1" style="width:400px"/>
		</div>
	</div>
	</div>
</div>
<br/>

<div class="col-md-4">
	<div class="clearfix">
	<img class="pull-left" id="photo" title="Click to upload photo" ng-click="launchPhoto()" ng-src="../imageRenderingServlet?source=local_client&clientId={{page.demo.demographicNo}}"/>
		<div class="pull-left" style="margin-left:5px;">
			<address>
	  			<strong>{{page.demo.lastName}}, {{page.demo.firstName}}</strong><br/>
		  		{{page.demo.address.address}}<br/>
		  		{{page.demo.address.city}}, {{page.demo.address.province}} {{page.demo.address.postal}}<br/>
	  			<abbr title="Phone" ng-show="page.preferredPhoneNumber"><bean:message key="web.record.details.phoneShortHand"/></abbr> {{page.preferredPhoneNumber}}
			</address>
			<a ng-click="macPHRDo('Verification')" ng-show="page.macPHRIdsSet"><bean:message key="global.phr"/>{{page.macPHRVerificationLevel}}</a>
		</div>
	</div>
	<br/>
	<div>
		<div id="pd2" ng-click="checkAction($event)" ng-keypress="checkAction($event)">
		<fieldset>
			<legend><bean:message key="demographic.demographiceditdemographic.formAlert"/></legend>
			<textarea ng-model="page.demo.alert" class="form-control form-control-details" style="height:55px; width:100%; color:red;"></textarea>
		</fieldset>
		<br/>
		<fieldset>
			<legend><bean:message key="demographic.demographiceditdemographic.formNotes"/></legend>
			<textarea ng-model="page.demo.scrNotes" class="form-control form-control-details" style="height:55px; width:100%;"></textarea>
		</fieldset>
		</div>
		<br/>
		<fieldset>
			<legend>
				<bean:message key="global.contacts"/>
				<button type="button" class="btn" ng-click="manageContacts()"><bean:message key="web.record.details.manage"/></button>
			</legend>
			<div class="form-group" ng-repeat="dc in page.demo.demoContacts">
				<div class="col-md-12" style="font-weight:bold">{{dc.role}}</div>
				<div class="col-md-7" style="white-space:nowrap">{{dc.lastName}}, {{dc.firstName}}</div>
				<div class="col-md-5">{{dc.phone}}</div>
			</div>
		</fieldset>
		<br/>
		<fieldset>
			<legend>
				<bean:message key="web.record.details.proContacts"/>
				<button type="button" class="btn" ng-click="manageContacts()"><bean:message key="web.record.details.manage"/></button>
			</legend>
			<div class="form-group" ng-repeat="dc in page.demo.demoContactPros">
				<div class="col-md-12" style="font-weight:bold">{{dc.role}}</div>
				<div class="col-md-7" style="white-space:nowrap">{{dc.lastName}}, {{dc.firstName}}</div>
				<div class="col-md-5">{{dc.phone}}</div>
			</div>
		</fieldset>
	</div>
</div>
<br/>
</div>
