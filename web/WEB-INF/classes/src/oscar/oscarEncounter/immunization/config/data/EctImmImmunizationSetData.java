/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarEncounter.immunization.config.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.w3c.dom.Document;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilMisc;
import oscar.util.UtilXML;

public class EctImmImmunizationSetData {
	public Vector createDateVec;
	public Vector setIdVec;
	public Vector setNameVec;

	public EctImmImmunizationSetData() {
		setNameVec = new Vector();
		setIdVec = new Vector();
		createDateVec = new Vector();
	}

	public void addImmunizationSet(
		String setName,
		Document setXmlDoc,
		String providerNo) {
		//StringQuote str = new StringQuote();
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			String sql =
				"insert into config_Immunization (setName,setXmlDoc,createDate,ProviderNo) values ('"
					+ UtilMisc.charEscape(setName, '\\')
					+ "','"
					+ UtilMisc.charEscape(UtilXML.toXML(setXmlDoc), '\\')
					+ "',"
					+ " current_date ,'"
					+ UtilMisc.charEscape(providerNo, '\\')
					+ "')";
			db.RunSQL(sql);
		} catch (SQLException eee) {
			System.out.println(eee.getMessage());
		}
	}

	public boolean estImmunizationVecs() {
		boolean verdict = true;
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			String sql =
				"select setId, setName, createDate from config_Immunization where archived = 0";
			ResultSet rs;
			for (rs = db.GetSQL(sql);
				rs.next();
				createDateVec.add(db.getString(rs,"createDate"))) {
				setNameVec.add(db.getString(rs,"setName"));
				setIdVec.add(db.getString(rs,"setId"));
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			verdict = false;
		}
		return verdict;
	}

	public boolean estImmunizationVecs(int stat) {
		boolean verdict = true;
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			String sql =
				"select setId, setName, createDate from config_Immunization where archived = "
					+ stat + " order by setName";
			ResultSet rs;
			for (rs = db.GetSQL(sql);
				rs.next();
				createDateVec.add(db.getString(rs,"createDate"))) {
				setNameVec.add(db.getString(rs,"setName"));
				setIdVec.add(db.getString(rs,"setId"));
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			verdict = false;
		}
		return verdict;
	}

	public String getSetXMLDoc(String setId) {
		String xmlDoc = null;
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			String sql =
				String.valueOf(
					String.valueOf(
						(
							new StringBuffer("select setXmlDoc from config_Immunization where  setId = '"))
							.append(setId)
							.append("'")));
			ResultSet rs = db.GetSQL(sql);
			if (rs.next())
				xmlDoc = db.getString(rs,"setXmlDoc");
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return xmlDoc;
	}

	public void updateImmunizationSetStatus(String setId, int stat) {
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			String sql =
				"update config_Immunization set archived = "
					+ stat
					+ " where setId = "
					+ setId;
			db.RunSQL(sql);
		} catch (SQLException eee) {
			System.out.println(eee.getMessage());
		}
	}
}
