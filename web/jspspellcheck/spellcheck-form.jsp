
<%/*

    spellcheck-form.jsp - Contains the form layout of the spell check dialog.
     This file can be modified to change the way that the screen looks, however
     be carefull not to change the form field names or the methods that the event
     handlers call (for example the onclick="xxxx();" portion of the tag).

    Copyright (C) 2005 Balanced Insight, Inc.

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/%>

<form name="correct">

<table id="MainSpellCheckTable" cellpadding="0" cellspacing="0"
	border="0">

	<tr>
		<!-- Misspelled word, Change to word, and Suggestions -->
		<td id="LeftTD">
		<table id="LeftTable" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="Label"><%=spellcheckBundle.getString("NotInDictionaryLabel")%><br />
				<input type="text" name="original" class="Disabled" readonly="true"
					accessKey="<%=spellcheckBundle.getString("NotInDictionaryKey")%>">
				</td>
			</tr>
			<tr>
				<td class="Label"><%=spellcheckBundle.getString("ChangeToLabel")%><br />
				<input type="text" name="replaceWith"
					accessKey="<%=spellcheckBundle.getString("ChangeToKey")%>">
				</td>
			</tr>
			<tr>
				<td class="Label"><%=spellcheckBundle.getString("SuggestionsLabel")%><br />
				<select size="2" name="suggestions"
					accessKey="<%=spellcheckBundle.getString("SuggestionsKey")%>"
					onkeypress="suggestionsKeyPress();"
					onclick="suggestionsSingleClick();"
					ondblclick="suggestionsDoubleClick();">
				</select></td>
			</tr>
		</table>
		</td>

		<%/*
    Sometimes if the user is clicking quickly we will get a onclick and a ondblclick. So to handle this
     we are performing the same thing that the user wanted on a click with the double click.
    */%>
		<!-- Buttons -->
		<td id="RightTD">
		<table id="RightTable" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
				<button type="button" name="replace" onclick="replaceWord( false );"
					ondblclick="replaceWord( false );"
					accessKey="<%=spellcheckBundle.getString("ReplaceButtonKey")%>">
				<%=spellcheckBundle.getString("ReplaceButtonLabel")%></button>
				</td>
			</tr>
			<tr>
				<td>
				<button type="button" name="replaceAll"
					onclick="replaceAllInstances();"
					ondblclick="replaceAllInstances();"
					accessKey="<%=spellcheckBundle.getString("ReplaceAllButtonKey")%>">
				<%=spellcheckBundle.getString("ReplaceAllButtonLabel")%></button>
				</td>
			</tr>
			<tr>
				<td>
				<button type="button" name="ignore" onclick="ignoreWord();"
					ondblclick="ignoreWord();"
					accessKey="<%=spellcheckBundle.getString("IgnoreButtonKey")%>">
				<%=spellcheckBundle.getString("IgnoreButtonLabel")%></button>
				</td>
			</tr>
			<tr>
				<td>
				<button type="button" name="ignoreAll"
					onclick="ignoreAllInstances();" ondblclick="ignoreAllInstances();"
					accessKey="<%=spellcheckBundle.getString("IgnoreAllButtonKey")%>">
				<%=spellcheckBundle.getString("IgnoreAllButtonLabel")%></button>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
				<button type="button" name="cancel" onclick="cancelAll();"
					accessKey="<%=spellcheckBundle.getString("CancelButtonKey")%>">
				<%=spellcheckBundle.getString("CancelButtonLabel")%></button>
				</td>
			</tr>
			<tr>
				<td>
				<button type="button" name="close" onclick="window.close();"
					accessKey="<%=spellcheckBundle.getString("CloseButtonKey")%>">
				<%=spellcheckBundle.getString("CloseButtonLabel")%></button>
				</td>
			</tr>
		</table>
		</td>
	</tr>

</table>

</form>