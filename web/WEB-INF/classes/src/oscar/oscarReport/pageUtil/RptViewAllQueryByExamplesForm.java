package oscar.oscarReport.pageUtil;

import java.io.PrintStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import org.apache.struts.action.*;
import oscar.util.*;

public final class RptViewAllQueryByExamplesForm extends ActionForm {

    GregorianCalendar now=new GregorianCalendar(); 
    int curYear = now.get(Calendar.YEAR);
    int curMonth = (now.get(Calendar.MONTH)+1);
    int curDay = now.get(Calendar.DAY_OF_MONTH);
    
    String sql;
    String selectedRecentSearch;
    DateUtils dateUtils = new DateUtils();
    String startDate = dateUtils.NextDay(curDay, curMonth, curYear-1);
    String endDate = dateUtils.NextDay(curDay, curMonth, curYear);

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
    
    public String getStartDate(){
       return startDate;
    }

    public void setStartDate(String startDate){
       this.startDate = startDate;
    }
    
    public String getEndDate(){
       return endDate;
    }

    public void setEndDate(String endDate){
       this.endDate = endDate;
    }
}
