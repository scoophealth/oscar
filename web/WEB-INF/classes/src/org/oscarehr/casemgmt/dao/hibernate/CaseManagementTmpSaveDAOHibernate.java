package org.oscarehr.casemgmt.dao.hibernate;

import java.util.List;

import org.oscarehr.casemgmt.model.CaseManagementTmpSave;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CaseManagementTmpSaveDAOHibernate extends HibernateDaoSupport implements
		org.oscarehr.casemgmt.dao.CaseManagementTmpSaveDAO {

	public void delete(String providerNo, Long demographicNo, Long programId) {
		List results = this.getHibernateTemplate().find("from CaseManagementTmpSave c where c.providerNo=? and c.demographicNo=? and c.programId=?"
				,new Object[] {providerNo,demographicNo,programId});
		this.getHibernateTemplate().deleteAll(results);
	}

	public CaseManagementTmpSave load(String providerNo, Long demographicNo, Long programId) {
		List results = this.getHibernateTemplate().find("from CaseManagementTmpSave c where c.providerNo=? and c.demographicNo=? and c.programId=? order by c.update_date DESC"
				,new Object[] {providerNo,demographicNo,programId});
		if(!results.isEmpty()) {
			return (CaseManagementTmpSave)results.get(0);
		}
		
		return null;
	}

	public void save(CaseManagementTmpSave obj) {
		this.getHibernateTemplate().saveOrUpdate(obj);
	}

}
