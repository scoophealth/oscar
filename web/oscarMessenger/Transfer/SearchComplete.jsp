<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<html>
<head>
<title>
Search Complete
</title>

<script language="JavaScript">
<!--
function BackToOscar()
{
       window.close();
}
//-->
</script>
</head>

<body class="BodyStyle" vlink="#0000FF" >
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                oscarComm
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            Search Complete
                        </td>
                        <td  >

                        </td>
                        <td style="text-align:right">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  >Help</a> | <a href="javascript:popupStart(300,400,'About.jsp')" >About</a> | <a href="javascript:popupStart(300,400,'License.jsp')" >License</a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">
            &nbsp;
            </td>
            <td class="MainTableRightColumn">
                <%
                String conf = (String) request.getAttribute("confMessage");
                if (conf.equals("1")){  %>
                   This attachment has already been attached to this demographic

                <%}else{%>
                   Attachment has been attached to this demographic
                <%}%>
                <br>
                <a href="javascript:BackToOscar();">Click here</a> to close this window.
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">

            </td>
            <td class="MainTableBottomRowRightColumn">

            </td>
        </tr>
    </table>
</body>
</html>
