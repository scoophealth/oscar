<?xml version="1.0"?>
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">
<xsl:output method="xml" indent="yes"/>

<xsl:template match="links">
<links>
  <xsl:apply-templates select="link"/>
</links>
</xsl:template>

<xsl:template match="link">
  <url>
  <xsl:value-of select="url"/>
  </url>
</xsl:template>

</xsl:stylesheet>
