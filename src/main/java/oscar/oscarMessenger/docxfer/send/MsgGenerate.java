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


package oscar.oscarMessenger.docxfer.send;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.docxfer.util.MsgCommxml;

public class MsgGenerate {
    private static Logger logger=MiscUtils.getLogger(); 

    int demographicNo;
    int itemId;
    
    // Constructors
    public Document getDocument(int demographicNo)
    throws java.sql.SQLException {
        this.demographicNo = demographicNo;
        this.itemId = 0;
        
        Document doc = MsgCommxml.newDocument();
        Element docRoot = MsgCommxml.addNode(doc, "root");
        
        
        
        Document cfg = null;
        try {
            cfg = MsgCommxml.parseXMLFile("/DocXferConfig.xml");
        }
        catch (Exception ex) {
        	logger.error("", ex);
        }
        
        Element cfgRoot = cfg.getDocumentElement();
        NodeList cfgTables = cfgRoot.getChildNodes();
        for(int i=0; i<cfgTables.getLength(); i++) {
            Node tbl = cfgTables.item(i);
            
            if(tbl.getNodeType() == Node.ELEMENT_NODE) {
                if(((Element)tbl).getTagName().equals("table")) {
                    Element newTable = constructTable((Element)tbl, doc);
                    if (newTable.hasChildNodes()){
                        docRoot.appendChild(newTable);
                    }
                }
            }
        }
        return doc;
    }
    
    private Element constructTable(Element cfgTable, Document doc)
    throws java.sql.SQLException {
        Element table = doc.createElement("table");
        
        NamedNodeMap map = cfgTable.getAttributes();
        for(int i=0; i<map.getLength(); i++) {
            Attr attr = (org.w3c.dom.Attr)map.item(i);
            
            table.setAttribute(attr.getNodeName(), attr.getNodeValue());
        }
        
        String sql = this.constructSQL(cfgTable);
        ResultSet rs = DBHandler.GetSQL(sql);
        ResultSetMetaData meta = rs.getMetaData();
        
        Element cfgItem = (Element)cfgTable.getElementsByTagName("item").item(0);
        NodeList cfgFlds = cfgItem.getElementsByTagName("fld");
        while(rs.next()) {
            Element item = MsgCommxml.addNode(table, "item");
            item.setAttribute("itemId", String.valueOf(itemId)); itemId++;
            item.setAttribute("name", cfgItem.getAttribute("name"));
            item.setAttribute("sql", cfgItem.getAttribute("sql"));
            item.setAttribute("removable", cfgItem.getAttribute("removable"));
            
            Element content = MsgCommxml.addNode(item, "content");
            Element data = MsgCommxml.addNode(item, "data");
            
            for(int i=1; i<=meta.getColumnCount(); i++) {
                if(!meta.getColumnName(i).startsWith("fld")) {
                    String name = meta.getTableName(i) + "." + meta.getColumnName(i);
                    
                    String fldData = "";
                    try {
                        
                        fldData = oscar.Misc.getString(rs, i);
                        
                        if(fldData==null) fldData = "";
                        
                    } catch(Exception ex) {}
                    
                    Element fld = doc.createElement(name);
                    
                    try {
                        Node tmp = MsgCommxml.parseXML(fldData).getDocumentElement();
                        Node fldSub = doc.importNode(tmp, true);
                        fld.appendChild(fldSub);
                    }
                    catch (Exception ex) {
                        try {
                            Node tmp = doc.createTextNode(fldData);
                            fld.appendChild(tmp);
                        }
                        catch (Exception ex2) {
                        }
                    }
                    
                    data.appendChild(fld);
                }
            }
            
            {
                String value = oscar.Misc.getString(rs, "fldItem");
                if(value==null) value="";
                item.setAttribute("value", value);
            }
            
            for(int i=0; i<cfgFlds.getLength(); i++) {
                Element cfgFld = (Element)cfgFlds.item(i);
                Element fld = MsgCommxml.addNode(content, "fld");
                
                fld.setAttribute("name", cfgFld.getAttribute("name"));
                fld.setAttribute("sql", cfgFld.getAttribute("sql"));
                String value = oscar.Misc.getString(rs, ("fld" + i));
                if(value==null) value="";
                fld.setAttribute("value", value);
            }
        }
        rs.close();
        
        return table;
    }
    
    private String constructSQL(Element cfgTable) {
        Element fldItem = (Element)cfgTable.getElementsByTagName("item").item(0);
        NodeList flds = cfgTable.getElementsByTagName("fld");
        
        String sql = "SELECT *";
        
        sql += ", " + fldItem.getAttribute("sql") + " AS fldItem";
        
        for(int i=0; i<flds.getLength(); i++) {
            Element fld = (Element)flds.item(i);
            sql += ", " + fld.getAttribute("sql") + " AS fld" + String.valueOf(i);
        }
        
        sql += " FROM " + cfgTable.getAttribute("sqlFrom")
        + " WHERE " + cfgTable.getAttribute("sqlLink")
        + " = '" + this.demographicNo + "'";
        if(cfgTable.getAttribute("sqlWhere").length()>0) {
            sql += " AND " + cfgTable.getAttribute("sqlWhere");
        }
        if(cfgTable.getAttribute("sqlOrder").length()>0) {
            sql += " ORDER BY " + cfgTable.getAttribute("sqlOrder");
        }

        return sql;
    }
}
