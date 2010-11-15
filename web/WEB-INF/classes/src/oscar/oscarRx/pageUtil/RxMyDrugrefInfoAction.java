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
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.apache.xmlrpc.XmlRpcClientLite;
import org.oscarehr.common.dao.UserDSMessagePrefsDAO;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserDSMessagePrefs;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.OscarProperties;
import oscar.oscarRx.util.MyDrugrefComparator;
import oscar.oscarRx.util.RxUtil;
import oscar.oscarRx.util.TimingOutCallback;
import oscar.oscarRx.util.TimingOutCallback.TimeoutException;

public final class RxMyDrugrefInfoAction extends DispatchAction {

    private static final Logger log2 = MiscUtils.getLogger();
    //return interactions about current pending prescriptions
    public ActionForward findInteractingDrugList (ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {
        MiscUtils.getLogger().debug("in findInteractingDrugList");
        oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
         if (bean == null) {
                response.sendRedirect("error.html");
                return null;
            }
      try{
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
        UserPropertyDAO  propDAO =  (UserPropertyDAO) ctx.getBean("UserPropertyDAO");
        String provider = (String) request.getSession().getAttribute("user");
        String retStr=RxUtil.findInterDrugStr(propDAO,provider,bean);

        bean.setInteractingDrugList(retStr);
          /*  int pp=23;
            if(pp==23)
                throw new Exception();*/
     }catch(Exception e){
        MiscUtils.getLogger().error("Error", e);
        ResourceBundle prop = ResourceBundle.getBundle("oscarResources");
        String failedMsg=prop.getString("oscarRx.MyDrugref.InteractingDrugs.error.msgFailed");
        bean.setInteractingDrugList(failedMsg);
     }
        return null;
    }

    public ActionForward view(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {
        MiscUtils.getLogger().debug("in view RxMyDrugrefInfoAction");
        try{

        long start = System.currentTimeMillis();
        String target=(String)request.getParameter("target");
        if(target==null) MiscUtils.getLogger().debug("target is null");
        else if(target.equals("interactionsRx")) MiscUtils.getLogger().debug("target is interactionsRx");
        String provider = (String) request.getSession().getAttribute("user");

        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
        UserPropertyDAO  propDAO =  (UserPropertyDAO) ctx.getBean("UserPropertyDAO");
        UserDSMessagePrefsDAO  dsmessageDAO =  (UserDSMessagePrefsDAO) ctx.getBean("UserDSMessagePrefsDAO");
        MiscUtils.getLogger().debug("hideResources is before "+request.getSession().getAttribute("hideResources"));
        Hashtable dsPrefs=new Hashtable();
        if (request.getSession().getAttribute("hideResources") == null){

            dsPrefs = dsmessageDAO.getHashofMessages(provider,UserDSMessagePrefs.MYDRUGREF);

        }
        UserProperty prop = propDAO.getProp(provider, UserProperty.MYDRUGREF_ID);
        String myDrugrefId = null;
        if (prop != null){
            myDrugrefId = prop.getValue();

        }

        RxSessionBean bean = (RxSessionBean) request.getSession().getAttribute("RxSessionBean");
        if ( bean == null ){
            return mapping.findForward("success");
        }
        Vector codes = bean.getAtcCodes();
        //String[] str = new String[]{"warnings_byATC","bulletins_byATC","interactions_byATC"};
        String[] str = new String[]{"warnings_byATC,bulletins_byATC,interactions_byATC,get_guidelines"};   //NEW more efficent way of sending multiple requests at the same time.
        MessageResources mr=getResources(request);
        Locale locale = getLocale(request);

        Vector all = new Vector();
        for (String command : str){
            try{
                Vector v = getMyDrugrefInfo(command,  codes,myDrugrefId) ;

                if (v !=null && v.size() > 0){
                    all.addAll(v);
                }

            }catch(Exception e){
                log2.debug("command :"+command+" "+e.getMessage());
                MiscUtils.getLogger().error("Error", e);
            }
        }
        Collections.sort(all, new MyDrugrefComparator());
        MiscUtils.getLogger().debug(all);
        //loop through all to add interaction to each warning
        try{
            for(int i=0;i<all.size();i++){
                Hashtable ht=(Hashtable)all.get(i);
                MiscUtils.getLogger().debug("**ht="+ht);
                String effect=(String)ht.get("effect");
                MiscUtils.getLogger().debug("**effect="+effect);
                String interactStr="";
               if(effect!=null){
                    if(effect.equals("a"))
                        effect=mr.getMessage(locale, "oscarRx.interactions.msgAugmentsNoClinical");
                    else if(effect.equals("A"))
                        effect=mr.getMessage(locale, "oscarRx.interactions.msgAugments");
                    else if(effect.equals("i"))
                        effect=mr.getMessage(locale, "oscarRx.interactions.msgInhibitsNoClinical");
                    else if(effect.equals("I"))
                        effect=mr.getMessage(locale, "oscarRx.interactions.msgInhibits");
                    else if(effect.equals("n"))
                        effect=mr.getMessage(locale, "oscarRx.interactions.msgNoEffect");
                    else if(effect.equals("N"))
                        effect=mr.getMessage(locale, "oscarRx.interactions.msgNoEffect");
                    else if(effect.equals(" "))
                        effect=mr.getMessage(locale, "oscarRx.interactions.msgUnknownEffect");
                    interactStr=ht.get("name")+" "+effect+" "+ht.get("drug2");
               }
                ht.put("interactStr", interactStr);
                MiscUtils.getLogger().debug("ineractStr="+interactStr);
            }
        }catch(NullPointerException npe){
            MiscUtils.getLogger().error("Error", npe);
        }
        //Vector idWarningVec=new Vector();
        Vector<Hashtable> allRetVec=new Vector();
        Vector<String> currentIdWarnings=new Vector();
        for(int i=0;i<all.size();i++){
            Hashtable ht=(Hashtable)all.get(i);
            Date dt=(Date)ht.get("updated_at");
            Long time=dt.getTime();
            String idWarning=ht.get("id")+"."+time;
            if(!currentIdWarnings.contains(idWarning)){
                currentIdWarnings.add(idWarning);
                allRetVec.add(ht);
                //idWarningVec.add(idWarning);
            }
        }
        MiscUtils.getLogger().debug("currentIdWarnings is  "+currentIdWarnings);
        //set session attribute hiddenResources if it was null
        if(dsPrefs!=null && dsPrefs.size()>0){
            Hashtable hiddenR=new Hashtable();
            Enumeration em=dsPrefs.keys();
            while(em.hasMoreElements()){
                String resId=(String)em.nextElement();
                resId=resId.replace(UserDSMessagePrefs.MYDRUGREF, "");
                for(String warning:currentIdWarnings){
                    if(warning.contains(resId)){
                        String[] arr=warning.split("\\.");
                        hiddenR.put(UserDSMessagePrefs.MYDRUGREF+resId, arr[1]);
                    }
                }
            }
            request.getSession().setAttribute("hideResources", hiddenR);
        }
        //if hideResources are not in warnings, remove them from hiddenResource and set them to archived=0 in database;
        Hashtable hiddenResAttribute=(Hashtable)request.getSession().getAttribute("hideResources");
        if(hiddenResAttribute==null){
            Hashtable emptyHiddenRes=new Hashtable();
            request.getSession().setAttribute("hideResources", emptyHiddenRes);
        }else{
        Enumeration hiddenResKeys=hiddenResAttribute.keys();
        while(hiddenResKeys.hasMoreElements()){
            String key=(String)hiddenResKeys.nextElement();
            String value=(String)hiddenResAttribute.get(key);
            Date updatedatId=new Date();
            updatedatId.setTime(Long.parseLong(value));
            String resId=key.replace(UserDSMessagePrefs.MYDRUGREF, "");
            String id=resId+"."+value;
            if(!currentIdWarnings.contains(id)){
                hiddenResAttribute.remove(key);
                //update database
                setShowDSMessage(dsmessageDAO, provider, resId, updatedatId);
            }
        }
        request.getSession().setAttribute("hideResources", hiddenResAttribute);
        }
        request.setAttribute("warnings",allRetVec);
        log2.debug("MyDrugref return time " + (System.currentTimeMillis() - start) );
        if(target!=null && target.equals("interactionsRx")) return mapping.findForward("updateInteractions");
        else return mapping.findForward("success");
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
            return mapping.findForward("failure");
    }
    }

    private void setShowDSMessage(UserDSMessagePrefsDAO  dsmessageDAO,String provider,String resId,Date updatedatId){
                UserDSMessagePrefs pref = dsmessageDAO.getDsMessage(provider,UserDSMessagePrefs.MYDRUGREF , resId,true);
                pref.setId(pref.getId());
                pref.setProviderNo(provider);
                pref.setRecordCreated(new Date());
                pref.setResourceId(resId);
                pref.setResourceType(UserDSMessagePrefs.MYDRUGREF);
                pref.setResourceUpdatedDate(updatedatId);
                pref.setArchived(Boolean.FALSE);
                dsmessageDAO.updateProp(pref);
    }

    public ActionForward setWarningToHide(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {

        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
        UserDSMessagePrefsDAO  dsmessageDAO =  (UserDSMessagePrefsDAO) ctx.getBean("UserDSMessagePrefsDAO");


        String provider = (String) request.getSession().getAttribute("user");
        String postId = request.getParameter("resId");
        String date = request.getParameter("updatedat");
        

        long datel = Long.parseLong(date);
        Date updatedatId = new Date();
        updatedatId.setTime(datel);

        log2.debug("post Id "+postId+"  date "+date);


        if (request.getSession().getAttribute("hideResources") == null){

            Hashtable dsPrefs = dsmessageDAO.getHashofMessages(provider,UserDSMessagePrefs.MYDRUGREF);
            request.getSession().setAttribute("hideResources",dsPrefs);//this doesn't save values that can be used directly
        }
        Hashtable h = (Hashtable) request.getSession().getAttribute("hideResources");

        h.put("mydrugref"+postId,date);

        UserDSMessagePrefs pref = new UserDSMessagePrefs();

        pref.setProviderNo(provider);
        pref.setRecordCreated(new Date());
        pref.setResourceId(postId);
        pref.setResourceType(UserDSMessagePrefs.MYDRUGREF);
        pref.setResourceUpdatedDate(updatedatId);
        pref.setArchived(Boolean.TRUE);
        request.getSession().setAttribute("hideResources", h);

        dsmessageDAO.saveProp(pref);

       return mapping.findForward("updateResources");
    }

    public ActionForward setWarningToShow(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {

        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
        UserDSMessagePrefsDAO  dsmessageDAO =  (UserDSMessagePrefsDAO) ctx.getBean("UserDSMessagePrefsDAO");

        String provider = (String) request.getSession().getAttribute("user");
        String resId = request.getParameter("resId");
        String date = request.getParameter("updatedat");
      

        long datel = Long.parseLong(date);
        Date updatedatId = new Date();
        updatedatId.setTime(datel);

        log2.debug("post Id "+resId+"  date "+date);

        if (request.getSession().getAttribute("hideResources") == null){
            Hashtable dsPrefs = dsmessageDAO.getHashofMessages(provider,UserDSMessagePrefs.MYDRUGREF);
            request.getSession().setAttribute("hideResources",dsPrefs);//this doesn't save values that can be used directly
        }
        Hashtable h = (Hashtable) request.getSession().getAttribute("hideResources");
        h.remove("mydrugref"+resId);
        MiscUtils.getLogger().debug("provider,UserDSMessagePrefs.MYDRUGREF , postId, updatedatId :"+provider+"--"+UserDSMessagePrefs.MYDRUGREF +"--"+ resId+"--"+ updatedatId);
        setShowDSMessage(dsmessageDAO, provider, resId, updatedatId);
        request.getSession().setAttribute("hideResources", h);       

       return mapping.findForward("updateResources");
    }


    public static void removeNullFromVector(Vector v){
        while(v != null && v.contains(null)){
            v.remove(null);
        }
    }


    public Vector getMyDrugrefInfo(String command, Vector drugs,String myDrugrefId) throws Exception {

        removeNullFromVector(drugs);
        Vector params = new Vector();

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

            vec = (Vector) obj;

        }else if(obj instanceof Hashtable){

            Object holbrook = ((Hashtable) obj).get("Holbrook Drug Interactions");
            if (holbrook instanceof Vector){

                vec = (Vector) holbrook;

            }
            Enumeration e = ((Hashtable) obj).keys();
            while (e.hasMoreElements()){
                String s = (String) e.nextElement();

                log2.debug(s+" "+((Hashtable) obj).get(s)+" "+((Hashtable) obj).get(s).getClass().getName());
            }
        }
        return vec;
    }


    public Object callWebserviceLite(String procedureName, Vector params) throws Exception{
        log2.debug("#CALLmyDRUGREF-"+procedureName);
        Object object = null;

        String server_url = OscarProperties.getInstance().getProperty("MY_DRUGREF_URL","http://mydrugref.org/backend/api");

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