package oscar.oscarReport.pageUtil;

import java.io.PrintStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class RptByExamplesFavoriteForm extends ActionForm {

    String favoriteName="";
    String query;
    String newQuery;
    String newName;
    String toDelete;
    String id;

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
    
    public String getNewQuery(){
       return newQuery;
    }

    public void setNewQuery(String newQuery){
       this.newQuery = newQuery;
    }
    
    public String getNewName(){
       return newName;
    }

    public void setNewName(String newName){
       this.newName = newName;
    }
    
    public String getToDelete(){
       return toDelete;
    }

    public void setToDelete(String toDelete){
       this.toDelete = toDelete;
    }
    
    public String getId(){
       return id;
    }

    public void setId(String id){
       this.id = id;
    }
}
