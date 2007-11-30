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
package oscar.appt;

import java.math.BigInteger;
import java.sql.*;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.Properties;
import oscar.oscarDB.DBHandler;
import oscar.oscarProvider.data.ProviderData;
import oscar.util.DAO;


public class AppointmentDAO extends DAO {
    public AppointmentDAO(Properties pvar) throws SQLException {
        super(pvar);
    }
    
    public int addAppointment(String providerNo, Date appointmentDate, Date startTime, Date endTime, String name, String demographicNo, String notes, String reason, String status) throws SQLException {
	String add_record_string = "insert into appointment (provider_no,appointment_date,start_time,end_time,name,demographic_no,notes,reason,status) values (?,?,?,?,?,?,?,?,?)";
	int key = 0;
	
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	Connection conn = db.GetConnection();
	PreparedStatement add_record = conn.prepareStatement(add_record_string);
	
	add_record.setString(1, providerNo);
	add_record.setDate(2, new java.sql.Date(appointmentDate.getTime()));
	add_record.setDate(3, new java.sql.Date(startTime.getTime()));
	add_record.setDate(4, new java.sql.Date(endTime.getTime()));
	add_record.setString(5, name);
	add_record.setString(6, demographicNo);
	add_record.setString(7, notes);
	add_record.setString(8, reason);
	add_record.setString(9, status);

	add_record.executeUpdate();
	ResultSet rs = add_record.getGeneratedKeys();
	if(rs.next()) key = rs.getInt(1);
	add_record.close();
	rs.close();
	db.CloseConn();
	
	return key;
    }
    
    public Vector retrieve(String demo_no) throws SQLException, ParseException {
	Vector appointments = new Vector();
        String sql = "select app.*, prov.first_name, prov.last_name, prov.ohip_no from appointment app, provider prov " +
		     "where app.provider_no = prov.provider_no and app.demographic_no =" + demo_no;

        DBHandler db = getDb();
        try {
            ResultSet rs = db.GetSQL(sql);

            while (rs.next()) {
		Appointment ap = new Appointment(rs.getString("prov.first_name"), rs.getString("prov.last_name"), rs.getString("prov.ohip_no"));
		
		ap.setAppointment_no(rs.getString("appointment_no"));
		ap.setAppointment_date(rs.getString("appointment_date"));
		ap.setStart_time(rs.getString("start_time"));
		ap.setEnd_time(rs.getString("end_time"));
		ap.setNotes(rs.getString("notes"));
		ap.setReason(rs.getString("reason"));
		ap.setStatus(rs.getString("status"));
		
		appointments.add(ap);
             }
        } finally {
            db.CloseConn();
        }
        return appointments;
    }
    
   public class Appointment extends ApptData {
	String providerLastName = null;
	String providerFirstName = null;
	String ohipNo = null;
	
	public Appointment(String firstname, String lastname, String ohip_no) {
	    this.providerFirstName = firstname;
	    this.providerLastName  = lastname;
	    this.ohipNo = ohip_no;
	}
	
	public Date getDateAppointment() throws ParseException {
	    return string_date(this.appointment_date, true);
	}
	
	public Date getDateStartTime() throws ParseException {
	    return string_date(this.start_time, false);
	}
	
	public Date getDateEndTime() throws ParseException {
	    return string_date(this.end_time, false);
	}
	
	public String getProviderFirstN() {
	    return this.providerFirstName;
	}
	
	public String getProviderLastN() {
	    return this.providerLastName;
	}
	
	public String getOhipNo() {
	    return this.ohipNo;
	}
	
	private Date string_date(String s, boolean isDate) throws ParseException {
	    if (isDate) return new SimpleDateFormat("yyyy-MM-dd").parse(s);
	    else return new SimpleDateFormat("HH:mm:ss").parse(s);
	}
    }
}