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
<div ng-controller="PatientListAppointmentListCtrl">
<a ng-repeat="patient in patients | offset:currentPage*pageSize | limitTo:pageSize | filter:query"
	class="list-group-item default hand-hover" ng-click="goToRecord(patient)"  ng-style="getAppointmentStyle(patient)">
	<!-- 
	<span ng-if="patient.status.length>0 && patient.status != 't'" class="badge">{{patient.status}}</span>
	-->
	<h4 class="list-group-item-heading">{{patient.name}}</h4>
	<h4 class="list-group-item-heading pull-right">{{patient.startTime}}</h4>
	<p class="list-group-item-text">Reason: {{patient.reason}}  </p>
</a>

</div>
