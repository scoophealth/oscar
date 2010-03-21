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
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author toby
 */
public class DxresearchDAO extends HibernateDaoSupport{

    public List getPatientRegisted(List dList) {

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
                    rList.add(new DxRegistedPTInfo(demo.getFirstName(),demo.getLastName(),demo.getSex(),
                                                    demo.getYearOfBirth()+"-"+demo.getMonthOfBirth()+"-"+demo.getDateOfBirth(),
                                                    demo.getPhone(),demo.getHin(),dxres.getCodingSystem(),dxres.getDxresearchCode(),
                                                    dxres.getStartDate().toString(),dxres.getUpdateDate().toString(),
                                                    dxres.getStatus().toString()));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
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


    public List patientRegistedDistincted(){
        List dList = null;
        Session session = null;
        try {
            session = getSession();
            dList = session.createQuery("select dxres from Dxresearch dxres group by dxres.demographicNo order by dxres.updateDate asc").list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                releaseSession(session);
            }
        }

        if (dList != null && dList.size() > 0) {
            Iterator i = dList.listIterator();
            while (i.hasNext())
                System.out.println(i.next());
            return getPatientRegisted (dList);
        } else {
            return null;
        }
    }

    public List patientRegistedAll(){
        List dList = null;
        Session session = null;
        try {
            session = getSession();
            dList = session.createQuery("select dxres from Dxresearch dxres order by dxres.demographicNo asc, dxres.updateDate asc").list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                releaseSession(session);
            }
        }

        if (dList != null && dList.size() > 0) {
            Iterator i = dList.listIterator();
            while (i.hasNext())
                System.out.println(i.next());
            return getPatientRegisted (dList);
        } else {
            return null;
        }
    }

    public List patientRegistedActive(){
        return patientRegistedStatus("A");
    }

    public List patientRegistedResolve(){
        return patientRegistedStatus("C");
    }

    public List patientRegistedDeleted(){
        return patientRegistedStatus("D");
    }

    public List patientRegistedStatus(String status){
        List dList = null;
        Session session = null;
        try {
            session = getSession();
            dList = session.createQuery("select dxres from Dxresearch dxres where dxres.status= '"+status+"' order by dxres.demographicNo asc, dxres.updateDate asc").list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                releaseSession(session);
            }
        }

        if (dList != null && dList.size() > 0) {
            Iterator i = dList.listIterator();
            while (i.hasNext())
                System.out.println(i.next());
            return getPatientRegisted (dList);
        } else {
            return null;
        }
    }

    public List patientRegisted(String dxResearchCode){
        return null;
    }
}
