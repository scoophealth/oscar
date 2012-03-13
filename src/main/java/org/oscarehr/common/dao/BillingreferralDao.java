/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oscarehr.common.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.common.model.Billingreferral;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author Toby
 */
public class BillingreferralDao extends HibernateDaoSupport {

	 public Billingreferral getByReferralNo(String referral_no) {
		 String sql = "From Billingreferral br WHERE br.referralNo=?";

		 @SuppressWarnings("unchecked")
		 List<Billingreferral> brs = this.getHibernateTemplate().find(sql,referral_no);
		 if(!brs.isEmpty())
			 return brs.get(0);
		 return null;
	 }

	 public Billingreferral getById(int id) {
		 return this.getHibernateTemplate().get(Billingreferral.class, id);
	 }

	 public List<Billingreferral> getBillingreferrals() {

		 @SuppressWarnings("unchecked")
		 List<Billingreferral> referrals = this.getHibernateTemplate().find("From Billingreferral b order by b.lastName, b.firstName");
		 return referrals;
	 }

    public List<Billingreferral> getBillingreferral(String referral_no) {

        List cList = null;
        Session session = null;
        try {
            session = getSession();
            cList = session.createCriteria(Billingreferral.class).add(Expression.eq("referralNo", referral_no)).addOrder(Order.asc("referralNo")).list();
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        } finally {
            if (session != null) {
                releaseSession(session);
            }
        }

        if (cList != null && cList.size() > 0) {
            return cList;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Billingreferral> getBillingreferral(String last_name, String first_name) {

        List<Billingreferral> cList = null;
        Session session = null;
        try {
            session = getSession();
            cList = session.createCriteria(Billingreferral.class).add(Restrictions.like("lastName", "%" + last_name + "%")).add(Restrictions.like("firstName", "%" + first_name + "%")).addOrder(Order.asc("lastName")).list();
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        } finally {
            if (session != null) {
                releaseSession(session);
            }
        }

        if (cList != null && cList.size() > 0) {
            return cList;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Billingreferral> getBillingreferralByLastName(String last_name) {

        List<Billingreferral> cList = null;
        Session session = null;
        try {
            session = getSession();
            cList = session.createCriteria(Billingreferral.class).add(Restrictions.like("lastName", "%" + last_name + "%")).addOrder(Order.asc("lastName")).list();
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        } finally {
            if (session != null) {
                releaseSession(session);
            }
        }

        if (cList != null && cList.size() > 0) {
            return cList;
        } else {
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    public List<Billingreferral> getBillingreferralBySpecialty(String specialty) {

        List<Billingreferral> cList = null;
        Session session = null;
        try {
            session = getSession();
            cList = session.createCriteria(Billingreferral.class).add(Restrictions.like("specialty", "%" + specialty + "%")).addOrder(Order.asc("lastName")).list();
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        } finally {
            if (session != null) {
                releaseSession(session);
            }
        }

        if (cList != null && cList.size() > 0) {
            return cList;
        } else {
            return null;
        }
    }

    /*
     * Don't blame me for this one, converted from SQL.
     */
    @SuppressWarnings("unchecked")
    public List<Billingreferral> searchReferralCode(String codeName, String codeName1, String codeName2, String desc, String fDesc, String desc1, String fDesc1, String desc2, String fDesc2) {

        List<Billingreferral> cList = null;

        cList = this.getHibernateTemplate().find("SELECT b FROM Billingreferral b WHERE b.referralNo LIKE ? or b.referralNo LIKE ? or b.referralNo LIKE ?"
        		+ " or (b.lastName LIKE ? and b.firstName LIKE ?)"
        		+ " or (b.lastName LIKE ? and b.firstName LIKE ?)"
        		+ " or (b.lastName LIKE ? and b.firstName LIKE ?)"
        		, new Object[]{codeName, codeName1,codeName2, desc,fDesc,desc1,fDesc1,desc2,fDesc2});

        return cList;
    }

    public void updateBillingreferral(Billingreferral obj) {
    	if(obj.getBillingreferralNo() == null || obj.getBillingreferralNo().intValue() == 0) {
    		getHibernateTemplate().save(obj);
    	} else {
    		getHibernateTemplate().update(obj);
    	}
    }

	 public String getReferralDocName(String referral_no) {
		 String sql = "From Billingreferral br WHERE br.referralNo=?";

		 @SuppressWarnings("unchecked")
		 List<Billingreferral> brs = this.getHibernateTemplate().find(sql,referral_no);
		 if(!brs.isEmpty())
			 return brs.get(0).getLastName() + ", " + brs.get(0).getFirstName();

		 return "";
	 }
}
