<%@ include file="/taglibs.jsp" %>

		<html:hidden property="bedlog.id"/>
		<html:hidden property="view.bedLogTime"/>
		<div class="tabs" id="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
				<tr>
					<th title="Programs">Bed Log</th>
				</tr>
			</table>
		</div>
		
		<table width="100%" border="1" cellspacing="2" cellpadding="3">
			<tr class="b">
				<td width="20%">Enabled:</td>
				<td>
					<html:select property="bedlog.enabled">
						<html:option value="true"/>
						<html:option value="false"/>
					</html:select>
				</td>
			</tr>
			<tr class="b">
				<td width="20%">Check Times:</td>
				<td>
					<display:table border="1" cellspacing="2" cellpadding="3" id="time" name="bedlog_checktimes" export="false" pagesize="0">
						<display:setProperty name="basic.show.header" value="false"/>
						<display:column sortable="false" title="Name">
							<c:out value="${time}"/>
						</display:column>
						<display:column sortable="false" title="Name">
							<a href="javascript:void(0);return false;" onclick="document.programManagerForm.elements['view.bedLogTime'].value='<c:out value="${time}"/>';document.programManagerForm.method.value='bedlog_remove_checktime';document.programManagerForm.submit();">Remove</a>
						</display:column>
						
					</display:table>
					<br/>
					<html:select property="view.bedLogHour">
						<% for(int x=1;x<13;x++) { %>
							<html:option value="<%=String.valueOf(x) %>"/>
						<% } %>
					</html:select>
					<html:select property="view.bedLogMinute">
						<% for(int x=0;x<60;x++) { %>
							<html:option value="<%=(x< 10)?"0" + String.valueOf(x):String.valueOf(x) %>"/>
						<% } %>
					</html:select>
					<html:select property="view.bedLogAmPm">
						<html:option value="AM"/>
						<html:option value="PM"/>
					</html:select>
					&nbsp;
					<input type="button" value="Add Check Time" onclick="this.form.method.value='bedlog_add_checktime';this.form.submit();"/>
				</td>
			</tr>
			<tr class="b">
				<td width="20%">Statuses:</td>
				<td>
					<display:table border="1" cellspacing="2" cellpadding="3" id="status" name="bedlog_statuses" export="false" pagesize="0">
						<display:setProperty name="basic.show.header" value="false"/>
						<display:column sortable="false" title="Name">
							<c:out value="${status}"/>
						</display:column>
						<display:column sortable="false" title="Name">
							<a href="javascript:void(0);return false;" onclick="document.programManagerForm.elements['view.bedLogStatus'].value='<c:out value="${status}"/>';document.programManagerForm.method.value='bedlog_remove_status';document.programManagerForm.submit();">Remove</a>
						</display:column>
					</display:table>
					<br/>
					<html:text property="view.bedLogStatus"/>
					&nbsp;<input type="button" value="Add Status" onclick="this.form.method.value='bedlog_add_status';this.form.submit();"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
		                <input type="button" value="Save" onclick="this.form.method.value='save_bedlog';this.form.submit();"/>
				</td>
			</tr>
		</table>
		