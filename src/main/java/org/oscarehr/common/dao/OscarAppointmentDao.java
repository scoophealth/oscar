package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentArchive;
import org.oscarehr.common.model.Facility;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class OscarAppointmentDao extends AbstractDao<Appointment> {

	Logger logger = MiscUtils.getLogger();
	
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
	
	public List<Appointment> getAppointmentHistory(Integer demographicNo) {
		String sql = "select a from Appointment a where a.demographicNo=? order by a.appointmentDate DESC, a.startTime DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
		
		@SuppressWarnings("unchecked")
		List<Appointment> rs =  query.getResultList();
		
		return rs;
	}
	
	
	public void archiveAppointment(int appointmentNo) {
		Appointment appointment = this.find(appointmentNo);
		if(appointment!=null) {
			AppointmentArchive apptArchive = new AppointmentArchive(appointment.getId());
			try {
				BeanUtils.copyProperties(apptArchive, appointment);
			}catch(Exception e) {
				logger.error("Error copying values to archive bean",e);
			}
			entityManager.persist(apptArchive);
		}
	}
	
	public void updateAppointmentStatus(int appointmentNo, String status, String user) {
		Appointment appointment = this.find(appointmentNo);
		if(appointment != null) {
			archiveAppointment(appointmentNo);
			appointment.setStatus(status);
			appointment.setLastUpdateUser(user);
			merge(appointment);			
		}
	}
	
	@Override
	public void merge(Appointment appointment) {
		//archiveAppointment(appointment.getId());
		super.merge(appointment);
	}
	
    public List<Appointment> getAllByDemographicNo(Integer demographicNo) {
        String sql = "SELECT a FROM Appointment a WHERE a.demographicNo = "+demographicNo+" ORDER BY a.id";       
        Query query = entityManager.createQuery(sql);
		
        @SuppressWarnings("unchecked")
		List<Appointment> rs = query.getResultList();
		
		return rs;
    }

    public List<Appointment> findByDateRange(Date startTime, Date endTime) {
		String sql = "SELECT a FROM Appointment a WHERE a.appointmentDate >=? and a.appointmentDate < ?";

    	Query query = entityManager.createQuery(sql);
		query.setParameter(1, startTime);
		query.setParameter(2, endTime);
		@SuppressWarnings("unchecked")
        List<Appointment> rs = query.getResultList();

        return rs;
    }
    
}
