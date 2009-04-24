package oscar.oscarRx.web;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.LazyValidatorForm;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import oscar.oscarRx.service.RxPrescriptionMgr;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author toby
 */
public class CopyFavoritesAction extends DispatchAction{

    private Log logger = LogFactory.getLog(this.getClass());

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("copyFavorites-update");
        
        RxPrescriptionMgr rxMgr = getRxMgr();
        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        String providerNo = lazyForm.get("userProviderNo").toString();
        int share = Integer.parseInt(lazyForm.get("rb_share").toString());
        rxMgr.setFavoritesPrivilege(providerNo, share==0?false:true, false);

        return mapping.findForward("success");
    }

    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("copyFavorites-refresh");

        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        String providerNo = lazyForm.get("ddl_provider").toString();
        request.setAttribute("copyProviderNo", providerNo);

        return mapping.findForward("success");
    }

    public ActionForward copy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("copyFavorites-copy");

        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        String providerNo = lazyForm.get("userProviderNo").toString();
        if( lazyForm.get("ddl_provider") == null || lazyForm.get("ddl_provider").toString()=="")
            return mapping.findForward("success");

        int count = Integer.parseInt(lazyForm.get("countFavorites").toString());
        List favIDs = new ArrayList<Integer>();
        for (int i=0;i<count;i++){
            String search = "selected"+i;
            if (lazyForm.get(search)!=null){
                int id = Integer.parseInt(lazyForm.get("fldFavoriteId"+i).toString());
                favIDs.add(id);
            }
        }
        RxPrescriptionMgr rxMgr = getRxMgr();
        rxMgr.setFavoritesForProvider(providerNo, favIDs);
        return mapping.findForward("success");
    }

    public WebApplicationContext getApptContext() {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
    }

    public RxPrescriptionMgr getRxMgr() {
        return (RxPrescriptionMgr) getApptContext().getBean("RxPrescriptionMgr");
    }
}
