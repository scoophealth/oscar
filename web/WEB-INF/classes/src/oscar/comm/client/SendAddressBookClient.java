package oscar.comm.client;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilXML;

public class SendAddressBookClient {
    public boolean sendAddressBook(String databaseURL, String databaseName)
            throws OscarCommClientException, SQLException {
        boolean ret = false;
        String wsURL = null;
        DBHandler db = null;
        WebServiceClient client = null;

        try {
            db = new DBHandler(databaseURL, databaseName);
            System.out.println("");

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
            Element request;
            Element response;

            request = createRequest(db);
            response = client.callSendRequest(request);

            if(response!=null) {
                parseResponse(response, db);
                ret = true;
            }
        } catch (Exception ex) {
            throw new OscarCommClientException("Error occurred parsing response from web service.", ex);
        }

        db.CloseConn();

        return ret;
    }

    private Element createRequest(DBHandler db) throws SQLException {
        Document doc = UtilXML.newDocument();
        Element root = UtilXML.addNode(doc, "request");

        root.appendChild(new Location(db).getLocal(doc));
        root.appendChild(new AddressBook(db).getLocalAddressBook(doc));

        return root;
    }

    private void parseResponse(Element response, DBHandler db) throws SQLException {
        NodeList rootChildren = response.getChildNodes();

        for(int i=0; i<rootChildren.getLength(); i++) {
            Element element = (Element)rootChildren.item(i);

            if(element.getNodeName().equalsIgnoreCase("remoteAddressBooks")) {
                new AddressBook(db).setRemoteAddressBooks(element);
            }

            if(element.getNodeName().equalsIgnoreCase("messageList")) {
                new Messages(db).addMessageList(element);
            }
        }
    }
}