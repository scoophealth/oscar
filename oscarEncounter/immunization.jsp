<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<html:html locale="true">

<html>
<head>
<title>
    Immunization
</title>
<html:base/>

<script type="text/javascript" language="javascript">

function typeA {
    start = birthdate =
}




</script>




<!-- This is from OscarMessenger to get the top and left borders on -->
<link rel="stylesheet" type="text/css" href="styles.css">

<body topmargin="0" leftmargin="0" vlink="#0000FF">

<table border="0" cellpadding="0" cellspacing="0" width="100%" style="border-collapse: collapse" bordercolor="#111111" id="AutoNumber1" height="100%">
    <tr>
        <td width="100%" style="padding-left: 3; padding-right: 3; padding-top: 2; padding-bottom: 2" height="0%" colspan="2">
        <p class="HelpAboutLogout"><span class="FakeLink"><a href="Help.htm">Help</a></span> |
        <span class="FakeLink"><a href="About.htm">About</a></span> | <span class="FakeLink">
        <a href="Disclaimer.htm">Disclaimer</a></span></p>
        </td>
    </tr>
    <tr>
        <td width="10%" height="37" bgcolor="#000000">&nbsp;
        </td>
        <td width="100%" bgcolor="#000000" style="border-left: 2px solid #A9A9A9; padding-left: 5" height="0%">
            <p class="ScreenTitle">Immunization<bean:message key="application.title"/></p>
        </td>
    </tr>
    <tr>
        <td valign="top">
<!-- This is the left-hand column -->
            <table border=0 align="centre" width="100%">
            </table>
        </td>
        <!-- width="100%" -->
        <td  style="border-left: 2px solid #A9A9A9; " height="100%" valign="top">
            <table cellpadding="0" cellspacing="2" width="100%" height="100%" class="encounterTable">


<!----Start new rows here-->
                <tr valign="top" class="doctorInfo">
                    <td width=auto >
                        Doc Name
                    </td>
                    <td>
                        Date
                    </td>
                <tr class="patientInfo">
                    <td>
                        PatientName
                    </td>
                    <td>
                        &nbsp;
                    </td>
                </tr>
                <tr>
                <form name="selector">
                    <td>
                        &nbsp;<br>
                        Select Immunization Status of Patient:<br>
                        <input type="radio" name="a" value="A">A<br>
                        <input type="radio" name="b" value="B">B<br>
                        <input type="radio" name="c" value="C">C<br>
                    </td>
                    <td>
                        Select Special Medical concerns of patient:<br>
                        <input type="radio" name="diabetic" >diabetic<br>
                        <input type="radio" name="leukemia" >leukemia<br>
                    </td>
                </form>
                </tr>
                <tr>
                    <td>
                        &nbsp;
                    </td>
                </tr>
                <tr>
                    <td>
                        Next Immunization due is: <br>
                        type &nbsp;&nbsp;&nbsp;&nbsp; date <br>
                    </td>
                    <td>
                        Past Immunizations: <br>
                        chart of type &nbsp;&nbsp; date<br>
                    </td>
                </tr>

<!----End new rows here-->
            </table>
        </td>
    </tr>

	<tr>
    	<td height="0%" style="border-bottom:2px solid #A9A9A9; border-top:2px solid #A9A9A9; "></td>
    	<td height="0%" style="border-bottom:2px solid #A9A9A9; border-top:2px solid #A9A9A9; "></td>
  	</tr>
  	<tr>
    	<td width="100%" height="0%" colspan="2">&nbsp;</td>
  	</tr>
  	<tr>
    	<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC" colspan="2"></td>
  	</tr>
</table>



</body>
</html:html>
