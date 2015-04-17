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
/* will move out when done */
legend{}
.add-summary{
   /* opacity: 0.4;
    filter: alpha(opacity=40); /* For IE8 and earlier */
}

.add-summary:hover{
color:#333 !important;
text-decoration:none;
}
</style>
	<div class="col-lg-3">
       <fieldset ng-repeat="mod in page.columnOne.modules">
       		<legend style="margin-bottom:0px;"> 

		<a href="javascript:void(0)" style="font-size:12px;color:#333;padding-top:10px" class="pull-right" ng-click="gotoState('add', mod)" ng-hide="mod.summaryCode=='meds' || mod.summaryCode=='assessments'">
			<span class="glyphicon glyphicon-plus-sign" title="{{mod.summaryCode}}"></span>
		</a> 

		<a href="javascript:void(0)" style="font-size:12px;color:#333;padding-top:10px" class="pull-right" ng-click="openRx(demographicNo)" ng-show="mod.summaryCode=='meds'">
		<span class="glyphicon glyphicon-plus-sign" title="{{mod.summaryCode}}"></span>
		</a>

		<a href="#/record/{{demographicNo}}/forms" style="font-size:12px;color:#333;padding-top:10px" class="pull-right" ng-show="mod.summaryCode=='assessments'">
		<span class="glyphicon glyphicon-plus-sign" title="{{mod.summaryCode}}"></span>
		</a>

		{{mod.displayName}}
		</legend>


        	<ul style="padding-left:12px;">
        	<%-- href="{{item.action}}" --%>
        	<li ng-repeat="item in mod.summaryItem" ng-show="$index < mod.displaySize"><span class="pull-right">{{item.date | date : 'dd-MMM-yyyy'}}</span><a ng-click="gotoState(item,mod,item.id)" href="javascript:void(0)" >{{item.displayName | limitTo: 34 }} {{item.displayName.length > 34 ? '...' : '' }}<small ng-show="item.classification">({{item.classification}})</small></a> </li> 



		<a href="javascript:void(0)" class="text-muted add-summary" ng-if="mod.summaryItem==null" ng-click="gotoState('add', mod)" ng-hide="mod.summaryCode=='meds' || mod.summaryCode=='assessments'">Add {{mod.displayName}}</a>

		<a href="javascript:void(0)" class="text-muted add-summary" ng-if="mod.summaryItem==null" ng-click="openRx(demographicNo)" ng-show="mod.summaryCode=='meds'">Add {{mod.displayName}}</a>

		<a href="#/record/{{demographicNo}}/forms" class="text-muted add-summary" ng-if="mod.summaryItem==null" ng-show="mod.summaryCode=='assessments'">Add {{mod.displayName}}</a>

        	</ul>
		
       </fieldset>   
    </div>
    
    <div class="col-lg-6" id="middleSpace">
        <ul class="nav nav-pills">
		  <li ng-class="isCurrentStatus('none')"><a data-target="#all" ng-click="removeFilter(0)" data-toggle="tab">All</a></li>
		  <li ng-class="isCurrentStatus('Just My Notes')"><a ng-click="changeNoteFilter('Just My Notes')">Just My Notes</a></li>
		  <li ng-class="isOnlyNotesStatus()"><a ng-click="setOnlyNotes()" >Just Notes</a></li>
		  <li><a href="javascript:void(0)" data-target="#tracker" role="tab" data-toggle="tab" ng-click="getTrackerUrl(demographicNo)" >Tracker</a></li>
		</ul>

    		<div class="tab-content">
			  <div class="tab-pane active" id="all">
			   	<dl infinite-scroll="addMoreItems()">
			    				<dt ng-style="setColor(note)" ng-repeat-start="note in page.notes.notelist" ng-show="showNoteHeader(note)">
			    				<%-- div class="btn-group btn-group-xs pull-right">
								  <button class="btn btn-default btn-xs" type="button">
								    edit 
								  </button>
								  <button class="btn btn-default btn-xs dropdown-toggle" type="button" data-toggle="dropdown">
								  	<span class="caret"></span>
			    					<span class="sr-only">Toggle Dropdown</span>
								  </button>
								  <ul class="dropdown-menu" role="menu">
								    <li><a href="#">print</a></li> <li><a href="#">annotate</a></li><li><a href="#">set Encounter Date</a></li><li><a href="#">set Encounter Type</a></li>
								  </ul>
								</div --%>{{note.observationDate | date : 'dd-MMM-yyyy'}} {{firstLine(note)}}</dt>
			    				<dd ng-repeat-end  ng-show="showNote(note)"><pre ng-class="isNoteBeingEdited(note)" style="margin-bottom:0px;" ng-show="showNote(note)" ng-hide="note.cpp==true" ng-dblclick="editNote(note)">{{note.note}}</pre>
			    					<h6 style="margin-top:1px;margin-bottom:0px;">Editors: <small>{{note.editorNames}}</small> <span class="pull-right">Encounter Date: <small>{{note.observationDate | date: 'medium'}}</small> Rev: <small ng-click="openRevisionHistory(note)" >{{note.revision}}</small></span></h6>

			    					<h6 style="margin-top:0px;">Assigned Issues: <small>{{note.issueDescriptions}}</small> <span class="pull-right">Enc Type: <small>{{note.encounterType}}</small></span></h6>
			    				</dd>					
			    		</dl>
			  </div>

			  <div class="tab-pane" id="tracker">
			      <iframe
				  id="trackerSlim"
				  scrolling="No"
				  frameborder="0"
				  ng-src="{{ trackerUrl }}"
				  width="100%"
				style="min-height:820px"
			      ></iframe>
			  </div>

		</div><!-- tab content -->  
	
     </div><!-- middleSpace -->


	 <div class="col-lg-3">
	 	<fieldset ng-repeat="mod in page.columnThree.modules">
       		<legend style="margin-bottom:0px;">{{mod.displayName}}
       			<div class="form-group">
					<input type="text" class="form-control search-query" ng-model="incomingQ" placeholder="Search">
				</div>
			</legend>
        	<ul style="padding-left:12px;">
        	<%-- href="{{item.action}}" --%>
        	<li ng-repeat="item in mod.summaryItem | filter:incomingQ" ng-show="$index < mod.displaySize"  ><span class="pull-right">{{item.date | date : 'dd-MMM-yyyy'}}</span><a ng-click="gotoState(item)" >{{item.displayName}}<small ng-show="item.classification">({{item.classification}})</small></a> </li> 
        	</ul>
        	<a ng-click="expandlist(mod)" ng-show="showMoreDocuments(mod)" ><span ng-class="showMoreDocumentsSymbol(mod)"></span></a>
       </fieldset>
	 	
	 <%-- 
      	 <fieldset>
        	<legend style="margin-bottom:0px;">Incoming 
        		<div class="form-group">
					<input type="text" class="form-control search-query" ng-model="incomingQ" placeholder="Search">
				</div>
			</legend>     		
       		
       		<h5 ng-repeat="item in documentlabs | filter:incomingQ" ng-show="$index < documentlabsSize" ><a ng-click="changeTab(12)" >{{item.desc}}<small>({{item.type}})</small></a><span class="pull-right">{{item.date}}</span></h5>
        		<a ng-click="expandlist()" ng-show="showMoreDocuments()" ><span class="glyphicon glyphicon-chevron-down pull-right"></span></a>
        		<a ng-click="expandlist()" ng-hide="showMoreDocuments()" ><span class="glyphicon glyphicon-chevron-up pull-right"></span></a>
      	</fieldset>
      
      --%>	
	 </div>
