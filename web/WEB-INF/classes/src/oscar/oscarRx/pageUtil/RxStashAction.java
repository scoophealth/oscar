/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.oscarRx.pageUtil;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import oscar.OscarProperties;
import oscar.oscarRx.util.TimingOutCallback.TimeoutException;
import org.apache.xmlrpc.XmlRpcClientLite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Hashtable;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.util.TimingOutCallback;

public final class RxStashAction extends DispatchAction {

//public final class RxStashAction extends Action {
    public ActionForward unspecified(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
            //    System.out.println("===========start in rxstatshaction.java===========");
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);

        // Setup variables

        RxStashForm frm = (RxStashForm)form;
        RxSessionBean bean = (RxSessionBean)request.getSession().getAttribute("RxSessionBean");
       // bean.setStashIndex(11);
        if(bean==null) {
            response.sendRedirect("error.html");
            return null;
        }

        if(frm.getStashId()>=0 && frm.getStashId() < bean.getStashSize()) {
            if(frm.getAction().equals("edit")) {

                request.setAttribute("BoxNoFillFirstLoad", "true");

                bean.setStashIndex(frm.getStashId());
                //bean.setStashIndex(11);
            }
            if(frm.getAction().equals("delete")) {
                bean.removeStashItem(frm.getStashId());

                if(bean.getStashIndex() >= bean.getStashSize()) {
                    bean.setStashIndex(bean.getStashSize() - 1);
                }
            }
        }
   /*     System.out.println("bean.getStashIndex()="+bean.getStashIndex());
         System.out.println("bean.getStashSize()="+bean.getStashSize());
        System.out.println("===========end in rxstatshaction.java===========");
     */   return mapping.findForward("success");
    }

    public ActionForward setStashIndex(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
       //         System.out.println("===========start in setStashIndex rxstatshaction.java===========");
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);
        String wp=null;
        try{
        wp=request.getParameter("randomId");

        int randomId;


        if(wp!=null && !wp.equals("null")){
         //   System.out.println("in if wp="+wp);
            randomId=Integer.parseInt(wp);
         //   System.out.println("in setStashIndex randomId="+""+randomId);
        }else{
         //   System.out.println("in else wp="+wp);
            randomId=-1;
        }


        // Setup variables
        RxSessionBean bean = (RxSessionBean)request.getSession().getAttribute("RxSessionBean");

        if(bean==null) {
            response.sendRedirect("error.html");
            return null;
        }
      //  System.out.println("bean.getStashSize()="+bean.getStashSize());
      //  System.out.println("bean.getStashIndex() before setting="+bean.getStashIndex());
        //find the stashIndex corresponding to the random number
        int stashId=bean.getIndexFromRx(randomId);
        if(stashId >=0 && stashId  < bean.getStashSize()) {
            bean.setStashIndex(stashId);
        }
     //   System.out.println("set the stash index to="+bean.getStashIndex());
     //    System.out.println("the stash size becomes="+bean.getStashSize());
        }catch(Exception e){e.printStackTrace();}
      //  System.out.println("===========end in setStashIndex rxstatshaction.java===========");
        return mapping.findForward("success");
    }

    public ActionForward deletePrescribe(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
                System.out.println("===========start in deletePrescribe ===========");
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);

        RxSessionBean bean = (RxSessionBean)request.getSession().getAttribute("RxSessionBean");

        if(bean==null) {
            response.sendRedirect("error.html");
            return null;
        }

        int randomId=Integer.parseInt(request.getParameter("randomId"));
       System.out.println("randomId="+randomId);
                bean.removeStashItem(bean.getIndexFromRx(randomId));
                if(bean.getStashIndex() >= bean.getStashSize()) {
                    bean.setStashIndex(bean.getStashSize() - 1);
                }



            //quiry mydrugref database to get a vector with all interacting drugs
            //if effect is not null or effect is not empty string
                //get a list of all pending prescriptions' ATC codes
                //compare if anyone match,
                        //if yes, get it's randomId and set an session attribute
                        //if not, do nothing
            WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
            UserPropertyDAO  propDAO =  (UserPropertyDAO) ctx.getBean("UserPropertyDAO");
            String provider = (String) request.getSession().getAttribute("user");
            UserProperty prop = propDAO.getProp(provider, UserProperty.MYDRUGREF_ID);
            String myDrugrefId = null;
            if (prop != null){
                myDrugrefId = prop.getValue();
                System.out.println("3myDrugrefId"+myDrugrefId);
            }
            RxPrescriptionData.Prescription[] rxs=bean.getStash();
            //acd contains all atccodes in stash
            Vector acd=new Vector();
            for(RxPrescriptionData.Prescription rxItem:rxs){
                acd.add(rxItem.getAtcCode());
            }
            System.out.println("3acd="+acd);

            String[] str = new String[]{"warnings_byATC,bulletins_byATC,interactions_byATC,get_guidelines"};   //NEW more efficent way of sending multiple requests at the same time.
            Vector allInteractions = new Vector();
            for (String command : str){
                try{
                    Vector v = getMyDrugrefInfo(command,  acd,myDrugrefId) ;
                    System.out.println("2v in for loop: "+v);
                    if (v !=null && v.size() > 0){
                        allInteractions.addAll(v);
                    }
                    System.out.println("3after all.addAll(v): "+allInteractions);
                }catch(Exception e){
                    log2.debug("3command :"+command+" "+e.getMessage());
                    e.printStackTrace();
                }
            }


            Vector allInteractingPairs=new Vector();
            for (RxPrescriptionData.Prescription rxItem:rxs){
                Vector interactedDrugs=new Vector();
                System.out.println("3rxItem="+rxItem.getDrugName());
                Vector uniqueDrugNameList=new Vector();
                for(int i=0;i<allInteractions.size();i++){
                    Hashtable hb=(Hashtable)allInteractions.get(i);
                    String interactingAtc=(String)hb.get("atc");
                    String interactingDrugName=(String)hb.get("drug2");
                    String effectStr=(String)hb.get("effect");
                    String significanceStr=(String)hb.get("significance");
                    if(rxItem.getAtcCode().equals(interactingAtc) && effectStr!=null && effectStr.length()>0 && !effectStr.equalsIgnoreCase("N")&& !effectStr.equals(" ")){
                        System.out.println("3interactingDrugName="+interactingDrugName);
                        RxPrescriptionData.Prescription rrx=findRxFromDrugNameOrGN(rxs,interactingDrugName);
                        System.out.println("3rrx.getDrugName()="+rrx.getDrugName());
                        if(rrx!=null && !uniqueDrugNameList.contains(rrx.getDrugName())) {
                            uniqueDrugNameList.add(rrx.getDrugName());
                            Vector oneDrugInfo=new Vector();
                            oneDrugInfo.add(rrx.getDrugName());
                            oneDrugInfo.add(effectStr);
                            oneDrugInfo.add(significanceStr);
                            interactedDrugs.add(oneDrugInfo);
                            System.out.println("3interactedDrugs="+interactedDrugs);
                        }
                    }
                }

                if(interactedDrugs.size()>0){
                    Hashtable interactingPairs=new Hashtable();
                    interactingPairs.put(rxItem.getRandomId(), interactedDrugs);
                    allInteractingPairs.add(interactingPairs);
                }
                System.out.println("3***next rxItem***");
            }
            System.out.println("3allInteractingPairs="+allInteractingPairs);
            bean.setInteractingDrugList(allInteractingPairs);
            //request.setAttribute("abc", allInteractingPairs);

        return mapping.findForward("success");
    }

        private RxPrescriptionData.Prescription findRxFromDrugNameOrGN(final RxPrescriptionData.Prescription[] rxs,String interactingDrugName){
        RxPrescriptionData.Prescription returnRx=null;
        for (RxPrescriptionData.Prescription rxItem:rxs){
            if(rxItem.getDrugName().contains(interactingDrugName)){
                returnRx=rxItem;
            }else if(rxItem.getGenericName().contains(interactingDrugName)){
                returnRx=rxItem;
            }
        }
        return returnRx;

    }
    private Vector getMyDrugrefInfo(String command, Vector drugs,String myDrugrefId) throws Exception {
        System.out.println("3in getMyDrugrefInfo");
        RxMyDrugrefInfoAction.removeNullFromVector(drugs);
        Vector params = new Vector();
        System.out.println("3command,drugs,myDrugrefId= "+command+"--"+drugs+"--"+myDrugrefId);
        params.addElement(command);
        params.addElement(drugs);
        if (myDrugrefId != null && !myDrugrefId.trim().equals("")){
            log2.debug("putting >"+myDrugrefId+ "< in the request");
            params.addElement(myDrugrefId);
            //params.addElement("true");
        }
        Vector vec = new Vector();
        Object obj =  callWebserviceLite("Fetch",params);
        log2.debug("RETURNED "+obj);
        if (obj instanceof Vector){
            System.out.println("3obj is instance of vector");
            vec = (Vector) obj;
            System.out.println(vec);
        }else if(obj instanceof Hashtable){
            System.out.println("3obj is instace of hashtable");
            Object holbrook = ((Hashtable) obj).get("Holbrook Drug Interactions");
            if (holbrook instanceof Vector){
                System.out.println("3holbrook is instance of vector ");
                vec = (Vector) holbrook;
                System.out.println(vec);
            }
            Enumeration e = ((Hashtable) obj).keys();
            while (e.hasMoreElements()){
                String s = (String) e.nextElement();
                System.out.println(s);
                log2.debug(s+" "+((Hashtable) obj).get(s)+" "+((Hashtable) obj).get(s).getClass().getName());
            }
        }
        return vec;
    }

    private static Log log2 = LogFactory.getLog(RxMyDrugrefInfoAction.class);
    public Object callWebserviceLite(String procedureName, Vector params) throws Exception{
        log2.debug("#CALLmyDRUGREF-"+procedureName);
        Object object = null;

        String server_url = OscarProperties.getInstance().getProperty("MY_DRUGREF_URL","http://mydrugref.org/backend/api");
        System.out.println("server_url: "+server_url);
        TimingOutCallback callback = new TimingOutCallback(10 * 1000);
        try{
            log2.debug("server_url :"+server_url);
            XmlRpcClientLite server = new XmlRpcClientLite(server_url);
            server.executeAsync(procedureName, params, callback);
            object = callback.waitForResponse();
        } catch (TimeoutException e) {
            log2.debug("No response from server."+server_url);
        }catch(Throwable ethrow){
            log2.debug("Throwing error."+ethrow.getMessage());
        }
        return object;
    }
}