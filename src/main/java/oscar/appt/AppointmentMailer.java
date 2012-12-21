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


package oscar.appt;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.utility.DateUtils;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import oscar.service.MessageMailer;
/**
 *
 * @author mweston4
 */

public class AppointmentMailer implements MessageMailer{
    
    private static final Logger logger=MiscUtils.getLogger();
    
    private MailSender mailSender;
    private SimpleMailMessage message;
    private StringBuilder msgTextTemplate;
    private Integer apptNo;
    private Demographic demographic;
    
    OscarAppointmentDao dao=(OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");

    
    public AppointmentMailer(Integer apptNo, Demographic demographic) {
        this.mailSender = (MailSender) SpringUtils.getBean("asyncMailSender");
        this.message = null;
        this.msgTextTemplate = new StringBuilder();
        this.apptNo = apptNo;
        this.demographic = demographic;
    }
    
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void setApptNo(Integer apptNo) {
        this.apptNo = apptNo;
    }
    
    public void setDemographic(Demographic demographic) {
        this.demographic = demographic;
    }
    
    private void setMessageHeader() {
        
        if (this.message == null) {
            Properties op = oscar.OscarProperties.getInstance();
            String msgTemplatePath = op.getProperty("appt_reminder_template");
            String msgMime = op.getProperty("appt_reminder_mime");  
            if (msgTemplatePath != null) { 
                if ((msgMime == null) || msgMime.equalsIgnoreCase("no")){
                    this.message = new SimpleMailMessage();
                }
                else {
                    //TODO
                }


                InputStream fstream = null;
                DataInputStream instream = null;

                try {   
                    InternetAddress emailAddress = new InternetAddress(demographic.getEmail(), true);
                    this.message.setTo(emailAddress.toString());


                    fstream = new FileInputStream(msgTemplatePath);
                    instream = new DataInputStream(fstream);

                    BufferedReader bufreader = new BufferedReader(new InputStreamReader(instream));
                    String strLine;
                
                    //read in message template and header information
                    int lineIndex = 0;                 
                    while ((strLine = bufreader.readLine()) != null)  {
                        if (lineIndex < 2) {
                            String[] msgConfig = strLine.split(":");
                            if (msgConfig[0].equalsIgnoreCase("From")){
                                this.message.setFrom(msgConfig[1]);
                            }
                            else if (msgConfig[0].equalsIgnoreCase("Subject")) {
                                this.message.setSubject(msgConfig[1]);
                            }
                        }
                        else {
                            this.msgTextTemplate.append(strLine).append("\n");
                        }
                        lineIndex++;
                    }
                }
                catch(FileNotFoundException fnf) {
                  logger.error("No Appointment Reminder Template found", fnf);
                }
                catch(IOException io) {
                  logger.error("IOException occurred", io);
                }
                catch(AddressException addr) {
                    logger.error("To Address not valid:" + demographic.getEmail());
                }

                finally {
                    try {
                        if (instream != null) {
                            instream.close();
                        }
                        
                        if ( fstream != null) {
                            fstream.close();
                        }
                    }
                    catch(IOException io) {
                        logger.error("IOException occurred", io);
                    }
                }
            }  
        }
    }
    
    private void fillMessageText() {
        
        if ((this.message != null) && (this.msgTextTemplate.length() > 0)) {
                 
            Date today = new Date();

            Appointment a = dao.find(this.apptNo);
           
            ClinicDAO clinicDao = (ClinicDAO)SpringUtils.getBean("clinicDAO");
            Clinic clinic = clinicDao.getClinic();
            
            if (a == null) {
              logger.error("Appointment ("+this.apptNo+") not found for demographic no (" + this.demographic.getDemographicNo() +") on Date: " + today);
            } else {
               
                String msgText = msgTextTemplate.toString();
                msgText = msgText.replaceAll("<today>", DateUtils.getDate());
                msgText = msgText.replaceAll("<appointment_date>", a.getAppointmentDate().toString());
                msgText = msgText.replaceAll("<appointment_time>", a.getStartTime().toString());
                msgText = msgText.replaceAll("<first_name>", this.demographic.getFirstName());
                msgText = msgText.replaceAll("<last_name>", this.demographic.getLastName());
                
                msgText = msgText.replaceAll("<clinic_name>", clinic.getClinicName());                
                msgText = msgText.replaceAll("<clinic_addressLine>", clinic.getClinicAddress());
                msgText = msgText.replaceAll("<clinic_phone>", clinic.getClinicPhone());
                 
                msgText = msgText.replaceAll("<appt_reason>", a.getReason());
                
                this.message.setText(msgText);
            }
        }
        else {
            logger.error("Cannot populate message text - no message or message template available");
        }
    }
    
    public void prepareMessage() {
        setMessageHeader();
        fillMessageText();
    }
    
    @Override
    public void send() throws Exception {
        try {
            boolean doSend = false;
            
            if ((mailSender != null) && (this.message != null)) {
                   
                if ((this.message.getText() != null) && (this.message.getFrom() != null) && (this.message.getSubject() != null)) {
                    String[] toAddrs = this.message.getTo();
                    if (toAddrs.length > 0) {
                        boolean toValid = true;
                        for (String addr : toAddrs) {
                            if (addr.isEmpty()) {
                                toValid = false;
                            }
                        }
                        if (toValid) {
                            doSend=true;
                        }
                    }        
                }
            }
            
            if (doSend) {
                mailSender.send(this.message);
                
                //Update appt history accordingly      
                Appointment appt = dao.find(this.apptNo);
                if(appt != null) {
                	appt.setRemarks(appt.getRemarks() + "Emailed:" + DateUtils.getCurrentDateOnlyStr("-") +"\n");
                	dao.merge(appt);
                }
            }
            else {
                logger.error("MailSender is not instantiated or MailMessage is not prepared");
            }
        }
        catch(Exception e) {
            logger.error("An error occurred", e);
        }
    }
}
