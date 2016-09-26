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
var drilldownTable; 
	
$(document).ready( function() {
	
	//--> sort the checkboxes
	$.fn.dataTable.ext.order['dom-checkbox'] = function  ( settings, col )
	{
	    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
	        return $('.ticklerChecked', td).prop('checked') ? '1' : '0';
	    } );
	};
	
	//--> table init
	drilldownTable = $('#drilldownTable').DataTable({
		
		// change the title of the main search box.
		"oLanguage": {
			"sSearch": "Search all columns:"
		},

        // 1. disable sorting and searching on the first column
		// 2. make the tickler checkboxes sortable.
	    "columnDefs": [ 
	        {
		        "searchable": false,
		        "orderable": false,
		        "targets": 0
	    	},
	    	{ 
	    		"orderDataType": "dom-checkbox",
	    		"targets": 1
	    	}
	    ],
	    
	    // turns the first column of the table into an ordered list.
	    "order": [[ 1, 'asc' ]]
	});
	
	// --> Number the Drilldown rows with static numbers.
	drilldownTable.on( 'order.dt search.dt', function () {
		drilldownTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();
	
	// --> add the filters to the bottom footer.
	$("#drilldownTable tfoot th.filter").each( function ( i ) {
		var columnId = this.id;
		
		// exclude the first column. 
		if( i > 0 ) {
	        var select = $('<select class="form-control" ><option value="">All</option></select>')
	            .appendTo( $(this).empty() )
	            .on( 'change', function () {
	            	drilldownTable.column( columnId ).search( $(this).val() ).draw();
	            } );
	 
	        drilldownTable.column( columnId ).data().unique().sort().each( function ( d, j ) {
	            select.append( '<option value="'+d+'">'+d+'</option>' )
	        } );
		}
		
    } );
	
	//--> Re-draw the dashboard.
	$(".backtoDashboardBtn").on('click', function(event) {
    	event.preventDefault();
    	var url = "/web/dashboard/display/DashboardDisplay.do";
    	var data = new Object();
    	data.dashboardId = (this.id).split("_")[1];
    	data.method = (this.id).split("_")[0];  

    	sendData(url, data);
    });
	
	//--> Check all for ticklers
	$("#selectAllDrilldown").on('click', function(event) {
		event.preventDefault();
	    $('.ticklerChecked').prop('checked', 'checked');
	});
	
	//--> Uncheck all for ticklers
	$("#selectNoneDrilldown").on('click', function(event) {
		event.preventDefault();
	    $('.ticklerChecked').prop('checked', '');
	});

	//--> Assign Tickler to all checked items - returns the tickler dialog.
	$("#assignTicklerChecked").on('click', function(event) {

		var demographics = [];		
		$("input:checkbox.ticklerChecked").each(function(){
			if( this.checked ) {
				demographics.push(this.id);
			}
		});
		
		var param = "demographics=" + demographics;
    	sendData( "/web/dashboard/display/AssignTickler.do" , param, "modal");
	})
	
	//--> Export the drilldown query results to csv
	$(".exportResults").on('click', function(){

    	var url = "/web/dashboard/display/ExportResults.do";
    	var data = new Object();
    	data.indicatorId = (this.id).split("_")[1];
     
    	sendData(url, data, null)
	})
    
})

//--> AJAX the data to the server.
function sendData(path, param, target) {
	$.ajax({
		url: ctx + path,
	    type: 'POST',
	    data: param,
	  	dataType: 'html',
	    success: function(data) {
	    	if( target == "close") {
	    		$('#assignTickler').modal('toggle');
	    	} else if( target == "modal") {
	    		$("#assignTickler").html(data).modal();
	    	} else {
		    	document.open();
		    	document.write(data);
		    	document.close();
	    	}
	    }
	});
}