package oscar.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.apache.commons.lang.StringEscapeUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class JDBCUtil
{
    public static Document toDocument(ResultSet rs)
       throws ParserConfigurationException, SQLException
    {
       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
       DocumentBuilder builder        = factory.newDocumentBuilder();
       Document doc                   = builder.newDocument();
       StringEscapeUtils strEscUtils = new StringEscapeUtils();
       Element results = doc.createElement("Results");
       doc.appendChild(results);

       ResultSetMetaData rsmd = rs.getMetaData();
       int colCount           = rsmd.getColumnCount();

       while (rs.next())
       {
          Element row = doc.createElement("Row");
          results.appendChild(row);

          for (int i = 1; i <= colCount; i++)
          {
             String columnName = strEscUtils.escapeXml(rsmd.getColumnName(i));
             String value      = strEscUtils.escapeXml(rs.getString(i));
             //System.out.println(columnName+": "+value );
             Element node      = doc.createElement(columnName);
             node.appendChild(doc.createTextNode(value));
             row.appendChild(node);
          }
       }
       return doc;
    }
    
    public static void saveAsXML(Document doc, String fileName) throws IOException
    {
        try{
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer(); 
            DOMSource source = new DOMSource(doc); 
            File newXML = new File(fileName);
            FileOutputStream os = new FileOutputStream(newXML);
            StreamResult result = new StreamResult(os);
            
            transformer.transform(source, result); 
        }
        catch(Exception e){            
            System.out.println(e.getMessage());
        }     
    }
}