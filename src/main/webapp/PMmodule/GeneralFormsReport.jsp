
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

<script type="text/javascript"
	src="<html:rewrite page="/share/calendar/calendar.js" />" /></script>
<script type="text/javascript"
	src="<html:rewrite page="/share/calendar/lang/calendar-en.js" />"></script>
<script type="text/javascript"
	src="<html:rewrite page="/share/calendar/calendar-setup.js" />"></script>

<script type="text/javascript">
			var djConfig = {
				isDebug: false,
				parseWidgets: false,
				searchIds: ["addPopupTimePicker"]
			};
		</script>

<script type="text/javascript"
	src="<html:rewrite page="/dojoAjax/dojo.js" />">
		</script>

<script type="text/javascript" language="JavaScript">
            dojo.require("dojo.date.format");
			dojo.require("dojo.widget.*");
			dojo.require("dojo.validate.*");
		</script>



<script type="text/javascript">
		
	function popupPage2(varpage, windowname) {
        var page = "" + varpage;
        var windowprops = "height=700,width=1000,top=10,left=0,location=yes,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
        var popup = window.open(page, windowname, windowprops);
        if (popup != null) {
            if (popup.opener == null) {
                popup.opener = self;
            }
            popup.focus();
        }
    }
    
    function getIntakeReportByNodeId(nodeId) {
    
    var oneWeekAgo = new Date();
    oneWeekAgo.setDate(oneWeekAgo.getDate() - 7);
  	
    var startDate = prompt("Please enter a start date in this format (e.g. 2000-01-01)", dojo.date.format(oneWeekAgo, {selector:'dateOnly', datePattern:'yyyy-MM-dd'}));
    
    if (startDate == null) {
        return;
    }
    if (!dojo.validate.isValidDate(startDate, 'YYYY-MM-DD')) {
        alert("'" + startDate + "' is not a valid start date");
        return;
    }

    var endDate = prompt("Please enter the end date in this format (e.g. 2000-12-01)", dojo.date.format(new Date(), {selector:'dateOnly', datePattern:'yyyy-MM-dd'}));
    if (endDate == null) {
        return;
    }
    if (!dojo.validate.isValidDate(endDate, 'YYYY-MM-DD')) {
        alert("'" + endDate + "' is not a valid end date");
        return;
    }

    var includePast = confirm("Do you want to include past intake forms in your report? ([OK] for yes / [Cancel] for no)");

    alert("Generating report from " + startDate + " to " + endDate + ". Please note: it is normal for the generation process to take up to a few minutes to complete, be patient.");

    var url = '<html:rewrite action="/PMmodule/GenericIntake/Report"/>?' + 'nodeId=' + nodeId + '&method=report' + '&type=&startDate=' + startDate + '&endDate=' + endDate + '&includePast=' + includePast;
    
    popupPage2(url, "IntakeReport" + nodeId);
}
</script>

Report:&nbsp;
<select onchange="getIntakeReportByNodeId(this.options[this.selectedIndex].value);">
	<option value="" selected></option>
	<c:forEach var="node" items="${generalIntakeNodes}">
		<option value="<c:out value="${node.id}"/>"><c:out value="${node.label.label}" /></option>
	</c:forEach>
</select>
