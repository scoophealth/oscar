/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * ManageLetters.java
 *
 * Created on October 15, 2006, 11:50 AM
 *
 */

package oscar.oscarReport.data;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import oscar.OscarDocumentCreator;
import oscar.oscarDB.DBHandler;

/**
  create table report_letters(
    ID int(10) auto_increment primary key,
    provider_no varchar(6),
    report_name varchar(255),
    file_name varchar(255),
    report_file mediumblob,
    date_time datetime,
    archive char(1) default 0
  );
 *
 * @author jay
 */


public class ManageLetters {
    
    /** Creates a new instance of ManageLetters */
    public ManageLetters() {
    }
    
    //method to save a new report
    public void saveReport(String providerNo,String reportName,String fileName, byte[] in){
        
        DBHandler dbhandler = null;
        try {
            //System.out.println("e-DATE: "+date);
            dbhandler = new DBHandler(DBHandler.OSCAR_DATA);
            String s = "insert into report_letters (provider_no,report_name, file_name,report_file,date_time,archive) values (?,?,?,?,now(),'0')" ;
            PreparedStatement pstmt = DBHandler.getConnection().prepareStatement(s);
            pstmt.setString(1,providerNo);
            pstmt.setString(2,reportName);
            pstmt.setString(3,fileName);
            pstmt.setBytes(4,in);
            pstmt.executeUpdate();
            pstmt.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //method to archive an existing report
    public void archiveReport(String id){
        DBHandler dbhandler = null;
        try {
            //System.out.println("e-DATE: "+date);
            dbhandler = new DBHandler(DBHandler.OSCAR_DATA);
            String s = "update report_letters set archive = '1' where id =  ?" ;
            PreparedStatement pstmt = DBHandler.getConnection().prepareStatement(s);
            pstmt.setString(1,id);
            pstmt.executeUpdate();
            pstmt.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //method getReport for id
    public JasperReport getReport(String id){
        DBHandler dbhandler = null;
        JasperReport  jasperReport = null;
        try {
            //System.out.println("e-DATE: "+date);
            dbhandler = new DBHandler(DBHandler.OSCAR_DATA);
            String s = "select report_file from report_letters where id  = ?" ;
            PreparedStatement pstmt = DBHandler.getConnection().prepareStatement(s);
            pstmt.setString(1,id);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                InputStream is = rs.getBinaryStream("report_file");
                OscarDocumentCreator osc = new OscarDocumentCreator();
                jasperReport = osc.getJasperReport(is);
                is.close();
            }
            rs.close();
            pstmt.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return jasperReport;
    }
    
    public static String[] getReportParams(JasperReport  jasperReport){
        JRParameter[] jrp =  jasperReport.getParameters();
        
        ArrayList list = new ArrayList();
        if(jrp != null){
            for (int i = 0 ; i < jrp.length; i++){
                if(!jrp[i].isSystemDefined()){
                    list.add(jrp[i].getName());
                    System.out.println("JRP "+i+" :"+jrp[i].getName());
                }
            }
                    
        }
        String[] s = new String[list.size()];
        return (String[]) list.toArray(s);
    }
    
    //method to write file to stream
    public void writeLetterToStream(String id,OutputStream out){
        DBHandler dbhandler = null;
        
        try {
            //System.out.println("e-DATE: "+date);
            dbhandler = new DBHandler(DBHandler.OSCAR_DATA);
            String s = "select report_file from report_letters where id  = ?" ;
            PreparedStatement pstmt = DBHandler.getConnection().prepareStatement(s);
            pstmt.setString(1,id);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
            
                InputStream in = rs.getBinaryStream("report_file");
            
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
            rs.close();
            pstmt.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }
    
    
    
    //method to validate xml
    
    //method to list active reports
    public ArrayList getActiveReportList(){
        DBHandler dbhandler = null;
        ArrayList list = new ArrayList();
        try {
            //System.out.println("e-DATE: "+date);
            dbhandler = new DBHandler(DBHandler.OSCAR_DATA);
            String s = "select ID, provider_no , report_name, file_name, date_time from report_letters where archive = '0' order by date_time,report_name" ;
            PreparedStatement pstmt = DBHandler.getConnection().prepareStatement(s);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
               list.add(getHashFromResultSet(rs));
            }
            rs.close();
            pstmt.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }   
    
    public Hashtable getReportData(String id){
        DBHandler dbhandler = null;
        Hashtable h = null;
        try {
            //System.out.println("e-DATE: "+date);
            dbhandler = new DBHandler(DBHandler.OSCAR_DATA);
            String s = "select ID, provider_no , report_name, file_name, date_time from report_letters where ID = ?" ;
            PreparedStatement pstmt = DBHandler.getConnection().prepareStatement(s);
            pstmt.setString(1,id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
               h = getHashFromResultSet(rs);
            }
            rs.close();
            pstmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return h;
    }
    
    private Hashtable getHashFromResultSet(ResultSet rs) throws Exception{
        Hashtable h = new Hashtable();
        h.put("ID",oscar.Misc.getString(rs,"ID"));
        h.put("provider_no",oscar.Misc.getString(rs,"provider_no"));
        h.put("report_name",oscar.Misc.getString(rs,"report_name"));
        h.put("file_name",oscar.Misc.getString(rs,"file_name"));
        h.put("date_time",rs.getDate("date_time"));
        return h;
    }
    
    
    /*
         
     create table log_letters(
            ID int(10) primary key auto_increment,
            date_time datetime,
            provider_no varchar(6),
            log text,
            report_id  int(10)
         )
         
         */
    public void logLetterCreated(String providerNo,String reportId,String[] demos){
        
         DBHandler dbhandler = null;
        try {
            //System.out.println("e-DATE: "+date);
            dbhandler = new DBHandler(DBHandler.OSCAR_DATA);
            String s = "insert into log_letters (provider_no,report_id, log, date_time) values (?,?,?,now())" ;
            PreparedStatement pstmt = DBHandler.getConnection().prepareStatement(s);
            pstmt.setString(1,providerNo);
            pstmt.setString(2,reportId);
            pstmt.setString(3,serializeDemographic(demos));

            pstmt.executeUpdate();
            pstmt.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    private String serializeDemographic(String[] demos){
        StringBuffer serialString = new StringBuffer();
        if(demos != null){
            for ( int i = 0; i < demos.length; i++){
                serialString.append(demos[i]);
                if (i < (demos.length - 1) ){
                    serialString.append(",");
                }
            }
        }
        return serialString.toString();
    }
    
    private String[] deSerializeDemographic(String demo){
        return demo.split(",");
    }
    
}
