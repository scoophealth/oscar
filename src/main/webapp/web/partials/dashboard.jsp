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
.flag-column {
	width:10px
}
</style>

<p class="lead">Welcome {{userFirstName}} <span class="pull-right">{{displayDate | date:'MMMM d, y'}}</span></p>
<hr>

<div class="row">
<div class="col-md-9" ng-controller="DashboardCtrl">


<p class="lead">You have {{ticklers.length}} active tickler assigned to you</p>

<table class="table table-condensed  table-hover" >
	<tr>
		<th class="flag-column"></th>
		<th>Demographic Name</th>
		<th>Due</th>
		<th>Message</th>
	</tr>
	
	<tr ng-repeat="item in ticklers" ng-hide="$index >= 5">
		<td><span ng-if="item.priority == 'High'" class="glyphicon glyphicon-flag" style="color:red"></span></td>
		<td>{{item.demographicName}}</td>
		<td>{{item.serviceDate | date:'yyyy-MM-dd'}}</td>
		<td>{{item.message}}</td>
	</tr>
	<tfoot>
    <tr ng-if="ticklers.length > 5">
      <td colspan="6">
		 <span class="label label-success">See More Ticklers</span> 
      </td>
    </tr>
  </tfoot>
	
</table>




<p class="lead">You have {{messages.length}} new messages</p>


<table class="table table-condensed  table-hover" >
    <tr>
    <!-- 	<th class="flag-column"></th> -->
        <th>From</th>
        <th>Subject</th>
        <th>Date</th>
        <th>Patient</th>
    </tr>
  
  	<tr ng-repeat="item in messages" ng-hide="$index >= 5">
	<!-- 	<td></td> -->
		<td>{{item.fromName}}</td>
		<td>{{item.subject}}</td>
		<td>{{item.dateOfMessage | date:'yyyy-MM-dd HH:mm'}}</td>
		<td ng-if="item.demographicNo">{{item.demographicName}}</td>
		<td ng-if="!item.demographicNo"></td>
	</tr>
	<tfoot>
    <tr ng-if="messages.length > 5">
      <td colspan="6">
		 <span class="label label-success">See More Messages</span> 
      </td>
    </tr>
  </tfoot>
 
</table>

<!-- 
<p class="lead">You have 0 new messages from patients</p>


<table class="table table-condensed  table-hover" >
    <tr>
    	<th class="flag-column"></th>
    	<th>From</th>
        <th>Subject</th>
        <th>Date</th>
      
    </tr>
               
    <tr >
		<td></td>
         <td >Sophia Smith</td>
        <td>
        <a href="/oscar_sfhc/oscarMessenger/ViewMessage.do?messageID=555879&boxType=0">
            Quality Breakfast - October 24
        </a>
        </td>
        <td>2013-10-10 14:54:03</td>
       
    </tr>

    <tfoot>
     <tr>
      <td colspan="5"><span class="label label-success">See More Messages</span></td>
     </tr>
    </tfoot>       
</table>

-->

<p class="lead">You have report(s) which are not yet acknowledged</p>

<table class="table table-condensed  table-hover" >
    <tr>
    	<th class="flag-column"></th>
 	    <th>Patient</th>
 	    <th>Category</th>
       <!--  <th>Source</th> -->
        <th>Date</th>
        <th>Status</th>
    </tr>
              
    <tr ng-repeat="item in inbox" ng-hide="$index >= 5">
  <td><span ng-if="item.priority != 'Routine'" class="glyphicon glyphicon-flag" style="color:red"></span></td>
		  	
        <td >{{item.demographicName}}</td>
        <td>{{item.discipline}}</td>
       <!--  <td>{{item.source}}</td> -->
        <td>{{item.dateReceived}}</td>
       	<td>{{item.status}}</td>
    </tr>
    <tfoot>
    <tr ng-if="inbox.length > 5">
      <td colspan="6">
		 <span class="label label-success">See More Inbox</span> 
      </td>
    </tr>
  </tfoot>
</table>


</div>

<div class="col-md-3">
<p class="lead">K2A News</p>

<blockquote class="pull-right" ng-repeat="item in k2afeed" ng-hide="$index >= 5">
  <p>{{item.title}}</p>
  <small><a target="k2afeeditem" href="{{item.link}}">link</a></small>
</blockquote>


</div>
</div>