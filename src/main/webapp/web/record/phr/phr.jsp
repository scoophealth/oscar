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

<div class="col-lg-3">		
	<ul class="nav nav-tabs nav-justified">
		<li ng-repeat="list in page.formlists" ng-class="getListClass(list.id)" ><a ng-click="changeTo(list.id)">{{list.label}}</a></li>
	</ul> 	
	<div class="panel panel-success"> 
	  	<!-- Default panel contents -->
	  	   <input type="search" class="form-control" placeholder="Filter" ng-model="filterFormsQ">
	  	   <ul class="list-group" tabindex="0" ng-keypress="keypress($event)">
   				<li class="list-group-item" ng-repeat="item in page.currentFormList[page.currentlistId] | filter:filterFormsQ" ng-class="getActiveFormClass(item)">
   					<a class="list-group-item-text" ng-click="viewFormState(item)" ><span  ng-show="item.date" class="pull-right">{{item.date | date : 'd-MMM-y'}}</span>{{item.name}}  </a>
   				</li>
   			</ul>
	</div>
</div>
<div class="col-lg-9">
	<div id="formInViewFrame"></div>
</div>