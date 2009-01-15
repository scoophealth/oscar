/*
 * 
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
 * This software was written for 
 * Centre for Research on Inner City Health, St. Michael's Hospital, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.common.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Comparator;
import java.util.TreeSet;

import javax.persistence.PersistenceException;

import org.hibernate.SQLQuery;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.DbConnectionFilter;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.util.SqlUtils;

/**
 */
public class DemographicDao extends HibernateDaoSupport {

    public Demographic getDemographic(String demographic_no) {
        if (demographic_no == null || demographic_no.length() == 0) {
            return null;
        }

        return (Demographic) this.getHibernateTemplate().get(Demographic.class, Integer.valueOf(demographic_no));
    }

    // ADD BY PINE-SOFT
    public List getDemographics() {
        return this.getHibernateTemplate().find("from Demographic d order by d.LastName");
    }

    public Demographic getDemographicById(Integer demographic_id) {
        String q = "FROM Demographic d WHERE d.DemographicNo = ?";
        List rs = (List) getHibernateTemplate().find(q, demographic_id);

        if (rs.size() == 0) return null;
        else return (Demographic) rs.get(0);
    }

    /*
     * get demographics according to their program, admit time, discharge time, ordered by lastname and first name
     */
    public List getDemographicByProgram(int programId, Date dt, Date defdt) {
        String q = "Select d From Demographic d, Admission a " + "Where d.DemographicNo=a.DlientId and a.ProgramId=? and a.AdmissionDate<=? and " + "(a.DischargeDate>=? or (a.DischargeDate is null) or a.DischargeDate=?)"
                + " order by d.LastName,d.FirstName";
        List rs = (List) getHibernateTemplate().find(q, new Object[] { new Integer(programId), dt, dt, defdt });
        return rs;
    }

    /*
     * get demographics according to their program, admit time, discharge time, ordered by lastname and first name
     */
    public List getActiveDemographicByProgram(int programId, Date dt, Date defdt) {
        // get duplicated clients from this sql
        String q = "Select d From Demographic d, Admission a " + "Where (d.PatientStatus=? or d.PatientStatus='' or d.PatientStatus=null) and d.DemographicNo=a.ClientId and a.ProgramId=? and a.AdmissionDate<=? and "
                + "(a.DischargeDate>=? or (a.DischargeDate is null) or a.DischargeDate=?)" + " order by d.LastName,d.FirstName";

        String status = "AC"; // only show active clients
        List rs = (List) getHibernateTemplate().find(q, new Object[] { status, new Integer(programId), dt, dt, defdt });

        List clients = new ArrayList();
        Integer clientNo = 0;
        Iterator it = rs.iterator();
        while (it.hasNext()) {
            Demographic demographic = (Demographic) it.next();

            // no dumplicated clients.
            if (demographic.getDemographicNo() == clientNo) continue;

            clientNo = demographic.getDemographicNo();

            clients.add(demographic);
        }
        // return rs;
        return clients;
    }

    public Set getArchiveDemographicByProgramOptimized(int programId, Date dt, Date defdt) {
    	Set<Demographic> archivedClients = new TreeSet<Demographic>(new Comparator<Demographic>() {
    		public int compare(Demographic o1, Demographic o2) {    	
    			return String.CASE_INSENSITIVE_ORDER.compare(o1.getLastName(), o2.getLastName());
    		}
    	});    	

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String sqlQuery = "select distinct d.demographic_no,d.first_name,d.last_name,(select count(*) from admission a where client_id=d.demographic_no and admission_status='current' and program_id="+programId+" and admission_date<='"+sdf.format(dt)+"') as is_active from admission a,demographic d where a.client_id=d.demographic_no and (d.patient_status='AC' or d.patient_status='' or d.patient_status=null) and program_id="+programId;
    	System.out.println(sqlQuery);
    	
    	
		SQLQuery q = this.getSession().createSQLQuery(sqlQuery);
		q.addScalar("d.demographic_no");
		q.addScalar("d.first_name");
		q.addScalar("d.last_name");
		q.addScalar("is_active");
		List results = q.list();
		
		Iterator iter = results.iterator();
		while(iter.hasNext()) {
			Object[] result = (Object[])iter.next();
			if(((BigInteger)result[3]).intValue() == 0) {
				Demographic d = new Demographic();
				d.setDemographicNo((Integer)result[0]);
				d.setFirstName((String)result[1]);
				d.setLastName((String)result[2]);
				archivedClients.add(d);
			}
		}
		return archivedClients;
    }
    
    public List getArchiveDemographicByPromgram(int programId, Date dt, Date defdt) {
        // get duplicated demographic records with the same id ....from this sql
        String q = "Select d From Demographic d, Admission a " + "Where (d.PatientStatus=? or d.PatientStatus='' or d.PatientStatus=null) and d.DemographicNo=a.ClientId and a.ProgramId=? and a.AdmissionDate<=? and a.AdmissionStatus=? "
                + " order by d.LastName,d.FirstName";

        String status = "AC"; // only show active clients
        String admissionStatus = "discharged"; // only show discharged clients
        List rs = (List) getHibernateTemplate().find(q, new Object[] { status, new Integer(programId), dt, admissionStatus });

        // and clients should not currently in this program.
        List clients = new ArrayList();
        Integer clientNo = 0;

        Iterator it = rs.iterator();
        while (it.hasNext()) {
            Demographic demographic = (Demographic) it.next();
            String q1 = "Select a.AdmissionStatus From Admission a " + "Where a.ClientId=? and a.ProgramId=? and a.AdmissionStatus=?";
            String ss = "current";

            // no dumplicated clients.
            if (demographic.getDemographicNo() == clientNo) continue;

            clientNo = demographic.getDemographicNo();
            List rs1 = (List) getHibernateTemplate().find(q1, new Object[] { clientNo, new Integer(programId), ss });
            if (rs1.size() == 0) {
                clients.add(demographic);
            }
        }

        return clients;
    }

    public List getProgramIdByDemoNo(String demoNo) {
        String q = "Select a.ProgramId From Admission a " + "Where a.ClientId=? and a.AdmissionDate<=? and " + "(a.DischargeDate>=? or (a.DischargeDate is null) or a.DischargeDate=?)";

        /* default time is Oscar default null time 0001-01-01. */
        Date defdt = new GregorianCalendar(1, 0, 1).getTime();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date dt = cal.getTime();

        List rs = (List) getHibernateTemplate().find(q, new Object[] { demoNo, dt, dt, defdt });
        return rs;
    }

    public void clear() {
        getHibernateTemplate().clear();

    }

    public List getDemoProgram(Integer demoNo) {
        String q = "Select a.ProgramId From Admission a Where a.ClientId=?";
        List rs = getHibernateTemplate().find(q, new Object[] { demoNo });
        return rs;
    }

    public static List<Integer> getDemographicIdsAdmittedIntoFacility(int facilityId) {
        Connection c = null;
        try {
            c = DbConnectionFilter.getThreadLocalDbConnection();
            PreparedStatement ps = c.prepareStatement("select distinct(admission.client_id) from admission,program,Facility where admission.program_id=program.id and program.facilityId=?");
            ps.setInt(1, facilityId);
            ResultSet rs = ps.executeQuery();
            ArrayList<Integer> results = new ArrayList<Integer>();
            while (rs.next())
                results.add(rs.getInt(1));
            return(results);
        }
        catch (SQLException e) {
            throw(new PersistenceException(e));
        }
        finally {
            SqlUtils.closeResources(c, null, null);
        }
    }

    
     public List<Demographic> searchDemographic(String searchStr){
        String fieldname = "", regularexp = "like";
          
        System.out.println("searchStr" + searchStr);

        if (searchStr.indexOf(",") == -1) {
            fieldname = "last_name";
        } else if (searchStr.trim().indexOf(",") == (searchStr.trim().length() - 1)) {
            fieldname = "last_name";
        } else {
            fieldname = "last_name " + regularexp + " ?" + " and first_name ";
        }
        
        String hql = "From Demographic d where " + fieldname + " " + regularexp + " ? ";
        System.out.println("====" + hql);
        
        String[] lastfirst = searchStr.split(",");
        Object[] object = null;
        if (lastfirst.length > 1) {
            object = new Object[] { lastfirst[0].trim()+"%",lastfirst[1].trim()+"%" };
        }else{
            object = new Object[] { lastfirst[0].trim()+"%" };
        }
        List list = getHibernateTemplate().find(hql,object );
        return list;
    }
    
    
}
