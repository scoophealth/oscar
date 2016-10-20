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
	
	//--> sort the checkboxes
	$.fn.dataTable.ext.order['dom-checkbox'] = function  ( settings, col )
	{
	    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
	        return $('.toggleActive', td).prop('checked') ? '1' : '0';
	    } );
	};
	
	//--> sort the dashboard selectors.
	$.fn.dataTable.ext.order['dom-select'] = function  ( settings, col )
	{
	    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
	        return $('select', td).val();
	    } );
	}
	
	// table sorting
	$('#libraryTable').DataTable({

		"columnDefs": [ 
		    {
		    	"orderDataType": "dom-checkbox",
		    	"targets": 0
		    },
		    
		    {
		        "searchable": false,
		        "orderable": false,
		        "targets": 1
		    },
		    
		    {
		    	"orderDataType": "dom-select",
		    	"targets": 2
		    }
		]
	});
	
	//--> Indicator template importing functions.
    $(':file').on('fileselect', function(event, numFiles, label) {
        $("#importxmltemplate").val(label);
    });
    
    $("#importbutton").on('click', function(){
    	if(  $("#importxmltemplate").val().match("\.xml$") ) {
    		$("#importForm").submit();
    	} else {
    		alert("XMLFormat Only");
    	}
    });
    
    // --> populate dashboard data into the edit modal window
    $(".editDashboardSelect").on('click', function(){

    	var dashboardId = "#" + this.id;
    	var dashboardName = $(dashboardId + " #selectName").val();
    	var dashboardDescription = $(dashboardId + " #selectDescription").val();
    	var dashboardActive = $(dashboardId + " #selectActive").val();

    	$("#dashboardActiveRow").show();
   	
    	$('#newDashboard #addEditDashboardForm').find('[name="dashboardId"]').val(dashboardId.split("_")[1]);
    	$('#newDashboard #addEditDashboardForm').find('[name="dashboardActive"]').prop("checked", ( dashboardActive == "true" ) );
    	$('#newDashboard #addEditDashboardForm').find('[name="dashboardName"]').val(dashboardName);
    	$('#newDashboard #addEditDashboardForm').find('[name="dashboardDescription"]').val(dashboardDescription);

    	$("#newDashboard").modal();
    })
    
    // --> reset the dashboard create/edit modal
    $("#newDashboard").on('hidden.bs.modal', function () {
        $( ".editDashboard" ).each(function(){
        	$(this).val("");
        })
        $("#dashboardActiveRow").hide();
    });
    
    // --> set the indicator dashboard
    $("#libraryTable  tbody").on('change', '.assignDashboard', function() {   	
    	var url = "/web/dashboard/admin/DashboardManager.do";
    	var data = new Object();
    	data.indicatorId = (this.id).split("_")[1];
    	data.method = (this.id).split("_")[0];
    	data.indicatorDashboardId = $(this).val();
    	sendData(url, data, null);
    });
    
    // --> set the indicator active status
    $("#libraryTable tbody").on('change', ".toggleActive", function() {   	
    	var url = "/web/dashboard/admin/DashboardManager.do";
    	var data = new Object();
    	data.objectId = (this.id).split("_")[1];
    	data.method = (this.id).split("_")[0];  	
    	data.objectClassName = (this.name).split("_")[0]; 
    	if( $(this).is(':checked') ) {
    		data.active = true;
    	}

    	sendData(url, data, null);
    });
    
    // Export Template button
    $("#libraryTable tbody").on('click', ".exportTemplate", function(event) {
    	// event.preventDefault();
    	var url = "/web/dashboard/admin/DashboardManager.do";
    	var data = new Object();
    	data.indicatorId = (this.id).split("_")[1];
    	data.method = (this.id).split("_")[0];  

    	sendData(url, data, "reload");
    });
    
    // Back to dashboard button
    $(".backtoDashboardBtn").on('click', function(event) {
    	event.preventDefault();
    	var url = "/web/dashboard/display/DashboardDisplay.do";
    	var data = new Object();
    	data.dashboardId = (this.id).split("_")[1];
    	data.method = (this.id).split("_")[0];  

    	sendData(url, data, "reload");
    });
    
    // --> File upload triggers.
    $(document).on('change', ':file', function() {
        var input = $(this),
        numFiles = input.get(0).files ? input.get(0).files.length : 1,
        label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
        input.trigger('fileselect', [numFiles, label]);
    });

});

//--> AJAX the data to the server.
function sendData(path, param, target) {
	$.ajax({
		url: ctx + path,
	    type: 'POST',
	    data: param,
	  	dataType: 'html',
	    success: function(data) {
	    	if( target instanceof Array ) {
	    		$.each(target, function(i, val){
	    			$(val).replaceWith( $(val, data) );
	    		});			
	    	} else if( target == "reload") { 
	    		document.open();
		    	document.write(data);
		    	document.close();
	    	} else {			
	    		$(data).replaceWith( $(target, data) );
	    	}
	    }
	});
}



	




