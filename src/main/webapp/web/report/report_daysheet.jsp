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
<h1>DaySheet</h1>
<style>
.bg-danger {
  background-color: #f2dede;
}
a.bg-danger:hover {
  background-color: #e4b9b9;
}
</style>
<div ng-controller="ReportDaySheetCtrl">
<div class="row">
	<form role="form">

		<div class="col-md-4" >
		
			<div class="form-group">
				<label>Report Type:</label>
				<select ng-model="params.type" class="form-control">
					<option value="">Choose a Type</option>
					<option value="all">All Appointments</option>
					<option value="all-nr">All Appointments (Non-Rostered Only)</option>
					<option value="new">New Appointments Only</option>
					<option value="lab">Lab</option>
					<option value="billing">Billing</option>
					<option value="tab">Tabular</option>
				</select>
			</div>
		
			<div class="bg-danger" ng-show="params.type=='new'">
				<h4><span class="glyphicon glyphicon-warning-sign"></span> Are you sure you want to see new appointments only? (The new appointments status would be changed to 'old')</h4>
			</div>
			<div class="form-group" ng-show="params.type == 'all' || params.type== 'all-nr' || params.type== 'new' || params.type== 'tab'">
				<label>Provider:</label> 
				
				<div class="input-group">
					<input type="text"
						ng-model="data.providerNo" placeholder="Provider"
						typeahead="pt.providerNo as pt.name for pt in searchProviders($viewValue)"
						typeahead-on-select="updateProviderNo($item, $model, $label)"
						class="form-control"/>
						<span class="input-group-addon"><span class="glyphicon glyphicon-remove" ng-click="params.providerNo='';data.providerNo=''"></span></span>
					</div>
			</div>

			 <div class="form-group"  ng-show="params.type == 'all' || params.type== 'all-nr' || params.type== 'new' || params.type== 'lab' || params.type== 'billing' || params.type== 'tab'">
			    <label for="startDate">Start Date</label>
			     <input ng-model="params.startDate" type="text" id="startDate" name="startDate" class="form-control" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="data.isOpen" ng-click="data.isOpen = true" placeholder="">
			  </div>
			  <div class="form-group"  ng-show="params.type == 'all' || params.type== 'all-nr'">
			    <label for="endDate">End Date</label>
			    <input ng-model="params.endDate" type="text" id="endDate" name="endDate" class="form-control" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="data.isOpen2" ng-click="data.isOpen2 = true" placeholder="">
			  </div>
			  
			  		
		</div>

		<div class="col-md-1" ></div>		
		
		<div class="col-md-4" >
			<div class="form-group"  ng-show="params.type == 'all' || params.type== 'all-nr'">
				<label for="startTime">Start Time:</label>
				<timepicker ng-model="params.startTime" id="startTime" name="startTime" hour-step="1" minute-step="15" show-meridian="true"></timepicker>
			</div>
			
			<div class="form-group"  ng-show="params.type == 'all' || params.type== 'all-nr'">
				<label for="endTime">End Time:</label>
				<timepicker ng-model="params.endTime" id="endTime" name="endTime" hour-step="1" minute-step="15" show-meridian="true"></timepicker>
			</div>
						
		</div>
		
	</form>
</div>

<div class="row">
	<div class="col-md-12">
	<button ng-click="generateReport()" type="submit" class="btn btn-primary">Submit</button>
	<button ng-click="reset()" type="submit" class="btn btn-default">Reset</button>
	</div>
</div>

</div>