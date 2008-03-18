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
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.ResultSet;
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

import oscar.oscarDB.DBHandler;
import oscar.oscarLab.FileUploadCheck;
import oscar.oscarLab.ca.all.upload.HandlerClassFactory;
import oscar.oscarLab.ca.all.upload.handlers.MessageHandler;
import oscar.oscarLab.ca.all.util.Utilities;


public class LabUploadAction extends Action {
    Logger logger = Logger.getLogger(LabUploadAction.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
        LabUploadForm frm = (LabUploadForm) form;
        FormFile importFile = frm.getImportFile();
        
        String signature = request.getParameter("signature");
        String key = request.getParameter("key");
        String service = request.getParameter("service");
        String outcome = "";
        String audit = "";
        
        ArrayList clientInfo = getClientInfo(service);
        PublicKey clientKey = (PublicKey) clientInfo.get(0);
        String type = (String) clientInfo.get(1);
        
        try{
            
            InputStream is = decryptMessage(importFile.getInputStream(), key, clientKey);
            Utilities u = new Utilities();
            String fileName = importFile.getFileName();
            String filePath = u.saveFile(is, fileName);
            importFile.getInputStream().close();
            File file = new File(filePath);
            
            if (validateSignature(clientKey, signature, file)){
                logger.debug("Validated Successfully");
                HandlerClassFactory f = new HandlerClassFactory();
                MessageHandler msgHandler = f.getHandler(type);
                
                is = new FileInputStream(file);
                FileUploadCheck fileC = new FileUploadCheck();
                int check = fileC.addFile(file.getName(),is,"0");
                if (check != FileUploadCheck.UNSUCCESSFUL_SAVE){
                    if((audit = msgHandler.parse(filePath,check)) != null)
                        outcome = "uploaded";
                    else
                        outcome = "upload failed";
                }else{
                    outcome = "uploaded previously";                    
                }
                is.close();
            }else{
                logger.info("failed to validate");
                outcome = "validation failed";
            }
            
            
            
        }catch(Exception e){
            logger.debug("Exception: "+e);
            e.printStackTrace();
            outcome = "exception";
        }
        request.setAttribute("outcome", outcome);
        request.setAttribute("audit", audit);
        logger.info("forwarding outcome "+outcome);
        return mapping.findForward("success");
    }
    
    
    public LabUploadAction() {
    }
    
    /*
     * Decrypt the encrypted message and return the original version of the message as an InputStream
     */
    InputStream decryptMessage(InputStream is, String skey, PublicKey pkey){
        
        Base64 base64 = new Base64();
        
        // Decrypt the secret key and the message
        try{
            
            // retrieve the servers private key
            PrivateKey key = getServerPrivate();
            
            // Decrypt the secret key using the servers private key
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] newSecretKey = cipher.doFinal(base64.decode(skey.getBytes("ASCII")));
            
            // Decrypt the message using the secret key
            SecretKeySpec skeySpec = new SecretKeySpec(newSecretKey, "AES");
            Cipher msgCipher = Cipher.getInstance("AES");
            msgCipher.init(Cipher.DECRYPT_MODE, skeySpec);
            
            is = new CipherInputStream(is, msgCipher);
            
            // Return the decrypted message
            return(new BufferedInputStream(is));
            
        }catch(Exception e){
            logger.error("Could not decrypt the message", e);
            return(null);
        }
    }
    
    /*
     *  Check that the signature 'sigString' matches the message InputStream 'msgIS' thus
     *  verifying that the message has not been altered.
     */
    boolean validateSignature(PublicKey key, String sigString, File input){
        Base64 base64 = new Base64();
        byte[] buf = new byte[1024];
        
        try{
            
            InputStream msgIs = new FileInputStream(input);
            Signature sig = Signature.getInstance("MD5WithRSA");
            sig.initVerify(key);
            
            // Read in the message bytes and update the signature
            int numRead = 0;
            while ((numRead = msgIs.read(buf)) >= 0) {
                sig.update(buf, 0, numRead);
            }
            msgIs.close();
            
            return(sig.verify(base64.decode(sigString.getBytes("ASCII"))));
            
        }catch(Exception e){
            logger.debug("Could not validate signature: "+e);
            e.printStackTrace();
            return(false);
        }
    }
    
   /*
    *  Retrieve the clients public key from the database
    */
    ArrayList getClientInfo(String service){
        
        PublicKey Key = null;
        Base64 base64 = new Base64();
        String keyString = "";
        String type = "";
        byte[] publicKey;
        ArrayList info = new ArrayList();
        
        try{
            
            String query = "SELECT pubKey, type FROM publicKeys WHERE service='"+service+"';";
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(query);
            
            while(rs.next()){
                keyString = db.getString(rs,"pubKey");
                type = db.getString(rs,"type");
            }
            
            rs.close();
            db.CloseConn();
            
            publicKey = base64.decode(keyString.getBytes("ASCII"));;
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            Key = keyFactory.generatePublic(pubKeySpec);
            
            info.add(Key);
            info.add(type);
            
        }catch(Exception e){
            logger.error("Could not retrieve private key: ", e);
        }
        return(info);
    }
    
    /*
     *  Retrieve the servers private key from the database
     */
    PrivateKey getServerPrivate(){
        
        PrivateKey Key = null;
        Base64 base64 = new Base64();
        byte[] privateKey;
        String keyString = "";
        
        try{
            
            String query = "SELECT privKey FROM oscarKeys WHERE name='oscar';";
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(query);
            
            while(rs.next()){
                keyString = db.getString(rs,"privKey");
            }
            logger.info("oscar key: "+keyString);
            rs.close();
            db.CloseConn();
            
            privateKey = base64.decode(keyString.getBytes("ASCII"));
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privateKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            Key = keyFactory.generatePrivate(privKeySpec);
        }catch(Exception e){
            logger.error("Could not retrieve private key: ", e);
        }
        return(Key);
    }
    
}
