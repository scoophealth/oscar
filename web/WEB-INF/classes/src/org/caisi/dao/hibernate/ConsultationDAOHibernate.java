package org.caisi.dao.hibernate;

import java.util.List;

import org.caisi.dao.ConsultationDAO;
import org.caisi.model.Consultation;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation for the corresponding DAO interface 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public class ConsultationDAOHibernate extends HibernateDaoSupport implements
		ConsultationDAO {

	public List getConsultations(String demographic_no) {
		return this.getHibernateTemplate().find("from Consultation c where c.demographic_no = ?", new Object[] {demographic_no});
	}

	public List getConsultationsByStatus(String demographic_no, String status) {
		return this.getHibernateTemplate().find("from Consultation c where c.demographic_no = ? and c.status = ?", new Object[] {demographic_no,status});
	}
	
	public Consultation getConsultation(Long requestId) {
		return (Consultation)this.getHibernateTemplate().get(Consultation.class,requestId);
	}
}
