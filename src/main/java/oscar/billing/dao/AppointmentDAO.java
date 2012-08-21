/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.billing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.oscarehr.common.dao.AppointmentArchiveDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.billing.model.Appointment;
import oscar.billing.model.Diagnostico;
import oscar.billing.model.ProcedimentoRealizado;
import oscar.billing.model.Provider;
import oscar.oscarDB.DBHandler;
import oscar.util.DAO;
import oscar.util.DateUtils;
import oscar.util.FieldTypes;
import oscar.util.SqlUtils;

@Deprecated
public class AppointmentDAO extends DAO {

	AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
    OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");


	public void billing(Appointment app) throws SQLException {
		String sqlProc;
		String sqlDiag;
		String sqlApp;

		sqlProc = "insert into cad_procedimento_realizado (appointment_no, co_procedimento, dt_realizacao) values (?, ?, ?)";
		sqlDiag = "insert into cad_diagnostico (appointment_no, co_cid) values (?, ?)";
		sqlApp = "update appointment set billing = 'P' where appointment_no = ?";

		Connection c = DbConnectionFilter.getThreadLocalDbConnection();
		c.setAutoCommit(false);
		PreparedStatement pstmProc = c.prepareStatement(sqlProc);
		PreparedStatement pstmDiag = c.prepareStatement(sqlDiag);
		PreparedStatement pstmApp = c.prepareStatement(sqlApp);

		try {
			unBilling(app, c);

			for (int i = 0; i < app.getProcedimentoRealizado().size(); i++) {
				ProcedimentoRealizado pr = app.getProcedimentoRealizado().get(i);

				// appoitment_no
				SqlUtils.fillPreparedStatement(pstmProc, 1, new Long(pr.getAppointment().getAppointmentNo()), FieldTypes.LONG);
				SqlUtils.fillPreparedStatement(pstmProc, 2, new Long(pr.getCadProcedimentos().getCoProcedimento()), FieldTypes.LONG);
				SqlUtils.fillPreparedStatement(pstmProc, 3, DateUtils.formatDate(DateUtils.getDate(pr.getDtRealizacao()), "dd/MM/yyyy"), FieldTypes.DATE);
				pstmProc.executeUpdate();
			}

			for (int i = 0; i < app.getDiagnostico().size(); i++) {
				Diagnostico diag = app.getDiagnostico().get(i);

				// appoitment_no
				SqlUtils.fillPreparedStatement(pstmDiag, 1, new Long(diag.getAppointment().getAppointmentNo()), FieldTypes.LONG);
				SqlUtils.fillPreparedStatement(pstmDiag, 2, diag.getCadCid().getCoCid(), FieldTypes.CHAR);
				pstmDiag.executeUpdate();
			}

			org.oscarehr.common.model.Appointment appt = appointmentDao.find((int)app.getAppointmentNo());
			appointmentArchiveDao.archiveAppointment(appt);

			SqlUtils.fillPreparedStatement(pstmApp, 1, String.valueOf(app.getAppointmentNo()), FieldTypes.LONG);
			pstmApp.executeUpdate();

			c.commit();
		} catch (Exception e) {
			c.rollback();
			MiscUtils.getLogger().error("Error", e);
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

		Connection c = DbConnectionFilter.getThreadLocalDbConnection();
		c.setAutoCommit(false);
		PreparedStatement pstmProc = c.prepareStatement(sqlProc);
		PreparedStatement pstmDiag = c.prepareStatement(sqlDiag);

		try {
			SqlUtils.fillPreparedStatement(pstmProc, 1, new Long(app.getAppointmentNo()), FieldTypes.LONG);
			pstmProc.executeUpdate();

			SqlUtils.fillPreparedStatement(pstmDiag, 1, new Long(app.getAppointmentNo()), FieldTypes.LONG);
			pstmDiag.executeUpdate();

			c.commit();
		} catch (Exception e) {
			c.rollback();
			MiscUtils.getLogger().error("Error", e);
			throw new SQLException(e.toString());
		} finally {
			pstmDiag.close();
			pstmProc.close();
		}
	}

	public void unBilling(Appointment app, Connection c) throws SQLException {
		String sqlProc;
		String sqlDiag;

		sqlProc = "delete from cad_procedimento_realizado where appointment_no = ?";
		sqlDiag = "delete from cad_diagnostico where appointment_no = ?";

		PreparedStatement pstmProc = c.prepareStatement(sqlProc);
		PreparedStatement pstmDiag = c.prepareStatement(sqlDiag);

		try {
			SqlUtils.fillPreparedStatement(pstmProc, 1, new Long(app.getAppointmentNo()), FieldTypes.LONG);
			pstmProc.executeUpdate();

			SqlUtils.fillPreparedStatement(pstmDiag, 1, new Long(app.getAppointmentNo()), FieldTypes.LONG);
			pstmDiag.executeUpdate();
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
			throw new SQLException(e.toString());
		} finally {
			pstmDiag.close();
			pstmProc.close();
		}
	}

	public Appointment retrieve(String id) throws SQLException {
		Appointment appointment = new Appointment();
		String sql = "select app.appointment_no, app.provider_no, prov.first_name, prov.last_name, app.demographic_no, dem.first_name, dem.last_name, app.name, app.reason, app.appointment_date " + "from appointment app, provider prov, demographic dem " + "where app.provider_no = prov.provider_no and " + "app.demographic_no = dem.demographic_no and " + "app.appointment_no = " + id;

		MiscUtils.getLogger().debug("sql = " + sql);



		try {
			ResultSet rs = DBHandler.GetSQL(sql);

			if (rs.next()) {
				appointment.setAppointmentNo(rs.getLong(1));
				appointment.getProvider().setProviderNo(oscar.Misc.getString(rs, 2));
				appointment.getProvider().setFirstName(oscar.Misc.getString(rs, 3));
				appointment.getProvider().setLastName(oscar.Misc.getString(rs, 4));
				appointment.getDemographic().setDemographicNo(rs.getInt(5));
				appointment.getDemographic().setFirstName(oscar.Misc.getString(rs, 6));
				appointment.getDemographic().setLastName(oscar.Misc.getString(rs, 7));
				appointment.setName(oscar.Misc.getString(rs, 8));
				appointment.setReason(oscar.Misc.getString(rs, 9));
				appointment.setAppointmentDate(rs.getDate(10));
			}
		} finally {
			//empty
		}

		return appointment;
	}

	public ArrayList<Appointment> listFatDoctor(String type, Provider provider) throws SQLException {
		ArrayList<Appointment> list = new ArrayList<Appointment>();
		String sql = "select a.appointment_no, a.appointment_date, a.provider_no, b.last_name, " + "b.first_name, a.demographic_no, c.last_name, c.first_name " + "from appointment a, provider b, demographic c " + "where a.provider_no = b.provider_no and " + "a.demographic_no = c.demographic_no ";

		if (type.equals(Appointment.AGENDADO)) {
			sql = sql + " and a.billing = '" + Appointment.AGENDADO + "'";
		} else if (type.equals(Appointment.FATURADO)) {
			sql = sql + " and a.billing = '" + Appointment.FATURADO + "'";
		} else if (type.equals(Appointment.PENDENTE)) {
			sql = sql + " and a.billing is null";
		}

		if (provider != null && !provider.getProviderNo().trim().equals("0")) {
			sql = sql + " and a.provider_no = " + provider.getProviderNo().trim();
		}

		sql = sql + " order by a.appointment_date desc";



		try {
			ResultSet rs = DBHandler.GetSQL(sql);

			while (rs.next()) {
				Appointment app = new Appointment();
				app.setAppointmentNo(rs.getLong(1));
				app.setAppointmentDate(rs.getDate(2));
				app.getProvider().setProviderNo(oscar.Misc.getString(rs, 3));
				app.getProvider().setLastName(oscar.Misc.getString(rs, 4));
				app.getProvider().setFirstName(oscar.Misc.getString(rs, 5));
				app.getDemographic().setDemographicNo(rs.getInt(6));
				app.getDemographic().setLastName(oscar.Misc.getString(rs, 7));
				app.getDemographic().setFirstName(oscar.Misc.getString(rs, 8));

				list.add(app);
			}
		} finally {
			//empty
		}

		return list;
	}

	public ArrayList<Appointment> listFatPatiente(Demographic demographic) throws SQLException {
		ArrayList<Appointment> list = new ArrayList<Appointment>();
		String sql = "select a.appointment_no, a.appointment_date, a.provider_no, b.last_name, " + "b.first_name, a.billing " + "from appointment a, provider b, demographic c " + "where a.provider_no = b.provider_no and " + "a.demographic_no = c.demographic_no and " + "a.demographic_no = " + demographic.getDemographicNo() + " and " + "a.billing is not null " + "order by a.appointment_date desc";



		try {
			ResultSet rs = DBHandler.GetSQL(sql);

			while (rs.next()) {
				Appointment app = new Appointment();
				app.setAppointmentNo(rs.getLong(1));
				app.setAppointmentDate(rs.getDate(2));
				app.getProvider().setProviderNo(oscar.Misc.getString(rs, 3));
				app.getProvider().setLastName(oscar.Misc.getString(rs, 4));
				app.getProvider().setFirstName(oscar.Misc.getString(rs, 5));
				app.setBilling(oscar.Misc.getString(rs, 6));

				list.add(app);
			}
		} finally {
			//empty
		}

		return list;
	}
}
