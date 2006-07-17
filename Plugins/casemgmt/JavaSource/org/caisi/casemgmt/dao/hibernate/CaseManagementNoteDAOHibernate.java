package org.caisi.casemgmt.dao.hibernate;

import java.util.List;

import org.caisi.casemgmt.dao.CaseManagementNoteDAO;
import org.caisi.casemgmt.model.CaseManagementNote;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CaseManagementNoteDAOHibernate extends HibernateDaoSupport
		implements CaseManagementNoteDAO {

	public CaseManagementNote getNote(Long id) {
		CaseManagementNote note = (CaseManagementNote)this.getHibernateTemplate().get(CaseManagementNote.class,id);
		getHibernateTemplate().initialize(note.getIssues());
		return note;
	}
	
	public List getNotesByDemographic(String demographic_no) {
		return this.getHibernateTemplate().find("from CaseManagementNote cmn where cmn.demographic_no = ?", new Object[] {demographic_no});
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
		String hql = "select distinct cmn from CaseManagementNote cmn where cmn.demographic_no = ? and cmn.issues.issue_id in (" + list + ")";
		System.out.println(hql);
		return this.getHibernateTemplate().find(hql,demographic_no);
	}

	public void saveNote(CaseManagementNote note) {
		this.getHibernateTemplate().saveOrUpdate(note);
	}

}
