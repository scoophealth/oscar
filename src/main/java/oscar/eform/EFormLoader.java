/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.eform;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang.StringUtils;
import org.oscarehr.util.MiscUtils;

import oscar.eform.data.DatabaseAP;
import oscar.eform.data.EForm;

public class EFormLoader {
    static private EFormLoader _instance;
    static private Vector<DatabaseAP> eFormAPs = new Vector<DatabaseAP>();
    static private String marker = "oscarDB";
    static private String opener = "oscarOPEN";
    static private String inputMarker = "oscarDBinput";


    static public EFormLoader getInstance() {
        if (_instance == null) {
            _instance = new EFormLoader();
            parseXML();
            MiscUtils.getLogger().debug("NumElements ====" + eFormAPs.size());
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
    /**
     *
     * @return list of names from database
     */
    public List<String> getNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (int i=0; i<eFormAPs.size(); i++) {
            DatabaseAP curap = eFormAPs.get(i);
            names.add(curap.getApName());
        }
        return names;
    }

    public static String getMarker() { return marker; }
    public static String getInputMarker() { return inputMarker; }
    public static String getOpener() { return opener; }

    public static String getOpenEform(String url, String fdid, String fname, String field, EForm efm) {
        String fid = EFormUtil.getEFormIdByName(fname);
        if (StringUtils.isBlank(fid)) return "alert('Eform does not exist ["+fname+"]');";
        if (StringUtils.isBlank(url)) return null;
        if (StringUtils.isBlank(field)) return null;

        String providerNo = efm.getProviderNo();
        String demographicNo = efm.getDemographicNo();
        String formId = efm.getFid();
        String appointmentNo = efm.getAppointmentNo();

        if (url.contains("efmformadd_data.jsp")) { //whole new eform
            url += "?fid="+fid+"&demographic_no="+demographicNo+"&appointment="+appointmentNo;
        } else if (!StringUtils.isBlank(fdid)) { //filled eform, eform already linked
            url += "?fdid="+fdid+"&appointment="+appointmentNo;
        } else if (demographicNo.equals("-1")) { //eform viewed in admin
            url += "?fid="+fid;
        } else { //filled eform, but create new eform link
            url = url.replaceFirst("efmshowform_data.jsp", "efmformadd_data.jsp");
            url += "?fid="+fid+"&demographic_no="+demographicNo+"&appointment="+appointmentNo;
        }
        String link = "&eform_link="+providerNo+"_"+demographicNo+"_"+formId+"_"+field;
        return "window.open('"+url+link+"');";
    }

    public static DatabaseAP getAP(String apName) {
        //returns he DatabaseAP corresponding to the ap name
        DatabaseAP curAP = null;
        for (int i=0; i<eFormAPs.size(); i++) {
            curAP =  eFormAPs.get(i);
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
      digester.addBeanPropertySetter("eformap-config/databaseap/ap-insql", "apInSQL");
      digester.addBeanPropertySetter("eformap-config/databaseap/archive", "archive");
      digester.addBeanPropertySetter("eformap-config/databaseap/ap-json-output", "apJsonOutput");
      digester.addSetNext("eformap-config/databaseap","addDatabaseAP");
      try {
          Properties op = oscar.OscarProperties.getInstance();
          String configpath = op.getProperty("eform_databaseap_config");
          InputStream fs = null;
          if (configpath == null) {
             EFormLoader eLoader = new EFormLoader();
             ClassLoader loader = eLoader.getClass().getClassLoader();
             fs = loader.getResourceAsStream("/oscar/eform/apconfig.xml");
          }else{
             fs = new FileInputStream(configpath);
          }
          digester.parse(fs);
          fs.close();
      } catch (Exception e) { MiscUtils.getLogger().error("Error", e); }
    }
 }
