package org.caisi.dao.hibernate;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.caisi.dao.DemographicDAO;
import org.caisi.model.Demographic;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation for the corresponding DAO interface
 * 
 * @author Marc Dumontier <a
 *         href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 * 
 */
public class DemographicDAOHibernate extends HibernateDaoSupport implements
		DemographicDAO
{

	public Demographic getDemographic(String demographic_no)
	{
		return (Demographic) this.getHibernateTemplate().get(Demographic.class,
				Long.valueOf(demographic_no));
	}

	// ADD BY PINE-SOFT
	public List getDemographics()
	{
		return this.getHibernateTemplate().find(
				"from Demographic p order by p.lastName");
	}

	public Demographic getDemographicById(Long demographic_id)
	{
		String q = "FROM Demographic d WHERE d.demographicNo=?";
		List rs = (List) getHibernateTemplate().find(q, demographic_id);
		if (rs.size() == 0)
			return null;
		else
			return (Demographic) rs.get(0);
	}

	public List getDemographicByProgram(int programId, Date dt, Date defdt)
	{
		/*
		 * get demographics according to their program, admit time, discharge
		 * time, ordered by lastname and first name
		 */
		String q = "Select d From Demographic d, Admission a "
				+ "Where d.demographicNo=a.clientId and a.programId=? and a.admissionDate<=? and "
				+ "(a.dischargeDate>=? or (a.dischargeDate is null) or a.dischargeDate=?)"
				+ " order by d.lastName,d.firstName";
		List rs;
		// q=" From org.caisi.model.Tickler ti";
		String status = "AC";// only show active clients
		rs = (List) getHibernateTemplate().find(q, new Object[]
		{ new Integer(programId), dt, dt, defdt });
		// rs=(List) getHibernateTemplate().find(q);
		return rs;
	}

	public List getActiveDemographicByProgram(int programId, Date dt, Date defdt)
	{
		/*
		 * get demographics according to their program, admit time, discharge
		 * time, ordered by lastname and first name
		 */
		String q= "Select d From Demographic d, Admission a " +
				"Where (d.patientStatus=? or d.patientStatus='' or d.patientStatus=null) and d.demographicNo=a.clientId and a.programId=? and a.admissionDate<=? and " +
				"(a.dischargeDate>=? or (a.dischargeDate is null) or a.dischargeDate=?)" +
				" order by d.lastName,d.firstName";
		List rs;
		// q=" From org.caisi.model.Tickler ti";
		String status = "AC";// only show active clients
		rs = (List) getHibernateTemplate().find(q, new Object[]
		{ status, new Integer(programId), dt, dt, defdt });
		// rs=(List) getHibernateTemplate().find(q);
		return rs;
	}

	public List getProgramIdByDemoNo(String demoNo)
	{
		String q = "Select a.programId From Admission a "
				+ "Where a.clientId=? and a.admissionDate<=? and "
				+ "(a.dischargeDate>=? or (a.dischargeDate is null) or a.dischargeDate=?)";
		/*default time is Oscar default null time 0001-01-01.*/
		Date defdt=new GregorianCalendar(1,0,1).getTime();
                GregorianCalendar cal = new GregorianCalendar();
                cal.set(GregorianCalendar.HOUR_OF_DAY, 23);
                cal.set(GregorianCalendar.MINUTE, 59);
                cal.set(GregorianCalendar.SECOND, 59);
		Date dt= cal.getTime();                
		/*
                dt.setHours(23);
		dt.setMinutes(59);
		dt.setSeconds(59);
                 */
		List rs = (List) getHibernateTemplate().find(q, new Object[]
		{demoNo, dt, dt, defdt});
		return rs;
	}
		public void clear() {
		getHibernateTemplate().clear();
		
	}

}
