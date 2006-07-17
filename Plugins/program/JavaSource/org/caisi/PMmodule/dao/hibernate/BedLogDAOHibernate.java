package org.caisi.PMmodule.dao.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.caisi.PMmodule.dao.BedLogDAO;
import org.caisi.PMmodule.model.BedLog;
import org.caisi.PMmodule.model.BedLogSheet;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BedLogDAOHibernate extends HibernateDaoSupport implements
		BedLogDAO {

	public void saveBedLog(BedLog bedlog) {
		this.getHibernateTemplate().saveOrUpdate(bedlog);
	}

	public List getBedLogsByProgram(Long programId) {
		return this.getHibernateTemplate().find("from BedLog b where b.ProgramId = ?",programId);
	}

	public List getBedLogsByClient(Long demographicNo) {
		return this.getHibernateTemplate().find("from BedLog b where b.DemographicNo = ?",demographicNo);
		
	}

	public List getBedLogs(Long programId, Date day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		Date startTime = cal.getTime();
		
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		Date endTime = cal.getTime();
		
		Criteria criteria = getSession().createCriteria(BedLog.class);
		criteria.add(Expression.ge("dateCreated",startTime));
		criteria.add(Expression.le("dateCreated",endTime));
		criteria.add(Expression.eq("ProgramId",programId));
		return criteria.list();
	}

	public BedLogSheet getLastSheet(Long programId) {
		List results = this.getHibernateTemplate().find("from BedLogSheet s where s.ProgramId = ? ORDER BY s.dateCreated DESC",programId);
		if(results.size()>0) {
			BedLogSheet sheet = (BedLogSheet)results.get(0);
			return sheet;
		}
		return null;
	}
	
	public List getBedLogsBySheet(Long sheetId) {
		return this.getHibernateTemplate().find("from BedLog b where b.SheetId = ?",sheetId);
	}
	
	public void saveBedLogSheet(BedLogSheet sheet) {
		this.getHibernateTemplate().save(sheet);
	}
	
	public List searchBedLogs(BedLog bedlog) {
		Criteria criteria = getSession().createCriteria(BedLog.class);
		criteria.add(Expression.eq("DemographicNo",bedlog.getDemographicNo()));
		criteria.add(Expression.eq("ProgramId",bedlog.getProgramId()));
		criteria.add(Expression.eq("SheetId",bedlog.getSheetId()));
		criteria.add(Expression.eq("Time",bedlog.getTime()));
		
		return criteria.list();
	}
	
	public List getBedLogSheetsByProgram(Long programId) {
		return this.getHibernateTemplate().find("from BedLogSheet s where s.ProgramId = ? ORDER BY s.Id DESC",programId);
	}
	
	public BedLogSheet getSheet(Long sheetId) {
		return (BedLogSheet)this.getHibernateTemplate().get(BedLogSheet.class,sheetId);
	}
}
