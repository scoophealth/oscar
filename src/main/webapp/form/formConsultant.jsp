<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%
    String formClass = "Consultant";
    String formLink = "formConsultant.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId")); // If <= 0, it means a new form is created, otherwise its an old form that is being editted
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
    FrmConsultantRecord rec1 = new FrmConsultantRecord();
    String doctor_name = rec1.getProvName(provNo);          // Retrieve the doctors name from provNo
    if (formId <= 0){
        props = rec1.getInitRefDoc(props, demoNo);
        String refdocNo = props.getProperty("refdocno", "");
        if( !"".equals(refdocNo)) {
        	props = rec1.getDocInfo(props, refdocNo);
        }
        props.setProperty("doc_name",doctor_name);
    }

    props.setProperty("formId", ""+formId);
    props.setProperty("provNo", ""+provNo);
    int i, k;
    String doctor, bill;
    String project_home = request.getContextPath().substring(1);
%>


<html:html locale="true">
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<head>
<title>Letterhead</title>
</head>
<body onload="cleanForm(); start();">
        <html:form action="/form/formname">  <%//The action of the form is important.  Keep the same%>
        <input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
        <input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
        <input type="hidden" name="form_class" value="<%=formClass%>" />
        <input type="hidden" name="form_link" value="<%=formLink%>" />
        <input type="hidden" name="formId" value="<%=formId%>" />
        <input type="hidden" name="provider_no" value="<%=provNo%>" />
        <input type="hidden" name="submit" value="exit"/>
        <input type="hidden" name="billingreferral_no" value="<%=props.getProperty("billingreferral_no", "")%>" />
        <input type="hidden" name="doc_name" value="<%=props.getProperty("doc_name", "")%>" />
        <input type="hidden" name="cl_name" value="<%=props.getProperty("cl_name","")%>" />
        <input type="hidden" name="cl_address1" value="<%=props.getProperty("cl_address1","")%>" />
        <input type="hidden" name="cl_address2" value="<%=props.getProperty("cl_address2","")%>" />
        <input type="hidden" name="cl_phone" value="<%=props.getProperty("cl_phone","")%>" />
        <input type="hidden" name="cl_fax" value="<%=props.getProperty("cl_fax","")%>" />
        <input type="hidden" name="project_home" value="<%=project_home%>" />
        <div style="font-size: 24px; font-family: arial, helvetica, sans-serif;">
            <center>
                <b><%=props.getProperty("doc_name", "")%></b>
            </center>
        </div>
        <div style="font-size: 19px; font-family: arial, helvetica, sans-serif;">
            <center>
                <b><i><%=props.getProperty("cl_name","")%></i></b>
            </center>
        </div>
        <font face="Arial, Helvetica, sans-serif">
        <TABLE WIDTH="100%" align="center" cellpadding="0" cellspacing="0"  style="font-size: 13px;">
            <TR>
                <TD><%=props.getProperty("cl_address1","")%></TD>
                <TD ALIGN="right">Phone: <%=props.getProperty("cl_phone","")%></TD>
            </TR>

            <TR>
                <TD><%=props.getProperty("cl_address2","")%></TD>
                <TD ALIGN="right">Fax: <%=props.getProperty("cl_fax","")%></TD>
            </TR>

        </TABLE>
        <center>
            <TABLE WIDTH="100%" cols="2" style="font-size: 13px;">
                <TD>
                    <TABLE align="center" width="100%" style="border: 1px solid;">
                        <TR>
                            <TD align="left" width="20%">Date:</TD>
                            <TD align="left">
                                    <INPUT name = "consultTime" id="consultTime" style="border: none; font-size: 12px; text-decoration: underline; width: 80%;" TYPE="text" value="<%=props.getProperty("consultTime", "")%>"/><span id="dating"><a href="javascript: function myFunction() {return false; }"id="hlSDate"><small>Select Date</small></a></span>
                            </TD>
                        </TR>
                        <TR>
                            <TD align="left">To:</TD>
                            <TD align="left">
                                    <INPUT name="t_name" style="border: none; font-size: 13px; text-decoration: underline; width: 80%; " type="text" value="<%=props.getProperty("t_name", "")%>" >&nbsp;</INPUT><span id="searching"><a href="javascript:search('billingreferral_no', 't_name', 't_address1', 't_phone', 't_fax')"><small>Search #</small></a></span>

                            </TD>
                        </TR>
                        <TR>
                                <TD align="left">Address:</TD>
                                <TD align="left">

                                    <textarea id="t_address1" name = "t_address1" style="font-size: 13px; text-decoration: underline; width: 90%;"><%=props.getProperty("t_address1", "")%></textarea>

                                </TD>
                            </TR>
                            <TR>
                                <TD align="left">Phone:</TD>
                                <TD align="left">

                                    <INPUT value="<%=props.getProperty("t_phone", "")%>" id="t_phone" name = "t_phone" style="border: none; font-size: 13px; text-decoration: underline; width: 90%;" TYPE="text">&nbsp;</INPUT>

                                </TD>
                            </TR>
                            <TR>
                                <TD align="left">Fax:</TD>
                                <TD align="left">

                                    <INPUT value="<%=props.getProperty("t_fax", "")%>" id="t_fax" name = "t_fax" style="border: none; font-size: 13px; text-decoration: underline; width: 90%;" TYPE="text">&nbsp;</INPUT>

                                </TD>
                            </TR>
                    </TABLE>
                </TD>
                <TD>
                    <TABLE WIDTH="100%" align="center" style="border: 1px solid;" >
                        <TR>
                            <TD align="left" width="40%">Patient:</TD>
                            <TD align="left">

                                    <INPUT NAME="p_name" style="border: none; font-size: 13px; text-decoration: underline; width: 100%;" TYPE="text" value="<%=props.getProperty("p_name","")%>">
                                    </INPUT>

                            </TD>
                        </TR>
                        <TR>
                            <TD align="left">Address:</TD>
                            <TD align="left">

                                    <INPUT NAME="p_address1" style="border: none; font-size: 13px; text-decoration: underline; width: 100%;" TYPE="text" value="<%=props.getProperty("p_address1","")%>">
                                    </INPUT>

                            </TD>
                        </TR>
                        <TR>
                            <TD align="left">&nbsp;</TD>
                            <TD align="left">

                                    <INPUT NAME="p_address2" style="border: none; font-size: 13px; text-decoration: underline; width: 100%;" TYPE="text" value="<%=props.getProperty("p_address2","")%>">
                                    </INPUT>

                            </TD>
                        </TR>
                        <TR>
                            <TD align="left">Phone:</TD>
                            <TD align="left">
                                <INPUT NAME="p_phone" style="border: none; font-size: 13px; text-decoration: underline; width: 100%;" TYPE="text" value="<%=props.getProperty("p_phone","")%>">
                                   </INPUT>

                            </TD>
                        </TR>
                        <TR>
                            <TD align="left">Birthdate:</TD>
                            <TD align="left">
                                <INPUT NAME="p_birthdate" style="border: none; font-size: 13px; text-decoration: underline; width: 100%;" TYPE="text" value="<%=props.getProperty("p_birthdate","")%>">
                                </INPUT>
                            </TD>
                        </TR>
                        <TR>
                            <TD align="left">Health Card No:</TD>
                            <TD align="left">
                                <INPUT NAME="p_healthcard" style="border: none; font-size: 13px; text-decoration: underline; width: 100%;" TYPE="text" value="<%=props.getProperty("p_healthcard","")%>">
                                </INPUT>
                            </TD>
                        </TR>
                    </TABLE>
                </TD>
            </TABLE>
        </center>
        <br>
                <div id="textDiv" style="visibility: hidden; font-size: 13px; font-family: arial, helvetica, sans-serif; align: left; position: absolute;">
                </div>
                <script type="text/javascript">
                document.forms[0].t_name.value = "<%=props.getProperty("t_name","")%>";
                document.forms[0].t_address.value = "<%=props.getProperty("t_address","")%>";
                document.forms[0].t_phone.value = "<%=props.getProperty("t_phone","")%>";
                document.forms[0].t_fax.value = "<%=props.getProperty("t_fax","")%>";
                </script>
<div id="textareaDiv" style="position: relative;">
    <textarea id="comments" name="comments" class="ta1" rows="60">
<%= props.getProperty("comments", "")%>
    </textarea>
</div>
        <div id="buttons">
            <input id="savebut" type="submit" value="Save" onclick="javascript: return onSave();" />
            <input id="saveexitbut" type="submit" value="Save and Exit" onclick="javascript: return onSaveExit();" />
            <input id="exitbut" type="submit" value="Exit" onclick="javascript: return onExit();" />
            <input id="printbut" type="submit" value="Print" onclick="javascript: return onPrint();"/>
        </div>
    </body>
    <style type="text/css">


        .tb1{
        border: 1px solid;
        width: 100%;
        }

        .tb2{
        width: 100%;
        border: 1px solid;
        }

        .ip{
        border: none;
        font-size: 13px;
        text-decoration: underline;
        font-family: arial, helvetica, sans-serif;
        width: 100%;
        }

        .ta1{
        width: 100%;
        align: center;
        font-size: 13px;
        color: black;
        border: 1px dotted;
        font-family: arial, helvetica, sans-serif;
        }
        .ta2{
        width: 100%;
        align: center;
        font-size: 13px;
        color: black;
        border: none;
        font-family: arial, helvetica, sans-serif;
        }

    </style>
    </font>


<script type="text/javascript">
      function onPrint() {
        document.forms[0].submit.value="save";
        setVisibility('buttons', 'hidden');
        setVisibility('textareaDiv', 'hidden');
        setVisibility('textDiv', 'visible');
        setVisibility('searching', 'hidden');
        setVisibility('dating', 'hidden');
        str1 = document.getElementById("comments").value;
        str2 = str1.replace(/\n/g, "<br>");


        document.getElementById("textDiv").innerHTML = str2;
        setStyle('textDiv', 'position', 'absolute');
        setStyle('textareaDiv', 'position', 'relative');
        window.print();

        if( ret = confirm("Do you wish to make changes?"))
        {
        setStyle('textareaDiv', 'position', 'absolute');
        setStyle('textDiv', 'position', 'absolute');
        setVisibility('textDiv', 'hidden');
        setVisibility('textareaDiv', 'visible');
        setVisibility('buttons', 'visible');
        setVisibility('dating', 'visible');
        setVisibility('searching', 'visible');
        }
        else{
        setVisibility('buttons', 'visible');
        }
        return(ret);
    }

    function onSave() {
        document.forms[0].submit.value="save";
        ret = confirm("Are you sure you want to save this form?");
        return ret;
    }
    function onExit() {
        if(confirm("Are you sure you wish to exit without saving your changes?")==true)
        {
            window.close();
        }
        return(false);
    }
    function onSaveExit() {
        document.forms[0].submit.value="exit";
            ret = confirm("Are you sure you wish to save and close this window?");
        return ret;
    }
    function setStyle(obj,style,value){
	getRef(obj).style[style]= value;
    }

    function getRef(obj){
	return (typeof obj == "string") ?
            document.getElementById(obj) : obj;
    }
    function setClassName(objId, className) {
    	document.getElementById(objId).className = className;
    }
    function setVisibility(objId, sVisibility) {
        var obj = document.getElementById(objId);
        obj.style.visibility = sVisibility;
    }

function countLines(strtocount, cols) {
    var hard_lines = 1;
    var last = 0;
    while ( true ) {
        last = strtocount.indexOf("\n", last+1);
        hard_lines ++;
        if ( last == -1 ) break;
    }
    var soft_lines = Math.round(strtocount.length / (cols-1));
    var hard = eval("hard_lines  " + unescape("%3e") + "soft_lines;");
    if ( hard ) soft_lines = hard_lines;
    return soft_lines;
}
function cleanForm() {
    var the_form = document.forms[0];
    for ( var x in the_form ) {
        if ( ! the_form[x] ) continue;
        if( typeof the_form[x].rows != "number" ) continue;
        the_form[x].rows = countLines(the_form[x].value,the_form[x].cols) +1;
    }
    setTimeout("cleanForm();", 300);
}
function rs(n,u,w,h,x) {
  args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
  remote=window.open(u,n,args);
}
function search(billno, toname, toaddress, tophone, tofax) {
     t0 = escape("document.forms[0].elements[\'"+billno+"\'].value");
     t2 = escape("document.forms[0].elements[\'"+toname+"\'].value");
     t3 = escape("document.forms[0].elements[\'"+toaddress+"\'].value");
     t5 = escape("document.forms[0].elements[\'"+tophone+"\'].value");
     t6 = escape("document.forms[0].elements[\'"+tofax+"\'].value");
     //rs('att',('../billing/CA/ON/searchRefDoc.jsp?param='+t0+'&toname='+t2),600,600,1);
     rs('att',('../billing/CA/ON/searchRefDoc.jsp?param='+t0+'&toname='+t2+'&toaddress1='+t3+'&tophone='+t5+'&tofax='+t6+'&submit=Search&keyword='+document.forms[0].elements[toname].value),600,600,1);
}

function start(){

}
</script>
    <script language='javascript'>
       Calendar.setup({inputField:"consultTime",ifFormat:"%Y/%m/%d",showsTime:false,button:"hlSDate",singleClick:true,step:1});
   </script>
</html:form>
</html:html>
