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
	<div class="modal-content">
		<div class="modal-header">
			<h3 class="modal-title">Add New Record</h3>
		</div>

		<div class="modal-body" ng-hide="hasRight">
			You have no right to create new patient!
		</div>		
		<div class="modal-body" ng-show="hasRight">
		<button style="margin-top: -10px;" type="button" class="close" data-dismiss="modal" aria-hidden="true" ng-click="cancel()"></button>
			<form class="form-horizontal" role="form" name="newDemographic">
			  <div class="form-group">
			    <label for="lastName" class="col-sm-2 control-label">Name</label>
			    <div class="col-sm-10">
			      <input type="text" class="form-control" id="lastName" placeholder="Last Name" name="lastName" ng-model="demographic.lastName" required>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label"></label>
			    <div class="col-sm-10">
			      <input type="text" class="form-control" placeholder="First Name" ng-model="demographic.firstName" required>
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="birthYear" class="col-sm-2 control-label">Birth</label>
			    <div class="col-sm-2">
			      <input type="text" class="form-control" id="birthYear" placeholder="Year" ng-model="demographic.dobYear" required>
			    </div>
			    <div class="col-sm-2">
			      <input type="text" class="form-control" placeholder="Month" ng-model="demographic.dobMonth" required>
			    </div>
			    <div class="col-sm-2">
			      <input type="text" class="form-control" placeholder="Day" ng-model="demographic.dobDay" required>
			    </div>
			
			  </div> 
			  <div class="form-group">
			    <label class="col-sm-2 control-label">Sex</label>
			    <div class="col-sm-5">
			    	<input type="checkbox" ng-model="demographic.sex" ng-true-value="M"/> Male
			    	<br/>
			    	<input type="checkbox" ng-model="demographic.sex" ng-true-value="F"/> Female
			    </div>
			  </div> 
			</form>
		</div>
		
		<div class="modal-footer">
			<button ng-show="hasRight" ng-click="saver(newDemographic)" type="button" class="btn btn-primary">Add New Record</button>
			<button ng-hide="hasRight" ng-click="cancel()" type="button" class="btn">Close</button>
		</div>
	</div>
