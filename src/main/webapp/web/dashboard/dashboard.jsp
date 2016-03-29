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

<style>
.flag-column {
	width:10px
}
.overdue {
 background-color:pink
}
.significance-high {
	border-color: red !important;
}
.significance-medium {
	border-color: yellow !important;
}
.significance-low {
	border-color: green !important;
}
</style>

<div ng-if="me != null">

<p class="lead"><bean:message key="dashboard.welcome" bundle="ui"/> {{me.firstName}} <span class="pull-right">{{displayDate() | date:'MMMM d, y'}}</span></p>
<hr>

<div class="row">
<div class="col-md-9" ng-controller="DashboardCtrl">


<!-- il18n problem here -->
<p class="lead">You have {{(totalInbox>0) && totalInbox || "no"}} report{{(totalInbox>1) && "s" || ""}}{{(totalInbox==0) && "s" || ""}}{{(totalInbox==null) && "s" || ""}} which are not yet acknowledged.</p>


<div ng-if="inbox.length > 0">
<table class="table table-condensed table-hover" >
	<thead>
    <tr>
    	<th class="flag-column"></th>
 	    <th><bean:message key="dashboard.inbox.header.patient" bundle="ui"/></th>
 	    <th><bean:message key="dashboard.inbox.header.category" bundle="ui"/></th>
       <!--  <th>Source</th> -->
        <th><bean:message key="dashboard.inbox.header.date" bundle="ui"/></th>
        <th><bean:message key="dashboard.inbox.header.status" bundle="ui"/></th>
    </tr>
    </thead>
              
     <tbody>
    <tr ng-repeat="item in inbox" ng-hide="$index >= 5" ng-click="openInbox()" class="hand-hover">
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
		 <span class="label label-success hand-hover" ui-sref="inbox"><bean:message key="dashboard.inbox.more" bundle="ui"/></span> 
      </td>
    </tr>
  </tfoot>
</table>
</div>

<br/>


<!-- il18n problem here -->
<p class="lead">You have {{(totalMessages > 0) && totalMessages || "no"}} unread message{{(totalMessages != 1) && "s" || ""}}.</p>


<div ng-if="totalMessages > 0">
<table class="table table-condensed  table-hover" >
	<thead>
    <tr>
    <!-- 	<th class="flag-column"></th> -->
        <th><bean:message key="dashboard.messages.header.from" bundle="ui"/></th>
        <th><bean:message key="dashboard.messages.header.subject" bundle="ui"/></th>
        <th><bean:message key="dashboard.messages.header.date" bundle="ui"/></th>
        <th><bean:message key="dashboard.messages.header.patient" bundle="ui"/></th>
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
		 <span class="label label-success hand-hover" ng-click="openClassicMessenger()"><bean:message key="dashboard.messages.more" bundle="ui"/></span> 
      </td>
    </tr>
  </tfoot>
 
</table>
</div>

<br/>
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


<!-- this is a bit of a problem for il18n -->
<p class="lead">You have {{(totalTicklers > 0) && totalTicklers || "no"}} active tickler{{(totalTicklers != 1) && "s" || ""}}. <span style="color:red" ng-if="prefs.expiredTicklersOnly == true">(Overdue)</span></p>

<div ng-if="totalTicklers>0">
	<table class="table table-condensed  table-hover" >
		<thead>
		<tr>
			<th class="flag-column">
			<span class="glyphicon glyphicon-cog hand-hover" ng-click="configureTicklers()"></span>
			</th>
			<th><bean:message key="dashboard.tickler.header.demographicName" bundle="ui"/></th>
			<th><bean:message key="dashboard.tickler.header.due" bundle="ui"/></th>
			<th><bean:message key="dashboard.tickler.header.message" bundle="ui"/></th>
			
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
			 <span class="label label-success hand-hover" ui-sref="ticklers"><bean:message key="dashboard.tickler.more" bundle="ui"/></span> 
	      </td>
	    </tr>
	  </tfoot>
		
	</table>
</div>

</div>

<p class="lead"><bean:message key="dashboard.k2a.header" bundle="ui"/></p>
<div id="rightColumn" class="col-md-3 hidden-xs" ng-hide="!k2afeed && !authenticatek2a" style="height:80vh;overflow-y:scroll">
	<div infinite-scroll="updateFeed(k2afeed.length,10)" infinite-scroll-parent="true">
		<blockquote class="pull-right" ng-repeat="item in k2afeed" ng-class="{'significance-high': item.significance === 'High', 'significance-medium': item.significance === 'Medium', 'significance-low': item.significance === 'Low'}">
			<h4>{{item.type}}: <a target="_blank" href="{{item.link}}">{{item.title}}</a></h4>
				<a href="" style="font-size:14px" data-toggle="modal" data-target="#expandFeed{{item.id}}" ng-if="item.link">{{item.body | cut:true:140 }}</a>
				<a href="" ng-click="authenticateK2A(item.id)" style="font-size:14px" ng-if="!item.link">{{item.body}}</a>
				<small ng-hide="!item.agree">You agree with this post</small>
				<small ng-hide="!item.disagree">You disagree with this post</small>
				<small>{{item.author}} posted {{item.publishedDate | date:'yyyy-MM-dd'}}</small>
				<small>
					<a class="glyphicon glyphicon-thumbs-up" ng-click="agreeWithK2aPost(item)"></a>&nbsp;{{item.agreeCount}}&nbsp;&nbsp;
					<a class="glyphicon glyphicon-thumbs-down" ng-click="disagreeWithK2aPost(item)"></a>&nbsp;{{item.disagreeCount}}&nbsp;&nbsp;
					<a class="glyphicon glyphicon-comment" data-toggle="modal" data-target="#expandFeed{{item.id}}"></a>&nbsp;{{item.commentCount}}
				</small>
				
				<div class="modal fade" id="expandFeed{{item.id}}" tabindex="-1" role="dialog" aria-labelledby="expandFeed{{item.id}}" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">	
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
								<h4 class="modal-title" id="imageTitle">{{item.type}}: {{item.title}}</h4>
							</div>					      		
							<div class="modal-body">
								<div>
									<h4>Summary</h4>
									<p style="white-space:pre-line;text-align:left">{{item.body}}</p><hr />
								</div>
								<div class="row">
									<div class="col-md-7">
										<a class="glyphicon glyphicon-thumbs-up" ng-click="agreeWithK2aPost(item)"></a>&nbsp;<b>{{item.agreeCount}}</b>&nbsp;Agree&nbsp;&nbsp;
										<a class="glyphicon glyphicon-thumbs-down" ng-click="disagreeWithK2aPost(item)"></a>&nbsp;<b>{{item.disagreeCount}}</b>&nbsp;Disagree&nbsp;&nbsp;
										<a class="glyphicon glyphicon-comment" ></a>&nbsp;<b>{{item.commentCount}}</b>&nbsp;Comments<br />
									</div>
									<div class="col-md-5">
										<p ng-show="item.agree"><i>You agree with this post</i></p>
										<p ng-show="item.disagree"><i>You disagree with this post</i></p>
									</div>
								</div>
								
								<div ng-repeat="comment in item.comments" | class="well">
									<h5><b>{{comment.author}}</b> posted {{comment.publishedDate | date:'yyyy-MM-dd'}}</h5>
									<p style="text-align:left">{{comment.body}}</p>
								</div>
								
								<div class="well" ng-if="item.comments.length < item.commentCount">
									<a target="_blank" href="{{item.link}}">See {{item.commentCount - item.comments.length}} more comments...</a>
								</div>
								
								<div>
									<textarea ng-model="item.newComment.body" rows="4" cols="50"></textarea><br />
									<button ng-click="commentOnK2aPost(item)" class="btn btn-default">Save Comment</button>
								</div>
							</div>
						</div>
					</div>
				</div>
		</blockquote>
		<div style="clear: both;"></div>
	</div>
</div>
</div>

</div>