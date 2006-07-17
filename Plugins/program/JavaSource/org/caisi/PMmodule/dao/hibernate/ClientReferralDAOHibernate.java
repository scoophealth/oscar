package org.caisi.PMmodule.dao.hibernate;

import java.util.List;

import org.caisi.PMmodule.dao.ClientReferralDAO;
import org.caisi.PMmodule.model.ClientReferral;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ClientReferralDAOHibernate extends HibernateDaoSupport implements
		ClientReferralDAO {

	public List getReferrals(Long clientId) {
		return this.getHibernateTemplate().find("from ClientReferral cr where cr.ClientId = ?",clientId);
	}
	
	public List getActiveReferrals(Long clientId) {
		return this.getHibernateTemplate().find("from ClientReferral cr where cr.ClientId = ? and cr.Status = 'active'",clientId);
	}

	public ClientReferral getClientReferral(Long id) {
		if(id==null) { return null;}
		return (ClientReferral)this.getHibernateTemplate().get(ClientReferral.class,id);
	}

	public void saveClientReferral(ClientReferral referral) {
		this.getHibernateTemplate().saveOrUpdate(referral);
	}

}
