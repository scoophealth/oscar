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
	
	// table sorting
	$('#libraryTable').DataTable();
	
    $(':file').on('fileselect', function(event, numFiles, label) {
        $("#importxmltemplate").val(label);
        // $("#importbutton").attr("class","input-group-addon btn btn-primary");
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
    
    // --> reset the modal
    $("#newDashboard").on('hidden.bs.modal', function () {
        $( ".editDashboard" ).each(function(){
        	$(this).val("");
        })
        $("#dashboardActiveRow").hide();
    });
    
    // --> set the indicator dashboard
    $(".assignDashboard").on('change', function() {   	
    	var url = "/web/dashboard/admin/DashboardManager.do";
    	var data = new Object();
    	data.indicatorId = (this.id).split("_")[1];
    	data.method = (this.id).split("_")[0];
    	data.indicatorDashboardId = $(this).val();
    	sendData(url, data, null);
    });
    
    // --> set the indicator active status
    $(".toggleActive").on('change', function() {   	
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
    
    $(".exportTemplate").on('click', function(event) {
    	event.preventDefault();
    	var url = "/web/dashboard/admin/DashboardManager.do";
    	var data = new Object();
    	data.indicatorId = (this.id).split("_")[1];
    	data.method = (this.id).split("_")[0];  

    	sendData(url, data, null);
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
	    	} else {			
	    		$(data).replaceWith( $(target, data) );
	    	}

	    }
	});
}

// --> File upload triggers.
$(document).on('change', ':file', function() {
    var input = $(this),
    numFiles = input.get(0).files ? input.get(0).files.length : 1,
    label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
    input.trigger('fileselect', [numFiles, label]);
});

	




