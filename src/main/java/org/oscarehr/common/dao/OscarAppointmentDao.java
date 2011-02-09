package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Facility;
import org.springframework.stereotype.Repository;

@Repository
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
		
		@SuppressWarnings("unchecked")
		List<Facility> results = query.getResultList();
		
		if(!results.isEmpty())
			return true;
		
		return false;
	}
	
}
