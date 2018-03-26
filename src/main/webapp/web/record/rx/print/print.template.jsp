<div class="modal-header">
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Print PDF</button>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.print()">Print</button>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Print & Paste into EMR</button>
    <button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Create New Rx</button>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Close</button>
    <button class="btn btn-warning" type="button" ng-click="$ctrl.cancel()">Edit Rx</button>
</div>
<div class="modal-body" id="modal-body">
				
	<div class="row">
		<div class="col-lg-6">
			<iframe id="preview" name="preview" src="{{$ctrl.scriptURL}}" border="0" align="middle" width="420px" height="1000px" frameborder="0"></iframe>	
		</div>
		<div class="col-lg-6">	
			Additional Notes to add to Rx
			<textarea class="form-control" rows="3"></textarea>
		</div>
	</div>

</div>
<div class="modal-footer">
 <%-- todo still need a way to pick the size of the drop box --%>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Print PDF</button>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.print()">Print</button>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Print & Paste into EMR</button>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Create New Rx</button>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Close</button>
    <button class="btn btn-warning" type="button" ng-click="$ctrl.cancel()">Edit Rx</button>
</div>




