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
			{"search_tickler","select * from tickler where demographic_no=? and service_date<=? and status='A' order by service_date desc"},
			{"search_studycount","select count(ds.study_no) from demographicstudy ds, study s where ds.demographic_no=? and ds.study_no=s.study_no and s.current1='1'"},
			{"search_study","select s.* from demographicstudy d, study s where demographic_no=? and d.study_no = s.study_no limit 1 "},
			{"searchappointmentday", "select appointment_no,provider_no, start_time,end_time,name,demographic_no,reason,notes,status from appointment where provider_no=? and appointment_date=? order by start_time, status desc "},
			{"searchmygroupcount", "select count(provider_no) from mygroup where mygroup_no=? "},
			{"searchmygroupprovider", "select provider_no, last_name, first_name from mygroup where mygroup_no=? "},
			{"searchmygroupall", "select * from mygroup order by mygroup_no"},

			{"searchmygroupno", "select mygroup_no from mygroup group by mygroup_no order by mygroup_no"},
            {"deletegroupmember", "delete from mygroup where mygroup_no=? and provider_no=?"},
            {"savemygroup", "insert into mygroup (mygroup_no,provider_no,last_name,first_name) values(?,?,?,?)" },
            {"updateapptstatus", "update appointment set status=? where appointment_no=? "},

            {"updatepreference", "update preference set start_hour=?, end_hour=?, every_min=?, mygroup_no=?, default_servicetype=?, color_template=? where provider_no=? "},
            {"add_preference", "insert into preference (provider_no, start_hour, end_hour, every_min, mygroup_no, default_servicetype, color_template) values (?, ?, ?, ?, ?, ?, ?)"},

            {"updatepreference_newtickler", "update preference set start_hour=?, end_hour=?, every_min=?, mygroup_no=?, default_servicetype=?, color_template=?, new_tickler_warning_window=? , default_caisi_pmm=? where provider_no=? "},
			{"add_preference_newtickler", "insert into preference (provider_no, start_hour, end_hour, every_min, mygroup_no, default_servicetype, color_template, new_tickler_warning_window, default_caisi_pmm) values (?, ?, ?, ?, ?, ?, ?, ?, ?)"},

            {"search_demograph", "select *  from demographic where demographic_no=?"},
            {"search_encounter", "select * from encounter where demographic_no = ? order by encounter_date desc, encounter_time desc"},
            {"search_encounter_no", "select * from encounter where demographic_no = ? and encounter_date=? and encounter_time=? and provider_no=? order by encounter_no desc limit 1"},
            {"search_encountersingle", "select * from encounter where encounter_no = ?"},
            {"search_previousenc", "select * from encounter where demographic_no = ? and provider_no=? order by encounter_date desc, encounter_time desc limit 1"},

			{"delete_encounter1", "insert into recyclebin (provider_no,updatedatetime,table_name,keyword,table_content) values(?,?,'encounter',?,?)"},
			{"delete_encounter2", "delete from encounter where encounter_no = ?"},
			{"search_encounterform", "select * from encounterform where form_name like ? order by form_name"},
			{"search_form", "select * from form where form_no=? "}, //new?delete
			{"search_form_no", "select form_no, content from form where demographic_no=? and form_name like ? order by form_date desc, form_time desc,form_no desc limit 1"}, //new?delete
			{"compare_form", "select form_no, form_name, content from form where demographic_no=? and form_name like ? order by form_date desc, form_time desc,form_no desc limit 1"},
			{"save_form", "insert into form (demographic_no, provider_no, form_date, form_time, form_name, content) values(?,?,?,?,?,?)"},
			{"search_template", "select * from encountertemplate where encountertemplate_name like ? order by encountertemplate_name"},

			{"add_encounter", "insert into encounter (demographic_no, encounter_date, encounter_time, provider_no, subject, content, encounterattachment) values(?,?,?,?,?,?,?)"},
			{"save_prescribe", "insert into prescribe (demographic_no, provider_no, prescribe_date, prescribe_time, content) values(?,?,?,?,?)"},
			{"search_prescribe", "select * from prescribe where prescribe_no= ?"},
			{"search_prescribe_no", "select prescribe_no from prescribe where demographic_no=?  order by prescribe_date desc, prescribe_time desc limit 1"},
			{"search_demographicaccessory", "select * from demographicaccessory where demographic_no=?"},
			{"search_demographicaccessorycount", "select count(demographic_no) from demographicaccessory where demographic_no=?"},
			{"add_demographicaccessory", "insert into demographicaccessory values(?,?)"},
			{"update_demographicaccessory", "update demographicaccessory set content=? where demographic_no=?"},

			{"add_template", "insert into encountertemplate values(?,?,?,?)"},
			{"delete_template", "delete from encountertemplate where encountertemplate_name = ?"},
			{"search_templatename", "select encountertemplate_name from encountertemplate order by encountertemplate_name"},
			{"add_encounterform", "insert into encounterform values(?,?,?,?)"},
			{"delete_encounterform", "delete from encounterform where form_name = ?"},
			{"search_encounterformname", "select form_name from encounterform order by form_name"},
			{"search_provider_slp", "select comments from provider where provider_no=?"},

			{"searchprovider", "select provider_no, last_name, first_name from provider where provider_type='doctor' and status='1' order by last_name"},
			{"searchallprovider", "select * from provider where status='1' order by last_name"},
			{"search_scheduleholiday", "select * from scheduleholiday where sdate > ?" },
			{"search_scheduledate_datep", "select * from scheduledate where sdate between ? and ? and status = 'A' order by sdate" },
			{"search_scheduledate_singlep", "select * from scheduledate where sdate between ? and ? and provider_no=? and status = 'A' order by sdate" },
			{"search_scheduledate_single", "select * from scheduledate where sdate=? and provider_no=? and status = 'A'" },

			{"search_appttimecode", "select scheduledate.provider_no, scheduletemplate.timecode, scheduledate.sdate from scheduletemplate, scheduledate where scheduletemplate.name=scheduledate.hour and scheduledate.sdate=? and  scheduledate.provider_no=? and scheduledate.status = 'A' and (scheduletemplate.provider_no=scheduledate.provider_no or scheduletemplate.provider_no='Public') order by scheduledate.sdate"},
			{"search_timecode", "select * from scheduletemplatecode order by code"},
			{"search_resource_baseurl", "select * from property where name = ?"},

			{"search_numgrpscheduledate", "select count(scheduledate.provider_no) from mygroup, scheduledate where mygroup_no = ? and scheduledate.sdate=? and mygroup.provider_no=scheduledate.provider_no and scheduledate.available = '1'  and scheduledate.status = 'A'"},

			{"search_pref_defaultbill", "select default_servicetype from preference where provider_no = ?"},
			{"list_bills_servicetype", "select distinct servicetype, servicetype_name from ctl_billingservice where status='A'"},

			{"searchpassword", "select password from security where provider_no = ?" },
			{"updatepassword", "update security set password = ? where  provider_no= ?" },

		    {"search_provider", "select provider_no, last_name, first_name from provider where last_name like ? and first_name like ? order by last_name"}, 
		    {"search_providersgroup", "select mygroup_no, last_name, first_name from mygroup where last_name like ? and first_name like ? order by last_name, first_name, mygroup_no"}, 
		    {"search_mygroup", "select mygroup_no from mygroup where mygroup_no like ? group by mygroup_no order by mygroup_no"}, 
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
