/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.common.model.Icd9;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author toby
 */
public class Icd9Dao extends HibernateDaoSupport{

    public List<Icd9> getIcd9(String query) {
        List cList = null;
        Session session = null;
        try {
            session = getSession();
            cList = session.createCriteria(Icd9.class).add
                    (Restrictions.or(
                        Restrictions.like("icd9", "%" + query + "%"),
                        Restrictions.like("description", "%" + query + "%")))
                            .addOrder(Order.asc("description")).list();
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

}
