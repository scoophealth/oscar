<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
<html>
<body>
	<table border="0" width="100%" cellspacing="1" cellpadding="2" id="Title">
		<tr>
			<td class="Header1">
				<xsl:value-of select="REPORT/REPORT-DTL/REPORT-NAME"/>
			</td>
		</tr>
	</table>
	<P></P>

	<table border="0" width="100%" cellspacing="0" cellpadding="1" id="Report-ID" style="font-family:Arial;font-size:10pt">
		<tr>
			<td width="25%" align="left"  class="dataBlack">REPORT ID</td>
			<td  class="dataBlack"> : <xsl:value-of select="REPORT/REPORT-DTL/REPORT-ID"/></td>
		</tr>
		<tr>
			<td width="25%" align="left" valign="top"  class="dataBlack">REPORT DATE</td>
			<td  class="dataBlack"> : <xsl:value-of select="REPORT/REPORT-DTL/REPORT-DATE"/></td>
		</tr>
		<tr>
			<td width="25%" align="left" valign="top"  class="dataBlack">REPORT PERIOD</td>
			<td  class="dataBlack"> : <xsl:value-of select="REPORT/REPORT-DTL/REPORT-PERIOD-START"/> TO 
			<xsl:value-of select="REPORT/REPORT-DTL/REPORT-PERIOD-END"/></td>
		</tr>
	</table>

	<p></p>
	<table border="0" width="100%" cellspacing="0" cellpadding="1" id="Group-ID">
		<tr>
			<td width="25%" align="left" valign="top"  class="dataBlack">GROUP (<xsl:value-of select="REPORT/GROUP/GROUP-DTL/GROUP-TYPE"/>)</td>
			<td  class="dataBlack"> : <xsl:value-of select="REPORT/GROUP/GROUP-DTL/GROUP-ID"/> - <xsl:value-of select="REPORT/GROUP/GROUP-DTL/GROUP-NAME"/></td>
		</tr>
	</table>
	<p></p>
	
	<table border="0" width="100%" cellspacing="0" cellpadding="1" id="Provider-ID">
		<tr>
			<td width="25%" align="left" valign="top" class="dataBlack">PHYSICIAN</td>
			<td class="dataBlack"> : <xsl:value-of select="REPORT/GROUP/PROVIDER/PROVIDER-DTL/PROVIDER-NUMBER"/> - 
				<xsl:value-of select="REPORT/GROUP/PROVIDER/PROVIDER-DTL/PROVIDER-LAST-NAME"/>, 
				<xsl:value-of select="REPORT/GROUP/PROVIDER/PROVIDER-DTL/PROVIDER-FIRST-NAME"/> 
				<xsl:value-of select="REPORT/GROUP/PROVIDER/PROVIDER-DTL/PROVIDER-MIDDLE-NAME"/>
			</td>
		</tr>
	</table>
	<p></p>
	
	<table border="0" width="100%" cellspacing="0" cellpadding="2" ID="Member" >
		<tr>
	   		<th width="12%" align="left" valign="top">LAST NAME</th>
			<th width="12%" align="left" valign="top">FIRST NAME</th>
			<th width="10%" align="left" valign="top">HEALTH NUMBER</th>
			<th width="10%" align="left" valign="top">DATE OF BIRTH</th>
			<th width="5%" align="left" valign="top">SEX CODE</th>
			<th width="10%" align="left" valign="top">SERVICE DATE</th>
			<th width="6%" align="left" valign="top">FEE CODE</th>
			<th width="25%" align="left" valign="top">DESCRIPTION</th>
			<th width="10%" align="left" valign="top">FEE PAID</th>
	    </tr>
		<xsl:for-each select="REPORT/GROUP/PROVIDER/PATIENT">
			<xsl:for-each select="./SERVICE-DTL1">
	        	<tr>
		        	<td width="12%" align="left" valign="top" class="dataBlack"> <xsl:value-of select="../PATIENT-DTL/PATIENT-LAST-NAME"/></td>
		            <td width="12%" align="left" valign="top" class="dataBlack"> <xsl:value-of select="../PATIENT-DTL/PATIENT-FIRST-NAME"/></td>
		            <td width="10%" align="left" valign="top" class="dataBlack"> <xsl:value-of select="../PATIENT-DTL/PATIENT-HEALTH-NUMBER"/></td>
		            <td width="10%" align="left" valign="top" class="dataBlack"> <xsl:value-of select="../PATIENT-DTL/PATIENT-BIRTHDATE"/></td>
		            <td width="5%"  align="left" valign="top" class="dataBlack"> <xsl:value-of select="../PATIENT-DTL/PATIENT-SEX"/></td>
		            <td width="10%" align="left" valign="top" class="dataBlack"> <xsl:value-of select="./SERVICE-DATE"/></td>
		            <td width="6%"  align="left" valign="top" class="dataBlack"> <xsl:value-of select="./SERVICE-CODE"/></td>
		            <td width="25%" align="left" valign="top" class="dataBlack"> <xsl:value-of select="./SERVICE-DESCRIPTION"/></td>
		            <td width="10%" align="left" valign="top" class="dataBlack">$ <xsl:value-of select="./SERVICE-AMT"/></td>
				</tr>
			</xsl:for-each>
		</xsl:for-each>
	</table>
	<p></p>
</body>
</html>

</xsl:template>
</xsl:stylesheet>
