package oscar.billing.dao;

import oscar.billing.model.ProcedimentoRealizado;

import oscar.oscarDB.DBHandler;

import oscar.util.DAO;
import oscar.util.DateUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class ProcedimentoRealizadoDAO extends DAO {
    public ProcedimentoRealizadoDAO(Properties pvar) throws SQLException {
        super(pvar);
    }

    public List list(String id) throws SQLException {
        ArrayList list = new ArrayList();
        String sql =
            "select pr.co_procedimento, p.ds_procedimento, pr.dt_realizacao " +
            "from cad_procedimento_realizado pr, cad_procedimentos p " +
            "where pr.co_procedimento = p.co_procedimento and " +
            "pr.appointment_no = " + id;
            
        System.out.println(sql);

        DBHandler db = getDb();

        try {
            ResultSet rs = db.GetSQL(sql);

            while (rs.next()) {
                ProcedimentoRealizado pr = new ProcedimentoRealizado();
                pr.getCadProcedimentos().setCoProcedimento(rs.getLong(1));
                pr.getCadProcedimentos().setDsProcedimento(rs.getString(2));
                pr.setDtRealizacao(rs.getDate(3,
                        DateUtils.getDateFormatter().getCalendar()));
                pr.getAppointment().setAppointmentNo(Long.parseLong(id));
                pr.setSave(true);
                list.add(pr);
            }
        } finally {
            db.CloseConn();
        }

        return list;
    }
}
