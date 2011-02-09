package com.quatro.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
// where is guy goes
public class ScratchPadDao extends HibernateDaoSupport {

	public boolean isScratchFilled(String providerNo) {
        ArrayList paramList = new ArrayList();
		String sSQL="SELECT s.scratch_text FROM ScratchPad s WHERE s.providerNo = ? order by s.id";		
        paramList.add(providerNo);
        Object params[] = paramList.toArray(new Object[paramList.size()]);
        List lst = getHibernateTemplate().find(sSQL ,params);

		if (lst.size()>0){
		  String obj= (String)lst.get(0);
		  return (obj.trim().length()>0);
		}
		else{
		  return false; 
		}
		
	}
	
/*
	public CaisiRole getRoleByProviderNo(String provider_no) {
		return (CaisiRole)this.getHibernateTemplate().find("from CaisiRole cr where cr.providerNo = ?",new Object[] {provider_no}).get(0);
	}

	public List getRolesByRole(String role) {
		return this.getHibernateTemplate().find("from CaisiRole cr where cr.role = ?",new Object[] {role});
	}
*/
	
/*	
    public List<CaisiRole> getRoles() {
		return this.getHibernateTemplate().find("from Role");
	}
*/
	
}
