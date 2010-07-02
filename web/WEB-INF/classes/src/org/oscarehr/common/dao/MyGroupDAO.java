/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.dao;

import java.util.List;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author Toby
 */
public class MyGroupDAO extends HibernateDaoSupport {

     public List getGroupDoctors (String groupNo){
        List dList = null;
        Session session = null;
        String HQL = "";

        if (groupNo != null) {
            HQL = "SELECT group.id.providerNo FROM Mygroup group WHERE group.id.mygroupNo= '" + groupNo + "'";

        }
        try {
            session = getSession();
            dList = session.createQuery(HQL).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                releaseSession(session);
            }
        }

        if (dList != null && dList.size() > 0) {
            return dList;
        } else {
            return null;
        }
     }
}
