/*
 * testClient.java
 *
 * Created on October 23, 2007, 11:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author wrighd
 */

import java.util.*;
import org.apache.xmlrpc.*;
import java.io.*;



public class testClient {
    
    public String server_url = "";
    
    /** Creates a new instance of testClient */
    public testClient(String url) throws IOException{
        server_url = url;
    }
    
    public static void main(String[] args) {
                
        if (args.length < 2){
            System.out.println("You must specify at least two ATC codes.");
        }else{
            Vector codes = new Vector();
            for (int i=0; i < args.length; i++){
                codes.add(args[i]);
            }
            
            try{
                Properties p = new Properties();
                p.load(new FileInputStream("./testClient.properties"));
                String url = p.getProperty("server_url");
                if (url.trim().equals("")){
                    System.out.println("You must specify the DrugRef server in the testClient.properties file.");
                }else{
                    testClient client = new testClient(url);
                    
                    Vector interactions = client.getInteractions(codes);
                    Object[] interactionsArray = interactions.toArray();
                    for (int i=0; i < interactionsArray.length; i++){
                        Hashtable ht = (Hashtable) interactionsArray[i];
                        System.out.println("\nEffect: "+ht.get("effect")
                        +"\nAffected Drug: "+ht.get("affecteddrug")
                        +"\nAffecting Drug: "+ht.get("affectingdrug")
                        +"\nEvidence: "+ht.get("evidence")
                        +"\nSignificance: "+ht.get("significance")
                        +"\nAffecting ATC: "+ht.get("affectingatc")
                        +"\nAffected ATC: "+ht.get("affectedatc")
                        +"\nComment: "+ht.get("comment"));
                    }
                }
            }catch(IOException ioe){
                System.out.println("Could not load properties file: "+ioe);
            }catch(Exception e){
                System.out.println("Failed to get interactions from server: "+e);
                e.printStackTrace();
            }
        }
    }
    
    private Object callWebserviceLite(String procedureName,Vector params) throws Exception{
        System.out.println("#CALLDRUGREF-"+procedureName);
        Object object = null;
        try{
            System.out.println("server_url :"+server_url);
            XmlRpcClientLite server = new XmlRpcClientLite(server_url);
            object = (Object) server.execute(procedureName, params);
        }catch (XmlRpcException exception) {
            
            System.err.println("JavaClient: XML-RPC Fault #" +
                    Integer.toString(exception.code) + ": " +
                    exception.toString());
            exception.printStackTrace();
            
            throw new Exception("JavaClient: XML-RPC Fault #" +
                    Integer.toString(exception.code) + ": " +
                    exception.toString());
            
        } catch (Exception exception) {
            System.err.println("JavaClient: " + exception.toString());
            exception.printStackTrace();
            throw new Exception("JavaClient: " + exception.toString());
        }
        return object;
    }
    
    
    public static void removeNullFromVector(Vector v){
        while(v != null && v.contains(null)){
            v.remove(null);
        }
    }
    
    public Vector getInteractions(Vector drugs)throws Exception{
        removeNullFromVector(drugs);
        Vector params = new Vector();
        params.addElement("interactions_byATC");
        params.addElement(drugs);
        //Vector vec = (Vector) callWebserviceLite("get",params);
        Vector vec = new Vector();
        Object obj =  callWebserviceLite("fetch",params);
        if (obj instanceof Vector){
            vec = (Vector) obj;
        }else if(obj instanceof Hashtable){
            Object holbrook = ((Hashtable) obj).get("Holbrook Drug Interactions");
            if (holbrook instanceof Vector){
                vec = (Vector) holbrook;
            }
            Enumeration e = ((Hashtable) obj).keys();
            while (e.hasMoreElements()){
                String s = (String) e.nextElement();
                System.out.println(s+" "+((Hashtable) obj).get(s)+" "+((Hashtable) obj).get(s).getClass().getName());
            }
        }
        
        return vec;
    }
    
}

