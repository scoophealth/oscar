<html:html locale="true">

<head>
    <title>Late Life FDI: Disability component</title>
    <html:base/>
        <style type="text/css">
        a:link{
            text-decoration: none;
            color:#FFFFFF;
        }

        a:active{
            text-decoration: none;
            color:#FFFFFF;
        }

        a:visited{
            text-decoration: none;
            color:#FFFFFF;
        }

        a:hover{
            text-decoration: none;
            color:#FFFFFF;
        }

	.Head {
            background-color:#BBBBBB;
            padding-top:3px;
            padding-bottom:3px;            
            height: 30px;
            font-size:12pt;
        }

        .Head INPUT {
            width: 100px;
        }

        .Head A {
            font-size:12pt;
        }

        BODY {
            font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;             
            background-color: #F2F2F2;            
        }

        TABLE {
            font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;
        }
        
        TD{
            font-size:13pt;
        }

        TH{
            font-size:14pt;            
        }

        .leftcol{
            width="100%";            
            color: #A9A9A9;              
            font-weight: bold;
            text-align: Left;            
        }

        .checkbox{
            height: 25px;
            width: 25px;    
            border: 0px solid #A9A9A9;
            background-color: #FFFFFF;              
        }

        .checkboxError{
            height: 25px;
            width: 25px;   
            border: 0px solid #A9A9A9;
            background-color: red;                   
        }
        
        .instruction{
            font-size:75%;
        }
        
        .lefttopCell{
            background-color: #000000;
            color: #FFFFFF;  
            font-size: 15pt;
            font-weight: bold;
            text-align: centre;
            border-bottom:2px solid #A9A9A9;
            border-right:2px solid #A9A9A9;
        }
        
        .question{
            text-align: left;
            font-weight: bold;
            height="10px";
        }
        .row{
            border-bottom: 2px solid #F2F2F2;
        }
        
        .scoreHeavy{
            border-top: 2px solid #A9A9A9;
            text-align: center;
        }
        .scoreLight{
            border-top: 1px solid #A9A9A9;
            text-align: center;
        }
        
        .smallTable{
            border: 2px solid #F2F2F2;
        }
        .subject {
            background-color: #000000;
            color: #FFFFFF;  
            font-size: 15pt;
            font-weight: bold;
            text-align: centre;
            border-bottom:2px solid #A9A9A9
        }
        .subTitle {
            backgroud-color: #F2F2F2;
            font-weight: bold;
            font-style: italic;
            text-align: Left;          
        }        
        .textbox{
            border: 1px solid #A9A9A9;
        }

        .title {
            background-color: #486ebd;
            color: #FFFFFF;            
            font-weight: bold;
            text-align: center;
            height="20px"
        }

        .vertContent1 {
            font-family: "Century Gothic";
            background-color: #FF6600;
            writing-mode: tb-rl;
            filter:   flipH() flipV();
            font-size: 24px;
            color: #FFFFFF;
            font-weight: bolder;
        }
        
    </style>
</head>

<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0" onload="window.resizeTo(600,768)">
<!--
@oscar.formDB Table="formAdf" 
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'" 
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL" 
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL" 
@oscar.formDB Field="formEdited" Type="timestamp"  
-->
<table border="0" cellspacing="1" cellpadding="0" width="600px" height="95%">
<tr>    
    <td valign="top" colspan="2">
    <table border="0" cellspacing="0" cellpadding="0" width="600px" height="10%">
        <tr>            
            <th class="subject">
                Late Life FDI: Disability Component
            </th>
        </tr> 
    </table>
</td></tr>
<tr>    
    <td>
    <table border="0" cellspacing="0" cellpadding="0" height="100%" width="100%">
    <tr><td valign="top">       
    <table border="0" cellspacing="0" cellpadding="0" height="85%" width="100%" id="visualAid1">        
        <tr>        
            <td valign="top" colspan="2">
                <table width="100%" height="650px" border="0" cellspacing="1" cellpadding="2" >                                        
                    <tr class="title" >
                        <th colspan="5">
                            Disability Visual Aid #2
                        </th>
                    </tr> 
                    <tr>
                        <td class="question" colspan="5">To what extent do you feel limited in...?
                            <br>
                            <br>
                        </td>
                    </tr>                   
                    <tr bgcolor="white" height="150px">
                        <td width="11%" align="center" align="center">
                            <img src="graphics/disabilityVisualAid/notAtAll.JPG" align="center" border='0'/>
                        </td>
                        <td width="20%" align="center">
                            <img src="graphics/disabilityVisualAid/little.JPG" align="center" border='0'/>
                        </td>
                        <td width="25%" align="center">
                            <img src="graphics/disabilityVisualAid/somewhat.JPG" align="center" border='0'/>
                        </td>
                        <td width="30%" align="center">
                            <img src="graphics/disabilityVisualAid/aLot.JPG" align="center" border='0'/>
                        </td>
                        <td width="34%" align="center">
                            <img src="graphics/disabilityVisualAid/completely.JPG" align="center" border='0'/>
                        </td>
                    </tr>
                    <tr bgcolor="white" height="80px">
                        <td align="center" valign="center" width="10%">
                            No Limitations
                        </td>
                        <td align="center" valign="center" width="20%">
                            Slight Limitations
                        </td>
                        <td align="center" valign="center" width="25%">
                            Moderate Limitations
                        </td>
                        <td align="center" valign="center" width="31%">
                            Heavy Limitations
                        </td>
                        <td align="center" valign="center" width="34%">
                            Total Limitation
                            <br>Cannot do
                        </td>
                    </tr>
                    <tr><td>&nbsp;</td></tr>
                     <tr>
                        <td style="border:1px solid #000000" colspan="4">
                            <table>
                                <tr>
                                    <td width="3%"></td>
                                    <td>
                                        <font style="text-decoration:underline">Examples of limiting factors that may restrict you:</font>
                                        <br>
                                        <li>Mental or Physical Energy</li>
                                        <li>Too much effort</li>
                                        <li>Social and economic circumstances</li>
                                        <li>Transportation problems</li>
                                        <li>Accessibility issues</li>
                                        <li>Health</li>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr> 
                    <tr>
                        <td colspan="3">
                            <table height="8px">
                                <tr><td></td></tr>
                            </table>
                        </td>
                    </tr> 
                </table>              
            </td>
        </tr>        
    </table>
    <tr><td valign="top">
    <table class="Head" valign="bottom" width="100%" height="15%" id="copyRight">        
        <tr>
            <td><font style="font-size:70%">&copy; Copyright 2002 Trustees of Boston University, All Right Reserved</font></td>
        </tr>
    </table>
    </td></tr>
    </table>
</td></tr>
</table>
</body>
</html:html>
