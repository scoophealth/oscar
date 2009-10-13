/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oscarehr.common.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.oscarehr.common.model.Billingreferral;

/**
 *
 * @author Toby
 */
public class BillingreferralDao extends HibernateDaoSupport {

    public List getBillingreferral(String referral_no) {

        List cList = null;
        Session session = null;
        try {
            session = getSession();
            cList = session.createCriteria(Billingreferral.class).add(Expression.eq("referralNo", referral_no)).addOrder(Order.asc("referralNo")).list();
        } catch (Exception e) {
            e.printStackTrace();
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

    public List<Billingreferral> getBillingreferral(String last_name, String first_name) {

        List cList = null;
        Session session = null;
        try {
            session = getSession();
            cList = session.createCriteria(Billingreferral.class).add(Restrictions.like("lastName", "%" + last_name + "%")).add(Restrictions.like("firstName", "%" + first_name + "%")).addOrder(Order.asc("lastName")).list();
        } catch (Exception e) {
            e.printStackTrace();
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

    public void updateBillingreferral(Billingreferral obj) {
        getHibernateTemplate().saveOrUpdate(obj);
    }
}
