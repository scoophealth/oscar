package oscar.billing.cad.dao;

import oscar.billing.cad.model.CadCid;

import oscar.oscarDB.DBHandler;
import oscar.oscarDB.DBPreparedHandlerAdvanced;

import oscar.util.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class CidDAO extends DAO {
    public CidDAO(Properties pvar) throws SQLException {
        super(pvar);
    }

    public CadCid retrieve(String id) throws SQLException {
        CadCid cid = new CadCid();
        String sql = "select co_cid, ds_cid " +
            "from cad_cid cid " +
            "where co_cid = '" +
            id + "'";

        DBHandler db = getDb();

        try {
            ResultSet rs = db.GetSQL(sql);

            if (rs.next()) {
                cid.setCoCid(rs.getString(1));
                cid.setDsCid(rs.getString(2));
            }
        } finally {
            db.CloseConn();
        }

        return cid;
    }
    
	public List list(String codigo, String desc) throws SQLException {
		List beans = null;
		String sql = "select co_cid, ds_cid from cad_cid where st_registro = 'A'";

		if ((codigo != null) && !codigo.trim().equals("")) {
			sql = sql + " and co_cid = '" + codigo.trim() + "'";
		}

		if ((desc != null) && !desc.trim().equals("")) {
			sql = sql + " and ds_cid like '" +
				desc.trim().toUpperCase() + "%'";
		}

		sql = sql + " order by ds_cid";

		DBPreparedHandlerAdvanced db = getDBPreparedHandlerAdvanced();
		PreparedStatement pstmCid = db.getPrepareStatement(sql);

		try {
			ResultSet rs = db.executeQuery(pstmCid);
			beans = new ArrayList();

			while (rs.next()) {
				CadCid bean = new CadCid();
				bean.setCoCid(rs.getString("co_cid"));
				bean.setDsCid(rs.getString("ds_cid"));
				beans.add(bean);
			}
		} catch (Exception err) {
			err.printStackTrace();
			throw new SQLException(err.getMessage());
		}

		return beans;
	}

}
