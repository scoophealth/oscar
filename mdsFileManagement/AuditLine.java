import java.lang.StringBuffer;

import java.util.*;
import oscar.oscarMDSLab.dbUtil.*;
import java.sql.ResultSet;
import java.text.*;
import java.lang.reflect.*;
import java.io.*;
import java.util.logging.*;



public class AuditLine {
    Logger logger = Logger.getLogger("mdsFileManagement.AuditLine");
    String delimiter = "  ";
    char bl = ' ';
    

    public  String getAuditLine(String procDate, String procTime, String logId,String formStatus, String formType, String accession, String hcNum, String hcVerCode, String patientName,String orderingClient,String  messageDate,String messageTime){
        logger.info("Getting Audit Line");
                


        return getPaddedString(procDate,11,bl)+delimiter+
               getPaddedString(procTime,8,bl)+delimiter+
               getPaddedString(logId,7,bl)+delimiter+
               getPaddedString(formStatus,1,bl)+delimiter+
               getPaddedString(formType,1,bl)+delimiter+
               getPaddedString(accession,9,bl)+delimiter+
               getPaddedString(hcNum,10,bl)+delimiter+
               getPaddedString(hcVerCode,2,bl)+delimiter+
               getPaddedString(patientName,61,bl)+delimiter+
               getPaddedString(orderingClient,8,bl)+delimiter+
               getPaddedString(messageDate,11,bl)+delimiter+
               getPaddedString(messageTime,8,bl)+"\n\r";             
   
	}

	String getPaddedString(String originalString, int length, char paddingChar){
           StringBuffer str = new StringBuffer(length);
           str.append(originalString);
                      
           for (int i = str.length(); i < length; i++){
             str.append(paddingChar);
           }
           
           return str.substring(0,length);
        }

	
    public String[] auditFormStatus(int segID){
        ArrayList retval = new ArrayList();
        DBHandler db =null;
        try{
            db = new DBHandler(DBHandler.MDS_DATA);             
            ResultSet rs;
            String sql ="select reportFormStatus from mdsZFR where segmentID = '"+segID+"' ";
            
            rs = db.GetSQL(sql);            
            while(rs.next()){
                String reportFormStatus = rs.getString("reportFormStatus");
                if      (reportFormStatus.equals("0")){ retval.add(new String("P")); }
                else if (reportFormStatus.equals("1")){ retval.add(new String("F")); }                                                
            }
            db.CloseConn();
        }catch(Exception e){
            try{ if(db !=null){ db.CloseConn(); } }
            catch(Exception f){ System.out.println("Inside the auditFormStatus::"+f); f.printStackTrace();  }
        }
        String[] retval2 = new String[retval.size()];
        return (String[]) retval.toArray(retval2);
    }
    
    public String[] auditFormType(int segID){
        ArrayList retval = new ArrayList();
        DBHandler db =null;
        try{
            db = new DBHandler(DBHandler.MDS_DATA);             
            ResultSet rs;
            String sql ="select reportForm from mdsZFR where segmentID = '"+segID+"' ";
            
            rs = db.GetSQL(sql);
            while(rs.next()){
                retval.add(formTypeHelper(rs.getString("reportForm")));                
            }
            db.CloseConn();
        }catch(Exception e){
            try{ if(db !=null){ db.CloseConn(); } }
            catch(Exception f){ System.out.println("Inside the auditFormType::"+f); f.printStackTrace();  }
        }
        String[] retval2 = new String[retval.size()];
        return (String[]) retval.toArray(retval2);
    }
    
    public String formTypeHelper(String str){
        HashMap hm = new HashMap();
        hm.put("1", "S");
        hm.put("4", "X");
        hm.put("5", "C");
        hm.put("6", "H");
        hm.put("7", "A");
        hm.put("9", "M");
        return (String) hm.get(str);    
    }

    
    public String auditAccession(int segID){
        String retval = null;
        DBHandler db =null;
        try{
            db = new DBHandler(DBHandler.MDS_DATA);             
            ResultSet rs;
            String sql ="select messageConID from mdsMSH where segmentID = '"+segID+"' ";
            
            rs = db.GetSQL(sql);
            if(rs.next()){
                String messageConID = rs.getString("messageConID");
                //StringTokenizer tokenizer = new StringTokenizer(messageConID,"-");
                String[] messageParts = messageConID.split("-");
                retval = messageParts[1];
                
            }
            db.CloseConn();
        }catch(Exception e){
            try{ if(db !=null){ db.CloseConn(); } }
            catch(Exception f){ System.out.println("Inside the auditAccession::"+f); f.printStackTrace();  }
        }
        return retval;
    }        
    
    public String auditClientNumber(int segID){
        String retval = null;
        DBHandler db =null;
        try{
            db = new DBHandler(DBHandler.MDS_DATA);             
            ResultSet rs;
            String sql ="select messageConID from mdsMSH where segmentID = '"+segID+"' ";
            
            rs = db.GetSQL(sql);
            if(rs.next()){
                String messageConID = rs.getString("messageConID");
                //StringTokenizer tokenizer = new StringTokenizer(messageConID,"-");
                String[] messageParts = messageConID.split("-");
                retval = messageParts[0];
                
            }
            db.CloseConn();
        }catch(Exception e){
            try{ if(db !=null){ db.CloseConn(); } }
            catch(Exception f){ System.out.println("Inside the auditClientNumber::"+f); f.printStackTrace();  }
        }
        return retval;
    }        
        
    public Date auditMessageDate(int segID){
        Date retval = null;
        DBHandler db =null;
        try{
            db = new DBHandler(DBHandler.MDS_DATA);             
            ResultSet rs;
            String sql ="select dateTime from mdsMSH where segmentID = '"+segID+"' ";
            
            rs = db.GetSQL(sql);
            if(rs.next()){
                String dateTime = rs.getString("dateTime");
                //StringTokenizer tokenizer = new StringTokenizer(messageConID,"-");
                try {
                    // Some examples   2002-04-30 09:54:40 
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    retval = (Date)formatter.parse(dateTime);
                    
                    
                } catch (ParseException e) {
                }
                
            }
            db.CloseConn();
        }catch(Exception e){
            try{ if(db !=null){ db.CloseConn(); } }
            catch(Exception f){ System.out.println("Inside the auditMessageDate::"+f); f.printStackTrace();  }
        }
        return retval;
    }
    
    public String auditPatientName(int segID){
        String retval = null;
        DBHandler db =null;
        try{
            db = new DBHandler(DBHandler.MDS_DATA);             
            ResultSet rs;
            String sql ="select patientName from mdsPID where segmentID = '"+segID+"' ";
            
            rs = db.GetSQL(sql);
            if(rs.next()){
                String patientName = rs.getString("patientName");
                
                retval = patientName;
                
            }
            db.CloseConn();
        }catch(Exception e){
            try{ if(db !=null){ db.CloseConn(); } }
            catch(Exception f){ System.out.println("Inside the auditPatientName::"+f); f.printStackTrace();  }
        }
        return retval;
    }        
    
    public String[] auditHealthCard(int segID){
        String[] retval = null;
        DBHandler db =null;
        try{
            db = new DBHandler(DBHandler.MDS_DATA);             
            ResultSet rs;
            String sql =" select healthNumber from mdsPID  where segmentID = '"+segID+"' ";
            
            rs = db.GetSQL(sql);
            if(rs.next()){
                String healthNumber = rs.getString("healthNumber");
                
                healthNumber = healthNumber.substring(1);
                
                String[] healthNumberArray = healthNumber.split(" ");
                System.out.println(" NUMBER IN ARRAY "+healthNumberArray.length);
                if (healthNumberArray.length < 2){
                    
                    String[] noVersionCode = (String[]) Array.newInstance(String.class, 2);
                    //String[] noVersionCode = new String[@];
                    noVersionCode[0] = healthNumber;
                    noVersionCode[1] = "";
                    healthNumberArray = noVersionCode;
                }
                
                retval = healthNumberArray;
                
            }
            db.CloseConn();
        }catch(Exception e){
            try{ if(db !=null){ db.CloseConn(); } }
            catch(Exception f){ System.out.println("Inside the auditHealthCard::"+f); f.printStackTrace();  }
        }
        return retval;
    }   
    
    public void addToAuditFile(String auditLine,String auditLogFile){
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(auditLogFile, true));
            out.write(auditLine);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
