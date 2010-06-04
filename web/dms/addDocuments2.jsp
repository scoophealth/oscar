<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="oscar.dms.data.*,java.util.*" %>
<%
            Hashtable queues = QueueData.getQueues();
            int queueId=0;
            //String provider ="";//(String) session.getValue("user");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Upload Multiple Documents</title>
        <script type="text/javascript" src="../share/javascript/swfupload.js"></script>
        <script type="text/javascript" src="../share/javascript/swfupload.queue.js"></script>
        <script type="text/javascript" src="../share/javascript/fileprogress.js"></script>
        <script type="text/javascript" src="../share/javascript/handlers.js"></script>
        <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
        <style type="text/css">
            .swfupload {
                border: 1px solid black;
            }
        </style>
        <script type="text/javascript">
            var swfu;

window.onload = function() {
			var settings = {
				flash_url : "../share/swfupload.swf",
				upload_url: "../dms/addEditDocument.do;jsessionid=<%=request.getRequestedSessionId()%>", 	// Relative to the SWF file
				//post_params: {"PHPSESSID" : ""},

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
				//button_image_url: "images/TestImageNoText_65x29.png",
				button_width: "65",
				button_height: "29",
				button_placeholder_id: "spanButtonPlaceHolder",
				button_text: '<span class="theFont">Upload</span>',
                                button_text_style: ".theFont { font-size: 14; margin-left:5px; border: 2px solid black; }",
				button_text_left_padding: 12,
				button_text_top_padding: 5,

                                button_action : SWFUpload.BUTTON_ACTION.SELECT_FILES,
                                button_disabled : false, button_cursor : SWFUpload.CURSOR.HAND,
                                //button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,

				// The event handler functions are defined in handlers.js
				file_queued_handler : fileQueued,
				file_queue_error_handler : fileQueueError,
				file_dialog_complete_handler : fileDialogComplete,
				upload_start_handler : uploadStart,
				upload_progress_handler : uploadProgress,
				upload_error_handler : uploadError,
				upload_success_handler : uploadSuccess,
				upload_complete_handler : uploadComplete,
				queue_complete_handler : queueComplete	// Queue plugin event
			};


                swfu = new SWFUpload(settings);
            };


            function addProviderToPost(ele){
                //console.log("ele.options[ele.selectedIndex].value "+ele.options[ele.selectedIndex].value);
                swfu.addPostParam("queueId",ele.options[ele.selectedIndex].value);
            }
        </script>

        <style type="text/css">
            /*body.mainbody {
                padding: 0px;
                margin: 3px;
                font-size: 13px;
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
            }*/

            body.mainbody {
                padding: 0px;
                margin: 3px;
                font-size: 13px;
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
    </head>
    <body class="mainbody">
        <div class="maindiv">
            <div class="maindivheading">
                &nbsp;&nbsp;&nbsp; Add Multiple Documents
            </div>

            <form id="form1" action="../dms/addEditDocument.do" method="post" enctype="multipart/form-data">
                <label for="provider" class="fields">Send to Provider:</label>
                <select onchange="javascript:addProviderToPost(this);" id="queueDrop" name="queue">
                    <option value="-1" <%= ("-1".equals(queueId) ? " selected" : "")%> >None</option>
                    <%Enumeration em=queues.keys();
                    while(em.hasMoreElements()){
                        int id=(Integer)em.nextElement();
                        String qName=(String)queues.get(id);
            %>
                    <option value="<%=id %>" <%=((id==queueId) ? " selected" : "")%>><%= qName%> </option>
                    <%}%>
                </select>

                <fieldset class="flash" id="fsUploadProgress">
                    <legend>Upload Queue</legend>
                </fieldset>
                <div id="divStatus">0 Files Uploaded</div>
                <div>
                    <span id="spanButtonPlaceHolder"></span>
                    <input id="btnCancel" type="button" value="Cancel All Uploads" onclick="swfu.cancelQueue();" disabled="disabled" style="font-size: 8pt;" />
                    <!--input type="button" onclick="window.location = 'undocumentReport.jsp'" value="Add document description"/-->
                    <input type="button" onclick="window.location = 'inboxManage.do'" value="Add document description"/>
                </div>

            </form>
        </div>
    </body>
</html>