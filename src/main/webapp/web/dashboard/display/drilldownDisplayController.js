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

var dateRegex = /^(\d{1,2})\/(\d{1,2})\/(\d{4})$/;


//*--> METHODS AND FUNCTIONS FOR TICKLER ASSIGNMENT <--*//

//--> check that all fields tagged "required" contain data.
function checkFields() {
	var verified = true;
	
	$("#ticklerAddForm .required").each(function(){
		if( $(this).val().length == 0 ) {
			verified = false;
			paintErrorField($(this));	
		}
	})

	return verified;
}

//paint a red border around missing fields
function paintErrorField( fieldobject ) {
	fieldobject.css( "border", "medium solid red" );
}



//*--> MASTER AJAX METHOD <--*//
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
	    		$('#assignTickler').modal('show').find('.modal-body').html(data);
	    	} else {
		    	document.open();
		    	document.write(data);
		    	document.close();
	    	}
	    }
	});
}

//--> Datatable Filter
$.fn.dataTableExt.afnFiltering.push( function( oSettings, aData, iDataIndex ) {	
    var inputLow = $( '#datatableFilterConditionGreaterThan').val();
    var inputHigh = $( '#datatableFilterConditionLessThan').val();
    var iStartDateCol = $('#datatableFilterColumnSelector').val();
    var iEndDateCol = $('#datatableFilterColumnSelector').val();   
    var condition = $('#datatableFilterConditionSelector').val();

    inputLow = inputLow.trim();
    inputHigh = inputHigh.trim();
    
    iFini = filterDate( inputLow ) * 1;
    iFfin = filterDate( inputHigh ) * 1;
    var datofini = filterDate( aData[iStartDateCol] );
    var datoffin = filterDate( aData[iEndDateCol] );

   	// alert("In Low: " + inputLow + ', Low Filter: ' + iFini + ", In High: " + inputHigh + ", High Filter: " + iFfin + ", Condition: " + condition + ", Data: " + datofini);

    if( condition === 'equal' 
    	&& iFini !== iFini
    	&& new RegExp( '^' + inputLow + '\.*$', 'i' ).test( datofini ) )
    {
    	return true;
    }
    else if ( condition === 'equal' && parseFloat(iFini) == parseFloat(datofini) ) 
    {
    	return true;
    }
    else if ( ( iFini === "" && iFfin === "" ) || ( iFini === 0 && iFfin === 0 ) ) 
    {
    	return true;
    }
    else if ( condition !== 'equal' && parseFloat(iFini) <= parseFloat(datofini) 
    		&& ( iFfin === "" || iFfin === 0 ) )
    {
    	return true;
    }
    else if ( condition !== 'equal' && parseFloat(iFfin) >= parseFloat(datoffin) 
    		&& ( iFini === "" || iFini === 0 ) )
    {
    	return true;
    }
    else if ( condition !== 'equal' && parseFloat(iFini) <= parseFloat(datofini) 
    		&& parseFloat(iFfin) >= parseFloat(datoffin) )
    {
    	return true;
    }

    return false;
});

// Returns Epoch time for easy sorting.
function filterDate(str){
	
	if(str === null ) {
		str = "";
	}
	
	var time = str.replace(/-/g, "/");
	time = time.match(dateRegex);
	
	if( time !== null ){
		var m =+ time[1], d =+ time[2], y =+ time[3];
		var date = new Date( y, m-1, d );
		if( date.getFullYear() === y && date.getMonth() === m-1 ){
			return date.getTime();   
		}
	}
	return str;
}

function isDate( str ) {
	var time = str.replace(/-/g, "/");
	return dateRegex.test(time);
}

//--> date format detection
$.fn.dataTable.moment( 'MM-DD-YYYY' );
$.fn.dataTable.moment( 'MM-D-YYYY' );
$.fn.dataTable.moment( 'M-DD-YYYY' );
$.fn.dataTable.moment( 'M-D-YYYY' );

//--> sort the checkboxes
$.fn.dataTable.ext.order['dom-checkbox'] = function  ( settings, col )
{
    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
        return $('.ticklerChecked', td).prop('checked') ? '1' : '0';
    } );
};

$(document).ready( function() {

	//--> table init
	var drilldownTable = $('#drilldownTable').DataTable({

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
	

	//--> add a customizable user filter to the table.
	$("#drilldownTable_filter").empty().prepend( function(){
		
		var filterGroup = $('#datatableFilterGroup');
		
		// add the columns and move the html into the header of the DataTable
		var select = filterGroup.find( '#datatableFilterColumnSelector' );
		
		$( '#drilldownTable thead th' ).each( function() {
			var id = this.id;
			if( id > 1 ) {
				 select.append( '<option value="' 
						 + id 
						 + '">' 
						 + $(this).html() 
						 + '</option>' );
			}
		});

		filterGroup.find('#datatableFilterColumnSelector').replaceWith( select );
		
		// execute a filter based on the parameters in the tool set.
		filterGroup.find( '#datatableFilterExecuteButton' ).on('click', function(){
			drilldownTable.draw();
		});
		
		// reset all the search values and restore the table.
		filterGroup.find( '#datatableFilterResetButton' ).on('click', function(){
			$( '#datatableFilterConditionGreaterThan').val("").show();
	        $( '#datatableFilterConditionLessThan').val("");
	        $('#datatableFilterColumnSelector').val('0');
	        $('#datatableFilterConditionSelector').val('all');
	        $('.andcondition').hide();
			drilldownTable.draw();
		});

		// bind a show hide event to the additional field for range filtering
		filterGroup.find('#datatableFilterConditionSelector').on('change', function(){
			if( $(this).val() === 'between' ) {
				$('#datatableFilterConditionGreaterThan').show();
				$('.andcondition').show();
			} else {
				$('.andcondition').hide();
			}
			
			if( $(this).val() === 'gt' || $(this).val() === 'equal' ) {
				$('#datatableFilterConditionGreaterThan').val( $('#datatableFilterConditionLessThan').val() );
				$('#datatableFilterConditionLessThan').val("")
				$('#datatableFilterConditionGreaterThan').show();
				$('.lessthancondition').hide();
			}
			
			if( $(this).val() === 'lt' ) {
				$('#datatableFilterConditionLessThan').val( $('#datatableFilterConditionGreaterThan').val() );
				$('#datatableFilterConditionGreaterThan').val("");
				$('#datatableFilterConditionGreaterThan').hide();
				$('.lessthancondition').show();
			}
		});

		return filterGroup.show();
	});

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

    	sendData(url, data, "reload");
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
		
		event.preventDefault();
		var demographics = [];		
		$("input:checkbox.ticklerChecked").each(function(){
			if( this.checked ) {
				demographics.push(this.id);
			}
		});		
		var param = "demographics=" + demographics;

		if( demographics.length > 0 ) {
    		sendData( $(this).attr('href'), param, "modal");
		} else {
			alert("Select at least 1 row to assign a Tickler.");
		}
	});
	
	//--> Export the drilldown query results to csv
	$(".exportResults").on('click', function(){

    	var url = "/web/dashboard/display/ExportResults.do";
    	var data = new Object();
    	data.indicatorId = (this.id).split("_")[1];
     
    	sendData(url, data, null)
	})
	
	//--> Execute the tickler assignment - save
	$("#saveTicklerBtn").on('click', function(event) {
		event.preventDefault();
		if( checkFields() ) {
			sendData("/web/dashboard/display/AssignTickler.do", $("#ticklerAddForm").serialize(), "close")
		}
	});
    
})
