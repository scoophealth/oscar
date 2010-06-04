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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;
import org.oscarehr.common.dao.QueueDocumentLinkDao;
import org.oscarehr.common.dao.QueueProviderLinkDao;
import org.oscarehr.common.model.ProviderInboxItem;
import org.oscarehr.common.model.QueueDocumentLink;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.dms.data.QueueData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.oscarProvider.data.ProviderData;

/**
 *
 * @author jackson
 */
public class DmsInboxManageAction extends DispatchAction {
    private ProviderInboxRoutingDao providerInboxRoutingDAO=null;
    private QueueDocumentLinkDao queueDocumentLinkDAO=null;
    private QueueProviderLinkDao queueProviderLinkDAO=null;
    public void setProviderInboxRoutingDAO(ProviderInboxRoutingDao providerInboxRoutingDAO){
        this.providerInboxRoutingDAO = providerInboxRoutingDAO;
    }
    public void setQueueDocumentLinkDAO(QueueDocumentLinkDao queueDocumentLinkDAO){
        this.queueDocumentLinkDAO=queueDocumentLinkDAO;
    }
    public void setQueueProviderLinkDAO(QueueProviderLinkDao queueProviderLinkDAO){
        this.queueProviderLinkDAO=queueProviderLinkDAO;
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
            //System.out.println("-------------2----------------------");

            
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

    public ActionForward addNewQueue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
       boolean success=false;
       try{
           String qn=request.getParameter("newQueueName");
           qn=qn.trim();
           if(qn!=null && qn.length()>0)
                success=QueueData.addNewQueue(qn);
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