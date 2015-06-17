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

<style>
	label {
		width: 125px;
	}
	.form-control-details {
		display: inline;
		width: 200px;
	}
	#photo {
		height: 150px;
		width: auto;
		cursor: pointer;
	}
</style>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<div class="col-lg-12" ng-hide="page.canRead">
	<bean:message key="demographic.demographiceditdemographic.accessDenied"/>
</div>
<div ng-show="page.canRead">
<div class="col-lg-8">
	<div class="alert alert-success" ng-show="page.saving">
		Saving...
	</div>
	
	<button type="button" class="btn {{needToSave()}}" ng-click="save()" ng-disabled="page.dataChanged<1">Save</button>
	
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
				Integrator <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li ng-show="page.integratorOffline"><a style="color:#FF5500">{{page.integratorStatusMsg}}</a></li>
				<li ng-hide="page.integratorOffline" title="{{page.integratorStatusMsg}}"><a style="color:{{page.integratorStatusColor}}" ng-click="integratorDo('ViewCommunity')">View Integrated Community</a></li>
				<li><a ng-click="integratorDo('Linking')">Manage Linked Clients</a></li>
				<div ng-show="page.conformanceFeaturesEnabled && !page.integratorOffline">
					<li><a ng-click="integratorDo('Compare')">Compare with Integrator</a></li>
					<li><a ng-click="integratorDo('Update')">Update from Integrator</a></li>
					<li><a ng-click="integratorDo('SendNote')">Send Note to Integrator</a></li>
				</div>
			</ul>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				<bean:message key="demographic.demographiceditdemographic.msgAppt"/> <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a class="hand-hover" ng-click="appointmentDo('ApptHistory')">Appointment History</a></li>
				<li><a class="hand-hover" ng-click="appointmentDo('WaitingList')"><bean:message key="demographic.demographiceditdemographic.msgWaitList"/></a></li>
			</ul>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				Billing <span class="caret"></span>
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
				<li><a class="hand-hover" ng-click="macPHRDo('ViewRecord')">View PHR Record</a></li>
			</ul>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-default" ng-click="exportDemographic()"><bean:message key="export"/></button>
		</div>
		
		<button type="button" class="btn btn-default" ng-click="loadHistoryList()" >History</button>
	</div>

	<button type="button" class="btn {{page.readyForSwipe}}" ng-show="page.workflowEnhance" ng-click="setSwipeReady()" title="Click for Card Swipe" style="padding-top: 0px; padding-bottom: 0px; font-size: small">
		{{page.swipecardMsg}}
	</button>
	<input type="text" id="swipecard" title="Click for Card Swipe" ng-model="page.swipecard" ng-show="page.workflowEnhance" ng-focus="setSwipeReady()" ng-blur="setSwipeReady('off')" ng-keypress="healthCardHandler($event.keyCode)" style="width:0px; border:none"/>

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
			<label title="Required Field">Date of Birth <span style="color:red">*</span></label>
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
			<select class="form-control form-control-details" title="Title" ng-model="page.demo.title" ng-options="tt.value as tt.label for tt in page.titles"/>
		</div>
		<div class="col-md-6">
			<label>SIN #</label>
			<input type="text" class="form-control form-control-details" placeholder="SIN #" title="SIN #" ng-model="page.demo.sin"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.msgDemoLanguage"/></label>
			<select class="form-control form-control-details" title="Language" ng-model="page.demo.officialLanguage" ng-options="ef.value as ef.label for ef in page.engFre"/>
		</div>
		<div class="col-md-6">
			<label>Spoken</label>
			<select class="form-control form-control-details" title="Spoken Language" ng-model="page.demo.spokenLanguage" ng-options="sl.value as sl.label for sl in page.spokenlangs">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.aboriginal"/></label>
			<select class="form-control form-control-details" title="Aboriginal" ng-model="page.aboriginal.value">
				<option value="Yes">Yes</option>
				<option value="No">No</option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formPHRUserName"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formPHRUserName"/>" title="<bean:message key="demographic.demographiceditdemographic.formPHRUserName"/>" ng-model="page.demo.myOscarUserName"/>
		</div>
		<div class="col-md-6"></div>
		<div class="col-md-6">
			<label/>
			<button type="button" class="btn" style="padding-top: 0px; padding-bottom: 0px; font-size: small" ng-click="macPHRDo('Register')" ng-show="page.demo.myOscarUserName==null || page.demo.myOscarUserName==''">Register for PHR</button>
		</div>
		<div class="col-md-11">
			<label><bean:message key="demographic.demographiceditdemographic.msgCountryOfOrigin"/></label>
			<select class="form-control form-control-details" title="<bean:message key="demographic.demographiceditdemographic.msgCountryOfOrigin"/>" ng-model="page.demo.countryOfOrigin" ng-options="cnty.value as cnty.label for cnty in page.countries" style="width:520px">
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
		<input type="text" placeholder="<bean:message key="demographic.demographiceditdemographic.formAddr"/>" title="<bean:message key="demographic.demographiceditdemographic.formAddr"/>" class="form-control form-control-details" ng-model="page.demo.address.address"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formCity"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formCity"/>" title="<bean:message key="demographic.demographiceditdemographic.formCity"/>" ng-model="page.demo.address.city"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formProcvince"/></label>
			<select class="form-control form-control-details" title="<bean:message key="demographic.demographiceditdemographic.formProcvince"/>" ng-model="page.demo.address.province" ng-options="pv.value as pv.label for pv in page.provinces"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formPostal"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formPostal"/>" title="Postal Code" ng-model="page.demo.address.postal" ng-change="checkPostal()" ng-blur="isPostalComplete()"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formEmail"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formEmail"/>" title="<bean:message key="demographic.demographiceditdemographic.formEmail"/>" ng-model="page.demo.email"/>
		</div>
		<div class="col-md-6">
			<label style="background-color: {{page.cellPhonePreferredColor}}" title="{{page.cellPhonePreferredMsg}}">
				<bean:message key="demographic.demographiceditdemographic.formPhoneC"/>
				<input type="checkbox" ng-model="page.preferredPhone" ng-change="setPreferredPhone()" ng-true-value="C" ng-disabled="isPhoneVoid(page.cellPhone)"/>
			</label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formPhoneC"/>" title="<bean:message key="demographic.demographiceditdemographic.formPhoneC"/>" ng-model="page.cellPhone" ng-change="checkPhone('C')"/>
		</div>
		<div class="col-md-6">
			<label style="background-color: {{page.homePhonePreferredColor}}" title="{{page.homePhonePreferredMsg}}">
				<bean:message key="demographic.demographiceditdemographic.formPhoneH"/>
				<input type="checkbox" ng-model="page.preferredPhone" ng-change="setPreferredPhone()" ng-true-value="H" ng-disabled="isPhoneVoid(page.homePhone)"/>
			</label>
			<span style="white-space:nowrap">
				<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formPhoneH"/>" title="<bean:message key="demographic.demographiceditdemographic.formPhoneH"/>" ng-model="page.homePhone" ng-change="checkPhone('H')" style="width:130px"/>
				<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.msgExt"/>" title="Home Phone Extension" ng-model="page.hPhoneExt.value" ng-change="checkPhone('HX')" style="width:65px"/>
			</span>
		</div>
		<div class="col-md-6">
			<label style="background-color: {{page.workPhonePreferredColor}}" title="{{page.workPhonePreferredMsg}}">
				<bean:message key="demographic.demographiceditdemographic.formPhoneW"/>
				<input type="checkbox" ng-model="page.preferredPhone" ng-change="setPreferredPhone()" ng-true-value="W" ng-disabled="isPhoneVoid(page.workPhone)"/>
			</label>
			<span style="white-space:nowrap">
				<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formPhoneW"/>" title="<bean:message key="demographic.demographiceditdemographic.formPhoneW"/>" ng-model="page.workPhone" ng-change="checkPhone('W')" style="width:130px"/>
				<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.msgExt"/>" title="Work Phone Extension" ng-model="page.wPhoneExt.value" ng-change="checkPhone('WX')" style="width:65px"/>
			</span>
		</div>
		<div class="col-md-11">
			<label><bean:message key="demographic.demographicaddrecordhtm.formPhoneComment"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographicaddrecordhtm.formPhoneComment"/>" title="<bean:message key="demographic.demographicaddrecordhtm.formPhoneComment"/>" ng-model="page.phoneComment.value" style="width:520px"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formNewsLetter"/></label>
			<select class="form-control form-control-details" title="<bean:message key="demographic.demographiceditdemographic.formNewsLetter"/>" ng-model="page.demo.newsletter">
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
		<div class="col-md-6">
			<label>
				<bean:message key="demographic.demographiceditdemographic.msgHealthIns"/>
				<span ng-show="page.HCValidation=='valid'" title="HIN Valid" style="font-size:large; color:#009900">&#10004;</span>
				<span ng-show="page.HCValidation=='invalid'" title="HIN Invalid" style="font-size:large; color:red">&#10008;</span>
				<span ng-show="page.HCValidation=='n/a'" title="Health Card Validation not ready" style="font-size:large; color:#ff5500">?</span>
				<button class="btn" title="Validate HIN #" ng-click="validateHC()" ng-hide="page.demo.hin==null || page.demo.hin=='' || page.demo.hcType!='ON'" style="padding: 0px 5px; font-size: small">Validate</button>
			</label>
			<span style="white-space:nowrap">
				<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.msgHealthIns"/>" title="<bean:message key="demographic.demographiceditdemographic.msgHealthIns"/>" ng-model="page.demo.hin" ng-change="checkHin()" style="width:140px; background-color:{{page.hinColor}}"/>
				<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formVer"/>" title="HIN Version" ng-model="page.demo.ver" ng-change="checkHinVer()" style="width:55px; background-color:{{page.verColor}}"/>
			</span>
		</div>
		<div class="col-md-6">
			<label>Health Card Type</label>
			<select class="form-control form-control-details" title="Health Card Type" ng-model="page.demo.hcType" ng-options="hct.value as hct.label for hct in page.provinces" style="background-color:{{page.hcTypeColor}}">
				<option value="" >--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Effective Date</label>
			<input id="effDate" ng-model="page.demo.effDate" type="text" class="form-control form-control-details" title="Health Card Effective Date" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.effDatePicker" ng-click="page.effDatePicker = true" placeholder="YYYY-MM-DD" style="background-color:{{page.effDateColor}}">
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formHCRenewDate"/></label>
			<input id="hcRenewDate" ng-model="page.demo.hcRenewDate" type="text" class="form-control form-control-details" title="Health Card Renew Date" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.hcRenewDatePicker" ng-click="page.hcRenewDatePicker = true" placeholder="YYYY-MM-DD" style="background-color:{{page.hcRenewDateColor}}">
		</div>
	</div>
	
	<div class="form-group row">
		<fieldset>
			<legend>Care Team</legend>
		</fieldset>
		<div class="col-md-6">
			<label>MRP</label>
			<select class="form-control form-control-details" title="MRP" ng-model="page.demo.providerNo" ng-options="mrp.providerNo as mrp.name for mrp in page.demo.doctors">
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
			<label>Midwife</label>
			<select class="form-control form-control-details" title="Midwife" ng-model="page.demo.midwife" ng-options="mw.providerNo as mw.name for mw in page.demo.midwives">
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
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formRefDoc"/>" title="<bean:message key="demographic.demographiceditdemographic.formRefDoc"/>" ng-model="page.referralDoc"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formRefDocNo"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.formRefDocNo"/>" title="<bean:message key="demographic.demographiceditdemographic.formRefDocNo"/>" ng-model="page.referralDocNo" style="width:130px"/>
			<button type="button" class="btn btn-sm" ng-click="showReferralDocList()"><bean:message key="demographic.demographiceditdemographic.btnSearch"/></button>
			<div style="position: absolute; right: 25px; z-index: 1; background-color: white" ng-show="page.showReferralDocList">
				<select class="form-control form-control-details" title="Pick a referral doctor" size="7" ng-model="page.referralDocObj" ng-options="rfd.label for rfd in page.demo.referralDoctors" ng-click="fillReferralDoc()">
					<option value="">--Pick a referral doctor--</option>
				</select>
			</div>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formRosterStatus"/></label>
			<select class="form-control form-control-details" title="<bean:message key="demographic.demographiceditdemographic.formRosterStatus"/>" ng-model="page.demo.rosterStatus" ng-options="rs.value as rs.label for rs in page.demo.rosterStatusList" style="width:150px"/>
			<button type="button" class="btn btn-sm" title="Add new roster status" ng-click="showAddNewRosterStatus()">Add</button>
			<div style="position: absolute; right: 55px; top: 0px; z-index: 1; background-color: #EEEEEE; padding: 5px;" ng-show="page.showAddNewRosterStatus">
				<input type="text" class="form-control" placeholder="New Roster Status" ng-model="page.newRosterStatus"/>
				<button type="button" class="btn" ng-click="addNewRosterStatus()">Add Status</button>
				<button type="button" class="btn" ng-click="showAddNewRosterStatus()"><bean:message key="global.btnCancel"/></button>
			</div>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.DateJoined"/></label>
			<input id="rosterDate" ng-model="page.demo.rosterDate" type="text" class="form-control form-control-details" title="<bean:message key="demographic.demographiceditdemographic.DateJoined"/>" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.rosterDatePicker" ng-click="page.rosterDatePicker = true" placeholder="YYYY-MM-DD">
		</div>
		<div class="col-md-6" ng-show="isRosterTerminated()">
			<label><bean:message key="demographic.demographiceditdemographic.RosterTerminationDate"/></label>
			<input id="rosterTerminationDate" ng-model="page.demo.rosterTerminationDate" type="text" class="form-control form-control-details" title="Roster Termination Date" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.rosterTerminationDatePicker" ng-click="page.rosterTerminationDatePicker = true" placeholder="YYYY-MM-DD">
		</div>
		<div class="col-md-11" ng-show="isRosterTerminated()">
			<label><bean:message key="demographic.demographiceditdemographic.RosterTerminationReason"/></label>
			<select class="form-control form-control-details" title="Roster Termination Reason" ng-model="page.demo.rosterTerminationReason" ng-options="rtr.value as rtr.label for rtr in page.rosterTermReasons" style="width:520px"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formPatientStatus"/></label>
			<select class="form-control form-control-details" title="Patient Status" ng-model="page.demo.patientStatus" ng-options="ps.value as ps.label for ps in page.demo.patientStatusList" ng-blur="checkPatientStatus()" style="width:150px"/>
			<button type="button" class="btn btn-sm" title="Add new patient status" ng-click="showAddNewPatientStatus()">Add</button>
			<div style="position: absolute; right: 55px; top: 0px; z-index: 1; background-color: #EEEEEE; padding: 5px;" ng-show="page.showAddNewPatientStatus">
				<input type="text" class="form-control" placeholder="New Patient Status" ng-model="page.newPatientStatus"/>
				<button type="button" class="btn" ng-click="addNewPatientStatus()">Add Status</button>
				<button type="button" class="btn" ng-click="showAddNewPatientStatus()">Cancel</button>
			</div>
		</div>
		<div class="col-md-6">
			<label>Status Date</label>
			<input id="patientStatusDate" ng-model="page.demo.patientStatusDate" type="text" class="form-control form-control-details" title="Patient Status Date" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.patientStatusDatePicker" ng-click="page.patientStatusDatePicker = true" placeholder="YYYY-MM-DD">
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formDateJoined1"/></label>
			<input id="dateJoined" ng-model="page.demo.dateJoined" type="text" class="form-control form-control-details" title="<bean:message key="demographic.demographiceditdemographic.formDateJoined1"/>" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.dateJoinedPicker" ng-click="page.dateJoinedPicker = true" placeholder="YYYY-MM-DD">
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.formEndDate"/></label>
			<input id="endDate" ng-model="page.demo.endDate" type="text" class="form-control form-control-details" title="<bean:message key="demographic.demographiceditdemographic.formEndDate"/>" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.endDatePicker" ng-click="page.endDatePicker = true" placeholder="YYYY-MM-DD">
		</div>
		<div class="col-md-6">
			<label>Chart Number</label>
			<input type="text" class="form-control form-control-details" placeholder="Chart Number" title="Chart Number" ng-model="page.demo.chartNo"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.cytolNum"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.cytolNum"/>" title="<bean:message key="demographic.demographiceditdemographic.cytolNum"/>" ng-model="page.cytolNum.value"/>
		</div>
	</div>
	
	<div class="form-group row">
		<fieldset>
			<legend>Additional Information</legend>
		</fieldset>
		<div class="col-md-6">
			<label style="width:150px">Archived Paper Chart</label>
			<select class="form-control form-control-details" title="Archived Paper Chart" ng-model="page.paperChartArchived.value" style="width:175px">
				<option value="NO"><bean:message key="demographic.demographiceditdemographic.paperChartIndicator.no"/></option>
				<option value="YES"><bean:message key="demographic.demographiceditdemographic.paperChartIndicator.yes"/></option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Archive Date</label>
			<input id="paperChartArchivedDate" ng-model="page.paperChartArchivedDate.value" type="text" class="form-control form-control-details" title="Paper Chart Archive Date" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.paperChartArchivedDatePicker" ng-click="page.paperChartArchivedDatePicker = true" placeholder="YYYY-MM-DD">
		</div>
		<div class="col-md-6">
			<label>Waiting List</label>
			<select class="form-control form-control-details" title="Waiting List" ng-model="page.demo.waitingListID" ng-options="wln.id as wln.name for wln in page.demo.waitingListNames">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographicaddarecordhtm.msgDateOfReq"/></label>
			<input id="onWaitingListSinceDate" ng-model="page.demo.onWaitingListSinceDate" type="text" class="form-control form-control-details" title="<bean:message key="demographic.demographicaddarecordhtm.msgDateOfReq"/>" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.onWaitingListSinceDatePicker" ng-click="page.onWaitingListSinceDatePicker = true" placeholder="YYYY-MM-DD">
		</div>
		<div class="col-md-11">
			<label><bean:message key="demographic.demographicaddarecordhtm.msgWaitListNote"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographicaddarecordhtm.msgWaitListNote"/>" title="<bean:message key="demographic.demographicaddarecordhtm.msgWaitListNote"/>" ng-model="page.demo.waitingListNote" style="width:520px"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.privacyConsent"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.privacyConsent"/>" title="<bean:message key="demographic.demographiceditdemographic.privacyConsent"/>" ng-model="page.privacyConsent.value"/>
		</div>
		<div class="col-md-6">
			<label><bean:message key="demographic.demographiceditdemographic.informedConsent"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.informedConsent"/>" title="<bean:message key="demographic.demographiceditdemographic.informedConsent"/>" ng-model="page.informedConsent.value"/>
		</div>
		<div class="col-md-6">
			<label style="width:195px"><bean:message key="demographic.demographiceditdemographic.usConsent"/></label>
			<input type="text" class="form-control form-control-details" placeholder="<bean:message key="demographic.demographiceditdemographic.usConsent"/>" title="<bean:message key="demographic.demographiceditdemographic.usConsent"/>" ng-model="page.usSigned.value" style="width:130px"/>
		</div>
		<div class="col-md-11">
			<label>Security Question</label>
			<select class="form-control form-control-details" title="Select a Security Question" ng-model="page.securityQuestion1.value" ng-options="sq.value as sq.label for sq in page.securityQuestions" style="width:520px">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-11">
			<label>Answer</label>
			<input type="text" class="form-control form-control-details" title="Answer to Security Question" ng-model="page.securityAnswer1.value" style="width:520px"/>
		</div>
	</div>
	</div>
</div>
<br/>

<div class="col-lg-4">
	<div class="clearfix">
	<img class="pull-left" id="photo" title="Click to upload photo" ng-click="launchPhoto()" ng-src="../imageRenderingServlet?source=local_client&clientId={{page.demo.demographicNo}}"/>
		<div class="pull-left" style="margin-left:5px;">
			<address>
	  			<strong>{{page.demo.lastName}}, {{page.demo.firstName}}</strong><br/>
		  		{{page.demo.address.address}}<br/>
		  		{{page.demo.address.city}}, {{page.demo.address.province}} {{page.demo.address.postal}}<br/>
	  			<abbr title="Phone" ng-show="page.preferredPhoneNumber">P:</abbr> {{page.preferredPhoneNumber}}
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
			<textarea ng-model="page.notes" class="form-control form-control-details" style="height:55px; width:100%;"></textarea>
		</fieldset>
		</div>
		<br/>
		<fieldset>
			<legend>
				<bean:message key="global.contacts"/>
				<button type="button" class="btn" ng-click="manageContacts()">Manage</button>
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
				Professional Contacts
				<button type="button" class="btn" ng-click="manageContacts()">Manage</button>
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
