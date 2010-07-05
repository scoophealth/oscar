/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.oscarMDS.pageUtil;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.oscarLab.ca.on.CommonLabResultData;
/**
 *
 * @author jackson
 */
public class SendMostResponProvAction extends Action{
        private DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
        public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

                String demoId=request.getParameter("demoId");
                String docLabId=request.getParameter("docLabId");
                String docLabType=request.getParameter("docLabType");
                //MiscUtils.getLogger().info(demoId+"--"+docLabId+"--"+docLabType);
                ArrayList listFlaggedLabs = new ArrayList();
                if(demoId!=null && docLabId!=null && docLabType!=null){
                    demoId=demoId.trim();
                    Demographic demog=demographicDao.getDemographicById(Integer.parseInt(demoId));
                    String mrp=demog.getProviderNo();
                    //MiscUtils.getLogger().info(mrp);
                     String[] la =  new String[] {docLabId,docLabType};
                      listFlaggedLabs.add(la);
                      CommonLabResultData.updateLabRouting(listFlaggedLabs, mrp);
                }else{
                    //return error in json
                }
            return null;
        }
}
