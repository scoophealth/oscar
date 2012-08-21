
package oscar.billing.cad.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

import oscar.billing.cad.model.CadCid;
import oscar.oscarDB.DBHandler;
import oscar.util.DAO;

@Deprecated
public class CidDAO extends DAO {

    public CadCid retrieve(String id) throws SQLException {
        CadCid cid = new CadCid();
        String sql = "select co_cid, ds_cid " +
            "from cad_cid cid " +
            "where co_cid = '" +
            id + "'";



        try {
            ResultSet rs = DBHandler.GetSQL(sql);

            if (rs.next()) {
                cid.setCoCid(rs.getString(1));
                cid.setDsCid(rs.getString(2));
            }
        } finally {
        	//empty
        }

        return cid;
    }

	public List<CadCid> list(String codigo, String desc) throws SQLException {
		List<CadCid> beans = null;
		String sql = "select co_cid, ds_cid from cad_cid where st_registro = 'A'";

		if ((codigo != null) && !codigo.trim().equals("")) {
			sql = sql + " and co_cid = '" + codigo.trim() + "'";
		}

		if ((desc != null) && !desc.trim().equals("")) {
			sql = sql + " and ds_cid like '" +
				desc.trim().toUpperCase() + "%'";
		}

		sql = sql + " order by ds_cid";

		Connection c=DbConnectionFilter.getThreadLocalDbConnection();
		PreparedStatement pstmCid = c.prepareStatement(sql);

		try {
			ResultSet rs = pstmCid.executeQuery();
			beans = new ArrayList<CadCid>();

			while (rs.next()) {
				CadCid bean = new CadCid();
				String temp=rs.getString("co_cid");
				if (temp==null) temp="";
				bean.setCoCid(temp);

				temp=rs.getString("ds_cid");
				if (temp==null) temp="";
				bean.setDsCid(temp);
				beans.add(bean);
			}
		} catch (Exception err) {MiscUtils.getLogger().error("Error", err);
			throw new SQLException(err.getMessage());
		}

		return beans;
	}

}
