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
package oscar.oscarEncounter.pageUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.util.MessageResources;
import org.oscarehr.util.MiscUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.util.QuestimedUtil;
import oscar.OscarProperties;
import static oscar.oscarEncounter.pageUtil.EctDisplayAction.MAX_LEN_TITLE;
import oscar.util.StringUtils;

public class EctDisplayQuestimedAction extends EctDisplayAction {

    private static final String cmd = "Questimed";
    private static Logger logger = MiscUtils.getLogger();

    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String defaultURL = "../questimed/launch.jsp?demographic_no=" + bean.demographicNo;
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_demographic", "r", null)) {
            return true;
        } else if (!(OscarProperties.getInstance().getBooleanProperty("questimed.enabled", "true") && QuestimedUtil.isServiceConnectionReady())) {
            return true;
        } else {
            try {

                //set lefthand module heading and link
                String winName = "Questimed";

                String responseBody = QuestimedUtil.getSurveysBody(loggedInInfo, bean.demographicNo);
                JSONObject responseJson = JSONObject.fromObject(responseBody);
                String mainUrl = responseJson.getString("mainURL");
                String newSurveyUrl = responseJson.getString("newSurveyURL");

                JSONArray itemList = JSONArray.fromObject(responseJson.getString("itemList"));
                JSONArray dateList = JSONArray.fromObject(responseJson.getString("dateList"));
                JSONArray urlList = JSONArray.fromObject(responseJson.getString("urlList"));

                if (mainUrl.isEmpty()) {
                    mainUrl = defaultURL;
                }
                       
                if (newSurveyUrl.isEmpty()) {
                    newSurveyUrl = defaultURL;
                }

                String url = "popupPage(700,800,'" + winName + "', '" + mainUrl + "')";

                Dao.setLeftHeading("Questimed");

                Dao.setLeftURL(url);

                //set the right hand heading link 
                url = "popupPage(700,800,'" + winName + "','" + newSurveyUrl + "');return false;";

                Dao.setRightURL(url);
                Dao.setRightHeadingID(cmd); //no menu so set div id to unique id for this action

                String msgSubject;
                String dbFormat = "yyyy-MM-dd";

                Date date;
                for (int i = 0; i < itemList.size(); i++) {
                    msgSubject = StringUtils.maxLenString((String) itemList.get(i), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);

                    NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
                    if (!dateList.getString(i).isEmpty()) {
                        try {
                            DateFormat formatter = new SimpleDateFormat(dbFormat);
                            String a = dateList.getString(i);
                            date = formatter.parse(dateList.getString(i));
                        } catch (ParseException e) {
                            MiscUtils.getLogger().debug("EctDisplayQuestimedAction: Error creating date " + e.getMessage());
                            date = null;
                        }
                    } else {
                        date = null;
                    }

                    item.setDate(date);
                    if (urlList.getString(i).isEmpty()) {
                        urlList.set(i, defaultURL);
                    }
                    url = "popupPage(700,800,'" + winName + "','" + urlList.getString(i) + "');return false;";

                    item.setURL(url);
                    item.setLinkTitle((String) itemList.get(i));
                    item.setTitle(msgSubject + " " + dateList.getString(i));
                    Dao.addItem(item);
                }

            } catch (Exception e) {
                MiscUtils.getLogger().error("Error", e);
            }
            return true;
        }
    }

    public String getCmd() {
        return cmd;
    }
}
