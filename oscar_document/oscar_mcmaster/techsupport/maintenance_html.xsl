<?xml version="1.0"?>
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">
<xsl:output method="html" indent="yes"/>

<xsl:template match="maintenances">
<html>
<head>
<title>oscar Maintenance </title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body class="BodyStyle" vlink="#0000FF" >
<font size="2" face="Tahoma"> 
<!--  -->
</font> 
<table width="100%"  class="MainTable" id="scrollNumber1" name="encounterTable">
  <tr class="MainTableTopRow"> 
    <td width="174" class="MainTableTopRowLeftColumn"><font size="2" face="Tahoma"> oscar 
      Maintenance Display </font></td>
    <td width="414" class="MainTableTopRowRightColumn">&nbsp; </td>
  </tr>
</table>

  
<table width="900" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td colspan="4">BACKUP</td>
    <td colspan="3">SECURITY</td>
    <td colspan="5">SERVER</td>
  </tr>
  <tr> 
    <td>Date</td>
    <td>By</td>
    <td>DailyBak</td>
    <td>Remote</td>
    <td>Disaster</td>
    <td>Notes</td>
    <td>Access</td>
    <td>Login Audit</td>
    <td>Notes</td>
    <td>Password</td>
    <td>Port</td>
    <td>Space</td>
    <td>Memory</td>
    <td>Notes</td>
  </tr>
        <xsl:apply-templates select="maintenance"/>
</table>

<p> <font size="2" face="Tahoma"> </font></p>

        </body>
</html>


</xsl:template>

<xsl:template match="maintenance">

  <tr> 
    <td><xsl:value-of select="servicedate"/></td>


          <td> <xsl:value-of select="servicedby"/></td>
          <td><xsl:value-of select="wbackup"/></td>
          <td><xsl:value-of select="remotebackup"/></td>
          <td><xsl:value-of select="disaster"/></td>
          <td><xsl:value-of select="backupnotes"/></td>  
          <td><xsl:value-of select="access"/></td>
          <td><xsl:value-of select="login"/></td>
          <td><xsl:value-of select="securitynotes"/></td>     
          <td><xsl:value-of select="password"/></td>
          <td><xsl:value-of select="port"/></td>
          <td><xsl:value-of select="space"/></td>
          <td><xsl:value-of select="memory"/></td>
          <td><xsl:value-of select="servernotes"/></td>
        </tr>
       
</xsl:template>
</xsl:stylesheet>