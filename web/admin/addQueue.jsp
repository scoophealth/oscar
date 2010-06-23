

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="oscar.oscarProvider.data.*,java.util.*,oscar.dms.data.QueueData" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/prototype.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/Oscar.js"/>"></script>
        <script type="text/javascript" src="../share/yui/js/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="../share/yui/js/connection-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/animation-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/datasource-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/autocomplete-min.js"></script>
        <link rel="stylesheet" type="text/css" href="../share/yui/css/fonts-min.css"/>
        <link rel="stylesheet" type="text/css" href="../share/yui/css/autocomplete.css"/>
        <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
        <style type="text/css">
            .swfupload {
                border: 1px solid black;
            }
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

            #myAutoComplete {
                width:15em; /* set width here or else widget will expand to fit its container */
                padding-bottom:2em;
            }
            .match {
                    font-weight:bold;
                }
        .yui-ac {
	    position:relative;font-family:arial;font-size:100%;
	}

	/* styles for input field */
	.yui-ac-input {
	    position:relative;width:97%;
	}

	/* styles for results container */
	.yui-ac-container {
	    position:absolute;top:0em;width:100%;
	}

	/* styles for header/body/footer wrapper within container */
	.yui-ac-content {
	    position:absolute;width:100%;border:1px solid #808080;background:#fff;overflow:hidden;z-index:9050;
	}

	/* styles for container shadow */
	.yui-ac-shadow {
	    position:absolute;margin:.0em;width:100%;background:#000;-moz-opacity: 0.10;opacity:.10;filter:alpha(opacity=10);z-index:9049;
	}

	/* styles for results list */
	.yui-ac-content ul{
	    margin:0;padding:0;width:100%;
	}

	/* styles for result item */
	.yui-ac-content li {
	    margin:0;padding:0px 0px;cursor:default;white-space:nowrap;
	}

	/* styles for prehighlighted result item */
	.yui-ac-content li.yui-ac-prehighlight {
	    background:#B3D4FF;
	}

	/* styles for highlighted result item */
	.yui-ac-content li.yui-ac-highlight {
	    background:#426FD9;color:#FFF;
	}

        </style>
    </head>
    <body class="mainbody">
         <table class="MainTable" id="scrollNumber1" name="encounterTable" style="margin: 0px;">
            <tr class="topbar">
                <td class="MainTableTopRowLeftColumn" width="60px">Groups</td>
                <td class="MainTableTopRowRightColumn">
                    <table class="TopStatusBar">
                        <tr>
                            <td>Add new groups and add members to groups</td>
                            <td style="text-align: right;"  >
                                    <a href="javascript: popupStart(300, 400, 'Help.jsp')">Help</a> |
                                    <a href="javascript: popupStart(300, 400, 'About.jsp')">About</a> |
                                    <a href="javascript: popupStart(300, 400, 'License.jsp')">License</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
         </table>
        <table  cellspacing="0" id="queue_provider_table" style="margin: 0px;" >
            <tr>
                <td>
                    Queue Name:
                </td>
                <td>
                    <input type="text" id="newQueueName" value="Type group name" onfocus="clearInput(this)" style="color:gray;width:97%"/>
                </td>
                <td>
                    <input type="button" onclick="saveQueue('newQueueName');" value="Submit" />
                </td>
                <td colspan="3">
                    <a id="addQueueSuccessMsg"></a>
                </td>
            </tr>
            <tr>
                <td><br><br></td>
            </tr>
            <tr>
                <td valign="top">
                    Existing queues:
                </td>
                <td>  
                       <%
                        List<Hashtable> queues=QueueData.getQueues();
                        System.out.println(queues);
                        for(Hashtable qht:queues){
                        %>                            
                                <%=(String)qht.get("queue")%>     <br>
                        <%}%>
                </td>
            </tr>
            <tr>
                <td> </td>
                <td><div id="addProviderList"></div></td>
            </tr>
         </table>
    </body>
    <script type="text/javascript">
        function clearInput(ele){
            var val=ele.value;
            if(val=='Type group name' || val=='Search for provider'){
                ele.value='';
                ele.setStyle({color:'black'});
            }
        }
        function optionsSelect(eleId){//loop through options and put selected item in an array
            var qObj=$(eleId);
            var qArr=new Array();
            var i;
            var count=0;
            for(i=0;i<qObj.options.length;i++){
                if(qObj.options[i].selected){
                    qArr[count]=qObj.options[i].value;
                    count++;
                }
            }
            return qArr;
        }
        function saveProviderQueueLink(qId,pName){
            var sAr=optionsSelect(qId);
            var queueStr;
            if(sAr.length>0){
                queueStr=sAr.join("+");

                var lpAr=document.getElementsByName(pName);
                var pAr=new Array();
                var providerStr;
                for(var i=0;i<lpAr.length;i++){
                    pAr.push(lpAr[i].value);
                }
                if(pAr.length>0){
                    providerStr=pAr.join("+");
                    //pass to server to save the links.
                    var data="providers="+providerStr+"&queues="+queueStr;
                    oscarLog(data);
                    var url="<c:out value='${ctx}'/>" + "/dms/inboxManage.do?method=addQueueProviderLink";
                    new Ajax.Request(url, {method: 'post',parameters:data,onSuccess:function(transport){
                            var json=transport.responseText.evalJSON();
                             if(json!=null){

                                 }
                                 else{
                                     
                                 }
                    }});

                }else{
                    //enter provider values
                }
            }else{
                //select queue
            }

        }
        function removeProv(th){
            var ele = th.up();
            ele.remove();
        }
        var highlightMatch = function(full, snippet, matchindex) {
            return full.substring(0, matchindex) +
                "<span class='match'>" +
                full.substr(matchindex, snippet.length) +
                "</span>" +
                full.substring(matchindex + snippet.length);
        };
        var resultFormatter = function(oResultData, sQuery, sResultMatch) {
                var query = sQuery.toLowerCase(),
                    fname = oResultData.fname,
                    lname = oResultData.lname,query = sQuery.toLowerCase(),
                    fnameMatchIndex = fname.toLowerCase().indexOf(query),
                    lnameMatchIndex = lname.toLowerCase().indexOf(query),
                    displayfname, displaylname ;
                if(fnameMatchIndex > -1) {
                    displayfname = highlightMatch(fname, query, fnameMatchIndex);
                }
                else {
                    displayfname = fname;
                }

                if(lnameMatchIndex > -1) {
                    displaylname = highlightMatch(lname, query, lnameMatchIndex);
                }
                else {
                    displaylname = lname;
                }
                return displayfname + " " + displaylname ;

        };
     var myContacts = [
       <%ArrayList providers = ProviderData.getProviderList();
        for (int i = 0; i < providers.size(); i++) {
           Hashtable h = (Hashtable) providers.get(i);%>
           {id: "<%= h.get("providerNo")%>", lname: "<%= h.get("lastName")%>", fname:"<%= h.get("firstName")%>"},
       <%}%>
           { id: "-1",lname: "none", fname:"none"}
    ];
        // Define a custom search function for the DataSource
    var matchNames = function(sQuery) {
        // Case insensitive matching
        var query = sQuery.toLowerCase(),
            contact,
            i=0,
            l=myContacts.length,
            matches = [];
            //console.log(l);

        // Match against each name of each contact
        for(; i<l; i++) {
            contact = myContacts[i];
            if((contact.fname.toLowerCase().indexOf(query) > -1) || (contact.lname.toLowerCase().indexOf(query) > -1) ) {
                matches[matches.length] = contact;
            }
        }

        return matches;
    };
        var oDS = new YAHOO.util.FunctionDataSource(matchNames);
        oDS.responseSchema = {fields: ["id", "fname", "lname"]};
        YAHOO.example.FnMultipleFields = function(){
                                oscarLog("FnMultipleFields ");
                                oscarLog(oDS.responseSchema);
                                // Instantiate AutoComplete
                                var oAC = new YAHOO.widget.AutoComplete("searchProviderName", "searchProviderName_choices", oDS);
                                oAC.useShadow = true;
                                oAC.resultTypeList = false;

                                // Custom formatter to highlight the matching letters
                                oAC.formatResult = resultFormatter;

                                // Define an event handler to populate a hidden form field
                                // when an item gets selected and populate the input field
                                //var myHiddenField = YAHOO.util.Dom.get("myHidden");
                                var myHandler = function(sType, aArgs) {
                                    var myAC = aArgs[0]; // reference back to the AC instance
                                    var elLI = aArgs[1]; // reference to the selected LI element
                                    var oData = aArgs[2]; // object literal of selected item's result data

                                    var bdoc = document.createElement('a');
                                    bdoc.setAttribute("onclick", "removeProv(this);");
                                    bdoc.appendChild(document.createTextNode(" -remove- "));

                                    var adoc = document.createElement('div');
                                    adoc.appendChild(document.createTextNode(oData.fname + " " + oData.lname));

                                    var idoc = document.createElement('input');
                                    idoc.setAttribute("type", "hidden");
                                    idoc.setAttribute("name","linkProviders");
                                    idoc.setAttribute("value",oData.id);
                                    adoc.appendChild(idoc);

                                    adoc.appendChild(bdoc);
                                    var providerList = $('addProviderList');
                                    providerList.appendChild(adoc);

                                    myAC.getInputEl().value = '';//;oData.fname + " " + oData.lname ;
                                };
                                oAC.itemSelectEvent.subscribe(myHandler);
                                return {
                                    oDS: oDS,
                                    oAC: oAC
                                };
                            }();


        function saveQueue(element){
            //oscarLog("in saveQueue "+element);
            var qn=$(element).getValue();
            //oscarLog("in saveQueue "+qn);
            qn=qn.replace(/^\s+/g,"");
            qn=qn.replace(/\s+$/g,"");//trim qn
            if(qn.length>0){                
                var data="newQueueName="+qn;
                var url="<c:out value='${ctx}'/>" + "/dms/inboxManage.do?method=addNewQueue";
                new Ajax.Request(url, {method: 'post',parameters:data,onSuccess:function(transport){
                        var json=transport.responseText.evalJSON();
                         if(json!=null){
                                 var success=json.addNewQueue;
                                 if(success!=null){
                                     if(success==true){
                                        $('addQueueSuccessMsg').innerHTML="Queue Name "+qn+" has been added.";
                                        $('newQueueName').value="";
                                     }else{
                                         $('addQueueSuccessMsg').innerHTML="Queue Name "+qn+" has NOT been added which is probably because it already exists.";
                                         $('newQueueName').value="";
                                     }
                                 }
                             }
                }});
            }else{
                $('addQueueSuccessMsg').innerHTML="Type a queue name";
                $('newQueueName').value="";
            }
        }
    </script>
</html>
