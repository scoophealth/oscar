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
.overdue {
 background-color:pink
}
</style>

<div ng-if="me != null">

<p class="lead">Welcome {{me.firstName}} <span class="pull-right">{{displayDate() | date:'MMMM d, y'}}</span></p>
<hr>

<div class="row">
<div class="col-md-9" ng-controller="DashboardCtrl">


<p class="lead">You have {{(totalTicklers > 0) && totalTicklers || "no"}} active tickler{{(totalTicklers != 1) && "s" || ""}}.</p>


<div ng-if="totalTicklers>0">
	<table class="table table-condensed  table-hover" >
		<thead>
		<tr>
			<th class="flag-column"></th>
			<th>Demographic Name</th>
			<th>Due</th>
			<th>Message</th>
		</tr>
		</thead>
		<tbody>
		<tr ng-repeat="item in ticklers" ng-hide="$index >= 5" ng-click="viewTickler(item)" class="hand-hover">
			<td>
				<span ng-if="isTicklerHighPriority(item)" class="glyphicon glyphicon-flag" style="color:red"></span>
			</td>
			<td>{{item.demographicName}}</td>
			<td>{{item.serviceDate | date:'yyyy-MM-dd'}}</td>
			<td>{{item.message  | cut:true:200 }}</td>
		</tr>
		</tbody>
		<tfoot>
	    <tr ng-if="totalTicklers > 5">
	      <td colspan="6">
			 <span class="label label-success hand-hover" ui-sref="ticklers">See More Ticklers</span> 
	      </td>
	    </tr>
	  </tfoot>
		
	</table>
</div>

<br/>


<p class="lead">You have {{(totalMessages > 0) && totalMessages || "no"}} unread message{{(totalMessages != 1) && "s" || ""}}.</p>


<div ng-if="totalMessages > 0">
<table class="table table-condensed  table-hover" >
	<thead>
    <tr>
    <!-- 	<th class="flag-column"></th> -->
        <th>From</th>
        <th>Subject</th>
        <th>Date</th>
        <th>Patient</th>
    </tr>
    </thead>
  	<tbody>
  	<tr ng-repeat="item in messages" ng-hide="$index >= 5" ng-click="viewMessage(item)" class="hand-hover">
	<!-- 	<td></td> -->
		<td>{{item.fromName}}</td>
		<td>{{item.subject}}</td>
		<td>{{item.dateOfMessage}}</td>
		<td ng-if="item.demographicNo">{{item.demographicName}}</td>
		<td ng-if="!item.demographicNo"></td>
	</tr>
	</tbody>
	<tfoot>
    <tr ng-if="totalMessages > 5">
      <td colspan="6">
		 <span class="label label-success hand-hover" ng-click="openClassicMessenger()">See More Messages</span> 
      </td>
    </tr>
  </tfoot>
 
</table>
</div>

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

<br/>


<p class="lead">You have {{(inbox.length>0) && inbox.length || "no"}} report{{(inbox.length>1) && "s" || ""}}{{(inbox.length==0) && "s" || ""}}{{(inbox==null) && "s" || ""}} which are not yet acknowledged.</p>


<div ng-if="inbox.length">
<table class="table table-condensed  table-hover" >
	<thead>
    <tr>
    	<th class="flag-column"></th>
 	    <th>Patient</th>
 	    <th>Category</th>
       <!--  <th>Source</th> -->
        <th>Date</th>
        <th>Status</th>
    </tr>
    </thead>
              
     <tbody>
    <tr ng-repeat="item in inbox" ng-hide="$index >= 5">
  <td><span ng-if="item.properity != null && item.priority != 'Routine'" class="glyphicon glyphicon-flag" style="color:red"></span></td>
		  	
        <td >{{item.demographicName}}</td>
        <td>{{item.discipline}}</td>
       <!--  <td>{{item.source}}</td> -->
        <td>{{item.dateReceived}}</td>
       	<td>{{item.status}}</td>
    </tr>
    </tbody>
    <tfoot>
    <tr ng-if="inbox.length > 5">
      <td colspan="6">
		 <span class="label label-success hand-hover" ui-sref="inbox">See More Inbox</span> 
      </td>
    </tr>
  </tfoot>
</table>
</div>

</div>

<div class="col-md-3">
<p class="lead">K2A News</p>

<blockquote class="pull-right" ng-repeat="item in k2afeed" ng-hide="$index >= 5">
  <p>{{item.title}}</p>
  <small><a target="k2afeeditem" href="{{item.link}}">link</a></small>
</blockquote>


</div>
</div>

</div>