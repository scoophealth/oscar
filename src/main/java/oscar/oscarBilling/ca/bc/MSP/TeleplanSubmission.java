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


package oscar.oscarBilling.ca.bc.MSP;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarBilling.ca.bc.Teleplan.TeleplanSequenceDAO;
import oscar.oscarBilling.ca.bc.data.BillActivityDAO;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.util.StringUtils;

/**
 * Holds Data about a teleplan submission 
 * @author jay
 */
public class TeleplanSubmission {
	
	private BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
    
    private String mspFileStr = null;
    private String mspHtmlStr = null;
    private int sequenceNum = 0;
    private ArrayList<String> billingToBeMarkedAsBilled = null;
    private ArrayList billingmasterToBeMarkedAsBilled = null;
    private BigDecimal bigTotal = null;
    private ArrayList logList = null;
    private int totalClaims = 0;
    
    
    /** Creates a new instance of TeleplanSubmission */
    public TeleplanSubmission() {
    }
    
    public TeleplanSubmission( String mspFileStr,String mspHtmlStr,int sequenceNum,
            ArrayList<String> billingToBeMarkedAsBilled,ArrayList billingmasterToBeMarkedAsBilled,BigDecimal bigTotal,ArrayList logList,int totalClaims){
         this.mspFileStr = mspFileStr;
         this.mspHtmlStr = mspHtmlStr;
         this.sequenceNum = sequenceNum;
         this.billingToBeMarkedAsBilled = billingToBeMarkedAsBilled;
         this.billingmasterToBeMarkedAsBilled = billingmasterToBeMarkedAsBilled;
         this.bigTotal = bigTotal ;
         this.logList = logList;
         this.totalClaims = totalClaims;   
    }

    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("msp file content\n");
        sb.append(this.getMspFile());
        sb.append("\n");
        sb.append("html file content\n");
        sb.append(this.getHtmlFile());
        sb.append("\n");
        sb.append("billing to be marked\n");
        sb.append(StringUtils.getCSV(this.billingToBeMarkedAsBilled));
        sb.append("\n");
        sb.append("billingmaster");
        sb.append(StringUtils.getCSV(this.billingmasterToBeMarkedAsBilled));
        sb.append("\n");
        sb.append("Ending Seq: "+sequenceNum+" total Claims: "+this.getNumClaims()+ " BigTotal: "+this.getBigTotal()+" # in Log: "+this.logList.size());
        return sb.toString();
    }
    
    public String getHtmlFile(){
        return mspHtmlStr;
    }
    
    public String getMspFile(){
        return mspFileStr;
    }
    
    public int getNumClaims(){
        return totalClaims;
    }
                           
    public BigDecimal getBigTotal(){
        return bigTotal;
    }
                        
        
    // -commit log
    // +commit billing
    // +commit billingmaster
    // +Save the File
    // +Save the HTML
    // -It would be nice to know what claims went on what submission!
    //
    //Create the file objects, two for the file and the html file
    //If would be nice 
    public void commit(String fileName,String dirToSaveFiles,String monthCode,String batchCount,String providerNo)  throws Exception{
        File htmlFile = null;
        File mspFile  = null;
        try{
           File directory = new File(dirToSaveFiles);
           if(!directory.exists()){
              throw new Exception("Directory:  "+dirToSaveFiles+ " does not exist");
           }
           
           htmlFile = new File(directory,fileName+".html");
           mspFile  = new File(directory,fileName);
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
            throw new Exception("Could not open file "+dirToSaveFiles+fileName +" does "+dirToSaveFiles+ " exist ?",e);
        }
        
        try{
           writeFile(mspFile);
        }catch(Exception e){
           throw new Exception("Could not write to file "+mspFile.getAbsolutePath()+" are permissions set correctly to "+dirToSaveFiles +"  ?",e);
        }
        
        try{
           writeHtml(htmlFile);
        }catch(Exception e){
           throw new Exception("Could not write to file "+htmlFile.getAbsolutePath() +" are permissions set correctly to "+dirToSaveFiles +"  ?",e);
        } 
        
          BillActivityDAO bActDao  = new BillActivityDAO();
            
          int bActId = bActDao.saveBillactivity(monthCode,batchCount,
                  fileName+".html", 
                  fileName, 
                  providerNo, 
                  this.getHtmlFile(),
                  this.getMspFile(), 
                  new Date(),
                  this.getNumClaims(), 
                  this.getBigTotal().toString());
    
        
        
        BillingmasterDAO billingmaster = SpringUtils.getBean(BillingmasterDAO.class);
        billingmaster.markListAsBilled(billingmasterToBeMarkedAsBilled);
        
        markListAsBilled(billingToBeMarkedAsBilled);
        
        TeleplanLogDAO logDAO = new TeleplanLogDAO();
        logDAO.save(logList);
        
        TeleplanSubmissionLinkDAO  subLinkDAO = new TeleplanSubmissionLinkDAO(); 
        subLinkDAO.save(bActId,billingmasterToBeMarkedAsBilled);
        
        TeleplanSequenceDAO sequenceDAO = new TeleplanSequenceDAO();
        sequenceDAO.saveUpdateSequence(sequenceNum);
    }
    
    public void commitLog(){
        TeleplanLogDAO logDAO = new TeleplanLogDAO();
        logDAO.save(logList);
        
    }

   
        
    //NEEDS TO BE MOVED OUT OF HERE    
    private void markListAsBilled(List<String> list){
    	for(Billing b:billingDao.findSet(list)) {
    		b.setStatus("B");
    		billingDao.merge(b);
    	}
    }
    
  
    public void writeFile(String fileName,File home_dir)  throws Exception{
        File file = new File(home_dir,fileName);
        write(file,mspFileStr);
    }
 
    public void writeHtml(String fileName,File home_dir)  throws Exception{
        File file = new File(home_dir,fileName);
        write(file,mspHtmlStr);
    }
    
    public void writeFile(File file) throws Exception{
        write(file,mspFileStr);
    }

    public void writeHtml(File file) throws Exception{
        write(file,mspHtmlStr);
    }
    
    private void write(File file,String fileValue) throws Exception{
          FileOutputStream out = new FileOutputStream(file);
          BufferedOutputStream bufout = new BufferedOutputStream(out);
          PrintStream p = new PrintStream(bufout);
          p.println(fileValue);
          p.close();
    }
    
    
  
    
}
