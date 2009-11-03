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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.billing.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import oscar.billing.model.Appointment;
import oscar.billing.model.Demographic;
import oscar.billing.model.Diagnostico;
import oscar.billing.model.ProcedimentoRealizado;
import oscar.billing.model.Provider;
import oscar.oscarDB.DBHandler;
import oscar.oscarDB.DBPreparedHandlerAdvanced;
import oscar.util.DAO;
import oscar.util.DateUtils;
import oscar.util.FieldTypes;
import oscar.util.SqlUtils;


public class AppointmentDAO extends DAO {
    public AppointmentDAO(Properties pvar) throws SQLException {
        super(pvar);
    }

    public void billing(Appointment app) throws SQLException {
        String sqlProc;
        String sqlDiag;
		String sqlApp;

        sqlProc = "insert into cad_procedimento_realizado (appointment_no, co_procedimento, dt_realizacao) values (?, ?, ?)";
        sqlDiag = "insert into cad_diagnostico (appointment_no, co_cid) values (?, ?)";
        sqlApp = "update appointment set billing = 'P' where appointment_no = ?";

        DBPreparedHandlerAdvanced db = new DBPreparedHandlerAdvanced();
        PreparedStatement pstmProc = db.getPrepareStatement(sqlProc);
        PreparedStatement pstmDiag = db.getPrepareStatement(sqlDiag);
		PreparedStatement pstmApp = db.getPrepareStatement(sqlApp);

        db.setAutoCommit(false);

        try {
            unBilling(app, db);

            for (int i = 0; i < app.getProcedimentoRealizado().size(); i++) {
                ProcedimentoRealizado pr = (ProcedimentoRealizado) app.getProcedimentoRealizado()
                                                                      .get(i);

                //appoitment_no
                SqlUtils.fillPreparedStatement(pstmProc, 1,
                    new Long(pr.getAppointment().getAppointmentNo()),
                    FieldTypes.LONG);
                SqlUtils.fillPreparedStatement(pstmProc, 2,
                    new Long(pr.getCadProcedimentos().getCoProcedimento()),
                    FieldTypes.LONG);
                SqlUtils.fillPreparedStatement(pstmProc, 3,
                    DateUtils.formatDate(DateUtils.getDate(pr.getDtRealizacao()),
                        "dd/MM/yyyy"), FieldTypes.DATE);
                db.execute(pstmProc);
            }

            for (int i = 0; i < app.getDiagnostico().size(); i++) {
                Diagnostico diag = (Diagnostico) app.getDiagnostico().get(i);

                //appoitment_no
                SqlUtils.fillPreparedStatement(pstmDiag, 1,
                    new Long(diag.getAppointment().getAppointmentNo()),
                    FieldTypes.LONG);
                SqlUtils.fillPreparedStatement(pstmDiag, 2,
                    diag.getCadCid().getCoCid(), FieldTypes.CHAR);
                db.execute(pstmDiag);
            }
            
			SqlUtils.fillPreparedStatement(pstmApp, 1,
				String.valueOf(app.getAppointmentNo()), FieldTypes.LONG);
			db.execute(pstmApp);

            db.commit();
        } catch (Exception e) {
            db.rollback();
            e.printStackTrace();
            throw new SQLException(e.toString());
        } finally {
            pstmDiag.close();
            pstmProc.close();
        }
    }

    public void unBilling(Appointment app) throws SQLException {
        String sqlProc;
        String sqlDiag;

        sqlProc = "delete from cad_procedimento_realizado where appointment_no = ?";
        sqlDiag = "delete from cad_diagnostico where appointment_no = ?";

        DBPreparedHandlerAdvanced db = new DBPreparedHandlerAdvanced();
        PreparedStatement pstmProc = db.getPrepareStatement(sqlProc);
        PreparedStatement pstmDiag = db.getPrepareStatement(sqlDiag);

        db.setAutoCommit(false);

        try {
            SqlUtils.fillPreparedStatement(pstmProc, 1,
                new Long(app.getAppointmentNo()), FieldTypes.LONG);
            db.execute(pstmProc);

            SqlUtils.fillPreparedStatement(pstmDiag, 1,
                new Long(app.getAppointmentNo()), FieldTypes.LONG);
            db.execute(pstmDiag);

            db.commit();
        } catch (Exception e) {
            db.rollback();
            e.printStackTrace();
            throw new SQLException(e.toString());
        } finally {
            pstmDiag.close();
            pstmProc.close();
        }
    }

    public void unBilling(Appointment app, DBPreparedHandlerAdvanced db)
        throws SQLException {
        String sqlProc;
        String sqlDiag;

        sqlProc = "delete from cad_procedimento_realizado where appointment_no = ?";
        sqlDiag = "delete from cad_diagnostico where appointment_no = ?";

        PreparedStatement pstmProc = db.getPrepareStatement(sqlProc);
        PreparedStatement pstmDiag = db.getPrepareStatement(sqlDiag);

        try {
            SqlUtils.fillPreparedStatement(pstmProc, 1,
                new Long(app.getAppointmentNo()), FieldTypes.LONG);
            db.execute(pstmProc);

            SqlUtils.fillPreparedStatement(pstmDiag, 1,
                new Long(app.getAppointmentNo()), FieldTypes.LONG);
            db.execute(pstmDiag);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e.toString());
        } finally {
            pstmDiag.close();
            pstmProc.close();
        }
    }

    public Appointment retrieve(String id) throws SQLException {
        Appointment appointment = new Appointment();
        String sql =
            "select app.appointment_no, app.provider_no, prov.first_name, prov.last_name, app.demographic_no, dem.first_name, dem.last_name, app.name, app.reason, app.appointment_date " +
            "from appointment app, provider prov, demographic dem " +
            "where app.provider_no = prov.provider_no and " +
            "app.demographic_no = dem.demographic_no and " +
            "app.appointment_no = " + id;

        System.out.println("sql = " + sql);
        
        DBHandler db = getDb();

        try {
            ResultSet rs = db.GetSQL(sql);

            if (rs.next()) {
                appointment.setAppointmentNo(rs.getLong(1));
                appointment.getProvider().setProviderNo(db.getString(rs,2));
                appointment.getProvider().setFirstName(db.getString(rs,3));
                appointment.getProvider().setLastName(db.getString(rs,4));
                appointment.getDemographic().setDemographicNo(rs.getLong(5));
                appointment.getDemographic().setFirstName(db.getString(rs,6));
                appointment.getDemographic().setLastName(db.getString(rs,7));
                appointment.setName(db.getString(rs,8));
                appointment.setReason(db.getString(rs,9));
				appointment.setAppointmentDate(rs.getDate(10));
            }
        } finally {
        }

        return appointment;
    }

    public ArrayList listFatDoctor(String type, Provider provider)
        throws SQLException {
        ArrayList list = new ArrayList();
        String sql =
            "select a.appointment_no, a.appointment_date, a.provider_no, b.last_name, " +
            "b.first_name, a.demographic_no, c.last_name, c.first_name " +
            "from appointment a, provider b, demographic c " +
            "where a.provider_no = b.provider_no and " +
            "a.demographic_no = c.demographic_no ";

        if (type.equals(Appointment.AGENDADO)) {
            sql = sql + " and a.billing = '" + Appointment.AGENDADO + "'";
        } else if (type.equals(Appointment.FATURADO)) {
			sql = sql + " and a.billing = '" + Appointment.FATURADO + "'";
        } else if (type.equals(Appointment.PENDENTE)) {
            sql = sql + " and a.billing is null";
        }
        
        if (provider != null && !provider.getProviderNo().trim().equals("0")) {
			sql = sql + " and a.provider_no = " +  provider.getProviderNo().trim();
        }

        sql = sql + " order by a.appointment_date desc";

        DBHandler db = getDb();

        try {
            ResultSet rs = db.GetSQL(sql);

            while (rs.next()) {
            	Appointment app = new Appointment();
            	app.setAppointmentNo(rs.getLong(1));
            	app.setAppointmentDate(rs.getDate(2));
            	app.getProvider().setProviderNo(db.getString(rs,3));
            	app.getProvider().setLastName(db.getString(rs,4));
            	app.getProvider().setFirstName(db.getString(rs,5));
				app.getDemographic().setDemographicNo(rs.getLong(6));
				app.getDemographic().setLastName(db.getString(rs,7));
				app.getDemographic().setFirstName(db.getString(rs,8));
				
				list.add(app);            	
            }
        } finally {
        }

        return list;
    }

	public ArrayList listFatPatiente(Demographic demographic)
		throws SQLException {
		ArrayList list = new ArrayList();
		String sql =
			"select a.appointment_no, a.appointment_date, a.provider_no, b.last_name, " +
			"b.first_name, a.billing " +
			"from appointment a, provider b, demographic c " +
			"where a.provider_no = b.provider_no and " +
			"a.demographic_no = c.demographic_no and " +
			"a.demographic_no = " + demographic.getDemographicNo() + " and " +
			"a.billing is not null " +
			"order by a.appointment_date desc";

		DBHandler db = getDb();

		try {
			ResultSet rs = db.GetSQL(sql);

			while (rs.next()) {
				Appointment app = new Appointment();
				app.setAppointmentNo(rs.getLong(1));
				app.setAppointmentDate(rs.getDate(2));
				app.getProvider().setProviderNo(db.getString(rs,3));
				app.getProvider().setLastName(db.getString(rs,4));
				app.getProvider().setFirstName(db.getString(rs,5));
				app.setBilling(db.getString(rs,6));
				
				list.add(app);            	
			}
		} finally {
		}

		return list;
	}
}
