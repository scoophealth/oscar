// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.ResourceBundle;
import java.util.Locale;

import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarConsultationRequest.config.data.EctConConfigurationJavascriptData;

public class EctConConstructSpecialistsScriptsFile
{

    public EctConConstructSpecialistsScriptsFile()
    {
        retval = null;
    }

    public String makeFile()
    {
        serviceId = new Vector();
        serviceDesc = new Vector();
        File file = new File("oscarEncounter/consult.js");
        try
        {
            FileWriter fileWriter = new FileWriter(file);
            retval = "writing file too ".concat(String.valueOf(String.valueOf(file.getAbsolutePath())));
            fileWriter.write("function makeSpecialistslist(dec){\n");
            fileWriter.write(" if(dec=='1') \n");
            fileWriter.write("{K(-1,\"----Choose a Service-------\");D(-1,\"--------Choose a Specialist-----\");}\n");
            fileWriter.write("else\n");
            fileWriter.write("{K(-1,\"----All Services-------\");D(-1,\"--------All Specialists-----\");}\n");
            try
            {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = "select * from  consultationServices";
                ResultSet rs;
                for(rs = db.GetSQL(sql); rs.next(); serviceDesc.add(db.getString(rs,"serviceDesc")))
                    serviceId.add(db.getString(rs,"serviceId"));

                System.out.println(String.valueOf(String.valueOf((new StringBuffer("there are ")).append(serviceId.size()).append(" services"))));
                for(int i = 0; i < serviceId.size(); i++)
                {
                    String servId = (String)serviceId.elementAt(i);
                    String servDesc = (String)serviceDesc.elementAt(i);
                    fileWriter.write(String.valueOf(String.valueOf((new StringBuffer("K(")).append(servId).append(",\"").append(servDesc).append("\");\n"))));
                    sql = String.valueOf(String.valueOf((new StringBuffer("select ser.specId, pro.fName, pro.lName, pro.proLetters, pro.address , pro.phone, pro.fax  from serviceSpecialists ser, professionalSpecialists pro where pro.specId = ser.specId and ser.serviceId = '")).append(servId).append("' order by pro.lName")));
                    for(rs = db.GetSQL(sql); rs.next(); System.out.println("this went this many times = ".concat(String.valueOf(String.valueOf(servId)))))
                    {
                        String name = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(db.getString(rs,"lName"))))).append(", ").append(db.getString(rs,"fName")).append(" ").append(db.getString(rs,"proLetters"))));
                        System.out.println("name : ".concat(String.valueOf(String.valueOf(name))));
                        String specId = db.getString(rs,"specId");
                        String phone = db.getString(rs,"phone");
                        String address = db.getString(rs,"address");
                        String fax = db.getString(rs,"fax");
                        fileWriter.write(String.valueOf(String.valueOf((new StringBuffer("D(")).append(servId).append(",\"").append(specId).append("\",\"").append(phone).append("\",\"").append(name).append("\",\"").append(fax).append("\",\"").append(address).append("\");\n"))));
                    }

                    fileWriter.write("\n");
                }

                rs.close();
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
            fileWriter.write("\n");
            fileWriter.write("}\n");
            fileWriter.close();
        }
        catch(IOException io)
        {
            System.out.println(io.getMessage());
        }
        return retval;
    }

    public void makeString(Locale locale)
    {
        serviceId = new Vector();
        serviceDesc = new Vector();
        ResourceBundle props = ResourceBundle.getBundle("oscarResources", locale);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("function makeSpecialistslist(dec){\n");
        stringBuffer.append(" if(dec=='1') \n");
        stringBuffer.append("{K(-1,\"----" + props.getString("oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.optChooseServ") + "-------\");D(-1,\"--------" + props.getString("oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.optChooseSpec") + "-----\");}\n");
        stringBuffer.append("else\n");
        stringBuffer.append("{K(-1,\"----" + props.getString("oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.optAllServices") + "-------\");D(-1,\"--------" + props.getString("oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.optAllSpecs") + "-----\");}\n");
        try
        {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select * from  consultationServices where active = '1' order by serviceDesc";
            ResultSet rs;
            for(rs = db.GetSQL(sql); rs.next(); serviceDesc.add(db.getString(rs,"serviceDesc")))
                serviceId.add(db.getString(rs,"serviceId"));

            System.out.println(String.valueOf(String.valueOf((new StringBuffer("there are ")).append(serviceId.size()).append(" services"))));
            for(int i = 0; i < serviceId.size(); i++)
            {
                String servId = (String)serviceId.elementAt(i);
                String servDesc = (String)serviceDesc.elementAt(i);
                stringBuffer.append(String.valueOf(String.valueOf((new StringBuffer("K(")).append(servId).append(",\"").append(servDesc).append("\");\n"))));
                sql = String.valueOf(String.valueOf((new StringBuffer("select ser.specId, pro.fName, pro.lName, pro.proLetters, pro.address , pro.phone, pro.fax  from serviceSpecialists ser, professionalSpecialists pro where pro.specId = ser.specId and ser.serviceId = '")).append(servId).append("' order by pro.lName")));
                for(rs = db.GetSQL(sql); rs.next(); System.out.println("this went this many times = ".concat(String.valueOf(String.valueOf(servId)))))
                {
                    String name = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(db.getString(rs,"lName"))))).append(", ").append(db.getString(rs,"fName")).append(" ").append(db.getString(rs,"proLetters"))));
                    name = this.escapeString(name);
                    System.out.println("name : ".concat(String.valueOf(String.valueOf(name))));
                    String specId = db.getString(rs,"specId");
                    String phone = db.getString(rs,"phone");
                    String address = db.getString(rs,"address");                 
                    address = this.escapeString(address);
                    String fax = db.getString(rs,"fax");
                    stringBuffer.append(String.valueOf(String.valueOf((new StringBuffer("D(")).append(servId).append(",\"").append(specId).append("\",\"").append(phone).append("\",\"").append(name).append("\",\"").append(fax).append("\",\"").append(address).append("\");\n"))));
                }

                stringBuffer.append("\n");
            }

            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        stringBuffer.append("\n");
        stringBuffer.append("}\n");
        javaScriptString = stringBuffer.toString();
        EctConConfigurationJavascriptData configurationJavascriptData = new EctConConfigurationJavascriptData();
        configurationJavascriptData.setJavascript(javaScriptString);
    }
    
    private String escapeString(String s) {
        s = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(s);
        s = org.apache.commons.lang.StringEscapeUtils.escapeJava(s);
        return s;
    }

    Vector serviceId;
    Vector serviceDesc;
    String javaScriptString;
    String retval;
}
