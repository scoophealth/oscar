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

<div class="lookupListItemsWrapper" >

	<div class="row lookupListItemHeading" >
		<div class="lookupListName row">
			<h3><c:out value="${ lookuplist.listTitle }" /></h3>
		</div>
		<div class="lookupListTitle row">
			<span class="lookupListDescription" >
				<c:out value="${ lookuplist.description }" />
			</span>	
<%-- 			<a class="showHideEdit" id="edit_${ lookuplist.id }" href="javascript:void(0);" >edit</a> --%>
<%-- 			<a class="showHideEdit" id="cancel_${ lookuplist.id }" style="display: none;" href="javascript:void(0);" >cancel</a>			 --%>
		</div>
	</div>
	
	<div class="row lookupListItems" id="lookupListItems_${ lookuplist.id }"  >		
		<ul>						
			<c:forEach var="lookupListItem" items="${ lookuplist.items }" >	
				<c:if test="${ lookupListItem.active }" >
					<li class="lookupListItem" id="lookupListItem_${ lookupListItem.displayOrder }">							
						<span class="label">
							<c:out value="${ lookupListItem.label }" />
						</span>	
						<a href="javascript:void(0);" id="removeLookupListItem_${ lookupListItem.id }_${ lookuplist.id }" 
							class="removeLookupListItem" >X</a>
					</li>
				</c:if>
			</c:forEach>			
		</ul>
	</div>
	
	<div class="addLookupListItemTools">
		<div class="addInput">		
			<input type="text" class="lookupListItemLabel" id="lookupListItemLabel_${ lookuplist.id }" name="lookupListItemLabel_${ lookuplist.id }" value="" />
		</div>
		<div class="addInputButton">
			<input type="button" class="addLookupListItemButton" id="addLookupListItemButton_${ lookuplist.id }" value="add">
		</div>
	</div>
	<div class="clearfix"></div>	
</div>		