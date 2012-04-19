/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarBilling.ca.bc.data;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.Billing;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import oscar.entities.Billingmaster;
import oscar.entities.WCB;
import oscar.oscarDB.DBHandler;
import oscar.util.StringUtils;

/**
 *
 * @author jay
 */
@Repository
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class BillingmasterDAO {
    private static Logger log = MiscUtils.getLogger();

    @PersistenceContext
    protected EntityManager entityManager = null;
    /** Creates a new instance of BillingmasterDAO */
    public BillingmasterDAO() {
    }

    public List<Billingmaster> getBillingMasterWithStatus(String billingmasterNo,String status){
        log.debug("WHAT IS NULL ? "+billingmasterNo+"  status "+status+"   "+entityManager);
        Query query = entityManager.createQuery("select b from Billingmaster b where b.billingNo = (:billingNo) and billingstatus = (:status)");
        query.setParameter("billingNo",Integer.parseInt(billingmasterNo));
        query.setParameter("status", status);
        List<Billingmaster> list= query.getResultList();
        return list;
    }

    public List getBillingMasterByBillingNo(String billingNo){
        return getBillingmasterByBillingNo(Integer.parseInt(billingNo));
    }
    public List<Billingmaster> getBillingmasterByBillingNo(int billingNo){
        Query query = entityManager.createQuery("select b from Billingmaster b where b.billingNo = (:billingNo)");
        query.setParameter("billingNo",billingNo);
        List<Billingmaster> list= query.getResultList();
        return list;
    }

    public List<Billing> getPrivateBillings(String demographicNo){
        Query query = entityManager.createQuery("select b from Billing b where b.billingtype = 'Pri' and b.demographicNo = (:demographicNo)");
        query.setParameter("demographicNo",Integer.parseInt(demographicNo));
        return query.getResultList();
    }

    public Billingmaster getBillingmaster(String billingNo){
        return getBillingmaster(Integer.parseInt(billingNo));
    }

    public Billingmaster getBillingmaster(int billingmasterNo){
        Query query = entityManager.createQuery("select b from Billingmaster b where b.billingmasterNo = (:billingmasterNo)");
        query.setParameter("billingmasterNo",billingmasterNo);
        List<Billingmaster> list= query.getResultList();
        return list.get(0);
    }

    public void save(Billingmaster bm){
        entityManager.persist(bm);
    }

    public void save(WCB wcb){
        if (wcb.getW_doi() == null){
            wcb.setW_doi(new Date());  //Fixes SF ID : 2962864
        }
        entityManager.persist(wcb);
    }

    public void save(Billing billing){
        entityManager.persist(billing);
    }



    public void update(Billingmaster bm){
        entityManager.merge(bm);
    }

    public void update(Billing billing){
        entityManager.merge(billing);
    }



    public Billing getBilling(int billingNo){
        return entityManager.find(Billing.class, billingNo);
    }


    public List<WCB> getWCBForms(int demographic){
        Query query = entityManager.createQuery("select wcb from WCB wcb where wcb.demographic_no = (:demographicNo) order by wcb.id desc");
        query.setParameter("demographicNo", demographic);
        List<WCB> list  = query.getResultList();
        return list;
    }

    public List<WCB> getWCBForms(String demographic){
        return getWCBForms(Integer.parseInt(demographic));
    }

    public WCB getWCBForm(String formID){
        if (formID == null){
            return null;
        }
        MiscUtils.getLogger().debug("\nFORM ID "+formID);
        return entityManager.find(WCB.class,Integer.parseInt(formID));
    }


    public Billingmaster getBillingMasterByBillingMasterNo(String billingNo){
        return getBillingmaster(billingNo);
    }

    public void markListAsBilled(List list){                    //TODO: Should be set form CONST var
        String query = "update billingmaster set billingstatus = 'B' where billingmaster_no in ("+ StringUtils.getCSV(list) +")";
        try {

        	DBHandler.RunSQL(query);
        }catch (SQLException sqlexception) {
           MiscUtils.getLogger().debug(sqlexception.getMessage());
        }
    }


}
