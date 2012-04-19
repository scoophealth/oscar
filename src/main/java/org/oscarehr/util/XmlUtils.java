/**
 * Copyright (c) 2007-2008. MB Software Vancouver, Canada. All Rights Reserved.
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
 * This software was written for 
 * MB Software, margaritabowl.com
 * Vancouver, B.C., Canada 
 */

package org.oscarehr.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

public final class XmlUtils
{
	public static void setLsSeriliserToFormatted(LSSerializer lsSerializer)
	{
		lsSerializer.getDomConfig().setParameter("format-pretty-print", true);
	}
	
	public static void writeNode(Node node, OutputStream os, boolean formatted) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		DOMImplementationRegistry domImplementationRegistry = DOMImplementationRegistry.newInstance();
		DOMImplementationLS domImplementationLS = (DOMImplementationLS) domImplementationRegistry.getDOMImplementation("LS");
		
		LSOutput lsOutput=domImplementationLS.createLSOutput();
		lsOutput.setEncoding("UTF-8");
		lsOutput.setByteStream(os);
		
		LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
		if (formatted) setLsSeriliserToFormatted(lsSerializer);
		lsSerializer.write(node, lsOutput);		
	}

	public static byte[] toBytes(Node node, boolean formatted) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException 
	{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		writeNode(node, baos, formatted);
		return(baos.toByteArray());
	}

	public static String toString(Node node, boolean formatted) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException 
	{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		writeNode(node, baos, formatted);
		return(baos.toString());
	}

	public static Document toDocumentFromFile(String url) throws ParserConfigurationException, SAXException, IOException
	{
		InputStream is = XmlUtils.class.getResourceAsStream(url);
		if (is == null) is = new FileInputStream(url);

		try
		{
			return(XmlUtils.toDocument(is));
		}
		finally
		{
			is.close();
		}
	}
	
	public static Document toDocument(String s) throws IOException, org.xml.sax.SAXException, ParserConfigurationException
	{
		return(toDocument(s.getBytes()));
	}

	public static Document toDocument(byte[] x) throws IOException, org.xml.sax.SAXException, ParserConfigurationException
	{
		ByteArrayInputStream is = new ByteArrayInputStream(x, 0, x.length);
		return(toDocument(is));
	}

	public static Document toDocument(InputStream is) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(is);
		return(document);
	}

	public static Document newDocument(String rootName) throws ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();

		doc.appendChild(doc.createElement(rootName));

		return(doc);
	}

	public static void appendChildToRoot(Document doc, String childName, byte[] childContents)
	{
		appendChild(doc, doc.getFirstChild(), childName, new String(Base64.encodeBase64(childContents)));
	}

	public static void appendChildToRoot(Document doc, String childName, String childContents)
	{
		appendChild(doc, doc.getFirstChild(), childName, childContents);
	}

	public static void appendChild(Document doc, Node parentNode, String childName, String childContents)
	{
		if (childContents == null) throw(new NullPointerException("ChildNode is null."));

		Element child = doc.createElement(childName);
		child.setTextContent(childContents);
		parentNode.appendChild(child);
	}

	public static void appendChildToRootIgnoreNull(Document doc, String childName, String childContents)
	{
		if (childContents==null) return;
		appendChildToRoot(doc, childName, childContents);
	}

	public static void replaceChild(Document doc, Node parentNode, String childName, String childContents)
	{
		Node node=getChildNode(parentNode, childName);

		// if some one passes in null
		if (childContents == null) 
		{
			// remove existing node
			if (node!=null) parentNode.removeChild(node);
			
			return;
		}
		
		// this means there's at least contents pass in,
		if (node==null)
		{
			// no existing node, so we just add one
			appendChild(doc, parentNode, childName, childContents);
		}
		else
		{
			// existing node so we update it instead.
			node.setTextContent(childContents);
		}
	}

	/**
	 * only returns the first instance of this child node
	 * @param node
	 * @param localName
	 * @return
	 */
	public static Node getChildNode(Node node, String name)
	{
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			Node temp = nodeList.item(i);
			if (temp.getNodeType() != Node.ELEMENT_NODE) continue;
			if (name.equals(temp.getLocalName()) || name.equals(temp.getNodeName())) return(temp);
		}

		return(null);
	}

	/**
	 * @return a list of all child nodes with this name
	 */
	public static ArrayList<Node> getChildNodes(Node node, String name)
	{
		ArrayList<Node> results=new ArrayList<Node>();
		
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			Node temp = nodeList.item(i);
			if (temp.getNodeType() != Node.ELEMENT_NODE) continue;
			if (name.equals(temp.getLocalName()) || name.equals(temp.getNodeName()))
			{
				results.add(temp);
			}
		}

		return(results);
	}

	public static String getChildNodeTextContents(Node node, String name)
	{
		Node tempNode = getChildNode(node, name);
		if (tempNode != null) return(tempNode.getTextContent());
		else return(null);
	}

	/**
	 * @return a list of all child node text contents with this name
	 */
	public static ArrayList<String> getChildNodesTextContents(Node node, String name)
	{
		ArrayList<String> results=new ArrayList<String>();
		
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			Node temp = nodeList.item(i);
			if (temp.getNodeType() != Node.ELEMENT_NODE) continue;
			if (name.equals(temp.getLocalName()) || name.equals(temp.getNodeName()))
			{
				results.add(temp.getTextContent());
			}
		}

		return(results);
	}

	/**
	 * removes any immediate child nodes with the given name.
	 */
	public static void removeAllChildNodes(Node node, String name)
	{
		// this must be done as a 2 pass algorithm. 
		// attempt at doing it at single pass fails because the list is 
		// being altered as we read it which leads to errors.
		ArrayList<Node> removeList=getChildNodes(node, name);
		
		for (Node temp : removeList)
		{
				node.removeChild(temp);
		}
	}
	
	/**
	 * @return the attribute value or null
	 */
	public static String getAttributeValue(Node node, String attributeName)
	{
		NamedNodeMap attributes = node.getAttributes();
		if (attributes == null) return(null);

		Node tempNode = attributes.getNamedItem(attributeName);
		if (tempNode == null) return(null);

		return(tempNode.getNodeValue());
	}

	public static void main(String... argv) throws Exception
	{
		Document doc = newDocument("testRoot");
		appendChildToRoot(doc, "testChild1", "test child< bla< > contents");
		appendChildToRoot(doc, "testChild2", "test child contents 2");

		String xml=toString(doc, true);
		MiscUtils.getLogger().info(xml);
		
		doc=XmlUtils.toDocument(xml);
	}
}
