package oscar.comm.client;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilXML;

public class SendMessageClient {
    public boolean sendMessage(String databaseURL, String databaseName, String messageXML)
            throws OscarCommClientException, java.sql.SQLException {
        boolean ret = false;
        DBHandler db = null;
        String wsURL = null;
        WebServiceClient client = null;

        try {
            db = new DBHandler(databaseURL, databaseName);

            //System.out.println("");
            String sql = "SELECT remoteServerURL FROM oscarcommlocations WHERE current = 1";
            ResultSet rs = db.GetSQL(sql);
            if(rs.next()) {
                wsURL = rs.getString("remoteServerURL");
            }
            rs.close();
        } catch (Exception ex) {
            throw new OscarCommClientException("Could not connect to database: " + databaseURL + databaseName, ex);
        }

        client = new WebServiceClient(wsURL);

        try {
            String s = UtilXML.toXML(client.callSendMessage(createRequest(db, messageXML)));
            ret = (s.indexOf("<response/>") > -1);
        } catch (Exception ex) {
            throw new OscarCommClientException("Send Message failed.", ex);
        }

        db.CloseConn();
        return ret;
    }

    private Element createRequest(DBHandler db, String messageXML) throws SQLException {
        Document doc = UtilXML.newDocument();
        Element root = UtilXML.addNode(doc, "sendMessage");
        root.appendChild(new Location(db).getLocal(doc));

        Element message = (Element)doc.importNode(UtilXML.parseXML(messageXML).getDocumentElement(), true);
        root.appendChild(message);

        return root;
    }
}