/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AuthenticationOutWSS4JInterceptorForIntegrator extends WSS4JOutInterceptor implements CallbackHandler {
	private static final String REQUESTING_CAISI_PROVIDER_NO_KEY = "requestingCaisiProviderNo";
	private static QName REQUESTING_CAISI_PROVIDER_NO_QNAME = new QName("http://oscarehr.org/caisi", REQUESTING_CAISI_PROVIDER_NO_KEY, "caisi");

	private String password = null;
	private String oscarProviderNo=null;

	public AuthenticationOutWSS4JInterceptorForIntegrator(String user, String password, String oscarProviderNo) {
		this.password = password;
		this.oscarProviderNo=oscarProviderNo;

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
		addRequestingCaisiProviderNo(message, oscarProviderNo);
		super.handleMessage(message);
	}

	private static void addRequestingCaisiProviderNo(SoapMessage message, String providerNo) {
		List<Header> headers = message.getHeaders();

		if (providerNo != null) {
			headers.add(createHeader(REQUESTING_CAISI_PROVIDER_NO_QNAME, REQUESTING_CAISI_PROVIDER_NO_KEY, providerNo));
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
