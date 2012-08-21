
package oscar.billing.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oscarehr.util.MiscUtils;

import oscar.billing.model.ProcedimentoRealizado;
import oscar.oscarDB.DBHandler;
import oscar.util.DAO;
import oscar.util.DateUtils;

@Deprecated
public class ProcedimentoRealizadoDAO extends DAO {

	/**
	 * Lists all done procedures within a given appointment.
	 * @param id - the appointment id.
	 * @returns a List of the procedures found.
	 */
    public List<ProcedimentoRealizado> list(String id) throws SQLException {
        ArrayList<ProcedimentoRealizado> list = new ArrayList<ProcedimentoRealizado>();
        String sql =
            "select pr.co_procedimento, p.ds_procedimento, " +
            "pr.dt_realizacao, pr.co_tipo_atendimento " +
            "from cad_procedimento_realizado pr, cad_procedimentos p " +
            "where pr.co_procedimento = p.co_procedimento and " +
            "pr.appointment_no = " + id;

        MiscUtils.getLogger().debug(sql);



        try {
            ResultSet rs = DBHandler.GetSQL(sql);

            // for each procedure found, assemble the
            // object and put it in the list
            while (rs.next()) {
                ProcedimentoRealizado pr = new ProcedimentoRealizado();
                pr.getCadProcedimentos().setCoProcedimento(rs.getLong(1));
                pr.getCadProcedimentos().setDsProcedimento(oscar.Misc.getString(rs, 2));
                pr.setDtRealizacao(rs.getDate(3,
                        DateUtils.getDateFormatter().getCalendar()));
                pr.getTpAtendimento().setCoTipoatendimento(rs.getLong(4));
                pr.getAppointment().setAppointmentNo(Long.parseLong(id));
                pr.setSave(true);
                list.add(pr);
            }
        } finally {
        	//empty
        }

        return list;
    }


	/**
	 * Update the attendance type of all done procedures within a given
	 * appointment. tomita - 14 nov 2003
	 * @param id - the appointment id.
	 * @param atType - the attendance type
	 */
    public void updateAttendanceType(String id, String atType) throws SQLException {

        String sql = "UPDATE cad_procedimento_realizado " +
        	"SET co_tipo_atendimento = '" + atType + "' " +
        	"WHERE appointment_no = " + id;

        MiscUtils.getLogger().debug(sql);


        try {
            DBHandler.RunSQL(sql);
        } finally {
        	//empty
        }

        return;
    }


}
