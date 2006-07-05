package org.caisi.dao.hibernate;

import java.util.List;

import org.caisi.dao.BedProgramDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateBedProgramDao extends HibernateDaoSupport implements BedProgramDao{
	private String bedType="Geographical"; 
	
	private List getProgramResultList(String q) {
		return getHibernateTemplate().find(q);
	}
	private List getProgramResultList(String q1,Object obj) {
		return getHibernateTemplate().find(q1,obj);
	}
	
	public List getAllBedProgram() {
		String qr= "FROM Program p where p.type = ?";
		List rs = getProgramResultList(qr,bedType);
		return rs;
	}
	public List getAllProgram() {
		String qr= "FROM Program p order by p.name";
		List rs = getProgramResultList(qr);
		return rs;
	}
	
	public List getAllProgramNameID() {
		String qr= "select p.name, p.id from Program p";
		List rs = getProgramResultList(qr);
		return rs;
	}
	public List getAllProgramName() {
		String qr= "select p.name from Program p";
		List rs = getProgramResultList(qr);
		return rs;
	}
	public List getProgramIdByName(String name)
	{
		String q="SELECT p.id FROM Program p WHERE p.name = ?";
		List rs=getProgramResultList(q,name);
		return rs;
	}

}
