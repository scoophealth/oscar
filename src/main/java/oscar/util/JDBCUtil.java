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


package oscar.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.xerces.parsers.DOMParser;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import oscar.oscarDB.DBHandler;

public class JDBCUtil
{
    public static Document toDocument(ResultSet rs)
       throws ParserConfigurationException, SQLException
    {
       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
       DocumentBuilder builder        = factory.newDocumentBuilder();
       Document doc                   = builder.newDocument();

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
             String columnName = StringEscapeUtils.escapeXml(rsmd.getColumnName(i));
             String value      = StringEscapeUtils.escapeXml(oscar.Misc.getString(rs,i));

             Element node      = doc.createElement(columnName);
             node.appendChild(doc.createTextNode(value));
             row.appendChild(node);
          }
       }
       rs.close();
       return doc;
    }

    public static void saveAsXML(Document doc, String fileName)
    {
        try{
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File newXML = new File(fileName);
            FileOutputStream os = new FileOutputStream(newXML);
            StreamResult result = new StreamResult(os);

            transformer.transform(source, result);
            MiscUtils.getLogger().debug("Next is to call zip function!");
            zip z = new zip();
            z.write2Zip("xml");
        }
        catch(Exception e){
            MiscUtils.getLogger().debug(e.getMessage() + "cannot saveAsXML");
            File newXML = new File(fileName);
            newXML.delete();
        }
    }

    public static void toDataBase(InputStream inputStream, String fileName)
    {
        boolean validation = true;
        DOMParser parser = new DOMParser();
        Document doc;

        try
        {
            //InputStream inputStream = file.getInputStream();
            InputSource source = new InputSource(inputStream);
            //String fileName = file.getFileName();
            int indexForm = fileName.indexOf("_");
            int indexDemo = fileName.indexOf("_", indexForm+1);
            int indexTimeStamp = fileName.indexOf(".",indexDemo);
            String formName = fileName.substring(0,indexForm);
            String demographicNo = fileName.substring(indexForm+1, indexDemo);
            String timeStamp = fileName.substring(indexDemo+1,indexTimeStamp);


            //check if the data existed in the database already...
            String sql = "SELECT * FROM " + formName + " WHERE demographic_no='"
                         + demographicNo + "' AND formEdited='" + timeStamp + "'";
            MiscUtils.getLogger().debug(sql);
            ResultSet rs = DBHandler.GetSQL(sql);
            if(!rs.first()){
                rs.close();
                sql = "SELECT * FROM " + formName + " WHERE demographic_no='"
                        + demographicNo + "' AND ID='0'";
                MiscUtils.getLogger().debug("sql: " + sql);
                rs = DBHandler.GetSQL(sql, true);
                rs.moveToInsertRow();
                //To validate or not
                parser.setFeature( "http://xml.org/sax/features/validation",validation );
                parser.parse(source);
                doc = parser.getDocument();
                rs = toResultSet(doc, rs);
                rs.insertRow();
            }
            rs.close();
        }
        catch(Exception e)
        {
            MiscUtils.getLogger().debug("Errors " + e);

        }

    }

    private static ResultSet toResultSet(Node node, ResultSet rs) throws SQLException
    {
        int type = node.getNodeType();

        if ( type == Node.ELEMENT_NODE ){

            String name = node.getNodeName();
            String value = "";

            Node next = node.getFirstChild();
            if (next!=null){
                type = next.getNodeType();
                if (type == Node.TEXT_NODE){

                    value = next.getNodeValue();
                }
            }

            if(!name.equalsIgnoreCase("Results")&&!name.equalsIgnoreCase("Row")&&!name.equalsIgnoreCase("ID"))
                rs.updateString(name, value);
        }

        //recurse
        for(Node child = node.getFirstChild(); child != null; child = child.getNextSibling()){
            toResultSet(child,rs);
        }

        return rs;

    }

}
