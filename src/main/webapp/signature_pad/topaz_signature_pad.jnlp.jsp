<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@page import="org.oscarehr.util.DigitalSignatureUtils"%>
<%@page contentType="application/x-java-jnlp-file"%>
<%
	// This jsp expects a "signatureRequestId" parameter which helps distinguish what page is making this request, that way multple open pages etc won't clash. Just use a random number or something unique.
	
	// original url something like http://127.0.0.1:8080/oscar/signature_pad/topaz_signature_pad.jnlp
	// signaturePadUrlBase should be something like http://127.0.0.1:8080/oscar/signature_pad as that's where all the jars are
	String signaturePadUrlBase=request.getRequestURL().toString();
	int lastSlash=signaturePadUrlBase.lastIndexOf('/');
	signaturePadUrlBase=signaturePadUrlBase.substring(0,lastSlash);
	
	String sessionId=session.getId();
	Cookie sessionCookie=null;
	for (Cookie temp : request.getCookies())
	{
		if (sessionId.equals(temp.getValue())) 
		{
			sessionCookie=temp;
			break;
		}
	}
%>
<jnlp spec="1.0+" codebase="<%=signaturePadUrlBase%>">
	<information>
		<title>Signature Pad</title>
		<vendor>caisi</vendor>
		<description>Signature Pad</description>
	</information>
	<resources>
		<j2se version="1.6+" href="http://java.sun.com/products/autodl/j2se" />
		<property name="sessionCookieKey" value="<%=sessionCookie.getName()%>" />
		<property name="sessionCookieValue" value="<%=sessionCookie.getValue()%>" />
		<property name="jnlp.signaturePadUrlBase" value="<%=signaturePadUrlBase%>" />
		<property name="jnlp.signatureRequestId" value="<%=request.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY)%>" />
		<jar href="topaz_signature_pad-0.0-SNAPSHOT.jar" />
		<jar href="comm-0.0_signed.jar" />
		<jar href="sigplus-2.52_signed.jar" />
		<jar href="commons-codec-1.3_signed.jar" />
		<jar href="commons-logging-1.1.1_signed.jar" />
		<jar href="httpclient-4.0-beta2_signed.jar" />
		<jar href="httpcore-4.0-beta3_signed.jar" />
		<nativelib href="libSigUsb-2.52_signed.jar" />
	</resources>
	<security>
		<all-permissions />
	</security>
	<application-desc main-class="org.oscarehr.topaz_signature_pad.MainWebStart" />
</jnlp>
