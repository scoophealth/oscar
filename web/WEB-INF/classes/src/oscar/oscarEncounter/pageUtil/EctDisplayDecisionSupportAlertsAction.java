/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.oscarEncounter.pageUtil;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.MessageResources;
import org.oscarehr.decisionSupport.model.DSConsequence;
import org.oscarehr.decisionSupport.model.DSDemographicAccess;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.service.DSService;
import org.oscarehr.decisionSupport.web.DSGuidelineAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

/**
 *
 * @author apavel
 */
public class EctDisplayDecisionSupportAlertsAction extends EctDisplayAction {
    private String cmd = "Guidelines";
    private static Log _log = LogFactory.getLog(DSGuidelineAction.class);

  public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {

	  boolean a = true;
      Vector v = OscarRoleObjectPrivilege.getPrivilegeProp("_newCasemgmt.decisionSupportAlerts");
      String roleName = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
      a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1));
      if(!a) {
           return true; //decisionSupportAlerts link won't show up on new CME screen.
      } else {    	
  	
        //set lefthand module heading and link
        String winName = "dsalert" + bean.demographicNo;
        String url = "popupPage(500,950,'" + winName + "','" + request.getContextPath() + "/oscarEncounter/decisionSupport/guidelineAction.do?method=list&provider_no=" + bean.providerNo + "&demographic_no=" + bean.demographicNo + "&parentAjaxId=" + cmd + "'); return false;";
        Dao.setLeftHeading(messages.getMessage(request.getLocale(), "global.decisionSupportAlerts"));
        Dao.setLeftURL(url);

        //set the right hand heading link
        winName = "AddeForm" + bean.demographicNo;
        
        Dao.setRightURL(url);
        Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action

        StringBuffer javascript = new StringBuffer("<script type=\"text/javascript\">");
        String js = "";

        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
        DSService  dsService =  (DSService) ctx.getBean("dsService");

        List<DSGuideline> dsGuidelines = dsService.getDsGuidelinesByProvider(bean.providerNo);

        String key;
        int hash;
        String BGCOLOUR = request.getParameter("hC");
        /*
        for( int i = 0; i < dsConsequences.size(); ++i ) {
            DSConsequence dsConsequence = (DSConsequence) dsConsequences.get(i);
            if (dsConsequence.getConsequenceType() == DSConsequence.ConsequenceType.warning) {
                winName = (String) dsConsequence.getConsequenceType().toString() + bean.demographicNo;
                hash = Math.abs(winName.hashCode());
                url = "popupPage( 700, 800, '" + hash + "', '" + request.getContextPath() + "/eform/efmformadd_data.jsp?fid=&demographic_no=" + bean.demographicNo + "&parentAjaxId=" + cmd + "', 'FormA" + i + "');";
                key = StringUtils.maxLenString(dsConsequence.getText(), MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES);
                key = StringEscapeUtils.escapeJavaScript(key);
            
                js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompleted['" + key + "'] = \"" + url + "\"; autoCompList.push('" + key + "');";
                javascript.append(js);
            }
        }*/

        int index = 0;
        for(DSGuideline dsGuideline: dsGuidelines) {
            try {
                List<DSConsequence> dsConsequences = dsGuideline.evaluate(bean.demographicNo);
                if (dsConsequences == null) continue;
                for (DSConsequence dsConsequence: dsConsequences) {
                    if (dsConsequence.getConsequenceType() != DSConsequence.ConsequenceType.warning)
                        continue;
                    index++;
                    NavBarDisplayDAO.Item item = Dao.Item();
                    winName = (String)dsConsequence.getConsequenceType().toString() + bean.demographicNo;
                    hash = Math.abs(winName.hashCode());
                    url = "popupPage(500,950,'" + winName + "','" + request.getContextPath() + "/oscarEncounter/decisionSupport/guidelineAction.do?method=detail&guidelineId=" + dsGuideline.getId() + "&provider_no=" + bean.providerNo + "&demographic_no=" + bean.demographicNo + "&parentAjaxId=" + cmd + "'); return false;";
                    //Date date = (Date)curform.get("formDateAsDate");
                    //String formattedDate = DateUtils.getDate(date,dateFormat,request.getLocale());
                    key = StringUtils.maxLenString((String)dsConsequence.getText(), MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES);
                    item.setLinkTitle(dsConsequence.getText());
                    key = StringEscapeUtils.escapeJavaScript(key);
                    js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompleted['" + key + "'] = \"" + url + "\"; autoCompList.push('" + key + "');";
                    javascript.append(js);
                    url += "return false;";
                    item.setURL(url);
                    String strTitle = StringUtils.maxLenString(dsConsequence.getText(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
                    item.setTitle(strTitle);
                    if (dsConsequence.getConsequenceStrength() == DSConsequence.ConsequenceStrength.warning) {
                        item.setColour("#ff5409;");
                    }
                    //item.setDate(new Date());
                    Dao.addItem(item);
                }
            } catch (Exception e) {
                _log.error("Unable to evaluate patient against a DS guideline '" + dsGuideline.getTitle() + "' of UUID '" + dsGuideline.getUuid() + "'", e);
            }
        }

        javascript.append("</script>");
        Dao.setJavaScript(javascript.toString());
        return true;
      }
  }

  public String getCmd() {
        return cmd;
  }
}
