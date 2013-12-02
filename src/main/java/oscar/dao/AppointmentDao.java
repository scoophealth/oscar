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
import java.util.List;
import java.util.TreeMap;

import org.springframework.jdbc.core.RowMapper;

import oscar.OscarProperties;

import oscar.appt.ApptData;

import oscar.oscarClinic.ClinicData;

import org.oscarehr.common.hl7.v2.HL7A04Data;

import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.dao.DemographicDao;

import oscar.oscarDemographic.data.DemographicData;

//import org.oscarehr.PMmodule.model.Program;
//import org.oscarehr.PMmodule.dao.ProgramDao;

import org.oscarehr.util.SpringUtils;

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
	
	/**
	 * Executes a parameterized insert/update/delete query identified by a key.<br>
	 * 
	 * @param queryName sql query key
	 * @param params sql query parameters
	 * @return number of affected rows
	 */
	public int executeUpdateQuery(String queryName, Object[] params) {
		int result = super.executeUpdateQuery(queryName, params);
		
		// Generate our HL7 A04 file when we add an appointment
		// Should we also generate an HL7 A04 when we import an appointment?
		if (OscarProperties.getInstance().isHL7A04GenerationEnabled() && 
			queryName.equalsIgnoreCase("add_apptrecord") && result == 1) {
			generateHL7A04(params);
		}
		
		return result;
	}
	
	/**
	 * 
	 */ 
	private void generateHL7A04(Object[] params) {
		try {	
			String[] param2 = new String[7];
			param2[0] = params[0].toString(); //provider_no
			param2[1] = params[1].toString(); //appointment_date
			param2[2] = params[2].toString(); //start_time
			param2[3] = params[3].toString(); //end_time
			param2[4] = params[13].toString(); //createdatetime
			param2[5] = params[14].toString(); //creator
			param2[6] = params[16].toString(); //demographic_no
			
			// get appointment data
			ApptData appData = new ApptData();
			List<Map<String, Object>> apptInfo = this.executeSelectQuery("search_appt_data", param2);
			if (apptInfo.size() > 0) {
				appData.setAppointment_no( 		apptInfo.get(0).get("appointment_no").toString() );
				appData.setAppointment_date( 	apptInfo.get(0).get("appointment_date").toString() );
				appData.setStart_time( 			apptInfo.get(0).get("start_time").toString() );
				appData.setEnd_time( 			apptInfo.get(0).get("end_time").toString() );
				appData.setNotes( 				apptInfo.get(0).get("notes").toString() );
				appData.setReason( 				apptInfo.get(0).get("reason").toString() );
				appData.setStatus( 				apptInfo.get(0).get("status").toString() );
				appData.setProviderFirstName( 	apptInfo.get(0).get("first_name").toString() );
				appData.setProviderLastName( 	apptInfo.get(0).get("last_name").toString() );
				appData.setOhipNo( 				apptInfo.get(0).get("ohip_no").toString() );
				appData.setUrgency( 			apptInfo.get(0).get("urgency").toString() );				
			}
				
			// get demographic data
			DemographicData demoData = new DemographicData();
			Demographic demo = demoData.getDemographic(params[16].toString());
			
			// get clinic name/id
			ClinicData clinicData = new ClinicData();
			
			//Program program = null;
			DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
			List programs = demographicDao.getDemoProgramCurrent( demo.getDemographicNo() );
			/*
			if ( apptInfo.get(0).get("adm_program_id").toString() != null ) {
				Integer programId = new Integer( apptInfo.get(0).get("adm_program_id").toString() );
				ProgramDao programDao = (ProgramDao)SpringUtils.getBean("programDao");
				program = programDao.getProgram( programId );
			}
			*/
			
			// generate A04 HL7
			HL7A04Data A04Obj = new HL7A04Data(demo, appData, clinicData, programs);
			A04Obj.save();
		} catch (Exception e) {
			logger.error("Unable to generate HL7 A04 file.", e);
		}
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

            {"add_apptrecord", "insert into appointment (provider_no,appointment_date,start_time,end_time,name, notes,reason,location,resources,type, style,billing,status,createdatetime,creator, remarks, demographic_no, program_id,urgency,updatedatetime) values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,now())" },
            {"search_waitinglist", "select wl.listID, wln.name from waitingList wl, waitingListName wln where wl.demographic_no=? and wln.ID=wl.listID and wl.is_history ='N' order by wl.listID"},
            {"appendremarks", "update appointment set remarks=CONCAT(remarks,?) where appointment_no=?"},
            {"updatestatusc", "update appointment set status=?, lastupdateuser=?, updatedatetime=now() where appointment_no=?"},
            {"update_apptrecord", "update appointment set demographic_no=?,appointment_date=?,start_time=?,end_time=?,name=?, notes=?,reason =?,location=?, resources=?, type=?,style=?,billing =?,status=?,lastupdateuser=?,remarks=?,updatedatetime=now(),urgency=? where appointment_no=? "},

            {"search", "select * from appointment where appointment_no=?"},
            {"search_detail", "select * from demographic where demographic_no=?"},

            {"delete", "delete from appointment where appointment_no=?"},

            {"search_otherappt", "select * from appointment where appointment_date=? and ((start_time <= ? and end_time >= ?) or (start_time > ? and start_time < ?) ) order by provider_no, start_time" },
            {"search_groupprovider", "select p.last_name, p.first_name, p.provider_no from mygroup m, provider p where m.mygroup_no=? and m.provider_no=p.provider_no order by p.last_name"},
            {"search_scheduledate_single", "select * from scheduledate where sdate=? and provider_no=?" },

            {"cancel_appt", "update appointment set status = ?, updatedatetime = ?, lastupdateuser = ? where appointment_date=? and provider_no=? and start_time=? and end_time=? and name=? and notes=? and reason=? and createdatetime like ?  and creator=? and demographic_no=? " },
            {"delete_appt", "delete from appointment where appointment_date=? and provider_no=? and start_time=? and end_time=? and name=? and notes=? and reason=? and createdatetime like ?  and creator=? and demographic_no=? " },
            {"update_appt", "update appointment set start_time=?, end_time=?, name=?, demographic_no=?, notes=?, reason=?, location=?, resources=?, updatedatetime=?, lastupdateuser=?, urgency=? where appointment_date=? and provider_no=? and start_time=? and end_time=? and name=? and notes=? and reason=? and createdatetime like ?  and creator=? and demographic_no=?" },

            {"search_group_day_appt", "select count(appointment_no) as numAppts from appointment, mygroup where mygroup.provider_no=appointment.provider_no and appointment.status != 'C' and mygroup.mygroup_no=? and  appointment.demographic_no=? and  appointment.appointment_date=?"},
            {"search_formtbl","select * from encounterForm where form_table= ?"},
            {"import_appt", "insert into appointment (provider_no,appointment_date,start_time,end_time,name,demographic_no,notes,reason,status,imported_status,updatedatetime) values (?,?,?,?,?,?,?,?,?,?,now())" },
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
