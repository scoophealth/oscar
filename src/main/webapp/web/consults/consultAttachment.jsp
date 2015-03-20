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
<div class="modal-content">
	<style>
		.middle {
			display: inline-block;
			vertical-align: middle;
			float: none;
		}
	</style>

	<div class="modal-header">
		<h3 class="modal-title">Documents for {{atth.patientName}}</h3>
	</div>

	<div class="modal-body">
		<div class="col-sm-5 middle">
			<label>Available Documents</label>
			<br/>
			<select id="selAvailDoc" class="form-control" size="8" ng-model="atth.selectedAvailableDoc" ng-options="doc.displayName group by doc.documentTypeDisplay for doc in atth.availableDocs" ng-dblclick="openDoc(atth.selectedAvailableDoc)"></select>
		</div>
		<div class="middle">
			<button type="button" class="btn btn-default" ng-click="attach()">&gt;&gt;</button>
			<br/>
			<button type="button" class="btn btn-default" ng-click="detach()">&lt;&lt;</button>
		</div>
		<div class="col-sm-5 middle">
			<label>Attached Documents</label>
			<br/>
			<select id="selAttachDoc" class="form-control" size="8" ng-model="atth.selectedAttachedDoc" ng-options="doc.displayName group by doc.documentTypeDisplay for doc in atth.attachedDocs" ng-dblclick="openDoc(atth.selectedAttachedDoc)"></select>
		</div>
		<div class="clear"></div>
		<div style="text-align:center;">(Double-click the document name to view it)</div>
	</div>

	<div class="modal-footer">
		<button type="button" class="btn" ng-click="done()">Done</button>
	</div>
</div>
