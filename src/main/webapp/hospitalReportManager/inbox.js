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

function popupPage(vheight,vwidth,varpage) { //open a new popup window
        var page = "" + varpage;
        windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
        var popup=window.open(page, "groupno", windowprops);
        if (popup != null) {
          if (popup.opener == null) {
            popup.opener = self;
          }
          popup.focus();
        }
}

function openPatient(demographicNo) {
	popupPage(700,1200,'../demographic/demographiccontrol.jsp?demographic_no='+demographicNo+'&displaymode=edit&dboperation=search_detail');
	
}
	
function openReport(id) {
	popupPage(700,1200,'Display.do?id='+id+'&duplicateLabIds=');
	
}
	
$(document).ready( function() {

	getHrmStatus();
	setInterval(function(){ 
		getHrmStatus();
		}, 5000);
		
	// table sorting
	$('#libraryTable').DataTable({
		serverSide : true,
		ajax : {
		 "url" : "../hospitalReportManager/hrm.do",
		 "data": function ( d ) {
                d.providerNo = $("#providerNo").val();
                d.providerUnmatched = $("#providerUnmatched").is(":checked");
                d.noSignOff = $("#noSignOff").is(":checked");
                d.demographicUnmatched = $("#demographicUnmatched").is(":checked");
            }
		},
		searching: false,
		"dom": '<"top"i>rt<"bottom"lp><"clear">',
		 "columns": [
			 	{render: function(data, type, full, meta) {
			 		var r = "<a href='javascript:void(0)' onClick=\"openReport("+full.id+");return false;\" ><img src='../images/details2.gif' border='0'/></a>";
			 		return r;
			 		} 
			 	},
			 	{ "data": "recipient_name" , render: function(data,type,full,meta){
			 		if(full.provider_no != null && full.provider_no.length>0) {
			 			return "<a href='javascript:void(0)' title='This recipient has been linked to a provider record'>"+full.recipient_name+"</a>";
			 		} else {
			 			return full.recipient_name;
			 		}
			 		
			 	}},
			 	{ "data": "patient_name", render: function(data, type, full, meta) {
			 		var r = data;
			 		if(full.demographic_no != null && full.demographic_no.length>0) {
			 			r = '<a href="javascript:void(0);" onClick="openPatient('+full.demographic_no+');return false;">'+data+'</a>';
			 		}
	                return r;
	            }},
	            { "data": "patient_dob" , "visible" : false},
	            { "data": "patient_hcn", "visible" : false },
	            { "data": "patient_gender", "visible" : false },
	            { "data": "report_date" },
	            { "data": "received_date" },
	            { "data": "sending_facility" },
	            { "data": "class_subclass" },
	            { "data": "category" , "visible" : false},
	            { "data":  "description"}
	        ]

	});
	
	$("#uploadHRM").on('click',function(){
		$("#uploadHRMDialog").modal();
	});
	
	$("#providerUnmatched").on('change',function(){
		updateProviderFilter();
	});
	$("#providerNo").on('change',function(){
		updateProviderFilter();
	});
	$("#noSignOff").on('change',function(){
		reloadTable();
	});
	$("#demographicUnmatched").on('change',function(){
		reloadTable();
	});
 
	$("#showAddlPatientInfo").on('change',function(){
		$("#libraryTable").DataTable().column(3).visible($(this).is(":checked"));	
		$("#libraryTable").DataTable().column(4).visible($(this).is(":checked"));	
		$("#libraryTable").DataTable().column(5).visible($(this).is(":checked"));	
	});
	
	$("#showCategoryInfo").on('change',function(){
		$("#libraryTable").DataTable().column(10).visible($(this).is(":checked"));	
	});
	
	$('#hrm_file').fileupload({
        dataType: 'json',
        done: function (e, data) {
        	$("#uploadHRMDialog").modal('hide');
        	$("#libraryTable").DataTable().ajax.reload();
        }, fail: function(e,data) {
        	alert('Error uploading file. See log for more information');
        }
    });
   
});

function updateProviderFilter() {
	var providerNoVal = $("#providerNo").val();
	var providerUnmatchedVal = $("#providerUnmatched").is(':checked');
	
	if(providerUnmatchedVal) {
		$("#providerNo").prop('disabled',true);
		$("#noSignOff").prop('checked',false);
		$("#noSignOff").prop('disabled',true);
	} else {
		$("#providerNo").prop('disabled',false);
		$("#noSignOff").prop('disabled',false);
		$("#noSignOff").prop('checked',true);
	}
	
	$("#libraryTable").DataTable().ajax.reload();
	
	//alert(providerNoVal + "\n" + providerUnmatchedVal);
}

function reloadTable() {
	$("#libraryTable").DataTable().ajax.reload();
}

function fetchNewData() {
	$.ajax({
				type:"GET",
				url:'../hospitalReportManager/hrm.do?method=fetch',
				dataType:'json',
				async:true, 
				success:function(data) {
					if(data && data.error) {
						alert('An error occured. Please check the HRM log for more information\n' + data.error);
					}
					$("#libraryTable").DataTable().ajax.reload();
				}
	});

}

function getHrmStatus() {
	$.ajax({
				type:"GET",
				url:'../hospitalReportManager/hrm.do?method=getHrmStatus',
				dataType:'json',
				async:true, 
				success:function(data) {
					if(data.running != null && data.running == true) {
						$("#hrm_status").html("Fetching data from HRM"); 
					} else {
						$("#hrm_status").html("Idle"); 
					}
				
				}
	});

}



