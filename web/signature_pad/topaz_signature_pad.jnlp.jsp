<%@page contentType="application/x-java-jnlp-file"%>
<%
	String url=request.getRequestURL().toString();
	int lastSlash=url.lastIndexOf('/');
	url=url.substring(0,lastSlash);
%>
<jnlp spec="1.0+" codebase="<%=url%>">
	<information>
		<title>Test</title>
		<vendor>caisi</vendor>
		<description>Test</description>
	</information>
	<resources>
		<j2se version="1.6+" href="http://java.sun.com/products/autodl/j2se" />
		<jar href="topaz_signature_pad-0.0-SNAPSHOT.jar" />
		<jar href="comm-0.0.jar" />
		<jar href="sigplus-2.52.jar" />
	</resources>
	<resources os="Linux">
		<nativelib href="libSigUsb_linux_i386-2.52.jar" />
	</resources>
	<resources os="Windows">
	</resources>
	<security>
		<all-permissions />
	</security>
	<application-desc main-class="org.oscarehr.topaz_signature_pad.MainWebStart" />
</jnlp>
