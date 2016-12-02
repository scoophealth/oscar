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
#summary tr > td:first-child{
width:400px;
}
</style>
<div class="row">

<div class="col-xs-6">
	<!-- <h1>Personalize OSCAR</h1> -->
	<h2>User Settings</h2>
</div>
<div class="col-xs-6">
	<p class="bg-danger pull-right">
	<button type="button" class="btn btn-default btn-lg" onClick="window.open('../provider/providerpreference.jsp?provider_no=999998','prefs','width=715,height=680,scrollbars=yes')">
	  <span class="glyphicon glyphicon-cog"></span> Open Classic Preferences
	</button>
	</p>
</div>
</div>

<div class="row">

	<div class="col-xs-12">

<nav class="navbar navbar-default" role="navigation"
	style="padding-top: 0px;margin-bottom:3px;">
	<!-- Brand and toggle get grouped for better mobile display -->
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-ex1-collapse">
			<span class="sr-only">Toggle navigation</span> <span
				class="icon-bar"></span> <span class="icon-bar"></span> <span
				class="icon-bar"></span>
		</button>
		<a class="navbar-brand navbar-toggle pull-left" href="#">Select Module</a>
	</div>

	<!-- Collect the nav links, forms, and other content for toggling   removed data-toggle="tab"  from a ngclick changeTab3 -->
	<div class="collapse navbar-collapse navbar-ex1-collapse"
		style="padding-left: 0px;">
		<ul class="nav navbar-nav" id="myTabs">
			<li ng-repeat="tab in tabs" ng-model="currentTab" ng-class="{'active':isActive(tab)}"><a ng-click="changeTab(tab)" class="hand-hover">{{tab.displayName}}</a></li>
			
		</ul>
	</div>
	<!-- /.navbar-collapse -->
</nav>
		
	</div>

</div>

<div class="row" style="height:20px"></div>

<form class="form-horizontal">

<div class="row" ng-show="currentTab.path == 'general'" style="margin-left:10px">

	<div class="col-xs-3">
		<label>Override Clinic</label>
		<div class="form-group">
		  <label>Address:</label>
		  <input ng-model="pref.rxAddress" placeholder="Address" class="form-control" type="text">
		</div>
		<div class="form-group">
		  <label>City:</label>
		  <input ng-model="pref.rxCity" placeholder="City" class="form-control" type="text">
		</div>
		<div class="form-group">
		  <label>Province:</label>
		  <input ng-model="pref.rxProvince" placeholder="Province" class="form-control" type="text">
		</div>
		<div class="form-group">
		  <label>Postal Code:</label>
		  <input ng-model="pref.rxPostal" placeholder="Postal Code" class="form-control" type="text">
		</div>
		<div class="form-group">
		  <label>Phone:</label>
		  <input ng-model="pref.rxPhone" placeholder="Phone" class="form-control" type="text">
		</div>
		<div class="form-group">
		  <label>Fax:</label>
		  <input ng-model="pref.faxNumber" placeholder="Fax" class="form-control" type="text">
		</div>
	</div>

	<div class="col-xs-2">
	</div>


	<div class="col-xs-3">
			<div class="form-group">
		  <label ></label>
		  <div class="controls">
		    <button class="btn btn-default" ng-click="openChangePasswordModal()">Change Password</button>
			</div>	
		</div>			
	
	
	
		
		<div class="form-group">
		  <label>Tickler Window Provider:</label>
		  <select ng-model="pref.ticklerWarningProvider" class="form-control" ng-options="p.providerNo as p.name for p in providerList">
		  </select>
		</div>
		
		
		<div class="form-group">
		  <label >Workload Management:</label>
		  <select ng-model="pref.workloadManagement" class="form-control" ng-options="item.name as item.type for item in billingServiceTypesMod">
		  </select>
		</div>

		<div class="form-group">
		  <label >Enable Tickler Window:</label>
		  <div class="controls">
		    <label class="radio inline" for="radios-0">
		      <input ng-model="pref.newTicklerWarningWindow" name="radios" id="radios-0" value="enabled" type="radio">
		      Enable
		    </label>
		    <label class="radio inline" for="radios-1">
		      <input ng-model="pref.newTicklerWarningWindow" name="radios" id="radios-1" value="disabled" type="radio">
		      Disable
		    </label>  
		  </div>
		</div>
		
		<div class="form-group">
		  <label>Enable receiving Tickler Notification emails:</label>
		  <div class="controls">
		   <label class="radio inline" for="radios-t-1">
		      <input name="radios-t-1" id="radios-t-1" ng-model="pref.enableTicklerEmailProvider" ng-value="true" type="radio">
		      Enable
		    </label> 
		    
		    <label class="radio inline" for="radios-t-0">
		      <input name="radios-t-0" id="radios-t-0" ng-model="pref.enableTicklerEmailProvider" ng-value="false" type="radio">
		      Disable
		    </label>
		    
		  </div>
		</div>	
		
		
	
	</div>
</div> <!--  end row -->

<div class="row" ng-show="currentTab.path == 'persona'" style="margin-left:10px">
	<div class="col-xs-3">
	<h3>Dashboard</h3>
	  Enable New UI on login:<br>
	    <label class="radio-inline" for="radios-per-0">
	      <input ng-model="pref.useCobaltOnLogin" name="radios-per-0" id="radios-per-0" ng-value="true" type="radio">
	      Enable
	    </label>
	    <label class="radio-inline" for="radios-1">
	      <input ng-model="pref.useCobaltOnLogin" name="radios-per-0" id="radios-per-1" ng-value="false" type="radio">
	      Disable
	    </label>  

		<h3>Recent Patient List</h3>
		
		Number of recent patients to display:<br>
		<select ng-model="pref.recentPatients" class="form-control">
			<option value="1">1</option>
			<option value="2">2</option>
			<option value="3">3</option>
			<option value="4">4</option>
			<option value="5">5</option>
			<option value="6">6</option>
			<option value="7">7</option>
			<option value="8">8</option>
			<option value="9">9</option>
			<option value="10">10</option>
			<option value="11">11</option>
			<option value="12">12</option>
			<option value="13">13</option>
			<option value="14">14</option>
			<option value="15">15</option>
			<option value="16">16</option>
		</select>	
	</div><!-- Dashboard -->
</div><!-- persona -->
	
<div class="row" ng-show="currentTab.path == 'schedule'">
	<div class="col-xs-3">
		<div class="form-group">
		  <label>Start Hour (0-23):</label>
		  <input ng-model="pref.startHour" placeholder="Start Hour" class="form-control" type="text">
		</div>
		<div class="form-group">
		  <label>End Hour (0-23):</label>
		  <input ng-model="pref.endHour" placeholder="End Hour" class="form-control" type="text">
		</div>
		<div class="form-group">
		  <label>Period:</label>
		  <input ng-model="pref.period" placeholder="Period" class="form-control" type="text">
		</div>
		<div class="form-group">
		  <label>Group No:</label>
		  <input ng-model="pref.groupNo" placeholder="Group No" class="form-control" type="text">
		</div>
		<div class="form-group">
		  <label>Length of link and form names to display on appointment screen:</label>
		  <input ng-model="pref.appointmentScreenLinkNameDisplayLength" placeholder="Length" class="form-control" type="text">
		</div>
		<div class="form-group">
			<label>Hide Old Echart Link in appointment</label>
			  <div class="controls">
			    <label class="radio inline" for="radioh-0">
			      <input ng-model="pref.hideOldEchartLinkInAppointment" ng-value="true" id="radioh-0" type="radio">
			      Enable
			    </label>
			    <label class="radio inline" for="radioh-1">
			      <input ng-model="pref.hideOldEchartLinkInAppointment" ng-value="false" id="radioh-1" type="radio">
			      Disable
			    </label>  
			  </div>
		</div>				
	</div>
	<div class="col-xs-9">
		<label>Encounter Forms to display on appointment screen</label>
		<div style="height:10em;border:solid grey 1px;overflow:auto;white-space:nowrap">
			<span ng-repeat="f in encounterForms"><input type="checkbox" ng-model="f.checked" ng-change="selectEncounterForms()" >{{f.formName}}<br/></span>
			
		</div>
		<label>Eforms to display on appointment screen</label>
		<div style="height:10em;border:solid grey 1px;overflow:auto;white-space:nowrap">
			<span ng-repeat="f in eforms"><input type="checkbox" ng-model="f.checked" ng-change="selectEForms()">{{f.formName}}<br/></span>
		</div>		
		<label>Quick links to display on appointment screen</label>
		<div style="height:10em;border:solid grey 1px;overflow:auto;white-space:nowrap">
			<span ng-repeat="q in pref.appointmentScreenQuickLinks"><input type="checkbox" ng-model="q.checked">{{q.name}}<br/></span>
			
			
			<button class="btn-sm" ng-click="removeQuickLinks()">Remove</button>
		<button class="btn-sm" ng-click="openQuickLinkModal()">Add</button>
		</div>				
		
	</div>
</div>


<div class="row" ng-show="currentTab.path == 'billing'" style="margin-left:10px">
	<div class="col-xs-3">
		<div class="form-group">
		  <label >Default Billing Form:</label>
		  <select class="form-control"  ng-model="pref.defaultServiceType" ng-options="item.type as item.name for item in billingServiceTypesMod">
		  </select>
		</div>
		<div class="form-group">
		  <label>Default Diagnostic Code:</label>
		  <input  ng-model="pref.defaultDxCode" placeholder="" class="form-control" type="text">
		  <button class="btn" ng-disabled="true">Search</button>
		</div>
		
		<div class="form-group">
		  <label >Do Not Delete Previous Billing:</label>
		  <div class="controls">
		    <label class="radio inline" for="radiosx-0">
		      <input ng-model="pref.defaultDoNotDeleteBilling" name="radiosx" id="radiosx-0" ng-value="true"  type="radio">
		      Enable
		    </label>
		    <label class="radio inline" for="radiosx-1">
		      <input ng-model="pref.defaultDoNotDeleteBilling" name="radiosx" id="radiosx-1" ng-value="false" type="radio">
		      Disable
		    </label>  
		  </div>
		</div>		
	</div>
</div>



<div class="row" ng-show="currentTab.path == 'rx'" style="margin-left:10px">
	<div class="col-xs-3">
		<div class="form-group">
		  <label >RX3:</label>
		  <div class="controls">
		    <label class="radio inline" for="radios-rx-0">
		      <input name="radios-rx-0" id="radios-rx-0" ng-model="pref.useRx3" ng-value="true" type="radio">
		      Enable
		    </label>
		    <label class="radio inline" for="radios-rx-1">
		      <input name="radios-rx-1" id="radios-rx-1" ng-model="pref.useRx3" ng-value="false" type="radio">
		      Disable
		    </label>  
		  </div>
		</div>

		<div class="form-group">
		  <label >Show Patient's DOB:</label>
		  <div class="controls">
		    <label class="radio inline" for="radios-rx-2">
		      <input name="radios-rx-2" id="radios-rx-2" ng-model="pref.showPatientDob" ng-value="true" type="radio">
		      Enable
		    </label>
		    <label class="radio inline" for="radios-rx-3">
		      <input name="radios-rx-3" id="radios-rx-3" ng-model="pref.showPatientDob" ng-value="false" type="radio">
		      Disable
		    </label>  
		  </div>
		</div>
		
			
		<div class="form-group">
		  <label>Signature:</label>
		  <input ng-model="pref.signature" placeholder="Signature" class="form-control" type="text">
		</div>
		
		<div class="form-group">
		  <label>Default Quantity:</label>
		  <input ng-model="pref.rxDefaultQuantity" placeholder="Default Qty" class="form-control" type="text">
		</div>
		<div class="form-group">
		  <label>Page Size:</label>
		  <select ng-model="pref.rxPageSize" class="form-control" ng-options="p.value as p.label for p in pageSizes">
		  </select>
		</div>
		<div class="form-group">
		  <label>Rx Interaction Warning Level:</label>
		  <select ng-model="pref.rxInteractionWarningLevel" class="form-control" ng-options="p.value as p.label for p in rxInteractionWarningLevels">
		  </select>
		</div>

		<div class="form-group">
		  <label >Print QR Codes:</label>
		  <div class="controls">
		    <label class="radio inline" for="radios-rx-4">
		      <input name="radios-rx-4" id="radios-rx-4" ng-model="pref.printQrCodeOnPrescription" ng-value="true" type="radio">
		      Enable
		    </label>
		    <label class="radio inline" for="radios-rx-5">
		      <input name="radios-rx-5" id="radios-rx-5" ng-model="pref.printQrCodeOnPrescription" ng-value="false" type="radio">
		      Disable
		    </label>  
		  </div>
		</div>
		
	</div> <!-- end col -->
	
	<div class="col-xs-2">
	</div>
	
	<div class="col-xs-3">
		<h3>External Prescriber</h3>
		<div class="form-group">
		  <label ></label>
		  <div class="controls">
		    <label class="radio inline" for="radios-rx-6">
		      <input name="radios-rx-6" id="radios-rx-6" ng-model="pref.eRxEnabled" ng-value="true" type="radio">
		      Enable
		    </label>
		    <label class="radio inline" for="radios-rx-7">
		      <input name="radios-rx-7" id="radios-rx-7" ng-model="pref.eRxEnabled" ng-value="false" type="radio">
		      Disable
		    </label>  
		  </div>
		</div>
		<div class="form-group">
		  <label >Training Mode:</label>
		  <div class="controls">
		    <label class="radio inline" for="radios-rx-8">
		      <input name="radios-rx-8" id="radios-rx-8" ng-model="pref.eRxTrainingMode" ng-value="true" type="radio">
		      Enable
		    </label>
		    <label class="radio inline" for="radios-rx-9">
		      <input name="radios-rx-9" id="radios-rx-9" ng-model="pref.eRxTrainingMode" ng-value="false" type="radio">
		      Disable
		    </label>  
		  </div>
		</div>
		<div class="form-group">
		  <label>Username:</label>
		  <input ng-model="pref.eRxUsername" placeholder="Username" class="form-control" type="text">
		</div>
		<div class="form-group">
		  <label>Password:</label>
		  <input ng-model="pref.eRxPassword" placeholder="Password" class="form-control" type="text">
		</div>
		<div class="form-group">
		  <label>Clinic #:</label>
		  <input ng-model="pref.eRxFacility" placeholder="Clinic #" class="form-control" type="text">
		</div>
		<div class="form-group">
		  <label>URL:</label>
		  <input ng-model="pref.eRxURL" placeholder="URL" class="form-control" type="text">
		</div>						
	</div> <!-- end col -->
</div>


<div class="row" ng-show="currentTab.path == 'masterdemo'" style="margin-left:10px">
	<div class="col-xs-3">
		<div class="form-group">
		  <label>Default HC Type:</label>
		  <select class="form-control" ng-model="pref.defaultHcType">
				<option value="" >--</option>
				<option value="AB" >AB-Alberta</option>
				<option value="BC" >BC-British Columbia</option>
				<option value="MB" >MB-Manitoba</option>
				<option value="NB" >NB-New Brunswick</option>
				<option value="NL" >NL-Newfoundland Labrador</option>
				<option value="NT" >NT-Northwest Territory</option>
				<option value="NS" >NS-Nova Scotia</option>
				<option value="NU" >NU-Nunavut</option>
				<option value="ON" >ON-Ontario</option>
				<option value="PE" >PE-Prince Edward Island</option>
				<option value="QC" >QC-Quebec</option>
				<option value="SK" >SK-Saskatchewan</option>
				<option value="YT" >YT-Yukon</option>
				<option value="US" >US resident</option>
				<option value="US-AK" >US-AK-Alaska</option>
				<option value="US-AL" >US-AL-Alabama</option>
				<option value="US-AR" >US-AR-Arkansas</option>
				<option value="US-AZ" >US-AZ-Arizona</option>
				<option value="US-CA" >US-CA-California</option>
				<option value="US-CO" >US-CO-Colorado</option>
				<option value="US-CT" >US-CT-Connecticut</option>
				<option value="US-CZ" >US-CZ-Canal Zone</option>
				<option value="US-DC" >US-DC-District Of Columbia</option>
				<option value="US-DE" >US-DE-Delaware</option>
				<option value="US-FL" >US-FL-Florida</option>
				<option value="US-GA" >US-GA-Georgia</option>
				<option value="US-GU" >US-GU-Guam</option>
				<option value="US-HI" >US-HI-Hawaii</option>
				<option value="US-IA" >US-IA-Iowa</option>
				<option value="US-ID" >US-ID-Idaho</option>
				<option value="US-IL" >US-IL-Illinois</option>
				<option value="US-IN" >US-IN-Indiana</option>
				<option value="US-KS" >US-KS-Kansas</option>
				<option value="US-KY" >US-KY-Kentucky</option>
				<option value="US-LA" >US-LA-Louisiana</option>
				<option value="US-MA" >US-MA-Massachusetts</option>
				<option value="US-MD" >US-MD-Maryland</option>
				<option value="US-ME" >US-ME-Maine</option>
				<option value="US-MI" >US-MI-Michigan</option>
				<option value="US-MN" >US-MN-Minnesota</option>
				<option value="US-MO" >US-MO-Missouri</option>
				<option value="US-MS" >US-MS-Mississippi</option>
				<option value="US-MT" >US-MT-Montana</option>
				<option value="US-NC" >US-NC-North Carolina</option>
				<option value="US-ND" >US-ND-North Dakota</option>
				<option value="US-NE" >US-NE-Nebraska</option>
				<option value="US-NH" >US-NH-New Hampshire</option>
				<option value="US-NJ" >US-NJ-New Jersey</option>
				<option value="US-NM" >US-NM-New Mexico</option>
				<option value="US-NU" >US-NU-Nunavut</option>
				<option value="US-NV" >US-NV-Nevada</option>
				<option value="US-NY" >US-NY-New York</option>
				<option value="US-OH" >US-OH-Ohio</option>
				<option value="US-OK" >US-OK-Oklahoma</option>
				<option value="US-OR" >US-OR-Oregon</option>
				<option value="US-PA" >US-PA-Pennsylvania</option>
				<option value="US-PR" >US-PR-Puerto Rico</option>
				<option value="US-RI" >US-RI-Rhode Island</option>
				<option value="US-SC" >US-SC-South Carolina</option>
				<option value="US-SD" >US-SD-South Dakota</option>
				<option value="US-TN" >US-TN-Tennessee</option>
				<option value="US-TX" >US-TX-Texas</option>
				<option value="US-UT" >US-UT-Utah</option>
				<option value="US-VA" >US-VA-Virginia</option>
				<option value="US-VI" >US-VI-Virgin Islands</option>
				<option value="US-VT" >US-VT-Vermont</option>
				<option value="US-WA" >US-WA-Washington</option>
				<option value="US-WI" >US-WI-Wisconsin</option>
				<option value="US-WV" >US-WV-West Virginia</option>
				<option value="US-WY" >US-WY-Wyoming</option>
				<option value="OT">Other</option>		
		  </select>
		</div>		
		<div class="form-group">
		  <label>Default Sex:</label>
		  <select ng-model="pref.defaultSex" class="form-control">
				<option value="M">Male</option>
				<option value="F">Female</option>
				<option value="T">Transgender</option>
				<option value="O">Other</option>
				<option value="U">Undefined</option>
		  </select>
		</div>				
	</div>
</div>

<div class="row" ng-show="currentTab.path == 'consults'" style="margin-left:10px">
	<div class="col-xs-3">
		<div class="form-group">
		  <label>Consultation Cutoff Time Warning:</label>
		  <input ng-model="pref.consultationTimePeriodWarning" placeholder="Cutoff Time Warning" class="form-control" type="text">
		</div>
		<div class="form-group">
		  <label>Consultation Team Warning:</label>
		  <select class="form-control"  ng-model="pref.consultationTeamWarning" ng-options="item.value as item.label for item in teams">
		  </select>
		</div>
		<div class="form-group">
		  <label>Paste Format:</label>
		   <select ng-model="pref.consultationPasteFormat" class="form-control" ng-options="p.value as p.label for p in pasteFormats">
		  </select>	 
		</div>		
		
		<div class="form-group">
		  <label>Consult Letterhead Name Default:</label>
		   <select ng-model="pref.consultationLetterHeadNameDefault" class="form-control" ng-options="l.value as l.label for l in letterHeadNameDefaults">
		  </select>
		</div>			
	</div>
</div>

<div class="row" ng-show="currentTab.path == 'documents'" style="margin-left:10px">
	<div class="col-xs-3">
		<div class="form-group">
		  <label >Document Browser In Document Report:</label>
		  <div class="controls">
		    <label class="radio inline" for="radios-doc-0">
		      <input name="radios-doc-0" id="radios-doc-0" ng-model="pref.documentBrowserInDocumentReport" ng-value="true" type="radio">
		      Enable
		    </label>
		    <label class="radio inline" for="radios-doc-1">
		      <input name="radios-doc-1" id="radios-doc-1" ng-model="pref.documentBrowserInDocumentReport" ng-value="false" type="radio">
		      Disable
		    </label>  
		  </div>
		</div>
		<div class="form-group">
		  <label >Document Browser In Master File:</label>
		  <div class="controls">
		    <label class="radio inline" for="radios-doc-2">
		      <input name="radios-doc-2" id="radios-doc-2" ng-model="pref.documentBrowserInMasterFile" ng-value="true" type="radio">
		      Enable
		    </label>
		    <label class="radio inline" for="radios-doc-3">
		      <input name="radios-doc-3" id="radios-doc-3" ng-model="pref.documentBrowserInMasterFile" ng-value="false" type="radio">
		      Disable
		    </label>  
		  </div>
		</div>		
		<div class="form-group">
		  <label >Clinic Document Template</label>
		  <div class="controls">
		    	<button class="btn btn-default" ng-click="editDocumentTemplates()">Manage Document Templates</button>
		  </div>
		</div>						
	</div>
</div>

<div class="row" ng-show="currentTab.path == 'summary'" id="summary">
	<div class="container-fluid">
	<div class="col-lg-9">	
				
		<h3>Notes</h3>
		<table class="table table-hover table-condensed">
		<tbody>
		<tr>
			<td>
				CPP Single Line
			</td>
			<td>
			<div class="col-lg-12 col-md-12 col-sm-12">
				<label class="radio-inline" for="radios-enc-0">
			      <input name="radios-enc-0" id="radios-enc-0" ng-model="pref.cppSingleLine" ng-value="true" type="radio">
			      Enable
			    </label>
			    <label class="radio-inline" for="radios-enc-1">
			      <input name="radios-enc-1" id="radios-enc-1" ng-model="pref.cppSingleLine" ng-value="false" type="radio">
			      Disable
			    </label>
			 </div>
			</td>
		</tr>
		<tr>
			<td>
				Use Single View
			</td>
			<td>
			<div class="col-lg-12 col-md-12 col-sm-12">
			    <label class="radio-inline" for="radios-enc-2">
			      <input name="radios-enc-2" id="radios-enc-2" ng-model="pref.cmeNoteFormat" ng-value="true" type="radio">
			      Enable
			    </label>
			    <label class="radio-inline" for="radios-enc-3">
			      <input name="radios-enc-3" id="radios-enc-3" ng-model="pref.cmeNoteFormat" ng-value="false" type="radio">
			      Disable
			    </label> 
			</div>
			</td>
		</tr>

		<tr>
			<td>
				Stale Date<br>
				<small><em>Please set how many months in the past before a Case Management Note is fully visible e.g. Set to 6 will display fully all notes within the last 6 months</em></small>
			</td>
			<td>
				<div class="col-lg-4 col-md-6 col-sm-8">
				  <select ng-model="pref.cmeNoteDate" class="form-control" ng-options="p.value as p.label for p in staleDates">
				  </select>
				</div>
			</td>
		</tr>

		  

		<tr>
			<td>
				Default Quick Chart Size<br>
				<small><em>Enter the number of notes for quick chart size.</em></small>
			</td>
			<td>
				<div class="col-lg-4 col-md-6 col-sm-8">
					<input ng-model="pref.quickChartSize" class="form-control" type="text">
				</div>
			</td>
		</tr>
		<!--   
		<tr>
			<td>
				
			</td>
			<td>

			</td>
		</tr>		
		-->
		</tbody>
		</table>
 	
 		<h3>Patient Summary Viewable Items</h3> <!-- CPP & Summary Item Display -->
		<table class="table table-hover table-condensed">
		<tbody>
		<tr>
			<td>
				Enable Custom Summary<br>
				<small><em>Enabling this feature will allow you to to hide or display CPP and Summary Items.</em></small>
		  	</td>
			<td>
				<div class="col-lg-12 col-md-12 col-sm-12">
				    <label class="radio-inline" for="radios-enc-4">
				      <input name="radios-enc-4" id="radios-enc-4" ng-model="pref.summaryItemCustomDisplay" ng-value="true" type="radio">
				      Enable
				    </label>
				    <label class="radio-inline" for="radios-enc-5">
				      <input name="radios-enc-5" id="radios-enc-5" ng-model="pref.summaryItemCustomDisplay" ng-value="false" type="radio">
				      Disable
				    </label>
				 </div>
			 </td>
		</tr>
 		</tbody>
 		</table>

		<ng-include ng-if="pref.summaryItemCustomDisplay" src="'settings/partials/patientSummaryItems.html'"></ng-include>
  
	</div>
			<!-- <div class="form-group">
		  <label ></label>
		  <div class="controls">
		    
		  </div>	
		</div>
		
		<div class="form-group">
		  <label ></label>
		  <div class="controls">
		    
			</div>	
		</div>-->
	<div class="col-lg-3 col-sm-9">
		<div class="well">
			<h4>Classic Encounter Preferences:</h4>
			<a href="javascript:void(0)" ng-click="showDefaultEncounterWindowSizePopup()">Set Default Encounter Window Size</a><br>
			<a href="javascript:void(0)" ng-click="showProviderColourPopup()">Set Provider Colour</a><br>
			<a href="javascript:void(0)" ng-click="openConfigureEChartCppPopup()">Configure EChart CPP</a><br>
		</div>
	</div>
	
	</div><!-- container -->
</div><!--  row summary  -->

<div class="row" ng-show="currentTab.path == 'eforms'" style="margin-left:10px">
	<div class="col-xs-3">
		<div class="form-group">
		  <label>Favorite Group:</label>
		  <select ng-model="pref.favoriteFormGroup" class="form-control" ng-options="p.value as p.label for p in formGroupNames">
		  </select>
		</div>	
	</div>
</div>

<div class="row" ng-show="currentTab.path == 'inbox'" style="margin-left:10px">
	<div class="col-xs-3">
		<div class="form-group">
		  <label >Disable comment on acknowledgment:</label>
		  <div class="controls">
		    <label class="radio inline" for="radios-inb-0">
		      <input name="radios-inb-0" id="radios-inb-0" ng-model="pref.disableCommentOnAck" ng-value="true" type="radio">
		      Enable
		    </label>
		    <label class="radio inline" for="radios-inb-1">
		      <input name="radios-inb-1" id="radios-inb-1" ng-model="pref.disableCommentOnAck" ng-value="false" type="radio">
		      Disable
		    </label>  
		  </div>
		</div>	

	</div>
</div>

<div class="row" ng-show="currentTab.path == 'programs'" style="margin-left:10px">
	<div class="col-xs-3">
		<div class="form-group">
		  <label >Default PMM:</label>
		  <div class="controls">
		    <label class="radio inline" for="radios-pro-0">
		      <input name="radios-pro-0" id="radios-pro-0" ng-model="pref.defaultPmm" ng-value="true" type="radio">
		      Enable
		    </label>
		    <label class="radio inline" for="radios-pro-1">
		      <input name="radios-pro-1" id="radios-pro-1" ng-model="pref.defaultPmm" ng-value="false" type="radio">
		      Disable
		    </label>  
		  </div>
		</div>			
	</div>
</div>

<div class="row" ng-show="currentTab.path == 'integration'" style="margin-left:10px">
	<div class="col-xs-4">
		<div class="form-group">
		  <label>OLIS Default Reporting Laboratory:</label>
		  <select class="form-control"  ng-model="pref.olisDefaultReportingLab" ng-options="item.value as item.label for item in olisLabs">
		  </select>
		</div>		
		<div class="form-group">
		  <label>OLIS Default Exclude Reporting Laboratory:</label>
		  <select class="form-control"  ng-model="pref.olisDefaultExcludeReportingLab" ng-options="item.value as item.label for item in olisLabs">
		  </select>
		</div>		
	
		<div class="form-group">
		  <label ></label>
		  <div class="controls">
		    <button class="btn btn-default" ng-click="openManageAPIClientPopup()">Manage API Clients</button>
			</div>	
		</div>			
	</div>
	
	
	
	<div class="col-xs-4">
		<div class="form-group">
		  <label>MyDrugRef ID:</label>
		  <input ng-model="pref.myDrugRefId" placeholder="MyDrugRef ID" class="form-control" type="text">
		</div>
	
		<div class="form-group">
		  <label ></label>
		  <div class="controls">
		    <button class="btn btn-default" ng-click="openMyOscarUsernamePopup()">Set PHR Username</button>
			</div>	
		</div>		
			
		<div class="form-group">
		  <label>Use MyMeds:</label>
		  <div class="controls">
		    <label class="radio inline" for="radios-int-0">
		      <input name="radios-int-0" id="radios-int-0" ng-model="pref.useMyMeds" ng-value="true" type="radio">
		      Enable
		    </label>
		    <label class="radio inline" for="radios-int-1">
		      <input name="radios-int-1" id="radios-int-1" ng-model="pref.useMyMeds" ng-value="false" type="radio">
		      Disable
		    </label>  
		  </div>
		</div>	
	</div>
	<div class="col-xs-4">
		<label>Apps: <a ng-click="refreshAppList()">Refresh</a></label>
		<table class="table table-striped table-bordered">
			<tr>
				<th>App Name</th>
				<th>Status</th>
			</tr>
			<tr ng-repeat="app in loadedApps" >
				<td>{{app.name}}</td>
				<td ng-show="app.authenticated">{{app.authenticated}}</td>
				<td ng-hide="app.authenticated"><a ng-click="authenticate(app)">Authenticate</a></td>
			</tr>
		</table>
		
		<div class="form-group">
		  <label>BORN prompts in RBR/NDDS:</label>
		  <div class="controls">
		    <label class="radio inline" for="radios-int-0">
		      <input name="radios-int-0" id="radios-int-0" ng-model="pref.disableBornPrompts" ng-value="true" type="radio">
		      Disable
		    </label>
		    <label class="radio inline" for="radios-int-1">
		      <input name="radios-int-1" id="radios-int-1" ng-model="pref.disableBornPrompts" ng-value="false" type="radio">
		      Enable
		    </label>  
		  </div>
		</div>	
		
		<div class="form-group">
		  <label>Send your metrics to Common Provider Dashboard:</label>
		  <div class="controls">
		    <label class="radio inline" for="radios-int-0">
		      <input name="radios-int-0" id="radios-int-0" ng-model="pref.dashboardShare" ng-value="true" type="radio">
		      Enable
		    </label>
		    <label class="radio inline" for="radios-int-1">
		      <input name="radios-int-1" id="radios-int-1" ng-model="pref.dashboardShare" ng-value="false" type="radio">
		      Disable
		    </label>  
		  </div>
		</div>	
		
		
	</div>
</div>
 	</form>

<div style="height:20px"></div>
<button class="btn btn-primary" ng-click="save()">Save All Settings</button>
<button class="btn btn-primary" ng-click="cancel()">Cancel & Abandon Changes</button>



<!--  
<div style="height:20px"></div>
<pre>{{pref}}</pre>
-->
