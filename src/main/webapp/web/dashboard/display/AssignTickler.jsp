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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript">

	//--> Date time picker
	$(function(){
	   $('.date-picker').datepicker({
	      format: 'mm-dd-yyyy'
	    });
	});
	
	$(function(){
	   $('.time-picker').timepicker();
	});
	
</script>

<div class="container">
<div class="modal-dialog">
	<div class="modal-content">
		<form name="ticklerAddForm" id="ticklerAddForm" action="${ pageContext.request.contextPath }/web/dashboard/display/AssignTickler.do" method="POST" novalidate >
			<input type="hidden" value="saveTickler" name="method" />
			
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4>Assign Tickler</h4>
			</div>
			<div class="modal-body">
			
				<div class="row">
					<div class="col-xs-12">
						<div class="form-group">
							<div class="well" id="patientTicklerList">
								Assign this Tickler action for each of the
								selected patients.
								<input type="hidden" name="demographics" value="${ demographics }" />
							</div>
						</div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-xs-12">
						<div class="form-group">
							<label>Action:</label> 
							<select class="form-control required" name="ticklerCategoryId" >
								<c:forEach items="${ ticklerCategories }" var="ticklerCategory" >
									<option title="${ ticklerCategory.description }" value="${ ticklerCategory.id }" >
										<c:out value="${ ticklerCategory.category }" />
									</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-xs-6">
						<div class="form-group">
							<label>Assign to:</label> 
							<select class="form-control required" name="taskAssignedTo" >
							<option value=""></option>
								<c:forEach items="${ providers }" var="provider">
									<option value="${ provider.providerNo }">
										<c:out value="${ provider.formattedName }" />
									</option>
								</c:forEach>
							</select>
						</div>
					</div>

					<div class="col-xs-6">
						<div class="form-group">
							<label>Priority:</label>
							
							<select class="form-control required" name="priority" >
							<option value=""></option> 
								<option value="Low" >Low</option>
								<option value="Normal" >Normal</option>
								<option value="High" >High</option>
							</select>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-xs-6">

						<label for="datePickerServiceDate" class="control-label">Service
							Date:</label>
						<div class="controls">
							<div class="input-group">
								<input name="serviceDate" id="datePickerServiceDate" type="text" class="date-picker form-control required" /> 
									<label for="datePickerServiceDate" class="input-group-addon btn">
									<span class="glyphicon glyphicon-calendar"></span>
								</label>
							</div>
						</div>
					</div>

					<div class="col-xs-6">
						<label for="ticklerTime" class="control-label" > Time:</label>
						<div class="controls">
							<div class="input-group">
								<input type="time" name="serviceTime" id="ticklerTime" class="time-picker form-control required" />
								<label for="ticklerTime" class="input-group-addon btn">
									<span class="glyphicon glyphicon-time"></span>
								</label>
							</div>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-xs-12">
					<div class="form-group">
							<label>Message:</label> 
							<select class="form-control required" name="message" >
							<option value=""></option>
								<c:forEach items="${ textSuggestions }" var="textSuggestion" >
									<option>
										<c:out value="${ textSuggestion.suggestedText }" />
									</option>
								</c:forEach>
							</select>
						</div>
						<textarea name="messageAppend" class="form-control" rows="6" placeholder="Additional message." ></textarea>
					</div>
				</div>

			</div>
			<div class="modal-footer">
				<button id="saveTicklerBtn" class="btn btn-default" >
					Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close</button>
			</div>
		</form>
	</div>
</div>
</div>
