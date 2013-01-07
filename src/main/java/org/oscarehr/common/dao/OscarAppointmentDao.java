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

package org.oscarehr.common.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Query;

import org.oscarehr.common.NativeSql;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentArchive;
import org.oscarehr.common.model.Facility;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class OscarAppointmentDao extends AbstractDao<Appointment> {

	public OscarAppointmentDao() {
		super(Appointment.class);
	}

	public boolean checkForConflict(Appointment appt) {
		String sb = "select a from Appointment a where a.appointmentDate = ? and a.startTime >= ? and a.endTime <= ? and a.providerNo = ? and a.status != 'N' and a.status != 'C'";

		Query query = entityManager.createQuery(sb);

		query.setParameter(1, appt.getAppointmentDate());
		query.setParameter(2, appt.getStartTime());
		query.setParameter(3, appt.getEndTime());
		query.setParameter(4, appt.getProviderNo());

		
		List<Facility> results = query.getResultList();

		if (!results.isEmpty()) return true;

		return false;
	}

	public List<Appointment> getAppointmentHistory(Integer demographicNo) {
		String sql = "select a from Appointment a where a.demographicNo=? order by a.appointmentDate DESC, a.startTime DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);

		
		List<Appointment> rs = query.getResultList();

		return rs;
	}

	public void archiveAppointment(int appointmentNo) {
		Appointment appointment = this.find(appointmentNo);
		if (appointment != null) {
			AppointmentArchive apptArchive = new AppointmentArchive();
			String[] ignores={"id"};
			BeanUtils.copyProperties(appointment, apptArchive, ignores);
			apptArchive.setAppointmentNo(appointment.getId());
			entityManager.persist(apptArchive);
		}
	}

	public List<Appointment> getAllByDemographicNo(Integer demographicNo) {
		String sql = "SELECT a FROM Appointment a WHERE a.demographicNo = " + demographicNo + " ORDER BY a.id";
		Query query = entityManager.createQuery(sql);

		
		List<Appointment> rs = query.getResultList();

		return rs;
	}

	public List<Appointment> findByDateRange(Date startTime, Date endTime) {
		String sql = "SELECT a FROM Appointment a WHERE a.appointmentDate >=? and a.appointmentDate < ?";

		Query query = entityManager.createQuery(sql);
		query.setParameter(1, startTime);
		query.setParameter(2, endTime);
		
		List<Appointment> rs = query.getResultList();

		return rs;
	}


	public List<Appointment> findByDateRangeAndProvider(Date startTime, Date endTime, String providerNo) {
		String sql = "SELECT a FROM Appointment a WHERE a.appointmentDate >=? and a.appointmentDate < ? and providerNo = ?";

		Query query = entityManager.createQuery(sql);
		query.setParameter(1, startTime);
		query.setParameter(2, endTime);
		query.setParameter(3, providerNo);

		
		List<Appointment> rs = query.getResultList();

		return rs;
	}

	public List<Appointment> getByProviderAndDay(Date date, String providerNo) {
		String sql = "SELECT a FROM Appointment a WHERE a.providerNo=? and a.appointmentDate = ? and a.status != 'N' and a.status != 'C' order by a.startTime";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, providerNo);
		query.setParameter(2, date);
		
		List<Appointment> rs = query.getResultList();

		return rs;
	}

	public List<Appointment> findByProviderAndDayandNotStatuses(String providerNo, Date date, String[] notThisStatus) {
		String sql = "SELECT a FROM Appointment a WHERE a.providerNo=?1 and a.appointmentDate = ?2 and a.status NOT IN ( ?3 )";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, providerNo);
		query.setParameter(2, date);
		query.setParameter(3, Arrays.asList(notThisStatus));

		List<Appointment> results = query.getResultList();
		return results;
	}
	
	public List<Appointment> findByProviderAndDayandNotStatus(String providerNo, Date date, String notThisStatus) {
		String sql = "SELECT a FROM Appointment a WHERE a.providerNo=?1 and a.appointmentDate = ?2 and a.status != ?3";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, providerNo);
		query.setParameter(2, date);
		query.setParameter(3, notThisStatus);

		
		List<Appointment> results = query.getResultList();
		return results;
	}

	public List<Appointment> findByProviderDayAndStatus(String providerNo,Date date, String status) {
		String sql = "SELECT a FROM Appointment a WHERE a.providerNo=? and a.appointmentDate = ? and a.status=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, providerNo);
		query.setParameter(2, date);
		query.setParameter(3, status);
		
		List<Appointment> rs = query.getResultList();

		return rs;
	}

	public List<Appointment> findByDayAndStatus(Date date, String status) {
		String sql = "SELECT a FROM Appointment a WHERE a.appointmentDate = ? and a.status=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, date);
		query.setParameter(2, status);
		
		List<Appointment> rs = query.getResultList();

		return rs;
	}

	public List<Appointment> find(Date date, String providerNo,Date startTime, Date endTime, String name,
			String notes, String reason, Date createDateTime, String creator, Integer demographicNo) {

		String sql = "SELECT a FROM Appointment a " +
				"WHERE a.appointmentDate = ? and a.providerNo=? and a.startTime=?" +
				"and a.endTime=? and a.name=? and a.notes=? and a.reason=? and a.createDateTime=?" +
				"and a.creator=? and a.demographicNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, date);
		query.setParameter(2, providerNo);
		query.setParameter(3, startTime);
		query.setParameter(4, endTime);
		query.setParameter(5, name);
		query.setParameter(6, notes);
		query.setParameter(7, reason);
		query.setParameter(8, createDateTime);
		query.setParameter(9, creator);
		query.setParameter(10, demographicNo);

		
		List<Appointment> rs = query.getResultList();

		return rs;
	}

	/**
	 * @return return results ordered by appointmentDate, most recent first
	 */
	public List<Appointment> findByDemographicId(Integer demographicId, int startIndex, int itemsToReturn) {
		String sql = "SELECT a FROM Appointment a WHERE a.demographicNo = ?1 ORDER BY a.appointmentDate desc";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicId);
		query.setFirstResult(startIndex);
		query.setMaxResults(itemsToReturn);

		
		List<Appointment> rs = query.getResultList();

		return rs;
	}

	public List<Appointment> findAll() {
		String sql = "SELECT a FROM Appointment a";
		Query query = entityManager.createQuery(sql);

		
		List<Appointment> rs = query.getResultList();

		return rs;
	}
	
	
    public List<Appointment> findNonCancelledFutureAppointments(Integer demographicId) {
		Query query = entityManager.createQuery("FROM Appointment appt WHERE appt.demographicNo = :demographicNo AND appt.status NOT LIKE '%C%' " +
				" AND appt.appointmentDate >= CURRENT_DATE ORDER BY appt.appointmentDate");
		query.setParameter("demographicNo", demographicId);
		return query.getResultList();
	}
	
	/**
	 * Finds appointment after current date and time for the specified demographic
	 * 
	 * @param demographicId
	 * 		Demographic to find appointment for
	 * @return
	 * 		Returns the next non-cancelled future appointment or null if there are no appointments
	 * 	scheduled
	 */
	public Appointment findNextAppointment(Integer demographicId) {
		Query query = entityManager.createQuery("FROM Appointment appt WHERE appt.demographicNo = :demographicNo AND appt.status NOT LIKE '%C%' " +
				"	AND (appt.appointmentDate >= CURRENT_DATE AND appt.startTime >= CURRENT_TIME)");
		query.setParameter("demographicNo", demographicId);
		query.setMaxResults(1);
		return getSingleResultOrNull(query);
	}


	public Appointment findDemoAppointmentToday(Integer demographicNo) {
		Appointment appointment = null;

		String sql = "SELECT a FROM Appointment a WHERE a.demographicNo = ? AND a.appointmentDate=DATE(NOW())";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);

		try {
			appointment = (Appointment) query.getSingleResult();
		} catch (Exception e) {
			MiscUtils.getLogger().info("Couldn't find appointment for demographic " + demographicNo + " today.");
		}

		return appointment;
	}
	

	public List<Appointment> findByEverything(Date appointmentDate, String providerNo, Date startTime, Date endTime, String name, String notes, String reason, Date createDateTime, String creator, int demographicNo) {
		String sql = "SELECT a FROM Appointment a WHERE a.appointmentDate=? and a.providerNo=? and a.startTime=? and a.endTime=? and a.name=? and a.notes=? and a.reason=? and a.createDateTime like ? and a.creator = ? and a.demographicNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, appointmentDate);
		query.setParameter(2, providerNo);
		query.setParameter(3, startTime);
		query.setParameter(4, endTime);
		query.setParameter(5, name);
		query.setParameter(6, notes);
		query.setParameter(7, reason);
		query.setParameter(8, createDateTime);
		query.setParameter(9, creator);
		query.setParameter(10, demographicNo);

		
		List<Appointment> rs = query.getResultList();

		return rs;
	}
	
	
    public List<Appointment> findByProviderAndDate(String providerNo, Date appointmentDate) {
		Query query = createQuery("a", "a.providerNo = :pNo and a.appointmentDate= :aDate");
		query.setParameter("pNo", providerNo);
		query.setParameter("aDate", appointmentDate);
	    return query.getResultList();
    }

	
	public List<Object[]> findAppointments(Date sDate, Date eDate) {
		String sql = "FROM Appointment a, Demographic d " +
				"WHERE a.demographicNo = d.DemographicNo " +
				"AND d.Hin <> '' " +
				"AND a.appointmentDate >= :sDate " +
				"AND a.appointmentDate <= :eDate " +
				"AND (" +
				"	UPPER(d.Province) = 'ONTARIO' " +
				"	OR d.Province='ON' " +
				") GROUP BY d.DemographicNo " +
				"ORDER BY d.LastName";
		Query query = entityManager.createQuery(sql);
		query.setParameter("sDate", sDate == null ? new Date(Long.MIN_VALUE) : sDate);
		query.setParameter("eDate", eDate == null ? new Date(Long.MAX_VALUE) : eDate);
		return query.getResultList();
	}
	
    public List<Object[]> findPatientAppointments(String providerNo, Date from, Date to) {
        StringBuilder sql = new StringBuilder("FROM Demographic d, Appointment a, Provider p " +
                "WHERE a.demographicNo = d.DemographicNo " +
                "AND a.providerNo = p.ProviderNo ");

        	Map<String, Object> params = new HashMap<String, Object>();
        	if(providerNo != null && !providerNo.trim().equals("")){
		       sql.append("and a.providerNo = :pNo ");
		       params.put("pNo", providerNo);
		   }
        	
		   if(from != null){
		       sql.append("AND a.appointmentDate >= :from ");
		       params.put("from", from);
		   }if(to != null){
		       sql.append("AND a.appointmentDate <= :to ");
		       params.put("to", to);
		   }
		   sql.append("ORDER BY a.appointmentDate");
		   
		   Query query = entityManager.createQuery(sql.toString());
		   for(Entry<String, Object> e : params.entrySet()) {
			   query.setParameter(e.getKey(), e.getValue());
		   }
		   return query.getResultList();
        }

	public List<Appointment> search_unbill_history_daterange(String providerNo, Date startDate, Date endDate) {
		String sql = "select a from Appointment a where a.providerNo=? and a.appointmentDate >=? and a.appointmentDate<=? and (a.status='P' or a.status='H' or a.status='PV' or a.status='PS') and a.demographicNo <> 0 order by a.appointmentDate desc, a.startTime desc";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, providerNo);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);
		
		return query.getResultList();
	}
    
	public List<Appointment> findByDateAndProvider(Date date, String provider_no) {
		Query query = createQuery("a", "a.providerNo = :provider_no and a.appointmentDate = :date order by a.startTime asc");
		query.setParameter("provider_no", provider_no);
		query.setParameter("date", date);
		return query.getResultList();
    }
	
	public List<Appointment> search_appt(Date startTime, Date endTime, String providerNo) {
		String sql = "SELECT a FROM Appointment a WHERE a.appointmentDate >=? and a.appointmentDate <= ? and a.providerNo = ? order by a.appointmentDate,a.startTime,a.endTime";

		Query query = entityManager.createQuery(sql);
		query.setParameter(1, startTime);
		query.setParameter(2, endTime);
		query.setParameter(3, providerNo);

		
		List<Appointment> rs = query.getResultList();

		return rs;
	}
	
	//search_appt_name
	public List<Appointment> search_appt(Date date, String providerNo, Date startTime1, Date startTime2, Date endTime1, Date endTime2, Date startTime3, Date endTime3, Integer programId) {
		String sql = "select a from Appointment a where a.appointmentDate = ? and a.providerNo = ? and a.status <>'C' and ((a.startTime >= ? and a.startTime<= ?) or (a.endTime>= ? and a.endTime<= ?) or (a.startTime <= ? and a.endTime>= ?) ) and program_id=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, date);
		query.setParameter(2, providerNo);
		query.setParameter(3, startTime1);
		query.setParameter(4, startTime2);
		query.setParameter(5, endTime1);
		query.setParameter(6, endTime2);
		query.setParameter(7, startTime3);
		query.setParameter(8, endTime3);
		query.setParameter(9, programId);
		
		List<Appointment> rs = query.getResultList();

		return rs;
	}
	
    public List<Object[]> search_appt_future(Integer demographicNo, Date from, Date to) {
        StringBuilder sql = new StringBuilder("FROM Appointment a, Provider p " +
                "WHERE a.providerNo = p.ProviderNo and " +
                "a.demographicNo = ? and " +
                "a.appointmentDate >= ? and " +
                "a.appointmentDate < ?  " +
                "order by a.appointmentDate desc, a.startTime desc");
        
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter(1, demographicNo);
        query.setParameter(2, from);
        query.setParameter(3, to);
        

        return query.getResultList();
    }
    
    public List<Object[]> search_appt_past(Integer demographicNo, Date from, Date to) {
        StringBuilder sql = new StringBuilder("FROM Appointment a, Provider p " +
                "WHERE a.providerNo = p.ProviderNo and " +
                "a.demographicNo = ? and " +
                "a.appointmentDate < ? and " +
                "a.appointmentDate > ?  " +
                "order by a.appointmentDate desc, a.startTime desc");
        
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter(1, demographicNo);
        query.setParameter(2, from);
        query.setParameter(3, to);
        

        return query.getResultList();
    }
    
    public Appointment search_appt_no(String providerNo, Date appointmentDate, Date startTime, Date endTime, Date createDateTime, String creator, Integer demographicNo) {
    	String sql = "select a from Appointment a where a.providerNo=? and a.appointmentDate=? and a.startTime=? and "+
    				"a.endTime=? and a.createDateTime=? and a.creator=? and a.demographicNo=? order by a.id desc";
    	Query query = entityManager.createQuery(sql.toString());
        query.setParameter(1, providerNo);
        query.setParameter(2, appointmentDate);
        query.setParameter(3, startTime);
        query.setParameter(4, endTime);
        query.setParameter(5, createDateTime);
        query.setParameter(6, creator);
        query.setParameter(7, demographicNo);
        query.setMaxResults(1);
        
        return this.getSingleResultOrNull(query);
    }
    
    public List<Object[]> search_appt_data1(String providerNo, Date appointmentDate, Date startTime, Date endTime, Date createDateTime, String creator, Integer demographicNo) {
    	String sql = "from Provider prov, Appointment app " +
    			"where app.providerNo = prov.id and " +
    			"app.providerNo=? and " +
    			"app.appointmentDate=? and " + 
    			"app.startTime=? and "  +
    			"app.endTime=? and " +
    			"app.createDateTime=? and " + 
    			"app.creator=? and " +
    			"app.demographicNo=? " +
    			"order by app.id desc";
    	Query query = entityManager.createQuery(sql);
    	query.setMaxResults(1);
    	query.setParameter(1, providerNo);
    	 
         query.setParameter(2, appointmentDate);
         query.setParameter(3, startTime);
         query.setParameter(4, endTime);
         query.setParameter(5, createDateTime);
         query.setParameter(6, creator);
         query.setParameter(7, demographicNo);
         
         return query.getResultList();
    }
    
    public List<Object[]> export_appt(Integer demographicNo) {
    	String sql="from Appointment app, Provider prov where app.id = prov.id and app.demographicNo = ?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1, demographicNo);
         
        return query.getResultList();
    }
    
    public List<Appointment> search_otherappt(Date appointmentDate, Date startTime1, Date endTime1, Date startTime2, Date startTime3) {
    	String sql = "from Appointment a where a.appointmentDate=? and ((a.startTime <= ? and a.endTime >= ?) or (a.startTime > ? and a.startTime < ?) ) order by a.providerNo, a.startTime";
    	
    	
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1, appointmentDate);
         query.setParameter(2, startTime1);
         query.setParameter(3, endTime1);
         query.setParameter(4, startTime2);
         query.setParameter(5, startTime3);
          
         return query.getResultList();
    }
    
    public List<Appointment> search_group_day_appt(String myGroup, Integer demographicNo, Date appointmentDate) {
    	String sql = "select a  from Appointment a, MyGroup m " +
    			"where m.id.providerNo = a.providerNo " +
    			"and a.status <> 'C' " +
    			"and m.id.myGroupNo = ? " +
    			"and a.demographicNo = ? " +
    			"and a.appointmentDate = ?";
    	
    	
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1, myGroup);
        query.setParameter(2, demographicNo);
        query.setParameter(3, appointmentDate);
        
        return query.getResultList();
    }


	public Appointment findByDate(Date appointmentDate) {
		Query query = createQuery("a", "a.appointmentDate < :appointmentDate ORDER BY a.appointmentDate DESC");
		query.setMaxResults(1);
		query.setParameter("appointmentDate", appointmentDate);
		return getSingleResultOrNull(query);
    }
	
	public List<Object[]> findAppointmentAndProviderByAppointmentNo(Integer apptNo) {
		String sql = "FROM Appointment a, Provider p WHERE a.providerNo = p.ProviderNo AND a.id = :apptNo";
		Query query = entityManager.createQuery(sql);
		query.setParameter("apptNo", apptNo);
		return query.getResultList();
	}
    
    public List<Appointment> searchappointmentday(String providerNo, Date appointmentDate, Integer programId) {
    	String sql = "select a  from Appointment a where a.providerNo = ? and a.appointmentDate=? and a.programId = ? order by a.startTime, a.status DESC";
    	
    	
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1, providerNo);
        query.setParameter(2, appointmentDate);
        query.setParameter(3, programId);
        
        return query.getResultList();
    }

	@NativeSql({"demographic", "appointment", "drugs", "provider"})
    public List<Object[]> findAppointmentsByDemographicIds(Set<String> demoIds, Date from, Date to) {   	
		String sql = "" +
				"select " +
				"a.appointment_date, " +
				"concat(pAppt.first_name, ' ', pAppt.last_name), " +
				"concat(pFam.first_name, ' ', pFam.last_name), " +
				"bi.service_code, " +
				"drugs.BN, " +
				"concat(pDrug.first_name,' ',pDrug.last_name), " +
				"a.demographic_no, " +
				"drugs.GN, " +
				"drugs.customName " +
				"from demographic d," +
				"appointment a left outer join drugs " +
				"on drugs.demographic_no = a.demographic_no and drugs.rx_date = a.appointment_date and a.appointment_date >= :from and a.appointment_date <= :to and a.demographic_no in (:demoIds) " +
				" left join provider pDrug on pDrug.provider_no = drugs.provider_no, billing_on_cheader1 bc, billing_on_item bi, provider pAppt, provider pFam where a.appointment_date >= :from and a.appointment_date <= :to and a.demographic_no = d.demographic_no" +
				" and a.provider_no = pAppt.provider_no and d.provider_no = pFam.provider_no and bc.appointment_no = a.appointment_no and bi.ch1_id = bc.id and a.demographic_no in (:demoIds) order by a.demographic_no, a.appointment_date";
		
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("demoIds", demoIds);
		query.setParameter("from", from);
		query.setParameter("to", to);
		return query.getResultList();
    }
    
}
