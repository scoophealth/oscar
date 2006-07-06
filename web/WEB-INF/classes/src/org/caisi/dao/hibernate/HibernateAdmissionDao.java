package org.caisi.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.caisi.dao.AdmissionDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateAdmissionDao extends HibernateDaoSupport implements AdmissionDao
{
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HibernateAdmissionDao.class);

	public List getClientIdByProgramDate(int programId, Date dt)
	{
		String q="FROM Admission a WHERE a.programId=? and a.admissionDate<=? and (a.dischargeDate>=? or (a.dischargeDate is null))";
		logger.debug("enter HibernateAdmissionDao");
		List rs=this.getHibernateTemplate().find(q,new Object[]{new Integer(programId),dt,dt});
		logger.debug(rs);
		return rs;
	}


}
