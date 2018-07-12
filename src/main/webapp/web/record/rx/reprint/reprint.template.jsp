<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<div class="modal-header">
	
	<h2>Reprint: </h2>
</div>
<div class="modal-body" id="modal-body">
				
	<div class="row" ng-repeat="rx in $ctrl.rxList | orderBy: '-datePrescribed'" >
	
	<div  class="col-md-11">
		<h3>{{rx.datePrescribed}}  Printed: {{rx.reprintCount+1}}  <button ng-click="$ctrl.reprint(rx)" class="btn btn-primary pull-right">Print</button></h3>
		<div class="well"><pre>{{rx.textView}}</pre></div>
	</div>
	
		 
		
	</div>

</div>
<div class="modal-footer">
 <%-- todo still need a way to pick the size of the drop box --%>
    <button class="btn btn-warning" type="button" ng-click="$ctrl.cancel()">Close</button>
</div>




