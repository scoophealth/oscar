package org.caisi.dao.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.caisi.dao.DemographicDAO;
import org.oscarehr.PMmodule.model.Demographic;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation for the corresponding DAO interface
 * 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 */
public class DemographicDAOHibernate extends HibernateDaoSupport implements DemographicDAO {

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

		if (rs.size() == 0)
			return null;
		else
			return (Demographic) rs.get(0);
	}

	/*
	 * get demographics according to their program, admit time, discharge time, ordered by lastname and first name
	 */
	public List getDemographicByProgram(int programId, Date dt, Date defdt) {
		String q = "Select d From Demographic d, Admission a " + "Where d.DemographicNo=a.DlientId and a.ProgramId=? and a.AdmissionDate<=? and " + "(a.DischargeDate>=? or (a.DischargeDate is null) or a.DischargeDate=?)" + " order by d.LastName,d.FirstName";
		List rs = (List) getHibernateTemplate().find(q, new Object[] { new Integer(programId), dt, dt, defdt });
		return rs;
	}

	/*
	 * get demographics according to their program, admit time, discharge time, ordered by lastname and first name
	 */
	public List getActiveDemographicByProgram(int programId, Date dt, Date defdt) {
		String q = "Select d From Demographic d, Admission a " + "Where (d.PatientStatus=? or d.PatientStatus='' or d.PatientStatus=null) and d.DemographicNo=a.ClientId and a.ProgramId=? and a.AdmissionDate<=? and " + "(a.DischargeDate>=? or (a.DischargeDate is null) or a.DischargeDate=?)"
		        + " order by d.LastName,d.FirstName";

		String status = "AC"; // only show active clients
		List rs = (List) getHibernateTemplate().find(q, new Object[] { status, new Integer(programId), dt, dt, defdt });

		return rs;
	}

	public List getProgramIdByDemoNo(String demoNo) {
		String q = "Select a.ProgramId From Admission a " + "Where a.ClientId=? and a.AdmissionDate<=? and " + "(a.DischargeDate>=? or (a.DischargeDate is null) or a.DischargeDate=?)";

		/* default time is Oscar default null time 0001-01-01. */
		Date defdt = new GregorianCalendar(1, 0, 1).getTime();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		Date dt = cal.getTime();	

		List rs = (List) getHibernateTemplate().find(q, new Object[] { demoNo, dt, dt, defdt });
		return rs;
	}

	public void clear() {
		getHibernateTemplate().clear();

	}

	public List getDemoProgram(String demoNo) {
		String q = "Select a.ProgramId From Admission a Where a.ClientId=?";
		List rs = (List) getHibernateTemplate().find(q, new Object[] { demoNo });
		return rs;
	}

}
