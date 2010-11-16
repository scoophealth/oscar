/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * *  
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   Creates a new instance of Prevention
 *
 * CombinePDFAction.java
 *
 * Created on July 29, 2006, 8:35 PM
 *
 */

package oscar.dms.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.PMmodule.utility.UtilDateUtilities;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DocumentResultsDao;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;
import org.oscarehr.common.dao.QueueDao;
import org.oscarehr.common.dao.QueueDocumentLinkDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProviderInboxItem;
import org.oscarehr.common.model.QueueDocumentLink;
import org.oscarehr.document.dao.DocumentDAO;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.oscarProvider.data.ProviderData;
import oscar.util.OscarRoleObjectPrivilege;

import com.quatro.dao.security.SecObjectNameDao;
import com.quatro.model.security.Secobjectname;

/**
 *
 * @author jackson
 */
public class DmsInboxManageAction extends DispatchAction {
    private ProviderInboxRoutingDao providerInboxRoutingDAO=null;
    private QueueDocumentLinkDao queueDocumentLinkDAO=null;
    private SecObjectNameDao secObjectNameDao=null;
    private SecUserRoleDao secUserRoleDao = (SecUserRoleDao) SpringUtils.getBean("secUserRoleDao");
    private QueueDao queueDAO=(QueueDao)SpringUtils.getBean("queueDao");
    public void setProviderInboxRoutingDAO(ProviderInboxRoutingDao providerInboxRoutingDAO){
        this.providerInboxRoutingDAO = providerInboxRoutingDAO;
    }
    public void setQueueDocumentLinkDAO(QueueDocumentLinkDao queueDocumentLinkDAO){
        this.queueDocumentLinkDAO=queueDocumentLinkDAO;
    }

    public void setSecObjectNameDao(SecObjectNameDao secObjectNameDao){
        this.secObjectNameDao=secObjectNameDao;
    }
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        
        
        return null;
       }
    private void setProviderDocsInSession(ArrayList<EDoc> privatedocs,HttpServletRequest request){
        ArrayList providers = ProviderData.getProviderListOfAllTypes();
        Hashtable providerDocs=new Hashtable();
        for(int i=0;i<providers.size();i++){
            Hashtable ht=(Hashtable)providers.get(i);
            List<EDoc> EDocs=new ArrayList();
            String providerNo=(String)ht.get("providerNo");
            providerDocs.put(providerNo , EDocs);
        }
        for(int i=0;i<privatedocs.size();i++){
            EDoc eDoc=privatedocs.get(i);
            List providerList=new ArrayList();
            String createrId=eDoc.getCreatorId();
            if(providerDocs.containsKey(createrId)){
                List<EDoc> EDocs=new ArrayList();
                EDocs=(List<EDoc>)providerDocs.get(createrId);
                EDocs.add(eDoc);
                providerDocs.put(createrId, EDocs);            }
            String docId=eDoc.getDocId();
            providerList.add(createrId);

            List<ProviderInboxItem> routeList=providerInboxRoutingDAO.getProvidersWithRoutingForDocument(LabResultData.DOCUMENT, docId);
            for(ProviderInboxItem pii:routeList){
                String routingPId=pii.getProviderNo();

                if(!routingPId.equals(createrId) && providerDocs.containsKey(routingPId)){
                    List<EDoc> EDocs=new ArrayList();
                    EDocs=(List<EDoc>)providerDocs.get(routingPId);
                    EDocs.add(eDoc);
                    providerDocs.put(routingPId, EDocs);
                }
            }
        }
        //remove providers which has no docs linked to
        Enumeration keys=providerDocs.keys();
        while(keys.hasMoreElements()){
            String key=(String)keys.nextElement();

            List<EDoc> EDocs=new ArrayList();
            EDocs=(List<EDoc>)providerDocs.get(key);
            if(EDocs==null || EDocs.size()==0){
                providerDocs.remove(key);

            }
        }

        request.getSession().setAttribute("providerDocs", providerDocs);
    }


    private void setQueueDocsInSession(ArrayList<EDoc> privatedocs,HttpServletRequest request){
         //docs according to queue name
        Hashtable queueDocs=new Hashtable();
        QueueDao queueDao = (QueueDao) SpringUtils.getBean("queueDao");
        List<Hashtable> queues=queueDao.getQueues();
        for(int i=0;i<queues.size();i++){
            Hashtable ht=(Hashtable)queues.get(i);
            List<EDoc> EDocs=new ArrayList();
            String queueId=(String)ht.get("id");
            queueDocs.put(queueId, EDocs);
        }
        MiscUtils.getLogger().debug("queueDocs="+queueDocs);
        for(int i=0;i<privatedocs.size();i++){
            EDoc eDoc=privatedocs.get(i);
            List queueList=new ArrayList();
            String docIdStr=eDoc.getDocId();
            Integer docId=-1;
            if(docIdStr!=null && !docIdStr.equalsIgnoreCase("")){
                docId=Integer.parseInt(docIdStr);
            }
            List<QueueDocumentLink> queueDocLinkList=queueDocumentLinkDAO.getQueueFromDocument(docId);

            for(QueueDocumentLink qdl:queueDocLinkList){
                Integer qidInt=qdl.getQueueId();
                String qidStr=qidInt.toString();
                MiscUtils.getLogger().debug("qid in link="+qidStr);
                if(queueDocs.containsKey(qidStr)){
                    List<EDoc> EDocs=new ArrayList();
                    EDocs=(List<EDoc>)queueDocs.get(qidStr);
                    EDocs.add(eDoc);
                    MiscUtils.getLogger().debug("add edoc id to queue id="+eDoc.getDocId());
                    queueDocs.put(qidStr, EDocs);
                }
            }       
        }

            //remove queues which has no docs linked to
            Enumeration queueIds=queueDocs.keys();
            while(queueIds.hasMoreElements()){
                String queueId=(String)queueIds.nextElement();
                List<EDoc> eDocs=new ArrayList();
                eDocs=(List<EDoc>)queueDocs.get(queueId);
                if(eDocs==null || eDocs.size()==0){
                    queueDocs.remove(queueId);
                    MiscUtils.getLogger().debug("removed queueId="+queueId);
                }
            }

        request.getSession().setAttribute("queueDocs", queueDocs);
    }

    private void addQueueSecObjectName(String queuename, String queueid){
        String q="_queue.";
        if(queuename!=null && queueid!=null ){
            q+=queueid;
            Secobjectname sbn=new Secobjectname();
            sbn.setObjectname(q);
            sbn.setDescription(queuename);
            sbn.setOrgapplicable(0);
            secObjectNameDao.saveOrUpdate(sbn);
        }
    }
    private boolean isSegmentIDUnique(ArrayList doclabs,LabResultData data){
        boolean unique=true;
        String sID=(data.segmentID).trim();
        for(int i=0;i<doclabs.size();i++){
            LabResultData lrd=(LabResultData)doclabs.get(i);
            if(sID.equals((lrd.segmentID).trim())){
                unique=false;
                break;
            }
        }
        return unique;
    }
    
    public ActionForward previewPatientDocLab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String demographicNo=request.getParameter("demog");
        String docs=request.getParameter("docs");
        String labs=request.getParameter("labs");
        String providerNo=request.getParameter("providerNo");
        String searchProviderNo=request.getParameter("searchProviderNo");
        String ackStatus=request.getParameter("ackStatus");
        ArrayList<EDoc> docPreview=new ArrayList();
        ArrayList labPreview=new ArrayList();
    
        if(docs.length()==0){
            //do nothing
        }else{
            String[] did=docs.split(",");
            List<String> didList=new ArrayList();
            for(int i=0;i<did.length;i++){
                if(did[i].length()>0){
                    didList.add(did[i]);
                }
            }
            if(didList.size()>0)
                docPreview=EDocUtil.listDocsPreviewInbox(didList);
            
        }
        
        if(labs.length()==0){
            //do nothing
        }else{
            String[] labids=labs.split(",");
            List<String> ls=new ArrayList();
            for(int i=0;i<labids.length;i++){
                if(labids.length>0)
                    ls.add(labids[i]);
            }
            
            Hl7textResultsData hrd=new Hl7textResultsData();
            if(ls.size()>0)
                labPreview=hrd.getNotAckLabsFromLabNos(ls);
        }
        
        request.setAttribute("docPreview",docPreview);
        request.setAttribute("labPreview",labPreview);
        request.setAttribute("providerNo", providerNo);
        request.setAttribute("searchProviderNo",searchProviderNo );
        request.setAttribute("ackStatus",ackStatus);
        DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");
    	Demographic demographic=demographicDao.getDemographic(demographicNo);
        String demoName="Not,Assigned";
        if(demographic!=null)
            demoName=demographic.getFirstName()+","+demographic.getLastName();
        request.setAttribute("demoName", demoName);
        return mapping.findForward("doclabPreview");
    }



    public ActionForward prepareForIndexPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session=request.getSession();
        try{if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");}
        catch(Exception e){MiscUtils.getLogger().error("Error", e);}
        
        //can't use userrole from session, because it changes if provider A search for provider B's documents

    //oscar.oscarMDS.data.MDSResultsData mDSData = new oscar.oscarMDS.data.MDSResultsData();
    CommonLabResultData comLab = new CommonLabResultData();
    //String providerNo = request.getParameter("providerNo");
    String providerNo =  (String) session.getAttribute("user");
    String searchProviderNo = request.getParameter("searchProviderNo");
    String ackStatus = request.getParameter("status");
    String demographicNo = request.getParameter("demographicNo"); // used when searching for labs by patient instead of provider
    String scannedDocStatus = request.getParameter("scannedDocument");
    scannedDocStatus = "I";

    if ( ackStatus == null ) { ackStatus = "N"; } // default to new labs only
    if ( providerNo == null ) { providerNo = ""; }
    if ( searchProviderNo == null ) { searchProviderNo = providerNo; }
        String roleName="";
    List<SecUserRole> roles=secUserRoleDao.getUserRoles(searchProviderNo);
    for(SecUserRole r:roles){
        if(roleName.length()==0){
            roleName=r.getRoleName();
            
        }else{
            roleName+=","+r.getRoleName();
        }
    }
    roleName+=","+searchProviderNo;
    //mDSData.populateMDSResultsData2(searchProviderNo, demographicNo, request.getParameter("fname"), request.getParameter("lname"), request.getParameter("hnum"), ackStatus);
    //HashMap<String,String> docQueue=comLab.getDocumentQueueLinks();
    List<QueueDocumentLink> qd=queueDocumentLinkDAO.getQueueDocLinks();
    HashMap<String,String> docQueue=new HashMap();
    for(QueueDocumentLink qdl: qd){
        Integer i=qdl.getDocId();
        Integer n=qdl.getQueueId();
        docQueue.put(i.toString(), n.toString());
    }
    ArrayList labdocs = comLab.populateLabResultsData(searchProviderNo, demographicNo, request.getParameter("fname"), request.getParameter("lname"), request.getParameter("hnum"), ackStatus,scannedDocStatus);

    ArrayList validlabdocs=new ArrayList();

    DocumentResultsDao documentResultsDao=(DocumentResultsDao)SpringUtils.getBean("documentResultsDao");
    //check privilege for documents only
    for(int i=0;i<labdocs.size();i++){
            LabResultData data=(LabResultData)labdocs.get(i);
            if(data.isDocument()){
               String docid=data.getSegmentID();

                String queueid=docQueue.get(docid);                
                if(queueid!=null){ 
                    queueid=queueid.trim();
                    //if doc sent to default queue and no valid provider, include it if it's unique
                    if(queueid.equals("1")&&!documentResultsDao.isSentToValidProvider(docid) && isSegmentIDUnique(validlabdocs,data)){
                        validlabdocs.add(data);
                    }
                    //if doc sent to default queue && valid provider, check if it's sent to this provider, if yes include it
                    else if(queueid.equals("1")&&documentResultsDao.isSentToValidProvider(docid)
                            &&documentResultsDao.isSentToProvider(docid, searchProviderNo)&&isSegmentIDUnique(validlabdocs,data))
                    {
                        validlabdocs.add(data);
                    }
                    //if doc setn to non-default queue and valid provider, check if provider is in the queue or equal to the provider
                    else if(!queueid.equals("1")&&documentResultsDao.isSentToValidProvider(docid)){
                        Vector vec=OscarRoleObjectPrivilege.getPrivilegeProp("_queue."+queueid);
                        if(OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties)vec.get(0), (Vector)vec.get(1))
                                || documentResultsDao.isSentToProvider(docid, searchProviderNo)){
                            //labs is in provider's queue,do nothing
                           if(isSegmentIDUnique(validlabdocs,data))
                           {
                                validlabdocs.add(data);
                           }
                        }
                    }
                    //if doc sent to non default queue and no valid provider, check if provider is in the non default queue
                    else if(!queueid.equals("1")&&!documentResultsDao.isSentToValidProvider(docid)){
                        Vector vec=OscarRoleObjectPrivilege.getPrivilegeProp("_queue."+queueid);
                        if(OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties)vec.get(0), (Vector)vec.get(1))){
                            //labs is in provider's queue,do nothing
                           if(isSegmentIDUnique(validlabdocs,data)){
                               validlabdocs.add(data);
                           }
                                
                        }
                    }
                }
            }else{//add lab
                if(isSegmentIDUnique(validlabdocs,data))
                           {validlabdocs.add(data);}
            }
    }
    labdocs=validlabdocs;
    Collections.sort(labdocs);
  
    HashMap labMap = new HashMap();
    LinkedHashMap accessionMap = new LinkedHashMap();
    LabResultData result;

    for( int i = 0; i < labdocs.size(); i++ ) {
        result = (LabResultData) labdocs.get(i);
        labMap.put(result.segmentID, result);
        ArrayList labNums = new ArrayList();

        if (result.accessionNumber == null || result.accessionNumber.equals("")){
            labNums.add(result.segmentID);
            accessionMap.put("noAccessionNum"+i+result.labType, labNums);
        }else if (!accessionMap.containsKey(result.accessionNumber+result.labType)){
            labNums.add(result.segmentID);
            accessionMap.put(result.accessionNumber+result.labType, labNums);

        // Different MDS Labs may have the same accession Number if they are seperated
        // by two years. So accession numbers are limited to matching only if their
        // labs are within one year of eachother
        }else{
            labNums = (ArrayList) accessionMap.get(result.accessionNumber+result.labType);
            boolean matchFlag = false;
            for (int j=0; j < labNums.size(); j++){
                LabResultData matchingResult = (LabResultData) labMap.get(labNums.get(j));

                Date dateA = result.getDateObj();
                Date dateB = matchingResult.getDateObj();
                int monthsBetween = 0;
                if (dateA.before(dateB)){
                    monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
                }else{
                    monthsBetween = UtilDateUtilities.getNumMonths(dateB, dateA);
                }

                if (monthsBetween < 4){
                    matchFlag = true;
                    break;
                }
            }
            if (!matchFlag){
                labNums.add(result.segmentID);
                accessionMap.put(result.accessionNumber+result.labType, labNums);
            }
        }
    }
    ArrayList labArrays = new ArrayList(accessionMap.values());
    labdocs.clear();

    for (int i=0; i < labArrays.size(); i++){
        ArrayList labNums = (ArrayList) labArrays.get(i);
        // must sort through in reverse to keep the labs in the correct order
        for (int j=labNums.size()-1; j >= 0; j--){
            labdocs.add(labMap.get(labNums.get(j)));
        }
    }

    Collections.sort(labdocs);

    int pageNum = 1;
    if ( request.getParameter("pageNum") != null ) {
        pageNum = Integer.parseInt(request.getParameter("pageNum"));
    }
    /* find all data for the index.jsp page*/
  Hashtable patientDocs=new Hashtable();
    Hashtable patientIdNames=new Hashtable();
    String patientIdNamesStr="";
    Hashtable docStatus=new Hashtable();
    Hashtable docType=new Hashtable();
    Hashtable<String,List<String>> ab_NormalDoc=new Hashtable();

    for(int i=0;i<labdocs.size();i++){
        LabResultData data=(LabResultData)labdocs.get(i);

        List<String> segIDs=new ArrayList();
        String labPatientId=data.getLabPatientId();
        if(labPatientId==null || labPatientId.equals("-1"))
            labPatientId="-1";

        if(data.isAbnormal()){
            List<String> abns=ab_NormalDoc.get("abnormal");
            if(abns==null){
                abns=new ArrayList();
                abns.add(data.getSegmentID());
            }else{
                abns.add(data.getSegmentID());
            }
            ab_NormalDoc.put("abnormal",abns);
        }else{
            List<String> ns=ab_NormalDoc.get("normal");
            if(ns==null){
                ns=new ArrayList();
                ns.add(data.getSegmentID());
            }else{
                ns.add(data.getSegmentID());
            }
            ab_NormalDoc.put("normal",ns);
        }
        if(patientDocs.containsKey(labPatientId)) {

            segIDs=(List)patientDocs.get(labPatientId);
            segIDs.add(data.getSegmentID());
            patientDocs.put(labPatientId,segIDs);
        }else{
            segIDs.add(data.getSegmentID());
            patientDocs.put(labPatientId, segIDs);
            patientIdNames.put(labPatientId, data.patientName);
            patientIdNamesStr+=";"+labPatientId+"="+data.patientName;
        }
        docStatus.put(data.getSegmentID(), data.getAcknowledgedStatus());
        docType.put(data.getSegmentID(), data.labType);
    }

    Integer totalDocs=0;
    Integer totalHL7=0;
    Hashtable<String,List<String>> typeDocLab=new Hashtable();
    Enumeration keys=docType.keys();
    while(keys.hasMoreElements()){
        String keyDocLabId=((String)keys.nextElement());
        String valType=(String)docType.get(keyDocLabId);

        if(valType.equalsIgnoreCase("DOC")){
            if(typeDocLab.containsKey("DOC")){
                List<String> docids=(List<String>)typeDocLab.get("DOC");
                docids.add(keyDocLabId);//add doc id to list
                typeDocLab.put("DOC", docids);
            }else{
                List<String> docids=new ArrayList();
                docids.add(keyDocLabId);
                typeDocLab.put("DOC", docids);
            }
            totalDocs++ ;
        }else if(valType.equalsIgnoreCase("HL7")){
            if(typeDocLab.containsKey("HL7")){
                List<String> hl7ids=(List<String>)typeDocLab.get("HL7");
                hl7ids.add(keyDocLabId);
                typeDocLab.put("HL7", hl7ids);
            }else{
                List<String> hl7ids=new ArrayList();
                hl7ids.add(keyDocLabId);
                typeDocLab.put("HL7", hl7ids);
            }
            totalHL7++  ;
        }
    }

    Hashtable patientNumDoc=new Hashtable();
    Enumeration patientIds=patientDocs.keys();
    String patientIdStr="";
    Integer totalNumDocs=0;
    while(patientIds.hasMoreElements()){
        String key=(String)patientIds.nextElement();
        patientIdStr+=key;
        patientIdStr+=",";
        List<String> val=(List<String>)patientDocs.get(key);
        Integer numDoc=val.size();
        patientNumDoc.put(key, numDoc);
        totalNumDocs+=numDoc;
    }





    List<String> normals=ab_NormalDoc.get("normal");
    List<String> abnormals=ab_NormalDoc.get("abnormal");
    
    //set attributes
    request.setAttribute("pageNum", pageNum);
    request.setAttribute("docType",docType );
    request.setAttribute("patientDocs", patientDocs);
    request.setAttribute("providerNo", providerNo);
    request.setAttribute("searchProviderNo", searchProviderNo);
    request.setAttribute("patientIdNames",patientIdNames );
    request.setAttribute("docStatus",docStatus );
    request.setAttribute("patientIdStr", patientIdStr);
    request.setAttribute("typeDocLab",typeDocLab );
    request.setAttribute("demographicNo", demographicNo);
    request.setAttribute("ackStatus",ackStatus );
    request.setAttribute("labdocs", labdocs);
    request.setAttribute("patientNumDoc", patientNumDoc);
    request.setAttribute("totalDocs",totalDocs );
    request.setAttribute("totalHL7", totalHL7);
    request.setAttribute("normals", normals);
    request.setAttribute("abnormals", abnormals);
    request.setAttribute("totalNumDocs",totalNumDocs );
    request.setAttribute("patientIdNamesStr",patientIdNamesStr);

        return mapping.findForward("dms_index");
    }

    public ActionForward addNewQueue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
       boolean success=false;
       try{
           String qn=request.getParameter("newQueueName");
           qn=qn.trim();
           if(qn!=null && qn.length()>0){
               QueueDao queueDao=(QueueDao)SpringUtils.getBean("queueDao");
                success=queueDao.addNewQueue(qn);
                addQueueSecObjectName(qn,queueDao.getLastId());
           }
       }catch(Exception e){
           MiscUtils.getLogger().error("Error", e);
       }

        HashMap hm = new HashMap();
        hm.put("addNewQueue", success);
        JSONObject jsonObject = JSONObject.fromObject(hm);
        try{
            response.getOutputStream().write(jsonObject.toString().getBytes());
        }catch(java.io.IOException ioe){
            MiscUtils.getLogger().error("Error", ioe);
        }
        return null;
    }

    public ActionForward isLabLinkedToDemographic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
        boolean success=false;
        String demoId=null;
       try{
           String qn=request.getParameter("labid");
           if(qn!=null) {
               qn=qn.trim();
               if(qn.length()>0){
                   CommonLabResultData c=new CommonLabResultData();
                   demoId= c.getDemographicNo(qn,"HL7");
                   if(demoId!=null && !demoId.equals("0"))
                       success=true;
               }
           }
       }catch(Exception e){
           MiscUtils.getLogger().error("Error", e);
       }

        HashMap hm = new HashMap();
        hm.put("isLinkedToDemographic", success);
        hm.put("demoId",demoId);
        JSONObject jsonObject = JSONObject.fromObject(hm);
        try{
            response.getOutputStream().write(jsonObject.toString().getBytes());
        }catch(java.io.IOException ioe){
            MiscUtils.getLogger().error("Error", ioe);
        }
        return null;
    }


    public ActionForward updateDocStatusInQueue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
            String docid=request.getParameter("docid");
            if(docid!=null){
                if(!queueDocumentLinkDAO.setStatusInactive(Integer.parseInt(docid))){
                    MiscUtils.getLogger().error("failed to set status in queue document link to be inactive");
                }
            }
            return null;
    }

      //return a hastable containing queue id to queue name, a hashtable of queue id and a list of document nos.
    //forward to documentInQueus.jsp
    public ActionForward getDocumentsInQueues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
        HttpSession session=request.getSession();
        try{if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");}
        catch(Exception e){MiscUtils.getLogger().error("Error", e);}
        String providerNo =  (String) session.getAttribute("user");
        String searchProviderNo = request.getParameter("searchProviderNo");
        String ackStatus = request.getParameter("status");


    if ( ackStatus == null ) { ackStatus = "N"; } // default to new labs only
    if ( providerNo == null ) { providerNo = ""; }
    if ( searchProviderNo == null ) { searchProviderNo = providerNo; }

        String patientIdNamesStr="";
        List<QueueDocumentLink> qs=queueDocumentLinkDAO.getActiveQueueDocLink();
        HashMap<Integer,List<Integer>> queueDocNos=new HashMap<Integer,List<Integer>>();
        HashMap<Integer,String> docType=new HashMap<Integer,String>();
        HashMap<Integer,List<Integer>> patientDocs=new HashMap<Integer,List<Integer>>();
        DocumentDAO documentDAO=(DocumentDAO) SpringUtils.getBean("documentDAO");
                Demographic demo =new Demographic();
                List<Integer> docsWithPatient=new ArrayList<Integer>();
                HashMap<Integer,String> patientIdNames=new HashMap<Integer,String>();//lbData.patientName = demo.getLastName()+ ", "+demo.getFirstName();
                List<Integer> patientIds=new ArrayList<Integer>();
                Integer demoNo;
                HashMap<Integer,String> docStatus=new HashMap<Integer,String>();
                String patientIdStr="";
                StringBuilder patientIdBud=new StringBuilder();
                HashMap<String,List<Integer>> typeDocLab=new HashMap<String,List<Integer>>();
                List<Integer> ListDocIds=new ArrayList<Integer>();
        for(QueueDocumentLink q:qs){
             int qid=q.getQueueId();
             int docid=q.getDocId();
             ListDocIds.add(docid);
             docType.put(docid,"DOC");
             demo=documentDAO.getDemoFromDocNo(Integer.toString(docid));
             if(demo==null)
                 demoNo=-1;
             else
                demoNo=demo.getDemographicNo();
             if(!patientIds.contains(demoNo))
                 patientIds.add(demoNo);
             if(!patientIdNames.containsKey(demoNo)){
                 if(demoNo==-1){
                     patientIdNames.put(demoNo,"Not, Assigned");
                     patientIdNamesStr+=";"+demoNo+"="+"Not, Assigned";
                 }
                 else{
                    patientIdNames.put(demoNo, demo.getLastName()+", "+demo.getFirstName());
                    patientIdNamesStr+=";"+demoNo+"="+demo.getLastName()+", "+demo.getFirstName();
                 }


            }
             List<ProviderInboxItem> providers=providerInboxRoutingDAO.getProvidersWithRoutingForDocument("DOC",Integer.toString(docid) );
             if(providers.size()>0){
                 ProviderInboxItem pii=(ProviderInboxItem)providers.get(0);
                 docStatus.put(docid, pii.getStatus());
             }
             if(patientDocs.containsKey(demoNo)){
                 docsWithPatient=patientDocs.get(demoNo);
                 docsWithPatient.add(docid);
                 patientDocs.put(demoNo,docsWithPatient);
             }else{
                 docsWithPatient=new ArrayList<Integer>();
                 docsWithPatient.add(docid);
                 patientDocs.put(demoNo, docsWithPatient);
             }
             if(queueDocNos.containsKey(qid)){
                 
                 List<Integer> ds= (List<Integer>)queueDocNos.get(qid);
                 ds.add(docid);
                 queueDocNos.put(qid, ds);

             }else{
                 List<Integer> ds=new ArrayList<Integer>();
                 ds.add(docid);
                 queueDocNos.put(qid, ds);
             }             
        }
        Integer dn=0;
        for(int i=0;i<patientIds.size();i++){
            dn=patientIds.get(i);
            patientIdBud.append(dn);
            if(i!=patientIds.size()-1)
                patientIdBud.append(",");
        }
        patientIdStr=patientIdBud.toString();
        typeDocLab.put("DOC", ListDocIds);
        List<Integer> normals=ListDocIds;//assume all documents are normal
        List<Integer> abnormals=new ArrayList<Integer>();
        request.setAttribute("typeDocLab", typeDocLab);
        request.setAttribute("docStatus", docStatus);
        request.setAttribute("patientDocs",patientDocs );
        request.setAttribute("patientIdNames",patientIdNames );
        request.setAttribute("docType", docType);
        request.setAttribute("patientIds", patientIds);
        request.setAttribute("patientIdStr",patientIdStr );
        request.setAttribute("normals", normals);
        request.setAttribute("abnormals", abnormals);
        request.setAttribute("queueDocNos", queueDocNos);
        request.setAttribute("patientIdNamesStr",patientIdNamesStr);
        request.setAttribute("queueIdNames", queueDAO.getHashMapOfQueues());
        request.setAttribute("providerNo", providerNo);
        request.setAttribute("searchProviderNo", searchProviderNo);
        return mapping.findForward("document_in_queues");
    
        
    }
}
