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
import org.oscarehr.util.LoggedInInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AuthenticationOutWSS4JInterceptor extends WSS4JOutInterceptor implements CallbackHandler {
	private static final String REQUESTING_CAISI_PROVIDER_NO_KEY = "requestingCaisiProviderNo";
	private static QName REQUESTING_CAISI_PROVIDER_NO_QNAME = new QName("http://oscarehr.org/caisi", REQUESTING_CAISI_PROVIDER_NO_KEY, "caisi");

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
		addRequestionCaisiProviderNo(message);
		super.handleMessage(message);
	}

	private static void addRequestionCaisiProviderNo(SoapMessage message) {
		List<Header> headers = message.getHeaders();

		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		if (loggedInInfo.loggedInProvider != null) {
			headers.add(createHeader(REQUESTING_CAISI_PROVIDER_NO_QNAME, REQUESTING_CAISI_PROVIDER_NO_KEY, loggedInInfo.loggedInProvider.getProviderNo()));
		}
	}

	private static Header createHeader(QName qName, String key, String value) {
		Document document = DOMUtils.createDocument();

		Element element = document.createElementNS("http://oscarehr.org/caisi", "caisi:" + key);
		element.setTextContent(value);

		SoapHeader header = new SoapHeader(qName, element);
		return (header);
	}
}
