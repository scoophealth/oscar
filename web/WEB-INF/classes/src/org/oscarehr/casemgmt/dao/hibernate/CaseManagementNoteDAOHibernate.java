package org.oscarehr.casemgmt.dao.hibernate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementSearchBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CaseManagementNoteDAOHibernate extends HibernateDaoSupport
		implements CaseManagementNoteDAO {
	
	private static Log log = LogFactory.getLog(CaseManagementNoteDAOHibernate.class);


	public CaseManagementNote getNote(Long id) {
		CaseManagementNote note = (CaseManagementNote)this.getHibernateTemplate().get(CaseManagementNote.class,id);
		getHibernateTemplate().initialize(note.getIssues());
		return note;
	}
	
	public List getNotesByDemographic(String demographic_no) {
		return this.getHibernateTemplate().find("from CaseManagementNote cmn where cmn.demographic_no = ? ORDER BY cmn.update_date DESC", new Object[] {demographic_no});
	}
	
	public List getNotesByDemographic(String demographic_no,String[] issues) {
		String list = null;
		if(issues != null && issues.length>0) {
			list="";
			for(int x=0;x<issues.length;x++) {
				if(x!=0) {
					list += ",";
				}
				list += issues[x];
			}
		}
		String hql = "select distinct cmn from CaseManagementNote cmn where cmn.demographic_no = ? and cmn.issues.issue_id in (" + list + ") ORDER BY cmn.update_date DESC";
		
		return this.getHibernateTemplate().find(hql,demographic_no);
	}

	public void saveNote(CaseManagementNote note) {
		this.getHibernateTemplate().saveOrUpdate(note);
	}

	public List search(CaseManagementSearchBean searchBean) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		Criteria criteria = getSession().createCriteria(CaseManagementNote.class);
		
		criteria.add(Expression.eq("demographic_no", searchBean.getDemographicNo()));
		
		if(searchBean.getSearchRoleId() > 0) {
			criteria.add(Expression.eq("reporter_caisi_role",String.valueOf(searchBean.getSearchRoleId())));
		}
		
		if(searchBean.getSearchProgramId()>0) {
			criteria.add(Expression.eq("program_no",String.valueOf(searchBean.getSearchProgramId())));			
		}
		
		try {
			Date startDate;
			Date endDate;
			if(searchBean.getSearchStartDate().length()>0) {
				startDate = formatter.parse(searchBean.getSearchStartDate());
			} else {
				startDate = formatter.parse("1970-01-01");
			}
			if(searchBean.getSearchEndDate().length()>0) {
				endDate = formatter.parse(searchBean.getSearchEndDate());
			} else {
				endDate = new Date();
			}
			criteria.add(Restrictions.between("update_date",startDate,endDate));
		}catch(ParseException e) {
			log.warn(e);
		}

		criteria.addOrder(Order.desc("update_date"));
		return criteria.list();
		
	}
	
	public List getAllNoteIds() {
		List results = this.getHibernateTemplate().find("select n.id from CaseManagementNote n");
		return results;
	}
}
