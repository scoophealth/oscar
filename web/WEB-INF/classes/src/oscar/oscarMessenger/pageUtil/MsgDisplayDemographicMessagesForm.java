/*
 * MsgDisplayDemographicMessagesForm.java
 *
 * Created on May 8, 2005, 12:22 AM
 */

package oscar.oscarMessenger.pageUtil;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
/**
 *
 * @author root
 */
public class MsgDisplayDemographicMessagesForm extends ActionForm {

      String[] messageNo;
      String unlinkMsg;

      /**
       * Used to get the MessageNo in the DisplayMessagesAction class
       * @return String[], these are the messages the will be set to del
       */
      public String[] getMessageNo(){
         if (messageNo == null){
            messageNo = new String[]{};
         }
      return messageNo;
      }

       /**
       * Used to set the MessageNo, these are the messageNo that will be set to be deleted
       * @param mess String[], these are the message No to be deleted
       */
      public void setMessageNo(String[] mess){
         this.messageNo = mess;
      }
      
      
}