
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>



<%@ include file="/taglibs.jsp"%>
<title>Caisi Editor Details</title>
<p>Please fill in information below:</p>
<!-- html:form action="/caisiEditor" focus="caisiEditor.code" onsubmit="return validateIssueAdminForm(this)" -->
<html:form action="/CaisiEditor" focus="caisiEditor.category">
	<input type="hidden" name="method" value="save" />
	<html:hidden property="caisiEditor.id" />

	<div style="color: red"><%@ include file="messages.jsp"%>

	<table>
		<tr>
			<th>Category:</th>
			<td><html:text maxlength="50" property="caisiEditor.category" /></td>
		</tr>
		<tr>
			<th>Label:</th>
			<td><html:text maxlength="255" property="caisiEditor.label" /></td>
		</tr>
		<input type="hidden" name="caisiEditor.type" value="" />
		<input type="hidden" name="caisiEditor.horizontal" value="" />
		<!-- tr>
     <th>Type: </th>
     <td>
     <select name="caisiEditor.type">
     	<option value="">&nbsp;</option>
     	<c:choose>
             <c:when test="${caisiEditorForm.map.caisiEditor.type == 'Select'}">
             <option value="Select" selected>Select</option>
             </c:when>
             <c:otherwise>
             <option value="Select">Select</option>
             </c:otherwise>
        </c:choose>
     	<c:choose>
             <c:when test="${caisiEditorForm.map.caisiEditor.type == 'Checkboxes'}">
             <option value="Checkboxes" selected>Checkboxes</option>
             </c:when>
             <c:otherwise>
             <option value="Checkboxes">Checkboxes</option>
             </c:otherwise>
        </c:choose> 
        <c:choose>
             <c:when test="${caisiEditorForm.map.caisiEditor.type == 'Radio Buttons'}">
             <option value="Radio Buttons" selected>Radio Buttons</option>
             </c:when>
             <c:otherwise>
             <option value="Radio Buttons">Radio Buttons</option>
             </c:otherwise>
        </c:choose>  
        <c:choose>
             <c:when test="${caisiEditorForm.map.caisiEditor.type == 'Text'}">
             <option value="Text" selected>Text</option>
             </c:when>
             <c:otherwise>
             <option value="Text">Text</option>
             </c:otherwise>
        </c:choose>      	
     </select>
     </td>
</tr -->
		<tr>
			<th>Label Value:</th>
			<td><html:text maxlength="255" property="caisiEditor.labelValue" /></td>
		</tr>
		<tr>
			<th>Label Code:</th>
			<td><html:text maxlength="50" property="caisiEditor.labelCode" /></td>
		</tr>
		<!--  tr>
     <th>Horizontal: </th>
     <td>
     <select name="caisiEditor.horizontal">
     	<option value="">&nbsp;</option>
     	<c:choose>
             <c:when test="${caisiEditorForm.map.caisiEditor.horizontal == 'Yes'}">
             <option value="Yes" selected>Yes</option>
             </c:when>
             <c:otherwise>
             <option value="Yes">Yes</option>
             </c:otherwise>
        </c:choose>
     	<c:choose>
             <c:when test="${caisiEditorForm.map.caisiEditor.horizontal == 'No'}">
             <option value="No" selected>No</option>
             </c:when>
             <c:otherwise>
             <option value="No">No</option>
             </c:otherwise>
        </c:choose>     	
     </select>
     </td>
</tr -->
		<tr>
			<th>Active?</th>
			<td><select name="caisiEditor.isActive">
				<option value="">&nbsp;</option>
				<c:choose>
					<c:when test="${caisiEditorForm.map.caisiEditor.isActive == 'Yes'}">
						<option value="Yes" selected>Yes</option>
					</c:when>
					<c:otherwise>
						<option value="Yes">Yes</option>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${caisiEditorForm.map.caisiEditor.isActive == 'No'}">
						<option value="No" selected>No</option>
					</c:when>
					<c:otherwise>
						<option value="No">No</option>
					</c:otherwise>
				</c:choose>
			</select></td>
		</tr>

		<tr>
			<td></td>
			<td><html:submit styleClass="button">Save</html:submit> <!--
       <c:if test="${not empty param.id}">
          <html:submit styleClass="button"   
              onclick="this.form.method.value='delete'">
              Delete</html:submit>
       </c:if>
       	--> <html:submit styleClass="button"
				onclick="this.form.method.value='cancel'">Cancel</html:submit></td>
	</table>
</html:form>
<!-- html:javascript formName="caisiEditorForm"/ -->
