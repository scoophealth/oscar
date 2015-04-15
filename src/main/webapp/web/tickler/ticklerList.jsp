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
<!-- this CSS makes it so the modals don't have the vertical sliding animation. Not sure if I will keep this or how I will use this yet -->
<style> 
.modal.fade {
  opacity: 1;
}

.modal.fade .modal-dialog, .modal.in .modal-dialog {
  -webkit-transform: translate(0, 0);
  -ms-transform: translate(0, 0);
  transform: translate(0, 0);
}
</style>

<div ng-show="ticklerReadAccess" class="col-lg-12">
 	

<form name="searchForm" id="searchForm" class="noprint">
    
<div class="row">
  <div class="col-xs-2">
    <input ng-model="search.serviceStartDate" type="text" id="serviceStartDate" name="serviceStartDate" class="form-control" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="data.isOpen" ng-click="data.isOpen = true" placeholder="<bean:message key="tickler.list.serviceStartDate" bundle="ui"/>">
  </div>
   <div class="col-xs-2">
    <input ng-model="search.serviceEndDate" type="text" id="serviceEndDate" name="serviceEndDate" class="form-control" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="data2.isOpen" ng-click="data2.isOpen = true" placeholder="<bean:message key="tickler.list.serviceEndDate" bundle="ui"/>">
  </div>
  <div class="col-xs-2">
	  <select ng-model="search.status" name="status" id="status" class="form-control" >
	 	<option value=""><bean:message key="tickler.list.status" bundle="ui"/></option>
	 	<option value="A"><bean:message key="tickler.list.status.active" bundle="ui"/></option>
	 	<option value="C"><bean:message key="tickler.list.status.completed" bundle="ui"/></option>
	 	<option value="D"><bean:message key="tickler.list.status.deleted" bundle="ui"/></option>
	  </select>     
  </div>
  <div class="col-xs-2">
	  <select ng-model="search.priority" name="priority" id="priority" class="form-control" ng-init="search.priority=''">
	   	<option value=""><bean:message key="tickler.list.priority" bundle="ui"/></option>
	   	<option value="Normal"><bean:message key="tickler.list.priority.normal" bundle="ui"/></option>
	   	<option value="High"><bean:message key="tickler.list.priority.high" bundle="ui"/></option>
	   	<option value="Low"><bean:message key="tickler.list.priority.low" bundle="ui"/></option>
	  </select>           
  </div> 
</div>


<div style="height:5px"></div>

<div class="row">
  <div class="col-xs-2">
  <select ng-model="search.taskAssignedTo" name="taskAssignedTo" id="taskAssignedTo" class="form-control"
            	 	    ng-model="search.taskAssignedTo" data-ng-options="a.providerNo as a.name for a in providers" ng-init="search.taskAssignedTo=''">
             	  	 <option value=""><bean:message key="tickler.list.assignee" bundle="ui"/></option> 	
					</select>
  </div>
   <div class="col-xs-2">
 <select ng-model="search.creator" name="creator" id="creator" class="form-control"
            	 	    ng-model="search.creator" data-ng-options="a.providerNo as a.name for a in providers" ng-init="search.creator=''">
             	  	 <option value=""><bean:message key="tickler.list.creator" bundle="ui"/></option> 	
					</select>
  </div>
  <div class="col-xs-2">
 					<select ng-model="search.mrp" name="mrp" id="mrp" class="form-control"
            	 	    ng-model="search.mrp" data-ng-options="a.providerNo as a.name for a in providers" ng-init="search.mrp=''">
             	  	 <option value=""><bean:message key="tickler.list.allMRP" bundle="ui"/></option> 	
					</select>
  </div>
  
</div>

<div style="height:5px"></div>

<div class="row">
<div class="col-xs-6">
<button class="btn btn-primary" type="button" ng-click="doSearch()" ><bean:message key="global.search" bundle="ui"/></button>
<button class="btn btn-default" type="button" ng-click="clear()" ><bean:message key="global.clear" bundle="ui"/></button>

<button class="btn btn-default" type="button" ng-click="printArea()"><span class="glyphicon glyphicon-print"></span> Print List</button>
</div>
</div>        	
</form>

<div style="height:15px"></div>

<table ng-table="tableParams" show-filter="false" class="table">
  <tbody>
    	
    <tr ng-repeat="tickler in $data">
 		<td ng-show="ticklerWriteAccess">
 			<input type="checkbox" ng-model="tickler.checked" class="noprint">
 		</td>
    	<td ng-show="ticklerWriteAccess" >
    		<a ng-click="editTickler(tickler)" class="hand-hover noprint"><bean:message key="global.edit" bundle="ui"/></a> 		
    	</td>
    	<td ng-show="!ticklerWriteAccess" >
    		<a ng-click="editTickler(tickler)" class="hand-hover"><bean:message key="global.view" bundle="ui"/></a> 		
    	</td>
		<td data-title="'<bean:message key="tickler.list.header.patientName" bundle="ui"/>'">
              {{tickler.demographicName}}
        </td>
   	    <td data-title="'<bean:message key="tickler.list.header.creator" bundle="ui"/>'">
              {{tickler.creatorName}}
  	    </td>
  	    <td data-title="'<bean:message key="tickler.list.header.serviceDate" bundle="ui"/>'" class="text-center">
              {{tickler.serviceDate | date: 'yyyy-MM-dd'}}
		</td>
		<td data-title="'<bean:message key="tickler.list.header.creationDate" bundle="ui"/>'" class="text-center">
              {{tickler.updateDate | date: 'yyyy-MM-dd HH:mm'}}
        </td>	
		<td data-title="'<bean:message key="tickler.list.header.priority" bundle="ui"/>'" class="text-center">
              {{tickler.priority}}
        </td>        
		<td data-title="'<bean:message key="tickler.list.header.taskAssignedTo" bundle="ui"/>'">
              {{tickler.taskAssignedToName}}
        </td>        
		<td data-title="'<bean:message key="tickler.list.header.status" bundle="ui"/>'" class="text-center">
              {{tickler.statusName}}
        </td>        
		<td data-title="'<bean:message key="tickler.list.header.message" bundle="ui"/>'">
		 {{tickler.message | cut:true:50}}
		 	<span ng-if="tickler.ticklerLinks != null">
		 		<a target="lab" href="{{tickler.ticklerLinks | ticklerLink}}">ATT</a>
		 	</span>
		 	<span ng-if="tickler.ticklerComments != null">
		 		<span class="glyphicon glyphicon-comment" ng-click="showComments(tickler)"></span>
		 	</span>
        </td>      
        <td ng-show="ticklerWriteAccess" data-title="''">     
    		<a ng-click="editNote2(tickler)" class="hand-hover noprint"><img src="../images/notepad.gif" border="0"></a>
    	</td>  
    </tr>
  </tbody>

    <tfoot ng-show="ticklerWriteAccess" class="noprint">   
        <tr><td colspan="11" class="white">
        	<a ng-click="checkAll()"><bean:message key="tickler.list.checkAll" bundle="ui"/></a> - <a ng-click="checkNone()"><bean:message key="tickler.list.checkNone" bundle="ui"/></a> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
            <button class="btn btn-default" name="button" type="button" ng-click="addTickler()"><bean:message key="tickler.list.add" bundle="ui"/></button>
             <button class="btn btn-default" type="button" ng-click="completeTicklers()"><bean:message key="tickler.list.complete" bundle="ui"/></button>
             <button class="btn btn-default"  type="button" ng-click="deleteTicklers()"><bean:message key="tickler.list.delete" bundle="ui"/></button>
           </td>
       </tr>
   </tfoot>
                          
</table> 


<!-- 
<pre>{{search}}</pre>
<pre>{{lastResponse}}</pre>
-->

</div>



<div ng-show="ticklerReadAccess != null && ticklerReadAccess == false" class="col-lg-12">
 	<h3 class="text-danger"><span class="glyphicon glyphicon-warning-sign"></span>You don't have access to view ticklers</h3>
</div>

