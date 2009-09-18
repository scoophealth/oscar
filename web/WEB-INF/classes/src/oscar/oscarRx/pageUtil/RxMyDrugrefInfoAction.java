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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.actions.DispatchAction;
import org.apache.xmlrpc.*;
import oscar.oscarRx.util.MyDrugrefComparator;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.WebApplicationContext;
import org.oscarehr.common.dao.*;
import org.oscarehr.common.model.*;
import oscar.OscarProperties;
import oscar.oscarRx.util.TimingOutCallback;
import oscar.oscarRx.util.TimingOutCallback.TimeoutException;


public final class RxMyDrugrefInfoAction extends DispatchAction {

    private static Log log2 = LogFactory.getLog(RxMyDrugrefInfoAction.class);
    
    public ActionForward view(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {
        long start = System.currentTimeMillis();
        String provider = (String) request.getSession().getAttribute("user");
        
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
        UserPropertyDAO  propDAO =  (UserPropertyDAO) ctx.getBean("UserPropertyDAO");
        UserDSMessagePrefsDAO  dsmessageDAO =  (UserDSMessagePrefsDAO) ctx.getBean("UserDSMessagePrefsDAO");
        if (request.getSession().getAttribute("hideResources") == null){
            Hashtable dsPrefs = dsmessageDAO.getHashofMessages(provider,UserDSMessagePrefs.MYDRUGREF);
            request.getSession().setAttribute("hideResources",dsPrefs);
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
        //Vector warnings = getWarnings(codes,myDrugrefId);
        //String[] str = new String[]{"warnings_byATC","bulletins_byATC","interactions_byATC"};
        String[] str = new String[]{"warnings_byATC,bulletins_byATC,interactions_byATC,get_guidelines"};   //NEW more efficent way of sending multiple requests at the same time.
        
        Vector all = new Vector();
        for (String command : str){
            try{
                Vector v = getMyDrugrefInfo(command,  codes,myDrugrefId) ;
                if (v !=null && v.size() > 0){
                    all.addAll(v);
                }
            }catch(Exception e){
                log2.debug("command :"+command+" "+e.getMessage());
                e.printStackTrace();
            }
        }
        Collections.sort(all, new MyDrugrefComparator());
        request.setAttribute("warnings",all);
        ///////
        log2.debug("MyDrugref return time " + (System.currentTimeMillis() - start) );
        return mapping.findForward("success");
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
            request.getSession().setAttribute("hideResources",dsPrefs);
        }
        Hashtable h = (Hashtable) request.getSession().getAttribute("hideResources");
        
        h.put("mydrugref"+postId,date);
        
        UserDSMessagePrefs pref = new UserDSMessagePrefs();
        
        pref.setProviderNo(provider);
        pref.setRecordCreated(new Date());
        pref.setResourceId(postId);
        pref.setResourceType(UserDSMessagePrefs.MYDRUGREF);
        pref.setResourceUpdatedDate(updatedatId);
        
       
       
        dsmessageDAO.saveProp(pref);
        
        return null;
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