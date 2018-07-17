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




