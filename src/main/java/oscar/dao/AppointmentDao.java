package oscar.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.jdbc.core.RowMapper;

import oscar.appt.ApptData;

/**
 * Oscar Appointment DAO implementation created to extract database access code
 * from appointment related JSP files. This class contains only actual sql
 * queries and row mappers.
 * 
 * @author Eugene Petruhin
 * 
 */
public class AppointmentDao extends OscarSuperDao {

	private Map<String, RowMapper> rowMappers = new TreeMap<String, RowMapper>();

	public AppointmentDao() {
		rowMappers.put("export_appt", new ExportApptDataRowMapper());
	}

	private String [][] dbQueries = new String[][] { 
            {"search_appt", "select count(appointment_no) AS n from appointment where appointment_date = ? and provider_no = ? and status !='C' and ((start_time>= ? and start_time<= ?) or (end_time>= ? and end_time<= ?) or (start_time<= ? and end_time>= ?) ) and program_id=?" },
            {"search_appt_name", "select name from appointment where appointment_date = ? and provider_no = ? and status !='C' and ((start_time>= ? and start_time<= ?) or (end_time>= ? and end_time<= ?) or (start_time<= ? and end_time>= ?) ) and program_id=?" },
            {"search_provider_name", "select last_name,first_name from provider where provider_no= ? " },
            {"search_demographic_statusroster", "select * from demographic where demographic_no = ? " },
            {"search_demographiccust_alert", "select cust3 from demographiccust where demographic_no = ? " },
            {"search_appt_future", "select appt.appointment_date, appt.start_time, appt.status, p.last_name, p.first_name from appointment appt, provider p where appt.provider_no = p.provider_no and appt.demographic_no = ? and appt.appointment_date >= ? and appt.appointment_date < ? order by appointment_date desc, start_time desc" },
            {"search_appt_past", "select appt.appointment_date, appt.start_time, appt.status, p.last_name, p.first_name from appointment appt, provider p where appt.provider_no = p.provider_no and appt.demographic_no = ? and appt.appointment_date < ? and appt.appointment_date > ? order by appointment_date desc, start_time desc"},
            {"search_appt_no", "select appointment_no from appointment where provider_no=? and appointment_date=? and start_time=? and end_time=? and createdatetime=? and creator=? and demographic_no=? order by appointment_no desc limit 1"},

            {"add_apptrecord", "insert into appointment (provider_no,appointment_date,start_time,end_time,name, notes,reason,location,resources,type, style,billing,status,createdatetime,creator, remarks, demographic_no, program_id) values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?)" },
            {"search_waitinglist", "select wl.listID, wln.name from waitingList wl, waitingListName wln where wl.demographic_no=? and wln.ID=wl.listID and wl.is_history ='N' order by wl.listID"},

            {"updatestatusc", "update appointment set status=?, lastupdateuser=?, updatedatetime=now() where appointment_no=?"},
            {"update_apptrecord", "update appointment set demographic_no=?,appointment_date=?,start_time=?,end_time=?,name=?, notes=?,reason =?,location=?, resources=?, type=?,style=?,billing =?,status=?,lastupdateuser=?,remarks=?,updatedatetime=now() where appointment_no=? "},

            {"search", "select * from appointment where appointment_no=?"},
            {"search_detail", "select * from demographic where demographic_no=?"},

            {"delete", "delete from appointment where appointment_no=?"},

            {"search_otherappt", "select * from appointment where appointment_date=? and ((start_time <= ? and end_time >= ?) or (start_time > ? and start_time < ?) ) order by provider_no, start_time" },
            {"search_groupprovider", "select p.last_name, p.first_name, p.provider_no from mygroup m, provider p where m.mygroup_no=? and m.provider_no=p.provider_no order by p.last_name"},
            {"search_scheduledate_single", "select * from scheduledate where sdate=? and provider_no=?" },

            {"cancel_appt", "update appointment set status = ?, updatedatetime = ?, lastupdateuser = ? where appointment_date=? and provider_no=? and start_time=? and end_time=? and name=? and notes=? and reason=? and createdatetime like ?  and creator=? and demographic_no=? " },
            {"delete_appt", "delete from appointment where appointment_date=? and provider_no=? and start_time=? and end_time=? and name=? and notes=? and reason=? and createdatetime like ?  and creator=? and demographic_no=? " },
            {"update_appt", "update appointment set start_time=?, end_time=?, name=?, demographic_no=?, notes=?, reason=?, location=?, resources=?, updatedatetime=?, lastupdateuser=? where appointment_date=? and provider_no=? and start_time=? and end_time=? and name=? and notes=? and reason=? and createdatetime like ?  and creator=? and demographic_no=?" },

            {"archive_appt", "insert into appointmentArchive (select * from appointment where appointment_no=?)"},
            {"archive_group", "insert into appointmentArchive (select * from appointment where appointment_date=? and provider_no=? and start_time=? and end_time=? and name=? and notes=? and reason=? and createdatetime like ?  and creator=? and demographic_no=?"},

            {"import_appt", "insert into appointment (provider_no,appointment_date,start_time,end_time,name,demographic_no,notes,reason,status) values (?,?,?,?,?,?,?,?,?)" },
            {"export_appt", "select app.*, prov.first_name, prov.last_name, prov.ohip_no from appointment app, provider prov where app.provider_no = prov.provider_no and app.demographic_no = ?" }
	};

    /**
     * Maps sql result set row to ApptData value object for export.
     */
    protected class ExportApptDataRowMapper implements RowMapper {

// annotated inner classes not supported till 1.6
//		@Override
        public Object mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			ApptData ad = new ApptData();

			ad.setAppointment_no(rs.getString("appointment_no"));
			ad.setAppointment_date(rs.getString("appointment_date"));
			ad.setStart_time(rs.getString("start_time"));
			ad.setEnd_time(rs.getString("end_time"));
			ad.setNotes(rs.getString("notes"));
			ad.setReason(rs.getString("reason"));
			ad.setStatus(rs.getString("status"));
			ad.setProviderFirstName(rs.getString("prov.first_name"));
			ad.setProviderLastName(rs.getString("prov.last_name"));
			ad.setOhipNo(rs.getString("prov.ohip_no"));

			return ad;
        }
    }

	/**
	 * Need to provide this method in order to let parent.getDbQueries() access child.dbQueries array.
	 */
	@Override
	protected String[][] getDbQueries() {
		return dbQueries;
	}

	/**
	 * Need to provide this method in order to let parent.getRowMappers() access child.rowMappers map.
	 */
	@Override
	protected Map<String, RowMapper> getRowMappers() {
		return rowMappers;
	}
}
