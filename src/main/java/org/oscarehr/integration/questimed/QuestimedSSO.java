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
package org.oscarehr.integration.questimed;

import java.util.Calendar;
import java.util.Date;
import org.oscarehr.util.MiscUtils;

import net.sf.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class QuestimedSSO {

    private static String serviceUserName = null;
    private static String servicePassword = null;
    private static String serviceLocation = null;
    private static String serviceToken = null;
    private static Date expiredDate = null;
    public static final int TOKEN_EXPIRED = 1;

    public static String getLaunchURL(String serviceUserName, String servicePassword, String serviceLocation, String userName, String demographicNo, String providerNo) {
        String url = null;
        String responseBody;
        Date now = new Date();
        Integer errorCode;
        try {
            if (isCredentialsChangedUpdateIfNecessary(serviceUserName, servicePassword, serviceLocation) || serviceToken == null || expiredDate == null || expiredDate.before(now)) {
                login(false);
            }

            responseBody = launchURL(userName, demographicNo, providerNo);
            JSONObject responseJson = JSONObject.fromObject(responseBody);
            errorCode = responseJson.getInt("errorCode");

            if (errorCode == TOKEN_EXPIRED) {
                login(false);
                responseBody = launchURL(userName,  demographicNo, providerNo);
                responseJson = JSONObject.fromObject(responseBody);
            }

            url = responseJson.getString("url");
        } catch (Exception e) {
            MiscUtils.getLogger().error("getLaunchURL Error!", e);
        }

        return url;
    }

     public static String createAccount(String serviceUserName, String servicePassword, String serviceLocation, String userName, String patientHIN, String demographicNo, String providerNo, String patientFirstName, String patientLastName, String patientDOB, String patientSex, String patientEmail) {
        String errorMsg = null;
        String responseBody;
        Date now = new Date();
        Integer errorCode;
        try {
            if (isCredentialsChangedUpdateIfNecessary(serviceUserName, servicePassword, serviceLocation) || serviceToken == null || expiredDate == null || expiredDate.before(now)) {
                login(false);
            }

            responseBody = createAccountREST(userName, patientHIN, demographicNo, providerNo, patientFirstName, patientLastName, patientDOB, patientSex, patientEmail);
            JSONObject responseJson = JSONObject.fromObject(responseBody);
            errorCode = responseJson.getInt("errorCode");

            if (errorCode == TOKEN_EXPIRED) {
                login(false);
                responseBody = createAccountREST(userName, patientHIN, demographicNo, providerNo, patientFirstName, patientLastName, patientDOB, patientSex, patientEmail);
                responseJson = JSONObject.fromObject(responseBody);
            }

            errorMsg = responseJson.getString("errorMsg");
        } catch (Exception e) {
            MiscUtils.getLogger().error("getLaunchURL Error!", e);
        }

        return errorMsg;
    }
     
    public static String getSurveysBody(String serviceUserName, String servicePassword, String serviceLocation, String questimedProviderUserName, String demographicNo, String providerNo) {
        String responseBody = null;
        Date now = new Date();
        try {
            if (isCredentialsChangedUpdateIfNecessary(serviceUserName, servicePassword, serviceLocation) || serviceToken == null || expiredDate == null || expiredDate.before(now)) {
                login(false);
            }
            responseBody = getSurveys(questimedProviderUserName, demographicNo, providerNo);
            JSONObject responseJson = JSONObject.fromObject(responseBody);
            Integer errorCode = responseJson.getInt("errorCode");
            if (errorCode == TOKEN_EXPIRED) {

                login(false);
                responseBody = getSurveys(questimedProviderUserName,  demographicNo, providerNo);
            }
        } catch (Exception e) {
            MiscUtils.getLogger().error("getSurveysBody Error!", e);
        }
        return responseBody;
    }

    public static Boolean testServiceConnection(String serviceUserName, String servicePassword, String serviceLocation) {
        Boolean connectionSuccess = false;
        try {
            isCredentialsChangedUpdateIfNecessary(serviceUserName, servicePassword, serviceLocation);
            connectionSuccess = login(true);
        } catch (Exception e) {
            MiscUtils.getLogger().error("testServiceConnection Error!", e);
        }
        return connectionSuccess;
    }

    public static boolean login(Boolean testConnection) {
        RestTemplate restTemplate = new RestTemplate();
        Boolean succesFlag = false;
        String serviceURL = serviceLocation;
        if (!serviceURL.endsWith("/")) {
            serviceURL += "/";
        }
        serviceURL += "login";
        try {
            JSONObject request = new JSONObject();
            request.put("email", serviceUserName);
            request.put("password", servicePassword);
            request.put("testConnection", testConnection);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(serviceURL, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject responseJson = JSONObject.fromObject(response.getBody());
                String errorMsg = responseJson.getString("errorMsg");
                if (testConnection == false) {
                    serviceToken = responseJson.getString("access_token");
                    Integer expires_in = responseJson.getInt("expires_in");
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.SECOND, expires_in);
                    expiredDate = now.getTime();
                }
                if (errorMsg.isEmpty()) {
                    succesFlag = true;
                }
            }
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return succesFlag;
    }

    private static String getSurveys(String questimedProviderUserName,String demographicNo, String providerNo) {
        RestTemplate restTemplate = new RestTemplate();
        String responseBody = "";
        String serviceURL = serviceLocation;
        if (!serviceURL.endsWith("/")) {
            serviceURL += "/";
        }
        serviceURL += "getSurveys";
        try {
            JSONObject request = new JSONObject();
            request.put("access_token", serviceToken);
            request.put("providerNo", providerNo);
            request.put("userName", questimedProviderUserName);
            request.put("demographicNo", demographicNo);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=utf-8");
            headers.add("Accept", "application/json; charset=utf-8");

            HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(serviceURL, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                responseBody = response.getBody();
            }
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return responseBody;
    }

    private static String launchURL(String userName, String demographicNo, String providerNo) {
        RestTemplate restTemplate = new RestTemplate();
        String responseBody = "";
        String serviceURL = serviceLocation;
        if (!serviceURL.endsWith("/")) {
            serviceURL += "/";
        }
        serviceURL += "getLaunchURL";
        try {
            JSONObject request = new JSONObject();
            request.put("access_token", serviceToken);
            request.put("providerNo", providerNo);
            request.put("userName", userName);
            request.put("demographicNo", demographicNo);


            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=utf-8");
            headers.add("Accept", "application/json; charset=utf-8");

            HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(serviceURL, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                responseBody = response.getBody();
            }
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return responseBody;
    }

      private static String createAccountREST(String userName, String patientHIN, String demographicNo, String providerNo, String patientFirstName, String patientLastName, String patientDOB, String patientSex, String patientEmail) {
        RestTemplate restTemplate = new RestTemplate();
        String responseBody = "";
        String serviceURL = serviceLocation;
        if (!serviceURL.endsWith("/")) {
            serviceURL += "/";
        }
        serviceURL += "createPatientAcccount";
        try {
            JSONObject request = new JSONObject();
            request.put("access_token", serviceToken);
            request.put("providerNo", providerNo);
            request.put("userName", userName);
            request.put("demographicNo", demographicNo);
            request.put("HIN", patientHIN);
            request.put("firstName", patientFirstName);
            request.put("lastName", patientLastName);
            request.put("DOB", patientDOB);
            request.put("sex", patientSex);
            request.put("email", patientEmail);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=utf-8");
            headers.add("Accept", "application/json; charset=utf-8");

            HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(serviceURL, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                responseBody = response.getBody();
            }
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return responseBody;
    }
      
    private static boolean isCredentialsChangedUpdateIfNecessary(String srvUserName, String srvPassword, String srvLocation) {
        boolean changed = false;
        if (!srvUserName.equals(serviceUserName)) {
            serviceUserName = srvUserName;
            changed = true;
        }
        if (!srvPassword.equals(servicePassword)) {
            servicePassword = srvPassword;
            changed = true;
        }
        if (!srvLocation.equals(serviceLocation)) {
            serviceLocation = srvLocation;
            changed = true;
        }
        return changed;
    }

}
