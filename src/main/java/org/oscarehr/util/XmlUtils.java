package org.oscarehr.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtils
{
	public static void writeNode(Node node, OutputStream os) throws TransformerException
	{
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();

		DOMSource domSource = new DOMSource(node);
		StreamResult streamResult = new StreamResult(os);
		transformer.transform(domSource, streamResult);
	}

	public static String toString(Node node) throws TransformerException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writeNode(node, baos);

		return(baos.toString());
	}

	public static Document toDocument(String s) throws IOException, org.xml.sax.SAXException, ParserConfigurationException
	{
		return(toDocument(s.getBytes()));
	}
	
	public static Document toDocument(byte[] x) throws IOException, org.xml.sax.SAXException, ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		ByteArrayInputStream baos = new ByteArrayInputStream(x, 0, x.length);
		Document document = builder.parse(baos);
		baos.close();
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
		appendChild(doc, doc.getFirstChild(), childName, Base64.encodeBase64String(childContents));
	}

	public static void appendChildToRoot(Document doc, String childName, String childContents)
	{
		appendChild(doc, doc.getFirstChild(), childName, childContents);
	}

	public static void appendChild(Document doc, Node parentNode, String childName, String childContents)
	{
		if (childContents==null) throw(new NullPointerException("ChildNode is null."));
		
		Element child = doc.createElement(childName);
		child.appendChild(doc.createTextNode(childContents));
		parentNode.appendChild(child);
	}

	public static String formatXml(String s)
	{
		int indent = 0;
		StringBuilder sb = new StringBuilder();
		char currentChar = ' ';
		char previousChar = ' ';
		char previousPreviousChar = ' ';
		for (int i = 0; i < s.length(); i++)
		{
			currentChar = s.charAt(i);

			sb.append(previousPreviousChar);

			if (currentChar != '/' && previousChar == '<' && previousPreviousChar == '>')
			{
				indent++;
				sb.append(getNewLineIndent(indent));
			}
			else if (currentChar == '/' && previousChar == '<')
			{
				if (previousPreviousChar == '>')
				{
					sb.append(getNewLineIndent(indent));
				}

				indent--;
			}

			previousPreviousChar = previousChar;
			previousChar = currentChar;
		}

		sb.append(previousPreviousChar);
		sb.append(previousChar);

		return(sb.toString());
	}

	private static String getNewLineIndent(int indent)
	{
		StringBuilder sb = new StringBuilder();
		sb.append('\n');
		for (int i = 0; i < indent; i++)
			sb.append('\t');
		return(sb.toString());
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
			if (name.equals(temp.getLocalName()) || name.equals(temp.getNodeName())) return(temp);
		}

		return(null);
	}

	public static String getChildNodeTextContents(Node node, String name)
	{
		Node tempNode = getChildNode(node, name);
		if (tempNode != null) return(tempNode.getTextContent());
		else return(null);
	}

	/**
	 * Only gets the first instance of the attribute value
	 * @param node
	 * @param attributeName
	 * @return
	 */
	public static String getAttribute(Node node, String attributeName)
	{
		NamedNodeMap attributes = node.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++)
		{
			Node tempNode = attributes.item(i);
			if (attributeName.equals(tempNode.getLocalName())) return(tempNode.getNodeValue());
		}

		return(null);
	}

	public static void main(String... argv) throws Exception
	{
		Document doc = newDocument("testRoot");
		appendChildToRoot(doc, "testChild1", "test child< bla< > contents");
		appendChildToRoot(doc, "testChild2", "test child contents 2");

		MiscUtils.getLogger().info(toString(doc));
	}
}
