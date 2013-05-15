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


package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Document;
import org.oscarehr.common.model.ProviderInboxItem;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;

public class DocumentResultsDao extends AbstractDao<Document>{

    Logger logger = Logger.getLogger(DocumentResultsDao.class);

    public DocumentResultsDao() {
        super(Document.class);
    }

    public boolean isSentToValidProvider(String docNo){//check if document attached to any existing provider
        if(docNo!=null){
            int dn=Integer.parseInt(docNo.trim());
            String sql="select p from ProviderInboxItem p where p.labType='DOC' and p.labNo="+dn;
            try{
                Query query=entityManager.createQuery(sql);
                @SuppressWarnings("unchecked")
                List<ProviderInboxItem> r=query.getResultList();
                if(r!=null && r.size()>0){
                    ProviderInboxItem pii=r.get(r.size()-1);
                    String pns=pii.getProviderNo();
                    if(pns.equals("000000"))//takes care of case when a provider number is 000000
                        return true;
                    else if (pns.equalsIgnoreCase("null"))
                        return false;
                    else{
                        int pn = Integer.parseInt(pns);
                        if(pn>0){
                            return true;
                        }else
                            return false;
                        }
                }
                else return false;
            }catch(Exception e){
                MiscUtils.getLogger().error("Error", e);
                return false;
            }
        }else return false;
    }
    public boolean isSentToProvider(String docNo, String providerNo){
        if(docNo!=null && providerNo!=null){
            int dn=Integer.parseInt(docNo.trim());
            providerNo=providerNo.trim();
            String sql="select p from ProviderInboxItem p where p.labType='DOC' and p.labNo="+dn+" and p.providerNo='"+providerNo+"'";
            try{
                Query query=entityManager.createQuery(sql);
                @SuppressWarnings("unchecked")
                List<ProviderInboxItem> r=query.getResultList();
                if(r!=null && r.size()>0){
                    return true;
                }else
                    return false;
            }catch(Exception e){
                MiscUtils.getLogger().error("Error", e);
                return false;
            }
        }else{
            return false;
        }
    }

    public ArrayList<LabResultData> populateDocumentResultsDataOfAllProviders(String providerNo, String demographicNo,
            String status) {

        if ( providerNo == null) { providerNo = ""; }
        if ( status == null ) { status = ""; }


        ArrayList<LabResultData> labResults =  new ArrayList<LabResultData>();
        String sql = "";
        try {
            //
            if ( demographicNo == null) {
                sql="select d from Document d, ProviderInboxItem p where d.documentNo=p.labNo and p.status like '%"+status+"%' "+
                        " and p.labType='DOC' order by d.documentNo DESC";
            } else {
                return labResults;
            }

            logger.debug(sql);
            Query query=entityManager.createQuery(sql);
            @SuppressWarnings("unchecked")
            List<Document> result=query.getResultList();
            for(Document d:result){
                LabResultData lbData = new LabResultData(LabResultData.DOCUMENT);
                lbData.labType = LabResultData.DOCUMENT;
                lbData.segmentID = d.getDocumentNo().toString();


                if (demographicNo == null && !providerNo.equals("0")) {
                    lbData.acknowledgedStatus = Character.toString(d.getStatus());
                } else {
                    lbData.acknowledgedStatus ="U";
                }

                lbData.healthNumber = "";
                lbData.patientName = "Not, Assigned";//change to use internationalization
                lbData.sex = "";


                //BAD!!!! CODING APROACHING
                DocumentDao documentDao=(DocumentDao) SpringUtils.getBean("documentDao");
                Demographic demo =documentDao.getDemoFromDocNo(lbData.segmentID);

                lbData.isMatchedToPatient = false;
                if(demo!=null){
                     lbData.patientName = demo.getLastName()+ ", "+demo.getFirstName();
                     lbData.healthNumber = demo.getHin();
                     lbData.sex = demo.getSex();
                     lbData.isMatchedToPatient = true;
                     lbData.setLabPatientId(Integer.toString(demo.getDemographicNo()));
                }
                if(lbData.getPatientName().equalsIgnoreCase("Not, Assigned"))
                    lbData.setLabPatientId("-1");
                logger.debug("DOCU<ENT "+lbData.isMatchedToPatient());
                lbData.accessionNumber = "";

                lbData.resultStatus = "N";

                lbData.dateTime = d.getObservationdate().toString();
                lbData.setDateObj(d.getObservationdate());

                //priority
                String priority = "";

                if(priority != null && !priority.equals("")){
                    switch ( priority.charAt(0) ) {
                        case 'C' : lbData.priority = "Critical"; break;
                        case 'S' : lbData.priority = "Stat/Urgent"; break;
                        case 'U' : lbData.priority = "Unclaimed"; break;
                        case 'A' : lbData.priority = "ASAP"; break;
                        case 'L' : lbData.priority = "Alert"; break;
                        default: lbData.priority = "Routine"; break;
                    }
                }else{
                    lbData.priority = "----";
                }

                lbData.requestingClient = "";
                lbData.reportStatus =  "F";

                // the "C" is for corrected excelleris labs
                if (lbData.reportStatus != null && (lbData.reportStatus.equals("F") || lbData.reportStatus.equals("C"))){
                    lbData.finalRes = true;
                }else{
                    lbData.finalRes = false;
                }

                lbData.discipline = d.getDoctype();
                if (lbData.discipline.trim().equals("")){
                    lbData.discipline = null;
                }

                lbData.finalResultsCount = 0;
                labResults.add(lbData);
            }

        }catch(Exception e){
            logger.error("exception in DOCPopulate:", e);
        }
        return labResults;
    }
    //retrieve documents belonging to a provider
    public ArrayList<LabResultData> populateDocumentResultsDataLinkToProvider(String providerNo, String demographicNo,
            String status){

        if ( status == null ) { status = ""; }


        ArrayList<LabResultData> labResults =  new ArrayList<LabResultData>();
        String sql = "";
        try {
            if ( demographicNo == null || providerNo==null) {
                sql="select d from Document d, ProviderInboxItem p where d.documentNo=p.labNo and p.status like '%"+status+"%' and p.providerNo = '"
                        +providerNo+"'"+" and p.labType='DOC' order by d.documentNo DESC";
            } else {
                return labResults;
            }

            logger.debug(sql);
            Query query=entityManager.createQuery(sql);
            @SuppressWarnings("unchecked")
            List<Document> result=query.getResultList();
            for(Document d:result){
                LabResultData lbData = new LabResultData(LabResultData.DOCUMENT);
                lbData.labType = LabResultData.DOCUMENT;
                lbData.segmentID = d.getDocumentNo().toString();

                if (demographicNo == null && !providerNo.equals("0")) {
                    lbData.acknowledgedStatus = Character.toString(d.getStatus());
                } else {
                    lbData.acknowledgedStatus ="U";
                }

                lbData.patientName = "Not, Assigned";//change to use internationalization

                DocumentDao documentDao=(DocumentDao) SpringUtils.getBean("documentDao");
                Demographic demo =documentDao.getDemoFromDocNo(lbData.segmentID);

                lbData.isMatchedToPatient = false;
                if(demo!=null){
                     lbData.patientName = demo.getLastName()+ ", "+demo.getFirstName();
                     lbData.healthNumber = demo.getHin();
                     lbData.sex = demo.getSex();
                     lbData.isMatchedToPatient = true;
                     lbData.setLabPatientId(Integer.toString(demo.getDemographicNo()));
                }
                if(lbData.getPatientName().equalsIgnoreCase("Not, Assigned"))
                    lbData.setLabPatientId("-1");
                logger.debug("DOCUMENT "+lbData.isMatchedToPatient());

                lbData.resultStatus = "N";
                
                lbData.dateTime = d.getObservationdate().toString();
                lbData.setDateObj(d.getObservationdate());

                //priority
                // Wow the following couple of lines of code will never work from what I can tell (but hey it's been a long day so maybe it does some how) it's always (true && false) which equals (false), who knows what jackson was thinking, I can't be bothered to fix it right now because I don't know what it was suppose to do.
                String priority = "";
                if(priority != null && !priority.equals("")){
                    switch ( priority.charAt(0) ) {
                        case 'C' : lbData.priority = "Critical"; break;
                        case 'S' : lbData.priority = "Stat/Urgent"; break;
                        case 'U' : lbData.priority = "Unclaimed"; break;
                        case 'A' : lbData.priority = "ASAP"; break;
                        case 'L' : lbData.priority = "Alert"; break;
                        default: lbData.priority = "Routine"; break;
                    }
                }else{
                    lbData.priority = "----";
                }

                lbData.reportStatus =  "F";

                // the "C" is for corrected excelleris labs
                if (lbData.reportStatus != null && (lbData.reportStatus.equals("F") || lbData.reportStatus.equals("C"))){
                    lbData.finalRes = true;
                }else{
                    lbData.finalRes = false;
                }

                lbData.discipline = StringUtils.trimToNull(d.getDoctype());

                lbData.finalResultsCount = 0;
                labResults.add(lbData);
            }

        }catch(Exception e){
            logger.error("exception in DOCPopulate:", e);
        }
        return labResults;


    }
    //retrieve all documents from database
    public ArrayList<LabResultData> populateDocumentResultsData(String providerNo, String demographicNo, String status) {

        if ( providerNo == null) { providerNo = ""; }
        if ( status == null ) { status = ""; }


        ArrayList<LabResultData> labResults =  new ArrayList<LabResultData>();
        String sql = "";
        try {

            if ( demographicNo == null) {
                sql="select d from Document d, ProviderInboxItem p where d.documentNo=p.labNo and p.status like '%"+status+"%' and (p.providerNo like '"+
                        (providerNo.equals("")?"%":providerNo)+"'"+" or p.providerNo='"+CommonLabResultData.NOT_ASSIGNED_PROVIDER_NO+"' ) "+
                        " and p.labType='DOC' order by d.documentNo DESC";
            } else {
                return labResults;
            }

            logger.debug(sql);
            Query query=entityManager.createQuery(sql);
            @SuppressWarnings("unchecked")
            List<Document> result=query.getResultList();
            for(Document d:result){
                LabResultData lbData = new LabResultData(LabResultData.DOCUMENT);
                lbData.labType = LabResultData.DOCUMENT;
                lbData.segmentID = d.getDocumentNo().toString();


                if (demographicNo == null && !providerNo.equals("0")) {
                    lbData.acknowledgedStatus = Character.toString(d.getStatus());
                } else {
                    lbData.acknowledgedStatus ="U";
                }

                lbData.healthNumber = "";
                lbData.patientName = "Not, Assigned";//change to use internationalization
                lbData.sex = "";


                //BAD!!!! CODING APROACHING
                DocumentDao documentDAO=(DocumentDao) SpringUtils.getBean("documentDao");
                Demographic demo =documentDAO.getDemoFromDocNo(lbData.segmentID);

                lbData.isMatchedToPatient = false;
                if(demo!=null){
                     lbData.patientName = demo.getLastName()+ ", "+demo.getFirstName();
                     lbData.healthNumber = demo.getHin();
                     lbData.sex = demo.getSex();
                     lbData.isMatchedToPatient = true;
                     lbData.setLabPatientId(Integer.toString(demo.getDemographicNo()));
                }
                if(lbData.getPatientName().equalsIgnoreCase("Not, Assigned"))
                    lbData.setLabPatientId("-1");
                logger.debug("DOCU<ENT "+lbData.isMatchedToPatient());
                lbData.accessionNumber = "";

                lbData.resultStatus = "N";

                lbData.dateTime = d.getObservationdate().toString();
                lbData.setDateObj(d.getObservationdate());

                //priority
                String priority = "";

                if(priority != null && !priority.equals("")){
                    switch ( priority.charAt(0) ) {
                        case 'C' : lbData.priority = "Critical"; break;
                        case 'S' : lbData.priority = "Stat/Urgent"; break;
                        case 'U' : lbData.priority = "Unclaimed"; break;
                        case 'A' : lbData.priority = "ASAP"; break;
                        case 'L' : lbData.priority = "Alert"; break;
                        default: lbData.priority = "Routine"; break;
                    }
                }else{
                    lbData.priority = "----";
                }

                lbData.requestingClient = "";
                lbData.reportStatus =  "F";

                // the "C" is for corrected excelleris labs
                if (lbData.reportStatus != null && (lbData.reportStatus.equals("F") || lbData.reportStatus.equals("C"))){
                    lbData.finalRes = true;
                }else{
                    lbData.finalRes = false;
                }

                lbData.discipline = d.getDoctype();
                if (lbData.discipline.trim().equals("")){
                    lbData.discipline = null;
                }

                lbData.finalResultsCount = 0;
                labResults.add(lbData);
            }

        }catch(Exception e){
            logger.error("exception in DOCPopulate:", e);
        }
        return labResults;
    }

    public List<Document> getPhotosByAppointmentNo(int appointmentNo) {
    	Query query = this.entityManager.createNamedQuery("Document.findPhotosByAppointmentNo");
    	query.setParameter("appointmentNo", appointmentNo);

    	@SuppressWarnings("unchecked")
    	List<Document> results =  query.getResultList();

    	return results;
    }
}
