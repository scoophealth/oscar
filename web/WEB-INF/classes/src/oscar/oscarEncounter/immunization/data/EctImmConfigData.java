// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
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
			Document setDoc = UtilXML.parseXML(db.getString(rs,"setXmlDoc"));
			Element setRoot = setDoc.getDocumentElement();
			newSet = doc.importNode(setRoot, true);
		}

		rs.close();
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
			ret.add(db.getString(rs,"setName"));
		}
		rs.close();
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
		return ret;
	}
	
}
