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
<%--
    KAI Innovations Inc.
    KAIInnovations.com
--%>
<%@ page errorPage="../error.jsp"%>
<!DOCTYPE html>
  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.oscar-emr.com/tags/integration" prefix="i"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.billing&type=_billing");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.math.BigInteger,java.util.*,org.oscarehr.integration.mcedt.web.DetailDataCustom,ca.ontario.health.edt.TypeListData, org.oscarehr.integration.mcedt.web.ActionUtils" %>


<%    

	List<DetailDataCustom> resourceListSent = (ArrayList<DetailDataCustom>)session.getAttribute("resourceListSent");
	//List<TypeListData> typeListData = (ArrayList<TypeListData>)session.getAttribute("typeListData");
	BigInteger resultSize = (BigInteger)session.getAttribute("resultSize");
	
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
        .web_dialog_overlay
        {
            position: fixed;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            height: 100%;
            width: 100%;
            margin: 0;
            padding: 0;
            background: #000000;
            opacity: .15;
            filter: alpha(opacity=15);
            -moz-opacity: .15;
            z-index: 101;
            display: none;
        }
        .web_dialog
        {
            display: none;
            position: fixed;
            width: 380px;
            height: 280px;
            top: 50%;
            left: 50%;
            margin-left: -190px;
            margin-top: -100px;
            background-color: #ffffff;
            border: 1px solid #191919;
			padding: 20px;
            z-index: 102;
        }
        .web_dialog_title
        {
            background-color: #6EC864;
            padding: 5px;
            color: #fff;
        }
        .web_dialog_title a
        {
            color: #fff;
            text-decoration: none;
        }
        .align_right
        {
            text-align: right;
        }
        .Level2HeadingStyle
        {
	        font-weight: 700;
	        font-size: 15pt;
        }
    </style>
<script src="../js/jquery-1.7.1.min.js"></script>

<script language="javascript">
	$(window).load(function(){
		$('input[type="checkbox"]').click(function () {
	   	 var pass = 5; //5 files at a time
	   	 var numOfFiles = $('input[type="checkbox"]:checked').length;
	   	 if (numOfFiles == pass) {
	        $('input[type="checkbox"]').not(':checked').prop('disabled', true);
	   	 } else {
	        $('input[type="checkbox"]').not(':checked').prop('disabled', false);
	   	 }
	   	
	   	 if (numOfFiles>0) {
	  	  	$("#unSelSent").prop('disabled', false);
	  	  	$("#deleteBtn").prop('disabled', false);
	    	} else {
	   	 	$("#unSelSent").prop('disabled', true);
	   	 	$("#deleteBtn").prop('disabled', true);
	  	  }
		})
		
		$("#unSelSent").click(function(){
			$('input[type="checkbox"]').filter(':checked').prop('checked', false);
			$('input[type="checkbox"]').filter(':disabled').prop('disabled', false);
			$("#unSelSent").prop('disabled', true);
			$("#deleteBtn").prop('disabled', true);
		})
	});

	window.onload = function() {
		
		return displayInfo();
	}
	
	function displayInfo()
	{
		var info = '<%= session.getAttribute("info") %>';
		if(info=='true'){
			ShowDialog(true);
		}		
		
	}

	function getInfo(resourceId, control) {
		if (control) {
			control.disabled = true;
		}
		var temp =jQuery("#serviceId").val();
		window.location.href = "resourceInfo.do?resourceId=" + resourceId +"&serviceId="+jQuery("#serviceId").val();		
		return false;
		
	}	
	
	function reSubmit(resourceId, control) {
		if (control) {
			control.disabled = true;
		}
		
		window.location.href = "reSubmit.do?resourceId=" + resourceId +"&serviceId="+jQuery("#serviceId").val();
		return false;		
		
	}
	
	function deleteFiles() {		
		
		if (!confirm("Please confirm that you would like to delete the selected files")) {
			return false;
		}							
		ShowSpin(true);
		var method = jQuery("#methodSent");
		method.val('deleteFiles');
		
		var form = jQuery("#formSent");
		form.submit();
		return true;
	}	
	
	function changeDisplay(control) {
		//submitFormSent("changeDisplay", control);
		var method = jQuery("#methodSent");
		method.val('changeDisplay');
		
		var form = jQuery("#formSent");
		form.submit();
		return true;
	}
	
	function submitFormSent(methodType, control){
		
		
		var method = jQuery("#methodSent");
		method.val(methodType);
		
		var form = jQuery("#formSent");
		form.submit();
		return true;
	}
	
	/* function setPageNumber(pageNo)
	{    
	    var element = document.getElementById('pageNo');
	    element.value = pageNo;
	} */
	
	/* for dialogbox */
$(document).ready(function ()
        {
            $("#btnShowSimple").click(function (e)
            {
                ShowDialog(false);
                e.preventDefault();
            });

            $("#btnShowModal").click(function (e)
            {
                ShowDialog(true);
                e.preventDefault();
            });

            $("#btnClose").click(function (e)
            {
                HideDialog();
                e.preventDefault();
            });

            $("#btnSubmit").click(function (e)
            {
            	
            	/* var brand = $("#brands input:radio:checked").val();
                $("#output").html("<b>Your favorite mobile brand: </b>" + brand); */
                HideDialog();
                e.preventDefault();
            });
        });

        function ShowDialog(modal)
        {
            $("#overlay").show();
            $("#dialog").fadeIn(300);

            if (modal)
            {
                $("#overlay").unbind("click");
            }
            else
            {
                $("#overlay").click(function (e)
                {
                    HideDialog();
                });
            }
        }

        function HideDialog()
        {
            $("#overlay").hide();
            $("#dialog").fadeOut(300);
            <% session.removeAttribute("info");%>
        }        
        
</script>



<title>Sent</title>
</head>
<body>
	<html:form action="/mcedt/resourceInfo" method="post" styleId="formSent">
		<jsp:include page="../messages.jsp" />		
		<input id="methodSent" name="method" type="hidden" value="" />	
	<div>		
	
	<div>
	
		Billing Number:
		<html:select property="serviceIdSent" styleId="serviceIdSent"
			styleClass="serviceIdSent">
			<c:forEach var="r" items="${serviceIds}">
            	<html:option value="${r}" >
					<c:out value="${r}" />
				</html:option>
							</c:forEach>
		</html:select>
		Resource Type:
		<html:select property="resourceType" styleId="resourceType"
			styleClass="input-xxlarge">
			<html:option value=""> - All - </html:option>
			<c:forEach var="r" items="${mcedtTypeList.data}">				
				<html:option value="${r.resourceType}">
					<c:out value="${r.resourceType}" /> -
						<c:out value="${r.access}" /> - 
						<c:out value="${r.descriptionEn}" />
				</html:option>
			</c:forEach>
		</html:select>
	
		Status:
		<html:select property="status" styleId="status" > <!-- onchange="setPageNumber(1)" -->
			<html:option value=""> - All - </html:option>
			<c:forEach var="i"
				items="${mcedtResourceForm.resourceStatusValues}">
				<html:option value="${i}" />
			</c:forEach>
		</html:select>
	
		Page #:
		<html:select  property="pageNo" styleId="pageNo">
			<c:forEach var="i" begin="1"
				end="${resultSize}">
				<html:option value="${i}">
					<c:out value="${i}" />
				</html:option>
			</c:forEach>
		</html:select>
		<button class="noBorder blackBox flatLink font12 small" onclick="ShowSpin(true); return changeDisplay();">Load Page</button>
	</div>
	* You may select a maximum of 5 files at a time to delete from MC-EDT
	<br />** Only files with upload status can be deleted or submitted from/to MC-EDT.
	<%
		if(resourceListSent!=null){									
	%>
	<table class="table scrollable whiteBox" width="100%" border="0" cellspacing="0" cellpadding="5" style="margin:5px 0 15px;">
				<thead>
					<tr class="greenBox">
						<th>Select</th>												
						<th>ID</th>
						<th>Date</th>
						<th>Type</th>
						<!-- <th>Result</th>
						<th>Status</th> -->
						<th>File Name</th>
						<th>Status</th>
						<th>Re Submit</th>
						<th>File Info</th>												
					</tr>
				</thead>
				<c:forEach var="r" items="${resourceListSent}" varStatus="loopStatus">						
					<tr bgcolor="${loopStatus.index % 2 == 0 ? '#FFF' : '#EEE'}">						
						<td>							
							<c:if test="${r.status == 'UPLOADED'}">								
								<input type="checkbox" value="${r.resourceID}" name="resourceId"/>								
							</c:if>							
						</td>																			
						<td><c:out value="${r.resourceID}" /></td>
						<td>														
							<fmt:formatDate value="${i:toDate(r.createTimestamp)}" pattern="MM/dd/yyyy hh:mm"/>
						</td>
						<td><c:out value="${r.resourceType}" /></td>						
						<td><c:out value="${r.description}" /></td>
						<td><c:out value="${r.status}" /></td>
						<td>
							<c:if test="${r.status == 'UPLOADED'}">														
								<button class="noBorder blackBox flatLink font12 small" onclick="ShowSpin(true);return reSubmit(${r.resourceID},this);">Submit</button>
							</c:if>
						</td>
						<td>
							<button class="noBorder blackBox flatLink font12 small" onclick="ShowSpin(true);return getInfo(${r.resourceID}, this)">Display	Info</button>
						</td>													
					</tr>						
				</c:forEach>
			</table>
			<%
				}
				else{
			%>		
				<h3>No new documents to download.</h3>
			<%
				}
			%>	
	</div>
	<div>
		<button type="button" id="unSelSent" class="noBorder blackBox flatLink font12 rightMargin5" disabled="true">Un-Select All</button>
		<button class="noBorder blackBox flatLink font12" id="deleteBtn" disabled="true" onclick="return deleteFiles()">Delete Selected</button>
	</div>
	</html:form>
	
	 <!-- -----------------------dialog box start---------------------------------------------- -->
	
		
	<%-- <input type="button" id="btnShowSimple" value="Simple Dialog" />
    <input type="button" id="btnShowModal" value="Modal Dialog" /> --%>
    
    <br /><br />       
    
    <div id="output"></div>
    
    <div id="overlay" class="web_dialog_overlay"></div>
    
    <div id="dialog" class="web_dialog">
        <table style="width: 100%; border: 0px;" cellpadding="5" cellspacing="0">
				
				<c:forEach var="d" items="${detail.data}">
					<tr>
						<td class="web_dialog_title">File Info</td>
                		<td class="web_dialog_title align_right">
                    		<a href="javascript:void(0);" id="btnClose">X</a>
                		</td>
					</tr>
					<tr>
						<td>ID</td>
						<td><c:out value="${d.resourceID}" /></td>
					</tr>
					<tr>
						<td>Created</td>
						<td><fmt:formatDate value="${i:toDate(d.createTimestamp)}" />
						</td>
					</tr>
					<tr>
						<td>Description</td>
						<td><c:out value="${d.description}" /></td>
					</tr>
					<tr>
						<td>Resource type</td>
						<td><c:out value="${d.resourceType}" /></td>
					</tr>
					<tr>
						<td>Modified</td>
						<td><fmt:formatDate value="${i:toDate(d.modifyTimestamp)}" />
						</td>
					</tr>
					<tr>
						<td>Result</td>
						<td><c:out value="${d.result.code}" /> - <c:out
								value="${d.result.msg}" /></td>
					</tr>
					<tr>
						<td>Status</td>
						<td><c:out value="${d.status}" /></td>
					</tr>
					<tr>
						<td></td>
						<td></td>
					</tr>
					<tr>
                		<td colspan="2" style="text-align: center;">
                    		<button id="btnSubmit" type="button" class="noBorder blackBox flatLink">Close</button>
                		</td>
            		</tr>
				</c:forEach>				
			</table>
		
    </div>
	
	
	<!-- ------------------------------------------------------------------------------- -->
</body>
</html>
