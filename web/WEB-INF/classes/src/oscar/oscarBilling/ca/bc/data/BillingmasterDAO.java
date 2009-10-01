/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * BillingmasterDAO.java
 *
 * Created on January 24, 2007, 11:57 PM
 *
 */

package oscar.oscarBilling.ca.bc.data;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import oscar.entities.Billing;
import oscar.entities.Billingmaster;
import oscar.entities.WCB;
import oscar.oscarDB.DBHandler;
import oscar.util.SqlUtils;
import oscar.util.StringUtils;

/**
 *
 * @author jay
 */
@Repository
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class BillingmasterDAO {
    @PersistenceContext
    protected EntityManager entityManager = null;
    /** Creates a new instance of BillingmasterDAO */
    public BillingmasterDAO() {
    }
    
    public List getBillingMasterWithStatus(String billingmasterNo,String status){
        String query = "select * from billingmaster where billing_no='"+ billingmasterNo +"' and billingstatus='"+status+"'"; 
        return getBillingMaster(query);
    }

    public List getBillingMasterByBillingNo(String billingNo){
        String query = "select * from billingmaster where billing_no='"+ billingNo +"'"; 
        return getBillingMaster(query);
    }
    
    public void save(Billingmaster bm){
        entityManager.persist(bm);
        //getHibernateTemplate().save((bm));
    }
    
    public void save(WCB wcb){
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
        return (Billing) entityManager.find(Billing.class, billingNo);
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
        System.out.println("\nFORM ID "+formID);
        return entityManager.find(WCB.class,Integer.parseInt(formID));
    }
    
    
    public Billingmaster getBillingMasterByBillingMasterNo(String billingNo){
        String query = "select * from billingmaster where billingmaster_no='"+ billingNo +"'"; 
        List l = getBillingMaster(query);
        Billingmaster billingmaster = null;
        try{
            billingmaster =(Billingmaster) l.get(0);
        }catch(Exception e){}
        return billingmaster;
    }
    
    private List getBillingMaster(String qry) {
        List res = SqlUtils.getBeanList(qry, Billingmaster.class);
        return res;
    }
    
    public void markListAsBilled(List list){                    //TODO: Should be set form CONST var
        String query = "update billingmaster set billingstatus = 'B' where billingmaster_no in ("+ StringUtils.getCSV(list) +")"; 
        try {             
           DBHandler dbhandler = new DBHandler(DBHandler.OSCAR_DATA);
           dbhandler.RunSQL(query);                                              //1 2 3 4 5 6 7 8 9 0 1 2 3
           dbhandler.CloseConn();
        }catch (SQLException sqlexception) {
           System.out.println(sqlexception.getMessage());
        }
    }
    
    
}
