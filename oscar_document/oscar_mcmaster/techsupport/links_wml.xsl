<?xml version="1.0"?>

<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">
<xsl:output method="xml" indent="yes"/>

<xsl:template match="links">
<xsl:text disable-output-escaping="yes">&lt;!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"&gt;</xsl:text>
<wml>
  <card id="card0" title="JSPInsider.com">
    <do type="prev" label="Back"><prev/></do>
    <p>
      <table columns="1">
        <xsl:apply-templates select="link"/>
      </table>
    </p>
  </card>
</wml>
</xsl:template>

<xsl:template match="link">
<tr>
  <td class="clear"></td>
</tr>
<tr>
  <td>
    <a xsl:use-attribute-sets="a"><xsl:value-of select="text"/></a><br />
  </td>
</tr>
<tr>
  <td>
    <xsl:value-of select="author"/><br />
  </td>
</tr>
<tr>
  <td>
    <xsl:value-of select="description"/><br />
  </td>
</tr>
</xsl:template>

<xsl:attribute-set name="a">
  <xsl:attribute name="href"><xsl:value-of select="url"/></xsl:attribute>
</xsl:attribute-set>

</xsl:stylesheet>
