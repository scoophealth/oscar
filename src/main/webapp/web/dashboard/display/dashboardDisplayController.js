/*
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
*/
$(document).ready( function() {

	// get the drill down page
	$(".indicatorDrilldownBtn").on('click', function(event) {
    	event.preventDefault();
    	var url = "/web/dashboard/display/DrilldownDisplay.do";
    	var data = new Object();
    	data.indicatorTemplateId = (this.id).split("_")[1];
    	data.method = (this.id).split("_")[0];  

    	sendData(url, data);
    });
	
	// get the dashboard manager page
	$(".dashboardManagerBtn").on('click', function(event) {
    	event.preventDefault();
    	var url = "/web/dashboard/admin/DashboardManager.do";
    	var data = "dashboardId=" + this.id; 
    	sendData(url, data);
    });
	
	// reload this dashboard with fresh data.
	$(".reloadDashboardBtn").on('click', function(event) {
    	event.preventDefault();
    	var url = "/web/dashboard/display/DashboardDisplay.do";
    	var data = new Object();
    	data.dashboardId = (this.id).split("_")[1];
    	data.method = (this.id).split("_")[0]; 
    	
    	sendData(url, data);
    });
	
	// pie graph plotting
	$(".indicatorGraph").each(function(){
		
		var id = this.id;
		var indicatorId = id.split("_")[1].trim();
		var data = "[" + $( "#graphPlots_" + indicatorId ).val() + "]";
		data = data.replace(/'/g, '"');
		data = JSON.parse( data );
		var tooltips = "[" + $( "#graphLabels_" + indicatorId ).val() + "]";
		tooltips = tooltips.replace(/'/g, '"');

		var plot2;

		try {
			plot2 = $.jqplot( id, data, {
				
				title: ' ',
				seriesDefaults: {
					shadow: false, 
					renderer: $.jqplot.PieRenderer, 
					rendererOptions: { 
						startAngle: 180, 
						sliceMargin: 4, 
						showDataLabels: true } 
				},
				grid: {
				    drawGridLines: false,        	// wether to draw lines across the grid or not.
				        gridLineColor: '#cccccc',   // CSS color spec of the grid lines.
				        background: 'white',      	// CSS color spec for background color of grid.
				        borderColor: 'white',     	// CSS color spec for border around grid.
				        borderWidth: 0,           	// pixel width of border around grid.
				        shadow: false,              // draw a shadow for grid.
				        shadowAngle: 0,            	// angle of the shadow.  Clockwise from x axis.
				        shadowOffset: 0,          	// offset from the line of the shadow.
				        shadowWidth: 0,             // width of the stroke for the shadow.
				        shadowDepth: 0
				},
				legend: { show:true, location: 's' }
				
			});
		} catch (e) {
			plot2 = $(this).append("<p>Data Error " + data + "</p>");
		}
		
		window.onresize = function(event) {
		    plot2.replot();
		}
	})
})

//--> AJAX the data to the server.
function sendData(path, param) {
	$.ajax({
		url: ctx + path,
	    type: 'POST',
	    data: param,
	  	dataType: 'html',
	    success: function(data) {
	    	document.open();
	    	document.write(data);
	    	document.close();
	    }
	});
}