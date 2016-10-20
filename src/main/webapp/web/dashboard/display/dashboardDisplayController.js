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

var jqplotOptions;
var placeHolderCount = 0;
var indicatorPanels = [];

$(document).ready( function() {
	
	jqplotOptions = {		
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
			
		};


	// get the drill down page
	$(".indicatorWrapper").on('click', ".indicatorDrilldownBtn", function(event) {
    	event.preventDefault();
    	var url = "/web/dashboard/display/DrilldownDisplay.do";
    	var data = new Object();
    	data.indicatorTemplateId = (this.id).split("_")[1];
    	data.method = (this.id).split("_")[0];  

    	sendData(url, data, null);
    });
	
	// get the dashboard manager page
	$(".dashboardManagerBtn").on('click', function(event) {
    	event.preventDefault();
    	var url = "/web/dashboard/admin/DashboardManager.do";
    	var data = "dashboardId=" + this.id; 
    	sendData(url, data, null);
    });
	
	// reload this dashboard with fresh data.
	$(".reloadDashboardBtn").on('click', function(event) {
    	event.preventDefault();
    	var url = "/web/dashboard/display/DashboardDisplay.do";
    	var data = new Object();
    	data.dashboardId = (this.id).split("_")[1];
    	data.method = (this.id).split("_")[0]; 
    	
    	sendData(url, data, null);
    });
	
	$(".indicatorWrapper").each(function(){	
		var data = new Object();
		data.method = "getIndicator";
		data.indicatorId = this.id.split("_")[1];

		sendData("/web/dashboard/display/DisplayIndicator.do", data, this.id.split("_")[0]);
	})
	
	placeHolderCount = $(".indicatorWrapper").length;

})

// build Indicator panel with Pie chart.
function buildIndicatorPanel( html, target, id ) {
	
	var indicatorGraph;
	
	if ( indicatorGraph ) {
		indicatorGraph.destroy();
	}
	
	var panel = $( "#" + target + "_" + id ).html( html ); //.append("<h3>" +id+ "</h3>");
	var data = "[" + panel.find( "#graphPlots_" + id ).val() + "]";
	data = data.replace(/'/g, '"');
	data = JSON.parse( data )
	indicatorGraph = $.jqplot ( 'graphContainer_' + id, data, jqplotOptions ).replot();
	
	window.onresize = function(event) {
		indicatorGraph.replot();
	}
	
	var name = panel.find( ".indicatorHeading div" ).text();
	
	var paneldata = [ name, id, data ];
	
	if( paneldata ) {
		indicatorPanels.push( paneldata );
	}

	if( indicatorPanels.length === placeHolderCount ) {

		var panelList;
		
		for(var i = 0; i < indicatorPanels.length; i++ ) {
			var ipanel = indicatorPanels[i];
			var name, id, data;

			if( ipanel ) {
				name = ipanel[0].trim();
				id = ipanel[1];
				data = ipanel[2];
				panelList += ( "NAME " + name + ", ID " + id + "\n " + "  DATA " + data + "\n" ); 
			}

		}		
		return panelList;
	}
}

function sendData(path, param, target) {
	$.ajax({
		url: ctx + path,
	    type: 'POST',
	    data: param,
	  	dataType: 'html',
	    success: function(data) {	    	
	    	if( target === "indicatorId") {
	    		var panelList = buildIndicatorPanel( data, target, param.indicatorId );
	    		if( panelList ) {
	    			console.log( panelList );
	    		}
	    	} else {
		    	document.open();
		    	document.write(data);
		    	document.close();	    		
	    	}
	    }
	});
}