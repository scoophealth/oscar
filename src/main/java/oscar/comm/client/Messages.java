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


package oscar.comm.client;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilMisc;
import oscar.util.UtilXML;

@Deprecated
class Messages {

    public void addMessageList(Element messageList) throws SQLException {
        NodeList messages = messageList.getChildNodes();

        for(int i=0; i<messages.getLength(); i++) {
            Element msgNode = (Element)messages.item(i);
            MessageData msg = new MessageData(msgNode);

            String sql = "INSERT INTO messagetbl (thedate, theime, sentby, "
                + "sentto, sentbyNo, sentbyLocation, thesubject, themessage, "
                + "attachment, actionstatus) VALUES ('"
                + UtilMisc.mysqlEscape(msg.get("msgDate"))        + "', '" 
                + UtilMisc.mysqlEscape(msg.get("msgTime"))        + "', '"
                + UtilMisc.mysqlEscape(msg.get("sentby"))         + "', '" 
                + UtilMisc.mysqlEscape(msg.get("sentto"))         + "', '"
                + UtilMisc.mysqlEscape(msg.get("sentbyNo"))       + "', '" 
                + UtilMisc.mysqlEscape(msg.get("sentbyLocation")) + "', '"
                + UtilMisc.mysqlEscape(msg.get("subject"))        + "', '" 
                + UtilMisc.mysqlEscape(msg.get("msgBody"))        + "', '"
                + UtilMisc.mysqlEscape(msg.get("attachment"))     + "', '" 
                + UtilMisc.mysqlEscape(msg.get("actionstatus"))   + "')";
                        
            DBHandler.RunSQL(sql);

            int msgId = -1;   

            sql = "SELECT LAST_INSERT_ID()";
            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next()) {
                msgId = rs.getInt(1);
            }
            rs.close();

            for(int j=0; j<msg.recipients.getLength(); j++) {
                String pNo = ((Element)msg.recipients.item(j)).getAttribute("providerNo");
                String loc = ((Element)msg.recipients.item(j)).getAttribute("locationId");

                sql = "INSERT INTO messagelisttbl (message, provider_no, status, remotelocation) VALUES (" + msgId + ", '"
                + pNo + "', 'new', '" + loc + "')";

                DBHandler.RunSQL(sql);
            }
        }
    }

    class MessageData {
        private java.util.Properties fld = new java.util.Properties();
        NodeList recipients;

        MessageData(Element message) {
            NodeList lst = message.getChildNodes();

            for(int i=0; i<lst.getLength(); i++) {
                Node nod = lst.item(i);            
                
                if(nod.getNodeType() == Node.ELEMENT_NODE) {
                    if(nod.getNodeName().equals("recipient")==false) {                       
                          fld.setProperty(nod.getNodeName(), UtilXML.getText(nod));                                              
                    }
                }
            }

            recipients = message.getElementsByTagName("recipient");
        }

        String get(String fieldName) {
            return fld.getProperty(fieldName);
        }
    }
}
