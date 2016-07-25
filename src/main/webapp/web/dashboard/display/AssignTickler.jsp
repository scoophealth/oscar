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
<%-- <script type="text/javascript" src="${ pageContext.request.contextPath }/web/dashboard/display/drilldownDisplayController.js" ></script> --%>
<script type="text/javascript">
// date picker for Tickler service dates.
$(".date-picker").datepicker();

$(".date-picker").on("change", function () {
    var id = $(this).attr("id");
    var val = $("label[for='" + id + "']").text();
    $("#msg").text(val + " changed");
});
</script>



<div id="assignTickler" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
		
<form name="ticklerAddForm" novalidate>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal">&times;</button>
    <h4>Assign Tickler</h4>
</div>  
<div class="modal-body">
<!-- 	<div class="row" ng-show="showErrors === true">
		<div class="col-xs-12">
			<ul>
				<li class="text-danger" ng-repeat="error in errors">{{error}}</li>
			</ul>
		</div>
	</div> -->

	<div class="row">
		<div class="col-xs-12">
				<div class="form-group">
				 <label>Patient:</label>
				 <div class="well" id="patientTicklerList" >
				 100
				 
				 </div>
				</div>		
		</div>
		<!-- <div class="col-xs-5">
			<div class="row" ng-if="tickler.demographic != null">
				<div class="col-xs-3">
					<img width="60px" ng-src="../imageRenderingServlet?source=local_client&clientId={{tickler.demographic.demographicNo}}"/>
				</div>
				<div class="col-xs-9">
				<div>{{tickler.demographic.lastName}}, {{tickler.demographic.firstName}}</div>
				<div>{{tickler.demographic.hin}}</div>
				<div>{{tickler.demographic.dateOfBirth | date : 'yyyy-MM-dd'}}</div>
				</div>
			</div>
	
		</div> -->
	</div>

	<div class="row">
		<div class="col-xs-7">
				<div class="form-group">
				  <label>Assign to:</label>
				 <input class="form-control" type="text" placeholder="Provider" name="provider_no" />
				</div>
			
		</div>
		
		<div class="col-xs-5">
		<div class="form-group">
				  <label>Priority:</label>
				  <select class="form-control">
				  	<option>Low</option>
				  	<option>Medium</option>
				  	<option>High</option>
				  </select>
				</div>
		</div>
	</div>	
	
	

	<div class="row" >
		<div class="col-xs-6">

		<label for="datePickerServiceDate" class="control-label">Service Date:</label>
        <div class="controls">
            <div class="input-group">
                <input id="datePickerServiceDate" type="text" class="date-picker form-control" />
                <label for="datePickerServiceDate" class="input-group-addon btn">
                <span class="glyphicon glyphicon-calendar"></span>
                </label>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-xs-6">
			<label> Time: </label>
		</div>
		<div class="col-xs-6">
			<input type="time" name="ticklerTime" class="controls" />
		</div>
	</div>
	
	
	
	<div class="row">
		<div class="col-xs-12">
				<div class="form-group">
				  <label>Template:</label>
				  <select class="form-control" name="ticklerTemplate" >
				  </select>
				</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-xs-12">
				<textarea name="ticklerMessage" class="form-control" rows="6" required></textarea>
		</div>
	</div>

</div>
<div class="modal-footer">
	<button type="button" class="btn btn-default" data-dismiss="modal">
		Save
	</button>
	<button type="button" class="btn btn-default" data-dismiss="modal">
		Close
	</button>
</div>
</form>
</div>
</div>
</div>