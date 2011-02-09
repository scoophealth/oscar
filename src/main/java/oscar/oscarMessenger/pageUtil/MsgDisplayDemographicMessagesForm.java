/*
 * MsgDisplayDemographicMessagesForm.java
 *
 * Created on May 8, 2005, 12:22 AM
 */

package oscar.oscarMessenger.pageUtil;

import org.apache.struts.action.ActionForm;
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
