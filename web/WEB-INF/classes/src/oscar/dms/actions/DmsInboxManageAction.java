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

import com.quatro.dao.security.SecObjectNameDao;
import com.quatro.model.security.Secobjectname;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
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
import org.oscarehr.PMmodule.utility.UtilDateUtilities;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;
import org.oscarehr.common.dao.QueueDocumentLinkDao;
import org.oscarehr.common.dao.QueueProviderLinkDao;
import org.oscarehr.common.model.ProviderInboxItem;
import org.oscarehr.common.model.QueueDocumentLink;
import org.oscarehr.document.DocumentResultsData;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.dms.data.QueueData;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.oscarProvider.data.ProviderData;
import oscar.util.OscarRoleObjectPrivilege;

/**
 *
 * @author jackson
 */
public class DmsInboxManageAction extends DispatchAction {
    private ProviderInboxRoutingDao providerInboxRoutingDAO=null;
    private QueueDocumentLinkDao queueDocumentLinkDAO=null;
    private QueueProviderLinkDao queueProviderLinkDAO=null;
    private SecObjectNameDao secObjectNameDao=null;
    public void setProviderInboxRoutingDAO(ProviderInboxRoutingDao providerInboxRoutingDAO){
        this.providerInboxRoutingDAO = providerInboxRoutingDAO;
    }
    public void setQueueDocumentLinkDAO(QueueDocumentLinkDao queueDocumentLinkDAO){
        this.queueDocumentLinkDAO=queueDocumentLinkDAO;
    }
    public void setQueueProviderLinkDAO(QueueProviderLinkDao queueProviderLinkDAO){
        this.queueProviderLinkDAO=queueProviderLinkDAO;
    }
    public void setSecObjectNameDao(SecObjectNameDao secObjectNameDao){
        this.secObjectNameDao=secObjectNameDao;
    }
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
       try{
           String module,moduleid, view, sort, viewstatus;
        module = "demographic";
        moduleid = "-1";
        view = "all";
        if (request.getParameter("view") != null) {
            view = (String) request.getParameter("view");
        } else if (request.getAttribute("view") != null) {
            view = (String) request.getAttribute("view");
        }
        sort =EDocUtil.SORT_CREATOR+",  "+ EDocUtil.SORT_OBSERVATIONDATE;
        String sortRequest = request.getParameter("sort");
        if (sortRequest != null) {
            if (sortRequest.equals("description")) {
                sort = EDocUtil.SORT_DESCRIPTION;
            } else if (sortRequest.equals("type")) {
                sort = EDocUtil.SORT_DOCTYPE;
            } else if (sortRequest.equals("contenttype")) {
                sort = EDocUtil.SORT_CONTENTTYPE;
            } else if (sortRequest.equals("creator")) {
                sort = EDocUtil.SORT_CREATOR;
            } else if (sortRequest.equals("uploaddate")) {
                sort = EDocUtil.SORT_DATE;
            } else if (sortRequest.equals("observationdate")) {
                sort = EDocUtil.SORT_OBSERVATIONDATE;
            }
        }
        viewstatus = "active";
        ArrayList<EDoc> privatedocs = new ArrayList();
        privatedocs = EDocUtil.listDocs(module, moduleid, view, EDocUtil.PRIVATE, sort, viewstatus);//TODO: implement public docs later if needed.
        
        setProviderDocsInSession(privatedocs,request);
        setQueueDocsInSession(privatedocs,request);
        
       }catch(Exception e){
           e.printStackTrace();
       }
        return mapping.findForward("success");
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
            //System.out.println("docId="+docId);
            List<ProviderInboxItem> routeList=providerInboxRoutingDAO.getProvidersWithRoutingForDocument(LabResultData.DOCUMENT, docId);
            for(ProviderInboxItem pii:routeList){
                String routingPId=pii.getProviderNo();
                //System.out.println("routingPId="+routingPId);
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
            //System.out.println("key="+key);
            List<EDoc> EDocs=new ArrayList();
            EDocs=(List<EDoc>)providerDocs.get(key);
            if(EDocs==null || EDocs.size()==0){
                providerDocs.remove(key);
                //System.out.println("removed key="+key);
            }
        }

        request.getSession().setAttribute("providerDocs", providerDocs);
    }


    private void setQueueDocsInSession(ArrayList<EDoc> privatedocs,HttpServletRequest request){
         //docs according to queue name
        Hashtable queueDocs=new Hashtable();
        List<Hashtable> queues=QueueData.getQueues();
        for(int i=0;i<queues.size();i++){
            Hashtable ht=(Hashtable)queues.get(i);
            List<EDoc> EDocs=new ArrayList();
            String queueId=(String)ht.get("id");
            queueDocs.put(queueId, EDocs);
        }
        System.out.println("queueDocs="+queueDocs);
        for(int i=0;i<privatedocs.size();i++){
            EDoc eDoc=privatedocs.get(i);
            List queueList=new ArrayList();
            String docIdStr=eDoc.getDocId();
            Integer docId=-1;
            if(docIdStr!=null && !docIdStr.equalsIgnoreCase("")){
                docId=Integer.parseInt(docIdStr);
            }
            List<QueueDocumentLink> queueDocLinkList=queueDocumentLinkDAO.getQueueFromDocument(docId);
            //System.out.println("-------------1----------------------");
            for(QueueDocumentLink qdl:queueDocLinkList){
                Integer qidInt=qdl.getQueueId();
                String qidStr=qidInt.toString();
                System.out.println("qid in link="+qidStr);
                if(queueDocs.containsKey(qidStr)){
                    List<EDoc> EDocs=new ArrayList();
                    EDocs=(List<EDoc>)queueDocs.get(qidStr);
                    EDocs.add(eDoc);
                    System.out.println("add edoc id to queue id="+eDoc.getDocId());
                    queueDocs.put(qidStr, EDocs);
                }
            }       
        }
        //System.out.println("-------------3----------------------");
            //remove queues which has no docs linked to
            Enumeration queueIds=queueDocs.keys();
            while(queueIds.hasMoreElements()){
                String queueId=(String)queueIds.nextElement();
                List<EDoc> eDocs=new ArrayList();
                eDocs=(List<EDoc>)queueDocs.get(queueId);
                if(eDocs==null || eDocs.size()==0){
                    queueDocs.remove(queueId);
                    System.out.println("removed queueId="+queueId);
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
     public ActionForward prepareForIndexPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session=request.getSession();
        try{if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");}
        catch(Exception e){e.printStackTrace();}
        String roleName = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

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

    for(int i=0;i<labdocs.size();i++){
        LabResultData data=(LabResultData)labdocs.get(i);
        //System.out.println("before*** checking privilege, lab doc id="+data.getSegmentID());
    }
    DocumentResultsData documentResult=new DocumentResultsData();
    //check privilege for documents only
    for(int i=0;i<labdocs.size();i++){
            LabResultData data=(LabResultData)labdocs.get(i);
            if(data.isDocument()){
               String docid=data.getSegmentID();

                String queueid=docQueue.get(docid);
                if(queueid!=null){ 
                    queueid=queueid.trim();
                    if(queueid.equals("1") && isSegmentIDUnique(validlabdocs,data)){
                        validlabdocs.add(data);
                    }
                    else{
                        String objectName="_queue."+queueid;
                        Vector vec=OscarRoleObjectPrivilege.getPrivilegeProp(objectName);
                        if(OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties)vec.get(0), (Vector)vec.get(1))
                                || documentResult.isSentToProvider(docid, providerNo)){
                            //labs is in provider's queue,do nothing                
                           if(isSegmentIDUnique(validlabdocs,data))
                           {validlabdocs.add(data);}
                        }else{
                            //lab should be removed because it's not in provider's queue                
                        }        
                   }
                }
            }else{
                if(isSegmentIDUnique(validlabdocs,data))
                           {validlabdocs.add(data);}
            }
    }
    labdocs=validlabdocs;
    for(int i=0;i<labdocs.size();i++){
        LabResultData data=(LabResultData)labdocs.get(i);
        //System.out.println("after checking privilege, lab doc id="+data.getSegmentID());
    }
    Collections.sort(labdocs);
  
    HashMap labMap = new HashMap();
    LinkedHashMap accessionMap = new LinkedHashMap();
    LabResultData result;
    //System.out.println("aftasdfasdfing labdocs.size()="+labdocs.size());
    for( int i = 0; i < labdocs.size(); i++ ) {
        result = (LabResultData) labdocs.get(i);
        labMap.put(result.segmentID, result);
        ArrayList labNums = new ArrayList();
        //System.out.println("result.segmentID="+result.segmentID);
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
    Iterator iter=accessionMap.entrySet().iterator();
    //System.out.println("-----------start print accessionmap");
    //while(iter.hasNext()){
    //    System.out.println(iter.next());
    //}
    //System.out.println("-----------end print accessionmap");
    labdocs.clear();
    //System.out.println("bbblabdocs.size()="+labdocs.size());
    for (int i=0; i < labArrays.size(); i++){
        ArrayList labNums = (ArrayList) labArrays.get(i);
        // must sort through in reverse to keep the labs in the correct order
        for (int j=labNums.size()-1; j >= 0; j--){
            labdocs.add(labMap.get(labNums.get(j)));
        }
    }
    //System.out.println("ccc checking labdocs.size()="+labdocs.size());
    Collections.sort(labdocs);
    //System.out.println("ddd checking labdocs.size()="+labdocs.size());
    int pageNum = 1;
    if ( request.getParameter("pageNum") != null ) {
        pageNum = Integer.parseInt(request.getParameter("pageNum"));
    }
    /* find all data for the index.jsp page*/
  Hashtable patientDocs=new Hashtable();
    Hashtable patientIdNames=new Hashtable();
    Hashtable docStatus=new Hashtable();
    Hashtable docType=new Hashtable();
    Hashtable<String,List<String>> ab_NormalDoc=new Hashtable();
    //System.out.println("after checking labdocs.size()="+labdocs.size());
    for(int i=0;i<labdocs.size();i++){
        LabResultData data=(LabResultData)labdocs.get(i);
        List<String> segIDs=new ArrayList();
        String labPatientId=data.getLabPatientId();
        if(labPatientId==null || labPatientId.equals("-1"))
            labPatientId="-1";
        //System.out.println(labPatientId+"--"+data.patientName);
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
            //System.out.println(labPatientId+"--"+patientDocs);
            segIDs=(List)patientDocs.get(labPatientId);
            segIDs.add(data.getSegmentID());
            patientDocs.put(labPatientId,segIDs);
        }else{
            segIDs.add(data.getSegmentID());
            patientDocs.put(labPatientId, segIDs);
            patientIdNames.put(labPatientId, data.patientName);
        }
        docStatus.put(data.getSegmentID(), data.getAcknowledgedStatus());
        docType.put(data.getSegmentID(), data.labType);
    }
    //System.out.println("docType="+docType);
    Integer totalDocs=0;
    Integer totalHL7=0;
    Hashtable<String,List<String>> typeDocLab=new Hashtable();
    Enumeration keys=docType.keys();
    while(keys.hasMoreElements()){
        String keyDocLabId=((String)keys.nextElement());
        String valType=(String)docType.get(keyDocLabId);
        //System.out.println("valType val="+valType);
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
    
    //System.out.println("patientDocs="+patientDocs);
    //System.out.println("patientNumDoc="+patientNumDoc);
    //System.out.println("docStatus="+docStatus);
    //System.out.println("typeDocLab="+typeDocLab);
    //System.out.println("ab_NormalDoc="+ab_NormalDoc);
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
    //System.out.println("labs.size="+labdocs.size());
    //System.out.println("forward to dms_index");

        return mapping.findForward("dms_index");
    }

    public ActionForward addNewQueue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
       boolean success=false;
       try{
           String qn=request.getParameter("newQueueName");
           qn=qn.trim();
           if(qn!=null && qn.length()>0){
                success=QueueData.addNewQueue(qn);
                addQueueSecObjectName(qn,QueueData.getLastId());
           }
       }catch(Exception e){
           e.printStackTrace();
       }

        HashMap hm = new HashMap();
        hm.put("addNewQueue", success);
        JSONObject jsonObject = JSONObject.fromObject(hm);
        try{
            response.getOutputStream().write(jsonObject.toString().getBytes());
        }catch(java.io.IOException ioe){
            ioe.printStackTrace();
        }
        return null;
    }

    public ActionForward addQueueProviderLink(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String providerS=request.getParameter("providers");
        String queueS=request.getParameter("queues");
        System.out.println(providerS+" ; "+queueS);
        String [] providerAr=providerS.split("\\+");
        String [] queueAr=queueS.split("\\+");
        for(String sprovider:providerAr){
            for(String squeue:queueAr){
                if(sprovider!=null && squeue!=null && sprovider.trim().length()>0 &&squeue.trim().length()>0)
                    queueProviderLinkDAO.addQueueProviderLink(Integer.parseInt(squeue.trim()), sprovider.trim());
            }
        }
        return null;
    }
}