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

import java.util.Map;
import java.util.TreeMap;

import org.springframework.jdbc.core.RowMapper;

/**
 * Oscar Provider DAO implementation created to extract database access code
 * from provider related JSP files. This class contains only actual sql
 * queries and row mappers.
 *
 * @author Eugene Petruhin
 *
 */
public class ProviderDao extends OscarSuperDao {

	private Map<String, RowMapper> rowMappers = new TreeMap<String, RowMapper>();

	public ProviderDao() {
	}

	private String [][] dbQueries = new String[][] {
			{"searchappointmentday", "select * from appointment where provider_no=? and appointment_date=? and program_id=? order by start_time, status desc "},
			{"searchappointmentdaywithlocation", "select * from appointment where provider_no=? and appointment_date=? and program_id=? and location=? order by start_time, status desc "},
			{"searchmygroupprovider", "select provider_no, last_name, first_name from mygroup where mygroup_no=? order by vieworder,provider_no"},

			{"searchmygroupno", "select mygroup_no from mygroup group by mygroup_no order by mygroup_no"},
            {"updateapptstatus", "update appointment set status=?, lastupdateuser=?, updatedatetime=now() where appointment_no=? "},
            {"updatepreference", "update preference set start_hour=?, end_hour=?, every_min=?, mygroup_no=?, default_servicetype=?, color_template=? where provider_no=? "},
            
            {"search_demograph", "select *  from demographic where demographic_no=?"},
            {"search_encountersingle", "select * from encounter where encounter_no = ?"},
            
			{"search_form", "select * from form where form_no=? "}, //new?delete
			
			{"searchloginteam", "select provider_no, last_name, first_name from provider where (provider_no=? || team=(select team from provider where provider_no=?)) and status='1' order by last_name"},
			{"searchprovider", "select provider_no, last_name, first_name from provider where provider_type='doctor' and status='1' order by last_name"},
			{"search_scheduleholiday", "select * from scheduleholiday where sdate > ?" },
			{"search_scheduledate_datep",   "select * from scheduledate where sdate between ? and ? and status = 'A' order by sdate" },
			{"search_scheduledate_singlep", "select * from scheduledate where sdate between ? and ? and provider_no=? and status = 'A' order by sdate" },
			{"search_scheduledate_single", "select * from scheduledate where sdate=? and provider_no=? and status = 'A'" },
            {"search_signed_confidentiality", "select signed_confidentiality from provider where provider_no = ?"},
			{"search_scheduledate_teamp", "select * from scheduledate where sdate between ? and ? and status = 'A' and provider_no in (select distinct provider_no from provider where team=(select team from provider where provider_no=?) || provider_no=?) order by sdate" },

			{"search_appttimecode", "select scheduledate.provider_no, scheduletemplate.timecode, scheduledate.sdate from scheduletemplate, scheduledate where scheduletemplate.name=scheduledate.hour and scheduledate.sdate=? and  scheduledate.provider_no=? and scheduledate.status = 'A' and (scheduletemplate.provider_no=scheduledate.provider_no or scheduletemplate.provider_no='Public') order by scheduledate.sdate"},
			{"search_timecode", "select * from scheduletemplatecode order by code"},
			
			{"search_numgrpscheduledate", "select count(scheduledate.provider_no) from mygroup, scheduledate where mygroup_no = ? and scheduledate.sdate=? and mygroup.provider_no=scheduledate.provider_no and scheduledate.available = '1'  and scheduledate.status = 'A'"},

			
		    {"search_provider", "select provider_no, last_name, first_name from provider where last_name like ? and first_name like ? order by last_name"},
		    {"search_providersgroup", "select mygroup_no, last_name, first_name from mygroup where last_name like ? and first_name like ? order by last_name, first_name, mygroup_no"},
		    //multi-site query, schedule day view page
			{"site_searchmygroupcount", "select count(provider_no) from mygroup where mygroup_no=?  and provider_no in (select ps.provider_no from providersite ps inner join site s on ps.site_id = s.site_id where s.name = ?)"},
			{"site_search_numgrpscheduledate", "select count(scheduledate.provider_no) from mygroup, scheduledate where mygroup_no = ? and scheduledate.sdate=? and mygroup.provider_no=scheduledate.provider_no and scheduledate.available = '1'  and scheduledate.status = 'A'  and mygroup.provider_no in (select ps.provider_no from providersite ps inner join site s on ps.site_id = s.site_id where s.name = ?) "},
			{"site_searchmygroupprovider", "select provider_no, last_name, first_name from mygroup where mygroup_no=?  and provider_no in (select ps.provider_no from providersite ps inner join site s on ps.site_id = s.site_id where s.name = ?)"},
			{"site_search_scheduledate_datep", "select * from scheduledate where sdate between ? and ? and status = 'A' and provider_no in (select ps.provider_no from providersite ps inner join site s on ps.site_id = s.site_id where s.name = ?) order by sdate" },

			{"intake_pharmacy","SELECT * FROM  pharmacyInfo where recordID = (select d.pharmacyID from demographicPharmacy d where  d.status = 1 and d.demographic_no = ? order by addDate desc limit 1) order by recordID desc limit 1"},
			{"intake_allergies","SELECT * FROM allergies WHERE demographic_no = ? and archived = '0' ORDER BY DESCRIPTION"},
			{"intake_medications","select * from drugs as D where demographic_no = ? and archived = 0 and drugId = (select max(drugId) from drugs where demographic_no = D.demographic_no and archived = 0 and regional_identifier = D.regional_identifier)  and (long_term=1 or (end_date is not null and end_date > now())) ORDER BY rx_date DESC, drugId DESC"},
			{"intake_demographic","select last_name, first_name, year_of_birth, month_of_birth, date_of_birth from demographic where demographic_no=?"},
			{"intake_get_measurement","select * from measurements where type=? and demographicNo=? order by dateObserved desc, dateEntered desc limit 1"},
			{"intake_get_measurement_ex","select * from measurements where type=? and demographicNo=? and measuringInstruction=? order by dateObserved desc, dateEntered desc limit 1"},
			{"intake_reminders","select distinct casemgmt_note.* from casemgmt_note, (select max(cmn2.note_id) as note_id from casemgmt_issue_notes as cmin2 left join casemgmt_note cmn2 USE INDEX (demographic_no) using (note_id) left join casemgmt_issue as cmi2 using (id) where cmn2.note_id = cmin2.note_id and cmin2.id = cmi2.id and cmi2.issue_id in (38) and cmn2.demographic_no = ? group by cmn2.uuid) as rem_notes where casemgmt_note.note_id=rem_notes.note_id and casemgmt_note.archived=0"},
			{"intake_preventions","select * from preventions where demographic_no = ? and deleted != 1 order by prevention_type,prevention_date"},
			{"intake_patient_dxcode","select * from dxresearch where demographic_no=? and status='A' and dxresearch_code=?"},

			{"search_rsstatus", "select distinct roster_status from demographic where roster_status not in ('', 'RO', 'NR', 'TE', 'FS')"},
			{"cl_demographic_query","select last_name, first_name, sex, month_of_birth, date_of_birth, CAST((DATE_FORMAT(NOW(), '%Y') - DATE_FORMAT(concat(year_of_birth,month_of_birth,date_of_birth), '%Y') - (DATE_FORMAT(NOW(), '00-%m-%d') < DATE_FORMAT(concat(year_of_birth,month_of_birth,date_of_birth), '00-%m-%d'))) as UNSIGNED INTEGER) as age from demographic where demographic_no=?"},
			{"cl_demographic_query_roster","select last_name, first_name, sex, month_of_birth, date_of_birth, CAST((DATE_FORMAT(NOW(), '%Y') - DATE_FORMAT(concat(year_of_birth,month_of_birth,date_of_birth), '%Y') - (DATE_FORMAT(NOW(), '00-%m-%d') < DATE_FORMAT(concat(year_of_birth,month_of_birth,date_of_birth), '00-%m-%d'))) as UNSIGNED INTEGER) as age from demographic where demographic_no=? AND roster_status=?"},
			{"cl_last_appt","select max(appointment_date) from appointment where appointment_date < now() and demographic_no=?"},
			{"cl_next_appt","select min(appointment_date) from appointment where appointment_date > now() and demographic_no=?"},
			{"cl_num_appts","select count(*) from appointment where demographic_no=? and appointment_date > curdate() - 365"},
			{"cl_new_labs","select count(*) from providerLabRouting left join patientLabRouting using (lab_no) where providerLabRouting.lab_type='HL7' and status='N' and provider_no=? and demographic_no=?"},
			{"cl_new_docs","select count(*) from providerLabRouting left join patientLabRouting using (lab_no) where providerLabRouting.lab_type='DOC' and status='N' and provider_no=? and demographic_no=?"},
			{"cl_new_ticklers","select count(*) from tickler where status='A' and demographic_no=?"},
			{"cl_new_msgs","select count(*) from msgDemoMap left join messagelisttbl on message = messageID where demographic_no=? and status='new'"},
			{"cl_measurement","select * from measurements where type=? and demographicNo=? order by dateObserved desc limit 1"},

	};

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
