<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<div class="modal-header">
	
	<h2>History: <%-- {{$ctrl.drug.instructions}} --%></h2>
</div>
<div class="modal-body" id="modal-body" style="overflow-x: scroll;">
				
	<div class="row">
	
	<table class="table table-striped table-condensed">
		<tr>
			<th>Provider</th>
			<th>Start Date</th>
			<th>End Date</th>
			<th>Written Date</th>
			<th>Medication Details</th>
			<th>Long Term</th>
			<th>Brand Name</th>
			<th>Name</th>
			<th>Generic Name</th>
			<th>ATC</th>
			<th>Regional Identifier</th>
			<th>Strength</th>
			<th>Strength Unit</th>
			<th>Demographic</th>
			<th>Provider</th>
			<th>External Provider</th>
			<th>Take Min</th>
			<th>Take Max</th>	
			<th>Route</th>
			<th>Frequency</th>
			<th>Duration</th>
			<th>Duration Unit</th>
			<th>Additional Instructions</th>
			<th>PRN</th>
			<th>Form</th>		
			<th>Method</th>
			<th>Repeats</th>
			<th>Quantity</th>
			<th>Archived</th>
			<th>Archived Reason</th>
			<th>Archived Date</th>
			<th>No Subs</th>
			<th>Generic Name</th>
			<th>New Med</th>
			<th>History</th>
			<th>Custom Name</th>
			
		</tr>
		<tr ng-repeat="drug in $ctrl.druglist | orderBy: '-rxDate'">
				<td title="{{drug.id}}"><providername provider-no="drug.provider"></providername></td>
				<td>{{drug.rxDate | date:'yyyy-MM-dd'}}</td>
				<td>{{drug.endDate | date:'yyyy-MM-dd'}}</td>
				<td>{{drug.writtenDate | date:'yyyy-MM-dd'}}</td>
				<td>{{drug.instructions}}</td>
				<td>{{drug.longTerm}}</td>
				<td>{{drug.brandName}}</td>
				<td>{{drug.name}}</td>
				<td>{{drug.genericName}}</td>
				<td>{{drug.atc}}</td>
				<td>{{drug.regionalIdentifier}}</td>
				<td>{{drug.strength}}</td>
				<td>{{drug.strengthUnit}}</td>
				<td>{{drug.demographic}}</td>
				<td>{{drug.provider}}</td>
				<td>{{drug.externalProvider}}</td>
				<td>{{drug.takeMin}}</td>
				<td>{{drug.takeMax}}</td>
				<td>{{drug.route}}</td>
				<td>{{drug.frequency}}</td>
				<td>{{drug.duration}}</td>
				<td>{{drug.durationUnit}}</td>
				<td>{{drug.additionalInstructions}}</td>
				<td>{{drug.prn}}</td>
				<td>{{drug.form}}</td>
				<td>{{drug.method}}</td>
				<td>{{drug.repeats}}</td>
				<td>{{drug.quantity}}</td>
				<td>{{drug.archived}}</td>
				<td>{{drug.archivedReason}}</td>
				<td>{{drug.archivedDate}}</td>
				<td>{{drug.longTerm}}</td>
				<td>{{drug.noSubs}}</td>
				<td>{{drug.gn}}</td>
				<td>{{drug.newMed}}</td>
				<td>{{drug.history}}</td>
				<td>{{drug.customName}}</td>
		</tr>
	
	</table>
		 
		
	</div>

</div>
<div class="modal-footer">
 <%-- todo still need a way to pick the size of the drop box 
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Discontinue</button> --%>
    <button class="btn btn-warning" type="button" ng-click="$ctrl.cancel()">Close</button>
</div>




