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
package oscar.billing.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import oscar.billing.model.Diagnostico;
import oscar.oscarDB.DBHandler;
import oscar.util.DAO;


public class DiagnosticoDAO extends DAO {
    public DiagnosticoDAO(Properties pvar) throws SQLException {
        super(pvar);
    }

    public List list(String id) throws SQLException {
        ArrayList list = new ArrayList();
        String sql = "select diag.co_cid, cid.ds_cid " +
            "from cad_diagnostico diag, cad_cid cid " +
            "where diag.co_cid = cid.co_cid and " + "diag.appointment_no = " +
            id;
		System.out.println(sql);
        DBHandler db = getDb();

        try {
            ResultSet rs = db.GetSQL(sql);

            while (rs.next()) {
                Diagnostico diagnostico = new Diagnostico();
                diagnostico.getCadCid().setCoCid(db.getString(rs,1));
                diagnostico.getCadCid().setDsCid(db.getString(rs,2));
                diagnostico.getAppointment().setAppointmentNo(Long.parseLong(id));
				diagnostico.setSave(true);

                list.add(diagnostico);
            }
        } finally {
        }

        return list;
    }
    
}
