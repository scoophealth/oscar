<?xml version="1.0"?>
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">
<xsl:output method="html" indent="yes"/>

<xsl:template match="links">
<HTML>
  <HEAD>
    <TITLE>Fun with XSL</TITLE>
  </HEAD>
  <BODY>
    <table cellspacing="0" width="100%">
      <tr>
        <td class="clear" align="center">
        <table width="95%">
        <xsl:apply-templates select="link"/>
        </table>
        </td>
      </tr>
    </table>
  </BODY>
</HTML>
</xsl:template>

<xsl:template match="link">
   <tr>
    <td class="clear">
     <table cellpadding="0" cellspacing="0" width="100%">
      <tr>
       <td style=" font-family: verdana; font-size: 10pt; background: #aabbbb; border-style: groove; border-width:2px; border-color:#ffffff; padding:2px;">

       <A xsl:use-attribute-sets="a"><xsl:value-of select="text"/></A><br />
       </td>
      </tr>
      <tr>
       <td style=" font-family: verdana; font-size: 10pt; border-style: groove; border-width:2px; border-color:#ffffff; padding:2px; background: #f5f5f5; border-top-width:0px; padding-left:10px; padding-top:0px; margin-top:0px;">
       <xsl:value-of select="author"/><br />
       </td>
      </tr>
      <tr>
       <td style=" font-family: verdana; font-size: 10pt; border-style: groove; border-width:2px; border-color:#ffffff; padding:2px; background: #eeeeee; border-top-width:0px; padding-left:10px; padding-top:0px; margin-top:0px;">

       <xsl:value-of select="description"/><br />
       </td>
      </tr>
     </table>
    </td>
   </tr>
</xsl:template>

<xsl:attribute-set name="a">
  <xsl:attribute name="href"><xsl:value-of select="url"/></xsl:attribute>
</xsl:attribute-set>

</xsl:stylesheet>