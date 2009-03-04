package org.oscarehr.common.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.jdom.Element;
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
 * Jason Gallagher
 *
 * UserPropertyDAO.java
 *
 * Created on December 19, 2007, 4:29 PM
 *
 *
 *
 */


import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.oscarehr.common.dao.FlowSheetCustomizerDAO;
import org.oscarehr.common.model.FlowSheetCustomization;
import oscar.oscarEncounter.oscarMeasurements.FlowSheetItem;
import oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet;
import oscar.oscarEncounter.oscarMeasurements.MeasurementTemplateFlowSheetConfig;
import oscar.oscarEncounter.oscarMeasurements.util.Recommendation;
import oscar.oscarEncounter.oscarMeasurements.util.RecommendationCondition;
import oscar.oscarEncounter.oscarMeasurements.util.TargetColour;
import oscar.oscarEncounter.oscarMeasurements.util.TargetCondition;

public class FlowSheetCustomAction extends DispatchAction {
    private static final Log log2 = LogFactory.getLog(FlowSheetCustomAction.class);

    private FlowSheetCustomizerDAO flowSheetCustomizerDAO;

    public void setFlowSheetCustomizerDAO(FlowSheetCustomizerDAO flowSheetCustomizerDAO) {
        this.flowSheetCustomizerDAO = flowSheetCustomizerDAO;
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log2.debug("AnnotationAction-unspec");
        //return setup(mapping, form, request, response);
        return null;
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String flowsheet = request.getParameter("flowsheet");
        String demographicNo = request.getParameter("demographic");

        MeasurementTemplateFlowSheetConfig templateConfig = MeasurementTemplateFlowSheetConfig.getInstance();
        MeasurementFlowSheet mFlowsheet = templateConfig.getFlowSheet(flowsheet);

        if (request.getParameter("measurement") != null) {

            Hashtable h = new Hashtable();

            h.put("measurement_type", request.getParameter("measurement"));
            h.put("display_name", request.getParameter("display_name"));
            h.put("guideline", request.getParameter("guideline"));
            h.put("graphable", request.getParameter("graphable"));
            h.put("value_name", request.getParameter("value_name"));
            String prevItem = null;
            if (request.getParameter("count") != null) {
                int cou = Integer.parseInt(request.getParameter("count"));
                if (cou != 0) {
                    prevItem = (String) mFlowsheet.getMeasurementList().get(cou);
                }
            }

            Enumeration<String> en = request.getParameterNames();

            List ds = new ArrayList();
            while (en.hasMoreElements()) {
                String s = en.nextElement();
                if (s.startsWith("monthrange")) {
                    String extrachar = s.replaceAll("monthrange", "").trim();
                    log2.debug("EXTRA CAH " + extrachar);
                    String mRange = request.getParameter("monthrange" + extrachar);
                    String strn = request.getParameter("strength" + extrachar);
                    String dsText = request.getParameter("text" + extrachar);
                    if (!mRange.trim().equals("")){
                       ds.add(new Recommendation("" + h.get("measurement_type"), mRange, strn, dsText));
                    }
                }
            }

            if (h.get("measurement_type") != null) {
                FlowSheetItem item = new FlowSheetItem(h);
                item.setRecommendations(ds);
                Element va = templateConfig.getItemFromObject(item);

                XMLOutputter outp = new XMLOutputter();
                outp.setFormat(Format.getPrettyFormat());

                FlowSheetCustomization cust = new FlowSheetCustomization();
                cust.setAction(FlowSheetCustomization.ADD);
                cust.setPayload(outp.outputString(va));
                cust.setFlowsheet(flowsheet);
                cust.setMeasurement(prevItem);//THIS THE MEASUREMENT TO SET THIS AFTER!
                cust.setProviderNo((String) request.getSession().getAttribute("user"));
                cust.setDemographicNo(demographicNo);
                cust.setCreateDate(new Date());
        
                log2.debug("SAVE "+cust);

                flowSheetCustomizerDAO.save(cust);

            }
        }
        request.setAttribute("demographic",demographicNo);
        request.setAttribute("flowsheet", flowsheet);
        return mapping.findForward("success");
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MeasurementTemplateFlowSheetConfig templateConfig = MeasurementTemplateFlowSheetConfig.getInstance();

        String flowsheet = request.getParameter("flowsheet");
        String measurement = request.getParameter("measurement");
        String demographicNo = request.getParameter("demographic");
        
        log2.debug("UPDATING FOR demographic "+demographicNo);

        if (request.getParameter("updater") != null) {
            Hashtable h = new Hashtable();
            h.put("measurement_type", request.getParameter("measurement_type"));
            h.put("display_name", request.getParameter("display_name"));
            h.put("guideline", request.getParameter("guideline"));
            h.put("graphable", request.getParameter("graphable"));
            h.put("value_name", request.getParameter("value_name"));

            //mFlowsheet.updateMeasurementFlowSheetInfo(measurement,h);
            FlowSheetItem item = new FlowSheetItem(h);
            

            Enumeration<String> en = request.getParameterNames();

            ///List ds = new ArrayList();
            List<TargetColour> targets = new ArrayList();
            List<Recommendation> recommendations = new ArrayList();
            while (en.hasMoreElements()) {
                String s = en.nextElement();
                if (s.startsWith("strength")) {
                    String extrachar = s.replaceAll("strength", "").trim();
                    log2.debug("EXTRA CAH " + extrachar);
                    boolean go = true;
                    Recommendation rec = new Recommendation();
                    rec.setStrength(request.getParameter(s));
                    int targetCount = 1;
                    rec.setText(request.getParameter("text"+extrachar));
                    List<RecommendationCondition> conds = new ArrayList();
                    while(go){
                        String type = request.getParameter("type"+extrachar+"c"+targetCount);
                        if (type != null){
                            if (!type.equals("-1")){
                                String param = request.getParameter("param"+extrachar+"c"+targetCount);
                                String value = request.getParameter("value"+extrachar+"c"+targetCount);
                                RecommendationCondition cond = new RecommendationCondition();
                                cond.setType(type);
                                cond.setParam(param);
                                cond.setValue(value);
                                if (value != null && !value.trim().equals("")){
                                   conds.add(cond);
                                }
                            }
                        }else{
                            go = false;
                        }
                        targetCount++;
                    }
                    if (conds.size() > 0){
                        rec.setRecommendationCondition(conds);
                        recommendations.add(rec);
                    }
                    //////
                    /*  Strength:   <select name="strength<%=count%>">                      
                        Text: <input type="text" name="text<%=count%>" length="100"  value="<%=e.getText()%>" />    
                        <select name="type<%=count%>c<%=condCount%>" >   
                        Param: <input type="text" name="param<%=count%>c<%=condCount%>" value="<%=s(cond.getParam())%>" />
                        Value: <input type="text" name="value<%=count%>c<%=condCount%>" value="<%=cond.getValue()%>" />
                    */           
                    //////
                    
                    
                    
                    
                    
                    
//                    String mRange = request.getParameter("monthrange" + extrachar);
//                    String strn = request.getParameter("strength" + extrachar);
//                    String dsText = request.getParameter("text" + extrachar);
//                    if (!mRange.trim().equals("")){
//                       ds.add(new Recommendation("" + h.get("measurement_type"), mRange, strn, dsText));
//                    }
                }else if(s.startsWith("col")){
                    String extrachar = s.replaceAll("col", "").trim();
                    log2.debug("EXTRA CHA "+extrachar);
                    boolean go = true;
                    int targetCount = 1;
                    TargetColour tcolour = new TargetColour();
                    tcolour.setIndicationColor(request.getParameter(s));  
                    List<TargetCondition> conds = new ArrayList();
                    while(go){
                        String type = request.getParameter("targettype"+extrachar+"c"+targetCount);
                        if (type != null){
                            if (!type.equals("-1")){
                                String param = request.getParameter("targetparam"+extrachar+"c"+targetCount);
                                String value = request.getParameter("targetvalue"+extrachar+"c"+targetCount);
                                TargetCondition cond = new TargetCondition();
                                cond.setType(type);
                                cond.setParam(param);
                                cond.setValue(value);
                                if(value !=null && !value.trim().equals("")){
                                   conds.add(cond);
                                }
                            }
                        }else{
                            go = false;
                        }
                        targetCount++;
                    }
                    if (conds.size() > 0){
                        tcolour.setTargetConditions(conds);
                        targets.add(tcolour);
                    }
                }
            }
            item.setTargetColour(targets);
            item.setRecommendations(recommendations);
            
            
            
            
            
            //DEALING WITH TARGET DATA//////////
            
          /*                      
            <select name="type<%=targetCount%>c1"> 
               <option value="-1">Not Set</option>
                <option value="getDataAsDouble"       >Number Value</option>
                <option value="isMale"              > Is Male </option>
                <option value="isFemale"            > Is Female </option>
                <option value="getNumberFromSplit"  > Number Split </option>
                <option value="isDataEqualTo"       >  String </option>
           </select>

           Param: <input type="text" name="param<%=targetCount%>c1" value="" />
           Value: <input type="text" name="value<%=targetCount%>c1" value="" />

             */                              
                               
            
            ////////////


            
            Element va = templateConfig.getItemFromObject(item);

            XMLOutputter outp = new XMLOutputter();
            outp.setFormat(Format.getPrettyFormat());

            FlowSheetCustomization cust = new FlowSheetCustomization();
            cust.setAction(cust.UPDATE);
            cust.setPayload(outp.outputString(va));
            cust.setFlowsheet(flowsheet);
            if(demographicNo != null ){
               cust.setDemographicNo(demographicNo);
            }
            cust.setMeasurement(item.getItemName());//THIS THE MEASUREMENT TO SET THIS AFTER!
            cust.setProviderNo((String) request.getSession().getAttribute("user"));
            log2.debug("UPDATE "+cust);

            flowSheetCustomizerDAO.save(cust);

        }
        request.setAttribute("demographic",demographicNo);
        request.setAttribute("flowsheet", flowsheet);
        return mapping.findForward("success");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log2.debug("IN DELETE");
        String flowsheet = request.getParameter("flowsheet");
        String measurement = request.getParameter("measurement");
        String demographicNo = request.getParameter("demographic");
        
        FlowSheetCustomization cust = new FlowSheetCustomization();

        cust.setAction(cust.DELETE);
        cust.setFlowsheet(flowsheet);
        cust.setMeasurement(measurement);
        cust.setProviderNo((String) request.getSession().getAttribute("user"));
        cust.setDemographicNo(demographicNo);

        flowSheetCustomizerDAO.save(cust);
        log2.debug("DELETE "+cust);
        
        request.setAttribute("demographic",demographicNo);
        request.setAttribute("flowsheet", flowsheet);
        return mapping.findForward("success");
    }
    
    public ActionForward archiveMod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log2.debug("IN MOD");
        String id = request.getParameter("id");
        String measurement = request.getParameter("measurement");
        
        String flowsheet = request.getParameter("flowsheet");
        String demographicNo = request.getParameter("demographic");
        
        FlowSheetCustomization cust = flowSheetCustomizerDAO.getFlowSheetCustomization(id);
        cust.setArchived(true);
        cust.setArchivedDate(new Date());
        flowSheetCustomizerDAO.save(cust);
        log2.debug("archiveMod "+cust);
        
        request.setAttribute("demographic",demographicNo);
        request.setAttribute("flowsheet", flowsheet);
        return mapping.findForward("success");
    }
    
    
    
}