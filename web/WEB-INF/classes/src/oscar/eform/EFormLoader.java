
package oscar.eform;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import oscar.eform.data.DatabaseAP;
import org.apache.commons.digester.*;

public class EFormLoader {
    static private EFormLoader _instance;
    static private Vector eFormAPs = new Vector();
    static private String marker = "oscarDB";
    
    static public EFormLoader getInstance() {
        if (_instance == null) {
            _instance = new EFormLoader();
            parseXML();
            System.out.println("NumElements ====" + eFormAPs.size());
        }
        return _instance;
    }
    
    static public void addDatabaseAP(DatabaseAP ap) {
        String processed = ap.getApOutput();
        //-------allow user to enter '\n' for new line---
        int pointer;
        while ((pointer = processed.indexOf("\\"+"n")) >= 0) {
            processed = processed.substring(0, pointer) + '\n' + 
                         processed.substring(pointer+2);
        }
        //-----------------------------------------------
        ap.setApOutput(processed);
        eFormAPs.addElement(ap);
    }
    
    private EFormLoader() {
        /*
        String name = "patient_name";
        String sql = "SELECT * FROM demographic WHERE demographic_no=${demographic}";
        String output = "${last_name}, ${first_name}";
        DatabaseAP ap1 = new DatabaseAP(name, sql, output);
        String name2 = "patient_sex";
        String sql2 = "SELECT * FROM demographic WHERE demographic_no=${demographic}";
        String output2 = "${first_name}'s hin is: ${hin}";
        DatabaseAP ap2 = new DatabaseAP(name2, sql2, output2);
        eFormAPs.addElement(ap1);
        eFormAPs.addElement(ap2);
        */
        
    }
    /*
    public static ArrayList getNames() {
        ArrayList names = new ArrayList();
        for (int i=0; i<eFormAPs.size(); i++) {
            DatabaseAP curap = (DatabaseAP) eFormAPs.get(i);
            names.add(curap.getApName());
        }
        return names;
    }*/
    
    public static String getMarker() { return marker; }
    
    public static DatabaseAP getAP(String apName) {
        //returns he DatabaseAP corresponding to the ap name
        DatabaseAP curAP = null;
        for (int i=0; i<eFormAPs.size(); i++) {
            curAP = (DatabaseAP) eFormAPs.get(i);
            if (apName.equalsIgnoreCase(curAP.getApName())) {
                return curAP;
            }
        }
        return null;
    }
    
    /* Example:
     *<eformap-config>
     *  <databaseap>
     *      <ap-name>patient_name</ap-name>
     *      <ap-sql>select * from demographic where demographic_no=${demographic}</ap-sql>
     *      <ap-output>${last_name}, ${first_name}</ap-output>
     *  </databaseap>
     *</eformap-config>
     *Call ap like so: <input type="text" oscarDB=patient_name size="20">*/
    
    public static void parseXML() {
      Digester digester = new Digester();
      digester.push(_instance); // Push controller servlet onto the stack
      digester.setValidating(false);
               
      digester.addObjectCreate("eformap-config/databaseap",DatabaseAP.class);
      //digester.addSetProperties("eformap-config/databaseap");
      digester.addBeanPropertySetter("eformap-config/databaseap/ap-name","apName");
      digester.addBeanPropertySetter("eformap-config/databaseap/ap-sql","apSQL");
      digester.addBeanPropertySetter("eformap-config/databaseap/ap-output","apOutput");
      digester.addSetNext("eformap-config/databaseap","addDatabaseAP");
      try {
          Properties op = oscar.OscarProperties.getInstance();
          String configpath = op.getProperty("eform_databaseap_config");
          if (configpath == null) {
              String project = op.getProperty("project_home");
              configpath = "/usr/local/tomcat/webapps/OscarDocument/" + project + "/eform/apconfig.xml";
          }
          FileInputStream fs = new FileInputStream(configpath);
          digester.parse(fs);
          fs.close();
      } catch (Exception e) { e.printStackTrace(); }
    }
}
