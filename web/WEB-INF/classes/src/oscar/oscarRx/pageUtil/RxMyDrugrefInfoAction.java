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
import java.util.Hashtable;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import java.util.*;
import oscar.oscarRx.data.*;
import oscar.oscarRx.pageUtil.*;
import java.io.*;
import org.apache.xmlrpc.*;
import oscar.oscarRx.util.MyDrugrefComparator;
import oscar.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.WebApplicationContext;
import org.oscarehr.common.dao.*;
import org.oscarehr.common.model.*;


public final class RxMyDrugrefInfoAction extends Action {
    
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {
        
        String provider = (String) request.getSession().getAttribute("user");
        
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
        UserPropertyDAO  propDAO =  (UserPropertyDAO) ctx.getBean("UserPropertyDAO");
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
        String[] str = new String[]{"warnings_byATC","bulletins_byATC","interactions_byATC"};
        Vector all = new Vector();
        for (String command : str){
            try{
                Vector v = getMyDrugrefInfo(command,  codes,myDrugrefId) ;
                if (v !=null && v.size() > 0){
                    all.addAll(v);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        Collections.sort(all, new MyDrugrefComparator());
        request.setAttribute("warnings",all);
        ///////
        return mapping.findForward("success");
    }
    
    public static void removeNullFromVector(Vector v){
        while(v != null && v.contains(null)){
            v.remove(null);
        }
    }
    
    
    public Vector getMyDrugrefInfo(String command, Vector drugs,String myDrugrefId)throws Exception{
        removeNullFromVector(drugs);
        Vector params = new Vector();
        params.addElement(command);
        params.addElement(drugs);
        
        if (myDrugrefId != null && !myDrugrefId.trim().equals("")){
            System.out.println("putting >"+myDrugrefId+ "< in the request");
            params.addElement(myDrugrefId);
        }
        
        Vector vec = new Vector();
        Object obj =  callWebserviceLite("Fetch",params);
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
                System.out.println(s+" "+((Hashtable) obj).get(s)+" "+((Hashtable) obj).get(s).getClass().getName());
            }
        }
        
        return vec;
    }
    
//    public Vector getWarnings(Vector drugs,String myDrugrefId)throws Exception{
//        removeNullFromVector(drugs);
//        Vector params = new Vector();
//        params.addElement("warnings_byATC");
//        params.addElement(drugs);
//
//        if (myDrugrefId != null && !myDrugrefId.trim().equals("")){
//           System.out.println("putting >"+myDrugrefId+ "< in the request");
//          params.addElement(myDrugrefId);
//        }
//
//        Vector vec = new Vector();
//        Object obj =  callWebserviceLite("Fetch",params);
//        if (obj instanceof Vector){
//            vec = (Vector) obj;
//        }else if(obj instanceof Hashtable){
//            Object holbrook = ((Hashtable) obj).get("Holbrook Drug Interactions");
//            if (holbrook instanceof Vector){
//                vec = (Vector) holbrook;
//            }
//            Enumeration e = ((Hashtable) obj).keys();
//            while (e.hasMoreElements()){
//                String s = (String) e.nextElement();
//                System.out.println(s+" "+((Hashtable) obj).get(s)+" "+((Hashtable) obj).get(s).getClass().getName());
//            }
//        }
//
//        return vec;
//    }
//
//    public Vector getBulletins(Vector drugs)throws Exception{
//        removeNullFromVector(drugs);
//        Vector params = new Vector();
//        params.addElement("bulletins_byATC");
//        params.addElement(drugs);
//        //params.addElement("jaygallagher@gmail.com");
//        //Vector vec = (Vector) callWebserviceLite("get",params);
//        Vector vec = new Vector();
//        Object obj =  callWebserviceLite("Fetch",params);
//        if (obj instanceof Vector){
//            vec = (Vector) obj;
//        }else if(obj instanceof Hashtable){
//            Object holbrook = ((Hashtable) obj).get("Holbrook Drug Interactions");
//            if (holbrook instanceof Vector){
//                vec = (Vector) holbrook;
//            }
//            Enumeration e = ((Hashtable) obj).keys();
//            while (e.hasMoreElements()){
//                String s = (String) e.nextElement();
//                System.out.println(s+" "+((Hashtable) obj).get(s)+" "+((Hashtable) obj).get(s).getClass().getName());
//            }
//        }
//
//        return vec;
//    }
//
//
//
//    public Vector getInteractions(Vector drugs,String myDrugrefId)throws Exception{
//        removeNullFromVector(drugs);
//        Vector params = new Vector();
//        params.addElement("interactions_byATC");
//        params.addElement(drugs);
//        params.addElement("jaygallagher@gmail.com");
//        //Vector vec = (Vector) callWebserviceLite("get",params);
//        Vector vec = new Vector();
//        Object obj =  callWebserviceLite("Fetch",params);
//        if (obj instanceof Vector){
//            vec = (Vector) obj;
//        }else if(obj instanceof Hashtable){
//            Object holbrook = ((Hashtable) obj).get("Holbrook Drug Interactions");
//            if (holbrook instanceof Vector){
//                vec = (Vector) holbrook;
//            }
//            Enumeration e = ((Hashtable) obj).keys();
//            while (e.hasMoreElements()){
//                String s = (String) e.nextElement();
//                System.out.println(s+" "+((Hashtable) obj).get(s)+" "+((Hashtable) obj).get(s).getClass().getName());
//            }
//        }
//
//        return vec;
//    }
//
    
    
    private Object callWebserviceLite(String procedureName,Vector params) throws Exception{
        System.out.println("#CALLDRUGREF-"+procedureName);
        Object object = null;
        String server_url = "http://dev2.mydrugref.org/backend/api";
        try{
            System.out.println("server_url :"+server_url);
            XmlRpcClientLite server = new XmlRpcClientLite(server_url);
            object = (Object) server.execute(procedureName, params);
        }catch (XmlRpcException exception) {
            
            System.err.println("JavaClient: XML-RPC Fault #" +
                    Integer.toString(exception.code) + ": " +
                    exception.toString());
            exception.printStackTrace();
            
            throw new Exception("JavaClient: XML-RPC Fault #" +
                    Integer.toString(exception.code) + ": " +
                    exception.toString());
            
        } catch (Exception exception) {
            System.err.println("JavaClient: " + exception.toString());
            exception.printStackTrace();
            throw new Exception("JavaClient: " + exception.toString());
        }
        return object;
    }
    
    
    
}