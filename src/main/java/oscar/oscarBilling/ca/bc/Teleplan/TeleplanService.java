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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

/**
 *
 * @author jay
 */
public class TeleplanService {
    static Logger log=MiscUtils.getLogger();
    
    /** Creates a new instance of TeleplanService */
    public TeleplanService() {
    }
    
    
    
    ////////
//     public int findExpectedSequenceNumber(){
//        //TETA-022 SEQ NUMBER ERROR. EXPECTED: 0262113 LAST COMMITTED:0262112
//        String errormsg = "TETA-022 SEQ NUMBER ERROR. EXPECTED: 0262113 LAST COMMITTED:0262112";
//        int i = errormsg.lastIndexOf("COMMITTED:");
//        log.debug(i);
//        log.debug(errormsg.substring(i+10));
//        return i;
//    }
    
     
    public TeleplanAPI getTeleplanAPI(String username,String password) throws Exception{
        TeleplanAPI tAPI = new TeleplanAPI();//
        
        TeleplanResponse tr = tAPI.login(username,password);
        
               
        if (tr != null && tr.getResult().equals("SUCCESS")){
           return tAPI;
        }
        //TODO: ALSO RESULT COULD BE   EXPIRED.PASSWORD   need some kind of trigger that will propmt user to change password
        
        throw new Exception(tr.getMsgs());
    } 
    
    
    
    
    //////
     public static int findExpectedSequenceNumber(String errormsg) throws Exception{
        //TETA-022 SEQ NUMBER ERROR. EXPECTED: 0262113 LAST COMMITTED:0262112
        log.debug("WORKING FROM ERROR MSG "+errormsg);
        int i = errormsg.lastIndexOf("COMMITTED:");
        if (i == -1){
            throw new Exception("Unexpected message "+errormsg);
        }
        String numStr = errormsg.substring(i+10);
        return Integer.parseInt(numStr);
    }
    
    
    
    //ATTEMPT to get the latest sequence number from teleplan.	
    //Creates a one-line (just the header )submission file with the last possible sequence number in it. 
    //If it submits successfully the next sequence # is 1 (return 0 so that the incrementing program will roll to one)
    //More than likely though this will be a failure and it will parse out the last committed number 
    public int getSequenceNumber(TeleplanAPI tAPI,String datacenter) throws Exception {		
        
        String e = "VS1"+datacenter+"9999999V6242OSCAR_MCMASTER           V1.1      20030930OSCAR MCMASTER                          (905) 575-1300                                                                                   ";

        File getSequenceFile =  File.createTempFile("oscarseq","fil");
        BufferedWriter out = new BufferedWriter(new FileWriter(getSequenceFile));
        out.write(e);
        out.close();
       
        TeleplanResponse tr = tAPI.putMSPFile(getSequenceFile);
        getSequenceFile.delete();
        log.debug(tr.toString());
        
        if(tr.isSuccess()){
            return 0;  // what are the chances!
        }   
        return findExpectedSequenceNumber(tr.getMsgs());
     }
    
    //////
//    public String getSequenceNumber(){
//    
//        TeleplanAPI tAPI = new TeleplanAPI();//
//        TeleplanResponse tr = tAPI.login("ttuv6242","jgprk07");
//        
//        String e = "VS1V62429999999V6242OSCAR_MCMASTER           V1.1      20030930OSCAR MCMASTER                          (905) 575-1300                                                                                   ";
//
//        
//        File getSequenceFile =  File.createTempFile("ddd","eee");
//        
//        BufferedWriter out = new BufferedWriter(new FileWriter(getSequenceFile));
//        out.write(e);
//        out.close();
//        
//        tr = tAPI.putAsciiFile(getSequenceFile);
//        getSequenceFile.delete();
//        log.debug(tr.toString());
//        tr = tAPI.logoff();
//        return findExpectedSequenceNumber(tr.toString());
//    }
    
    public void changePassword(TeleplanAPI tAPI,String oldpassword,String password){
          //TeleplanAPI tAPI = getTeleplanAPI("ttuv6242","jgprk07");
          TeleplanResponse chgPasswordResp = tAPI.changePassword("ttuv6242","prkjg07","jgprk07","jgprk07");

          log.debug("RESULT "+chgPasswordResp.getResult());
          log.debug(chgPasswordResp.toString());
          tAPI.logoff();
    }
        /*
        tr = tAPI.getLogList();
        log.debug(tr.toString()); 
        
        tr = tAPI.getRemittance(true);
        log.debug(tr.toString());
        
        tr = tAPI.getAsciiFile("9");
        log.debug(tr.toString());
        
        tr = tAPI.checkElig("9151210456","1959","09","17","","","",true,true,true);
        log.debug("CHECKELIG"+tr.toString());
        
        File f = new File("/Users/jay/sandbox/teleplanCommunication/sendMe.txt");
        tr = tAPI.putAsciiFile(f);
        log.debug(tr.toString());
        
         f = new File("/Users/jay/sandbox/teleplanCommunication/sendMe.txt");
        tr = tAPI.putMSPFile(f);
        log.debug(tr.toString());
        */
        
        
      //  tr = tAPI.logoff();
      //  log.debug(tr.toString());
    
    ////////
    
    
}
