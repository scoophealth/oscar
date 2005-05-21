/*
 * MsgViewMessageByPositionAction.java
 *
 * Created on May 14, 2005, 4:38 PM
 */

package oscar.oscarMessenger.pageUtil;

/**
 *
 * @author ichan
 */
import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.*;
import oscar.util.*;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

public class MsgViewMessageByPositionAction extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {
        // System.out.println("in view message action jackson");
        // Extract attributes we will need
        
        String orderBy = request.getParameter("orderBy")==null?"date":request.getParameter("orderBy");        
        String messagePosition = request.getParameter("messagePosition");           
        String demographic_no = request.getParameter("demographic_no");   
        String from = request.getParameter("from")==null?"oscarMessenger":request.getParameter("from");
        MsgDisplayMessagesBean displayMsgBean = new MsgDisplayMessagesBean();
        ParameterActionForward actionforward = new ParameterActionForward(mapping.findForward("success"));
        
        try{
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           java.sql.ResultSet rs;
           
                String sql = new String("select m.messageid from messagelisttbl ml, messagetbl m, msgDemoMap map"
                +" where map.demographic_no = '"+ demographic_no+"' and status not like \'del\' and remoteLocation = '"+displayMsgBean.getCurrentLocationId()+"' "
                +" and m.messageid = map.messageID and ml.message=m.messageid order by "+ displayMsgBean.getOrderBy(orderBy) + " limit " + messagePosition +", 1");
                System.out.println(sql);
                rs = db.GetSQL(sql);
                if (rs.next()) {                                                                   
                    actionforward.addParameter("messageID", rs.getString("messageid"));
                    actionforward.addParameter("from", "encounter");     
                    actionforward.addParameter("demographic_no", demographic_no);
                    actionforward.addParameter("messagePostion", messagePosition);
                }
              
         rs.close();
         db.CloseConn();

        }
        catch (java.sql.SQLException e){ 
            e.printStackTrace(System.out); 
        }
                               
        return actionforward;
    }

}

