package org.oscarehr.eyeform.dao;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.eyeform.model.Macro;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class MacroDao extends HibernateDaoSupport {

	public void save(Macro obj) {
		this.getHibernateTemplate().saveOrUpdate(obj);
	}
	
	public Macro find(int id) {
		return (Macro)getHibernateTemplate().get(Macro.class, id);
	}
	
	public List<Macro> getAll() {
		List<Macro> results = new ArrayList<Macro>();
		results = this.getHibernateTemplate().find("FROM Macro m");
		return results;
	}
}
