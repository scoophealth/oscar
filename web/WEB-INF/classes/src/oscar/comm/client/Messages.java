package oscar.comm.client;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilXML;

class Messages {
    private DBHandler db;

    public Messages(DBHandler db) {
        this.db = db;
    }

    public void addMessageList(Element messageList) throws SQLException {
        NodeList messages = messageList.getChildNodes();

        for(int i=0; i<messages.getLength(); i++) {
            Element msgNode = (Element)messages.item(i);
            MessageData msg = new MessageData(msgNode);

            String sql = "INSERT INTO messagetbl (thedate, theime, sentby, "
                + "sentto, sentbyNo, sentbyLocation, thesubject, themessage, "
                + "attachment, actionstatus) VALUES ('"
                + msg.get("msgDate") + "', '" + msg.get("msgTime") + "', '"
                + msg.get("sentby") + "', '" + msg.get("sentto") + "', '"
                + msg.get("sentbyNo") + "', '" + msg.get("sentbyLocation") + "', '"
                + msg.get("subject") + "', '" + msg.get("msgBody") + "', '"
                + msg.get("attachment") + "', '" + msg.get("actionstatus")
                + "')";
            db.RunSQL(sql);

            int msgId = -1;

            sql = "SELECT LAST_INSERT_ID()";
            ResultSet rs = db.GetSQL(sql);
            if(rs.next()) {
                msgId = rs.getInt(1);
            }
            rs.close();

            for(int j=0; j<msg.recipients.getLength(); j++) {
                String pNo = ((Element)msg.recipients.item(j)).getAttribute("providerNo");
                String loc = ((Element)msg.recipients.item(j)).getAttribute("locationId");

                sql = "INSERT INTO messagelisttbl (message, provider_no, status, remotelocation) VALUES (" + msgId + ", '"
                + pNo + "', 'new', '" + loc + "')";

                db.RunSQL(sql);
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