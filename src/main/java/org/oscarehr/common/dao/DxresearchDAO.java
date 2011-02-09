/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DxRegistedPTInfo;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.oscarResearch.oscarDxResearch.bean.dxCodeSearchBean;
import oscar.oscarResearch.oscarDxResearch.bean.dxQuickListItemsHandler;

/**
 *
 * @author toby
 */
public class DxresearchDAO extends HibernateDaoSupport{

    public List getPatientRegisted(List dList, List doctorList) {

        List<DxRegistedPTInfo> rList = new ArrayList<DxRegistedPTInfo>();

        Session session = null;
        try {
            session = getSession();
            Iterator i = dList.listIterator();
            while (i.hasNext()) {
                //Integer demographicNo = (Integer)iter.next();
                  
                Dxresearch dxres = (Dxresearch)i.next();
                Integer demographicNo = dxres.getDemographicNo();
                Demographic demo = (Demographic)getHibernateTemplate().get(Demographic.class, demographicNo);

                if (demo != null ) {
                    String demoprovider = demo.getProviderNo();
                    boolean isDoctorPatient = false;
                    // if patient's doctor is not in the doctor's list then skip, "*" means all doctor
                    Iterator j = doctorList.listIterator();
                    while (j.hasNext()) {
                     String providerNo = (String)j.next();
                     if (providerNo.equalsIgnoreCase("*") || providerNo.equalsIgnoreCase(demoprovider)) {
                         isDoctorPatient = true;
                         break;
                     }
                    }
                 if (isDoctorPatient)
                    rList.add(new DxRegistedPTInfo(demo.getFirstName(),demo.getLastName(),demo.getSex(),
                                                    demo.getYearOfBirth()+"-"+demo.getMonthOfBirth()+"-"+demo.getDateOfBirth(),
                                                    demo.getPhone(),demo.getHin(),dxres.getCodingSystem(),dxres.getDxresearchCode(),
                                                    dxres.getStartDate().toString(),dxres.getUpdateDate().toString(),
                                                    dxres.getStatus().toString()));
                }
            }


        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        } finally {
            if (session != null) {
                releaseSession(session);
            }
        }

        if (rList != null && rList.size() > 0) {
            return rList;
        } else {
            return null;
        }
    }


    public List patientRegistedDistincted(List searchItems, List doctorList){
        List dList = null;
        Session session = null;
        List<dxCodeSearchBean> listItems = searchItems;
        String HQL = "";

        if (listItems != null && listItems.size()>0) {
            Iterator ite = listItems.listIterator();
            HQL = "SELECT dxres FROM Dxresearch dxres";

            if (ite.hasNext()) {
                HQL += " WHERE ";
            }

            while (ite.hasNext()) {
                dxCodeSearchBean bean = (dxCodeSearchBean)ite.next();
                String codeSys = bean.getType();
                String code = bean.getDxSearchCode();
                HQL += "dxres.codingSystem= '" + codeSys + "' AND dxres.dxresearchCode='" + code + "'";
                if (ite.hasNext()) {
                    HQL += " OR ";
                }
            }
            HQL += " GROUP BY dxres.demographicNo ORDER BY dxres.updateDate asc";
            
        } else {
            HQL = "SELECT dxres FROM Dxresearch dxres GROUP BY dxres.demographicNo ORDER BY dxres.updateDate asc";
        }
        try {
            session = getSession();
            dList = session.createQuery(HQL).list();
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        } finally {
            if (session != null) {
                releaseSession(session);
            }
        }

        if (dList != null && dList.size() > 0) {
            //Iterator i = dList.listIterator();
            //while (i.hasNext())

            return getPatientRegisted (dList,doctorList);
        } else {
            return null;
        }
    }

    public List patientRegistedAll(List searchItems, List doctorList){
        List dList = null;
        Session session = null;
        List<dxCodeSearchBean> listItems = searchItems;
        String HQL = "";

        if (listItems != null && listItems.size()>0) {
            Iterator ite = listItems.listIterator();
            HQL = "SELECT dxres FROM Dxresearch dxres";

            if (ite.hasNext()) {
                HQL += " WHERE ";
            }

            while (ite.hasNext()) {
                dxCodeSearchBean bean = (dxCodeSearchBean)ite.next();
                String codeSys = bean.getType();
                String code = bean.getDxSearchCode();
                HQL += "dxres.codingSystem= '" + codeSys + "' AND dxres.dxresearchCode='" + code + "'";
                if (ite.hasNext()) {
                    HQL += " OR ";
                }
            }
            HQL += " ORDER BY dxres.demographicNo asc, dxres.updateDate asc";
            
        } else {
            HQL = "SELECT dxres FROM Dxresearch dxres ORDER BY dxres.demographicNo asc, dxres.updateDate asc";
        }

        try {
            session = getSession();
            dList = session.createQuery(HQL).list();
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        } finally {
            if (session != null) {
                releaseSession(session);
            }
        }

        if (dList != null && dList.size() > 0) {
            //Iterator i = dList.listIterator();
            //while (i.hasNext())

            return getPatientRegisted (dList,doctorList);
        } else {
            return null;
        }
    }

    public List patientRegistedActive(List searchItems, List doctorList){
        return patientRegistedStatus("A",searchItems, doctorList);
    }

    public List patientRegistedResolve(List searchItems, List doctorList){
        return patientRegistedStatus("C",searchItems, doctorList);
    }

    public List patientRegistedDeleted(List searchItems, List doctorList){
        return patientRegistedStatus("D",searchItems, doctorList);
    }

    public List patientRegistedStatus(String status,List searchItems, List doctorList){
        List dList = null;
        Session session = null;
        List<dxCodeSearchBean> listItems = searchItems;
        String HQL = "";

        if (listItems != null && listItems.size()>0) {
            Iterator ite = listItems.listIterator();
            HQL = "SELECT dxres FROM Dxresearch dxres WHERE (";

            while (ite.hasNext()) {
                dxCodeSearchBean bean = (dxCodeSearchBean)ite.next();
                String codeSys = bean.getType();
                String code = bean.getDxSearchCode();
                HQL += "dxres.codingSystem= '" + codeSys + "' AND dxres.dxresearchCode='" + code + "'";
                if (ite.hasNext()) {
                    HQL += " OR ";
                }
            }
            if (listItems.size()>0)
                HQL += ") AND ";

            HQL += "dxres.status= '" + status + "' ORDER BY dxres.demographicNo asc, dxres.updateDate asc";
            
        } else {
            HQL = "SELECT dxres FROM Dxresearch dxres WHERE dxres.status= '" + status + "' ORDER BY dxres.demographicNo asc, dxres.updateDate asc";
            
        }
        try {
            session = getSession();
            dList = session.createQuery(HQL).list();
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        } finally {
            if (session != null) {
                releaseSession(session);
            }
        }

        if (dList != null && dList.size() > 0) {
            //Iterator i = dList.listIterator();
            //while (i.hasNext())

            return getPatientRegisted (dList,doctorList);
        } else {
            return null;
        }
    }

    public List getQuickListItems(String quickListName){

        List<dxCodeSearchBean> listItems = new ArrayList<dxCodeSearchBean>();
        listItems.addAll(new dxQuickListItemsHandler(quickListName).getDxQuickListItemsVector());
        return listItems;
    }
}
