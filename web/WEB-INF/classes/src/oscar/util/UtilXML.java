package oscar.util;

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import java.util.*;

public class UtilXML
{

    public static Document newDocument()
    {
        try
        {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            return document;
        }
        catch(Exception e)
        {
            Document document1 = null;
            return document1;
        }
    }

    public static Element addNode(Node parentNode, String name)
    {
        return addNode(parentNode, name, null);
    }

    public static Element addNode(Node parentNode, String name, String value)
    {
        Element node = null;
        if(parentNode.getNodeType() == 9)
            node = ((Document)parentNode).createElement(name);
        else
            node = parentNode.getOwnerDocument().createElement(name);
        if(value != null)
            node.appendChild(node.getOwnerDocument().createTextNode(value));
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
            //trans.setOutputProperty(OutputKeys.INDENT, "yes");
            //trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");
            trans.transform(src, rslt);
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
        }
        //System.out.print(ret.toString());
        return ret.toString();
    }
    public static String toXML(Document xmlDoc, String dtdname) {
        StringWriter ret = new StringWriter();
        DOMSource src = new DOMSource(xmlDoc);
        StreamResult rslt = new StreamResult(ret);
        try
        {
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            trans.setOutputProperty(javax.xml.transform.OutputKeys.DOCTYPE_SYSTEM, dtdname);
            //trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");
            trans.transform(src, rslt);
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
        }
        //System.out.print(ret.toString());
        return ret.toString();
    }
    public static String toXML(Node xmlDoc)
    {
        StringWriter ret = new StringWriter();
        DOMSource src = new DOMSource(xmlDoc);
        StreamResult rslt = new StreamResult(ret);
        try
        {
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.transform(src, rslt);
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
        }
        return ret.toString();
    }

    public static Document parseXML(String xmlInput)
    {
        Document document;
        try
        {
            InputSource is = new InputSource(new StringReader(xmlInput));
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            Document document1 = doc;
            return document1;
        }
        catch(Exception e)
        {
            document = null;
        }
        return document;
    }
    public static Document parseXMLFile(String fileName)
        throws  IOException,  FileNotFoundException, Exception
    {
        InputSource is = new InputSource(new FileReader(fileName));
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        return doc;
    }

 
    public static String getText(Node node) {
        String ret = "";
        if (node.hasChildNodes()) {
            
            for(int i = 0; i < node.getChildNodes().getLength(); i++) {
                Node sub = node.getChildNodes().item(i);            
                if(sub.getNodeType() == 3)
                    ret += sub.getNodeValue();
                if(sub.getNodeType() == 1)
                    ret += toXML(sub);            
            }

        }        
        return ret;
    }

    public static void setText(Node node, String text){
        Text txt = node.getOwnerDocument().createTextNode(text);
        node.appendChild(txt);
        node.normalize();
    }


    static Properties prop = null;

    // name - tagName, value - text
    public static Properties getPropText(Node node) {
        prop = new Properties();
        setPropText(node);
        return prop;
    }

    public static void setPropText(Node node) {
        NodeList list = node.getChildNodes();

        if (list != null) {
            for (int i=0; i < list.getLength(); i++) {
                setPropText(list.item(i));
            }
        }

        if (node.getNodeType() == Node.TEXT_NODE) {
            prop.setProperty(node.getParentNode().getNodeName(), node.getNodeValue());
        }
    }

    // name - attrName1, value - attrName2
    public static Properties getPropText(Node node, String tagName, String attrName1, String attrName2) {
        prop = new Properties();
        setPropText(node, tagName, attrName1, attrName2);
        return prop;
    }

    public static void setPropText(Node node, String tag, String attr1, String attr2) {
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();

        NodeList list = node.getChildNodes();

        if (list != null) {
            for (int i=0; i < list.getLength(); i++) {
                setPropText(list.item(i), tag, attr1, attr2);
            }
        }
        
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (node.getNodeName().equals(tag) ) {
                NamedNodeMap attrib = node.getAttributes();
                for (int i = 0; i < attrib.getLength(); i++ ) {
                    Node curAttr = attrib.item(i);
                    if (curAttr.getNodeName().equals(attr1) ) sb1 = new StringBuffer(curAttr.getNodeValue());
                    if (curAttr.getNodeName().equals(attr2) ) sb2 = new StringBuffer(curAttr.getNodeValue());
                }
                prop.setProperty(sb1.toString(), sb2.toString());
            }
        }
    }

    // name - attrName1, value - text
    public static Properties getPropText(Node node, String tagName, String attrName1) {
        prop = new Properties();
        setPropText(node, tagName, attrName1);
        return prop;
    }

    public static void setPropText(Node node, String tag, String attr1) {
        String attrName = "";
        NodeList list = node.getChildNodes();

        if (list != null) {
            for (int i=0; i < list.getLength(); i++) {
                setPropText(list.item(i), tag, attr1);
            }
        }
        
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (node.getNodeName().equals(tag) ) {
                NamedNodeMap attrib = node.getAttributes();
                for (int i = 0; i < attrib.getLength(); i++ ) {
                    Node curAttr = attrib.item(i);
                    if (curAttr.getNodeName().equals(attr1) ) 
                    {
                        attrName = curAttr.getNodeValue();
                        NodeList oldlist = node.getChildNodes();
                        for (int j=0; j < oldlist.getLength(); j++) {

                             if (oldlist.item(j).getNodeType() == Node.TEXT_NODE) {
                                 prop.setProperty(attrName, oldlist.item(j).getNodeValue());
                             }

                        }
                        
                    }
                }
            }
        }
    }

    //get subtag '<tag attr="attr1"> </tag>' xml string
    public static String getNodeXML(Node node, String tag, String attrName, String attr1) {
        String ret = null;
        NodeList list = node.getChildNodes();

        if (list != null) {
            for (int i=0; i < list.getLength(); i++) {
                ret = getNodeXML(list.item(i), tag, attrName, attr1) ;
                if(ret != null) return ret;
            }
        }
        
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (node.getNodeName().equals(tag) ) {
                NamedNodeMap attrib = node.getAttributes();
                for (int i = 0; i < attrib.getLength(); i++ ) {
                    Node curAttr = attrib.item(i);
                    if (curAttr.getNodeName().equals(attrName) && curAttr.getNodeValue().equals(attr1)) {
                        ret = toXML(node);
                        break;
                    }
                }
            }
        }
        return ret;
    }


}