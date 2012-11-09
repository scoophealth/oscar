/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

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
            {"search_appt_future", "select appt.appointment_date, appt.start_time, appt.status, p.last_name, p.first_name from appointment appt, provider p where appt.provider_no = p.provider_no and appt.demographic_no = ? and appt.appointment_date >= ? and appt.appointment_date < ? order by appointment_date desc, start_time desc" },
            {"search_appt_past", "select appt.appointment_date, appt.start_time, appt.status, p.last_name, p.first_name from appointment appt, provider p where appt.provider_no = p.provider_no and appt.demographic_no = ? and appt.appointment_date < ? and appt.appointment_date > ? order by appointment_date desc, start_time desc"},
            {"search_appt_no", "select appointment_no from appointment where provider_no=? and appointment_date=? and start_time=? and end_time=? and createdatetime=? and creator=? and demographic_no=? order by appointment_no desc limit 1"},
            
            {"search_appt_data", "select app.*, prov.first_name, prov.last_name, prov.ohip_no, adm.program_id as adm_program_id from provider prov, appointment app left join admission adm on app.demographic_no = adm.client_id where app.provider_no = prov.provider_no and app.provider_no=? and app.appointment_date=? and app.start_time=? and app.end_time=? and app.createdatetime=? and app.creator=? and app.demographic_no=? order by app.appointment_no desc limit 1"},

            {"search_waitinglist", "select wl.listID, wln.name from waitingList wl, waitingListName wln where wl.demographic_no=? and wln.ID=wl.listID and wl.is_history ='N' order by wl.listID"},
            
            {"search", "select * from appointment where appointment_no=?"},
            {"search_detail", "select * from demographic where demographic_no=?"},

            
            {"search_otherappt", "select * from appointment where appointment_date=? and ((start_time <= ? and end_time >= ?) or (start_time > ? and start_time < ?) ) order by provider_no, start_time" },
            {"search_groupprovider", "select p.last_name, p.first_name, p.provider_no from mygroup m, provider p where m.mygroup_no=? and m.provider_no=p.provider_no order by p.last_name"},
            {"search_scheduledate_single", "select * from scheduledate where sdate=? and provider_no=?" },

            
            {"search_group_day_appt", "select count(appointment_no) as numAppts from appointment, mygroup where mygroup.provider_no=appointment.provider_no and appointment.status != 'C' and mygroup.mygroup_no=? and  appointment.demographic_no=? and  appointment.appointment_date=?"},
            {"search_formtbl","select * from encounterForm where form_table= ?"},
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
			ad.setUrgency(rs.getString("urgency"));
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
