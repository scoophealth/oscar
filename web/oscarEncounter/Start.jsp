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

<html>
<head>
<title>Oscar Encounter Start</title>
<script language=javascript>
 function popupPage2() {
    windowprops = "height=700,width=960,location=no,"
    + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=50,left=50";
    window.open('Index2.jsp', "apptProviderSearch", windowprops);
}
</script>


</head>
<body>
<form name=Form1>
    <table>
        <tr><td>
            Demographic No:
        </td><td>
            <input type="text" name="demographicNo" value="10000032" />
        </td></tr>
        <tr><td>
            Provider No:
        </td><td>
            <input type="text" name="providerNo" value="174" />
        </td></tr>
    </table>

    <input type=button onclick="javascript:popupPage2();" value="Launch OscarEncounter" />
</form>
</body>
</html>
