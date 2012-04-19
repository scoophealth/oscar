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


package oscar.service;

/**
 * See: 
 * http://www.i-develop.be/blog/2010/10/01/execute-tasks-asynchronously-with-spring-3-0/
 * For reference of code implementation
 * 
 * @author mweston4
 */

import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailException;
import org.springframework.beans.factory.annotation.Required;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


@Service(value = "asyncMailSender")
public class AsyncMailSender implements MailSender{
    
    @Resource(name = "mailSender")
    private MailSender mailSender;
    
    private TaskExecutor taskExecutor;
    
 
    @Required
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
    
    public void send(SimpleMailMessage[] mailMessages) throws MailException {
        for (SimpleMailMessage message : mailMessages) {
            send(message);
        }
    }
    
    public void send(SimpleMailMessage mailMessage) throws MailException {
           taskExecutor.execute(new AsyncMailTask(mailMessage));
    }
    
    private class AsyncMailTask implements Runnable {
 
        private SimpleMailMessage message;
 
        private AsyncMailTask(SimpleMailMessage message) {
            this.message = message;
        }
 
        public void run() {       
                mailSender.send(message);
            
        }
    }
}
