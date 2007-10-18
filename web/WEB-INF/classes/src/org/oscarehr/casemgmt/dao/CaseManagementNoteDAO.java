/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/


package org.oscarehr.casemgmt.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementSearchBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CaseManagementNoteDAO extends HibernateDaoSupport {
	
	private static Log log = LogFactory.getLog(CaseManagementNoteDAO.class);

        public List getHistory(CaseManagementNote note) {
            String uuid = note.getUuid();
            return this.getHibernateTemplate().find("from CaseManagementNote cmn where cmn.uuid = ? order by cmn.update_date asc", uuid);
        }

	public CaseManagementNote getNote(Long id) {
		CaseManagementNote note = (CaseManagementNote)this.getHibernateTemplate().get(CaseManagementNote.class,id);
		getHibernateTemplate().initialize(note.getIssues());
		return note;
	}
	
	//This was created by OSCAR. if all notes' UUID are same like null, it will only get one note.
	 public List getNotesByDemographic(String demographic_no) {            
           return this.getHibernateTemplate().findByNamedQuery("mostRecent", new Object[] {demographic_no});
	}
	
	 //This is the original method. It was created by CAISI, to get all notes for each client.
	/*public List getNotesByDemographic(String demographic_no) {
		return this.getHibernateTemplate().find("from CaseManagementNote cmn where cmn.demographic_no = ? ORDER BY cmn.update_date DESC", new Object[] {demographic_no});
	}
	*/
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
		//String hql = "select distinct cmn from CaseManagementNote cmn where cmn.demographic_no = ? and cmn.issues.issue_id in (" + list + ") and cmn.id in (select max(cmn.id) from cmn GROUP BY uuid) ORDER BY cmn.observation_date asc";
		String hql = "select distinct cmn from CaseManagementNote cmn join cmn.issues i where i.issue_id in (" + list + ") and cmn.demographic_no = ? and cmn.id in (select max(cmn.id) from cmn GROUP BY uuid) ORDER BY cmn.observation_date asc";
		return this.getHibernateTemplate().find(hql,demographic_no);
	}

	public void saveNote(CaseManagementNote note) {
                if( note.getUuid() == null ) {
                    UUID uuid = UUID.randomUUID();
                    note.setUuid(uuid.toString());
                }
		this.getHibernateTemplate().save(note);
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
