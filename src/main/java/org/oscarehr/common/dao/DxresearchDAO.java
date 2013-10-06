/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DxRegistedPTInfo;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.util.SpringUtils;
import org.springframework.stereotype.Repository;

import oscar.oscarResearch.oscarDxResearch.bean.dxCodeSearchBean;
import oscar.oscarResearch.oscarDxResearch.bean.dxQuickListItemsHandler;

/**
 *
 * @author toby
 */
@Repository
public class DxresearchDAO extends AbstractDao<Dxresearch>{


	public DxresearchDAO() {
		super(Dxresearch.class);
	}

    public List<DxRegistedPTInfo> getPatientRegisted(List<Dxresearch> dList, List<String> doctorList) {

        List<DxRegistedPTInfo> rList = new ArrayList<DxRegistedPTInfo>();



        Iterator<Dxresearch> i = dList.listIterator();
        while (i.hasNext()) {

            Dxresearch dxres = i.next();
            Integer demographicNo = dxres.getDemographicNo();

            DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
            Demographic demo = demographicDao.getClientByDemographicNo(demographicNo);

            if (demo != null ) {
                String demoprovider = demo.getProviderNo();
                boolean isDoctorPatient = false;
                // if patient's doctor is not in the doctor's list then skip, "*" means all doctor
                Iterator<String> j = doctorList.listIterator();
                while (j.hasNext()) {
                 String providerNo = j.next();
                 if (providerNo.equalsIgnoreCase("*") || providerNo.equalsIgnoreCase(demoprovider)) {
                     isDoctorPatient = true;
                     break;
                 }
                }


                if (isDoctorPatient && demo.getPatientStatus().equalsIgnoreCase("AC"))
                	rList.add(new DxRegistedPTInfo(demo.getFirstName(),demo.getLastName(),demo.getSex(),
                                                demo.getYearOfBirth()+"-"+demo.getMonthOfBirth()+"-"+demo.getDateOfBirth(),
                                                demo.getPhone(),demo.getHin(),dxres.getCodingSystem(),dxres.getDxresearchCode(),
                                                dxres.getStartDate().toString(),dxres.getUpdateDate().toString(),
                                                dxres.getStatus().toString()));
            }
        }

        if (rList != null && rList.size() > 0) {
            return rList;
        } else {
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    public List<DxRegistedPTInfo> patientRegistedDistincted(List<dxCodeSearchBean> searchItems, List<String> doctorList){
        List<Dxresearch> dList = null;
       List<dxCodeSearchBean> listItems = searchItems;
        String HQL = "";

        Query query = null;
        if (listItems != null && listItems.size()>0) {
            Iterator<dxCodeSearchBean> ite = listItems.listIterator();
            HQL = "SELECT dxres FROM Dxresearch dxres";

            if (ite.hasNext()) {
                HQL += " WHERE ";
            }

            while (ite.hasNext()) {
                dxCodeSearchBean bean = ite.next();
                String codeSys = bean.getType();
                String code = bean.getDxSearchCode();
                HQL += "dxres.codingSystem= '" + codeSys + "' AND dxres.dxresearchCode='" + code + "'";
                if (ite.hasNext()) {
                    HQL += " OR ";
                }
            }
            HQL += " GROUP BY dxres.demographicNo ORDER BY dxres.updateDate asc";
            query = entityManager.createQuery(HQL);
        } else {
            HQL = "SELECT dxres FROM Dxresearch dxres GROUP BY dxres.demographicNo ORDER BY dxres.updateDate asc";
            query = entityManager.createQuery(HQL);
        }

        dList = query.getResultList();

        if (dList != null && dList.size() > 0) {
            return getPatientRegisted (dList,doctorList);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<DxRegistedPTInfo> patientRegistedAll(List<dxCodeSearchBean> searchItems, List<String> doctorList){
        List<Dxresearch> dList = null;
        List<dxCodeSearchBean> listItems = searchItems;
        String HQL = "";
        Query query = null;
        if (listItems != null && listItems.size()>0) {
            Iterator<dxCodeSearchBean> ite = listItems.listIterator();
            HQL = "SELECT dxres FROM Dxresearch dxres";

            if (ite.hasNext()) {
                HQL += " WHERE ";
            }

            while (ite.hasNext()) {
                dxCodeSearchBean bean = ite.next();
                String codeSys = bean.getType();
                String code = bean.getDxSearchCode().trim();
                HQL += "dxres.codingSystem= '" + codeSys + "' AND dxres.dxresearchCode='" + code + "'";
                if (ite.hasNext()) {
                    HQL += " OR ";
                }
            }
            HQL += " ORDER BY dxres.demographicNo asc, dxres.updateDate asc";
            query = entityManager.createQuery(HQL);
        } else {
            HQL = "SELECT dxres FROM Dxresearch dxres ORDER BY dxres.demographicNo asc, dxres.updateDate asc";
            query = entityManager.createQuery(HQL);
        }

        dList = query.getResultList();

        if (dList != null && dList.size() > 0) {
            //Iterator i = dList.listIterator();
            //while (i.hasNext())

            return getPatientRegisted (dList,doctorList);
        } else {
            return null;
        }
    }

    public List<DxRegistedPTInfo> patientRegistedActive(List<dxCodeSearchBean> searchItems, List<String> doctorList){
        return patientRegistedStatus("A",searchItems, doctorList);
    }

    public List<DxRegistedPTInfo> patientRegistedResolve(List<dxCodeSearchBean> searchItems, List<String> doctorList){
        return patientRegistedStatus("C",searchItems, doctorList);
    }

    public List<DxRegistedPTInfo> patientRegistedDeleted(List<dxCodeSearchBean> searchItems, List<String> doctorList){
        return patientRegistedStatus("D",searchItems, doctorList);
    }

    @SuppressWarnings("unchecked")
    public List<DxRegistedPTInfo> patientRegistedStatus(String status,List<dxCodeSearchBean> searchItems, List<String> doctorList){
        List<Dxresearch> dList = null;

        List<dxCodeSearchBean> listItems = searchItems;
        String HQL = "";

        Query query = null;
        if (listItems != null && listItems.size()>0) {
            Iterator<dxCodeSearchBean> ite = listItems.listIterator();
            HQL = "SELECT dxres FROM Dxresearch dxres WHERE (";

            while (ite.hasNext()) {
                dxCodeSearchBean bean = ite.next();
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
            query = entityManager.createQuery(HQL);
        } else {
            HQL = "SELECT dxres FROM Dxresearch dxres WHERE dxres.status= '" + status + "' ORDER BY dxres.demographicNo asc, dxres.updateDate asc";
            query = entityManager.createQuery(HQL);
        }
        dList = query.getResultList();


        if (dList != null && dList.size() > 0) {
            //Iterator i = dList.listIterator();
            //while (i.hasNext())

            return getPatientRegisted (dList,doctorList);
        } else {
            return null;
        }
    }

    public List<dxCodeSearchBean> getQuickListItems(String quickListName){

        List<dxCodeSearchBean> listItems = new ArrayList<dxCodeSearchBean>();
        listItems.addAll(new dxQuickListItemsHandler(quickListName).getDxQuickListItemsVector());
        return listItems;
    }

    public List<Dxresearch> getDxResearchItemsByPatient(Integer demographicNo) {
    	String hql = "select d from Dxresearch d where d.demographicNo=?";
    	Query query = entityManager.createQuery(hql);
    	query.setParameter(1,demographicNo);

    	@SuppressWarnings("unchecked")
    	List<Dxresearch> items = query.getResultList();

    	return items;
    }

	public void save(Dxresearch d) {
		if(d.getId() != null && d.getId().intValue()>0) {
			merge(d);
		} else {
			persist(d);
		}

	}

	public List<Dxresearch> getByDemographicNo(int demographicNo) {
		String hql = "select d from Dxresearch d where d.demographicNo=? and d.status='A'";
    	Query query = entityManager.createQuery(hql);
    	query.setParameter(1,demographicNo);

    	@SuppressWarnings("unchecked")
    	List<Dxresearch> items = query.getResultList();

		return items;
	}
	
	public List<Dxresearch> getByDemographicNoSince(int demographicNo,Date lastUpdateDate) {
		String hql = "select d from Dxresearch d where d.demographicNo=? and d.status='A' and d.updateDate > ?";
    	Query query = entityManager.createQuery(hql);
    	query.setParameter(1,demographicNo);
    	query.setParameter(2, lastUpdateDate);

    	@SuppressWarnings("unchecked")
    	List<Dxresearch> items = query.getResultList();

		return items;
	}
	
	//for integrator
	public List<Integer> getByDemographicNoSince(Date lastUpdateDate) {
		String hql = "select d.demographicNo from Dxresearch d where d.updateDate > ?";
    	Query query = entityManager.createQuery(hql);
    	query.setParameter(1, lastUpdateDate);

    	@SuppressWarnings("unchecked")
    	List<Integer> items = query.getResultList();

		return items;
	}

	public boolean entryExists(int demographicNo, String codeType, String code) {
		String hql = "select d from Dxresearch d where d.demographicNo=? and d.codingSystem=? and d.code=?";
    	Query query = entityManager.createQuery(hql);
    	query.setParameter(1,demographicNo);
    	query.setParameter(2,codeType);
    	query.setParameter(3,code);

    	@SuppressWarnings("unchecked")
    	List<Dxresearch> items = query.getResultList();

		return !items.isEmpty();
	}

	public void removeAllAssociationEntries() {
		String hql = "DELETE from DxResearch dx WHERE dx.association=true";
    	Query query = entityManager.createQuery(hql);

		query.executeUpdate();
	}
}
