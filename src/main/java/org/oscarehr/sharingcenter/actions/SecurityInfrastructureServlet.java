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
package org.oscarehr.sharingcenter.actions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.marc.shic.core.CertificateIdentifier;
import org.marc.shic.core.exceptions.SslException;
import org.marc.shic.core.utils.SslUtility;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.sharingcenter.dao.InfrastructureDao;
import org.oscarehr.sharingcenter.model.InfrastructureDataObject;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class SecurityInfrastructureServlet extends Action {

    private static final Logger LOGGER = MiscUtils.getLogger();
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "r", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }
    	
        String status = "error";
        //if this is an update request from the chain import page
        if (request.getMethod().equalsIgnoreCase("post")) {
            status = performImport(new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request));
            response.sendRedirect(request.getContextPath() + status);
        } else if (request.getParameter("id") != null && !request.getParameter("id").equals("")) {
            try {
                Integer id = Integer.parseInt(request.getParameter("id"));
                if (request.getParameter("action") != null && request.getParameter("action").equals("delete")) {
                    //delete
                    status = deleteInfrastructure(id);
                    response.sendRedirect(request.getContextPath() + "/sharingcenter/security/infrastructure.jsp?" + status);
                } else if (request.getParameter("action") != null && request.getParameter("action").equals("update")) {
                    //update
                    status = updateInfrastructure(id, request.getParameter("form_alias"), request.getParameter("form_commonName"), request.getParameter("form_organizationalUnit"), request.getParameter("form_organization"), request.getParameter("form_locality"), request.getParameter("form_state"), request.getParameter("form_country"));
                    response.sendRedirect(request.getContextPath() + "/sharingcenter/security/import.jsp?id=" + id + "&" + status);
                } else if (request.getParameter("action") != null && request.getParameter("action").equals("csr")) {
                    //CSR generation
                    generateCertificateSigningRequest(id, response);
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/sharingcenter/security/import.jsp?id=" + request.getParameter("id") + "&" + status);
            }
        } else {
            status = this.createNewInfrastructure(request.getParameter("form_alias"), request.getParameter("form_commonName"), request.getParameter("form_organizationalUnit"), request.getParameter("form_organization"), request.getParameter("form_locality"), request.getParameter("form_state"), request.getParameter("form_country"));
            if (status.equals("exists")) {
                response.sendRedirect(request.getContextPath() + "/sharingcenter/security/new.jsp?" + status);
            } else {
                response.sendRedirect(request.getContextPath() + "/sharingcenter/security/infrastructure.jsp?" + status);
            }
        }

        return null;
    }

    private String performImport(List<FileItem> files)  {
        //import certificate
        String status = "";
        InputStream filecontent = null;
        Integer id = null;

        try {
            for (FileItem item : files) {
                if (!item.isFormField()) {
                    // Process form file field (input type="file").
                    filecontent = item.getInputStream();
                } else {
                    // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                    String fieldname = item.getFieldName();
                    String fieldvalue = item.getString();

                    if (fieldname.equals("id")) {
                        id = Integer.parseInt(fieldvalue);
                    }
                }
            }
            if (id != null && filecontent != null) {
                status = importCertificates(id, filecontent);
            }
        } catch (Exception ex) {
            LOGGER.info(ex);
        }

        return "/sharingcenter/security/import.jsp?id=" + id + "&" + status;
    }

    private String createNewInfrastructure(String alias, String commonName, String organizationalUnit, String organization, String locality, String state, String country) {

        InfrastructureDao dao = SpringUtils.getBean(InfrastructureDao.class);

        if (alias == null) {
            return "error";
        }

        if (dao.aliasExists(alias)) {
            return "exists";
        } else {
            InfrastructureDataObject infrastructure = new InfrastructureDataObject();
            infrastructure.setAlias(alias);
            infrastructure.setCommonName(commonName);
            infrastructure.setOrganizationalUnit(organizationalUnit);
            infrastructure.setOrganization(organization);
            infrastructure.setLocality(locality);
            infrastructure.setState(state);
            infrastructure.setCountry(country);

            //Generate, encode, and set the public and private keys
            try {
                KeyPair keyPair = SslUtility.generateKeyPair();
                PublicKey publicKey = keyPair.getPublic();
                PrivateKey privateKey = keyPair.getPrivate();

                Base64 base64 = new Base64();
                String encodedPubKey = new String(base64.encode(publicKey.getEncoded()), MiscUtils.DEFAULT_UTF8_ENCODING);
                String encodedPrivKey = new String(base64.encode(privateKey.getEncoded()), MiscUtils.DEFAULT_UTF8_ENCODING);

                infrastructure.setBase64EncodedPublicKey(encodedPubKey);
                infrastructure.setBase64EncodedPrivateKey(encodedPrivKey);

                dao.persist(infrastructure);

            } catch (SslException e) {
                return "sslerror";
            } catch (UnsupportedEncodingException e) {
                return "encodingerror";
            }
        }

        return "success";

    }

    private String updateInfrastructure(Integer id, String alias, String commonName, String organizationalUnit, String organization, String locality, String state, String country) {

        InfrastructureDao dao = SpringUtils.getBean(InfrastructureDao.class);
        InfrastructureDataObject infrastructure = dao.getInfrastructure(id);
        infrastructure.setAlias(alias);
        infrastructure.setCommonName(commonName);
        infrastructure.setOrganizationalUnit(organizationalUnit);
        infrastructure.setOrganization(organization);
        infrastructure.setLocality(locality);
        infrastructure.setState(state);
        infrastructure.setCountry(country);

        dao.merge(infrastructure);

        return "update";

    }

    private String deleteInfrastructure(Integer id) {

        InfrastructureDao dao = SpringUtils.getBean(InfrastructureDao.class);
        InfrastructureDataObject toDelete = dao.getInfrastructure(id);

        // there is a problem if the alias is null..
        if (toDelete.getAlias() == null) {
            dao.remove(id);
            return "delete";
        }

        //Preparing for the KeyStore containsAlias() test
        OscarProperties oscarProperties = OscarProperties.getInstance();
        String keyStoreFile = oscarProperties.getProperty("TOMCAT_KEYSTORE_FILE");
        String trustStoreFile = oscarProperties.getProperty("TOMCAT_TRUSTSTORE_FILE");
        String keyStorePass = oscarProperties.getProperty("TOMCAT_KEYSTORE_PASSWORD");
        String trustStorePass = oscarProperties.getProperty("TOMCAT_TRUSTSTORE_PASSWORD");

        String alias = toDelete.getAlias();

        KeyStore ks = null;
        KeyStore ts = null;

        try {

            ks = SslUtility.loadKeyStore(keyStoreFile, keyStorePass.toCharArray());
            ts = SslUtility.loadKeyStore(trustStoreFile, trustStorePass.toCharArray());

            if (ks.containsAlias(alias)) {
                ks.deleteEntry(alias);
                ts.deleteEntry(alias);
            }

            // save the keystore
            ks.store(new FileOutputStream(keyStoreFile), keyStorePass.toCharArray());
            // save the truststore
            ts.store(new FileOutputStream(trustStoreFile), trustStorePass.toCharArray());

        } catch (SslException ex) {
            LOGGER.info(ex);
        } catch (KeyStoreException ex) {
            LOGGER.info(ex);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.info(ex);
        } catch (CertificateException ex) {
            LOGGER.info(ex);
        } catch (FileNotFoundException ex) {
            LOGGER.info(ex);
        } catch (IOException ex) {
            LOGGER.info(ex);
        }

        dao.remove(id);

        return "delete";

    }

    private void generateCertificateSigningRequest(Integer infrastructureId, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();

        try {

            InfrastructureDao dao = SpringUtils.getBean(InfrastructureDao.class);
            InfrastructureDataObject infrastructure = dao.getInfrastructure(infrastructureId);

            String CN = infrastructure.getCommonName();
            String OU = infrastructure.getOrganizationalUnit();
            String O = infrastructure.getOrganization();
            String L = infrastructure.getLocality();
            String S = infrastructure.getState();
            String C = infrastructure.getCountry();

            Base64 base64 = new Base64();
            byte[] pubKey = base64.decode(infrastructure.getBase64EncodedPublicKey());
            byte[] privKey = base64.decode(infrastructure.getBase64EncodedPrivateKey());

            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKey));
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privKey));
            
            CertificateIdentifier principal = new CertificateIdentifier(CN, OU, O, L, S, C);
            PKCS10CertificationRequest request = SslUtility.generatePKCS10(publicKey, privateKey, principal);
            String csr = SslUtility.generateCSR(request);
            out.println(csr);
        } catch (NumberFormatException e) {
            out.println(e);
        } catch (InvalidKeySpecException e) {
            out.println(e);
        } catch (NoSuchAlgorithmException e) {
            out.println(e);
        } catch (SslException e) {
            out.println(e);
        }
    }

    private String importCertificates(Integer infrastructureId, InputStream inputStream) {

        String status = "fail";
        OscarProperties oscarProperties = OscarProperties.getInstance();
        String keyStoreFile = oscarProperties.getProperty("TOMCAT_KEYSTORE_FILE");
        String trustStoreFile = oscarProperties.getProperty("TOMCAT_TRUSTSTORE_FILE");
        String keyStorePass = oscarProperties.getProperty("TOMCAT_KEYSTORE_PASSWORD");
        String trustStorePass = oscarProperties.getProperty("TOMCAT_TRUSTSTORE_PASSWORD");

        InfrastructureDao dao = SpringUtils.getBean(InfrastructureDao.class);
        InfrastructureDataObject infrastructure = dao.getInfrastructure(infrastructureId);

        String alias = infrastructure.getAlias();
        PrivateKey privateKey = null;
        KeyStore ks = null;
        KeyStore ts = null;

        try {
            //acquiring the private key
            Base64 base64 = new Base64();
            byte[] privKey = base64.decode(infrastructure.getBase64EncodedPrivateKey());
            privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privKey));

            ks = SslUtility.loadKeyStore(keyStoreFile, keyStorePass.toCharArray());
            ts = SslUtility.loadKeyStore(trustStoreFile, trustStorePass.toCharArray());

        } catch (SslException ex) {
            LOGGER.info(ex);
        } catch (InvalidKeySpecException ex) {
            LOGGER.info(ex);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.info(ex);
        }

        if (ks != null && ts != null && privateKey != null) {
            // import certificates to keystore and truststore
            try {
                // extract certificates
                ArrayList<X509Certificate> certificates = SslUtility.extractX509Certificates(inputStream);
                // get the private key and add certificate chain
                X509Certificate[] chain = new X509Certificate[2];
                ks.setKeyEntry(alias, privateKey, keyStorePass.toCharArray(), certificates.toArray(chain));

                // save the keystore
                ks.store(new FileOutputStream(keyStoreFile), keyStorePass.toCharArray());

                // add root CA certificate truststore
                ArrayList<X509Certificate> caCerts = SslUtility.retrieveCACertificates(certificates);
                for (X509Certificate x509Certificate : caCerts) {
                    ts.setCertificateEntry(alias, x509Certificate);
                }

                // save the truststore
                ts.store(new FileOutputStream(trustStoreFile), trustStorePass.toCharArray());
                status = "import";
            } catch (NoSuchAlgorithmException ex) {
                LOGGER.info(ex);
            } catch (CertificateException ex) {
                LOGGER.info(ex);
            } catch (KeyStoreException ex) {
                LOGGER.info(ex);
            } catch (IOException ex) {
                LOGGER.info(ex);
            } catch (SslException ex) {
                LOGGER.info(ex);
            }
        } else {
            LOGGER.debug("Bad data. Keystore/Truststore/PrivateKey might be null");
        }

        return status;

    }
}
