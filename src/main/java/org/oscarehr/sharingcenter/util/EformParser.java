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


package org.oscarehr.sharingcenter.util;

import java.io.StringReader;

import org.apache.commons.lang.StringUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This page was used to help create this file.
 * http://docs.oracle.com/javaee/1.4/tutorial/doc/JAXPSAX3.html
 *
 * @author yousifiy
 */
public class EformParser extends DefaultHandler implements LexicalHandler { //The LexicalHandler handles comments{
	private static StringBuilder textBuffer;
	private boolean suppressNextClosingTag = false;
	private String[] selfClosingElements = { "meta", "br", "input" };
	private String formSerialNumber;
	private StringBuilder rebuiltHtml = new StringBuilder();

	public EformParser(String formSerialNumber) {
		super();
		this.formSerialNumber = formSerialNumber;
	}

	public String parse(String html) {
		Parser p = new Parser();
		p.setContentHandler(this);
		p.setErrorHandler(this);
		try {
			p.setFeature(Parser.defaultAttributesFeature, false); //or else some default attributes get added to <br>
			p.setFeature(Parser.ignorableWhitespaceFeature, false);

			p.setProperty(Parser.lexicalHandlerProperty, this);

			InputSource inputSource = new InputSource(new StringReader(html));
			p.parse(inputSource);

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		return rebuiltHtml.toString();
	}

	//Checks if selfClosingElements contains name
	private boolean isSelfClosing(String name) {
		boolean result = false;
		for (int i = 0; i < selfClosingElements.length; i++) {
			if (name.equals(selfClosingElements[i])) {
				result = true;
			}
		}
		return result;
	}

	//Write String s to our output
	private void emit(String s) {
		rebuiltHtml.append(s);
	}

	//Write a new line to our output
	private void nl() {
		String lineEnd = System.getProperty("line.separator");
		rebuiltHtml.append(lineEnd);
	}

	@Override
	public void startDocument() throws SAXException {
		emit("<?xml version='1.0' encoding='UTF-8'?>");
		nl();
	}

	@Override
	public void endDocument() throws SAXException {
		nl();
	}

	@Override
	public void startElement(String namespaceURI, String sName, // simple name
	        String qName, // qualified name
	        Attributes attrs) throws SAXException {

		boolean isHTMLElement = false;
		boolean isFormElement = false;

		String eName = sName; // element name
		if ("".equals(eName)) {
			eName = qName; // not namespace-aware
		}
		echoText();
		emit("<" + eName);

		if (eName.equalsIgnoreCase("html")) {
			isHTMLElement = true;
		} else if (eName.equalsIgnoreCase("form")) {
			isFormElement = true;
		}

		if (attrs != null) {
			if (isHTMLElement) {
				emit(" xmlns=\"http://www.w3.org/1999/xhtml\"");
			}
			for (int i = 0; i < attrs.getLength(); i++) {
				String aName = attrs.getLocalName(i); // Attr name

				if ("".equals(aName)) {
					aName = attrs.getQName(i);
				}
				emit(" ");

				String attributeValue = StringUtils.replaceEach(attrs.getValue(i), new String[] { "&", "\"", "<", ">" }, new String[] { "&amp;", "&quot;", "&lt;", "&gt;" });

				emit(aName + "=\"" + attributeValue + "\"");
			}

		}

		//Check if this element name is in the array of self-closing names array.
		if (isSelfClosing(eName)) {
			suppressNextClosingTag = true;
			emit(" />");
			return;
		} else {

			if (eName.equals("style") || eName.equals("script")) {
				emit("><![CDATA[");
			} else {
				emit(">");
			}
		}

		if (isFormElement) {
			nl();
			//emit("<input type=\"hidden\" name=\"pcsFormId\" value=\"" + formSerialNumber + "\" />");
			nl();
		}

	}

	@Override
	public void endElement(String namespaceURI, String sName, // simple name
	        String qName // qualified name
	) throws SAXException {

		echoText();

		if (suppressNextClosingTag) {
			suppressNextClosingTag = false;
			return;
		}

		String eName = sName; // element name
		if ("".equals(eName)) {
			eName = qName; // not namespace-aware
		}

		if (eName.equals("style") || eName.equals("script")) {
			emit("]]></" + eName + ">");
		} else {
			emit("</" + eName + ">");
		}

		nl();
	}

	@Override
	public void characters(char[] buf, int offset, int len) throws SAXException {
		String s = new String(buf, offset, len);
		if (textBuffer == null) {
			textBuffer = new StringBuilder(s);
		} else {
			textBuffer.append(s);
		}
	}

	@Override
	public void comment(char[] buf, int offset, int len) throws SAXException {
		//throw new UnsupportedOperationException("Not supported yet.");
		String s = new String(buf, offset, len);
		if (textBuffer == null) {
			textBuffer = new StringBuilder(s);
		} else {
			textBuffer.append("<!--").append(s).append("-->"); //which is ("<!-- " + s + " -->");
		}
	}

	private void echoText() {
		if (textBuffer == null) {
			return;
		}
		String s = "" + textBuffer;
		emit(s);
		textBuffer = null;
	}

	@Override
	public void startDTD(String name, String publicId, String systemId) throws SAXException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void endDTD() throws SAXException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void startEntity(String name) throws SAXException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void endEntity(String name) throws SAXException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void startCDATA() throws SAXException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void endCDATA() throws SAXException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}
}
