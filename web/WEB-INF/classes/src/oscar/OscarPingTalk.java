package oscar;

import java.util.*;
import org.chip.ping.client.*;
import org.chip.ping.xml.*;
import org.chip.ping.xml.talk.*;
import org.chip.ping.xml.record.*;
import org.chip.ping.xml.record.impl.*;
import org.chip.ping.xml.cddm.impl.*;
import org.chip.ping.xml.cddm.*;
import javax.xml.bind.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import oscar.*;

/*
 * PingOscar.java
 *
 * Created on April 4, 2004, 9:16 PM
 */

/**
 *
 * @author  root
 */
public class OscarPingTalk {
    
    PingTalkClientImpl client ;
    /** Creates a new instance of OscarPingTalk */
    public OscarPingTalk() {
    }
    
    
    
    public String connect(String username,String password) throws Exception {        
        String serverURL = OscarProperties.getInstance().getProperty("PING-SERVER",""); //"http://127.0.0.1:8080/ping-server/PingServlet";
        String actorTicket = null;
        Map properties = new Properties();
        properties.put(PingTalkClient.VALIDATE_REQUESTS, Boolean.TRUE);
        properties.put(PingTalkClient.SERVER_LOCATION, serverURL);
        client = new PingTalkClientImpl(properties);

        AuthenticateResultType art=   client.authenticate(username,password);      
        if (art == null){                
            throw new Exception("Problem Authenticating User");
        }
        actorTicket = art.getActorTicket();
        return actorTicket;
    }
    
    public boolean sendCddm(String actorTicket,String pingId,String owner,String originAgent,String author,String level1,String level2,DataType dataType) throws Exception{                                          
        DefaultCddmGenerator cddmGenerator  = new DefaultCddmGenerator() ;
        CddmType cddmType =  cddmGenerator.generateDefaultCddm(owner,originAgent,author,level1,level2);                                                                        
        BodyType bodyType = cddmType.getCddmBody();            
        List dataList = bodyType.getData();                                                                                    
        dataList.add(dataType);                      
        System.out.println(actorTicket+" "+pingId+" "+cddmType);        
        AddCddmResultType adrt = client.addCddm(actorTicket,pingId,cddmType);        
        return true;
    }
    
    public boolean sendCddm(String actorTicket,String pingId,CddmType cddmType) throws Exception{         
        AddCddmResultType adrt = client.addCddm(actorTicket,pingId,cddmType);        
        return true;
    }
    
    public  CddmType getCddm(String owner,String originAgent,String author,String level1,String level2,DataType dataType){       
        DefaultCddmGenerator cddmGenerator  = new DefaultCddmGenerator() ;
        CddmType cddmType =  cddmGenerator.generateDefaultCddm(owner,originAgent,author,level1,level2);                                                                        
        BodyType bodyType = cddmType.getCddmBody();            
        List dataList = bodyType.getData();                                                                                    
        dataList.add(dataType);
        return cddmType;
    }
    
    public DataType getDataType(org.w3c.dom.Element element) throws Exception{                
        org.chip.ping.xml.cddm.ObjectFactory objectFactory = new org.chip.ping.xml.cddm.ObjectFactory();                                        
        DataType dt = objectFactory.createDataType();            
        dt.setBodyContentFormat("text/xml");            
        BodyContentType content = objectFactory.createBodyContentType();                                                
        content.setAny(element);
        dt.setBodyContent(content);
        return dt;
    }
    
    public DataType getDataType(Object body) throws Exception{   
        
        JAXBContext context = JAXBContext.newInstance("org.chip.ping.xml.talk:org.chip.ping.xml.record:org.chip.ping.xml.pid:org.chip.ping.xml.cddm");
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        
        org.chip.ping.xml.cddm.ObjectFactory objectFactory = new org.chip.ping.xml.cddm.ObjectFactory();

        DataType dt = objectFactory.createDataType();            
        dt.setBodyContentFormat("text/xml");     
        
        BodyContentType content = objectFactory.createBodyContentType();
        
        marshaller.marshal(body, doc);
        content.setAny(doc.getDocumentElement());

        dt.setBodyContent(content);
                                                
        return dt;
    }
    
    
  
    
}
