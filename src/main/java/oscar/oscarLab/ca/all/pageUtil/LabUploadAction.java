/*
 * LabUploadAction.java
 *
 * Created on June 12, 2007, 2:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.pageUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.oscarehr.common.dao.OscarKeyDao;
import org.oscarehr.common.dao.PublicKeyDao;
import org.oscarehr.common.model.OscarKey;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.FileUploadCheck;
import oscar.oscarLab.ca.all.upload.HandlerClassFactory;
import oscar.oscarLab.ca.all.upload.handlers.MessageHandler;
import oscar.oscarLab.ca.all.util.Utilities;

public class LabUploadAction extends Action {
	protected static Logger logger = Logger.getLogger(LabUploadAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LabUploadForm frm = (LabUploadForm) form;
		FormFile importFile = frm.getImportFile();

		String signature = request.getParameter("signature");
		String key = request.getParameter("key");
		String service = request.getParameter("service");
		String outcome = "";
		String audit = "";
		Integer httpCode = 200;

		@SuppressWarnings("unchecked")
		ArrayList clientInfo = getClientInfo(service);
		PublicKey clientKey = (PublicKey) clientInfo.get(0);
		String type = (String) clientInfo.get(1);

		try {

			InputStream is = decryptMessage(importFile.getInputStream(), key, clientKey);
			String fileName = importFile.getFileName();
			String filePath = Utilities.saveFile(is, fileName);
			importFile.getInputStream().close();
			File file = new File(filePath);

			if (validateSignature(clientKey, signature, file)) {
				logger.debug("Validated Successfully");
				MessageHandler msgHandler = HandlerClassFactory.getHandler(type);

				is = new FileInputStream(file);
				int check = FileUploadCheck.addFile(file.getName(), is, "0");
				if (check != FileUploadCheck.UNSUCCESSFUL_SAVE) {
					if ((audit = msgHandler.parse(service, filePath, check)) != null) {
						outcome = "uploaded";
						httpCode = HttpServletResponse.SC_OK;
					} else {
						outcome = "upload failed";
						httpCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
					}
				} else {
					outcome = "uploaded previously";
					httpCode = HttpServletResponse.SC_CONFLICT;
				}
				is.close();
			} else {
				logger.info("failed to validate");
				outcome = "validation failed";
				httpCode = HttpServletResponse.SC_NOT_ACCEPTABLE;
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
			outcome = "exception";
			httpCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		request.setAttribute("outcome", outcome);
		request.setAttribute("audit", audit);

		if (request.getParameter("use_http_response_code") != null) {
			try {
	            response.sendError(httpCode, outcome);
            } catch (IOException e) {
	            logger.error("Error", e);
            }
			return (null);
		} else return mapping.findForward("success");
	}

	public LabUploadAction() {
	}

	/*
	 * Decrypt the encrypted message and return the original version of the message as an InputStream
	 */
	public static InputStream decryptMessage(InputStream is, String skey, PublicKey pkey) {

		Base64 base64 = new Base64();

		// Decrypt the secret key and the message
		try {

			// retrieve the servers private key
			PrivateKey key = getServerPrivate();

			// Decrypt the secret key using the servers private key
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] newSecretKey = cipher.doFinal(base64.decode(skey.getBytes(MiscUtils.ENCODING)));

			// Decrypt the message using the secret key
			SecretKeySpec skeySpec = new SecretKeySpec(newSecretKey, "AES");
			Cipher msgCipher = Cipher.getInstance("AES");
			msgCipher.init(Cipher.DECRYPT_MODE, skeySpec);

			is = new CipherInputStream(is, msgCipher);

			// Return the decrypted message
			return (new BufferedInputStream(is));

		} catch (Exception e) {
			logger.error("Could not decrypt the message", e);
			return (null);
		}
	}

	/*
	 * Check that the signature 'sigString' matches the message InputStream 'msgIS' thus verifying that the message has not been altered.
	 */
	public static boolean validateSignature(PublicKey key, String sigString, File input) {
		Base64 base64 = new Base64();
		byte[] buf = new byte[1024];

		try {

			InputStream msgIs = new FileInputStream(input);
			Signature sig = Signature.getInstance("MD5WithRSA");
			sig.initVerify(key);

			// Read in the message bytes and update the signature
			int numRead = 0;
			while ((numRead = msgIs.read(buf)) >= 0) {
				sig.update(buf, 0, numRead);
			}
			msgIs.close();

			return (sig.verify(base64.decode(sigString.getBytes(MiscUtils.ENCODING))));

		} catch (Exception e) {
			logger.debug("Could not validate signature: " + e);
			MiscUtils.getLogger().error("Error", e);
			return (false);
		}
	}

	/*
	 * Retrieve the clients public key from the database
	 */
	public static ArrayList<Object> getClientInfo(String service) {

		PublicKey Key = null;
		Base64 base64 = new Base64();
		String keyString = "";
		String type = "";
		byte[] publicKey;
		ArrayList<Object> info = new ArrayList<Object>();

		try {
			PublicKeyDao publicKeyDao = (PublicKeyDao) SpringUtils.getBean("publicKeyDao");
			org.oscarehr.common.model.PublicKey publicKeyObject = publicKeyDao.find(service);

			if (publicKeyObject != null) {
				keyString = publicKeyObject.getBase64EncodedPublicKey();
				type = publicKeyObject.getType();
			}

			publicKey = base64.decode(keyString.getBytes(MiscUtils.ENCODING));
			;
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			Key = keyFactory.generatePublic(pubKeySpec);

			info.add(Key);
			info.add(type);

		} catch (Exception e) {
			logger.error("Could not retrieve private key: ", e);
		}
		return (info);
	}

	/*
	 * Retrieve the servers private key from the database
	 */
	private static PrivateKey getServerPrivate() {

		PrivateKey Key = null;
		Base64 base64 = new Base64();
		byte[] privateKey;

		try {
			OscarKeyDao oscarKeyDao = (OscarKeyDao) SpringUtils.getBean("oscarKeyDao");
			OscarKey oscarKey = oscarKeyDao.find("oscar");
			logger.info("oscar key: " + oscarKey);

			privateKey = base64.decode(oscarKey.getPrivateKey().getBytes(MiscUtils.ENCODING));
			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privateKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			Key = keyFactory.generatePrivate(privKeySpec);
		} catch (Exception e) {
			logger.error("Could not retrieve private key: ", e);
		}
		return (Key);
	}

}
