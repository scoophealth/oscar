/*
 * KeyPairGen.java
 *
 * Created on June 15, 2007, 12:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import oscar.oscarDB.DBHandler;



/**
 *
 * @author wrighd
 */
public class KeyPairGen {
    
    Logger logger = Logger.getLogger(KeyPairGen.class);
    
    /**
     * Creates a new instance of KeyPairGen
     */
    public KeyPairGen() {
    }
        
    /**
     *  Create a key pair for the specified service and store it in the database
     */
    public String createKeys(String name, String type){
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
                String insertStmt = "INSERT INTO oscarKeys (name, pubKey, privKey) VALUES ('oscar','"+(new String(pubKey, "ASCII"))+"','"+(new String(privKey, "ASCII"))+"');";
                runInsertStmt(insertStmt);
                
                // no keys need to be returned so return "success" instead to 
                // indicate the operation completed successfully
                return("success");
            }else{
                // insert the public key into that database
                String insertStmt = "INSERT INTO publicKeys (service, type, pubKey) VALUES ('"+name+"', '"+type+"', '"+(new String(pubKey, "ASCII"))+"');";
                if(!runInsertStmt(insertStmt))
                    return null;
                                
                return(new String(privKey, "ASCII"));
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
    public String getPublic(){
        String key = "";
        
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            
            String query = "SELECT pubKey FROM oscarKeys WHERE name='oscar';";
            ResultSet rs = db.GetSQL(query);
            
            while(rs.next()){
                key = db.getString(rs,"pubKey");
            }
            rs.close();
            return(key);
            
        }catch(Exception e){
            logger.error("Could not save public key", e);
            return null;
        }

    }
    
    /**
     *  Check if the specified service name has already been used to create a key
     *  pair, return 'true' if it has and 'false' otherwise.
     */
    public boolean checkName(String name){
        String query = "";
        String result = "";
        
        if (name.equals("oscar"))
            query = "SELECT name FROM oscarKeys Where name='oscar';";
        else
            query = "SELECT service FROM publicKeys WHERE service='"+name+"';";
        
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(query);
            
            while(rs.next()){
                result = db.getString(rs,0);
            }
            rs.close();
        }catch(Exception e){
            logger.error("Could not retrieve service name from database: ",e);
            return true;
        }
        
        if (result.equals(""))
            return false;
        else
            return true;
        
    }
    
    /**
     *  Connect to the database and run the specified insert statement
     */
    private boolean runInsertStmt(String insertStmt){
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            db.RunSQL(insertStmt);
            return true;
        }catch(Exception e){
            logger.error("Could not save insert key(s) into the database", e);
            return false;
        }
    }
    
}
