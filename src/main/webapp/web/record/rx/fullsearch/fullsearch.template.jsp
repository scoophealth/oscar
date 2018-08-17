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
<div class="modal-header">
	<form class="form-inline" ng-submit="$ctrl.fullSearch()">
	<div class="input-group">
	
      <input type="text" ng-model="$ctrl.searchTerm" class="form-control" placeholder="Search">
			
      <div class="input-group-btn">
      	<button type="button" class="btn btn-default" ng-click="$ctrl.fullSearch()">Search</button>
    </div><!-- /input-group -->
	</div>
	</form>  
	 
</div>

<div class="modal-body" id="modal-body">
				
	<div class="row">
	
	<%-- div class="col-sm-4">
	  	<div class="panel panel-default">
		  <!-- Default panel contents -->
		  <div class="panel-heading">Drug Class</div>
		
		  <!-- List group -->
		  <div class="list-group">
	  		<a href="#"  ng-repeat="drug in $ctrl.afhcClass" class="list-group-item list-group-item-success">{{drug.name}}</a>
	  	  </div>
		</div>
  	</div> 
  	--%>
  	<div class="col-sm-6">
	  	<div class="panel panel-default">
		  <!-- Default panel contents -->
		  <div class="panel-heading">Generic</div>
		
		  <!-- List group -->
		  <div class="list-group">
	  		<a ng-click="$ctrl.listBrands(drug);" ng-repeat="drug in $ctrl.gen" class="list-group-item list-group-item-success">{{drug.name}}</a>
	  	  </div>
		</div>
  	</div>
  	<div class="col-sm-6">
  		<div class="panel panel-default">
		  <!-- Default panel contents -->
		  <div class="panel-heading">Brand</div>
		
		  <!-- List group -->
		  <div class="list-group">
	  		<a ng-click="$ctrl.selectDrug(drug)"  ng-repeat="drug in $ctrl.brand" class="list-group-item list-group-item-success">{{drug.name}}</a>
	  	  </div>
		</div>
	  	
  	</div>

		 
		
	</div>

</div>
<div class="modal-footer">
 <%-- todo still need a way to pick the size of the drop box 
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Discontinue</button> --%>
    <button class="btn btn-warning" type="button" ng-click="$ctrl.cancel()">Close</button>
</div>




