package oscar.oscarLab.ca.all.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.OscarKeyDao;
import org.oscarehr.common.dao.PublicKeyDao;
import org.oscarehr.common.model.OscarKey;
import org.oscarehr.common.model.PublicKey;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public final class KeyPairGen {
    
    private static Logger logger = MiscUtils.getLogger();
    
    /**
     * Creates a new instance of KeyPairGen
     */
    private KeyPairGen() {
    	// utility class needs no instantiation
    }
        
    /**
     *  Create a key pair for the specified service and store it in the database
     */
    public static String createKeys(String name, String type){
        byte[] pubKey;
        byte[] privKey;
        Base64 base64 = new Base64();
        
        // Generate an RSA key
        try{
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair key = keyGen.generateKeyPair();
            
            pubKey = base64.encode(key.getPublic().getEncoded());
            privKey = base64.encode(key.getPrivate().getEncoded());
                       
            if( name.equals("oscar")){
                // the primary key is name, therefore this statement will only
                // be able to run once and the oscar key pair will not be overwritten
            	
            	OscarKeyDao oscarKeyDao=(OscarKeyDao)SpringUtils.getBean("oscarKeyDao");
            	OscarKey oscarKey=new OscarKey();
            	oscarKey.setName("oscar");
            	oscarKey.setPublicKey(new String(pubKey, MiscUtils.ENCODING));
            	oscarKey.setPrivateKey(new String(privKey, MiscUtils.ENCODING));

            	oscarKeyDao.persist(oscarKey);            	
            	
                // no keys need to be returned so return "success" instead to 
                // indicate the operation completed successfully
                return("success");
            }else{
            	
            	PublicKeyDao publicKeyDao=(PublicKeyDao)SpringUtils.getBean("publicKeyDao");
                PublicKey publicKeyObject=new PublicKey();
                publicKeyObject.setService(name);
                publicKeyObject.setType(type);
                publicKeyObject.setBase64EncodedPublicKey(new String(pubKey, MiscUtils.ENCODING));
                publicKeyObject.setBase64EncodedPrivateKey(new String(privKey, MiscUtils.ENCODING));
                
                publicKeyDao.persist(publicKeyObject);
                
                return(publicKeyObject.getBase64EncodedPrivateKey());
            }
            
        }catch(NoSuchAlgorithmException e){
            logger.error("Could not create RSA key", e);
            return null;
        }catch(Exception e){
            logger.error("Could not save keys",e);
            return null;
        }

    }
    
    
    /**
     *  Retrieve oscars public key and return it as a string
     */
    public static String getPublic(){
        try{
        	OscarKeyDao oscarKeyDao=(OscarKeyDao)SpringUtils.getBean("oscarKeyDao");
        	OscarKey oscarKey=oscarKeyDao.find("oscar");

            return(oscarKey.getPublicKey());
        }catch(Exception e){
            logger.error("Could not get public key", e);
            return null;
        }

    }
    
    /**
     *  Check if the specified service name has already been used to create a key
     *  pair, return 'true' if it has and 'false' otherwise.
     */
    public static boolean checkName(String name){
        if (name.equals("oscar"))
        {
        	OscarKeyDao oscarKeyDao=(OscarKeyDao)SpringUtils.getBean("oscarKeyDao");
        	OscarKey oscarKey=oscarKeyDao.find("oscar");
        	return(oscarKey!=null);
        }
        else
        {
        	PublicKeyDao publicKeyDao=(PublicKeyDao)SpringUtils.getBean("publicKeyDao");
            PublicKey publicKeyObject=publicKeyDao.find(name);
            return(publicKeyObject!=null);
        }
    }    
}
