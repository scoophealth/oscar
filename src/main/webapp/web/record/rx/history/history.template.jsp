<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<div class="modal-header">
	
	<h2>History: <%-- {{$ctrl.drug.instructions}} --%></h2>
</div>
<div class="modal-body" id="modal-body">
				
	<div class="row">
	
	<table class="table table-striped table-condensed">
		<tr>
			<th>Provider</th>
			<th>Start Date</th>
			<th>End Date</th>
			<th>Medication Details</th>
			<th>&nbsp;</th>
		</tr>
		<tr ng-repeat="drug in $ctrl.druglist">
				<td>{{drug.provider}}</td>
				<td>{{drug.rxDate | date:'yyyy-MM-dd'}}</td>
				<td>{{drug.endDate | date:'yyyy-MM-dd'}}</td>
				<td>{{drug.instructions}}</td>
				<td>&nbsp;</td>
		</tr>
	
	</table>
		 
		
	</div>

</div>
<div class="modal-footer">
 <%-- todo still need a way to pick the size of the drop box 
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Discontinue</button> --%>
    <button class="btn btn-warning" type="button" ng-click="$ctrl.cancel()">Close</button>
</div>




