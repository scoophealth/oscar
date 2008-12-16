package org.oscarehr.PMmodule.caisi_integrator;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AuthenticationOutInterceptor extends AbstractSoapInterceptor {
    private String username = null;
    private String password = null;

    public AuthenticationOutInterceptor(String username, String password) {
	super(Phase.WRITE);
	this.username = username;
	this.password = password;
    }

    public void handleMessage(SoapMessage message) throws Fault {
	addUserPassword(message);
    }

    private void addUserPassword(SoapMessage message) {
	List<Header> headers = message.getHeaders();
	headers.add(getHeader("username", username));
	headers.add(getHeader("password", password));
    }

    private Header getHeader(String key, String value) {
	QName qName = new QName("http://oscarehr.org/caisi", key, "caisi");

	Document document = DOMUtils.createDocument();
	Element element = document.createElementNS("http://oscarehr.org/caisi", "caisi:" + key);
	element.setTextContent(value);

	SoapHeader header = new SoapHeader(qName, element);
	return (header);
    }
}
