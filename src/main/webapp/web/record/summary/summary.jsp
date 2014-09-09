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
	<div class="col-lg-3">
       <fieldset ng-repeat="mod in page.columnOne.modules">
       		<legend style="margin-bottom:0px;">{{mod.displayName}}</legend>
        	<ul style="padding-left:12px;">
        	<%-- href="{{item.action}}" --%>
        	<li ng-repeat="item in mod.summaryItem" ng-show="$index < mod.displaySize"  ><a ng-click="gotoState(item)" >{{item.displayName}}<small ng-show="item.type">({{item.type}})</small></a> <span class="pull-right">{{item.date}}</span></li> 
        	</ul>
       </fieldset>   
    </div>
    
    <div class="col-lg-5" id="middleSpace">
        <ul class="nav nav-pills">
		  <li ng-class="isCurrentStatus('none')"><a data-target="#all" ng-click="removeFilter(0)" data-toggle="tab">All</a></li>
		  <li ng-class="isCurrentStatus('Just My Notes')"><a ng-click="changeNoteFilter('Just My Notes')" >Just My Notes</a></li>
		  <li ng-class="isOnlyNotesStatus()"><a ng-click="setOnlyNotes()" >Just Notes</a></li>
		  <li><a data-target="#tracker" role="tab" data-toggle="tab">Tracker</a></li>
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
			    				<dd ng-repeat-end  ng-show="showNote(note)"><pre ng-class="isNoteBeingEdited(note)" style="margin-bottom:0px;" ng-show="showNote(note)"  ng-dblclick="editNote(note)">{{note.note}}</pre>
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
			  	ng-src="{{getTrackerUrl(demographicNo)}}" 
			  	width="100%"
			  	onload="resizeIframe(this)"
				style="min-height:820px"
			  ></iframe>
			  </div>

		</div><!-- tab content -->  
	
     </div><!-- middleSpace -->




	<%--div class="col-lg-4" id="rSpace">
     		<ul ui-scroll-viewport style="height:800px;border:1px solid black;">
				<li ui-scroll="note in datasource" buffer-size="30"> 
				
        			<dl >
    					<dt ng-style="setColor(note)" >{{note.observationDate | date : 'dd-MMM-yyyy'}} {{firstLine(note)}} </dt>
    					<dd><pre class="pre-scrollable" ng-show="showNote(note)">{{note.note}}</pre></dd>
    					
    				</dl>
    				
    			</li>
    		</ul>
    		
    			
	</div --%>
