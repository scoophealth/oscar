package oscar.oscarReport.pageUtil;

import java.io.PrintStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class RptByExamplesAllFavoritesForm extends ActionForm {

    String favoriteName="";
    String query;
    String forward;

    public String getFavoriteName(){
       return favoriteName;
    }

    public void setFavoriteName(String favoriteName){
       this.favoriteName = favoriteName;
    }
    
    public String getQuery(){
       return query;
    }

    public void setQuery(String query){
       this.query = query;
    }
    
    public String getForward(){
       return forward;
    }

    public void setForward(String forward){
       this.forward = forward;
    }
}
