package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.PrintStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class EctConsultationFaxForm extends ActionForm {

    public String getRecipient()
    {
        if(recipient == null)
            recipient = new String();
        return recipient;
    }

    public void setRecipient(String str)
    {
        System.out.println("recipient has been set");
        recipient = str;
    }

    public String getFrom()
    {
        if(from == null)
            from = new String();
        return from;
    }

    public void setFrom(String str)
    {
        System.out.println("from has been set");
        from = str;
    }

    public String getRecipientsFaxNumber()
    {
        if(recipientsFaxNumber == null)
            recipientsFaxNumber = new String();
        return recipientsFaxNumber;
    }

    public void setRecipientsFaxNumber(String str)
    {
        System.out.println("recipientsFaxNumber setter");
        recipientsFaxNumber = str;
    }

    public String getSendersPhone()
    {
        if(sendersPhone == null)
            sendersPhone = new String();
        return sendersPhone;
    }

    public void setSendersPhone(String str)
    {
        System.out.println(" setter");
        sendersPhone = str;
    }

    public String getSendersFax()
    {
        if(sendersFax == null)
            sendersFax = new String();
        return sendersFax;
    }

    public void setSendersFax(String str)
    {
        System.out.println("sendersFax setter");
        sendersFax = str;
    }

    public String getComments()
    {
        if(comments == null)
            comments = new String();
        return comments;
    }

    public void setComments(String str)
    {
        System.out.println("appointmentDay setter");
        comments = str;
    }

    public String getRequestId()
    {
        if(requestId == null)
            requestId = new String();
        return requestId;
    }

    public void setRequestId(String str)
    {
        System.out.println("requestId setter");
        requestId = str;
    }
    String recipient;
    String from;
    String recipientsFaxNumber;
    String sendersPhone;
    String sendersFax;
    String comments;
    String requestId;
}
