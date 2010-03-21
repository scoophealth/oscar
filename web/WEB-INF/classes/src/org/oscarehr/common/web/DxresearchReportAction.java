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
        return mapping.findForward(SUCCESS);
    }
    
    public ActionForward patientRegistedAll(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaBean lazyForm = (DynaBean) form;
        List patientInfo = dxresearchdao.patientRegistedAll();
        request.getSession().setAttribute("listview", patientInfo);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward patientRegistedDistincted(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        DynaBean lazyForm = (DynaBean) form;
        List patientInfo = dxresearchdao.patientRegistedDistincted();
        request.getSession().setAttribute("listview", patientInfo);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward patientRegistedDeleted(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaBean lazyForm = (DynaBean) form;
        List patientInfo = dxresearchdao.patientRegistedDeleted();
        request.getSession().setAttribute("listview", patientInfo);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward patientRegistedActive(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaBean lazyForm = (DynaBean) form;
        List patientInfo = dxresearchdao.patientRegistedActive();
        request.getSession().setAttribute("listview", patientInfo);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward patientRegistedResolve(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaBean lazyForm = (DynaBean) form;
        List patientInfo = dxresearchdao.patientRegistedResolve();
        request.getSession().setAttribute("listview", patientInfo);
        return mapping.findForward(SUCCESS);
    }

}