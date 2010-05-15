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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.billing.cad.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import oscar.billing.cad.model.CadCid;
import oscar.oscarDB.DBHandler;
import oscar.oscarDB.DBPreparedHandlerAdvanced;
import oscar.util.DAO;


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

		DBPreparedHandlerAdvanced db = new DBPreparedHandlerAdvanced();
		PreparedStatement pstmCid = db.getPrepareStatement(sql);

		try {
			ResultSet rs = db.executeQuery(pstmCid);
			beans = new ArrayList();

			while (rs.next()) {
				CadCid bean = new CadCid();
				bean.setCoCid(db.getString(rs,"co_cid"));
				bean.setDsCid(db.getString(rs,"ds_cid"));
				beans.add(bean);
			}
		} catch (Exception err) {
			err.printStackTrace();
			throw new SQLException(err.getMessage());
		}

		return beans;
	}

}
