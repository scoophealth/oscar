<?xml version="1.0"?>
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">
<xsl:output method="html" indent="yes"/>

<xsl:template match="maintenances">
<html>
<head>
<title>oscar Maintenance </title>
</head>
<body>
<table width="900" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td><div align="center"></div></td>
    <td><div align="center"></div></td>
    <td colspan="4" bgcolor="#F3EDDE"><div align="center"><strong><font size="1" face="Tahoma">BACKUP</font></strong></div></td>
    <td colspan="3" bgcolor="#E4F2D0"><div align="center"><strong><font size="1" face="Tahoma">SECURITY</font></strong></div></td>
    <td colspan="5" bgcolor="#D9DDEC"><div align="center"><strong><font size="1" face="Tahoma">SERVER</font></strong></div></td>
  </tr>
  <tr> 
    <td><div align="center"><strong><font size="1" face="Tahoma">Date</font></strong></div></td>
    <td><div align="center"><strong><font size="1" face="Tahoma">By</font></strong></div></td>
    <td bgcolor="#F3EDDE"><div align="center"><strong><font size="1" face="Tahoma">DailyBak</font></strong></div></td>
    <td bgcolor="#F3EDDE"><div align="center"><strong><font size="1" face="Tahoma">Remote</font></strong></div></td>
    <td bgcolor="#F3EDDE"><div align="center"><strong><font size="1" face="Tahoma">Disaster</font></strong></div></td>
    <td bgcolor="#F3EDDE"><div align="center"><strong><font size="1" face="Tahoma">Notes</font></strong></div></td>
    <td bgcolor="#E4F2D0"><div align="center"><strong><font size="1" face="Tahoma">Access</font></strong></div></td>
    <td bgcolor="#E4F2D0"><div align="center"><strong><font size="1" face="Tahoma">Login 
        Audit</font></strong></div></td>
    <td bgcolor="#E4F2D0"><div align="center"><strong><font size="1" face="Tahoma">Notes</font></strong></div></td>
    <td bgcolor="#D9DDEC"><div align="center"><strong><font size="1" face="Tahoma">Password</font></strong></div></td>
    <td bgcolor="#D9DDEC"><div align="center"><strong><font size="1" face="Tahoma">Port</font></strong></div></td>
    <td bgcolor="#D9DDEC"><div align="center"><strong><font size="1" face="Tahoma">Space</font></strong></div></td>
    <td bgcolor="#D9DDEC"><div align="center"><strong><font size="1" face="Tahoma">Memory</font></strong></div></td>
    <td bgcolor="#D9DDEC"><div align="center"><strong><font size="1" face="Tahoma">Notes</font></strong></div></td>
  </tr>
          <xsl:apply-templates select="maintenance"/>
</table>

 </body>
</html>
</xsl:template>

<xsl:template match="maintenance">
  <tr> 
    <td><div align="center"><font size="1" face="Tahoma"><xsl:value-of select="servicedate"/></font></div></td>
    <td><div align="center"><font size="1" face="Tahoma"><xsl:value-of select="servicedby"/></font></div></td>
    <td bgcolor="#F3EDDE"><div align="center"><font size="1" face="Tahoma"><xsl:value-of select="wbackup"/></font></div></td>
    <td bgcolor="#F3EDDE"><div align="center"><font size="1" face="Tahoma"><xsl:value-of select="remotebackup"/></font></div></td>
    <td bgcolor="#F3EDDE"><div align="center"><font size="1" face="Tahoma"><xsl:value-of select="disaster"/></font></div></td>
    <td bgcolor="#F3EDDE"><div align="center"><font size="1" face="Tahoma"><xsl:value-of select="backupnotes"/></font></div></td>
    <td bgcolor="#E4F2D0"><div align="center"><font size="1" face="Tahoma"><xsl:value-of select="waccess"/></font></div></td>
    <td bgcolor="#E4F2D0"><div align="center"><font size="1" face="Tahoma"><xsl:value-of select="wlogin"/></font></div></td>
    <td bgcolor="#E4F2D0"><div align="center"><font size="1" face="Tahoma"><xsl:value-of select="securitynotes"/></font></div></td>
    <td bgcolor="#D9DDEC"><div align="center"><font size="1" face="Tahoma"><xsl:value-of select="wpassword"/></font></div></td>
    <td bgcolor="#D9DDEC"><div align="center"><font size="1" face="Tahoma"><xsl:value-of select="port"/></font></div></td>
    <td bgcolor="#D9DDEC"><div align="center"><font size="1" face="Tahoma"><xsl:value-of select="space"/></font></div></td>
    <td bgcolor="#D9DDEC"><div align="center"><font size="1" face="Tahoma"><xsl:value-of select="memory"/></font></div></td>
    <td bgcolor="#D9DDEC"><div align="center"><font size="1" face="Tahoma"><xsl:value-of select="servernotes"/></font></div></td>
  </tr>
</xsl:template>

</xsl:stylesheet>