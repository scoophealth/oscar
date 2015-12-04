<%--

    Copyright (c) 2014-2015. KAI Innovations Inc. All Rights Reserved.
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
<%@page contentType="text/javascript"%>
function makeIndependent(reportId) {
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=makeIndependent&reportId=" + reportId,
		success: function(data) {
			
		}
	});
}

function addDemoToHrm(reportId) {
	var demographicNo = $("demofind" + reportId + "hrm").value;
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=assignDemographic&reportId=" + reportId + "&demographicNo=" + demographicNo,
		success: function(data) {
			if (data != null)
				$("demostatus" + reportId).innerHTML = data;
		}
	});
}

function removeDemoFromHrm(reportId) {
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=removeDemographic&reportId=" + reportId,
		success: function(data) {
			if (data != null)
				$("demostatus" + reportId).innerHTML = data;
		}
	});
}

function addProvToHrm(reportId, providerNo) {
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=assignProvider&reportId=" + reportId + "&providerNo=" + providerNo,
		success: function(data) {
			if (data != null)
				$("provstatus" + reportId).innerHTML = data;
		}
	});
}

function removeProvFromHrm(mappingId, reportId) {
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=removeProvider&providerMappingId=" + mappingId,
		success: function(data) {
			if (data != null)
				$("provstatus" + reportId).innerHTML = data;
		}
	});
}

function makeActiveSubClass(reportId, subClassId) {
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=makeActiveSubClass&reportId=" + reportId + "&subClassId=" + subClassId,
		success: function(data) {
			if (data != null)
				$("subclassstatus" + reportId).innerHTML = data;
		}
	});
	
	window.location.reload();
}

function addComment(reportId) {
	var comment = jQuery("#commentField_" + reportId + "_hrm").val();
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=addComment&reportId=" + reportId + "&comment=" + comment,
		success: function(data) {
			if (data != null)
				$("commentstatus" + reportId).innerHTML = data;
		}
	});
}

function deleteComment(commentId, reportId) {
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=deleteComment&commentId=" + commentId,
		success: function(data) {
			if (data != null)
				$("commentstatus" + reportId).innerHTML = data;
		}
	});
}


function doSignOff(reportId, isSign) {
	var data;
	if (isSign)
		data = "method=signOff&signedOff=1&reportId=" + reportId;
	else
		data = "method=signOff&signedOff=0&reportId=" + reportId;
	
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: data,
		success: function(data) {
			if (window.location.href.indexOf("inboxManage")!= -1){
				var signoff = document.getElementById("signoff"+reportId);
				if (isSign) {
					signoff.value = "Revoke Sign-Off";
					signoff.setAttribute( "onclick", "javascript: revokeSignOffHrm('"+reportId+"');" );
				} else {
					signoff.value = "Sign-Off";
					signoff.setAttribute( "onclick", "javascript: signOffHrm('"+reportId+"');" );
                                }
			}
			else {
				window.location.reload();
			}
		}
	});
}

function signOffHrm(reportId) {
	
	doSignOff(reportId, true);	
}

function revokeSignOffHrm(reportId) {
	doSignOff(reportId, false);
}

function popupStart(vheight,vwidth,varpage,windowname) {
                //console.log("in popupstart 4 args");
                //console.log(vheight+"--"+ vwidth+"--"+ varpage+"--"+ windowname);
                if(!windowname)
                    windowname="helpwindow";
                //console.log(vheight+"--"+ vwidth+"--"+ varpage+"--"+ windowname);
                var page = varpage;
                windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
                var popup=window.open(varpage, windowname, windowprops);
            }