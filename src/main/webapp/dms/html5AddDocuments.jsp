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
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<!--   /** No SWF Object Multiple File Upload
     * @version         1.1.1
     * @compatibility   Chrome 1, FireFox 3+, Internet Explorer 5+, Opera 8+, Safari 3+
     * @author          Andrea Giammarchi
     * @blog            webreflection.blogspot.com
     * @license         Mit Style License
     */
-->
<%@page import="oscar.dms.data.*,oscar.oscarProvider.data.*,java.util.*,oscar.oscarLab.ca.on.CommonLabResultData,org.oscarehr.util.SpringUtils,org.oscarehr.common.dao.QueueDao" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
          <title><bean:message key="inboxmanager.document.title"/></title>
        <script type="text/javascript" src="../share/javascript/prototype.js"></script>
        <script type="text/javascript" src="../share/javascript/scriptaculous.js"></script>
        <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />

        <%
            QueueDao queueDao = (QueueDao) SpringUtils.getBean("queueDao");
            List<Hashtable> queues=queueDao.getQueues();
            List providers = ProviderData.getProviderList();
            String queueIdStr = (String) request.getSession().getAttribute("preferredQueue");
            int queueId = 1;
            if (queueIdStr != null) {
                queueIdStr = queueIdStr.trim();
                queueId = Integer.parseInt(queueIdStr);
            }
            String provider = CommonLabResultData.NOT_ASSIGNED_PROVIDER_NO;
            //String provider ="";//(String) session.getValue("user");
%>

        <style type="text/css">
        * {
            font-family:Verdana,Tahoma,Arial,sans-serif;
            font-size: 10pt;
            font-weight: normal;
        }
        img {
            border: 0;
        }
        body {
            text-align: center;
        }
        h1 {
            font-size: 12pt;
        }
        h2 {
            margin-top: 32px;
            font-size: 8pt;
            font-weight: bold;
        }
        h3 {
            font-size: 8pt;
        }
        div.form {
            text-align: left;
            margin: auto;
            width: 246px !important;
            width: 242px;
            height: 180px;
            position: relative;
            border: 1px solid #999;
        }
        input.submit {
            font-family:Verdana,Tahoma,Arial,sans-serif;
            font-size: 10pt;
            width: 248px !important;
            width: 242px;
            border: 1px solid #999;
            margin: -1px 0 0 -1px;
            position: absolute;
            top: 180px;
            left: 0;
            cursor:pointer;
        }
        </style>
        <script type="text/javascript" src="../share/javascript/noswfupload.js"></script>

        <script type="text/javascript">

        // add dedicated css
        noswfupload.css("../share/css/noswfupload.css", "../share/css/noswfupload-icons.css");
        var upload_url;//global to attached provider no and queue no.

        onload = function(){
            var
                // the input type file to wrap
                input   = document.getElementsByName("filedata")[0],

                // the submit button
                submit  = document.getElementById("noswfuploadSubmit"),

                // the form
                form    = document.getElementById("noswfupload_form"),

                // the form action to use with noswfupload
                //url     = form.getAttribute("action") || form.action,

                // noswfupload wrap Object
                wrap;

                upload_url=form.getAttribute("action") || form.action;
            // if we do not need the form ...
                // move inputs outside the form since we do not need it
                with(form.parentNode){
                    appendChild(input);
                    appendChild(submit);
                };

                // remove the form
                form.parentNode.removeChild(form);

            // create the noswfupload.wrap Object with 1Mb of limit
            wrap = noswfupload.wrap(input, 10024 * 10024);

            // form and input are useless now (remove references)
            form = input = null;

            // assign event to the submit button
            noswfupload.event.add(submit, "click", function(e){
                wrap.url=upload_url;
                // only if there is at least a file to upload
                if(wrap.files.length){
                    submit.setAttribute("disabled", "disabled");
                    wrap.upload(
                        // it is possible to declare events directly here
                        // via Object
                        // {onload:function(){ ... }, onerror:function(){ ... }, etc ...}
                        // these callbacks will be injected in the wrap object
                        // In this case events are implemented manually
                    );
                } else
                    noswfupload.text(wrap.dom.info, "No files selected");

                submit.blur();

                // block native events
                return  noswfupload.event.stop(e);
            });

            // set wrap object properties and methods (events)

            // url to upload files
            wrap.url = upload_url;

            // accepted file types (filter)
            // wrap.fileType = "Images (*.jpg, *.jpeg, *.png, *.gif, *.bmp)";
             wrap.fileType = "PDF (*.pdf, *.PDF)";
            // fileType could contain whatever text but filter checks *.{extension} if present

            // handlers
            wrap.onerror = function(msg){
                noswfupload.text(this.dom.info, "WARNING: Unable to upload " + this.file.fileName + "(" + msg + ")");
            };

            // instantly vefore files are sent
            wrap.onloadstart = function(){

                // we need to show progress bars and disable input file (no choice during upload)
                this.show(0);

                // write something in the span info
                noswfupload.text(this.dom.info, "Preparing for upload ... ");
            };

            // event called during progress. It could be the real one, if browser supports it, or a simulated one.
            wrap.onprogress = function(rpe, xhr){

                // percent for each bar
                this.show((this.sent + rpe.loaded) * 100 / this.total, rpe.loaded * 100 / rpe.total);

                // info to show during upload
                noswfupload.text(this.dom.info, "Uploading: " + this.file.fileName);

                // fileSize is -1 only if browser does not support file info access
                // this if splits recent browsers from others
                if(this.file.fileSize !== -1){

                    // simulation property indicates when the progress event is fake
                    if(rpe.simulation)
                        // in this case sent data is fake but we still have the total so we could show something
                        noswfupload.text(this.dom.info,
                            "Uploading: " + this.file.fileName,
                            "Total Sent: " + noswfupload.size(this.sent + rpe.loaded) + " of " + noswfupload.size(this.total)
                        );
                    else
                        // this is the best case scenario, every information is valid
                        noswfupload.text(this.dom.info,
                            "Uploading: " + this.file.fileName,
                            "Sent: " + noswfupload.size(rpe.loaded) + " of " + noswfupload.size(rpe.total),
                            "Total Sent: " + noswfupload.size(this.sent + rpe.loaded) + " of " + noswfupload.size(this.total)
                        );
                } else
                    // if fileSIze is -1 browser is using an iframe because it does not support
                    // files sent via Ajax (XMLHttpRequest)
                    // We can still show some information
                    noswfupload.text(this.dom.info,
                        "Uploading: " + this.file.fileName,
                        "Sent: " + (this.sent / 100) + " out of " + (this.total / 100)
                    );
            };

            // generated if there is something wrong during upload
            wrap.onerror = function(msg){
            	// just inform the user something was wrong
                noswfupload.text(this.dom.info, "WARNING: Unable to upload " + this.file.fileName + "(" + msg + ")");
            };

            // generated when every file has been sent (one or more, it does not matter)
            wrap.onload = function(rpe, xhr){
            	var self = this;
                // just show everything is fine ...
                noswfupload.text(this.dom.info, "Upload complete");

                // ... and after a second reset the component
                setTimeout(function(){
                    self.clean();   // remove files from list
                    self.hide();    // hide progress bars and enable input file

                    noswfupload.text(self.dom.info, "");

                    // enable again the submit button/element
                    submit.removeAttribute("disabled");
                }, 1000);
            };

        };

     function changeProviderAndQueue(){
            upload_url="../dms/addEditDocument.do?method=html5MultiUpload&queue="+$('queue').value+"&provider="+$('provider').value;

     }
     function addProviderToPost(ele){
                $('provider').value=ele.options[ele.selectedIndex].value;
                changeProviderAndQueue();
            }
    function addQueueToPost(ele){
                $('queue').value=ele.options[ele.selectedIndex].value;
                changeProviderAndQueue();
            }
        </script>
<style type="text/css">
            div.maindiv {
                background-color: #f2f7ff;
                border: 1px solid #acb3f5;
                height: 390px;
            }
            .noswfupload{
                 background-color: #f2f7ff;
            }
            div.maindivheading {
                font-family:Verdana,Tahoma,Arial,sans-serif;
                background-color: #acb3f5;
                font-size: 14px;
            }
            label.labels {
                float: left;
                clear: left;
                width: 160px;
            }
            .fields{
                font-family:Verdana,Tahoma,Arial,sans-serif;
                font-size: 10pt;
            }


        </style>
    </head>
    <body>
        <div class="maindiv">
            <div class="maindivheading">
                &nbsp;&nbsp;&nbsp; <bean:message key="inboxmanager.document.addMultipleDocuments"/>
            </div>
           <div >
                <input type="hidden" id="queue" value="<%=queueId%>"/>
                <input type="hidden" id="provider" value="<%=provider%>"/>
                <label for="queueDrop" class="fields">Send to Queue:</label>
                <select onchange="javascript:addQueueToPost(this);" id="queueDrop" name="queueDrop">

                    <%
                                for (Hashtable ht : queues) {
                                    int id = (Integer)ht.get("id");
                                    String qName = (String) ht.get("queue");
                    %>
                    <option value="<%=id%>" <%=((id == queueId) ? " selected" : "")%>><%= qName%> </option>
                    <%}%>
                </select>
                <br/>
                <label for="providerDrop" class="fields">Send to Provider:</label>
                <select onchange="javascript:addProviderToPost(this);" id="providerDrop" name="providerDrop">
                    <option value="-2" <%=("-2".equals(provider) ? " selected" : "")%> >None</option>
                    <%for (int i = 0; i < providers.size(); i++) {
                                    Map h = (Map) providers.get(i);%>
                    <option value="<%= h.get("providerNo")%>" <%= (h.get("providerNo").equals(provider) ? " selected" : "")%>><%= h.get("lastName")%> <%= h.get("firstName")%></option>
                    <%}%>
                </select>
            </div>
        <div class="form">
            <form id="noswfupload_form" method="post" action="../dms/addEditDocument.do?method=html5MultiUpload&queue=<%=queueId%>&provider=<%=provider%>" enctype="multipart/form-data">
                <div>
                    <input type="file" name="filedata" />
                    <input id="noswfuploadSubmit" class="submit" type="submit" value="Upload File" />
                </div>
            </form>
        </div>
        </div>

    </body>

</html>
