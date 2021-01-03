<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="oscar.OscarProperties" %>
<%@page import="org.oscarehr.util.WebUtilsOld"%>
<%@ page import="org.oscarehr.integration.cdx.model.CdxProvenance" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxMessenger" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxMessengerDao" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxProvenanceDao" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- add for special encounter -->
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special" %>
<!-- end -->
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

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
    CdxMessengerDao cdxMessengerDao = SpringUtils.getBean(CdxMessengerDao.class);
    CdxProvenanceDao provenanceDao = SpringUtils.getBean(CdxProvenanceDao.class);
    String demoName="";
    int docId=0;
    String docKind="";
     demoName= request.getParameter("demoName");
    CdxProvenance doc= (CdxProvenance)session.getAttribute("document");
    if(doc!=null){
        docId=doc.getId();
        docKind=doc.getKind();
    }
       String draftId=request.getParameter("Id");
       String patient="";
       String primary="";
       String secondary="";
       String msgType="";
       String documentType="";
       String content="";
      if(draftId!=null && !draftId.equalsIgnoreCase("")){
          CdxMessenger cdxMessenger=cdxMessengerDao.getCdxMessenger(Integer.parseInt(draftId));
          patient=cdxMessenger.getPatient();
          primary=cdxMessenger.getPrimaryRecipient();
          secondary=cdxMessenger.getSecondaryRecipient();
          msgType=cdxMessenger.getCategory();

          //Get document details to show in category when populating from draft
          if(!cdxMessenger.getCategory().equalsIgnoreCase("New")) {
              CdxProvenance cdxProvenance = provenanceDao.getCdxProvenance(Integer.parseInt(cdxMessenger.getCategory().split(":")[1]));
              docKind = cdxProvenance.getKind();
              docId = cdxProvenance.getId();
          }

          documentType=cdxMessenger.getDocumentType();
          content=cdxMessenger.getContent();
      }


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
    <title>CDX Composer</title>
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
            display: none;
        }
        .ovalbutton{
            float:right;
            border-radius: 22px;
            background-color: #E8E8E8;
            width: 120px;
        }
        #showrecipient{
            max-height:200px;
            overflow-y:scroll;
            width: 100%;
            display: none;
        }

        #primary, #secondary{
            display: none;
            max-height: 200px;
            overflow-y: scroll;
            overflow-x: scroll;
            white-space: nowrap;
            background-color: white;
            width: 100%;
            color: #555;
            font-size: 14px;
            line-height: 1.42857143;
        }

ul#primary li, ul#secondary li{
    width: max-content;
    padding: 10px 15px;
}
        #otherinfo{
            max-height: 100px;
            overflow-y: scroll;
            background-color: white;
            border: 1px solid #ccc;
            min-height: 70px;
            border-radius: 6px;
            padding: 10px;
            color: #555;
            font-size: 14px;
            line-height: 1.42857143;

        }
        #infobuttons button{
            position: relative;
            margin: 4px;
        }

    </style>
    <script type="text/javascript">
        $(document).ready(function(){

            init();
            $("form :input").attr("autocomplete", "off");
            //searching patient from a demographic table.

            $('#FamilyHistory').hide();
            $('#MedicalHistory').hide();
            $('#ongoingConcerns').hide();
            $('#SocialHistory').hide();
            $('#OtherMeds').hide();
            $('#Reminders').hide();
            $('#fetchRiskFactors').hide();
            $('#fetchMedications').hide();
            $('#fetchLongTermMedications').hide();
            $('#fetchAllergies').hide();

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
                getDemographicId();
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

        $(document).on("click", "a.remove" , function() {
            $(this).parent().remove();
            if ( $('#primary').children().length<1 ) {
                $('#primary').hide();
                $('#hiddeninput').show();
            }

        });

        function addprimary(){
             var recipient=$('#recipient').val();
             var items=document.getElementsByName('seletectedclinics');
             checked = $("input[type=checkbox]:checked").length;

            if(!checked) {
                alert("You must select at least one clinic.");
                return false;
            }
             var selectedItems=recipient+"@";
             for(var i=0; i<items.length; i++){
                if(items[i].type=='checkbox' && items[i].checked==true)
                    selectedItems+=items[i].value+", ";
             }
             var allclinics= selectedItems.substring(0, selectedItems.length - 2);

            $('#primary').append("<li> <a href='javascript:void(0);' class='remove'><b>&times;</b></a><input type='hidden' name='precipients' value='"+allclinics+"'>"+ allclinics+"</li>");


            $('#addClient_Modal').modal('hide');
            $('#clinics').html('');
            $('#recipient').val('');
            $('#hiddeninput').hide();
            $('#primary').show();
            $('#butns').hide();
        }

        $(document).on("click", "a.removes" , function() {
            $(this).parent().remove();

            if ( $('#secondary').children().length<1 ) {
                $('#secondary').hide();
                $('#hiddensecondary').show();
            }

        });

        function addsecondary(){
            var recipient=$('#recipient').val();
            var items=document.getElementsByName('seletectedclinics');
            checked = $("input[type=checkbox]:checked").length;

            if(!checked) {
                alert("You must select at least one clinic.");
                return false;
            }

            var selectedItems=recipient+"@";
            for(var i=0; i<items.length; i++){
                if(items[i].type=='checkbox' && items[i].checked==true)
                    selectedItems+=items[i].value+", ";
            }
            var allclinics= selectedItems.substring(0, selectedItems.length - 2);

            $('#secondary').append("<li> <a href='javascript:void(0);' class='removes'><b>&times;</b></a><input type='hidden' name='srecipients' value='"+allclinics+"'>"+ allclinics+"</li>");
            //select.options[select.options.length] = new Option(allclinics, allclinics);

            $('#addClient_Modal').modal('hide');
            $('#clinics').html('');
            $('#recipient').val('');
            $('#hiddensecondary').hide();
            $('#secondary').show();
            $('#butns').hide();
        }

        var demographicid=null;
        function updateAttached() {
            var t = setTimeout('fetchAttached(demographicid)', 2000);
        }

        function fetchAttached(d) {


            var params = "demo="+d;

            $.ajax({
                type:'GET',
                url:'displayMessengerAttachedFiles.jsp',
                data:params,
                success:function(data)
                {

                    $('#tdAttachedDocs').html(data);
                    console.log(data);

                }
            });
        }



        function getDemographicId(){
            var search=$('#patient').val();


            $.ajax({
                type:'POST',
                url:'messengerController.jsp',
                data:'demoName='+search,
                success:function(data)
                {

                    console.log(data);
                    $('#demop').html(data);
                    demographicid=$('#demoid').text();
                    fetchAttached($('#demoid').text());
                }
            });
        }
        var DocPopup = null;
        function popup() {
            var location="<rewrite:reWrite jspPage="../cdx/cdxMessengerAttachment.jsp"/>?&demo="+demographicid;
            DocPopup = window.open(location,"_blank","height=380,width=580");

            if (DocPopup != null) {
                if (DocPopup.opener == null) {
                    DocPopup.opener = self;
                }
            }
        }


        function init(){
            var d='<%=demoName%>';
            var draft='<%=draftId%>';
            if(d!='null' && d!='' && '<%=docId%>' !='null' && '<%=docId%>' !='') {
                $('#patient').val('<%=demoName%>');
                getDemographicId();
                $("a#mtype").attr('href','../dms/showCdxDocumentArchive.jsp?ID=<%=docId%>');
                $('#msgtype').val('In response to:'+'<%=docId%>');
                $("span#ptype").text('In response to ');
                $("#mtype").text('<%=docKind%>');
            }

            if(draft!='null' && draft!='' && '<%=patient%>' !='null' && '<%=patient%>' !=''){
                $('#patient').val('<%=patient%>');
                getDemographicId();
                var preci='<%=primary%>';
                var sreci='<%=secondary%>';
                var primarysplit=preci.split("#");
                var secondarysplit=sreci.split("#");

                if(preci!=null && preci!='' && primarysplit.length>=1) {
                    for (var i = 0; i < primarysplit.length; i++) {
                        $('#primary').append("<li> <a href='javascript:void(0);' class='remove'><b>&times;</b></a><input type='hidden' name='precipients' value='" + primarysplit[i] + "'>" + primarysplit[i] + "</li>");

                    }
                    $('#hiddeninput').hide();
                    $('#primary').show();

                }
                else{
                        $('#primary').hide();
                        $('#hiddeninput').show();
                    }


                if(sreci!=null && sreci!='' && secondarysplit.length>=1) {
                    for (var i = 0; i < secondarysplit.length; i++) {
                        $('#secondary').append("<li> <a href='javascript:void(0);' class='removes'><b>&times;</b></a><input type='hidden' name='srecipients' value='" + secondarysplit[i] + "'>" + secondarysplit[i] + "</li>");

                    }
                    $('#hiddensecondary').hide();
                    $('#secondary').show();

                }
                else {
                    $('#secondary').hide();
                    $('#hiddensecondary').show();
                }


                $("#msgtype").val('<%=msgType%>');

                if('<%=msgType%>'!='New'){
                    $("a#mtype").attr('href','../dms/showCdxDocumentArchive.jsp?ID=<%=docId%>');
                    $("span#ptype").text('In response to ');
                    $("#mtype").text('<%=docKind%>');
                }


                $('#documenttype').val('<%=documentType%>');
                $('#content1').val('<%=content%>');
            }

        }


        function saveDraft(){
            var patient=$('#patient').val();
            var precipients=[];
            var srecipients=[];

            var primary=document.getElementsByName('precipients');
            var secondary=document.getElementsByName('srecipients');
             for(var i=0;i<primary.length;i++){
                 precipients[i]=primary[i].value;
             }
            for(var i=0;i<secondary.length;i++){
                srecipients[i]=secondary[i].value;
            }

            var msgtype=$('#msgtype').val();
            var documenttype=$('#documenttype').val();
            var content=$('#content1').val();

                $.ajax({

                    type:'POST',
                    url:'cdxMessengerController.jsp',
                    data:{patient:patient,"precipients[]":precipients,"srecipients[]":srecipients,msgtype:msgtype,documenttype:documenttype,content:content},
                    success:function(data)
                    {
                        console.log(data);
                       alert("Saved as draft.");

                    }
                });



        }
        function getInfo(type) {

            $.ajax({

                type: 'POST',
                url: 'messengerController.jsp',
                data: {
                    type: type,
                    demoid: demographicid
                },
                success: function (data) {
                    console.log(data);
                    //$('#otherinfo').append(data);

                    if (data) {
                        if (type == 'FamilyHistory') {
                            $('#FamilyHistory').show();
                            $('#FamilyHistory').html('<b>Family History: </b>'+data.trim());

                        } else if (type == 'MedicalHistory') {
                            $('#MedicalHistory').show();
                            $('#MedicalHistory').html('<b>Medical History: </b>'+data.trim());

                        } else if (type == 'ongoingConcerns') {
                            $('#ongoingConcerns').show();
                            $('#ongoingConcerns').html('<b>Ongoing Concerns: </b>'+data.trim());

                        } else if (type == 'SocialHistory') {
                            $('#SocialHistory').show();
                            $('#SocialHistory').html('<b>Social History: </b>'+data.trim());

                        } else if (type == 'OtherMeds') {
                            $('#OtherMeds').show();
                            $('#OtherMeds').html('<b>Other Meds: </b>'+data.trim());

                        } else if (type == 'Reminders') {
                            $('#Reminders').show();
                            $('#Reminders').html('<b>Reminders: </b>'+data.trim());

                        }

                    }
                }
            });

        }


        function getRestInfo( type) {
            $.ajax({
                type: 'POST',
                url : "${ pageContext.request.contextPath }/oscarConsultationRequest/consultationClinicalData.do",
                data: {
                    method: type,
                    demographicNo: demographicid
                },
                dataType : 'JSON',
                success: function(data) {
                    var jsondata=JSON.parse(JSON.stringify(data));
                    if (type == 'fetchRiskFactors') {
                        $('#fetchRiskFactors').show();
                        $('#fetchRiskFactors').html('<b>Risk Factors: </b><br>'+jsondata.note);

                    } else if (type == 'fetchMedications') {
                        $('#fetchMedications').show();
                        $('#fetchMedications').html('<b>Medications: </b><br>'+jsondata.note);

                    } else if (type == 'fetchLongTermMedications') {
                        $('#fetchLongTermMedications').show();
                        $('#fetchLongTermMedications').html('<b>Long Medications: </b><br>'+jsondata.note);

                    } else if (type == 'fetchAllergies') {
                        $('#fetchAllergies').show();
                        $('#fetchAllergies').html('<b>Allergies: </b><br>'+jsondata.note);

                    }
                }
            });
        }

    function populateAndSubmit(){

    $('#hiddentextarea').val($('#FamilyHistory').text().trim()+'\n');
    $('#hiddentextarea').val(  $('#hiddentextarea').val().trim()+'\n'+$('#MedicalHistory').text().trim()+'\n');
    $('#hiddentextarea').val(  $('#hiddentextarea').val().trim()+'\n'+$('#ongoingConcerns').text().trim()+'\n');
    $('#hiddentextarea').val(  $('#hiddentextarea').val().trim()+'\n'+$('#SocialHistory').text().trim()+'\n');
    $('#hiddentextarea').val(  $('#hiddentextarea').val().trim()+'\n'+$('#OtherMeds').text().trim()+'\n');
    $('#hiddentextarea').val(  $('#hiddentextarea').val().trim()+'\n'+$('#Reminders').text().trim()+'\n');
    $('#hiddentextarea').val(  $('#hiddentextarea').val().trim()+'\n'+$('#fetchRiskFactors').text().trim()+'\n');
    $('#hiddentextarea').val(  $('#hiddentextarea').val().trim()+'\n'+$('#fetchMedications').text().trim()+'\n');
    $('#hiddentextarea').val(  $('#hiddentextarea').val().trim()+'\n'+$('#fetchLongTermMedications').text().trim()+'\n');
    $('#hiddentextarea').val(  $('#hiddentextarea').val().trim()+'\n'+$('#fetchAllergies').text().trim()+'\n');

    $('#hiddentextarea').val( $('#hiddentextarea').val().trim());
    $('#messengerForm').submit();

}
    </script>

</head>

<body>
<div class="container">

    <form class="well form-horizontal" action="<%=request.getContextPath()%>/cdx/CDXMessenger.do" method="post"  id="messengerForm">
        <fieldset>

            <!-- Form Name -->


                <div>  <legend><center><h2> <b>CDX Composer</b>
                    <span>
                        <a href="../cdx/cdxMessengerHistory.jsp" target="_blank" class="btn ovalbutton" role="button">History</a>
                        <a href="../cdx/showDrafts.jsp" target="_blank" class="btn ovalbutton" role="button">Drafts</a>
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
                        <input  name="messagetype" id="msgtype" value="New" class="form-control"  type="hidden" readonly style="color:gray;" >
                        <span id="ptype">New</span> <a href="#" id="mtype" target="_blank">
                    </a>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-xs-4 control-label">Document type</label>
                <div class="col-xs-4 selectContainer">
                    <div class="">
                        <select name="documenttype" id="documenttype" class="form-control selectpicker" required>
                            <option value="">Select document type</option>
                            <option>Information Request</option>
                            <option>Patient Summary</option>
                            <option>Progress Note</option>

                        </select>
                    </div>
                </div>
            </div>

            <!-- Text input-->

            <div class="form-group">
                <label class="col-xs-4 control-label">Content</label>
                <div class="col-xs-4 inputGroupContainer">
                    <div class="">
                        <textarea  name="contentmessage" id="content1" placeholder="Content" class="form-control"  type="text"></textarea>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-xs-4 control-label">Other important info</label>
                <div class="col-xs-4 inputGroupContainer" id="infobuttons">
                    <button type="button" class="btn btn-primary btn-xs" onclick="getInfo('FamilyHistory')">Family History</button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getInfo('MedicalHistory')">Medical History</button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getInfo('ongoingConcerns')">Ongoing Concerns</button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getInfo('SocialHistory')">Social History</button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getInfo('OtherMeds')">Other Meds</button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getInfo('Reminders')">Reminders</button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getRestInfo('fetchRiskFactors')">Risk Factors</button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getRestInfo('fetchMedications')">Medications</button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getRestInfo('fetchLongTermMedications')">Long Term Medications</button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getRestInfo('fetchAllergies')">Allergies</button>
                    <br>
                    <br>
                    <textarea  id="hiddentextarea" name="otherinfo" class="form-control"  style="display:none;"></textarea>

                    <div contenteditable="true" id="otherinfo" class="otherinfo">
                        <p id="FamilyHistory"></p>
                        <p id="MedicalHistory"></p>
                        <p id="ongoingConcerns"></p>
                        <p id="SocialHistory"></p>
                        <p id="OtherMeds"></p>
                        <p id="Reminders"></p>
                        <p id="fetchRiskFactors"></p>
                        <p id="fetchMedications"></p>
                        <p id="fetchLongTermMedications"></p>
                        <p id="fetchAllergies"></p>

                    </div>
                </div>
            </div>



            <!-- Text input-->


            <div class="form-group">
                <label class="col-xs-4 control-label" >Attachment</label>
                <div class="col-xs-4 inputGroupContainer">
                    <div class="custom-file mb-3">
                        <a href="#" onclick="popup();return false;">
                            Attach file to Composer
                        </a>
                        <p id="tdAttachedDocs"></p>

                    </div>
                </div>
            </div>



            <!-- Select Basic -->

            <!-- Success message -->

            <!-- Button -->
            <div class=" button-group">
                <div class="col-xs-4">
                    <button type="button" class="btn btn-success" onclick="populateAndSubmit()">Send</button>
                </div>
                <div class="col-xs-4 ">
                    <input  name="draftId"  value=<%=draftId%> class="form-control"  type="hidden">
                    <button type="button" class="btn btn-warning" onclick="saveDraft()">Save Draft</button>
                </div>
                <div class="col-xs-4">
                    <button type="button" class="btn btn-default" onclick="window.open('', '_self', ''); window.close();">Discard</button>
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
<p hidden id="demop"> </p>
</body>

</html>
