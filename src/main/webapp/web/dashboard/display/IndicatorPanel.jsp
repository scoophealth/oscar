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

<div class="indicatorPanelContainer" >
			
	<div class="row indicatorHeading" >
		<div class="col-md-12">
			<c:out value="${ indicatorPanel.name }" />
		</div>					
	</div>
	
	<!-- indicator panel results body - the graph and numbers -->
	<div class="row indicatorData" >						
		<div class="col-md-12">
	
			<input type="hidden" id="graphPlots_${ indicatorPanel.id }" value="${ indicatorPanel.stringArrayPlots }" />
			<input type="hidden" id="graphLabels_${ indicatorPanel.id }" value="${ indicatorPanel.stringArrayTooltips }" />
			<div class="indicatorGraph" id="graphContainer_${ indicatorPanel.id }" ></div>
		</div>									
	</div>
	
	<div class="row indicatorFooter" >
		<div class="col-md-12 text-right">	
									
	        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" 
	        	aria-haspopup="true" aria-expanded="false">
	        	options <span class="caret"></span>
	        </a>
	        
			<ul class="dropdown-menu pull-right text-left">
				<li>
					<a href="#" data-toggle="modal" data-target="#indicatorInfo_${ indicatorPanel.id }" >
						Indicator Info
					</a>
			    </li>
			    <li>
					<a href="#" class="indicatorDrilldownBtn" id="getDrilldown_${ indicatorPanel.id }" >
						Drill Down
					</a>
			    </li>
	        </ul>
	        					        
		</div>
	</div>
	
	
	<!-- modal panel for displaying this indicators details -->	
	<div id="indicatorInfo_${ indicatorPanel.id }" class="modal fade" role="dialog">
		<div class="modal-dialog">
	
			<div class="modal-content">
				<div class="modal-header">	
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">
						<c:out value="${ indicatorPanel.name }" />
					</h4>
				</div>
				
				<div class="modal-body">
				
					<h4>Category</h4>
					<p><c:out value="${ indicatorPanel.category }" /></p>
					<h4>Sub Category</h4>
					<p><c:out value="${ indicatorPanel.subCategory }" /></p>
					<h4>Definition</h4>
					<p><c:out value="${ indicatorPanel.definition }" /></p>
					<h4>Indicator Framework</h4>
					<p><c:out value="${ indicatorPanel.framework }" /></p>
					<h4>Indicator Framework Version</h4>
					<p><c:out value="${ indicatorPanel.frameworkVersion }" /></p>
					<h4>Notes</h4>
					<p><c:out value="${ indicatorPanel.notes }" /></p>											
					<h4>Query</h4>
					<p><c:out value="${ indicatorPanel.queryString }" /></p>
	
				</div>
				
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						Close
					</button>
				</div>						
			</div> 
			<!-- end modal content -->								
		</div>
	</div> 
	<!--  end indicator modal  -->							
</div>
														

