package oscar.oscarEncounter.immunization.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilXML;

public class EctImmConfigData {

	public String getImmunizationConfig()
		throws SAXException, ParserConfigurationException, SQLException {
		DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
		String sql =
			"SELECT setName, setXmlDoc FROM config_Immunization WHERE archived=0 AND setName IS NOT NULL AND setName <> '' ORDER BY setName";
		ResultSet rs = db.GetSQL(sql);
		Document doc = UtilXML.newDocument();
		Element root = UtilXML.addNode(doc, "immunization");
		Node newSet;
		for (; rs.next(); root.appendChild(newSet)) {
			Document setDoc = UtilXML.parseXML(rs.getString("setXmlDoc"));
			Element setRoot = setDoc.getDocumentElement();
			newSet = doc.importNode(setRoot, true);
		}

		rs.close();
		db.CloseConn();
		return UtilXML.toXML(doc);
	}

	public Vector getImmunizationConfigName()
		throws SAXException, ParserConfigurationException, SQLException {
		Vector ret = new Vector();
		DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
		String sql =
			"SELECT setName FROM config_Immunization WHERE archived=0 AND setName IS NOT NULL AND setName <> '' ORDER BY setName";
		ResultSet rs = db.GetSQL(sql);
		
		while (rs.next()){
			ret.add(rs.getString("setName"));
		}
		rs.close();
		db.CloseConn();
		return ret;
	}
	
	public Vector getImmunizationConfigId()
	throws SAXException, ParserConfigurationException, SQLException {
		Vector ret = new Vector();
		DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
		String sql =
			"SELECT setId FROM config_Immunization WHERE archived=0 AND setName IS NOT NULL AND setName <> '' ORDER BY setName";
		ResultSet rs = db.GetSQL(sql);
		
		while (rs.next()){
			ret.add(""+rs.getInt("setId"));
		}
		rs.close();
		db.CloseConn();
		return ret;
	}
	
}
