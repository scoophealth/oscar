package org.caisi.dao.hibernate;

import java.util.List;

import org.caisi.dao.BedProgramDao;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
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
	
	public String[] getProgramInfo(int programId) {
		String[] result = new String[3];
		
		SQLQuery query = getSession().createSQLQuery("SELECT name,address,phone,fax from program where program_id=" + programId);
		query.addScalar("name", Hibernate.STRING);
		query.addScalar("address",Hibernate.STRING);
		query.addScalar("phone",Hibernate.STRING);
		query.addScalar("fax",Hibernate.STRING);
		Object[] o = (Object[])query.uniqueResult();
		if(o != null) {
			result[0] = new String(o[0] + "\n" + o[1]);
			result[1] = (String)o[2];
			result[2] = (String)o[3];
		}
		return result;
	}

}
