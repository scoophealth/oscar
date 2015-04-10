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
<style>
/* TODO:move this out of here */
li.ui-state-default{
    background:#fff0;
    border:none;
    border-bottom:1px solid #ddd;
    text-align: left;
    padding-top:4px;
    padding-bottom: 10px;
}

li.ui-state-default{
cursor: pointer;
}

li.ui-state-default:last-child{
    border-bottom:none;
}

.well-note{
    border-color:#d2d2d2;
    box-shadow:0 1px 0 #cfcfcf;
    border-radius:3px;
    
    /*not sure if this will stay*/
    max-height:300px;
    overflow-y:auto;
}

.
</style>


<!-- #996633 cpp -->
<!-- #006600 famhx -->
<!-- #306754 othermeds -->
<!-- not sure colours are staying -->
<!--ng-style="page.code == 'famhx' && {'background-color' : '#006600'} || page.code == 'othermeds' && {'background-color' : '#306754'}"-->

<!-- make div layout more fluid see medical history as an example -->
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
	<div class="modal-content" style="background-color: #996633; color: white;   border:0px;  border-radius: 0px;">

		<div class="modal-header"  > <!-- ng-style="setColor(note.cpp)" -->
		<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="cancel()"><span aria-hidden="true" >&times;</span></button>
			<h4 class="modal-title">{{page.title}}</h4>
		</div>

		<div class="modal-body" style="background-color:#fff;color:#333">
			<!--<h1> {{page.code}} </h1>
			{{page}}-->

			
<!-- 
        	      
        <div> 
			<table class="table table-striped">
				<tr ng-repeat="item in page.items">
        			<td><a ng-click="gotoState(item,mod)" >{{item.displayName}}</td>
        			<td>{{item.date | date : 'dd-MMM-yyyy'}}</td>
        			<td>{{item.editor}}</td>
        			<%--td><a ng-click="gotoState(item,mod)" >code</a></td  --%>
        	    </tr> 
        	</table>
        	</div>		
-->	
	
		<!-- TODO: what happens here if there is a long list of notes??? -->
		<div class="well well-note">
		<ul data-brackets-id="12674" id="sortable" class="list-unstyled ui-sortable" >
		    
		
		    <li class="ui-state-default" ng-repeat="item in page.items" ng-click="changeNote(item,item.id)">
		    <small class="pull-left text-muted">{{item.editor}}</small>
		    <small class="pull-right text-muted">
		       <span class="glyphicon glyphicon-calendar"></span> {{item.date | date : 'dd-MMM-yyyy'}}
		    </small>
		    <br>
		    {{item.displayName}}
		    </li>
			<span class="text-muted" ng-if="page.items==null">No entries</span>
		</ul>
		</div><!-- well -->
    
		<hr>
    
		<div id="form-wrapper"> <!-- ng-if="action == 'add'" --> 
		
		<form class="form-inline" id="frmIssueNotes"> 

			<!-- hidden not needed -->
			<input type="hidden" id="issueChange" name="issueChange" value="">

			<input type="hidden" id="annotation_attrib" name="annotation_attrib" ng-model="groupNotesForm.encounterNote.annotation_attrib">


				<input type="hidden" name="id" ng-model="groupNotesForm.encounterNote.id">

				<em><small>Editors: <span>{{ groupNotesForm.encounterNote.providerName }}</span></small></em>  
				<div class="pull-right"><em><small>Encounter Date: <span>{{groupNotesForm.encounterNote.updateDate | date : 'dd-MMM-yyyy'}}</span>  Rev: <a href="javascript:void(0)" ng-click="openRevisionHistory(groupNotesForm.encounterNote)">{{groupNotesForm.encounterNote.revision}}</a></small></em></div>
				
				<textarea class="form-control" rows="5" placeholder="Enter Note" ng-model="groupNotesForm.encounterNote.note" style="margin-bottom:6px;" required></textarea>
					
				<div ng-if="page.code == 'ongoingconcerns' " class="row">
					<div class="col-lg-6">					
					<label><bean:message key="oscarEncounter.problemdescription.title" /></label>
					<input type="text" class="form-control" id="problemdescription"	name="problemdescription" ng-model="groupNotesForm.groupNoteExt.problemDesc" placeholder="<bean:message key="oscarEncounter.problemdescription.title" />" />
					</div><!-- col-lg-6 -->
					
					<div class=" col-lg-6">	
					<label><bean:message key="oscarEncounter.problemStatus.title" /> <span class="glyphicon glyphicon-info-sign" tooltip-placement="right" tooltip="Examples: <bean:message key="oscarEncounter.problemStatusExample.msg" />"></span></label>
					<input type="text" class="form-control" id="problemstatus" name="problemstatus" ng-model="groupNotesForm.groupNoteExt.problemStatus" placeholder="<bean:message key="oscarEncounter.problemStatus.title" /> " />
					
					<!-- example: <bean:message key="oscarEncounter.problemStatusExample.msg" /> -->
					</div><!-- col-lg-6 -->
					
				</div><!-- row -->
		
		
				<div class="row">		    
					<div class="col-lg-6">
					<label><bean:message key="oscarEncounter.startdate.title" /></label>
					<div class="input-group">
					<input type="text" class="form-control" placeholder="<bean:message key="oscarEncounter.startdate.title" />"  
					ng-model="groupNotesForm.groupNoteExt.startDate" 
					datepicker-popup="yyyy-MM-dd" 
					datepicker-append-to-body="false" 
					is-open="startDatePicker" 
					ng-click="startDatePicker = true" 
					placeholder="YYYY-MM-DD"
					/>
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
					</div><!-- col-lg-6 -->		
						

					<div class="col-lg-6">
					<label><bean:message key="oscarEncounter.resolutionDate.title" /></label>			  
					<div class="input-group">	
					<input type="text" class="form-control" placeholder="<bean:message key="oscarEncounter.resolutionDate.title" />"  
					ng-model="groupNotesForm.groupNoteExt.resolutionDate" 
					datepicker-popup="yyyy-MM-dd" 
					datepicker-append-to-body="false" 
					is-open="resolutionDatePicker" 
					ng-click="resolutionDatePicker = true" 
					placeholder="YYYY-MM-DD"
					/>
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
					</div><!-- col-lg-6 -->
				</div><!-- row -->			    

<div class="row">
		
<div ng-if="page.code == 'famhx' " class="col-lg-6"> 
		<label><bean:message key="oscarEncounter.ageAtOnset.title" /></label>
		<input ng-if="page.code == 'famhx' " type="text" class="form-control" id="ageatonset" name="ageatonset" ng-model="groupNotesForm.groupNoteExt.ageAtOnset" placeholder="<bean:message key="oscarEncounter.ageAtOnset.title" />" />
</div>				
		<div ng-if="page.code == 'medhx'" class="col-lg-6">	
		<label><bean:message key="oscarEncounter.procedureDate.title" /></label>	 
		<div  class="input-group">   	
		<input type="text" class="form-control" id="proceduredate" name="proceduredate" placeholder="<bean:message key="oscarEncounter.procedureDate.title" />" 
		ng-model="groupNotesForm.groupNoteExt.procedureDate" 
		datepicker-popup="yyyy-MM-dd" 
		datepicker-append-to-body="false" 
		is-open="procedureDatePicker" 
		ng-click="procedureDatePicker = true" 
		placeholder="YYYY-MM-DD"
		/>
		<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
		</div>
		</div>
		
<div ng-if="page.code == 'medhx' || page.code == 'famhx' " class="col-lg-6"> 
		<label><bean:message key="oscarEncounter.treatment.title" /></label>				    	
		<input  type="text" class="form-control" id="treatment" name="treatment" ng-model="groupNotesForm.groupNoteExt.treatment" placeholder="<bean:message key="oscarEncounter.treatment.title" />" />
</div>

<div ng-if="page.code == 'medhx'" class="col-lg-6">
		<label><bean:message key="oscarEncounter.relationship.title" /></label>
		<input type="text" class="form-control" id="relationship" name="relationship" ng-model="groupNotesForm.groupNoteExt.relationship" placeholder="<bean:message key="oscarEncounter.relationship.title" />" />
</div>


				
		<input ng-if="page.code == 'riskfactors' " type="text" class="form-control" id="exposuredetail" ng-model="groupNotesForm.groupNoteExt.exposureDetail" placeholder="<bean:message key="oscarEncounter.exposureDetail.title" />" />

</div><!--row-->			

		
				<div class="row">		    
				<div ng-if="page.code == 'medhx' || page.code == 'famhx' || page.code == 'ongoingconcerns' " class="col-lg-6">
				    <label><bean:message key="oscarEncounter.lifestage.title" /></label>
				    <select class="form-control" name="lifestage" id="lifestage" ng-model="groupNotesForm.groupNoteExt.lifeStage">
							<option value="">
								<bean:message key="oscarEncounter.lifestage.opt.notset" />
							</option>
							<option value="N">
								<bean:message key="oscarEncounter.lifestage.opt.newborn" />
							</option>
							<option value="I">
								<bean:message key="oscarEncounter.lifestage.opt.infant" />
							</option>
							<option value="C">
								<bean:message key="oscarEncounter.lifestage.opt.child" />
							</option>
							<option value="T">
								<bean:message key="oscarEncounter.lifestage.opt.adolescent" />
							</option>
							<option value="A">
								<bean:message key="oscarEncounter.lifestage.opt.adult" />
							</option>
					</select>
				</div><!-- col-lg-6 -->
		

				</div><!-- row -->
		
		
				<div class="row">
					    
				<div class="col-lg-6"><!-- TODO: most likely a typeahead and display assigned issues below using the badges or labels-->
				<label><bean:message key="oscarEncounter.Index.assnIssue" /></label>			
					 <input type="text" class="form-control" placeholder="<bean:message key="oscarEncounter.Index.assnIssue" />"  />
				</div><!-- col-lg-6 -->	   
				
				<div class="col-lg-3">		   
				<label><bean:message key="oscarEncounter.Index.btnPosition" /></label>
					<select class="form-control" id="position" name="position" >
					<option id="popt0" value="0">1</option>
					</select>
				</div> <!-- col-lg-4 -->

				<div class="col-lg-3">
		<!--shouldn't this just be a single checkbox and the answer is always no unless checked?-->				    
				 <label><bean:message key="oscarEncounter.hidecpp.title" /></label>
					<div class="form-group" ng-init="groupNotesForm.groupNoteExt.hideCpp=0">
					<label class="radio-inline" id="hidecpp" name="hidecpp">
					  <input type="radio" id="hidecpp" name="hidecpp" ng-model="groupNotesForm.groupNoteExt.hideCpp" value="0"> No
					</label>
					<label class="radio-inline" >
					  <input type="radio" id="hidecpp" name="hidecpp" ng-model="groupNotesForm.groupNoteExt.hideCpp" value="1"> Yes
					</label>
					</div><!-- form-group -->
				
				</div><!-- col-lg-4 -->
				
				</div> <!-- row -->

				<div class="row">
				  <div class="col-lg-12" style="margin-top:6px;">
				<div class="checkbox" >
				    <label>
				      <input type="checkbox" ng-model="groupNotesForm.issue.issueId" ng-true-value="'{{page.issueId}}'" ng-false-value="'0'">  <em>{{page.title}}</em>  as part of cpp

				    </label>
				</div>
				
				  </div>
				</div><!-- row -->
		
		</form>
			
		</div><!-- form-wrapper -->
	        	
		</div><!-- modal-body -->		
		
		<div class="modal-footer" style="background-color:#eeeeee;margin-top:0px">
		<%--
			<form class="form-horizontal">
				 <div class="form-group">
				 
				 	<div class="col-sm-12">
						<textarea class="form-control" rows="3"></textarea>
					</div>
					<br/>
				    <div class="col-sm-8">
				      <input type="text" class="form-control" placeholder="Start Date"  />
				    </div>
				    <br/>
				    <div class="col-sm-8">
				      <input type="text" class="form-control" placeholder="Resolution Date"  />
				    </div>
				    
			   
				</div>
			</form>
			--%>

<!-- TODO: see what of these can be functions inline or maybe obsolete???
 
 				<input type="image"	src="<c:out value="${ctx}/oscarEncounter/graphics/copy.png"/>" title='<bean:message key="oscarEncounter.Index.btnCopy"/>' onclick="copyCppToCurrentNote(); return false;"> 
 				<input type="image"	src="<c:out value="${ctx}/oscarEncounter/graphics/annotation.png"/>" title='<bean:message key="oscarEncounter.Index.btnAnnotation"/>' id="anno" style="padding-right: 10px;"> 
 				<input type="image"	src="<c:out value="${ctx}/oscarEncounter/graphics/edit-cut.png"/>" title='<bean:message key="oscarEncounter.Index.btnArchive"/>' onclick="$('archived').value='true';" style="padding-right: 10px;">

 -->

<div ng-show="master" style="text-align:left;color:#000000">{{master | json}}</div>

		<br>
	<button ng-click="archiveGroupNotes()" type="button" class="btn btn-danger"><bean:message key="oscarEncounter.Index.btnArchive"/></button>	
	<button ng-click="saveGroupNotes()" type="button" class="btn btn-primary"><bean:message key="oscarEncounter.Index.btnSignSave"/></button>	
		
			<button ng-click="cancel()" type="button" class="btn"><bean:message key="modal.newPatient.close" bundle="ui"/></button>
		</div>
	</div>
