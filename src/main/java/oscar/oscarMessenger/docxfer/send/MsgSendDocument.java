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


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import oscar.oscarMessenger.docxfer.util.MsgCommxml;

public class MsgSendDocument
{
    public Document parseChecks(String originalDocument, String checks){
        Document doc = MsgCommxml.parseXML(originalDocument);
        Element root = doc.getDocumentElement();

        NodeList items = root.getElementsByTagName("item");

        // since we're removing nodes, we must do this in reverse
        for(int i = items.getLength() - 1; i >= 0; i--)
        {
            Element item = (Element)items.item(i);

            if( ! item.getAttribute("removable").equalsIgnoreCase("false"))
            {
                if(checks.indexOf(item.getAttribute("itemId") + ",")<0)
                {
                    item.getParentNode().removeChild(item);
                }
            }
        }

        NodeList tbls = root.getElementsByTagName("table");
        for(int i=0; i<tbls.getLength(); i++)
        {
            Element tbl = (Element)tbls.item(i);


            if( ! tbl.hasChildNodes())
            {
                tbl.getParentNode().removeChild(tbl);
            }
        }



        return doc;
    }
////////////////////////////////////////////////////////////////////////////////
    public Document parseChecks2(String originalDocument, String checks, java.util.ArrayList aList){
        Document doc = MsgCommxml.parseXML(originalDocument);
        Element root = doc.getDocumentElement();

        NodeList items = root.getElementsByTagName("item");

        // since we're removing nodes, we must do this in reverse
        for(int i = items.getLength() - 1; i >= 0; i--)
        {
            Element item = (Element)items.item(i);

            if( ! item.getAttribute("removable").equalsIgnoreCase("false"))
            {
                if(checks.indexOf(item.getAttribute("itemId") + ",")<0)
                {
                    item.getParentNode().removeChild(item);
                }
            }
        }

        NodeList tbls = root.getElementsByTagName("table");
        for(int i=0; i<tbls.getLength(); i++)
        {
            Element tbl = (Element)tbls.item(i);


            if( ! tbl.hasChildNodes())
            {
                tbl.getParentNode().removeChild(tbl);
            }
        }

        tbls = root.getElementsByTagName("table");
        for(int i=0; i<tbls.getLength(); i++){
            Element tbl = (Element)tbls.item(i);
            aList.add(tbl.getAttribute("name"));
        }

        return doc;
    }
}
