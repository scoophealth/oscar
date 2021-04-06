<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxProvenance" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxMessenger" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxMessengerDao" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxProvenanceDao" %>
<%@ page import="org.oscarehr.integration.cdx.CDXSpecialist" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- add for special encounter -->
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special" %>
<!-- end -->
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>

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
    Integer docId = null;
    String docKind = "";
    String draftId = request.getParameter("Id");
    String patient = request.getParameter("demoName");
    String patientId = request.getParameter("demoId");
    String primary = request.getParameter("replyTo");
    String secondary = "";
    String msgType = "";
    String documentType = "";
    String content = "";

    // Init reply variables
    CdxProvenance doc = (CdxProvenance) session.getAttribute("document");
    if (doc != null) {
        docId = doc.getId();
        docKind = doc.getKind();
        primary = CDXSpecialist.extractAuthorAtClinic(doc.getPayload());
        session.removeAttribute("document"); // clear session attribute to avoid issues when
    }

    // Init draft variables
    if (draftId != null && !draftId.equalsIgnoreCase("")) {
        CdxMessenger cdxMessenger = cdxMessengerDao.getCdxMessenger(Integer.parseInt(draftId));
        patient = cdxMessenger.getPatient();
        primary = cdxMessenger.getPrimaryRecipient();
        secondary = cdxMessenger.getSecondaryRecipient();
        msgType = cdxMessenger.getCategory();

        //Get document details to show in category when populating from draft
        if (!cdxMessenger.getCategory().equalsIgnoreCase("New")) {
            CdxProvenance cdxProvenance = provenanceDao.getCdxProvenance(Integer.parseInt(cdxMessenger.getCategory().split(":")[1]));
            docId = cdxProvenance.getId();
            docKind = cdxProvenance.getKind();
        }

        documentType = cdxMessenger.getDocumentType();
        content = cdxMessenger.getContent();
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
        #success_message {
            display: none;
        }

        .button-group {
            width: 60%;
            margin: 40px auto;
        }

        .list-group-item {
            cursor: pointer;
        }

        #showList {
            max-height: 200px;
            overflow-y: scroll;
            width: 100%;
            display: none;
        }

        .ovalbutton {
            float: right;
            border-radius: 22px;
            background-color: #E8E8E8;
            width: 120px;
        }

        #showrecipient {
            max-height: 200px;
            overflow-y: scroll;
            width: 100%;
            display: none;
        }

        #primary, #secondary {
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

        ul#primary li, ul#secondary li {
            width: max-content;
            padding: 10px 15px;
        }

        #otherinfo {
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

        #infobuttons button {
            position: relative;
            margin: 4px;
        }
    </style>

    <script type="text/javascript">
        $(document).ready(function () {

            init();
            $("form :input").attr("autocomplete", "off");
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

            // search patient info
            $('#patientName').keyup(function () {
                var search = $('#patientName').val();
                if (search !== '' && search !== null) {
                    $.ajax({
                        type: 'GET',
                        url: '${ pageContext.request.contextPath }/cdx/CDXMessenger.do',
                        data: 'method=searchPatient&patient=' + search,
                        success: function (data) { // JSON response
                            console.log(data);
                            var patients = data;
                            var patientList = '<ul class="list-group">\n';
                            for (var i = 0; i < patients.length; i++) {
                                patientList += '    <li class="list-group-item" data-id="' + patients[i].id + '">' + patients[i].fullName + '</li>\n'
                            }
                            if (patients.length === 0) { // Nothing found?
                                patientList += '<li class="list-group-item">No result found !</li>\n';
                            }
                            patientList += '</ul>\n';
                            $('#showList').html(patientList);
                            $('#showList').show();
                        }
                    });
                } else {
                    $('#showList').html('');
                }
            });

            // Select patient
            $(document).on('click', '#showList li', function () {
                $('#showList').hide();
                var patientId = $(this).data("id");
                if (!patientId) { // abort if there is no result to select
                    $('#showList').html('');
                    $('#patientId').val('')
                    $('#patientName').val('');
                    return;
                }
                $('#patientId').val(patientId);
                $('#patientName').val($(this).text());
            });

            var recipients = []; // Used to store the providers/clinics

            // Search cdx enabled specialist
            $('#recipient').keyup(function () {
                var searchRecipient = $('#recipient').val();
                if (searchRecipient !== '' && searchRecipient !== null && searchRecipient.length >= 4) {
                    $.ajax({
                        type: 'GET',
                        url: '${ pageContext.request.contextPath }/cdx/CDXMessenger.do',
                        data: 'method=searchRecipient&recipient=' + searchRecipient,
                        success: function (data) { // JSON response
                            console.log(data);
                            $('#clinics').html('');
                            $('#butns').hide();
                            var recipientList = '<ul class="list-group" id="searchrecipients">\n';
                            recipients = data; // store in a global variable
                            for (var i = 0; i < recipients.length; i++) {
                                recipientList += '<li class="list-group-item" data-id="' + recipients[i].id + '">' + recipients[i].fullName + '</li>\n';
                            }
                            if (recipients.length === 0) { // Nothing found?
                                recipientList += '<li class="list-group-item">No result found !</li>\n';
                            }
                            recipientList += '</ul>';
                            $('#showrecipient').html(recipientList);
                            $('#showrecipient').show();
                        }
                    });
                } else {
                    $('#showrecipient').html('');
                    recipients = [];
                }
            });

            // Select specialist and list clinics info for the specialist
            $(document).on('click', '#showrecipient li', function () {
                $('#showrecipient').hide();
                var recipientId = $(this).data('id');
                if (!recipientId) { // abort if there is no result to select
                    $('#recipient').val('');
                    $('#showrecipient').html('');
                    return;
                }
                $('#recipient').val($(this).text());
                for (var i = 0; i < recipients.length; i++) { // get recipients from global variable
                    if (recipients[i].id == recipientId) { // using abstract equality due possibility of differences in types
                        var clinics = recipients[i].clinics;
                        if (clinics.length > 0) {
                            var recipientTable = '<table class="table table-bordered table-striped">\n' +
                                '    <h5>Clinics information for <u><b>' + recipients[i].fullName + '</b></u></h5>\n' +
                                '    <thead><tr>\n' +
                                '        <th>Check to Add</th>\n' +
                                '        <th>Clinic Id</th>\n' +
                                '        <th>Clinic Name</th>\n' +
                                '        <th>Clinic Address</th>\n' +
                                '    </tr></thead>\n' +
                                '    <tbody>\n';
                            for (var j = 0; j < clinics.length; j++) {
                                recipientTable += '        <tr>\n' +
                                    '            <td><input type=checkbox name="checkClinic" value="' + clinics[j].id + '"></td>\n' +
                                    '            <td>' + clinics[j].id + '</td>\n' +
                                    '            <td>' + clinics[j].name + '</td>\n' +
                                    '            <td>' + clinics[j].address + '</td>\n' +
                                    '        </tr>\n';
                            }
                            recipientTable += '    </tbody>\n' +
                                '</table>\n';
                            $('#clinics').html(recipientTable);
                        } else {
                            $('#clinics').html('<h5> No clinic information is available ! </h5>');
                        }
                        $('#butns').show();
                    }
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

            // Clear recipients modal on hide
            $('#addClient_Modal').on('hide.bs.modal', function (e) {
                $('#clinics').html('');
                $('#recipient').val('');
                $('#showrecipient').html('');
                $('#showrecipient').hide();
                $('#butns').hide();
            })
        });

        function addRecipient(isPrimary) {
            var recipient = $('#recipient').val();
            var clinics = document.getElementsByName('checkClinic');
            var checked = $("input[type=checkbox]:checked").length;
            if (!checked) {
                alert("You must select at least one clinic.");
                return false;
            }

            var selectedItems = recipient + "@";
            for (var i = 0; i < clinics.length; i++) {
                if (clinics[i].type === 'checkbox' && clinics[i].checked === true) {
                    selectedItems += clinics[i].value + ", ";
                }
            }
            selectedItems = selectedItems.substring(0, selectedItems.length - 2);
            if (isPrimary) {
                $('#primary').append("<li> <a href='javascript:void(0);' class='remove'><b>&times;</b></a><input type='hidden' name='primaryRecipients' value='" + selectedItems + "'>" + selectedItems + "</li>");
                $('#primary').show();
            } else {
                $('#secondary').append("<li> <a href='javascript:void(0);' class='remove'><b>&times;</b></a><input type='hidden' name='secondaryRecipients' value='" + selectedItems + "'>" + selectedItems + "</li>");
                $('#secondary').show();
            }

            $('#addClient_Modal').modal('hide');
            $('#clinics').html('');
            $('#recipient').val('');
            $('#hiddenPrimary').hide();
            $('#butns').hide();
        }

        $(document).on("click", "a.remove", function () {
            $(this).parent().remove();
            if ($('#primary').children().length < 1) {
                $('#primary').hide();
                $('#hiddenPrimary').show();
            }
            if ($('#secondary').children().length < 1) {
                $('#secondary').hide();
                $('#hiddenSecondary').show();
            }
        });

        function updateAttached() {
            var demographicId = $("#patientId").val();
            setTimeout(function() { fetchAttached(demographicId) }, 2000);
        }

        function fetchAttached(demoId) {
            $.ajax({
                type: 'GET',
                url: '${ pageContext.request.contextPath }/cdx/CDXMessenger.do',
                data: 'method=fetchAttachments&demoNo=' + demoId,
                success: function (data) { // JSON response
                    console.log(data);
                    var attachments = data;
                    if (attachments.docs.length === 0 && attachments.labs.length === 0) {
                        $('#tdAttachedDocs').html("<p id='attachDefault' style='background-color: white; text-align: center;'><bean:message key='oscarEncounter.oscarConsultationRequest.AttachDoc.Empty'/></p>");
                    } else {
                        let attachList = "<ul id='attachedList' style='background-color: white; padding-left: 20px; list-style-position: outside; list-style-type: lower-roman;'>\n";
                        for (let i = 0; i < attachments.docs.length; i++) {
                            attachList += "    <li class='doc'>" + attachments.docs[i] + "</li>\n";
                        }
                        for (let i = 0; i < attachments.labs.length; i++) {
                            attachList += "    <li class='lab'>" + attachments.labs[i] + "</li>\n";
                        }
                        attachList += "</ul>\n";
                        $('#tdAttachedDocs').html(attachList);
                    }
                }
            });
        }

        function reloadDemographic() {
            var fullName = $('#patientName').val(); // last, first
            var search = fullName.split(","); // search by last name
            $.ajax({
                type: 'GET',
                url: '${ pageContext.request.contextPath }/cdx/CDXMessenger.do',
                data: 'method=searchPatient&patient=' + search[0],
                success: function (data) { // JSON response
                    console.log(data);
                    var patients = data;
                    for (var i = 0; i < patients.length; i++) {
                        if (patients[i].fullName === fullName) { // filter by full name
                            $("#patientId").val(patients[i].id);
                            fetchAttached(patients[i].id);
                            return;
                        }
                    }
                    console.warn('No patient found!');
                }
            });
        }

        var DocPopup = null;

        function popup() {
            var demographicId = $("#patientId").val();
            var location = "<rewrite:reWrite jspPage="../cdx/cdxMessengerAttachment.jsp"/>?&demo=" + demographicId;
            DocPopup = window.open(location, "_blank", "height=380,width=580");
            if (DocPopup != null) {
                if (DocPopup.opener == null) {
                    DocPopup.opener = self;
                }
            }
        }

        function init() {
            <% if (patient != null && !patient.isEmpty()) { %>
                $('#patientName').val('<%=patient%>');
                reloadDemographic();
            <% } %>

            <% if (primary != null && !primary.isEmpty()) { %>
                var preci = '<%=primary%>';
                var primarysplit = preci.split("#");

                if (preci !== '' && primarysplit.length >= 1) {
                    for (var i = 0; i < primarysplit.length; i++) {
                        $('#primary').append("<li> <a href='javascript:void(0);' class='remove'><b>&times;</b></a><input type='hidden' name='primaryRecipients' value='" + primarysplit[i] + "'>" + primarysplit[i] + "</li>");
                    }
                    $('#hiddenPrimary').hide();
                    $('#primary').show();
                } else {
                    $('#primary').hide();
                    $('#hiddenPrimary').show();
                }
            <% } %>

            <% if (secondary != null && !secondary.isEmpty()) { %>
                var sreci = '<%=secondary%>';
                var secondarysplit = sreci.split("#");

                if (sreci !== '' && secondarysplit.length >= 1) {
                    for (var i = 0; i < secondarysplit.length; i++) {
                        $('#secondary').append("<li> <a href='javascript:void(0);' class='remove'><b>&times;</b></a><input type='hidden' name='secondaryRecipients' value='" + secondarysplit[i] + "'>" + secondarysplit[i] + "</li>");
                    }
                    $('#hiddenSecondary').hide();
                    $('#secondary').show();
                } else {
                    $('#secondary').hide();
                    $('#hiddenSecondary').show();
                }
            <% } %>

            <% if (docId != null && !docId.equals(0)) { %>
                $("a#mtype").attr('href', '<%=request.getContextPath()%>/dms/showCdxDocumentArchive.jsp?ID=<%=docId%>');
                $('#msgtype').val('In response to:' + '<%=docId%>');
                $("span#ptype").text('In response to ');
                $("#mtype").text('<%=docKind%>');
            <% } %>

            <% if (msgType != null && !msgType.isEmpty()) { %>
                $("#msgtype").val('<%=msgType%>');
                $('#documenttype').val('<%=documentType%>');
                $('#content1').val('<%=content%>');
            <% } %>
        }

        function saveDraft() {
            var patient = $('#patientName').val();
            var primaryRecipients = [];
            var secondaryRecipients = [];

            var primary = document.getElementsByName('primaryRecipients');
            var secondary = document.getElementsByName('secondaryRecipients');
            for (var i = 0; i < primary.length; i++) {
                primaryRecipients[i] = primary[i].value;
            }
            for (var i = 0; i < secondary.length; i++) {
                secondaryRecipients[i] = secondary[i].value;
            }

            var msgtype = $('#msgtype').val();
            var documenttype = $('#documenttype').val();
            var content = $('#content1').val();

            $.ajax({
                type: 'POST',
                url : "${ pageContext.request.contextPath }/cdx/CDXMessenger.do",
                data: {
                    method: "saveDraft",
                    patient: patient,
                    primaryRecipients: primaryRecipients,
                    secondaryRecipients: secondaryRecipients,
                    msgtype: msgtype,
                    documenttype: documenttype,
                    content: content
                },
                success: function (data) {
                    console.log(data);
                    alert("Saved as draft.");
                }
            });
        }

        function getInfo(type) {
            var demographicId = $("#patientId").val();
            $.ajax({
                type: 'GET',
                url: '${ pageContext.request.contextPath }/cdx/CDXMessenger.do',
                data: 'method=fetchPatientInfo&demographyId=' + demographicId + '&type=' + type,
                success: function (data) { // plain text response
                    console.log(data);
                    if (data) {
                        if (type === 'FamilyHistory') {
                            $('#FamilyHistory').show();
                            $('#FamilyHistory').html('<b>Family History: </b>' + data.trim());
                        } else if (type === 'MedicalHistory') {
                            $('#MedicalHistory').show();
                            $('#MedicalHistory').html('<b>Medical History: </b>' + data.trim());
                        } else if (type === 'ongoingConcerns') {
                            $('#ongoingConcerns').show();
                            $('#ongoingConcerns').html('<b>Ongoing Concerns: </b>' + data.trim());
                        } else if (type === 'SocialHistory') {
                            $('#SocialHistory').show();
                            $('#SocialHistory').html('<b>Social History: </b>' + data.trim());
                        } else if (type === 'OtherMeds') {
                            $('#OtherMeds').show();
                            $('#OtherMeds').html('<b>Other Meds: </b>' + data.trim());
                        } else if (type === 'Reminders') {
                            $('#Reminders').show();
                            $('#Reminders').html('<b>Reminders: </b>' + data.trim());
                        }
                    }
                }
            });
        }

        function getRestInfo(type) {
            var demographicId = $("#patientId").val();
            $.ajax({
                type: 'POST',
                url: "${ pageContext.request.contextPath }/oscarConsultationRequest/consultationClinicalData.do",
                data: {
                    method: type,
                    demographicNo: demographicId
                },
                dataType: 'JSON',
                success: function (data) { // JSON response
                    var jsondata = data;
                    if (type === 'fetchRiskFactors') {
                        $('#fetchRiskFactors').show();
                        $('#fetchRiskFactors').html('<b>Risk Factors: </b><br>' + jsondata.note);
                    } else if (type === 'fetchMedications') {
                        $('#fetchMedications').show();
                        $('#fetchMedications').html('<b>Medications: </b><br>' + jsondata.note);
                    } else if (type === 'fetchLongTermMedications') {
                        $('#fetchLongTermMedications').show();
                        $('#fetchLongTermMedications').html('<b>Long Medications: </b><br>' + jsondata.note);
                    } else if (type === 'fetchAllergies') {
                        $('#fetchAllergies').show();
                        $('#fetchAllergies').html('<b>Allergies: </b><br>' + jsondata.note);
                    }
                }
            });
        }

        function populateAndSubmit() {
            $('#hiddentextarea').val($('#FamilyHistory').text().trim() + '\n');
            $('#hiddentextarea').val($('#hiddentextarea').val().trim() + '\n' + $('#MedicalHistory').text().trim() + '\n');
            $('#hiddentextarea').val($('#hiddentextarea').val().trim() + '\n' + $('#ongoingConcerns').text().trim() + '\n');
            $('#hiddentextarea').val($('#hiddentextarea').val().trim() + '\n' + $('#SocialHistory').text().trim() + '\n');
            $('#hiddentextarea').val($('#hiddentextarea').val().trim() + '\n' + $('#OtherMeds').text().trim() + '\n');
            $('#hiddentextarea').val($('#hiddentextarea').val().trim() + '\n' + $('#Reminders').text().trim() + '\n');
            $('#hiddentextarea').val($('#hiddentextarea').val().trim() + '\n' + $('#fetchRiskFactors').text().trim() + '\n');
            $('#hiddentextarea').val($('#hiddentextarea').val().trim() + '\n' + $('#fetchMedications').text().trim() + '\n');
            $('#hiddentextarea').val($('#hiddentextarea').val().trim() + '\n' + $('#fetchLongTermMedications').text().trim() + '\n');
            $('#hiddentextarea').val($('#hiddentextarea').val().trim() + '\n' + $('#fetchAllergies').text().trim() + '\n');
            $('#hiddentextarea').val($('#hiddentextarea').val().trim());
            $('#messengerForm').submit();
        }
    </script>

</head>

<body>
<div class="container">

    <form class="well form-horizontal" action="<%=request.getContextPath()%>/cdx/CDXMessenger.do" method="post" id="messengerForm">
        <fieldset>
            <input type="hidden" name="method" value="submitDocument" />

            <!-- Form Name -->
            <div class="text-center">
                <legend><h2>
                    <b>CDX Composer</b>
                    <span>
                        <a href="<%=request.getContextPath()%>/cdx/cdxMessengerHistory.jsp" target="_blank" class="btn ovalbutton" role="button">History</a>
                        <a href="<%=request.getContextPath()%>/cdx/showDrafts.jsp" target="_blank" class="btn ovalbutton" role="button">Drafts</a>
                    </span>
                </h2></legend>
                <br>
            </div>

            <!-- Text input-->
            <div class="form-group">
                <label class="col-xs-4 control-label">Patient</label>
                <div class="col-xs-4 inputGroupContainer">
                    <div class="">
                        <input name="patientId" id="patientId" type="hidden">
                        <input name="patientName" id="patientName" placeholder="Search" class="form-control" type="text" required>
                        <div id="showList">
                            <ul class="list-group"></ul>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Text input-->

            <div class="form-group">
                <label class="col-xs-4 control-label">Primary Recipient(s)</label>
                <div class="col-xs-4 inputGroupContainer">
                    <div class="">
                        <input id="hiddenPrimary" name="" placeholder="Add primary recipients" class="form-control" type="text" readonly>
                        <ul class="list-group" id="primary" name="primaryrecipient"></ul>
                    </div>
                </div>
                <div class="add-button col-xs-4">
                    <button type="button" data-toggle="modal" data-target="#addClient_Modal" class="btn btn-primary">
                        Add
                    </button>
                </div>
            </div>

            <div class="form-group">
                <label class="col-xs-4 control-label">Secondary Recipient(s)</label>
                <div class="col-xs-4 inputGroupContainer">
                    <div class="">
                        <input id="hiddenSecondary" name="" placeholder="Add secondary recipients" class="form-control" type="text" readonly>
                        <ul class="list-group" id="secondary" name="secondaryrecipient"></ul>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-xs-4 control-label">Message Type</label>
                <div class="col-xs-4 inputGroupContainer">
                    <div class="">
                        <input name="messagetype" id="msgtype" value="New" class="form-control" type="hidden" readonly style="color:gray;">
                        <span id="ptype">New</span> <a href="#" id="mtype" target="_blank"></a>
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
                            <option>Discharge Summary</option>
                            <option>Progress Note</option>
                            <option>Consult Note</option>
                            <option>Referral Note</option>
                            <option>General Purpose Note</option>
                            <option>Care Plan</option>
                        </select>
                    </div>
                </div>
            </div>

            <!-- Text input-->

            <div class="form-group">
                <label class="col-xs-4 control-label">Content</label>
                <div class="col-xs-4 inputGroupContainer">
                    <div class="">
                        <textarea name="contentmessage" id="content1" placeholder="Content" class="form-control" type="text"></textarea>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-xs-4 control-label">Other important info</label>
                <div class="col-xs-4 inputGroupContainer" id="infobuttons">
                    <button type="button" class="btn btn-primary btn-xs" onclick="getInfo('FamilyHistory')">
                        Family History
                    </button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getInfo('MedicalHistory')">
                        Medical History
                    </button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getInfo('ongoingConcerns')">
                        Ongoing Concerns
                    </button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getInfo('SocialHistory')">
                        Social History
                    </button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getInfo('OtherMeds')">
                        Other Meds
                    </button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getInfo('Reminders')">
                        Reminders
                    </button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getRestInfo('fetchRiskFactors')">
                        Risk Factors
                    </button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getRestInfo('fetchMedications')">
                        Medications
                    </button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getRestInfo('fetchLongTermMedications')">
                        Long Term Medications
                    </button>
                    <button type="button" class="btn btn-primary btn-xs" onclick="getRestInfo('fetchAllergies')">
                        Allergies
                    </button>
                    <br>
                    <br>
                    <textarea id="hiddentextarea" name="otherinfo" class="form-control" style="display:none;"></textarea>

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
                <label class="col-xs-4 control-label">Attachment</label>
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
                    <input name="draftId" value=<%=draftId%> class="form-control" type="hidden">
                    <button type="button" class="btn btn-warning" onclick="saveDraft()">Save Draft</button>
                </div>
                <div class="col-xs-4">
                    <button type="button" class="btn btn-default" onclick="window.open('', '_self', ''); window.close();">
                        Discard
                    </button>
                </div>
            </div>

            <div class="alert alert-success" role="alert" id="success_message">
                Success <i class="glyphicon glyphicon-thumbs-up"></i> Success: Message sent!.
            </div>

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
                            <form class="well form-horizontal" action=" " method="post" id="myform">
                                <div class="form-group">
                                    <label class="col-xs-4 control-label">Recipient</label>
                                    <div class="col-xs-4 inputGroupContainer">
                                        <div class="">
                                            <input name="recipients" id="recipient" placeholder="Search"
                                                   class="form-control" type="text">
                                            <div id="showrecipient">
                                                <ul class="list-group" id="searchrecipients"></ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="" id="clinics"></div>

                                <div style="text-align: center; display: none;" id="butns">
                                    <input type="button" class="btn btn-success" value="Add as primary" onclick='addRecipient(true)'>
                                    <input type="button" class="btn btn-warning" value="Add as secondary" onclick='addRecipient(false)'>
                                    <input type="button" class="btn btn-info" value="Select all" id="selectAll">
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
