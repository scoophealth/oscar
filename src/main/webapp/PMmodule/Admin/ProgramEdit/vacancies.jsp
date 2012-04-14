<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->
<%@ include file="/taglibs.jsp"%>
<script type="text/javascript">
    $(document).ready(
        function () {
            $('#addGender').click(
                function (e) {
                    $('#sourceOfGender > option:selected').appendTo('#targetOfGender');
                    e.preventDefault();
                });
            $('#removeGender').click(
                function (e) {
                    $('#targetOfGender > option:selected').appendTo('#sourceOfGender');
                    e.preventDefault();
                });
            
            $('#addLanguage').click(
                function (e) {
                    $('#sourceOfLanguage > option:selected').appendTo('#targetOfLanguage');
                    e.preventDefault();
                });
            $('#removeLanguage').click(
                function (e) {
                    $('#targetOfLanguage > option:selected').appendTo('#sourceOfLanguage');
                    e.preventDefault();
                });
            
            $('#addCriteria').click(
                    function (e) {
                        $('#sourceOfCriteria > option:selected').appendTo('#targetOfCriteria');
                        e.preventDefault();
                    });
                $('#removeCriteria').click(
                    function (e) {
                        $('#targetOfCriteria > option:selected').appendTo('#sourceOfCriteria');
                        e.preventDefault();
                    });
        });
</script>
<div class="tabs" id="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Templates">Vacancies</th>
		</tr>
	</table>
</div>
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="30%" class="beright">Requirement Template:</td>
		<td><select name="program.associatedProgram">
				<option selected="selected" value=" ">None Selected</option>
		</select></td>
	</tr>
</table>
<br>
<fieldset>
	<legend>Additional Criteria For this Vavancy/Service Opening</legend>
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td width="30%" class="beright">Requires Specific Gender:</td>
			<td><input type="checkbox" value="on"
				name="program.genderRequired"></td>
		</tr>
		<tr class="b">
			<td colspan="2" style="padding-left: 10%;">
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Gender List</div>
					<div>
						<select id="sourceOfGender" name="sourceOfGender" multiple="multiple" size="7"
							style="width: 200px;">
							<option value="male">male</option>
							<option value="female">female</option>
						</select>
					</div>
				</div>
				<div class="horizonton" style="padding-top: 40px;">
					<div>
						<input type="button" id="addGender" name="addGender" value=">>">
					</div>
					<div>
						<input type="button" id="removeGender" name="removeGender" value="<<">
					</div>
				</div>
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Required Gender</div>
					<div>
						<select id="targetOfGender" name="targetOfGender" multiple="multiple" size="7"
							style="width: 200px;">
						</select>
					</div>
				</div>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td width="30%" class="beright">Requires Specific Language:</td>
			<td><input type="checkbox" value="on"
				name="program.languageRequired"></td>
		</tr>
		<tr class="b">
			<td colspan="2" style="padding-left: 10%;">
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Language List</div>
					<div>
						<select id="sourceOfLanguage" name="sourceOfLanguage" multiple="multiple" size="7"
							style="width: 200px;">
							<option value="fr">French</option>
						</select>
					</div>
				</div>
				<div class="horizonton" style="padding-top: 40px;">
					<div>
						<input type="button" id="addLanguage" name="addLanguage" value=">>">
					</div>
					<div>
						<input type="button" id="removeLanguage" name="removeLanguage" value="<<">
					</div>
				</div>
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Required Language</div>
					<div>
						<select id="targetOfLanguage" name="targetOfLanguage" multiple="multiple" size="7"
							style="width: 200px;">
						</select>
					</div>
				</div>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td width="30%" class="beright">Other Criteria Required:</td>
			<td><input type="checkbox" value="on"
				name="program.otherRequired"></td>
		</tr>
		<tr class="b">
			<td colspan="2" style="padding-left: 10%;">
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Criteria List</div>
					<div>
						<select id="sourceOfCriteria" name="sourceOfCriteria" multiple="multiple" size="7"
							style="width: 200px;">
							<option value="other">Other</option>
						</select>
					</div>
				</div>
				<div class="horizonton" style="padding-top: 40px;">
					<div>
						<input type="button" id="addCriteria" name="addCriteria" value=">>">
					</div>
					<div>
						<input type="button" id="removeCriteria" name="removeCriteria" value="<<">
					</div>
				</div>
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Required Criteria</div>
					<div>
						<select id="targetOfCriteria" name="targetOfCriteria" multiple="multiple" size="7"
							style="width: 200px;">
						</select>
					</div>
				</div>
			</td>
		</tr>
	</table>
</fieldset>
<br>
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="30%" class="beright">Match Closed:</td>
		<td><input type="checkbox" value="on"
			name="program.matchClosed"></td>
	</tr>
	<tr class="b">
		<td class="beright">Date Closed:</td>
		<td><select name="program.dateClosed">
				<option selected="selected" value=" ">04/05/2010</option>
		</select></td>
	</tr>
	<tr class="b">
		<td class="beright">Reason Closed:</td>
		<td><select name="program.reasonClosed">
				<option selected="selected" value=" ">None Selected</option>
		</select></td>
	</tr>
</table>
