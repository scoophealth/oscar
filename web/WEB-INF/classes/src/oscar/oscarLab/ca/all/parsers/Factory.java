/*
 * Factory.java
 *
 * Created on June 4, 2007, 10:46 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.parsers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;

/**
 *
 * @author wrighd
 */
public class Factory {
    
    Logger logger = Logger.getLogger(Factory.class);
    
    public Factory getInstance(){
        return( new Factory());
    }
    
    public Factory() {
    }
    /**
     *  Find the lab corresponding to segmentID and return the appropriate
     *  MessageHandler for it
     */
    public MessageHandler getHandler(String segmentID){
        String type = "";
        String hl7Body = "";
        String getMessage = "SELECT type, message from hl7TextMessage where lab_id = '"+segmentID+"';";
        
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(getMessage);
            Base64 base64 = new Base64();
            
            while(rs.next()){
                
                String fileString = db.getString(rs,"message");
                hl7Body = new String(base64.decode(fileString.getBytes("ASCII")), "ASCII");
                type = db.getString(rs,"type");
                
            }
            
            rs.close();
        }catch(Exception e){
            logger.error("Could not retrieve lab for segmentID("+segmentID+")", e);
        }
        
        return getHandler(type, hl7Body);
        
    }
    
    
    public String getHL7Body(String segmentID){
        String getMessage = "SELECT type, message from hl7TextMessage where lab_id = '"+segmentID+"';";
        String ret = null;
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(getMessage);
            Base64 base64 = new Base64();
            while(rs.next()){
                String fileString = db.getString(rs,"message");
                ret = new String(base64.decode(fileString.getBytes("ASCII")), "ASCII");
            }
            rs.close();
        }catch(Exception e){
            logger.error("Could not retrieve lab for segmentID("+segmentID+")", e);
        }
        return ret;
    }
    
    /*
     *  Create and return the message handler corresponding to the message type
     */
    public MessageHandler getHandler(String type, String hl7Body){
        Document doc = null;
        String msgType;
        String msgHandler = "";
        
        
        try{
            
            // return default handler if the type is not specified
            if (type == null){
                MessageHandler handler = new DefaultGenericHandler();
                handler.init(hl7Body);
                return(handler);
            }
            
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("oscar/oscarLab/ca/all/upload/message_config.xml");
            
            if (OscarProperties.getInstance().getProperty("LAB_TYPES") != null){
                String filename = OscarProperties.getInstance().getProperty("LAB_TYPES");
                is = new FileInputStream(filename) ;
            }
            SAXBuilder parser = new SAXBuilder();
            doc = parser.build(is);
           
            Element root = doc.getRootElement();
            List items = root.getChildren();
            for (int i = 0; i < items.size(); i++){
                Element e = (Element) items.get(i);
                msgType = e.getAttributeValue("name");
                if (msgType.equals(type))
                    msgHandler = "oscar.oscarLab.ca.all.parsers."+e.getAttributeValue("className");
            }

            // create and return the message handler
            if (msgHandler.equals("")){
                logger.info("No message handler specified for type: "+type+
                        "\nUsing default message handler instead");
                MessageHandler mh = new DefaultGenericHandler();
                mh.init(hl7Body);
                return(mh);
            }else{
                try {
                    Class classRef = Class.forName(msgHandler);
                    MessageHandler mh = (MessageHandler) classRef.newInstance();
                    logger.info("Message handler '"+msgHandler+"' created successfully");
                    mh.init(hl7Body);
                    return(mh);
                } catch (ClassNotFoundException e) {
                    logger.info("Could not find message handler: "+msgHandler+
                            "\nUsing default message handler instead");
                    MessageHandler mh = new DefaultGenericHandler();
                    mh.init(hl7Body);
                    return(mh);
                } catch (Exception e1){
                    logger.error("Could not create message handler: "+msgHandler+
                            "\nUsing default message handler instead", e1);
                    MessageHandler mh = new DefaultGenericHandler();
                    mh.init(hl7Body);
                    return(mh);
                }
            }
        } catch (Exception e) {
            logger.error("Could not create message handler", e);
            return(null);
        }
    }
    
}