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

<div class="row">
	<div class="col-md-2">
		<h1>Reporting</h1>
	</div>
	<div class="col-md-10">
		<div class="pull-right">
			<span class="dropdown-toggle hand-hover" data-toggle="dropdown"><h2><span class="glyphicon glyphicon-cog hand-hover"></span></h2></span>
			<ul class="dropdown-menu" role="menu">
				<li>
					<a ng-click="editDemographicSet()">Demographic Set Edit</a>
				</li>
			</ul>
		</div>
	</div>
</div>
				  	
<hr/>

<div class="row">
	<div class="col-md-3">
				
		<form class="form-search" role="search">
			<span class="form-group" class="twitter-typeahead">
				<select class="form-control" data-ng-options="a.value as a.label for a in getReportGroups()" ng-model="reportGroup" ng-init="reportGroup=''">
				</select>
			</span>
			<span class="form-group" class="twitter-typeahead">
				<input type="text"  class="form-control" placeholder="Filter" ng-model="reportFilter" ng-init="reportFilter=''"/>
			</span>
		</form>
		<div class="list-group">		
			<a  ng-repeat="report in getReports() | filter: reportFilter "
			 class="list-group-item default" ng-click="selectReport(report)">{{report.name}}</a>
		</div>
	</div>

	<div class="col-md-8">
		<div ng-init="test=reportSidebar.url" ng-include="reportSidebar.location"></div>
	</div>
</div>