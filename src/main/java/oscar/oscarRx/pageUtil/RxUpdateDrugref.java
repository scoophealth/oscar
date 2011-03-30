/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.oscarRx.pageUtil;

import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import oscar.oscarRx.util.RxDrugRef;

/**
 *
 * @author jackson
 */
public class RxUpdateDrugref  extends DispatchAction{
  public ActionForward updateDB(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception, ServletException {
        RxDrugRef drugref = new RxDrugRef();
        String s= drugref.updateDB();

        HashMap<String,Object> d = new HashMap<String,Object>();
        d.put("result",s);
        response.setContentType("text/x-json;charset=UTF-8");

        JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON( d );
        jsonArray.write(response.getWriter());
        return null;
    }
    public ActionForward getLastUpdate(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception, ServletException {
        RxDrugRef drugref = new RxDrugRef();
        String s= drugref.getLastUpdateTime();
        HashMap<String,String> d = new HashMap<String,String>();
        d.put("lastUpdate",s);
        //response.setContentType("text/x-json;charset=UTF-8");
        JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON( d );
        jsonArray.write(response.getWriter());
        return null;
    }
}
