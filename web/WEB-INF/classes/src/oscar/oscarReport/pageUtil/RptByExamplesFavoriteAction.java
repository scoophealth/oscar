package oscar.oscarReport.pageUtil;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import java.util.Properties;
import java.util.Collection;
import oscar.oscarReport.data.*;
import oscar.oscarReport.bean.*;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.pageUtil.EctSessionBean;

public class RptByExamplesFavoriteAction extends Action {

    Properties oscarVariables = null;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {   
        RptByExamplesFavoriteForm frm = (RptByExamplesFavoriteForm) form;     
        String providerNo = (String) request.getSession().getAttribute("user");
        if(frm.getNewQuery()!=null){
            if(frm.getNewQuery().compareTo("")!=0){                
                frm.setQuery(frm.getNewQuery());
                if(frm.getNewName()!=null)
                    frm.setFavoriteName(frm.getNewName());
                else{
                    try {
                        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                            

                        String sql = "SELECT * from reportByExamplesFavorite WHERE query LIKE '" + frm.getNewQuery() + "'";
                        ResultSet rs = db.GetSQL(sql);
                        if(rs.next())
                            frm.setFavoriteName(rs.getString("name"));                                       
                        db.CloseConn();
                    }
                    catch(SQLException e) {
                        System.out.println(e.getMessage());            
                    }
                }
                return mapping.findForward("edit");    
            }
            else if(frm.getToDelete()!=null){
                if(frm.getToDelete().compareTo("true")==0){
                    deleteQuery(frm.getId());                    
                }
            }
        }
        else{
            String favoriteName = frm.getFavoriteName();
            String query = frm.getQuery();   
            oscar.oscarReport.data.RptByExampleData exampleData  = new oscar.oscarReport.data.RptByExampleData();       
            String queryWithEscapeChar = exampleData.replaceSQLString ("'","\\'",query);
            write2Database(providerNo, favoriteName, queryWithEscapeChar);            
        }
        RptByExampleQueryBeanHandler hd = new RptByExampleQueryBeanHandler(providerNo);  
        request.setAttribute("allFavorites", hd);  
        return mapping.findForward("success");        
    }        
    
    public void write2Database(String providerNo, String favoriteName, String query){
        if (query!=null && query.compareTo("")!=0){
            try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                oscar.oscarReport.data.RptByExampleData exampleData  = new oscar.oscarReport.data.RptByExampleData();

                query = exampleData.replaceSQLString (";","",query);
                query = exampleData.replaceSQLString("\"", "\'", query);            

                String sql = "SELECT * from reportByExamplesFavorite WHERE name LIKE '" + favoriteName + "' OR query LIKE '" + query + "'";
                ResultSet rs = db.GetSQL(sql);
                if(!rs.next()){
                    sql = "INSERT INTO reportByExamplesFavorite(providerNo, name, query) VALUES('" + providerNo + "','" 
                          + favoriteName + "','" + query + "')";
                    db.RunSQL(sql);
                }
                else{
                    sql = "UPDATE reportByExamplesFavorite SET name='" + favoriteName + "', query='" + query + 
                          "' WHERE id ='" + rs.getString("id") + "'";
                    db.RunSQL(sql);
                }
                db.CloseConn();
            }
            catch(SQLException e) {
                System.out.println(e.getMessage());            
            }
        }
    }
    
    public void deleteQuery(String id){
        
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                   

            String sql = "DELETE FROM reportByExamplesFavorite WHERE id = '" + id + "'";                
            db.RunSQL(sql);

            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());            
        }

    }
}

