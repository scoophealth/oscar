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

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;


/**
 * Retrieves the eConsults for the demographic and displays them in the eChart
 */
public class EctDisplayEconsultAction extends EctDisplayAction {
	
    private String cmd = "eConsult";
	private final OscarProperties oscarProperties = OscarProperties.getInstance();
	private final String backendEconsultUrl = oscarProperties.getProperty("backendEconsultUrl");

    /**
     * Generates the eConsult module in the eChart, making it so that when the user clicks on the title it takes them to the demographic's eConsults, when they click the
     * + in the header then it opens a new eConsult, and listing out the eConsults in the eChart
     *
     * @param bean
     * 		Current session information
     * @param request
     * 		Current request
     * @param Dao
     * 		View DAO responsible for rendering encounter
     * @param messages
     * 		i18n message bundle
     * @return Always returns a true boolean
     */
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
        Logger logger = MiscUtils.getLogger();
        
    	// hide the econsult option if it is not available. 
    	if(backendEconsultUrl == null || backendEconsultUrl.equals("")) {
            return true;
        } else {
        	
            HttpSession session = request.getSession();
        	String oneIdEmail = session.getAttribute("oneIdEmail") != null ? session.getAttribute("oneIdEmail").toString() : "";
        	String demographicNo = bean.getDemographicNo();
            String winName = "eConsult";
            
            //set lefthand module heading and link
            Dao.setLeftHeading("eConsult");
            StringBuilder eConsultDisplayUrl = new StringBuilder(String.format("..%1$s%2$s", File.separator, "econsult.do"));
            eConsultDisplayUrl.append(String.format("?%1$s=%2$s", "demographicNo", demographicNo));
            eConsultDisplayUrl.append(String.format("&%1$s=%2$s", "method", "frontend"));
            StringBuilder viewConsultUrl = new StringBuilder(eConsultDisplayUrl.toString());
            StringBuilder createNewEconsultUrl = new StringBuilder(eConsultDisplayUrl.toString());
            eConsultDisplayUrl.append(String.format("&%1$s=%2$s", "task", "patientSummary"));
            
            String url = "popupPage(700,960,'" + winName + "','" + eConsultDisplayUrl + "'); return false;";

            Dao.setLeftURL(url);

            //set the right hand heading link
            winName = "new eConsult" + demographicNo;
            createNewEconsultUrl.append(String.format("&%1$s=%2$s", "task", "draft"));
            
            url = "popupPage(700,960,'" + winName + "','" + createNewEconsultUrl + "'); return false;";

            Dao.setRightURL(url);
            Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action

            try {
 
                String delegateOneIdEmail = (String) session.getAttribute("delegateOneIdEmail");
	            StringBuilder backendEconsultRequest = new StringBuilder(backendEconsultUrl);
	    		
	            if(! backendEconsultUrl.endsWith(File.separator)) {
	            	backendEconsultRequest.append(File.separator);
	    		}
	            
	            backendEconsultRequest.append(String.format("?%1$s=%2$s", "oneid_email", URLEncoder.encode(oneIdEmail, StandardCharsets.UTF_8.toString())));
   
	        	if(delegateOneIdEmail != null && ! delegateOneIdEmail.isEmpty()) {
	        		backendEconsultRequest.append(String.format("&%1$s=%2$s", "delegate_oneid_email", URLEncoder.encode(delegateOneIdEmail, StandardCharsets.UTF_8.toString())));
        		}

        		backendEconsultRequest.append(String.format("#!%s", File.separator));	        		
        		backendEconsultRequest.append(String.format("consultSummary?"));
        		backendEconsultRequest.append(String.format("%1$s=%2$s", "patient_id", demographicNo));
        		backendEconsultRequest.append(String.format("&%1$s=%2$s", "count", 25));
        		backendEconsultRequest.append(String.format("&%1$s=%2$s", "offset", 0));

        		//Creates an HttpGet with the url to get eConsults and sets a header for the oneIdEmail
                HttpGet httpGet = new HttpGet(backendEconsultRequest.toString());
                String oneIdToken = (String)session.getAttribute("oneid_token");
                
                httpGet.addHeader("x-oneid-email", URLEncoder.encode(oneIdEmail, StandardCharsets.UTF_8.toString()));
                httpGet.addHeader("x-access-token", oneIdToken);
                
                //Gets an HttpClient that will ignore SSL validation
                HttpClient httpClient = getHttpClient2();
                //Executes the GET request and stores the response
                HttpResponse httpResponse = httpClient.execute(httpGet);
                //Gets the entity from the response and stores it as a JSONObject
                String entity = EntityUtils.toString(httpResponse.getEntity());
                
                // Exception here if the entity string is not JSON. 
                JSONObject jsonObject = new JSONObject(entity);

                //Creates the onClick string for each eConsult that will be listed
                viewConsultUrl.append(String.format("&%1$s=%2$s", "task", "econsult"));
 
                //Gets the data and then entry sections of the response entity
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray entryArray = data.getJSONArray("entry");
                //Creates a SimpleDateFormat for parsing the dat
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                //Gets the length of the entryArray
                Integer entryLength = entryArray.length();

                //Loops for the length of the array to output each eConsult
                for (Integer i = 0; i < entryLength; i++) {
                    //Gets the JSONObject from the entryArray and then the resource object from the entry
                    JSONObject entry = (JSONObject)entryArray.get(i);
                    JSONObject resource = entry.getJSONObject("resource");
                    //Gets the eConsultId
                    String eConsultId = resource.getString("id");
                    //Gets the last update date
                    String date = resource.getString("date");
                    //Gets the eConsult title
                    String title = resource.getString("description");
                    String status = resource.getString("status");

                    //Creates a new item to populate and display the eConsult
                    NavBarDisplayDAO.Item item = new NavBarDisplayDAO.Item();
                    //Sets the item's title
                    item.setTitle(title + "(" + status + ")");
                    //Formats the url string with the oneIdEmail and eConsultId and sets the eConsult's URL                    
                    StringBuilder stringBuilder = new StringBuilder("popupPage(700,960, 'eConsult', '");
                    viewConsultUrl.append(String.format("&%1$s=%2$s", "itemId", eConsultId));
                    stringBuilder.append(viewConsultUrl.toString());
                    stringBuilder.append("'); return false;");
                    item.setURL(stringBuilder.toString());
                    try {
                        //Parses and sets the eConsult's date
                    	item.setDate(dateFormatter.parse(date));   
                    }
                    catch (ParseException e) {
                        logger.error("Could not parse the date for eConsult " + eConsultId, e);
                    }

                    //Adds the eConsult to the Dao
                    Dao.addItem(item);
                }
            }
            catch (IOException e) {
                logger.error("Failed to retrieve eConsults for the OneID account " + oneIdEmail, e);
            }
            catch (NoSuchAlgorithmException e) {
                logger.error("Failed to create an HttpClient that allows all SSL", e);
            }
            catch (KeyManagementException e) {
            	 logger.error("Failed to create an HttpClient that allows all SSL", e);
            }
            catch (JSONException e) {
                logger.error("Failed to convert the response entity to a JSON Object", e);
            } 

            return true;
        }
    }

    public String getCmd() {
         return cmd;
    }

    protected HttpClient getHttpClient2() throws KeyManagementException, NoSuchAlgorithmException {

        //setup SSL
        SSLContext sslcontext = SSLContexts.custom().useTLS().build();
        sslcontext.getDefaultSSLParameters().setNeedClientAuth(true);
        sslcontext.getDefaultSSLParameters().setWantClientAuth(true);
        SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslcontext);

        //setup timeouts
        int timeout = Integer.parseInt(OscarProperties.getInstance().getProperty("dhir.timeout", "60"));
        RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout * 1000).setConnectTimeout(timeout * 1000).build();

        CloseableHttpClient httpclient3 = HttpClients.custom().setDefaultRequestConfig(config).setSSLSocketFactory(sf).build();

        return httpclient3;

    }
}
