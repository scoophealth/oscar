/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.DynaBean;

import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.model.DxRegistedPTInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import oscar.oscarResearch.oscarDxResearch.bean.dxCodeSearchBean;
import oscar.oscarResearch.oscarDxResearch.bean.dxQuickListBeanHandler;
import oscar.oscarResearch.oscarDxResearch.util.dxResearchCodingSystem;

/**
 *
 * @author toby
 */
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class DxresearchReportAction extends DispatchAction {
    
    private final static String SUCCESS = "success";
    private DxresearchDAO dxresearchdao ;

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
        return mapping.findForward(SUCCESS);
    }
    
    public ActionForward patientRegistedAll(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        List codeSearch = (List)request.getSession().getAttribute("codeSearch");
        List patientInfo = dxresearchdao.patientRegistedAll(codeSearch);
        request.getSession().setAttribute("listview", patientInfo);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward patientRegistedDistincted(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        List codeSearch = (List)request.getSession().getAttribute("codeSearch");
        List patientInfo = dxresearchdao.patientRegistedDistincted(codeSearch);
        request.getSession().setAttribute("listview", patientInfo);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward patientRegistedDeleted(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        List codeSearch = (List)request.getSession().getAttribute("codeSearch");
        List patientInfo = dxresearchdao.patientRegistedDeleted(codeSearch);
        request.getSession().setAttribute("listview", patientInfo);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward patientRegistedActive(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        List codeSearch = (List)request.getSession().getAttribute("codeSearch");
        List patientInfo = dxresearchdao.patientRegistedActive(codeSearch);
        request.getSession().setAttribute("listview", patientInfo);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward patientRegistedResolve(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        List codeSearch = (List)request.getSession().getAttribute("codeSearch");
        List patientInfo = dxresearchdao.patientRegistedResolve(codeSearch);
        request.getSession().setAttribute("listview", patientInfo);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward addSearchCode(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

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
            throws Exception {

        DynaBean lazyForm = (DynaBean) form;

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