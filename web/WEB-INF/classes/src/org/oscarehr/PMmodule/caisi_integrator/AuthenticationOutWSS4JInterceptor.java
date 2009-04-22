package org.oscarehr.PMmodule.caisi_integrator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.oscarehr.util.LoggedInUserFilter;
import org.oscarehr.util.LoggedInUserFilter.LoggedInInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AuthenticationOutWSS4JInterceptor extends WSS4JOutInterceptor implements CallbackHandler {
	private static final String AUDIT_TRAIL_KEY = "auditTrail";
	private static QName AUDIT_TRAIL_QNAME = new QName("http://oscarehr.org/caisi", AUDIT_TRAIL_KEY, "caisi");

	private String password = null;

	public AuthenticationOutWSS4JInterceptor(String user, String password) {
		this.password = password;

		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		properties.put(WSHandlerConstants.USER, user);
		properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		properties.put(WSHandlerConstants.PW_CALLBACK_REF, this);

		setProperties(properties);
	}

	// don't like @override until jdk1.6?
	// @Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (Callback callback : callbacks) {
			if (callback instanceof WSPasswordCallback) {
				WSPasswordCallback wsPasswordCallback = (WSPasswordCallback) callback;
				wsPasswordCallback.setPassword(password);
			}
		}
	}

	public void handleMessage(SoapMessage message) throws Fault {
		addAuditTrail(message);
		super.handleMessage(message);
	}

	private static void addAuditTrail(SoapMessage message) {
		List<Header> headers = message.getHeaders();

		StringBuilder auditTrail = new StringBuilder();

		LoggedInInfo loggedInInfo = LoggedInUserFilter.loggedInInfo.get();
		if (loggedInInfo.internalThreadDescription != null) auditTrail.append("oscar_caisi.thread=").append(loggedInInfo.internalThreadDescription);

		if (loggedInInfo.loggedInProvider != null) {
			if (auditTrail.length() > 0) auditTrail.append(", ");

			String callingUIPage = attemptToExtrapolateCallingUIPage();
			if (callingUIPage != null) {
				auditTrail.append(callingUIPage);
				auditTrail.append(", ");
			}

			auditTrail.append("oscar_caisi.providerNo=");
			auditTrail.append(loggedInInfo.loggedInProvider.getProviderNo());
		}

		if (loggedInInfo.currentFacility != null) {
			if (auditTrail.length() > 0) auditTrail.append(", ");

			auditTrail.append("oscar_caisi.facilityId=");
			auditTrail.append(loggedInInfo.currentFacility.getId());
		}

		headers.add(createHeader(auditTrail.toString()));
	}

	private static String attemptToExtrapolateCallingUIPage() {
		Exception exception = new Exception();
		for (StackTraceElement stackLine : exception.getStackTrace()) {
			String fullClassName = stackLine.getClass().getName();
			String fileName = stackLine.getFileName();
			if (fullClassName.startsWith("org.apache.jsp.") || fileName.endsWith("_jsp.java")) {
				return (fullClassName + '.' + stackLine.getMethodName());
			}
		}

		return (null);
	}

	private static Header createHeader(String value) {
		Document document = DOMUtils.createDocument();

		Element element = document.createElementNS("http://oscarehr.org/caisi", "caisi:" + AUDIT_TRAIL_KEY);
		element.setTextContent(value);

		SoapHeader header = new SoapHeader(AUDIT_TRAIL_QNAME, element);
		return (header);
	}
}
