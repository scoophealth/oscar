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

import oscar.oscarLab.ca.all.upload.handlers.DefaultHandler;
import oscar.oscarLab.ca.all.upload.handlers.MessageHandler;

/**
 *
 * @author wrighd
 */
public class HandlerClassFactory {
    
    Logger logger = Logger.getLogger(HandlerClassFactory.class);
    
    public HandlerClassFactory(){
    }
    
    /*
     *  Create and return the message handler corresponding to the message type
     */
    public MessageHandler getHandler(String type) {
        Document doc = null;
        String msgType;
        String msgHandler = "";
        
        if (type.equals("")){
            logger.debug("Type not specified using Default Handler");
            return( new DefaultHandler());
        }
        try{
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("oscar/oscarLab/ca/all/upload/message_config.xml");
            SAXBuilder parser = new SAXBuilder();
            doc = parser.build(is);
            
            Element root = doc.getRootElement();
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
                Class classRef = Class.forName(msgHandler);
                MessageHandler mh = (MessageHandler) classRef.newInstance();
                logger.debug("Message handler '"+msgHandler+"' created successfully");
                return(mh);
            } catch (ClassNotFoundException e) {
                logger.debug("Could not find message handler: "+msgHandler+
                        "\nUsing default message handler instead");
                e.printStackTrace();
                return(new DefaultHandler());
            } catch (Exception e1){
                logger.error("Could not create message handler: "+msgHandler+
                        "\nUsing default message handler instead", e1);
                e1.printStackTrace();
                return(new DefaultHandler());
            }
        }
    }
}
