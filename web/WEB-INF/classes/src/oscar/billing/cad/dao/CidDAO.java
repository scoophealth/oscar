package oscar.billing.cad.dao;

import oscar.billing.cad.model.CadCid;

import oscar.oscarDB.DBHandler;

import oscar.util.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

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
    
}
