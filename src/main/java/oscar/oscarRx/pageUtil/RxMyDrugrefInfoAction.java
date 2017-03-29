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


package oscar.oscarRx.pageUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcClientLite;
import org.oscarehr.PMmodule.caisi_integrator.RemoteDrugAllergyHelper;
import org.oscarehr.common.dao.AppDefinitionDao;
import org.oscarehr.common.dao.AppUserDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.UserDSMessagePrefsDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.common.model.AppUser;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.UserDSMessagePrefs;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.app.OAuth1Utils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.OscarProperties;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.util.MyDrugrefComparator;
import oscar.oscarRx.util.RxDrugRef;
import oscar.oscarRx.util.RxUtil;
import oscar.oscarRx.util.TimingOutCallback;
import oscar.oscarRx.util.TimingOutCallback.TimeoutException;

public final class RxMyDrugrefInfoAction extends DispatchAction {

    private static final Logger log2 = MiscUtils.getLogger();
    //return interactions about current pending prescriptions
    public ActionForward findInteractingDrugList (ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws IOException {
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

    public ActionForward view(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)  {
        MiscUtils.getLogger().debug("in view RxMyDrugrefInfoAction");
        
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

        try{

        long start = System.currentTimeMillis();
        String target=request.getParameter("target");
        if(target==null) MiscUtils.getLogger().debug("target is null");
        else if(target.equals("interactionsRx")) MiscUtils.getLogger().debug("target is interactionsRx");
        String provider = (String) request.getSession().getAttribute("user");

        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
        UserPropertyDAO  propDAO =  (UserPropertyDAO) ctx.getBean("UserPropertyDAO");
        UserDSMessagePrefsDao  dsmessageDao =  (UserDSMessagePrefsDao) ctx.getBean("userDSMessagePrefsDao");
        MiscUtils.getLogger().debug("hideResources is before "+request.getSession().getAttribute("hideResources"));
        Hashtable dsPrefs=new Hashtable();
        if (request.getSession().getAttribute("hideResources") == null){

            dsPrefs = dsmessageDao.getHashofMessages(provider,UserDSMessagePrefs.MYDRUGREF);

        }
        UserProperty prop = propDAO.getProp(provider, UserProperty.MYDRUGREF_ID);
        String myDrugrefId = null;
        //get from system first
        myDrugrefId  = OscarProperties.getInstance().getProperty("mydrugref_id");

        //override with user pref
        if (prop != null && prop.getValue().length()>0){
            myDrugrefId = prop.getValue();
        }

        RxSessionBean bean = (RxSessionBean) request.getSession().getAttribute("RxSessionBean");
        if ( bean == null ){
            return mapping.findForward("success");
        }
        Vector codes = bean.getAtcCodes();

        if(Boolean.valueOf(OscarProperties.getInstance().getProperty("drug_allergy_interaction_warnings", "false"))) {
        	RxDrugRef d = new RxDrugRef();
        	Allergy[]  allerg = RxPatientData.getPatient(loggedInInfo, bean.getDemographicNo()).getActiveAllergies();
        	Vector vec = new Vector();
            for (int i =0; i < allerg.length; i++){
               Hashtable h = new Hashtable();
               h.put("id",""+i);
               h.put("description",allerg[i].getDescription());
               h.put("type",""+allerg[i].getTypeCode());
               vec.add(h);
            }
        	codes.addAll(d.getAllergyClasses(vec));
        }
        //String[] str = new String[]{"warnings_byATC","bulletins_byATC","interactions_byATC"};
        String[] str = new String[]{"atcfetch/getWarnings","atcfetch/getBulletins","atcfetch/getInteractions"};   //NEW more efficent way of sending multiple requests at the same time.
        MessageResources mr=getResources(request);
        Locale locale = getLocale(request);

        log2.debug("Interaction, local drug atc codes : "+codes);

        if (loggedInInfo.getCurrentFacility().isIntegratorEnabled())
        {
        	ArrayList<String> remoteDrugAtcCodes=RemoteDrugAllergyHelper.getAtcCodesFromRemoteDrugs(loggedInInfo, bean.getDemographicNo());
        	codes.addAll(remoteDrugAtcCodes);
            log2.debug("remote drug atc codes : "+remoteDrugAtcCodes);
        }        
        log2.debug("Interaction, local + remote drug atc codes : "+codes);   

        Vector all = new Vector();
        for (String command : str){
            try{
                Vector v = getMyDrugrefInfo(loggedInInfo,command, codes, provider, myDrugrefId);

                if (v !=null && v.size() > 0){
                    all.addAll(v);
                }

            }catch(Exception e){
                log2.debug("command :"+command+" "+e.getMessage());
                MiscUtils.getLogger().error("Error", e);
            }
        }
        
        if(OscarProperties.getInstance().isPropertyActive("RX_INTERACTION_LOCAL_DRUGREF_REGIONAL_IDENTIFIER")){
        	List regionalIdentifiers = bean.getRegionalIdentifier();
        	if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()){
        		ArrayList<String> remoteDrugRegionalIdentiferCodes=RemoteDrugAllergyHelper.getRegionalIdentiferCodesFromRemoteDrugs(loggedInInfo, bean.getDemographicNo());
        		regionalIdentifiers.addAll(remoteDrugRegionalIdentiferCodes);
        	}
        
	        try{
	        	RxDrugRef rxDrugRef = new RxDrugRef();
	        	List localInteractions =  rxDrugRef.interactionByRegionalIdentifier(regionalIdentifiers,0);
	        	log2.debug("local interactions size = "+localInteractions.size());
	        	all.addAll(localInteractions);
	        }catch(Exception e){
	        	log2.error("Error calling interactions by regional identifier",e);
	        }
        }else{
        	log2.debug("RX_INTERACTION_LOCAL_DRUGREF_REGIONAL_IDENTIFIER was null" );
        }
        
        
        Collections.sort(all, new MyDrugrefComparator());
        
        //filter out based on significance by facility, provider, demographic
        int level = 0;
        int orgLevel = loggedInInfo.getCurrentFacility().getRxInteractionWarningLevel();
        level = orgLevel;
        MiscUtils.getLogger().debug("orgLevel="+orgLevel);

        UserProperty uprop = propDAO.getProp(loggedInInfo.getLoggedInProviderNo(), "rxInteractionWarningLevel");
        if(uprop!=null) {
        	if(uprop.getValue()!=null&&uprop.getValue().length()>0) {
        		int providerLevel = Integer.parseInt(uprop.getValue());
        		MiscUtils.getLogger().debug("providerLevel="+providerLevel);
        		if(providerLevel>0)
        			level = providerLevel;
        	}
        }


        DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
        DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);

        DemographicExt demoWarn = demographicExtDao.getLatestDemographicExt(bean.getDemographicNo(), "rxInteractionWarningLevel");
        if(demoWarn!=null) {
        	if(demoWarn.getValue()!=null&&demoWarn.getValue().length()>0) {
        		int demoLevel = Integer.valueOf(demoWarn.getValue());
        		MiscUtils.getLogger().debug("demoLevel="+demoLevel);
        		if(demoLevel>0)
        			level = demoLevel;
        	}
        }
        MiscUtils.getLogger().debug("level="+level);

        List<Hashtable> toRemove = new ArrayList<Hashtable>();

        for(int x=0;x<all.size();x++) {
        	 Hashtable ht=(Hashtable)all.get(x);
        	 String significanceStr = (String)ht.get("significance");
        	 if(significanceStr==null || significanceStr.equals("")) {significanceStr="0";}
        	 int significance = Integer.valueOf(significanceStr);
        	 MiscUtils.getLogger().debug("significance="+significance);
        	 if((level == 4) || level>0 && significance<level) {
        		 toRemove.add(ht);
        	 }
        }

        for(Hashtable ht:toRemove) {
        	all.remove(ht);
        }

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
                setShowDSMessage(dsmessageDao, provider, resId, updatedatId);
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

    private void setShowDSMessage(UserDSMessagePrefsDao  dsmessageDao,String provider,String resId,Date updatedatId){
	    UserDSMessagePrefs pref = dsmessageDao.getDsMessage(provider,UserDSMessagePrefs.MYDRUGREF , resId,true);
	    if(pref != null) {
	    	pref.setProviderNo(provider);
	        pref.setRecordCreated(new Date());
	        pref.setResourceId(resId);
	        pref.setResourceType(UserDSMessagePrefs.MYDRUGREF);
	        pref.setResourceUpdatedDate(updatedatId);
	        pref.setArchived(Boolean.FALSE);
	        dsmessageDao.updateProp(pref);
	    }

    }

    public ActionForward setWarningToHide(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) {

        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
        UserDSMessagePrefsDao  dsmessageDao =  (UserDSMessagePrefsDao) ctx.getBean("userDSMessagePrefsDao");


        String provider = (String) request.getSession().getAttribute("user");
        String postId = request.getParameter("resId");
        String date = request.getParameter("updatedat");


        long datel = Long.parseLong(date);
        Date updatedatId = new Date();
        updatedatId.setTime(datel);

        log2.debug("post Id "+postId+"  date "+date);


        if (request.getSession().getAttribute("hideResources") == null){

            Hashtable dsPrefs = dsmessageDao.getHashofMessages(provider,UserDSMessagePrefs.MYDRUGREF);
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

        dsmessageDao.saveProp(pref);

       return mapping.findForward("updateResources");
    }

    @SuppressWarnings("unchecked")
    public ActionForward setWarningToShow(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) {

        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
        UserDSMessagePrefsDao  dsmessageDao =  (UserDSMessagePrefsDao) ctx.getBean("userDSMessagePrefsDao");

        String provider = (String) request.getSession().getAttribute("user");
        String resId = request.getParameter("resId");
        String date = request.getParameter("updatedat");


        long datel = Long.parseLong(date);
        Date updatedatId = new Date();
        updatedatId.setTime(datel);

        log2.debug("post Id "+resId+"  date "+date);

        if (request.getSession().getAttribute("hideResources") == null){
            Hashtable<String,Long> dsPrefs = dsmessageDao.getHashofMessages(provider,UserDSMessagePrefs.MYDRUGREF);
            request.getSession().setAttribute("hideResources",dsPrefs);//this doesn't save values that can be used directly
        }
        Hashtable<String,Long> h = (Hashtable<String,Long>) request.getSession().getAttribute("hideResources");
        h.remove("mydrugref"+resId);
        MiscUtils.getLogger().debug("provider,UserDSMessagePrefs.MYDRUGREF , postId, updatedatId :"+provider+"--"+UserDSMessagePrefs.MYDRUGREF +"--"+ resId+"--"+ updatedatId);
        setShowDSMessage(dsmessageDao, provider, resId, updatedatId);
        request.getSession().setAttribute("hideResources", h);

       return mapping.findForward("updateResources");
    }


    public static void removeNullFromVector(Vector v){
        while(v != null && v.contains(null)){
            v.remove(null);
        }
    }


    public Vector getMyDrugrefInfo(LoggedInInfo loggedInInfo,String command, Vector drugs, String providerNo, String myDrugrefId) {

        removeNullFromVector(drugs);

        Vector vec = new Vector();
        log2.debug("CALL : FETCH:"+drugs);
        Object obj =  callOAuthService(loggedInInfo,command,drugs, myDrugrefId);
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


    public Vector callOAuthService(LoggedInInfo loggedInInfo,String procedureName, Vector params, String myDrugrefId){
    	try {
    		AppDefinitionDao appDefinitionDao = SpringUtils.getBean(AppDefinitionDao.class);
    		AppUserDao appUserDao = SpringUtils.getBean(AppUserDao.class);
    		
    		AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
    		
    		boolean useXMLRPC = false;
    		Vector result = null;
    		
    		if(k2aApp != null) {
	    		AppUser k2aUser = appUserDao.findForProvider(k2aApp.getId(),loggedInInfo.getLoggedInProviderNo());
	    		
	    		if(k2aUser != null) {
	    			
		    		String requestURI = "/ws/api/" + procedureName;
		    		
		    		String requestURIWithParams = null;
		    		if(params != null && !params.isEmpty() && params.size() > 0) {
		    			requestURIWithParams = requestURI + "?";
			    		for(int i=0;i<params.size();i++){
							if(procedureName.contains("guidelines")) {
								requestURIWithParams += "uuidCodes=" + params.get(i) + "&";
							} else {
								requestURIWithParams += "atcCodes=" + params.get(i) + "&";
							}
						}
			    		requestURIWithParams = requestURIWithParams.substring(0,requestURIWithParams.length()-1);
		    		}
		    		
		    		
	    			if(requestURIWithParams == null) {
		    			requestURIWithParams = requestURI;
		    		} 
		    		
		    		String jsonString = OAuth1Utils.getOAuthGetResponse( loggedInInfo,k2aApp, k2aUser, requestURIWithParams, requestURI);
		    		//Convert JSON return to Vector/Hashtable
		    		JSONArray jsonArray = new JSONArray();
		    		
		    		if(jsonString != null && !jsonString.isEmpty()) {
		    			jsonArray = new JSONArray(jsonString);
		    			result = new Vector();
		    			
		    			for (int i = 0; i < jsonArray.length(); i++) {
		    	        	JSONObject eform = jsonArray.getJSONObject(i);
		    	        	Hashtable drugInfo = new Hashtable();
		    	        	
		    	        	Iterator iterator = eform.keys();
		    	        	while(iterator.hasNext()){
		    	        		String key = (String) iterator.next();
		    	        		if(key.equals("significance") || key.equals("version")) {
		    	        			drugInfo.put(key, eform.get(key).toString());
		    	        		} else if(key.equals("updated_at") || key.equals("updatedAt") || key.equals("created_at")){
		    	        			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		    	        			Date date = formatter.parse(eform.get(key).toString());
		    	        			drugInfo.put(key, date);
		    	        		} else {
		    	        			drugInfo.put(key, eform.get(key));
		    	        		}
		    	        	}
		    	     
		    	        	result.add(drugInfo);
		    			}
		    		}
	    		} else {
	    			useXMLRPC = true;
	    		}
    		} else {
    			useXMLRPC = true;
    		}
    		
    		if(useXMLRPC) {
    			Vector newParams = new Vector();
    			
    			//Convert from OAuth procedure name to xml rpc procedure name
    			if(procedureName.equals("atcfetch/getWarnings")) {
    				newParams.addElement("warnings_byATC");
    				procedureName = "Fetch";
    			} else if(procedureName.equals("atcfetch/getBulletins")) {
    				newParams.addElement("bulletins_byATC");
    				procedureName = "Fetch";
    			} else if(procedureName.equals("atcfetch/getInteractions")) {
    				newParams.addElement("interactions_byATC");
    				procedureName = "Fetch";
    			} else if(procedureName.equals("guidelines/getGuidelineIds")) {
    				procedureName = "GetGuidelineIds";
    			} else if(procedureName.equals("guidelines/getGuidelines")) {
    				procedureName = "GetGuidelines";
    			}
    			
    			if(params != null) {
    				newParams.add(params);
    			}

    	        if (myDrugrefId != null && !myDrugrefId.trim().equals("")){
    	            log2.debug("putting >"+myDrugrefId+ "< in the request");
    	            newParams.addElement(myDrugrefId);
    	            //params.addElement("true");
    	        }
    			log2.debug("#CALLmyDRUGREF-"+procedureName);
    	        Object object = null;

    	        String server_url = OscarProperties.getInstance().getProperty("MY_DRUGREF_URL","http://know2act.org/backend/api");

    	        TimingOutCallback callback = new TimingOutCallback(10 * 1000);
    	        try{
    	            log2.debug("server_url :"+server_url);
    	            if (!System.getProperty("http.proxyHost","").isEmpty()) {
    	                //The Lite client won't recgonize JAVA_OPTS as it uses a customized http
    	                XmlRpcClient server = new XmlRpcClient(server_url);
    	                server.executeAsync(procedureName, newParams, callback);
    	            } else {
    	                XmlRpcClientLite server = new XmlRpcClientLite(server_url);
    	                server.executeAsync(procedureName, newParams, callback);
    	            }
    	            object = callback.waitForResponse();
    	        } catch (TimeoutException e) {
    	            log2.debug("No response from server."+server_url);
    	        }catch(Exception ethrow){
    	            log2.debug("Throwing error."+ethrow.getMessage());
    	        }
    	        result = (Vector)object;
    		}
    		
    		return result;
    	} catch(Exception e) {
    		log2.error("Failed to retrieve K2A drug ref information", e);
    		return null;
    	}
    }
}
