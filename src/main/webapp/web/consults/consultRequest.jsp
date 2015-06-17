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
	.clear {clear: both;}
	.inline {display: inline;}
	.btn-large {padding: 11px 19px; font-size: 17.5px;}
	.btn-tall {padding: 0px 10px; height: 42px;}
	.wrapper-action {
	    background-color: #FFFFFF;
	    border: 1px solid #FFFFFF;
	    bottom: 0;
	    opacity: 0.4;
	    padding-bottom: 4px;
	    padding-top: 4px;
	    position: fixed;
	    text-align: right;
	    width: 80%;
	    z-index: 999;
	}
	.wrapper-action:hover{
		background-color:#f5f5f5;
		border: 1px solid #E3E3E3;
		box-shadow: 0 1px 1px rgba(0, 0, 0, 0.05) inset;
		opacity:1;
		filter:alpha(opacity=100); /* For IE8 and earlier */
	}
	.attachment-modal-window .modal-dialog {
		width: 60%;
		min-width: 600px;
	}
</style>

<div class="col-md-12">
	<h2>Consultation Request</h2>
</div>
<div class="col-md-12 alert alert-success" ng-show="consultSaving">
	Saving...
</div>

<div id="left_pane" class="col-md-2" ng-show="consultReadAccess">
	<label class="control-label">Patient Details:</label>
	<div class="demographic">
		<p>{{consult.demographic.lastName}}, {{consult.demographic.firstName}} ({{consult.demographic.title}})</p>
		<p>DOB: {{consult.demographic.dateOfBirth | date:'yyyy-MM-dd'}} ({{consult.demographic.age.years}})</p> 		
		<p>Sex: {{consult.demographic.sexDesc}}</p> 
		<p>HIN: {{consult.demographic.hin}} - {{consult.demographic.ver}}</p> 
		<p>Address:</p> 
		<address>
		{{consult.demographic.address.address}}<br/>
		{{consult.demographic.address.city}}, {{consult.demographic.address.province}}, {{consult.demographic.address.postal}}<br>
		</address>
		<p>Phone (H): {{consult.demographic.phone}}</p>
		<p>Phone (W): {{consult.demographic.alternativePhone}}</p>
		<p>Phone (C): {{consult.demographic.cellPhone}}</p>
		<p>Email: {{consult.demographic.email}}</p>
		<p>MRP: {{consult.demographic.provider.firstName}}, {{consult.demographic.provider.lastName}}</p>
	</div>
	<br/>
	<div id="consult_status">
		<label class="control-label">Consultation Status:</label>
		<div class="form-group">
			<select class="form-control" ng-model="consult.status" ng-required="true" ng-options="status.value as status.name for status in statuses"/>
		</div>
	</div>
	<br/>
	<button type="button" class="btn btn-small btn-primary" ng-click="attachFiles()">Attachments</button>
	<ol style="padding-left:20px;">
		<li ng-repeat="attachment in consult.attachments">
			<a ng-click="openAttach(attachment)" title="{{attachment.displayName}}">{{attachment.shortName}}</a>
		</li>
	</ol>
</div><!-- Left pane End -->

<div id="right_pane" class="col-md-10" ng-show="consultReadAccess">
	<div class="col-md-6"><!-- Letterhead -->
		<h4>Letterhead:</h4>
		<div class="well">
			<div>
				<select id="letterhead" class="form-control" 
						ng-model="consult.letterheadName" 
						ng-options="letterhead.id as letterhead.name for letterhead in consult.letterheadList"
						ng-change="changeLetterhead()">
				</select>
			</div>
			<p class="letterheadDetails">
				<address>
					<label>Address:</label> <span style="white-space:nowrap">{{consult.letterheadAddress}}</span><br/>
					<label>Phone:</label> {{consult.letterheadPhone}} <br/>
					<label>Fax:</label>
					<select id="letterheadFax" class="form-control inline" style="width: auto;"
							ng-model="consult.letterheadFax"
							ng-options="fax.faxNumber as fax.faxUser for fax in consult.faxList">
					</select>
				</address>
			</p>
		</div>
	</div><!-- Letterhead End-->
	<div class="col-md-6"><!-- Specialist -->
		<h4>Specialist:</h4>
		<div class="well">
			<div>
				<select id="serviceId" class="form-control inline" style="width: 35%;"
						title="Service" 
						ng-model="consult.serviceId" 
						ng-options="service.serviceId as service.serviceDesc for service in consult.serviceList"
						ng-required="true"
						ng-change="changeService()">
				</select>
				<select id="specId" class="form-control inline" style="width: 50%;"
						title="Consultant"
						ng-model="consult.professionalSpecialist"
						ng-options="spec.name for spec in specialists">
				</select>
			</div>
			<p class="specialistDetails">
				<address>
					<label>Address:</label> {{consult.professionalSpecialist.streetAddress}}<br/>
					<label>Phone:</label> {{consult.professionalSpecialist.phoneNumber}} <br/>
					<label>Fax:</label> {{consult.professionalSpecialist.faxNumber}}<br />
				</address>
			</p>
		</div>
	</div><!-- Specialist End -->
	<div class="clear"></div>
	
	<div class="col-md-12"><!-- Referral -->
		<div class="well">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">Referral Date:</label>
					<input id="dp-referralDate" type="text" class="form-control inline" style="width:60%" ng-model="consult.referralDate" placeholder="Referral Date" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.refDatePicker" ng-click="page.refDatePicker=true"/>
				</div>
				<div class="form-group">
					<label class="control-label">Urgency:</label>
					<select id="urgency" class="form-control inline" style="width:70%" ng-model="consult.urgency" ng-required="true" ng-options="urgency.value as urgency.name for urgency in urgencies"/>
				</div>
				<div class="form-group">
					<label class="control-label">Send To:</label>
					<select id="sendTo" class="form-control inline" style="width:70%" ng-model="consult.sendTo" ng-required="true" ng-options="sendTo for sendTo in consult.sendToList"/>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">Referrer Instructions:</label>
					<textarea cols="80" rows="4" class="form-control" readOnly>{{consult.professionalSpecialist.annotation}}</textarea>
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</div><!-- Referral End -->
	
	<div class="col-md-12"><!-- Appointment -->
		<div class="well" id="appointmentDetail">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">Appointment Date:</label>
					<input id="dp-appointmentDate" type="text" class="form-control inline" style="width:50%" ng-model="consult.appointmentDate" placeholder="Appointment Date"  datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.aptDatePicker" ng-click="page.aptDatePicker=true" ng-disabled="consult.patientWillBook"/>
				</div>
				<div class="form-group">
					<label class="control-label">Appointment Time:</label>
					<span style="white-space:nowrap;">
						<select class="form-control inline" style="width:20%;" 
								ng-model="consult.appointmentHour"
								ng-options="hour for hour in hours"
								ng-change="changeAppointmentTime()" ng-disabled="consult.patientWillBook">
						</select> :
						<select class="form-control inline" style="width:20%;" 
								ng-model="consult.appointmentMinute"
								ng-options="minute for minute in minutes"
								ng-change="changeAppointmentTime()" ng-disabled="consult.patientWillBook">
						</select>
					</span>
				</div>
				<div class="form-group">
					<label class="control-label">Last Follow-up Date:</label>
					<input id="dp-followUpDate" type="text" class="form-control inline" style="width:50%" ng-model="consult.followUpDate" placeholder="Follow Up Date"  datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.lfdDatePicker" ng-click="page.lfdDatePicker=true"/>
				</div>
				<div>
					<label class="control-label">
						<input type="checkbox" id="willBook" ng-model="consult.patientWillBook"/>
						Patient Will Book
					</label>
				</div>
			</div>
			<div class="col-md-6">
				<label class="control-label">Appointment Notes:</label>
				<div class="form-group">
					<textarea cols="80" rows="5" class="form-control" ng-model="consult.statusText"></textarea>
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</div><!-- Appointment End -->
	<div class="col-md-12"><!-- Reason for Consultation -->
		<h4>Reason for Consultation:</h4>
		<div class="well">
			<textarea cols="120" rows="4" class="form-control" ng-model="consult.reasonForReferral"></textarea>
		</div>
	</div><!-- Reason End -->
	<div class="clear"></div>
	
	<div id="clinical-note" class="col-md-6"><!-- Clinic Notes -->
		<div>
			<h4>Pertinent clinical information:</h4>
			<p>
				<button type="button" class="btn btn-tall btn-success" ng-click="getFamilyHistory('clinicalInfo');">Family<br/>History</button>
				<button type="button" class="btn btn-tall btn-success" ng-click="getMedicalHistory('clinicalInfo');">Medical<br/>History</button>
				<button type="button" class="btn btn-tall btn-success" ng-click="getOngoingConcerns('clinicalInfo');">Ongoing<br/>Concerns</button>
				<button type="button" class="btn btn-tall btn-success" ng-click="getOtherMeds('clinicalInfo');">Other<br/>Meds</button>
				<button type="button" class="btn btn-tall btn-success" ng-click="getReminders('clinicalInfo');">Reminders</button>
			</p>					
			<div class="well">
				<div>
					<textarea id="clinicalInfo" cols="80" rows="5" class="form-control" placeholder="Use the buttons above to insert data from the patients chart"
						ng-model="consult.clinicalInfo"></textarea>
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	
	<div id="concurrent-problem" class="col-md-6"><!-- Concurrent Problem -->
		<div>
			<h4>Significant Concurrent Problems:</h4>
			<p>
				<button type="button" class="btn btn-tall btn-success" ng-click="getFamilyHistory('concurrentProblems');">Family<br/>History</button>
				<button type="button" class="btn btn-tall btn-success" ng-click="getMedicalHistory('concurrentProblems');">Medical<br/>History</button>
				<button type="button" class="btn btn-tall btn-success" ng-click="getOngoingConcerns('concurrentProblems');">Ongoing<br/>Concerns</button>
				<button type="button" class="btn btn-tall btn-success" ng-click="getOtherMeds('concurrentProblems');">Other<br/>Meds</button>
				<button type="button" class="btn btn-tall btn-success" ng-click="getReminders('concurrentProblems');">Reminders</button>
			</p>						
			<div class="well">
				<div>
					<textarea id="concurrentProblems" cols="80" rows="5" class="form-control" placeholder="Use the buttons above to insert data from the patients chart"
						ng-model="consult.concurrentProblems"></textarea>
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	<div class="clear"></div>
	
	<div class="col-md-6"><!-- Alergies / Current Medications -->
		<h4>Allergies:</h4>
		<div class="well">
			<textarea cols="80" rows="5" class="form-control" ng-model="consult.allergies"></textarea>
		</div>
	</div><!-- Alergies End -->	
	<div class="col-md-6">
		<h4>Current Medications: <button type="button" class="btn btn-success" style="padding:0px 10px;" ng-click="getOtherMeds('currentMeds');">Other Meds</button></h4>
		
		<div class="well">
			<textarea id="currentMeds" cols="80" rows="5" class="form-control" ng-model="consult.currentMeds" placeholder="Use the button above to insert Other Meds data from the patients chart"></textarea>
		</div>
	</div><!-- Current Medications End -->	
	<div class="clear"></div>
</div><!-- Right pane End -->

<div class="wrapper-action" ng-show="consultReadAccess"><!-- Action Buttons -->
	<button type="button" class="btn btn-large btn-warning action" ng-click="printPreview()" ng-show="consult.id!=null && consultChanged<=0">Print Preview</button>&nbsp;
	<button type="button" class="btn btn-large btn-warning action" ng-click="sendFax()" ng-show="consult.id!=null && consultChanged<=0">Send Fax</button>&nbsp;
	<button type="button" class="btn btn-large btn-warning action" ng-click="eSend()" ng-show="eSendEnabled && consult.id!=null && consultChanged<=0">Send Electronically</button>&nbsp;
	<button type="button" class="btn btn-large btn-primary action" ng-click="save()" ng-show="consultChanged>0">Save</button>&nbsp;
	<button type="button" class="btn btn-large btn-default action" ng-click="close()">Close</button>&nbsp;
</div>

<div ng-show="consultReadAccess != null && consultReadAccess == false"
	class="col-lg-12">
	<h3 class="text-danger">
		<span class="glyphicon glyphicon-warning-sign"></span>You don't have access to view consult
	</h3>
</div>
