package oscar.billing.dao;

import oscar.billing.model.Diagnostico;

import oscar.oscarDB.DBHandler;

import oscar.util.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


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
                diagnostico.getCadCid().setCoCid(rs.getString(1));
                diagnostico.getCadCid().setDsCid(rs.getString(2));
                diagnostico.getAppointment().setAppointmentNo(Long.parseLong(id));
				diagnostico.setSave(true);

                list.add(diagnostico);
            }
        } finally {
            db.CloseConn();
        }

        return list;
    }
    
}
