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
					+ " curdate() ,'"
					+ UtilMisc.charEscape(providerNo, '\\')
					+ "')";
			db.RunSQL(sql);
			db.CloseConn();
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
				createDateVec.add(rs.getString("createDate"))) {
				setNameVec.add(rs.getString("setName"));
				setIdVec.add(rs.getString("setId"));
			}

			rs.close();
			db.CloseConn();
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
				createDateVec.add(rs.getString("createDate"))) {
				setNameVec.add(rs.getString("setName"));
				setIdVec.add(rs.getString("setId"));
			}

			rs.close();
			db.CloseConn();
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
				xmlDoc = rs.getString("setXmlDoc");
			rs.close();
			db.CloseConn();
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
			db.CloseConn();
		} catch (SQLException eee) {
			System.out.println(eee.getMessage());
		}
	}
}
