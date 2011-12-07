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
