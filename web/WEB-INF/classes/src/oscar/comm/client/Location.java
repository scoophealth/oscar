package oscar.comm.client;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilXML;

class Location {
    private DBHandler db;

    public Location(DBHandler db) {
        this.db = db;
    }

    public Element getLocal(Document doc) throws SQLException {
        Element local = doc.createElement("local");

        ResultSet rs = db.GetSQL("SELECT * FROM oscarcommlocations WHERE current = 1");
        if(rs.next()) {
            UtilXML.addNode(local, "locationId", String.valueOf(rs.getInt("locationId")));
            UtilXML.addNode(local, "locationDesc", rs.getString("locationDesc"));
            UtilXML.addNode(local, "locationAuth", rs.getString("locationAuth"));
        }
        rs.close();

        return local;
    }

    public Element getRemotes(Document doc) throws SQLException {
        Element remoteLocations = doc.createElement("recipients");

        ResultSet rs = db.GetSQL("SELECT * FROM oscarcommlocations WHERE current = 0");
        while(rs.next()) {
            UtilXML.addNode(remoteLocations, "remote").setAttribute("locationId", String.valueOf(rs.getInt("locationId")));
        }
        rs.close();

        return remoteLocations;
    }
}