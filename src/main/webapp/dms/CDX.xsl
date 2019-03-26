<!--
<?altova_samplexml XSLOutput.xml ?>
  Title: CDA XSL StyleSheet
  Original Filename: cda.xsl
  Version: 4.0-BC-FINAL

  Revision History: 08/12/08 Jingdong Li updated
  Revision History: 12/11/09 KH updated
  Revision History:  03/30/10 Jingdong Li updated.
  Revision History:  08/25/10 Jingdong Li updated
  Revision History:  09/17/10 Jingdong Li updated
  Revision History:  01/05/11 Jingdong Li updated
  2012-2013: revised for CA-BC use by Alan Bruce

  2013-08-02: Final Revision by Alan Bruce for the CDX project

  2016-05-09: Removed Lab inquiries and confidential disclaimer, as it's moved into the CDA itself now.

  2016-05-30:

  Specification: ANSI/HL7 CDAR2
  The current version and documentation are available at http://www.lantanagroup.com/resources/tools/.
  We welcome feedback and contributions to tools@lantanagroup.com
  The stylesheet is the cumulative work of several developers; the most significant prior milestones were the foundation work from HL7
  Germany and Finland (Tyylitiedosto) and HL7 US (Calvin Beebe), and the presentation approach from Tony Schaller, medshare GmbH provided at IHIC 2009.
-->
<!-- LICENSE INFORMATION
  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
  You may obtain a copy of the License at  http://www.apache.org/licenses/LICENSE-2.0
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:n1="urn:hl7-org:v3" xmlns:lab="urn:oid:1.3.6.1.4.1.19376.1.3.2" xmlns:bccda="urn:bccda" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0">
    <xsl:output method="html" indent="yes" version="4.01" encoding="ISO-8859-1" doctype-system="http://www.w3.org/TR/html4/strict.dtd" doctype-public="-//W3C//DTD HTML 4.01//EN" />

    <xsl:variable name="patientName">
        <xsl:call-template name="show-name">
            <xsl:with-param name="name" select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:name" />
        </xsl:call-template>
    </xsl:variable>

    <xsl:variable name="patientPhn">
        <xsl:if test="normalize-space(/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:id/@root='2.16.840.1.113883.4.50')">
            <xsl:value-of select="normalize-space(/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:id[@root='2.16.840.1.113883.4.50']/@extension)" />
        </xsl:if>
    </xsl:variable>

    <!-- global variable title -->
    <xsl:variable name="title">
        <xsl:choose>
            <xsl:when test="string-length(/n1:ClinicalDocument/n1:title)  &gt;= 1">
                <xsl:value-of select="/n1:ClinicalDocument/n1:title" />
            </xsl:when>
            <xsl:when test="/n1:ClinicalDocument/n1:code/@displayName">
                <xsl:value-of select="/n1:ClinicalDocument/n1:code/@displayName" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>Clinical Document</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    <xsl:key name="byUseCode" match="n1:addr|n1:telecom" use="@use" />

    <!-- Main -->
    <xsl:template match="/">
        <xsl:apply-templates select="n1:ClinicalDocument" />
    </xsl:template>

    <!-- produce browser rendered, human readable clinical document -->
    <xsl:template match="n1:ClinicalDocument">
        <html>
            <head>
                <xsl:comment> Do NOT edit this HTML directly: it was generated via an XSLT transformation from a CDA Release 2 XML document. </xsl:comment>
                <title>
                    <xsl:value-of select="$patientName" />
                    <xsl:text> - </xsl:text>
                    <xsl:value-of select="$patientPhn" />
                    <xsl:text> - </xsl:text>
                    <xsl:value-of select="$title" />
                </title>
                <!-- Moved styling out of this document and into a separate CSS document: Alan Bruce, 2012-01-20 -->
                <!-- link rel="stylesheet" type="text/css" href="CDA.css"/ -->
                <!-- Moved styling back into this document: Alan Bruce, 2013-07-26 -->
                <style type="text/css">
                    body {
                    /* black */
                    color: black;
                    background-color: #ffffff;
                    font-family: Verdana, Tahoma, sans-serif;
                    font-size: 11px;
                    }

                    a {
                    /*   */
                    color: #336699;
                    background-color: #FFFFFF;
                    }

                    .header_table{
                    /*  */
                    border: 1px solid;
                    border-color: #ffffff;
                    }

                    tr {
                    /*  */
                    background-color: #efefef;
                    }

                    td{
                    border: 1px solid #ffffff;
                    padding: 2px;
                    margin: 2px;
                    }

                    .narr_tr {
                    /*  */
                    background-color: #efefef;
                    }

                    .narr_th {
                    /*   */
                    background-color: #336699;
                    border: 1px solid;
                    border-color: #dddddd;
                    color: #ffffff;
                    }

                    .td_label{
                    font-weight: bold;
                    color: white;
                    }

                    .td_label_dark{
                    /*   */
                    background-color: #336699;
                    font-weight: bold;
                    color: white;
                    }

                    h1 {
                    font-size: 12pt;
                    font-weight: bold;
                    }

                    h2 {
                    font-size: 11pt;
                    font-weight: bold;
                    }

                    h3 {
                    font-size: 10pt;
                    font-weight: bold;
                    }

                    h4 {
                    font-size: 8pt;
                    font-weight: bold;
                    }

                    div {
                    width: 99%;
                    }

                    table {
                    border-collapse:collapse;
                    border: 1px solid;
                    border-color: #ffffff;
                    line-height: 10pt;
                    width: 100%;
                    }

                    pre {
                    white-space: pre-wrap;       /* css-3 */
                    white-space: -moz-pre-wrap;  /* Mozilla, since 1999 */
                    white-space: -pre-wrap;      /* Opera 4-6 */
                    white-space: -o-pre-wrap;    /* Opera 7 */
                    word-wrap: break-word;       /* Internet Explorer 5.5+ */
                    }

                    .h1center {
                    font-size: 12pt;
                    font-weight: bold;
                    text-align: center;
                    width: 100%;
                    }

                    .narr_table {
                    width: 100%;
                    }

                    .h3 {
                    font-size: 10pt;
                    font-weight: bold;
                    }

                    .h4 {
                    font-size: 8pt;
                    font-weight: bold;
                    }

                    .labelTitleCase {
                    text-transform: capitalize;
                    }

                    .alert {
                    font-weight: bold;
                    color: red;
                    }

                    .disclaimer {
                    text-align: center;
                    }

                    .footnote {
                    text-align: center;
                    }

                    .testName {
                    width: 33%;
                    }

                    .testTimeResulted {
                    width: 12%;
                    }

                    @media print {
                    * {
                    background-color: #ffffff;
                    }

                    body {
                    /* black */
                    color: black;
                    background-color: #ffffff;
                    font-family: monospace;
                    font-size: 8pt;
                    }

                    td {
                    border: 1px solid #808080;
                    }

                    .narr_th {
                    /*   */
                    background-color: #efefef;
                    border: 1px solid #808080;
                    color: #000000;
                    font-weight: bold;
                    }

                    .td_label_dark {
                    background-color: #EFEFEF;
                    font-weight: bold;
                    color: black;
                    }

                    td.td_label_dark {
                    border: 1px solid #808080;
                    }
                    }
                </style>
            </head>
            <body>
                <div>
                    <h1 class="h1center">
                        <xsl:value-of select="$title" />
                    </h1>
                </div>

                <!-- START display top portion of clinical document -->
                <table class="header_table">
                    <tbody>
                        <xsl:call-template name="recordTarget" />
                        <xsl:call-template name="documentationOf" />
                        <xsl:call-template name="componentof" />
                        <xsl:call-template name="participant" />
                        <xsl:call-template name="informant" />
                        <xsl:call-template name="informationRecipient" />
                        <xsl:call-template name="statusCode" />
                        <xsl:call-template name="author" />
                        <xsl:call-template name="authenticator" />
                        <xsl:call-template name="legalAuthenticator" />
                        <xsl:call-template name="dataEnterer" />
                        <xsl:call-template name="custodian" />
                        <xsl:call-template name="documentGeneral" />
                    </tbody>
                </table>
                <!-- END display top portion of clinical document -->

                <!-- Making the check for greater than 2 because there is now the disclaimer section for IHA/NH CDAs -->
                <xsl:if test="not(//n1:nonXMLBody)">
                    <xsl:if test="count(/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component[n1:section]) &gt; 2">
                        <xsl:call-template name="make-tableofcontents" />
                    </xsl:if>
                </xsl:if>
                <hr />

                <!-- produce human readable document content -->
                <xsl:apply-templates select="n1:component/n1:structuredBody|n1:component/n1:nonXMLBody" />
            </body>
        </html>
    </xsl:template>

    <!-- generate table of contents -->
    <xsl:template name="make-tableofcontents">
        <h2>
            <a name="toc">Table of Contents</a>
        </h2>
        <ul>
            <xsl:for-each select="n1:component/n1:structuredBody/n1:component/n1:section/n1:title">
                <li>
                    <a href="#{generate-id(.)}">
                        <xsl:value-of select="." />
                    </a>
                </li>
            </xsl:for-each>
        </ul>
    </xsl:template>

    <!-- header elements -->
    <xsl:template name="documentGeneral">
        <tr>
            <td width="15%" class="td_label_dark">
                <span class="td_label_dark">
                    <xsl:text>Document Created</xsl:text>
                </span>
            </td>
            <td class="td_header_data" colspan="3">
                <xsl:call-template name="show-time">
                    <xsl:with-param name="datetime" select="n1:effectiveTime" />
                </xsl:call-template>
            </td>
        </tr>
        <tr>
            <td width="15%" class="td_label_dark">
                <span class="td_label_dark">
                    <xsl:text>Document Id</xsl:text>
                </span>
            </td>
            <td class="td_header_data" colspan="3">
                <xsl:call-template name="show-id">
                    <xsl:with-param name="id" select="n1:id" />
                </xsl:call-template>
            </td>
        </tr>
        <xsl:if test="normalize-space(n1:documentationOf/n1:serviceEvent/bccda:statusCode/@code) != '' ">
            <tr>
                <td width="15%" class="td_label_dark">
                    <span class="td_label_dark">
                        <xsl:text>Document Status</xsl:text>
                    </span>
                </td>
                <td class="td_header_data" colspan="3">
                    <xsl:value-of select="normalize-space(n1:documentationOf/n1:serviceEvent/bccda:statusCode/@code)" />
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <!-- confidentiality -->
    <xsl:template name="confidentiality">
        <tr>
            <td width="15%" class="td_label_dark">
                <xsl:text>Confidentiality</xsl:text>
            </td>
            <td class="td_header_data" colspan="3">
                <xsl:choose>
                    <xsl:when test="n1:confidentialityCode/@code  = 'N'">
                        <xsl:text>Normal</xsl:text>
                    </xsl:when>
                    <xsl:when test="n1:confidentialityCode/@code  = 'R'">
                        <xsl:text>Restricted</xsl:text>
                    </xsl:when>
                    <xsl:when test="n1:confidentialityCode/@code  = 'V'">
                        <xsl:text>Very restricted</xsl:text>
                    </xsl:when>
                </xsl:choose>
                <xsl:if test="n1:confidentialityCode/n1:originalText">
                    <xsl:text />
                    <xsl:value-of select="n1:confidentialityCode/n1:originalText" />
                </xsl:if>
            </td>
        </tr>
    </xsl:template>

    <!-- author -->
    <xsl:template name="author">
        <xsl:if test="n1:author">
            <xsl:for-each select="n1:author/n1:assignedAuthor">
                <xsl:choose>
                    <xsl:when test="n1:assignedPerson/n1:name">
                        <tr>
                            <td width="15%" class="td_label_dark">
                                <span class="td_label_dark">
                                    <xsl:text>Author</xsl:text>
                                </span>
                            </td>
                            <td class="td_header_data" colspan="3">
                                <xsl:call-template name="show-name">
                                    <xsl:with-param name="name" select="n1:assignedPerson/n1:name" />
                                </xsl:call-template>
                                <xsl:if test="n1:representedOrganization">
                                    <xsl:text>,&#xA0;</xsl:text>
                                    <xsl:call-template name="show-name">
                                        <xsl:with-param name="name" select="n1:representedOrganization/n1:name" />
                                    </xsl:call-template>
                                </xsl:if>
                            </td>
                        </tr>
                    </xsl:when>
                    <xsl:when test="n1:assignedAuthoringDevice/n1:softwareName">
                        <tr>
                            <td width="15%" class="td_label_dark">
                                <span class="td_label_dark">
                                    <xsl:text>Authoring System</xsl:text>
                                </span>
                            </td>
                            <td  class="td_header_data" colspan="3">
                                <xsl:value-of select="n1:assignedAuthoringDevice/n1:softwareName" />
                                <xsl:call-template name="show-code">
                                    <xsl:with-param name="showCodeSystem" select="no" />
                                    <xsl:with-param name="code" select="n1:assignedAuthoringDevice/n1:softwareName" />
                                </xsl:call-template>
                            </td>
                        </tr>
                    </xsl:when>
                    <xsl:when test="n1:representedOrganization">
                        <xsl:call-template name="show-name">
                            <xsl:with-param name="name" select="n1:representedOrganization/n1:name" />
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:for-each select="n1:id">
                            <div>
                                <xsl:call-template name="show-id" />
                            </div>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:if test="n1:addr | n1:telecom">
                    <tr>
                        <td class="td_label_dark">
                            <span class="td_label_dark">
                                <xsl:text>Author Contact Info</xsl:text>
                            </span>
                        </td>
                        <td class="td_header_data" colspan="3">
                            <xsl:call-template name="show-contactInfo">
                                <xsl:with-param name="contact" select="." />
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>

    <!--  authenticator -->
    <xsl:template name="authenticator">
        <xsl:if test="n1:authenticator">
            <tr>
                <xsl:for-each select="n1:authenticator">
                    <tr>
                        <td width="15%" class="td_label_dark">
                            <span class="td_label_dark">
                                <xsl:text>Signed </xsl:text>
                            </span>
                        </td>
                        <td class="td_header_data" colspan="3">
                            <xsl:call-template name="show-name">
                                <xsl:with-param name="name" select="n1:assignedEntity/n1:assignedPerson/n1:name" />
                            </xsl:call-template>
                            <xsl:text> at </xsl:text>
                            <xsl:call-template name="show-time">
                                <xsl:with-param name="date" select="n1:time" />
                            </xsl:call-template>
                        </td>
                    </tr>
                    <xsl:if test="n1:assignedEntity/n1:addr | n1:assignedEntity/n1:telecom">
                        <tr>
                            <td class="td_label_dark">
                                <span class="td_label_dark">
                                    <xsl:text>Authenticator Contact Info</xsl:text>
                                </span>
                            </td>
                            <td  class="td_header_data" colspan="3">
                                <xsl:call-template name="show-contactInfo">
                                    <xsl:with-param name="contact" select="n1:assignedEntity" />
                                </xsl:call-template>
                            </td>
                        </tr>
                    </xsl:if>
                </xsl:for-each>
            </tr>
        </xsl:if>
    </xsl:template>

    <!-- legalAuthenticator -->
    <xsl:template name="legalAuthenticator">
        <xsl:if test="n1:legalAuthenticator">
            <table class="header_table">
                <tbody>
                    <tr>
                        <td width="15%" class="td_label_dark">
                            <span class="td_label_dark">
                                <xsl:text>Legal Authenticator</xsl:text>
                            </span>
                        </td>
                        <td class="td_header_data" colspan="3">
                            <xsl:call-template name="show-assignedEntity">
                                <xsl:with-param name="asgnEntity" select="n1:legalAuthenticator/n1:assignedEntity" />
                            </xsl:call-template>
                            <xsl:text />
                            <xsl:call-template name="show-sig">
                                <xsl:with-param name="sig" select="n1:legalAuthenticator/n1:signatureCode" />
                            </xsl:call-template>
                            <xsl:if test="n1:legalAuthenticator/n1:time/@value">
                                <xsl:text> at </xsl:text>
                                <xsl:call-template name="show-time">
                                    <xsl:with-param name="datetime" select="n1:legalAuthenticator/n1:time" />
                                </xsl:call-template>
                            </xsl:if>
                        </td>
                    </tr>
                    <xsl:if test="n1:legalAuthenticator/n1:assignedEntity/n1:addr | n1:legalAuthenticator/n1:assignedEntity/n1:telecom">
                        <tr>
                            <td class="td_label_dark">
                                <span class="td_label_dark">
                                    <xsl:text>Legal Authenticator Contact Info</xsl:text>
                                </span>
                            </td>
                            <td>
                                <xsl:call-template name="show-contactInfo">
                                    <xsl:with-param name="contact" select="n1:legalAuthenticator/n1:assignedEntity" />
                                </xsl:call-template>
                            </td>
                        </tr>
                    </xsl:if>
                </tbody>
            </table>
        </xsl:if>
    </xsl:template>

    <!-- dataEnterer -->
    <xsl:template name="dataEnterer">
        <xsl:if test="n1:dataEnterer">
            <tr>
                <td width="15%" class="td_label_dark">
                    <span class="td_label_dark">
                        <xsl:text>Entered by</xsl:text>
                    </span>
                </td>
                <td class="td_header_data" colspan="3">
                    <xsl:call-template name="show-assignedEntity">
                        <xsl:with-param name="asgnEntity" select="n1:dataEnterer/n1:assignedEntity" />
                    </xsl:call-template>
                </td>
            </tr>
            <xsl:if test="n1:dataEnterer/n1:assignedEntity/n1:addr | n1:dataEnterer/n1:assignedEntity/n1:telecom">
                <tr>
                    <td class="td_label_dark">
                        <span class="td_label_dark">
                            <xsl:text>Data Enterer Contact Info</xsl:text>
                        </span>
                    </td>
                    <td>
                        <xsl:call-template name="show-contactInfo">
                            <xsl:with-param name="contact" select="n1:dataEnterer/n1:assignedEntity" />
                        </xsl:call-template>
                    </td>
                </tr>
            </xsl:if>
        </xsl:if>
    </xsl:template>

    <!-- componentOf -->
    <xsl:template name="componentof">
        <xsl:if test="n1:componentOf">
            <xsl:for-each select="n1:componentOf/n1:encompassingEncounter">
                <!-- If there is no ID, we need to fill the void with a colspan. -->
                <xsl:variable name="colSpan">
                    <xsl:choose>
                        <xsl:when test="not(n1:code) or not(n1:id)">
                            <xsl:value-of select="3" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="1" />
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <tr>
                    <xsl:if test="n1:id">
                        <td width="15%" class="td_label_dark">
                            <span class="td_label_dark">
                                <xsl:text>Encounter Id</xsl:text>
                            </span>
                        </td>
                        <xsl:element name="td">
                            <xsl:attribute name="colspan">
                                <xsl:value-of select="$colSpan" />
                            </xsl:attribute>
                            <xsl:attribute name="width">
                                <xsl:text>30%</xsl:text>
                            </xsl:attribute>
                            <xsl:call-template name="show-id">
                                <xsl:with-param name="id" select="n1:id" />
                            </xsl:call-template>
                        </xsl:element>
                    </xsl:if>
                    <xsl:if test="n1:code">
                        <td width="15%" class="td_label_dark">
                            <span class="td_label_dark">
                                <xsl:text>Encounter Type</xsl:text>
                            </span>
                        </td>
                        <!--<td>
                          <xsl:call-template name="show-code">
                            <xsl:with-param name="code" select="n1:code" />
                          </xsl:call-template>
                        </td>-->
                        <xsl:element name="td">
                            <xsl:attribute name="colspan">
                                <xsl:value-of select="$colSpan" />
                            </xsl:attribute>
                            <xsl:attribute name="width">
                                <xsl:text>15%</xsl:text>
                            </xsl:attribute>
                            <xsl:call-template name="show-code">
                                <xsl:with-param name="code" select="n1:code" />
                            </xsl:call-template>
                        </xsl:element>
                    </xsl:if>
                </tr>
                <xsl:if test="n1:effectiveTime">
                    <tr>
                        <td width="15%" class="td_label_dark">
                            <span class="td_label_dark">
                                <xsl:text>Encounter Date(s)</xsl:text>
                            </span>
                        </td>
                        <xsl:element name="td">
                            <xsl:attribute name="colspan">
                                <!--<xsl:value-of select="$colSpan" />-->
                                <xsl:text>3</xsl:text>
                            </xsl:attribute>
                            <xsl:choose>
                                <xsl:when test="n1:effectiveTime/@value">
                                    <xsl:text>Admission/Registration: </xsl:text>
                                    <xsl:call-template name="show-time">
                                        <xsl:with-param name="datetime" select="n1:effectiveTime" />
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:when test="n1:effectiveTime/n1:low">
                                    <xsl:text>Admission: </xsl:text>
                                    <xsl:call-template name="show-time">
                                        <xsl:with-param name="datetime" select="n1:effectiveTime/n1:low" />
                                    </xsl:call-template>
                                    <xsl:if test="n1:effectiveTime/n1:high">
                                        <xsl:element name="br" />
                                        <xsl:text>Discharge: </xsl:text>
                                        <xsl:call-template name="show-time">
                                            <xsl:with-param name="datetime" select="n1:effectiveTime/n1:high" />
                                        </xsl:call-template>
                                    </xsl:if>
                                </xsl:when>
                            </xsl:choose>
                        </xsl:element>
                    </tr>
                </xsl:if>


                <xsl:if test="n1:location/n1:healthCareFacility">
                    <tr>
                        <td width="15%" class="td_label_dark">
                            <span class="td_label_dark">
                                <xsl:text>Encounter Location</xsl:text>
                            </span>
                        </td>
                        <td  class="td_header_data" colspan="3">
                            <xsl:if test="n1:location/n1:healthCareFacility/n1:location/n1:name">
                                <xsl:element name="div">
                                    <xsl:call-template name="show-name">
                                        <xsl:with-param name="name" select="n1:location/n1:healthCareFacility/n1:location/n1:name" />
                                    </xsl:call-template>
                                    <xsl:for-each select="n1:location/n1:healthCareFacility/n1:serviceProviderOrganization/n1:name">
                                        <xsl:text> of </xsl:text>
                                        <xsl:call-template name="show-name">
                                            <xsl:with-param name="name" select="n1:location/n1:healthCareFacility/n1:serviceProviderOrganization/n1:name" />
                                        </xsl:call-template>
                                    </xsl:for-each>
                                </xsl:element>
                            </xsl:if>
                            <xsl:if test="n1:location/n1:healthCareFacility/n1:code">
                                <xsl:element name="div">
                                    <xsl:call-template name="show-code">
                                        <xsl:with-param name="code" select="n1:location/n1:healthCareFacility/n1:code" />
                                    </xsl:call-template>
                                </xsl:element>
                            </xsl:if>
                            <xsl:if test="n1:location/n1:healthCareFacility/n1:id">
                                <xsl:element name="div">
                                    <xsl:text>id: </xsl:text>
                                    <xsl:for-each select="n1:location/n1:healthCareFacility/n1:id">
                                        <xsl:call-template name="show-id">
                                            <xsl:with-param name="id" select="." />
                                        </xsl:call-template>
                                    </xsl:for-each>
                                </xsl:element>
                            </xsl:if>
                        </td>
                    </tr>
                </xsl:if>
                <xsl:if test="n1:responsibleParty">
                    <tr>
                        <td width="15%" class="td_label_dark">
                            <span class="td_label_dark">
                                <xsl:text>Responsible party</xsl:text>
                            </span>
                        </td>
                        <td colspan="3">
                            <xsl:call-template name="show-assignedEntity">
                                <xsl:with-param name="asgnEntity" select="n1:responsibleParty/n1:assignedEntity" />
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <xsl:if test="n1:responsibleParty/n1:assignedEntity/n1:addr | n1:responsibleParty/n1:assignedEntity/n1:telecom">
                    <tr>
                        <td width="15%" class="td_label_dark">
                            <span class="td_label_dark">
                                <xsl:text>Contact Info</xsl:text>
                            </span>
                        </td>
                        <td class="td_header_data" colspan="3">
                            <xsl:call-template name="show-contactInfo">
                                <xsl:with-param name="contact" select="n1:responsibleParty/n1:assignedEntity" />
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>

    <!-- custodian -->
    <xsl:template name="custodian">
        <xsl:if test="n1:custodian">
            <tr>
                <td width="15%" class="td_label_dark">
                    <xsl:text>Document Maintained By</xsl:text>
                </td>
                <td class="td_header_data" colspan="3">

                    <xsl:for-each select="n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:id">
                        <div>
                            <xsl:call-template name="show-id" />
                            <xsl:text>&#xA0;</xsl:text>
                            <!--<xsl:if test="n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name">-->
                            <xsl:if test="../n1:name">
                                <xsl:text> - </xsl:text>
                                <xsl:call-template name="show-name">
                                    <xsl:with-param name="name" select="../n1:name" />
                                </xsl:call-template>
                                <xsl:text>&#xA0;</xsl:text>
                            </xsl:if>
                        </div>
                    </xsl:for-each>
                </td>
            </tr>
            <xsl:if test="n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:addr | n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:telecom">
                <tr>
                    <td class="td_label_dark">
                        <span class="td_label_dark">
                            <xsl:text>Maintainer Contact Info</xsl:text>
                        </span>
                    </td>
                    <td class="td_header_data" colspan="3">
                        <xsl:call-template name="show-contactInfo">
                            <xsl:with-param name="contact" select="n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization" />
                        </xsl:call-template>
                    </td>
                </tr>
            </xsl:if>
        </xsl:if>
    </xsl:template>

    <xsl:template name="statusCode">
        <xsl:if test="//lab:statusCode">
            <tr>
                <td width="15%" class="td_label_dark">
                    <span class="td_label_dark">
                        <xsl:text>Report Status</xsl:text>
                    </span>
                </td>
                <td class="td_header_data" colspan="3">
                    <xsl:text>ABORTED / CANCELED</xsl:text>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <!-- documentationOf -->
    <xsl:template name="documentationOf">
        <xsl:if test="n1:documentationOf">
            <xsl:for-each select="n1:documentationOf">
                <xsl:if test="n1:serviceEvent/@classCode and n1:serviceEvent/n1:code">
                    <xsl:variable name="displayName">
                        <xsl:call-template name="show-actClassCode">
                            <xsl:with-param name="clsCode" select="n1:serviceEvent/@classCode" />
                        </xsl:call-template>
                    </xsl:variable>
                    <!-- December 2013: Removed by Alan Bruce at the request of Cindie Robertson. -->
                    <!--
                           <xsl:if test="$displayName">
                          -->
                    <xsl:if test="n1:serviceEvent/n1:effectiveTime">
                        <tr>
                            <td width="15%" class="td_label_dark">
                                <span class="labelTitleCase td_label_dark">
                                    <!-- December 2013: Added by Alan Bruce at the request of Cindie Robertson. -->
                                    <xsl:text>Service Event</xsl:text>
                                    <!-- December 2013: Removed by Alan Bruce at the request of Cindie Robertson. -->
                                    <!--
                                                 <xsl:call-template name="firstCharCaseUp">
                                                  <xsl:with-param name="data" select="$displayName"/>
                                               </xsl:call-template>
                                  -->
                                </span>
                            </td>
                            <td  class="td_header_data" colspan="3">
                                <!-- December 2013: show-code enclosed by IF by Alan Bruce at the request of Cindie Robertson. -->
                                <xsl:if test="n1:serviceEvent/n1:code/@code">
                                    <div>
                                        <xsl:call-template name="show-code">
                                            <xsl:with-param name="code" select="n1:serviceEvent/n1:code" />
                                        </xsl:call-template>
                                    </div>
                                </xsl:if>
                                <div>
                                    <xsl:if test="n1:serviceEvent/n1:effectiveTime">
                                        <xsl:text>Service Event Date: </xsl:text>
                                        <xsl:choose>
                                            <xsl:when test="n1:serviceEvent/n1:effectiveTime/@value">
                                                <xsl:call-template name="show-time">
                                                    <xsl:with-param name="datetime" select="n1:serviceEvent/n1:effectiveTime" />
                                                </xsl:call-template>
                                            </xsl:when>
                                            <xsl:when test="n1:serviceEvent/n1:effectiveTime/n1:low">
                                                <xsl:text> from </xsl:text>
                                                <xsl:call-template name="show-time">
                                                    <xsl:with-param name="datetime" select="n1:serviceEvent/n1:effectiveTime/n1:low" />
                                                </xsl:call-template>
                                                <xsl:if test="n1:serviceEvent/n1:effectiveTime/n1:high">
                                                    <xsl:text> to </xsl:text>
                                                    <xsl:call-template name="show-time">
                                                        <xsl:with-param name="datetime" select="n1:serviceEvent/n1:effectiveTime/n1:high" />
                                                    </xsl:call-template>
                                                </xsl:if>
                                            </xsl:when>
                                        </xsl:choose>
                                    </xsl:if>
                                </div>
                            </td>
                        </tr>
                    </xsl:if>
                </xsl:if>
                <xsl:for-each select="n1:serviceEvent/n1:performer">
                    <xsl:variable name="displayName">
                        <xsl:call-template name="show-participationType">
                            <xsl:with-param name="ptype" select="@typeCode" />
                        </xsl:call-template>
                        <xsl:text />
                        <xsl:if test="n1:functionCode/@code">
                            <xsl:call-template name="show-participationFunction">
                                <xsl:with-param name="pFunction" select="n1:functionCode/@code" />
                            </xsl:call-template>
                        </xsl:if>
                    </xsl:variable>
                    <tr>
                        <td width="15%" class="td_label_dark">
                            <span class="td_label_dark">
                                <xsl:call-template name="firstCharCaseUp">
                                    <xsl:with-param name="data" select="$displayName" />
                                </xsl:call-template>
                            </span>
                        </td>
                        <td class="td_header_data" colspan="3">
                            <xsl:call-template name="show-assignedEntity">
                                <xsl:with-param name="asgnEntity" select="n1:assignedEntity" />
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:for-each>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>

    <!-- inFulfillmentOf -->
    <xsl:template name="inFulfillmentOf">
        <xsl:if test="n1:infulfillmentOf">
            <table class="header_table">
                <tbody>
                    <xsl:for-each select="n1:inFulfillmentOf">
                        <tr>
                            <td width="15%" class="td_label_dark">
                                <span class="td_label_dark">
                                    <xsl:text>In fulfillment of</xsl:text>
                                </span>
                            </td>
                            <td class="td_header_data" colspan="3">
                                <xsl:for-each select="n1:order">
                                    <xsl:for-each select="n1:id">
                                        <xsl:call-template name="show-id" />
                                    </xsl:for-each>
                                    <xsl:for-each select="n1:code">
                                        <xsl:text>&#xA0;</xsl:text>
                                        <xsl:call-template name="show-code">
                                            <xsl:with-param name="code" select="." />
                                        </xsl:call-template>
                                    </xsl:for-each>
                                    <xsl:for-each select="n1:priorityCode">
                                        <xsl:text />
                                        <xsl:call-template name="show-code">
                                            <xsl:with-param name="code" select="." />
                                        </xsl:call-template>
                                    </xsl:for-each>
                                </xsl:for-each>
                            </td>
                        </tr>
                    </xsl:for-each>
                </tbody>
            </table>
        </xsl:if>
    </xsl:template>

    <!-- informant -->
    <xsl:template name="informant">
        <xsl:if test="n1:informant">
            <xsl:for-each select="n1:informant">
                <tr>
                    <td width="15%" class="td_label_dark">
                        <span class="td_label_dark">
                            <xsl:text>Informant</xsl:text>
                        </span>
                    </td>
                    <td class="td_header_data" colspan="3">
                        <xsl:if test="n1:assignedEntity">
                            <xsl:call-template name="show-assignedEntity">
                                <xsl:with-param name="asgnEntity" select="n1:assignedEntity" />
                            </xsl:call-template>
                        </xsl:if>
                        <xsl:if test="n1:relatedEntity">
                            <xsl:call-template name="show-relatedEntity">
                                <xsl:with-param name="relatedEntity" select="n1:relatedEntity" />
                            </xsl:call-template>
                        </xsl:if>
                    </td>
                </tr>
                <xsl:choose>
                    <xsl:when test="n1:assignedEntity/n1:addr | n1:assignedEntity/n1:telecom">
                        <tr>
                            <td class="td_label_dark">
                                <span class="td_label_dark">
                                    <xsl:text>Informant Contact Info</xsl:text>
                                </span>
                            </td>
                            <td class="td_header_data" colspan="3">
                                <xsl:if test="n1:assignedEntity">
                                    <xsl:call-template name="show-contactInfo">
                                        <xsl:with-param name="contact" select="n1:assignedEntity" />
                                    </xsl:call-template>
                                </xsl:if>
                            </td>
                        </tr>
                    </xsl:when>
                    <xsl:when test="n1:relatedEntity/n1:addr | n1:relatedEntity/n1:telecom">
                        <tr>
                            <td class="td_label_dark">
                                <span class="td_label_dark">
                                    <xsl:text>Related Contact Info</xsl:text>
                                </span>
                            </td>
                            <td class="td_header_data" colspan="3">
                                <xsl:if test="n1:relatedEntity">
                                    <xsl:call-template name="show-contactInfo">
                                        <xsl:with-param name="contact" select="n1:relatedEntity" />
                                    </xsl:call-template>
                                </xsl:if>
                            </td>
                        </tr>
                    </xsl:when>
                </xsl:choose>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>

    <!-- informationRecipient -->
    <xsl:template name="informationRecipient">
        <xsl:if test="n1:informationRecipient">
            <xsl:for-each select="n1:informationRecipient">
                <tr>
                    <td width="15%" class="td_label_dark">
                        <span class="td_label_dark">
                            <xsl:call-template name="show-recipientType">
                                <xsl:with-param name="typeCode" select="@typeCode" />
                            </xsl:call-template>
                        </span>
                    </td>
                    <td class="td_header_data" colspan="3">
                        <xsl:choose>
                            <xsl:when test="n1:intendedRecipient/n1:informationRecipient/n1:name">
                                <xsl:for-each select="n1:intendedRecipient">
                                    <xsl:call-template name="show-name">
                                        <xsl:with-param name="name" select="n1:informationRecipient/n1:name" />
                                    </xsl:call-template>
                                    <xsl:for-each select="n1:id">
                                        <div>
                                            <xsl:text />
                                            <xsl:call-template name="show-id" />
                                        </div>
                                    </xsl:for-each>
                                </xsl:for-each>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:for-each select="n1:intendedRecipient">
                                    <xsl:for-each select="n1:id">
                                        <div>
                                            <xsl:text>&#xA0;</xsl:text>
                                            <xsl:call-template name="show-id" />
                                        </div>
                                    </xsl:for-each>
                                </xsl:for-each>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
                <xsl:if test="n1:intendedRecipient/n1:addr | n1:intendedRecipient/n1:telecom">
                    <tr>
                        <td class="td_label_dark">
                            <span class="td_label_dark">
                                <xsl:text>Recipient Contact Info</xsl:text>
                            </span>
                        </td>
                        <td class="td_header_data" colspan="3">
                            <xsl:call-template name="show-contactInfo">
                                <xsl:with-param name="contact" select="n1:intendedRecipient" />
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <xsl:if test="n1:intendedRecipient/n1:receivedOrganization">
                    <tr>
                        <td class="td_label_dark">
                            <span class="td_label_dark">
                                <xsl:text>Recipient Location:</xsl:text>
                            </span>
                        </td>
                        <td>
                            <xsl:if test="n1:intendedRecipient/n1:receivedOrganization/n1:name">
                                <xsl:call-template name="show-name">
                                    <xsl:with-param name="name" select="n1:intendedRecipient/n1:receivedOrganization/n1:name" />
                                </xsl:call-template>
                            </xsl:if>
                            <xsl:for-each select="n1:intendedRecipient/n1:receivedOrganization/n1:id">
                                <div>
                                    <xsl:text />
                                    <xsl:call-template name="show-id" />
                                </div>
                            </xsl:for-each>
                        </td>
                    </tr>
                </xsl:if>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>


    <!-- participant -->
    <xsl:template name="participant">
        <xsl:if test="n1:participant">
            <xsl:for-each select="n1:participant">
                <tr>
                    <td width="15%" class="td_label_dark">
                        <xsl:variable name="participtRole">
                            <xsl:call-template name="translateRoleAssoCode">
                                <xsl:with-param name="classCode" select="n1:associatedEntity/@classCode" />
                                <xsl:with-param name="code" select="n1:associatedEntity/n1:code" />
                            </xsl:call-template>
                        </xsl:variable>
                        <xsl:choose>
                            <xsl:when test="$participtRole">
                                <span class="labelTitleCase td_label_dark">
                                    <xsl:call-template name="firstCharCaseUp">
                                        <xsl:with-param name="data" select="$participtRole" />
                                    </xsl:call-template>
                                </span>
                            </xsl:when>
                            <xsl:otherwise>
                                <span class="td_label_dark">
                                    <xsl:text>Participant</xsl:text>
                                </span>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td class="td_header_data" colspan="3">
                        <xsl:call-template name="show-associatedEntity">
                            <xsl:with-param name="assoEntity" select="n1:associatedEntity" />
                        </xsl:call-template>
                        <xsl:if test="n1:functionCode">
                            <xsl:call-template name="show-code">
                                <xsl:with-param name="code" select="n1:functionCode" />
                                <xsl:with-param name="showCodeSystem" select="'no'" />
                            </xsl:call-template>
                        </xsl:if>
                        <xsl:if test="n1:time">
                            <div>
                                <xsl:choose>
                                    <xsl:when test="n1:time/@value">
                                        <xsl:call-template name="show-time">
                                            <xsl:with-param name="datetime" select="n1:time" />
                                        </xsl:call-template>
                                    </xsl:when>
                                    <xsl:when test="n1:time/n1:low">
                                        <xsl:call-template name="show-time">
                                            <xsl:with-param name="datetime" select="n1:time/n1:low" />
                                        </xsl:call-template>
                                        <xsl:if test="n1:time/n1:high">
                                            <xsl:text> to </xsl:text>
                                            <xsl:call-template name="show-time">
                                                <xsl:with-param name="datetime" select="n1:time/n1:high" />
                                            </xsl:call-template>
                                        </xsl:if>
                                    </xsl:when>
                                </xsl:choose>
                            </div>
                        </xsl:if>
                    </td>
                </tr>
                <xsl:if test="n1:associatedEntity/n1:addr | n1:associatedEntity/n1:telecom">
                    <tr>
                        <td class="td_label_dark">
                            <span class="td_label_dark">
                                <xsl:text>Participant Contact Info</xsl:text>
                            </span>
                        </td>
                        <td class="td_header_data" colspan="3">
                            <xsl:call-template name="show-contactInfo">
                                <xsl:with-param name="contact" select="n1:associatedEntity" />
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>

    <!-- recordTarget -->
    <xsl:template name="recordTarget">
        <xsl:for-each select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole">
            <tr>
                <td width="15%" class="td_label_dark">
                    <span class="td_label_dark">
                        <xsl:text>Patient</xsl:text>
                    </span>
                </td>
                <td colspan="3">
                    <div class="h3">
                        <xsl:call-template name="show-name">
                            <xsl:with-param name="name" select="n1:patient/n1:name" />
                        </xsl:call-template>
                    </div>
                </td>
            </tr>
            <tr>
                <td width="15%" class="td_label_dark">
                    <span class="td_label_dark">
                        <xsl:text>Date of Birth</xsl:text>
                    </span>
                </td>
                <td width="30%">
                    <div class="h4">
                        <xsl:call-template name="show-time">
                            <xsl:with-param name="datetime" select="n1:patient/n1:birthTime" />
                        </xsl:call-template>
                    </div>
                    <xsl:call-template name="calc-age">
                        <xsl:with-param name="birthdate" select="n1:patient/n1:birthTime/@value" />
                        <xsl:with-param name="currentDate" select="/n1:ClinicalDocument/n1:effectiveTime/@value" />
                    </xsl:call-template>
                </td>
                <td width="15%" class="td_label_dark">
                    <span class="td_label_dark">
                        <xsl:text>Sex</xsl:text>
                    </span>
                </td>
                <td>
                    <xsl:for-each select="n1:patient/n1:administrativeGenderCode">
                        <div class="h4">
                            <xsl:call-template name="show-gender" />
                        </div>
                    </xsl:for-each>
                </td>
            </tr>
            <xsl:if test="n1:patient/n1:raceCode | (n1:patient/n1:ethnicGroupCode)">
                <tr>
                    <td width="15%" class="td_label_dark">
                        <span class="td_label_dark">
                            <xsl:text>Race</xsl:text>
                        </span>
                    </td>
                    <td width="30%">
                        <xsl:choose>
                            <xsl:when test="n1:patient/n1:raceCode">
                                <xsl:for-each select="n1:patient/n1:raceCode">
                                    <xsl:call-template name="show-race-ethnicity" />
                                </xsl:for-each>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td width="15%" class="td_label_dark">
                        <span class="td_label_dark">
                            <xsl:text>Ethnicity</xsl:text>
                        </span>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="n1:patient/n1:ethnicGroupCode">
                                <xsl:for-each select="n1:patient/n1:ethnicGroupCode">
                                    <xsl:call-template name="show-race-ethnicity" />
                                </xsl:for-each>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Information not available</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:if>
            <tr>
                <td class="td_label_dark">
                    <span class="td_label_dark">
                        <xsl:text>Patient Contact Info</xsl:text>
                    </span>
                </td>
                <td>
                    <xsl:call-template name="show-contactInfo">
                        <xsl:with-param name="contact" select="." />
                    </xsl:call-template>
                </td>
                <td class="td_label_dark">
                    <span class="td_label_dark">
                        <xsl:text>Patient IDs</xsl:text>
                    </span>
                </td>
                <td>
                    <xsl:for-each select="n1:id[not(./@nullFlavor)]">
                        <div>
                            <xsl:call-template name="show-id" />
                        </div>
                    </xsl:for-each>
                </td>
            </tr>
        </xsl:for-each>
    </xsl:template>

    <!-- relatedDocument -->
    <xsl:template name="relatedDocument">
        <xsl:if test="n1:relatedDocument">
            <xsl:for-each select="n1:relatedDocument">
                <tr>
                    <td width="15%" class="td_label_dark">
                        <span class="td_label_dark">
                            <xsl:text>Related document</xsl:text>
                        </span>
                    </td>
                    <td class="td_header_data" colspan="3">
                        <xsl:for-each select="n1:parentDocument">
                            <xsl:for-each select="n1:id">
                                <div>
                                    <xsl:call-template name="show-id" />
                                </div>
                            </xsl:for-each>
                        </xsl:for-each>
                    </td>
                </tr>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>

    <!-- authorization (consent) -->
    <xsl:template name="authorization">
        <xsl:if test="n1:authorization">
            <xsl:for-each select="n1:authorization">
                <tr>
                    <td width="15%" class="td_label_dark">
                        <span class="td_label_dark">
                            <xsl:text>Consent</xsl:text>
                        </span>
                    </td>
                    <td class="td_header_data" colspan="3">
                        <div>
                            <xsl:choose>
                                <xsl:when test="n1:consent/n1:code">
                                    <xsl:call-template name="show-code">
                                        <xsl:with-param name="code" select="n1:consent/n1:code" />
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:call-template name="show-code">
                                        <xsl:with-param name="code" select="n1:consent/n1:statusCode" />
                                    </xsl:call-template>
                                </xsl:otherwise>
                            </xsl:choose>
                        </div>
                    </td>
                </tr>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>

    <!-- setAndVersion -->
    <xsl:template name="setAndVersion">
        <xsl:if test="n1:setId and n1:versionNumber">
            <tr>
                <td width="15%">
                    <xsl:text>SetId and Version</xsl:text>
                </td>
                <td class="td_header_data" colspan="3">
                    <xsl:text>SetId: </xsl:text>
                    <xsl:call-template name="show-id">
                        <xsl:with-param name="id" select="n1:setId" />
                    </xsl:call-template>
                    <xsl:text>  Version: </xsl:text>
                    <xsl:value-of select="n1:versionNumber/@value" />
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <!-- show StructuredBody  -->
    <xsl:template match="n1:component/n1:structuredBody">
        <xsl:for-each select="n1:component[1]/n1:section">
            <xsl:call-template name="section" />
        </xsl:for-each>
    </xsl:template>

    <!-- show nonXMLBody -->
    <xsl:template match="n1:component/n1:nonXMLBody">
        <xsl:choose>
            <!-- if there is a reference, use that in an IFRAME -->
            <xsl:when test="n1:text/n1:reference">
                <IFRAME name="nonXMLBody" id="nonXMLBody" WIDTH="80%" HEIGHT="600" src="{n1:text/n1:reference/@value}" />
            </xsl:when>
            <xsl:when test="n1:text/@mediaType=&quot;text/plain&quot;">
                <pre>
                    <xsl:value-of select="n1:text/text()" />
                </pre>
            </xsl:when>
            <xsl:otherwise>
                <CENTER>Cannot display the text</CENTER>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- top level component/section: display title and text, and process any nested component/sections -->
    <xsl:template name="section">
        <xsl:call-template name="section-title">
            <xsl:with-param name="title" select="n1:title" />
        </xsl:call-template>
        <xsl:call-template name="section-author" />
        <xsl:call-template name="section-text" />
        <xsl:for-each select="n1:component/n1:section">
            <xsl:call-template name="nestedSection">
                <xsl:with-param name="margin" select="1" />
            </xsl:call-template>
        </xsl:for-each>
    </xsl:template>

    <!-- top level section title -->
    <xsl:template name="section-title">
        <xsl:param name="title" />
        <!-- only display the table of contents when greater than 2 because we're hacking the disclaimer here -->
        <xsl:choose>
            <xsl:when test="count(/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component[n1:section]) &gt; 2">
                <h3>
                    <a name="{generate-id($title)}" href="#toc">
                        <xsl:value-of select="$title" />
                    </a>
                </h3>
            </xsl:when>
            <xsl:otherwise>
                <h3>
                    <xsl:value-of select="$title" />
                </h3>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- section author -->
    <xsl:template name="section-author">
        <xsl:if test="count(n1:author)&gt;0">
            <div style="margin-left : 2em;">
                <b>
                    <xsl:text>Section Author: </xsl:text>
                </b>
                <xsl:for-each select="n1:author/n1:assignedAuthor">
                    <div>
                        <xsl:choose>
                            <xsl:when test="n1:assignedPerson/n1:name">
                                <xsl:call-template name="show-name">
                                    <xsl:with-param name="name" select="n1:assignedPerson/n1:name" />
                                </xsl:call-template>
                                <xsl:if test="n1:representedOrganization">
                                    <xsl:text>,&#xA0;</xsl:text>
                                    <xsl:call-template name="show-name">
                                        <xsl:with-param name="name" select="n1:representedOrganization/n1:name" />
                                    </xsl:call-template>
                                </xsl:if>
                            </xsl:when>
                            <xsl:when test="n1:assignedAuthoringDevice/n1:softwareName">
                                <xsl:value-of select="n1:assignedAuthoringDevice/n1:softwareName" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:for-each select="n1:id">
                                    <div>
                                        <xsl:call-template name="show-id" />
                                    </div>
                                </xsl:for-each>
                            </xsl:otherwise>
                        </xsl:choose>
                    </div>
                </xsl:for-each>
            </div>
        </xsl:if>
    </xsl:template>

    <!-- top-level section Text   -->
    <xsl:template name="section-text">
        <div>
            <xsl:apply-templates select="n1:text" />
        </div>
    </xsl:template>

    <!--   footnote  -->
    <xsl:template match="n1:footnote">
        <div id="footnote" class="footnote">
            <xsl:apply-templates />
        </div>
    </xsl:template>

    <!-- nested component/section -->
    <xsl:template name="nestedSection">
        <xsl:param name="margin" />
        <h4 style="margin-left : {$margin}em;">
            <xsl:value-of select="n1:title" />
        </h4>
        <div style="margin-left : {$margin}em;">
            <xsl:apply-templates select="n1:text" />
        </div>
        <xsl:for-each select="n1:component/n1:section">
            <xsl:call-template name="nestedSection">
                <xsl:with-param name="margin" select="2*$margin" />
            </xsl:call-template>
        </xsl:for-each>
    </xsl:template>

    <!--   paragraph  -->
    <xsl:template match="n1:paragraph">
        <p>
            <xsl:apply-templates />
        </p>
    </xsl:template>

    <!--   pre format  -->
    <xsl:template match="n1:pre">
        <pre>
            <xsl:apply-templates />
        </pre>
    </xsl:template>

    <!--   Content w/ deleted text is hidden -->
    <xsl:template match="n1:content[@revised='delete']" />
    <!--   content  -->
    <xsl:template match="n1:content">
        <xsl:apply-templates />
    </xsl:template>
    <!-- line break -->
    <xsl:template match="n1:br">
        <xsl:element name="br">
            <xsl:apply-templates />
        </xsl:element>
    </xsl:template>
    <!--   list  -->
    <xsl:template match="n1:list">
        <xsl:if test="n1:caption">
            <p>
                <b>
                    <xsl:apply-templates select="n1:caption" />
                </b>
            </p>
        </xsl:if>
        <ul>
            <xsl:for-each select="n1:item">
                <li>
                    <xsl:apply-templates />
                </li>
            </xsl:for-each>
        </ul>
    </xsl:template>
    <xsl:template match="n1:list[@listType='ordered']">
        <xsl:if test="n1:caption">
            <span style="font-weight:bold; ">
                <xsl:apply-templates select="n1:caption" />
            </span>
        </xsl:if>
        <ol>
            <xsl:for-each select="n1:item">
                <li>
                    <xsl:apply-templates />
                </li>
            </xsl:for-each>
        </ol>
    </xsl:template>
    <!--   caption  -->
    <xsl:template match="n1:caption">
        <xsl:apply-templates />
        <xsl:text>: </xsl:text>
    </xsl:template>
    <!--  Tables   -->
    <xsl:template match="n1:table/@*|n1:thead/@*|n1:tfoot/@*|n1:tbody/@*|n1:colgroup/@*|n1:col/@*|n1:tr/@*|n1:th/@*|n1:td/@*">
        <xsl:copy>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="n1:table">
        <table class="narr_table">
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </table>
    </xsl:template>
    <xsl:template match="n1:thead">
        <thead>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </thead>
    </xsl:template>
    <xsl:template match="n1:tfoot">
        <tfoot>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </tfoot>
    </xsl:template>
    <xsl:template match="n1:tbody">
        <tbody>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </tbody>
    </xsl:template>
    <xsl:template match="n1:colgroup">
        <colgroup>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </colgroup>
    </xsl:template>
    <xsl:template match="n1:col">
        <col>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </col>
    </xsl:template>
    <xsl:template match="n1:tr">
        <tr class="narr_tr">
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </tr>
    </xsl:template>
    <xsl:template match="n1:th">
        <!-- we want to add some custom CSS classes -->
        <xsl:element name="th">
            <xsl:choose>
                <xsl:when test="contains(., 'Name')">
                    <xsl:attribute name="class">narr_th testName</xsl:attribute>
                </xsl:when>
                <xsl:when test="contains(., 'Resulted')">
                    <xsl:attribute name="class">narr_th testTimeResulted</xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="class">narr_th</xsl:attribute>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="." />
        </xsl:element>
        <!--
         <th class="narr_th">
              <xsl:copy-of select="@*" />
              <xsl:apply-templates />

        </th>-->
    </xsl:template>
    <xsl:template match="n1:td">
        <td>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </td>
    </xsl:template>
    <xsl:template match="n1:table/n1:caption">
        <span style="font-weight:bold; ">
            <xsl:apply-templates />
        </span>
    </xsl:template>
    <!--   RenderMultiMedia
      this currently only handles GIF's and JPEG's.  It could, however,
      be extended by including other image MIME types in the predicate
      and/or by generating <object> or <applet> tag with the correct
      params depending on the media type  @ID  =$imageRef  referencedObject
      -->
    <xsl:template match="n1:renderMultiMedia">
        <xsl:variable name="imageRef" select="@referencedObject" />
        <xsl:choose>
            <xsl:when test="//n1:regionOfInterest[@ID=$imageRef]">
                <!-- Here is where the Region of Interest image referencing goes -->
                <xsl:if test="//n1:regionOfInterest[@ID=$imageRef]//n1:observationMedia/n1:value[@mediaType='image/gif' or @mediaType='image/jpeg']">
                    <br clear="all" />
                    <xsl:element name="img">
                        <xsl:attribute name="src">
                            <xsl:value-of select="//n1:regionOfInterest[@ID=$imageRef]//n1:observationMedia/n1:value/n1:reference/@value" />
                        </xsl:attribute>
                    </xsl:element>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <!-- Here is where the direct MultiMedia image referencing goes -->
                <xsl:if test="//n1:observationMedia[@ID=$imageRef]/n1:value[@mediaType='image/gif' or @mediaType='image/jpeg']">
                    <br clear="all" />
                    <xsl:element name="img">
                        <xsl:attribute name="src">
                            <xsl:value-of select="//n1:observationMedia[@ID=$imageRef]/n1:value/n1:reference/@value" />
                        </xsl:attribute>
                    </xsl:element>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!--    Stylecode processing
      Supports Bold, Underline and Italics display
      -->
    <xsl:template match="//n1:*[@styleCode]">
        <xsl:if test="@styleCode='Bold'">
            <xsl:element name="b">
                <xsl:apply-templates />
            </xsl:element>
        </xsl:if>
        <xsl:if test="@styleCode='Italics'">
            <xsl:element name="i">
                <xsl:apply-templates />
            </xsl:element>
        </xsl:if>
        <xsl:if test="@styleCode='Underline'">
            <xsl:element name="u">
                <xsl:apply-templates />
            </xsl:element>
        </xsl:if>
        <xsl:if test="@styleCode='alert'">
            <xsl:element name="span">
                <xsl:attribute name="class">alert</xsl:attribute>
                <xsl:apply-templates />
            </xsl:element>
        </xsl:if>
        <xsl:if test="contains(@styleCode,'Bold') and contains(@styleCode,'Italics') and not (contains(@styleCode, 'Underline'))">
            <xsl:element name="b">
                <xsl:element name="i">
                    <xsl:apply-templates />
                </xsl:element>
            </xsl:element>
        </xsl:if>
        <xsl:if test="contains(@styleCode,'Bold') and contains(@styleCode,'Underline') and not (contains(@styleCode, 'Italics'))">
            <xsl:element name="b">
                <xsl:element name="u">
                    <xsl:apply-templates />
                </xsl:element>
            </xsl:element>
        </xsl:if>
        <xsl:if test="contains(@styleCode,'Italics') and contains(@styleCode,'Underline') and not (contains(@styleCode, 'Bold'))">
            <xsl:element name="i">
                <xsl:element name="u">
                    <xsl:apply-templates />
                </xsl:element>
            </xsl:element>
        </xsl:if>
        <xsl:if test="contains(@styleCode,'Italics') and contains(@styleCode,'Underline') and contains(@styleCode, 'Bold')">
            <xsl:element name="b">
                <xsl:element name="i">
                    <xsl:element name="u">
                        <xsl:apply-templates />
                    </xsl:element>
                </xsl:element>
            </xsl:element>
        </xsl:if>
        <xsl:if test="not (contains(@styleCode,'Italics') or contains(@styleCode,'Underline') or contains(@styleCode, 'Bold') or contains(@styleCode, 'alert'))">
            <xsl:apply-templates />
        </xsl:if>
    </xsl:template>
    <!--    Superscript or Subscript   -->
    <xsl:template match="n1:sup">
        <xsl:element name="sup">
            <xsl:apply-templates />
        </xsl:element>
    </xsl:template>
    <xsl:template match="n1:sub">
        <xsl:element name="sub">
            <xsl:apply-templates />
        </xsl:element>
    </xsl:template>
    <!-- show-signature -->
    <xsl:template name="show-sig">
        <xsl:param name="sig" />
        <xsl:choose>
            <xsl:when test="$sig/@code ='S'">
                <xsl:text>signed</xsl:text>
            </xsl:when>
            <xsl:when test="$sig/@code='I'">
                <xsl:text>intended</xsl:text>
            </xsl:when>
            <xsl:when test="$sig/@code='X'">
                <xsl:text>signature required</xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!--  show-id -->
    <xsl:template name="show-id">
        <xsl:param name="id" />
        <xsl:choose>
            <xsl:when test="not($id)">
                <xsl:if test="not(@nullFlavor)">
                    <xsl:if test="@extension">
                        <xsl:value-of select="@extension" />
                    </xsl:if>
                    <xsl:if test="@identifierName|@assigningAuthorityName|@root">
                        <xsl:text> (</xsl:text>
                        <xsl:choose>
                            <xsl:when test="@identifierName">
                                <xsl:value-of select="@identifierName" />
                            </xsl:when>
                            <xsl:when test="@assigningAuthorityName">
                                <xsl:choose>
                                    <xsl:when test="contains(@assigningAuthorityName, ': IHA-MT')">
                                        <xsl:value-of select="substring-before(@assigningAuthorityName, ': IHA-MT')" />
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="@assigningAuthorityName" />
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="@root" />
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:text>)</xsl:text>
                    </xsl:if>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="not($id/@nullFlavor)">
                    <xsl:if test="$id/@extension">
                        <xsl:value-of select="$id/@extension" />
                    </xsl:if>
                    <xsl:text> (</xsl:text>
                    <xsl:choose>
                        <xsl:when test="$id/@identifierName">
                            <xsl:value-of select="$id/@identifierName" />
                        </xsl:when>
                        <xsl:when test="$id/@assigningAuthorityName">
                            <xsl:value-of select="$id/@assigningAuthorityName" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="$id/@root" />
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:text>)</xsl:text>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- Def:show-name  -->
    <!-- HERE NAMES Search and replace the char between the <xsl:text> </xsl:text> below and replace with &#xA0; yes, there's a character there and it's not a space-->
    <xsl:template name="show-name">
        <xsl:param name="name" />
        <xsl:choose>
            <xsl:when test="$name/n1:family">
                <xsl:if test="$name/n1:prefix">
                    <xsl:value-of select="$name/n1:prefix" />
                    <xsl:text>&#xA0;</xsl:text>
                </xsl:if>
                <xsl:for-each select="$name/n1:given">
                    <xsl:value-of select="normalize-space(.)" />
                    <xsl:text>&#xA0;</xsl:text>
                </xsl:for-each>
                <xsl:value-of select="$name/n1:family" />
                <xsl:if test="$name/n1:suffix">
                    <xsl:text>,&#xA0;</xsl:text>
                    <xsl:value-of select="$name/n1:suffix" />
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$name" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- show-gender  -->
    <xsl:template name="show-gender">
        <xsl:choose>
            <xsl:when test="@code   = 'M'">
                <xsl:text>Male</xsl:text>
            </xsl:when>
            <xsl:when test="@code  = 'F'">
                <xsl:text>Female</xsl:text>
            </xsl:when>
            <xsl:when test="@code  = 'U'">
                <xsl:text>Undifferentiated</xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- show-race-ethnicity  -->
    <xsl:template name="show-race-ethnicity">
        <xsl:choose>
            <xsl:when test="@displayName">
                <xsl:value-of select="@displayName" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="@code" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- show-contactInfo -->
    <xsl:template name="show-contactInfo">
        <xsl:param name="contact" />
        <xsl:for-each select="$contact/n1:addr">
            <div>
                <xsl:variable name="thisUse" select="./@use" />
                <xsl:call-template name="show-address">
                    <xsl:with-param name="address" select="." />
                </xsl:call-template>
                <xsl:for-each select="$contact/n1:telecom[@use = $thisUse]">
                    <xsl:call-template name="show-telecom">
                        <xsl:with-param name="telecom" select="." />
                    </xsl:call-template>
                </xsl:for-each>
            </div>
        </xsl:for-each>
        <xsl:for-each select="$contact/n1:telecom[not(@use = $contact/n1:addr/@use)]">
            <div>
                <xsl:call-template name="show-telecom">
                    <xsl:with-param name="telecom" select="." />
                </xsl:call-template>
            </div>
        </xsl:for-each>
    </xsl:template>
    <!-- show-address -->
    <xsl:template name="show-address">
        <xsl:param name="address" />
        <xsl:choose>
            <xsl:when test="$address">
                <xsl:for-each select="$address">
                    <div>
                        <xsl:if test="./@use">
                            <xsl:text />
                            <div class="h4">
                                <xsl:call-template name="translateTelecomCode">
                                    <xsl:with-param name="code" select="./@use" />
                                </xsl:call-template>
                                <xsl:text>:</xsl:text>
                            </div>
                        </xsl:if>
                        <xsl:for-each select="./text()[normalize-space(.)]">
                            <div>
                                <xsl:value-of select="normalize-space(.)" />
                            </div>
                        </xsl:for-each>
                        <xsl:if test="./n1:streetAddressLine">
                            <xsl:for-each select="./n1:streetAddressLine">
                                <div>
                                    <xsl:value-of select="." />
                                </div>
                            </xsl:for-each>
                        </xsl:if>
                        <xsl:if test="./n1:streetName">
                            <div>
                                <xsl:value-of select="./n1:streetName" />
                                <xsl:text />
                                <xsl:value-of select="./n1:houseNumber" />
                            </div>
                        </xsl:if>
                        <xsl:if test="string-length(./n1:city)&gt;0">
                            <xsl:value-of select="./n1:city" />
                        </xsl:if>
                        <xsl:if test="string-length(./n1:state)&gt;0">
                            <xsl:text>,&#xA0;</xsl:text>
                            <xsl:value-of select="./n1:state" />
                        </xsl:if>
                        <xsl:if test="string-length(./n1:postalCode)&gt;0">
                            <xsl:text>&#xA0;</xsl:text>
                            <xsl:value-of select="./n1:postalCode" />
                        </xsl:if>
                        <xsl:if test="string-length(./n1:country)&gt;0">
                            <xsl:text>,&#xA0;</xsl:text>
                            <xsl:value-of select="./n1:country" />
                        </xsl:if>
                    </div>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>address not available</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- show-telecom -->
    <xsl:template name="show-telecom">
        <xsl:param name="telecom" />
        <xsl:choose>
            <xsl:when test="$telecom">
                <xsl:variable name="type" select="substring-before($telecom/@value, ':')" />
                <xsl:variable name="value" select="substring-after($telecom/@value, ':')" />
                <xsl:if test="$type">
                    <xsl:call-template name="translateTelecomCode">
                        <xsl:with-param name="code" select="$type" />
                    </xsl:call-template>
                    <xsl:if test="@use">
                        <xsl:text> (</xsl:text>
                        <xsl:call-template name="translateTelecomCode">
                            <xsl:with-param name="code" select="@use" />
                        </xsl:call-template>
                        <xsl:text>)</xsl:text>
                    </xsl:if>
                    <xsl:text>: </xsl:text>
                    <xsl:text />
                    <xsl:value-of select="$value" />
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>Telecom information not available</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
        <br />
    </xsl:template>
    <!-- show-recipientType -->
    <xsl:template name="show-recipientType">
        <xsl:param name="typeCode" />
        <xsl:choose>
            <xsl:when test="$typeCode='PRCP'">Primary </xsl:when>
            <xsl:when test="$typeCode='TRC'">Secondary </xsl:when>
        </xsl:choose>
        <xsl:text>Recipient</xsl:text>
    </xsl:template>
    <!-- Convert Telecom URL to display text -->
    <xsl:template name="translateTelecomCode">
        <xsl:param name="code" />
        <!--xsl:value-of select="document('voc.xml')/systems/system[@root=$code/@codeSystem]/code[@value=$code/@code]/@displayName"/-->
        <!--xsl:value-of select="document('codes.xml')/*/code[@code=$code]/@display"/-->
        <xsl:choose>
            <!-- lookup table Telecom URI -->
            <xsl:when test="$code='tel'">
                <xsl:text>Tel</xsl:text>
            </xsl:when>
            <xsl:when test="$code='fax'">
                <xsl:text>Fax</xsl:text>
            </xsl:when>
            <xsl:when test="$code='http'">
                <xsl:text>Web</xsl:text>
            </xsl:when>
            <xsl:when test="$code='mailto'">
                <xsl:text>Mail</xsl:text>
            </xsl:when>
            <xsl:when test="$code='CONF'">
                <xsl:text>Confidential Address</xsl:text>
            </xsl:when>
            <xsl:when test="$code='DIR'">
                <xsl:text>Direct</xsl:text>
            </xsl:when>
            <xsl:when test="$code='EC'">
                <xsl:text>Emergency Contact</xsl:text>
            </xsl:when>
            <xsl:when test="$code='H'">
                <xsl:text>Home</xsl:text>
            </xsl:when>
            <xsl:when test="$code='HV'">
                <xsl:text>Vacation Home</xsl:text>
            </xsl:when>
            <xsl:when test="$code='HP'">
                <xsl:text>Primary Home</xsl:text>
            </xsl:when>
            <xsl:when test="$code='MC'">
                <xsl:text>Mobile Contact</xsl:text>
            </xsl:when>
            <xsl:when test="$code='PG'">
                <xsl:text>Pager</xsl:text>
            </xsl:when>
            <xsl:when test="$code='PHYS'">
                <xsl:text>Physical Visit Address</xsl:text>
            </xsl:when>
            <xsl:when test="$code='PST'">
                <xsl:text>Postal Address</xsl:text>
            </xsl:when>
            <xsl:when test="$code='TMP'">
                <xsl:text>Temporary</xsl:text>
            </xsl:when>
            <xsl:when test="$code='WP'">
                <xsl:text>Work Place</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>{$code='</xsl:text>
                <xsl:value-of select="$code" />
                <xsl:text>'?}</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- convert RoleClassAssociative code to display text -->
    <xsl:template name="translateRoleAssoCode">
        <xsl:param name="classCode" />
        <xsl:param name="code" />
        <xsl:choose>
            <xsl:when test="$classCode='AFFL'">
                <xsl:text>affiliate</xsl:text>
            </xsl:when>
            <xsl:when test="$classCode='AGNT'">
                <xsl:text>agent</xsl:text>
            </xsl:when>
            <xsl:when test="$classCode='ASSIGNED'">
                <xsl:text>assigned entity</xsl:text>
            </xsl:when>
            <xsl:when test="$classCode='COMPAR'">
                <xsl:text>commissioning party</xsl:text>
            </xsl:when>
            <xsl:when test="$classCode='CON'">
                <xsl:text>contact</xsl:text>
            </xsl:when>
            <xsl:when test="$classCode='ECON'">
                <xsl:text>emergency contact</xsl:text>
            </xsl:when>
            <xsl:when test="$classCode='NOK'">
                <xsl:text>next of kin</xsl:text>
            </xsl:when>
            <xsl:when test="$classCode='SGNOFF'">
                <xsl:text>signing authority</xsl:text>
            </xsl:when>
            <xsl:when test="$classCode='GUARD'">
                <xsl:text>guardian</xsl:text>
            </xsl:when>
            <xsl:when test="$classCode='GUAR'">
                <xsl:text>guardian</xsl:text>
            </xsl:when>
            <xsl:when test="$classCode='CIT'">
                <xsl:text>citizen</xsl:text>
            </xsl:when>
            <xsl:when test="$classCode='COVPTY'">
                <xsl:text>covered party</xsl:text>
            </xsl:when>
            <xsl:when test="$classCode='PRS'">
                <xsl:text>personal relationship</xsl:text>
            </xsl:when>
            <xsl:when test="$classCode='CAREGIVER'">
                <xsl:text>care giver</xsl:text>
            </xsl:when>
            <xsl:when test="$classCode='PROV'">
                <xsl:text>healthcare provider</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>{$classCode='</xsl:text>
                <xsl:value-of select="$classCode" />
                <xsl:text>'?}</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="($code/@code) and ($code/@codeSystem='2.16.840.1.113883.5.111')">
            <xsl:text>&#xA0;</xsl:text>
            <xsl:choose>
                <xsl:when test="$code/@code='FTH'">
                    <xsl:text>(Father)</xsl:text>
                </xsl:when>
                <xsl:when test="$code/@code='MTH'">
                    <xsl:text>(Mother)</xsl:text>
                </xsl:when>
                <xsl:when test="$code/@code='NPRN'">
                    <xsl:text>(Natural parent)</xsl:text>
                </xsl:when>
                <xsl:when test="$code/@code='STPPRN'">
                    <xsl:text>(Step parent)</xsl:text>
                </xsl:when>
                <xsl:when test="$code/@code='SONC'">
                    <xsl:text>(Son)</xsl:text>
                </xsl:when>
                <xsl:when test="$code/@code='DAUC'">
                    <xsl:text>(Daughter)</xsl:text>
                </xsl:when>
                <xsl:when test="$code/@code='CHILD'">
                    <xsl:text>(Child)</xsl:text>
                </xsl:when>
                <xsl:when test="$code/@code='EXT'">
                    <xsl:text>(Extended family member)</xsl:text>
                </xsl:when>
                <xsl:when test="$code/@code='NBOR'">
                    <xsl:text>(Neighbor)</xsl:text>
                </xsl:when>
                <xsl:when test="$code/@code='SIGOTHR'">
                    <xsl:text>(Significant other)</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>{$code/@code='</xsl:text>
                    <xsl:value-of select="$code/@code" />
                    <xsl:text>'?}</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>
    <!-- show time -->
    <xsl:template name="show-time">
        <xsl:param name="datetime" />
        <xsl:choose>
            <xsl:when test="not($datetime)">
                <xsl:call-template name="formatDateTime">
                    <xsl:with-param name="date" select="@value" />
                </xsl:call-template>
                <xsl:text />
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="formatDateTime">
                    <xsl:with-param name="date" select="$datetime/@value" />
                </xsl:call-template>
                <xsl:text />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- paticipant facility and date -->
    <xsl:template name="facilityAndDates">
        <table class="header_table">
            <tbody>
                <!-- facility id -->
                <tr>
                    <td width="15%" class="td_label_dark">
                        <span class="td_label_dark">
                            <xsl:text>Facility ID</xsl:text>
                        </span>
                    </td>
                    <td colspan="3">
                        <xsl:choose>
                            <xsl:when test="count(/n1:ClinicalDocument/n1:participant[@typeCode='LOC'][@contextControlCode='OP']/n1:associatedEntity[@classCode='SDLOC']/n1:id)&gt;0">
                                <!-- change context node -->
                                <xsl:for-each select="/n1:ClinicalDocument/n1:participant[@typeCode='LOC'][@contextControlCode='OP']/n1:associatedEntity[@classCode='SDLOC']/n1:id">
                                    <xsl:call-template name="show-id" />
                                    <!-- change context node again, for the code -->
                                    <xsl:for-each select="../n1:code">
                                        <xsl:text> (</xsl:text>
                                        <xsl:call-template name="show-code">
                                            <xsl:with-param name="code" select="." />
                                        </xsl:call-template>
                                        <xsl:text>)</xsl:text>
                                    </xsl:for-each>
                                </xsl:for-each>
                            </xsl:when>
                            <xsl:otherwise>
                                Not available
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
                <!-- Period reported -->
                <tr>
                    <td width="15%" class="td_label_dark">
                        <span class="td_label_dark">
                            <xsl:text>First day of period reported</xsl:text>
                        </span>
                    </td>
                    <td colspan="3">
                        <xsl:call-template name="show-time">
                            <xsl:with-param name="datetime" select="/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:effectiveTime/n1:low" />
                        </xsl:call-template>
                    </td>
                </tr>
                <tr>
                    <td width="15%" class="td_label_dark">
                        <span class="td_label_dark">
                            <xsl:text>Last day of period reported</xsl:text>
                        </span>
                    </td>
                    <td colspan="3">
                        <xsl:call-template name="show-time">
                            <xsl:with-param name="datetime" select="/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:effectiveTime/n1:high" />
                        </xsl:call-template>
                    </td>
                </tr>
            </tbody>
        </table>
    </xsl:template>
    <!-- show assignedEntity -->
    <xsl:template name="show-assignedEntity">
        <xsl:param name="asgnEntity" />
        <xsl:choose>
            <xsl:when test="$asgnEntity/n1:assignedPerson/n1:name">
                <xsl:call-template name="show-name">
                    <xsl:with-param name="name" select="$asgnEntity/n1:assignedPerson/n1:name" />
                </xsl:call-template>
                <xsl:if test="$asgnEntity/n1:representedOrganization/n1:name">
                    <xsl:text> of </xsl:text>
                    <xsl:value-of select="$asgnEntity/n1:representedOrganization/n1:name" />
                </xsl:if>
            </xsl:when>
            <xsl:when test="$asgnEntity/n1:representedOrganization">
                <xsl:value-of select="$asgnEntity/n1:representedOrganization/n1:name" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$asgnEntity/n1:id">
                    <xsl:call-template name="show-id" />
                    <xsl:choose>
                        <xsl:when test="position()!=last()">
                            <xsl:text>,&#xA0;</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <br />
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- show relatedEntity -->
    <xsl:template name="show-relatedEntity">
        <xsl:param name="relatedEntity" />
        <xsl:choose>
            <xsl:when test="$relatedEntity/n1:relatedPerson/n1:name">
                <xsl:call-template name="show-name">
                    <xsl:with-param name="name" select="$relatedEntity/n1:relatedPerson/n1:name" />
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- show associatedEntity -->
    <xsl:template name="show-associatedEntity">
        <xsl:param name="assoEntity" />
        <xsl:choose>
            <xsl:when test="$assoEntity/n1:associatedPerson">
                <xsl:for-each select="$assoEntity/n1:associatedPerson/n1:name">
                    <div>
                        <xsl:call-template name="show-name">
                            <xsl:with-param name="name" select="." />
                        </xsl:call-template>
                    </div>
                </xsl:for-each>
            </xsl:when>
            <xsl:when test="$assoEntity/n1:scopingOrganization">
                <xsl:for-each select="$assoEntity/n1:scopingOrganization">
                    <xsl:if test="n1:name">
                        <div>
                            <xsl:call-template name="show-name">
                                <xsl:with-param name="name" select="n1:name" />
                            </xsl:call-template>
                        </div>
                    </xsl:if>
                    <xsl:if test="n1:standardIndustryClassCode">
                        <xsl:value-of select="n1:standardIndustryClassCode/@displayName" />
                        <xsl:text> code:</xsl:text>
                        <xsl:value-of select="n1:standardIndustryClassCode/@code" />
                    </xsl:if>
                </xsl:for-each>
            </xsl:when>
            <xsl:when test="$assoEntity/n1:code">
                <xsl:call-template name="show-code">
                    <xsl:with-param name="code" select="$assoEntity/n1:code" />
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$assoEntity/n1:id">
                <xsl:value-of select="$assoEntity/n1:id/@extension" />
                <xsl:text> (</xsl:text>
                <xsl:choose>
                    <xsl:when test="$assoEntity/n1:id/@identifierName">
                        <xsl:value-of select="$assoEntity/n1:id/@identifierName" />
                    </xsl:when>
                    <xsl:when test="$assoEntity/n1:id/@assigningAuthorityName">
                        <xsl:value-of select="$assoEntity/n1:id/@assigningAuthorityName" />
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$assoEntity/n1:id/@root" />
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:text>)</xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- show code
      if originalText present, return it, otherwise, check and return attribute: display name
      -->
    <xsl:template name="show-code">
        <xsl:param name="code" />
        <xsl:param name="showCodeSystem" select="'yes'" />
        <xsl:variable name="this-codeSystem">
            <xsl:value-of select="$code/@codeSystem" />
        </xsl:variable>
        <xsl:variable name="this-code">
            <xsl:value-of select="$code/@code" />
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$code/n1:originalText">
                <xsl:value-of select="$code/n1:originalText" />
            </xsl:when>
            <xsl:when test="$code/@displayName">
                <xsl:value-of select="$code/@displayName" />
            </xsl:when>
            <!--
            <xsl:when test="$the-valuesets/*/voc:system[@root=$this-codeSystem]/voc:code[@value=$this-code]/@displayName">
              <xsl:value-of select="$the-valuesets/*/voc:system[@root=$this-codeSystem]/voc:code[@value=$this-code]/@displayName"/>
            </xsl:when>
            -->
            <xsl:otherwise>
                <xsl:value-of select="$this-code" />
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="($showCodeSystem='yes') and ($code/@codeSystem|$code/@codeSystemName)">
            <xsl:text> (</xsl:text>
            <xsl:choose>
                <xsl:when test="$code/@codeSystemName">
                    <xsl:value-of select="$code/@codeSystemName" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$code/@codeSystem" />
                </xsl:otherwise>
            </xsl:choose>
            <xsl:text>)</xsl:text>
        </xsl:if>
        <xsl:text />
    </xsl:template>
    <!-- show classCode -->
    <xsl:template name="show-actClassCode">
        <xsl:param name="clsCode" />
        <xsl:choose>
            <xsl:when test=" $clsCode = 'ACT' ">
                <xsl:text>healthcare service</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'ACCM' ">
                <xsl:text>accommodation</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'ACCT' ">
                <xsl:text>account</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'ACSN' ">
                <xsl:text>accession</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'ADJUD' ">
                <xsl:text>financial adjudication</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'CONS' ">
                <xsl:text>consent</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'CONTREG' ">
                <xsl:text>container registration</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'CTTEVENT' ">
                <xsl:text>clinical trial timepoint event</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'DISPACT' ">
                <xsl:text>disciplinary action</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'ENC' ">
                <xsl:text>encounter</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'INC' ">
                <xsl:text>incident</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'INFRM' ">
                <xsl:text>inform</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'INVE' ">
                <xsl:text>invoice element</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'LIST' ">
                <xsl:text>working list</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'MPROT' ">
                <xsl:text>monitoring program</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'PCPR' ">
                <xsl:text>care provision</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'PROC' ">
                <xsl:text>procedure</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'REG' ">
                <xsl:text>registration</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'REV' ">
                <xsl:text>review</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'SBADM' ">
                <xsl:text>substance administration</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'SPCTRT' ">
                <xsl:text>speciment treatment</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'SUBST' ">
                <xsl:text>substitution</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'TRNS' ">
                <xsl:text>transportation</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'VERIF' ">
                <xsl:text>verification</xsl:text>
            </xsl:when>
            <xsl:when test=" $clsCode = 'XACT' ">
                <xsl:text>financial transaction</xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- show participationType -->
    <xsl:template name="show-participationType">
        <xsl:param name="ptype" />
        <xsl:choose>
            <xsl:when test=" $ptype='PPRF' ">
                <xsl:text>primary performer</xsl:text>
            </xsl:when>
            <xsl:when test=" $ptype='PRF' ">
                <xsl:text>performer</xsl:text>
            </xsl:when>
            <xsl:when test=" $ptype='VRF' ">
                <xsl:text>verifier</xsl:text>
            </xsl:when>
            <xsl:when test=" $ptype='SPRF' ">
                <xsl:text>secondary performer</xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- show participationFunction -->
    <xsl:template name="show-participationFunction">
        <xsl:param name="pFunction" />
        <xsl:choose>
            <!-- From the CDA encounterParticipant code system -->
            <xsl:when test=" $pFunction = 'ADM' ">
                <xsl:text>(admitting physician)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'ATND' ">
                <xsl:text>(attending physician)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'CON' ">
                <xsl:text>(consulting provider)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'DIS' ">
                <xsl:text>(discharging physician)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'REF' ">
                <xsl:text>(referring provider)</xsl:text>
            </xsl:when>
            <!-- From the HL7 v3 ParticipationFunction code system -->
            <xsl:when test=" $pFunction = 'ANEST' ">
                <xsl:text>(anesthesist)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'ANRS' ">
                <xsl:text>(anesthesia nurse)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'FASST' ">
                <xsl:text>(first assistant surgeon)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'MDWF' ">
                <xsl:text>(midwife)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'NASST' ">
                <xsl:text>(nurse assistant)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'PCP' ">
                <xsl:text>(primary care physician)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'PRISURG' ">
                <xsl:text>(primary surgeon)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'RNDPHYS' ">
                <xsl:text>(rounding physician)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'SASST' ">
                <xsl:text>(second assistant surgeon)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'SNRS' ">
                <xsl:text>(scrub nurse)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'TASST' ">
                <xsl:text>(third assistant)</xsl:text>
            </xsl:when>
            <!-- From the HL7 v2 Provider Role code system (2.16.840.1.113883.12.443) which is used by HITSP -->
            <xsl:when test=" $pFunction = 'CP' ">
                <xsl:text>(consulting provider)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'PP' ">
                <xsl:text>(primary care provider)</xsl:text>
            </xsl:when>
            <xsl:when test=" $pFunction = 'MP' ">
                <xsl:text>(medical home provider)</xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- Alan Bruce, June 2013: A really hackish, unpleasant way to determine if a date is within Cdn DST -->
    <xsl:template name="isDST">
        <xsl:param name="date" />
        <!-- xsl:variable name="month" select="substring($date,5,2)"/ -->
        <!-- xsl:variable name="day" select="substring($date,7,2)"/ -->
        <xsl:variable name="year" select="substring($date,0,5)" />
        <xsl:variable name="mody" select="substring($date,5,4)" />
        <xsl:variable name="dstStart">
            <xsl:choose>
                <xsl:when test="$year='2012'">0311</xsl:when>
                <xsl:when test="$year='2013'">0310</xsl:when>
                <xsl:when test="$year='2014'">0309</xsl:when>
                <xsl:when test="$year='2015'">0308</xsl:when>
                <xsl:when test="$year='2016'">0313</xsl:when>
                <xsl:when test="$year='2017'">0312</xsl:when>
                <xsl:when test="$year='2018'">0311</xsl:when>
                <xsl:otherwise>0310</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="dstEnd">
            <xsl:choose>
                <xsl:when test="$year='2012'">1104</xsl:when>
                <xsl:when test="$year='2013'">1103</xsl:when>
                <xsl:when test="$year='2014'">1102</xsl:when>
                <xsl:when test="$year='2015'">1101</xsl:when>
                <xsl:when test="$year='2016'">1106</xsl:when>
                <xsl:when test="$year='2017'">1105</xsl:when>
                <xsl:when test="$year='2018'">1104</xsl:when>
                <xsl:otherwise>1101</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="string-length($date) &gt; 7">
                <xsl:choose>
                    <xsl:when test="$mody &gt;= $dstStart and $mody &lt; $dstEnd">
                        <xsl:value-of select="true()" />
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="false()" />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="false()" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="formatDateTime">
        <xsl:param name="date" />
        <!-- Need to know daylight savings time -->
        <xsl:variable name="DST">
            <xsl:call-template name="isDST">
                <xsl:with-param name="date" select="$date" />
            </xsl:call-template>
        </xsl:variable>
        <!-- month -->
        <xsl:variable name="month" select="substring ($date, 5, 2)" />
        <xsl:choose>
            <xsl:when test="$month='01'">
                <xsl:text>January </xsl:text>
            </xsl:when>
            <xsl:when test="$month='02'">
                <xsl:text>February </xsl:text>
            </xsl:when>
            <xsl:when test="$month='03'">
                <xsl:text>March </xsl:text>
            </xsl:when>
            <xsl:when test="$month='04'">
                <xsl:text>April </xsl:text>
            </xsl:when>
            <xsl:when test="$month='05'">
                <xsl:text>May </xsl:text>
            </xsl:when>
            <xsl:when test="$month='06'">
                <xsl:text>June </xsl:text>
            </xsl:when>
            <xsl:when test="$month='07'">
                <xsl:text>July </xsl:text>
            </xsl:when>
            <xsl:when test="$month='08'">
                <xsl:text>August </xsl:text>
            </xsl:when>
            <xsl:when test="$month='09'">
                <xsl:text>September </xsl:text>
            </xsl:when>
            <xsl:when test="$month='10'">
                <xsl:text>October </xsl:text>
            </xsl:when>
            <xsl:when test="$month='11'">
                <xsl:text>November </xsl:text>
            </xsl:when>
            <xsl:when test="$month='12'">
                <xsl:text>December </xsl:text>
            </xsl:when>
        </xsl:choose>
        <!-- day -->
        <xsl:choose>
            <xsl:when test="substring ($date, 7, 1)=&quot;0&quot;">
                <xsl:value-of select="substring ($date, 8, 1)" />
                <xsl:text>,&#xA0;</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="substring ($date, 7, 2)" />
                <xsl:text>,&#xA0;</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
        <!-- year -->
        <xsl:value-of select="substring ($date, 1, 4)" />
        <!-- time and US timezone -->
        <xsl:if test="string-length($date) &gt; 8">
            <!-- time -->
            <xsl:variable name="time">
                <xsl:value-of select="substring($date,9,6)" />
            </xsl:variable>
            <xsl:variable name="hh">
                <xsl:value-of select="substring($time,1,2)" />
            </xsl:variable>
            <xsl:variable name="mm">
                <xsl:value-of select="substring($time,3,2)" />
            </xsl:variable>
            <xsl:variable name="ss">
                <xsl:value-of select="substring($time,5,2)" />
            </xsl:variable>
            <xsl:if test="(string-length($hh)&gt;1) and (substring($time,1,4) != '0000')">
                <xsl:text>,&#xA0;</xsl:text>
                <xsl:value-of select="$hh" />
                <xsl:if test="string-length($mm)&gt;1 and not(contains($mm,'-')) and not (contains($mm,'+'))">
                    <xsl:text>:</xsl:text>
                    <xsl:value-of select="$mm" />
                    <xsl:if test="string-length($ss)&gt;1 and not(contains($ss,'-')) and not (contains($ss,'+'))">
                        <xsl:text>:</xsl:text>
                        <xsl:value-of select="$ss" />
                    </xsl:if>
                </xsl:if>
                <!-- time zone -->
                <xsl:variable name="tzon">
                    <xsl:choose>
                        <xsl:when test="contains($date,'+')">
                            <xsl:text>+</xsl:text>
                            <xsl:value-of select="substring-after($date, '+')" />
                        </xsl:when>
                        <xsl:when test="contains($date,'-')">
                            <xsl:text>-</xsl:text>
                            <xsl:value-of select="substring-after($date, '-')" />
                        </xsl:when>
                    </xsl:choose>
                </xsl:variable>
                <xsl:choose>
                    <!-- reference: http://www.timeanddate.com/library/abbreviations/timezones/na/ -->
                    <xsl:when test="($tzon = '-0500') and $DST='false'">
                        <xsl:text>, EST</xsl:text>
                    </xsl:when>
                    <xsl:when test="($tzon = '-0500') and $DST">
                        <xsl:text>, CDT</xsl:text>
                    </xsl:when>
                    <xsl:when test="($tzon = '-0600') and $DST='false'">
                        <xsl:text>, CST</xsl:text>
                    </xsl:when>
                    <xsl:when test="($tzon = '-0600') and $DST">
                        <xsl:text>, MDT</xsl:text>
                    </xsl:when>
                    <xsl:when test="($tzon = '-0700') and $DST='false'">
                        <xsl:text>, MST</xsl:text>
                    </xsl:when>
                    <xsl:when test="($tzon = '-0700') and $DST">
                        <xsl:text>, PDT</xsl:text>
                    </xsl:when>
                    <xsl:when test="($tzon = '-0800') and $DST='false'">
                        <xsl:text>, PST</xsl:text>
                    </xsl:when>
                    <xsl:when test="($tzon = '-0800') and $DST">
                        <xsl:text>, AKDT</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text />
                        <xsl:value-of select="$tzon" />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
        </xsl:if>
    </xsl:template>
    <!-- calculate and display age -->
    <xsl:template name="calc-age">
        <xsl:param name="birthdate" />
        <xsl:param name="currentDate" />
        <xsl:variable name="birthDay" select="number(substring ($birthdate, 7, 2))" />
        <xsl:variable name="currentDay" select="number(substring ($currentDate, 7, 2))" />
        <xsl:variable name="birthMonth" select="number(substring ($birthdate, 5, 2))" />
        <xsl:variable name="currentMonth" select="number(substring ($currentDate, 5, 2))" />
        <xsl:variable name="birthYear" select="number(substring ($birthdate, 1, 4))" />
        <xsl:variable name="currentYear" select="number(substring ($currentDate, 1, 4))" />
        <xsl:if test="(string($birthYear) != 'NaN') and (string($currentYear) != 'NaN') ">
            <div>
                <xsl:text>Age: </xsl:text>
                <xsl:choose>
                    <xsl:when test="($currentMonth &gt; $birthMonth) or ($currentMonth = $birthMonth) and ($currentDay &gt;= $birthDay)">
                        <xsl:value-of select="$currentYear - $birthYear" />
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$currentYear - $birthYear - 1" />
                    </xsl:otherwise>
                </xsl:choose>
            </div>
        </xsl:if>
    </xsl:template>
    <!-- convert to lower case -->
    <xsl:template name="caseDown">
        <xsl:param name="data" />
        <xsl:if test="$data">
            <xsl:value-of select="translate($data, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')" />
        </xsl:if>
    </xsl:template>
    <!-- convert to upper case -->
    <xsl:template name="caseUp">
        <xsl:param name="data" />
        <xsl:if test="$data">
            <xsl:value-of select="translate($data,'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ')" />
        </xsl:if>
    </xsl:template>
    <!-- convert first character to upper case -->
    <xsl:template name="firstCharCaseUp">
        <xsl:param name="data" />
        <xsl:if test="$data">
            <xsl:call-template name="caseUp">
                <xsl:with-param name="data" select="substring($data,1,1)" />
            </xsl:call-template>
            <xsl:value-of select="substring($data,2)" />
        </xsl:if>
    </xsl:template>
    <!-- show-noneFlavor -->
    <xsl:template name="show-noneFlavor">
        <xsl:param name="nf" />
        <xsl:choose>
            <xsl:when test=" $nf = 'NI' ">
                <xsl:text>no information</xsl:text>
            </xsl:when>
            <xsl:when test=" $nf = 'INV' ">
                <xsl:text>invalid</xsl:text>
            </xsl:when>
            <xsl:when test=" $nf = 'MSK' ">
                <xsl:text>masked</xsl:text>
            </xsl:when>
            <xsl:when test=" $nf = 'NA' ">
                <xsl:text>not applicable</xsl:text>
            </xsl:when>
            <xsl:when test=" $nf = 'UNK' ">
                <xsl:text>unknown</xsl:text>
            </xsl:when>
            <xsl:when test=" $nf = 'OTH' ">
                <xsl:text>other</xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>