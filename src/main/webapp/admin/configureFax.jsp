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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.common.dao.FaxConfigDao"%>
<%@page import="org.oscarehr.common.model.FaxConfig"%>
<%@page import="org.oscarehr.common.dao.QueueDao"%>
<%@page import="org.oscarehr.common.model.Queue" %>
<%@page import="org.oscarehr.util.SpringUtils"%>

<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>

<!DOCTYPE html>
<html>
<head>
<title>Manage Fax</title>

<meta name="viewport" content="width=device-width,initial-scale=1.0">
                              
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/bootstrap.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" type="text/css">

<style type="text/css">

.center { float: none; margin-left: auto; margin-right: auto; }

</style>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.js"></script>

<script type="text/javascript">
	
	$(document).keypress(function() {
		$("#submit").prop("disabled", false);
		$(this).off();
	});
			
	
	$(document).ready(function() {
		$("#faxUrl").focus();				
		
		$("select").change(function() {
			$("#submit").prop("disabled", false);
			$(this).off();
		});
				
		$("#submit").click(function(e) {
			e.preventDefault();
			
			if( verify() ) {
				var url = "<%=request.getContextPath() %>/admin/ManageFax.do";
				var data = $("#configFrm").serialize();
				
				$.ajax({
					url: url,
					method: 'POST',
					data: data,
					dataType: "json",
					success: function(data){
						
						if( data.success ) {
							$("#msg").html("Configuration saved!");
							$('.alert').removeClass('alert-error');
							$('.alert').addClass('alert-success');
							$('.alert').show();
						}
						else {
							$("#msg").html("There was a problem saving your configuration.  Check the logs for further details.");
							$('.alert').removeClass('alert-success');
							$('.alert').adqdClass('alert-error');
							$('.alert').show(); 
						}
					}});
				
			} 
			else {
				alert("The configuration form is incomplete");
			}//end if
		});
		
		if( $("#faxUrl").val().substr(0,2) == "ip" ) {
			$("#faxUrl").keypress(function() {
				$(this).val("");
				$(this).off();
				$(this).dblclick(function() {
					$(this).val("");
				});
			});
		}
		else {
			$("#faxUrl").dblclick(function() {
				$(this).val("");
			});
		}
		
		
		if( $("#faxServiceUser").val() == "Fax Service login" ) {
			$("#faxServiceUser").keypress(function() {
				$(this).val("");
				$(this).off();
				$(this).dblclick(function() {
					$(this).val("");
				});
			});
		}
		else {
			$("#faxServiceUser").dblclick(function() {
				$(this).val("");
			});
		}
				
		if( $("#faxServicePasswd").val() == "**********" ) {
			$("#faxServicePasswd").keypress(function() {
				$(this).val("");
				$(this).off();
				$(this).dblclick(function() {
					$(this).val("");
				});
			});
		}
		else {
			$("#faxServicePasswd").dblclick(function() {
				$(this).val("");
			});
		}
		
		if( $("#faxUser").val() == "user login" ) {
			$("#faxUser").keypress(function() {
				$(this).val("");
				$(this).off();
				$(this).dblclick(function() {
					$(this).val("");
				});
			});
		}
		
		
		if( $("#faxPasswd").val() == "**********" ) {
			$("#faxPasswd").keypress(function() {
				$(this).val("");
				$(this).off();
				$(this).dblclick(function() {
					$(this).val("");
				});
			});
		}
		
		if( $("#faxNumber").val() == "Clinic Fax Number" ) {
			$("#faxNumber").keypress(function() {
				$(this).val("");
				$(this).off();
				$(this).dblclick(function() {
					$(this).val("");
				});
			});
			
			$("#faxNumber").blur(function() {
				if( !$(this).val().match("^\\d{10}$")) {
					alert("please enter fax number in form 1234567890");
					var input = $(this);
					setTimeout(function() {input.focus();},10);
				}
			});
		}


		$("input[type='text']").filter(function() {
			return this.id.match("^faxUser\d+")
		}).each(function() {			
			$(this).dblclick(function() {
				$(this).val("");
			});
		});
			
		$("input[type='password']").filter(function() {
			return this.id.match("^faxPasswd\\d+")
		}).each(function() {			
			$(this).dblclick(function() {
				$(this).val("");
			});
						
		});
		
		
		$("input[type='text']").filter(function() {
			return this.id.match("^faxNumber\\d+")
		}).each(function() {				
			$(this).dblclick(function() {
				$(this).val("");
			});
			
			$(this).blur(function() {
				if( !$(this).val().match("^\\d{10}")) {
					alert("please enter fax number in form 1234567890");
					var input = $(this);
					setTimeout(function() {input.focus();},10);
				}
			});
						
		});
				
		$("input[type='radio']").click(function() {
			$("#submit").prop("disabled", false);
			setState(this);
		});
		
		
	});

	<%

	FaxConfigDao faxConfigDao = SpringUtils.getBean(FaxConfigDao.class);
	List<FaxConfig>faxConfigList = faxConfigDao.findAll(null, null);
	Integer count = 0;

	QueueDao queueDao = SpringUtils.getBean(QueueDao.class);
	HashMap<Integer,String>queueMap = queueDao.getHashMapOfQueues();

	%>

	
	var userCount = <%=faxConfigList.isEmpty() ? "0" : faxConfigList.size()%>;
	function addUser() {
		++userCount;
		
		var userDivId = "user" + userCount;
		var div = $("#user").clone(true,true);
		
		$(div).attr("id",userDivId);
		$(div).find("#faxUser").attr("id","faxUser" + userCount);
		$(div).find("#faxPasswd").attr("id","faxPasswd" + userCount);
		$(div).find("#faxPasswd"+userCount).val("");
		
		$(div).find("#remove").attr("id","r"+userCount);
		$(div).find("#r"+userCount).attr("onclick","removeUser("+userCount+");return false;");
		$(div).find('input[type="text"]').val("");
		$(div).find('input[type="radio"]').prop('checked', false);
		$(div).find("#on").attr("name","active" + userCount);
		$(div).find("#of").attr("name","active" + userCount);
		$(div).find("#on").attr("id","on" + userCount);
		$(div).find("#of").attr("id","of" + userCount);
		$(div).find("#activeState").val("");
		$(div).find("#activeState").attr("id","activeState"+userCount);
		$(div).find("#id").val("-1");
		$(div).find("#id").attr("id","id"+userCount);
		
	  	
		$(div).find("#id").val("-1");
		$(div).find("select").val("-1");
		
		$(div).appendTo("#content");
		$("#faxUser"+userCount).focus();	
		$("#submit").prop("disabled", false);
	}
	
	function removeUser(divCount) {
		var divId;
		
		$("#submit").prop("disabled", false);
		
		if( divCount > 0 ) {
			divId = "user" + divCount;
			$("#"+divId).remove();
		}
		else {
			divId = "user";
			$('#'+divId + ' input[type="text"]').val("");			
			$('#'+divId + ' input[type="radio"]').attr("checked",false);
			$('#'+divId + ' input[type="hidden"]').val("");
			$('#'+divId + ' select').val("-1");				
		}
	}
	
	function verify() {
		var names = ["faxUrl","siteUser","sitePasswd","faxUser","faxPassword","faxNumber","activeState","inboxQueue"];
		var valid = true;
		var incomplete = new Object();
		
		for( var idx = 0; idx < names.length; ++idx ) {
			try {
			 	$('[name="'+names[idx] + '"]').each(function(index) {	
			 		if( index == 0 ) {
			 			return;
			 		}			 					 	
			 		
			 		if( $(this).val() == "" || $(this).val() == "-1" || $(this).val() == "ip addr of fax service" || $(this).val() == "Fax Service login" ||
			 				$(this).val() == "Fax Service Passwd" || $(this).val() == "user login" || $(this).val() == "login passwd" || $(this).val() == "Clinic Fax Number" ) {			 			
			 			throw incomplete;
			 		}
			 	});
			 	}
				catch( incomplete ) {
					valid = false;
					break;
				}
			
		}
		
		return valid;
	}
	
	function setState(elem) {
		var id = "#activeState" + elem.id.substring(2);
		$(id).val($(elem).val());
	}
	

</script>
</head>


<body>
	<div class="container-fluid">
		<form id="configFrm" method="post" onSubmit="return verify()"> 
		<input type="hidden" name="method" value="configure"/> 
		<div id="bodyrow" class="row">
			<legend>Fax Server Credentials</legend>
			<div class="span12">
			
			<div class="row">			
				<div class="span12">
					<label for="faxUrl" > Fax Server URL</label>
					<input class="span12" id="faxUrl" type="text" name="faxUrl" placeholder="fax web service URL" value="<%=faxConfigList.get(0).getUrl()%>" />				
				</div>			
			</div>
			<div class="row">
				<div class="span6">
					<label for="faxServiceUser">Fax Server Username</label>
					<input class="span6" id="faxServiceUser" type="text" name="siteUser" value="<%=faxConfigList.isEmpty() || faxConfigList.get(0).getSiteUser() == null ? "Fax Service login" : faxConfigList.get(0).getSiteUser() %>" />				
				</div>			
	
				<div class="span6">
					<%
						String faxServicePassword = "";
						
						if(faxConfigList != null && faxConfigList.get(count) != null && faxConfigList.get(count).getPasswd() != null
								&& faxConfigList.get(count).getPasswd().length() > 0) {
							faxServicePassword="**********";
						}
						
					%>
					<label for="faxServicePasswd">Fax Server Password</label>
					<input class="span6" id="faxServicePasswd" type="password" name="sitePasswd" value="<%=faxServicePassword%>" />
				</div>
			</div>
			<div class="span3">
			</div>
		</div>
		<div id="content" class="row">
			<legend>Fax Gateway Accounts <a class="pull-right" style="margin-right:40px;" href="" onclick="addUser();return false;">+Add</a></legend>
			
			<div class="span12">
		
				<% do { %>
				<div class="row" id="user<%=count == 0 ? "" : count%>">
				<div class="span12">
				
				<div class="row">
					<div class="span6">
						<label for="faxUser<%=count == 0 ? "" : count%>">User</label>
							<input class="span6" type="text" id="faxUser<%=count == 0 ? "" : count%>" name="faxUser" value="<%=faxConfigList.isEmpty() ? "user login" : faxConfigList.get(count).getFaxUser()%>"/>
							<input type="hidden" id="id<%=count == 0 ? "" : count%>" name="id" value="<%=faxConfigList.isEmpty() ? "-1" : faxConfigList.get(count).getId()%>"/>
						
					</div>

					<div class="span6">
						<label for="faxPasswd<%=count == 0 ? "" : count%>" >Password</label>
						<%
						String faxPassword = "";
						
						if(faxConfigList != null && faxConfigList.get(count) != null && faxConfigList.get(count).getFaxPasswd() != null
								&& faxConfigList.get(count).getFaxPasswd().length() > 0) {
							faxPassword="**********";
						}
						
						%>
						<input class="span6" type="password" id="faxPasswd<%=count == 0 ? "" : count%>" name="faxPassword" value="<%=faxPassword%>"/>
						
					</div>
				</div>
				<div class="row">
					<div class="span6">
						<label for="faxNumber<%=count == 0 ? "" : count%>" >Fax Number</label>
						<input class="span6" type="text" id="faxNumber<%=count == 0 ? "" : count%>" name="faxNumber" value="<%=faxConfigList.isEmpty() ? "Clinic Fax Number" : faxConfigList.get(count).getFaxNumber()%>"/>
					</div>	
					
					<div class="span6">
					<label for="senderEmail<%=count == 0 ? "" : count%>">Email</label>
					<input class="span6" type="email" id="senderEmail<%=count == 0 ? "" : count%>" name="senderEmail" placeholder="Account email" value="<%=faxConfigList.get(count).getSenderEmail()%>" />
				</div>
				</div>
				<div class="row">
					<div class="span6">
						<label for="inBoxQueue<%=count == 0 ? "" : count%>">Inbox Queue</label>
							<select class="span6" id="inBoxQueue<%=count == 0 ? "" : count%>" name="inboxQueue">
								<option value="-1">-</option>						
								<%
									for( Integer queueId : queueMap.keySet() ) {
							 	
							 			out.print("<option value='" + queueId+"'");
							 			
							 			if( !faxConfigList.isEmpty() ) {
																		    							
											if( faxConfigList.get(count).getQueue().compareTo(queueId) == 0 ) {						
												out.print(" selected");											
											}
									    }
									    
									    out.print(">" + queueMap.get(queueId) + "</option>");
									}
								%>
							</select>
						
					</div>
					<div class="span6">
						<label>Enable/Disable Gateway</label>
						
							<label class="radio inline control-label">
							<input type="radio" id="on<%=count == 0 ? "" : count %>" name="active<%=count == 0 ? "" : count%>" value="true" <%=faxConfigList.isEmpty() ? "" : faxConfigList.get(count).isActive() ? "checked" : ""%>  />
							On</label>
							<label class="radio inline control-label">
							<input type="radio" id="of<%=count == 0 ? "" : count %>" name="active<%=count == 0 ? "" : count%>" value="false" <%=faxConfigList.isEmpty() ? "" : faxConfigList.get(count).isActive()  ? "" : "checked"%> />
							Off</label>
							
							<input type="hidden" id="activeState<%=count == 0 ? "" : count%>" name="activeState" value="<%=faxConfigList.isEmpty() ? "" : faxConfigList.get(count).isActive()%>" />			
					</div>
				</div>

						<% if( count <= faxConfigList.size() ) { %>
						<div class="row">
							<div class="span12">
								<a class="pull-right" style="color:red;" id="remove" href="" onclick="removeUser(<%=count%>);return false;">-Delete</a>
							</div>
						</div>
					    <%} %>					
				</div> <!--  end master column -->
				</div>	<!-- end account row -->		
					<%
						++count;
					} while(count < faxConfigList.size());
					%>
				
			</div> <!-- end master column -->
		</div> <!-- end content -->
				
				<div class="row">
					<input class="btn btn-default" id="submit" type="submit" disabled value="Save" />
				</div>
			</form>
			
		<div id="msg" class="row alert">
   		</div>								
</div>	<!-- end container -->	
		

	
</body>
</html>
