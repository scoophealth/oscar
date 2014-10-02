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
<div class="modal-body">
	<div class="row">
		<div class="col-xs-12">
			 <h4>Tickler re: <strong>{{tickler.demographicName}}</strong></h4>
		</div>
	</div>
	
	<div class="row" style="height:5px">
	
	</div>
	
	<div class="row">
		<div class="col-xs-4">
			<strong>Assigned to:</strong>
		</div>
		<div class="col-xs-6" ng-show="!showTaskAssignedToFormControl">
			{{tickler.taskAssignedToName}} <span ng-show="ticklerWriteAccess" class="glyphicon glyphicon-pencil" ng-click="editTaskAssignedTo()"></span>
		</div>
		<div class="col-xs-6" ng-show="showTaskAssignedToFormControl">	
		 <div class="form-group">
    <div class="input-group">
      <div class="input-group-addon"><span class="glyphicon glyphicon-remove" ng-click="cancelTaskAssignedToUpdate()"></span></div>
      <input type="text" ng-model="ticklerUpdate.taskAssignedToName" placeholder="Enter Last Name or First Name" 
				typeahead="pt.providerNo as pt.name for pt in searchProviders($viewValue)" 
				typeahead-on-select="updateTaskAssignedTo($item, $model, $label)"
				class="form-control input-sm">
    </div>
  </div>
		</div>
		
		
	</div>
	<div class="row">
		<div class="col-xs-4">
			<strong>Last Updated:</strong>
		</div>
		<div class="col-xs-4">
			{{tickler.updateDate | date :'yyyy-MM-dd HH:mm'}}
		</div>
	</div>	
	

  <div class="row">
  	<div class="col-xs-8">
  		<textarea rows="10" ng-model="ticklerUpdate.message" class="form-control" ng-readonly="!ticklerWriteAccess">{{tickler.message}}</textarea>
  		
  	</div>
  	<div class="col-xs-4">
  		<table class="table">
  			<tr>
  				<td ng-show="!showServiceDateAndTimeFormControl"><strong>Service Date:</strong></td>
  				<td ng-show="!showServiceDateAndTimeFormControl">{{tickler.serviceDate |date: 'yyyy-MM-dd HH:mm'}}  <span ng-show="ticklerWriteAccess" class="glyphicon glyphicon-pencil" ng-click="editServiceDateAndTime()"></span></td>
				<td ng-show="showServiceDateAndTimeFormControl" colspan="2">
					 <input  ng-model="ticklerUpdate.serviceDate" type="text" class="form-control">
					 <input  ng-model="ticklerUpdate.serviceTime" type="text" class="form-control">
					 <button class="btn btn-primary" ng-click="updateServiceDateAndTime()" >Set</button>
					 <button class="btn btn-default" ng-click="cancelServiceDateAndTimeUpdate()" >Cancel</button>
				</td>
  			</tr>
  			<tr>
  				<td><strong>Created By:</strong></td>
  				<td>{{tickler.creatorName}}</td>
  			</tr>
  			<tr>
  				<td ng-show="!showPriorityFormControl"><strong>Priority:<strong> </td>
  				<td ng-show="!showPriorityFormControl">{{tickler.priority}}  <span ng-show="ticklerWriteAccess" class="glyphicon glyphicon-pencil" ng-click="editPriority()"></span></td>
  				<td ng-show="showPriorityFormControl" colspan="2">
  						 <div class="form-group">
    <div class="input-group">
      <div class="input-group-addon"><span class="glyphicon glyphicon-remove" ng-click="cancelPriorityUpdate()"></span></div>
      
  					 <input type="text" typeahead-on-select="updatePriority($item, $model, $label)" placeholder="Priority" ng-model="ticklerUpdate.priority" typeahead="p for p in priorities | filter:$viewValue" class="form-control">
  					  </div></div>
  				</td>
  			</tr>
  			<tr>
  				<td ng-show="!showStatusFormControl"><strong>Status:<strong> </td>
  				<td ng-show="!showStatusFormControl">{{tickler.statusName}}  <span ng-show="ticklerWriteAccess" class="glyphicon glyphicon-pencil" ng-click="editStatus()"></span></td>
  				<td ng-show="showStatusFormControl" colspan="2">
  				 <div class="form-group">
    <div class="input-group">
      <div class="input-group-addon"><span class="glyphicon glyphicon-remove" ng-click="cancelStatusUpdate()"></span></div>
      
  					 <input type="text" typeahead-on-select="updateStatus($item, $model, $label)" placeholder="Status" ng-model="ticklerUpdate.status" typeahead="s.id as s.label for s in statuses | filter:$viewValue" class="form-control">
  					  </div></div>
  				</td>
  			</tr>  			
  			<tr>
  				<td><strong>Program:</strong></td>
  				<td>{{tickler.program != null && tickler.program.name || 'N/A'}}</td>
  			</tr>
  			<tr>
  				<td><strong>Note:</strong></td>
  				<td>{{ticklerNote != null && 'Rev:'+ticklerNote.revision || 'N/A'}}</td>
  			</tr>
  			<tr>
  				<td><strong>Lab:</strong></td>
  				<td ng-if="tickler.ticklerLinks == null || tickler.ticklerLinks.length == 0">N/A</td>
  				<td ng-if="tickler.ticklerLinks != null"><a target="lab" href="{{tickler.ticklerLinks | ticklerLink}}">ATT</a></td>
  				
  			</tr>
  		</table>
  	</div>
  </div>
    
  <div class="row">
  	<div class="col-xs-12">
  		<strong><span  ng-click="showComments = !showComments">Comments</span> ({{tickler.ticklerComments != null && tickler.ticklerComments.length || 0}}) <span class="glyphicon glyphicon-pencil" ng-show="ticklerWriteAccess" ng-click="addComment()"></span></strong>
		<div ng-if="showComments">
			
	<div class="form-group" ng-show="showCommentFormControl">
    <div class="input-group">
      <div class="input-group-addon"><span class="glyphicon glyphicon-remove" ng-click="cancelCommentUpdate()"></span></div>
      <div class="input-group-addon"><span class="glyphicon glyphicon-ok" ng-click="saveComment()"></span></div>
      		
			<input  type="text" ng-model="ticklerUpdate.comment" class="form-control"/>
	  		
	</div></div>	  		
	  		<hr ng-if="tickler.ticklerComments == null || tickler.ticklerComments.length == 0"/>
	  		<table ng-if="tickler.ticklerComments != null && tickler.ticklerComments.length > 0" class="table">
	  			<tr ng-repeat="tc in tickler.ticklerComments">
	  				<td>{{tc.updateDate | date : 'yyyy-MM-dd'}}</td>
	  				<td>{{tc.providerName}}</td>
	  				<td>{{tc.message}}</td>
	  			</tr>
	  		</table>
  		</div>
  	</div>
  </div>

  
    <div class="row">
  	<div class="col-xs-12">
  		<strong ng-click="showUpdates = !showUpdates">Updates ({{tickler.ticklerUpdates != null && tickler.ticklerUpdates.length || 0}})</strong>
  		<div ng-if="showUpdates">
  			<hr ng-if="tickler.ticklerUpdates == null || tickler.ticklerUpdates.length == 0"/>
	  		<table ng-if="tickler.ticklerUpdates != null && tickler.ticklerUpdates.length > 0" class="table">
	  			<tr ng-repeat="tc in tickler.ticklerUpdates">
	  				<td>{{tc.updateDate | date : 'yyyy-MM-dd HH:mm'}}</td>
	  				<td>{{tc.providerName}}</td>
	  			</tr>
	  		</table>
  		</div>
  	</div>
  </div>
  
  

  
  <!-- 
 	<pre>{{tickler}}</pre>
 	-->
</div>
<div class="modal-footer">
<!-- 
    <button class="btn" ng-click="save()">Save</button>
    
-->
	<div class="pull-left">
	<button class="btn btn-warning" ng-click="completeTickler()" ng-show="ticklerWriteAccess">Complete</button>
	<button class="btn btn-danger" ng-click="deleteTickler()" ng-show="ticklerWriteAccess"><span class="glyphicon glyphicon-trash"></span>Delete</button>
</div>
	<button class="btn btn-primary" ng-click="saveChanges()" ng-show="ticklerWriteAccess">Save Changes</button>
    <button class="btn btn-default" ng-click="close()">Close</button>
</div>


