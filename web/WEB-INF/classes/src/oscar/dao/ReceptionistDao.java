package oscar.dao;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.jdbc.core.RowMapper;

/**
 * Oscar Receptionist DAO implementation created to extract database access code
 * from receptionist related JSP files. This class contains only actual sql
 * queries and row mappers.
 * 
 * @author Eugene Petruhin
 * 
 */
public class ReceptionistDao extends OscarSuperDao {

	private Map<String, RowMapper> rowMappers = new TreeMap<String, RowMapper>();

	public ReceptionistDao() {
	}

	private String [][] dbQueries = new String[][] { 
			{"search_tickler","select * from tickler where demographic_no=? and service_date<=? and status='A' order by service_date desc"},
			{"searchappointmentday", "select appointment_no, provider_no, start_time,end_time,name,demographic_no,reason,notes,status from appointment where provider_no=? and appointment_date=? order by start_time, status desc "}, 
			{"searchmygroupcount", "select count(provider_no) from mygroup where mygroup_no=? "}, 
			{"searchmygroupsubcount", "select count(provider_no) from mygroup where mygroup_no=? and vieworder like ?"}, 
			{"searchmygroupprovider", "select provider_no, last_name, first_name, vieworder from mygroup where mygroup_no=? order by vieworder, first_name"}, 
			{"searchmygroupsubprovider", "select provider_no, last_name, first_name from mygroup where mygroup_no=? and vieworder like ? order by vieworder, first_name"}, 
			{"searchmygroupall", "select * from mygroup order by mygroup_no"}, 
			{"searchmygroupno", "select mygroup_no from mygroup group by mygroup_no order by mygroup_no"}, 
			{"upgradegroupmember", "update mygroup set vieworder = ? where mygroup_no=? and provider_no=?"}, 
			{"deletegroupmember", "delete from mygroup where mygroup_no=? and provider_no=?"}, 
			{"savemygroup", "insert into mygroup (mygroup_no,provider_no,last_name,first_name) values(?,?,?,?)" },
			{"searchprovider", "select provider_no, last_name, first_name from provider where provider_type='doctor' and status='1' order by last_name"}, 
			{"searchallprovider", "select * from provider where status='1' order by last_name"}, 
			{"updateapptstatus", "update appointment set status=? where appointment_no=? "},

			{"search_provider", "select provider_no, last_name, first_name from provider where last_name like ? and first_name like ? order by last_name"}, 
		    {"search_providersgroup", "select mygroup_no, last_name, first_name from mygroup where last_name like ? and first_name like ? order by last_name, first_name, mygroup_no"}, 
		    {"search_mygroup", "select mygroup_no from mygroup where mygroup_no like ? group by mygroup_no order by mygroup_no"}, 

			{"updatepreference", "update preference set start_hour=?, end_hour=?, every_min=?, mygroup_no=?, color_template=? where provider_no=? "},
			{"add_preference", "insert into preference (provider_no, start_hour, end_hour, every_min, mygroup_no, color_template) values (?, ?, ?, ?, ?, ?)"},
			{"updatepreference_newtickler", "update preference set start_hour=?, end_hour=?, every_min=?, mygroup_no=?, color_template=?,new_tickler_warning_window=? where provider_no=? "},
			{"add_preference_newtickler", "insert into preference (provider_no, start_hour, end_hour, every_min, mygroup_no, color_template, new_tickler_warning_window) values (?, ?, ?, ?, ?, ?, ?)"},

			{"search_scheduleholiday", "select * from scheduleholiday where sdate > ?" }, 
			{"search_scheduledate_datep", "select * from scheduledate where sdate between ? and ? order by sdate, reason" }, 
			{"search_scheduledate_singlep", "select * from scheduledate where sdate between ? and ? and provider_no=? order by sdate" }, 
			{"search_scheduledate_single", "select * from scheduledate where sdate=? and provider_no=?" }, 

			{"search_appttimecode", "select scheduledate.provider_no, scheduletemplate.timecode, scheduledate.sdate from scheduletemplate, scheduledate where scheduletemplate.name=scheduledate.hour and scheduledate.sdate=? and  scheduledate.provider_no=? and (scheduletemplate.provider_no=scheduledate.provider_no or scheduletemplate.provider_no='Public') order by scheduledate.sdate"}, 
			{"search_timecode", "select * from scheduletemplatecode order by code"}, 
			{"search_resource_baseurl", "select * from property where name = ?"}, 

			{"search_numgrpscheduledate", "select count(scheduledate.provider_no) from mygroup, scheduledate where mygroup_no = ? and scheduledate.sdate=? and mygroup.provider_no=scheduledate.provider_no and scheduledate.available = '1' "} 
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
