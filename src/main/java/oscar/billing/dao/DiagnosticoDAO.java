
package oscar.billing.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oscarehr.util.MiscUtils;

import oscar.billing.model.Diagnostico;
import oscar.oscarDB.DBHandler;
import oscar.util.DAO;

@Deprecated
public class DiagnosticoDAO extends DAO {

    public List<Diagnostico> list(String id) throws SQLException {
        ArrayList<Diagnostico> list = new ArrayList<Diagnostico>();
        String sql = "select diag.co_cid, cid.ds_cid " +
            "from cad_diagnostico diag, cad_cid cid " +
            "where diag.co_cid = cid.co_cid and " + "diag.appointment_no = " +
            id;
		MiscUtils.getLogger().debug(sql);


        ResultSet rs = DBHandler.GetSQL(sql);

        while (rs.next()) {
            Diagnostico diagnostico = new Diagnostico();
            diagnostico.getCadCid().setCoCid(oscar.Misc.getString(rs, 1));
            diagnostico.getCadCid().setDsCid(oscar.Misc.getString(rs, 2));
            diagnostico.getAppointment().setAppointmentNo(Long.parseLong(id));
			diagnostico.setSave(true);

            list.add(diagnostico);
        }


        return list;
    }

}
