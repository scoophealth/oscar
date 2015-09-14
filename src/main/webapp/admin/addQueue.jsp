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

<!DOCTYPE HTML>

<%@page import="oscar.oscarProvider.data.*,java.util.*,org.oscarehr.util.SpringUtils,org.oscarehr.common.dao.QueueDao" %>

<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html lang="en">
    <head>
    <title>Add New Inbox Queue</title>
         
        <style type="text/css">
  		.input-queue{font-size:18px !important;}
  		.alert{display:none;}
  		
  		.alert em{font-size:18px;}
        </style>
    </head>

    <body>

<h3>Add New Inbox Queue</h3>

<div class="well">



<form class="form-inline" id="addQueueForm">
<input type="text" id="newQueueName" class="input-xlarge input-queue" placeholder="Type queue name"  value="" />
<input type="button" class="btn btn-primary" value="Add" id="add-btn" />

<i class="icon-question-sign" style="margin-left:20px;"></i> <oscar:help keywords="queue" key="app.top1"/>
</form>

</div>

    <div class="alert">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <div id="addQueueSuccessMsg">
		<strong>Warning!</strong> Best check yo self, you're not looking too good.
	</div>
    </div>
      
      
          <h4>Existing Queues:</h4>

					   <ol>
                       <%
                        QueueDao queueDao = (QueueDao) SpringUtils.getBean("queueDao");
                        List<Hashtable> queues=queueDao.getQueues();
                        for(Hashtable qht:queues){
                        %>                            
                                <li><%=(String)qht.get("queue")%></li>     
                        <%}%>
                        </ol>
 
</body>

<script type="text/javascript"> 

var pageTitle = $(document).attr('title');
$(document).attr('title', 'Administration Panel | Add New Inbox Queue');

$( document ).ready(function( $ ) {

	$("#add-btn").click(function(e) {
		e.preventDefault();
		
		var qn=$('#newQueueName').val();
        qn=qn.replace(/^\s+/g,"");
        qn=qn.replace(/\s+$/g,"");

		if(qn=="default"){
			 $('.alert').removeClass('alert-success');
			 $('.alert').addClass('alert-error');
		     $('.alert').show();

		     $('#addQueueSuccessMsg').html("<strong>Error!</strong> You can not overwrite the <em>default</em> queue.");
		}else{     
			
        if(qn.length>0){  		
            var data="method=addNewQueue&newQueueName="+qn;
            var url="${ctx}/dms/inboxManage.do";

        $.ajax({
            url: url,
            method: 'POST',
            data: data,
            dataType: "json",
            success: function(data){
                $('.alert').addClass('alert-success');
                $('.alert').show();
                
            	 $('#addQueueSuccessMsg').html("<strong>Success!</strong> Queue Name <em>"+qn+"</em> has been added.");
                 $('#newQueueName').val("");

                 var json = data.addNewQueue;
                 if(json!=null){
                     if(json==true){
                    	$('.alert').removeClass('alert-error');
                        $('.alert').addClass('alert-success');
                        $('.alert').show();
                         
                        $('#addQueueSuccessMsg').html("<strong>Success!</strong> Queue Name <em>"+qn+"</em> has been added.");
                        $('#newQueueName').val("");
                     }else{
                    	 $('.alert').removeClass('alert-success');
                         $('.alert').addClass('alert-error');
                         $('.alert').show();
                         
                         $('#addQueueSuccessMsg').html("<strong>Error!</strong> Queue Name <em>"+qn+"</em> has NOT been added which is probably because it already exists.");
                     }
                 }
               
            },
            error: function(data) {
            	 $('.alert').removeClass('alert-success');
            	 $('.alert').addClass('alert-error');
                 $('.alert').show();
                 
                 $('#addQueueSuccessMsg').html("<strong>Error!</strong> Queue Name <em>"+qn+"</em> has NOT been added, please contact support.");
             }

        
        });
        
        
		}else{
			$('.alert').removeClass('alert-success');
        	$('.alert').addClass('alert-error');
            $('.alert').show();
            
            $('#addQueueSuccessMsg').html("<strong>Error!</strong> Queue Name can not be empty.");
        }

		}//=default

	});

	
		
});   

registerFormSubmit('addQueueForm', 'dynamic-content');
</script>

</html>
