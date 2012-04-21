
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



<%-- Error Messages --%>

<table width="100%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">

	<logic:messagesPresent>
		<html:messages id="error">
			<tr>
				<td class="error"><c:out value="${error}" /></td>
			</tr>
		</html:messages>
	</logic:messagesPresent>

	<%-- Success Messages --%>
	<logic:messagesPresent message="true">
		<html:messages id="message" message="true">
			<tr>
				<td class="message"><c:out value="${message}" /></td>
			</tr>
		</html:messages>
	</logic:messagesPresent>

</table>
