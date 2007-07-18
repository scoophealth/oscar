<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<%
    String formClass = "Consultant";
    String formLink = "formConsultant.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId")); // If <= 0, it means a new form is created, otherwise its an old form that is being editted
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(demoNo, formId);
    FrmConsultantRecord rec1 = new FrmConsultantRecord();
    java.util.Properties choiceprops = rec1.getDoctors();   // Retrieve all doctors into choiceprops

    String doctor_name = rec1.getProvName(provNo);          // Retrieve the doctors name from provNo

    props.setProperty("formId", ""+formId);
    props.setProperty("provNo", ""+provNo);
    String initialbill;
    initialbill = props.getProperty("billingreferral_no","");   // If form exists, set initial billingreferral_no
    
    if (initialbill != "")
        choiceprops = rec1.getDocInfo(choiceprops, initialbill);    //If form exists, grab doctor info
        
    int i, k;
    String doctor, bill;
    String project_home = request.getContextPath().substring(1);
%>


<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Letterhead</title>
</head>
<body onload="cleanForm();">

        <html:form action="/form/formname">  <%//The action of the form is important.  Keep the same%>
        <input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
        <input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
        <input type="hidden" name="form_class" value="<%=formClass%>" />
        <input type="hidden" name="form_link" value="<%=formLink%>" />
        <input type="hidden" name="formId" value="<%=formId%>" />
        <input type="hidden" name="provider_no" value="<%=request.getParameter("provNo")%>" />
        <input type="hidden" name="submit" value="exit"/>
        <input type="hidden" name="billingreferral_no" value="<%=props.getProperty("billingreferral_no", "")%>" />
        <input type="hidden" name="doc_name" value="<%=doctor_name%>" />
        <input type="hidden" name="cl_name" value="<%=props.getProperty("cl_name","")%>" />
        <input type="hidden" name="cl_address1" value="<%=props.getProperty("cl_address1","")%>" />
        <input type="hidden" name="cl_address2" value="<%=props.getProperty("cl_address2","")%>" />
        <input type="hidden" name="cl_phone" value="<%=props.getProperty("cl_phone","")%>" />
        <input type="hidden" name="cl_fax" value="<%=props.getProperty("cl_fax","")%>" />
        <input type="hidden" name="project_home" value="<%=project_home%>" />
        <div style="font-size: 24px; font-family: arial, helvetica, sans-serif;">
            <center>
                <b><%=doctor_name%></b>
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
                               <INPUT name = "formCreated" style="border: none; font-size: 12px; text-decoration: underline; width: 90%;" TYPE="text" value="<%=props.getProperty("formCreated", "")%>"></INPUT>
                            </TD>
                        </TR>
                        <TR>
                            <TD align="left">To:</TD>
                            <TD align="left">
                                <DIV id="selectName" style="position: absolute;">
                                    <% if (formId <= 0) { %>
                                    <SELECT name ="toNames" ONCHANGE="docChoice(this.form.toNames.selectedIndex)">
                                        <OPTION SELECTED VALUE="none"> -- Choose a Doctor -- </OPTION>
                                        <% i = 0; k = Integer.parseInt(choiceprops.getProperty("n", ""));                                        
                                       
                                        doctor = "";
                                        while( k > 0 ){
                                            i++;
                                            k--;
                                            doctor = choiceprops.getProperty(i + "to_name","");
                                        %>
                                        <OPTION value="<%=doctor%>"><%=doctor%></OPTION>
                                        <% } %>
                                    </SELECT>
                                    <% } else { %>
                                    <SELECT name ="toNames" ONCHANGE="docChoice(this.form.toNames.selectedIndex)">
                                        <OPTION SELECTED value="initial"><%=choiceprops.getProperty("to_name","")%></OPTION>
                                        <% i = 0; k = Integer.parseInt(choiceprops.getProperty("n", ""));                                        
                                       
                                        doctor = "";
                                        bill = "";
                                        while( k > 0 ){
                                            i++;
                                            k--;
                                            doctor = choiceprops.getProperty(i + "to_name","");
                                        %>
                                        <OPTION value="<%=doctor%>"><%=doctor%></OPTION>
                                        <% } %>
                                    </SELECT>
                                    <% } %>
                                </DIV>
                                <DIV id="selectBill" style="visibility: hidden; position: absolute;">
                                    <SELECT name ="toBill">
                                        <OPTION SELECTED VALUE="none">&nbsp;</OPTION>
                                        <% i = 0; k = Integer.parseInt(choiceprops.getProperty("n", ""));                                        
                                       
                                        bill = "";
                                        while( k > 0 ){
                                            i++;
                                            k--;
                                            bill = choiceprops.getProperty(i + "billingreferral_no","");
                                        %>
                                        <OPTION value="<%=bill%>"></OPTION>
                                        <% } %>
                                    </SELECT>
                                </DIV>
                                <DIV id="selectAddress1" style="visibility: hidden; position: absolute;">
                                    <SELECT name ="toAddress1">
                                        <OPTION SELECTED VALUE="none">&nbsp;</OPTION>
                                        <% i = 0; k = Integer.parseInt(choiceprops.getProperty("n", ""));                                        
                                        
                                        String address1 = "";
                                        while( k > 0 ){
                                        i++;
                                        k--;
                                        address1 = choiceprops.getProperty(i + "to_address1","");
                                        %>
                                        <OPTION value="<%= address1%>" ><%=address1%></OPTION>
                                        <% } %>
                                    </SELECT>
                                </DIV>
                                <DIV id="selectAddress2" style="visibility: hidden; position: absolute;">
                                    <SELECT name ="toAddress2">
                                        <OPTION SELECTED VALUE="none">&nbsp;</OPTION>
                                        <% i = 0; k = Integer.parseInt(choiceprops.getProperty("n", ""));                                        
                                       
                                        String address2 = "";
                                        while( k > 0 ){
                                            i++;
                                            k--;
                                            address2 = choiceprops.getProperty(i + "to_address2","");
                                        %>
                                        <OPTION value="<%= address2%>"></OPTION>
                                        <% } %>
                                    </SELECT>
                                </DIV>
                                <DIV id="selectPhone" style="visibility: hidden; position: absolute;">
                                    <SELECT name ="toPhone">
                                        <OPTION SELECTED VALUE="none">&nbsp;</OPTION>
                                        <% i = 0; k = Integer.parseInt(choiceprops.getProperty("n", ""));                                        
                                       
                                        String phone = "";
                                        while( k > 0 ){
                                            i++;
                                            k--;
                                            phone = choiceprops.getProperty(i + "to_phone","");
                                        %>
                                        <OPTION value="<%= phone%>"></OPTION>
                                        <% } %>
                                    </SELECT>
                                </DIV>
                                <DIV id="selectFax" style="visibility: hidden; position: absolute;">
                                    <SELECT name ="toFax">
                                        <OPTION SELECTED VALUE="none">&nbsp;</OPTION>
                                        <% i = 0; k = Integer.parseInt(choiceprops.getProperty("n", ""));                                        
                                       
                                        String fax = "";
                                        while( k > 0 ){
                                            i++;
                                            k--;
                                            fax = choiceprops.getProperty(i + "to_fax","");
                                        %>
                                        <OPTION value="<%= fax%>"></OPTION>
                                        <% } %>
                                    </SELECT>
                                </DIV>
                                <DIV id="chosenName" style="visibility: hidden;">
                                    <INPUT name="t_name" style="border: none; font-size: 13px; text-decoration: underline; width: 90%; " type="text">&nbsp;</INPUT>
                                </DIV>
                                
                            </TD>
                        </TR>
                        <TR>
                                <TD align="left">Address:</TD>
                                <TD align="left">
                                    
                                    <INPUT name = "t_address1" style="border: none; font-size: 13px; text-decoration: underline; width: 90%;" TYPE="text" >&nbsp;</INPUT>
                                    
                                </TD>
                            </TR>
                            <TR>
                                <TD align="left">&nbsp;</TD>
                                <TD align="left">
                                    <INPUT name = "t_address2" style="border: none; font-size: 13px; text-decoration: underline; width: 90%;" TYPE="text" >&nbsp;</INPUT>
                                    
                                </TD>
                            </TR>
                            <TR>
                                <TD align="left">Phone:</TD>
                                <TD align="left">
                                    
                                    <INPUT name = "t_phone" style="border: none; font-size: 13px; text-decoration: underline; width: 90%;" TYPE="text">&nbsp;</INPUT>
                                    
                                </TD>
                            </TR>
                            <TR>			
                                <TD align="left">Fax:</TD>
                                <TD align="left">
                                    
                                    <INPUT name = "t_fax" style="border: none; font-size: 13px; text-decoration: underline; width: 90%;" TYPE="text">&nbsp;</INPUT>
                                    
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
                <% if (formId <= 0){ %>
                <div id="textareaDiv" style="position: relative;">
                    <textarea id="comments" name="comments" class="ta1" rows="60">


Sincerely,

<%=doctor_name%>
                    </textarea>
                </div>
                <%} else {%>
                <script type="text/javascript">
                document.forms[0].t_name.value = "<%=choiceprops.getProperty("to_name","")%>";
                document.forms[0].t_address1.value = "<%=choiceprops.getProperty("to_address1","")%>";
                document.forms[0].t_address2.value = "<%=choiceprops.getProperty("to_address2","")%>";
                document.forms[0].t_phone.value = "<%=choiceprops.getProperty("to_phone","")%>";
                document.forms[0].t_fax.value = "<%=choiceprops.getProperty("to_fax","")%>";
                </script>
<div id="textareaDiv" style="position: relative;">
    <textarea id="comments" name="comments" class="ta1" rows="60">
<%= props.getProperty("comments", "")%>
    </textarea>
</div>
                
                <%}%>
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
        setVisibility('chosenName', 'visible');
        setVisibility('selectName', 'hidden');
        setVisibility('buttons', 'hidden');
        setVisibility('textareaDiv', 'hidden');
        setVisibility('textDiv', 'visible');
        str1 = document.getElementById("comments").value;
        str2 = str1.replace(/\n/g, "<br>");
        
        
        document.getElementById("textDiv").innerHTML = str2;
        setStyle('textDiv', 'position', 'relative');
        setStyle('textareaDiv', 'position', 'absolute');
        window.print();

        if( ret = confirm("Do you wish to make changes?"))
        {        
        setStyle('textareaDiv', 'position', 'relative');
        setStyle('textDiv', 'position', 'absolute');
        setVisibility('textDiv', 'hidden');
        setVisibility('textareaDiv', 'visible');
        setVisibility('chosenName', 'hidden');
        setVisibility('selectName', 'visible');
        setVisibility('buttons', 'visible');
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
function docChoice(index)
{
    document.forms[0].billingreferral_no.value = document.forms[0].toBill.options[index].value;
    document.forms[0].t_name.value = document.forms[0].toNames.options[index].value;
    document.forms[0].t_address1.value = document.forms[0].toAddress1.options[index].value;
    document.forms[0].t_address2.value = document.forms[0].toAddress2.options[index].value;
    document.forms[0].t_phone.value = document.forms[0].toPhone.options[index].value;
    document.forms[0].t_fax.value = document.forms[0].toFax.options[index].value;
 
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


</script>    
</html:form>
</html:html>
