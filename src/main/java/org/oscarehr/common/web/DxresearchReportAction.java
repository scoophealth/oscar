/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.MyGroupDao;
import org.oscarehr.common.model.DxRegistedPTInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import oscar.OscarDocumentCreator;
import oscar.oscarResearch.oscarDxResearch.bean.dxCodeSearchBean;
import oscar.oscarResearch.oscarDxResearch.bean.dxQuickListBeanHandler;
import oscar.oscarResearch.oscarDxResearch.bean.dxQuickListItemsHandler;
import oscar.oscarResearch.oscarDxResearch.util.dxResearchCodingSystem;
/**
 *
 * @author toby
 */
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class DxresearchReportAction extends DispatchAction {
    private final static String SUCCESS = "success";
    private final static String EDIT_DESC = "editdesc";
    private DxresearchDAO dxresearchdao ;
    private MyGroupDao mygroupdao = (MyGroupDao)SpringUtils.getBean("myGroupDao");
    private static final String REPORTS_PATH = "org/oscarehr/common/web/DxResearchReport.jrxml";

    public void setDxresearchdao(DxresearchDAO dxresearchdao) {
        this.dxresearchdao = dxresearchdao;
    }


    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().setAttribute("listview", new DxRegistedPTInfo());
        dxQuickListBeanHandler quicklistHd = new dxQuickListBeanHandler();
        request.getSession().setAttribute("allQuickLists", quicklistHd);
        dxResearchCodingSystem codingSys = new dxResearchCodingSystem();
        request.getSession().setAttribute("codingSystem", codingSys);
        request.getSession().setAttribute("radiovaluestatus", request.getSession().getAttribute("radiovaluestatus"));
        return mapping.findForward(SUCCESS);
    }

    public ActionForward patientRegistedAll(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
             {

        List<String> providerNoList = new ArrayList<String>();
        String providerNo = request.getParameter("provider_no");
        if (providerNo.startsWith("_grp_")){
            providerNo=providerNo.replaceFirst("_grp_", "");
            providerNoList = mygroupdao.getGroupDoctors(providerNo);
        }else
            providerNoList.add(providerNo);


        List codeSearch = (List)request.getSession().getAttribute("codeSearch");
        List patientInfo = dxresearchdao.patientRegistedAll(codeSearch,providerNoList);
        request.getSession().setAttribute("listview", patientInfo);
        if (patientInfo == null || patientInfo.size()==0)
        {
            request.getSession().setAttribute("Counter", 0);
        }else
            request.getSession().setAttribute("Counter", patientInfo.size());
        request.getSession().setAttribute("radiovaluestatus", "patientRegistedAll");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward patientExcelReport(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
             {
        ServletOutputStream outputStream = getServletOstream(response);

        List<DxRegistedPTInfo> patients = null;

        if(request.getSession().getAttribute("listview").getClass().getCanonicalName().contains("ArrayList")) {
            patients = (List<DxRegistedPTInfo>) request.getSession().getAttribute("listview");
        } else if(request.getSession().getAttribute("listview").getClass().getCanonicalName().contains("DxRegistedPTInfo")) {
        	patients = new ArrayList<DxRegistedPTInfo>();
        	DxRegistedPTInfo info = (DxRegistedPTInfo) request.getSession().getAttribute("listview");
        	patients.add(info);
        }

        String providerNo = request.getParameter("provider_no");

        if (providerNo.startsWith("_grp_")){
            providerNo=providerNo.replaceFirst("_grp_", "");
        }

        String mode = (String) request.getSession().getAttribute("radiovaluestatus");

    	OscarDocumentCreator osc = new OscarDocumentCreator();
        HashMap<String,String> reportParams = new HashMap<String,String>();
        reportParams.put("provider", providerNo);
        reportParams.put("mode", mode);

        InputStream reportInstream = this.getClass().getClassLoader().getResourceAsStream(REPORTS_PATH);

    	response.setContentType("application/excel");
    	response.setHeader( "Content-disposition", "inline; filename=dxResearchReport.xls");

    	osc.fillDocumentStream(reportParams, outputStream, OscarDocumentCreator.EXCEL, reportInstream, patients);

        return null;
    }

    public ActionForward patientRegistedDistincted(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
             {

        List<String> providerNoList = new ArrayList<String>();
        String providerNo = request.getParameter("provider_no");
        if (providerNo.startsWith("_grp_")){
            providerNo=providerNo.replaceFirst("_grp_", "");
            providerNoList = mygroupdao.getGroupDoctors(providerNo);
        }else
            providerNoList.add(providerNo);

        List codeSearch = (List)request.getSession().getAttribute("codeSearch");
        List patientInfo = dxresearchdao.patientRegistedDistincted(codeSearch,providerNoList);
        request.getSession().setAttribute("listview", patientInfo);
        if (patientInfo == null || patientInfo.size()==0)
        {
            request.getSession().setAttribute("Counter", 0);
        }else
            request.getSession().setAttribute("Counter", patientInfo.size());
        request.getSession().setAttribute("radiovaluestatus", "patientRegistedDistincted");
        return mapping.findForward(SUCCESS);
    }

    protected ServletOutputStream getServletOstream(HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try {
          outputStream = response.getOutputStream();
        } catch (IOException ex) {
		MiscUtils.getLogger().warn("Warning",ex);
        }
        return outputStream;
    }

    public ActionForward patientRegistedDeleted(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            {

        List<String> providerNoList = new ArrayList<String>();
        String providerNo = request.getParameter("provider_no");
        if (providerNo.startsWith("_grp_")){
            providerNo=providerNo.replaceFirst("_grp_", "");
            providerNoList = mygroupdao.getGroupDoctors(providerNo);
        }else
            providerNoList.add(providerNo);

        List codeSearch = (List)request.getSession().getAttribute("codeSearch");
        List patientInfo = dxresearchdao.patientRegistedDeleted(codeSearch,providerNoList);
        request.getSession().setAttribute("listview", patientInfo);
        if (patientInfo == null || patientInfo.size()==0)
        {
            request.getSession().setAttribute("Counter", 0);
        }else
            request.getSession().setAttribute("Counter", patientInfo.size());
        request.getSession().setAttribute("radiovaluestatus", "patientRegistedDeleted");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward patientRegistedActive(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
             {

        List<String> providerNoList = new ArrayList<String>();
        String providerNo = request.getParameter("provider_no");
        if (providerNo.startsWith("_grp_")){
            providerNo=providerNo.replaceFirst("_grp_", "");
            providerNoList = mygroupdao.getGroupDoctors(providerNo);
        }else
            providerNoList.add(providerNo);

        List codeSearch = (List)request.getSession().getAttribute("codeSearch");
        List patientInfo = dxresearchdao.patientRegistedActive(codeSearch,providerNoList);
        request.getSession().setAttribute("listview", patientInfo);
        if (patientInfo == null || patientInfo.size()==0)
        {
            request.getSession().setAttribute("Counter", 0);
        }else
            request.getSession().setAttribute("Counter", patientInfo.size());
        request.getSession().setAttribute("radiovaluestatus", "patientRegistedActive");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward patientRegistedResolve(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
             {

        List<String> providerNoList = new ArrayList<String>();
        String providerNo = request.getParameter("provider_no");
        if (providerNo.startsWith("_grp_")){
            providerNo=providerNo.replaceFirst("_grp_", "");
            providerNoList = mygroupdao.getGroupDoctors(providerNo);
        }else
            providerNoList.add(providerNo);

        List codeSearch = (List)request.getSession().getAttribute("codeSearch");
        List patientInfo = dxresearchdao.patientRegistedResolve(codeSearch,providerNoList);
        request.getSession().setAttribute("listview", patientInfo);
        if (patientInfo == null || patientInfo.size()==0)
        {
            request.getSession().setAttribute("Counter", 0);
        }else
            request.getSession().setAttribute("Counter", patientInfo.size());
        request.getSession().setAttribute("radiovaluestatus", "patientRegistedResolve");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward editDesc( ActionMapping mapping, ActionForm  form,
                                HttpServletRequest request, HttpServletResponse response)

    {
      String editingCodeType = request.getParameter( "editingCodeType" );
      String editingCodeCode = request.getParameter( "editingCodeCode" );
      String editingCodeDesc = request.getParameter( "editingCodeDesc" );

      dxQuickListItemsHandler.updatePatientCodeDesc( editingCodeType, editingCodeCode, editingCodeDesc );

      editingCodeDesc = String.format( "\"%s\"", editingCodeDesc );
      request.getSession().setAttribute( "editingCodeDesc", editingCodeDesc );

      return mapping.findForward(SUCCESS);
    }

    public ActionForward addSearchCode(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response) {

        DynaBean lazyForm = (DynaBean) form;
        String quickListName = (String)lazyForm.get("quicklistname");
        List codeSearch = dxresearchdao.getQuickListItems(quickListName);
        dxCodeSearchBean newAddition = null;

        String codeSingle = request.getParameter("codesearch");

        if (codeSingle!=null && codeSingle.contains("-->")) // buggy here as user can input "-->" and press ADD button, little odds

        {
            newAddition = new dxCodeSearchBean();
            newAddition.setType("icd9"); // ichppccode/icd10 not supported yet
            newAddition.setDxSearchCode(codeSingle.split("-->")[0]);
            newAddition.setDescription(codeSingle.split("-->")[1]);
        }

        String action = request.getParameter( "action" );
        if( action != null && action.equalsIgnoreCase( "edit" ) && newAddition != null)
        {
//          List editingCodeList = new ArrayList();
//          editingCodeList.add( newAddition );
//          request.getSession().setAttribute("editingCode", editingCodeList );
//          request.getSession().setAttribute("codeSearch", editingCodeList );
          //editingCodeType
          request.getSession().setAttribute( "editingCodeType", newAddition.getType() );
          request.getSession().setAttribute( "editingCodeCode", newAddition.getDxSearchCode() );
          String description = newAddition.getDescription().trim();
          description = String.format( "\"%s\"", description );
          request.getSession().setAttribute( "editingCodeDesc", description );
          return mapping.findForward(EDIT_DESC);
        }

        List existcodeSearch;

        if (request.getSession().getAttribute("codeSearch")!=null && ((List)(request.getSession().getAttribute("codeSearch"))).size()>0)
        {
            existcodeSearch = (List)(request.getSession().getAttribute("codeSearch"));
            codeSearch.addAll(existcodeSearch);
        }

        if (newAddition!=null)
            codeSearch.add(newAddition);

        request.getSession().setAttribute("codeSearch", codeSearch);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward clearSearchCode(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
             {

        
        //String quickListName = (String)lazyForm.get("quicklistname");
        //List codeSearch = dxresearchdao.getQuickListItems(quickListName);;
        List existcodeSearch =null;

        if (request.getSession().getAttribute("codeSearch")!=null && ((List)(request.getSession().getAttribute("codeSearch"))).size()>0)
        {
            existcodeSearch = (List)(request.getSession().getAttribute("codeSearch"));
            existcodeSearch.clear();
            //codeSearch.addAll(existcodeSearch);
        }

        request.getSession().setAttribute("codeSearch", existcodeSearch);

        return mapping.findForward(SUCCESS);
    }

}
