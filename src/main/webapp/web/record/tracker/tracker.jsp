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
<!-- <div style="position:fixed;right:20px;">
 <a href="../oscarEncounter/oscarMeasurements/TemplateFlowSheetPrint.jsp?demographic_no=1&template=tracker&htracker" target="trackerSlim" class="btn btn-default"><span class="glyphicon glyphicon-print"></span> Print</a>
 <a href="../oscarEncounter/oscarMeasurements/adminFlowsheet/EditFlowsheet.jsp?flowsheet=tracker&demographic=2&htracker" target="trackerSlim" class="btn btn-default"><span class="glyphicon glyphicon-pencil"></span> Edit</a> 
 <button class="btn btn-default" id="save-all">Save All</button>
</div>
-->

<div style="margin-left:15px;margin-right:15px;">
			  <iframe 
			  	id="trackerSlim"
			  	name="trackerSlim" 
			  	scrolling="No" 
			  	frameborder="0" 
			  	ng-src="{{getTrackerUrl(demographicNo)}}" 
			  	width="100%"
			  	onload="resizeIframe(this)"
				style="min-height:820px"
			  ></iframe>
</div>  
			
<script>
$("#save-all").click(function(){
	//document.getElementById('trackerSlim').contentWindow.saveAll();
	//$('#trackerSlim').contents().find('#trackerForm').submit();
});
</script>