<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
<html>
<body> 

<table border="0" width="100%" cellspacing="1" cellpadding="2" id="Title">
<tr><td class="Header1"> 
	  <xsl:value-of select="REPORT/REPORT-DTL/REPORT-NAME"/>
</td></tr>
</table>

<p></p>
<table border="0" width="100%" cellspacing="1" cellpadding="1" id="Report-ID" >
	<tr>
	<td width="30%" align="left" class="dataBlack">REPORT ID</td>
	<td class="dataBlack"> : <xsl:value-of select="REPORT/REPORT-DTL/REPORT-ID"/></td>
	</tr>
	<tr>
	<td width="30%" align="left" class="dataBlack">REPORT DATE</td>
	<td class="dataBlack"> : <xsl:value-of select="REPORT/REPORT-DTL/REPORT-DATE"/></td>
	</tr>
	<tr>
	<td width="30%" align="left" class="dataBlack">REPORT AS OF</td>
	<td class="dataBlack"> : <xsl:value-of select="REPORT/REPORT-DTL/REPORT-AS-OF-DATE"/></td>
	</tr>
</table>

<p></p> 
<table border="0" width="100%" cellspacing="1" cellpadding="1" id="Group-ID" >
	<tr>
	<td width="30%" align="left" class="dataBlack">GROUP (<xsl:value-of select="REPORT/GROUP/GROUP-DTL/GROUP-TYPE"/>)</td><td class="dataBlack"> : <xsl:value-of select="REPORT/GROUP/GROUP-DTL/GROUP-ID"/> - <xsl:value-of select="REPORT/GROUP/GROUP-DTL/GROUP-NAME"/></td>
	</tr>
</table>
<p></p>

<table border="0" width="100%" cellspacing="1" cellpadding="1" ID="Provider-ID">
	<tr>
	<td width="30%" align="left" class="dataBlack">PHYSICIAN</td>
	<td class="dataBlack"> : <xsl:value-of select="REPORT/GROUP/PROVIDER/PROVIDER-DTL/PROVIDER-NUMBER"/> - 
	<xsl:value-of select="REPORT/GROUP/PROVIDER/PROVIDER-DTL/PROVIDER-LAST-NAME"/>, 
	<xsl:value-of select="REPORT/GROUP/PROVIDER/PROVIDER-DTL/PROVIDER-FIRST-NAME"/> 
	<xsl:value-of select="REPORT/GROUP/PROVIDER/PROVIDER-DTL/PROVIDER-MIDDLE-NAME"/></td>
	</tr>
</table>

<p></p>

<table border="0" width="100%" cellspacing="1" cellpadding="1" ID="Member">
   <xsl:for-each select="REPORT/GROUP/PROVIDER/MEMBER-DTL">
      <tr><td width="30%" align="left" class="dataBlack"><xsl:value-of select="MEMBER-TYPE"/></td>
		<td class="dataBlack"> : <xsl:value-of select="MEMBER-TYPE-TOTAL"/></td>
      </tr>
    </xsl:for-each>
	<tr><td width="30%" align="left" class="dataBlack">TOTAL MEMBERS</td>
      <td class="dataBlack"> : <xsl:value-of select="REPORT/GROUP/PROVIDER/TOTAL-DTL/MEMBER-TOTAL"/></td>
	</tr>
</table>

</body>
</html>
</xsl:template>
</xsl:stylesheet>
