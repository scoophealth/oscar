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


package oscar.oscarBilling.ca.bc.Teleplan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

/**
 *
 * @author jay
 */
public class TeleplanResponse {
    static Logger log=MiscUtils.getLogger();
    private String transactionNo = null;
    private String result = null;
    private String filename = null;
    private String realFilename = null;
    private String msgs = null;
    private int lineCount = 0;

    /** Creates a new instance of TeleplanResponse */
    public TeleplanResponse() {
    }
  

	
	
    void processResponseStream(InputStream in){
        try {
            String directory = OscarProperties.getInstance().getProperty("DOCUMENT_DIR","./");
            double randNum= Math.random();
            String tempFile = directory+"teleplan.msp"+randNum;
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));
            BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));

            String str = "";
            String lastLine = null;
            while ((str = bin.readLine()) != null) {
           //write str to temp file
                lineCount++;
                out.write(str+"\n");
                log.debug(str);
                lastLine = new String(str);
            }
            out.close();
            bin.close();
            lineCount--;
            processLastLine(lastLine);
            //If it has a filename same to
            
            if (this.getFilename() != null && !this.getFilename().trim().equals("")){
               File file = new File(tempFile); 
               realFilename = "teleplan"+this.getFilename()+randNum;
               File file2 = new File(directory+realFilename);
               boolean success = file.renameTo(file2);
                if (!success) {
                   log.error("File was not successfully renamed");
                    // 
                }
            }
            
           
        } catch (IOException e) {
            MiscUtils.getLogger().error("Error", e);
        }
    }
	
	
    //#TID=001;Result=SUCCESS;Filename=TPBULET-I.txt;Msgs=;
    //	String str = "#TID=001;Result=SUCCESS;Filename=TPBULET-I.txt;Msgs
    void processLastLine(String str){
        int idx = str.indexOf("Msgs=");
    	msgs = str.substring(idx+5,str.lastIndexOf(';'));
    	str = str.substring(0,idx);
    	idx = str.indexOf("Filename=");
    	filename = str.substring(idx+9,str.lastIndexOf(';'));
    	str = str.substring(0,idx);
        idx = str.indexOf("Result=");
    	result = str.substring(idx+7,str.lastIndexOf(';'));
    	str = str.substring(0,idx);
        idx = str.indexOf("#TID=");
        transactionNo = str.substring(idx+5,str.lastIndexOf(';'));
    }
		
    public String toString(){
        return "#TID="+getTransactionNo()+";Result="+getResult()+";Filename="+getFilename()+";Msgs="+getMsgs()+"; NUM LINES "+lineCount+" REALFILNAME ="+realFilename;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public String getResult() {
        return result;
    }

    public boolean isFailure(){
        return result.equals("FAILURE");
    }
    
    public boolean isSuccess(){
        return result.equals("SUCCESS");
    }
    
    public String getFilename() {
        return filename;
    }
    
    public String getRealFilename(){
        return realFilename;
    }

    public String getMsgs() {
        return msgs;
    }
    
    public File getFile(){
        String directory = OscarProperties.getInstance().getProperty("DOCUMENT_DIR","./");
        return new File (directory+realFilename);    
    }
		
}
