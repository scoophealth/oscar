<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="oscar.dms.data.*,oscar.oscarProvider.data.*,java.util.*,oscar.oscarLab.ca.on.CommonLabResultData" %>
<%
            List<Hashtable> queues = QueueData.getQueues();
            ArrayList providers = ProviderData.getProviderList();
            String queueIdStr = (String) request.getSession().getAttribute("preferredQueue");
            int queueId = 1;
            if (queueIdStr != null) {
                queueIdStr = queueIdStr.trim();
                queueId = Integer.parseInt(queueIdStr);
            }
            String provider = CommonLabResultData.NOT_ASSIGNED_PROVIDER_NO;
            //String provider ="";//(String) session.getValue("user");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="inboxmanager.document.title"/></title>
        <script type="text/javascript" src="../share/javascript/prototype.js"></script>
        <script type="text/javascript" src="../share/javascript/scriptaculous.js"></script>

        <script type="text/javascript" src="../share/javascript/swfupload.js"></script>
        <script type="text/javascript" src="../share/javascript/swfupload.queue.js"></script>
        <script type="text/javascript" src="../share/javascript/fileprogress.js"></script>
        <script type="text/javascript" src="../share/javascript/handlers.js"></script>
        <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
        <style type="text/css">
            .swfupload {
                border: 2px outset blue;
                vertical-align: top;
                font-family:Arial,Helvetica,sans-serif;
            }
            body.mainbody {
                padding: 0px;
                margin: 3px;
                font-size: 13px;
            }
            input.theFont{
                font-size: 8pt;
            }
            div.maindiv {
                background-color: #f2f7ff;
                border: 1px solid #acb3f5;
                height: 390px;
            }
            div.maindivheading {
                background-color: #acb3f5;
                font-size: 14px;
            }
            label.labels {
                float: left;
                clear: left;
                width: 160px;
            }
            .field {
                float: left;
            }
            table.layouttable {
                font-family: Verdana, Tahoma, Arial, sans-serif;
                font-size: 12px;
            }

        </style>
        <script type="text/javascript">
            var swfu;

            window.onload = function() {
                var settings = {
                    flash_url : "../share/swfupload.swf",
                    upload_url: "../dms/addEditDocument.do;jsessionid=<%=request.getRequestedSessionId()%>", 	// Relative to the SWF file
                    //post_params: {"queue" : "<%=queueId%>","provider":"<%=provider%>"},


                    file_post_name : "filedata",
                    post_params: {"method" : "multifast"},
                    use_query_string : true,

                    file_size_limit : "100 MB",
                    file_types : "*.*",
                    file_types_description : "All Files",
                    file_upload_limit : 100,
                    file_queue_limit : 0,
                    custom_settings : {
                        progressTarget : "fsUploadProgress",
                        cancelButtonId : "btnCancel"
                    },
                    debug: false,

                    // Button settings
                    button_image_url: "../images/btn.png",
                    button_width: "74",
                    button_height: "29",
                    button_placeholder_id: "btnUpload",
                    button_text: '<span class=\"theFont\">Upload</span>',
                    button_text_style: ".theFont { font-size: 9pt;  font-family:Arial,Helvetica,sans-serif;}",
                    button_text_left_padding: 12,
                    button_text_top_padding: 5,

                    button_action : SWFUpload.BUTTON_ACTION.SELECT_FILES,
                    button_disabled : false,
                    button_cursor : SWFUpload.CURSOR.HAND,
                    //button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,

                    // The event handler functions are defined in handlers.js
                    file_queued_handler : fileQueued,
                    file_queue_error_handler : fileQueueError,
                    file_dialog_complete_handler : fileDialogComplete,
                    //upload_start_handler : uploadStart,
                    upload_start_handler : function(file){
                        var q=$('queue').value;
                        var p=$('provider').value;
                        swfu.addPostParam("queue",q);
                        swfu.addPostParam("provider",p);
                        return true;
                    },
                    upload_progress_handler : uploadProgress,
                    upload_error_handler : uploadError,
                    upload_success_handler : uploadSuccess,
                    upload_complete_handler : uploadComplete,
                    queue_complete_handler : queueComplete	// Queue plugin event
                };

                
                swfu = new SWFUpload(settings);
            };
           
            function addProviderToPost(ele){
                $('provider').value=ele.options[ele.selectedIndex].value;
                //swfu.addPostParam("provider",ele.options[ele.selectedIndex].value);
            }
            function addQueueToPost(ele){
                $('queue').value=ele.options[ele.selectedIndex].value;
                //console.log("ele.options[ele.selectedIndex].value "+ele.options[ele.selectedIndex].value);
                //swfu.addPostParam("queue",ele.options[ele.selectedIndex].value);
            }
            function addpostparameters(){                
                var q=$('queue').value;
                var p=$('provider').value;
                //console.log('q='+q+';p='+p);
                swfu.addPostParam("queue",q);
                swfu.addPostParam("provider",p);
            }
        </script>

    
    </head>
    <body class="mainbody">
        <div class="maindiv">
            <div class="maindivheading">
                &nbsp;&nbsp;&nbsp; <bean:message key="inboxmanager.document.addMultipleDocuments"/>
            </div>

            <form id="form1" action="../dms/addEditDocument.do" method="post" enctype="multipart/form-data">
                <input type="hidden" id="queue" value="<%=queueId%>"/>
                <input type="hidden" id="provider" value="<%=provider%>"/>
                <label for="queueDrop" class="fields">Send to Queue:</label>
                <select onchange="javascript:addQueueToPost(this);" id="queueDrop" name="queueDrop">

                    <%
                                for (Hashtable ht : queues) {
                                    int id = Integer.parseInt((String) ht.get("id"));
                                    String qName = (String) ht.get("queue");
                    %>
                    <option value="<%=id%>" <%=((id == queueId) ? " selected" : "")%>><%= qName%> </option>
                    <%}%>
                </select>
                <label for="providerDrop" class="fields">Send to Provider:</label>
                <select onchange="javascript:addProviderToPost(this);" id="providerDrop" name="providerDrop">
                    <option value="-2" <%=("-2".equals(provider) ? " selected" : "")%> >None</option>
                    <%for (int i = 0; i < providers.size(); i++) {
                                    Hashtable h = (Hashtable) providers.get(i);%>
                    <option value="<%= h.get("providerNo")%>" <%= (h.get("providerNo").equals(provider) ? " selected" : "")%>><%= h.get("lastName")%> <%= h.get("firstName")%></option>
                    <%}%>
                </select>

                <fieldset class="flash" id="fsUploadProgress">
                    <legend><bean:message key="inboxmanager.document.uploadDocuments"/></legend>
                </fieldset>
                <div id="divStatus"><bean:message key="inboxmanager.document.initialFileUpload"/></div>
                <div>
                    <span id="swfupload"></span>
                    <input id="btnUpload" class="theFont" type="button" value="Upload" onclick="" style="" />
                    <input id="btnCancel" type="button" value="Cancel All Uploads" onclick="swfu.cancelQueue();" disabled="disabled" style="color: gray;font-size: 8pt; width: 113px; height: 30px; border: 2px outset  silver; background-image:url('../images/btn.png');  " />

                </div>

            </form>
        </div>
    </body>
</html>