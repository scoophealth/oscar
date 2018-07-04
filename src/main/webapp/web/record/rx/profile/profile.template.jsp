<fieldset>
	<legend style="margin-bottom:0px;">Drug Profile
 		<button type="button" class="btn btn-default btn-xs" ng-class="$ctrl.buttonStyle(1)" ng-click="$ctrl.setMode(1)">All</button>
		<button type="button" class="btn btn-primary btn-xs" ng-class="$ctrl.buttonStyle(0)" ng-click="$ctrl.setMode(0)">Current</button>
		<div class="row">
			<div class="col-sm-12">
			    <div class="input-group">
			      <input type="text" class="form-control" placeholder="Search Drug Profile" ng-model="$ctrl.drugProfileFilter">
			      <div class="input-group-btn">
			        <button type="button" class="btn btn-default" ng-click="$ctrl.print()">Print</button>
			        <button type="button" class="btn btn-default" ng-click="$ctrl.rePrint()">Reprint</button>
			        <button type="button" class="btn btn-default" ng-click="$ctrl.timeline()">Timeline</button>
			      </div>
			    </div><!-- /input-group -->
		  	</div>
		</div>
	</legend>
			
	<table class="table table-condensed table-striped">
		<tr>
			<th>Medication</th>
			<th>Start Date</th>
			<th>Days To Exp</th>
			<th>reRx</th>
			<th>Action</th>
		</tr>
		<tr ng-repeat="drug in $ctrl.rxComp.drugs | filter:$ctrl.drugProfileFilter">
			<td ng-class="{ 'deletedItem': drug.archived }">
				{{drug.instructions}}
			</td>
			<td>{{drug.rxDate}}</td>
			<td>{{$ctrl.daysToExp(drug)}}</td>
			<td><a ng-click="$ctrl.reRx({'drug':drug})">rx</a></td>
			<td>
				<div class="btn-group">
					<button class="btn btn-default btn-xs dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						Actions<span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
					    <li><a ng-click="$ctrl.discontinue(drug)" >Discontinue</a></li>
					    <li><a ng-click="$ctrl.delete(drug)" >Delete</a></li>
					    <li><a ng-click="$ctrl.addReason(drug)" >Add Reason</a></li>
					    <li><a ng-click="$ctrl.setAsLongTermMed(drug)" >Set as Long Term Med</a></li>
					    <li><a ng-click="$ctrl.annotate(drug)" >Annotate</a></li>
					    <li><a ng-click="$ctrl.hideFromCPP(drug)" >Hide From CPP</a></li>
					    <li><a ng-click="$ctrl.moveUpInList(drug)" >Move up in list</a></li>
					    <li><a ng-click="$ctrl.moveDownInList(drug)" >Move down in list</a></li>
					</ul>
				</div>
			</td>
		</tr>	
	</table>
			
</fieldset>
