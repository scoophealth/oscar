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
<head>
<title>oscarEncounter</title>
<html:base/>
<link rel="stylesheet" type="text/css" href="encounterStyles.css">

</head>

<body  onkeypress="javascript:handlePress(event);" topmargin="0" leftmargin="0" vlink="#0000FF">
<html:errors/>
<!--  -->
<div style="color:red">J session security ID <%=request.getRequestedSessionId()%></div>
<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse;width:100%;height:680" bordercolor="#111111" id="scrollNumber1" name="encounterTable">
    <tr>
        <td  bgcolor="#003399" style="border-left: 2px solid #A9A9A9;border-right: 2px solid #A9A9A9;height:34px;" >
            <div class="ScreenTitle">
                oscarTemplate
            </div>
        </td>


        <td  bgcolor="#003399" style="text-align:right;border-left: 2px solid #A9A9A9;height:34px;padding-left:3px;" >

            </td>
    </tr>
    <tr style="height:100%">
        <td style="font-size:80%;border-top:2px solid #A9A9A9;border-bottom:2px solid #A9A9A9;vertical-align:top">
        </td>
        <td style="border-left: 2px solid #A9A9A9;border-right: 2px solid #A9A9A9;border-bottom:2px solid #A9A9A9;" valign="top">
            <table  name="encounterTableRightCol" >
<!----Start new rows here-->
                <tr>
                    <td>
                    </td>
                </tr>
    <!-- social history row-->
                <tr>
                    <td>
                    </td>
                </tr>
    <!--encounter row-->
                <tr>
                    <td>
                    </td>
                </tr>
<!----End new rows here-->
            </table>
            </form>
        </td>
    </tr>
    <tr style="height:100%">
        <td>
        &nbsp;
        </td>
    </tr>
</table>
</body>
</html:html>

