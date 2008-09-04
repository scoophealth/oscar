/*
 * RxCloneServiceImpl.java
 *
 * Created on August 19, 2008, 12:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarRx.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import oscar.oscarRx.client.service.RxCloneService;
import oscar.oscarRx.pageUtil.RxSessionBean;
import oscar.oscarRx.service.RxPrescriptionMgr;

/**
 *
 * @author toby
 */
public class RxCloneServiceImpl extends RemoteServiceServlet implements
        RxCloneService {

    public List getProviders() {
        RxPrescriptionMgr mgr = getRxPrescriptionMgr();
        return (List) mgr.getProviders();
    }

    public List<String[]> getFavoritesName(String provider_no) {
        RxPrescriptionMgr mgr = getRxPrescriptionMgr();
        return (List<String[]>) mgr.getFavoritesName(mgr.getFavoritesFromProvider(provider_no));
    }

    public void cloneFavorites(String provider_no, List<Integer> ids) {
        RxPrescriptionMgr mgr = getRxPrescriptionMgr();
        mgr.setFavoritesForProvider(provider_no, ids);
    }

    public String getCurrentProviderNo() {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) session.getAttribute("RxSessionBean");
        return bean.getProviderNo();
    }

    public void setFavoritesPrivilege(String providerNo, boolean openpublic, boolean writeable) {
        RxPrescriptionMgr mgr = getRxPrescriptionMgr();
        mgr.setFavoritesPrivilege(providerNo, openpublic, writeable);
    }
    
    public boolean[] getFavoritesPrivilege(String providerNo) {
        RxPrescriptionMgr mgr = getRxPrescriptionMgr();
        return mgr.getFavoritesPrivilege(providerNo);
    }

    public WebApplicationContext getApptContext() {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }

    public RxPrescriptionMgr getRxPrescriptionMgr() {
        return (RxPrescriptionMgr) getApptContext().getBean("RxPrescriptionMgr");
    }

}
