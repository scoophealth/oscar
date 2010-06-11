/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 *
 */
package org.oscarehr.clinicalconnect.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;

import org.oscarehr.clinicalconnect.ws.EmrDownloadService;
import org.oscarehr.clinicalconnect.ws.GeneratedFile;
import org.oscarehr.clinicalconnect.ws.GeneratedFileInfo;

public class EmrDownloadEngine {

    private boolean enabled;
    private String serviceUserName;
    private String servicePassword;
    private EmrDownloadService service;
    private String serviceLocation;

    public EmrDownloadEngine() {
        super();
        enabled = true;
    }

    public void init() {
        createClient();
    }

    public void createClient() {
        if (StringUtils.isBlank(serviceLocation)) {
            throw new IllegalArgumentException("serviceLocation is required!");
        }
        if (StringUtils.isBlank(serviceUserName)) {
            throw new IllegalArgumentException("serviceUserName is required!");
        }
        if (StringUtils.isBlank(servicePassword)) {
            throw new IllegalArgumentException("servicePassword is required!");
        }

        try {

            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

            factory.setServiceClass(EmrDownloadService.class);
            factory.setAddress(serviceLocation);

            // WS-SECURITY stuff follows
            Map<String, Object> outProps = new HashMap<String, Object>();
            outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN + " " + WSHandlerConstants.TIMESTAMP);
            // Specify our username
            outProps.put(WSHandlerConstants.USER, serviceUserName);
            // Password type : plain text
            outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
            // set our password callback class this is used to lookup the password for a user.
            outProps.put(WSHandlerConstants.PW_CALLBACK_REF, new ClientPasswordCallback(servicePassword));
            WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
            factory.getOutInterceptors().add(wssOut);

            service = (EmrDownloadService) factory.create();

        } catch (Exception e) {
            e.printStackTrace();
            service = null;
            disable();
        }
    }

    public List<GeneratedFileInfo> listFiles(String group, String password) {
        return service.listFiles(group, password);
    }

    public void deleteFile(String group, String password, Long fileId) {
        service.deleteFile(group, password, fileId);
    }

    public Long generateFile(String group, String password, String comment, String transactionId) {
        return service.generateFile(group, password, comment, transactionId);
    }

    public GeneratedFile downloadFile(String group, String password, Long fileId) {
        return service.downloadFile(group, password, fileId);
    }

    public void disable() {
        enabled = false;
    }

    public void enable() {
        enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean inEnabled) {
        enabled = inEnabled;
    }

    public String getServiceUserName() {
        return serviceUserName;
    }

    public void setServiceUserName(String serviceUserName) {
        this.serviceUserName = serviceUserName;
    }

    public String getServicePassword() {
        return servicePassword;
    }

    public void setServicePassword(String servicePassword) {
        this.servicePassword = servicePassword;
    }

    public String getServiceLocation() {
        return serviceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }
}

