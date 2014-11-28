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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<form name="ticklerAddForm" novalidate>

<div class="modal-header">
    <h4><bean:message key="tickler.add.title" bundle="ui"/></h4>
</div>  
<div class="modal-body">
	<div class="row" ng-show="showErrors === true">
		<div class="col-xs-12">
			<ul>
				<li class="text-danger" ng-repeat="error in errors">{{error}}</li>
			</ul>
		</div>
	</div>

	<div class="row">
		<div class="col-xs-7">
				<div class="form-group">
				  <label>Patient:</label>
				 <input type="text" ng-model="tickler.demographicName" placeholder="<bean:message key="tickler.add.patient" bundle="ui"/>" 
				typeahead="pt.demographicNo as pt.name for pt in searchPatients($viewValue)" 
				typeahead-on-select="updateDemographicNo($item, $model, $label)"
				class="form-control">
				
				</div>
		
		</div>
		<div class="col-xs-5">
			<div class="row" ng-if="tickler.demographic != null">
				<div class="col-xs-3">
					<img width="60px" ng-src="../imageRenderingServlet?source=local_client&clientId={{tickler.demographic.demographicNo}}"/>
				</div>
				<div class="col-xs-9">
				<div>{{tickler.demographic.lastName}}, {{tickler.demographic.firstName}}</div>
				<div>{{tickler.demographic.hin}}</div>
				<div>{{tickler.demographic.dateOfBirth | date : 'yyyy-MM-dd'}}</div>
				</div>
			</div>
	
		</div>
	</div>

	<div class="row">
		<div class="col-xs-7">
				<div class="form-group">
				  <label>Assign to:</label>
				 <input type="text" ng-model="tickler.taskAssignedToName" placeholder="<bean:message key="tickler.add.provider" bundle="ui"/>" 
				typeahead="pt.providerNo as pt.name for pt in searchProviders($viewValue)" 
				typeahead-on-select="updateProviderNo($item, $model, $label)"
				class="form-control">
				
				</div>
			
		</div>
		
		<div class="col-xs-5">
		<div class="form-group">
				  <label><bean:message key="tickler.add.priority" bundle="ui"/>:</label>
				  <select ng-model="tickler.priority" ng-init="tickler.priority='Normal'" ng-options="p for p in priorities" class="form-control">
				  </select>
				</div>
		</div>
	</div>	
	
	

	<div class="row" ng-hide="showServiceDateEditor === true" ng-click="showServiceDateEditor=true">
		<div class="col-xs-6">
			<strong><bean:message key="tickler.add.serviceDate" bundle="ui"/>:</strong> {{tickler.serviceDateDate | date : 'yyyy-MM-dd'}} {{tickler.serviceDateTime | date : 'HH:mm'}}
			<br/><br/>
		</div>
	</div>
	
	<div class="row" ng-show="showServiceDateEditor === true">
		<div class="col-xs-6">
			<strong ng-click="showServiceDateEditor=false"><bean:message key="tickler.add.serviceDate" bundle="ui"/>:</strong>
			<datepicker ng-model="tickler.serviceDateDate" show-weeks="true" class="well well-sm"></datepicker>
			
		</div>
		<div class="col-xs-6">
			<timepicker ng-model="tickler.serviceDateTime"  hour-step="1" minute-step="1" show-meridian="true"></timepicker>

		</div>
	</div>	
	
	
	
	<div class="row">
		<div class="col-xs-12">
				<div class="form-group">
				  <label><bean:message key="tickler.add.templates" bundle="ui"/>:</label>
				  <select ng-model="tickler.suggestedTextId" ng-change="setSuggestedText()" ng-options="a.id as a.suggestedText for a in textSuggestions" class="form-control">
				  </select>
				</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-xs-12">
				<textarea ng-model="tickler.message" class="form-control" rows="6" required></textarea>
		</div>
	</div>

</div>
<div class="modal-footer">
    <button class="btn" ng-click="save()"><bean:message key="global.save" bundle="ui"/></button>
    <button class="btn" ng-click="close()"><bean:message key="global.close" bundle="ui"/></button>
</div>
</form>





