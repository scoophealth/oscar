package oscar.oscarReport.pageUtil;

import java.io.PrintStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class RptByExampleForm extends ActionForm {

    String sql;
    String selectedRecentSearch;

    public String getSql(){
       return sql;
    }

    public void setSql(String sql){
       this.sql = sql;
    }
    
    public String getSelectedRecentSearch(){
       return selectedRecentSearch;
    }

    public void setSelectedRecentSearch(String selectedRecentSearch){
       this.selectedRecentSearch = selectedRecentSearch;
    }
}
