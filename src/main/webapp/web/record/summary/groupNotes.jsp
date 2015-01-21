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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
	<div class="modal-content">
		<div class="modal-header">
			<h3 class="modal-title">{{page.title}}</h3>
		</div>

		<div class="modal-body">
			<button style="margin-top: -10px;" type="button" class="close" data-dismiss="modal" aria-hidden="true" ng-click="cancel()"></button>
			<table class="table table-striped">
				<tr ng-repeat="item in page.items"   >
        			
        			<td><a ng-click="gotoState(item,mod)" >{{item.displayName}}</td>
        			<td>{{item.date | date : 'dd-MMM-yyyy'}}</td>
        			<td>{{item.editor}}</td>
        			<%--td><a ng-click="gotoState(item,mod)" >code</a></td  --%>
        	    </tr> 
        	</table>
		</div>		
		
		<div class="modal-footer">
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
		<br>
			<button ng-click="cancel()" type="button" class="btn"><bean:message key="modal.newPatient.close" bundle="ui"/></button>
		</div>
	</div>