<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>
<%

    DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");


%>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script type="text/javascript" src="/oscar/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="/oscar/js/jquery-ui-1.10.2.custom.min.js"></script>
    <script type="text/javascript" src="/oscar/share/javascript/Oscar.js"></script>

    <link rel="stylesheet" href="/oscar/share/css/bootstrap.min.css">

    <script src="/oscar/share/javascript/bootstrap.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/oscar/share/yui/css/fonts-min.css">
    <link rel="stylesheet" type="text/css" href="/oscar/share/yui/css/autocomplete.css">
    <title>CDX Messenger v0.1</title>
    <style>
        #success_message{ display: none;}

        .button-group{
            width: 60%;
            margin: 40px auto;
        }
        #showList{
            max-height:200px;
            overflow-y:scroll;
            width: 100%;
        }
        .ovalbutton{
            float:right;
            border-radius: 22px;
            background-color: #E8E8E8;
        }
        #showrecipient{
            max-height:200px;
            overflow-y:scroll;
            width: 100%;
        }

        #primary, #secondary{
            display: none;
            max-height: 200px;
            overflow-y: scroll;
            overflow-x: scroll;
            white-space: nowrap;
            background-color: white;
            width: 100%;
        }

ul#primary li, ul#secondary li{
    width: max-content;
    padding: 10px 15px;
}


    </style>
    <script type="text/javascript">
        $(document).ready(function(){

            //searching patient from a demographic table.

            $('#patient').keyup(function(){
                var search=$('#patient').val();
                if(search !=='' && search !==null)
                {
                    $.ajax({
                        type:'POST',
                        url:'messengerController.jsp',
                        data:'patient='+search,
                        success:function(data)
                        {
                            console.log(data);
                            $('#showList').html(data);
                            $('#showList').show();

                        }
                    });
                }
                else
                {
                    $('#showList').html('');
                }
            });
          $(document).on('click','#showList li',function(){
                $('#patient').val($(this).text());
                $('#showList').hide();

            });

          // search cdx enabled specialist

            $('#recipient').keyup(function(){
                var searchRecipient=$('#recipient').val();
                if(searchRecipient !=='' && searchRecipient !==null && searchRecipient.length>=4 )
                {
                    $.ajax({
                        type:'POST',
                        url:'messengerController.jsp',
                        data:'recipient='+searchRecipient,
                        success:function(data)
                        {
                            $('#clinics').html('');
                            $('#butns').hide();
                            $('#showrecipient').html(data);

                            $('#showrecipient').show();

                            console.log(data);

                        }
                    });
                }
                else
                {
                    $('#showrecipient').html('');
                }
            });

            //Getting clinics info for a specialist
            $(document).on('click','#showrecipient li',function(){
                 $('#recipient').val($(this).text());
                var names= $('#recipient').val();
                $('#showrecipient').hide();

                if(names !=='' && names !==null )
                {
                    $.ajax({
                        type:'POST',
                        url:'messengerController.jsp',
                        data:'names='+names,
                        success:function(data)
                        {

                            $('#clinics').html(data);
                            $('#butns').show();
                            console.log(data);

                        }
                    });
                }
                else
                {
                    $('#clinics').html('');
                }

            });


            // for selecting all clinics at once

            $('body').on('click', '#selectAll', function () {
                if ($(this).hasClass('allChecked')) {
                    $('input[type="checkbox"]', '#clinics').prop('checked', false);
                } else {
                    $('input[type="checkbox"]', '#clinics').prop('checked', true);
                }
                $(this).toggleClass('allChecked');
            })


        });

        function addprimary(){
             var recipient=$('#recipient').val();
             var items=document.getElementsByName('seletectedclinics');
             var selectedItems=recipient+"@";
             for(var i=0; i<items.length; i++){
                if(items[i].type=='checkbox' && items[i].checked==true)
                    selectedItems+=items[i].value+", ";
             }
             var allclinics= selectedItems.substring(0, selectedItems.length - 1);

            $('#primary').append("<li> <a href='javascript:void(0);' class='remove'><b>&times;</b></a><input type='hidden' name='precipients' value='"+allclinics+"'>"+ allclinics+"</li>");
            $(document).on("click", "a.remove" , function() {
                $(this).parent().remove();
                if ( $('#primary').children().length<1 ) {
                    $('#primary').hide();
                    $('#hiddeninput').show();
                }

            });
            $('#addClient_Modal').modal('hide');
            $('#clinics').html('');
            $('#recipient').val('');
            $('#hiddeninput').hide();
            $('#primary').show();
            $('#butns').hide();
        }

        function addsecondary(){
            var recipient=$('#recipient').val();
            var items=document.getElementsByName('seletectedclinics');
            var selectedItems=recipient+"@";
            for(var i=0; i<items.length; i++){
                if(items[i].type=='checkbox' && items[i].checked==true)
                    selectedItems+=items[i].value+", ";
            }
            var allclinics= selectedItems.substring(0, selectedItems.length - 1);

            $('#secondary').append("<li> <a href='javascript:void(0);' class='remove'><b>&times;</b></a><input type='hidden' name='srecipients' value='"+allclinics+"'>"+ allclinics+"</li>");
            //select.options[select.options.length] = new Option(allclinics, allclinics);
            $(document).on("click", "a.remove" , function() {
                $(this).parent().remove();
                if ( $('#secondary').children().length<1 ) {
                    $('#secondary').hide();
                    $('#hiddensecondary').show();
                }

            });
            $('#addClient_Modal').modal('hide');
            $('#clinics').html('');
            $('#recipient').val('');
            $('#hiddensecondary').hide();
            $('#secondary').show();
            $('#butns').hide();
        }


    </script>

</head>

<body>
<div class="container">

    <form class="well form-horizontal" action="<%=request.getContextPath()%>/cdx/CDXMessenger.do" method="post"  id="contact_form">
        <fieldset>

            <!-- Form Name -->


                <div>  <legend><center><h2> <b>CDX Messenger v0.1</b>
                    <span>
                        <a href="../cdx/cdxMessengerHistory.jsp" target="_blank" class="btn ovalbutton" role="button">History</a>
                    </span>
                </h2></center></legend><br>

                </div>


            <!-- Text input-->

            <div class="form-group">
                <label class="col-xs-4 control-label">Patient</label>
                <div class="col-xs-4 inputGroupContainer">
                    <div class="">
                        <input  name="patientsearch" id="patient" placeholder="Search" class="form-control"  type="text" required>
                           <div id="showList" >
                               <ul class="list-group" >
                               </ul>
                           </div>
                       </div>
                   </div>
               </div>

               <!-- Text input-->

            <div class="form-group">
                <label class="col-xs-4 control-label" >Primary Recipient(s)</label>
                <div class="col-xs-4 inputGroupContainer">
                    <div class="">
                        <input id="hiddeninput" name="" placeholder="Add primary recipients" class="form-control"  type="text" readonly >
                        <ul class="list-group" id="primary" name="primaryrecipient">

                        </ul>

                    </div>
                </div>
                <div class="add-button col-xs-4" > <button type="button" data-toggle="modal" data-target="#addClient_Modal" class="btn btn-primary">Add</button></div>
            </div>

            <div class="form-group">
                <label class="col-xs-4 control-label" >Secondary Recipient(s)</label>
                <div class="col-xs-4 inputGroupContainer">
                    <div class="">
                        <input id="hiddensecondary" name="" placeholder="Add secondary recipients" class="form-control"  type="text" readonly >
                        <ul class="list-group" id="secondary" name="secondaryrecipient">
                        </ul>

                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-xs-4 control-label">Message Type</label>
                <div class="col-xs-4 inputGroupContainer">
                    <div class="">
                        <input  name="messagetype" value="New" class="form-control"  type="text" readonly style="color:gray;" >
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-xs-4 control-label">Document type</label>
                <div class="col-xs-4 selectContainer">
                    <div class="">
                        <select name="documenttype" class="form-control selectpicker" required>
                            <option value="">Select document type</option>
                            <option>Information Request</option>
                            <option>Clinical Summaries</option>
                            <option>Progress Updates</option>

                        </select>
                    </div>
                </div>
            </div>

            <!-- Text input-->

            <div class="form-group">
                <label class="col-xs-4 control-label">Content</label>
                <div class="col-xs-4 inputGroupContainer">
                    <div class="">
                        <textarea  name="contentmessage" placeholder="Content" class="form-control"  type="text" required></textarea>
                    </div>
                </div>
            </div>

            <!-- Text input-->

            <div class="form-group">
                <label class="col-xs-4 control-label" >Attachment</label>
                <div class="col-xs-4 inputGroupContainer">
                    <div class="custom-file mb-3">
                        <input type="file" class="custom-file-input" id="customFile" name="filename">
                    </div>
                </div>
            </div>



            <!-- Select Basic -->

            <!-- Success message -->

            <!-- Button -->
            <div class=" button-group">
                <div class="col-xs-4">
                    <button type="submit" class="btn btn-success">Send</button>
                </div>
                <div class="col-xs-4 ">
                    <button type="button" class="btn btn-warning">Save Draft</button>
                </div>
                <div class="col-xs-4">
                    <button type="button" class="btn btn-default">Cancel</button>
                </div>
            </div>

            <div class="alert alert-success" role="alert" id="success_message">Success <i class="glyphicon glyphicon-thumbs-up"></i> Success: Message sent!.</div>

            <!-- Modal -->
            <div class="modal fade" id="addClient_Modal" role="dialog">
                <div class="modal-dialog">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Search Recepient(s)</h4>
                        </div>
                        <div class="modal-body">

                                <form class="well form-horizontal" action=" " method="post"  id="myform">
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Recipient</label>

                                        <div class="col-xs-4 inputGroupContainer">
                                            <div class="">
                                                <input  name="recipients" id="recipient" placeholder="Search" class="form-control"  type="text">

                                                <div id="showrecipient" >
                                                    <ul class="list-group" id="searchrecipients">
                                                    </ul>
                                                </div>
                                            </div>

                                        </div>
                                    </div>

                                            <div class="" id="clinics">
                                            </div>

                                    <div style="text-align: center; display: none;" id="butns" >
                                        <input type="button" class="btn btn-success" value="Add as primary" onclick='addprimary()'>
                                        <input type="button" class="btn btn-warning" value="Add as secondary" onclick='addsecondary()'>
                                        <input type="button" class="btn btn-info" value="Select all" id="selectAll" >
                                    </div>

                                </form>


                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>

                </div>
            </div>
            <!-- Modal ends-->

        </fieldset>
    </form>
</div>
</div><!-- /.container -->

</body>

</html>
