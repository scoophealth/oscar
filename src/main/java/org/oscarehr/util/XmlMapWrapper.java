package org.oscarehr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class uses an xml document more or less like a Map object.
 * It only really works if your XML is more or less a key value pair
 * of 1 level deep elements. All the get methods are presumed to work 
 * only on the 1st level deep items.
 * <br /><br />
 * The default date format is the ISO format yyyy-MM-dd'T'HH:mm:ss.SSS, it can be overridden with the set method.
 * <br /><br />
 * will add methods to be like map as needed, will start with read only.
 * <br /><br />
 * This class is not thread safe. I doubt Document objects and Nodes are thread safe and I know for sure SimpleDateFormat is not thread safe.
 */
public class XmlMapWrapper
{
	private static final Logger logger=MiscUtils.getLogger();
	
	private Document doc;
	private Node rootNode;
	private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	public XmlMapWrapper(Document doc)
	{
		this.doc=doc;
		rootNode=doc.getFirstChild();
	}
	
	/**
	 * This method changes the date formatting parser.
	 */
	public void setSimpleDateFormat(SimpleDateFormat simpleDateFormat)
	{
		this.simpleDateFormat=simpleDateFormat;
	}
	
	/**
	 * @return the string or null if not exist
	 */
	public String getString(String nodeName)
	{
		return(XmlUtils.getChildNodeTextContents(rootNode, nodeName));
	}
	
	/**
	 * @return the Boolean or null if not exist
	 */
	public Boolean getBooleanOrNull(String nodeName)
	{
		String temp=getString(nodeName);
		
		if (temp==null) return(null);
		
		return(Boolean.valueOf(temp));
	}

	/**
	 * @return true if the contents exist and resolve to true, false otherwise, not this never returns null.
	 */
	public boolean getBooleanValue(String nodeName)
	{
		String temp=getString(nodeName);
		
		return(Boolean.parseBoolean(temp));
	}

	/**
	 * @return the Integer or null if not exist
	 */
	public Integer getInteger(String nodeName)
	{
		String temp=getString(nodeName);
		
		if (temp==null) return(null);
		
		try
		{
			return(Integer.parseInt(temp));
		}
		catch (NumberFormatException e) {
			logger.error("Error parsing Integer : " + temp, e);
			return(null);
		}		
	}
	
	/**
	 * @return the Long or null if not exist
	 */
	public Long getLong(String nodeName)
	{
		String temp=getString(nodeName);
		
		if (temp==null) return(null);
		
		try
		{
			return(Long.parseLong(temp));
		}
		catch (NumberFormatException e) {
			logger.error("Error parsing Long : " + temp, e);
			return(null);
		}		
	}

	/**
	 * @return the Float or null if not exist
	 */
	public Float getFloat(String nodeName)
	{
		String temp=getString(nodeName);
		
		if (temp==null) return(null);
		
		try
		{
			return(Float.parseFloat(temp));
		}
		catch (NumberFormatException e) {
			logger.error("Error parsing Float : " + temp, e);
			return(null);
		}		
	}

	/**
	 * @return the Date or null if not exist
	 */
	public Date getDate(String nodeName)
	{
		String temp=getString(nodeName);
		
		if (temp==null) return(null);
		
		try
		{
			return(simpleDateFormat.parse(temp));
		}
		catch (ParseException e) {
			logger.error("Error parsing date : " + temp, e);
			return(null);
		}
	}
	
	/**
	 * @return the Node or null if not exist
	 */
	public Node getNode(String nodeName)
	{
		return(XmlUtils.getChildNode(rootNode, nodeName));
	}
	
	/**
	 * @return a list of nodes with the name, will return empty list if none.
	 */
	public ArrayList<Node> getChildNodes(String nodeName)
	{
		ArrayList<Node> results=new ArrayList<Node>();
		
		NodeList nodeList=rootNode.getChildNodes();
		for (int i=0; i<nodeList.getLength(); i++)
		{
			Node node=nodeList.item(i);
			if (nodeName.equals(node.getNodeName()) || nodeName.equals(node.getLocalName()))
			{
				results.add(node);
			}
		}
		
		return(results);
	}
}