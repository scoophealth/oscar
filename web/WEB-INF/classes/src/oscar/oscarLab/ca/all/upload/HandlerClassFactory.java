/*
 * HandlerClassFactory.java
 *
 * Created on May 23, 2007, 11:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarLab.ca.all.upload;

import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.ca.all.upload.handlers.DefaultHandler;
import oscar.oscarLab.ca.all.upload.handlers.MessageHandler;

public final class HandlerClassFactory {
    
    private static final Logger logger = MiscUtils.getLogger();
    
    private HandlerClassFactory(){
    	// don't instantiate
    }
    
    /*
     *  Create and return the message handler corresponding to the message type
     */
    public static MessageHandler getHandler(String type) {
        Document doc = null;
        String msgType;
        String msgHandler = "";
        
        if (type.equals("")){
            logger.debug("Type not specified using Default Handler");
            return( new DefaultHandler());
        }
        try{
            InputStream is = HandlerClassFactory.class.getClassLoader().getResourceAsStream("oscar/oscarLab/ca/all/upload/message_config.xml");
            SAXBuilder parser = new SAXBuilder();
            doc = parser.build(is);
            
            Element root = doc.getRootElement();

            @SuppressWarnings("unchecked")
            List items = root.getChildren();
            for (int i = 0; i < items.size(); i++){
                Element e = (Element) items.get(i);
                msgType = e.getAttributeValue("name");
                if (msgType.equals(type))
                    msgHandler = "oscar.oscarLab.ca.all.upload.handlers."+e.getAttributeValue("className");
            }
        }catch(Exception e){
            logger.error("Could not parse config file", e);
        }
        // create and return the message handler
        if (msgHandler.equals("")){
            return( new DefaultHandler() );
        }else{
            try {
                @SuppressWarnings("unchecked")
                Class classRef = Class.forName(msgHandler);
                MessageHandler mh = (MessageHandler) classRef.newInstance();
                logger.debug("Message handler '"+msgHandler+"' created successfully");
                return(mh);
            } catch (Exception e){
                logger.error("Could not create message handler: "+msgHandler+", Using default message handler instead", e);
                return(new DefaultHandler());
            }
        }
    }
}
