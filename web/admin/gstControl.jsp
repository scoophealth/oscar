<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ page language="java" %>
<%@ page import="java.util.*,oscar.oscarReport.data.*, java.util.Properties, oscar.oscarBilling.ca.on.administration.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
    
<%

Properties props = new Properties();
GstControlAction db = new GstControlAction();
props = db.readDatabase();
String flag = props.getProperty("gstFlag");
String percent = props.getProperty("gstPercent");

%>
    
<script type="text/javascript">
    function submitcheck(){
        if( document.getElementById("gstCheck").checked == true ){
            document.getElementById("gstFlag").value = "checked";
            document.getElementById("gstPercent").value = extractNums(document.getElementById("gstPercent").value);
        } else {
            document.getElementById("gstFlag").value = "unchecked";
            document.getElementById("gstPercent").value = "";
        }
    }
    function extractNums(str){
        return str.replace(/\D/g, "");
    }

    function loadData(){
        if ( <%=flag%> == "1" ){
            document.getElementById("gstCheck").checked = true;
            document.getElementById("gstPercent").value = <%=percent%>;
        }
        else {
            document.getElementById("gstCheck").checked == false;
        }
    }

        
        function gstClick(){
            if ( document.getElementById("gstCheck").checked == false )
                document.getElementById("gstPercent").value = "";
        }
</script>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit GST</title>
    </head>
    <body style="font-family: arial, helvetica, sans-serif; font-size: 13px;" onload="loadData()">
        <html:form action="/admin/GstControl">
            <input type="hidden" name="gstFlag" value="checked" id="gstFlag"/>
            <TABLE>
                <TR>
                    <TD>Include GST:</TD>
                    <TD align="right"><input type="checkbox" id="gstCheck" onclick="gstClick()" /></TD>
                </TR>
                <TR>
                    <TD>GST :</TD>
                    <TD align="right"><input type="text" size="3" maxlength="3" id="gstPercent" name="gstPercent" />%</TD>
                </TR>
            </TABLE>
            <input type="submit" value="Submit" onclick="submitcheck()" />
        </html:form>
    </body>
</html:html>
