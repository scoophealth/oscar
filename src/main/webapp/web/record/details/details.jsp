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
<div class="col-lg-8">
	<button type="button" class="btn btn-primary">Save</button>

	<div class="btn-group">
		<button type="button" class="btn btn-default">Print Label</button>
		<div class="btn-group">
			<button type="button" class="btn btn-default">Print PDF</button>
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				<span class="caret"></span> <span class="sr-only">Toggle Dropdown</span>
			</button>
			<ul class="dropdown-menu" role="menu">
				<li><a href="#">PDF Address</a></li>
				<li class="divider"></li>
			</ul>
		</div>
	</div>
	<div class="btn-group">

		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				Integrator <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a href="#">Compare with Integrator</a></li>
				<li><a href="#">Update from Integrator</a></li>
				<li><a href="#">Send Note Integrator</a></li>
			</ul>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				More <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a href="#">Export to CDS</a></li>
				<li><a href="#">Export to CCD</a></li>
				<li><a href="#">Export to XPHR</a></li>
			</ul>
		</div>
	</div>

	<fieldset>
		<legend>Demographic</legend>
		<div class="form-group">
			<label for="birthday" class="col-xs-2 control-label">Name</label>
			<div class="col-xs-10">
				<div class="form-inline">
					<div class="form-group col-xs-2">
						<select class="form-control">
							<option>Mr.</option>
							<option>Mrs.</option>
							<option>Ms.</option>
							<option>Master</option>
						</select>
					</div>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="lastname" ng-model="page.demo.lastName" />
					</div>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="firstname" ng-model="page.demo.firstName" />
					</div>
				</div>
			</div>
			<br> <label for="birthday" class="col-xs-2 control-label">Sex</label>
			<div class="col-xs-10">
				<div class="form-inline">
					<div class="form-group col-xs-2">
						<select class="form-control" ng-model="page.demo.sex">
							<option>M</option>
							<option>F</option>
							<option>U</option>
						</select>
					</div>
					<label for="birthday" class="col-xs-2 control-label">DOB</label>
					<div class="form-group col-xs-2">
						<input type="text" class="form-control" placeholder="YYYY-MM-DD" ng-model="page.demo.dateOfBirth" />
					</div>
				</div>
			</div>
		</div>
	</fieldset>

</div>

<div class="col-lg-4">
	<img src="<%=request.getContextPath() %>/images/defaultR_img.jpg" /> <br />

	<fieldset>
		<legend>Alerts</legend>
		<textarea class="form-control" rows="3" style="color: red;">{{page.alert}}</textarea>
	</fieldset>

	<fieldset>
		<legend>Notes</legend>
		<textarea class="form-control" rows="3">{{page.notes}}</textarea>
	</fieldset>
	<fieldset>
		<legend>Contacts</legend>
		<ul class="list-group">
			<li class="list-group-item" ng-repeat="contact in page.contacts">{{contact.name}} <span class="pull-right">{{contact.relation}}</span></li>
		</ul>
	</fieldset>

	<fieldset>
		<legend>Professional Contacts</legend>
		<h5 ng-repeat="contact in page.professionalContacts">{{contact.name}}<small>({{contact.profession}})</small><span class="pull-right">{{contact.phone}}</span></h5>
	</fieldset>
</div>