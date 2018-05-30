<div class="modal-header">
	
	<h2>{{$ctrl.alert.heading}}</h2>
</div>
<div class="modal-body" id="modal-body">
				
	<div class="row">
		
		
		<blockquote>
  		<p>{{$ctrl.alert.summary}}</p>
  		<footer>Source: {{$ctrl.alert.messageSource}} Author: {{$ctrl.alert.author}}</footer>
		</blockquote>
		<div class="lead"></div>
		<div class="well" ng-bind-html="$ctrl.alert.body"></div>
		<hr>
		<%-- pre>{{$ctrl.alert | json}}</pre> --%>
	</div>

</div>
<div class="modal-footer">
 <%-- todo still need a way to pick the size of the drop box --%>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Hide</button>
    <button class="btn btn-warning" type="button" ng-click="$ctrl.cancel()">Close</button>
</div>




