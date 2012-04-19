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


package oscar.oscarMessenger.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class Msgxml
{
    public static Document newDocument()
    {
        try
        {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static Element addNode(Node parentNode, String name)
    {
        return addNode(parentNode, name, null);
    }

    public static Element addNode(Node parentNode, String name, String value)
    {
        Element node = null;

        if(parentNode.getNodeType()==Node.DOCUMENT_NODE)
        {
            node = ((Document)parentNode).createElement(name);
        }
        else
        {
            node = parentNode.getOwnerDocument().createElement(name);
        }

        if(value!=null)
        {
            node.appendChild(node.getOwnerDocument().createTextNode(value));
        }

        return (Element)parentNode.appendChild(node);
    }

    public static String toXML(Document xmlDoc)
    {
        StringWriter ret = new StringWriter();

        DOMSource src = new DOMSource(xmlDoc);
        StreamResult rslt = new StreamResult(ret);

        try
        {
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.transform(src, rslt);
        } catch (Exception e)
        {
           MiscUtils.getLogger().error("Error", e);
        }

        return ret.toString();
    }

    public static Document parseXML(String xmlInput)
    {
        try
        {
            InputSource is = new InputSource(new StringReader(xmlInput));

            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

            return doc;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String getText(Node node)
    {
        String ret = "";

        if(node.hasChildNodes())
        {
            int i;
            for(i=0; i<node.getChildNodes().getLength(); i++)
            {
                Node sub = node.getChildNodes().item(i);

                if(sub.getNodeType()==Node.TEXT_NODE)
                {
                    ret += sub.getNodeValue();
                }
            }
        }

        return ret;
    }
}
